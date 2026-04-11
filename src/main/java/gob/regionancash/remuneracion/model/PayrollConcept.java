package gob.regionancash.remuneracion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rem_payroll_concept")
public class PayrollConcept implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PayrollConceptPK id;

    @Column(name = "id_tipomov")
    private Integer idTipomov;

    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private BigDecimal amount;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "concepto")
    private String concept;

    @Column(name = "tipoconcepto")
    private Integer conceptTypeId;

    @Transient
    private Object ext;

    @Transient
    private short col;

    @Transient
    private Character ind;

    public PayrollConcept(PayrollConceptPK id) {
        this.id = id;
    }

}