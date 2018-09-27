package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;

public class ForeignKey {
    private String foreignTable;
    private String foreignColumnName;
    private String fkName;
    private TablePackageStructure tPackageStruc;
    private ColumnMetaData columnMetaData;

    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public TablePackageStructure getTPackageStruc() {
        return tPackageStruc;
    }

    public void setTPackageStruc(TablePackageStructure packageStruc) {
        tPackageStruc = packageStruc;
    }

    public String getAttributeName() {
        if (getTPackageStruc() == null)
            return getForeignTable().toLowerCase();
        return getTPackageStruc().getBulkName();
    }

    public String getPackageName() {
        if (getTPackageStruc() == null)
            return null;
        return getTPackageStruc().getPackageName();
    }

    public ColumnMetaData getColumnMetaData() {
        return columnMetaData;
    }

    public void setColumnMetaData(ColumnMetaData columnMetaData) {
        this.columnMetaData = columnMetaData;
    }

}
