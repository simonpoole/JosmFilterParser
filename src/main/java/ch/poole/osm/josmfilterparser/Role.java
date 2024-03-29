package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Role implements Condition {
    final String role;

    /**
     * Create a Condition that checks if the element has the specified role in a relation
     * 
     * @param role the role
     */
    public Role(@NotNull String role) {
        this.role = role;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.getRoles().contains(role);
    }

    @Override
    public String toString() {
        return "role:" + role;
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
