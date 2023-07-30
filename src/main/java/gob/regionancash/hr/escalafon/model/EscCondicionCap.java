package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "esc_condicion_cap")
public class EscCondicionCap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cc")
    private Short id;
    @Basic(optional = false)
    @Column(name = "condicion")
    private String name;

    public EscCondicionCap() {
    }

    public EscCondicionCap(Short idCc) {
        this.id = idCc;
    }

    public EscCondicionCap(Short idCc, String condicion) {
        this.id = idCc;
        this.name = condicion;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscCondicionCap)) {
            return false;
        }
        EscCondicionCap other = (EscCondicionCap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + ": "+this.name;
    }
    
}
