package gob.regionancash.remuneracion.model;

import lombok.*;
import org.isobit.directory.model.People;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rem_payroll_type_people")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PayrollTypePeople {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "payroll_type_id")
    private Integer payrollTypeId;

    @Column(name = "people_id")
    private Integer peopleId;

    @Transient
    private People people;

    @Column(name = "beneficiary")
    private Integer beneficiaryId;

    @Transient
    private People beneficiary;

    @NotNull
    @Column(name = "benefit_type_id")
    private Character benefitTypeId;
}