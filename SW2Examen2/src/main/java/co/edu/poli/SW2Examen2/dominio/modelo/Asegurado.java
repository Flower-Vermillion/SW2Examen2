package co.edu.poli.SW2Examen2.dominio.modelo;

/**
 * Entidad de dominio que representa a la persona asegurada.
 * <p>
 * Un {@link Seguro} pertenece siempre a un asegurado, que se identifica de
 * forma única por su número de identificación.
 * </p>
 */
public class Asegurado {

    /** Número de identificación único del asegurado. */
    private String identificacion;

    /** Nombre completo del asegurado. */
    private String nombre;

    /** Correo electrónico de contacto del asegurado. */
    private String correoElectronico;

    /**
     * Crea un asegurado con todos sus datos.
     *
     * @param identificacion    número de identificación único
     * @param nombre            nombre completo
     * @param correoElectronico correo electrónico de contacto
     */
    public Asegurado(String identificacion, String nombre, String correoElectronico) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }

    /** @return número de identificación del asegurado */
    public String getIdentificacion() { return identificacion; }

    /** @param identificacion nuevo número de identificación */
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    /** @return nombre completo del asegurado */
    public String getNombre() { return nombre; }

    /** @param nombre nuevo nombre completo */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return correo electrónico del asegurado */
    public String getCorreoElectronico() { return correoElectronico; }

    /** @param correoElectronico nuevo correo electrónico */
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    /**
     * Texto con el que se muestra al asegurado en la interfaz (por ejemplo,
     * en el combo del formulario).
     *
     * @return el nombre seguido de la identificación entre paréntesis
     */
    @Override
    public String toString() {
        return nombre + " (" + identificacion + ")";
    }
}