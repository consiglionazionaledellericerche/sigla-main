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
import it.cnr.contab.config00.ejb.Parametri_livelli_epComponentSession;
import it.cnr.contab.config00.pdcep.bulk.Voce_epHome;
import it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;


public class CRUDConfigParametriLivelliEPBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private boolean isEcoDisabled=false;
	private boolean isPatDisabled=false;
	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDConfigParametriLivelliEPBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDConfigParametriLivelliEPBP(String function) {
		super(function);
	}

	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		((Parametri_livelli_epBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeModelForInsert(actioncontext, oggettobulk);
	}

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		Parametri_livelli_epComponentSession sessione = (Parametri_livelli_epComponentSession) createComponentSession();
		try {
			Parametri_livelli_epBulk parLivBulk = sessione.getParametriLivelli(actioncontext.getUserContext(),	CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			if (parLivBulk != null) {
				isEcoDisabled = !this.isParametriLivelliEcoEnabled(actioncontext.getUserContext(), parLivBulk);
				isPatDisabled = !this.isParametriLivelliPatEnabled(actioncontext.getUserContext(), parLivBulk);
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

	public boolean isParametriLivelliPatEnabled(UserContext userContext, Parametri_livelli_epBulk parLiv)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_livelli_epComponentSession sessione = (Parametri_livelli_epComponentSession) createComponentSession();
			return sessione.isParametriLivelliPatEnabled(userContext, parLiv);
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

	public boolean isParametriLivelliEcoEnabled(UserContext userContext, Parametri_livelli_epBulk parLiv)  throws it.cnr.jada.action.BusinessProcessException {
		try {
			Parametri_livelli_epComponentSession sessione = (Parametri_livelli_epComponentSession) createComponentSession();
			return sessione.isParametriLivelliEcoEnabled(userContext, parLiv);
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
		return !isEcoDisabled||!isPatDisabled;
	}

	public boolean isSaveButtonEnabled() {
		return !isEcoDisabled||!isPatDisabled;
	}

	/**
	 * Genera l'HTML per disegnare nell'interfaccia utente una tabella che presenta nelle righe le funzioni e nelle
	 * colonne  i tipi CDS; inoltre, se il modello passato come parametro e' valorizzato, inizializza le celle
	 * di questa tabella con i dati presenti nel modello.
	 */

	public void writeTable(javax.servlet.jsp.JspWriter out, Parametri_livelli_epBulk parLiv) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
	{

		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");

		out.print("<tr>");
		out.print("<td valign=top>");
		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		writeTableHeader( out, Voce_epHome.ECONOMICA, parLiv);
		if (parLiv.getLivelli_eco() != null && parLiv.getLivelli_eco().intValue()>0) {
			for (int i=1;i<=8;i++) {
				writeTableRow( out, Voce_epHome.ECONOMICA, i , parLiv );
			}	
		}
		out.println("</table>");
		out.print("</td>");

		out.print("<td valign=top>");
		out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		writeTableHeader( out, Voce_epHome.PATRIMONIALE, parLiv);
		if (parLiv.getLivelli_pat() != null && parLiv.getLivelli_pat().intValue()>0) {
			for (int i=1;i<=8;i++) {
				writeTableRow( out, Voce_epHome.PATRIMONIALE, i , parLiv );
			}	
		}
		out.println("</table>");
		out.print("</td>");
		out.print("</tr>");
		out.println("</table>");
	}

	private void writeTableHeader(javax.servlet.jsp.JspWriter out, String tipo, Parametri_livelli_epBulk parLiv) throws java.io.IOException, PersistencyException, IntrospectionException, it.cnr.jada.comp.ApplicationException 
	{
		//title
		out.println("<tr><td colspan=3><h3><CENTER>");
		if (tipo==Voce_epHome.ECONOMICA) 
			out.println("Economica</CENTER></h3></td>");
		else
			out.println("Patrimoniale</CENTER></h3></td>");
		out.print("</tr>");

		//Nr. Livelli
		if (tipo==Voce_epHome.ECONOMICA) { 
			out.print("<tr>");	
			out.print("<td colspan=2>");	
			this.getController().writeFormLabel(out,"livelli_eco");
			out.print("</td>");	
			out.print("<td><CENTER>");	
			this.getController().writeFormInput(out,null,"livelli_eco",isEcoDisabled,null,"");
			out.print("</td>");	
			out.print("</tr>");	
		}
		else if (tipo==Voce_epHome.PATRIMONIALE) { 
			out.print("<tr>");	
			out.print("<td colspan=2>");	
			this.getController().writeFormLabel(out,"livelli_pat");
			out.print("</td>");	
			out.print("<td><CENTER>");	
			this.getController().writeFormInput(out,null,"livelli_pat",isPatDisabled,null,"");
			out.print("</td>");	
			out.print("</tr>");	
		}

		//column header
		if ((tipo==Voce_epHome.ECONOMICA && parLiv.getLivelli_eco()!=null && 
			 parLiv.getLivelli_eco().intValue()>0) ||
		    (tipo==Voce_epHome.PATRIMONIALE && parLiv.getLivelli_pat()!=null && 
			 parLiv.getLivelli_pat().intValue()>0)) { 
			out.print("<tr></tr>");
			out.print("<tr>");
			out.print("<td></td>");
			out.print("<td><CENTER><span class=\"FormLabel\">Lunghezza</span></CENTER></td>");		
			out.print("<td><CENTER><span class=\"FormLabel\">Descrizione</span></CENTER></td>");		
			out.println("</tr>");
		}
	}

	private void writeTableRow(javax.servlet.jsp.JspWriter out, String tipo, int liv, Parametri_livelli_epBulk parLiv ) throws java.io.IOException, PersistencyException , it.cnr.jada.comp.ApplicationException, IntrospectionException
	{
		if (tipo==Voce_epHome.ECONOMICA) { 
			if (parLiv.getLung_livello_eco(liv)!=null||parLiv.getDs_livello_eco(liv)!=null||
			    parLiv.getLivelli_eco().intValue()>=liv) { 		
				out.print("<tr>");	
				
				//Prima Colonna: Intestazione
				out.print("<td><span class=\"FormLabel\">");
				out.print(liv + "° Livello");
				out.print("</span></td>");
			
				//Seconda Colonna: Lunghezza
				out.print("<td>");
				this.getController().writeFormInput(out,null,"lung_livello" + liv + "e",isEcoDisabled,null,"");
				out.print("</td>");

				//Terza Colonna: Descrizione
				out.print("<td>");
				this.getController().writeFormInput(out,null,"ds_livello" + liv + "e",isEcoDisabled,null,"");
				out.print("</td>");

				out.println("</tr>");	
			}
		}
		else
		{	
			if (parLiv.getLung_livello_pat(liv)!=null||parLiv.getDs_livello_pat(liv)!=null||
				parLiv.getLivelli_pat().intValue()>=liv) { 		

				out.print("<tr>");	
				
				//Prima Colonna: Intestazione
				out.print("<td><span class=\"FormLabel\">");
				out.print(liv + "° Livello");
				out.print("</span></td>");
			
				//Seconda Colonna: Lunghezza
				out.print("<td>");
				this.getController().writeFormInput(out,null,"lung_livello" + liv + "p",isPatDisabled,null,"");
				out.print("</td>");

				//Terza Colonna: Descrizione
				out.print("<td>");
				this.getController().writeFormInput(out,null,"ds_livello" + liv + "p",isPatDisabled,null,"");
				out.print("</td>");
				
				out.println("</tr>");	
			}
		}
	}
}