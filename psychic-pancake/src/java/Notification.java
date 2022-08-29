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
@Table(name = "notification", schema="main")
@Data
@NoArgsConstructor
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date displayAt;
    @Column(columnDefinition="boolean DEFAULT false", nullable=false)
    private boolean is_seen;

    @ManyToOne
    private Message message_ref;
    @ManyToOne
    private Listing listing_ref;
    @ManyToOne
    private Bid bid_ref;
}
