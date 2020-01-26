package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Ways extends Range {
    
    public Ways(@NotNull String range) throws ParseException {
        super(range);
        name = "ways:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getWayCount();
    } 
}
