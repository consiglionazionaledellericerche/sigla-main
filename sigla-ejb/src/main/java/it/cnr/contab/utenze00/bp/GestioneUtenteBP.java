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

package it.cnr.contab.utenze00.bp;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * BusinessProecess che mantiene le informazioni sull'utente connesso
 * e sull'albero del menu applicativo. GestioneUtenteBP è il BusinessProcess
 * radice di tutti gli altri BusinessProcess applicativi
 */
public class GestioneUtenteBP extends it.cnr.jada.util.action.FormBP {
	private it.cnr.contab.utenze00.bulk.CNRUserInfo userInfo;
	private it.cnr.contab.utenze00.bulk.Albero_mainBulk radiceAlbero_main;
	private it.cnr.contab.utenze00.bulk.Albero_mainBulk nodo_attivo;
	private java.util.Map nodiEspansi = new java.util.HashMap();
	private java.util.Map nodi = new java.util.HashMap();
	public static Integer NUMERO_COLONNE_PREFERITI = 6;
	
	public GestioneUtenteBP() {
		super();
	}
	/**
	 * Aggiunge il nodo "nodo" alla Map dei nodi
	 *
	 * @param nodo nodo da aggiungere
	 */
	public void addNodoAlbero_main(Albero_mainBulk nodo) {
		nodi.put(nodo.getCd_nodo(),nodo);
	}

