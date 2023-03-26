package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class ElementState implements Condition {

    public enum State {
        NEW, MODIFIED, DELETED, UNCHANGED
    }

    final State elementState;

    /**
     * Check an OSM element has a specified state
     * 
     * @param elementState the state
     */
    public ElementState(@NotNull State elementState) {
        this.elementState = elementState;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return elementState.equals(meta.getState());
    }

    @Override
    public String toString() {
        return elementState.toString().toLowerCase();
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
