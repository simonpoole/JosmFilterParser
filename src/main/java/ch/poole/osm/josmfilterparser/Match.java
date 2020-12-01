package ch.poole.osm.josmfilterparser;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

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

    interface Eval {
        boolean eval(Type type, Meta meta, Map<String, String> tags);
    }

    Eval evaluator;

    public Match(@NotNull String key, @Nullable String op, @Nullable String value) {
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
            evaluator = this::tagMatch;
            keyAsterix = ASTERIX.equals(key);
            valueAsterix = ASTERIX.equals(value);
        }
    }

    public boolean tagMatch(Type type, Meta meta, Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (op == null) {
                if (tagKey.contains(key) || tagValue.contains(key)) {
                    return true;
                }
            } else {
                boolean keyEquals = key.equals(tagKey);
                if (keyEquals && value == null) {
                    if (tagValue == null || "".equals(tagValue)) {
                        return true;
                    }
                } else {
                    boolean valueEquals = value.equals(tagValue);
                    if (keyAsterix && valueEquals) {
                        return true;
                    } else {
                        if (keyEquals && (valueEquals || valueAsterix)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean valueComparison(Type type, Meta meta, Map<String, String> tags) {
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

    public static String quote(@NotNull String text) {
        if (needsQuotes.matcher(text).matches()) {
            return "\"" + text + "\"";
        }
        return text;
    }

    @Override
    public boolean eval(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        return evaluator.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return quote(key) + (op != null ? " " + op : "") + (value != null ? " " + quote(value) : "");
    }
}
