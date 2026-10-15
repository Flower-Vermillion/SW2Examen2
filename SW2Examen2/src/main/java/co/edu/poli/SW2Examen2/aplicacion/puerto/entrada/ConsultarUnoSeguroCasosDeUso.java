package co.edu.poli.SW2Examen2.aplicacion.puerto.entrada;

import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;

/**
 * Puerto de entrada — caso de uso: leer un seguro por su identificador.
 * Define el contrato que el adaptador de entrada (UI) usa para
 * invocar la lógica de aplicación sin conocer su implementación.
 */

public interface ConsultarUnoSeguroCasosDeUso {

    /**
     * Busca y retorna un seguro dado su identificador.
     *
     * @param numero identificador del seguro a buscar
     * @return seguro encontrado, o {@code null} si no existe
     * @throws Exception si ocurre un error durante la búsqueda
     */
    Seguro consultarUno(int numero) throws Exception;
}
