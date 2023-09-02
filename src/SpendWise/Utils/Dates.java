package SpendWise.Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public abstract class Dates {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat MONTH_AND_YEAR_FORMATTER = new SimpleDateFormat("MM/yyyy");
    public static final SimpleDateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");

    public static Date convLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convString(String date) {
        try {
            return DATE_FORMATTER.parse(date);
        } catch (Exception e) {
            return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static LocalDate monthStart(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    public static LocalDate monthEnd(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    public static LocalDate yearStart(LocalDate date) {
        return date.withDayOfYear(1);
    }

    public static LocalDate yearEnd(LocalDate date) {
        return date.withDayOfYear(date.lengthOfYear());
    }

    public static LocalDate prevMonth(LocalDate date) {
        return date.minusMonths(1);
    }

    public static LocalDate nextMonth(LocalDate date) {
        return date.plusMonths(1);
    }
}
