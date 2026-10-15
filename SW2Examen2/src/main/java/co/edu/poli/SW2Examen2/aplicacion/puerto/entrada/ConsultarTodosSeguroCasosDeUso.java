package co.edu.poli.SW2Examen2.aplicacion.puerto.entrada;

import java.util.List;

import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;



/**
 * Puerto de entrada — caso de uso: listar todos los seguros.
 * Define el contrato que el adaptador de entrada (UI) usa para
 * invocar la lógica de aplicación sin conocer su implementación.
 */
public interface ConsultarTodosSeguroCasosDeUso {

    /**
     * Retorna todos los seguros registrados.
     *
     * @return lista de seguros, vacía si no hay registros
     * @throws Exception si ocurre un error durante la consulta
     */
    List<Seguro> consultarTodos() throws Exception;
}