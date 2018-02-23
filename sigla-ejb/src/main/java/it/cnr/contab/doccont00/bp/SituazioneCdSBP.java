package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

/**
 * Business process che gestisce attività relative alla situazione del CdS.
 */

public class SituazioneCdSBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController cds = new SimpleDetailCRUDController("centriDiSpesa",V_disp_cassa_cdsBulk.class,"centriDiSpesaColl",this);
public SituazioneCdSBP() {
	super();
	initialize();
	}
public SituazioneCdSBP(String function) {
	super(function);
	initialize();	
	}
/**
 * Metodo utilizzato per la ricerca della somma dell'importo di obbligazioni 
 * in sacdenza per i CdS selezionati dall'utente.
 * @param context Il contesto dell'azione
 */

public void cercaObbligazioniCds(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{

	RicercaMandatoAccreditamentoBulk ricerca;
	try {
		fillModel(context);		
		if ( getCds().getSelectedModels(context ).size() == 0 )
			throw new MessageToUser("E' necessario selezionare al meno un Cds");
		ricerca = ((RicercaMandatoAccreditamentoBulk)getModel());
	} catch(Exception e) {
			throw handleException(e);
	}
		
	ricerca.setFlTuttiCdsCaricati(false);
	try
	{
		ricerca.setCentriDiSpesaSelezionatiColl( getCds().getSelectedModels( context ));		
		ricerca = ((MandatoComponentSession)createComponentSession()).listaSituazioneCassaCds( context.getUserContext(), ricerca );
		setModel( context, ricerca );
		getCds().getSelection().clear();
		resyncChildren( context );
	} catch(Exception e) {
			ricerca.setFlTuttiCdsCaricati( true );
			throw handleException(e);
	}
}
/**
 * Metodo utilizzato per la ricerca di tutti i CdS e della loro disponibilità di cassa
 * @param context Il contesto dell'azione
 */

public void cercaTuttiCds(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{

	RicercaMandatoAccreditamentoBulk ricerca;
	try {
		fillModel(context);		
		ricerca = ((RicercaMandatoAccreditamentoBulk)getModel());
	} catch(Exception e) {
			throw handleException(e);
	}
		
	ricerca.setFlTuttiCdsCaricati(true);
	try
	{
		ricerca = ((MandatoComponentSession)createComponentSession()).listaSituazioneCassaCds( context.getUserContext(), ricerca );
		setModel( context, ricerca );
		getCds().getSelection().clear();		
		resyncChildren( context );
	} catch(Exception e) {
			ricerca.setFlTuttiCdsCaricati( false );
			throw handleException(e);
	}
}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 *
 * @return null In questo caso la toolbar è vuota
 */

protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	return null;
	/*
	Button[] toolbar = new Button[8];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
	return toolbar;
	*/
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>cds</code>
 * di tipo <code>SimpleDetailCRUDController</code>.
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCds() {
	return cds;
}
/* 
	inizializzazione del DetailedController che gestisce l'elenco di Cds
*/
public void initialize()
{
	getCds().setPaged( false );
}
/**
 *	Abilito il bottone di emissione del mandato solo se questo non e' gia' in fase di modifica
 *
 *	isEditable 	= FALSE se il mandato e' in visualizzazione
 *				= TRUE se il mandato e' in modifica/inserimento
 */

public boolean isEmettiMandatiEnabled() {
	return isEditable() && (isEditing() || isInserting()) && (getCds().getSelection().getFocus() >= 0 );
}
/**
 *	Abilito il bottone di visualizzazione dell'entrate del mandato solo se
 *  questo non e' gia' in fase di modifica
 *
 *	isEditable 	= FALSE se il mandato e' in visualizzazione
 *				= TRUE se il mandato e' in modifica/inserimento
 */

public boolean isVisualizzaEntrateEnabled() {
	return isEditable() && (isEditing() || isInserting()) && (getCds().getSelection().getFocus() >= 0 );
}
}
