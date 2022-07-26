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
@Table(name = "message", schema="main")
@Data
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private Long msg_id;

    @NotNull
    @ManyToOne
    @JoinColumn(insertable=false, updatable=false)
    User from;
    @NotNull
    @ManyToOne
    @JoinColumn(insertable=false, updatable=false)
    User to;
    @NotNull
    Date timestamp;
    @NotNull
    private String subject;
    @NotNull
    private String body;
}
