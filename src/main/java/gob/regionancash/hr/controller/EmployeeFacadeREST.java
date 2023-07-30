package gob.regionancash.hr.controller;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.repository.EmployeeRepository;
import gob.regionancash.hr.service.EmployeeFacade;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.isobit.app.X;
import org.isobit.util.XFile;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@RestController
@RequestMapping("/employee")
public class EmployeeFacadeREST {

    @Autowired
    private EmployeeRepository employeeFacade;

    @PostMapping    
    public void create(Employee entity) {
        employeeFacade.edit(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Employee entity){
        Employee e = employeeFacade.findById(id).get();
        e.setPensionSystem(entity.getPensionSystem());
        employeeFacade.edit(e);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        Employee employee = employeeFacade.findById(id).get();
        employee.setCanceled(true);
        employeeFacade.edit(employee);
    }

    @GetMapping("status")
    public Object getStatusList() {
        return employeeFacade.getStatusList();
    }

    @GetMapping("{id}")
    public Employee find(@PathVariable("id") Integer id) {
        return employeeFacade.load(id);
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) String laborRegime,
            @RequestParam(required = false) String workModality,
            @RequestParam(required = false) String pensionSystem,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String dependency) {
        Map m = new HashMap();
        if (employee != null) {
            m.put("employee", employee);
        }
        if (pensionSystem != null) {
            m.put("pensionSystem", toList(pensionSystem, (String o) -> Integer.parseInt(o.trim())));
        }
        if (workModality != null) {
            m.put("workModality", toList(workModality, (String o) -> o.trim()));
        }
        if (laborRegime != null) {
            m.put("laborRegime", toList(laborRegime, (String o) -> Short.parseShort(o.trim())));
        }
        if (code != null) {
            m.put("code", code);
        }
        if (query != null) {
            m.put("query", query);
        }
        if (dependency != null) {
            m.put("dependency", dependency);
        }
        List ll = employeeFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

    @PostMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> exportFile(Map params) throws IOException {
        params.put("sorter", "w.fullName,o.detalle DESC,o.fecSoli DESC");
        params.put("type", Arrays.asList("V"));
        int option = XUtil.intValue(params.get("option"));
        Object pensionSystem = params.get("pensionSystem");
        if (pensionSystem != null) {
            params.put("pensionSystem", pensionSystem instanceof List ? pensionSystem : toList(pensionSystem.toString(), (String o) -> Integer.parseInt(o.trim())));
        }
        Object laborRegime = params.get("laborRegime");
        if (laborRegime != null) {
            if(laborRegime instanceof List){
                List original=(List) laborRegime;
                laborRegime = original.stream().map(o->Short.parseShort(o.toString())).collect(Collectors.toList());
            }
            params.put("laborRegime", laborRegime instanceof List ? laborRegime : toList(laborRegime.toString(), (String o) -> Short.parseShort(o.trim())));
        }
        Object workModality = params.get("workModality");
        if (workModality != null) {
            params.put("workModality", workModality instanceof List ? workModality : toList(workModality.toString(), (String o) -> o.toString().trim()));
        }
        Object people = params.get("people");
        if (people != null) {
            params.put("people", people instanceof List ? people : 
                    toList(people.toString(),(String o) -> Integer.parseInt(o.trim())));
        }
        Object employee = params.get("employee");
        if (employee != null) {
            params.put("employee", employee instanceof List ? employee : toList(employee.toString(), (String o) -> Integer.parseInt(o.trim())));
        }
        List m = (List) employeeFacade.getActiveEmployeList(params);
        String template = "employees";
        if (option == 1) {
            template = "licenceSummary";
        }else if (option == 2) {
            template = "EmployeesDetailed";
        }
        params.put("data", m);


        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", XFile.writeObject(new File(File.createTempFile("temp-file-name", ".tmp").getParentFile(),
                "data.jao"), params), MediaType.APPLICATION_OCTET_STREAM)
                .filename("data.jao")
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"file\"; filename=\"data.jao\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        bodyBuilder.part("template", template, MediaType.TEXT_PLAIN);

        MultiValueMap<String, HttpEntity<?>> parts = new MultiValueMapAdapter<>(bodyBuilder.build());

        return WebClient.create("http://localhost/api/jreport/")
                .post()
                .uri((UriBuilder uriBuilder) -> uriBuilder.path("/").build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .retrieve()
                .bodyToMono(byte[].class)
                .map(bytes -> toResponse(bytes, "employees." + params.get("-EXTENSION")));
    }

    public ResponseEntity<byte[]> toResponse(byte[] bytes, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    public List<Object> toList(String s, Converter converter) {
        return Arrays.stream(s.split(","))
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    public interface Converter {
        Object convert(String s);
    }
    
}