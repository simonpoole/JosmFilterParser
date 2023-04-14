package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Nodes extends Range {

    /**
     * Create a Condition that checks if a way has a number of nodes in the specified range
     * 
     * @param range the range
     * @throws ParseException if parsing the range values fail
     */
    public Nodes(@NotNull String range) throws ParseException {
        super(range);
        name = "nodes:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getNodeCount();
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}