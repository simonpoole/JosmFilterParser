package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Preset implements Condition {
    final String presetPath;
    Object       preset = null;

    /**
     * Check if an object matches with a preset or a preset group
     * 
     * @param presetPath the path to the preset or group
     */
    public Preset(@NotNull String presetPath) {
        this.presetPath = presetPath;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        if (preset == null) {
            preset = meta.getPreset(presetPath);
        }
        return meta.matchesPreset(preset);
    }

    @Override
    public String toString() {
        return "preset:\"" + presetPath + "\"";
    }
}
