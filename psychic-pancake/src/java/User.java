package psychic_pancake;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

import lombok.*;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name = "user", schema="main")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    public static enum Role {
	admin, buyer, seller
    }

    @Id
    @NotNull
    private String uid;
    @NotNull
    private String email;
    @NotNull
    private String password_digest;
    @ManyToOne
    @JoinColumn(insertable=true, updatable=true)
    @NotNull
    private Location location;
    @ManyToOne
    @JoinColumn(insertable=true, updatable=true, nullable=false)
    private Country country;
    @NotNull
    private String VAT;
    @NotNull
    private String first_name;
    @NotNull
    private String last_name;
    @NotNull
    private String phone_num;
    @NotNull
    private boolean pending;
    @NotNull
    @Column(columnDefinition="bigint DEFAULT 0", nullable=false)
    private Long rating;

    @OrderBy ("timestamp DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy="to")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Set<Message> inbox;
    @OrderBy ("timestamp DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy="from")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Set<Message> outbox;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    public Message sendMessage(Message msg) {
	if(msg.getFrom() == null)
	    msg.setTo(this);

	if(msg.getFrom().getUid().equals(uid) &&
	   msg.getTo() != null) {
	    outbox.add(msg);
	    msg.getTo().getInbox().add(msg);
	    return msg;
	}
	return null;		// message was not sent
    }
}
