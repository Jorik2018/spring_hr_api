package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_categoria_doc")
@Data
public class EscCategoriaDoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cat")
    private Integer idCat;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idCat")
    private List<EscNivelRemunerativoUns> escNivelRemunerativoUnsList;
    @OneToMany(mappedBy = "idCat")
    private List<EscCategoria> escCategoriaList;
}