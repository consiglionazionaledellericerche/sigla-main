package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
import it.cnr.contab.generator.model.Filter;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Generation;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] refactoring (generate + pulizia)
 */
public class JavaBase extends ClassBase {

    public JavaBase(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        Filter f = new Filter();
        addPackage();
        addImports(Generation.base_Keyed);

        // classe
        StringBuffer sb = new StringBuffer();
        sb.append(the_class);
        sb.append(Generation.java_base);
        sb.append(" extends ");
        sb.append(the_class);
        sb.append(Generation.java_key);
        sb.append(" implements ");
        sb.append(getClassName(Generation.base_Keyed));

        openClass(sb.toString());

        // Campi della classe
        Iterator it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (f.isFilter(c.getColumnName()) || c.isPrimary()) {
                continue;
            }
            sb = new StringBuffer(c.getColumnName());
            sb.append(' ');
            sb.append(c.getSqlTypeName());
            sb.append('(');
            sb.append(c.getColumnSize());
            if (c.getSqlTypeName().equals("DECIMAL")) {
                sb.append(',');
                sb.append(c.getColumnScale());
            }
            sb.append(')');
            if (!c.isNullable()) {
                sb.append(" NOT NULL");
            }
            addRowComment(sb.toString());
            addField(getType(c), c.getPropertyName());
            addRow(" ");

        }
        // Primo Constructor
        addCommentConstructor(the_table);
        addConstructor(the_class + Generation.java_base + "()");
        addSuper();
        closeConstructor();
        // Secondo Constructor
        it = columns.iterator();
        String allFieldFormat = "";
        String allField = "";
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                allFieldFormat =
                        allFieldFormat
                                + getType(c)
                                + " "
                                + c.getPropertyName()
                                + ", ";
                allField = allField + c.getPropertyName() + ", ";
            }

        }
        if (allFieldFormat == null || allFieldFormat.length() < 1) {
        } else {
            allFieldFormat =
                    allFieldFormat.substring(0, allFieldFormat.length() - 2);
        }
        if (allField == null || allField.length() < 1) {
        } else {
            allField = allField.substring(0, allField.length() - 2);
        }

        addConstructor(the_class + Generation.java_base + "(" + allFieldFormat + ")");
        addSuper(allField);
        closeConstructor();
        // metodi Get e Set
        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (f.isFilter(c.getColumnName()) || c.isPrimary()) {
                continue;
            }
            addCommentGetSet(c.getLabel(), "GET");
            methodGet(getType(c), c.getPropertyName());
            addCommentGetSet(c.getLabel(), "SET");
            methodSet(getType(c), c.getPropertyName());
        }

        closeClass();
    }

}
