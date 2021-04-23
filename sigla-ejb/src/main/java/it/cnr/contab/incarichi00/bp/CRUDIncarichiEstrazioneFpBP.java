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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiEstrazioneFpComponentSession;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_prestazioneBulk;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.Esito;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.incarichi00.xmlfp.ObjectFactory;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.perlapa.incarico.*;
import it.cnr.perlapa.incarico.consulente.Allegati;
import it.cnr.perlapa.incarico.consulente.DatiIncarico;
import it.cnr.perlapa.incarico.consulente.PercettorePf;
import it.cnr.perlapa.incarico.consulente.PercettorePg;
import it.cnr.si.spring.storage.StoreService;
import it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType.Incarico.RiferimentoNormativo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

	protected StoreService storeService;

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
		Incarichi_archivio_xml_fpBulk archivio = (Incarichi_archivio_xml_fpBulk)getModel();
		if (archivio.getFl_merge_perla().equals(Boolean.FALSE))
			caricaValidaXML(actioncontext, archivio);
		else
			caricaValidaCSV(actioncontext, archivio);
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
			if (!allegato.getFl_perla()) {
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
			} else {
				try {
					jc = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory.class);
				} catch (JAXBException e) {
					throw new ValidationException("Errore in fase di inizializzazione di un oggetto JAXB. "+e.getMessage());
				}
			
				JAXBElement<?> comunicazione;
				try{
					comunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(fileInviato.getFile()); 
				} catch (ClassCastException e) {
					throw new ValidationException("File inviato alla funzione pubblica non di tipo corretto. "+e.getMessage());
				} catch (JAXBException e) {
					throw new ValidationException("Errore generico in fase di caricamento del file inviato alla funzione pubblica. "+e.getMessage());
				}
					
				JAXBElement<?> esitoComunicazione;
				try{
					esitoComunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(fileRicevuto.getFile()); 
				} catch (ClassCastException e) {
					throw new ValidationException("File ricevuto dalla funzione pubblica non di tipo corretto. "+e.getMessage());
				} catch (JAXBException e) {
					throw new ValidationException("Errore generico in fase di caricamento del file ricevuto dalla funzione pubblica. "+e.getMessage());
				}
					
				if (comunicazione==null)
					throw new ValidationException("Errore nel caricamento del file inviato alla Funzione Pubblica.");
				else if (esitoComunicazione==null)
					throw new ValidationException("Errore nel caricamento del file ricevuto dalla Funzione Pubblica.");
				else {
					if (comunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType.class) &&
						esitoComunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType.class)) {
						if (((it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType)comunicazione.getValue()).getInserimentoIncarichi().getNuoviIncarichi().getConsulente().size()!=((it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().size())
							throw new ValidationException("Attenzione: il numero degli incarichi presenti nel file inviato ("+((it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType)comunicazione.getValue()).getInserimentoIncarichi().getNuoviIncarichi().getConsulente().size()+") è diverso dal numero degli incarichi presenti nel file ricevuto("+((it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().size()+").");
						else if (((it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoInserimentoIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.KO))
							throw new ValidationException("Attenzione: il file ricevuto dalla funzione pubblica non è andato a buon fine.");
					} else if (comunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType.class) &&
							   esitoComunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType.class)) {
						if (((it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType)comunicazione.getValue()).getVariazioneIncarichi().getModificaIncarichi().getConsulente().size()!=((it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().size())
							throw new ValidationException("Attenzione: il numero degli incarichi presenti nel file inviato ("+((it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType)comunicazione.getValue()).getVariazioneIncarichi().getModificaIncarichi().getConsulente().size()+") è diverso dal numero degli incarichi presenti nel file ricevuto("+((it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().size()+").");
						else if (((it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType)esitoComunicazione.getValue()).getEsitoVariazioneIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.KO))
							throw new ValidationException("Attenzione: il file ricevuto dalla funzione pubblica non è andato a buon fine.");
					} else 
						throw new ValidationException("Tipologia di file ricevuto dalla Funzione Pubblica non gestita.");
				}
			}
		} catch (ValidationException e){
			throw new ValidationException(e.getMessage());
		}
	}

	private void caricaValidaCSV(ActionContext actioncontext, Incarichi_archivio_xml_fpBulk allegato) throws ValidationException {
		try{
			long LUNGHEZZA_MAX=0x1000000;
	
			UploadedFile fileRicevuto = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.blob_ric");
			
			if (fileRicevuto == null || fileRicevuto.getName().equals(""))
				throw new ValidationException("Attenzione: caricare il File di tipo CSV ottenuto dalla funzione REPORT/Incarichi presente nel sistema PERLA-PA.");
	
			if (!(fileRicevuto == null || fileRicevuto.getName().equals(""))) { 
				if (fileRicevuto.length() > LUNGHEZZA_MAX)
					throw new ValidationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");
	
				allegato.setFile_ric(fileRicevuto.getFile());
				allegato.setNome_file_ric(Incarichi_archivioBulk.parseFilename(fileRicevuto.getName()));
				allegato.setToBeUpdated();
				setDirty(true);
			}
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

	private it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType getAnomalieNuovoConsulentePerla(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		//ERRORI INCARICATO
		it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ErroriConsulenteType.Incaricato elementErroriConsulenteTypeIncaricato = objectFactory.createErroriConsulenteTypeIncaricato();
		elementErroriConsulenteTypeIncaricato.setPersonaFisica(objectFactory.createErroriConsulenteTypeIncaricatoPersonaFisica());

		boolean erroreIncaricato = false;
		if (incarico.getTerzo()==null ||incarico.getTerzo().getAnagrafico()==null){
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setCognome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Terzo.");
		}	
		if (incarico.getTerzo().getAnagrafico().getCodice_fiscale()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setCodiceFiscale("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Fiscale Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getComune_nascita()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setCodiceFiscale("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Comune Nascita Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getCognome()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setCognome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Cognome Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getNome()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setNome("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Nome Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getDt_nascita()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setDataNascita("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Nascita Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 
		if (incarico.getTerzo().getAnagrafico().getTi_sesso()==null) {
			erroreIncaricato = true;
			elementErroriConsulenteTypeIncaricato.getPersonaFisica().setSesso("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Tipo Sesso Terzo (Cod.Terzo: "+incarico.getTerzo().getCd_terzo()+").");
		} 

		//ERRORI INCARICO
		it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ErroriConsulenteType.Incarico elementErroriConsulenteTypeIncarico = objectFactory.createErroriConsulenteTypeIncarico();

		boolean erroreIncarico = false;
		if (incarico.getDt_inizio_validita()==null){
			erroreIncarico = true;
			elementErroriConsulenteTypeIncarico.setDataInizio("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Inizio.");
		}	
		if (incarico.getDt_fine_validita()==null){
			erroreIncarico = true;
			elementErroriConsulenteTypeIncarico.setDataFine("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Data Fine.");
		}	
		if (incarico.getUnita_organizzativa().getId_funzione_pubblica()==null) {
			erroreIncarico = true;
			elementErroriConsulenteTypeIncarico.setDescrizioneIncarico("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Identificativo Funzione Pubblica sulla UO di appartenenza ("+incarico.getUnita_organizzativa().getCd_unita_organizzativa()+").");
		} 

		it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType esitoConsulentePerla = objectFactory.createEsitoConsulenteType();
		
		if (erroreIncaricato || erroreIncarico) {
			esitoConsulentePerla.setEsito(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.KO);
			esitoConsulentePerla.setErrori(objectFactory.createErroriConsulenteType());
			if (erroreIncaricato)
				esitoConsulentePerla.getErrori().setIncaricato(elementErroriConsulenteTypeIncaricato);
			if (erroreIncarico)
				esitoConsulentePerla.getErrori().setIncarico(elementErroriConsulenteTypeIncarico);
		} else {
			esitoConsulentePerla.setEsito(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK);
		}
			
		return esitoConsulentePerla;
	}

	private it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType getAnomalieModificaConsulentePerla(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		//ERRORI INCARICO
		it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ErroriConsulenteType.Incarico elementErroriConsulenteTypeIncarico = objectFactory.createErroriConsulenteTypeIncarico();

		boolean erroreIncarico = false;
		if (incarico.getUnita_organizzativa().getId_funzione_pubblica()==null) {
			erroreIncarico = true;
			elementErroriConsulenteTypeIncarico.setDescrizioneIncarico("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca Codice Identificativo Funzione Pubblica sulla UO di appartenenza ("+incarico.getUnita_organizzativa().getCd_unita_organizzativa()+").");
		} 

		it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType esitoConsulentePerla = objectFactory.createEsitoConsulenteType();
	
		if (erroreIncarico) {
			esitoConsulentePerla.setEsito(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.KO);
			esitoConsulentePerla.setErrori(objectFactory.createErroriConsulenteType());
			esitoConsulentePerla.getErrori().setIncarico(elementErroriConsulenteTypeIncarico);
		} else {
			esitoConsulentePerla.setEsito(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.OK);
		}
			
		return esitoConsulentePerla;
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
		/*eliminato per problemi legati ai caratteri accentati
		if (!Utility.equalsNull(incaricoComunicatoFP.getDescrizione_incarico(), nuovoIncarico.getDescrizioneIncarico())) {
			modificaIncarico.setDescrizioneIncarico(nuovoIncarico.getDescrizioneIncarico());
			isModificato=true;
		}
		*/
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
	
	private it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType generaNuovoConsulentePerla(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory objectFactory, V_incarichi_elenco_fpBulk v_incarico) throws DatatypeConfigurationException{
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();

		it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType elementConsulente = objectFactory.createConsulenteType();
		
		elementConsulente.setIdMittente(incarico.getEsercizio().toString()+'/'+incarico.getPg_repertorio().toString());

		/*
		 * CREAZIONE TAG INCARICO
		 */
		elementConsulente.setIncarico(objectFactory.createConsulenteTypeIncarico());
		
		//SEMESTRE DI RIFERIMENTO
		Calendar dt_inizio = Calendar.getInstance();
		dt_inizio.setTime(incarico.getDt_inizio_validita());
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

		elementConsulente.getIncarico().setSemestreRiferimento(BigInteger.valueOf(semestreRiferimento));
		
		//MODALITA' DI ACQUISIZIONE
		String modalitaAcquisizione; //DI NATURA DISCREZIONALE
		if (incarico.getIncarichi_procedura().getTipo_prestazione()!=null && incarico.getIncarichi_procedura().getTipo_prestazione().getTipo_classificazione()!=null) {
			if (incarico.getIncarichi_procedura().getTipo_prestazione().getTipo_classificazione().equals(Tipo_prestazioneBulk.PREVISTA_DA_NORME_DI_LEGGE))
				modalitaAcquisizione = "1"; //PREVISTO DA NORME DI LEGGE
			else
				modalitaAcquisizione = "10"; //DI NATURA DISCREZIONALE
		} else {
			if (incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("626")>=0 ||
				incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("230")>=0 ||
				incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("NOTARIL")>=0 ||
				incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("NOTAI")>=0 ||
				incarico.getIncarichi_procedura().getOggetto().toUpperCase().indexOf("AUDIT")>=0)
				modalitaAcquisizione = "1"; //PREVISTO DA NORME DI LEGGE
			else
				modalitaAcquisizione = "10"; //DI NATURA DISCREZIONALE
		}
		
		elementConsulente.getIncarico().setModalitaAcquisizione(modalitaAcquisizione);
		
		//TIPO DI RAPPORTO
		String tipoRapporto;
		if (incarico.getIncarichi_procedura().getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue())
			tipoRapporto="2"; //COLLABORAZIONE COORDINATA E CONTINUATIVA
		else
			tipoRapporto="1"; //PRESTAZIONE OCCASIONALE

		elementConsulente.getIncarico().setTipoRapporto(tipoRapporto);
		
		//ATTIVITA ECONOMICA
		String attivitaEconomica;

		Incarichi_archivio_xml_fpBulk archivioXmlPerlaFP = (Incarichi_archivio_xml_fpBulk)getModel();
		if (archivioXmlPerlaFP.getEsercizio().compareTo(new Integer(2010)) == 1){
			if (incarico.getIncarichi_procedura().getTipo_attivita_fp()!=null && incarico.getIncarichi_procedura().getTipo_attivita_fp().getCd_tipo_attivita()!=null)
				attivitaEconomica=incarico.getIncarichi_procedura().getTipo_attivita_fp().getCd_tipo_attivita();
			else
				attivitaEconomica="74"; //ALTRE ATTIVITA' PROFESSIONALI, SCIENTIFICHE E TECNICHE
		} else {
			if (incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("1") || //Studio
				incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("2") || //Ricerca
				incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("5") || //Studio - in attuazione di progetti di ricerca ed innovazione tecnologica 
				incarico.getIncarichi_procedura().getTipo_attivita().getCd_tipo_attivita().equals("6")) //Ricerca - in attuazione di progetti di ricerca ed innovazione tecnologica 
				attivitaEconomica="963"; //ATTIVITA' DI STUDIO E RICERCA
			else
				attivitaEconomica="956"; //ATTIVITA' DI CONSULENZA TECNICA
		}

		elementConsulente.getIncarico().setAttivitaEconomica(attivitaEconomica);

		//DESCRIZIONE INCARICO //contiene anche i riferimenti normativi????
		StringBuffer descrizione = new StringBuffer();
		descrizione.append("("+incarico.getEsercizio()+'/'+incarico.getPg_repertorio()+")");
		descrizione.append(" - "+incarico.getIncarichi_procedura().getOggetto());
		elementConsulente.getIncarico().setDescrizioneIncarico(descrizione.length()>200?descrizione.substring(0, 199):descrizione.toString());
		
		//RIFERIMENTO REGOLAMENTO
		elementConsulente.getIncarico().setRiferimentoRegolamento(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.YesNoType.N);

		//RIFERIMENTO NORMATIVO
		if (incarico.getIncarichi_procedura().getFl_applicazione_norma().equals(Boolean.TRUE) && !incarico.getIncarichi_procedura().getCd_tipo_norma_perla().equals("999")) {
			RiferimentoNormativo riferimentoNormativo = objectFactory.createConsulenteTypeIncaricoRiferimentoNormativo();
			riferimentoNormativo.setArticolo(incarico.getIncarichi_procedura().getTipo_norma_perla().getArticolo_tipo_norma());
			riferimentoNormativo.setComma(incarico.getIncarichi_procedura().getTipo_norma_perla().getComma_tipo_norma());
			riferimentoNormativo.setNumero(incarico.getIncarichi_procedura().getTipo_norma_perla().getNumero_tipo_norma());
			//DATA
			GregorianCalendar dtRif = new GregorianCalendar();
			dtRif.setTime(incarico.getIncarichi_procedura().getTipo_norma_perla().getDt_tipo_norma());
			riferimentoNormativo.setData(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(dtRif.get(Calendar.YEAR), dtRif.get(Calendar.MONTH), dtRif.get(Calendar.DAY_OF_MONTH))));
			riferimentoNormativo.setRiferimento(new BigInteger(incarico.getIncarichi_procedura().getTipo_norma_perla().getCd_tipo_norma_perla()));
			elementConsulente.getIncarico().setRiferimentoNormativo(riferimentoNormativo);
		}

		//DATA AFFIDAMENTO
		GregorianCalendar gcds = new GregorianCalendar(), dtLimite = new GregorianCalendar();
		
		if (archivioXmlPerlaFP.getSemestre().equals(Incarichi_archivio_xml_fpBulk.PRIMO_SEMESTRE))
			dtLimite = new GregorianCalendar((archivioXmlPerlaFP.getEsercizio()-1),GregorianCalendar.JULY,new Integer(1));
		else
			dtLimite = new GregorianCalendar(archivioXmlPerlaFP.getEsercizio(),GregorianCalendar.JANUARY,new Integer(1));

		if (v_incarico.getDt_stipula().before(dtLimite.getTime()) && dtLimite.getTime().before(v_incarico.getDt_inizio_validita()))
			gcds.setTime(dtLimite.getTime());
		else
			gcds.setTime(v_incarico.getDt_stipula());

		elementConsulente.getIncarico().setDataAffidamento(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcds.get(Calendar.YEAR), gcds.get(Calendar.MONTH), gcds.get(Calendar.DAY_OF_MONTH))));

		//DATA INIZIO
		GregorianCalendar gcdi = new GregorianCalendar();
		gcdi.setTime(v_incarico.getDt_inizio_validita());
		elementConsulente.getIncarico().setDataInizio(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdi.get(Calendar.YEAR), gcdi.get(Calendar.MONTH), gcdi.get(Calendar.DAY_OF_MONTH))));
		
		//DATA FINE
		GregorianCalendar gcdf = new GregorianCalendar();
		gcdf.setTime(v_incarico.getDt_fine_validita_variazione()==null?v_incarico.getDt_fine_validita():v_incarico.getDt_fine_validita_variazione());
		elementConsulente.getIncarico().setDataFine(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdf.get(Calendar.YEAR), gcdf.get(Calendar.MONTH), gcdf.get(Calendar.DAY_OF_MONTH))));
		
		//INCARICO_SALDATO
		elementConsulente.getIncarico().setIncaricoSaldato(BigInteger.valueOf(2));  //NO

		//TIPO_IMPORTO
		elementConsulente.getIncarico().setTipoImporto(BigInteger.valueOf(1));  //PREVISTO

		//IMPORTO
		elementConsulente.getIncarico().setImporto(v_incarico.getImporto_lordo_con_variazioni().setScale(2));
		
		//NOTE
		elementConsulente.getIncarico().setNote(null);
		
		/*
		 * CREAZIONE TAG INCARICATO
		 */
		elementConsulente.setIncaricato(objectFactory.createConsulenteTypeIncaricato());
		elementConsulente.getIncaricato().setPersonaFisica(objectFactory.createConsulenteTypeIncaricatoPersonaFisica());

		//CODICE FISCALE
		try{
			elementConsulente.getIncaricato().getPersonaFisica().setCodiceFiscale(incarico.getTerzo().getAnagrafico().getCodice_fiscale());
		} catch (NullPointerException e){
		}
	
		//PARTITA IVA
		try{
			if (elementConsulente.getIncaricato().getPersonaFisica().getCodiceFiscale()==null)
				elementConsulente.getIncaricato().getPersonaFisica().setPartitaIva(incarico.getTerzo().getAnagrafico().getPartita_iva());
		} catch (NullPointerException e){
		}
		
		//COGNOME
		try{
			elementConsulente.getIncaricato().getPersonaFisica().setCognome(incarico.getTerzo().getAnagrafico().getCognome());
		} catch (NullPointerException e){
		}
			
		//NOME
		try{
			elementConsulente.getIncaricato().getPersonaFisica().setNome(incarico.getTerzo().getAnagrafico().getNome());
		} catch (NullPointerException e){
		}

		//ESTERO
		//Se trattasi di consulente estero che ha il codice fiscale valorizzato metto il campo estero a "false" così come indicato dalla Dott.ssa Paola Sarti
		//della Funzione Pubblica altrimenti metto quello corretto
		boolean estero=false;
		if (elementConsulente.getIncaricato().getPersonaFisica().getCodiceFiscale()==null ||
			elementConsulente.getIncaricato().getPersonaFisica().getCodiceFiscale().length()!=16){
			try{
				elementConsulente.getIncaricato().getPersonaFisica().setCodiceFiscale(null);
				estero = !NazioneBulk.ITALIA.equals(incarico.getTerzo().getAnagrafico().getComune_nascita().getTi_italiano_estero());
			} catch (NullPointerException e){
			}
		}
		elementConsulente.getIncaricato().getPersonaFisica().setEstero(estero?it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.YesNoType.Y
				                                                             :it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.YesNoType.N);

		//SESSO
		try{
			elementConsulente.getIncaricato().getPersonaFisica().setSesso(incarico.getTerzo().getAnagrafico().getTi_sesso().equals("M")?it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.SessoType.M
					                 																								   :it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.SessoType.F);
		} catch (NullPointerException e){
		}
			
		//DATA NASCITA
		try{
			if (estero || incarico.getTerzo().getAnagrafico().getDt_nascita()!=null) {
				GregorianCalendar gcdn = new GregorianCalendar();
				gcdn.setTime(incarico.getTerzo().getAnagrafico().getDt_nascita());
				elementConsulente.getIncaricato().getPersonaFisica().setDataNascita(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdn.get(Calendar.YEAR), gcdn.get(Calendar.MONTH), gcdn.get(Calendar.DAY_OF_MONTH))));
			}
		} catch (NullPointerException e){
		}
		return elementConsulente;
	}

	private it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType generaModificaConsulentePerla(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory objectFactoryUpd, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory objectFactoryAdd, Incarichi_comunicati_fpBulk incaricoComunicatoFP, V_incarichi_elenco_fpBulk incaricoElenco) throws DatatypeConfigurationException{
		it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType elementNuovoConsulentePerla = generaNuovoConsulentePerla(objectFactoryAdd,incaricoElenco);
		if (elementNuovoConsulentePerla==null) return null;

		it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType elementModificaConsulentePerla = objectFactoryUpd.createConsulenteType();

		elementModificaConsulentePerla.setIdMittente(incaricoComunicatoFP.getEsercizio_repertorio().toString()+'/'+incaricoComunicatoFP.getPg_repertorio().toString());

		elementModificaConsulentePerla.setIncarico(objectFactoryUpd.createConsulenteTypeIncarico());

		elementModificaConsulentePerla.getIncarico().setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));

		boolean isModificato=false;

		//verifico variazione
		if (incaricoComunicatoFP.getAnno_riferimento().compareTo(Integer.valueOf(2010))==1 || 
			(incaricoComunicatoFP.getAnno_riferimento().compareTo(Integer.valueOf(2010))==0 && incaricoComunicatoFP.getSemestre_riferimento().equals(Incarichi_archivio_xml_fpBulk.SECONDO_SEMESTRE))) {
			/*eliminato perchè nel file di ritorno PERLA non restituisce il valore con la conseguenza che ricreando i file
			 * di modifica li reimposta tutti
			if (incaricoComunicatoFP.getAttivita_economica()!="74" &&
				!Utility.equalsNull(incaricoComunicatoFP.getAttivita_economica(), elementNuovoConsulentePerla.getIncarico().getAttivitaEconomica())) {
				elementModificaConsulentePerla.getIncarico().setAttivitaEconomica(elementNuovoConsulentePerla.getIncarico().getAttivitaEconomica());
				isModificato=true;
			}
			*/
			/*eliminato per problemi legati ai caratteri accentati
			if (!Utility.equalsNull(incaricoComunicatoFP.getDescrizione_incarico(), elementNuovoConsulentePerla.getIncarico().getDescrizioneIncarico())) {
				elementModificaConsulentePerla.getIncarico().setDescrizioneIncarico(elementNuovoConsulentePerla.getIncarico().getDescrizioneIncarico());
				isModificato=true;	
			}
			*/
			if (!Utility.equalsNull(incaricoComunicatoFP.getModalita_acquisizione(), elementNuovoConsulentePerla.getIncarico().getModalitaAcquisizione())) {
				elementModificaConsulentePerla.getIncarico().setModalitaAcquisizione(elementNuovoConsulentePerla.getIncarico().getModalitaAcquisizione());
				isModificato=true;
			}
			if (!Utility.equalsNull(incaricoComunicatoFP.getTipo_rapporto(), elementNuovoConsulentePerla.getIncarico().getTipoRapporto())) {
				elementModificaConsulentePerla.getIncarico().setTipoRapporto(elementNuovoConsulentePerla.getIncarico().getTipoRapporto());
				isModificato=true;
			}
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getDt_fine(), new Timestamp(elementNuovoConsulentePerla.getIncarico().getDataFine().toGregorianCalendar().getTime().getTime()))) {
			elementModificaConsulentePerla.getIncarico().setDataFine(elementNuovoConsulentePerla.getIncarico().getDataFine());
			isModificato=true;
		}
		if (!((incaricoComunicatoFP.getImporto_previsto()==null && elementNuovoConsulentePerla.getIncarico().getImporto()==null) ||
			  (incaricoComunicatoFP.getImporto_previsto()!=null && elementNuovoConsulentePerla.getIncarico().getImporto()!=null &&
			   incaricoComunicatoFP.getImporto_previsto().compareTo(elementNuovoConsulentePerla.getIncarico().getImporto())==0))) {
			elementModificaConsulentePerla.getIncarico().setImporto(elementNuovoConsulentePerla.getIncarico().getImporto());
			isModificato=true;
		}
		if (!Utility.equalsNull(incaricoComunicatoFP.getFl_saldo()==null?BigInteger.valueOf(2):(incaricoComunicatoFP.getFl_saldo()?BigInteger.valueOf(1):BigInteger.valueOf(2)), elementNuovoConsulentePerla.getIncarico().getIncaricoSaldato())) {
			elementModificaConsulentePerla.getIncarico().setIncaricoSaldato(elementNuovoConsulentePerla.getIncarico().getIncaricoSaldato());
			isModificato=true;
		}
		if (isModificato) return elementModificaConsulentePerla;
		return null;
	}

	private IncaricoConsulente generaNuovoConsulentePerla2018(ActionContext context, V_incarichi_elenco_fpBulk v_incarico) throws BusinessProcessException, ComponentException {
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();
		IncaricoConsulente incaricoPerla = new IncaricoConsulente();

		//AMMINISTRAZIONE DICHIARANTE
		Dichiarante dichiarante = new Dichiarante();
		dichiarante.setCodiceAooIpa(v_incarico.getCodiceAooIpa());
		incaricoPerla.setDichiarante(dichiarante);

		//PERCETTORE

		//ESTERO
		//Se trattasi di consulente estero che ha il codice fiscale valorizzato metto il campo estero a "false" così come indicato dalla Dott.ssa Paola Sarti
		//della Funzione Pubblica altrimenti metto quello corretto
		boolean terzoEstero=false;
		if (incarico.getTerzo().getAnagrafico().getCodice_fiscale()==null ||
				incarico.getTerzo().getAnagrafico().getCodice_fiscale().length()!=16){
			try{
				terzoEstero = !NazioneBulk.ITALIA.equals(incarico.getTerzo().getAnagrafico().getComune_nascita().getTi_italiano_estero());
			} catch (Exception e){
			}
		}

		if (incarico.getTerzo().getAnagrafico().isPersonaFisica()) {
			PercettorePf percettorePf = new PercettorePf();
			percettorePf.setEstero(terzoEstero? YesNoEnum.Y:YesNoEnum.N);
			if (!terzoEstero) {
				//CODICE FISCALE
				try {
					percettorePf.setCodiceFiscale(incarico.getTerzo().getAnagrafico().getCodice_fiscale());
				} catch (Exception e) {
				}
			}
			//COGNOME
			try{
				percettorePf.setCognome(incarico.getTerzo().getAnagrafico().getCognome());
			} catch (Exception e){
			}
			//NOME
			try{
				percettorePf.setNome(incarico.getTerzo().getAnagrafico().getNome());
			} catch (Exception e){
			}
			//SESSO
			try{
				percettorePf.setGenere("M".equals(incarico.getTerzo().getAnagrafico().getTi_sesso())? GenereEnum.M:GenereEnum.F);
			} catch (Exception e){
			}
			//DATA NASCITA
			percettorePf.setDataNascita(incarico.getTerzo().getAnagrafico().getDt_nascita());

			if (!terzoEstero) {
				//COMUNE NASCITA
				try {
					percettorePf.setComuneNascita(incarico.getTerzo().getAnagrafico().getComune_nascita().getCd_catastale());
					if (percettorePf.getComuneNascita().equals("*"))
						percettorePf.setComuneNascita(percettorePf.getCodiceFiscale().substring(12,4));
				} catch (Exception e) {
					percettorePf.setComuneNascita(percettorePf.getCodiceFiscale().substring(12,4));
				}
			}
			incaricoPerla.setPercettorePf(percettorePf);
		} else {
			PercettorePg percettorePg = new PercettorePg();
			percettorePg.setEstero(terzoEstero?YesNoEnum.Y:YesNoEnum.N);

			if (!terzoEstero) {
				//CODICE FISCALE
				try {
					percettorePg.setCodiceFiscale(incarico.getTerzo().getAnagrafico().getCodice_fiscale());
				} catch (Exception e) {
				}
			}
			percettorePg.setDenominazione(incarico.getTerzo().getDenominazione_sede());
			incaricoPerla.setPercettorePg(percettorePg);
		}
		//FINE PERCETTORE

		//DATI INCARICO
		DatiIncarico datiIncarico = new DatiIncarico();

		//DESCRIZIONE INCARICO
		StringBuffer descrizione = new StringBuffer();
		descrizione.append("("+incarico.getEsercizio()+'/'+incarico.getPg_repertorio()+")");
		descrizione.append(" - "+incarico.getIncarichi_procedura().getOggetto());
		datiIncarico.setOggettoIncarico(descrizione.length()>200?descrizione.substring(0, 199):descrizione.toString());

		//DATA AFFIDAMENTO
		datiIncarico.setDataConferimento(v_incarico.getDt_stipula());

		//DATA INIZIO
		datiIncarico.setDataInizio(v_incarico.getDt_inizio_validita());

		//ATTO CONFERIMENTO
		StringBuilder atto = new StringBuilder();
		atto.append(incarico.getCd_provv());
		if (atto.length()>0 && incarico.getNr_provv()!=null)
			atto.append("/");
		atto.append(incarico.getNr_provv());
		if (atto.length()>0 && incarico.getDt_provv()!=null)
			atto.append(" del ");
		atto.append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(incarico.getDt_provv()));
		datiIncarico.setEstremiAttoConferimento(atto.toString());

		//TIPO DI RAPPORTO
		BigInteger tipoRapporto;
		if (incarico.getIncarichi_procedura().getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue())
			datiIncarico.setTipoRapporto(TipoRapportoEnum.COLLABORAZIONE_COORDINATA_CONTINUATIVA);
		else
			datiIncarico.setTipoRapporto(TipoRapportoEnum.PRESTAZIONE_OCCASIONALE);

		// NATURA CONFERIMENTO
		BigInteger naturaConferimento;
		if (incarico.getIncarichi_procedura().getTipo_prestazione()!=null && incarico.getIncarichi_procedura().getTipo_prestazione().getTipo_classificazione()!=null) {
			if (incarico.getIncarichi_procedura().getTipo_prestazione().isPrevistaDaNormeDiLegge())
				datiIncarico.setNaturaConferimento(NaturaConferimentoEnum.NATURA_VINCOLATA); //DI NATURA VINCOLATA
			else
				datiIncarico.setNaturaConferimento(NaturaConferimentoEnum.NATURA_DISCREZIONALE); //DI NATURA DISCREZIONALE
		} else
			datiIncarico.setNaturaConferimento(NaturaConferimentoEnum.NATURA_DISCREZIONALE); //DI NATURA DISCREZIONALE

		//ATTESTAZIONE VERIFICA INSUSSISTENZA
		if (incarico.getConflittoInteressi()!=null && incarico.getConflittoInteressi().getCms_node_ref()!=null)
			datiIncarico.setAttestazioneVerificaInsussistenza(YesNoEnum.Y);
		else
			datiIncarico.setAttestazioneVerificaInsussistenza(YesNoEnum.N);

		//RIFERIMENTO REGOLAMENTO
		//Y= si è fatto riferimento ad un regolamento adottato dall'amministrazione,
		//N= non si è fatto riferimento ad un regolamento adottato dall'amministrazione
		datiIncarico.setRiferimentoRegolamento(YesNoEnum.Y);

		incaricoPerla.setDatiIncarico(datiIncarico);

		//DATI ECONOMICI INCARICO
		DatiEconomici datiEconomiciIncarico = new DatiEconomici();

		//COMPENSO
		datiEconomiciIncarico.setCompenso(v_incarico.getImporto_lordo_con_variazioni().setScale(2));

		// TIPO COMPENSO
		// 1 PREVISTO, 2 PRESUNTO, 3 GRATUITO
		if (datiEconomiciIncarico.getCompenso().compareTo(BigDecimal.ZERO)<=0)
			datiEconomiciIncarico.setTipoCompenso(TipoCompensoEnum.GRATUITO); //GRATUITO
		else
			datiEconomiciIncarico.setTipoCompenso(TipoCompensoEnum.PREVISTO); //PREVISTO

		//ComponentiVariabilCompenso
		datiEconomiciIncarico.setComponentiVariabilCompenso(YesNoEnum.N);

		//DATA FINE
		GregorianCalendar gcdf = new GregorianCalendar();
		gcdf.setTime(v_incarico.getDt_fine_validita_variazione()==null?v_incarico.getDt_fine_validita():v_incarico.getDt_fine_validita_variazione());
		datiEconomiciIncarico.setDataFine(gcdf.getTime());

		incaricoPerla.setDatiEconomici(datiEconomiciIncarico);
		//FINE DATI ECONOMICI INCARICO

		//FINE DATI INCARICO

		//RIFERIMENTO NORMATIVO (Obbligatorio soltanto se l’incarico è stato conferito in applicazione di una specifica norma)
		if (Optional.ofNullable(incarico.getIncarichi_procedura().getTipo_norma_perla()).isPresent() &&
				incarico.getIncarichi_procedura().getTipo_norma_perla().getCd_tipo_norma().equals("67")) {
			//RIFERIMENTO NORMATIVO (GESTIONE TABELLA TIPO_NORMA_PERLA)
			it.cnr.perlapa.incarico.consulente.RiferimentoNormativo riferimentoNormativo = new it.cnr.perlapa.incarico.consulente.RiferimentoNormativo();
			riferimentoNormativo.setRiferimento(34);
			riferimentoNormativo.setNumero(incarico.getIncarichi_procedura().getTipo_norma_perla().getNumero_tipo_norma());
			riferimentoNormativo.setArticolo(incarico.getIncarichi_procedura().getTipo_norma_perla().getArticolo_tipo_norma());
			riferimentoNormativo.setComma(incarico.getIncarichi_procedura().getTipo_norma_perla().getComma_tipo_norma());
			riferimentoNormativo.setData(incarico.getIncarichi_procedura().getTipo_norma_perla().getDt_tipo_norma());
			incaricoPerla.setRiferimentoNormativo(riferimentoNormativo);
		}

		//ALLEGATI

		//CURRICULUM VITAE
		Allegati allegati = new Allegati();
		try {
			allegati.setCurriculumVitae(SpringUtil.getBean("storeService", StoreService.class).getResource(incarico.getCurriculumVincitore().getCms_node_ref()));
		} catch (Throwable e) {
		}

		//DICHIARAZIONE INCARICHI
		try {
			if (incarico.getIncarichi_repertorio_rappColl() != null && !incarico.getIncarichi_repertorio_rappColl().isEmpty()) {
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
				data_da.setTime(incarico.getDt_stipula());
				for (Iterator i = incarico.getIncarichi_repertorio_rappColl().iterator(); i.hasNext(); ) {
					Incarichi_repertorio_rappBulk rapporto = (Incarichi_repertorio_rappBulk) i.next();
					if (!rapporto.isAnnullato() && rapporto.getAnno_competenza().equals(data_da.get(java.util.Calendar.YEAR))) {
						allegati.setDichiarazioneIncarichi(storeService.getResource(rapporto.getCms_node_ref()));
						break;
					}
				}
			}
		} catch (Throwable e) {
		}
		incaricoPerla.setAllegati(allegati);

		//PAGATO
		if (incaricoPerla.getDatiEconomici().getCompenso().compareTo(BigDecimal.ZERO) != 0) {
			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession) createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);

			BigDecimal importoPagato = comp.getPagatoIncarico(context.getUserContext(), incarico);
			incaricoPerla.getDatiEconomici().setAmmontareErogato(importoPagato.setScale(2));
			if (importoPagato.compareTo(incaricoPerla.getDatiEconomici().getCompenso()) < 0)
				incaricoPerla.getDatiEconomici().setIncaricoSaldato(YesNoEnum.N);
			else
				incaricoPerla.getDatiEconomici().setIncaricoSaldato(YesNoEnum.Y);
		} else {
			incaricoPerla.getDatiEconomici().setIncaricoSaldato(YesNoEnum.N);
			incaricoPerla.getDatiEconomici().setAmmontareErogato(BigDecimal.ZERO);
		}
		return incaricoPerla;
	}

	public void generaXML(ActionContext context) throws BusinessProcessException {
      try{
            Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
                 
            if (archivioXmlFP==null || archivioXmlFP.getEsercizio()==null) {
               setMessage("Valorizzare tutti i campi di selezione per effettuare l'estrazione.");
               return;
            } else if (archivioXmlFP.isFl_crea_file_da_file()){
               archivioXmlFP.setFile_ric_err(((it.cnr.jada.action.HttpActionContext)context).getMultipartParameter("main.blob_ric_err").getFile());
               if ((archivioXmlFP.getFile_ric_err() == null || archivioXmlFP.getFile_ric_err().getName().equals(""))) {
                  setMessage("Indicare il file di risposta con errori della Funzione Pubblica dal quale estrarre solo gli incarichi corretti.");
                  return;
               }
            }
  
			ConsultazioniBP newBp = getConsIncarichiEstrazioneFpBP(context);

			if (newBp!=null){
				if (newBp.getIterator()==null || newBp.getIterator().countElements()==0)
					newBp.openIterator(context);
				generaXMLPerla2018(context, newBp.getIterator());
				if (((Incarichi_archivio_xml_fpBulk)getModel()).getPathFileZip()==null) {
					setMessage("Nessun Incarico da estrarre. File non prodotto.");
		    		return;
				}
				EJBCommonServices.closeRemoteIterator(context, newBp.detachIterator());
			}
		} catch (Exception e){
			throw handleException(e);
		}
	}

	public ConsultazioniBP getConsIncarichiEstrazioneFpBP(ActionContext context) throws BusinessProcessException {
		Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();

		CompoundFindClause clauses = new CompoundFindClause();
		
		try{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			Timestamp dt_inizio = new Timestamp(sdf.parse("01/01/"+archivioXmlFP.getEsercizio().intValue()).getTime());
			Timestamp dt_fine = new Timestamp(sdf.parse("31/12/"+archivioXmlFP.getEsercizio().intValue()).getTime());

			//clauses.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,195);

			if (archivioXmlFP.getDt_calcolo().equals(Incarichi_archivio_xml_fpBulk.DATA_STIPULA)) {
				clauses.addClause(FindClause.AND,"dt_stipula",SQLBuilder.GREATER_EQUALS,dt_inizio);
				clauses.addClause(FindClause.AND,"dt_stipula",SQLBuilder.LESS_EQUALS,dt_fine);
			} else {
				clauses.addClause(FindClause.AND,"dt_inizio_validita",SQLBuilder.GREATER_EQUALS,dt_inizio);
				clauses.addClause(FindClause.AND,"dt_inizio_validita",SQLBuilder.LESS_EQUALS,dt_fine);
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
			
			if (archivioXmlFP.getEsercizio()!=null && archivioXmlFP.getDt_calcolo()!=null) {
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
			CompoundFindClause clauses = new CompoundFindClause();
			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);
			
			if (getModel()!=null && ((Incarichi_archivio_xml_fpBulk)getModel()).isFl_crea_file_da_file()) {
				JAXBContext jc = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory.class);

				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType esitoComunicazione = ((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileErr)).getValue();
				
				for (Iterator iterator = esitoComunicazione.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType nuovoConsulenteEsito = (it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType) iterator.next();

					if (nuovoConsulenteEsito.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK)) {
						int esercizio_repertorio = new Integer(nuovoConsulenteEsito.getIdMittente().substring(0,4)); 
						Long pg_repertorio = new Long(nuovoConsulenteEsito.getIdMittente().substring(5)); 
						CompoundFindClause parzClause = new CompoundFindClause();
						parzClause.setLogicalOperator(FindClause.OR);
						parzClause.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,esercizio_repertorio);
						parzClause.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,pg_repertorio);
						clauses.addChild(parzClause);
					}
				}
			} else {
				JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
				
				EsitoComunicazione esitoComunicazione = (EsitoComunicazione)jc.createUnmarshaller().unmarshal(fileErr);
	
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
			}
			return clauses;
		} catch (Exception e){
			throw handleException(e);
		}
	}

	@Deprecated
	public void generaXML(ActionContext context, RemoteIterator sourceIterator) throws BusinessProcessException {
		try{
			List<V_incarichi_elenco_fpBulk> arraylist = new ArrayList<V_incarichi_elenco_fpBulk>();
	        sourceIterator.moveTo(0);
			while(sourceIterator.hasMoreElements()) 
				arraylist.add((V_incarichi_elenco_fpBulk)sourceIterator.nextElement());
			
			generaXML(context, arraylist);
			EJBCommonServices.closeRemoteIterator(context, sourceIterator);
		} catch (Exception e){
			throw handleException(e);
		}
	}

	@Deprecated
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
			
			if (!(listFileNuovoIncaricoXML.isEmpty() && listFileModificaIncaricoXML.isEmpty() && listFileCancellaIncaricoXML.isEmpty() && 
			 	  listFileAnomalieNuovoIncaricoXML.isEmpty() && listFileAnomalieModificaIncaricoXML.isEmpty())) {
				hashFileIncaricoXML.put("Incarichi Nuovi", listFileNuovoIncaricoXML);
				hashFileIncaricoXML.put("Incarichi Modificati", listFileModificaIncaricoXML);
				hashFileIncaricoXML.put("Incarichi Cancellati", listFileCancellaIncaricoXML);
				hashFileIncaricoXML.put("Anomalie Incarichi Nuovi", listFileAnomalieNuovoIncaricoXML);
				hashFileIncaricoXML.put("Anomalie Incarichi Modificati", listFileAnomalieModificaIncaricoXML);
	
				String fileName = "EstrazioneIncarichiFp"+archivioXmlFP.getEsercizio()+archivioXmlFP.getSemestre()+".zip";
				creaFileZip(hashFileIncaricoXML, fileName);
				archivioXmlFP.setPathFileZip("tmp/"+fileName);
			}
			
			cancellaListaFile(listFileNuovoIncaricoXML, listFileModificaIncaricoXML, listFileCancellaIncaricoXML, listFileAnomalieNuovoIncaricoXML, listFileAnomalieModificaIncaricoXML);
		} catch (Exception e){
			throw handleException(e);
		}
    }

	@Deprecated
	public void generaXMLPerla(ActionContext context, RemoteIterator sourceIterator) throws BusinessProcessException {
		try{
			List<V_incarichi_elenco_fpBulk> arraylist = new ArrayList<V_incarichi_elenco_fpBulk>();
	        sourceIterator.moveTo(0);
			while(sourceIterator.hasMoreElements()) 
				arraylist.add((V_incarichi_elenco_fpBulk)sourceIterator.nextElement());
			
			generaXMLPerla(context, arraylist);
			EJBCommonServices.closeRemoteIterator(context, sourceIterator);
		} catch (Exception e){
			throw handleException(e);
		}
	}

	@Deprecated
	public void generaXMLPerla(ActionContext context, List<V_incarichi_elenco_fpBulk> list) throws BusinessProcessException {
		try{
			Incarichi_archivio_xml_fpBulk archivioXmlPerlaFP = (Incarichi_archivio_xml_fpBulk)getModel();
			archivioXmlPerlaFP.setMapFileNuovoIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setMapFileModificaIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setMapFileCancellaIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setMapFileAnomalieNuovoIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setMapFileAnomalieModificaIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setMapFileAnomalieCancellaIncaricoXMLPerla(new HashMap<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>>());
			archivioXmlPerlaFP.setPathFileZip(null);			

			List<File> listFileNuovoIncaricoXMLPerla=new ArrayList<File>();
			List<File> listFileModificaIncaricoXMLPerla=new ArrayList<File>();
			List<File> listFileCancellaIncaricoXMLPerla=new ArrayList<File>();
			List<File> listFileAnomalieNuovoIncaricoXMLPerla=new ArrayList<File>();
			List<File> listFileAnomalieModificaIncaricoXMLPerla=new ArrayList<File>();

			JAXBContext jcNew = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory.class);
			JAXBContext jcUpd = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory.class);
			JAXBContext jcDel = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory.class);
			
			it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory objAdd = new it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory();
			it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory objUpd = new it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory();
			it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory objDel = new it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory();

			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);

			Map<String,String> mapIdFpUo = new HashMap<String,String>();
			
			Map<String,List<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType>> mapElencoNuoviConsulentiPerla = new HashMap<String,List<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType>>();
			Map<String,List<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType>> mapElencoModificheConsulentiPerla = new HashMap<String,List<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType>>();
			Map<String,List<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType>> mapElencoCancellaIncarichiPerla = new HashMap<String,List<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType>>();
			
			Map<String,List<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType>> mapElencoAnomalieNuoviConsulentiPerla = new HashMap<String,List<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType>>();
			Map<String,List<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType>> mapElencoAnomalieModificheConsulentiPerla = new HashMap<String,List<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType>>();

			for (Iterator<V_incarichi_elenco_fpBulk> iterator = list.iterator(); iterator.hasNext();) {
				V_incarichi_elenco_fpBulk incaricoElenco = (V_incarichi_elenco_fpBulk)iterator.next();
				incaricoElenco = comp.completaIncaricoElencoFP(context.getUserContext(), incaricoElenco);
					
				Incarichi_comunicati_fpBulk incaricoComunicatoFP = comp.getIncarichiComunicatiAggFP(context.getUserContext(), incaricoElenco);

				if (incaricoComunicatoFP==null || incaricoComunicatoFP.getId_incarico()==null) {
					it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType nuovoConsulenteAnomaliaPerla=getAnomalieNuovoConsulentePerla(objAdd,incaricoElenco);
					if (nuovoConsulenteAnomaliaPerla.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK)){
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType elementNuovoConsulentePerla = generaNuovoConsulentePerla(objAdd,incaricoElenco);
						if (elementNuovoConsulentePerla!=null) {
							if (elementNuovoConsulentePerla.getIncarico().getImporto().compareTo(BigDecimal.ZERO)!=0) {
								if (archivioXmlPerlaFP.getTipo_estrazione_pagamenti().equals(Incarichi_archivio_xml_fpBulk.PAGAMENTI_INCLUDI)){
									//CREAZIONE TAG PAGAMENTO
									List<Incarichi_comunicati_fp_detBulk> listPagamenti = comp.getPagatoPerSemestre(context.getUserContext(), incaricoElenco.getIncaricoRepertorio());
									
									//Rimuovo i pagamenti di semestri successivi a quello di comunicazione
									List<Incarichi_comunicati_fp_detBulk> listPagamentiOltreLimite = new ArrayList<Incarichi_comunicati_fp_detBulk>();
									for (Iterator<Incarichi_comunicati_fp_detBulk> iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
										Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDetBulk = iterator2.next();
										if (incarichiComunicatiFpDetBulk.getAnno_pag().compareTo(archivioXmlPerlaFP.getEsercizio())==1 ||
											(incarichiComunicatiFpDetBulk.getAnno_pag().compareTo(archivioXmlPerlaFP.getEsercizio())==0 &&
											 incarichiComunicatiFpDetBulk.getSemestre_pag().compareTo(archivioXmlPerlaFP.getSemestre())==1))
											listPagamentiOltreLimite.add(incarichiComunicatiFpDetBulk);
									}
									listPagamenti.removeAll(listPagamentiOltreLimite);

									//scrivo il tag dei pagamenti rimasti
									if (!listPagamenti.isEmpty()) {
										elementNuovoConsulentePerla.setPagamenti(objAdd.createConsulenteTypePagamenti());
										for (Iterator<Incarichi_comunicati_fp_detBulk> iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
											Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDetBulk = iterator2.next();
											
											it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType.Pagamenti.NuovoPagamento elementNuovoPagamento = objAdd.createConsulenteTypePagamentiNuovoPagamento();
											elementNuovoPagamento.setAnno(BigInteger.valueOf(incarichiComunicatiFpDetBulk.getAnno_pag()));
											elementNuovoPagamento.setSemestre(BigInteger.valueOf(incarichiComunicatiFpDetBulk.getSemestre_pag()));
											elementNuovoPagamento.setImporto(incarichiComunicatiFpDetBulk.getImporto_pag().setScale(2));
											elementNuovoConsulentePerla.getPagamenti().getNuovoPagamento().add(elementNuovoPagamento);
										}
									}
								}
								if (!mapElencoNuoviConsulentiPerla.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
									mapElencoNuoviConsulentiPerla.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType>());
								if (!mapIdFpUo.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
									mapIdFpUo.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getCd_unita_organizzativa());
								mapElencoNuoviConsulentiPerla.get(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()).add(elementNuovoConsulentePerla);
							}
						} else {
							nuovoConsulenteAnomaliaPerla = objAdd.createEsitoConsulenteType();
							nuovoConsulenteAnomaliaPerla.setEsito(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.KO);
							nuovoConsulenteAnomaliaPerla.setErrori(objAdd.createErroriConsulenteType());
							nuovoConsulenteAnomaliaPerla.getErrori().setIncarico(objAdd.createErroriConsulenteTypeIncarico());
							nuovoConsulenteAnomaliaPerla.getErrori().getIncarico().setDescrizioneIncarico("AnomaliaIncarico: "+incaricoElenco.getEsercizio().toString()+"/"+incaricoElenco.getPg_repertorio().toString());
							if (!mapElencoAnomalieNuoviConsulentiPerla.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
								mapElencoAnomalieNuoviConsulentiPerla.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType>());
							if (!mapIdFpUo.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
								mapIdFpUo.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getCd_unita_organizzativa());
							mapElencoAnomalieNuoviConsulentiPerla.get(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()).add(nuovoConsulenteAnomaliaPerla);
						}
					} else {
						if (!mapElencoAnomalieNuoviConsulentiPerla.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
							mapElencoAnomalieNuoviConsulentiPerla.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType>());
						if (!mapIdFpUo.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
							mapIdFpUo.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getCd_unita_organizzativa());
						mapElencoAnomalieNuoviConsulentiPerla.get(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()).add(nuovoConsulenteAnomaliaPerla);
					}
				} else {
					//AGGIORNAMENTO COMUNICAZIONE
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType modificaConsulenteAnomaliaPerla=getAnomalieModificaConsulentePerla(objUpd,incaricoElenco);
					if (modificaConsulenteAnomaliaPerla.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.OK)){
						it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType elementModificaConsulentePerla=generaModificaConsulentePerla(objUpd,objAdd,incaricoComunicatoFP,incaricoElenco);
						if (archivioXmlPerlaFP.getTipo_estrazione_pagamenti().equals(Incarichi_archivio_xml_fpBulk.PAGAMENTI_INCLUDI)){
							//AGGIUNGO MODIFICHE PAGAMENTI
							List<Incarichi_comunicati_fp_detBulk> listPagamenti = comp.getPagatoPerSemestre(context.getUserContext(), incaricoElenco.getIncaricoRepertorio());
							elementModificaConsulentePerla=generaPagamentiPerModificaConsulentePerla(objUpd, incaricoComunicatoFP, elementModificaConsulentePerla, listPagamenti);
						}
						if (elementModificaConsulentePerla!=null) {
							if (!mapElencoModificheConsulentiPerla.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
								mapElencoModificheConsulentiPerla.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType>());
							if (!mapIdFpUo.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
								mapIdFpUo.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getCd_unita_organizzativa());
							mapElencoModificheConsulentiPerla.get(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()).add(elementModificaConsulentePerla);
						}
					} else {
						modificaConsulenteAnomaliaPerla.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));

						if (!mapElencoAnomalieModificheConsulentiPerla.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
							mapElencoAnomalieModificheConsulentiPerla.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType>());
						if (!mapIdFpUo.containsKey(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()))
							mapIdFpUo.put(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica(), incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getCd_unita_organizzativa());
						mapElencoAnomalieModificheConsulentiPerla.get(incaricoElenco.getIncaricoRepertorio().getUnita_organizzativa().getId_funzione_pubblica()).add(modificaConsulenteAnomaliaPerla);
					}
				}
			}
			
			List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> incarichiDaEliminare = comp.getIncarichiComunicatiEliminatiFP(context.getUserContext(), archivioXmlPerlaFP.getEsercizio(),archivioXmlPerlaFP.getSemestre());

			for (Iterator<Incarichi_comunicati_fpBulk> iterator = incarichiDaEliminare.iterator(); iterator.hasNext();) {
				Incarichi_comunicati_fpBulk incaricoComunicatoFP = iterator.next();
				if (incaricoComunicatoFP.getId_incarico()!=null) {
					it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType elementCancellaIncaricoPerla = objDel.createIncaricoType();
					elementCancellaIncaricoPerla.setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));
					if (!mapElencoCancellaIncarichiPerla.containsKey(incaricoComunicatoFP.getCodice_ente()))
						mapElencoCancellaIncarichiPerla.put(incaricoComunicatoFP.getCodice_ente(), new ArrayList<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType>());
					mapElencoCancellaIncarichiPerla.get(incaricoComunicatoFP.getCodice_ente()).add(elementCancellaIncaricoPerla);
				}
			}
			
			//creo i file dei nuovi incarichi
			for (Iterator<String> iteratorKey = mapElencoNuoviConsulentiPerla.keySet().iterator(); iteratorKey.hasNext();) {
				String codiceEnte = iteratorKey.next();

				int nrFile=0;

				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType> iterator = mapElencoNuoviConsulentiPerla.get(codiceEnte).iterator(); iterator.hasNext();) {
					int nrRighe=0;

					it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType currentComunicazioneType = objAdd.createComunicazioneType();
					currentComunicazioneType.setInserimentoIncarichi(objAdd.createComunicazioneTypeInserimentoIncarichi());
					currentComunicazioneType.getInserimentoIncarichi().setAnnoRiferimento(BigInteger.valueOf(archivioXmlPerlaFP.getEsercizio()));
					currentComunicazioneType.getInserimentoIncarichi().setCodiceEnte(Long.valueOf(codiceEnte));
					currentComunicazioneType.getInserimentoIncarichi().setNuoviIncarichi(objAdd.createComunicazioneTypeInserimentoIncarichiNuoviIncarichi());
	
					while(iterator.hasNext() && nrRighe<archivioXmlPerlaFP.getNum_max_file_record()) {
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType nuovoConsulente = iterator.next();
						currentComunicazioneType.getInserimentoIncarichi().getNuoviIncarichi().getConsulente().add(nuovoConsulente);
						nrRighe++;
					}
				
					nrFile++;
					String fileName = "EstrazioneNuoviIncarichiPerla_"+mapIdFpUo.get(codiceEnte)+"_"+codiceEnte+"_"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+"_"+nrFile+".xml";
					File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);					
					JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> currentComunicazione = objAdd.createComunicazione(currentComunicazioneType);
					jcNew.createMarshaller().marshal(currentComunicazione, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					archivioXmlPerlaFP.getMapFileNuovoIncaricoXMLPerla().put("tmp/"+fileName, currentComunicazione);
					listFileNuovoIncaricoXMLPerla.add(file);
				}
			}

			//creo i file degli incarichi modificati
			for (Iterator<String> iteratorKey = mapElencoModificheConsulentiPerla.keySet().iterator(); iteratorKey.hasNext();) {
				String codiceEnte = iteratorKey.next();

				int nrFile=0;
				
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType> iterator = mapElencoModificheConsulentiPerla.get(codiceEnte).iterator(); iterator.hasNext();) {
					int nrRighe=0;
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType currentComunicazioneType = objUpd.createComunicazioneType();
					currentComunicazioneType.setVariazioneIncarichi(objUpd.createComunicazioneTypeVariazioneIncarichi());
					currentComunicazioneType.getVariazioneIncarichi().setCodiceEnte(Long.valueOf(codiceEnte));
					currentComunicazioneType.getVariazioneIncarichi().setModificaIncarichi(objUpd.createComunicazioneTypeVariazioneIncarichiModificaIncarichi());
	
					while(iterator.hasNext() && nrRighe<archivioXmlPerlaFP.getNum_max_file_record()) {
						it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente = iterator.next();
						currentComunicazioneType.getVariazioneIncarichi().getModificaIncarichi().getConsulente().add(modificaConsulente);
						nrRighe++;
					}
	
					nrFile++;
					String fileName = "EstrazioneModificheIncarichiPerla_"+mapIdFpUo.get(codiceEnte)+"_"+codiceEnte+"_"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+"_"+nrFile+".xml";
					File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);					
					JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> currentComunicazione = objUpd.createComunicazione(currentComunicazioneType);
					jcUpd.createMarshaller().marshal(currentComunicazione, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					archivioXmlPerlaFP.getMapFileModificaIncaricoXMLPerla().put("tmp/"+fileName, currentComunicazione);
					listFileModificaIncaricoXMLPerla.add(file);
				}
			}
			
			//creo i file degli incarichi eliminati
			for (Iterator<String> iteratorKey = mapElencoCancellaIncarichiPerla.keySet().iterator(); iteratorKey.hasNext();) {
				String codiceEnte = iteratorKey.next();

				int nrFile=0;

				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType> iterator = mapElencoCancellaIncarichiPerla.get(codiceEnte).iterator(); iterator.hasNext();) {
					int nrRighe=0;
					it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType currentComunicazioneType = objDel.createComunicazioneType();
					currentComunicazioneType.setCancellazioneIncarichi(objDel.createComunicazioneTypeCancellazioneIncarichi());
					currentComunicazioneType.getCancellazioneIncarichi().setCodiceEnte(Long.valueOf(codiceEnte));
					currentComunicazioneType.getCancellazioneIncarichi().setIncarichi(objDel.createComunicazioneTypeCancellazioneIncarichiIncarichi());

					while(iterator.hasNext() && nrRighe<archivioXmlPerlaFP.getNum_max_file_record()) {
						it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.IncaricoType cancellaIncarico = iterator.next();
						currentComunicazioneType.getCancellazioneIncarichi().getIncarichi().getConsulente().add(cancellaIncarico);
						nrRighe++;
					}
	
					nrFile++;
					String fileName = "EstrazioneCancellaIncarichiPerla_"+mapIdFpUo.get(codiceEnte)+"_"+codiceEnte+"_"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+"_"+nrFile+".xml";
					File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(file);					
					JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> currentComunicazione = objDel.createComunicazione(currentComunicazioneType);
					jcDel.createMarshaller().marshal(currentComunicazione, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					archivioXmlPerlaFP.getMapFileCancellaIncaricoXMLPerla().put("tmp/"+fileName, currentComunicazione);
					listFileCancellaIncaricoXMLPerla.add(file);
				}
			}

			//creo i file delle anomalie nuovi consulenti
			for (Iterator<String> iteratorKey = mapElencoAnomalieNuoviConsulentiPerla.keySet().iterator(); iteratorKey.hasNext();) {
				String codiceEnte = iteratorKey.next();

				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType currentComunicazioneType = objAdd.createComunicazioneType();
				currentComunicazioneType.setEsitoInserimentoIncarichi(objAdd.createComunicazioneTypeEsitoInserimentoIncarichi());
				currentComunicazioneType.getEsitoInserimentoIncarichi().setAnnoRiferimento(BigInteger.valueOf(archivioXmlPerlaFP.getEsercizio()));
				currentComunicazioneType.getEsitoInserimentoIncarichi().setCodiceEnte(codiceEnte!=null?Long.valueOf(codiceEnte):Long.valueOf(0));
				currentComunicazioneType.getEsitoInserimentoIncarichi().setEsitoFile(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.KO);
				currentComunicazioneType.getEsitoInserimentoIncarichi().setEsitoNuoviIncarichi(objAdd.createComunicazioneTypeEsitoInserimentoIncarichiEsitoNuoviIncarichi());
				currentComunicazioneType.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().addAll(mapElencoAnomalieNuoviConsulentiPerla.get(codiceEnte));
				
				String fileName = "AnomalieEstrazioneNuoviIncarichiPerla_"+(codiceEnte!=null?mapIdFpUo.get(codiceEnte)+"_"+Long.valueOf(codiceEnte):Long.valueOf(0))+"_"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+".xml";
				File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);					
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> currentComunicazione = objAdd.createComunicazione(currentComunicazioneType);
				jcNew.createMarshaller().marshal(currentComunicazione, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				archivioXmlPerlaFP.getMapFileAnomalieNuovoIncaricoXMLPerla().put("tmp/"+fileName, currentComunicazione);
				listFileAnomalieNuovoIncaricoXMLPerla.add(file);
			}

			//creo i file delle anomalie modifica consulenti
			for (Iterator<String> iteratorKey = mapElencoAnomalieModificheConsulentiPerla.keySet().iterator(); iteratorKey.hasNext();) {
				String codiceEnte = iteratorKey.next();

				it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType currentComunicazioneType = objUpd.createComunicazioneType();
				currentComunicazioneType.setEsitoVariazioneIncarichi(objUpd.createComunicazioneTypeEsitoVariazioneIncarichi());
				currentComunicazioneType.getEsitoVariazioneIncarichi().setCodiceEnte(codiceEnte!=null?Long.valueOf(codiceEnte):Long.valueOf(0));
				currentComunicazioneType.getEsitoVariazioneIncarichi().setEsitoFile(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.KO);
				currentComunicazioneType.getEsitoVariazioneIncarichi().setEsitoModificaIncarichi(objUpd.createComunicazioneTypeEsitoVariazioneIncarichiEsitoModificaIncarichi());
				currentComunicazioneType.getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().addAll(mapElencoAnomalieModificheConsulentiPerla.get(codiceEnte));

				String fileName = "AnomalieEstrazioneModificheIncarichiPerla"+(codiceEnte!=null?mapIdFpUo.get(codiceEnte)+"_"+Long.valueOf(codiceEnte):Long.valueOf(0))+"_"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+".xml";
				File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);					
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> currentComunicazione = objUpd.createComunicazione(currentComunicazioneType);
				jcUpd.createMarshaller().marshal(currentComunicazione, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				archivioXmlPerlaFP.getMapFileAnomalieModificaIncaricoXMLPerla().put("tmp/"+fileName, currentComunicazione);
				listFileAnomalieModificaIncaricoXMLPerla.add(file);
			}

			Hashtable<String, List<File>> hashFileIncaricoXML=new Hashtable<String, List<File>>();
			
			if (!(listFileNuovoIncaricoXMLPerla.isEmpty() && listFileModificaIncaricoXMLPerla.isEmpty() && listFileCancellaIncaricoXMLPerla.isEmpty() && 
			 	  listFileAnomalieNuovoIncaricoXMLPerla.isEmpty() && listFileAnomalieModificaIncaricoXMLPerla.isEmpty())) {
				hashFileIncaricoXML.put("Incarichi Nuovi", listFileNuovoIncaricoXMLPerla);
				hashFileIncaricoXML.put("Incarichi Modificati", listFileModificaIncaricoXMLPerla);
				hashFileIncaricoXML.put("Incarichi Cancellati", listFileCancellaIncaricoXMLPerla);
				hashFileIncaricoXML.put("Anomalie Incarichi Nuovi", listFileAnomalieNuovoIncaricoXMLPerla);
				hashFileIncaricoXML.put("Anomalie Incarichi Modificati", listFileAnomalieModificaIncaricoXMLPerla);
	
				String fileName = "EstrazioneIncarichiPerla"+archivioXmlPerlaFP.getEsercizio()+archivioXmlPerlaFP.getSemestre()+".zip";
				creaFileZip(hashFileIncaricoXML, fileName);
				archivioXmlPerlaFP.setPathFileZip("tmp/"+fileName);
			}
			cancellaListaFile(listFileNuovoIncaricoXMLPerla, listFileModificaIncaricoXMLPerla, listFileCancellaIncaricoXMLPerla, listFileAnomalieNuovoIncaricoXMLPerla, listFileAnomalieModificaIncaricoXMLPerla);
		} catch (Exception e){
			throw handleException(e);
		}
    }

	public File creaFileZip(Hashtable<String, List<File>> hashFileXML, String fileName) throws BusinessProcessException {
		try {
			byte[] buffer = new byte[18024];
			File fileZip = File.createTempFile(fileName, ".zip");
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
	
	public void clearSelection(ActionContext context, Incarichi_archivio_xml_fpBulk archivioXmlFP) throws BusinessProcessException {
		if (archivioXmlFP==null) 
			archivioXmlFP = (Incarichi_archivio_xml_fpBulk)getModel();
		if (archivioXmlFP!=null){
			archivioXmlFP.setEsercizio(null);
			archivioXmlFP.setSemestre(null);
			archivioXmlFP.setEsercizio_inizio(Integer.valueOf(2009));
			archivioXmlFP.setSemestre_inizio(Integer.valueOf(1));
			archivioXmlFP.setDt_calcolo(Incarichi_archivio_xml_fpBulk.DATA_INIZIO_ATTIVITA);
			archivioXmlFP.setTipo_estrazione_pagamenti(Incarichi_archivio_xml_fpBulk.PAGAMENTI_INCLUDI);
			archivioXmlFP.setFl_crea_file_modifiche(false);
			archivioXmlFP.setFl_crea_file_per_tipologia(true);
			archivioXmlFP.setFl_crea_file_perla(true);
			archivioXmlFP.setNum_max_file_record(Integer.decode("1000"));
			archivioXmlFP.setMapFileNuovoIncaricoXML(null);
			archivioXmlFP.setMapFileModificaIncaricoXML(null);
			archivioXmlFP.setMapFileCancellaIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieNuovoIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieModificaIncaricoXML(null);
			archivioXmlFP.setMapFileAnomalieCancellaIncaricoXML(null);
			archivioXmlFP.setMapFileNuovoIncaricoXMLPerla(null);
			archivioXmlFP.setMapFileModificaIncaricoXMLPerla(null);
			archivioXmlFP.setMapFileCancellaIncaricoXMLPerla(null);
			archivioXmlFP.setMapFileAnomalieNuovoIncaricoXMLPerla(null);
			archivioXmlFP.setMapFileAnomalieModificaIncaricoXMLPerla(null);
			archivioXmlFP.setMapFileAnomalieCancellaIncaricoXMLPerla(null);
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

	private it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType generaPagamentiPerModificaConsulentePerla(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory objectFactory, Incarichi_comunicati_fpBulk incaricoComunicatoFP, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente, List<Incarichi_comunicati_fp_detBulk> listPagamenti) throws DatatypeConfigurationException{
		it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType myModificaConsulente = objectFactory.createConsulenteType();
		myModificaConsulente.setIdMittente(incaricoComunicatoFP.getEsercizio_repertorio().toString()+'/'+incaricoComunicatoFP.getPg_repertorio().toString());
		myModificaConsulente.setIncarico(objectFactory.createConsulenteTypeIncarico());
		myModificaConsulente.getIncarico().setId(Long.parseLong(incaricoComunicatoFP.getId_incarico()));
		myModificaConsulente.setPagamenti(objectFactory.createConsulenteTypePagamenti());
		
		for (Iterator iterator = incaricoComunicatoFP.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
			Incarichi_comunicati_fp_detBulk incarichiComunicatiFpDet = (Incarichi_comunicati_fp_detBulk) iterator.next();
			//lo aggiorno solo se di semestre non successivo a quello richiesto
			if (!(incarichiComunicatiFpDet.getAnno_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getEsercizio())==1 ||
			      (incarichiComunicatiFpDet.getAnno_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getEsercizio())==0 &&
			       incarichiComunicatiFpDet.getSemestre_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getSemestre())==1))) {
				boolean trovato=false;
				for (Iterator iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagamento = (Incarichi_comunicati_fp_detBulk) iterator2.next();
					if (incarichiComunicatiFpDet.getAnno_pag().equals(pagamento.getAnno_pag()) &&
						incarichiComunicatiFpDet.getSemestre_pag().equals(pagamento.getSemestre_pag())) {
						if (!incarichiComunicatiFpDet.getImporto_pag().setScale(2).equals(pagamento.getImporto_pag().setScale(2))) {
							it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.ModificaPagamento elementModificaPagamento = new it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.ModificaPagamento();
							elementModificaPagamento.setAnno(BigInteger.valueOf(pagamento.getAnno_pag()));
							elementModificaPagamento.setSemestre(BigInteger.valueOf(pagamento.getSemestre_pag()));
							elementModificaPagamento.setImporto(pagamento.getImporto_pag().setScale(2));
							myModificaConsulente.getPagamenti().getModificaPagamento().add(elementModificaPagamento);
						}
						trovato=true;
						break;
					}
				}
				if (!trovato){
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.CancellaPagamento elementCancellaPagamento = new it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.CancellaPagamento();
					elementCancellaPagamento.setAnno(BigInteger.valueOf(incarichiComunicatiFpDet.getAnno_pag()));
					elementCancellaPagamento.setSemestre(BigInteger.valueOf(incarichiComunicatiFpDet.getSemestre_pag()));
					myModificaConsulente.getPagamenti().getCancellaPagamento().add(elementCancellaPagamento);
				}
			}
		}

		for (Iterator iterator2 = listPagamenti.iterator(); iterator2.hasNext();) {
			Incarichi_comunicati_fp_detBulk pagamento = (Incarichi_comunicati_fp_detBulk) iterator2.next();

			//lo aggiorno solo se di semestre non successivo a quello richiesto
			if (!(pagamento.getAnno_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getEsercizio())==1 ||
  			      (pagamento.getAnno_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getEsercizio())==0 &&
				   pagamento.getSemestre_pag().compareTo(((Incarichi_archivio_xml_fpBulk)getModel()).getSemestre())==1))) {
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
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.NuovoPagamento elementNuovoPagamento = new it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.NuovoPagamento();
					elementNuovoPagamento.setAnno(BigInteger.valueOf(pagamento.getAnno_pag()));
					elementNuovoPagamento.setSemestre(BigInteger.valueOf(pagamento.getSemestre_pag()));
					elementNuovoPagamento.setImporto(pagamento.getImporto_pag().setScale(2));
					myModificaConsulente.getPagamenti().getNuovoPagamento().add(elementNuovoPagamento);
				}
			}
		}
		if (myModificaConsulente.getPagamenti().getNuovoPagamento().isEmpty() &&
			myModificaConsulente.getPagamenti().getModificaPagamento().isEmpty() &&
			myModificaConsulente.getPagamenti().getCancellaPagamento().isEmpty())
			return modificaConsulente;
		else if (modificaConsulente==null)
			return myModificaConsulente;
		else {
			modificaConsulente.setPagamenti(objectFactory.createConsulenteTypePagamenti());
			modificaConsulente.getPagamenti().getNuovoPagamento().addAll(myModificaConsulente.getPagamenti().getNuovoPagamento());
			modificaConsulente.getPagamenti().getModificaPagamento().addAll(myModificaConsulente.getPagamenti().getModificaPagamento());
			modificaConsulente.getPagamenti().getCancellaPagamento().addAll(myModificaConsulente.getPagamenti().getCancellaPagamento());
			return modificaConsulente;
		}
	}

	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk =  super.initializeModelForInsert(actioncontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk) {
			((Incarichi_archivio_xml_fpBulk)oggettobulk).setFl_perla(Boolean.TRUE);
			((Incarichi_archivio_xml_fpBulk)oggettobulk).setFl_merge_perla(Boolean.TRUE);
			clearSelection(actioncontext, (Incarichi_archivio_xml_fpBulk)oggettobulk);
		}
		return oggettobulk;
	}
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk =  super.initializeModelForEdit(actioncontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk)
			clearSelection(actioncontext, (Incarichi_archivio_xml_fpBulk)oggettobulk);
		return oggettobulk;
	}

	public void generaXMLPerla2018(ActionContext context, RemoteIterator sourceIterator) throws BusinessProcessException {
		try{
			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);
			IncarichiRepertorioComponentSession incRepComponent = (IncarichiRepertorioComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiRepertorioComponentSession", IncarichiRepertorioComponentSession.class);
			List<String> allListIncarichiOK = new ArrayList<String>();
			List<String> allListAnomaliePerla = new ArrayList<String>();
			sourceIterator.moveTo(0);
			while(sourceIterator.hasMoreElements()) {
				V_incarichi_elenco_fpBulk incaricoElenco = (V_incarichi_elenco_fpBulk)sourceIterator.nextElement();
				try {
					incaricoElenco = comp.completaIncaricoElencoFP(context.getUserContext(), incaricoElenco);
					comp.comunicaPerla2018(context.getUserContext(), incaricoElenco);
					allListIncarichiOK.add("Incarico: " + incaricoElenco.getEsercizio() + "/" + incaricoElenco.getPg_repertorio() +
							" - Aggiornamento effettuato!");
				} catch (Exception e) {
					incRepComponent.aggiornaDatiPerla(context.getUserContext(), incaricoElenco.getIncaricoRepertorio(), null, e.getMessage());
					allListAnomaliePerla.add("Incarico: " + incaricoElenco.getEsercizio() + "/" + incaricoElenco.getPg_repertorio() +
							" - Errore: " + e.getMessage());
				}
			}
			EJBCommonServices.closeRemoteIterator(context, sourceIterator);

			if (!allListIncarichiOK.isEmpty())
				allListIncarichiOK.stream().forEach(el->System.out.println(el));
			if (!allListAnomaliePerla.isEmpty())
				allListAnomaliePerla.stream().forEach(el->System.out.println(el));
		} catch (Exception e){
			throw handleException(e);
		}
	}
