package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Preset implements Condition {
    final String preset;

    /**
     * Check if an object matches with a preset or a preset group
     * 
     * @param preset the path to the preset or group
     */
    public Preset(@NotNull String preset) {
        this.preset = preset;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.matchesPreset(preset);
    }

    @Override
    public String toString() {
        return "preset:\"" + preset + "\"";
    }
}
