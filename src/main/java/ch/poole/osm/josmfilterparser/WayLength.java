package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class WayLength extends Range {
    
    public WayLength(@NotNull String range) throws ParseException {
        super(range);
        name = "waylength:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getWayLength();
    } 
}
