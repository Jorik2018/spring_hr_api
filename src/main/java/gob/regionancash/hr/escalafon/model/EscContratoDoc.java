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
@Table(name = "esc_contratos_doc")
@Data
public class EscContratoDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_cnd")
    private Integer idCnd;
    
    @Basic(optional = false)
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private String idDtra;
    
    @Basic(optional = false)
    private short horas;
    
    private String mencion;
    
    private String periodo;
    
    @JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap"),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")})
    @ManyToOne(optional = false)
    private EscCapDocente escCapDocente;

    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;
    
}
