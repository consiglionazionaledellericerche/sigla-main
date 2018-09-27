package it.cnr.contab.generator.artifacts;

public class TableComments {
    private String tableName;
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String columnName) {
        this.comments = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
