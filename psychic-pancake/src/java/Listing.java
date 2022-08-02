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

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "listing", schema="main")
@Data
@NoArgsConstructor
public class Listing implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long item_id;
    @NotNull
    private String name;

    @NotNull
    @ManyToMany
    @JoinTable(schema="main")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
		      property = "name",
		      scope = Listing.class)
    @JsonIdentityReference(alwaysAsId=true)
    private List<Category> categories;
    
    @NotNull
    private Double first_bid;

    private Double currently;

    @NotNull
    @OneToMany(mappedBy="listing")
    @OrderBy("time DESC")
    private List<Bid> bids;
    
    @NotNull
    @ManyToOne
    private Location location;
    @NotNull
    @ManyToOne
    private Country country;

    @NotNull
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
	    nullable=false,
	    updatable=false,
	    insertable=false)
    private Date started;
    @NotNull
    private Date ends;
    @NotNull
    @ManyToOne
    private User seller;
    @NotNull
    private String description;
}
