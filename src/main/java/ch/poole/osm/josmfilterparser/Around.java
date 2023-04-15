package ch.poole.osm.josmfilterparser;

import static ch.poole.osm.josmfilterparser.I18n.tr;

import java.util.Map;

public class Around implements Condition {
    private static final int DEFAULT_RADIUS = 1000;

    private final String region;

    /**
     * Create a Condition that checks the id of an OSM element
     * 
     * @param id the id that should match
     */
    public Around(String region) {
        this.region = region;
    }

    /**
     * Get the region
     * 
     * @return the region
     */
    String getRegion() {
        return region;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        throw new UnsupportedOperationException(tr("only_overpass", "around"));
    }

    @Override
    public String toString() {
        return "in " + Match.quote(region);
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(around:" + DEFAULT_RADIUS + ",{{geocodeCoords:" + region + "}})";
    }
}
