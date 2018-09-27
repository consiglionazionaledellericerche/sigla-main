package it.cnr.contab.generator.util;

import it.cnr.contab.generator.artifacts.*;
import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.Filter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 19-nov-04
 *
 * @author Marco Spasiano
 * @version 2.1 [8-aug-2006] test connection null in chiusura
 */
public class DatabaseUtil {

    public static Hashtable<String, TablePackageStructure> tablePackageStructure = new Hashtable<String, TablePackageStructure>();
    public static Hashtable<String, CheckSN> checkSN = new Hashtable<String, CheckSN>();
    public static Hashtable<String, TableComments> tableComments = new Hashtable<String, TableComments>();
    /**
     * Holds this object single instance
     */
    private static DatabaseUtil instance;
    /**
     * Holds an internal connection
     */
    private Connection connection;
    /**
     * Private constructor avoiding instantiation
     */
    private DatabaseUtil() {
    }

    /**
     * Returns this object single instance
     */
    public synchronized static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public static OrderedHashtable getForeignAttributes(List columns) {
        //Recupero le ForeignKey e le memorizzo in una HashTable con le colonne che la compongono
        Filter f = new Filter();
        OrderedHashtable foreignAttributes = new OrderedHashtable();
        Iterator itAttrFo = columns.iterator();
        while (itAttrFo.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) itAttrFo.next();
            if (f.isFilter(c.getColumnName()) || !c.isForeign())
                continue;
            for (Iterator i = c.getForeignTable().iterator(); i.hasNext(); ) {
                ForeignKey fkName = (ForeignKey) i.next();
                String fk = TextUtil.capitalizeAndLower(fkName.getFkName());
                try {
                    String fkNew = fk.substring(fk.indexOf("_") + 1);
                    fkNew = fkNew.substring(0, fkNew.lastIndexOf("_"));
                    fkNew = TextUtil.capitalize(fkNew);
                    if (fkNew != null)
                        fk = fkNew;
                } catch (Exception e) {

                }
                //String attributeName = fkName.getAttributeName()+fk;
                String attributeName = TextUtil.capitalizeUnderscore(fkName.getForeignTable());

                ForeignAttribute foreignAttribute = (ForeignAttribute) foreignAttributes.get(attributeName);
                if (foreignAttribute != null)
                    foreignAttribute.getColumnName().put(c, fkName.getForeignColumnName());
                else {
                    foreignAttribute = new ForeignAttribute();
                    foreignAttribute.setAttributeName(attributeName);
                    foreignAttribute.setPackageName(fkName.getPackageName());
                    foreignAttribute.setForeignTable(fkName.getForeignTable());
                    foreignAttribute.setClassName(fkName.getAttributeName());
                    foreignAttribute.getColumnName().put(c, fkName.getForeignColumnName().toLowerCase());
                }
                foreignAttributes.put(attributeName, foreignAttribute);
            }
        }
        return foreignAttributes;
    }

    public static String getPropertyName(List columns, ColumnMetaData c) {
        OrderedHashtable fa = getForeignAttributes(columns);
        for (Enumeration attr = fa.keys(); attr.hasMoreElements(); ) {
            String attributeName = (String) attr.nextElement();
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.get(attributeName);
            for (Enumeration cols = foreignAttribute.getColumnName().keys(); cols.hasMoreElements(); ) {
                ColumnMetaData col = (ColumnMetaData) cols.nextElement();
                if (col.getColumnName().equalsIgnoreCase(c.getColumnName()))
                    return attributeName;
            }
        }
        return null;
    }

    /**
     * ritorna il metadata per la connessione interna
     */
    public DatabaseMetaData getDatabaseMetaData() throws Exception {
        if (connection == null)
            return null;
        else
            return connection.getMetaData();
    }

    /**
     * apre la connessione in base ai parametri contenuti nell'oggetto indicato
     */
    public void openConnection(DatabaseInfo info) throws Exception {
        if (connection != null) closeConnectionQuietly();
        Class.forName(info.getDriver());
        connection = DriverManager.getConnection(info.getUrl(), info.getUser(), info.getPassword());
    }

    /**
     * ritorna la conneccione interna
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * chiude la connessione interna
     */
    public void closeConnection() throws Exception {
        if (connection != null)
            connection.close();
    }

    /**
     * ciude la connessione interna senza riportare errori
     */
    public void closeConnectionQuietly() {
        try {
            connection.close();
        } catch (Throwable e) {
        }
    }

    /**
     * ciude la connessione indicata senza riportare errori
     */
    public void closeConnectionQuietly(Connection connection) {
        try {
            connection.close();
        } catch (Throwable e) {
        }
    }
}
