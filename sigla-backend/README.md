# Liquibase configuration
#### _Per attivare il profilo liquibase aggiungerlo ai jvm param_
```
-Dspring.profiles.active=CMIS,liquibase
```
#### Il ChangeLog file master : _src/main/resources/db-changelog-master.xml_
```
Per gli aggiornamenti di view, package, procedure e funzioni limitarsi a modificare il file senza aggiungere nessun changeset
```  
**Esempi liquibase**
   * http://www.liquibase.org/quickstart.html
   * http://www.liquibase.org/bestpractices.html
   * Aggiunta di una colonna 
         http://www.liquibase.org/documentation/changes/add_column.html
   * Esecuzione di uno script sql
        http://www.liquibase.org/documentation/changes/sql_file.html