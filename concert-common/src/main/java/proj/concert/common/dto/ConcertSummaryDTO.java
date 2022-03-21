package proj.concert.common.dto;

/**
 * Represents a small portion of the information about a concert. Intended to be used when not all info is requred.
 *
 * id         the concert's id
 * title      the concert's title
 * imageName  the concert's image name
 */
public class ConcertSummaryDTO {

    private Long id;
    private String title;
    private String imageName;

    public ConcertSummaryDTO() {
    }

    public ConcertSummaryDTO(Long id, String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
