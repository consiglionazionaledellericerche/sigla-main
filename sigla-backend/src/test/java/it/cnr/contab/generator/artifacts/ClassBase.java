package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Preferences;
import it.cnr.contab.generator.util.DatabaseUtil;
import it.cnr.contab.generator.util.TextUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [21-aug-2006] utilizzo di ArtifactContents e inibizione di write()
 * [22-aug-2006] refactoring (generate + pulizia)
 * [24-aug-2006] gestione campo Preferences.TYPE_DECIMAL_SIZE
 */
public abstract class ClassBase implements ArtifactContents {
    private static final String TAB = "\t", CRLF = "\n";
    protected GeneratorBean bean;
    protected String the_class;
    protected String the_package;
    protected String the_table;
    private StringBuffer document;
    private int nTabs = -1;

    public ClassBase(GeneratorBean bean) {
        this.bean = bean;
        document = new StringBuffer();
        the_class = bean.getPrefix();
        the_package = bean.getPackageName();
        the_table = bean.getTable();

    }

    protected void addComment() {
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        document.append("/*");
        document.append(CRLF);
        document.append(" * Created by BulkGenerator " + bean.getString(Preferences.VERSION));
        document.append(CRLF);
        document.append(" * Date " + formatoData.format(new Date()));
        document.append(CRLF);
        document.append(" */");
        document.append(CRLF);

    }

    protected void addCommentConstructor(String tableName) {
        document.append(CRLF);
        indent(++nTabs);
        document.append("/**");
        document.append(CRLF);
        indent(nTabs);
        document.append(" * Created by BulkGenerator " + bean.getString(Preferences.VERSION));
        document.append(CRLF);
        indent(nTabs);
        document.append(" * Table name: " + tableName);
        document.append(CRLF);
        indent(nTabs);
        document.append(" **/");
        nTabs--;
    }

    protected void addCommentGetSet(String comment, String type) {
        document.append(CRLF);
        indent(++nTabs);
        document.append("/**");
        document.append(CRLF);
        indent(nTabs);
        document.append(" * Created by BulkGenerator " + bean.getString(Preferences.VERSION));
        document.append(CRLF);
        indent(nTabs);
        if (type.equalsIgnoreCase("GET"))
            document.append(" * Restituisce il valore di: [" + comment + "]");
        else if (type.equalsIgnoreCase("SET"))
            document.append(" * Setta il valore di: [" + comment + "]");
        document.append(CRLF);
        indent(nTabs);
        document.append(" **/");
        nTabs--;
    }

    protected void addCommentAttribute(String comment) {
        document.append(CRLF);
        indent(++nTabs);
        document.append("/**");
        document.append(CRLF);
        indent(nTabs);
        document.append(" * [" + comment + "]");
        document.append(CRLF);
        indent(nTabs);
        document.append(" **/");
        nTabs--;
    }

    protected void addRowComment(String row) {
        document.append(CRLF);
        document.append("//    ");
        document.append(row);
    }

    protected void addRow(String row) {
        document.append(CRLF);
        document.append(row);
    }

    protected void addPackage() {
        addComment();
        document.append("package ");
        document.append(the_package);
        document.append(";");

    }

    protected void addImports(String the_import) {
        document.append(CRLF);
        document.append("import ");
        document.append(the_import);
        document.append(";");
    }

