package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EscNivelRemunerativoUnsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_nivel")
    private short idNivel;
    @Basic(optional = false)
    @Column(name = "tipo")
    private boolean tipo;

    public EscNivelRemunerativoUnsPK() {
    }

    public EscNivelRemunerativoUnsPK(short idNivel, boolean tipo) {
        this.idNivel = idNivel;
        this.tipo = tipo;
    }

    public short getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(short idNivel) {
        this.idNivel = idNivel;
    }

    public boolean getTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idNivel;
        hash += (tipo ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscNivelRemunerativoUnsPK)) {
            return false;
        }
        EscNivelRemunerativoUnsPK other = (EscNivelRemunerativoUnsPK) object;
        if (this.idNivel != other.idNivel) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jsuns.escalafon.jpa.EscNivelRemunerativoUnsPK[ idNivel=" + idNivel + ", tipo=" + tipo + " ]";
    }
    
}
