package gob.regionancash.hr.model;

import lombok.Data;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rh_session")
@Data
public class Session implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int loginId;

    private int peopleId;

    @Column(name = "logout_time", insertable = false, updatable = false)
    private long peopleIdLong;

    @Temporal(TemporalType.DATE)
    private Date accessDate;

    @Column(name = "logout_time")
    @Temporal(TemporalType.TIME)
    private Date logoutTime;

    private Long companyId;

    private Integer dependencyId;

    private Integer positionId;

    private Integer time;

    @Column(name = "login_time", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date loginTime;
}
