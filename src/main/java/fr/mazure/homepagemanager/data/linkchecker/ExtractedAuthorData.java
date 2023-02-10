package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Optional;

/**
 * Author data
 *
 * @param namePrefix prefix
 * @param firstName first name
 * @param middleName middle name
 * @param lastName last name
 * @param nameSuffix suffix
 * @param givenName given name
 *
 */
public record ExtractedAuthorData(Optional<String> namePrefix,
                                  Optional<String> firstName,
                                  Optional<String> middleName,
                                  Optional<String> lastName,
                                  Optional<String> nameSuffix,
                                  Optional<String> givenName) {
    // EMPTY
}
