package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.isobit.directory.model.Dependency;

@Entity
@Table(name = "esc_cap_uns")
public class EscCapUns implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscCapUnsPK escCapUnsPK;
    
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private Dependency orgDependencia;
        
    @Column(name = "nro_cargo")
    private Short nroCargo;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "total_necesario")
    private Short totalNecesario;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "previstos")
    private Short previstos;
    @Column(name = "nombrados")
    private Short nombrados;
    @Column(name = "contratados")
    private Short contratados;
    @Column(name = "vacantes")
    private Short vacantes;
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "id_hist_cap", referencedColumnName = "id_hist_cap")
    @ManyToOne
    private EscHistoriaCap idHistCap;
    @JoinColumns({
        @JoinColumn(name = "id_nivel", referencedColumnName = "id_nivel",insertable=false,updatable=false),
        @JoinColumn(name = "tipo_cap", referencedColumnName = "tipo",insertable=false,updatable=false)})
    @ManyToOne(optional = false)
    private EscNivelRemunerativoUns escNivelRemunerativoUns;

    public Dependency getOrgDependencia() {
        return orgDependencia;
    }

    public void setOrgDependencia(Dependency orgDependencia) {
        this.orgDependencia = orgDependencia;
    }

    public EscCapUns() {
    }

    public EscCapUns(EscCapUnsPK escCapUnsPK) {
        this.escCapUnsPK = escCapUnsPK;
    }

    public EscCapUns(int idCap, int idDep, boolean tipoCap) {
        this.escCapUnsPK = new EscCapUnsPK(idCap, idDep, tipoCap);
    }

    public EscCapUnsPK getEscCapUnsPK() {
        return escCapUnsPK;
    }

    public void setEscCapUnsPK(EscCapUnsPK escCapUnsPK) {
        this.escCapUnsPK = escCapUnsPK;
    }

    public Short getNroCargo() {
        return nroCargo;
    }

    public void setNroCargo(Short nroCargo) {
        this.nroCargo = nroCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Short getTotalNecesario() {
        return totalNecesario;
    }

    public void setTotalNecesario(Short totalNecesario) {
        this.totalNecesario = totalNecesario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Short getPrevistos() {
        return previstos;
    }

    public void setPrevistos(Short previstos) {
        this.previstos = previstos;
    }

    public Short getNombrados() {
        return nombrados;
    }

    public void setNombrados(Short nombrados) {
        this.nombrados = nombrados;
    }

    public Short getContratados() {
        return contratados;
    }

    public void setContratados(Short contratados) {
        this.contratados = contratados;
    }

    public Short getVacantes() {
        return vacantes;
    }

    public void setVacantes(Short vacantes) {
        this.vacantes = vacantes;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EscHistoriaCap getIdHistCap() {
        return idHistCap;
    }

    public void setIdHistCap(EscHistoriaCap idHistCap) {
        this.idHistCap = idHistCap;
    }

    public EscNivelRemunerativoUns getEscNivelRemunerativoUns() {
        return escNivelRemunerativoUns;
    }

    public void setEscNivelRemunerativoUns(EscNivelRemunerativoUns escNivelRemunerativoUns) {
        this.escNivelRemunerativoUns = escNivelRemunerativoUns;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escCapUnsPK != null ? escCapUnsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscCapUns)) {
            return false;
        }
        EscCapUns other = (EscCapUns) object;
        if ((this.escCapUnsPK == null && other.escCapUnsPK != null) || (this.escCapUnsPK != null && !this.escCapUnsPK.equals(other.escCapUnsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jsuns.escalafon.jpa.EscCapUns[ escCapUnsPK=" + escCapUnsPK + " ]";
    }
    
}
