package data.nodechecker.checker.nodechecker;

/**
 * @param tag tag of the node
 * @param value text content of the node
 * @param violation description of the check that us violated
 * @param detail details of the violation
*
*/
public record NodeCheckError(String tag,
                             String value,
                             String violation,
                             String detail) {
    // EMPTY
}