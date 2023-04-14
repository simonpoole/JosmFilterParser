package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Tags extends Range {

    /**
     * Create a Condition that checks if an element has a tag count in the specified range
     * 
     * @param range the range
     * @throws ParseException if parsing the range values fail
     */
    public Tags(@NotNull String range) throws ParseException {
        super(range);
        name = "tags:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return tags != null ? tags.size() : 0;
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        final boolean upperUnset = upper == UNINITALIZED;
        final boolean lowerUnset = lower == UNINITALIZED;
        if (upperUnset && lowerUnset) {
            return "(if:count_tags() == " + Integer.toString(exact) + ")";
        }
        StringBuilder builder = new StringBuilder();
        if (!lowerUnset) {
            builder.append("(if:count_tags() >= " + Integer.toString(lower) + ")");
        }
        if (!upperUnset) {
            builder.append("(if:count_tags() <= " + Integer.toString(upper) + ")");
        }
        return builder.toString();
    }
}
