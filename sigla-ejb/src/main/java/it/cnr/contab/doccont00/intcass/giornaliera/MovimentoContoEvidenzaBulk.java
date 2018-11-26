/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.doccont00.intcass.xmlbnl.*;
public class MovimentoContoEvidenzaBulk extends MovimentoContoEvidenzaBase {
	/**
	 * [INFORMAZIONI_CONTO_EVIDENZA null]
	 **/
	private InformazioniContoEvidenzaBulk informazioniContoEvidenza =  new InformazioniContoEvidenzaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza, java.lang.String stato, java.lang.Long progressivo) {
		super(esercizio, identificativoFlusso, contoEvidenza, stato, progressivo);
		setInformazioniContoEvidenza( new InformazioniContoEvidenzaBulk(esercizio,identificativoFlusso,contoEvidenza) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public InformazioniContoEvidenzaBulk getInformazioniContoEvidenza() {
		return informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setInformazioniContoEvidenza(InformazioniContoEvidenzaBulk informazioniContoEvidenza)  {
		this.informazioniContoEvidenza=informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getInformazioniContoEvidenza().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getIdentificativoFlusso();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.getInformazioniContoEvidenza().setIdentificativoFlusso(identificativoFlusso);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [contoEvidenza]
	 **/
	public java.lang.String getContoEvidenza() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getContoEvidenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [contoEvidenza]
	 **/
	public void setContoEvidenza(java.lang.String contoEvidenza)  {
		this.getInformazioniContoEvidenza().setContoEvidenza(contoEvidenza);
	}
}