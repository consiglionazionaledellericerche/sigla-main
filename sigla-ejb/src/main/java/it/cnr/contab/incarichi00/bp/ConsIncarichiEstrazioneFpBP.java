package it.cnr.contab.incarichi00.bp;


import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsIncarichiEstrazioneFpBP extends ConsultazioniBP {
	public ConsIncarichiEstrazioneFpBP() {
		super();
	}

	public ConsIncarichiEstrazioneFpBP(String s) {
		super(s);
	}

	@Override
	public  Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 1 ];
		for ( int i = 0; i< toolbar.length; i++ )
			newToolbar[ i ] = toolbar[ i ];
		newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.generaXML");
		newToolbar[ toolbar.length ].setSeparator(true);
		return newToolbar;
	}
}