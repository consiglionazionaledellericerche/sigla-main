/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
