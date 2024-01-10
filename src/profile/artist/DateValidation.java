package profile.artist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateValidation {
    private static final int BEGINNING_YEAR = 1899;
    private static final int ENDING_YEAR = 2024;
    private static final int FIRST_MONTH = 1;
    private static final int LAST_MONTH = 12;
    private static final int FIRST_DAY = 1;
    private static final int LAST_DAY = 31;
    private static final LocalDate BEGINNING_DATE =
            LocalDate.of(BEGINNING_YEAR, LAST_MONTH, LAST_DAY);
    private static final LocalDate ENDING_DATE = LocalDate.of(ENDING_YEAR, FIRST_MONTH, FIRST_DAY);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateValidation() {
    }

    /**
     * Validates a date string against a predefined date range.
     *
     * @param dateString The date string to validate.
     * @return {@code true} if the date is within the valid range, {@code false} otherwise.
     */
    public static boolean validateDate(final String dateString) {
        try {
            LocalDate parsedDate = LocalDate.parse(dateString, FORMATTER);
            return parsedDate.isAfter(BEGINNING_DATE) && parsedDate.isBefore(ENDING_DATE);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
