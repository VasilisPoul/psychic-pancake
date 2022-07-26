package psychic_pancake;

import jakarta.persistence.*;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "location", schema="main")
@Data
public class Location extends Resource implements Serializable {
    @Id
    private String name;
    private double latitude, longitude;

    public Location() {}

    public Location(User owner, String name, double lat, double lon) {
	super(owner);
	this.name = name;
	this.latitude = lat;
	this.longitude = lon;
    }
}
