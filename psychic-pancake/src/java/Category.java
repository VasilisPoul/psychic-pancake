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
@Data
@Table(name="item_category", schema="main")
public class Category implements Serializable {
    @Id
    @NotNull
    private String name;

    @ManyToMany(mappedBy="categories")
    private List<Listing> listings;

    public Category() {}
    public Category(String name) { this.name = name; }
}
