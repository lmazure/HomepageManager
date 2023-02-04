package data;

/**
 * Class defining the location of a violation as unknown
 */
public class ViolationLocationUnknown extends ViolationLocation {

    @Override
    final String getDescription() {
        return "unknown";
    }
}
