package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "per_espec_pl")
public class PerEspecPl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_espe")
    private Short idEspe;
    @JoinColumn(name = "id_tipo_pl", referencedColumnName = "id_tipo_pl")
    @ManyToOne(optional = true)
    private LicenseType permLice;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "especificacion")
    private String especificacion;
    @Size(max = 7)
    @Column(name = "id_regla")
    private String idRegla;

    public PerEspecPl() {
    }

    public PerEspecPl(Short idEspe) {
        this.idEspe = idEspe;
    }

    public Short getIdEspe() {
        return idEspe;
    }

    public void setIdEspe(Short idEspe) {
        this.idEspe = idEspe;
    }

    public LicenseType getPermLice() {
        return permLice;
    }

    public void setPermLice(LicenseType permLice) {
        this.permLice = permLice;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public String getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(String idRegla) {
        this.idRegla = idRegla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEspe != null ? idEspe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerEspecPl)) {
            return false;
        }
        PerEspecPl other = (PerEspecPl) object;
        if ((this.idEspe == null && other.idEspe != null) || (this.idEspe != null && !this.idEspe.equals(other.idEspe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uns.ocper.jpa.PerEspecPl[ idEspe=" + idEspe + " ]";
    }

}
