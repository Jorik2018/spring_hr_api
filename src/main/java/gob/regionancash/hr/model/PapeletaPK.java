package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class PapeletaPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "ano_eje")
    private String anoEje;
    @Basic(optional = false)
    @Column(name = "n_papeleta")
    private String nPapeleta;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anoEje != null ? anoEje.hashCode() : 0);
        hash += (nPapeleta != null ? nPapeleta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PapeletaPK)) {
            return false;
        }
        PapeletaPK other = (PapeletaPK) object;
        if ((this.anoEje == null && other.anoEje != null) || (this.anoEje != null && !this.anoEje.equals(other.anoEje))) {
            return false;
        }
        if ((this.nPapeleta == null && other.nPapeleta != null) || (this.nPapeleta != null && !this.nPapeleta.equals(other.nPapeleta))) {
            return false;
        }
        return true;
    }

}
