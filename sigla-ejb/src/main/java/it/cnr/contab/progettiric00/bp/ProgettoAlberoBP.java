package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.progettiric00.ejb.*;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * BusinessProecess che presenta i Progetti sotto forma di Albero
 */
public class ProgettoAlberoBP extends SelezionatoreListaAlberoBP {

public ProgettoAlberoBP() {
	super();
}
    public void writeHistoryLabel(PageContext pagecontext)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<span class=\"FormLabel\">Struttura del progetto: </span>");
    }

/*
	public boolean isExpandButtonEnabled()
	{
		if (((TestataProgettiRicercaBP)this.getParent()).getLivelloProgetto()==1)
			return false;
		else
			return true;
	}
*/

public boolean isBringBackButtonEnabled()
{
	if (getFocusedElement()!=null) {
		ProgettoBulk rec = (ProgettoBulk) getFocusedElement();
		Integer livello = rec.getLivello();
		Integer livellobp = new Integer(((IProgettoBP)this.getParent()).getLivelloProgetto()-1);
		if (livello.compareTo(livellobp)==0)
			return true;
	}
	return false;
}

public boolean isExpandButtonEnabled()
{
	if (!super.isExpandButtonEnabled())
		return false;

	if (getFocusedElement()!=null) {
		ProgettoBulk rec = (ProgettoBulk) getFocusedElement();
		Integer livello = rec.getLivello();
		Integer livellobp = new Integer(((IProgettoBP)this.getParent()).getLivelloProgetto()-1);
		if (livello.compareTo(livellobp)<0)
			return true;
	}
	return false;
}

}
