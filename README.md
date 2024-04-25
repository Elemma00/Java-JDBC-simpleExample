# Java-JDBC-simpleExample
Un ejemplo simple de cómo utilizar la API JDBC de Java para realizar conexiones a DB relacionales como MySQL

* En java_jdbc_close se tiene un ejemplo para un conexión singular hacia una base de datos.
* En java_jdbc_pool se tiene el mismo ejemplo pero para multiconexiones realizadas hacia la misma BD.
* En java_jdbc_singleton_trx se implementan transacciones, en la cuales si hay un error en alguna de las operaciones se cancela todo.
* En java_jdbc_pool_trx es igual que el anterior pero se tiene un pool de conexiones en vez de solo una.
