package org.emma.java.jdbc.sqlconec;

import org.emma.java.jdbc.sqlconec.modelo.Categoria;
import org.emma.java.jdbc.sqlconec.modelo.Producto;
import org.emma.java.jdbc.sqlconec.repositorio.CategoriaRepositorioImpl;
import org.emma.java.jdbc.sqlconec.repositorio.ProductoRepositorioImpl;
import org.emma.java.jdbc.sqlconec.repositorio.Repositorio;
import org.emma.java.jdbc.sqlconec.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class EjemploJdbc {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConexionBD.getConnection()) {
            if(connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            try {
                Repositorio<Categoria> repositorioCategoria = new CategoriaRepositorioImpl(connection);
                System.out.println("================= Insertar Nueva categoria ================");
                Categoria categoria = new Categoria();
                categoria.setNombre("ElectroHogar");
                Categoria nuevaCategoria = repositorioCategoria.guardar(categoria);
                System.out.println("Categoria guardada con éxito");

                Repositorio<Producto> repositorio = new ProductoRepositorioImpl(connection);
                System.out.println("================= listamos ================");
                repositorio.listar().forEach(System.out::println);

                System.out.println("================= buscar por ID ================");
                System.out.println(repositorio.buscarPorID(2L));

                System.out.println("================= insertar nuevo producto ================");
                Producto producto = new Producto();
                producto.setNombre("Refrigerador Samsung");
                producto.setPrecio(9900);
                producto.setFechaRegistro(new Date());
                producto.setSku("000077");
                producto.setCategoria(nuevaCategoria);
                repositorio.guardar(producto);
                System.out.println("producto guardado con éxito");
                repositorio.listar().forEach(System.out::println);
                System.out.println("================= Editar producto ================");
                producto = new Producto();
                producto.setPrecio(10000);
                producto.setId(7L);
                producto.setCategoria(nuevaCategoria);
                repositorio.guardar(producto);
                repositorio.listar().forEach(System.out::println);
                System.out.println("================= eliminar producto ================");
                repositorio.eliminar(3L);
                repositorio.listar().forEach(System.out::println);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }
    }
}
