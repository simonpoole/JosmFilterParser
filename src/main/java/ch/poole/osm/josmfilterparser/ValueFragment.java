package ch.poole.osm.josmfilterparser;

import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

public class ValueFragment implements Condition {
    final String key;
    final String value;

    public ValueFragment(@NotNull String key, @NotNull String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        for (Entry<String, String> tag : tags.entrySet()) {
            String tagKey = tag.getKey();
            String tagValue = tag.getValue();
            if (tagKey.equals(key) && tagValue.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Match.quote(key) + ":" + Match.quote(value);
    }
}
