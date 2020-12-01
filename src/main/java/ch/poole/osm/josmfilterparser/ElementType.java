package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class ElementType implements Condition {
    final Type elementType;

    /**
     * Create a Condition that checks the type of OSM element
     * 
     * @param elementType the type a OSM element should be
     */
    public ElementType(@NotNull Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return elementType.equals(type);
    }

    @Override
    public String toString() {
        return "type:" + elementType.toString().toLowerCase();
    }
}