	public it.cnr.jada.util.action.SelezionatoreListaBP cercaUnitaOrganizzative(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			closeAllChildren();
			it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().listaUOPerUtente(context.getUserContext(),getUserInfo().getUtente(),getUserInfo().getEsercizio()));
			SelezionatoreUnitaOrganizzativaBP bp = (SelezionatoreUnitaOrganizzativaBP)context.createBusinessProcess("SelezionatoreUnitaOrganizzativa");
			bp.setUserInfo((CNRUserInfo)getUserInfo().clone());
			bp.setPageSize(20);
			bp.setIterator(context,ri);
			bp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).getColumnFieldPropertyDictionary("scrivania"));
			context.setBusinessProcess(this);
			context.addBusinessProcess(bp);
			return bp;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public it.cnr.jada.util.action.SelezionatoreListaBP listaUnitaOrganizzative(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			closeAllChildren();
			it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().listaUOPerUtente(context.getUserContext(),getUserInfo().getUtente(),getUserInfo().getEsercizio()));
			SelezionatoreUnitaOrganizzativaBP bp = (SelezionatoreUnitaOrganizzativaBP)context.createBusinessProcess("SelezionatoreUnitaOrganizzativa");
			bp.setUserInfo((CNRUserInfo)getUserInfo().clone());
			bp.setPageSize(20);
			bp.setIterator(context,ri);
			bp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).getColumnFieldPropertyDictionary("scrivania"));
			context.setBusinessProcess(this);
			context.addBusinessProcess(bp);
	
			if (((CNRUserContext)context.getUserContext()).getCd_unita_organizzativa()!=null) {
				int pos=0;
				bp.getIterator().moveTo(0);
				while (bp.getIterator().hasMoreElements()) {
					Unita_organizzativaBulk uo = (Unita_organizzativaBulk) bp.getIterator().nextElement();
					if (uo.getCd_unita_organizzativa().equals(((CNRUserContext)context.getUserContext()).getCd_unita_organizzativa()))
						break;
					pos++;
				}
				bp.getIterator().moveTo(0);
				Selection sel = bp.getSelection();
				sel.setFocus(pos);
				bp.setSelection(sel);
				int page = pos/bp.getPageSize();
				bp.goToPage(context, page);
			}
	
			return bp;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public it.cnr.jada.util.action.BulkBP cercaCds(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			closeAllChildren();
			SelezionaCdsBP bp = (SelezionaCdsBP)context.createBusinessProcess("SelezionaCdsBP",new Object[] { "M" });
			bp.setUserInfo((CNRUserInfo)getUserInfo().clone());
			context.setBusinessProcess(this);
			context.addBusinessProcess(bp);
			return bp;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public it.cnr.jada.util.action.SelezionatoreListaBP cercaCdr(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			closeAllChildren();
			it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().listaCdrPerUtente(context.getUserContext(),getUserInfo().getUtente(),getUserInfo().getEsercizio()));
			SelezionatoreCdrBP bp = (SelezionatoreCdrBP)context.createBusinessProcess("SelezionatoreCdr");
			bp.setUserInfo((CNRUserInfo)getUserInfo().clone());
			bp.setPageSize(20);
			bp.setIterator(context,ri);
			bp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.CdrBulk.class).getColumnFieldPropertyDictionary("scrivania"));
			context.setBusinessProcess(this);
			context.addBusinessProcess(bp);
			return bp;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	/**
	 * Rimuove il nodo specificato dalla Map dei nodi espansi
	 *
	 * @param cd_nodo codice del nodo da rimuovere
	 */
	public void chiudiNodoEspanso(String cd_nodo) {
		nodiEspansi.remove(cd_nodo);
	}
	/**
	 * Aggiunge il nodo "nodo" al Map dei nodi espansi
	 *
	 * @param nodo nodo da aggiungere
	 */
	public void espandiNodo(Albero_mainBulk nodo) {
		nodiEspansi.put(nodo.getCd_nodo(),nodo);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'componentSession'
	 *
	 * @return Il valore della proprietà 'componentSession'
	 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
	 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
	 */
	public static GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'nodo_attivo'
	 *
	 * @return Il valore della proprietà 'nodo_attivo'
	 */
	public it.cnr.contab.utenze00.bulk.Albero_mainBulk getNodo_attivo() {
		return nodo_attivo;
	}
	/**
	 * Ritorna il nodo con codice uguale a quello specificato
	 *
	 * @param cd_nodo codice del nodo da ritornare	
	 * @return istanza di Albero_mainBulk
	 */
	public Albero_mainBulk getNodoAlbero_main(String cd_nodo) {
		return (Albero_mainBulk)nodi.get(cd_nodo);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'radiceAlbero_main'
	 *
	 * @return Il valore della proprietà 'radiceAlbero_main'
	 */
	public it.cnr.contab.utenze00.bulk.Albero_mainBulk getRadiceAlbero_main() {
		return radiceAlbero_main;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'userInfo'
	 *
	 * @return Il valore della proprietà 'userInfo'
	 */
	public it.cnr.contab.utenze00.bulk.CNRUserInfo getUserInfo() {
		return userInfo;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'nodo_attivo'
	 *
	 * @param newNodo_attivo	Il valore da assegnare a 'nodo_attivo'
	 */
	public void setNodo_attivo(it.cnr.contab.utenze00.bulk.Albero_mainBulk newNodo_attivo) {
		nodo_attivo = newNodo_attivo;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'radiceAlbero_main'
	 *
	 * @param newRadiceAlbero_main	Il valore da assegnare a 'radiceAlbero_main'
	 * @throws BusinessProcessException	
	 */
	public void setRadiceAlbero_main(ActionContext context, it.cnr.contab.utenze00.bulk.Albero_mainBulk newRadiceAlbero_main) throws it.cnr.jada.action.BusinessProcessException {
		radiceAlbero_main = newRadiceAlbero_main;
		nodo_attivo = null;
		nodi.clear();
		nodiEspansi.clear();
		closeAllChildren(context);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'userInfo'
	 *
	 * @param newUserInfo	Il valore da assegnare a 'userInfo'
	 */
	public void setUserInfo(it.cnr.contab.utenze00.bulk.CNRUserInfo newUserInfo) {
		userInfo = newUserInfo;
	}
	public void writeCollapseButton(javax.servlet.jsp.JspWriter out)throws java.io.IOException {
		if (radiceAlbero_main == null)
		    return;
		out.print("&nbsp;&nbsp;&nbsp;&nbsp;<button class=\"Button\" onclick=\"cancelBubble(event); if (disableDblClick()) collapseAllNode(); return false\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\" title=\"Chiudi tutti i rami\">");
		out.print("<img align=\"middle\" class=\"Button\" src=\"img/collapseall.png\">");
		out.print("</button>");
	}

	public void writePreferiti(javax.servlet.jsp.JspWriter out, UserContext userContext)throws java.io.IOException, BusinessProcessException {
		try {
			List<PreferitiBulk> preferiti = getComponentSession().preferitiList(userContext);
			out.println("<table width=\"100%\" cellspacing=\"2\">");
			out.println("<tr>");
			int index = 0;
			for (Iterator<PreferitiBulk> iterator = preferiti.iterator(); iterator.hasNext();) {
				index++;
				PreferitiBulk preferito = iterator.next();
				Button bt = new Button();
				bt.setImg(preferito.getUrl_icona());
				bt.setLabel(preferito.getDescrizione());
				bt.setTitle(preferito.getDescrizione());
				bt.setHref("javascript:submitForm('doCallPreferiti("+preferito.getBusiness_process()+","+preferito.getTi_funzione()+")')");
				bt.setStyle("color:white; border:none; cursor:hand");
				if (index%NUMERO_COLONNE_PREFERITI == 0){
					out.println("</tr><tr>");
					index=1;
				}
				out.println("<td align=\"center\" wrap>");
				bt.write(out, true, CNRUserContext.isFromBootstrap(userContext));
				out.println("</td>");
			}
			out.println("</tr>");
			out.println("</table>");			
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (EJBException e) {
			throw handleException(e);
		}
	}	
	/**
	 * Disegna il menù applicativo usando il Writer specificato
	 * 
	 * @param out writer
	 * @throws IOException	
	 */
	public void writeMenu(javax.servlet.jsp.JspWriter out)
	    throws java.io.IOException {
	    if (radiceAlbero_main == null)
	        return;
	    java.util.BitSet livelliCompleti = new java.util.BitSet();
	    writeMenuItem(out, radiceAlbero_main, -1, livelliCompleti, false, true);
	}
	private void writeMenu(javax.servlet.jsp.JspWriter out,it.cnr.contab.utenze00.bulk.Albero_mainBulk nodo,int livello,java.util.BitSet livelliCompleti) throws java.io.IOException {
		for (java.util.Iterator i = nodo.getNodi_figli().iterator();i.hasNext();){
			writeMenuItem(out,(Albero_mainBulk)i.next(),livello,livelliCompleti,i.hasNext());
		}
	}
	private void writeMenuItem(javax.servlet.jsp.JspWriter out,it.cnr.contab.utenze00.bulk.Albero_mainBulk nodoFiglio,int livello,java.util.BitSet livelliCompleti,boolean hasNext) throws java.io.IOException {
		writeMenuItem(out,nodoFiglio,livello,livelliCompleti,hasNext, false);
	}
	private void writeMenuItem(javax.servlet.jsp.JspWriter out,it.cnr.contab.utenze00.bulk.Albero_mainBulk nodoFiglio,int livello,java.util.BitSet livelliCompleti,boolean hasNext,boolean writeCollapseButton) throws java.io.IOException {
			out.println("<div>");
			Albero_mainBulk nodoEspanso = (Albero_mainBulk)nodiEspansi.get(nodoFiglio.getCd_nodo());
			if (nodoEspanso != null)
				nodoFiglio = nodoEspanso;
			boolean isFoglia = nodoFiglio.getFl_terminale() == null || nodoFiglio.getFl_terminale().booleanValue();
			boolean isEspandibile = !isFoglia && nodoFiglio.getNodi_figli() == null;
			for (int j = 0;j < livello;j++) {
				if (!livelliCompleti.get(j)) {
					out.print("<img style=\"vertical-align: top\" src=\"");
					out.print("img/treejoini16.gif");
					out.print("\">");
				}
				else {
					out.print("<img style=\"vertical-align: top\" width=\"16\" src=\"");
					out.print("img/spacer.gif");
					out.print("\">");
				}
	///				out.print("<span style=\"width:16px;vertical-align: top\"></span>");
			}
			if (livello >= 0) {
				if (!isFoglia) {
	//				out.print("<a class=\"Button\" href=\"\" onclick=\"");
					out.print("<span onclick=\"");
					out.print(isEspandibile ? "apriMenu('" : "chiudiMenu('");
					out.print(nodoFiglio.getCd_nodo());
					out.print("')\">");
				}
				out.print("<img class=\"Button\" style=\"vertical-align: top\" src=\"");
				if (hasNext)
					out.print(isFoglia ?
						"img/treejoint16.gif" :
						isEspandibile ?
							"img/treejointo16.gif" :
							"img/treejointc16.gif" );
				else
					out.print(isFoglia ?
						"img/treejoinl16.gif" :
						isEspandibile ?
							"img/treejoinlo16.gif" :
							"img/treejoinlc16.gif" );
				out.print('"');
				out.print('>');
				if (!isFoglia)
	//				out.print("</a>");
					out.print("</span>");
				if (hasNext)
					livelliCompleti.clear(livello);
				else
					livelliCompleti.set(livello);
			}
			if (isFoglia)
				out.print("<span class=\"MenuItem\">");
			else
				out.print("<span class=\"MenuItemGroup\">");
			if (nodoFiglio.getUrl_icona() != null || !isFoglia) {
				if (writeCollapseButton)
					out.print("<img style=\"vertical-align: middle\" src=\"");
				else
					out.print("<img style=\"vertical-align: top\" src=\"");
				out.print(nodoFiglio.getUrl_icona() == null ? 
					( isEspandibile ? 
						"img/folderclose16.gif" : 
						"img/folderopen16.gif") :
					(isEspandibile ?	 
					it.cnr.jada.util.Config.getHandler().getProperty(getClass(),"iconsPath")+nodoFiglio.getUrl_icona():
					(nodoFiglio.getUrl_icona_open()!= null ?
					it.cnr.jada.util.Config.getHandler().getProperty(getClass(),"iconsPath")+nodoFiglio.getUrl_icona_open():
					it.cnr.jada.util.Config.getHandler().getProperty(getClass(),"iconsPath")+nodoFiglio.getUrl_icona())));
				out.print("\">");
			}		
		    out.print("&nbsp;");
			if (isFoglia && nodoFiglio.getBusiness_process() != null) {
				out.print("<span title=\""+nodoFiglio.getDs_nodo()+" - "+nodoFiglio.getCd_accesso()+"\" class=\"MenuItem\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\" onclick=\"selezionaMenu('");
				out.print(nodoFiglio.getCd_nodo());
				out.print("')\">");
			} else if (!isFoglia) {
				out.print("<span");
				if (writeCollapseButton)
					out.print(" style=\"vertical-align: middle\""); 
				out.print(" title=\""+nodoFiglio.getDs_nodo()+"\" class=\"MenuItem\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\" onclick=\"");
				out.print(isEspandibile ? "apriMenu('" : "chiudiMenu('");
				out.print(nodoFiglio.getCd_nodo());
				out.print("')\">");
			}
			out.print(nodoFiglio.getDs_nodo());
			if (!isFoglia || nodoFiglio.getBusiness_process() != null)
				out.print("</span>");
			if (writeCollapseButton)
				writeCollapseButton(out);
			out.println("</span>");
			out.println("</div>");
			if (!isFoglia && !isEspandibile)
				writeMenu(out,nodoFiglio,livello+1,livelliCompleti);
	}
	public void collapseAllNodi() {
		nodiEspansi.clear();
	}
}
