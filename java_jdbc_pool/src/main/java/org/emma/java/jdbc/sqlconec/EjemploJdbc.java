package org.emma.java.jdbc.sqlconec;

import org.emma.java.jdbc.sqlconec.modelo.Categoria;
import org.emma.java.jdbc.sqlconec.modelo.Producto;
import org.emma.java.jdbc.sqlconec.repositorio.ProductoRepositorioImpl;
import org.emma.java.jdbc.sqlconec.repositorio.Repositorio;

import java.util.Date;

public class EjemploJdbc {
    public static void main(String[] args) {

        Repositorio<Producto> repositorio = new ProductoRepositorioImpl();
        System.out.println("================= listamos ================");
        repositorio.listar().forEach(System.out::println);

        System.out.println("================= buscar por ID ================");
        System.out.println(repositorio.buscarPorID(2L));

        System.out.println("================= insertar nuevo producto ================");
        Producto producto = new Producto();
        producto.setNombre("Teclado Red Dragon Mecánico");
        producto.setPrecio(450);
        producto.setFechaRegistro(new Date());
        Categoria categoria = new Categoria();
        categoria.setId(3L);
        producto.setCategoria(categoria);
        repositorio.guardar(producto);
        System.out.println("producto guardado con éxito");
        repositorio.listar().forEach(System.out::println);
        System.out.println("================= Editar producto ================");
        producto.setPrecio(700);
        producto.setId(7L);
        repositorio.guardar(producto);
        repositorio.listar().forEach(System.out::println);
        System.out.println("================= eliminar producto ================");
        repositorio.eliminar(3L);
        repositorio.listar().forEach(System.out::println);
    }
}
