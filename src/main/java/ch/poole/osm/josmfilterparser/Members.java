package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Members extends Range {

    /**
     * Create a Condition that checks if a relation has a number of members in the range
     * This is an extension
     * 
     * @param range the range
     * @throws ParseException if parsing the range values fail
     */
    public Members(@NotNull String range) throws ParseException {
        super(range);
        name = "members:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getMemberCount();
    }
}
