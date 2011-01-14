/*
 * Created on Nov 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.consultazioni.bp;

import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgPianoRipartoBP extends ConsultazioniBP {
	/**
	 *  Il metodo aggiunge alla normale toolbar del CRUD i bottoni di "consulta dettaglio"
	 */
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.consultazionedettaglio");
		button.setSeparator(true);
		listButton.addElement(button);
		return listButton;
	}
}
