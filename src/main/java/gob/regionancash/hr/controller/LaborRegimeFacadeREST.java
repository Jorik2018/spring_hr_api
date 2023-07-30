package gob.regionancash.hr.controller;

import gob.regionancash.hr.repository.LaborRegimeRepository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labor-regime")
public class LaborRegimeFacadeREST{

    @Autowired
    private LaborRegimeRepository laborRegimeFacade;

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String name) {
        Map m = new HashMap();
        m.put("data",laborRegimeFacade.load(from, to, null, m));
        return m;
    }

}
