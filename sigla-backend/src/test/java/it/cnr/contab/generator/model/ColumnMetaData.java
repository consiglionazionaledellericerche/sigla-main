package it.cnr.contab.generator.model;

import it.cnr.contab.generator.artifacts.ForeignKey;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Modello per il metadata di una colonna
 *
 * @author Marco Spasiano
 * @version 1.1 [7-Aug-2006] adattamento a Java 5
 */
public class ColumnMetaData implements Comparable<ColumnMetaData> {
    private String tableName;
    private String columnName;
    private String propertyName;
    private String sqlTypeName;
    private String label;
    private int columnSize;
    private int columnScale;
    private boolean nullable;
    private boolean primary;
    private boolean foreign;
    private int order;
    private String foreignColumnName;
    private String foreignTableName;
    private Vector<ForeignKey> foreignTable = new Vector<ForeignKey>();

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    public int getColumnScale() {
        return columnScale;
    }

    public void setColumnScale(int i) {
        columnScale = i;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int i) {
        columnSize = i;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean b) {
        nullable = b;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean b) {
        primary = b;
    }

    public String getPropertyName() {
        if (getColumnName().equals("PG_VER_REC"))
            return getColumnName().toLowerCase();
        return getHibernatePropertyName();
    }

    public void setPropertyName(String string) {
        propertyName = string;
    }

    public String getHibernatePropertyName() {
        String temp = propertyName.toLowerCase();
        String as[] = null;
        StringTokenizer stringtokenizer = new StringTokenizer(temp, "_");
        as = new String[stringtokenizer.countTokens()];
        for (int k = 0; k < as.length; k++)
            as[k] = stringtokenizer.nextToken();
        String prefisso = "";
        for (int j = 0; j < as.length; j++) {
            if (j == 0)
                prefisso = prefisso.concat(as[j]);
            else
                prefisso = prefisso.concat(Character.toUpperCase(as[j].charAt(0)) + as[j].substring(1));
        }
        return prefisso;
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

    public void setSqlTypeName(String string) {
        sqlTypeName = string;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean b) {
        foreign = b;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String string) {
        label = string;
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(ColumnMetaData o) {
        if (this.getOrder() < o.getOrder())
            return -1;
        if (this.getOrder() == o.getOrder())
            return 0;
        if (this.getOrder() > o.getOrder())
            return 1;
        return -1;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addToForeignTable(ForeignKey fk) {
        getForeignTable().add(fk);
    }

    public Vector<ForeignKey> getForeignTable() {
        return foreignTable;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    public String getHibernateForeignColumnName() {
        String temp = getForeignColumnName().toLowerCase();
        String as[] = null;
        StringTokenizer stringtokenizer = new StringTokenizer(temp, "_");
        as = new String[stringtokenizer.countTokens()];
        for (int k = 0; k < as.length; k++)
            as[k] = stringtokenizer.nextToken();
        String prefisso = "";
        for (int j = 0; j < as.length; j++) {
            if (j == 0)
                prefisso = prefisso.concat(as[j]);
            else
                prefisso = prefisso.concat(Character.toUpperCase(as[j].charAt(0)) + as[j].substring(1));
        }
        return prefisso;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }


}
