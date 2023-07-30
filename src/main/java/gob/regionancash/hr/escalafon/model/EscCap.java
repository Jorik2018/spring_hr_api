package gob.regionancash.hr.escalafon.model;

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
import org.isobit.util.XUtil;

@Entity
@Table(name = "esc_cap")
@Data
public class EscCap implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscCapPK escCapPK;
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "nro_cargo")
    private Short nroCargo;
    @Column(name = "total_necesario")
    private Short totalNecesario;
    @Column(name = "previstos")
    private Short previstos;
    @Column(name = "nombrados")
    private Short nombrados;
    @Column(name = "contratados")
    private Short contratados;
    @Column(name = "vacantes")
    private Short vacantes;
    @JoinColumn(name = "id_hist_cap", referencedColumnName = "id_hist_cap")
    @ManyToOne
    private EscHistoriaCap idHistCap;
    @JoinColumn(name = "id_nivel", referencedColumnName = "id_nivel")
    @ManyToOne
    private EscNivelRemunerativo nivelRem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escCap")
    private List<EscPersDep> escPersDepList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escCap")
    private List<EscRotacion> escRotacionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escCap")
    private List<EscPromocion> escPromocionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escCap")
    private List<EscRotacionCas> escRotacionesCasList;

     @JoinColumn(name = "id_dep", referencedColumnName = "id_dep",insertable=false,updatable=false)
    @ManyToOne(optional = false)
    private Dependency dependency;
    
}
