/*
* Created by Generator 1.0
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_stipendi_cofi_dettBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    MESE DECIMAL(2,0)
	private java.lang.Integer mese;
 
//    TIPO_FLUSSO VARCHAR(1)
	private java.lang.String tipo_flusso;
 
//    ENTRATA_SPESA CHAR(1)
	private java.lang.String entrata_spesa;
 
	public V_stipendi_cofi_dettBase() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getMese () {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.lang.String getTipo_flusso () {
		return tipo_flusso;
	}
	public void setTipo_flusso(java.lang.String tipo_flusso)  {
		this.tipo_flusso=tipo_flusso;
	}
	public java.lang.String getEntrata_spesa () {
		return entrata_spesa;
	}
	public void setEntrata_spesa(java.lang.String entrata_spesa)  {
		this.entrata_spesa=entrata_spesa;
	}
}