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
@Table(name = "image", schema="main")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private User owner;
    @Lob
    private String b64;
}
