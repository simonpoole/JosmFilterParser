package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Container for a OSM condition
 * 
 * @author simon
 *
 */
public interface Condition {
    /**
     * Evaluate the Condition against a concrete OSM element
     * 
     * @param type the Type of the OSM element
     * @param meta meta information for the OSM element or null
     * @param tags tags of the OSM element or null
     * @return whatever boolean value the condition evaluated to
     */
    public boolean eval(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags);
}
