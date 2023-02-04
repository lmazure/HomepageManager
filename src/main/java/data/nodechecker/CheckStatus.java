package data.nodechecker;

import java.util.Optional;

import data.violationcorrection.ViolationCorrection;

/**
 * @param checkName name of the check
 * @param detail details of a check violation
 * @param correction correction of the violation
 */
public record CheckStatus(String checkName, String detail, Optional<ViolationCorrection> correction) {
    // EMPTY
}