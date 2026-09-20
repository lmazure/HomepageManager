package fr.mazure.homepagemanager.utils.xmlparsing;

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
    private final NameOrder _order;

    /**
     * @param namePrefix prefix
     * @param firstName first name
     * @param middleName middle name
     * @param lastName last name
     * @param nameSuffix suffix
     * @param givenName given name
     * @param order name order
     */
    public AuthorData(final Optional<String> namePrefix,
                      final Optional<String> firstName,
                      final Optional<String> middleName,
                      final Optional<String> lastName,
                      final Optional<String> nameSuffix,
                      final Optional<String> givenName,
                      final NameOrder order) {
        _namePrefix = namePrefix;
        _firstName = firstName;
        _middleName = middleName;
        _lastName = lastName;
        _nameSuffix = nameSuffix;
        _givenName = givenName;
        _order = order;
    }

    @Override
    public String toString() {
        return String.join(" ",
                           _namePrefix.isPresent() ? "prefix=" + _namePrefix.get() :  "▭",
                           _firstName.isPresent() ? "first=" + _firstName.get() :  "▭",
                           _middleName.isPresent() ? "middle=" + _middleName.get() :  "▭",
                           _lastName.isPresent() ? "last=" + _lastName.get() :  "▭",
                           _nameSuffix.isPresent() ? "suffix=" + _nameSuffix.get() :  "▭",
                           _givenName.isPresent() ? "given=" + _givenName.get() :  "▭",
                           "order=" + _order.name().toLowerCase());
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
        return prime * result + _order.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final AuthorData other = (AuthorData) obj;
        return _givenName.equals(other._givenName) &&
               _firstName.equals(other._firstName) &&
               _lastName.equals(other._lastName) &&
               _middleName.equals(other._middleName) &&
               _namePrefix.equals(other._namePrefix) &&
               _nameSuffix.equals(other._nameSuffix) &&
               _order == other._order;
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

    /**
     * @return name order
     */
    public NameOrder getOrder() {
        return _order;
    }

    /**
     * Order in which the name parts of a person are displayed.
     */
    public enum NameOrder {
        /**
         * Western name order (prefix, firstName, middleName, lastName, suffix, givenName).
         */
        WESTERN,
        /**
         * Eastern name order (prefix, lastName, middleName, firstName, suffix, givenName).
         */
        EASTERN;

        /**
         * Parse a name order from its string representation.
         *
         * @param str string representation ("western" or "eastern")
         * @return the corresponding NameOrder
         * @throws XmlParsingException if the string is not a valid name order
         */
        public static NameOrder parse(final String str) throws XmlParsingException {
            return switch (str) {
            case "western" -> WESTERN;
            case "eastern" -> EASTERN;
            default -> throw new XmlParsingException("Illegal name order value (\"" + str + "\")");
            };
        }
    }
}
