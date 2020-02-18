/*
 * Created on Jan 11, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.ejb.MovimentiMagComponentSession;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelezionatoreEvasioneForzataBP extends SelezionatoreListaBP {

	private boolean selectAllOnly;

	public SelezionatoreEvasioneForzataBP()
	{
		super();
	}

	public SelezionatoreEvasioneForzataBP(String s)
	{
		super(s);
	}

	public boolean isSelezionaButtonHidden()
	{
		return !table.isMultiSelection() || isSelectAllOnly();
	}
	
	public boolean isSelectAllOnly() {
		return selectAllOnly;
	}

	public void setSelectAllOnly(boolean b) {
		selectAllOnly = b;
	}
	public void chiusuraForzataOrdini(ActionContext context) throws BusinessProcessException{
		try {
	        UserContext userContext = context.getUserContext();
	        List<OggettoBulk> lista = getSelectedElements(context);

			OrdineAcqComponentSession component = (OrdineAcqComponentSession) createComponentSession(context);

	        for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
				OrdineAcqConsegnaBulk ordine = (OrdineAcqConsegnaBulk)i.next();
	            component.chiusuraForzataOrdini(context.getUserContext(), ordine);
	        }

            commitUserTransaction();
            setFocusedElement(context, null);
            refresh(context);
			
		} catch (ApplicationException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession(ActionContext actionContext) throws BusinessProcessException {

		return (OrdineAcqComponentSession) createComponentSession(
				"CNRORDMAG00_EJB_OrdineAcqComponentSession",
				OrdineAcqComponentSession.class);

	}


}
