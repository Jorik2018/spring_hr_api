package gob.regionancash.hr.escalafon.model;

import gob.regionancash.hr.model.Employee;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "esc_pers_dep")
@Data
public class EscPersDep implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pd")
    private Integer idPd;
    @Column(name = "fec_inicio")
    @Temporal(TemporalType.DATE)
    private Date fecInicio;
    @Column(name = "fec_fin")
    @Temporal(TemporalType.DATE)
    private Date fecFin;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "id_dtra")
    private String idDtra;
    @JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap"),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")})
    @ManyToOne(optional = false)
    private EscCap escCap;

    @JoinColumn(name = "id_desp", referencedColumnName = "id_desp")
    @ManyToOne(optional = false)
    private EscDesplazamiento idDesp;
    
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;

}
