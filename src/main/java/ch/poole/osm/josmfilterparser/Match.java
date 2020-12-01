package ch.poole.osm.josmfilterparser;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Match implements Condition {
    private static final String LT           = "<";
    private static final String GT           = ">";
    private static final String EQUALS       = "=";
    private static final String ASTERIX      = "*";
    private final String        key;
    private final String        value;
    private final String        op;
    private Double              numericValue = null;
    private AlphanumComparator  comparator   = null;
    private boolean             keyAsterix;
    private boolean             valueAsterix;
    private Pattern             keyPattern;
    private Pattern             valuePattern;

    interface Eval {
        /**
         * Evaluate against against concrete tags
         * 
         * @param type type of object
         * @param meta meta information for the object
         * @param tags the objects tags
         * @return true if it matched
         */
        boolean eval(Map<String, String> tags);
    }

    Eval evaluator;

    /**
     * Match tags
     * 
     * @param key key to match
     * @param op kind of match operation
     * @param value value to match or null
     * @param regexp key and value are regular expressions
     * @throws ParseException if regexp is true and parsing a regular expression fails
     */
    public Match(@NotNull String key, @Nullable String op, @Nullable String value, boolean regexp) throws ParseException {
        this.key = key;
        this.value = value;
        this.op = op;
        if (!EQUALS.equals(op) && op != null) {
            try {
                numericValue = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                // ignore
            }
            comparator = new AlphanumComparator();
            evaluator = this::valueComparison;
        } else {
            if (regexp) {
                evaluator = this::tagRegexpMatch;
                try {
                    keyPattern = Pattern.compile(key);
                    if (value != null) {
                        valuePattern = Pattern.compile(value);
                    }
                } catch (PatternSyntaxException psex) {
                    throw new ParseException(psex.getLocalizedMessage());
                }
            } else {
                evaluator = this::tagMatch;
                keyAsterix = ASTERIX.equals(key);
                valueAsterix = ASTERIX.equals(value);
            }
        }
    }

    /**
     * Match tags against key / value potentially containing regexps
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean tagRegexpMatch(@NotNull Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (op == null) {
                if (keyPattern.matcher(tagKey).matches() || (tagValue != null && keyPattern.matcher(tagValue).matches())) {
                    return true;
                }
            } else {
                boolean keyMatches = keyPattern.matcher(tagKey).matches();
                if (keyMatches && value == null) {
                    if (tagValue == null || "".equals(tagValue)) {
                        return true;
                    }
                } else if (value != null && valuePattern.matcher(tagValue).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Match tags against key / value potentially containing a * as a wildcard
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean tagMatch(@NotNull Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (op == null) {
                if (tagKey.contains(key) || (tagValue != null && tagValue.contains(key))) {
                    return true;
                }
            } else {
                boolean keyEquals = key.equals(tagKey);
                if (keyEquals && value == null) {
                    if (tagValue == null || "".equals(tagValue)) {
                        return true;
                    }
                } else if (value != null) {
                    boolean valueEquals = value.equals(tagValue);
                    if ((keyAsterix && valueEquals) || (keyEquals && (valueEquals || valueAsterix))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a value comparison hold trues for the tags
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean valueComparison(@NotNull Map<String, String> tags) {
        String tagValue = tags.get(key);
        if (tagValue == null) {
            return false;
        }
        // try numeric comparision first
        if (numericValue != null) {
            Double numericTagValue = null;
            try {
                numericTagValue = Double.parseDouble(tagValue);
            } catch (NumberFormatException e) {
                // ignore
            }
            if (numericTagValue != null) {
                if (GT.equals(op)) {
                    return numericTagValue > numericValue;
                }
                return numericTagValue < numericValue;
            }
        }
        // ok then try something else
        if (GT.equals(op)) {
            return comparator.compare(tagValue, value) > 0;
        }
        return comparator.compare(tagValue, value) < 0;
    }

    private static Pattern needsQuotes = Pattern.compile(".*[ \t:].*");

    /**
     * Add quotes to a string if necessary
     * 
     * @param text the string
     * @return a potentialyl quoted string
     */
    public static String quote(@NotNull String text) {
        if (needsQuotes.matcher(text).matches()) {
            return "\"" + text + "\"";
        }
        return text;
    }

    @Override
    public boolean eval(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (tags == null) {
            return false;
        }
        return evaluator.eval(tags);
    }

    @Override
    public String toString() {
        return quote(key) + (op != null ? " " + op : "") + (value != null ? " " + quote(value) : "");
    }
}
