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
@Table(name = "esc_promociones")
@Data
public class EscPromocion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pro")
    private Integer idPro;
    
    @Basic(optional = false)
    @Column(name = "fecha_prom")
    @Temporal(TemporalType.DATE)
    private Date fechaProm;
    
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private String idDtra;

    @Basic(optional = false)
    private boolean estado;
    
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap"),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")})
    @ManyToOne(optional = false)
    private EscCap escCap;

    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;
    
    @JoinColumn(name = "id_tp", referencedColumnName = "id_tp")
    @ManyToOne(optional = false)
    private EscTipoPromocion idTp;
        
}
