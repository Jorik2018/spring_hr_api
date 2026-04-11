package edu.uns.remuneracion.model;

import java.io.Serializable;
import java.math.BigDecimal;

import gob.regionancash.remuneracion.model.PayrollConceptPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "per_planillaconcepto")
public class PlanillaConcept implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected PayrollConceptPK id;
    
    @Column(name = "id_tipomov")
    private Integer idTipomov;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal amount;
    
    @Size(max = 80)
    @Column(name = "concepto")
    private String conceptName;
    
    @Column(name = "tipoconcepto")
    private Short conceptTypeId;
    
    @JoinColumn(name = "id_planilla", referencedColumnName = "id_planilla", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PerPlanilla payroll;
    
}
