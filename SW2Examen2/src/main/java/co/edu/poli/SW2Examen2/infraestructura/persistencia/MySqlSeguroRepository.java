package co.edu.poli.SW2Examen2.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;
import co.edu.poli.SW2Examen2.dominio.modelo.Asegurado;
import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;
import co.edu.poli.SW2Examen2.dominio.modelo.SeguroVehiculo;
import co.edu.poli.SW2Examen2.dominio.modelo.SeguroVida;

/**
 * Adaptador de salida — implementa {@link SeguroRepository} sobre MySQL (JDBC).
 * <p>
 * Usa una tabla por subtipo: {@code seguro} (datos comunes),
 * {@code seguroVida} y {@code seguroVehiculo}, unidas por la columna
 * {@code numero}. El asegurado se enlaza con {@code identificacionAsegurado}.
 * </p>
 *
 * @see ConexionBD
 */
public class MySqlSeguroRepository implements SeguroRepository {

    /** Consulta base: une el seguro con su asegurado y con su subtipo. */
    private static final String SELECT_BASE = """
            SELECT s.numero, s.fechaExpedicion, s.valor, s.estado,
                   a.identificacion, a.nombre, a.correoElectronico,
                   v.numero AS vidaNumero, v.nombre_beneficiario, v.parentesco,
                   h.numero AS vehiculoNumero, h.marca
            FROM seguro s
            JOIN asegurado a ON a.identificacion = s.identificacionAsegurado
            LEFT JOIN seguroVida v ON v.numero = s.numero
            LEFT JOIN seguroVehiculo h ON h.numero = s.numero
            """;

    /** Conexión JDBC compartida obtenida de {@link ConexionBD}. */
    private final Connection conexion;

    /**
     * Crea el repositorio usando la conexión única de {@link ConexionBD}.
     */
    public MySqlSeguroRepository() {
        this.conexion = ConexionBD
                .getInstancia()
                .getConexion();
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException si falla la inserción en alguna de las tablas
     */
    @Override
    public void guardar(Seguro seguro) throws SQLException {

        String sqlSeguro = """
                INSERT INTO seguro
                (numero, fechaExpedicion, valor, estado, identificacionAsegurado)
                VALUES (?, ?, ?, ?, ?)
                """;

        boolean autoCommitPrevio = conexion.getAutoCommit();
        conexion.setAutoCommit(false);
        try {
            try (PreparedStatement ps = conexion.prepareStatement(sqlSeguro)) {
                ps.setInt(1, seguro.getNumero());
                ps.setString(2, seguro.getFechaExpedicion());
                ps.setDouble(3, seguro.getValor());
                ps.setBoolean(4, seguro.isEstado());
                ps.setString(5, seguro.getAsegurado().getIdentificacion());
                ps.executeUpdate();
            }

            if (seguro instanceof SeguroVida) {
                SeguroVida vida = (SeguroVida) seguro;
                String sql = """
                        INSERT INTO seguroVida (numero, nombre_beneficiario, parentesco)
                        VALUES (?, ?, ?)
                        """;
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    ps.setInt(1, vida.getNumero());
                    ps.setString(2, vida.getNombreBeneficiario());
                    ps.setString(3, vida.getParentesco());
                    ps.executeUpdate();
                }
            } else if (seguro instanceof SeguroVehiculo) {
                SeguroVehiculo vehiculo = (SeguroVehiculo) seguro;
                String sql = "INSERT INTO seguroVehiculo (numero, marca) VALUES (?, ?)";
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    ps.setInt(1, vehiculo.getNumero());
                    ps.setString(2, vehiculo.getMarca());
                    ps.executeUpdate();
                }
            }

            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(autoCommitPrevio);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException si falla la consulta
     */
    @Override
    public Seguro consultarPorNumero(int numero) throws SQLException {

        String sql = SELECT_BASE + "WHERE s.numero = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirSeguro(rs);
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException si falla la consulta
     */
    @Override
    public List<Seguro> consultarTodos() throws SQLException {

        List<Seguro> seguros = new ArrayList<>();

        String sql = SELECT_BASE + "ORDER BY s.numero";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                seguros.add(convertirSeguro(rs));
            }
        }

        return seguros;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException si falla la actualización o no existe un seguro
     *                      con el número indicado
     */
    @Override
    public void actualizar(Seguro seguro) throws SQLException {

        String sqlSeguro = """
                UPDATE seguro
                SET fechaExpedicion = ?,
                    valor = ?,
                    estado = ?,
                    identificacionAsegurado = ?
                WHERE numero = ?
                """;

        boolean autoCommitPrevio = conexion.getAutoCommit();
        conexion.setAutoCommit(false);
        try {
            try (PreparedStatement ps = conexion.prepareStatement(sqlSeguro)) {
                ps.setString(1, seguro.getFechaExpedicion());
                ps.setDouble(2, seguro.getValor());
                ps.setBoolean(3, seguro.isEstado());
                ps.setString(4, seguro.getAsegurado().getIdentificacion());
                ps.setInt(5, seguro.getNumero());

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("No existe un seguro con el número " + seguro.getNumero() + ".");
                }
            }

            if (seguro instanceof SeguroVida) {
                SeguroVida vida = (SeguroVida) seguro;
                String sql = """
                        UPDATE seguroVida
                        SET nombre_beneficiario = ?, parentesco = ?
                        WHERE numero = ?
                        """;
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    ps.setString(1, vida.getNombreBeneficiario());
                    ps.setString(2, vida.getParentesco());
                    ps.setInt(3, vida.getNumero());
                    ps.executeUpdate();
                }
            } else if (seguro instanceof SeguroVehiculo) {
                SeguroVehiculo vehiculo = (SeguroVehiculo) seguro;
                String sql = "UPDATE seguroVehiculo SET marca = ? WHERE numero = ?";
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    ps.setString(1, vehiculo.getMarca());
                    ps.setInt(2, vehiculo.getNumero());
                    ps.executeUpdate();
                }
            }

            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(autoCommitPrevio);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException si falla la eliminación o no existe un seguro
     *                      con el número indicado
     */
    @Override
    public void eliminar(int numero) throws SQLException {

        boolean autoCommitPrevio = conexion.getAutoCommit();
        conexion.setAutoCommit(false);
        try {
            // Primero las tablas de subtipo (por si no hay ON DELETE CASCADE).
            ejecutarBorrado("DELETE FROM seguroVida WHERE numero = ?", numero);
            ejecutarBorrado("DELETE FROM seguroVehiculo WHERE numero = ?", numero);

            if (ejecutarBorrado("DELETE FROM seguro WHERE numero = ?", numero) == 0) {
                throw new SQLException("No existe un seguro con el número " + numero + ".");
            }

            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(autoCommitPrevio);
        }
    }

