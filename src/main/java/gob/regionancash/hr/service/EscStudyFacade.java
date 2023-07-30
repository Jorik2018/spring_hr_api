package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Study;

public interface EscStudyFacade{

    public List<Study> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Study perlicencia);

    public Study load(Object m);

}