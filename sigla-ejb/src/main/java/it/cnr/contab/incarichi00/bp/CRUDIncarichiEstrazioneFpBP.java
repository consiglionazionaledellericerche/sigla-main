package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiEstrazioneFpComponentSession;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.Esito;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.incarichi00.xmlfp.ObjectFactory;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class CRUDIncarichiEstrazioneFpBP extends SimpleCRUDBP {
	public CRUDIncarichiEstrazioneFpBP() {
		super();
	}

	public CRUDIncarichiEstrazioneFpBP(String s) {
		super(s);
	}

	public CRUDIncarichiEstrazioneFpBP(String s, OggettoBulk bulk) {
		super(s);
	}

	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabIncarichiArchivio");
		super.init(config, actioncontext);
	}

	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data");
	}

	protected void resetTabs(ActionContext context) {
		setTab("tab","tabIncarichiArchivio");
	}

	@Override
	public void validate(ActionContext actioncontext) throws ValidationException {
		caricaValidaXML(actioncontext, (Incarichi_archivio_xml_fpBulk)getModel());
		super.validate(actioncontext);
	}
	
	private void caricaValidaXML(ActionContext actioncontext, Incarichi_archivio_xml_fpBulk allegato) throws ValidationException {
		try{
			long LUNGHEZZA_MAX=0x1000000;
	
			UploadedFile fileInviato = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.blob_inv");
			
			if (fileInviato == null || fileInviato.getName().equals(""))
				throw new ValidationException("Attenzione: caricare il File inviato alla Funzione Pubblica.");
	
			if (!(fileInviato == null || fileInviato.getName().equals(""))) { 
				if (fileInviato.length() > LUNGHEZZA_MAX)
					throw new ValidationException("Attenzione: la dimensione del file inviato alla Funzione Pubblica è superiore alla massima consentita (10 Mb).");
	
				allegato.setFile_inv(fileInviato.getFile());
				allegato.setNome_file_inv(Incarichi_archivioBulk.parseFilename(fileInviato.getName()));
				allegato.setToBeUpdated();
				setDirty(true);
			}
			
			UploadedFile fileRicevuto = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.blob_ric");
			
			if (fileRicevuto == null || fileRicevuto.getName().equals(""))
				throw new ValidationException("Attenzione: caricare il File ricevuto dalla Funzione Pubblica.");
	
			if (!(fileRicevuto == null || fileRicevuto.getName().equals(""))) { 
				if (fileRicevuto.length() > LUNGHEZZA_MAX)
					throw new ValidationException("Attenzione: la dimensione del file ricevuto dalla Funzione Pubblica è superiore alla massima consentita (10 Mb).");
	
				allegato.setFile_ric(fileRicevuto.getFile());
				allegato.setNome_file_ric(Incarichi_archivioBulk.parseFilename(fileRicevuto.getName()));
				allegato.setToBeUpdated();
				setDirty(true);
			}
	
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
			} catch (JAXBException e) {
				throw new ValidationException("Errore in fase di inizializzazione di un oggetto JAXB. "+e.getMessage());
			}
		
			Comunicazione comunicazione;
			try{
				comunicazione = (Comunicazione)jc.createUnmarshaller().unmarshal(fileInviato.getFile());
			} catch (ClassCastException e) {
				throw new ValidationException("File inviato alla funzione pubblica non di tipo corretto. "+e.getMessage());
			} catch (JAXBException e) {
				throw new ValidationException("Errore generico in fase di caricamento del file inviato alla funzione pubblica. "+e.getMessage());
			}
				
			EsitoComunicazione esitoComunicazione;
			try{
				esitoComunicazione = (EsitoComunicazione)jc.createUnmarshaller().unmarshal(fileRicevuto.getFile());
			} catch (ClassCastException e) {
				throw new ValidationException("File ricevuto dalla funzione pubblica non di tipo corretto. "+e.getMessage());
			} catch (JAXBException e) {
				throw new ValidationException("Errore generico in fase di caricamento del file ricevuto dalla funzione pubblica. "+e.getMessage());
			}
				
			if (comunicazione==null)
				throw new ValidationException("Errore nel caricamento del file inviato alla Funzione Pubblica.");
			else if (esitoComunicazione==null)
				throw new ValidationException("Errore nel caricamento del file ricevuto dalla Funzione Pubblica.");
			else if (comunicazione.getConsulenti().getNuovoIncarico().size()!=esitoComunicazione.getConsulenti().getNuovoIncarico().size())
				throw new ValidationException("Attenzione: il numero degli incarichi presenti nel file inviato ("+comunicazione.getConsulenti().getNuovoIncarico().size()+") è diverso dal numero degli incarichi presenti nel file ricevuto("+esitoComunicazione.getConsulenti().getNuovoIncarico().size()+").");
		} catch (ValidationException e){
			throw new ValidationException(e.getMessage());
		}
	}

	private EsitoComunicazione.Consulenti.NuovoIncarico getAnomalieNuovoIncaricoFP(ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncaricoAnomalia = objectFactory.createEsitoComunicazioneConsulentiNuovoIncarico();
		EsitoComunicazione.Consulenti.NuovoIncarico.IncaricatoPersona nuovoIncaricatoPersona = objectFactory.createEsitoComunicazioneConsulentiNuovoIncaricoIncaricatoPersona();

		nuovoIncaricoAnomalia.setIncaricatoPersona(nuovoIncaricatoPersona);
		nuovoIncaricoAnomalia.setEsito(Esito.OK);

		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		if (incarico.getDt_inizio_validita()==null){
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.setErrDataInizio("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Inizio.");
		}	
		if (incarico.getDt_fine_validita()==null){
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.setErrDataFine("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Fine.");
		}	
		if (incarico.getTerzo()==null ||incarico.getTerzo().getAnagrafico()==null){
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrCognome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Terzo.");
		}	
		if (incarico.getTerzo().getAnagrafico().getCodice_fiscale()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrCodiceFiscalePartitaIva("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Fiscale Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getComune_nascita()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrCodiceFiscalePartitaIva("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Comune Nascita Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getCognome()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrCognome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Cognome Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getNome()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrNome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Nome Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getDt_nascita()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrDataNascita("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Nascita Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getTi_sesso()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.getIncaricatoPersona().setErrSesso("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Tipo Sesso Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getUnita_organizzativa().getId_funzione_pubblica()==null) {
			nuovoIncaricoAnomalia.setEsito(Esito.ERRATO);
			nuovoIncaricoAnomalia.setErrDescrizioneIncarico("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Identificativo Funzione Pubblica sulla UO di appartenenza ("+incarico.getUnita_organizzativa().getCd_unita_organizzativa()+").");
		} 

		return nuovoIncaricoAnomalia;
	}

	private EsitoComunicazione.Consulenti.ModificaIncarico getAnomalieModificaIncaricoFP(ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		EsitoComunicazione.Consulenti.ModificaIncarico modificaIncaricoAnomalia = objectFactory.createEsitoComunicazioneConsulentiModificaIncarico();

		modificaIncaricoAnomalia.setEsito(Esito.OK);

		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		if (incarico.getUnita_organizzativa().getId_funzione_pubblica()==null) {
			modificaIncaricoAnomalia.setEsito(Esito.ERRATO);
			modificaIncaricoAnomalia.setErrDescrizioneIncarico("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Identificativo Funzione Pubblica sulla UO di appartenenza ("+incarico.getUnita_organizzativa().getCd_unita_organizzativa()+").");
		} 

		return modificaIncaricoAnomalia;
	}

	private Comunicazione.Consulenti.ModificaIncarico generaModificaIncaricoFP(ObjectFactory objectFactory, Incarichi_comunicati_fpBulk incaricoComunicatoFP, V_incarichi_elenco_fpBulk incaricoElenco) throws DatatypeConfigurationException{
		Comunicazione.Consulenti.NuovoIncarico nuovoIncarico=generaNuovoIncaricoFP(objectFactory,incaricoElenco);
		if (nuovoIncarico==null) return null;

		Comunicazione.Consulenti.ModificaIncarico modificaIncarico = objectFactory.createComunicazioneConsulentiModificaIncarico();
		modificaIncarico.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));
		boolean isModificato=false;

		//verifico variazione
		if (!Utility.equalsNull(incaricoComunicatoFP.getAttivita_economica(), nuovoIncarico.getAttivitaEconomica())) {
			modificaIncarico.setAttivitaEconomica(nuovoIncarico.getAttivitaEconomica());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getDescrizione_incarico(), nuovoIncarico.getDescrizioneIncarico())) {
			modificaIncarico.setDescrizioneIncarico(nuovoIncarico.getDescrizioneIncarico());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getModalita_acquisizione(), nuovoIncarico.getModalitaAcquisizione())) {
			modificaIncarico.setModalitaAcquisizione(nuovoIncarico.getModalitaAcquisizione());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getTipo_rapporto(), nuovoIncarico.getTipoRapporto())) {
			modificaIncarico.setTipoRapporto(nuovoIncarico.getTipoRapporto());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getVariazioni_incarico(), nuovoIncarico.getVariazioniIncarico())) {
			modificaIncarico.setVariazioniIncarico(nuovoIncarico.getVariazioniIncarico());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getDt_inizio(), new Timestamp(nuovoIncarico.getDataInizio().toGregorianCalendar().getTime().getTime()))) {
			modificaIncarico.setDataInizio(nuovoIncarico.getDataInizio());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getDt_fine(), new Timestamp(nuovoIncarico.getDataFine().toGregorianCalendar().getTime().getTime()))) {
			modificaIncarico.setDataFine(nuovoIncarico.getDataFine());
			isModificato=true;
		}
		if (!((incaricoComunicatoFP.getImporto_previsto()==null && nuovoIncarico.getImportoPrevisto()==null) ||
			  (incaricoComunicatoFP.getImporto_previsto()!=null && nuovoIncarico.getImportoPrevisto()!=null &&
			   incaricoComunicatoFP.getImporto_previsto().compareTo(nuovoIncarico.getImportoPrevisto())==0))) {
			modificaIncarico.setImportoPrevisto(nuovoIncarico.getImportoPrevisto());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getFl_saldo()==null?false:incaricoComunicatoFP.getFl_saldo(), nuovoIncarico.isSaldo())) {
			modificaIncarico.setSaldo(nuovoIncarico.isSaldo());
			isModificato=true;
		}
		if (isModificato) return modificaIncarico;
		return null;
	}

	private Comunicazione.Consulenti.NuovoIncarico generaNuovoIncaricoFP(ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

		Comunicazione.Consulenti.NuovoIncarico elementNuovoIncarico = objectFactory.createComunicazioneConsulentiNuovoIncarico();
		
		//ANNO DI RIFERIMENTO
		Calendar dt_inizio = Calendar.getInstance();
		dt_inizio.setTime(incarico.getDt_inizio_validita());
		elementNuovoIncarico.setAnnoRiferimento(new Integer(dt_inizio.get(Calendar.YEAR)).intValue());

		//SEMESTRE DI RIFERIMENTO
		int semestreRiferimento;
		switch (dt_inizio.get(Calendar.MONTH)) {
			case Calendar.JANUARY:
				semestreRiferimento=1;
				break;
			case Calendar.FEBRUARY:
				semestreRiferimento=1;
				break;
			case Calendar.MARCH:
				semestreRiferimento=1;
				break;
			case Calendar.APRIL:
				semestreRiferimento=1;
				break;
			case Calendar.MAY:
				semestreRiferimento=1;
				break;
			case Calendar.JUNE:
				semestreRiferimento=1;
				break;
		default:
			semestreRiferimento=2;
			break;
		}

		elementNuovoIncarico.setSemestreRiferimento(semestreRiferimento);
		
		elementNuovoIncarico.setCodiceEnte(incarico.getUnita_organizzativa().getId_funzione_pubblica());

		//MODALITA' DI ACQUISIZIONE
		String modalitaAcquisizione; //DI NATURA DISCREZIONALE
		if (incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("626")>=0 ||
			incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("230")>=0 ||
			incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("NOTARIL")>=0 ||
			incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("NOTAI")>=0 ||
			incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("AUDIT")>=0)
			modalitaAcquisizione = "M3"; //PREVISTO DA NORME DI LEGGE
		else
			modalitaAcquisizione = "M1"; //DI NATURA DISCREZIONALE

		elementNuovoIncarico.setModalitaAcquisizione(modalitaAcquisizione);
		
		//TIPO DI RAPPORTO
		String tipoRapporto;
		if (incarico.getIncarichi_procedura().getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue())
			tipoRapporto="009"; //COLLABORAZIONE COORDINATA E CONTINUATIVA
		else
			tipoRapporto="008"; //PRESTAZIONE OCCASIONALE

		elementNuovoIncarico.setTipoRapporto(tipoRapporto);
		
		//ATTIVITA ECONOMICA
		String attivitaEconomica;
		if (incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("1") || //Studio
			incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("2") || //Ricerca
			incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("5") || //Studio - in attuazione di progetti di ricerca ed innovazione tecnologica 
			incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("6")) //Ricerca - in attuazione di progetti di ricerca ed innovazione tecnologica 
			attivitaEconomica="963"; //ATTIVITA' DI STUDIO E RICERCA
		else
			attivitaEconomica="956"; //ATTIVITA' DI CONSULENZA TECNICA

		elementNuovoIncarico.setAttivitaEconomica(attivitaEconomica);

		//DESCRIZIONE INCARICO //contiene anche i riferimenti normativi????
		StringBuffer descrizione = new StringBuffer();
		descrizione.append("("+incarico.getEsercizio()+'/'+incarico.getPg_repertorio()+")");
		descrizione.append(" - "+incarico.getIncarichi_procedura().getOggetto());
		elementNuovoIncarico.setDescrizioneIncarico(descrizione.length()>200?descrizione.substring(0, 199):descrizione.toString());
		
		//DATA INIZIO
		GregorianCalendar gcdi = new GregorianCalendar();
		gcdi.setTime(v_incarico.getDt_inizio_validita());
		elementNuovoIncarico.setDataInizio(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdi.get(Calendar.YEAR), gcdi.get(Calendar.MONTH), gcdi.get(Calendar.DAY_OF_MONTH), 0, 0, 0)));
		
		//DATA FINE
		GregorianCalendar gcdf = new GregorianCalendar();
		gcdf.setTime(v_incarico.getDt_fine_validita_variazione()==null?v_incarico.getDt_fine_validita():v_incarico.getDt_fine_validita_variazione());
		elementNuovoIncarico.setDataFine(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdf.get(Calendar.YEAR), gcdf.get(Calendar.MONTH), gcdf.get(Calendar.DAY_OF_MONTH), 0, 0, 0)));
		
		//RIFERIMENTO REGOLAMENTO
		elementNuovoIncarico.setRiferimentoRegolamento(true);

		//IMPORTO PREVISTO
		elementNuovoIncarico.setImportoPrevisto(v_incarico.getImporto_lordo_con_variazioni().setScale(2));
		
		//SALDO
		elementNuovoIncarico.setSaldo(false);
		
		//CREAZIONE TAG INCARICOPERSONA
		Comunicazione.Consulenti.NuovoIncarico.IncaricatoPersona elementIncaricatoPersona = objectFactory.createComunicazioneConsulentiNuovoIncaricoIncaricatoPersona();
		elementNuovoIncarico.setIncaricatoPersona(elementIncaricatoPersona);
	
		elementIncaricatoPersona.setTipologiaConsulente("P1");
			
		try{
			elementIncaricatoPersona.setCodiceFiscalePartitaIva(incarico.getTerzo().getAnagrafico().getCodice_fiscale());
		} catch (NullPointerException e){
		}

		//Se trattasi di consulente estero che ha il codice fiscale valorizzato metto il campo estero a "false" così come indicato dalla Dott.ssa Paola Sarti
		//della Funzione Pubblica altrimenti metto quello corretto
		boolean estero=false;
		if (elementIncaricatoPersona.getCodiceFiscalePartitaIva()==null ||
			elementIncaricatoPersona.getCodiceFiscalePartitaIva().length()!=16){
			try{
				elementIncaricatoPersona.setCodiceFiscalePartitaIva(null);
				estero = !NazioneBulk.ITALIA.equals(incarico.getTerzo().getAnagrafico().getComune_nascita().getTi_italiano_estero());
				elementIncaricatoPersona.setEstero(estero);
			} catch (NullPointerException e){
			}
		}
		elementIncaricatoPersona.setEstero(estero);

		try{
			elementIncaricatoPersona.setCognome(incarico.getTerzo().getAnagrafico().getCognome());
		} catch (NullPointerException e){
		}
			
		try{
			elementIncaricatoPersona.setNome(incarico.getTerzo().getAnagrafico().getNome());
		} catch (NullPointerException e){
		}
			
		try{
			if (estero || incarico.getTerzo().getAnagrafico().getDt_nascita()!=null)
				elementIncaricatoPersona.setDataNascita(formatter.format(incarico.getTerzo().getAnagrafico().getDt_nascita()).toString());
		} catch (NullPointerException e){
		}
			
		try{
			elementIncaricatoPersona.setSesso(incarico.getTerzo().getAnagrafico().getTi_sesso());
		} catch (NullPointerException e){
		}

		return elementNuovoIncarico;
	}
	
	public void generaXML(ActionContext context) throws BusinessProcessException {
                try{
                        Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
                  
                        if (archivioXmlFP==null || archivioXmlFP.getEsercizio()==null || archivioXmlFP.getSemestre()==null ||
                              (!archivioXmlFP.isFl_crea_file_da_file() && archivioXmlFP.getDt_calcolo()==null)) {
                              setMessage("Valorizzare tutti i campi di selezione per effettuare l'estrazione.");
                              return;
                        } else if (archivioXmlFP.isFl_crea_file_da_file()){
                              archivioXmlFP.setFile_ric_err(((it.cnr.jada.action.HttpActionContext)context).getMultipartParameter("main.blob_ric_err").getFile());
                              if ((archivioXmlFP.getFile_ric_err() == null || archivioXmlFP.getFile_ric_err().getName().equals(""))) {
                                   setMessage("Indicare il file di risposta con errori della Funzione Pubblica dal quale estrarre solo gli incarichi corretti.");
                                   return;
                              }
                        }
  
			ConsultazioniBP newBp = null; 
			if (!archivioXmlFP.isFl_crea_file_da_file()) 
				newBp = getConsIncarichiEstrazioneFpBP(context);
		    else {
		    	CompoundFindClause clauses = getCompoundFindClause(context, archivioXmlFP.getFile_ric_err());
		    	if (clauses.getClauses().hasMoreElements())
			    	newBp = getConsIncarichiEstrazioneFpBP(context, getCompoundFindClause(context, archivioXmlFP.getFile_ric_err()));
		    	else {
					setMessage("File indicato senza incarichi corretti. Estrazione non effettuata.");
		    		return;
		    	}
		    }

			if (newBp!=null){
				if (newBp.getIterator()==null || newBp.getIterator().countElements()==0)
					newBp.openIterator(context);
				generaXML(context, newBp.getIterator());
			}
		} catch (Exception e){
			throw handleException(e);
		}
	}

	public ConsultazioniBP getConsIncarichiEstrazioneFpBP(ActionContext context) throws BusinessProcessException {
		Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();

		CompoundFindClause clauses = new CompoundFindClause();
		
		try{
			Timestamp dt_inizio = null, dt_fine = null;
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
	
			if (archivioXmlFP.getSemestre().equals(Incarichi_archivio_xml_fpBulk.PRIMO_SEMESTRE)){
				dt_inizio = new Timestamp(sdf.parse("01/01/"+archivioXmlFP.getEsercizio().intValue()).getTime());
				dt_fine = new Timestamp(sdf.parse("30/06/"+archivioXmlFP.getEsercizio().intValue()).getTime());
			} else {
				dt_inizio = new Timestamp(sdf.parse("01/07/"+archivioXmlFP.getEsercizio().intValue()).getTime());
				dt_fine = new Timestamp(sdf.parse("31/12/"+archivioXmlFP.getEsercizio().intValue()).getTime());
			}				
	
			if (archivioXmlFP.getDt_calcolo().equals(Incarichi_archivio_xml_fpBulk.DATA_STIPULA)) {
				clauses.addClause("AND","dt_stipula",SQLBuilder.GREATER_EQUALS,dt_inizio);
				clauses.addClause("AND","dt_stipula",SQLBuilder.LESS_EQUALS,dt_fine);
			} else {
				clauses.addClause("AND","dt_inizio_validita",SQLBuilder.GREATER_EQUALS,dt_inizio);
				clauses.addClause("AND","dt_inizio_validita",SQLBuilder.LESS_EQUALS,dt_fine);
			}				
		} catch(Throwable e) {
			throw handleException(e);
		}
		
		return getConsIncarichiEstrazioneFpBP(context, clauses);
	}

	private ConsultazioniBP getConsIncarichiEstrazioneFpBP(ActionContext context, CompoundFindClause clauses) throws BusinessProcessException {
		ConsultazioniBP newBp=null;
		try{
			Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
			
			if (archivioXmlFP.getEsercizio()!=null && archivioXmlFP.getSemestre()!=null && archivioXmlFP.getDt_calcolo()!=null) {
				newBp = (ConsultazioniBP)context.getUserInfo().createBusinessProcess(
						context,
						"ConsIncarichiEstrazioneFpBP",
						new Object[] {
							"V"
						}
				);
				newBp.setBaseclause(clauses);
			}
		} catch(Throwable e) {
			throw handleException(e);
		}
		return newBp;
	}

	private CompoundFindClause getCompoundFindClause(ActionContext context, File fileErr) throws BusinessProcessException {
		try{
			JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
			
			EsitoComunicazione esitoComunicazione = (EsitoComunicazione)jc.createUnmarshaller().unmarshal(fileErr);

			CompoundFindClause clauses = new CompoundFindClause();
			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);

			for (Iterator iterator = esitoComunicazione.getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncarico = (EsitoComunicazione.Consulenti.NuovoIncarico) iterator.next();
				boolean estrai = true;
				if (nuovoIncarico.getEsito().equals(Esito.ERRATO) || 
					(nuovoIncarico.getIncaricatoPersona()!=null && nuovoIncarico.getIncaricatoPersona().getEsito().equals(Esito.ERRATO))) {
					estrai = false;
				}
				if (estrai) {
					for (Iterator iteratorPag = nuovoIncarico.getNuovoPagamento().iterator(); iteratorPag.hasNext();) {
						EsitoComunicazione.Consulenti.NuovoIncarico.NuovoPagamento pagamento = (EsitoComunicazione.Consulenti.NuovoIncarico.NuovoPagamento) iteratorPag.next();
						if (pagamento.getEsito().equals(Esito.ERRATO)){
							estrai=false;
							break;
						}
					}
				}
				if (estrai) {
					int esercizio_repertorio = new Integer(nuovoIncarico.getDescrizioneIncarico().substring(1,5)); 
					Long pg_repertorio = new Long(nuovoIncarico.getDescrizioneIncarico().substring(6,nuovoIncarico.getDescrizioneIncarico().indexOf(")"))); 
					CompoundFindClause parzClause = new CompoundFindClause();
					parzClause.setLogicalOperator(FindClause.OR);
					parzClause.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,esercizio_repertorio);
					parzClause.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,pg_repertorio);
					clauses.addChild(parzClause);
				}
			}

			for (Iterator iterator = esitoComunicazione.getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico modificaIncarico = (EsitoComunicazione.Consulenti.ModificaIncarico) iterator.next();
				boolean estrai = true;
				if (modificaIncarico.getEsito().equals(Esito.ERRATO))
					estrai = false;
				if (estrai) {
					for (Iterator iteratorPag = modificaIncarico.getNuovoPagamento().iterator(); iteratorPag.hasNext();) {
						EsitoComunicazione.Consulenti.ModificaIncarico.NuovoPagamento pagamento = (EsitoComunicazione.Consulenti.ModificaIncarico.NuovoPagamento) iteratorPag.next();
						if (pagamento.getEsito().equals(Esito.ERRATO)){
							estrai=false;
							break;
						}
					}
				}
				if (estrai) {
					for (Iterator iteratorPag = modificaIncarico.getModificaPagamento().iterator(); iteratorPag.hasNext();) {
						EsitoComunicazione.Consulenti.ModificaIncarico.ModificaPagamento pagamento = (EsitoComunicazione.Consulenti.ModificaIncarico.ModificaPagamento) iteratorPag.next();
						if (pagamento.getEsito().equals(Esito.ERRATO)){
							estrai=false;
							break;
						}
					}
				}
				if (estrai) {
					for (Iterator iteratorPag = modificaIncarico.getCancellaPagamento().iterator(); iteratorPag.hasNext();) {
						EsitoComunicazione.Consulenti.ModificaIncarico.CancellaPagamento pagamento = (EsitoComunicazione.Consulenti.ModificaIncarico.CancellaPagamento) iteratorPag.next();
						if (pagamento.getEsito().equals(Esito.ERRATO)){
							estrai=false;
							break;
						}
					}
				}
				if (estrai) {
					Incarichi_comunicati_fpBulk incarico = new Incarichi_comunicati_fpBulk();
					incarico.setId_incarico(Long.toString(modificaIncarico.getId()));
					
					Incarichi_comunicati_fpBulk incaricoAgg = comp.getIncarichiComunicatiAggFP(context.getUserContext(), incarico); 
					CompoundFindClause parzClause = new CompoundFindClause();
					parzClause.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incaricoAgg.getEsercizio_repertorio());
					parzClause.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.LESS_EQUALS,incaricoAgg.getPg_repertorio());
					clauses.or(clauses, parzClause);
				}
			}

			for (Iterator iterator = esitoComunicazione.getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.CancellaIncarico cancellaIncarico = (EsitoComunicazione.Consulenti.CancellaIncarico) iterator.next();
				boolean estrai = true;
				if (cancellaIncarico.getEsito().equals(Esito.ERRATO))
					estrai = false;
				if (estrai) {
					Incarichi_comunicati_fpBulk incarico = new Incarichi_comunicati_fpBulk();
					incarico.setId_incarico(Long.toString(cancellaIncarico.getId()));
					
					Incarichi_comunicati_fpBulk incaricoAgg = comp.getIncarichiComunicatiAggFP(context.getUserContext(), incarico); 
					CompoundFindClause parzClause = new CompoundFindClause();
					parzClause.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,incaricoAgg.getEsercizio_repertorio());
					parzClause.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.LESS_EQUALS,incaricoAgg.getPg_repertorio());
					clauses.or(clauses, parzClause);
				}
			}

			return clauses;
		} catch (Exception e){
			throw handleException(e);
		}
	}

	public void generaXML(ActionContext context, RemoteIterator sourceIterator) throws BusinessProcessException {
		try{
			List<V_incarichi_elenco_fpBulk> arraylist = new ArrayList<V_incarichi_elenco_fpBulk>();
	        sourceIterator.moveTo(0);
			while(sourceIterator.hasMoreElements()) 
				arraylist.add((V_incarichi_elenco_fpBulk)sourceIterator.nextElement());
			
			generaXML(context, arraylist);
		} catch (Exception e){
			throw handleException(e);
		}
	}

	public void generaXML(ActionContext context, List<V_incarichi_elenco_fpBulk> list) throws BusinessProcessException {
		try{
			Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
			archivioXmlFP.setMapFileNuovoIncaricoXML(new HashMap<String, Comunicazione>());
			archivioXmlFP.setMapFileModificaIncaricoXML(new HashMap<String, Comunicazione>());
			archivioXmlFP.setMapFileCancellaIncaricoXML(new HashMap<String, Comunicazione>());
			archivioXmlFP.setMapFileAnomalieNuovoIncaricoXML(new HashMap<String, EsitoComunicazione>());
			archivioXmlFP.setMapFileAnomalieModificaIncaricoXML(new HashMap<String, EsitoComunicazione>());
			archivioXmlFP.setMapFileAnomalieCancellaIncaricoXML(new HashMap<String, EsitoComunicazione>());
			archivioXmlFP.setPathFileZip(null);			

			List<File> listFileNuovoIncaricoXML=new ArrayList<File>();
			List<File> listFileModificaIncaricoXML=new ArrayList<File>();
			List<File> listFileCancellaIncaricoXML=new ArrayList<File>();
			List<File> listFileAnomalieNuovoIncaricoXML=new ArrayList<File>();
			List<File> listFileAnomalieModificaIncaricoXML=new ArrayList<File>();

			JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
			ObjectFactory obj = new ObjectFactory();
			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);

			List<Comunicazione.Consulenti.NuovoIncarico> elencoNuoviIncarichi = new ArrayList<Comunicazione.Consulenti.NuovoIncarico>();
			List<Comunicazione.Consulenti.ModificaIncarico> elencoModificheIncarichi = new ArrayList<Comunicazione.Consulenti.ModificaIncarico>();
			List<Comunicazione.Consulenti.CancellaIncarico> elencoCancellaIncarichi = new ArrayList<Comunicazione.Consulenti.CancellaIncarico>();
			
			List<EsitoComunicazione.Consulenti.NuovoIncarico> elencoAnomalieNuoviIncarichi = new ArrayList<EsitoComunicazione.Consulenti.NuovoIncarico>(); 
			List<EsitoComunicazione.Consulenti.ModificaIncarico> elencoAnomalieModificheIncarichi = new ArrayList<EsitoComunicazione.Consulenti.ModificaIncarico>(); 

			for (Iterator<V_incarichi_elenco_fpBulk> iterator = list.iterator(); iterator.hasNext();) {
				V_incarichi_elenco_fpBulk incaricoElenco = (V_incarichi_elenco_fpBulk)iterator.next();
				incaricoElenco = comp.completaIncaricoElencoFP(context.getUserContext(), incaricoElenco);
					
				Incarichi_comunicati_fpBulk incaricoComunicatoFP = comp.getIncarichiComunicatiAggFP(context.getUserContext(), incaricoElenco);

				if (incaricoComunicatoFP==null || incaricoComunicatoFP.getId_incarico()==null) {
					EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncaricoAnomalia=getAnomalieNuovoIncaricoFP(obj,incaricoElenco);
					if (nuovoIncaricoAnomalia.getEsito().equals(Esito.OK)){
						Comunicazione.Consulenti.NuovoIncarico elementNuovoIncarico=generaNuovoIncaricoFP(obj,incaricoElenco);
						if (elementNuovoIncarico!=null) {
							if (elementNuovoIncarico.getImportoPrevisto().compareTo(BigDecimal.ZERO)!=0) {
								if (archivioXmlFP.getTipo_estrazione_pagamenti().equals(Incarichi_archivio_xml_fpBulk.PAGAMENTI_INCLUDI)){
									//CREAZIONE TAG PAGAMENTO
									List<Incarichi_comunicati_fp_detBulk> listPagamenti = comp.getPagatoPerSemestre(context.getUserContext(), incaricoElenco.getIncaricoRepertorio());
									for (Iterator iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
										Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDetBulk = (Incarichi_comunicati_fp_detBulk) iterator2.next();
										Comunicazione.Consulenti.NuovoIncarico.NuovoPagamento elementNuovoPagamento = obj.createComunicazioneConsulentiNuovoIncaricoNuovoPagamento();
										elementNuovoPagamento.setAnno(incarichiComunicatiFpDetBulk.getAnno_pag());
										elementNuovoPagamento.setSemestre(incarichiComunicatiFpDetBulk.getSemestre_pag());
										elementNuovoPagamento.setImporto(incarichiComunicatiFpDetBulk.getImporto_pag().setScale(2));
										elementNuovoIncarico.getNuovoPagamento().add(elementNuovoPagamento);
									}
								}
								elencoNuoviIncarichi.add(elementNuovoIncarico);
							}
						} else {
							nuovoIncaricoAnomalia = obj.createEsitoComunicazioneConsulentiNuovoIncarico();
							nuovoIncaricoAnomalia.setErrDescrizioneIncarico("AnomaliaIncarico: "+incaricoElenco.getEsercizio().toString()+"/"+incaricoElenco.getPg_repertorio().toString());
							elencoAnomalieNuoviIncarichi.add(nuovoIncaricoAnomalia);
						}
					} else
						elencoAnomalieNuoviIncarichi.add(nuovoIncaricoAnomalia);
				} else {
					//AGGIORNAMENTO COMUNICAZIONE
					EsitoComunicazione.Consulenti.ModificaIncarico modificaIncaricoAnomalia=getAnomalieModificaIncaricoFP(obj,incaricoElenco);
					if (modificaIncaricoAnomalia.getEsito().equals(Esito.OK)){
						Comunicazione.Consulenti.ModificaIncarico elementModificaIncarico=generaModificaIncaricoFP(obj,incaricoComunicatoFP,incaricoElenco);
						if (archivioXmlFP.getTipo_estrazione_pagamenti().equals(Incarichi_archivio_xml_fpBulk.PAGAMENTI_INCLUDI)){
							//AGGIUNGO MODIFICHE PAGAMENTI
							List<Incarichi_comunicati_fp_detBulk> listPagamenti = comp.getPagatoPerSemestre(context.getUserContext(), incaricoElenco.getIncaricoRepertorio());
							elementModificaIncarico=generaPagamentiPerModificaIncaricoFP(obj, incaricoComunicatoFP, elementModificaIncarico, listPagamenti);
						}
						if (elementModificaIncarico!=null)
							elencoModificheIncarichi.add(elementModificaIncarico);
					} else {
						modificaIncaricoAnomalia.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));
						elencoAnomalieModificheIncarichi.add(modificaIncaricoAnomalia);
					}
				}
			}
			
			List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> incarichiDaEliminare = comp.getIncarichiComunicatiEliminatiFP(context.getUserContext(), archivioXmlFP.getEsercizio(),archivioXmlFP.getSemestre());

			for (Iterator<Incarichi_comunicati_fpBulk> iterator = incarichiDaEliminare.iterator(); iterator.hasNext();) {
				Incarichi_comunicati_fpBulk incaricoComunicatoFP = iterator.next();
				if (incaricoComunicatoFP.getId_incarico()!=null) {
					Comunicazione.Consulenti.CancellaIncarico elementCancellaIncarico = obj.createComunicazioneConsulentiCancellaIncarico();
					elementCancellaIncarico.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));
					elencoCancellaIncarichi.add(elementCancellaIncarico);					
				}
			}
			
			//creo i file dei nuovi incarichi
			Comunicazione currentComunicazione = null;

			int nrFile=0, nrRighe=0;

			boolean creaNewFile=true;
			for (Iterator<Comunicazione.Consulenti.NuovoIncarico> iterator = elencoNuoviIncarichi.iterator(); iterator.hasNext();) {
				if (creaNewFile) {
					currentComunicazione = obj.createComunicazione();
					Comunicazione.Consulenti elementConsulenti = obj.createComunicazioneConsulenti();
					currentComunicazione.setConsulenti(elementConsulenti);
					nrRighe=0;
					creaNewFile=false;
				}

				while(iterator.hasNext() && nrRighe<archivioXmlFP.getNum_max_file_record()) {
					Comunicazione.Consulenti.NuovoIncarico nuovoIncarico = iterator.next();
					currentComunicazione.getConsulenti().getNuovoIncarico().add(nuovoIncarico);
					nrRighe++;
				}
			
				if (archivioXmlFP.isFl_crea_file_per_tipologia() || nrRighe==archivioXmlFP.getNum_max_file_record() ||
					(elencoModificheIncarichi.isEmpty() && elencoCancellaIncarichi.isEmpty())) {
					nrFile++;
					String fileName = "Estrazione"+(archivioXmlFP.isFl_crea_file_per_tipologia()?"Nuovi":"All")+"IncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+"_"+nrFile+".xml";
					File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);					
					jc.createMarshaller().marshal(currentComunicazione, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					archivioXmlFP.getMapFileNuovoIncaricoXML().put("tmp/"+fileName, currentComunicazione);
					listFileNuovoIncaricoXML.add(file);
					creaNewFile=true;
				}
			}

			if (archivioXmlFP.isFl_crea_file_per_tipologia()) 
				nrFile=0;

			//creo o aggiungo i file degli incarichi modificati
			for (Iterator<Comunicazione.Consulenti.ModificaIncarico> iterator = elencoModificheIncarichi.iterator(); iterator.hasNext();) {
				if (creaNewFile) {
					currentComunicazione = obj.createComunicazione();
					Comunicazione.Consulenti elementConsulenti = obj.createComunicazioneConsulenti();
					currentComunicazione.setConsulenti(elementConsulenti);
					nrRighe=0;
					creaNewFile=false;
				}

				while(iterator.hasNext() && nrRighe<archivioXmlFP.getNum_max_file_record()) {
					Comunicazione.Consulenti.ModificaIncarico modificaIncarico = iterator.next();
					currentComunicazione.getConsulenti().getModificaIncarico().add(modificaIncarico);
					nrRighe++;
				}

				if (archivioXmlFP.isFl_crea_file_per_tipologia() || nrRighe==archivioXmlFP.getNum_max_file_record() ||
					elencoCancellaIncarichi.isEmpty()) {
					nrFile++;
					String fileName = "Estrazione"+(archivioXmlFP.isFl_crea_file_per_tipologia()?"Modifiche":"All")+"IncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+"_"+nrFile+".xml";
					File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);					
					jc.createMarshaller().marshal(currentComunicazione, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					if (archivioXmlFP.isFl_crea_file_per_tipologia()){
						archivioXmlFP.getMapFileModificaIncaricoXML().put("tmp/"+fileName, currentComunicazione);
						listFileModificaIncaricoXML.add(file);
					} else {
						archivioXmlFP.getMapFileNuovoIncaricoXML().put("tmp/"+fileName, currentComunicazione);
						listFileNuovoIncaricoXML.add(file);
					}
					creaNewFile=true;
				}
			}
			
			if (archivioXmlFP.isFl_crea_file_per_tipologia()) 
				nrFile=0;

			//creo o aggiungo i file degli incarichi eliminati
			for (Iterator<Comunicazione.Consulenti.CancellaIncarico> iterator = elencoCancellaIncarichi.iterator(); iterator.hasNext();) {
				if (creaNewFile) {
					currentComunicazione = obj.createComunicazione();
					Comunicazione.Consulenti elementConsulenti = obj.createComunicazioneConsulenti();
					currentComunicazione.setConsulenti(elementConsulenti);
					nrRighe=0;
					creaNewFile=false;
				}

				while(iterator.hasNext() && nrRighe<archivioXmlFP.getNum_max_file_record()) {
					Comunicazione.Consulenti.CancellaIncarico cancellaIncarico = iterator.next();
					currentComunicazione.getConsulenti().getCancellaIncarico().add(cancellaIncarico);
					nrRighe++;
				}

				nrFile++;
				String fileName = "Estrazione"+(archivioXmlFP.isFl_crea_file_per_tipologia()?"Cancella":"All")+"IncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+"_"+nrFile+".xml";
				File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);					
				jc.createMarshaller().marshal(currentComunicazione, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				if (archivioXmlFP.isFl_crea_file_per_tipologia()) {
					archivioXmlFP.getMapFileCancellaIncaricoXML().put("tmp/"+fileName, currentComunicazione);
					listFileCancellaIncaricoXML.add(file);
				} else{
					archivioXmlFP.getMapFileNuovoIncaricoXML().put("tmp/"+fileName, currentComunicazione);
					listFileNuovoIncaricoXML.add(file);
				}
				creaNewFile=true;
			}

			if (!elencoAnomalieNuoviIncarichi.isEmpty()) {
				//creo i file delle anomalie nuovi incarichi
				EsitoComunicazione comunicazioneAnomaliaNuoviIncarichi = obj.createEsitoComunicazione();
				EsitoComunicazione.Consulenti elementConsulentiAnomaliaNuoviIncarichi = obj.createEsitoComunicazioneConsulenti();
				comunicazioneAnomaliaNuoviIncarichi.setConsulenti(elementConsulentiAnomaliaNuoviIncarichi);
				comunicazioneAnomaliaNuoviIncarichi.getConsulenti().getNuovoIncarico().addAll(elencoAnomalieNuoviIncarichi);
				String fileName = "AnomalieEstrazione"+(archivioXmlFP.isFl_crea_file_per_tipologia()?"Nuovi":"All")+"IncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+".xml";
				File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);					
				jc.createMarshaller().marshal(comunicazioneAnomaliaNuoviIncarichi, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				archivioXmlFP.getMapFileAnomalieNuovoIncaricoXML().put("tmp/"+fileName, comunicazioneAnomaliaNuoviIncarichi);
				listFileAnomalieNuovoIncaricoXML.add(file);
			}

			if (!elencoAnomalieModificheIncarichi.isEmpty()) {
				//creo i file delle anomalie modifiche incarichi
				EsitoComunicazione comunicazioneAnomaliaModificheIncarichi = obj.createEsitoComunicazione();
				EsitoComunicazione.Consulenti elementConsulentiAnomaliaModificheIncarichi = obj.createEsitoComunicazioneConsulenti();
				comunicazioneAnomaliaModificheIncarichi.setConsulenti(elementConsulentiAnomaliaModificheIncarichi);
				comunicazioneAnomaliaModificheIncarichi.getConsulenti().getModificaIncarico().addAll(elencoAnomalieModificheIncarichi);
				String fileName = "AnomalieEstrazione"+(archivioXmlFP.isFl_crea_file_per_tipologia()?"Modifiche":"All")+"IncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+".xml";
				File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);					
				jc.createMarshaller().marshal(comunicazioneAnomaliaModificheIncarichi, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				archivioXmlFP.getMapFileAnomalieModificaIncaricoXML().put("tmp/"+fileName, comunicazioneAnomaliaModificheIncarichi);
				listFileAnomalieModificaIncaricoXML.add(file);
			}

			Hashtable<String, List<File>> hashFileIncaricoXML=new Hashtable<String, List<File>>();
			
			hashFileIncaricoXML.put("Incarichi Nuovi", listFileNuovoIncaricoXML);
			hashFileIncaricoXML.put("Incarichi Modificati", listFileModificaIncaricoXML);
			hashFileIncaricoXML.put("Incarichi Cancellati", listFileCancellaIncaricoXML);
			hashFileIncaricoXML.put("Anomalie Incarichi Nuovi", listFileAnomalieNuovoIncaricoXML);
			hashFileIncaricoXML.put("Anomalie Incarichi Modificati", listFileAnomalieModificaIncaricoXML);

			String fileName = "EstrazioneIncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+".zip";
			creaFileZip(hashFileIncaricoXML, fileName);
			archivioXmlFP.setPathFileZip("tmp/"+fileName);
			cancellaListaFile(listFileNuovoIncaricoXML, listFileModificaIncaricoXML, listFileCancellaIncaricoXML, listFileAnomalieNuovoIncaricoXML, listFileAnomalieModificaIncaricoXML);
		} catch (Exception e){
			throw handleException(e);
		}
    }
	
	public File creaFileZip(Hashtable<String, List<File>> hashFileXML, String fileName) throws BusinessProcessException {
		try {
			byte[] buffer = new byte[18024];
			File fileZip = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName==null?"download.zip":fileName);
			ZipOutputStream zipFileOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileZip)));

			// Set the compression ratio
			zipFileOut.setLevel(Deflater.DEFAULT_COMPRESSION);
			for (Iterator<String> iterator = hashFileXML.keySet().iterator(); iterator.hasNext();) {
				String directory = iterator.next();

				for (File myFile : hashFileXML.get(directory)) {
					InputStream file = new FileInputStream(myFile);
					// Add ZIP entry to output stream.
					zipFileOut.putNextEntry(new ZipEntry(directory+ "/" + myFile.getName()));
					// Transfer bytes from the current file to the ZIP file
					int len;
					while ((len = file.read(buffer)) > 0) {
						zipFileOut.write(buffer, 0, len);
					}
					// Close the current entry
					zipFileOut.closeEntry();
				}
			}
			zipFileOut.finish();
			zipFileOut.close();
			return fileZip;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void cancellaListaFile(List<File>... listFile){
		for (List<File> list : listFile) {
			for (File file : list) {
				file.delete();
			}
		}
	}
	
	public void clearSelection(ActionContext context) throws BusinessProcessException {
		Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
		if (archivioXmlFP!=null){
			archivioXmlFP.setEsercizio(null);
			archivioXmlFP.setSemestre(null);
			archivioXmlFP.setDt_calcolo(null);
			archivioXmlFP.setFl_crea_file_per_tipologia(false);
			archivioXmlFP.setNum_max_file_record(Integer.decode("1000"));
			archivioXmlFP.setMapFileNuovoIncaricoXML(null);
			archivioXmlFP.setMapFileModificaIncaricoXML(null);
			archivioXmlFP.setMapFileCancellaIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieNuovoIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieModificaIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieCancellaIncaricoXML(null);
			archivioXmlFP.setPathFileZip(null);			
		}
	}

	private Comunicazione.Consulenti.ModificaIncarico generaPagamentiPerModificaIncaricoFP(ObjectFactory objectFactory, Incarichi_comunicati_fpBulk incaricoComunicatoFP, Comunicazione.Consulenti.ModificaIncarico modificaIncarico, List<Incarichi_comunicati_fp_detBulk> listPagamenti) throws DatatypeConfigurationException{
		Comunicazione.Consulenti.ModificaIncarico myModificaIncarico = objectFactory.createComunicazioneConsulentiModificaIncarico();
		myModificaIncarico.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));

		for (Iterator iterator = incaricoComunicatoFP.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
			Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDet = (Incarichi_comunicati_fp_detBulk) iterator.next();
			boolean trovato=false;
			for (Iterator iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
				Incarichi_comunicati_fp_detBulk pagamento = (Incarichi_comunicati_fp_detBulk) iterator2.next();
				if (incarichiComunicatiFpDet.getAnno_pag().equals(pagamento.getAnno_pag()) &&
					incarichiComunicatiFpDet.getSemestre_pag().equals(pagamento.getSemestre_pag())) {
					if (!incarichiComunicatiFpDet.getImporto_pag().equals(pagamento.getImporto_pag())) {
						Comunicazione.Consulenti.ModificaIncarico.ModificaPagamento elementModificaPagamento = new Comunicazione.Consulenti.ModificaIncarico.ModificaPagamento();
						elementModificaPagamento.setAnno(pagamento.getAnno_pag());
						elementModificaPagamento.setSemestre(pagamento.getSemestre_pag());
						elementModificaPagamento.setImporto(pagamento.getImporto_pag().setScale(2));
						myModificaIncarico.getModificaPagamento().add(elementModificaPagamento);
					}
					trovato=true;
					break;
				}
			}
			if (!trovato){
				Comunicazione.Consulenti.ModificaIncarico.CancellaPagamento elementCancellaPagamento = new Comunicazione.Consulenti.ModificaIncarico.CancellaPagamento();
				elementCancellaPagamento.setAnno(incarichiComunicatiFpDet.getAnno_pag());
				elementCancellaPagamento.setSemestre(incarichiComunicatiFpDet.getSemestre_pag());
				myModificaIncarico.getCancellaPagamento().add(elementCancellaPagamento);
			}
		}

		for (Iterator iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
			Incarichi_comunicati_fp_detBulk pagamento = (Incarichi_comunicati_fp_detBulk) iterator2.next();

			boolean trovato=false;
			for (Iterator iterator = incaricoComunicatoFP.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
				Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDet = (Incarichi_comunicati_fp_detBulk) iterator.next();
				if (incarichiComunicatiFpDet.getAnno_pag().equals(pagamento.getAnno_pag()) &&
					incarichiComunicatiFpDet.getSemestre_pag().equals(pagamento.getSemestre_pag())) {
					trovato=true;
					break;
				}
			}
			if (!trovato){
				Comunicazione.Consulenti.ModificaIncarico.NuovoPagamento elementNuovoPagamento = new Comunicazione.Consulenti.ModificaIncarico.NuovoPagamento();
				elementNuovoPagamento.setAnno(pagamento.getAnno_pag());
				elementNuovoPagamento.setSemestre(pagamento.getSemestre_pag());
				elementNuovoPagamento.setImporto(pagamento.getImporto_pag().setScale(2));
				myModificaIncarico.getNuovoPagamento().add(elementNuovoPagamento);
			}
		}
		if (myModificaIncarico.getNuovoPagamento().isEmpty() &&
			myModificaIncarico.getModificaPagamento().isEmpty() &&
			myModificaIncarico.getCancellaPagamento().isEmpty())
			return modificaIncarico;
		else if (modificaIncarico==null)
			return myModificaIncarico;
		else {
			modificaIncarico.getNuovoPagamento().addAll(myModificaIncarico.getNuovoPagamento());
			modificaIncarico.getModificaPagamento().addAll(myModificaIncarico.getModificaPagamento());
			modificaIncarico.getCancellaPagamento().addAll(myModificaIncarico.getCancellaPagamento());
			return modificaIncarico;
		}
	}
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk =  super.initializeModelForInsert(actioncontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk)
			clearSelection(actioncontext);
		return oggettobulk;
	}
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk =  super.initializeModelForEdit(actioncontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk)
			clearSelection(actioncontext);
		return oggettobulk;
	}
}