    /**
     * Ejecuta una sentencia {@code DELETE} parametrizada por el número del seguro.
     *
     * @param sql    sentencia {@code DELETE} con un único parámetro {@code ?} para el número
     * @param numero número del seguro a borrar
     * @return cantidad de filas eliminadas
     * @throws SQLException si falla la ejecución de la sentencia
     */
    private int ejecutarBorrado(String sql, int numero) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numero);
            return ps.executeUpdate();
        }
    }

    /**
     * Construye el {@link Seguro} concreto (vida o vehículo) a partir de la
     * fila actual del {@link ResultSet}, según la tabla de subtipo que tenga datos.
     *
     * @param rs resultado posicionado en la fila a convertir
     * @return {@link SeguroVida} o {@link SeguroVehiculo} con los datos de la fila
     * @throws SQLException si falla la lectura o el seguro no tiene subtipo
     */
    private Seguro convertirSeguro(ResultSet rs) throws SQLException {

        Asegurado asegurado = new Asegurado(
                rs.getString("identificacion"),
                rs.getString("nombre"),
                rs.getString("correoElectronico"));

        int numero = rs.getInt("numero");
        String fecha = rs.getString("fechaExpedicion");
        double valor = rs.getDouble("valor");
        boolean estado = rs.getBoolean("estado");

        if (rs.getObject("vidaNumero") != null) {
            return new SeguroVida(numero, fecha, valor, estado, asegurado,
                    rs.getString("nombre_beneficiario"), rs.getString("parentesco"));
        }
        if (rs.getObject("vehiculoNumero") != null) {
            return new SeguroVehiculo(numero, fecha, valor, estado, asegurado,
                    rs.getString("marca"));
        }
        throw new SQLException("El seguro " + numero + " no tiene un subtipo registrado.");
    }
}