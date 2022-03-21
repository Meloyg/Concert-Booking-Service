package proj.concert.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO class to represent concerts.
 * <p>
 * A ConcertDTO describes a concert in terms of
 * id           the unique identifier for a concert.
 * title        the concert's title.
 * dates        the concert's scheduled dates and times (represented as a Set of LocalDateTime instances).
 * imageName    an image name for the concert.
 * performers   the performers in the concert
 * blurb        the concert's description
 */
public class ConcertDTO {

    private Long id;
    private String title;
    private String imageName;
    private String blrb;
    private List<LocalDateTime> dates = new ArrayList<>();
    private List<PerformerDTO> performers = new ArrayList<>();

    public ConcertDTO() {
    }

    public ConcertDTO(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blrb = blurb;
    }

    public ConcertDTO(String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
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

    public String getBlurb() {
        return blrb;
    }

    public void setBlurb(String blrb) {
        this.blrb = blrb;
    }

    public List<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(List<LocalDateTime> dates) {
        this.dates = dates;
    }

    public List<PerformerDTO> getPerformers() {
        return performers;
    }

    public void setPerformers(List<PerformerDTO> performers) {
        this.performers = performers;
    }
}
