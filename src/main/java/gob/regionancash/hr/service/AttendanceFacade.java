package gob.regionancash.hr.service;

import gob.regionancash.hr.model.Attendance;
import gob.regionancash.hr.model.DevicePeople;
import gob.regionancash.zk.LogData;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AttendanceFacade{

    public List<Attendance> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    Object importFile(File position);
    
    //long count();

    public void bulk(List<LogData> list);

    public List getPeople(List<Integer> list);

    public void save(DevicePeople user);

    public Object getReport(Integer dependencyId,Date from, Date to,Map m);

}