package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class In implements Condition {
    private final String region;

    /**
     * Create a Condition that returns true if the objects are in a region
     * 
     * @param region the region
     */
    public In(@NotNull String region) {
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
        return meta.in(meta, region);
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
        return "(area." + Overpass.normalize(region) + ")";
    }
}
