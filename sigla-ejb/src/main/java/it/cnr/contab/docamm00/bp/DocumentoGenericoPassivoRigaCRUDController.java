package it.cnr.contab.docamm00.bp;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Riga del documento generico passivo
 */

public class DocumentoGenericoPassivoRigaCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public DocumentoGenericoPassivoRigaCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}

public boolean isGrowable() {
	
	Documento_genericoBulk doc = (Documento_genericoBulk)getParentModel();
	return	super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() &&
			!doc.STATO_PAGATO.equalsIgnoreCase(doc.getStato_cofi()) &&
			!doc.isDoc1210Associato();
}

public boolean isShrinkable() {
	
	Documento_genericoBulk doc = (Documento_genericoBulk)getParentModel();
	return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() &&
			!doc.STATO_PAGATO.equalsIgnoreCase(doc.getStato_cofi()) &&
			!doc.isDoc1210Associato();
}

public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
	if (context.getCurrentCommand().equals("doContabilizzaObbligazioni"))
		return;
	try {
		if ((Documento_generico_rigaBulk)model!=null && (((Documento_generico_rigaBulk)model).getTerzo()==null || ((Documento_generico_rigaBulk)model).getTerzo().getAnagrafico()==null))
			throw new ValidationException("Il campo anagrafica e' obbligatorio");
		if ((Documento_generico_rigaBulk)model!=null && ((Documento_generico_rigaBulk)model).getDs_riga()==null)
			throw new ValidationException("Inserire una descrizione");
		if ((Documento_generico_rigaBulk)model!=null && (((Documento_generico_rigaBulk)model).getIm_riga()==null || ((Documento_generico_rigaBulk)model).getIm_riga().compareTo(new java.math.BigDecimal(0))==0))
			throw new ValidationException("Inserire un importo positivo");
		if ((Documento_generico_rigaBulk)model!=null && ((Documento_generico_rigaBulk)model).getBanca()==null)
			throw new ValidationException("Inserire dei riferimenti bancari corretti");
		((DocumentoGenericoComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).validaRiga(context.getUserContext(), (Documento_generico_rigaBulk)model);
	} catch (ValidationException e) {
		throw e;
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw new ValidationException(e.getMessage());
	} catch (Throwable e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}

public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
	try {
		Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)detail;
		if (riga.getTi_associato_manrev() != null && riga.ASSOCIATO_A_MANDATO.equalsIgnoreCase(riga.getTi_associato_manrev()))
			throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
											((riga.getDs_riga() != null) ?
												riga.getDs_riga() :
												String.valueOf(riga.getProgressivo_riga().longValue())) + 
											"\" perchè associato a mandato.");
		((DocumentoGenericoComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).eliminaRiga(context.getUserContext(), (Documento_generico_rigaBulk)detail);
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

		String command = "javascript:submitForm('doRicercaObbligazione')";
		it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
			context,
			"img/history16.gif",
			!(isInputReadonly() || getDetails().isEmpty() || ((CRUDDocumentoGenericoPassivoBP)getParentController()).isSearching())? command : null,
			true,"Contabilizza");
}
}