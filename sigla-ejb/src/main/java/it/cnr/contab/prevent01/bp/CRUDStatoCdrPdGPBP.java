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

/*
 * Created on Oct 21, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDStatoCdrPdGPBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private Parametri_cnrBulk parametriCnr;

	private SimpleDetailCRUDController crudDettagli = new SimpleDetailCRUDController( "Dettagli", Pdg_esercizioBulk.class, "dettagli", this, false) {
		public boolean isFiltered()
		{
			return false;
		}
		public boolean isReadonly()
		{
			return true;
		}
		public boolean isGrowable()
		{
			return true;	
		}
		public boolean isShrinkable()
		{
			return true;	
		}
	};

	public CRUDStatoCdrPdGPBP() {
		super();
	}

	public CRUDStatoCdrPdGPBP(String function) {
		super(function);
	}

	protected void initialize(ActionContext context) throws BusinessProcessException {
		super.initialize(context);
		setModel(context, new CdrBulk());
		try {
			setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
			cerca(context);
		} catch(Exception e) {
			throw handleException(e);
		}
		crudDettagli.setMultiSelection(true);
	}
	public Pdg_esercizioBulk cambiaStato(it.cnr.jada.action.ActionContext context, Pdg_esercizioBulk esercizio, boolean avanza) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			if (avanza) {
				if (isStatoModificabile(esercizio))
					
				esercizio = (Pdg_esercizioBulk) ((it.cnr.contab.prevent01.ejb.PdGPreliminareComponentSession)createComponentSession()).cambiaStatoConBulk(context.getUserContext(),esercizio);
			}
			else {
				esercizio = (Pdg_esercizioBulk) ((it.cnr.contab.prevent01.ejb.PdGPreliminareComponentSession)createComponentSession()).riportaStatoPrecedenteConBulk(context.getUserContext(),esercizio);
			}
			//setModel(context,esercizio );
			return esercizio;
		} catch(Exception e) 
		{
			throw handleException(e);
		}

	}
	public void cambiaStati(ActionContext context, boolean avanza) throws it.cnr.jada.action.BusinessProcessException {

		try {
			// controlliamo che gli stati delle righe selezionate siano tra loro congruenti
			List listaSel = getCrudDettagli().getSelectedModels(context);
			String oldStato = ((Pdg_esercizioBulk)(getCrudDettagli().getModel())).getStato();
			if (!listaSel.isEmpty()) {
				for (Iterator it=listaSel.iterator();it.hasNext();) {
					Pdg_esercizioBulk esercizio = (Pdg_esercizioBulk) it.next();
					if (!esercizio.getStato().equals(oldStato)) {
						throw new ApplicationException("Attenzione: le righe selezionate devono avere lo stesso stato!");		
					}
					if (avanza && !isStatoModificabile(esercizio)) {
						throw new ApplicationException("Attenzione: non è possibile modificare lo stato del CdR "+esercizio.getCd_centro_responsabilita()+"!");	
					}
					if (!avanza && !isStatoPrecedenteModificabile(esercizio)) {
						throw new ApplicationException("Attenzione: non è possibile modificare lo stato del CdR "+esercizio.getCd_centro_responsabilita()+"!");	
					}
				}

				Selection sel = getCrudDettagli().getSelection();
				for (Iterator it=sel.iterator();it.hasNext();) {
					Integer iSel=(Integer)it.next();
					Pdg_esercizioBulk esercizio = (Pdg_esercizioBulk) getCrudDettagli().getDetails().get(iSel.intValue());
					try {
						Pdg_esercizioBulk newEs = cambiaStato(context, esercizio, avanza);
						esercizio.setStato(newEs.getStato());
						sel.removeFromSelection(iSel);
						getCrudDettagli().setSelection(context, sel);	
					} catch(Throwable e) {
						throw new ApplicationException("CdR: "+esercizio.getCd_centro_responsabilita()+". "+e.getMessage());		
					}
				}
				setMessage("Stati aggiornati in modo corretto.");		
			}
			else {
				cambiaStato(context, (Pdg_esercizioBulk)getCrudDettagli().getModel(), avanza);
				cerca(context);
				setMessage("Stato aggiornato in modo corretto.");		
			}
		} catch(Exception e) 
		{
			throw handleException(e);
		}
	}

	public boolean isCambiaStatoButtonEnabled()
	{
		Pdg_esercizioBulk pdg_es = (Pdg_esercizioBulk) getCrudDettagli().getModel();
		return (isStatoModificabile(pdg_es));
	}
	public boolean isStatoModificabile(Pdg_esercizioBulk pdg_es)
	{
		if ( isEditing() && isEditable() &&
			pdg_es != null &&
			pdg_es.getStato() != null &&
			!pdg_es.getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR))
			return true;

		return false;
	}

	public boolean isRiportaStatoPrecedenteButtonEnabled()
	{
		Pdg_esercizioBulk pdg_es = (Pdg_esercizioBulk) getCrudDettagli().getModel();
		return (isStatoPrecedenteModificabile(pdg_es));
	}
	public boolean isStatoPrecedenteModificabile(Pdg_esercizioBulk pdg_es)
	{
		if ( isEditing() && isEditable() &&
			pdg_es != null &&
		    pdg_es.getStato() != null &&
			!pdg_es.getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_CDR) &&
			 (pdg_es.getStato().equals(Pdg_esercizioBulk.STATO_PRECHIUSURA_CDR) ||
			  pdg_es.getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_CDR)))
			return true;
		return false;
	}
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}
    protected Button[] createToolbar()
    {
        Button abutton[] = new Button[5];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
        //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
        return abutton;
    }

    public void cerca(ActionContext actioncontext)
		throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
	{
		try
		{
			fillModel(actioncontext);
			OggettoBulk oggettobulk = getModel();
			RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk);
			if(remoteiterator == null || remoteiterator.countElements() == 0)
			{
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				return;
			}
			if(remoteiterator.countElements() == 1)
			{
				OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				if(oggettobulk1 != null) {
					edit(actioncontext, oggettobulk1);
				}
				return;
			}
			else {
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				reset(actioncontext);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}

	public void setParametriCnr(Parametri_cnrBulk bulk) {
			parametriCnr = bulk;
	}
}
