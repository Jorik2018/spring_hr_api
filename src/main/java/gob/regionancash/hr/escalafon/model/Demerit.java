package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import java.util.Date;

import gob.regionancash.hr.model.Employee;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "esc_demeritos")
@Data
public class Demerit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_dtra")
    
    private String document;
    
    @Column(name = "mencion")
    private String mencion;

    @Column(name = "dias_sancion")
    private Short diasSancion;
    
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Basic(optional = false)
    @Column(name = "fecha_dem")
    @Temporal(TemporalType.DATE)
    
    private Date fechaDem;

    @Basic(optional = false)
    private boolean activo;

    @Column(name = "observacion")
    private String observation;

    @Column(name = "id_esc")
    private Integer employeeId;

    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc",insertable=false,updatable=false)
    @ManyToOne(optional = false)
    private Employee employee;

    @JoinColumn(name = "id_tf", referencedColumnName = "id_tf")
    @ManyToOne(optional = true)
    private FaultType faultType;

    @JoinColumn(name = "id_ts", referencedColumnName = "id_ts")
    @ManyToOne(optional = false)
    private SanctionType sanctionType;

}
