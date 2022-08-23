package psychic_pancake;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.*;

@Entity
@Table(name = "country", schema="main")
@Data
@NoArgsConstructor
public class Country implements Serializable {
    @Id
    private String name;
    private double latitude, longitude;
}
