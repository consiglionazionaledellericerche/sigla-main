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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * Utility wrapper per interrrogazione sul Metadata
 *
 * @author Marco Spasiano
 * @version 1.0 [8-Aug-2006]
 */
public class DatabaseMetadataUtil {
    private DatabaseMetaData metadata;

    public DatabaseMetadataUtil(DatabaseMetaData metadata) {
        this.metadata = metadata;
    }

    public String[] getCatalogs() throws Exception {
        Vector<String> vector = new Vector<String>();

        ResultSet rs = metadata.getCatalogs();

        while (rs.next())
            vector.add(rs.getString(1));

        rs.close();

        // From Vector to Array
        String[] array = new String[vector.size()];
        vector.copyInto(array);

        return array;
    }

    public String[] getSchemas() throws Exception {
        Vector<String> vector = new Vector<String>();

        ResultSet rs = metadata.getSchemas();

        while (rs.next())
            vector.add(rs.getString(1));

        rs.close();

        // From Vector to Array
        String[] array = new String[vector.size()];
        vector.copyInto(array);

        return array;
    }

    public String[] getTableTypes() throws Exception {
        Vector<String> vector = new Vector<String>();

        ResultSet rs = metadata.getTableTypes();

        while (rs.next())
            vector.add(rs.getString(1));

        rs.close();

        // From Vector to Array
        String[] array = new String[vector.size()];
        vector.copyInto(array);

        return array;
    }

    public String[] getTables(String catalog, String schema, String table, String[] type) throws Exception {
        Vector<String> vector = new Vector<String>();

        ResultSet rs = metadata.getTables(catalog, schema, table, type);

        while (rs.next())
            vector.add(rs.getString(3));

        rs.close();

        // From Vector to Array
        String[] array = new String[vector.size()];
        vector.copyInto(array);

        return array;
    }

}
