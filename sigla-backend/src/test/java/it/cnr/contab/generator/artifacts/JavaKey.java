package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.ColumnMetaData;
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
public class JavaKey extends ClassBase {

    public JavaKey(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        addPackage();
        addImports(Generation.bulk_OggettoBulk);
        addImports(Generation.key_KeyedPersistent);
// classe
        StringBuffer sb = new StringBuffer(the_class);
        sb.append(Generation.java_key);
        sb.append(" extends ");
        sb.append(getClassName(Generation.bulk_OggettoBulk));
        sb.append(" implements ");
        sb.append(getClassName(Generation.key_KeyedPersistent));
        openClass(sb.toString());

        Iterator it = columns.iterator();
        String allField = "";
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                allField = allField
                        + getType(c)
                        + " "
                        + c.getPropertyName()
                        + ", ";
                addField(getType(c), c.getPropertyName());
            }
        }
        if (allField == null || allField.length() < 1) {
        } else {
            allField = allField.substring(0, allField.length() - 2);
        }
        // Primo Constructor
        addCommentConstructor(the_table);
        addConstructor(the_class
                + Generation.java_key
                + "()");
        addSuper();
        closeConstructor();
        // Secondo Constructor
        addConstructor(the_class
                + Generation.java_key
                + "(" + allField + ")");
        addSuper();
        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                addThis(c.getPropertyName());
            }
        }
        closeConstructor();
        // primo metodo (equalsByPrimaryKey)
        openMethod("boolean", "equalsByPrimaryKey(Object o)");
        addStatement("if (this== o) return true");
        addStatement("if (!(o instanceof "
                + the_class
                + Generation.java_key
                + ")) return false");
        addStatement(the_class
                + Generation.java_key
                + " k = ("
                + the_class
                + Generation.java_key
                + ") o");
        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                String get = "get"
                        + theFieldMethod(c.getPropertyName())
                        + "()";
                addStatement("if (!compareKey("
                        + get
                        + ", k."
                        + get
                        + ")) return false");
            }
        }
        addStatement("return true");
        closeMethod();
// secondo metodo (primaryKeyHashCode)
        openMethod("int", "primaryKeyHashCode()");
        addStatement("int i = 0");
        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                String get = "get"
                        + theFieldMethod(c.getPropertyName())
                        + "()";
                addStatement("i = i + calculateKeyHashCode("
                        + get
                        + ")");
            }
        }
        addStatement("return i");
        closeMethod();
// metodo get and metodo set
        it = columns.iterator();
        while (it.hasNext()) {
            ColumnMetaData c = (ColumnMetaData) it.next();
            if (c.isPrimary()) {
                addCommentGetSet(c.getLabel(), "GET");
                methodSet(getType(c), c.getPropertyName());
                addCommentGetSet(c.getLabel(), "SET");
                methodGet(getType(c), c.getPropertyName());
            }
        }

        closeClass();
    }

}
