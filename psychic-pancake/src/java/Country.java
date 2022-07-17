package psychic_pancake;

import jakarta.persistence.*;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "country", schema="main")
public class Country implements Serializable {
    @Id
    private String name;
    private double latitude, longitude;

    public Country() {}

    public Country(String name, double lat, double lon) {
	this.name = name;
	this.latitude = lat;
	this.longitude = lon;
    }
}
