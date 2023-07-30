package gob.regionancash.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.regionancash.hr.escalafon.model.Merit;
import gob.regionancash.hr.escalafon.repository.MeritRepository;

@RestController
@RequestMapping("/merit")
public class MeritFacadeREST{

    @Autowired
    private MeritRepository meritFacade;

    @PostMapping
    public void create(Merit entity) {
        meritFacade.edit(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Merit entity) {
        meritFacade.edit(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        //contractFacade.remove(super.find(id));
    }

    @GetMapping("{id}")
    public Merit find(@PathVariable("id") Integer id) {
        return meritFacade.find(id);
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String people,
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) Integer peopleId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String dependency,
            @RequestParam(required = false) Integer dependencyId,
            @RequestParam(required = false) String query) {
        Map m = new HashMap();
        if (position != null) {
            m.put("position", position);
        }
        if (query != null) {
            m.put("query", query);
        }
        if (code != null) {
            m.put("code", code);
        }
        if (active != null) {
            m.put("active", active);
        }
        if (peopleId != null) {
            m.put("people.id", peopleId);
        }
        if (employee != null) {
            m.put("employee", employee);
        }
        if (people != null) {
            m.put("people", people);
        }

        if (dependency != null) {
            m.put("dependency", dependency);
        }
        if (XUtil.intValue(dependencyId) != 0) {
            m.put("dependency", dependencyId);
        }
        List ll = meritFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

}