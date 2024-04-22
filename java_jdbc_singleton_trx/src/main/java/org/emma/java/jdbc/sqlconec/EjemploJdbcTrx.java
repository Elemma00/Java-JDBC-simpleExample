package org.emma.java.jdbc.sqlconec;

import org.emma.java.jdbc.sqlconec.modelo.Categoria;
import org.emma.java.jdbc.sqlconec.modelo.Producto;
import org.emma.java.jdbc.sqlconec.repositorio.ProductoRepositorioImpl;
import org.emma.java.jdbc.sqlconec.repositorio.Repositorio;
import org.emma.java.jdbc.sqlconec.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class EjemploJdbcTrx {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = ConexionBD.getInstance()) {
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }

            try {
                Repositorio<Producto> repositorio = new ProductoRepositorioImpl();
                System.out.println("================= listamos ================");
                repositorio.listar().forEach(System.out::println);

                System.out.println("================= buscar por ID ================");
                System.out.println(repositorio.buscarPorID(1L));

                System.out.println("================= insertar nuevo producto ================");
                Producto producto = new Producto();
                producto.setNombre("Airpads Sony Genéricos");
                producto.setPrecio(450);
                producto.setFechaRegistro(new Date());
                Categoria categoria = new Categoria();
                categoria.setId(3L);
                producto.setCategoria(categoria);
                producto.setSku("777777");
                repositorio.guardar(producto);
                System.out.println("producto guardado con éxito");


                System.out.println("================= Editar producto ================");
                producto = new Producto();
                producto.setId(6L);
                producto.setNombre("Teclado AZIO MKLG");
                producto.setPrecio(1000);
                producto.setSku("000000");
                categoria = new Categoria();
                categoria.setId(2L);
                producto.setCategoria(categoria);
                repositorio.guardar(producto);
                System.out.println("producto editado con éxito");

                repositorio.listar().forEach(System.out::println);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

}
