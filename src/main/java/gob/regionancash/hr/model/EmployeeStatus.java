package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rh_employee_status")
@Data
public class EmployeeStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    private Short id;
    
    @Basic(optional = false)
    private String name;

    private Boolean activo;

}
