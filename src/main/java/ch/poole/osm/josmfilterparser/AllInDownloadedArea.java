package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class AllInDownloadedArea implements Condition {

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isAllInDownloadedArea();
    }

    @Override
    public String toString() {
        return "allindownloadedarea";
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}