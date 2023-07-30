package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_especialidad")
@Data
public class EscEspecialidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_esp")
    private Short idEsp;
    @Basic(optional = false)
    @Column(name = "especialidad")
    private String especialidad;
    @Column(name = "abrev")
    private String abrev;

}
