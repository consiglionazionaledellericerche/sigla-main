package it.cnr.contab.generator.artifacts;

import java.util.List;

/**
 * Provvede i metodi di generazione e di accesso al contenuto generato
 *
 * @author Marco Spasiano
 * @version 1.0
 * [21-Aug-2006] creazione
 * [22-Aug-2006] aggiunto contratto generate(List columns)
 */
public interface ArtifactContents {

    public String getContents();

    public void generate(List columns) throws Exception;
}
