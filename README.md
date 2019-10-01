SIGLA
===

#### _Per avviare una istanza di SIGLA con h2 in memoria_ 
```
docker run -p 8080:8080  -e THORNTAIL_PROJECT_STAGE="demo-h2" -ti consiglionazionalericerche/sigla-main:latest
```
#### _Per avviare una istanza di SIGLA con oracle locale_ 
```
docker run -d --name oracle-xe -p 1521:1521 -v $PWD/sigla-backend/initdb-oracle:/etc/entrypoint-initdb.d orangehrm/oracle-xe-11g
docker run -p 8080:8080  -e THORNTAIL_PROJECT_STAGE="demo-oracle" -ti consiglionazionalericerche/sigla-main:latest
```
#### _Per avviare una istanza di SIGLA con postgres locale_
```
docker run --name sigla-postgres -p 5432:5432 -v $PWD/sigla-backend/init-user-postgres-db.sh:/docker-entrypoint-initdb.d/init-user-db.sh -e POSTGRES_PASSWORD=mysecretpassword -d postgres:9.6
docker run -p 8080:8080  -e THORNTAIL_PROJECT_STAGE="demo-postgres" -ti consiglionazionalericerche/sigla-main:latest
```
