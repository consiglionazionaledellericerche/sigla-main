package it.cnr.contab.docamm00.bp;
import java.util.List;

import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * <!-- @TODO: da completare -->
 */

public class FatturaAttivaRigaCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
	private boolean inventoriedChildDeleted = false;

public FatturaAttivaRigaCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}

/**
 * Restituisce true se è possibile aggiungere nuovi elementi
 */
public boolean isGrowable() {
	
	Fattura_attivaBulk fattura = (Fattura_attivaBulk)getParentModel();
	return	super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() &&
			!fattura.isPagata();
			//Tolto come da richiesta 423. Per NDC e NDD reimplementato
			//&&
			//fattura.getProtocollo_iva() == null &&
			//fattura.getProtocollo_iva_generale() == null;
	
}

/**
 * Restituisce il valore della proprietà 'inventoriedChildDeleted'
 *
 * @return Il valore della proprietà 'inventoriedChildDeleted'
 */
public boolean isInventoriedChildDeleted() {
	return inventoriedChildDeleted;
}

/**
 * Restituisce true se è possibile aggiungere nuovi elementi
 */
public boolean isShrinkable() {
	Fattura_attivaBulk fatturaA = (Fattura_attivaBulk)getParentModel();
	return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() &&
			!fatturaA.isPagata();
			//Tolto come da richiesta 423. Per NDC e NDD reimplementato
			//&&
			//fatturaA.getProtocollo_iva() == null &&
			//fatturaA.getProtocollo_iva_generale() == null;
}

/**
 * Imposta il valore della proprietà 'inventoriedChildDeleted'
 *
 * @param newInventoriedChildDeleted	Il valore da assegnare a 'inventoriedChildDeleted'
 */
public void setInventoriedChildDeleted(boolean newInventoriedChildDeleted) {
	inventoriedChildDeleted = newInventoriedChildDeleted;
}

public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
	if (context.getCurrentCommand().equals("doContabilizza"))
		return;
	try {
		((FatturaAttivaSingolaComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).validaRiga(context.getUserContext(), (Fattura_attiva_rigaBulk)model);
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw new ValidationException(e.getMessage());
	} catch (Throwable e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}

public void validateForDelete(ActionContext context,OggettoBulk model) throws ValidationException {
	try {
		Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk)model;
		if (riga.getTi_associato_manrev() != null && riga.ASSOCIATO_A_MANDATO.equalsIgnoreCase(riga.getTi_associato_manrev()))
			throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
											((riga.getDs_riga_fattura() != null) ?
												riga.getDs_riga_fattura() :
												String.valueOf(riga.getProgressivo_riga().longValue())) + 
											"\" perchè associato a mandato.");

		FatturaAttivaSingolaComponentSession comp = ((FatturaAttivaSingolaComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession()));
		comp.eliminaRiga(context.getUserContext(), riga);

		if (!riga.isPagata() && !riga.isToBeCreated()) {
			try {
				List result = comp.findManRevRigaCollegati(context.getUserContext(), riga);
				if (result!=null && !result.isEmpty())
					throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
							((riga.getDs_riga_fattura() != null) ?
								riga.getDs_riga_fattura() :
								String.valueOf(riga.getProgressivo_riga().longValue())) + 
							"\" perchè associato a mandati annullati.");
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			}
		}
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw new ValidationException(e.getMessage());
	} catch (ValidationException e) {
		throw e;
	} catch (Throwable e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}

public void writeHTMLToolbar(
		javax.servlet.jsp.PageContext context,
		boolean reset,
		boolean find,
		boolean delete) throws java.io.IOException, javax.servlet.ServletException {

		super.writeHTMLToolbar(context, reset, find, delete);

		String command = null;
		if (getParentController() != null)
			command = (getParentModel() instanceof it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk) ?
							"javascript:submitForm('doStornaDettagli')" :
							(getParentModel() instanceof it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk) ?
							"javascript:submitForm('doAddebitaDettagli')" :
							"javascript:submitForm('doRicercaAccertamento')";
		it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
			context,
			HttpActionContext.isFromBootstrap(context) ? "fa fa-fw fa-bolt" : "img/history16.gif",
			!(isInputReadonly() || getDetails().isEmpty() || ((CRUDFatturaAttivaBP)getParentController()).isSearching())? command : null,
			true,
			"Contabilizza",
			"btn-sm btn-outline-primary btn-title",
			HttpActionContext.isFromBootstrap(context));
		
		if (getParentController() instanceof CRUDFatturaAttivaIBP) {
			CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)getParentController();
			boolean enabled = bp.isDetailDoubleable() && 
							  (!(isInputReadonly() || getDetails().isEmpty() || bp.isSearching() || bp.isViewing()) ||
	                            bp.isManualModify()); 
	
			Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk)getModel();
			enabled = enabled && !(riga==null || riga.getTi_associato_manrev() != null && riga.ASSOCIATO_A_MANDATO.equalsIgnoreCase(riga.getTi_associato_manrev()));

			it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
					context,
					HttpActionContext.isFromBootstrap(context) ? "fa fa-fw fa-clipboard" : "img/bookmarks16.gif",
					enabled ? "javascript:submitForm('doSdoppiaDettaglio');" : null,
					true,
					"Sdoppia",
					"btn-sm btn-outline-success btn-title",
					HttpActionContext.isFromBootstrap(context));
		}
}

}