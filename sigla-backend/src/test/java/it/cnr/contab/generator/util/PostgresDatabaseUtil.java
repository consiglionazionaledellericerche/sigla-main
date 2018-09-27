package it.cnr.contab.generator.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresDatabaseUtil {
    private final static String sql = "select * from pg_catalog.pg_user";

    /**
     * esegue una qury di test sulla connessione in oggetto
     */
    public static boolean testConnection(Connection connection) throws Exception {
        if (connection == null) throw new Exception("Connessione nulla");
        synchronized (connection) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            boolean result = false;
            if (rs.next())
                result = true;
            rs.close();
            return result;
        }
    }

}
