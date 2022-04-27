package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import proj.concert.service.jaxrs.LocalDateTimeParam;

@Entity
@Table(name = "CONCERTS")
public class Concert{

    // TODO Implement this class.

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(name="IMAGE_NAME")
    private String imageName;
    @Column(name="BLURB",columnDefinition="LONGTEXT")
    private String blurb;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="CONCERT_PERFORMER",
            joinColumns={@JoinColumn(name="CONCERT_ID")},
            inverseJoinColumns={@JoinColumn(name="PERFORMER_ID")})
    private Set<Performer> performers;
    @ElementCollection
    @CollectionTable(name="CONCERT_DATES")
    @Column(name = "DATE")
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Set<LocalDateTime> dates = new HashSet<>();

    public Concert(Long id, String title, String imageName, String blurb, Set<LocalDateTime> dates, Set<Performer> performers) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
        this.dates = dates;
        this.performers = performers;
    }

    public Concert() {
        this(null, null, null, null, null, null);
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
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformer(Set<Performer> performers) {
        this.performers = performers;
    }

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public Set<LocalDateTime> getDates() {
        return dates;
    }

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Concert, id: ");
        buffer.append(id);
        buffer.append(", title: ");
        buffer.append(title);
        buffer.append(", date: ");
        buffer.append(dates.toString());
        buffer.append(", featuring: ");
        buffer.append(performers.toString());

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;
        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(title, rhs.title).
                isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imageName, blurb);
    }

}
