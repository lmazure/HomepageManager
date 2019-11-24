package data.jsongenerator;

public class ParserLinkDto {

    final private String a_title;
    final private String a_subtitle;
    final private String a_url;
    final private String a_languages[];
    final private String a_formats[];
    final private Integer a_durationHour;
    final private Integer a_durationMinute;
    final private Integer a_durationSecond;
    final private String a_status;
    final private String a_protection;
    
    /**
     * @param title
     * @param subtitle
     * @param url
     * @param languages
     * @param formats
     * @param durationHour
     * @param durationMinute
     * @param durationSecond
     * @param status
     * @param protection
     */
    public ParserLinkDto(final String title,
                         final String subtitle,
                         final String url,
                         final String[] languages,
                         final String[] formats,
                         final Integer durationHour,
                         final Integer durationMinute,
                         final Integer durationSecond,
                         final String status,
                         final String protection) {
        a_title = title;
        a_subtitle = subtitle;
        a_url = url;
        a_languages = languages;
        a_formats = formats;
        a_durationHour = durationHour;
        a_durationMinute = durationMinute;
        a_durationSecond = durationSecond;
        a_status = status;
        a_protection = protection;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return a_title;
    }

    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return a_subtitle;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return a_url;
    }

    /**
     * @return the languages
     */
    public String[] getLanguages() {
        return a_languages;
    }

    /**
     * @return the formats
     */
    public String[] getFormats() {
        return a_formats;
    }

    /**
     * @return the durationHour
     */
    public Integer getDurationHour() {
        return a_durationHour;
    }

    /**
     * @return the durationMinute
     */
    public Integer getDurationMinute() {
        return a_durationMinute;
    }

    /**
     * @return the durationSecond
     */
    public Integer getDurationSecond() {
        return a_durationSecond;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return a_status;
    }

    /**
     * @return the protection
     */
    public String getProtection() {
        return a_protection;
    }
}
