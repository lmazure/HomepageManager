package data.nodechecker;

/**
 * @param checkName name of the check
 * @param detail details of a check violation
 */
public record CheckStatus(String checkName, String detail) {
    // EMPTY
}