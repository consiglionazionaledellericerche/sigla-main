package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.sto.bulk.Ass_uo_areaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.util.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.*;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Unita Organizzativa
 */

public class CRUDConfigUnitaOrgBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private SimpleDetailCRUDController crudAssUoArea = new SimpleDetailCRUDController( "AssociazioneUoArea", Ass_uo_areaBulk.class, "associazioneUoArea", this);

public CRUDConfigUnitaOrgBP() {
	super();
	
}
public CRUDConfigUnitaOrgBP(String function) {
	super(function);
	
}
/**
 * Inizializza alcuni attributi del modello Unita_organizzativaBulk del business process
 * @param context contesto dell'Action che e' stata richiesta
 * 
 */
public void create(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException {

	Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getModel();
	uo.setFl_uo_cds( new Boolean( false) );
	if ( uo.getFl_rubrica() == null )
		uo.setFl_rubrica( new Boolean( false) );

	super.create(context);
}		
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 * @return toolbar Toolbar in uso
 *
 */

public it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	Button[] toolbar;
	Button[] newToolbar;

	toolbar = super.createToolbar();
	newToolbar = new Button[ ( toolbar.length ) + 1 ];
	int i;
	for ( i = 0; i < toolbar.length; i++ )
		newToolbar[ i ] = toolbar[ i ];
	newToolbar[i] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.richiamaTerzo");
	return newToolbar; 
}
/**
 *	Abilito il bottone di ricerca del terzo solo se l'unità organizzativa e'
 *  in fase di modifica
 */

public boolean isRichiamaTerzoButtonEnabled() {
	return isViewing() || isEditing();
}
/**
 * Inizializza alcuni attributi del modello Unita_organizzativaBulk del business process
 * @param context contesto dell'Action che e' stata richiesta
 * 
 */
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try
	{
		super.reset(context);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getModel();
		uo.setLivello( Constants.LIVELLO_UO );
		uo.setFl_cds( new Boolean( false) );
		
	} catch(Throwable e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

	/** 
	 * Metodo per riportare il fuoco sul tab iniziale 
	 */
	protected void resetTabs(ActionContext context) {
		setTab( "tab", "tabUnitaOrganizzativa");
	}

	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUoArea() {
		return crudAssUoArea;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUoArea(SimpleDetailCRUDController controller) {
		crudAssUoArea = controller;
	}
	
}
