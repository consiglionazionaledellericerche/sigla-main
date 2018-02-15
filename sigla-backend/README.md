# Liquibase configuration
#### _Per avviare una istanza di oracle locale_ 
```
sudo docker run -d --name oracle-xe -p 1521:1521 -v $PWD/initdb:/etc/entrypoint-initdb.d alexeiled/docker-oracle-xe-11g
```
#### _Cambiare la variabile di ambiente_
```
export SIGLA_CONNECTION_URL=jdbc:oracle:thin:@localhost:1521:xe
```
#### _Dalla directory sigla-ear lanciare il comando_
```
mvn wildfly:run
```
#### _Per attivare il profilo liquibase aggiungerlo ai jvm param_
```
-Dspring.profiles.active=CMIS,liquibase
```
**Esempi liquibase**
   * http://www.liquibase.org/quickstart.html
   * Aggiunta di una colonna 
         http://www.liquibase.org/documentation/changes/add_column.html
   * Aggiornamento di una VIEW
        http://www.liquibase.org/documentation/changes/create_view.html
   * Esecuzione di uno script sql
        http://www.liquibase.org/documentation/changes/sql_file.html