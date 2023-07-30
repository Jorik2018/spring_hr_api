package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Position;

public interface PositionFacade{

    public List<Position> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Position position);

}