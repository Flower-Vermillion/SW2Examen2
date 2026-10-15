package co.edu.poli.SW2Examen2.aplicacion.puerto.entrada;


/**
 * Puerto de entrada — caso de uso: eliminar un seguro.
 * Define el contrato que el adaptador de entrada (UI) usa para
 * invocar la lógica de aplicación sin conocer su implementación.
 */

public interface EliminarSeguroCasosDeUso {

    /**
     * Elimina el seguro con el numero dado.
     *
     * @param numero identificador del seguro a eliminar
     * @throws Exception si el seguro no existe o ocurre un error al eliminar
     */
    void eliminar(int numero) throws Exception;
}