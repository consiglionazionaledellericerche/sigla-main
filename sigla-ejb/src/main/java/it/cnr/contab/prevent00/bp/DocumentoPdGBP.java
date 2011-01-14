package it.cnr.contab.prevent00.bp;
/**
 * Classe che imposta la stampa dei documenti dei piani di gestione.
 * 
 */
public class DocumentoPdGBP  extends it.cnr.jada.util.action.FormBP {
/**
 * Costruttore standard di CRUDTotaliPdGAggregatoBP.
 */
public DocumentoPdGBP() {
	super();
}

/**
 * Costruttore di CRUDTotaliPdGAggregatoBP cui viene passata la funzione in ingresso.
 *
 * @param funzione java.lang.String
 */
public DocumentoPdGBP(String function) {
	super(function);
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