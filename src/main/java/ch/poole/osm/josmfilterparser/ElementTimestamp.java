package ch.poole.osm.josmfilterparser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementTimestamp implements Condition {
    private long exact = 0;
    private long upper = -1;
    private long lower = -1;

    private static DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy[-M[-d['T'H[:m[:s[Z]]]]]]", Locale.US);
    private static DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy[-MM[-sd['T'HH[:mm[:ss[Z]]]]]]", Locale.US);
    private static ZoneId            utc    = ZoneId.of("UTC");

    public ElementTimestamp(@NotNull String range) throws ParseException {

        try {
            if (range.indexOf('/') >= 0) {
                String[] lowerUpper = range.split("/", 2);
                if (lowerUpper.length != 2) {
                    throw new ParseException("Illegal range " + range + " split resulted in " + lowerUpper.length + " parts");
                }
                if (!"".equals(lowerUpper[0])) {
                    lower = parseDateTime(lowerUpper[0]);
                }
                if (!"".equals(lowerUpper[1])) {
                    upper = parseDateTime(lowerUpper[1]);
                }
            } else {
                exact = parseDateTime(range);
            }
        } catch (DateTimeParseException ex) {
            throw new ParseException("Date parse error " + range + " " + ex.getMessage() + " at " + ex.getErrorIndex());
        }
    }

    /**
     * Parse a date using the very 2nd system syndromish
     * 
     * @param datetime the String to parse
     */
    public static long parseDateTime(@NotNull String datetime) throws DateTimeParseException {
        TemporalAccessor ta = inputFormat.parseBest(datetime, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        if (ta instanceof LocalDate) {
            return ((LocalDate) ta).toEpochDay() * 24 * 3600;
        } else if (ta instanceof LocalDateTime ) {
            return ((LocalDateTime) ta).atZone(utc).toEpochSecond();
        } else if (ta instanceof YearMonth ) {
            return ((YearMonth) ta).atDay(1).toEpochDay() * 24 * 3600;
        } else {
            return ((Year) ta).atDay(1).toEpochDay() * 24 * 3600;
        }
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        long value = meta.getTimestamp();
        return (upper == -1 && lower == -1) ? value == exact : (value > lower) && (upper == -1 || value <= upper);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("timestamp:");
        if (upper == -1 && lower == -1) {
            result.append(LocalDateTime.ofEpochSecond(exact, 0, ZoneOffset.UTC).format(outputFormat));
        } else {
            if (lower != -1) {
                result.append(LocalDateTime.ofEpochSecond(lower, 0, ZoneOffset.UTC).format(outputFormat));
            }
            result.append("/");
            if (upper != -1) {
                result.append(LocalDateTime.ofEpochSecond(upper, 0, ZoneOffset.UTC).format(outputFormat));
            }
        }
        return result.toString();
    }
}
