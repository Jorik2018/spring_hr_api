package gob.regionancash.hr.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DaySummary implements Serializable {

    private String dayName;

    private List exceptions;

    private String entry;

    private String breaking;

    private String out;

    private long entryDelaySeconds;

    private long breakDelaySeconds;

    private int day;

    private Date date;

    private String decision = "";

    private String remark = "";

    private String fullName;

    private String code;

    private int period;

    private String foul = "";

    private String particular = "";

    private String tolerance = "";

}
