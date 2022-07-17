package psychic_pancake;


import javax.persistence.*;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Resource implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    public Long GetID() {return id;}
    public void SetID(Long id) {this.id = id;}


    @ManyToOne
    private User owner;
    public User GetOwner() {return owner;}
    public void SetOwner(User owner) {this.owner = owner;}

    public Resource() {}
    public Resource(User owner) {
	this.owner = owner;
    }
}
