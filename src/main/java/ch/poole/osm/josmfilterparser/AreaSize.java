package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class AreaSize extends Range {
    
    public AreaSize(@NotNull String range) throws ParseException {
        super(range);
        name = "areasize:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getAreaSize();
    } 
}
