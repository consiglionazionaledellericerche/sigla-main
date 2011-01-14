package it.cnr.contab.prevent00.bp;

/**
 * Classe che imposta la stampa dei Totali dei piani aggregati.
 * 
 * @author: Vincenzo Bisquadro
 */
public class CRUDTotaliPdGAggregatoBP  extends it.cnr.jada.util.action.BulkBP {
/**
 * Costruttore standard di CRUDTotaliPdGAggregatoBP.
 */
public CRUDTotaliPdGAggregatoBP() {
	super();
}
/**
 * Costruttore di CRUDTotaliPdGAggregatoBP cui viene passata la funzione in ingresso.
 *
 * @param funzione java.lang.String
 */
public CRUDTotaliPdGAggregatoBP(String function) {
	super(function);
}
/**
 * @see it.cnr.jada.util.action.BulkBP
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}	
	    
public String getPrintbp() {
 return "OfflineReportPrintBP";
}
/**
 * Inizializza la schermata presentando in primo piano il Tab dell'esercizio dell'anno corrente
 *
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @exception it.cnr.jada.action.BusinessProcessException
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
	}
}
