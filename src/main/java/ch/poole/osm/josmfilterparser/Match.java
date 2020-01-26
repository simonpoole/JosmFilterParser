package ch.poole.osm.josmfilterparser;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Match implements Condition {
    final String key;
    final String value;
    final String op;

    public Match(@NotNull String key, @Nullable String op, @Nullable String value) {
        this.key = key;
        this.value = value;
        this.op = op;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (op == null) {
                if (tagKey.contains(key) || tagValue.contains(key)) {
                    return true;
                }
            } else {
                switch (op) {
                case "=":
                    if (value == null) {
                        if (tagValue == null || "".equals(tagValue)) {
                            return true;
                        }
                    } else {
                        boolean valueEquals = value.equals(tagValue);
                        if ("*".equals(key) && valueEquals) {
                            return true;
                        } else {
                            boolean keyEquals = key.equals(tagKey);
                            if (keyEquals && valueEquals) {
                                return true;
                            } else if (keyEquals && "*".equals(value)) {
                                return true;
                            }
                        }
                    }
                    break;
                case ">":
                case "<":
                default:
                    throw new IllegalArgumentException("Unsupported comparision " + op);
                }
            }
        }

        return false;

    }
    
    private static Pattern needsQuotes = Pattern.compile(".*[ \t:].*");
    public static String quote(@NotNull String text) {
        if (needsQuotes.matcher(text).matches()) {
            return "\"" + text + "\"";
        }
        return text;
    }

    @Override
    public String toString() {
        return quote(key) + (op != null ? " " + op : "") + (value != null ? " " + quote(value) : "");
    }
}
