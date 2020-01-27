package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.action.HttpActionContext;

/**
 * Riga del documento generico passivo
 */

public class OrdineAcqRigaCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    public OrdineAcqRigaCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    public boolean isGrowable() {

        OrdineAcqBulk ordine = (OrdineAcqBulk) getParentModel();
        return super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() &&
                !ordine.isAnnullato();
    }
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 4:23:46 PM)
 * @return boolean
 */
    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isShrinkable() {
        OrdineAcqBulk ordine = (OrdineAcqBulk) getParentModel();
        return super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching() &&
                !ordine.isAnnullato();
        //Tolto come da richiesta 423
        //&&
        //fatturaP.getProtocollo_iva() == null &&
        //fatturaP.getProtocollo_iva_generale() == null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/11/2001 4:23:46 PM)
     *
     * @param newInventoriedChildDeleted boolean
     */
//public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
//	try {
//		((FatturaPassivaComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).validaRiga(context.getUserContext(), (Fattura_passiva_rigaBulk)model);
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw new ValidationException(e.getMessage());
//	} catch (Throwable e) {
//		throw new it.cnr.jada.DetailedRuntimeException(e);
//	}
//}
//public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
//	try {
//		Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk)detail;
//		if (fpr.getTi_associato_manrev() != null && fpr.ASSOCIATO_A_MANDATO.equalsIgnoreCase(fpr.getTi_associato_manrev()))
//			throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
//											((fpr.getDs_riga_fattura() != null) ?
//												fpr.getDs_riga_fattura() :
//												String.valueOf(fpr.getProgressivo_riga().longValue())) + 
//											"\" perchè associato a mandato.");
//		FatturaPassivaComponentSession comp = ((FatturaPassivaComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession()));
//		comp.eliminaRiga(context.getUserContext(), fpr);
//
//		if (!fpr.isPagata() && !fpr.isToBeCreated()) {
//			try {
//				List result = comp.findManRevRigaCollegati(context.getUserContext(), fpr);
//				if (result!=null && !result.isEmpty())
//					throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
//							((fpr.getDs_riga_fattura() != null) ?
//								fpr.getDs_riga_fattura() :
//								String.valueOf(fpr.getProgressivo_riga().longValue())) + 
//							"\" perchè associato a mandato annullato.");
//			} catch (PersistencyException e) {
//				throw new ComponentException(e);
//			} catch (IntrospectionException e) {
//				throw new ComponentException(e);
//			}
//		}
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw new ValidationException(e.getMessage());
//	} catch (ValidationException e) {
//		throw e;
//	} catch (Throwable e) {
//		throw new it.cnr.jada.DetailedRuntimeException(e);
//	}
//}
    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        super.writeHTMLToolbar(context, reset, find, delete, false);
        boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
        String command = null;
        if (getParentController() != null)
            command = "javascript:submitForm('doRicercaObbligazione')";
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getParentController();
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                context,
                isFromBootstrap ? "fa fa-fw fa-bolt" : "img/history16.gif",
                !(isInputReadonly() || getDetails().isEmpty() || bp.isSearching()) ? command : null,
                true,
                "Crea/Associa Impegni",
                "btn-sm btn-outline-primary btn-title",
                isFromBootstrap);

        boolean enabled = (!(isInputReadonly() || getDetails().isEmpty() || bp.isSearching() || bp.isViewing()) ||
                bp.isManualModify());
        super.closeButtonGROUPToolbar(context);

    }
}