    protected void openClass(String stringa) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("public class ");
        document.append(stringa);
        document.append(" {");
    }

    protected void closeClass() {
        closeMethod();
    }

    protected void closeConstructor() {
        closeMethod();
    }

    protected void addField(String the_format, String the_field) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("private ");
        document.append(the_format);
        document.append(" ");
        document.append(the_field);
        document.append(";");
        // formattazione del testo
        nTabs--;
        // fine formattazione
    }

    protected void addFieldAttribute(String the_format, String the_field, boolean createObject) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("private ");
        document.append(the_format);
        document.append(" ");
        document.append(the_field);
        if (createObject) {
            document.append(" = ");
            document.append(" new " + the_format + "()");
        }
        document.append(";");
        // formattazione del testo
        nTabs--;
        // fine formattazione
    }

    protected void addStatement(String the_row) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append(the_row);
        document.append(";");
        // formattazione del testo
        nTabs--;
        // fine formattazione
    }

    protected void addConstructor(String stringa) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("public ");
        document.append(stringa);
        document.append(" {");
    }

    protected void addSuper() {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("super();");
        nTabs--;
    }

    protected void addSuper(String stringa) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("super(");
        document.append(stringa);
        document.append(");");
        nTabs--;
    }

    protected void addReturn(String the_field) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("return ");
        document.append(the_field);
        document.append(";");
        nTabs--;
    }

    protected void addThis(String the_field) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("this.");
        document.append(the_field);
        document.append("=");
        document.append(the_field);
        document.append(";");
        nTabs--;
    }

    protected void closeMethod() {
        document.append(CRLF);
        // formattazione del testo
        indent(nTabs);
        // fine formattazione
        document.append("}");
        nTabs--;
    }

    protected void openMethod(String the_format, String stringa) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("public ");
        document.append(the_format);
        document.append(" ");
        document.append(stringa);
        document.append(" {");
    }

    protected void writeMethodForeignAttribute(String the_format, String stringa, String bulkClass, String attributeName) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append(bulkClass + " " + attributeName + " = this.get" + TextUtil.capitalize(attributeName) + "();");
        document.append(CRLF);
        indent(nTabs);
        document.append("if (" + attributeName + " == null)");
        document.append(CRLF);
        indent(++nTabs);
        document.append("return null;");
        nTabs--;
        nTabs--;
    }

    protected String theFieldMethod(String the_field) {
        if (the_field.length() > 0) {
            return the_field.substring(0, 1).toUpperCase() + the_field.substring(1, the_field.length());
        }
        return null;
    }

    protected void methodGet(String the_format, String the_field) {
        String the_method = "get" + theFieldMethod(the_field) + "()";
        openMethod(the_format, the_method);
        addReturn(the_field);
        closeMethod();
    }

    protected void methodGetForeign(String the_format, String the_field, String returnType, String bulkClass, String attributeName) {
        String the_method = "get" + theFieldMethod(the_field) + "()";
        openMethod(the_format, the_method);
        writeMethodForeignAttribute(the_format, the_method, bulkClass, attributeName);
        addReturn(returnType);
        closeMethod();
    }

    protected void methodSetForeign(String the_format, String the_field, String attributeName, String foreignColumnAttribite) {
        String the_method = "set" + theFieldMethod(the_field) + "(" + the_format + " " + the_field + ") ";
        openMethod("void", the_method);
        addThisForeignAttribute(the_field, attributeName, foreignColumnAttribite);
        closeMethod();
    }

    protected void addThisForeignAttribute(String the_field, String attributeName, String foreignColumnAttribite) {
        document.append(CRLF);
        // formattazione del testo
        indent(++nTabs);
        // fine formattazione
        document.append("this.");
        document.append("get" + TextUtil.capitalize(attributeName) + "().");
        document.append("set" + TextUtil.capitalizeAndLower(foreignColumnAttribite) + "(");
        document.append(the_field);
        document.append(")");
        document.append(";");
        nTabs--;
    }

    protected void methodSetKeys(String the_format, String attribute, Vector<String> the_field) {
        document.append(CRLF);
        indent(++nTabs);
        document.append("set" + the_format + "( new " + attribute + "(");
        for (Enumeration campi = the_field.elements(); campi.hasMoreElements(); ) {
            String nome_campo = (String) campi.nextElement();
            document.append(nome_campo);
            if (!the_field.lastElement().equalsIgnoreCase(nome_campo))
                document.append(",");
        }
        document.append(") );");
        nTabs--;
    }

    protected void methodSet(String the_format, String the_field) {
        String the_method = "set" + theFieldMethod(the_field) + "(" + the_format + " " + the_field + ") ";
        openMethod("void", the_method);
        addThis(the_field);
        closeMethod();
    }

    protected void writeMethodAttributeBoolean(Vector<String> flag) {
        for (Iterator<String> i = flag.iterator(); i.hasNext(); ) {
            String name = i.next();
            String the_value = "set" + theFieldMethod(name) + "(new Boolean(false));";
            document.append(CRLF);
            indent(++nTabs);
            document.append(the_value);
            nTabs--;
        }
        document.append(CRLF);
        indent(++nTabs);
        document.append("return this;");
        nTabs--;
    }

    private void indent(int times) {
        for (int i = 0; i < times; i++) {
            document.append(TAB);

        }
    }

    protected String getType(ColumnMetaData c) {
        if (DatabaseUtil.checkSN.get(("TABLE-" + c.getTableName() + "-COLUMN-" + c.getColumnName()).toUpperCase()) != null) {
            return getClasse(Preferences.TYPE_CHAR_BOOLEAN);
        }
        if (c.getSqlTypeName().equalsIgnoreCase("char") || c.getSqlTypeName().equalsIgnoreCase("bpchar")) {
            if (c.getColumnName().toUpperCase().startsWith("FL_")) {
                return getClasse(Preferences.TYPE_CHAR_BOOLEAN);
            } else {
                return getClasse(Preferences.TYPE_CHAR);
            }
        }
        if (c.getSqlTypeName().equalsIgnoreCase("decimal") || c.getSqlTypeName().equalsIgnoreCase("int8")) {
            return getForDecimal(c);
        }
        return getClasse(Preferences.TYPE_PREFIX + c.getSqlTypeName());
    }

    /**
     * @param c
     * @return
     */
    private String getForDecimal(ColumnMetaData c) {
        if (c.getColumnScale() == 0) {
            int longSize = Long.valueOf(bean.getString(Preferences.TYPE_DECIMAL_SIZE)).intValue();
            if (c.getColumnSize() < longSize) {
                return getClasse(Preferences.TYPE_DECIMAL_INT);
            } else {
                return getClasse(Preferences.TYPE_DECIMAL_LONG);
            }
        }
        return getClasse(Preferences.TYPE_DECIMAL_BIG);
    }

    protected String getClasse(String key) {
        String stringa = bean.getString(key.toLowerCase());
        if (stringa == null || stringa.trim().length() < 1) {
            return bean.getString(Preferences.TYPE_UKNOWN);
        }
        return stringa;
    }

    /**
     * ritorna il contenuto generato (ver2.0)
     */
    public String getContents() {
        return document == null ? null : document.toString();
    }

    /**
     * genera il contenuto utilizzando il modello indicato
     */
    public abstract void generate(List columns) throws Exception;

    /**
     * ritorna il nome non qualificato della classe indicata
     */
    protected String getClassName(String fullName) {
        int from = fullName.length() - 1;
        int index = fullName.lastIndexOf('.', from);
        if (index >= 0)
            return fullName.substring(index + 1);
        return fullName;
    }
}
