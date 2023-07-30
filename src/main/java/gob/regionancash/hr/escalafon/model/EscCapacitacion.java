package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Transient;
import lombok.Data;

import org.isobit.directory.model.Company;

import gob.regionancash.hr.model.Employee;

@Entity
@Table(name = "esc_capacitacion")
@Data
public class EscCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;
    
    @JoinColumn(name = "id_cc", referencedColumnName = "id_cc")
    @ManyToOne(optional = false)
    private EscCondicionCap condition;
    
    @JoinColumn(name = "id_tc", referencedColumnName = "id_tc")
    @ManyToOne(optional = false)
    private EscTipoCap type;
    
    @Basic(optional = false)
    @Column(name = "capacitacion")
    private String denomination;
    
    @Basic(optional = false)
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Column(name = "id_pl")
    private Integer idPl;
    
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Basic(optional = false)
    @Column(name = "id_inst")
    private int companyId;
    
    @JoinColumn(name = "id_inst", referencedColumnName = "id_dir", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Company institution;

    @Transient
    private Company company;
    
    @Column(name = "horas")
    private Short hours;
    
    @Column(name = "horas_text")
    private String horasText;
    
    @Column(name = "mencion")
    private String mencion;
    
    @Transient
    private Object ext;

}