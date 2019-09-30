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

package it.cnr.contab.generator.model;

import it.cnr.contab.generator.util.DatabaseUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Spasiano
 * @version 1.1 [28-Jul-2006] uso di GeneratorBean
 */
public class SchemaMetaData {
    public static synchronized List getTables(GeneratorBean bean) throws Exception {
        return getList(bean, "TABLE");
    }

    public static synchronized List getViews(GeneratorBean bean) throws Exception {
        return getList(bean, "VIEW");
    }

    private static synchronized List getList(GeneratorBean bean, String type) throws Exception {
        List<String> list = null;
        DatabaseMetaData dm = DatabaseUtil.getInstance().getDatabaseMetaData();
        ResultSet rs = dm.getTables(bean.getCatalog(), bean.getSchema(), bean.getFilter(), new String[]{type});
        while (rs.next()) {
            if (null == list) {
                list = new ArrayList<String>();
            }
            list.add(rs.getString("TABLE_NAME"));
        }
        rs.close();
        return list;
    }
}
