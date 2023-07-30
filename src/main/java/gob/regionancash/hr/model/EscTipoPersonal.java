package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_tipo_personal")
@Data
public class EscTipoPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Short id;

    @Basic(optional = false)
    @Column(name = "tipo")
    private String name;

    @Column(name = "id_modpers")
    private Short idModpers;
    
}
