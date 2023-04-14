package ch.poole.osm.josmfilterparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Overpass {

    static final String SEMICOLON     = ";\n";
    static final String CLOSE_BRACKET = ")\n";
    static final String OPEN_BRACKET  = "(\n";

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
        StringBuilder result = new StringBuilder();
        appendAsOverpass(result, condition.toDNF());
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
}
