package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import org.isobit.directory.model.Dependency;

@Entity
@Table(name = "esc_cap_docente")
@Data
public class EscCapDocente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscCapDocentePK escCapDocentePK;
    @Basic(optional = false)
    @Column(name = "nro_cargo")
    private short nroCargo;
    @Basic(optional = false)
    @Column(name = "total_necesario")
    private short totalNecesario;
    @Basic(optional = false)
    @Column(name = "previstos")
    private short previstos;
    @Basic(optional = false)
    @Column(name = "nombrados")
    private short nombrados;
    @Basic(optional = false)
    @Column(name = "vacantes")
    private short vacantes;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "contratados")
    private Integer contratados;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false)
    private EscCategoria categoria;
    @JoinColumn(name = "id_hist_cap", referencedColumnName = "id_hist_cap")
    @ManyToOne(optional = false)
    private EscHistoriaCap idHistCap;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escCapDocente")
    private List<EscPromocionDoc> escPromocionesDocList;
    @JoinColumn(name = "id_dep", referencedColumnName = "id_dep",insertable=false,updatable=false)
    @ManyToOne(optional = false)
    private Dependency dependency;

}
