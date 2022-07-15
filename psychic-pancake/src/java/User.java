package psychic_pancake;

import jakarta.persistence.*;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user", schema="main")
public class User implements Serializable {
    @Id
    private String uid;
    public String GetUID() {return uid;}
    public void SetUID(String uid) {this.uid = uid;}

    private String email;
    public String GetEmail() {return email;}
    public void SetEmail(String email) {this.email = email;}
}
