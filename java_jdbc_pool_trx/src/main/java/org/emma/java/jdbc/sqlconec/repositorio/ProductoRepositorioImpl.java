package org.emma.java.jdbc.sqlconec.repositorio;

import org.emma.java.jdbc.sqlconec.modelo.Categoria;
import org.emma.java.jdbc.sqlconec.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImpl implements Repositorio<Producto> {

    private Connection conn;

    public ProductoRepositorioImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT p.*, c.nombre as 'categoria' FROM productos as p " + "inner join categorias as c ON (p.categoria_id = c.id)")) {
            while (rs.next()) {
                Producto p = crearProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }

    @Override
    public Producto buscarPorID(Long id) throws SQLException {
        Producto producto = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT p.*, c.nombre as 'categoria' FROM productos as p " + "inner join categorias as c ON (p.categoria_id = c.id) WHERE p.id=?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = crearProducto(rs);
                }
            }
        }
        return producto;
    }

    @Override
    public Producto guardar(Producto producto) throws SQLException {
        String query;
        if (producto.getId() != null && producto.getId() > 0) {
            query = "UPDATE productos SET nombre=?, precio=?, categoria_id=?, sku=? WHERE id=?";
        } else {
            query = "INSERT INTO productos(nombre, precio, categoria_id, sku, fecha_registro) VALUES(?,?,?,?,?)";
        }
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setLong(2, producto.getPrecio());
            stmt.setLong(3, producto.getCategoria().getId());
            stmt.setString(4, producto.getSku());
            if (producto.getId() != null && producto.getId() > 0) {
                stmt.setLong(5, producto.getId());
            } else {
                stmt.setDate(5, new Date(producto.getFechaRegistro().getTime()));
            }

            stmt.executeUpdate();
            if (producto.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getLong(1));
                    }
                }
            }

            return producto;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM productos WHERE id = ? ")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            resetIndices();
        }
    }

    private void resetIndices() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE productos AUTO_INCREMENT = 1");
        }
    }

    private static Producto crearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getInt("precio"));
        p.setFechaRegistro(rs.getDate("fecha_registro"));
        p.setSku(rs.getString("sku"));
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("categoria_id"));
        categoria.setNombre(rs.getString("categoria"));
        p.setCategoria(categoria);
        return p;
    }
}
