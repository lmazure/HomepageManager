package data;

/**
 *
 */
public record Violation(
    String file,
    String type,
    String rule,
    ViolationLocation location,
    String description,
    ViolationCorrections[] correction) {}
