package edu.uns.remuneracion.model;

import java.io.Serializable;

import gob.regionancash.remuneracion.model.PayrollPeoplePK;
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
@Table(name = "per_detallepla")
public class PlanillaPeople implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected PayrollPeoplePK id;
    
    @Column(name = "id_tipotrabajor")
    private Integer employeeTypeId;
    
    @Column(name = "id_dedicacion")
    private Short idDedicacion;
    
    @Size(max = 15)
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "id_nivel")
    private Integer remunerativeLevelId;
    
    @Size(max = 25)
    @Column(name = "nivel")
    private String remunerativeLevel;
    
    @Size(max = 5)
    @Column(name = "dedicacion")
    private String dedicacion;
    
    @Size(max = 400)
    @Column(name = "cargo")
    private String position;
    
    @Size(max = 50)
    @Column(name = "resolucion")
    private String document;
    
    @Size(max = 50)
    @Column(name = "encargaturaadm")
    private String commission;
    
    @Size(max = 15)
    @Column(name = "acti_codigo")
    private String actiCodigo;
    
    @Column(name = "horas")
    private Integer hours;

    @JoinColumn(name = "id_planilla", referencedColumnName = "id_planilla", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PerPlanilla perPlanilla;
    
}
