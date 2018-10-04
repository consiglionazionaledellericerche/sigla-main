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
