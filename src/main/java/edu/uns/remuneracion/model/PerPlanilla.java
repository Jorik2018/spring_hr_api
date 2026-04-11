package edu.uns.remuneracion.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "per_planilla")
public class PerPlanilla implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_planilla")
    private Integer id;
    
    @Column(name = "fecha_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInsert;
    
    @Column(name = "id_reponsable")
    private Integer idReponsable;
    
    @Column(name = "id_emisor")
    private Integer idEmisor;
    
    @Column(name = "id_tipopla")
    private Integer payrollTypeId;
    
    @Column(name = "fechaemision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaemision;
    
    @Column(name = "fechacierre")
    @Temporal(TemporalType.DATE)
    private Date fechacierre;
     
    @Basic(optional = false)
    @NotNull
    @Column(name = "cierre")
    private boolean cierre;

    @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
    @ManyToOne(optional = false)
    private PayrollPeriod period;
    
}
