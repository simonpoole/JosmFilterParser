package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class WayLength extends Range {

    /**
     * Check if the computes length of a way is in the specified range
     * 
     * @param range the range
     * @throws ParseException if the range can't be parsed
     */
    public WayLength(@NotNull String range) throws ParseException {
        super(range);
        name = "waylength:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getWayLength();
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
