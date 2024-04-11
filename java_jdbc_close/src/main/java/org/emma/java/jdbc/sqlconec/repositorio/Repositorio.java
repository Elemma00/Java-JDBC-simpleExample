package org.emma.java.jdbc.sqlconec.repositorio;

import java.util.List;

public interface Repositorio<T> {
    List<T> listar();
    T buscarPorID(Long id);
    void guardar(T t);

    void eliminar(Long id);
}
