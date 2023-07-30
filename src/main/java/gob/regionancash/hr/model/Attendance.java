package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "rh_assist")
@Setter
@Data
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_dir")
    private Long peopleId=(long)0;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "code")
    private Long code;

    @Column(name = "contract_id")
    private Integer contractId;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "time")
    @Temporal(TemporalType.TIME)
    private Date time;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "in_out_mode")
    private Integer inOutMode;
    @Column(name = "machine_number")
    private Integer machineNumber;
    @Column(name = "verify_mode")
    private Integer verifyMode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canceled")
    private short canceled;

}
