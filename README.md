SIGLA
===
#### _Per avviare una istanza di oracle locale_ 
```
sudo docker run -d --name oracle-xe -p 1521:1521 -v $PWD/sigla-backend/initdb-oracle:/etc/entrypoint-initdb.d alexeiled/docker-oracle-xe-11g
```
#### _Per avviare una istanza di postgres locale_
```
sudo docker run --name sigla-postgres -p 5432:5432 -v $PWD/sigla-backend/init-user-postgres-db.sh:/docker-entrypoint-initdb.d/init-user-db.sh -e POSTGRES_PASSWORD=mysecretpassword -d postgres:9.6
```

**_Cambiare la variabile di ambiente_**
   * oracle
        ```
        export SIGLA_CONNECTION_URL=jdbc:oracle:thin:@localhost:1521:xe
        ```
   * postgres
      * linux 
       ```
       export SIGLA_ORACLE_ENABLE=false
       export SIGLA_POSTGRES_ENABLE=true
       export SIGLA_CONNECTION_URL=jdbc:postgresql://localhost:5432/sigladb?schema=public
       ```
      * windows
       ```
       set SIGLA_ORACLE_ENABLE=false
       set SIGLA_POSTGRES_ENABLE=true
       set SIGLA_CONNECTION_URL=jdbc:postgresql://localhost:5432/sigladb?schema=public
       ```
    
#### _Dalla directory sigla-ear lanciare il comando_
```
mvn wildfly:run
```