package ch.poole.osm.josmfilterparser;


import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class ElementState implements Condition {
    
    
    enum State {NEW, MODIFIED, DELETED}
    
    final State elementState;

    public ElementState(@NotNull State elementState) {
        this.elementState = elementState;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return elementState.equals(meta.getState());
    }

    @Override
    public String toString() {
        return "type:" + elementState.toString().toLowerCase();
    }
}