/*
	public void generaXMLPerla2018(ActionContext context, List<V_incarichi_elenco_fpBulk> list) throws BusinessProcessException {
		try{
			Incarichi_archivio_xml_fpBulk archivioXmlPerlaFP = (Incarichi_archivio_xml_fpBulk)getModel();

			IncarichiEstrazioneFpComponentSession comp = (IncarichiEstrazioneFpComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiEstrazioneFpComponentSession", IncarichiEstrazioneFpComponentSession.class);
			IncarichiRepertorioComponentSession incRepComponent = (IncarichiRepertorioComponentSession)createComponentSession("CNRINCARICHI00_EJB_IncarichiRepertorioComponentSession", IncarichiRepertorioComponentSession.class);

			List<String> allListIncarichiOK=new ArrayList<String>();
			List<String> allListAnomaliePerla = new ArrayList<String>();

			for (Iterator<V_incarichi_elenco_fpBulk> iterator = list.iterator(); iterator.hasNext();) {
				V_incarichi_elenco_fpBulk incaricoElenco = iterator.next();
				incaricoElenco = comp.completaIncaricoElencoFP(context.getUserContext(), incaricoElenco);

				IncaricoConsulente elementNuovoConsulentePerla = this.generaNuovoConsulentePerla2018(context, incaricoElenco);
				if (elementNuovoConsulentePerla!=null) {
					Incarichi_repertorioBulk incarico = incaricoElenco.getIncaricoRepertorio();

					List<String> listaAnomalie = this.getAnomalie(incaricoElenco, elementNuovoConsulentePerla);
					if (listaAnomalie.isEmpty()) {
						if (incarico.getIdPerla()==null) {
							try {
								Long idPerla = Long.parseLong("10"); //PerlaIncarico.getInstance().inserisci(elementNuovoConsulentePerla);
								incRepComponent.aggiornaDatiPerla(context.getUserContext(), incarico, idPerla, null);

								allListIncarichiOK.add("Incarico: " + incaricoElenco.getIncaricoRepertorio().getEsercizio() + "/" + incaricoElenco.getIncaricoRepertorio().getPg_repertorio() +
										" - Inserimento possibile!");

								String xmlInserimento = PerlaIncarico.getInstance().getXmlInserimento(elementNuovoConsulentePerla);
								storeXmlPerla(xmlInserimento, incarico.getCMISFolder().getCMISPath());
							} catch(PerlaException perlaException){
								allListAnomaliePerla.add("Incarico: " + incaricoElenco.getIncaricoRepertorio().getEsercizio() + "/" + incaricoElenco.getIncaricoRepertorio().getPg_repertorio() +
										" - Inserimento - Errore WEB Perla: " + perlaException.getMessage());
							}
						} else {
							ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);

							elementNuovoConsulentePerla.setId(incarico.getIdPerla().longValue());

							//devo verificare che non è cambiato-.... se cambiato devo fare aggiornamento
							InputStream inputStreamLastInserimento = contrattiService.getChildren(contrattiService.getStorageObjectByPath(incarico.getCMISFolder().getCMISPath()).getKey())
									.stream()
									.filter(doc->doc.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_COMUNICAZIONE_PERLAPA.value()))
									.filter(doc->{
										try {
											return Optional.ofNullable(this.unmarshal(contrattiService.getResource(doc), it.gov.perlapa.incarichi.Comunicazione.class).getInserimentoincaricoconsulente()).isPresent();
										} catch (Exception e) {
										}
										return false;
									})
									.max(Comparator.comparing(doc->doc.getPropertyValue("cmis:creationDate")))
									.map(doc->contrattiService.getResource(doc)).orElse(null);

							InputStream inputStreamLastVariazione = contrattiService.getChildren(contrattiService.getStorageObjectByPath(incarico.getCMISFolder().getCMISPath()).getKey())
									.stream().filter(doc->doc.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_COMUNICAZIONE_PERLAPA.value()))
									.filter(doc->{
										try {
											return Optional.ofNullable(this.unmarshal(contrattiService.getResource(doc), it.gov.perlapa.incarichi.Comunicazione.class).getVariazioneincaricococonsulente()).isPresent();
										} catch (Exception e) {
										}
										return false;
									})
									.max(Comparator.comparing(doc->doc.getPropertyValue("cmis:creationDate")))
									.map(doc->contrattiService.getResource(doc)).orElse(null);

							//devo verificare che l'ultimo comando non sia la cancellazione.... vuol dire che si è verificato un problema durante un aggiornamento precedente e quindi procedo direttamente alla creazione dell'incarico
							InputStream inputStreamLastComando = contrattiService.getChildren(contrattiService.getStorageObjectByPath(incarico.getCMISFolder().getCMISPath()).getKey())
									.stream()
									.filter(doc->doc.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_COMUNICAZIONE_PERLAPA.value()))
									.max(Comparator.comparing(doc->doc.getPropertyValue("cmis:creationDate")))
									.map(doc->contrattiService.getResource(doc)).orElse(null);

							int tipoAzione = this.findAzione(elementNuovoConsulentePerla, inputStreamLastInserimento, inputStreamLastVariazione, inputStreamLastComando);

							try {
								if (tipoAzione == OggettoBulk.TO_BE_DELETED) {
									//PerlaIncarico.getInstance().elimina(elementNuovoConsulentePerla);
									String xmlCancellazione = PerlaIncarico.getInstance().getXmlCancellazione(elementNuovoConsulentePerla);
									storeXmlPerla(xmlCancellazione, incarico.getCMISFolder().getCMISPath());
								}
								if (tipoAzione == OggettoBulk.TO_BE_DELETED || tipoAzione == OggettoBulk.TO_BE_CREATED) {
									Long idPerla = Long.parseLong("10"); //PerlaIncarico.getInstance().inserisci(elementNuovoConsulentePerla);
									incRepComponent.aggiornaDatiPerla(context.getUserContext(), incarico, idPerla, null);
									String xmlInserimento = PerlaIncarico.getInstance().getXmlInserimento(elementNuovoConsulentePerla);
									storeXmlPerla(xmlInserimento, incarico.getCMISFolder().getCMISPath());
								} else if (tipoAzione == OggettoBulk.TO_BE_UPDATED) {
									//PerlaIncarico.getInstance().modifica(elementNuovoConsulentePerla);
									String xmlVariazione = PerlaIncarico.getInstance().getXmlVariazione(elementNuovoConsulentePerla);
									storeXmlPerla(xmlVariazione, incarico.getCMISFolder().getCMISPath());
								}
							} catch(PerlaException perlaException){
								allListAnomaliePerla.add("Incarico: " + incaricoElenco.getIncaricoRepertorio().getEsercizio() + "/" + incaricoElenco.getIncaricoRepertorio().getPg_repertorio() +
										" - Inserimento - Errore WEB Perla: " + perlaException.getMessage());
							}
						}
					} else
						allListAnomaliePerla.addAll(listaAnomalie);
				}else
					allListAnomaliePerla.add("Incarico: " + incaricoElenco.getIncaricoRepertorio().getEsercizio() + "/" + incaricoElenco.getIncaricoRepertorio().getPg_repertorio() +
							" - Errore generico - Contattare il servizio assistenza.");
			}

			JAXBContext jcNew = JAXBContext.newInstance(it.gov.perlapa.incarichi.ObjectFactory.class);
			List<String> listXMLPerla=new ArrayList<String>();
			List<File> listFileXMLPerla=new ArrayList<File>();
			List<File> listFileAnomalie=new ArrayList<File>();

			if (!allListIncarichiOK.isEmpty())
				allListIncarichiOK.stream().forEach(el->System.out.println(el));
			if (!allListAnomaliePerla.isEmpty()) {
				allListAnomaliePerla.stream().forEach(el->System.out.println(el));
				//SendMail.sendMail("Errori Perla", allListAnomaliePerla.toString(), Arrays.asList("raffaele.pagano@cnr.it"));
			}
			Hashtable<String, List<File>> hashFileIncaricoXML=new Hashtable<String, List<File>>();


			if (!listXMLPerla.isEmpty() || !listFileAnomalie.isEmpty()) {
				hashFileIncaricoXML.put("Incarichi Nuovi", listFileXMLPerla);
				hashFileIncaricoXML.put("Anomalie Incarichi Nuovi", listFileAnomalie);

				String fileName = "EstrazioneIncarichiPerla.zip";
				File file = creaFileZip(hashFileIncaricoXML, fileName);
				archivioXmlPerlaFP.setPathFileZip(file.getPath());
			}
			cancellaListaFile(listFileXMLPerla, listFileAnomalie);
		} catch (Exception e){
			throw handleException(e);
		}
	}

	public List<String> getAnomalie(V_incarichi_elenco_fpBulk v_incarico, IncaricoConsulente consulentePerla) {
		List<String> listAnomalie = new ArrayList<String>();
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();
		if (!Optional.ofNullable(consulentePerla.getDichiarante().getCodiceAooIpa()).isPresent() && !Optional.ofNullable(consulentePerla.getDichiarante().getCodiceUoIpa()).isPresent())
			listAnomalie.add("Incarico: "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - Manca sia il Codice AOO Ipa sul CDS "+incarico.getCd_cds()+" che il Codice Uo Ipa sulla Uo "+incarico.getCd_unita_organizzativa()+".");
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getPercettorePf().getCodiceFiscale() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il codice fiscale del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getCognome() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il cognome del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getNome() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il nome del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getGenere() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il sesso del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getDataNascita() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca la data di nascita del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getEstero().equals(YesNoEnum.N) &&
					(consulentePerla.getPercettorePf().getComuneNascita() == null || consulentePerla.getPercettorePf().getComuneNascita().equals("*")))
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il codice catastale del comune di nascita del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
		}
		if (Optional.ofNullable(consulentePerla.getPercettorePg()).isPresent()) {
			if (consulentePerla.getPercettorePg().getEstero().equals(YesNoEnum.N) && consulentePerla.getPercettorePg().getCodiceFiscale() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il codice fiscale del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePg().getDenominazione() == null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca il nominativo del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
		}
		if (consulentePerla.getDatiIncarico().getOggettoIncarico()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'oggetto dell'incarico.");
		if (consulentePerla.getDatiIncarico().getDataConferimento()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca la data di conferimento dell'incarico.");
		if (consulentePerla.getDatiIncarico().getDataInizio()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca la data di inizio dell'incarico.");
		if (consulentePerla.getDatiIncarico().getEstremiAttoConferimento()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Mancano gli estremi dell'atto di conferimento dell'incarico.");
		if (consulentePerla.getDatiIncarico().getTipoRapporto()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione del tipo di rapporto dell'incarico.");
		if (consulentePerla.getDatiIncarico().getNaturaConferimento()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione della natura conferimento dell'incarico.");
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getDatiIncarico().getAttestazioneVerificaInsussistenza()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione della presenza dell'attestazione verifica insussistenza.");
		}
		if (consulentePerla.getDatiIncarico().getRiferimentoRegolamento()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Mancano l'indicazione se l'incarico è stato assegnato con riferimento ad un regolamento dell'amministrazione.");

		if (consulentePerla.getDatiEconomici().getTipoCompenso()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione della tipologia di compenso.");
		if (consulentePerla.getDatiEconomici().getCompenso()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione dell'importo del compenso.");
		if (consulentePerla.getDatiEconomici().getAmmontareErogato()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione dell'importo del compenso erogato.");
		if (consulentePerla.getDatiEconomici().getIncaricoSaldato()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione se il compenso è stato saldato.");
		if (consulentePerla.getDatiEconomici().getIncaricoSaldato().equals(YesNoEnum.Y) && consulentePerla.getDatiEconomici().getDataFine()==null)
			listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione della data di fine dell'incarico saldato.");
		if (consulentePerla.getDatiEconomici().getTipoCompenso().equals(TipoCompensoEnum.GRATUITO)) {
			if (consulentePerla.getDatiEconomici().getAmmontareErogato().compareTo(BigDecimal.ZERO) > 0)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - L'incarico gratuito deve risultare di importo zero.");
			if (!consulentePerla.getDatiEconomici().getIncaricoSaldato().equals(YesNoEnum.N))
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - L'incarico gratuito deve risultare non saldato.");
			if (!consulentePerla.getDatiEconomici().getComponentiVariabilCompenso().equals(YesNoEnum.N))
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - L'incarico gratuito non deve avere componenti variabili del compenso.");
		}

		if (consulentePerla.getRiferimentoNormativo()!=null) {
			if (consulentePerla.getRiferimentoNormativo().getRiferimento()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getNumero()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione del numero del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getArticolo()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione dell'articolo del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getComma()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione del comma del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getData()==null)
				listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'indicazione della data del riferimento normativo.");
		}
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getAllegati().getCurriculumVitae()==null) {
				//TODO DA ELIMINARE
//				if (incarico.getCurriculumVincitore()==null || incarico.getCurriculumVincitore().getCms_node_ref()==null)
//					listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'allegato di tipo Curriculum Vitae.");
			}
			if (consulentePerla.getAllegati().getDichiarazioneIncarichi()==null) {
				//TODO DA ELIMINARE
//				if (incarico.getIncarichi_repertorio_rappColl() == null || incarico.getIncarichi_repertorio_rappColl().isEmpty())
//					listAnomalie.add("Incarico: " + incarico.getEsercizio() + "/" + incarico.getPg_repertorio() + " - Manca l'allegato di tipo Dichiarazione Altri Incarichi.");
			}
		}
		return listAnomalie;
	}

	private void storeXmlPerla(String xmlPerla, String cmisPath) throws IOException {
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);

		//Rimuovo username e password
		String username = StringUtils.substringBetween(xmlPerla, "<username>", "</username>");
		String password = StringUtils.substringBetween(xmlPerla, "<password>", "</password>");
		xmlPerla = xmlPerla.replace(username,"");
		xmlPerla = xmlPerla.replace(password,"");

		File tempFile = File.createTempFile("XmlPerla_"+ LocalDate.now(),".xml");
		FileWriter fileWriter = new FileWriter(tempFile, true);
		fileWriter.write(xmlPerla);
		fileWriter.close();

		StorageFile storageFile = new StorageFile(tempFile, tempFile.getName());

		contrattiService.storeSimpleDocument(
				storageFile,
				storageFile.getInputStream(),
				storageFile.getContentType(),
				storageFile.getFileName(),
				cmisPath,
				StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_COMUNICAZIONE_PERLAPA.value(),
				true);
	}

	private int findAzione(IncaricoConsulente newIncaricoConsulente, InputStream inputStreamLastInserimento, InputStream inputStreamLastVariazione, InputStream inputStreamLastComando) throws Exception{
		it.gov.perlapa.incarichi.Comunicazione lastComunicazione = this.unmarshal(inputStreamLastComando, it.gov.perlapa.incarichi.Comunicazione.class);
		if (Optional.ofNullable(lastComunicazione.getCancellazioneincarico()).isPresent())
			return OggettoBulk.TO_BE_CREATED;

		String xmlLastInserimento = this.refreshXMLIncarico(IOUtils.toString(inputStreamLastInserimento));
		String xmlLastVariazione = null;
		if (inputStreamLastVariazione!=null)
			xmlLastVariazione = this.refreshXMLIncarico(IOUtils.toString(inputStreamLastVariazione));

		String xmlNewInserimento = this.refreshXMLIncarico(PerlaIncarico.getInstance().getXmlInserimento(newIncaricoConsulente));

		String oldPercettore = this.getTag(xmlLastInserimento, "percettore");
		String newPercettore = this.getTag(xmlNewInserimento, "percettore");

		if (!oldPercettore.equals(newPercettore))
			return OggettoBulk.TO_BE_DELETED;

		if (!Optional.ofNullable(xmlLastVariazione).isPresent()) {
			String oldIncarico = this.getTag(xmlLastInserimento, "datiincarico");
			String newIncarico = this.getTag(xmlNewInserimento, "datiincarico");

			if (!oldIncarico.equals(newIncarico))
				return OggettoBulk.TO_BE_UPDATED;

			String oldRifnorma = this.getTag(xmlLastInserimento, "riferimentonormativo");
			String newRifnorma = this.getTag(xmlNewInserimento, "riferimentonormativo");

			if (Optional.ofNullable(oldRifnorma).map(el->!el.equals(newRifnorma)).orElse(Optional.ofNullable(newRifnorma).isPresent()))
				return OggettoBulk.TO_BE_UPDATED;

			String oldAllegati = this.getTag(xmlLastInserimento, "allegati");
			String newAllegati = this.getTag(xmlNewInserimento, "allegati");

			if (Optional.ofNullable(oldAllegati).map(el->!el.equals(newAllegati)).orElse(Optional.ofNullable(newAllegati).isPresent()))
				return OggettoBulk.TO_BE_UPDATED;

		} else {
			String xmlNewVariazione = StringUtils.substringBetween(PerlaIncarico.getInstance().getXmlVariazione(newIncaricoConsulente), "<incarico>", "</incarico>");

			String oldIncarico = this.getTag(xmlLastVariazione, "datiincarico");
			String newIncarico = this.getTag(xmlNewVariazione, "datiincarico");

			if (!oldIncarico.equals(newIncarico))
				return OggettoBulk.TO_BE_UPDATED;

			String oldRifnorma = this.getTag(xmlLastVariazione, "riferimentonormativo");
			String newRifnorma = this.getTag(xmlNewVariazione, "riferimentonormativo");

			if (Optional.ofNullable(oldRifnorma).map(el->!el.equals(newRifnorma)).orElse(Optional.ofNullable(newRifnorma).isPresent()))
				return OggettoBulk.TO_BE_UPDATED;

			String oldAllegati = this.getTag(xmlLastVariazione, "allegati");
			String newAllegati = this.getTag(xmlNewVariazione, "allegati");

			if (Optional.ofNullable(oldAllegati).map(el->!el.equals(newAllegati)).orElse(Optional.ofNullable(newAllegati).isPresent()))
				return OggettoBulk.TO_BE_UPDATED;
		}
		return OggettoBulk.UNDEFINED;
	}

	private String marshal(Object obj) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(obj, sw);
		return sw.toString();
	}

	private <T> T unmarshal(InputStream inputStream, Class<T> objClass) throws Exception {
		return this.unmarshal(StringUtils.substringBetween(IOUtils.toString(inputStream), "<incarico>", "</incarico>"), objClass);
	}

	private <T> T unmarshal(String xml, Class<T> objClass) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(objClass);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (T)jaxbUnmarshaller.unmarshal(new InputSource(new StringReader(xml)));
	}

	//Questo metodo parte da un xml ed effettua un refresh dello stesso per evitare che eventuali modifiche manuali del file possanno compromettere i confronti tra le versioni
	//Lo ritrasforma in oggetto e quindi rieffettua il marshal.
	private String refreshXMLIncarico(String xml) throws Exception {
		it.gov.perlapa.incarichi.Comunicazione comunicazione = this.unmarshal(StringUtils.substringBetween(xml, "<incarico>", "</incarico>"), it.gov.perlapa.incarichi.Comunicazione.class);
		return this.marshal(comunicazione);
	}

	//Questo metodo ritorna la parte string compresa tra 2 tag
	private String getTag(String xml, String tagName) {
		return StringUtils.substringBetween(xml, "<"+tagName, "</"+tagName+">");
	}
*/
}