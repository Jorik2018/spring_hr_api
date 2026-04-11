package gob.regionancash.remuneracion.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PayrollPeoplePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_planilla")
    private int payrollId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_esc")
    private int employeeId;

}
