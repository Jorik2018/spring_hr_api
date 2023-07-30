package gob.regionancash.hr.controller;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.repository.ContractRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.isobit.util.XMap;
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

@RestController
@RequestMapping("/contract")
public class ContractFacadeREST{

    @Autowired
    private ContractRepository contractFacade;

    @PostMapping
    public void create(Contract entity) {
        contractFacade.edit(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Contract entity) {
        contractFacade.edit(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        contractFacade.remove(id);
    }

    @GetMapping("{id}")
    public Contract find(@PathVariable("id") Integer id) {
        return contractFacade.load(id);
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, 
            @PathVariable("to") Integer to,
            @RequestParam(name="people",required = false) String employee,
            @RequestParam(name="peopleId",required = false) Integer peopleId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String company,
            @RequestParam(name="active",required = false) Integer active,
            @RequestParam(name="dependency",required = false) String dependency,
            @RequestParam(name="dependencyId",required = false) Integer dependencyId,
            @RequestParam(name="query",required = false) String query) {
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
            int i = XUtil.intValue(employee);
            m.put("people", i > 0 ? i : employee);
        }
        if (company != null) {
            int i = XUtil.intValue(company);
            m.put("company", i > 0 ? i : company);
        }
        if (dependency != null) {
            m.put("dependency", dependency);
        }
        if (XUtil.intValue(dependencyId) != 0) {
            m.put("dependency", dependencyId);
        }
        List ll = contractFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

    @GetMapping("markink")
    public List markingList() {
        return contractFacade.getDataSet(1, new XMap());
    }

}
