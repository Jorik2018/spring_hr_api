package gob.regionancash.remuneracion.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "per_concept")
public class Concept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String abbreviation;

    @Column(name = "type_id")
    private Integer typeId;

    private Integer weight;

    @Column(name = "pdt_code")
    private String pdtCode;

    private String description;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Transient
    private Object ext;

    public Concept(Integer id) {
        this.id = id;
    }

    public Concept(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}