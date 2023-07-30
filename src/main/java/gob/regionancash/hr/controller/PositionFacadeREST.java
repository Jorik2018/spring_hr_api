package gob.regionancash.hr.controller;

import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.repository.PositionRepository;
import gob.regionancash.hr.service.PositionFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.isobit.app.X;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/position")
public class PositionFacadeREST{

    @Autowired
    private PositionRepository positionFacade;

    @PostMapping
    public void create(Position entity) {
        positionFacade.save(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Position entity) {
        positionFacade.edit(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        positionFacade.deleteById(id);
    }

    @GetMapping("{id}")
    public Optional<Position> find(@PathVariable("id") Integer id) {
        return positionFacade.findById(id);
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String name) {
        Map m = new HashMap();
        if(name!=null)m.put("name", name);
        List ll = positionFacade.load(from, to, "o.name ASC", m);
        m.put("data", ll);
        return m;
    }

}
