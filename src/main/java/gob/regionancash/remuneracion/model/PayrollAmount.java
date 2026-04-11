package gob.regionancash.remuneracion.model;

import gob.regionancash.remuneracion.model.Concept;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rem_payroll_amount")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PayrollAmount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "payroll_group_id")
    private Integer payrollGroupId;

    @Column(name = "payroll_type_id")
    private Integer payrollTypeId;

    @Size(max = 2)
    private String type;

    // puede ser cargo / nivel / persona
    @Column(name = "target_id")
    private Integer targetId;

    @Transient
    private String targetName;

    @Transient
    private Object target;

    @NotNull
    @Column(name = "concept_id")
    private Integer conceptId;

    @Transient
    private Concept concept;

    @NotNull
    @Column(name = "ini_date")
    @Temporal(TemporalType.DATE)
    private Date iniDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private boolean canceled;
}