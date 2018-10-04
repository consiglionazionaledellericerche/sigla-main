package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.Filter;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Preferences;
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
public class XMLBulkInfo extends XMLBase {

    private int maxSize, areaCols, areaRows;

    public XMLBulkInfo(GeneratorBean bean) {
        super(bean);

        String s = bean.getString(Preferences.TEXTAREA_TEXT);
        maxSize = Integer.parseInt(s);
        s = bean.getString(Preferences.TEXTAREA_COLS);
        areaCols = Integer.parseInt(s);
        s = bean.getString(Preferences.TEXTAREA_ROWS);
        areaRows = Integer.parseInt(s);
    }

    public void generate(List columns) throws Exception {
        Filter f = new Filter();
        TableComments tc = DatabaseUtil.tableComments.get("TABLE-" + the_table);
        openTag(Tags.BULK_INFO);
        if (tc != null) {
            addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            addAttribute("xmlns", "http://contab.cnr.it/schema/SIGLA");
            addAttribute("xsi:schemaLocation", "http://contab.cnr.it/schema/SIGLA http://contab.cnr.it/schema/SIGLA/BulkInfo_1_5.xsd");
            if (tc.getComments() != null) {
                addAttribute(Tags.SHORT_DESCRIPTION, tc.getComments());
                addAttribute(Tags.LONG_DESCRIPTION, tc.getComments());
            }
        }
        closeTag();

        // generate tags fieldProperty
        TablePackageStructure tPackageStruc = null;
        Hashtable<String, String> alredyInsert = new Hashtable<String, String>();
        Iterator it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isForeign()) {
                tPackageStruc = DatabaseUtil.tablePackageStructure.get(c.getForeignTableName());
                if (tPackageStruc != null && alredyInsert.get(DatabaseUtil.getPropertyName(columns, c)) == null) {
                    openTag(Tags.FIELD_PROPERTY);
                    addAttribute(Tags.NAME, "find" + DatabaseUtil.getPropertyName(columns, c));
                    addAttribute(Tags.PROPERTY, TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c)));
                    addAttribute(Tags.INPUT_TYPE, Tags.SEARCHTOOL);
                    addAttribute(Tags.FORMNAME, Tags.SEARCHTOOL);
                    addAttribute(Tags.ENABLED_ON_SEARCH, "true");
                    addAttribute(Tags.LABEL, tPackageStruc.getBulkName());
                    closeEmptyTag();
                    alredyInsert.put(DatabaseUtil.getPropertyName(columns, c), "Y");
                }
            }
            if (f.isFilter(c.getColumnName())) {
                continue;
            }
            int size = c.getColumnSize();
            openTag(Tags.FIELD_PROPERTY);
            addAttribute(Tags.NAME, c.getHibernatePropertyName());
            if (c.isForeign() && tPackageStruc != null)
                addAttribute(Tags.PROPERTY, TextUtil.lowerFirst(DatabaseUtil.getPropertyName(columns, c)) + "." + c.getHibernateForeignColumnName());
            else
                addAttribute(Tags.PROPERTY, c.getHibernatePropertyName());
            int nameLen = c.getColumnName().length();
            int len = 3;
            if (nameLen < 3)
                len = nameLen - 1;
            String name = c.getColumnName().substring(0, len);
            String type = c.getSqlTypeName();
            if (type.equalsIgnoreCase(Tags.CHAR)
                    && size == 1
                    && name.equalsIgnoreCase(Tags.FLAG)) {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_CHECKBOX);
            } else if (DatabaseUtil.checkSN.get(("TABLE-" + c.getTableName() + "-COLUMN-" + c.getColumnName()).toUpperCase()) != null) {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_CHECKBOX);
            } else if (size > maxSize) {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_TEXTAREA);
                addAttribute(
                        Tags.COLS,
                        new Integer(areaCols).toString());
                addAttribute(
                        Tags.ROWS,
                        new Integer(areaRows).toString());
                addAttribute(
                        Tags.MAX_LENGTH,
                        new Integer(size).toString());
            } else if (type.equalsIgnoreCase(Tags.TIMESTAMP)) {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_TEXT);
                addAttribute(Tags.FORMAT_NAME, Tags.DATE_SHORT);
            } else if (type.equalsIgnoreCase("DECIMAL") && c.getColumnScale() == 2 && c.getColumnSize() == 15) {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_TEXT);
                addAttribute(Tags.FORMAT_NAME, Tags.EURO_FORMAT);
            } else {
                addAttribute(Tags.INPUT_TYPE, Tags.TYPE_TEXT);
                addAttribute(Tags.INPUT_SIZE, new Integer(size).toString());
                addAttribute(
                        Tags.MAX_LENGTH,
                        new Integer(size).toString());

            }
            addAttribute(Tags.ENABLED_ON_EDIT, "false");
            if (c.isPrimary())
                addAttribute(Tags.ENABLED_ON_SEARCH, "false");
            if (!c.isNullable())
                addAttribute(Tags.NULLABLE, "false");
            addAttribute(Tags.LABEL, c.getLabel());
            closeEmptyTag();
        }

        // generate tags form
        it = columns.listIterator();
        openTag(Tags.FORM);
        addAttribute(Tags.NAME, Tags.SEARCHTOOL);
        closeTag();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (f.isFilter(c.getColumnName()) || !c.isPrimary()) {
                continue;
            }
            openTag(Tags.FORM_FIELD_PROPERTY);
            addAttribute(Tags.NAME, c.getHibernatePropertyName());
            closeEmptyTag();
        }
        closeTag(Tags.FORM);

        // generate tags formFieldProperty
        TablePackageStructure tPackageStrucForm = null;
        Hashtable<String, String> alredyInsertForm = new Hashtable<String, String>();
        it = columns.listIterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (f.isFilter(c.getColumnName())) {
                continue;
            }
            if (c.isForeign()) {
                tPackageStrucForm = DatabaseUtil.tablePackageStructure.get(c.getForeignTableName());
                if (tPackageStrucForm != null && alredyInsertForm.get("find" + DatabaseUtil.getPropertyName(columns, c)) == null) {
                    openTag(Tags.FORM_FIELD_PROPERTY);
                    addAttribute(Tags.NAME, "find" + DatabaseUtil.getPropertyName(columns, c));
                    closeEmptyTag();
                    alredyInsertForm.put("find" + DatabaseUtil.getPropertyName(columns, c), "Y");
                }
            } else {
                openTag(Tags.FORM_FIELD_PROPERTY);
                addAttribute(Tags.NAME, c.getHibernatePropertyName());
                closeEmptyTag();
            }
        }

        // generate tags findFieldProperty

        it = columns.listIterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (!f.isFilter(c.getColumnName())) {
                openTag(Tags.FIND_FIELD_PROPERTY);
                addAttribute(Tags.NAME, c.getHibernatePropertyName());
                closeEmptyTag();
            }
        }

        //		generate tags columnFieldProperty
        it = columns.listIterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (!f.isFilter(c.getColumnName())) {
                openTag(Tags.COLUMN_FIELD_PROPERTY);
                addAttribute(Tags.NAME, c.getHibernatePropertyName());
                closeEmptyTag();
            }
        }
        closeTag(Tags.BULK_INFO);
    }

}
