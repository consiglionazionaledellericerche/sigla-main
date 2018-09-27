package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.util.DatabaseUtil;
import it.cnr.contab.generator.util.TextUtil;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 22-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] genera() --> generate()
 */
public class XMLBulkPersistentInfo extends XMLBase {

    public XMLBulkPersistentInfo(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        ArtifactNames names = new ArtifactNames(bean);

        openTag(Tags.SQL_PERSISTENT_INFO);

        String classe = names.getJavaClass(ArtifactNames.JAVA_BULK);
        addAttribute(Tags.PERSISTENT_CLASS_NAME, classe);

        classe = names.getJavaClass(ArtifactNames.JAVA_HOME);
        addAttribute(Tags.HOME_CLASS_NAME, classe);

        closeTag();

        openTag(Tags.DEFAULT_COLUMN_MAP);
        addAttribute(Tags.TABLE_NAME, the_table);
        closeTag();
        //Gestione delle Foreign Key
        Iterator it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (!c.isForeign())
                continue;
            if (c.isForeign()) {
                openTag(Tags.COLUMN_MAPPING);
                addAttribute(Tags.COLUMN_NAME, c.getColumnName());
                addAttribute(Tags.PROPERTY_NAME, TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c)) + "." + c.getForeignColumnName().toLowerCase());
                closeEmptyTag();
            }
        }
        closeTag(Tags.DEFAULT_COLUMN_MAP);

        it = columns.iterator();
        Hashtable<String, String> alredyInsert = new Hashtable<String, String>();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (!c.isForeign())
                continue;
            if (c.isForeign() && alredyInsert.get(TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c))) == null) {
                openTag(Tags.PERSISTENT_PROPERTY);
                addAttribute(Tags.NAME, TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c)));
                if (c.isPrimary())
                    addAttribute(Tags.PART_OF_ID, "true");
                alredyInsert.put(TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c)), "Y");
                closeEmptyTag();
            }
        }
        closeTag(Tags.SQL_PERSISTENT_INFO);
        write();
    }
}