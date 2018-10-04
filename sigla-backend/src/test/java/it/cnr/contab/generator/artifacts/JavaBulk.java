package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.Filter;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Generation;
import it.cnr.contab.generator.util.DatabaseUtil;
import it.cnr.contab.generator.util.OrderedHashtable;
import it.cnr.contab.generator.util.TextUtil;

import java.util.*;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] refactoring (generate + pulizia)
 */
public class JavaBulk extends ClassBase {

    public JavaBulk(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        Filter f = new Filter();
        addPackage();
        addImports(Generation.bulk_ActionContext);
        addImports(Generation.bulk_OggettoBulk);
        addImports(Generation.bulk_CRUDBP);

        //Recupero le ForeignKey e le memorizzo in una HashTable con le colonne che la compongono
        OrderedHashtable foreignAttributes = DatabaseUtil.getForeignAttributes(columns);

        //Aggiungo gli import dei package
        Hashtable<String, String> alredyInsert = new Hashtable<String, String>();
        for (Enumeration fa = foreignAttributes.elements(); fa.hasMoreElements(); ) {
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.nextElement();
            if (foreignAttribute.getPackageName() != null) {
                if (alredyInsert.get(foreignAttribute.getPackageName()) == null)
                    addImports(foreignAttribute.getPackageName() + ".*");
                alredyInsert.put(foreignAttribute.getPackageName(), "Y");
            }
        }

        // classe
        openClass(the_class
                + Generation.java_bulk
                + " extends "
                + the_class
                + Generation.java_base);

        // Attributi della classe
        for (Enumeration fa = foreignAttributes.elements(); fa.hasMoreElements(); ) {
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.nextElement();
            boolean createObject = !the_table.equalsIgnoreCase(foreignAttribute.getForeignTable());
            String comments = " ";
            if (DatabaseUtil.tableComments.get("TABLE-" + foreignAttribute.getForeignTable()) != null)
                comments += DatabaseUtil.tableComments.get("TABLE-" + foreignAttribute.getForeignTable()).getComments();
            addCommentAttribute(TextUtil.nvl(foreignAttribute.getForeignTable()) + comments);
            addFieldAttribute(TextUtil.capitalize(foreignAttribute.getClassName()) + "Bulk", TextUtil.lowerFirst(foreignAttribute.getAttributeName()), createObject);
        }

        // Primo Constructor
        addCommentConstructor(the_table);
        addConstructor(the_class + Generation.java_bulk + "()");
        addSuper();
        closeConstructor();
        // Secondo Constructor
        addCommentConstructor(the_table);
        Iterator it = columns.iterator();
        String allFieldFormat = "";
        String allField = "";
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                allFieldFormat = allFieldFormat
                        + getType(c)
                        + " "
                        + c.getPropertyName()
                        + ", ";
                allField = allField
                        + c.getPropertyName()
                        + ", ";
            }

        }
        if (allFieldFormat == null || allFieldFormat.length() < 1) {
        } else {
            allFieldFormat = allFieldFormat.substring(0, allFieldFormat.length() - 2);
        }
        if (allField == null || allField.length() < 1) {
        } else {
            allField = allField.substring(0, allField.length() - 2);
        }

        addConstructor(the_class + Generation.java_bulk + "("
                + allFieldFormat + ")");
        addSuper(allField);
        //aggiungo inizializzazione degli oggetti per le chiavi migrate
        for (Enumeration fa = foreignAttributes.elements(); fa.hasMoreElements(); ) {
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.nextElement();
            Vector<String> the_field = new Vector<String>();
            for (Enumeration campi = foreignAttribute.getColumnName().keys(); campi.hasMoreElements(); ) {
                ColumnMetaData column = (ColumnMetaData) campi.nextElement();
                if (!column.isPrimary())
                    continue;
                the_field.add(column.getPropertyName());
            }
            if (!the_field.isEmpty())
                methodSetKeys(TextUtil.capitalize(foreignAttribute.getAttributeName()), TextUtil.capitalize(foreignAttribute.getClassName()) + "Bulk", the_field);
        }
        //FINE aggiungo inizializzazione degli oggetti per le chiavi migrate

        closeConstructor();
        // metodi Get e Set per gli attributi della classe
        for (Enumeration fa = foreignAttributes.elements(); fa.hasMoreElements(); ) {
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.nextElement();
            TableComments tableComments = DatabaseUtil.tableComments.get("TABLE-" + foreignAttribute.getForeignTable());
            if (tableComments != null)
                addCommentGetSet(tableComments.getComments(), "GET");
            methodGet(TextUtil.capitalize(foreignAttribute.getClassName()) + "Bulk", TextUtil.lowerFirst(foreignAttribute.getAttributeName()));
            if (tableComments != null)
                addCommentGetSet(tableComments.getComments(), "SET");
            methodSet(TextUtil.capitalize(foreignAttribute.getClassName()) + "Bulk", TextUtil.lowerFirst(foreignAttribute.getAttributeName()));
        }

        // metodi Get e Set per le Foreign Key
        for (Enumeration fa = foreignAttributes.elements(); fa.hasMoreElements(); ) {
            ForeignAttribute foreignAttribute = (ForeignAttribute) fa.nextElement();
            for (Enumeration colonne = foreignAttribute.getColumnName().keys(); colonne.hasMoreElements(); ) {
                ColumnMetaData c = (ColumnMetaData) colonne.nextElement();
                String colonnaFK = (String) foreignAttribute.getColumnName().get(c);
                addCommentGetSet(c.getLabel(), "GET");
                methodGetForeign(getType(c), c.getPropertyName(),
                        "get" + TextUtil.capitalize(foreignAttribute.getAttributeName()) + "()." + "get" + TextUtil.capitalizeAndLower(colonnaFK) + "()",
                        TextUtil.capitalize(foreignAttribute.getClassName()) + "Bulk",
                        TextUtil.lowerFirst(foreignAttribute.getAttributeName()));
                addCommentGetSet(c.getLabel(), "SET");
                methodSetForeign(getType(c), c.getPropertyName(),
                        foreignAttribute.getAttributeName(),
                        TextUtil.capitalizeAndLower(colonnaFK));
            }
        }
        //Recupero tutti i flag eventuali per implementare il metodo initializeForInsert
        // Campi della classe
        Iterator itFlag = columns.iterator();
        Vector<String> flag = new Vector<String>();
        while (itFlag.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) itFlag.next();
            if (DatabaseUtil.checkSN.get(("TABLE-" + c.getTableName() + "-COLUMN-" + c.getColumnName()).toUpperCase()) != null) {
                flag.add(c.getPropertyName());
            }
        }
        if (!flag.isEmpty()) {
            openMethod("OggettoBulk", "initializeForInsert(CRUDBP bp, ActionContext context)");
            writeMethodAttributeBoolean(flag);
            closeMethod();
        }
        closeClass();
    }

}
