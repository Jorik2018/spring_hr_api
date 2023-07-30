package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "esc_categoria")
public class EscCategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_categoria")
    private Short idCategoria;
    @Basic(optional = false)
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "dedicacion")
    private String dedicacion;
    @Basic(optional = false)
    @Column(name = "abrev_cat")
    private String abrevCat;
    @Column(name = "abrev_ded")
    private String abrevDed;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne
    private EscCategoriaDoc idCat;

    public EscCategoria() {
    }

    public EscCategoria(Short idCategoria) {
        this.idCategoria = idCategoria;
    }

    public EscCategoria(Short idCategoria, String categoria, String abrevCat) {
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.abrevCat = abrevCat;
    }

    public Short getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Short idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDedicacion() {
        return dedicacion;
    }

    public void setDedicacion(String dedicacion) {
        this.dedicacion = dedicacion;
    }

    public String getAbrevCat() {
        return abrevCat;
    }

    public void setAbrevCat(String abrevCat) {
        this.abrevCat = abrevCat;
    }

    public String getAbrevDed() {
        return abrevDed;
    }

    public void setAbrevDed(String abrevDed) {
        this.abrevDed = abrevDed;
    }

    
    public EscCategoriaDoc getIdCat() {
        return idCat;
    }

    public void setIdCat(EscCategoriaDoc idCat) {
        this.idCat = idCat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscCategoria)) {
            return false;
        }
        EscCategoria other = (EscCategoria) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jsuns.escalafon.jpa.EscCategoria[ idCategoria=" + idCategoria + " ]";
    }
    
}
