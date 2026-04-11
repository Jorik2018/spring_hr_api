package gob.regionancash.remuneracion.controller;

import lombok.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import edu.uns.remuneracion.model.PayrollPeriod;
import edu.uns.remuneracion.model.PerPlanilla;
import edu.uns.remuneracion.model.PlanillaPeople;
import edu.uns.remuneracion.model.PlanillaConcept;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import gob.regionancash.remuneracion.model.PayrollConcept;
import gob.regionancash.remuneracion.model.PayrollConceptPK;
import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import gob.regionancash.remuneracion.service.PayrollImportService;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class PayrollImportController {

    private final PayrollImportService importService;

    @PostMapping("/period")
    public ResponseEntity<?> uploadPeriodo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "confirmDelete", defaultValue = "false") boolean confirmDelete,
            @RequestParam(defaultValue = "0") int skip) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().skip(skip).forEach(line -> {
                String[] cols = line.split(",");
                PayrollPeriod period = new PayrollPeriod();
                period.setId(Integer.valueOf(cols[0]));
                period.setYear(Short.valueOf(cols[1]));
                period.setMonth(Short.valueOf(cols[2]));
                importService.savePeriodo(period, confirmDelete);
            });
            return ResponseEntity.ok("Periodo importado correctamente!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/payroll")
    public ResponseEntity<?> uploadPlanilla(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "confirmDelete", defaultValue = "false") boolean confirmDelete,
            @RequestParam(defaultValue = "0") int skip) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().skip(skip).forEach(line -> {
                String[] cols = line.split(",");
                PerPlanilla planilla = new PerPlanilla();
                planilla.setId(Integer.parseInt(cols[0]));
                planilla.setFechaInsert(Timestamp.valueOf(cols[1].replace("'", "").trim()));
                planilla.setIdReponsable(Integer.parseInt(cols[2]));
                planilla.setIdEmisor(Integer.parseInt(cols[3]));
                planilla.setPayrollTypeId(Integer.parseInt(cols[4]));
                planilla.setFechaemision(Timestamp.valueOf(cols[5].replace("'", "").trim()));
                //planilla.setFechacierre(Date.parse(cols[7]));
                planilla.setCierre(Integer.valueOf(cols[8])>0);
                importService.savePlanilla(planilla, Integer.parseInt(cols[6]), confirmDelete);
            });
            return ResponseEntity.ok("Planilla importada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/payroll/people")
    public ResponseEntity<?> uploadDetalle(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "0") int skip) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            final List<String> failedLines = new ArrayList<>();

            reader.lines().skip(skip).forEach(line -> {
                try {
                    String[] cols = line.split(",", -1);
                    PlanillaPeople pp = new PlanillaPeople();
                    pp.setId(new PayrollPeoplePK(
                        parseIntOrDefault(cols[0], null),
                        parseIntOrDefault(cols[1], null)
                    ));
                    pp.setEmployeeTypeId(parseIntOrDefault(cols[2], null));
                    pp.setIdDedicacion(parseShortOrDefault(cols[3], null));
                    pp.setTipo(cols[4]);
                    pp.setRemunerativeLevelId(parseIntOrDefault(cols[5], null));
                    pp.setRemunerativeLevel(cols[6]);
                    pp.setDedicacion(cols[7]);
                    //categoria aux asoci princ
                    //pp.setDiasLaborados(parseFloatOrDefault(cols[5], null));
                    pp.setPosition(cols[8]);
                    //pp.setDependencyName(cols[8]);
                    pp.setDocument(cols[9]);
                    pp.setCommission(cols[10]);

                    pp.setHours(parseIntOrDefault(cols[12], null));

                    importService.savePayrollPeople(pp);

                } catch (Exception e) {
                    // Guardar la línea que falló
                    failedLines.add(line+" -> "+e.getMessage());
                }
            });
            if (!failedLines.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Fallaron las siguientes líneas:\n" + String.join("\n", failedLines));
            }
            return ResponseEntity.ok("Detalle de planilla importado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public static Integer parseIntOrDefault(String str, Integer valorPorDefecto) {
        if (str == null || str.isEmpty()) return valorPorDefecto;
        return Integer.parseInt(str); // Lanzará NumberFormatException si es inválido
    }

    public static Short parseShortOrDefault(String str, Short valorPorDefecto) {
        if (str == null || str.isEmpty()) return valorPorDefecto;
        return Short.parseShort(str); // Lanzará NumberFormatException si es inválido
    }

    // Convierte un String a float, devuelve valorPorDefecto si está vacío o nulo
    public static Float parseFloatOrDefault(String str, Float valorPorDefecto) {
        if (str == null || str.isEmpty()) return valorPorDefecto;
        return Float.parseFloat(str); // Lanzará NumberFormatException si es inválido
    }

    @PostMapping("/payroll/concept")
    public ResponseEntity<?> uploadConcepto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "0") int skip) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().skip(skip).forEach(line -> {
                String[] cols = line.split(",",-1);
                PlanillaConcept pc = new PlanillaConcept();
                pc.setId(new PayrollConceptPK(
                        Integer.parseInt(cols[0]),
                        Integer.parseInt(cols[1]),
                        Integer.parseInt(cols[2])));
                pc.setIdTipomov(Integer.parseInt(cols[3]));
                pc.setAmount(new BigDecimal(cols[4]));
                pc.setConceptName(cols[5]);
                pc.setConceptTypeId(parseShortOrDefault(cols[6], null));
                importService.savePayrollConcept(pc);
            });
            return ResponseEntity.ok("Conceptos de planilla importados correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}