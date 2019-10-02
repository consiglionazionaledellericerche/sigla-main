# SIGLA - _Sistema Informativo Gestione Linee di Attività_

#### _Per avviare una istanza di SIGLA con h2 in memoria_ 
```
docker run -p 8080:8080  -e THORNTAIL_PROJECT_STAGE="demo-h2" -ti consiglionazionalericerche/sigla-main:latest
```
#### _Per avviare una istanza di SIGLA con oracle locale_ 
```
docker run -d --name sigla-oracle -v $PWD/sigla-backend/initdb-oracle:/etc/entrypoint-initdb.d orangehrm/oracle-xe-11g
docker run -p 8080:8080 --link sigla-oracle:db -e LC_ALL="it_IT.UTF-8" -e LANG="it_IT.UTF-8" -e LANGUAGE="it_IT:it" -e THORNTAIL_DATASOURCES_DATA-SOURCES_SIGLA_CONNECTION-URL="jdbc:oracle:thin:@db:1521:xe" -e THORNTAIL_PROJECT_STAGE="demo-oracle" -ti consiglionazionalericerche/sigla-main:latest
```
#### _Per avviare una istanza di SIGLA con postgres locale_
```
docker run --name sigla-postgres -v $PWD/sigla-backend/init-user-postgres-db.sh:/docker-entrypoint-initdb.d/init-user-db.sh -e POSTGRES_PASSWORD=mysecretpassword -d postgres:9.6
docker run -p 8080:8080 --link sigla-postgres:db -e LC_ALL="it_IT.UTF-8" -e LANG="it_IT.UTF-8" -e LANGUAGE="it_IT:it" -e THORNTAIL_DATASOURCES_DATA-SOURCES_SIGLA_CONNECTION-URL="jdbc:postgresql://db:5432/sigladb?schema=public" -e THORNTAIL_PROJECT_STAGE="demo-postgres" -ti consiglionazionalericerche/sigla-main:latest
```
