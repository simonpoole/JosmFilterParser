package ch.poole.osm.josmfilterparser;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Overpass {

    static final String SEMICOLON     = ";\n";
    static final String CLOSE_BRACKET = ")\n";
    static final String OPEN_BRACKET  = "(\n";

    private static final Pattern DEACCENT_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    /**
     * Private constructor
     */
    private Overpass() {
        // nothing
    }

    /**
     * Transform to Overpass
     * 
     * This will not contain global setup and output configuration
     * 
     * @param condition the Condition to compile
     * @return an Overpass QL string
     */
    @NotNull
    public static String transform(@NotNull Condition condition) {
        Condition asDNF = condition.toDNF();
        StringBuilder result = new StringBuilder();
        Set<String> regions = new HashSet<>();
        findRegions(asDNF, regions);
        for (String region : regions) {
            result.append("{{geocodeArea:" + region + "}}->." + normalize(region) + ";\n");
        }
        appendAsOverpass(result, asDNF);
        return result.toString();
    }

    /**
     * Append a condition in Overpass form, expanding it to all types if necessary
     * 
     * @param builder the StringBuilder to append to
     * @param c the Condition
     */
    static void appendAsOverpass(@NotNull StringBuilder builder, @NotNull Condition c) {

        ElementType type = findType(c);
        if (!(c instanceof LogicalOperator) || ((c instanceof And) && type == null)) {
            builder.append(OPEN_BRACKET);
            builder.append(Type.NODE.name().toLowerCase());
            builder.append(c.toOverpass());
            builder.append(SEMICOLON);
            builder.append(Type.WAY.name().toLowerCase());
            builder.append(c.toOverpass());
            builder.append(SEMICOLON);
            builder.append(Type.RELATION.name().toLowerCase());
            builder.append(c.toOverpass());
            builder.append(SEMICOLON);
            builder.append(")");
        } else if (c instanceof And) {
            builder.append(type.elementType.name().toLowerCase()); // NOSONAR
            builder.append(c.toOverpass());
        } else {
            builder.append(c.toOverpass());
        }
        builder.append(SEMICOLON);
    }

    /**
     * Recursively try to determine the elements type required
     * 
     * @param c the Condition to check
     * @return an ElementType or null
     */
    @Nullable
    private static ElementType findType(@NotNull Condition c) {
        if (c instanceof And) {
            ElementType e1 = findType(((And) c).c1);
            if (e1 != null) {
                return e1;
            }
            return findType(((And) c).c2);
        } else if (c instanceof ElementType) {
            return (ElementType) c;
        }
        return null;
    }

    /**
     * Recursively build a list of regions used
     * 
     * @param c the Condition to check
     * @param list the List to add to
     */
    @NotNull
    private static void findRegions(@NotNull Condition c, Set<String> list) {
        if (c instanceof And) {
            findRegions(((And) c).c1, list);
            findRegions(((And) c).c2, list);
        } else if (c instanceof Or) {
            findRegions(((Or) c).c1, list);
            findRegions(((Or) c).c2, list);
        } else if (c instanceof Xor) {
            findRegions(((Xor) c).c1, list);
            findRegions(((Xor) c).c2, list);
        } else if (c instanceof Not) {
            findRegions(((Not) c).c1, list);
        } else if (c instanceof Brackets) {
            findRegions(((Brackets) c).c, list);
        } else if (c instanceof Parent) {
            findRegions(((Parent) c).c, list);
        } else if (c instanceof Child) {
            findRegions(((Child) c).c, list);
        } else if (c instanceof In) {
            list.add(((In) c).getRegion());
        }
    }

    /**
     * Normalize a string and replace whitespace with underscores
     * 
     * @param str the input string
     * @return a normalized String
     */
    static String normalize(@NotNull String str) {
        String deaccented = DEACCENT_PATTERN.matcher(Normalizer.normalize(str, Normalizer.Form.NFD)).replaceAll("");
        StringBuilder result = new StringBuilder();
        for (char c : deaccented.toCharArray()) {
            if (Character.isWhitespace(c) || !Character.isAlphabetic(c)) {
                result.append('_');
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }
}
