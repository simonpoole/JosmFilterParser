package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class HasRole implements Condition {
    final String role;

    public HasRole(@NotNull String role) {
        this.role = role;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.hasRole(role);

    }

    @Override
    public String toString() {
        return "hasRole:" + role;
    }
}
