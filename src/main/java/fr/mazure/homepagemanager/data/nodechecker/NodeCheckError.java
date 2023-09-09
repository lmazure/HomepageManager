package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * @param tag tag of the node
 * @param value text content of the node
 * @param violation description of the check that us violated
 * @param checkName name of the check
 * @param detail details of the violation
 * @param correction correctio of the violation
*
*/
public record NodeCheckError(String tag,
                             String value,
                             String violation,
                             String checkName,
                             String detail,
                             Optional<ViolationCorrection> correction) {
    // EMPTY
}