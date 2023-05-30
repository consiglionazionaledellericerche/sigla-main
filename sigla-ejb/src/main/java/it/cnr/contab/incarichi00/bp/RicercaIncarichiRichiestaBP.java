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

import it.cnr.contab.config00.bp.ResponseXMLBP;
import it.cnr.contab.config00.consultazioni.bulk.VContrattiTotaliDetBulk;
import it.cnr.contab.config00.contratto.bulk.*;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.incarichi00.bulk.*;
import it.cnr.contab.incarichi00.ejb.IncarichiRichiestaComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class RicercaIncarichiRichiestaBP extends SelezionatoreListaBP implements ResponseXMLBP {
	private String query;
	private String dominio;
	private String anno;
	private String page;
	private String rows;
	private Integer numMax;
	private Integer codiceErrore;
	private String user;
	private List incarichi;
	private String ricerca;
	private String tipofile="1";
	private String order;
	private String strRic;
	private String cdCds;
	private String tipoInc;
    private transient static final Logger logger = LoggerFactory.getLogger(RicercaIncarichiRichiestaBP.class);

	public RicercaIncarichiRichiestaBP() {
		super();
	}

	public RicercaIncarichiRichiestaBP(String s) {
		super(s);
	}
	private Element generaErrore(Document xmldoc){
		Element e = xmldoc.createElement(getTagRadice()+":errore");
		e.setAttribute("codice",codiceErrore.toString());
		Node n = xmldoc.createTextNode(Constants.erroriINC.get(codiceErrore));
		e.appendChild(n);
		return e;
	}
	private Element generaNumeroRichieste(Document xmldoc){
		Element e = xmldoc.createElement(getTagRadice()+":numris");
		int size;
		if (getIncarichi()!=null)
			size=getIncarichi().size();
		else
			size=0;
		Node n = xmldoc.createTextNode(new Integer(size).toString());
    	e.appendChild(n);
    	return e;	
	}
	private Element generaTotaleRichieste(Document xmldoc) throws RemoteException{
		Element e = xmldoc.createElement(getTagRadice()+":totris");
		int size;
		if (getIncarichi()!=null)
			size=this.getIterator().countElements();
		else
			size=0;
		Node n = xmldoc.createTextNode(new Integer(size).toString());
    	e.appendChild(n);
    	return e;	
	}
	private Element generaNumeroPagina(Document xmldoc) throws RemoteException{
		Element e = xmldoc.createElement(getTagRadice()+":numpag");
		int size;
		if (getIncarichi()!=null)
			size=this.getCurrentPage()+1;
		else
			size=0;
		Node n = xmldoc.createTextNode(new Integer(size).toString());
    	e.appendChild(n);
    	return e;	
	}
	private Element generaDettaglioIncarichiRichiesta(Document xmldoc, V_incarichi_richiestaBulk incarico){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dato;
		Element elementRichiesta = xmldoc.createElement(getTagRadice()+":richiesta");

		Element elementNumeroRichiesta = xmldoc.createElement(getTagRadice()+":numric");
		dato = incarico.getEsercizio().toString()+"/"+incarico.getPg_richiesta().toString(); 
		Node nodeNumeroRichiesta = xmldoc.createTextNode(dato!=null?dato:"");
		elementNumeroRichiesta.appendChild(nodeNumeroRichiesta);
		elementRichiesta.appendChild(elementNumeroRichiesta);

		Element elementEsercizio = xmldoc.createElement(getTagRadice()+":esercizio");
		dato = incarico.getEsercizio().toString(); 
		Node nodeEsercizio = xmldoc.createTextNode(dato!=null?dato:"");
		elementEsercizio.appendChild(nodeEsercizio);
		elementRichiesta.appendChild(elementEsercizio);

		Element elementProgressivo = xmldoc.createElement(getTagRadice()+":progressivo");
		dato = incarico.getPg_richiesta().toString(); 
		Node nodeProgressivo = xmldoc.createTextNode(dato!=null?dato:"");
		elementProgressivo.appendChild(nodeProgressivo);
		elementRichiesta.appendChild(elementProgressivo);

		// aggiungiamo gli Estremi del Richiedente
		Element elementRichiedente = xmldoc.createElement(getTagRadice()+":richiedente");

		Element elementCds = xmldoc.createElement(getTagRadice()+":cds");
		dato = incarico.getCd_cds(); 
		Node nodeCds = xmldoc.createTextNode(dato!=null?dato:"");
		elementCds.appendChild(nodeCds);
		elementRichiedente.appendChild(elementCds);

		Element elementIstituto = xmldoc.createElement(getTagRadice()+":istituto");
		dato = incarico.getDs_cds(); 
		Node nodeIstituto = xmldoc.createTextNode(dato!=null?dato:"");
		elementIstituto.appendChild(nodeIstituto);
		elementRichiedente.appendChild(elementIstituto);

		Element elementSede = xmldoc.createElement(getTagRadice()+":sede");
		dato = incarico.getSede(); 
		Node nodeSede = xmldoc.createTextNode(dato!=null?dato:"");
		elementSede.appendChild(nodeSede);
		elementRichiedente.appendChild(elementSede);

		Element elementEmail = xmldoc.createElement(getTagRadice()+":email");
		dato = incarico.getEmail_risposte(); 
		Node nodeEmail = xmldoc.createTextNode(dato!=null?dato:"");
		elementEmail.appendChild(nodeEmail);
		elementRichiedente.appendChild(elementEmail);

		elementRichiesta.appendChild(elementRichiedente);
		
		// aggiungiamo le Informazioni sull'Attività
		Element elementAttivita = xmldoc.createElement(getTagRadice()+":attivita");

		Element elementOggetto = xmldoc.createElement(getTagRadice()+":oggetto");
		dato = incarico.getAttivita();
		Node nodeAttivita = xmldoc.createCDATASection(dato!=null?dato:"");
		elementOggetto.appendChild(nodeAttivita);
		elementAttivita.appendChild(elementOggetto);

		Element elementDurata = xmldoc.createElement(getTagRadice()+":durata");
		dato = incarico.getDurata(); 
		Node nodeDurata = xmldoc.createTextNode(dato!=null?dato:"");
		elementDurata.appendChild(nodeDurata);
		elementAttivita.appendChild(elementDurata);

		Element elementSedeLavoro = xmldoc.createElement(getTagRadice()+":sedelavoro");
		dato = incarico.getSede_lavoro(); 
		Node nodeSedeLavoro = xmldoc.createTextNode(dato!=null?dato:"");
		elementSedeLavoro.appendChild(nodeSedeLavoro);
		elementAttivita.appendChild(elementSedeLavoro);

		Element elementNumRisorse = xmldoc.createElement(getTagRadice()+":numrisorse");
		dato = incarico.getNr_risorse_da_trovare().toString(); 
		Node nodeNumRisorse = xmldoc.createTextNode(dato!=null?dato:"");
		elementNumRisorse.appendChild(nodeNumRisorse);
		elementAttivita.appendChild(elementNumRisorse);

		elementRichiesta.appendChild(elementAttivita);

		// aggiungiamo le Informazioni Aggiuntive
		Element elementAggiuntive = xmldoc.createElement(getTagRadice()+":aggiuntive");

		Element elementNote = xmldoc.createElement(getTagRadice()+":note");
		dato = incarico.getNote();
		Node nodeNote = xmldoc.createTextNode(dato!=null?dato:"");
		elementNote.appendChild(nodeNote);
		elementAggiuntive.appendChild(elementNote);

		Element elementCompetenze = xmldoc.createElement(getTagRadice()+":competenze");
		dato = incarico.getCompetenze();
		Node nodeCompetenze = xmldoc.createCDATASection(dato!=null?dato:"");
		elementCompetenze.appendChild(nodeCompetenze);
		elementAggiuntive.appendChild(elementCompetenze);

		elementRichiesta.appendChild(elementAggiuntive);

		// aggiungiamo le Date di Validità
		Element elementDate = xmldoc.createElement(getTagRadice()+":date");

		Element elementDataInizioPubblicazione = xmldoc.createElement(getTagRadice()+":iniziopubblicazione");
		String datai=null;
		if(incarico.getData_pubblicazione()!=null)
			datai = formatter.format(incarico.getData_pubblicazione()).toString();
		dato = datai; 
		Node nodeDataPubblicazione = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataInizioPubblicazione.appendChild(nodeDataPubblicazione);
		elementDate.appendChild(elementDataInizioPubblicazione);

		Element elementDataFinePubblicazione = xmldoc.createElement(getTagRadice()+":finepubblicazione");
		String dataf=null;
		if(incarico.getData_fine_pubblicazione()!=null)
			dataf = formatter.format(incarico.getData_fine_pubblicazione()).toString();
		dato = dataf; 
		Node nodeDataFinePubblicazione = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataFinePubblicazione.appendChild(nodeDataFinePubblicazione);
		elementDate.appendChild(elementDataFinePubblicazione);

		elementRichiesta.appendChild(elementDate);

		return elementRichiesta;
	}
	private Element generaDettaglioIncarichiCollaborazione(Document xmldoc, V_incarichi_collaborazioneBulk incarico){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dato;
		Element elementRichiesta = xmldoc.createElement(getTagRadice()+":richiesta");

		Element elementNumeroRichiesta = xmldoc.createElement(getTagRadice()+":numric");
		dato = incarico.getEsercizio().toString()+"/"+incarico.getPg_procedura().toString(); 
		Node nodeNumeroRichiesta = xmldoc.createTextNode(dato!=null?dato:"");
		elementNumeroRichiesta.appendChild(nodeNumeroRichiesta);
		elementRichiesta.appendChild(elementNumeroRichiesta);

		Element elementEsercizio = xmldoc.createElement(getTagRadice()+":esercizio");
		dato = incarico.getEsercizio().toString(); 
		Node nodeEsercizio = xmldoc.createTextNode(dato!=null?dato:"");
		elementEsercizio.appendChild(nodeEsercizio);
		elementRichiesta.appendChild(elementEsercizio);

		Element elementProgressivo = xmldoc.createElement(getTagRadice()+":progressivo");
		dato = incarico.getPg_procedura().toString(); 
		Node nodeProgressivo = xmldoc.createTextNode(dato!=null?dato:"");
		elementProgressivo.appendChild(nodeProgressivo);
		elementRichiesta.appendChild(elementProgressivo);

		// aggiungiamo gli Estremi del Richiedente
		Element elementRichiedente = xmldoc.createElement(getTagRadice()+":richiedente");

		Element elementCds = xmldoc.createElement(getTagRadice()+":cds");
		dato = incarico.getCd_cds(); 
		Node nodeCds = xmldoc.createTextNode(dato!=null?dato:"");
		elementCds.appendChild(nodeCds);
		elementRichiedente.appendChild(elementCds);

		Element elementIstituto = xmldoc.createElement(getTagRadice()+":istituto");
		dato = incarico.getDs_cds(); 
		Node nodeIstituto = xmldoc.createTextNode(dato!=null?dato:"");
		elementIstituto.appendChild(nodeIstituto);
		elementRichiedente.appendChild(elementIstituto);

		Element elementSede = xmldoc.createElement(getTagRadice()+":sede");
		dato = incarico.getSede(); 
		Node nodeSede = xmldoc.createTextNode(dato!=null?dato:"");
		elementSede.appendChild(nodeSede);
		elementRichiedente.appendChild(elementSede);

		elementRichiesta.appendChild(elementRichiedente);
		
		// aggiungiamo le Informazioni sull'URL del bando
		Incarichi_procedura_archivioBulk bando = incarico.getIncaricoProcedura().getBando();
		if (bando!=null && bando.getCms_node_ref()!=null) {
			Element elementUrl = xmldoc.createElement(getTagRadice()+":url");
			dato = "genericdownload/"+bando.getNome_file()+"?nodeRef="+bando.getCms_node_ref(); 
			Node nodeUrl = xmldoc.createTextNode(dato!=null?dato:"");
			elementUrl.appendChild(nodeUrl);
			elementRichiesta.appendChild(elementUrl);
		}

		// aggiungiamo le Informazioni sull'Attività
		Element elementAttivita = xmldoc.createElement(getTagRadice()+":attivita");

		Element elementOggetto = xmldoc.createElement(getTagRadice()+":oggetto");
		dato = incarico.getOggetto();
		Node nodeAttivita = xmldoc.createCDATASection(dato!=null?dato:"");
		elementOggetto.appendChild(nodeAttivita);
		elementAttivita.appendChild(elementOggetto);

		// aggiungiamo le Informazioni sul numero contratti
		Element elementNumContratti = xmldoc.createElement(getTagRadice()+":numcontratti");
		dato = incarico.getNr_contratti().toString(); 
		Node nodeNumContratti = xmldoc.createTextNode(dato!=null?dato:"");
		elementNumContratti.appendChild(nodeNumContratti);
		elementAttivita.appendChild(elementNumContratti);

		elementRichiesta.appendChild(elementAttivita);

		Element elementDataScadenza = xmldoc.createElement(getTagRadice()+":datascadenza");
		String datas=null;
		if(incarico.getDt_scadenza()!=null)
			datas = formatter.format(incarico.getDt_scadenza()).toString();
		dato = datas; 
		Node nodeDataScadenza = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataScadenza.appendChild(nodeDataScadenza);
		
		elementRichiesta.appendChild(elementDataScadenza);
		elementDataScadenza = xmldoc.createElement(getTagRadice()+":datapubblicazione");
		datas=null;
		if(incarico.getDt_pubblicazione()!=null)
			datas = formatter.format(incarico.getDt_pubblicazione()).toString();
		dato = datas; 
		nodeDataScadenza = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataScadenza.appendChild(nodeDataScadenza);
		elementRichiesta.appendChild(elementDataScadenza);
		return elementRichiesta;
	}

	private Element generaDettaglioContratti(Document xmldoc, ContrattoBulk contratto, String tipoFile) throws ParseException{

		String dato;
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		Element elementContratto = xmldoc.createElement(getTagRadice()+":contratto");

		Element elementChiave = xmldoc.createElement(getTagRadice()+":chiave");
		dato = String.valueOf(contratto.getEsercizio()).concat("/").concat(String.valueOf(contratto.getPg_contratto())); 
		elementChiave.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementChiave);
		
		Element elementDenominazione = xmldoc.createElement(getTagRadice()+":denominazione");
		dato = contratto.getFigura_giuridica_esterna().getDenominazione_sede(); 
		Node nodeNumeroRichiesta = xmldoc.createTextNode(dato!=null?dato:"");
		elementDenominazione.appendChild(nodeNumeroRichiesta);
		elementContratto.appendChild(elementDenominazione);

		Element elementCodFis = xmldoc.createElement(getTagRadice()+":codicefiscale");
		dato = contratto.getFigura_giuridica_esterna().getAnagrafico().getCodice_fiscale(); 
		elementCodFis.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementCodFis);
		
		Element elementPariva = xmldoc.createElement(getTagRadice()+":partitaiva");
		dato = contratto.getFigura_giuridica_esterna().getAnagrafico().getPartita_iva(); 
		elementPariva.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementPariva);
		
		Element elementItaEst = xmldoc.createElement(getTagRadice()+":ita_est");
		dato = contratto.getFigura_giuridica_esterna().getAnagrafico().getTi_italiano_estero(); 
		elementItaEst.appendChild(xmldoc.createTextNode(dato!=null? dato.compareTo("I")==0?"I":"E":""));
		elementContratto.appendChild(elementItaEst);
		
		Element elementOggetto = xmldoc.createElement(getTagRadice()+":oggetto");
		dato = contratto.getOggetto();
		elementOggetto.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementOggetto);
		
		Element elementImporto = xmldoc.createElement(getTagRadice()+":importo");
		dato = new it.cnr.contab.util.EuroFormat().format(contratto.getIm_contratto_passivo()); 
		elementImporto.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementImporto);
		 
		Element elementImportoNetto = xmldoc.createElement(getTagRadice()+":importo_netto");
		dato = new it.cnr.contab.util.EuroFormat().format(contratto.getIm_contratto_passivo_netto());
		elementImportoNetto.appendChild(xmldoc.createTextNode(dato!=null?dato:"0")); 
		elementContratto.appendChild(elementImportoNetto);

		Element elementImportoLiq = xmldoc.createElement(getTagRadice() + ":importo_liquidato_netto");
		dato = new it.cnr.contab.util.EuroFormat().format(contratto.getTot_docamm_cont_spe_netto());
		elementImportoLiq.appendChild(xmldoc.createTextNode(dato != null ? dato : "0"));
		elementContratto.appendChild(elementImportoLiq);

		if (tipoFile.equals("6")) {
			Element elementPagamenti = xmldoc.createElement(getTagRadice() + ":pagamenti");

			contratto.getMapPagamenti().forEach((dataInizio, importoMandati) -> {
				Element elementPagamento = xmldoc.createElement(getTagRadice() + ":pagamento");

				Element elementDataLimite = xmldoc.createElement(getTagRadice()+":data_limite");
				String datalimite = formatter.format(dataInizio.getTime()).toString();
				elementDataLimite.appendChild(xmldoc.createTextNode(datalimite!=null?datalimite:""));
				elementPagamento.appendChild(elementDataLimite);

				Element elementImportoPagato = xmldoc.createElement(getTagRadice()+":importo_pagato_netto");
				String importo = new it.cnr.contab.util.EuroFormat().format(importoMandati);
				elementImportoPagato.appendChild(xmldoc.createTextNode(importo!=null?importo:""));
				elementPagamento.appendChild(elementImportoPagato);

				elementPagamenti.appendChild(elementPagamento);
			});
			elementContratto.appendChild(elementPagamenti);
		}

		Element elementTipoNorma = xmldoc.createElement(getTagRadice()+":tiponorma");
		dato = contratto.getTipo_norma(); 
		elementTipoNorma.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementTipoNorma);

		Element elementDenominazioneCDR = xmldoc.createElement(getTagRadice()+":cdr");
		dato = contratto.getUnita_organizzativa().getDs_unita_organizzativa(); 
		elementDenominazioneCDR.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementDenominazioneCDR);

		Element elementDirettore = xmldoc.createElement(getTagRadice()+":responsabile");
		dato = contratto.getDirettore().getDenominazione_sede(); 
		elementDirettore.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementDirettore);

		Element elementResponsabileProcedimento = xmldoc.createElement(getTagRadice()+":responsabile_procedimento");
		dato = contratto.getResponsabile().getDenominazione_sede(); 
		elementResponsabileProcedimento.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementResponsabileProcedimento);
		
		Element elementBeneficiario = xmldoc.createElement(getTagRadice()+":mod_individuazione_beneficiario");
		dato = contratto.getProcedura_amministrativa().getDs_proc_amm(); 
		elementBeneficiario.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementBeneficiario);
			
		Element elementDataInizio = xmldoc.createElement(getTagRadice()+":datainizio");
		String datai=null;
		if(contratto.getDt_inizio_validita()!=null)
			datai = formatter.format(contratto.getDt_inizio_validita()).toString();
		dato = datai; 
		Node nodeDataInizio = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataInizio.appendChild(nodeDataInizio);
		elementContratto.appendChild(elementDataInizio);

		Element elementDataFine = xmldoc.createElement(getTagRadice()+":datafine");
		String dataf=null;
		if(contratto.getDt_proroga()!=null)
			dataf = formatter.format(contratto.getDt_proroga()).toString();
		else if(contratto.getDt_fine_validita()!=null)
			dataf = formatter.format(contratto.getDt_fine_validita()).toString();
		dato = dataf; 
		Node nodeDataFine = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataFine.appendChild(nodeDataFine);
		elementContratto.appendChild(elementDataFine);

		Element elementDataStipula = xmldoc.createElement(getTagRadice()+":datastipula");
		String datas=null;
		if(contratto.getDt_stipula()!=null)
			datas = formatter.format(contratto.getDt_stipula()).toString();
		dato = datas; 
		Node nodeDataStipula = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataStipula.appendChild(nodeDataStipula);
		elementContratto.appendChild(elementDataStipula);
		
		Element elementCig = xmldoc.createElement(getTagRadice()+":cig");
		if(contratto.getCig()!=null &&contratto.getCig().getCdCig()!=null)
			dato = contratto.getCig().getCdCig();
		else
			dato="0000000000";
		elementCig.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementCig);
		
		Element elementCodFisCnr = xmldoc.createElement(getTagRadice()+":codicefiscaleCNR");
		dato = contratto.getFigura_giuridica_interna().getAnagrafico().getCodice_fiscale(); 
		elementCodFisCnr.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementCodFisCnr);

		Element elementCodiceAnac = xmldoc.createElement(getTagRadice()+":codice_anac");
		dato = contratto.getProcedura_amministrativa().getCodice_anac(); 
		elementCodiceAnac.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementCodiceAnac);
		
		if ( !contratto.getDitteInvitate().isEmpty() && contratto.getDitteInvitate().size()!=0 ){ 
			String oldRag=null;
			Element ditte = xmldoc.createElement(getTagRadice()+":ditte");
			Element raggruppamento=null;
			Element ditta=null;
			for (java.util.Iterator i=contratto.getDitteInvitate().iterator();i.hasNext();){ 
				Ass_contratto_ditteBulk ditteInv =(Ass_contratto_ditteBulk)i.next();
				if(ditteInv.getDenominazione_rti()!=null && (oldRag==null || oldRag.compareTo(ditteInv.getDenominazione_rti())!=0)){
					raggruppamento = xmldoc.createElement(getTagRadice()+":raggruppamento");
					for (java.util.Iterator j=contratto.getDitteInvitate().iterator();j.hasNext();){ 
						Ass_contratto_ditteBulk ditteInvRag =(Ass_contratto_ditteBulk)j.next();	
						 if(ditteInvRag.getDenominazione_rti()!=null && ditteInv.getDenominazione_rti().compareTo(ditteInvRag.getDenominazione_rti())==0){
							
							ditta = xmldoc.createElement(getTagRadice()+":ditta");
						 	Element denominaz = xmldoc.createElement(getTagRadice()+":denominaz");
							dato =ditteInvRag.getDenominazione(); 					
							denominaz.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
							ditta.appendChild(denominaz);
							
							Element cf = xmldoc.createElement(getTagRadice()+":cf");
							dato =ditteInvRag.getCodice_fiscale(); 					
							cf.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
							ditta.appendChild(cf);
							
							Element id_fiscale = xmldoc.createElement(getTagRadice()+":id_fiscale");
							dato =ditteInvRag.getId_fiscale(); 					
							id_fiscale.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
							ditta.appendChild(id_fiscale);
							
							Element ruolo = xmldoc.createElement(getTagRadice()+":ruolo");
							dato =ditteInvRag.getRuolo(); 					
							ruolo.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
							ditta.appendChild(ruolo);

							Element offerta_presentata = xmldoc.createElement(getTagRadice()+":offerta_presentata");
							dato =ditteInvRag.getFl_offerta_presentata()?"true":"false";
							offerta_presentata.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
							ditta.appendChild(offerta_presentata);

							oldRag=ditteInvRag.getDenominazione_rti();
							raggruppamento.appendChild(ditta);
						} // ditteInv denominazioneRti = ditteInvRag denominazioneRti
					}  // for ditteInvRag 
					  ditte.appendChild(raggruppamento);
				}  // if rti e nuovorag o cambio rag
				else if(ditteInv.getDenominazione_rti()==null)
				{
				    ditta = xmldoc.createElement(getTagRadice()+":ditta");
					
					Element denominaz = xmldoc.createElement(getTagRadice()+":denominaz");
					dato =ditteInv.getDenominazione(); 					
					denominaz.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					ditta.appendChild(denominaz);
					
					Element cf = xmldoc.createElement(getTagRadice()+":cf");
					dato =ditteInv.getCodice_fiscale(); 					
					cf.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					ditta.appendChild(cf);
					
					Element id_fiscale = xmldoc.createElement(getTagRadice()+":id_fiscale");
					dato =ditteInv.getId_fiscale(); 					
					id_fiscale.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					ditta.appendChild(id_fiscale);

					Element offerta_presentata = xmldoc.createElement(getTagRadice()+":offerta_presentata");
					dato =ditteInv.getFl_offerta_presentata()?"true":"false";
					offerta_presentata.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					ditta.appendChild(offerta_presentata);

					ditte.appendChild(ditta);	
				}	
			}
			elementContratto.appendChild(ditte);
		}
		if (!contratto.getArchivioAllegatiFlusso().isEmpty()){
			Element allegati = xmldoc.createElement(getTagRadice()+":allegati");
			
			for (AllegatoContrattoFlussoDocumentBulk allegato : contratto.getArchivioAllegatiFlusso()) {
				Element all = xmldoc.createElement(getTagRadice()+allegato.getType());

				Element label = xmldoc.createElement(getTagRadice()+":label");
				dato =allegato.getLabel(); 					
				label.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				all.appendChild(label);
				
				Element nodeRef = xmldoc.createElement(getTagRadice()+":id");
				dato =allegato.getNodeId();
				nodeRef.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				all.appendChild(nodeRef);

				allegati.appendChild(all);
			}
			elementContratto.appendChild(allegati);
		}
		return elementContratto;
	}	
		
	private Element generaDettaglioIncarichiArt18(Document xmldoc, V_incarichi_elencoBulk incarico) throws ParseException{
		String dato;
		Element elementIncarico = xmldoc.createElement(getTagRadice()+":contratto");

		Element elementChiave = xmldoc.createElement(getTagRadice()+":chiave");
		dato = String.valueOf(incarico.getEsercizio()).concat("/").concat(String.valueOf(incarico.getPg_repertorio())); 
		elementChiave.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementChiave);
		
		Element elementDenominazione = xmldoc.createElement(getTagRadice()+":denominazione");
		dato = incarico.getBenef_denominazione_sede(); 
		Node nodeNumeroRichiesta = xmldoc.createTextNode(dato!=null?dato:"");
		elementDenominazione.appendChild(nodeNumeroRichiesta);
		elementIncarico.appendChild(elementDenominazione);

		Element elementCodFis = xmldoc.createElement(getTagRadice()+":codicefiscale");
		dato = incarico.getBenef_codice_fiscale(); 
		elementCodFis.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementCodFis);
		
		Element elementPariva = xmldoc.createElement(getTagRadice()+":partitaiva");
		dato = incarico.getBenef_partita_iva(); 
		elementPariva.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementPariva);
		
		Element elementImporto = xmldoc.createElement(getTagRadice()+":importo");
		dato = new it.cnr.contab.util.EuroFormat().format(incarico.getImporto_lordo_con_variazioni()); 
		elementImporto.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementImporto);

		Element elementTipoNorma = xmldoc.createElement(getTagRadice()+":tiponorma");
		dato = incarico.getDs_tipo_norma(); 
		elementTipoNorma.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementTipoNorma);

		Element elementDenominazioneCDR = xmldoc.createElement(getTagRadice()+":cdr");
		dato = incarico.getDs_unita_organizzativa(); 
		elementDenominazioneCDR.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementDenominazioneCDR);

		Element elementDirettore = xmldoc.createElement(getTagRadice()+":responsabile");
		dato = incarico.getFirm_denominazione_sede(); 
		elementDirettore.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementDirettore);

		Element elementResponsabileProcedimento = xmldoc.createElement(getTagRadice()+":responsabile_procedimento");
		dato = incarico.getResp_denominazione_sede();
		elementResponsabileProcedimento.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementResponsabileProcedimento);
		
		Element elementBeneficiario = xmldoc.createElement(getTagRadice()+":mod_individuazione_beneficiario");
		dato = incarico.getDs_proc_amm(); 
		elementBeneficiario.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementIncarico.appendChild(elementBeneficiario);

		Incarichi_repertorio_archivioBulk contratto = incarico.getIncaricoRepertorio().getContratto();
		if (contratto!=null && contratto.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_contratto");
			dato = "genericdownload/"+contratto.getNome_file()+"?nodeRef="+contratto.getCms_node_ref(); 
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementIncarico.appendChild(elementLink);
		}

		Incarichi_repertorio_archivioBulk curriculum = incarico.getIncaricoRepertorio().getCurriculumVincitore();
		if (curriculum!=null && curriculum.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_capitolato");
			dato = "genericdownload/"+curriculum.getNome_file()+"?nodeRef="+curriculum.getCms_node_ref(); 
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementIncarico.appendChild(elementLink);
		}

		Incarichi_repertorio_archivioBulk conflitto = incarico.getIncaricoRepertorio().getConflittoInteressi();
		if (conflitto!=null && conflitto.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_conflitto_interessi");
			dato = "genericdownload/"+conflitto.getNome_file()+"?nodeRef="+conflitto.getCms_node_ref();
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementIncarico.appendChild(elementLink);
		}

		Incarichi_repertorio_archivioBulk attestazione = incarico.getIncaricoRepertorio().getAttestazioneDirettore();
		if (attestazione!=null && attestazione.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_attestazione_direttore");
			dato = "genericdownload/"+attestazione.getNome_file()+"?nodeRef="+attestazione.getCms_node_ref();
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementIncarico.appendChild(elementLink);
		}

		Incarichi_procedura_archivioBulk progetto = incarico.getIncaricoRepertorio().getIncarichi_procedura().getProgetto();
		if (progetto!=null && progetto.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_progetto");
			dato = "genericdownload/"+progetto.getNome_file()+"?nodeRef="+progetto.getCms_node_ref(); 
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementIncarico.appendChild(elementLink);
		}
		return elementIncarico;
	}	

	private Element generaDettaglioIncarichiElenco(Document xmldoc, V_incarichi_elencoBulk incarico) throws ParseException{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dato;
		Element elementRichiesta = xmldoc.createElement(getTagRadice()+":richiesta");

		Element elementNumeroRichiesta = xmldoc.createElement(getTagRadice()+":numric");
		dato = incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString(); 
		Node nodeNumeroRichiesta = xmldoc.createTextNode(dato!=null?dato:"");
		elementNumeroRichiesta.appendChild(nodeNumeroRichiesta);
		elementRichiesta.appendChild(elementNumeroRichiesta);

		Element elementEsercizio = xmldoc.createElement(getTagRadice()+":esercizio");
		dato = incarico.getEsercizio().toString(); 
		Node nodeEsercizio = xmldoc.createTextNode(dato!=null?dato:"");
		elementEsercizio.appendChild(nodeEsercizio);
		elementRichiesta.appendChild(elementEsercizio);

		Element elementProgressivo = xmldoc.createElement(getTagRadice()+":progressivo");
		dato = incarico.getPg_repertorio().toString(); 
		Node nodeProgressivo = xmldoc.createTextNode(dato!=null?dato:"");
		elementProgressivo.appendChild(nodeProgressivo);
		elementRichiesta.appendChild(elementProgressivo);

		Element elementCdr = xmldoc.createElement(getTagRadice()+":cdr");
		dato = incarico.getDs_unita_organizzativa(); 
		Node nodeCdr = xmldoc.createTextNode(dato!=null?dato:"");
		elementCdr.appendChild(nodeCdr);
		elementRichiesta.appendChild(elementCdr);

		// aggiungiamo gli Estremi del Richiedente
		/*
		Element elementRichiedente = xmldoc.createElement(getTagRadice()+":richiedente");

		Element elementCds = xmldoc.createElement(getTagRadice()+":cds");
		dato = incarico.getCd_cds(); 
		Node nodeCds = xmldoc.createTextNode(dato!=null?dato:"");
		elementCds.appendChild(nodeCds);
		elementRichiedente.appendChild(elementCds);

		Element elementIstituto = xmldoc.createElement(getTagRadice()+":istituto");
		dato = incarico.getDs_cds(); 
		Node nodeIstituto = xmldoc.createTextNode(dato!=null?dato:"");
		elementIstituto.appendChild(nodeIstituto);
		elementRichiedente.appendChild(elementIstituto);

		Element elementSede = xmldoc.createElement(getTagRadice()+":sede");
		dato = incarico.getSede(); 
		Node nodeSede = xmldoc.createTextNode(dato!=null?dato:"");
		elementSede.appendChild(nodeSede);
		elementRichiedente.appendChild(elementSede);

		elementRichiesta.appendChild(elementRichiedente);
		*/
		
		Element elementNominativo = xmldoc.createElement(getTagRadice()+":nominativo");
		dato = incarico.getBenef_denominazione_sede();
		Node nodeNominativo = xmldoc.createTextNode(dato!=null?dato:"");
		elementNominativo.appendChild(nodeNominativo);
		elementRichiesta.appendChild(elementNominativo);

		Element elementOggetto = xmldoc.createElement(getTagRadice()+":oggetto");
		dato = incarico.getOggetto();
		Node nodeAttivita = xmldoc.createCDATASection(dato!=null?dato:"");
		elementOggetto.appendChild(nodeAttivita);
		elementRichiesta.appendChild(elementOggetto);

		Element elementImporto = xmldoc.createElement(getTagRadice()+":importo");
		String sImp=null;
		if(incarico.getImporto_lordo_con_variazioni()!=null && incarico.getImporto_lordo_con_variazioni().compareTo(Utility.ZERO)!=0)
			sImp = new it.cnr.contab.util.EuroFormat().format(incarico.getImporto_lordo_con_variazioni());
		else
			sImp = "Gratuito";
		dato = sImp; 
		Node nodeImporto = xmldoc.createTextNode(dato!=null?dato:"");
		elementImporto.appendChild(nodeImporto);
		elementRichiesta.appendChild(elementImporto);

		Element elementDataInizio = xmldoc.createElement(getTagRadice()+":datainizio");
		String datai=null;
		if(incarico.getDt_inizio_validita()!=null)
			datai = formatter.format(incarico.getDt_inizio_validita()).toString();
		dato = datai; 
		Node nodeDataInizio = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataInizio.appendChild(nodeDataInizio);
		elementRichiesta.appendChild(elementDataInizio);

		Element elementDataFine = xmldoc.createElement(getTagRadice()+":datafine");
		java.sql.Timestamp tsDataFine = null;
		if(incarico.getIncaricoVariazione()!=null && incarico.getIncaricoVariazione().getDt_fine_validita()!=null)
			tsDataFine = incarico.getIncaricoVariazione().getDt_fine_validita();
		else if(incarico.getDt_fine_validita()!=null)
			tsDataFine = incarico.getDt_fine_validita();
		dato = formatter.format(tsDataFine);
		Node nodeDataFine = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataFine.appendChild(nodeDataFine);
		elementRichiesta.appendChild(elementDataFine);

		Element elementDataProroga = xmldoc.createElement(getTagRadice()+":dataproroga");
		String datap=null;
		if(incarico.getDt_proroga()!=null) {
			if (tsDataFine == null || incarico.getDt_proroga().compareTo(tsDataFine) > 0)
				datap = formatter.format(incarico.getDt_proroga());
		}
		dato = datap;
		Node nodeDataProroga = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataProroga.appendChild(nodeDataProroga);
		elementRichiesta.appendChild(elementDataProroga);

		Element elementDataStipula = xmldoc.createElement(getTagRadice()+":datastipula");
		String datas=null;
		if(incarico.getDt_stipula()!=null)
			datas = formatter.format(incarico.getDt_stipula()).toString();
		dato = datas; 
		Node nodeDataStipula = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataStipula.appendChild(nodeDataStipula);
		elementRichiesta.appendChild(elementDataStipula);

		Element elementProvvedimento = xmldoc.createElement(getTagRadice()+":provvedimento");
		dato = incarico.getDs_provvedimento();
		Node nodeProvvedimento = xmldoc.createCDATASection(dato!=null?dato:"");
		elementProvvedimento.appendChild(nodeProvvedimento);
		elementRichiesta.appendChild(elementProvvedimento);
		
		Element elementDataDichiarazione = xmldoc.createElement(getTagRadice()+":datadichiarazione");
		datas=null;
		if(incarico.getDt_dichiarazione()!=null)
			datas = formatter.format(incarico.getDt_dichiarazione()).toString();
		dato = datas; 
		Node nodeDataDichiarazione= xmldoc.createTextNode(dato!=null?dato:"");
		elementDataDichiarazione.appendChild(nodeDataDichiarazione);
		elementRichiesta.appendChild(elementDataDichiarazione);

		Incarichi_repertorio_archivioBulk curriculum = incarico.getIncaricoRepertorio().getCurriculumVincitore();
		if (curriculum!=null && curriculum.getCms_node_ref()!=null && incarico.getDt_stipula()!=null) {
			GregorianCalendar gc = (java.util.GregorianCalendar)GregorianCalendar.getInstance();
			gc.setTime(incarico.getDt_stipula());
			if (gc.get(GregorianCalendar.YEAR)>=2013) {
				Element elementLink = xmldoc.createElement(getTagRadice()+":url_curriculum");
				dato = "genericdownload/"+curriculum.getNome_file()+"?nodeRef="+curriculum.getCms_node_ref(); 
				elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				elementRichiesta.appendChild(elementLink);
			}
		}

		if (incarico.getIncaricoRepertorio().getAggiornamentiCurriculumVincitore()!=null && 
				!incarico.getIncaricoRepertorio().getAggiornamentiCurriculumVincitore().isEmpty()){
			Element elementAggiornamentiCurriculum = xmldoc.createElement(getTagRadice()+":aggiornamenti_curriculum");
			for (Iterator<Incarichi_repertorio_archivioBulk> i = incarico.getIncaricoRepertorio().getAggiornamentiCurriculumVincitore().iterator();i.hasNext();){
				Incarichi_repertorio_archivioBulk aggCurriculum = i.next();
				
				Element elementAggiornamento = xmldoc.createElement(getTagRadice()+":aggiornamento_curriculum");

				Element elementLink = xmldoc.createElement(getTagRadice()+":url_aggiornamento_curriculum");
				dato = "genericdownload/"+aggCurriculum.getNome_file()+"?nodeRef="+aggCurriculum.getCms_node_ref(); 
				elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				elementAggiornamento.appendChild(elementLink);

				Element elementDataAggiornamento = xmldoc.createElement(getTagRadice()+":data_aggiornamento_curriculum");
				dato = formatter.format(aggCurriculum.getDacr()).toString();
				Node nodeDataAggiornamento = xmldoc.createTextNode(dato!=null?dato:"");
				elementDataAggiornamento.appendChild(nodeDataAggiornamento);
				elementAggiornamento.appendChild(elementDataAggiornamento);

				elementAggiornamentiCurriculum.appendChild(elementAggiornamento);
			}
			elementRichiesta.appendChild(elementAggiornamentiCurriculum);
		}

		Incarichi_repertorio_archivioBulk conflittoInteresse = incarico.getIncaricoRepertorio().getConflittoInteressi();
		if (conflittoInteresse!=null && conflittoInteresse.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_conflitto_interessi");
			dato = "genericdownload/"+conflittoInteresse.getNome_file()+"?nodeRef="+conflittoInteresse.getCms_node_ref();
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementRichiesta.appendChild(elementLink);
		}

		Incarichi_repertorio_archivioBulk attestazioneDirettore = incarico.getIncaricoRepertorio().getAttestazioneDirettore();
		if (attestazioneDirettore!=null && attestazioneDirettore.getCms_node_ref()!=null) {
			Element elementLink = xmldoc.createElement(getTagRadice()+":url_attestazione_direttore");
			dato = "genericdownload/"+attestazioneDirettore.getNome_file()+"?nodeRef="+attestazioneDirettore.getCms_node_ref();
			elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
			elementRichiesta.appendChild(elementLink);
		}

		if (incarico.getIncarichi_repertorio_rapp_detColl()!=null && !incarico.getIncarichi_repertorio_rapp_detColl().isEmpty()){
			Element elementAltriRapporti = xmldoc.createElement(getTagRadice()+":altrirapporti");
		
			for (Iterator<Incarichi_repertorio_rapp_detBulk> i = incarico.getIncarichi_repertorio_rapp_detColl().iterator();i.hasNext();){
				Incarichi_repertorio_rapp_detBulk rapDett = i.next();

				Element elementRapporto = xmldoc.createElement(getTagRadice()+":rapporto");

				Element elementConferenteRapporto = xmldoc.createElement(getTagRadice()+":conferente");
				dato = rapDett.getConferente_rapporto();
				Node nodeConferenteRapporto = xmldoc.createCDATASection(dato!=null?dato:"");
				elementConferenteRapporto.appendChild(nodeConferenteRapporto);
				elementRapporto.appendChild(elementConferenteRapporto);

				Element elementNaturaRapporto = xmldoc.createElement(getTagRadice()+":natura");
				dato = Incarichi_repertorio_rapp_detBulk.tipoRapportoKeys.get(rapDett.getNatura_rapporto()).toString();
				Node nodeNaturaRapporto = xmldoc.createTextNode(dato!=null?dato:"");
				elementNaturaRapporto.appendChild(nodeNaturaRapporto);
				elementRapporto.appendChild(elementNaturaRapporto);

				Element elementDataInizioRapporto = xmldoc.createElement(getTagRadice()+":datainizio");
				dato = formatter.format(rapDett.getDt_ini_rapporto()).toString();
				Node nodeDataInizioRapporto = xmldoc.createTextNode(dato!=null?dato:"");
				elementDataInizioRapporto.appendChild(nodeDataInizioRapporto);
				elementRapporto.appendChild(elementDataInizioRapporto);

				Element elementImportoRapporto = xmldoc.createElement(getTagRadice()+":importo");
				dato = new it.cnr.contab.util.EuroFormat().format(rapDett.getImporto_rapporto());
				Node nodeImportoRapporto = xmldoc.createTextNode(dato!=null?dato:"");
				elementImportoRapporto.appendChild(nodeImportoRapporto);
				elementRapporto.appendChild(elementImportoRapporto);
				
				elementAltriRapporti.appendChild(elementRapporto);
			}
			elementRichiesta.appendChild(elementAltriRapporti);
		}
		return elementRichiesta;
	}
    public void generaXML(PageContext pagecontext) throws IOException, ServletException{
		try {
			if (getNumMax()==null)
				setNumMax(new Integer(100));
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	DOMImplementation impl = builder.getDOMImplementation();
			Document xmldoc = impl.createDocument("http://contab.cnr.it/"+getTagRadice(), getTagRadice()+":root", null);

	    	Element root = xmldoc.getDocumentElement();
	    	if (codiceErrore!= null){
	    		root.appendChild(generaErrore(xmldoc));
	    	}else{
	    		root.appendChild(generaNumeroRichieste(xmldoc));
	    		root.appendChild(generaTotaleRichieste(xmldoc));
	    		root.appendChild(generaNumeroPagina(xmldoc));	    		
	    		int num = 0;
	    		if (getIncarichi() != null && !getIncarichi().isEmpty()){
		    		for (Iterator i = getIncarichi().iterator();i.hasNext()&&num<getNumMax().intValue();){
		    			OggettoBulk incarico = (OggettoBulk)i.next();
		    			Element elem=null;
		    			if (getTipofile().equals("1"))
		    				elem = generaDettaglioIncarichiRichiesta(xmldoc,(V_incarichi_richiestaBulk)incarico);
		    			else if (getTipofile().equals("2"))
		    				elem = generaDettaglioIncarichiCollaborazione(xmldoc,(V_incarichi_collaborazioneBulk)incarico);
		    			else if (getTipofile().equals("3"))
		    				elem = generaDettaglioIncarichiElenco(xmldoc,(V_incarichi_elencoBulk)incarico);
		    			else if (getTipofile().equals("4") || getTipofile().equals("6")){
		    				elem = generaDettaglioContratti(xmldoc,(ContrattoBulk)incarico, getTipofile());
		    			}
		    			else if (getTipofile().equals("5"))
		    				elem = generaDettaglioIncarichiArt18(xmldoc,(V_incarichi_elencoBulk)incarico);
		    			if (elem!=null)
		    				root.appendChild(elem);
		    		}
	    		}
	    	}
	    	DOMSource domSource = new DOMSource(xmldoc);
	    	StreamResult streamResult = new StreamResult(pagecontext.getOut());
	    	TransformerFactory tf = TransformerFactory.newInstance();
	    	Transformer serializer = tf.newTransformer();
	    	serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
	    	//serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://150.146.206.250/DTD/"+getTagRadice()+".dtd");
	    	serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,getTagRadice());
	    	serializer.setOutputProperty(OutputKeys.INDENT,"yes");
	    	serializer.setOutputProperty(OutputKeys.STANDALONE,"no");
	    	//serializer.setOutputProperty(OutputKeys.MEDIA_TYPE,"text/xml");
	    	serializer.transform(domSource, streamResult);
	    	closed();
		} catch (ParserConfigurationException e) {
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		} catch (ParseException e) {
		} catch (BusinessProcessException e) {
		}

    }
    
	public void eseguiRicerca(it.cnr.jada.action.ActionContext context)throws BusinessProcessException{
		// impostiamo i valori di default
		String query = getQuery();
		String dominio = getDominio();
		String anno = getAnno();
		Integer esercizio = null;		

		if (dominio==null) {
			dominio="data";
		}
		if(dominio.equalsIgnoreCase("data")){
			if (!getTipofile().equals("4") && !getTipofile().equals("6")){
				if (query!=null&&(!query.equalsIgnoreCase(Constants.RICHIESTE_IN_CORSO)&&!query.equalsIgnoreCase(Constants.RICHIESTE_SCADUTE))) {
					codiceErrore = Constants.ERRORE_INC_104;
					return;
				}
			}else if (query!=null&&(!query.equalsIgnoreCase("chiave"))) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}
		}
		if(anno!=null){
			try{
				esercizio = new Integer(anno);
			} catch(Exception e) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}
		} else if (getTipofile().equals("6")) {
			//il file di tipo 6 richiede come obbligatorio l'anno
			codiceErrore = Constants.ERRORE_INC_104;
			return;
		}
		try{
            this.setPageSize(
                    Optional.ofNullable(getRows())
                        .map(s -> Integer.valueOf(s))
                        .orElse(100)
            );
        } catch(Exception e) {
            codiceErrore = Constants.ERRORE_INC_104;
            return;
        }

        logger.debug("query: {} dominio: {} anno:{} rows: {}", query, dominio, anno, getRows());
		IncarichiRichiestaComponentSession componentSession = ((IncarichiRichiestaComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiRichiestaComponentSession",IncarichiRichiestaComponentSession.class));
		ContrattoComponentSession contrattoComponentSession = Utility.createContrattoComponentSession();
		try {
			if (getTipofile().equals("1"))
				this.setIterator(context, componentSession.findListaIncarichiRichiesta(context.getUserContext(false),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
			else if (getTipofile().equals("2"))
				this.setIterator(context, componentSession.findListaIncarichiCollaborazione(context.getUserContext(false),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
			else if (getTipofile().equals("3"))
				this.setIterator(context, componentSession.findListaIncarichiElenco(context.getUserContext(false),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic(),getTipoInc()));
			else if (getTipofile().equals("4") || getTipofile().equals("6"))
				this.setIterator(context, contrattoComponentSession.findListaContrattiElenco(context.getUserContext(false),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
			else if (getTipofile().equals("5"))
				this.setIterator(context, componentSession.findListaIncarichiElencoArt18(context.getUserContext(false),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
		} catch (ComponentException e) {
            logger.error(Constants.erroriINC.get(Constants.ERRORE_INC_100), e);
			codiceErrore = Constants.ERRORE_INC_100;
			return;
		} catch (RemoteException e) {
            logger.error(Constants.erroriINC.get(Constants.ERRORE_INC_100), e);
			codiceErrore = Constants.ERRORE_INC_100;
			return;
		}

		try{
			this.reset();
		} catch(Exception e) {
			codiceErrore = Constants.ERRORE_INC_100;
			return;
		}

		if(getPage()!=null){
			try{
				setCurrentPage(new Integer(getPage())-1);
			} catch(Exception e) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}
		} else {
            try {
                setCurrentPage(0);
            } catch (RemoteException e1) {
                codiceErrore = Constants.ERRORE_INC_104;
                return;
            }
        }
        this.refetchPage(context);
        List list = new BulkList(Arrays.asList(getPageContents()));

		try {
			if (getTipofile().equals("1"))
				setIncarichi(componentSession.completaListaIncarichiRichiesta(context.getUserContext(false), list));
			else if (getTipofile().equals("2"))
				setIncarichi(componentSession.completaListaIncarichiCollaborazione(context.getUserContext(false),list));
			else if (getTipofile().equals("3") || getTipofile().equals("5"))
				setIncarichi(componentSession.completaListaIncarichiElenco(context.getUserContext(false),list));
			else if (getTipofile().equals("4")||getTipofile().equals("6"))
				setIncarichi(completaListaContrattiElenco(context.getUserContext(false),list,getTipofile().equals("6")));
		} catch (ComponentException e) {
			codiceErrore = Constants.ERRORE_INC_100;
		} catch (RemoteException e) {
			codiceErrore = Constants.ERRORE_INC_100;
		}		
	}	
	
	@SuppressWarnings("rawtypes")
	private List<ContrattoBulk> completaListaContrattiElenco(UserContext userContext, List list, boolean caricaPagamenti) throws ApplicationException {
		List<ContrattoBulk> result = new ArrayList<ContrattoBulk>();
		ContrattoService contrattoService = SpringUtil.getBean("contrattoService", ContrattoService.class);		
		for (Object object : list) {
			ContrattoBulk contratto = (ContrattoBulk)object;
			
			for (AllegatoContrattoFlussoDocumentBulk allegato :  contrattoService.findAllegatiFlussoContratto(contratto)) {
				if (allegato != null && allegato.getTrasparenza() != null && !allegato.getTrasparenza()){
					if (allegato.getLabel() == null){
						allegato.setLabel((String)allegato.getTi_allegatoFlussoKeys().get(allegato.getType()));
					}
					contratto.addToArchivioAllegatiFlusso(allegato);
				}
			}
			try {
				ContrattoComponentSession contrattoComponentSession= Utility.createContrattoComponentSession();
				contratto = contrattoComponentSession.calcolaTotDocCont(userContext, contratto);
				if (caricaPagamenti)
					contratto = contrattoComponentSession.calcolaMapPagamentiForServizioRest(userContext, contratto);
			} catch (ComponentException|RemoteException e) {
				logger.error("ERROR -> ", e);
				codiceErrore = Constants.ERRORE_INC_100;
				return null;
			}
			result.add(contratto);
		}
		return result;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getNumMax() {
		return numMax;
	}

	public void setNumMax(Integer numMax) {
		this.numMax = numMax;
	}

	public Integer getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(Integer codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRicerca() {
		return ricerca;
	}

	public void setRicerca(String ricerca) {
		this.ricerca = ricerca;
	}

	public List getIncarichi() {
		return incarichi;
	}

	public void setIncarichi(List terzi) {
		this.incarichi = terzi;
	}

	public String getTipofile() {
		return tipofile;
	}

	public void setTipofile(String tipofile) {
		this.tipofile = tipofile;
	}
	public String getTagRadice() {
		if (getTipofile().equals("1"))
			return "incarichi";
		else if (getTipofile().equals("2"))
			return "collaborazioni";
		else if (getTipofile().equals("3") || getTipofile().equals("5"))
			return "elenco";
		else if (getTipofile().equals("4"))
			return "contratti";
		return "incarichi";
	}

	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		// TODO Auto-generated method stub
		return null;
	}

	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getStrRic() {
		return strRic;
	}

	public void setStrRic(String strRic) {
		this.strRic = strRic;
	}

	public String getCdCds() {
		return cdCds;
	}

	public void setCdCds(String cdCds) {
		this.cdCds = cdCds;
	}
	
	public String getTipoInc() {
		return tipoInc;
	}
	
	public void setTipoInc(String tipoInc) {
		this.tipoInc = tipoInc;
	}
}
