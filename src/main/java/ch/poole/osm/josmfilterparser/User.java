package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class User implements Condition {
    final String user;

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
}
