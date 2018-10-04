package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.model.TableMetaData;
import it.cnr.contab.generator.util.DatabaseUtil;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Genera gli artefatti (java e xml) e li colloca nel dominio del modello eclipse
 *
 * @author Marco Spasiano
 * @version 1.0 [21-Aug-2006]
 */
public class ArtifactGenerator {
    private GeneratorBean bean;
    private ArtifactNames names;
    private List columns;

    public ArtifactGenerator(GeneratorBean bean) {
        this.bean = bean;
    }

    public void generate() throws InvocationTargetException, InterruptedException {
        try {
            doGeneration();
            DatabaseUtil.getInstance().closeConnectionQuietly();
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            String stackTrace = sw.toString();
            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            } else {
                DatabaseUtil.getInstance().closeConnectionQuietly();
                throw new InvocationTargetException(new Exception(stackTrace));
            }

        }
    }

    public void doGeneration() throws Exception {
        // creazione dei modelli dati
        columns = new TableMetaData(bean).getTableMetaData();
        names = new ArtifactNames(bean);

        generateJava();
        generateXML();

    }

    private void generateXML() throws Exception {
        for (int i = 0; i < names.getArtifactNames().length; i++) {
            if (names.isXMLName(i)) {
                ArtifactContents generator = names.getGenerator(i);
                generator.generate(columns);
                createFile(new File(bean.getTargetXMLFolder() + names.getArtifactName(i)), getContents(generator));
            }
        }
    }

    private void generateJava() throws Exception {
        for (int i = 0; i < names.getArtifactNames().length; i++) {
            if (names.isJavaName(i)) {
                ArtifactContents generator = names.getGenerator(i);
                generator.generate(columns);
                createFile(new File(bean.getSourceFolder() + names.getArtifactName(i)), getContents(generator));
            }
        }
    }

    private InputStream getContents(ArtifactContents contents) {
        return new ByteArrayInputStream(contents.getContents().getBytes());

    }

    /**
     * Creates a file resource given the file handle and contents.
     */
    protected void createFile(File fileHandle, InputStream contents) throws Exception {
        if (contents == null)
            contents = new ByteArrayInputStream(new byte[0]);

        IOUtils.copy(contents, new FileOutputStream(fileHandle));
    }

}
