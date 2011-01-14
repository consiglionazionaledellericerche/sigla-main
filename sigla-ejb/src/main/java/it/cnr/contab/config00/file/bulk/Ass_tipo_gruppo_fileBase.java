/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_gruppo_fileBase extends Ass_tipo_gruppo_fileKey implements Keyed {
	public Ass_tipo_gruppo_fileBase() {
		super();
	}
	public Ass_tipo_gruppo_fileBase(java.lang.String cd_tipo_file, java.lang.String cd_gruppo_file) {
		super(cd_tipo_file, cd_gruppo_file);
	}
}