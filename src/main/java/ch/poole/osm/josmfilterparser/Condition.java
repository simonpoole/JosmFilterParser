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

    /**
     * Generate the appropriate Overpass QL for the expression, this assumes that the logic is in DNF
     * 
     * @see <a href="https://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL">Overpass QL</a>
     * 
     * @return a String containing the Overpass QL equivalent
     */
    @NotNull
    default String toOverpass() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " is not supported for Overpass QL output");
    }

    /**
     * Recursively convert the logic to Disjunctive Normal Form
     * 
     * @return a Condition
     */
    @NotNull
    default Condition toDNF() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " is not supported for conversion to DNF");
    }
}
