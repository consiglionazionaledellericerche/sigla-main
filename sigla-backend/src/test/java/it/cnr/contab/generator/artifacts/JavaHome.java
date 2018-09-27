package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Generation;

import java.util.List;

/**
 * Created on 18-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [22-aug-2006] refactoring (generate + pulizia)
 */
public class JavaHome extends ClassBase {

    public JavaHome(GeneratorBean bean) {
        super(bean);
    }

    public void generate(List columns) throws Exception {
        addPackage();
        addImports(Generation.home_Connection);
        addImports(Generation.home_BulkHome);
        addImports(Generation.home_PersistentCache);
// classe
        StringBuffer sb = new StringBuffer(the_class);
        sb.append(Generation.java_home);
        sb.append(" extends ");
        sb.append(getClassName(Generation.home_BulkHome));
        openClass(sb.toString());

// Primo costruttore
        sb = new StringBuffer(the_class);
        sb.append(Generation.java_home);
        sb.append("(");
        sb.append(getClassName(Generation.home_Connection));
        sb.append(" conn)");
        addConstructor(sb.toString());
        sb = new StringBuffer(the_class);
        sb.append(Generation.java_bulk);
        sb.append(".class, conn");
        addSuper(sb.toString());
        closeConstructor();

// Secondo costruttore
        sb = new StringBuffer(the_class);
        sb.append(Generation.java_home);
        sb.append("(");
        sb.append(getClassName(Generation.home_Connection));
        sb.append(" conn, ");
        sb.append(getClassName(Generation.home_PersistentCache));
        sb.append(" persistentCache)");
        addConstructor(sb.toString());
        sb = new StringBuffer(the_class);
        sb.append(Generation.java_bulk);
        sb.append(".class, conn, persistentCache");
        addSuper(sb.toString());
        closeConstructor();

// close calsse     
        closeClass();
    }

}
