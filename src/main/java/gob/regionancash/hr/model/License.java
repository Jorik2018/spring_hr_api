package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import org.isobit.directory.model.People;

@Entity
@Table(name = "rh_permission")
@Data
public class License implements Serializable {

    public static HashMap<Short, String> AUTHORIZACTION_TYPE = new HashMap();
    
    static {
        AUTHORIZACTION_TYPE.put((short) 1, "COMISION OFICIAL DE SERVICIOS");
        AUTHORIZACTION_TYPE.put((short) 2, "PARTICULAR");
        AUTHORIZACTION_TYPE.put((short) 3, "ENFERMEDAD");
        AUTHORIZACTION_TYPE.put((short) 4, "CAPACITACIONES");
        AUTHORIZACTION_TYPE.put((short) 5, "CITACION EXPRESA JUDICIAL, MILITAR O POLICIAL");
        AUTHORIZACTION_TYPE.put((short) 6, "OTROS");
        AUTHORIZACTION_TYPE.put((short) 7, "FUNCION EDIL O REGIONAL");
        AUTHORIZACTION_TYPE.put((short) 8, "LACTANCIA");
        AUTHORIZACTION_TYPE.put((short) 9, "MATERNIDAD");
    }
    
    public static HashMap<String, String> TYPE = new HashMap();
               
    static {
        TYPE.put("P", "PAPELETA");
        TYPE.put("J", "JUSTIFICACION");
        TYPE.put("O", "ONOMASTICO");
        TYPE.put("V", "VACACIONES");
        TYPE.put("LGH", "LICENCIA CON GOCE DE HABER");
        TYPE.put("LSH", "LICENCIA SIN GOCE DE HABER");
        TYPE.put("LS", "LICENCIA SINDICAL");
        TYPE.put("LP", "LICENCIA POR PATERNIDAD");
        TYPE.put("LF", "LICENCIA POR FALLECIMIENTO");
        TYPE.put("DM", "DESCANSO MEDICO");
        TYPE.put("DMC", "DESCANSO MEDICO COVID");
        TYPE.put("TR", "TRABAJO REMOTO");
        TYPE.put("TM", "TRABAJO MIXTO");
        TYPE.put("TP", "TRABAJO PRESENCIAL");
    }

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "days")
    private Integer days;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "plaza")
    private String plaza;
    
    @Column(name = "trabajador_id", insertable = false, updatable = false)
    private Integer peopleId;
    
    @Column(name = "trabajador_id", insertable = false, updatable = false)
    private Long peopleIdLong;
    
    @JoinColumn(name = "trabajador_id", referencedColumnName = "id_dir")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private People worker;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fec_soli")
    @Temporal(TemporalType.DATE)
    private Date fecSoli;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "nro_soli")
    private String nroSoli;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "tipo_soli")
    private String type;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "esta_soli")
    private String estaSoli;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 10)
    @Column(name = "hora_soli")
    private String horaSoli;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fec_ini")
    @Temporal(TemporalType.DATE)
    private Date fecIni;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fec_fin")
    @Temporal(TemporalType.DATE)
    private Date fecFin;
    
    //@JsonFormat(pattern="dd/MM/yyyy HH:mm")
    @Temporal(TemporalType.TIME)
    @Column(name = "time_start", insertable = false, updatable = false)
    private Date timeStart;
    
    @Column(name = "time_start")
    private String timeStartString;
    
    //@JsonFormat(pattern="dd/MM/yyyy HH:mm")
    @Temporal(TemporalType.TIME)
    @Column(name = "time_end", insertable = false, updatable = false)
    private Date timeEnd;
    
    @Column(name = "time_end")
    private String timeEndString;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 25)
    @Column(name = "detalle")
    private String detalle;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 3)
    @Column(name = "cod_movi")
    private String codMovi;
    
    @Transient
    private Object ext;
    
    @Transient
    private Object file;
    
    @Column(name = "authorization_type")
    private Short authorizationType;
    
    private Integer fid;
    
    @Transient
    private String description;

}
