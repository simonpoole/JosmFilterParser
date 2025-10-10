package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Around implements Condition, Serializable {

    private static final long serialVersionUID = 1L;
    private static final int  DEFAULT_RADIUS   = 1000;

    private final String location;

    /**
     * Create a Condition that returns true if the objects are around a location
     * 
     * @param location the region
     */
    public Around(@NotNull String location) {
        this.location = location;
    }

    /**
     * Get the region
     * 
     * @return the region
     */
    String getRegion() {
        return location;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.around(meta, location);
    }

    @Override
    public String toString() {
        return "around " + Match.quote(location);
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(around:" + DEFAULT_RADIUS + ",{{geocodeCoords:" + location + "}})";
    }
}
