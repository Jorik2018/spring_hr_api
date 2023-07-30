package gob.regionancash.zk;

import java.io.Serializable;

import lombok.Data;

@Data
public class LogData implements Serializable {

    private int machineNumber;
    
    private long enrollNumber;
    
    private int verifyMode;
    
    private int inOutMode;

    private int year;
    
    private int month;
    
    private int day;
    
    private int hour;
    
    private int minute;
    
    private int second;

    private String timestamp;

}
