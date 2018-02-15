# Liquibase configuration
#### _Per attivare il profilo liquibase aggiungerlo ai jvm param_
```
-Dspring.profiles.active=CMIS,liquibase
```
#### Il ChangeLog file master è src/main/resources/db-changelog-master.xml
 
**Esempi liquibase**
   * http://www.liquibase.org/quickstart.html
   * http://www.liquibase.org/bestpractices.html
   * Aggiunta di una colonna 
         http://www.liquibase.org/documentation/changes/add_column.html
   * Aggiornamento di una VIEW
        http://www.liquibase.org/documentation/changes/create_view.html
   * Esecuzione di uno script sql
        http://www.liquibase.org/documentation/changes/sql_file.html