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
@Table(name = "bid", schema="main")
@Data
@NoArgsConstructor
public class Bid implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private User bidder;
    @NotNull
    private Date time;
    @NotNull
    private int ammount;

    @ManyToOne
    @NotNull
    @JoinColumn(name="item_id", nullable=false)
    private Listing listing;
}
