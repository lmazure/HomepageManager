package utils.xmlparsing;

import java.util.Optional;

/**
 * Author data
 */
public class AuthorData {

    private final Optional<String> _namePrefix;
    private final Optional<String> _firstName;
    private final Optional<String> _middleName;
    private final Optional<String> _lastName;
    private final Optional<String> _nameSuffix;
    private final Optional<String> _givenName;

    /**
     * @param namePrefix prefix
     * @param firstName first name
     * @param middleName middle name
     * @param lastName last name
     * @param nameSuffix suffix
     * @param givenName given name
     */
    public AuthorData(final Optional<String> namePrefix,
                      final Optional<String> firstName,
                      final Optional<String> middleName,
                      final Optional<String> lastName,
                      final Optional<String> nameSuffix,
                      final Optional<String> givenName) {
        _namePrefix = namePrefix;
        _firstName = firstName;
        _middleName = middleName;
        _lastName = lastName;
        _nameSuffix = nameSuffix;
        _givenName = givenName;
    }

    @Override
    public String toString() {
        return String.join(" ",
                           _namePrefix.isPresent() ? "prefix=" + _namePrefix.get() :  "▭",
                           _firstName.isPresent() ? "first=" + _firstName.get() :  "▭",
                           _middleName.isPresent() ? "middle=" + _middleName.get() :  "▭",
                           _lastName.isPresent() ? "last=" + _lastName.get() :  "▭",
                           _nameSuffix.isPresent() ? "suffix=" + _nameSuffix.get() :  "▭",
                           _givenName.isPresent() ? "given=" + _givenName.get() :  "▭");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _firstName.hashCode();
        result = prime * result + _givenName.hashCode();
        result = prime * result + _lastName.hashCode();
        result = prime * result + _middleName.hashCode();
        result = prime * result + _namePrefix.hashCode();
        result = prime * result + _nameSuffix.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if ((this == obj) || (obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final AuthorData other = (AuthorData) obj;
        return _givenName.equals(other._givenName) &&
               _firstName.equals(other._firstName) &&
               _lastName.equals(other._lastName) &&
               _middleName.equals(other._middleName) &&
               _namePrefix.equals(other._namePrefix) &&
               _nameSuffix.equals(other._nameSuffix);
    }

    /**
     * @return prefix
     */
    public Optional<String> getNamePrefix() {
        return _namePrefix;
    }

    /**
     * @return first name
     */
    public Optional<String> getFirstName() {
        return _firstName;
    }

    /**
     * @return middle name
     */
    public Optional<String> getMiddleName() {
        return _middleName;
    }

    /**
     * @return last name
     */
    public Optional<String> getLastName() {
        return _lastName;
    }

    /**
     * @return suffix
     */
    public Optional<String> getNameSuffix() {
        return _nameSuffix;
    }

    /**
     * @return given name
     */
    public Optional<String> getGivenName() {
        return _givenName;
    }
}
