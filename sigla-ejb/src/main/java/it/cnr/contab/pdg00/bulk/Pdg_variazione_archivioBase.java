/*
* Created by Generator 1.0
* Date 21/07/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_variazione_archivioBase extends Pdg_variazione_archivioKey implements Keyed {
//    TIPO_ARCHIVIO VARCHAR(1) NOT NULL
	private java.lang.String tipo_archivio;
 
//    BDATA BLOB(4000)
	private java.lang.String bdata;
 
	public Pdg_variazione_archivioBase() {
		super();
	}
	public Pdg_variazione_archivioBase(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Long progressivo_riga) {
		super(esercizio, pg_variazione_pdg, progressivo_riga);
	}
	public java.lang.String getTipo_archivio () {
		return tipo_archivio;
	}
	public void setTipo_archivio(java.lang.String tipo_archivio)  {
		this.tipo_archivio=tipo_archivio;
	}
	public java.lang.String getBdata () {
		return bdata;
	}
	public void setBdata(java.lang.String bdata)  {
		this.bdata=bdata;
	}
}