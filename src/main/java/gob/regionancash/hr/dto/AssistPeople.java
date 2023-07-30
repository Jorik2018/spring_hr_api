package gob.regionancash.hr.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import gob.regionancash.hr.model.Attendance;

@Entity
@Table(name = "rh_assist")
public class AssistPeople implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_dir")
    private int idDir;
    @Transient
    private boolean early;

    public boolean isEarly() {
        return early;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setEarly(boolean early) {
        this.early = early;
    }
    @Size(max = 45)
    @Column(name = "contract_id")
    private String contractId;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "time")
    @Temporal(TemporalType.TIME)
    private Date time;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private int turno;
    private String fullName;
    private String dependency;
    private String position;
    private String code;
    private Date entryTime;
    private String entryDelay;
    private Long entryDelaySeconds;
    private Long outDelaySeconds;
    private Date breakTime;
    private String breakDelay;
    private Long breakDelaySeconds;
    private Date outTime;
    private String justification;
    private String aux;
    private String dayName;

    @Transient
    private List<Attendance> times;

    public String getBreakDelay() {
        return breakDelay;
    }

    public void setBreakDelay(String breakDelay) {
        this.breakDelay = breakDelay;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public List<Attendance> getTimes() {
        return times;
    }

    public void setTimes(List<Attendance> times) {
        this.times = times;
    }

    public Long getOutDelaySeconds() {
        return outDelaySeconds;
    }

    public void setOutDelaySeconds(Long outDelaySeconds) {
        this.outDelaySeconds = outDelaySeconds;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Long getEntryDelaySeconds() {
        return entryDelaySeconds;
    }

    public void setEntryDelaySeconds(Long entryDelaySeconds) {
        this.entryDelaySeconds = entryDelaySeconds;
    }

    public Long getBreakDelaySeconds() {
        return breakDelaySeconds;
    }

    public void setBreakDelaySeconds(Long breakDelaySeconds) {
        this.breakDelaySeconds = breakDelaySeconds;
    }



    public String getEntryDelay() {
        return entryDelay;
    }

    public void setEntryDelay(String entryDelay) {
        this.entryDelay = entryDelay;
    }

    public Date getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Date breakTime) {
        this.breakTime = breakTime;
    }



    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getIdDir() {
        return idDir;
    }

    public void setIdDir(int idDir) {
        this.idDir = idDir;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssistPeople)) {
            return false;
        }
        AssistPeople other = (AssistPeople) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gob.regionancash.hr.rest.RhAssist[ id=" + id + " ]";
    }

}
