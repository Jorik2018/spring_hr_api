package org.isobit.directory.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "mes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mes implements Serializable {

    @Id
    @Column(name = "mes_eje")
    private String mesEje; // nombre de columna correcto, minúscula para seguir convención

    @Column(name = "nombre_mes") // si quieres agregar un nombre descriptivo
    private String name;
}