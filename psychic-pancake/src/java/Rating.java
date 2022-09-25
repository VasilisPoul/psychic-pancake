package psychic_pancake;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.*;

@Entity
@Table(name = "rating", schema="main")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Serializable {
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingId implements Serializable {
	@ManyToOne
	private User rater;
	@ManyToOne
	private User rated;
    }

    @EmbeddedId
    private RatingId rating_id;
    private Integer points;
}
