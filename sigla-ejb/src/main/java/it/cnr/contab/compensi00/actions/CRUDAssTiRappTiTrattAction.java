package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.tabrif.bulk.Ass_ti_rapp_ti_trattBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;

/**
 * Insert the type's description here.
 * Creation date: (11/03/2002 17.11.58)
 * @author: Roberto Fantino
 */
public class CRUDAssTiRappTiTrattAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDAssTiRappTiTrattAction constructor comment.
 */
public CRUDAssTiRappTiTrattAction() {
	super();
}
public Forward handleDuplicateKeyException(ActionContext context, it.cnr.jada.comp.CRUDDuplicateKeyException e) {

	setErrorMessage(context, "Errore! Si sta tentando di creare un oggetto già esistente in archivio");

	return context.findDefaultForward();
}
}
