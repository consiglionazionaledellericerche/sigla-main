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
