package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import gob.regionancash.hr.model.License;

public interface LicenseFacade{

    public List<License> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    public List getLicenseSummaryList(Map<String, Object> filters);
    
    public List getPeriodList(Integer peopleId);
    
    void edit(License perlicencia);
    
    public void prepareCreate(License l);
    
    public License load(Object m);
    
    public Object getContent(Map m);

    public Object report();

        void remove(Optional<License> findById);
        
}