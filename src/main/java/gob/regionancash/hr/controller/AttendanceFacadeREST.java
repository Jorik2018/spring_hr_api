package gob.regionancash.hr.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gob.regionancash.hr.model.Attendance;
import gob.regionancash.hr.model.DevicePeople;
import gob.regionancash.hr.repository.AttendanceRepository;
import gob.regionancash.zk.LogData;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
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

@RestController
@RequestMapping("/attendance")
public class AttendanceFacadeREST{

    @Autowired
    private AttendanceRepository assistFacade;

    @PostMapping
    public void create(Attendance entity) {
        assistFacade.save(entity);
    }

    @PostMapping("bulk")
    public boolean bulk(List<LogData> list) {
        assistFacade.bulk(list);
        return true;
    }

    @PostMapping("people")
    public List getPeople(List<Integer> list) {
        return assistFacade.getPeople(list);
    }

    @PostMapping("user")
    public Object getPeople(DevicePeople user) {
        assistFacade.save(user);
        return user;
    }

    /*@PostMapping("zk")
    public boolean zk() {
        client.target("http://localhost:7761/sync-attendance")
                .request()
                .post(Entity.json(new HashMap()), String.class);
        return true;
    }*/

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, Attendance entity) {
        assistFacade.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        assistFacade.deleteById(id);
    }

    @GetMapping("{id}")
    public Attendance find(@PathVariable("id") Integer id) {
        return assistFacade.findById(id).get();
    }

    @GetMapping
    public List<Attendance> findAll() {
        return assistFacade.findAll();
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam("people") String name, @RequestParam("code") String code) {
        Map m = new HashMap();
        if (name != null) {
            m.put("people", name);
        }
        if (code != null) {
            m.put("code", code);
        }
        List ll = assistFacade.load(from, to, null, m);
        m.put("data", ll);
        return m;
    }

    @GetMapping("count")
    public String countREST() {
        return String.valueOf(assistFacade.count());
    }

    @PostMapping("import/{fileName}")
    public Object importFile(@PathVariable("fileName") String fileName) throws IOException {
//        fileName = client.target("http://localhost:" + X.getRequest().getLocalPort() + "/xls/api/jao/" + fileName)
//                .request()
//                .post(Entity.text(""), String.class);
        assistFacade.importFile(
                new File(File.createTempFile("temp-file-name", ".tmp").getParentFile(), fileName)
        );
        return true;
    }

    @PostMapping(value="download",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> exportFile(Map params) throws IOException {
        int option = XUtil.intValue(params.get("option"));
        Date from = null, to = null;
        try {
            to = new Date(Long.parseLong(params.get("to").toString()));
        } catch (Exception e) {
        }
        try {
            from = new Date(Long.parseLong(params.get("from").toString()));
        } catch (Exception e) {
        }
        //System.out.println("m=" + params);
        Calendar c = Calendar.getInstance();
        if (to == null && params.containsKey("year")) {
            c.set(Calendar.YEAR, XUtil.intValue(params.get("year")));
            c.set(Calendar.MONTH, XUtil.intValue(params.get("month")) - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            //System.out.println("year=" + params.get("year"));
            to = c.getTime();
        }
        c.setTime(to);
        c.set(Calendar.DAY_OF_MONTH, 1);
        if (from == null) {
            from = c.getTime();
        }
        params.putAll((Map) assistFacade.getReport(67, from, to, params));
        String template = "Matriz_Asistencia_A4_L";
        switch (option) {
            case 2:
                template = "Matriz_Faltas_Tardanzas";
                break;
            case 3:
                template = "TardanzaDia";
        }
        params.put("template", template);
        String fileName = "data.jao";



        /*MultipartFormDataOutput multiPart = new MultipartFormDataOutput();
        multiPart.addFormData("file", XFile.writeObject(new File(File.createTempFile("temp-file-name", ".tmp").getParentFile(),
                fileName), params), MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.addFormData("template", template, MediaType.TEXT_PLAIN_TYPE);
        return toResponse(
                ClientBuilder.newClient().target("http://localhost/api/jreport/")
                        .request(MediaType.WILDCARD_TYPE, MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_OCTET_STREAM_TYPE)
                        .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE), InputStream.class),
                "attendance." + params.get("-EXTENSION"));

*/

        WebClient webClient = WebClient.create("http://localhost");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", writeObjectToByteArray(params), MediaType.APPLICATION_OCTET_STREAM)
                .filename(fileName)
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=file; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        builder.part("template", template, MediaType.TEXT_PLAIN);
        return webClient.post()
                .uri("/api/jreport/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(InputStream.class)
                .map(responseBody -> {
                    String extension = String.valueOf(params.get("-EXTENSION"));
                    String filename = "attendance." + extension;
                    byte[] bytes = readAllBytes(responseBody);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData(filename, filename);
                    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
                });
    }

    public static byte[] writeObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        return baos.toByteArray();
    }    

    private byte[] readAllBytes(InputStream inputStream) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @PostMapping("report")
    public Object report(Map params) {
        int dependency = XUtil.intValue(params.get("dependency"));
        Date to = null;
        //System.out.println("m=" + params);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, XUtil.intValue(params.get("year")));
        c.set(Calendar.MONTH, XUtil.intValue(params.get("month")) - 1);
        int day = XUtil.intValue(params.get("day"));
        if (day == 0) {
            day = 9;
        }
        Date from;
        if (day == 1) {
            c.set(Calendar.DAY_OF_MONTH, day);
            from = c.getTime();
            c.set(Calendar.DAY_OF_MONTH, c.getMaximum(Calendar.DAY_OF_MONTH));
            to = c.getTime();
        } else {
            c.set(Calendar.DAY_OF_MONTH, day);
            to = c.getTime();
            c.setTime(to);
            c.add(Calendar.MONTH, -1);
            c.set(Calendar.DAY_OF_MONTH, day + 1);
            from = c.getTime();
        }
        Date today = new Date();

        if (to.after(today)) {
            to = today;
        }
        int option = XUtil.intValue(params.get("option"));
        params.putAll((Map) assistFacade.getReport(dependency, from, to, params));
        String template = "Matriz_Asistencia_A4_L";
        switch (option) {
            case 2:
                template = "Matriz_Faltas_Tardanzas";
        }
        params.put("template", template);
        return params;
    }

}
