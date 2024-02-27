package ch.poole.osm.josmfilterparser;

import static ch.poole.osm.josmfilterparser.I18n.tr;

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
     * Reset any state created during evaluation
     */
    default void reset() {
        // do nothing
    }

    /**
     * Generate the appropriate Overpass QL for the expression, this assumes that the logic is in DNF
     * 
     * @see <a href="https://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL">Overpass QL</a>
     * 
     * @return a String containing the Overpass QL equivalent
     */
    @NotNull
    default String toOverpass() {
        throw new UnsupportedOperationException(tr("overpass_not_supported", this.getClass().getSimpleName()));
    }

    /**
     * Recursively convert the logic to Disjunctive Normal Form
     * 
     * @return a Condition
     */
    @NotNull
    default Condition toDNF() {
        throw new UnsupportedOperationException(tr("conversion_to_dnf_not_supported", this.getClass().getSimpleName()));
    }

    /**
     * Return a string for debugging
     * 
     * @return a String
     */
    @NotNull
    default String toDebugString() {
        return toString();
    }
}
