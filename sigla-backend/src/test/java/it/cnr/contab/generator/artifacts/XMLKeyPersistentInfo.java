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
import it.cnr.contab.generator.model.GeneratorBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] refactoring
 */
public class XMLKeyPersistentInfo extends XMLBase {

    public XMLKeyPersistentInfo(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        ArtifactNames names = new ArtifactNames(bean);

        openTag(Tags.SQL_PERSISTENT_INFO);

        String classe = names.getJavaClass(ArtifactNames.JAVA_KEY);
        addAttribute(Tags.PERSISTENT_CLASS_NAME, bean.getPackageName().concat(".").concat(classe));

        closeTag();

        openTag(Tags.DEFAULT_COLUMN_MAP);
        addAttribute(Tags.TABLE_NAME, the_table);
        closeTag();

        Iterator it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                openTag(Tags.COLUMN_MAPPING);
                addAttribute(Tags.COLUMN_NAME, c.getColumnName());
                addAttribute(Tags.PROPERTY_NAME, c.getPropertyName());
                addAttribute(Tags.SQL_TYPE_NAME, c.getSqlTypeName());
                addAttribute(
                        Tags.COLUMN_SIZE,
                        new Integer(c.getColumnSize()).toString());
                addAttribute(
                        Tags.NULLABLE,
                        new Boolean(c.isNullable()).toString());
                addAttribute(
                        Tags.PRIMARY,
                        new Boolean(c.isPrimary()).toString());
                closeEmptyTag();
            }
        }
        closeTag(Tags.DEFAULT_COLUMN_MAP);

        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                openTag(Tags.PERSISTENT_PROPERTY);
                addAttribute(Tags.NAME, c.getPropertyName());
                addAttribute(Tags.PART_OF_ID, "true");
                closeEmptyTag();
            }
        }

        closeTag(Tags.SQL_PERSISTENT_INFO);
    }

}
