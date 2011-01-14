/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_fileBase extends Tipo_fileKey implements Keyed {
//    DS_TIPO_FILE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_file;
 
//    ESTENSIONE_FILE VARCHAR(10) NOT NULL
	private java.lang.String estensione_file;
 
	public Tipo_fileBase() {
		super();
	}
	public Tipo_fileBase(java.lang.String cd_tipo_file) {
		super(cd_tipo_file);
	}
	public java.lang.String getDs_tipo_file() {
		return ds_tipo_file;
	}
	public void setDs_tipo_file(java.lang.String ds_tipo_file)  {
		this.ds_tipo_file=ds_tipo_file;
	}
	public java.lang.String getEstensione_file() {
		return estensione_file;
	}
	public void setEstensione_file(java.lang.String estensione_file)  {
		this.estensione_file=estensione_file;
	}
}