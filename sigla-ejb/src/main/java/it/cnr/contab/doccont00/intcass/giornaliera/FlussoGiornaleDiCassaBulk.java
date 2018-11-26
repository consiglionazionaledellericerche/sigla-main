/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
public class FlussoGiornaleDiCassaBulk extends FlussoGiornaleDiCassaBase {
	
	private BulkList<InformazioniContoEvidenzaBulk>  infoConto=new BulkList<InformazioniContoEvidenzaBulk>();
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso) {
		super(esercizio, identificativoFlusso);
	}
	public BulkList<InformazioniContoEvidenzaBulk> getInfoConto() {
		return infoConto;
	}
	public void setInfoConto(BulkList<InformazioniContoEvidenzaBulk> infoConto) {
		this.infoConto = infoConto;
	}
	public int addToInfoConto( InformazioniContoEvidenzaBulk doc ) {
		infoConto.add(doc);
		doc.setFlussoGiornaleDiCassa(this);		
		return infoConto.size()-1;
	}
	@Override
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		return new it.cnr.jada.bulk.BulkCollection[] { 
				infoConto				
		};
	}
}