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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import gob.regionancash.remuneracion.service.IngresoDescuentoService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/hd")
@RequiredArgsConstructor
public class HDReportController {

    private final IngresoDescuentoService ingresoDescuentoService;

    @PostMapping("/report")
    public ResponseEntity<?> process() {
        LocalDate localDate = LocalDate.of(2000, 1, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        var result =ingresoDescuentoService.getConstanciaHD(
            13187,
            date,
            null,
            List.of(12, 32)
        );
        return ResponseEntity.ok(result);
    }

}