package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;

public class CRUDReversaleRigaController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public CRUDReversaleRigaController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
public void remove(it.cnr.jada.action.ActionContext context) 	throws ValidationException, it.cnr.jada.action.BusinessProcessException {
	super.remove( context );
	((ReversaleBulk)getParentModel()).refreshImporto();

}
/*
caso 1 - riga non ancora inserita nel db: se l'utente ne richiede la cancellazione viene effettivamente rimossa
         se viene richiesta la cancellazione di una fattura con associate note a debito/credito, anche questi doc. amm
         devono essere rimossi
caso 2 - NON USATO - riga già salvata nel db: se l'utente ne richiede la cancellazione viene annullata
*/
public OggettoBulk removeDetail(int index) 
{
	Reversale_rigaIBulk rigaDaCancellare = (Reversale_rigaIBulk) getDetails().get( index );
	Reversale_rigaIBulk row;
	// la riga non e' ancora stata inserita nel db --> la cancello
	if ( rigaDaCancellare != null && rigaDaCancellare.isToBeCreated() && rigaDaCancellare.isFlCancellazione())
	{
		int len = getDetails().size();
		for ( int i = len - 1; i >= 0 ; i--)
		{
			row = (Reversale_rigaIBulk) getDetails().get(i);
			if ( row.getEsercizio_accertamento().equals( rigaDaCancellare.getEsercizio_accertamento()) &&
				 row.getCd_cds().equals( rigaDaCancellare.getCd_cds()) &&
				 row.getEsercizio_ori_accertamento().equals( rigaDaCancellare.getEsercizio_ori_accertamento()) &&
				 row.getPg_accertamento().equals( rigaDaCancellare.getPg_accertamento()) &&
				 row.getPg_accertamento_scadenzario().equals( rigaDaCancellare.getPg_accertamento_scadenzario()))
			{
				row = (Reversale_rigaIBulk) ((ReversaleBulk)getParentModel()).removeFromReversale_rigaColl( i );
				row.setToBeDeleted();
			}
		}
		return rigaDaCancellare;
	}
	// la riga e' già stata inserita nel db e il suo stato e' != da STATO_ANNULLATO --> aggiorno lo stato
	else if ( rigaDaCancellare != null && !rigaDaCancellare.isToBeCreated() && !rigaDaCancellare.getStato().equals( rigaDaCancellare.STATO_ANNULLATO))
	{
		rigaDaCancellare.annulla();
		return rigaDaCancellare;
	}
	else 
		return rigaDaCancellare;
}
/* Non e' possibile cancellare note a credito / debito; e' necessario cancellare la fattura attiva da cui dipendono */
public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException 
{
	if ( detail instanceof Reversale_rigaIBulk && !((Reversale_rigaIBulk)detail).isFlCancellazione())
	{
		Reversale_rigaIBulk rigaDaCancellare = (Reversale_rigaIBulk) detail;
		Reversale_rigaIBulk row = null;
		int len = getDetails().size();
		Long pg_fattura_attiva = new Long(0);
		//ricerco il progressivo della fattura attiva da cui la nota di debito/credito dipende
		int index; 
		for ( index = len - 1; index >= 0 ; index--)
		{
			row = (Reversale_rigaIBulk) getDetails().get(index);
			if ( row.getEsercizio_accertamento().equals( rigaDaCancellare.getEsercizio_accertamento()) &&
				 row.getCd_cds().equals( rigaDaCancellare.getCd_cds()) &&
				 row.getEsercizio_ori_accertamento().equals( rigaDaCancellare.getEsercizio_ori_accertamento()) &&
				 row.getPg_accertamento().equals( rigaDaCancellare.getPg_accertamento()) &&
				 row.getPg_accertamento_scadenzario().equals( rigaDaCancellare.getPg_accertamento_scadenzario()) &&
				 row.isFlCancellazione())
			{
				pg_fattura_attiva = row.getPg_doc_amm();
				break;
			}
			
		}
		//se la fattura attiva non e' stata selezionata per essere cancellata segnalo l'errore
		if ( row != null && !getSelection().isSelected(index) )	
		throw new ValidationException( "Cancellazione impossibile: e' necessario rimuovere la fattura attiva " + pg_fattura_attiva + " da cui " +
			"il documento amministrativo " + ((Reversale_rigaIBulk)detail).getPg_doc_amm() + " dipende");
	}	
}
/**
 * Metodo per aggiungere alla toolbar del Controller un tasto necessario all'utente
 * per la ricerca rapida della riga da quadrare con il SIOPE
 * @param context Il contesto dell'azione
 * @param scadenza La scadenza dell'oggetto bulk in uso
 */
public void writeHTMLToolbar(
		javax.servlet.jsp.PageContext context,
		boolean reset,
		boolean find,
		boolean delete) throws java.io.IOException, javax.servlet.ServletException {

		super.writeHTMLToolbar(context, reset, find, delete);

		if (getParentController() instanceof CRUDReversaleBP) {
			CRUDReversaleBP bp = (CRUDReversaleBP)getParentController();
			if (bp.isSiope_attiva() && !((ReversaleBulk)getParentModel()).isSiopeTotalmenteAssociato()){
				String command = "javascript:submitForm('doSelezionaRigaSiopeDaCompletare')";
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
					context,
					"img/find16.gif",
					!(isInputReadonly() || getDetails().isEmpty() || ((CRUDReversaleBP)getParentController()).isSearching())? command : null,
					true,"SIOPE - Vai a riga successiva da completare",
					HttpActionContext.isFromBootstrap(context));
			}
		}
}
}
