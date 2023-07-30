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
@Table(name = "esc_nombramientos_doc")
@Data
public class EscNombramientoDoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_nomd")
    private Integer idNomd;
    @Basic(optional = false)
    @Column(name = "fecha_nomb")
    @Temporal(TemporalType.DATE)
    private Date fechaNomb;
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private String idDtra;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    private Short horas;

    private Boolean estado;
    @JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap"),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")})
    @ManyToOne(optional = false)
    private EscCapDocente escCapDocente;
    
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;

    public EscNombramientoDoc() {
    }

    public EscNombramientoDoc(Integer idNomd) {
        this.idNomd = idNomd;
    }

    public EscNombramientoDoc(Integer idNomd, Date fechaNomb, String idDtra) {
        this.idNomd = idNomd;
        this.fechaNomb = fechaNomb;
        this.idDtra = idDtra;
    }

    public Integer getIdNomd() {
        return idNomd;
    }

    public void setIdNomd(Integer idNomd) {
        this.idNomd = idNomd;
    }

    public Date getFechaNomb() {
        return fechaNomb;
    }

    public void setFechaNomb(Date fechaNomb) {
        this.fechaNomb = fechaNomb;
    }

    public String getIdDtra() {
        return idDtra;
    }

    public void setIdDtra(String idDtra) {
        this.idDtra = idDtra;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Short getHoras() {
        return horas;
    }

    public void setHoras(Short horas) {
        this.horas = horas;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public EscCapDocente getEscCapDocente() {
        return escCapDocente;
    }

    
}
