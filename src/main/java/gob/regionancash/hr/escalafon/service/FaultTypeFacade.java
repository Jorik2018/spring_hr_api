package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.escalafon.model.FaultType;

public interface FaultTypeFacade{

    public List<FaultType> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(FaultType esctipofalta);

}