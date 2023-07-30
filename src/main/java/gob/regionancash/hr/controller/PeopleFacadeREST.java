package gob.regionancash.hr.controller;

import gob.regionancash.hr.repository.ContractRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
public class PeopleFacadeREST{

    @Autowired
    private ContractRepository contractFacade;

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String dependency,
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
        if (employee != null) {
            m.put("people", employee);
        }
        if (dependency != null) {
            m.put("dependency", dependency);
        }
        List ll = contractFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

}
