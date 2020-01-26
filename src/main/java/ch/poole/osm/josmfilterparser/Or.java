package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Or implements Condition {
    final Condition c1;
    final Condition c2;
    
    public Or(@NotNull Condition c1, @NotNull Condition c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c1.eval(type, meta, tags) || c2.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return c1.toString() + " OR " + c2.toString();
    }  
}
