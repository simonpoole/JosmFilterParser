package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class Version implements Condition {
    final long version;

    /**
     * Create a Condition that checks if an element has the specified version
     * 
     * @param version the version
     */
    public Version(long version) {
        this.version = version;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return version == meta.getVersion();
    }

    @Override
    public String toString() {
        return "version:" + Long.toString(version);
    }
}
