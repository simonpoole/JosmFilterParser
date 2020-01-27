package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Nodes extends Range {

    public Nodes(@NotNull String range) throws ParseException {
        super(range);
        name = "nodes:";
    }

    @Override
    int getValue(Meta meta, Map<String, String> tags) {
        return meta.getNodeCount();
    }
}
