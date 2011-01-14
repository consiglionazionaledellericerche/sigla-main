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
 * BusinessProcess che presenta i Progetti sotto forma di Albero
 */
public class ProgettoAlberoLABP extends SelezionatoreListaAlberoBP {

public ProgettoAlberoLABP() {
	super();
}
	public void writeHistoryLabel(PageContext pagecontext)
		throws IOException, ServletException
	{
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.println("<span class=\"FormLabel\">Struttura del progetto: </span>");
	}

	public boolean isBringBackButtonEnabled()
	{
		if (getFocusedElement()!=null) {
			Progetto_sipBulk rec = (Progetto_sipBulk) getFocusedElement();
			Integer livello = rec.getLivello();
			if (livello.compareTo(ProgettoBulk.LIVELLO_PROGETTO_TERZO)==0)
				return true;
		}
		return false;
	}

	public boolean isExpandButtonEnabled()
	{
		if (!super.isExpandButtonEnabled())
			return false;

		if (getFocusedElement()!=null) {
			Progetto_sipBulk rec = (Progetto_sipBulk) getFocusedElement();
			Integer livello = rec.getLivello();
			if (livello.compareTo(ProgettoBulk.LIVELLO_PROGETTO_TERZO)<0)
				return true;
		}
		return false;
	}
	public void setParentElement(OggettoBulk oggettobulk) {
		Progetto_sipBulk progetto = (Progetto_sipBulk)oggettobulk;
		if (progetto != null && progetto.getLivello() != null){
			if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_PRIMO))
			  setColumns( getBulkInfo().getColumnFieldPropertyDictionary("commesse_sip"));
			else if (progetto.getLivello().equals(ProgettoBulk.LIVELLO_PROGETTO_SECONDO))
			  setColumns( getBulkInfo().getColumnFieldPropertyDictionary("moduli_sip")); 
		}else
		  setColumns( getBulkInfo().getColumnFieldPropertyDictionary("progetti_sip"));
		super.setParentElement(oggettobulk);
	}

}
