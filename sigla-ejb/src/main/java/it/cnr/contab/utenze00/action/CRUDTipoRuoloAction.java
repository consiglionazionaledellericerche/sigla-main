package it.cnr.contab.utenze00.action;

/**
 * Action che gestisce l'interfaccia di Gestione Tipo Ruoli: in particolare gestisce l'aggiunta e la rimozione di una
 * privilegio ad un Ruolo
 *	
 */

import it.cnr.contab.utenze00.bp.CRUDTipoRuoloBP;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class CRUDTipoRuoloAction extends CRUDAction {
/**
 * CRUDRuoloAction constructor comment.
 */
public CRUDTipoRuoloAction() {
	super();
}
/**
 * Aggiunge un nuovo accesso alla lista di Privilegi associati ad un Tipo Ruolo
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo 
 */


public Forward doAggiungiPrivilegio(ActionContext context) throws FillException
{
	CRUDTipoRuoloBP bp = (CRUDTipoRuoloBP)context.getBusinessProcess();
	fillModel(context);
	Tipo_ruoloBulk ruolo = (Tipo_ruoloBulk)bp.getModel();
	int[] indexes = bp.getCrudPrivilegi_disponibili().getSelectedRows(context);
	
	java.util.Arrays.sort( indexes );
	
	for (int i = indexes.length - 1 ;i >= 0 ;i--) 
	{
		Ass_tipo_ruolo_privilegioBulk ra = ruolo.addToTipo_ruolo_privilegi(indexes[i]);
		ra.setToBeCreated();
		ra.setUser(context.getUserInfo().getUserid());
	}
	bp.getCrudPrivilegi_disponibili().getSelection().clearSelection();
	return context.findDefaultForward();
}
/**
 * Rimuove un accesso dalla lista di Privilegi associati ad un Tipo Ruolo
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public Forward doRimuoviPrivilegio(ActionContext context) throws FillException,BusinessProcessException {
	CRUDTipoRuoloBP bp = (CRUDTipoRuoloBP)context.getBusinessProcess();
	fillModel(context);	
	Tipo_ruoloBulk ruolo = (Tipo_ruoloBulk)bp.getModel();
	int indexes[] = bp.getCrudPrivilegi().getSelectedRows(context);
	
	java.util.Arrays.sort( indexes );
	
	for (int i = indexes.length - 1 ;i >= 0 ;i--) 
	{	
		Ass_tipo_ruolo_privilegioBulk ra = ruolo.removeFromTipo_ruolo_privilegi(indexes[i]);
		ra.setToBeDeleted();
	}
	bp.getCrudPrivilegi().reset(context);
	return context.findDefaultForward();
}
}
