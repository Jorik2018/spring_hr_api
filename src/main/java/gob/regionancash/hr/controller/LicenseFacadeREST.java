package gob.regionancash.hr.controller;

import gob.regionancash.hr.model.License;
import gob.regionancash.hr.repository.LicenseRepository;
import gob.regionancash.hr.service.LicenseFacade;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.isobit.util.XFile;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;

@RestController
@RequestMapping("/license")
public class LicenseFacadeREST {

    @Value("${service.jreport}")
    private String jreportPath;

    @Autowired
    private LicenseRepository licenseFacade;

    @PostMapping
    public void create(License entity) {
        licenseFacade.edit(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, License entity) {
        licenseFacade.edit(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        licenseFacade.deleteById(id);
    }

    @GetMapping("{id}")
    public License find(@PathVariable("id") Integer id) {
        return licenseFacade.load(id);
    }

    @GetMapping("period/{peopleId}")
    public List getPeriodList(@PathVariable("peopleId") Integer peopleId) {
        return licenseFacade.getPeriodList(peopleId);
    }

    @GetMapping
    public Object findRange(
            @RequestParam("people") String people,
            @RequestParam("employee") String employee,
            @RequestParam("peopleId") Integer peopleId,
            @RequestParam("position") String position,
            @RequestParam("code") String code,
            @RequestParam("type") String type,
            @RequestParam("document") String document,
            @RequestParam("detail") String detail,
            @RequestParam("active") Integer active,
            @RequestParam("dependency") String dependency,
            @RequestParam("dependencyId") Integer dependencyId,
            @RequestParam("query") String query) {

        return findRange(0, 0, people, employee, peopleId, position,
                code, type, document, detail, active, dependency, dependencyId,
                query);
    }

    @GetMapping("{from}/{to}")
    public Object findRange(@PathVariable("from") Integer from, @PathVariable("to") Integer to,
            @RequestParam(required = false) String people,
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) Integer peopleId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String document,
            @RequestParam(required = false) String detail,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String dependency,
            @RequestParam(required = false) Integer dependencyId,
            @RequestParam(required = false) String query) {
        Map m = new HashMap();
        if (detail != null) {
            m.put("detail", detail);
        }
        if (type != null) {
            m.put("type", XUtil.toList(type, (String o) -> o.toString().trim()));
        }
        if (position != null) {
            m.put("position", position);
        }
        if (query != null) {
            m.put("query", query);
        }
        if (code != null) {
            m.put("code", code);
        }
        if (document != null) {
            m.put("document", document);
        }
        if (active != null) {
            m.put("active", active);
        }
        if (peopleId != null) {
            m.put("people.id", peopleId);
        }
        if (people != null) {
            m.put("people", people);
        }
        if (peopleId != null) {
            m.put("peopleId", peopleId);
        }
        if (dependency != null) {
            m.put("dependency", dependency);
        }
        if (XUtil.intValue(dependencyId) != 0) {
            m.put("dependency", dependencyId);
        }
        List ll = licenseFacade.load(from, to, null, m);

        m.put("data", ll);
        return m;
    }

    @PostMapping("download")
    public Mono<ResponseEntity<byte[]>> exportFile(@RequestBody Map<String, Object> params) throws IOException {
        params.put("sorter", "w.fullName,o.detalle DESC,o.fecSoli DESC");
        params.put("type", Arrays.asList("V"));
        int option = XUtil.intValue(params.get("option"));

        List m;
        if (option == 1) {
            m = (List) licenseFacade.getLicenseSummaryList(params);
        } else {
            m = (List) licenseFacade.load(0, 0, null, params);
        }
        String template = "Holidays";
        if (option == 1) {
            template = "licenceSummary";
        }
        params.put("data", m);
        MultipartBodyBuilder multiPart = new MultipartBodyBuilder();
        File file = XFile.writeObject(new File(File.createTempFile("temp-file-name", ".tmp").getParentFile(), "data.jao"),
        params);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        multiPart.part("file",
                fileContent,
                MediaType.APPLICATION_OCTET_STREAM);
        multiPart.part("template", template, MediaType.TEXT_PLAIN);

        return WebClient.create(jreportPath).post()
                .uri(UriComponentsBuilder.newInstance().path("").toUriString())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multiPart.build()))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return DataBufferUtils.join(response.body(BodyExtractors.toDataBuffers()))
                                .map(dataBuffer -> {
                                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(bytes);
                                    DataBufferUtils.release(dataBuffer);
                                    return bytes;
                                });
                    } else {
                        return Mono.error(new RuntimeException("Failed to download file: " + response.statusCode()));
                    }
                })
                .map(fileBytes -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("attachment", "holidays." + params.get("-EXTENSION"));
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileBytes);
                });
    }
    /*
     * public Response toResponse(InputStream is2, String filename) {
     * return Response.ok((StreamingOutput) (java.io.OutputStream output) -> {
     * try {
     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
     * int len;
     * byte[] buffer = new byte[4096];
     * while ((len = is2.read(buffer, 0, buffer.length)) != -1) {
     * output.write(buffer, 0, len);
     * }
     * output.flush();
     * is2.close();
     * } catch (IOException e) {
     * throw new WebApplicationException("File Not Found !!", e);
     * }
     * }, MediaType.APPLICATION_OCTET_STREAM)
     * .header("content-disposition", "attachment; filename =" + filename
     * ).build();
     * }
     */
}
