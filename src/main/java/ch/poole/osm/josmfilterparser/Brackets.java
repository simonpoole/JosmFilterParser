package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Brackets implements Condition {
    final Condition c;
    
    public Brackets(@NotNull Condition c) {
        this.c = c;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return "(" + c.toString() + ")";
    }  
}
