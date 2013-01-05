package it.cnr.contab.incarichi00.bp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

import it.cnr.contab.config00.bp.ResponseXMLBP;
import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_collaborazioneBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elencoBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiRichiestaComponentSession;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_norma_perlaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaBP;

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
		Element elementUrl = xmldoc.createElement(getTagRadice()+":url");
		dato = incarico.getDownloadUrl(); 
		Node nodeUrl = xmldoc.createTextNode(dato!=null?dato:"");
		elementUrl.appendChild(nodeUrl);
		elementRichiesta.appendChild(elementUrl);

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

	private Element generaDettaglioContratti(Document xmldoc, ContrattoBulk contratto) throws ParseException{
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dato;
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
		
		Element elementImporto = xmldoc.createElement(getTagRadice()+":importo");
		dato = new it.cnr.contab.util.EuroFormat().format(contratto.getIm_contratto_passivo()); 
		elementImporto.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
		elementContratto.appendChild(elementImporto);

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

		for (AllegatoContrattoDocumentBulk allegato : contratto.getArchivioAllegati()) {
			if (allegato.getType().equals(AllegatoContrattoDocumentBulk.CONTRATTO)){
				Element elementLink = xmldoc.createElement(getTagRadice()+":url_contratto");
				dato = "genericdownload/"+allegato.getName()+"?nodeRef="+allegato.getNodeId(); 
				elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				elementContratto.appendChild(elementLink);
			}else if (allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO)) {
				if (allegato.getLink()!= null){
					Element elementLink = xmldoc.createElement(getTagRadice()+":url_esterno_progetto");
					dato = allegato.getLink(); 					
					elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					elementContratto.appendChild(elementLink);
				}else if (allegato.isContentStreamPresent()) {
					Element elementLink = xmldoc.createElement(getTagRadice()+":url_progetto");
					dato = "genericdownload/"+allegato.getName()+"?nodeRef="+allegato.getNodeId(); 
					elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
					elementContratto.appendChild(elementLink);
				}
			}else if (allegato.getType().equals(AllegatoContrattoDocumentBulk.CAPITOLATO)) {
				Element elementLink = xmldoc.createElement(getTagRadice()+":url_capitolato");
				dato = "genericdownload/"+allegato.getName()+"?nodeRef="+allegato.getNodeId(); 
				elementLink.appendChild(xmldoc.createTextNode(dato!=null?dato:""));
				elementContratto.appendChild(elementLink);
			}
		}
		return elementContratto;
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
		dato = incarico.getNominativo();
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
		String dataf=null;
		if(incarico.getIncaricoVariazione()!=null && incarico.getIncaricoVariazione().getDt_fine_validita()!=null)
			dataf = formatter.format(incarico.getIncaricoVariazione().getDt_fine_validita()).toString();
		else if(incarico.getDt_fine_validita()!=null)
			dataf = formatter.format(incarico.getDt_fine_validita()).toString();
		dato = dataf; 
		Node nodeDataFine = xmldoc.createTextNode(dato!=null?dato:"");
		elementDataFine.appendChild(nodeDataFine);
		elementRichiesta.appendChild(elementDataFine);

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
/*
		// si dovranno togliere i due if innestati
		if(incarico.getDt_stipula()!=null) {
  	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");		
  	        java.util.Date dataMinima = sdf.parse("13022008");
  	        if (incarico.getDt_stipula().after(dataMinima)) {

  	        	// aggiungiamo le Informazioni sull'URL del bando
  	        	for (Iterator i = incarico.getListDownloadUrl().iterator();i.hasNext();){
  					Incarichi_archivioBulk rep = (Incarichi_archivioBulk)i.next();
  					if (rep!=null) {
  						dato = rep.getDownloadUrl();
  						Element elementUrl = xmldoc.createElement(getTagRadice()+":url");
  						Node nodeUrl = xmldoc.createTextNode(dato!=null?dato:"");
  						elementUrl.appendChild(nodeUrl);
  						elementRichiesta.appendChild(elementUrl);
  					}
  				}

  	        }
		}
*/
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
		    			else if (getTipofile().equals("4"))
		    				elem = generaDettaglioContratti(xmldoc,(ContrattoBulk)incarico);
		    			if (elem!=null)
		    				root.appendChild(elem);
		    		}
	    		}
	    	}
	    	DOMSource domSource = new DOMSource(xmldoc);
	    	StreamResult streamResult = new StreamResult(pagecontext.getOut());
	    	TransformerFactory tf = TransformerFactory.newInstance();
	    	Transformer serializer = tf.newTransformer();
	    	serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
	    	//serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://150.146.206.250/DTD/"+getTagRadice()+".dtd");
	    	serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,getTagRadice());
	    	serializer.setOutputProperty(OutputKeys.INDENT,"yes");
	    	serializer.setOutputProperty(OutputKeys.STANDALONE,"no");
	    	//serializer.setOutputProperty(OutputKeys.MEDIA_TYPE,"text/xml");
	    	serializer.transform(domSource, streamResult); 
		} catch (ParserConfigurationException e) {
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		} catch (ParseException e) {
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
			if (query!=null&&(!query.equalsIgnoreCase(Constants.RICHIESTE_IN_CORSO)&&!query.equalsIgnoreCase(Constants.RICHIESTE_SCADUTE))) {
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
		}
		if(getRows()!=null){
			try{
				this.setPageSize(new Integer(getRows()));
			} catch(Exception e) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}
		}

		IncarichiRichiestaComponentSession componentSession = ((IncarichiRichiestaComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiRichiestaComponentSession",IncarichiRichiestaComponentSession.class));
		ContrattoComponentSession contrattoComponentSession = ((ContrattoComponentSession)createComponentSession("CNRCONFIG00_EJB_ContrattoComponentSession",ContrattoComponentSession.class));
		try {
			if (getTipofile().equals("1"))
				this.setIterator(context, componentSession.findListaIncarichiRichiesta(context.getUserContext(),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
			else if (getTipofile().equals("2"))
				this.setIterator(context, componentSession.findListaIncarichiCollaborazione(context.getUserContext(),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));
			else if (getTipofile().equals("3"))
				this.setIterator(context, componentSession.findListaIncarichiElenco(context.getUserContext(),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));			
			else if (getTipofile().equals("4"))
				this.setIterator(context, contrattoComponentSession.findListaContrattiElenco(context.getUserContext(),query,dominio,esercizio,getCdCds(),getOrder(),getStrRic()));			

		} catch (ComponentException e) {
			e.printStackTrace();
			codiceErrore = Constants.ERRORE_INC_100;
			return;
		} catch (RemoteException e) {
			e.printStackTrace();
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
		} else
			try {
				setCurrentPage(0);
			} catch (RemoteException e1) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}

		List list = new BulkList();
		if (getRows()==null)
			try {
				this.getIterator().moveTo(0);
				while(this.getIterator().hasMoreElements())
					list.add(this.getIterator().nextElement());
			} catch (RemoteException e1) {
				codiceErrore = Constants.ERRORE_INC_100;
				return;
			}
		else {
			try {
				setPageSize(new Integer(getRows()));
			} catch (Exception e) {
				codiceErrore = Constants.ERRORE_INC_104;
				return;
			}
			this.refetchPage(context);
			for (int i = 0; i < getPageContents().length; i++) {
				list.add(this.getPageContents()[i]);
			}
			list=new BulkList(Arrays.asList(getPageContents()));
		}
			
		try {
			if (getTipofile().equals("1"))
				setIncarichi(componentSession.completaListaIncarichiRichiesta(context.getUserContext(), list));
			else if (getTipofile().equals("2"))
				setIncarichi(componentSession.completaListaIncarichiCollaborazione(context.getUserContext(),list));
			else if (getTipofile().equals("3"))
				setIncarichi(componentSession.completaListaIncarichiElenco(context.getUserContext(),list));
			else if (getTipofile().equals("4"))
				setIncarichi(completaListaContrattiElenco(context.getUserContext(),list));

		} catch (ComponentException e) {
			codiceErrore = Constants.ERRORE_INC_100;
		} catch (RemoteException e) {
			codiceErrore = Constants.ERRORE_INC_100;
		}
	}	
	
	@SuppressWarnings("rawtypes")
	private List<ContrattoBulk> completaListaContrattiElenco(UserContext userContext, List list) {
		List<ContrattoBulk> result = new ArrayList<ContrattoBulk>();
		ContrattoService contrattoService = SpringUtil.getBean("contrattoService",
				ContrattoService.class);		
		for (Object object : list) {
			ContrattoBulk contratto = (ContrattoBulk)object;
			for (AllegatoContrattoDocumentBulk allegato :  contrattoService.findAllegatiContratto(contratto)) {
				contratto.addToArchivioAllegati(allegato);
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
		else if (getTipofile().equals("3"))
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
}
