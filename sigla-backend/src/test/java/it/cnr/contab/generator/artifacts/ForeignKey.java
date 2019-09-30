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
