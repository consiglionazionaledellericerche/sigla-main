/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_archivioBase extends Incarichi_archivioKey implements Keyed {
//  NOME_FILE  VARCHAR2(400) NOT NULL
	private java.lang.String nome_file;

//    DS_FILE VARCHAR(2000)
	private java.lang.String ds_file;

//    TIPO_ARCHIVIO VARCHAR(1) NOT NULL
	private java.lang.String tipo_archivio;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;

	public Incarichi_archivioBase() {
		super();
	}
	public java.lang.String getTipo_archivio() {
		return tipo_archivio;
	}
	public void setTipo_archivio(java.lang.String tipo_archivio)  {
		this.tipo_archivio=tipo_archivio;
	}
	public java.lang.String getNome_file() {
		return nome_file;
	}
	public void setNome_file(java.lang.String nome_file) {
		this.nome_file = nome_file;
	}
	public java.lang.String getDs_file() {
		return ds_file;
	}
	public void setDs_file(java.lang.String ds_file) {
		this.ds_file = ds_file;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}