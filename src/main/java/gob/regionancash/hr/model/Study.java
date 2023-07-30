package gob.regionancash.hr.model;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import org.isobit.directory.model.Company;
import org.isobit.directory.model.People;
import org.isobit.util.OptionMap;

@Entity
@Table(name = "esc_study")
@Data
public class Study implements Serializable {

    private static OptionMap STUDY_TYPE;

    static {
        STUDY_TYPE = new OptionMap();
        STUDY_TYPE.put('D', new Object[]{"DOCTORADO", 0});
        STUDY_TYPE.put('M', new Object[]{"MAESTRIA", 0});
        STUDY_TYPE.put('P', new Object[]{"PRIMARIA", 0});
        STUDY_TYPE.put('S', new Object[]{"SECUNDARIA", 0});
        STUDY_TYPE.put('T', new Object[]{"TECNICO", 0});
        STUDY_TYPE.put('U', new Object[]{"UNIVERSITARIO", 1});
    }

    public OptionMap getSTUDY_TYPE() {
        return STUDY_TYPE;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_id")
    private Character typeId;
    @Column(name = "level_id")
    private Character levelId;
    @Column(name = "cycle")
    private Integer cycle;
    @Size(max = 100)
    @Column(name = "career")
    private String career;
    @Column(name = "career_id")
    private Integer careerId;
    @Column(name = "entity_id")
    private Integer entityId;
    @Column(name = "school_id")
    private Integer schoolId;
    @Size(max = 100)
    @Column(name = "entity")
    private String entityName;
    @JoinColumn(name = "entity_id", referencedColumnName = "id_dir", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Company entity;

    @Column(name = "meritOrder")
    private Integer meritOrder;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "people_id")
    private Integer peopleId;

    @Transient
    private People people;

    @Column(name = "employee_id")
    private Integer employeeId;
    
    @Column(name = "code")
    private String code;
    
    @Transient
    private String tempFile;


    public void setEntity(Company entity) {
        this.entityId = entity != null ? entity.getId() : null;
        this.entity = entity;
    }

    @Transient
    private Object ext;

}
