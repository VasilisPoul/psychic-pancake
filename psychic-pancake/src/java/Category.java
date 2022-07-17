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
@Data
@Table(name="item_category", schema="default")
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    public Category() {}
    public Category(String name) { this.name = name; }
}
