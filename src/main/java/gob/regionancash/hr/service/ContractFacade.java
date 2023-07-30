package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;
import org.isobit.util.XMap;
import gob.regionancash.hr.model.Contract;

public interface ContractFacade{

    public List<Contract> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    public List<Object> peopleList(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Contract consejero);

    public Contract load(Object o);

    public List<Contract> getConsejeroList(Map m);

    public List getDataSet(int i, XMap map);

    public void remove(Integer id);

}
