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
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_livelliComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;


public class CRUDConfigParametriLivelliBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private boolean isEtrDisabled=false;
	private boolean isSpeDisabled=false;
	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDConfigParametriLivelliBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDConfigParametriLivelliBP(String function) {
		super(function);
	}

	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		((Parametri_livelliBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeModelForInsert(actioncontext, oggettobulk);
	}

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		Parametri_livelliComponentSession sessione = (Parametri_livelliComponentSession) createComponentSession();
		try {
			Parametri_livelliBulk parLivBulk = sessione.getParametriLivelli(actioncontext.getUserContext(),	CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			if (parLivBulk != null) {
				isEtrDisabled = !this.isParametriLivelliEtrEnabled(actioncontext.getUserContext(), parLivBulk);
				isSpeDisabled = !this.isParametriLivelliSpeEnabled(actioncontext.getUserContext(), parLivBulk);
				this.setModel(actioncontext, parLivBulk);
				this.setStatus(it.cnr.jada.util.action.FormController.EDIT);
			}
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean isParametriLivelliSpeEnabled(UserContext userContext, Parametri_livelliBulk parLiv)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_livelliComponentSession sessione = (Parametri_livelliComponentSession) createComponentSession();
			return sessione.isParametriLivelliSpeEnabled(userContext, parLiv);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	} 

	public boolean isParametriLivelliEtrEnabled(UserContext userContext, Parametri_livelliBulk parLiv)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_livelliComponentSession sessione = (Parametri_livelliComponentSession) createComponentSession();
			return sessione.isParametriLivelliEtrEnabled(userContext, parLiv);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	} 

	public boolean isSearchButtonHidden() {
		return true;
	}

	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	public boolean isNewButtonHidden() {
		return true;
	}

	public boolean isDeleteButtonEnabled() {
		return !isEtrDisabled||!isSpeDisabled;
	}

	public boolean isSaveButtonEnabled() {
		return !isEtrDisabled||!isSpeDisabled;
	}

	/**
	 * Genera l'HTML per disegnare nell'interfaccia utente una tabella che presenta nelle righe le funzioni e nelle
	 * colonne  i tipi CDS; inoltre, se il modello passato come parametro e' valorizzato, inizializza le celle
	 * di questa tabella con i dati presenti nel modello.
	 */

	public void writeTable(javax.servlet.jsp.JspWriter out, Parametri_livelliBulk parLiv) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
	{

		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");

		out.print("<tr>");
		out.print("<td valign=top>");
		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		writeTableHeader( out, Elemento_voceHome.GESTIONE_ENTRATE, parLiv);
		if (parLiv.getLivelli_entrata() != null && parLiv.getLivelli_entrata().intValue()>0) {
			for (int i=1;i<=7;i++) {
				writeTableRow( out, Elemento_voceHome.GESTIONE_ENTRATE, i , parLiv );
			}	
		}
		out.println("</table>");
		out.print("</td>");

		out.print("<td valign=top>");
		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		writeTableHeader( out, Elemento_voceHome.GESTIONE_SPESE, parLiv);
		if (parLiv.getLivelli_spesa() != null && parLiv.getLivelli_spesa().intValue()>0) {
			for (int i=1;i<=7;i++) {
				writeTableRow( out, Elemento_voceHome.GESTIONE_SPESE, i , parLiv );
			}	
		}
		out.println("</table>");
		out.print("</td>");
		out.print("</tr>");
		out.println("</table>");
	}

	private void writeTableHeader(javax.servlet.jsp.JspWriter out, String tipo, Parametri_livelliBulk parLiv) throws java.io.IOException, PersistencyException, IntrospectionException, it.cnr.jada.comp.ApplicationException 
	{
		//title
		out.println("<tr><td colspan=3><h3><CENTER>");
		if (tipo==Elemento_voceHome.GESTIONE_ENTRATE) 
			out.println("Entrate</CENTER></h3></td>");
		else
			out.println("Spese</CENTER></h3></td>");
		out.print("</tr>");

		//Nr. Livelli
		if (tipo==Elemento_voceHome.GESTIONE_ENTRATE) { 
			out.print("<tr>");	
			out.print("<td colspan=2>");	
			this.getController().writeFormLabel(out,"livelli_entrata");
			out.print("</td>");	
			out.print("<td><CENTER>");	
			this.getController().writeFormInput(out,null,"livelli_entrata",isEtrDisabled,null,"");
			out.print("</td>");	
			out.print("</tr>");	
		}
		else if (tipo==Elemento_voceHome.GESTIONE_SPESE) { 
			out.print("<tr>");	
			out.print("<td colspan=2>");	
			this.getController().writeFormLabel(out,"livelli_spesa");
			out.print("</td>");	
			out.print("<td><CENTER>");	
			this.getController().writeFormInput(out,null,"livelli_spesa",isSpeDisabled,null,"");
			out.print("</td>");	
			out.print("</tr>");	
		}

		//column header
		if ((tipo==Elemento_voceHome.GESTIONE_ENTRATE && parLiv.getLivelli_entrata()!=null && 
			 parLiv.getLivelli_entrata().intValue()>0) ||
		    (tipo==Elemento_voceHome.GESTIONE_SPESE && parLiv.getLivelli_spesa()!=null && 
			 parLiv.getLivelli_spesa().intValue()>0)) { 
			out.print("<tr></tr>");
			out.print("<tr>");
			out.print("<td></td>");
			out.print("<td><CENTER><span class=\"FormLabel\">Lunghezza</span></CENTER></td>");		
			out.print("<td><CENTER><span class=\"FormLabel\">Descrizione</span></CENTER></td>");		
			out.println("</tr>");
		}
	}

	private void writeTableRow(javax.servlet.jsp.JspWriter out, String tipo, int liv, Parametri_livelliBulk parLiv ) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
	{
		if (tipo==Elemento_voceHome.GESTIONE_ENTRATE) { 
			if (parLiv.getLung_livello_etr(liv)!=null||parLiv.getDs_livello_etr(liv)!=null||
			    parLiv.getLivelli_entrata().intValue()>=liv) { 		
				out.print("<tr>");	
				
				//Prima Colonna: Intestazione
				out.print("<td><span class=\"FormLabel\">");
				out.print(liv + "° Livello");
				out.print("</span></td>");
			
				//Seconda Colonna: Lunghezza
				out.print("<td>");
				this.getController().writeFormInput(out,null,"lung_livello" + liv + "e",isEtrDisabled,null,"");
				out.print("</td>");

				//Terza Colonna: Descrizione
				out.print("<td>");
				this.getController().writeFormInput(out,null,"ds_livello" + liv + "e",isEtrDisabled,null,"");
				out.print("</td>");

				out.println("</tr>");	
			}
		}
		else
		{	
			if (parLiv.getLung_livello_spe(liv)!=null||parLiv.getDs_livello_spe(liv)!=null||
				parLiv.getLivelli_spesa().intValue()>=liv) { 		

				out.print("<tr>");	
				
				//Prima Colonna: Intestazione
				out.print("<td><span class=\"FormLabel\">");
				out.print(liv + "° Livello");
				out.print("</span></td>");
			
				//Seconda Colonna: Lunghezza
				out.print("<td>");
				this.getController().writeFormInput(out,null,"lung_livello" + liv + "s",isEtrDisabled,null,"");
				out.print("</td>");

				//Terza Colonna: Descrizione
				out.print("<td>");
				this.getController().writeFormInput(out,null,"ds_livello" + liv + "s",isSpeDisabled,null,"");
				out.print("</td>");
				
				out.println("</tr>");	
			}
		}
	}
}