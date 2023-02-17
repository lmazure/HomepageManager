package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Optional;

import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Violation of a link content check
 */
public class LinkContentCheck {

    private final String _checkName;
    private final String _description;
    private final Optional<ViolationCorrection> _correction;

    /**
     * Constructor
     * @param checkName Name of the check
     * @param description Description of the check violation
     * @param correction Correction of the check violation
     */
    public LinkContentCheck(final String checkName,
                            final String description,
                            Optional<ViolationCorrection> correction) {
        _checkName = checkName;
        _description = description;
        _correction = correction;
    }

    /**
     * @return Name of the check
     */
    public String getCheckName() {
        return _checkName;
    }

    /**
     * @return Description of the check violation
     */
    public String getDescription() {
        return _description;
    }

    /**
     * @return Correction of the check violation
     */
    public Optional<ViolationCorrection> getCorrection() {
        return _correction;
    }}
