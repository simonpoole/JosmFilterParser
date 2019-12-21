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
interface Condition {
    public boolean eval(@NotNull Type type, @NotNull Meta meta, @Nullable Map<String, String> tags);
}
