package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hd.model.OrgContrato;

public interface OrgContratoFacade{

    public List<OrgContrato> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(OrgContrato orgcontrato);
}