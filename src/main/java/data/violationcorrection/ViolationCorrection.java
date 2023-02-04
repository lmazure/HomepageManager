package data.violationcorrection;

/**
 * Base class for the violation corrections
 */
public abstract class ViolationCorrection {
    private final String _description;

    protected ViolationCorrection(final String description) {
        _description = description;
    }

    /**
     * @return description of the correction
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Apply the correction
     * @param content content to fix
     * @return content fixed
     */
    public abstract String apply(final String content);
}
