package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "rh_device_people")
@Data
public class DevicePeople implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "enroll_number")
    private Integer enrollNumber;

    @Basic(optional = false)
    @NotNull
    @Column(name = "people_id")
    private int peopleId;
    
    @Transient
    private long code;

    @Transient
    private String fullName;
    
}
