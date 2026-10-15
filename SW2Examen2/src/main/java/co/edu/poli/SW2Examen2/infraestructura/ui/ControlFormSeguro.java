package co.edu.poli.SW2Examen2.infraestructura.ui;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.ActualizarSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.ConsultarTodosSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.ConsultarUnoSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.CrearSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.EliminarSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.dominio.modelo.Asegurado;
import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;
import co.edu.poli.SW2Examen2.dominio.modelo.SeguroVehiculo;
import co.edu.poli.SW2Examen2.dominio.modelo.SeguroVida;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * Adaptador de entrada — controlador del formulario {@code FormSeguro.fxml}.
 * <p>
 * Solo depende de los puertos de entrada (interfaces {@code ...CasosDeUso});
 * no conoce MySQL ni los servicios concretos. Los casos de uso y la lista de
 * asegurados se reciben por constructor, por eso {@code App} lo asigna con
 * {@code FXMLLoader.setController(...)}.
 * </p>
 */
public class ControlFormSeguro {

    /** Texto del combo de tipo que identifica al seguro de vida. */
    private static final String VIDA = "Vida";

    /** Texto del combo de tipo que identifica al seguro de vehículo. */
    private static final String VEHICULO = "Vehículo";

    /** Caso de uso para crear seguros. */
    private final CrearSeguroCasosDeUso crearSeguro;

    /** Caso de uso para consultar un seguro por su número. */
    private final ConsultarUnoSeguroCasosDeUso consultarUnoSeguro;

    /** Caso de uso para listar todos los seguros. */
    private final ConsultarTodosSeguroCasosDeUso consultarTodosSeguro;

    /** Caso de uso para actualizar seguros. */
    private final ActualizarSeguroCasosDeUso actualizarSeguro;

    /** Caso de uso para eliminar seguros. */
    private final EliminarSeguroCasosDeUso eliminarSeguro;

    /** Asegurados disponibles en el combo del formulario. */
    private final List<Asegurado> asegurados;

    // ---- componentes del FXML (los nombres deben coincidir con los fx:id) ----

    /** Combo para elegir el tipo de seguro (Vida o Vehículo). */
    @FXML private ComboBox<String> cmbTipo;

    /** Campo del número del seguro (llave). */
    @FXML private TextField txtNumero;

    /** Campo de la fecha de expedición ({@code yyyy-MM-dd}). */
    @FXML private TextField txtFecha;

    /** Campo del valor asegurado. */
    @FXML private TextField txtValor;

    /** Casilla del estado: marcada si el seguro está activo. */
    @FXML private CheckBox chkEstado;

    /** Combo para elegir el asegurado. */
    @FXML private ComboBox<Asegurado> cmbAsegurado;

    /** Campo del beneficiario (solo seguro de vida). */
    @FXML private TextField txtBeneficiario;

    /** Campo del parentesco del beneficiario (solo seguro de vida). */
    @FXML private TextField txtParentesco;

    /** Campo de la marca del vehículo (solo seguro de vehículo). */
    @FXML private TextField txtMarca;

    /** Etiqueta del campo beneficiario. */
    @FXML private Label lblBeneficiario;

    /** Etiqueta del campo parentesco. */
    @FXML private Label lblParentesco;

    /** Etiqueta del campo marca. */
    @FXML private Label lblMarca;

    /** Tabla con el listado de seguros. */
    @FXML private TableView<Seguro> tabla;

    /** Columna del número del seguro. */
    @FXML private TableColumn<Seguro, String> colNumero;

    /** Columna del tipo de seguro. */
    @FXML private TableColumn<Seguro, String> colTipo;

    /** Columna de la fecha de expedición. */
    @FXML private TableColumn<Seguro, String> colFecha;

    /** Columna del valor asegurado. */
    @FXML private TableColumn<Seguro, String> colValor;

    /** Columna del estado (Activo / Inactivo). */
    @FXML private TableColumn<Seguro, String> colEstado;

    /** Columna del nombre del asegurado. */
    @FXML private TableColumn<Seguro, String> colAsegurado;

    /** Columna con el detalle propio del tipo (beneficiario o marca). */
    @FXML private TableColumn<Seguro, String> colDetalle;

