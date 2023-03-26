package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class User implements Condition {
    final String user;

    /**
     * Create a Condition that checks if the last editor of an element is the specified user
     * 
     * @param user the users display name
     */
    public User(@NotNull String user) {
        this.user = user;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return user.equals(meta.getUser());

    }

    @Override
    public String toString() {
        return "user:" + user;
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(user:\"" + user + "\")";
    }
}
