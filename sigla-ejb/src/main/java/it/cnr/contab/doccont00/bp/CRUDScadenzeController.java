package it.cnr.contab.doccont00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.docamm00.bp.CRUDDocumentoGenericoAttivoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.VoidableBP;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.doccont00.comp.CheckDisponibilitaCdrGAEFailed;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;

public class CRUDScadenzeController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public CRUDScadenzeController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 * @param context Il contesto dell'azione
 * @param scadenza La scadenza dell'accertamento
 */
public void validateForDelete(ActionContext context, Accertamento_scadenzarioBulk scadenza) throws ValidationException 
{
	if ( scadenza.getIm_associato_doc_amm().compareTo( new java.math.BigDecimal(0)) != 0 )
		throw new ValidationException( "Impossibile cancellare una scadenza che e' stata associata a documenti amministrativi");

}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 * @param context Il contesto dell'azione
 * @param scadenza La scadenza dell'obbligazione
 */
public void validateForDelete(ActionContext context, Obbligazione_scadenzarioBulk scadenza) throws ValidationException 
{
	if ( scadenza.getIm_associato_doc_amm().compareTo( new java.math.BigDecimal(0)) != 0 )
		throw new ValidationException( "Impossibile cancellare una scadenza che e' stata associata a documenti amministrativi");

}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 * @param context Il contesto dell'azione
 * @param scadenza La scadenza dell'oggetto bulk in uso
 */
public void validateForDelete(ActionContext context, OggettoBulk scadenza) throws ValidationException 
{
	if ( scadenza instanceof Obbligazione_scadenzarioBulk )
		validateForDelete( context, (Obbligazione_scadenzarioBulk) scadenza );
	else if ( scadenza instanceof Accertamento_scadenzarioBulk )
		validateForDelete( context, (Accertamento_scadenzarioBulk) scadenza );
}
/**
 * Metodo per aggiungere alla toolbar del Controller un tasto necessario per accorpare
 * le righe di scadenza dell'obbligazione.
 * @param context Il contesto dell'azione
 * @param scadenza La scadenza dell'oggetto bulk in uso
 */
public void writeHTMLToolbar(
		javax.servlet.jsp.PageContext context,
		boolean reset,
		boolean find,
		boolean delete) throws java.io.IOException, javax.servlet.ServletException {

		super.writeHTMLToolbar(context, reset, find, delete);

		if (getParentController() instanceof CRUDObbligazioneBP) {
			String command = "javascript:submitForm('doRaggruppaScadenze')";
			it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
				context,
				HttpActionContext.isFromBootstrap(context)?"fa fa-fw fa-object-group text-primary":"img/properties16.gif",
				!(isInputReadonly() || getDetails().isEmpty() || ((CRUDObbligazioneBP)getParentController()).isSearching())? command : null,
				true,
				"Raggruppa",
				"btn-sm btn-secondary btn-outline-secondary btn-title",
				HttpActionContext.isFromBootstrap(context));
		}
}
}
