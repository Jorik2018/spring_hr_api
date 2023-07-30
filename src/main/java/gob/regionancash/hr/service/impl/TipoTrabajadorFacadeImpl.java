package gob.regionancash.hr.service.impl;

import gob.regionancash.hr.service.TipoTrabajadorFacade;


public class TipoTrabajadorFacadeImpl implements TipoTrabajadorFacade {

    public enum Perm {
        PERSONAL, ABASTECIMIENTO,
        AUTORIZA, CONFIGURACION, ALMACEN, CONTABILIDAD,
        DIRECCION, ADQUISICION, PRESUPUESTO, COMMERCE, REGISTRO_CIVIL,
        PROGRAMACION, FORMULACION, OPERACION, SEGURIDAD, TRAMITE, CATASTRO
    };

    public void init() {
        /*
        add(new MenuFacade.MenuModule() {
            @Override
            public void addMenu(List<Object[]> moduleList) {
                moduleList.add(new Object[]{Perm.ALMACEN, new MenuFacade.Tree("Almacen", "", "/admin/almacen", "/resources/images/m_almacen.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/almacen/movimiento/List.xhtml", "Movimientos de Almacen", null, null, null, null, null},
                        {"-"},
                        {"/admin/almacen/combustible/List.xhtml", "Control de Combustible", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/admin/almacen/planContable.xhtml", "Plan Contable", null, null, null, null, null},
                        {"-"},
                        {"/admin/almacen/movil.xhtml", "Moviles", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/itemBienSEACE/List.xhtml", "Catalogo del SBN", null, null, null, null, null},}},
                    {null, "Consultas", null, null, null, null, new Object[][]{
                        {"/faces/almacen/movimiento/detalle/List.xhtml", "Stock en Almacen", null, null, null, null, null},
                        {"/admin/almacen/movil.xhtml", "Stock Prestamo", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/report/inventarioFisicoAlmacen.xhtml", "Inventario Fisico de Almacen", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/report/detallePoliza.xhtml", "Generar Detalle de Polizas Mensuales", null, null, null, null, null},
                        {"/faces/almacen/report/resumenPoliza.xhtml", "Generar Resumen de Polizas Mensuales", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/report/kardexValorizado.xhtml", "Kardex Valorizado de Almacen", null, null, null, null, null},
                        {"/faces/almacen/report/kardexArticulo.xhtml", "Kardex Valorizado x Articulo", null, null, null, null, null},
                        {"/faces/almacen/movimiento/detalle/List.xhtml", "Actualiza Stock", null, null, null, null, null}
                    }},
                    {null, "Reportes", null, null, null, null, new Object[][]{
                        {"/faces/almacen/report/cronologicoGI.xhtml", "Reporte de Cronologicos de G.I.", null, null, null, null, null},
                        {"/faces/almacen/report/cronologicoGS.xhtml", "Reporte de Cronologicos de G.S.", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/report/cronologicoGIProveedor.xhtml", "Cronologico de GI por Proveedor", null, null, null, null, null},
                        {"/faces/almacen/report/cronologicoGSPersonal.xhtml", "Cronologico de GS por Personal", null, null, null, null, null},
                        {"-"},
                        {"/faces/almacen/report/kardexMensualCombustible.xhtml", "Kardex Mensual de Combustible", null, null, null, null, null}
                    }},
                    {null, "Herramientas", null, null, null, null, new Object[][]{
                        {"/admin/operacion/w_interface_simi", "Interface SIMI", null, null, null, null, null},
                        {"/admin/operacion/w_registro_cuenta_bienes", "Actualización de Cuentas de Bienes", null, null, null, null, null},
                        {"-"},
                        {"/faces/personal/report/onomastico.xhtml", "w_ano_eje", null, null, null, null, null}
                    }}
                }});
                moduleList.add(new Object[]{Perm.REGISTRO_CIVIL, new MenuFacade.Tree("Registro Civil", "", "/admin/registroCivil", "/resources/images/familly.png"), new Object[][]{
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/registroCivil/partidaNacimiento/List.xhtml", "Partidas Nacimiento", null, null, null, null, null},
                        {"/faces/registroCivil/partidaMatrimonio/List.xhtml", "Partidas Matrimonio", null, null, null, null, null},
                        {"/faces/registroCivil/partidaDefuncion/List.xhtml", "Partidas Defuncion", null, null, null, null, null}
                    }},
                    {null, "Reportes", null, null, null, null, new Object[][]{
                        {"/faces/registroCivil/report/PartidasNacimientoEntregadasMes.xhtml", "Partidas Nacimiento Entregadas Mensualmente", null, null, null, null, null},
                        {"/faces/registroCivil/report/PartidasMatrimonioEntregadasMes.xhtml", "Partidas Matrimonio Entregadas Mensualmente", null, null, null, null, null},
                        {"/faces/registroCivil/report/PartidasDefuncionEntregadasMes.xhtml", "Partidas Defuncion Entregadas Mensualmente", null, null, null, null, null},
                        {"-"},
                        {"/faces/registroCivil/report/NacimientosAnio.xhtml", "Nacimientos por Año", null, null, null, null, null},
                        {"/faces/registroCivil/report/MatrimoniosAnio.xhtml", "Matrimonios por Año", null, null, null, null, null},
                        {"/faces/registroCivil/report/DefuncionesAnio.xhtml", "Defunciones por Año", null, null, null, null, null}

                    }}
                }});

                moduleList.add(new Object[]{Perm.TRAMITE, new MenuFacade.Tree("Tramite Documentario", "", "/admin/tramiteDocumentario", "/resources/images/m_judiciales.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/tramiteDocumentario/movimiento/List.xhtml", "Bandeja de Documentos", null, null, null, null, null},
                        {"/faces/tramiteDocumentario/orden/List.xhtml", "Historial de Documentos", null, null, null, null, null}
                    }}
                }});
                //            moduleList.add(new Object[]{EscPersonalFacadeLocal.Perm.ACCESS_PERSONAL, new Tree("Ocper", "", "/admin/ocper", "/resources/images/m_personal.png"), new Object[][]{
//                {null, "Operacion", null, null, null, null, new Object[][]{
//                    {"/faces/ocper/pdt.xhtml", "Interface PDT", null, null, null, null, null},
//                    {"/faces/escalafon/personal/List.xhtml", "Personal", null, null, null, null, null}
//                }},
//                {null, "Reportes", null, null, null, null, new Object[][]{
//                    {"/faces/ocper/report/constanciaQuinta.xhtml", "Constancia de Quinta Categoria", null, null, null, null, null},
//                    {"/faces/ocper/report/hd.xhtml", "Constancia de Haberes y Descuentos", null, null, null, null, null},
//                    {"/faces/ocper/report/hcc.xhtml", "Hoja de Control Contable", null, null, null, null, null}
//                }}
//            }});
                moduleList.add(new Object[]{Perm.DIRECCION, new MenuFacade.Tree("Alta Dirección", "", "/admin/direccion", "/resources/images/m_direccion.png"), new Object[][]{
                    {null, "Bandejas", null, null, null, null, new Object[][]{
                        {"/faces/direccion/requerimiento/List.xhtml", "Bandeja de Requerimientos", null, null, null, null, null},
                        {"/faces/direccion/orden/List.xhtml", "Bandeja de Ordenes", null, null, null, null, null},
                        {"-"},
                        {"/faces/direccion/planilla/List.xhtml", "Bandeja de Planillas", null, null, null, null, null},
                        {"/faces/direccion/htareo/List.xhtml", "Bandeja de Hoja de Tareo", null, null, null, null, null},
                        {"/faces/direccion/papeleta/List.xhtml", "Bandeja de Papeletas", null, null, null, null, null},
                        {"-"},
                        {"/faces/direccion/papeleta/List.xhtml", "Movimiento de Ingreso Almacen", null, null, null, null, null},
                        {"/faces/direccion/papeleta/List.xhtml", "Movimiento de Salida Almacen", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/direccion/catalogo/List.xhtml", "Catalogo de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/direccion/catalogo/List.xhtml", "Registro de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/direccion/meta/List.xhtml", "Metas", null, null, null, null, null},
                        {"/faces/direccion/maestroDocumento/List.xhtml", "Maestro de Documentos", null, null, null, null, null},
                        {"-"},
                        {"/faces/direccion/meta/List.xhtml", "Metas", null, null, null, null, null}
                    }},
                    {null, "Reportes", null, null, null, null, new Object[][]{
                        {"/faces/oceda/estudiante/List.xhtml", "Reporte de Ordenes de C/S por Meta", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Reporte General por Meta agrupada por Fuente Financ.", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Reporte de Totales por Meta", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Reporte de Ordenes por Proveedor", null, null, null, null, null},
                        {null, "Personal", null, null, null, null, new Object[][]{
                            {"w_reporte_resumen_tardanzas", "Reporte Mensual Tardanza Administrativos", null, null, null, null, null},
                            {"/faces/direccion/report/marcacionDiaria.xhtml", "Reporte de Asistencia Diaria", null, null, null, null, null},
                            {"/admin/direccion/report/marcacionMensualTrabajador.xhtml", "Marcacion Mensual por Trabajador", null, null, null, null, null}
                        }},
                        {"-"},
                        {"/admin/presupuesto/report/calendarioInicial.xhtml", "Calendario Mensual Detallado Inicial", null, null, null, null, null},
                        {"/admin/presupuesto/report/calendarioAmpliacion.xhtml", "w_rpte_calendario_ampliacion", null, null, null, null, null}
                    }}
                }});
                moduleList.add(new Object[]{Perm.PRESUPUESTO, new MenuFacade.Tree("Presupuesto", "", "/admin/presupuesto", "/resources/images/m_presupuesto.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/admin/presupuesto/calendario/List.xhtml", "Calendario: Programación Mensual de Gasto", null, null, null, null, null},
                        {"/admin/presupuesto/requerimiento/List.xhtml", "Requerimientos", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/admin/presupuesto/meta/List.xhtml", "Metas Presupuestales", null, null, null, null, null},
                        {"-"},
                        {"/faces/presupuesto/funcion/CadenaFuncional.xhtml", "Cadena Funcional", null, null, null, null, null},
                        {"/faces/presupuesto/gastos/Clasificador.xhtml", "Clasificador de Gastos", null, null, null, null, null},
                        {"-"},
                        {"/admin/presupuesto/fuenteFinanciamiento/List.xhtml", "Fuente de Financiamiento", null, null, null, null, null}
                    }},
                    {null, "Reportes", null, null, null, null, new Object[][]{
                        {"/admin/presupuesto/report/calendarioResumen.xhtml", "Calendario Mensual Resumido", null, null, null, null, null},
                        {"-"},
                        {"/admin/presupuesto/report/calendarioInicial.xhtml", "Calendario Mensual Detallado Inicial", null, null, null, null, null},
                        {"/admin/presupuesto/report/calendarioAmpliacion.xhtml", "w_rpte_calendario_ampliacion", null, null, null, null, null}
                    }},
                    {null, "Herramientas", null, null, null, null, new Object[][]{
                        {"/admin/operacion/w_interface_simi", "Interface SIMI", null, null, null, null, null},
                        {"/admin/operacion/w_registro_cuenta_bienes", "Actualización de Cuentas de Bienes", null, null, null, null, null}
                    }}
                }});
                moduleList.add(new Object[]{Perm.CONFIGURACION, new MenuFacade.Tree("Configuracion", "", "/admin/configuracion", "/resources/images/configuration.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/configuracion/configuracion/List.xhtml", "Configuracion del Sistema", null, null, null, null, null},
                        {"/faces/configuracion/Contact.xhtml", "Plantillas de correo", null, null, null, null, null},
                        {"-"},
                        {"/admin/configuracion/user/List.xhtml", "Usuarios", null, null, null, null, null},
                        {"/faces/configuracion/role/List.xhtml", "Roles", null, null, null, null, null},
                        {"-"},
                        {"/faces/configuracion/db/List.xhtml", "Fuentes de Datos", null, null, null, null, null},
                        {"/faces/configuracion/requerimiento/List.xhtml", "Requerimientos", null, null, null, null, null},
                        {"/faces/oceda/estudiante/List.xhtml", "PECOSA", null, null, null, null, null},
                        {"/faces/oceda/estudiante/List.xhtml", "Migrar Personal", null, null, null, null, null},
                        {"/faces/oceda/estudiante/List.xhtml", "SQUID", null, null, null, null, null}

                    }},
                    {null, "Contenidos", null, null, null, null, new Object[][]{
                        {"/faces/configuracion/node/List.xhtml", "Publicaciones", null, null, null, null, null},
                        {"/faces/configuracion/block/List.xhtml", "Bloques", null, null, null, null, null},
                        {"/faces/configuracion/file/List.xhtml", "Archivos", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/configuracion/parametro/List.xhtml", "Parametros", null, null, null, null, null},
                        {"/faces/oceda/estudiante/List.xhtml", "HCC", null, null, null, null, null},
                        {"/faces/oceda/estudiante/List.xhtml", "Actualizar Orden", null, null, null, null, null},
                        {"/faces/configuracion/Siaf.xhtml", "Sincronización SIAF", null, null, null, null, null}
                    }}
                }});
                
                moduleList.add(new Object[]{Perm.FORMULACION, new MenuFacade.Tree("Formulación", "", "/admin/formulacion", "/resources/images/m_almacen.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/formulacion/proyecto/List.xhtml", "Proyectos", null, null, null, null, null},
                        {"/faces/formulacion/htareo/List.xhtml", "Hoja de Tareos", null, null, null, null, null}
                    }},
                    {null, "SEACE", null, null, null, null, new Object[][]{
                        {"/faces/formulacion/requerimiento/List.xhtml", "Requerimientos", null, null, null, null, null},
                        {"/faces/formulacion/htareo/List.xhtml", "Hoja de Tareos", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/formulacion/catalogo/List.xhtml", "Unidades de Medida", null, null, null, null, null},
                        {"/faces/formulacion/meta/List.xhtml", "Topes de Proceso de Selección", null, null, null, null, null}
                    }}
                }
                });
                moduleList.add(new Object[]{Perm.OPERACION, new MenuFacade.Tree("Operaciones", "", "/admin/operacion", "/resources/images/m_requerimiento.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/operacion/requerimiento/List.xhtml", "Solicitud de Requerimientos", null, null, null, null, null},
                        {"/faces/operacion/htareo/List.xhtml", "Hojas de Tareo", null, null, null, null, null}
                    }},
                    {null, "SEACE", null, null, null, null, new Object[][]{
                        {"/faces/operacion/requerimientoSeace/List.xhtml", "Requerimientos", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/operacion/unidadMef/List.xhtml", "Unidades de Medida", null, null, null, null, null},
                        {"/faces/operacion/meta/List.xhtml", "Metas", null, null, null, null, null}
                    }},
                    {null, "Consultas", null, null, null, null, new Object[][]{
                        {"/faces/operacion/catalogo/List.xhtml", "Catálogo de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/operacion/topeProcesoSeleccion/List.xhtml", "Topes de Proceso de Selección", null, null, null, null, null}
                    }}
                }});
                moduleList.add(new Object[]{Perm.PROGRAMACION, new MenuFacade.Tree("Programación", "", "/admin/programacion", "/resources/images/gantt.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/admin/programacion/orden/List.xhtml", "Solicitudes de Valorización", null, null, null, null, null},
                        {"/faces/programacion/htareo/List.xhtml", "Hoja de Tareo", null, null, null, null, null}
                    }},
                    {null, "SEACE", null, null, null, null, new Object[][]{
                        {"/faces/oceda/estudiante/List.xhtml", "Bandeja de Requerimientos", null, null, null, null, null},
                        {"-"},
                        {"/faces/programacion/meta/List.xhtml", "Busqueda de Requerimientos", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Busqueda PAAC", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Busqueda Proceso de Selección", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/programacion/meta/List.xhtml", "Metas Presupuestales", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Metas", null, null, null, null, null}
                    }},
                    {null, "Consultas", null, null, null, null, new Object[][]{
                        {"/faces/programacion/catalogo/List.xhtml", "Catálogo de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/programacion/meta/List.xhtml", "Topes de Proceso de Selección", null, null, null, null, null}
                    }}
                }});

                moduleList.add(new Object[]{Perm.ABASTECIMIENTO, new MenuFacade.Tree("Abastecimiento", "", "/admin/abastecimiento", "/resources/images/m_judiciales.png"), new Object[][]{
                    //url,title,icon,perm,load,flag
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/abastecimiento/proveedor/List.xhtml", "Registro de Proveedores", null, null, null, '-', null},
                        //usuario.idCargo IN (6, 17, 12)
                        {"-"},
                        {"/faces/abastecimiento/requerimiento/List.xhtml", "Autorización de requerimiento", null, null, null, '-', null},
                        //
                        {"-"},
                        {"/faces/abastecimiento/orden/List.xhtml", "Generar Ordenes", null, null, null, '-', null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/faces/abastecimiento/catalogo/List.xhtml", "Catalogo de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/abastecimiento/catalogo/List.xhtml", "Registro de Bienes y Servicios", null, null, null, null, null},
                        {"/faces/abastecimiento/maestroDocumento/List.xhtml", "Maestro de Documentos", null, null, null, null, null},
                        {"/faces/abastecimiento/meta/List.xhtml", "Metas", null, null, null, '-', null}
                    }},
                    {null, "Reportes", null, null, null, null, new Object[][]{
                        {"/faces/abastecimiento/report/ordenMeta.xhtml", "Ordenes de C/S por Meta", null, null, null, null, null},
                        {"/faces/abastecimiento/report/generalMeta.xhtml", "General por Meta agrupada por Fuente Financ", null, null, null, null, null},
                        {"/faces/abastecimiento/report/totalOrden.xhtml", "Totales por Meta", null, null, null, null, null},
                        {"/faces/abastecimiento/report/ordenProveedor.xhtml", "Ordenes por Proveedor", null, null, null, null, null}
                    }}
                }});
               
                moduleList.add(new Object[]{Perm.SEGURIDAD, new MenuFacade.Tree("Seguridad de Informacion", "", "/admin/sgsi", "/resources/images/ITSecurity.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/faces/sgsi/activo/List.xhtml", "Inventario de Activos", null, null, null, null, null},
                        {"/faces/sgsi/riesgo/List.xhtml", "Riesgos", null, null, null, null, null},
                        {"/faces/sgsi/evaluacion/List.xhtml", "Evaluacion", null, null, null, null, null},
                        {"/faces/sgsi/tratamiento/List.xhtml", "Tratamiento", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/admin/sgsi/proceso/List.xhtml", "Procesos", null, null, null, null, null},
                        {"/faces/sgsi/tipoActivo/List.xhtml", "Tipos de Activos", null, null, null, null, null},
                        {"/faces/sgsi/probabilidad/List.xhtml", "Probabilidad", null, null, null, null, null},
                        {"/faces/sgsi/impacto/List.xhtml", "Impacto", null, null, null, null, null}
                    }}
                }});
                moduleList.add(new Object[]{Perm.CONTABILIDAD, new MenuFacade.Tree("Contabilidad", "", "/admin/contabilidad", "/resources/images/m_tramite_documentario.png"), new Object[][]{
                    {null, "Operacion", null, null, null, null, new Object[][]{
                        {"/admin/contabilidad/w_resume_planilla.xhtml", "Consolidado de Planillas", null, null, null, null, null}
                    }},
                    {null, "Tablas", null, null, null, null, new Object[][]{
                        {"/admin/contabilidad/concepto/List.xhtml", "Conceptos", null, null, null, null, null},
                        {"/faces/sgsi/impacto/List.xhtml", "Impacto", null, null, null, null, null}
                    }}
                }});
            }
        });
*/
    }

}
