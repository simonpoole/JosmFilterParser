package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Range implements Condition {
    static final int UNINITALIZED = -1;

    private int exact = 0;
    private int upper = UNINITALIZED;
    private int lower = UNINITALIZED;

    protected String name;

    /**
     * Check if a value is in the specified range
     * 
     * @param range the range ( "-" is the from - to separator)
     * @throws ParseException if the range can't be parsed
     */
    public Range(@NotNull String range) throws ParseException {
        if (range.indexOf('-') >= 0) {
            String[] lowerUpper = range.split("-", 2);
            if (lowerUpper.length != 2) {
                throw new ParseException("Illegal range " + range + " split resulted in " + lowerUpper.length + " parts");
            }

            try {
                if (!"".equals(lowerUpper[0])) {
                    lower = Integer.parseInt(lowerUpper[0]);
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Illegal integer " + lowerUpper[0]);
            }
            try {
                if (!"".equals(lowerUpper[1])) {
                    upper = Integer.parseInt(lowerUpper[1]);
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Illegal integer " + lowerUpper[1]);
            }

        } else {
            exact = Integer.parseInt(range);
        }
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        int value = getValue(meta, tags);
        return (upper == UNINITALIZED && lower == UNINITALIZED) ? value == exact : (value >= lower) && (upper == UNINITALIZED || value <= upper);
    }

    /**
     * Retrieve the value that we want to compare against
     * 
     * @param meta the object implementing the Meta interface to the OSM element
     * @param tags a Map containing the tags
     * @return the value the value to check against the range
     */
    abstract int getValue(@Nullable Meta meta, @Nullable Map<String, String> tags);

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        if (upper == UNINITALIZED && lower == UNINITALIZED) {
            result.append(Integer.toString(exact));
        } else {
            if (lower != UNINITALIZED) {
                result.append(Integer.toString(lower));
            }
            result.append("-");
            if (upper != UNINITALIZED) {
                result.append(Integer.toString(upper));
            }
        }
        return result.toString();
    }
}
