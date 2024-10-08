package gob.regionancash.hr.controller;

import gob.regionancash.hr.escalafon.model.Demerit;
import gob.regionancash.hr.model.Holiday;
import gob.regionancash.hr.repository.HolidayRepository;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holiday")
@AllArgsConstructor
public class HolidayREST{

    private HolidayRepository repository;

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String name) {
        Map m = new HashMap();
        //m.put("data",laborRegimeFacade.load(from, to, null, m));
        m.put("data",repository.findAll());
        return m;
    }

    @PostMapping
    public Holiday create(@RequestBody Holiday entity) {
        return repository.save(entity);
    }

}
