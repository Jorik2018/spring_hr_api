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
@Table(name = "esc_nivel_estudios")
@Data
public class EscNivelEstudio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_nivel")
    private Short idNivel;
    @Basic(optional = false)
    @Column(name = "nivel")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "abrev")
    private String abrev;
    @Column(name = "ambito")
    private String ambito;
    @Column(name = "ultimo")
    private Boolean ultimo;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNivel")
//    private List<EscEstudio> escEstudioList;

    
}
