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

/*
 * Created on 22-nov-04
 * Table Metadata Model
 */
package it.cnr.contab.generator.model;

import it.cnr.contab.generator.artifacts.ForeignKey;
import it.cnr.contab.generator.artifacts.Tags;
import it.cnr.contab.generator.properties.Preferences;
import it.cnr.contab.generator.util.DatabaseUtil;
import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;


/**
 * Modello per il metadata di una tabella
 *
 * @author Marco Spasiano
 * @version 1.1 [7-Aug-2006] adattamento a plugin e uso di GeneratorBean
 */
public class TableMetaData {
    private DatabaseUtil dbUtil;
    private GeneratorBean bean;

    /**
     * Table Metadata Model
     */
    public TableMetaData(GeneratorBean bean) {
        this.bean = bean;
        dbUtil = DatabaseUtil.getInstance();
    }

    public List getTableMetaData() throws Exception {
        if (dbUtil == null) {
            throw new Exception("TableMetaData: la database utility e' NULL");
        }
        // senza questo setting il commento alle colonne è null
        Connection oc = (Connection) dbUtil.getConnection();
        Optional.ofNullable(oc)
                .filter(OracleConnection.class::isInstance)
                .map(OracleConnection.class::cast)
                .ifPresent(oracleConnection -> {
                    oracleConnection.setRemarksReporting(true);
                });

        String schema = bean.getSchema();
        String table = bean.getTable();
        DatabaseMetaData dm = dbUtil.getDatabaseMetaData();
        ResultSet rs = dm.getColumns(null, schema, table, "%");

        // gets columns attribute
        List<ColumnMetaData> list = new Vector<ColumnMetaData>();
        while (rs.next()) {
            String s = null;
            ColumnMetaData c = new ColumnMetaData();
            c.setTableName(rs.getString(Tags.MD_TABLE_NAME));
            c.setColumnName(rs.getString(Tags.MD_COLUMN_NAME));
            c.setPropertyName(c.getColumnName());
            c.setSqlTypeName(
                    convertSqlTypeName(rs.getString(Tags.MD_TYPE_NAME)));
            c.setColumnSize(rs.getInt(Tags.MD_COLUMN_SIZE));
            c.setColumnScale(rs.getInt(Tags.MD_DECIMAL_DIGITS));
            s = rs.getString(Tags.MD_IS_NULLABLE);
            c.setNullable(!(s.equalsIgnoreCase("NO")));
            s = rs.getString(Tags.MD_REMARKS);
            if (s == null || s.trim().length() < 1) {
                c.setLabel(c.getPropertyName());
            } else {
                c.setLabel(makeRemarks(s));
            }
            c.setOrder(rs.getShort(Tags.MD_ORDINAL_POSITION));
            list.add(c);
        }
        rs.close();

        if (list.isEmpty()) {
            throw new Exception("TableMetaData: impossibile determinare i metadata della tabella.");
        }
        // sets primary keys
        rs = dm.getPrimaryKeys(null, schema, table);
        while (rs.next()) {
            String s = null;
            s = rs.getString(Tags.MD_COLUMN_NAME);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ColumnMetaData c = (ColumnMetaData) it.next();
                if (c.getColumnName().equals(s)) {
                    c.setPrimary(true);
                    c.setOrder(rs.getShort(Tags.MD_KEY_SEQ));
                }
            }
        }
        // sets foreign keys
        rs = dm.getImportedKeys(null, schema, table);
        while (rs.next()) {
            String s = null;
            s = rs.getString(Tags.MD_FKTABLE_NAME);
            if (!(s.equals(table))) {
                continue;
            }
            s = rs.getString(Tags.MD_FKCOLUMN_NAME);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ColumnMetaData c = (ColumnMetaData) it.next();
                if (c.getColumnName().equals(s)) {
                    String foreignTableName = rs.getString(Tags.MD_PKTABLE_NAME);
                    String foreignColumnName = rs.getString(Tags.MD_PKCOLUMN_NAME);
                    c.setForeign(true);
                    c.setForeignColumnName(foreignColumnName);
                    c.setForeignTableName(foreignTableName);
                    ForeignKey foreignKey = new ForeignKey();
                    foreignKey.setFkName(rs.getString(Tags.MD_FK_NAME));
                    foreignKey.setForeignTable(foreignTableName);
                    foreignKey.setForeignColumnName(foreignColumnName);
                    foreignKey.setTPackageStruc(DatabaseUtil.tablePackageStructure.get(foreignTableName));
                    foreignKey.setColumnMetaData(c);
                    c.addToForeignTable(foreignKey);
                }
            }
        }

        rs.close();
        Collections.sort(list);
        return list;
    }

    private String makeRemarks(String string) {
        char cr = new String("\n").charAt(0);
        int pos = string.indexOf(cr);
        if (pos > 0) {
            return string.substring(0, pos).trim();
        }
        return string.trim();
    }

    /**
     * Ritorna il valore associato a sqlTypeName se presente nel property file
     * altrimenti ritorna sqlTypeName
     *
     * @param string (sqlTypeName)
     * @return string (sqlTypeName o value of property file)
     */
    private String convertSqlTypeName(String string) {
        String key = Preferences.SQL_PREFIX + string.toLowerCase();
        String value = bean.getString(key);
        if (value == null || value.trim().length() < 1) {
            return string;
        }
        return value.toUpperCase();
    }

}
