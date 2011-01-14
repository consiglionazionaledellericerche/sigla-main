package it.cnr.contab.logs.bp;

import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ConsLogsApplicativiBP extends ConsultazioniBP {
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
		button.setSeparator(true);
		listButton.addElement(button);
		return listButton;
	}
}