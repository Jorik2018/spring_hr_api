package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_tipo_falta")
@Data
public class FaultType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tf")
    private Short id;
    @Basic(optional = false)
    @Column(name = "falta")
    private String description;

}
