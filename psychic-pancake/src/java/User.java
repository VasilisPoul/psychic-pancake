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
    @NotNull
    private Location location;
    @NotNull
    private Country country;
    @NotNull
    private String SSN;
    @NotNull
    private String first_name;
    @NotNull
    private String last_name;
    @NotNull
    private String phone_num;
    @NotNull
    private boolean pending;

    @OrderBy ("timestamp DESC")
    @OneToMany
    Set<Message> inbox;
    @OrderBy ("timestamp DESC")
    @OneToMany
    Set<Message> outbox;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    public Message sendMessage(Message msg) {
	if(msg.getFrom().getUid().equals(uid)) {
	    outbox.add(msg);
	    return msg;
	}
	return null;		// message was not sent
    }
}
