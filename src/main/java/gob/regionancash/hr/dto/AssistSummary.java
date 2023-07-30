package gob.regionancash.hr.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.isobit.directory.model.Dependency;

public class AssistSummary implements Serializable {

    public int getPp() {
        return pp;
    }

    private String td;
    private int particularMinutes;

    public int getParticularMinutes() {
        return particularMinutes;
    }

    public void setParticularMinutes(int particularMinutes) {
        this.particularMinutes = particularMinutes;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getParticularTime() {
        return particularTime;
    }

    public void setParticularTime(int particularTime) {
        this.particularTime = particularTime;
    }

    private int pp;
    private int particularTime;
    private HashMap children = new HashMap();
    private String code;
    private Long[][] times;
    private String[] status;
    private String boss;
    private int peopleId;
    private String fullName;
    private String position;
    private int tr;
    private int tp;
    private int v;
    private int l;
    private int lgh;
    private int dm;
    private int dmc;
    private int f;
    private int workedDays;
    private int hours;

    private String delay;

    private String tolerance;
    private Long accumulatedSeconds;
    private Long seconds;
    private List data;
    private Integer dependencyId;
    private Dependency dependency;
    private String laborRegime;

    public String getLaborRegime() {
        return laborRegime;
    }

    public void setLaborRegime(String laborRegime) {
        this.laborRegime = laborRegime;
    }

    public AssistSummary get(Object k) {
        return (AssistSummary) children.get(k);
    }

    public Collection values() {
        return children.values();
    }

    public void put(Object k, Object o) {
        children.put(k, o);
    }

    public Long[][] getTimes() {
        return times;
    }

    public void setTimes(Long[][] times) {
        this.times = times;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String minutes) {
        this.delay = minutes;
    }

    public Long getAccumulatedSeconds() {
        return accumulatedSeconds;
    }

    public void setAccumulatedSeconds(Long accumulatedSeconds) {
        this.accumulatedSeconds = accumulatedSeconds;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDependencyId() {
        return dependencyId;
    }

    public void setDependencyId(Integer dependencyId) {
        this.dependencyId = dependencyId;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public String[] getStatus() {
        return status;
    }

    public int getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(int peopleId) {
        this.peopleId = peopleId;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getLgh() {
        return lgh;
    }

    public void setLgh(int lgh) {
        this.lgh = lgh;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getDmc() {
        return dmc;
    }

    public void setDmc(int dmc) {
        this.dmc = dmc;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(int workedDays) {
        this.workedDays = workedDays;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "AssistSummary{" + "code=" + code + ", peopleId=" + peopleId + ", fullName=" + fullName + '}';
    }

}
