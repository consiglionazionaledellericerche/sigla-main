/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.doccont00.intcass.xmlbnl.*;
public class InformazioniContoEvidenzaBulk extends InformazioniContoEvidenzaBase {
	private BulkList<MovimentoContoEvidenzaBulk>  movConto=new BulkList<MovimentoContoEvidenzaBulk>();

	/**
	 * [FLUSSO_GIORNALE_DI_CASSA null]
	 **/
	private FlussoGiornaleDiCassaBulk flussoGiornaleDiCassa =  new FlussoGiornaleDiCassaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: INFORMAZIONI_CONTO_EVIDENZA
	 **/
	public InformazioniContoEvidenzaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: INFORMAZIONI_CONTO_EVIDENZA
	 **/
	public InformazioniContoEvidenzaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza) {
		super(esercizio, identificativoFlusso, contoEvidenza);
		setFlussoGiornaleDiCassa( new FlussoGiornaleDiCassaBulk(esercizio,identificativoFlusso) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public FlussoGiornaleDiCassaBulk getFlussoGiornaleDiCassa() {
		return flussoGiornaleDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setFlussoGiornaleDiCassa(FlussoGiornaleDiCassaBulk flussoGiornaleDiCassa)  {
		this.flussoGiornaleDiCassa=flussoGiornaleDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		FlussoGiornaleDiCassaBulk flussoGiornaleDiCassa = this.getFlussoGiornaleDiCassa();
		if (flussoGiornaleDiCassa == null)
			return null;
		return getFlussoGiornaleDiCassa().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getFlussoGiornaleDiCassa().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		FlussoGiornaleDiCassaBulk flussoGiornaleDiCassa = this.getFlussoGiornaleDiCassa();
		if (flussoGiornaleDiCassa == null)
			return null;
		return getFlussoGiornaleDiCassa().getIdentificativoFlusso();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.getFlussoGiornaleDiCassa().setIdentificativoFlusso(identificativoFlusso);
	}
	public BulkList<MovimentoContoEvidenzaBulk> getMovConto() {
		return movConto;
	}
	public void setMovConto(BulkList<MovimentoContoEvidenzaBulk> movConto) {
		this.movConto = movConto;
	}
	public int addToMovConto( MovimentoContoEvidenzaBulk doc ) {
		movConto.add(doc);
		doc.setInformazioniContoEvidenza(this);		
		return movConto.size()-1;
	}
	@Override
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		return new it.cnr.jada.bulk.BulkCollection[] { 
				movConto				
		};
	}
}