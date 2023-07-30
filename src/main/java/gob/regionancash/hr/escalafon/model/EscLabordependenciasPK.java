package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Embeddable
public class EscLabordependenciasPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_percl")
    private int idPercl;
    @Basic(optional = false)
    @Column(name = "labor")
    private String labor;
    @Basic(optional = false)
    @Column(name = "id_dep")
    private int idDep;
    @Basic(optional = false)
    @Column(name = "fec_ini")
    @Temporal(TemporalType.DATE)
    private Date fecIni;

    public EscLabordependenciasPK() {
    }

    public EscLabordependenciasPK(int idPercl, String labor, int idDep, Date fecIni) {
        this.idPercl = idPercl;
        this.labor = labor;
        this.idDep = idDep;
        this.fecIni = fecIni;
    }

    public int getIdPercl() {
        return idPercl;
    }

    public void setIdPercl(int idPercl) {
        this.idPercl = idPercl;
    }

    public String getLabor() {
        return labor;
    }

    public void setLabor(String labor) {
        this.labor = labor;
    }

    public int getIdDep() {
        return idDep;
    }

    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPercl;
        hash += (labor != null ? labor.hashCode() : 0);
        hash += (int) idDep;
        hash += (fecIni != null ? fecIni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscLabordependenciasPK)) {
            return false;
        }
        EscLabordependenciasPK other = (EscLabordependenciasPK) object;
        if (this.idPercl != other.idPercl) {
            return false;
        }
        if ((this.labor == null && other.labor != null) || (this.labor != null && !this.labor.equals(other.labor))) {
            return false;
        }
        if (this.idDep != other.idDep) {
            return false;
        }
        if ((this.fecIni == null && other.fecIni != null) || (this.fecIni != null && !this.fecIni.equals(other.fecIni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jsuns.escalafon.jpa.EscLabordependenciasPK[ idPercl=" + idPercl + ", labor=" + labor + ", idDep=" + idDep + ", fecIni=" + fecIni + " ]";
    }
    
}
