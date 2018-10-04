package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.util.OrderedHashtable;

public class ForeignAttribute {
    private String attributeName;
    private String packageName;
    private String foreignTable;
    private String className;
    private OrderedHashtable columnName = new OrderedHashtable();

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public OrderedHashtable getColumnName() {
        return columnName;
    }

    public void setColumnName(OrderedHashtable columnName) {
        this.columnName = columnName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
