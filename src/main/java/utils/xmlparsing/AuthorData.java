package utils.xmlparsing;

import java.util.Optional;

public class AuthorData {


    final private Optional<String> _namePrefix;
    final private Optional<String> _firstName;
    final private Optional<String> _middleName;
    final private Optional<String> _lastName;
    final private Optional<String> _nameSuffix;
    final private Optional<String> _givenName;
    
    /**
     * @param namePrefix
     * @param firstName
     * @param middleName
     * @param lastName
     * @param nameSuffix
     * @param givenName
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

    /**
     * @return the namePrefix
     */
    public Optional<String> getNamePrefix() {
        return _namePrefix;
    }

    /**
     * @return the firstName
     */
    public Optional<String> getFirstName() {
        return _firstName;
    }

    /**
     * @return the middleName
     */
    public Optional<String> getMiddleName() {
        return _middleName;
    }

    /**
     * @return the lastName
     */
    public Optional<String> getLastName() {
        return _lastName;
    }

    /**
     * @return the nameSuffix
     */
    public Optional<String> getNameSuffix() {
        return _nameSuffix;
    }

    /**
     * @return the givenName
     */
    public Optional<String> getGivenName() {
        return _givenName;
    }
}