    /**
     * Crea el controlador recibiendo los casos de uso por sus puertos de entrada.
     *
     * @param crearSeguro          caso de uso para crear seguros
     * @param consultarUnoSeguro   caso de uso para consultar un seguro por número
     * @param consultarTodosSeguro caso de uso para listar todos los seguros
     * @param actualizarSeguro     caso de uso para actualizar seguros
     * @param eliminarSeguro       caso de uso para eliminar seguros
     * @param asegurados lista predeterminada de asegurados (deben existir en la tabla {@code asegurado})
     */
    public ControlFormSeguro(CrearSeguroCasosDeUso crearSeguro,
                             ConsultarUnoSeguroCasosDeUso consultarUnoSeguro,
                             ConsultarTodosSeguroCasosDeUso consultarTodosSeguro,
                             ActualizarSeguroCasosDeUso actualizarSeguro,
                             EliminarSeguroCasosDeUso eliminarSeguro,
                             List<Asegurado> asegurados) {
        this.crearSeguro = crearSeguro;
        this.consultarUnoSeguro = consultarUnoSeguro;
        this.consultarTodosSeguro = consultarTodosSeguro;
        this.actualizarSeguro = actualizarSeguro;
        this.eliminarSeguro = eliminarSeguro;
        this.asegurados = asegurados;
    }

