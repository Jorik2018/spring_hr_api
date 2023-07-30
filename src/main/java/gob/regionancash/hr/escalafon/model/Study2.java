package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

import org.isobit.directory.model.Company;

import gob.regionancash.hr.model.Employee;

@Entity
@Table(name = "esc_estudios")
@Data
public class Study2 implements Serializable {

    /*
    //        if(escEstudio.getIdInst()!=null){
//            ResultSet 
    rs=_JDBC.getCnx(request).createStatement()
    .executeQuery("select id_inst,nombre_inst,* FROM esc_instituciones 
    where id_inst="+escEstudio.getIdInst());
//        }
     */
    @Column(name = "nro_matricula")
    private Integer nroMatricula;
    /*@JoinColumn(name = "id_inst", referencedColumnName = "id_dir")
    @ManyToOne(optional = false)*/
    @Column(name = "id_inst")
    private Integer idInstitucion;
    /**/
    @Transient
    private Company institucion;
    @JoinColumn(name = "id_nivel", referencedColumnName = "id_nivel")
    @ManyToOne(optional = false)
    private EscNivelEstudio nivel;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escEstudio")
//    private List<EscSuscdiploma> escSuscdiplomaList;
    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Employee employee;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private String mension;
    
    @Basic(optional = false)
    private String materia;
    @Id
    @Basic(optional = false)
    @TableGenerator(table = "sequence", pkColumnValue = "id_estud", pkColumnName = "seq_name", valueColumnName = "seq_count", name = "id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Column(name = "fecha_aprcu")
    @Temporal(TemporalType.DATE)
    private Date fechaAprcu;

    @Column(name = "nro_libro")
    private Integer nroLibro;

    private Integer folio;
    
    @Column(name = "nro_orden")
    private Integer nroOrden;
    
    @Column(name = "grado_abrev")
    private String gradoAbrev;

}
