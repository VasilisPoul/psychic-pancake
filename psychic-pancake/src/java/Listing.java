package psychic_pancake;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "listing", schema="main")
@Data
@NoArgsConstructor
public class Listing extends Resource implements Serializable {
    @Id
    @GeneratedValue
    private int item_id;
    @NotNull
    private String name;

    @NotNull
    @OneToMany
    private List<Category> Categories;
    
    @NotNull
    private int first_bid;

    private int currently;

    @NotNull
    @OneToMany
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
