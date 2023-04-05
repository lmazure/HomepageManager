package fr.mazure.homepagemanager.data;

import java.util.Optional;

import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Record of a check violation
 */
public class Violation {

    private final String _file;
    private final String _type;
    private final String _rule;
    private final ViolationLocation _location;
    private final String _description;
    private final Optional<ViolationCorrection> _correction;

    /**
     * @param file file violating the check
     * @param type type of check
     * @param rule check rule
     * @param location location of the violation in the file
     * @param description description of the violation
     * @param correction possible automated corrections
     */
    public Violation(final String file,
                     final String type,
                     final String rule,
                     final ViolationLocation location,
                     final String description,
                     final Optional<ViolationCorrection> correction) {
        _file = file;
        _type = type;
        _rule = rule;
        _location = location;
        _description = description;
        _correction = correction;
    }
    /**
     * @return the file
     */
    public String getFile() {
        return _file;
    }

    /**
     * @return the type
     */
    public String getType() {
        return _type;
    }

    /**
     * @return the rule
     */
    public String getRule() {
        return _rule;
    }

    /**
     * @return the description of the location
     */
    public String getLocationDescription() {
        return _location.getDescription();
    }

    /**
     * @return the description
     */
    public String getHtmlDescription() {
        return HtmlHelper.convertStringToHtml(_description);
    }

    /**
     * @return the correction
     */
    public Optional<ViolationCorrection> getCorrection() {
        return _correction;
    }

    /**
     * @return the description of the location
     */
    public String getCorrectionDescription() {
        return _correction.isPresent() ? _correction.get().getDescription()
                                       : "none";
    }
}