    /**
     * JavaFX la invoca automáticamente cuando el FXML termina de cargarse.
     * <p>
     * Llena los combos, configura las columnas de la tabla, registra el
     * listener que carga en el formulario el seguro seleccionado y deja el
     * formulario limpio con la tabla actualizada.
     * </p>
     */
    @FXML
    private void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(VIDA, VEHICULO));
        cmbAsegurado.setItems(FXCollections.observableArrayList(asegurados));

        configurar(colNumero, s -> String.valueOf(s.getNumero()));
        configurar(colTipo, Seguro::getTipo);
        configurar(colFecha, Seguro::getFechaExpedicion);
        configurar(colValor, s -> String.format("%,.2f", s.getValor()));
        configurar(colEstado, s -> s.isEstado() ? "Activo" : "Inactivo");
        configurar(colAsegurado, s -> s.getAsegurado().getNombre());
        configurar(colDetalle, this::detalle);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                cargarFormulario(actual);
            }
        });

        limpiar();
        refrescarTabla();
    }

    // --------------------------------------------------------------- acciones

    /**
     * Acción del combo de tipo: muestra solo los campos del tipo elegido.
     */
    @FXML
    private void onTipoSeleccionado() {
        actualizarCamposPorTipo();
    }

    /**
     * Acción del botón <b>Crear</b>: lee el formulario, crea el seguro, informa
     * el resultado, refresca la tabla y limpia el formulario. Si ocurre un
     * error, lo muestra en una alerta.
     */
    @FXML
    private void onCrear() {
        try {
            Seguro seguro = leerFormulario();
            crearSeguro.crear(seguro);
            info("Seguro " + seguro.getNumero() + " creado correctamente.");
            refrescarTabla();
            limpiar();
        } catch (Exception e) {
            error(mensaje(e));
        }
    }

    /**
     * Acción del botón <b>Consultar por número</b>: pide el número en un
     * diálogo, busca el seguro, lo carga en el formulario y lo selecciona en la
     * tabla. Informa si no existe o si el número no es un entero.
     */
    @FXML
    private void onConsultar() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Consultar seguro");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Número del seguro:");
        Optional<String> respuesta = dialogo.showAndWait();
        if (respuesta.isEmpty()) {
            return;
        }
        try {
            int numero = Integer.parseInt(respuesta.get().trim());
            // Si tu interfaz ConsultarUnoSeguroCasosDeUso usa otro nombre de método, cámbialo aquí.
            Seguro seguro = consultarUnoSeguro.consultarUno(numero);
            if (seguro == null) {
                info("No existe un seguro con el número " + numero + ".");
                return;
            }
            tabla.getSelectionModel().clearSelection();
            cargarFormulario(seguro);
            tabla.getItems().stream()
                    .filter(s -> s.getNumero() == numero)
                    .findFirst()
                    .ifPresent(s -> tabla.getSelectionModel().select(s));
        } catch (NumberFormatException e) {
            error("El número debe ser un entero.");
        } catch (Exception e) {
            error(mensaje(e));
        }
    }

    /**
     * Acción del botón <b>Actualizar</b>: lee el formulario, actualiza el
     * seguro, informa el resultado, refresca la tabla y limpia el formulario.
     * Si ocurre un error, lo muestra en una alerta.
     */
    @FXML
    private void onActualizar() {
        try {
            Seguro seguro = leerFormulario();
            actualizarSeguro.actualizar(seguro);
            info("Seguro " + seguro.getNumero() + " actualizado correctamente.");
            refrescarTabla();
            limpiar();
        } catch (Exception e) {
            error(mensaje(e));
        }
    }

    /**
     * Acción del botón <b>Eliminar</b>: toma el número del formulario, pide
     * confirmación y elimina el seguro. Luego refresca la tabla y limpia el
     * formulario. Si el número no es válido o falla la eliminación, informa el
     * error.
     */
    @FXML
    private void onEliminar() {
        int numero;
        try {
            numero = Integer.parseInt(txtNumero.getText().trim());
        } catch (NumberFormatException e) {
            error("Seleccione un seguro de la tabla o escriba su número.");
            return;
        }
        Alert confirmacion = new Alert(AlertType.CONFIRMATION,
                "¿Eliminar el seguro " + numero + "?", ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText(null);
        if (confirmacion.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            eliminarSeguro.eliminar(numero);
            info("Seguro " + numero + " eliminado.");
            refrescarTabla();
            limpiar();
        } catch (Exception e) {
            error(mensaje(e));
        }
    }

    /**
     * Acción del botón <b>Limpiar</b>: deja el formulario en su estado inicial.
     */
    @FXML
    private void onLimpiar() {
        limpiar();
    }

    // ------------------------------------------------------------- utilidades

    /**
     * Configura una columna de la tabla para que muestre un texto calculado a
     * partir del seguro de cada fila.
     *
     * @param columna   columna a configurar
     * @param extractor función que obtiene el texto a mostrar desde el seguro
     */
    private void configurar(TableColumn<Seguro, String> columna, Function<Seguro, String> extractor) {
        columna.setCellValueFactory(c -> new ReadOnlyStringWrapper(extractor.apply(c.getValue())));
    }

    /**
     * Construye el texto de la columna de detalle según el tipo de seguro:
     * beneficiario y parentesco para vida, marca para vehículo.
     *
     * @param s seguro del que se obtiene el detalle
     * @return el texto del detalle, o cadena vacía si el tipo no se reconoce
     */
    private String detalle(Seguro s) {
        if (s instanceof SeguroVida) {
            SeguroVida v = (SeguroVida) s;
            return v.getNombreBeneficiario() + " (" + v.getParentesco() + ")";
        }
        if (s instanceof SeguroVehiculo) {
            return "Marca: " + ((SeguroVehiculo) s).getMarca();
        }
        return "";
    }

    /**
     * Muestra u oculta una etiqueta y su campo de texto. Al ocultarlos
     * también los quita del layout para no dejar espacios vacíos.
     *
     * @param etiqueta etiqueta del campo
     * @param campo    campo de texto
     * @param visible  {@code true} para mostrarlos, {@code false} para ocultarlos
     */
    private void mostrarCampo(Label etiqueta, TextField campo, boolean visible) {
        etiqueta.setVisible(visible);
        etiqueta.setManaged(visible);
        campo.setVisible(visible);
        campo.setManaged(visible);
    }

    /** Muestra solo los campos propios del tipo de seguro elegido. */
    private void actualizarCamposPorTipo() {
        String tipo = cmbTipo.getValue();
        mostrarCampo(lblBeneficiario, txtBeneficiario, VIDA.equals(tipo));
        mostrarCampo(lblParentesco, txtParentesco, VIDA.equals(tipo));
        mostrarCampo(lblMarca, txtMarca, VEHICULO.equals(tipo));
    }

    /**
     * Construye el seguro (vida o vehículo) con los datos del formulario.
     *
     * @return el seguro construido según el tipo elegido
     * @throws IllegalArgumentException si no se eligió el tipo, o si el número
     *                                  o el valor no son numéricos
     */
    private Seguro leerFormulario() {
        String tipo = cmbTipo.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Seleccione el tipo de seguro.");
        }
        int numero;
        double valor;
        try {
            numero = Integer.parseInt(txtNumero.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El número debe ser un entero.");
        }
        try {
            valor = Double.parseDouble(txtValor.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor debe ser numérico.");
        }
        String fecha = txtFecha.getText().trim();
        boolean estado = chkEstado.isSelected();
        Asegurado asegurado = cmbAsegurado.getValue();

        if (VIDA.equals(tipo)) {
            return new SeguroVida(numero, fecha, valor, estado, asegurado,
                    txtBeneficiario.getText().trim(), txtParentesco.getText().trim());
        }
        return new SeguroVehiculo(numero, fecha, valor, estado, asegurado, txtMarca.getText().trim());
    }

    /**
     * Carga en el formulario los datos de un seguro existente. Bloquea el
     * número y el tipo, que no se modifican en un seguro existente.
     *
     * @param s seguro a mostrar en el formulario
     */
    private void cargarFormulario(Seguro s) {
        cmbTipo.setValue(s.getTipo());
        txtNumero.setText(String.valueOf(s.getNumero()));
        txtFecha.setText(s.getFechaExpedicion());
        txtValor.setText(String.valueOf(s.getValor()));
        chkEstado.setSelected(s.isEstado());
        cmbAsegurado.getItems().stream()
                .filter(a -> a.getIdentificacion().equals(s.getAsegurado().getIdentificacion()))
                .findFirst()
                .ifPresent(cmbAsegurado::setValue);

        if (s instanceof SeguroVida) {
            SeguroVida v = (SeguroVida) s;
            txtBeneficiario.setText(v.getNombreBeneficiario());
            txtParentesco.setText(v.getParentesco());
            txtMarca.clear();
        } else if (s instanceof SeguroVehiculo) {
            txtMarca.setText(((SeguroVehiculo) s).getMarca());
            txtBeneficiario.clear();
            txtParentesco.clear();
        }

        // El número (llave) y el tipo no se modifican en un seguro existente.
        txtNumero.setDisable(true);
        cmbTipo.setDisable(true);
        actualizarCamposPorTipo();
    }

    /**
     * Deja el formulario en su estado inicial: borra los campos, quita la
     * selección de la tabla, marca el estado como activo y habilita el número
     * y el tipo.
     */
    private void limpiar() {
        tabla.getSelectionModel().clearSelection();
        cmbTipo.setValue(null);
        txtNumero.clear();
        txtFecha.clear();
        txtValor.clear();
        chkEstado.setSelected(true);
        cmbAsegurado.setValue(null);
        txtBeneficiario.clear();
        txtParentesco.clear();
        txtMarca.clear();
        txtNumero.setDisable(false);
        cmbTipo.setDisable(false);
        actualizarCamposPorTipo();
    }

    /**
     * Vuelve a cargar la tabla con todos los seguros registrados. Si la
     * consulta falla, muestra el error en una alerta.
     */
    private void refrescarTabla() {
        try {
            // Si tu interfaz ConsultarTodosSeguroCasosDeUso usa otro nombre de método, cámbialo aquí.
            tabla.setItems(FXCollections.observableArrayList(consultarTodosSeguro.consultarTodos()));
        } catch (Exception e) {
            error(mensaje(e));
        }
    }

    /**
     * Obtiene el texto a mostrar de una excepción.
     *
     * @param e excepción a describir
     * @return el mensaje de la excepción, o su representación en texto si no tiene mensaje
     */
    private String mensaje(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.toString();
    }

    /**
     * Muestra una alerta informativa y espera a que el usuario la cierre.
     *
     * @param texto mensaje a mostrar
     */
    private void info(String texto) {
        Alert alerta = new Alert(AlertType.INFORMATION, texto);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de error y espera a que el usuario la cierre.
     *
     * @param texto mensaje de error a mostrar
     */
    private void error(String texto) {
        Alert alerta = new Alert(AlertType.ERROR, texto);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}