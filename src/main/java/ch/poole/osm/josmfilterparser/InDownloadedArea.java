package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class InDownloadedArea implements Condition {

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isInDownloadedArea();
    }

    @Override
    public String toString() {
        return "indownloadedarea";
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}