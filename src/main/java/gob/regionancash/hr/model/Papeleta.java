package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.isobit.util.OptionMap;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Entity
@Table(name = "papeleta")
@Data
public class Papeleta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PapeletaPK papeletaPK;
    @Column(name = "tipo_pape")
    private Character tipoPape;
    @Column(name = "regulariza")
    private Character regulariza;
    @Column(name = "peri_pape")
    private Character periPape;
    @Column(name = "fecha_pape")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPape;
    @Column(name = "fecha_del")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDel;
    @Column(name = "fecha_al")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAl;
    @Column(name = "hora_ini")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaIni;
    
    @Column(name = "hora_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;

    @JoinColumn(name = "dni", referencedColumnName = "dni")
    @ManyToOne
    private Personal personal;
    private static final Map estadoMap = new OptionMap();
    private static final Map tipoPapeMap = new OptionMap();
    private static final Map periPapeMap = new OptionMap();
    static {
        estadoMap.put('0', "Borrador");
        estadoMap.put('1', "Tramite");
        estadoMap.put('2', "Pendiente");
        estadoMap.put('3', "Iniciado");
        estadoMap.put('4', "Concluido");
        estadoMap.put('9', "Anulado");
        tipoPapeMap.put('1', "NORMAL");
        tipoPapeMap.put('2', "DE REGULARIZACION");
        periPapeMap.put('1', "HORAS");
        periPapeMap.put('2', "DIAS");
    }

    public Map getEstadoMap() {
        return estadoMap;
    }

    public Map getTipoPapeMap() {
        return tipoPapeMap;
    }

    public Map getPeriPapeMap() {
        return periPapeMap;
    }
    
    public String getKey(){
        return this.getPapeletaPK()!=null?this.getPapeletaPK().getAnoEje()+"-"+this.getPapeletaPK().getNPapeleta():null;
    }
    
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "estado")
    private Character estado;
    
}
