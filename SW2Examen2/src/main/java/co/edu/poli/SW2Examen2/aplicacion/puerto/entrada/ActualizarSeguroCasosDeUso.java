package co.edu.poli.SW2Examen2.aplicacion.puerto.entrada;

import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;

/**
 * Puerto de entrada — caso de uso: modificar un seguro existente.
 * Define el contrato que el adaptador de entrada (UI) usa para
 * invocar la lógica de aplicación sin conocer su implementación.
 */
public interface ActualizarSeguroCasosDeUso {

    /**
     * Actualiza los datos de un seguro existente.
     *
     * @param seguro seguro con los datos actualizados
     * @throws Exception si el seguro no existe o ocurre un error al actualizar
     */
    void actualizar(Seguro seguro) throws Exception;
}