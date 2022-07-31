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
    @JoinTable(name="listing_categories")
    private Set<Category> Categories;
    
    @NotNull
    private Double first_bid;

    private Double currently;

    @NotNull
    @OneToMany(mappedBy="listing")
    @OrderBy("time DESC")
    private List<Bid> bids;
    
    @NotNull
    private Location location;
    @NotNull
    private Country country;

    @NotNull
    private Date started;
    @NotNull
    private Date ends;
    @NotNull
    private User seller;
    @NotNull
    private String description;
}
