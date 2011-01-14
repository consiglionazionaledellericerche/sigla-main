/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import it.cnr.jada.persistency.Keyed;
public class Gruppo_fileBase extends Gruppo_fileKey implements Keyed {
//    DS_GRUPPO_FILE VARCHAR(300) NOT NULL
	private java.lang.String ds_gruppo_file;
 
	public Gruppo_fileBase() {
		super();
	}
	public Gruppo_fileBase(java.lang.String cd_gruppo_file) {
		super(cd_gruppo_file);
	}
	public java.lang.String getDs_gruppo_file() {
		return ds_gruppo_file;
	}
	public void setDs_gruppo_file(java.lang.String ds_gruppo_file)  {
		this.ds_gruppo_file=ds_gruppo_file;
	}
}