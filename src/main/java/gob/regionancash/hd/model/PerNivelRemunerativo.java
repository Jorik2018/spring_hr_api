package gob.regionancash.hd.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "per_nivel_remunerativo")
@Data
public class PerNivelRemunerativo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nivel_r")
    private Integer idNivelR;

    @Column(name = "id_grupo_o")
    private Integer idGrupoO;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nivel")
    private String nivel;

    @Size(max = 100)
    @Column(name = "descripcion")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_nivel")
    private int idNivel;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_go")
    private int idGo;
    
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private PerCategoriaDocente category;
    
    @JoinColumn(name = "id_dedicacion", referencedColumnName = "iddedicacion")
    @ManyToOne
    private PerDedicacion idDedicacion;

}
