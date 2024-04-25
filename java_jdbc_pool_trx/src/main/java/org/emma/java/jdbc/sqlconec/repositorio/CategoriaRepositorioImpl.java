package org.emma.java.jdbc.sqlconec.repositorio;

import org.emma.java.jdbc.sqlconec.modelo.Categoria;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorioImpl implements Repositorio<Categoria> {

    private Connection conn;

    public CategoriaRepositorioImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM categorias");
            while (rs.next()) {
                categorias.add(crearCategoria(rs));
            }
        }
        return List.of();
    }

    @Override
    public Categoria buscarPorID(Long id) throws SQLException {
        Categoria categoria = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categorias WHERE id=?")) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                categoria = crearCategoria(rs);
            }
        }
        return categoria;
    }

    @Override
    public Categoria guardar(Categoria categoria) throws SQLException {
        String sql = null;
        if(categoria.getId() !=null && categoria.getId()>0){
            sql = "UPDATE categorias SET nombre=? WHERE id=?";
        }else {
            sql = "INSERT INTO categorias(nombre) VALUES(?)";
        }
        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, categoria.getNombre());
            if(categoria.getId() !=null && categoria.getId()>0){
                stmt.setLong(2, categoria.getId());
            }
            stmt.executeUpdate();

            if(categoria.getId() == null){
               try(ResultSet rs = stmt.getGeneratedKeys()) {
                   if (rs.next()) {
                       categoria.setId(rs.getLong(1));
                   }
               }
            }
        }
        return categoria;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("DELETE FROM categorias WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Categoria crearCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        return c;
    }
}
