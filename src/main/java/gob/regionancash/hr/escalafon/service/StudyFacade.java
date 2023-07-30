package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Study;

public interface StudyFacade{

    public List<Study> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    public Study find(Object id);
    
}