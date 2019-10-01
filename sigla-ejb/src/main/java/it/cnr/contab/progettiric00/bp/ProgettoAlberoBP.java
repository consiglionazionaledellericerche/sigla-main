/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
