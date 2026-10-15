package co.edu.poli.SW2Examen2.aplicacion.puerto.salida;

import java.util.List;

import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;



/**
 * Puerto de salida — repositorio de seguros.
 * Define el contrato que la capa de aplicación usa para persistir y
 * consultar seguros, sin conocer la tecnología de almacenamiento
 * (MySQL, memoria, etc.). El adaptador de salida lo implementa.
 */
public interface SeguroRepository {

    /**
     * Persiste un nuevo seguro.
     *
     * @param seguro seguro a guardar (concreto: de vida o vehiculo)
     * @throws Exception si ocurre un error al guardar o el numero ya existe
     */
    void guardar(Seguro seguro) throws Exception;

    /**
     * Busca un seguro por su identificador.
     *
     * @param numero identificador del seguro
     * @return seguro encontrado, o {@code null} si no existe
     * @throws Exception si ocurre un error durante la consulta
     */
    Seguro consultarPorNumero(int numero) throws Exception;

    /**
     * Retorna todos los seguros registrados.
     *
     * @return lista de seguros, vacía si no hay registros
     * @throws Exception si ocurre un error durante la consulta
     */
    List<Seguro> consultarTodos() throws Exception;

    /**
     * Actualiza los datos de un seguro existente.
     *
     * @param seguro seguro con los datos actualizados
     * @throws Exception si el seguro no existe o ocurre un error al actualizar
     */
    void actualizar(Seguro seguro) throws Exception;

    /**
     * Elimina el seguro con el identificador dado.
     *
     * @param numero identificador del seguro a eliminar
     * @throws Exception si el seguro no existe o ocurre un error al eliminar
     */
    void eliminar(int numero) throws Exception;
}