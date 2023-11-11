package fr.mazure.homepagemanager.data.violationcorrection;

/**
 * Base class for the violation corrections
 */
public abstract class ViolationCorrection {

    private final String _description;

    protected ViolationCorrection(final String description) {
        _description = description;
    }

    /**
     * Get the description of the correction
     *
     * @return description of the correction
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Apply the correction
     *
     * @param content content to fix
     * @return content fixed
     */
    public abstract String apply(final String content);
}
