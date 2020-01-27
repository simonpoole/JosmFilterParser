package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class AreaSize extends Range {

    /**
     * Check if the computes area size of a way is in the specified range
     * 
     * @param range the range
     * @throws ParseException if the range can't be parsed
     */
    public AreaSize(@NotNull String range) throws ParseException {
        super(range);
        name = "areasize:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getAreaSize();
    }
}
