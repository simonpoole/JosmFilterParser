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
}
