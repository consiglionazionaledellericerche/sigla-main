package it.cnr.contab.docamm00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:16 PM)
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiFatturazioneElettronicaBP extends ListaDocumentiAmministrativiBP {
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 */

public DocumentiAmministrativiFatturazioneElettronicaBP() {
	this("");
}
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 * @param function java.lang.String
 */
public DocumentiAmministrativiFatturazioneElettronicaBP(String function) {
	super(function+"Tr");
}
//protected final BulkHome getHome(UserContext usercontext, Class class1) throws ComponentException{
//    return (BulkHome)getHomeCache(usercontext).getHome(class1);
//}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk bulk = new Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}

public boolean isUtenteNonAbilitatoFirma(UserContext userContext) throws ApplicationException {
    try {
        return !UtenteBulk.isAbilitatoFirmaFatturazioneElettronica(userContext);
    } catch (ComponentException | RemoteException e) {
        throw new ApplicationException(e);
    }
}


/**
 * Ottiene il business process responsabile del documento amministativo docAmm.
 */
public IDocumentoAmministrativoBP getBusinessProcessForDocAmm(
		ActionContext context,
		IDocumentoAmministrativoBulk docAmm) 
		throws BusinessProcessException {

	IDocumentoAmministrativoBP docAmmBP = super.getBusinessProcessForDocAmm(context, docAmm);
	if (docAmm == null) return null;

	addChild((BusinessProcess)docAmmBP);
	
	return docAmmBP;
}
}
