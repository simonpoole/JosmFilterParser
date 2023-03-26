package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Ways extends Range {

    /**
     * Create a Condition that checks if a node is member of number of ways in the specified range
     * 
     * @param range the range
     * @throws ParseException if parsing the range values fail
     */
    public Ways(@NotNull String range) throws ParseException {
        super(range);
        name = "ways:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getWayCount();
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
