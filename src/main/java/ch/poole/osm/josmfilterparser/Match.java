package ch.poole.osm.josmfilterparser;

import static ch.poole.osm.josmfilterparser.I18n.tr;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Match implements Condition {

    private static final String   REG_EXP_ANY  = "\".*\"";
    private static final String   LT           = "<";
    private static final String   GT           = ">";
    private static final String   EQUALS       = "=";
    private static final String   DOUBLECOLON  = ":";
    private static final String   QUESTIONMARK = "?";
    private static final String   TILDE        = "~";
    private static final String   ASTERIX      = "*";
    private static final String[] TRUTHY       = { "true", "yes", "1", "on" };

    private final String       key;
    private final String       value;
    private final String       op;
    private Double             numericValue = null;
    private AlphanumComparator comparator   = null;
    private boolean            keyAsterix;
    private boolean            valueAsterix;
    private Pattern            keyPattern;
    private Pattern            valuePattern;
    private boolean            negate;

    interface Eval {
        /**
         * Evaluate against against concrete tags
         * 
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
     * @throws JosmFilterParseException if regexp is true and parsing a regular expression fails
     */
    public Match(@NotNull String key, @Nullable String op, @Nullable String value, boolean regexp) throws JosmFilterParseException {
        this.key = key;
        this.value = value;
        this.op = op;
        try {
            if (GT.equals(op) || LT.equals(op)) {
                try {
                    numericValue = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    // ignore
                }
                comparator = new AlphanumComparator();
                evaluator = this::valueComparison;
            } else if (TILDE.equals(op)) {
                evaluator = this::tagRegexpValueMatch;
                valuePattern = Pattern.compile(value);
            } else if (DOUBLECOLON.equals(op) && value == null) {
                evaluator = this::exactKeyMatch;
            } else if (QUESTIONMARK.equals(op) && value == null) {
                evaluator = this::booleanValueMatch;
            } else {
                if (regexp) {
                    evaluator = this::tagRegexpMatch;
                    keyPattern = Pattern.compile(key);
                    if (value != null) {
                        valuePattern = Pattern.compile(value);
                    }
                } else {
                    evaluator = this::tagMatch;
                    keyAsterix = ASTERIX.equals(key);
                    valueAsterix = ASTERIX.equals(value);
                }
            }
        } catch (PatternSyntaxException psex) {
            throw new JosmFilterParseException(psex.getLocalizedMessage());
        }
    }

    /**
     * Construct a copy of an instance
     * 
     * @param match the Match to copy
     */
    Match(@NotNull Match match) {
        key = match.key;
        value = match.value;
        op = match.op;
        numericValue = match.numericValue;
        comparator = match.comparator;
        keyAsterix = match.keyAsterix;
        valueAsterix = match.valueAsterix;
        keyPattern = match.keyPattern;
        valuePattern = match.valuePattern;
        negate = match.negate;
    }

    /**
     * Set the negate flag, this is only used for overpass output
     */
    void negate() {
        negate = true;
    }

    /**
     * Match tags against key and a value potentially containing a regexp
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean tagRegexpValueMatch(@NotNull Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (tagKey.contains(key) && valuePattern.matcher(tagValue).matches()) {
                return true;
            }
        }
        return false;
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
     * Look for an exact key match
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean exactKeyMatch(@NotNull Map<String, String> tags) {
        for (String k : tags.keySet()) {
            if (k.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check for a "true" boolean value
     * 
     * @param tags a map holding the tags
     * @return true if there is a match
     */
    private boolean booleanValueMatch(@NotNull Map<String, String> tags) {
        for (Entry<String, String> e : tags.entrySet()) {
            if (e.getKey().equals(key)) {
                return isTruthy(e.getValue());
            }
        }
        return false;
    }

    /**
     * Check if value is in a list of "true" equivalent strings
     * 
     * @param value value to check
     * @return true if value is "true"
     */
    private boolean isTruthy(@NotNull String value) {
        for (String t : TRUTHY) {
            if (t.equalsIgnoreCase(value)) {
                return true;
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

    private static final Pattern NEEDS_QUOTES = Pattern.compile(".*[ \t:].*");

    /**
     * Add quotes to a string if necessary
     * 
     * @param text the string
     * @return a potentially quoted string
     */
    public static String quote(@NotNull String text) {
        if (NEEDS_QUOTES.matcher(text).matches()) {
            return "\"" + text + "\"";
        }
        return text;
    }

    private static final Pattern NEEDS_QUOTES_OVERPASS = Pattern.compile(".*[ \t:\\[\\]\\(\\)\\{\\}].*");

    /**
     * Add quotes to a string if necessary
     * 
     * @param text the string
     * @return a potentially quoted string
     */
    public static String quoteOverpass(@NotNull String text) {
        if (NEEDS_QUOTES_OVERPASS.matcher(text).matches()) {
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
    public String toOverpass() {
        StringBuilder builder = new StringBuilder();
        if (op != null) {
            builder.append("[");
            if (value != null && !"".equals(value) && !valueAsterix) {
                builder.append(keyAsterix ? TILDE + REG_EXP_ANY : quoteOverpass(key));
                negate(builder);
                switch (op) {
                case EQUALS:
                    if (keyAsterix) {
                        builder.append(TILDE);
                        builder.append("\"^" + value + "$\"");
                    } else {
                        builder.append(EQUALS);
                        builder.append(quoteOverpass(value));
                    }
                    break;
                case TILDE:
                    builder.append(TILDE);
                    builder.append("\"" + value + "\"");
                    break;
                default:
                    throw new UnsupportedOperationException(tr("op_with_value_not_supported", op));
                }
            } else {
                switch (op) {
                case EQUALS:
                    builder.append(quoteOverpass(key));
                    negate(builder);
                    builder.append(TILDE);
                    builder.append(valueAsterix ? REG_EXP_ANY : "\"^$\"");
                    break;
                case DOUBLECOLON:
                    negate(builder);
                    builder.append(quoteOverpass(key));
                    // exact match already done
                    break;
                case QUESTIONMARK:
                    builder.append(quoteOverpass(key));
                    negate(builder);
                    builder.append(TILDE);
                    builder.append("\"^(true|yes|1|on)$\"");
                    break;
                default:
                    throw new UnsupportedOperationException(tr("op_without_value_not_supported", op));
                }
            }
            builder.append("]");
        } else {
            throw new UnsupportedOperationException(tr("substring_match_not_supported"));
        }
        return builder.toString();
    }

    /**
     * Generate negated output
     * 
     * @param builder the StringBuilder we are appending to
     */
    private void negate(@NotNull StringBuilder builder) {
        if (negate) {
            builder.append("!");
        }
    }

    @Override
    public String toString() {
        boolean hasOp = op != null;
        String space = hasOp && (op.equals(DOUBLECOLON) || op.equals(QUESTIONMARK)) ? "" : " ";
        return quote(key) + (hasOp ? space + op : "") + (value != null ? " " + quote(value) : "");
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
