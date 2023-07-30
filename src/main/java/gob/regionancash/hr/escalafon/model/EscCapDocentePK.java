package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EscCapDocentePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_cap")
    private int idCap;
    @Basic(optional = false)
    @Column(name = "id_dep")
    private int idDep;

    public EscCapDocentePK() {
    }

    public EscCapDocentePK(int idCap, int idDep) {
        this.idCap = idCap;
        this.idDep = idDep;
    }

    public int getIdCap() {
        return idCap;
    }

    public void setIdCap(int idCap) {
        this.idCap = idCap;
    }

    public int getIdDep() {
        return idDep;
    }

    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCap;
        hash += (int) idDep;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscCapDocentePK)) {
            return false;
        }
        EscCapDocentePK other = (EscCapDocentePK) object;
        if (this.idCap != other.idCap) {
            return false;
        }
        if (this.idDep != other.idDep) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jsuns.escalafon.jpa.EscCapDocentePK[ idCap=" + idCap + ", idDep=" + idDep + " ]";
    }
    
}
