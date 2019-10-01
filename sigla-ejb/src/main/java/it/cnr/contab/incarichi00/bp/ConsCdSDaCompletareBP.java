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

package it.cnr.contab.incarichi00.bp;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.ReferenteAmministrativoBulk;
import it.cnr.contab.incarichi00.bulk.V_terzi_da_completareBulk;
import it.cnr.contab.incarichi00.ejb.CdSDaCompletareComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsCdSDaCompletareBP extends ConsultazioniBP {
	private boolean uoEnte = false;
	public static final String STRING_TO_BE_REPLACE = "{0}";
	public static final String XML_REFERENTI_AMMINISTRATIVI = "http://www.cnr.it/sitocnr/referentiamministrativi.xml";
	public static final String EMAILCCN = "roberto.tatarelli@cnr.it";
	public static final String EMAILCCN2 = "raffaele.pagano@cnr.it";
	public static final String EMAILCCN3 = "matilde.durso@cnr.it";
	private boolean testMode = true;
	private List<ReferenteAmministrativoBulk> referentiAmministrativi = new ArrayList<ReferenteAmministrativoBulk>();
	@SuppressWarnings("unchecked")
	@Override
	public Vector addButtonsToToolbar(Vector listButton) {
		if (isUoEnte()){		
			Button eMail = new Button();
			eMail.setImg("img/Email.gif");
			eMail.setDisabledImg("img/Email.gif");
			eMail.setLabel("<u>E</u>-Mail");
			eMail.setHref("javascript:submitForm('doEMail')");
			eMail.setStyle("width:90px;");
			eMail.setTitle("Comunica ai segretari dei CdS selezionati i terzi ancora da completare.");
			eMail.setAccessKey("E");
			eMail.setSeparator(true);
			listButton.add(eMail);
		}
		return super.addButtonsToToolbar(listButton);
	}

	@Override
	public CRUDComponentSession createComponentSession() throws EJBException, RemoteException, BusinessProcessException {
		return (CdSDaCompletareComponentSession)createComponentSession(getComponentSessioneName());
	}
	
	@SuppressWarnings("unchecked")
	public void inviaEMail(ActionContext actioncontext, List<CdsBulk> cdsSelezionati) throws BusinessProcessException{
		try {
			for (Iterator<CdsBulk> iterator = cdsSelezionati.iterator(); iterator.hasNext();) {
				CdsBulk cds = iterator.next();
				List<V_terzi_da_completareBulk> terziDaConguagliare = ((CdSDaCompletareComponentSession)createComponentSession(getComponentSessioneName())).findTerzi(actioncontext.getUserContext(), cds);
				Parametri_cnrBulk parametriCNR = (Parametri_cnrBulk) createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), new Parametri_cnrBulk(CNRUserContext.getEsercizio(actioncontext.getUserContext())));
				String subject = parametriCNR.getOggettoEmail() + " - (CDS: "+cds.getCd_unita_organizzativa()+")";
				String corpo = parametriCNR.getCorpoEmail();				
				String messTerzi = componiMesaggio(actioncontext, terziDaConguagliare);
				if (corpo != null && corpo.contains(STRING_TO_BE_REPLACE)){
					corpo.replaceAll(STRING_TO_BE_REPLACE, messTerzi);
				}else{
					if (corpo == null)
						corpo = new String();
					corpo += messTerzi;
				}
				java.util.List<String> addressTO = new ArrayList<String>();
				java.util.List<String> addressCCN = new ArrayList<String>();
				String eMailReferente = null;
				for (Iterator<ReferenteAmministrativoBulk> iterator2 = referentiAmministrativi.iterator(); iterator2 .hasNext();) {
					ReferenteAmministrativoBulk dett = iterator2.next();
					if (dett.getCds().equalsIgnoreCase(cds.getCd_unita_organizzativa()) && dett.getSegr_refe().startsWith("S"))
						eMailReferente = dett.getEmail();
				}

				Parametri_enteBulk parametriEnte = ((Parametri_enteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession")).getParametriEnte(actioncontext.getUserContext());
				
				if (!parametriEnte.getTipo_db().equals(Parametri_enteBulk.DB_PRODUZIONE)){
					addressTO.add("matilde.durso@cnr.it");
					addressTO.add("r.pagano@libero.it");
					subject+= " Referente :"+eMailReferente;
				}else{
					if (eMailReferente != null){
						addressTO.add(eMailReferente);
						addressCCN.add(EMAILCCN);
						addressCCN.add(EMAILCCN2);
						addressCCN.add(EMAILCCN3);
					}else{
						addressTO.add(EMAILCCN);
						addressTO.add(EMAILCCN2);
						addressTO.add(EMAILCCN3);
						subject = "Non è stato possibile inviare l'E-Mail poichè non si conosce il riferimento del referente amministrativo del CdS :"+cds.getCd_unita_organizzativa();
					}
				}
				
				SendMail.sendMail(subject, corpo, addressTO, new ArrayList<String>(), addressCCN);			
			}
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		} 			
	}
		
	private String componiMesaggio(ActionContext actioncontext, List<V_terzi_da_completareBulk> terzi) throws ComponentException, RemoteException, EJBException, BusinessProcessException{
		StringBuffer mess = new StringBuffer("<table cellspacing=\"2\" cellpadding=\"2\" style=\" \">");
		TreeMap<String, List<V_terzi_da_completareBulk>> treeUO = new TreeMap<String, List<V_terzi_da_completareBulk>>(); 
		for (Iterator<V_terzi_da_completareBulk> iterator = terzi.iterator(); iterator.hasNext();) {
			V_terzi_da_completareBulk terzo = iterator.next();
			if (treeUO.get(terzo.getCd_unita_organizzativa()) == null){
				ArrayList<V_terzi_da_completareBulk> listaTerzi = new ArrayList<V_terzi_da_completareBulk>();
				listaTerzi.add(terzo);
				treeUO.put(terzo.getCd_unita_organizzativa(), listaTerzi);
			}else{
				treeUO.get(terzo.getCd_unita_organizzativa()).add(terzo);
			}
		}		
		for (Iterator<String> iterator = treeUO.keySet().iterator(); iterator.hasNext();) {
			String cdUO = iterator.next();
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk) createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), new Unita_organizzativaBulk(cdUO));
			mess.append("<tr><td colspan=\"2\" style=\"font-weight : bold\">");
			mess.append("Unità Organizzativa: "+uo.getCd_unita_organizzativa() + " " + uo.getDs_unita_organizzativa());
			mess.append("</td></tr>");
			mess.append("<tr><td style=\"font-weight : bold\">Codice Terzo</td><td style=\"font-weight : bold\">Denominazione</td></tr>");		
			for (Iterator<V_terzi_da_completareBulk> iteratorTerzi = treeUO.get(cdUO).iterator(); iteratorTerzi.hasNext();) {
				V_terzi_da_completareBulk terzo = iteratorTerzi.next();
				mess.append("<tr>");
				mess.append("<td>");
				mess.append(terzo.getCd_terzo());
				mess.append("</td>");
				mess.append("<td>");
				mess.append(terzo.getDenominazione());
				mess.append("</td>");
				mess.append("</tr>");
			}
			mess.append("<tr><td colspan=\"2\"></td></tr>");
		}
		mess.append("</table>");		
		return mess.toString();
	}
	
	@Override
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
		setUoEnte(context);
		readXMLReferentiAmministrativi();
		openIterator(context);
	}
	public void setUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			setUoEnte(true);	
	}

	public boolean isUoEnte() {
		return uoEnte;
	}

	public void setUoEnte(boolean uoEnte) {
		this.uoEnte = uoEnte;
	}	
	public void addToReferentiAmministrativi(ReferenteAmministrativoBulk referenteAmministrativo){
		referentiAmministrativi.add(referenteAmministrativo);
	}
	
	private void caricaOggetto(Element fstElmnt, String property, ReferenteAmministrativoBulk referenteAmministrativo) throws DOMException, IntrospectionException, InvocationTargetException, ParseException{
		NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(property);
		Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		NodeList fstNm = fstNmElmnt.getChildNodes();
		Introspector.setPropertyValue(referenteAmministrativo, property, ((Node) fstNm.item(0)).getNodeValue());
	}
	
	private void readXMLReferentiAmministrativi() throws BusinessProcessException{
		try {
			  URL url = new URL(XML_REFERENTI_AMMINISTRATIVI);
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  Document doc = db.parse(url.openStream());
			  doc.getDocumentElement().normalize();
			  NodeList nodeLst = doc.getElementsByTagName("record");
			  for (int s = 0; s < nodeLst.getLength(); s++) {
				  Node fstNode = nodeLst.item(s);
				  ReferenteAmministrativoBulk referenteAmministrativo = new ReferenteAmministrativoBulk();
				  if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					  Element fstElmnt = (Element) fstNode;
					  caricaOggetto(fstElmnt,"cds",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"uo",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"cd_terzo",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"cognome",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"nome",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"email",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"telefono",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"segr_refe",referenteAmministrativo);
					  caricaOggetto(fstElmnt,"descrizione",referenteAmministrativo);
				  }
				  addToReferentiAmministrativi(referenteAmministrativo);
			  }
		} catch (Exception e) {
			throw handleException(e);
		}
	}
}
