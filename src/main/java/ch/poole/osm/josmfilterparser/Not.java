package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Not implements Condition {
    final Condition c1;
    
    public Not(@NotNull Condition c1) {
        this.c1 = c1;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return !c1.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return "-" + c1.toString();
    }  
}
