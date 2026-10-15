package co.edu.poli.SW2Examen2.aplicacion.puerto.entrada;

import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;

/**
 * Puerto de entrada — caso de uso: crear un seguro.
 * Define el contrato que el adaptador de entrada (UI) usa para
 * invocar la lógica de aplicación sin conocer su implementación.
 */

public interface CrearSeguroCasosDeUso {

    /**
     * Registra un nuevo seguro.
     *
     * @param seguro seguro a registrar
     * @throws Exception si ocurre un error durante el registro
     */
    void crear(Seguro seguro) throws Exception;
}