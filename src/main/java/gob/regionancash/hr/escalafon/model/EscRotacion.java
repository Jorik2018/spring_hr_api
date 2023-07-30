package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;

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
@Table(name = "esc_rotaciones")
@Data
public class EscRotacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_rot")
    private Integer idRot;
    
    @Basic(optional = false)
    @Column(name = "fec_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Column(name = "fec_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    
    @Column(name = "id_dtra")
    private String idDtra;
    
    @Column(name = "mencion")
    private String mencion;
    
    @Basic(optional = false)
    @Column(name = "jefe")
    private boolean jefe;
    
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
