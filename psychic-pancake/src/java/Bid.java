package psychic_pancake;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "bid", schema="main")
@Data
@NoArgsConstructor
public class Bid implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="item_id", nullable=false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
		      property = "item_id",
		      scope = Bid.class)
    @JsonIdentityReference(alwaysAsId=true)
    private Listing listing;



    @NotNull
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
		      property = "uid",
		      scope = Bid.class)
    @JsonIdentityReference(alwaysAsId=true)
    private User bidder;
    @NotNull
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
	    nullable=false,
	    updatable=false,
	    insertable=false)
    private Date time;
    @NotNull
    private Double amount;


}
