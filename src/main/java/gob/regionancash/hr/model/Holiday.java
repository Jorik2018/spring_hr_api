package gob.regionancash.hr.model;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "rh_holiday")
@Data
public class Holiday implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    private LocalDate date;
    
    private String description; 
    
    private String type;
    
    @Transient
    private Integer index;

}
