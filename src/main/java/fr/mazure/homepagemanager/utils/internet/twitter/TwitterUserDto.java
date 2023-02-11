package fr.mazure.homepagemanager.utils.internet.twitter;

/**
 *
 */
public class TwitterUserDto {

    private String _description;

    /**
     * @param description
     */
    public TwitterUserDto(final String description) {
        _description = description;
    }

    /**
     * @return
     */
    public String getDescription() {
        return _description;
    }
}
