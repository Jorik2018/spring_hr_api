package gob.regionancash.hr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;
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

import gob.regionancash.hr.escalafon.repository.StudyRepository;
import gob.regionancash.hr.model.Study;

@RestController
@RequestMapping("/study")
public class StudyFacadeREST {

    @Autowired
    private StudyRepository studyFacade;

    @PostMapping
    public void create(Study entity) {
        studyFacade.save(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Study entity) {
        studyFacade.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        //contractFacade.remove(super.find(id));
    }

    @GetMapping("{id}")
    public Study find(@PathVariable("id") Integer id) {
        return studyFacade.find(id);
    }
    
    @GetMapping("type")
    public Object getTypeList() {
        List option=new ArrayList();
        Map m=new Study().getSTUDY_TYPE();
        for(Entry set:(Set<Entry>)m.entrySet()){
            Object row[]=(Object[]) set.getValue();
            option.add(new XMap("id",set.getKey(),"name",row[0]));
        }
        return option;
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
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
            m.put("people", employee);
        }

        if (dependency != null) {
            m.put("dependency", dependency);
        }
        if (XUtil.intValue(dependencyId) != 0) {
            m.put("dependency", dependencyId);
        }
        List ll = studyFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

    @GetMapping(value="count",produces=MediaType.TEXT_MARKDOWN_VALUE)
    public String countREST() {
        return String.valueOf(studyFacade.count());
    }

}
