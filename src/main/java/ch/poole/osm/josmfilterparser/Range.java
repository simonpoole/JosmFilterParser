package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Range implements Condition {
    private int exact = 0;
    private int upper = -1;
    private int lower = -1;

    protected String name;
    
    public Range(@NotNull String range) throws ParseException {
        if (range.indexOf('-') >= 0) {
            String[] lowerUpper = range.split("-",2);
            if (lowerUpper.length != 2) {
                throw new ParseException("Illegal range " + range + " split resulted in " + lowerUpper.length + " parts");
            }
            if (!"".equals(lowerUpper[0])) {
                lower = Integer.parseInt(lowerUpper[0]);
            }
            if (!"".equals(lowerUpper[1])) {
                upper = Integer.parseInt(lowerUpper[1]);
            }
        } else {
            exact = Integer.parseInt(range);
        }
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        int value = getValue(meta, tags);
        return (upper == -1 && lower == -1) ? value == exact : (value >= lower) && (upper == -1 || value <= upper);
    }
    
    /**
     * Retrieve the value that we want to compare against
     * 
     * @param meta
     * @param tags
     * @return the value
     */
    abstract int getValue(@Nullable Meta meta, @Nullable Map<String, String> tags);

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        if (upper == -1 && lower == -1) {
            result.append(Integer.toString(exact));
        } else {
            if (lower != -1) {
                result.append(Integer.toString(lower));
            }
            result.append("-");
            if (upper != -1) {
                result.append(Integer.toString(upper));
            }
        }
        return result.toString();
    }
}
