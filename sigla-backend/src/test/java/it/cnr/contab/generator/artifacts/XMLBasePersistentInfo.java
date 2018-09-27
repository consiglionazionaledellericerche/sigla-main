package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Preferences;
import it.cnr.contab.generator.util.DatabaseUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] genera() --> generate()
 */
public class XMLBasePersistentInfo extends XMLBase {

    public XMLBasePersistentInfo(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        ArtifactNames names = new ArtifactNames(bean);

        openTag(Tags.SQL_PERSISTENT_INFO);

        String classe = names.getJavaClass(ArtifactNames.JAVA_BASE);
        addAttribute(Tags.PERSISTENT_CLASS_NAME, classe);

        classe = names.getJavaClass(ArtifactNames.JAVA_KEY);
        addAttribute(Tags.KEY_CLASS_NAME, classe);

        classe = names.getJavaClass(ArtifactNames.JAVA_HOME);
        addAttribute(Tags.HOME_CLASS_NAME, classe);

        closeTag();

        openTag(Tags.DEFAULT_COLUMN_MAP);
        addAttribute(Tags.TABLE_NAME, the_table);
        closeTag();

        Iterator it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                continue;
            }
            int size = c.getColumnSize();
            openTag(Tags.COLUMN_MAPPING);
            addAttribute(Tags.COLUMN_NAME, c.getColumnName());
            addAttribute(Tags.PROPERTY_NAME, c.getPropertyName());
            addAttribute(Tags.SQL_TYPE_NAME, c.getSqlTypeName());
            addAttribute(Tags.COLUMN_SIZE, new Integer(size).toString());
            if (c.getColumnScale() > 0) {
                addAttribute(
                        Tags.COLUMN_SCALE,
                        new Integer(c.getColumnScale()).toString());
            }
            addAttribute(Tags.NULLABLE, new Boolean(c.isNullable()).toString());
            int nameLen = c.getColumnName().length();
            int len = 3;
            if (nameLen < 3)
                len = nameLen - 1;
            String name = c.getColumnName().substring(0, len);
            String type = c.getSqlTypeName();
            if (type.equalsIgnoreCase(Tags.CHAR)
                    && size == 1
                    && name.equalsIgnoreCase(Tags.FLAG)) {
                addAttribute(
                        Tags.CONVERTER_CLASS_NAME,
                        bean.getString(Preferences.TYPE_CHAR_FLAG));
            } else if ((DatabaseUtil.checkSN.get(("TABLE-" + c.getTableName() + "-COLUMN-" + c.getColumnName()).toUpperCase()) != null)) {
                addAttribute(
                        Tags.CONVERTER_CLASS_NAME,
                        bean.getString(Preferences.TYPE_CHAR_FLAG));
            }
            closeEmptyTag();
        }
        closeTag(Tags.DEFAULT_COLUMN_MAP);

        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                continue;
            }
            openTag(Tags.PERSISTENT_PROPERTY);
            addAttribute(Tags.NAME, c.getPropertyName());
            closeEmptyTag();
        }

        closeTag(Tags.SQL_PERSISTENT_INFO);

        write();
    }
}
