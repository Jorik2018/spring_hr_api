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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "esc_reposiciones")
@Data
public class EscReposicion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_rep")
    private Integer idRep;
    
    @Column(name = "id_dtra")
    private String idDtra;
    
    @Column(name = "mencion")
    private String mencion;
    
    @Column(name = "fecha_rep")
    @Temporal(TemporalType.DATE)
    private Date fechaRep;
    
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    
    @JoinColumn(name = "id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Demerit id;
    
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;

}
