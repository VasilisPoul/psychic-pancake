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
@Table(name = "message", schema="main")
@Data
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msg_id;

    @NotNull
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
		      property = "uid",
		      scope = User.class)
    @JsonIdentityReference(alwaysAsId=true)
    User from;
    @NotNull
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
		      property = "uid",
		      scope = Message.class)
    @JsonIdentityReference(alwaysAsId=true)
    User to;
    @NotNull
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
	    nullable=false,
	    updatable=false,
	    insertable=false)
    Date timestamp;
    @NotNull
    private String subject;
    @NotNull
    private String body;
}
