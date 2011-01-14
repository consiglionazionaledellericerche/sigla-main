package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:16 PM)
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiProtocollabiliBP extends ListaDocumentiAmministrativiBP {
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 */
public DocumentiAmministrativiProtocollabiliBP() {
	this("");
}
/**
 * DocumentiAmministrativiProtocollabiliBP constructor comment.
 * @param function java.lang.String
 */
public DocumentiAmministrativiProtocollabiliBP(String function) {
	super(function+"Tr");
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Filtro_ricerca_doc_ammVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Filtro_ricerca_doc_amm_protocollabileVBulk bulk = new Filtro_ricerca_doc_amm_protocollabileVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
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
