package it.cnr.contab.incarichi00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpHome;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpHome;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.apache.commons.io.IOUtils;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncarichiEstrazioneFpComponent extends CRUDComponent {
	public V_incarichi_elenco_fpBulk completaIncaricoElencoFP(UserContext userContext, V_incarichi_elenco_fpBulk bulk) throws ComponentException{
		try {
			Incarichi_repertorioBulk incaricoRepertorio = (Incarichi_repertorioBulk)getHome(userContext, Incarichi_repertorioBulk.class).findByPrimaryKey(new Incarichi_repertorioBulk(bulk.getEsercizio(), bulk.getPg_repertorio()));
			Incarichi_proceduraBulk incaricoProcedura = (Incarichi_proceduraBulk)getHome(userContext, Incarichi_proceduraBulk.class).findByPrimaryKey(new Incarichi_proceduraBulk(bulk.getEsercizio_procedura(), bulk.getPg_procedura()));

			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
			incaricoRepertorio.setV_terzo(home.loadVTerzo(userContext,Tipo_rapportoBulk.ALTRO, incaricoRepertorio.getCd_terzo(), incaricoRepertorio.getDt_inizio_validita(), incaricoRepertorio.getDt_inizio_validita()));
			
			incaricoRepertorio.setIncarichi_procedura(incaricoProcedura);
			bulk.setIncaricoRepertorio(incaricoRepertorio);
			
			return bulk;
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		}
	}

	public void aggiornaIncarichiComunicatiFP(UserContext userContext, List<Incarichi_comunicati_fpBulk> list) throws ComponentException{
		for (Iterator<Incarichi_comunicati_fpBulk> iterator = list.iterator(); iterator.hasNext();)
			aggiornaIncarichiComunicatiFP(userContext, iterator.next());
	}

	public void aggiornaIncarichiComunicatiFP(UserContext userContext, Incarichi_comunicati_fpBulk bulk) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fp_detHome incFpDetHome = (Incarichi_comunicati_fp_detHome)getTempHome(userContext, Incarichi_comunicati_fp_detBulk.class);
			Incarichi_comunicati_fpBulk incaricoPrincFpDB = null;
			
			incaricoPrincFpDB = (Incarichi_comunicati_fpBulk)incFpHome.findIncaricoComunicatoAggFP(bulk);

			//recupero l'ultimo progressivo necessario per verificare che l'ultimo record inserito non sia un record identico
			Long maxProg = (Long)incFpHome.findMax(bulk,"pg_record");

			//Recupero il record principale
			if (incaricoPrincFpDB!=null){
				if (!incaricoPrincFpDB.getId_incarico().equals(bulk.getId_incarico()))
					return;

				//è stato già comunicato un nuovo incarico......devo aggiornarlo
				incaricoPrincFpDB.updateFrom(userContext, bulk);
				incaricoPrincFpDB.setToBeUpdated();
				updateBulk(userContext,incaricoPrincFpDB);

				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();

					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						Incarichi_comunicati_fp_detBulk pagamPrincDB =  (Incarichi_comunicati_fp_detBulk)incFpDetHome.findByPrimaryKey(new Incarichi_comunicati_fp_detBulk(incaricoPrincFpDB.getEsercizio_repertorio(), incaricoPrincFpDB.getPg_repertorio(), Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO, incaricoPrincFpDB.getPg_record(), pagam.getAnno_pag(), pagam.getSemestre_pag())); 
						
						if (pagamPrincDB!=null){
							if (pagam.getImporto_pag()!=null) pagamPrincDB.setImporto_pag(pagam.getImporto_pag());
							pagamPrincDB.setToBeUpdated();
							pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
							updateBulk(userContext,pagamPrincDB);
						} else {
							pagamPrincDB = (Incarichi_comunicati_fp_detBulk)pagam.clone();
							pagamPrincDB.setIncarichi_comunicati_fp(incaricoPrincFpDB);
							pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
							pagamPrincDB.setToBeCreated();
							insertBulk(userContext,pagamPrincDB);
						}
					}
				}
			} else {
				if (maxProg!=null) {
					Incarichi_comunicati_fpBulk otherIncaricoPrincFp = (Incarichi_comunicati_fpBulk)incFpHome.findByPrimaryKey(new Incarichi_comunicati_fpBulk(bulk.getEsercizio_repertorio(), bulk.getPg_repertorio(), bulk.getTipo_record(), Incarichi_comunicati_fpBulk.PG_RECORD_PRINCIPALE));
					if (!otherIncaricoPrincFp.getId_incarico().equals(bulk.getId_incarico()))
						return;
				}
				incaricoPrincFpDB = (Incarichi_comunicati_fpBulk)bulk.clone();
				incaricoPrincFpDB.setEsercizio_repertorio(bulk.getEsercizio_repertorio());
				incaricoPrincFpDB.setPg_repertorio(bulk.getPg_repertorio());
				incaricoPrincFpDB.setTipo_record(Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
				incaricoPrincFpDB.setPg_record(Incarichi_comunicati_fpBulk.PG_RECORD_PRINCIPALE);
				incaricoPrincFpDB.setToBeCreated();
				insertBulk(userContext,incaricoPrincFpDB);
				
				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();
					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						Incarichi_comunicati_fp_detBulk pagamPrincDB = (Incarichi_comunicati_fp_detBulk)pagam.clone();
						pagamPrincDB.setIncarichi_comunicati_fp(incaricoPrincFpDB);
						pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
						pagamPrincDB.setToBeCreated();
						insertBulk(userContext, pagamPrincDB);
					}
				}
			}

			Incarichi_comunicati_fpBulk incFpLast = null;
			if (maxProg!=null && maxProg>1)
				incFpLast = (Incarichi_comunicati_fpBulk)incFpHome.findByPrimaryKey(new Incarichi_comunicati_fpBulk(bulk.getEsercizio_repertorio(), bulk.getPg_repertorio(), bulk.getTipo_record(), maxProg));

			if (incFpLast==null || !incFpLast.similar(bulk)){
				if (maxProg==null) 
					bulk.setPg_record(Incarichi_comunicati_fpBulk.PG_RECORD_PRINCIPALE+1);
				bulk.setUser(CNRUserContext.getUser(userContext));
				bulk.setToBeCreated();
				insertBulk(userContext,bulk);
				
				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();
					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						pagam.setUser(CNRUserContext.getUser(userContext));
						pagam.setToBeCreated();
						insertBulk(userContext, pagam);
					}
				}
			}
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(UserContext userContext, V_incarichi_elenco_fpBulk incaricoElenco) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fpBulk bulk = new Incarichi_comunicati_fpBulk();
			bulk.setIncarichi_repertorio(incaricoElenco.getIncaricoRepertorio());
			Incarichi_comunicati_fpBulk incaricoComunicatoAgg = incFpHome.findIncaricoComunicatoAggFP(bulk);
			if (incaricoComunicatoAgg!=null)
				incaricoComunicatoAgg.setIncarichi_comunicati_fp_detColl(new BulkList(incFpHome.findIncarichiComunicatiFpDet(incaricoComunicatoAgg)));
			return incaricoComunicatoAgg;
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(UserContext userContext, Incarichi_comunicati_fpBulk incaricoComunicato) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fpBulk incaricoComunicatoAgg = incFpHome.findIncaricoComunicatoAggFP(incaricoComunicato);
			if (incaricoComunicatoAgg!=null)
				incaricoComunicatoAgg.setIncarichi_comunicati_fp_detColl(new BulkList(incFpHome.findIncarichiComunicatiFpDet(incaricoComunicatoAgg)));
			return incaricoComunicatoAgg;
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public List<Incarichi_comunicati_fpBulk> getIncarichiComunicatiEliminatiFP(UserContext userContext, Integer esercizio, Integer semestre) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			return incFpHome.findIncarichiComunicatiEliminatiFP(esercizio, semestre);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	@Override
	protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		oggettobulk = super.eseguiCreaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk) {
			Incarichi_archivio_xml_fpBulk archivio = (Incarichi_archivio_xml_fpBulk)oggettobulk;
			if (archivio.getFl_merge_perla().equals(Boolean.FALSE))
				archiviaAllegati(usercontext,archivio);
			else
				mergePerla(usercontext, archivio);
		}
		return oggettobulk;
	}
	
	private void archiviaAllegati(UserContext userContext, Incarichi_archivio_xml_fpBulk incaricoArchivioXmlFp) throws ComponentException{
		Incarichi_archivio_xml_fpHome archiveHome = (Incarichi_archivio_xml_fpHome)getHome(userContext,Incarichi_archivio_xml_fpBulk.class);
		
		if (!(incaricoArchivioXmlFp.getFile_inv() == null || incaricoArchivioXmlFp.getFile_inv().getName().equals("") ||
			  incaricoArchivioXmlFp.getFile_ric() == null || incaricoArchivioXmlFp.getFile_ric().getName().equals(""))) {
			incaricoArchivioXmlFp.setToBeUpdated();

			try{
				List<Incarichi_comunicati_fpBulk> listIncarichiComunicati = new ArrayList<Incarichi_comunicati_fpBulk>(); 
				if (!incaricoArchivioXmlFp.getFl_perla()) {
					JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");

					Comunicazione comunicazione = (Comunicazione)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_inv());
					EsitoComunicazione esitoComunicazione = (EsitoComunicazione)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_ric());
		
					for (Iterator iterator = comunicazione.getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
						Comunicazione.Consulenti.NuovoIncarico nuovoIncarico = (Comunicazione.Consulenti.NuovoIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, nuovoIncarico);
						listIncarichiComunicati.add(incaricoComunicato);
					}
					for (Iterator iterator = comunicazione.getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
						Comunicazione.Consulenti.ModificaIncarico modificaIncarico = (Comunicazione.Consulenti.ModificaIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, modificaIncarico);
						listIncarichiComunicati.add(incaricoComunicato);
					}
					for (Iterator iterator = comunicazione.getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
						Comunicazione.Consulenti.CancellaIncarico cancellaIncarico = (Comunicazione.Consulenti.CancellaIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, cancellaIncarico);
						listIncarichiComunicati.add(incaricoComunicato);
					}
					for (Iterator iterator = esitoComunicazione.getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
						EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncarico = (EsitoComunicazione.Consulenti.NuovoIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, nuovoIncarico);
						listIncarichiComunicati.add(incaricoRicevuto);
					}
					for (Iterator iterator = esitoComunicazione.getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
						EsitoComunicazione.Consulenti.ModificaIncarico modificaIncarico = (EsitoComunicazione.Consulenti.ModificaIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, modificaIncarico);
						listIncarichiComunicati.add(incaricoRicevuto);
					}
					for (Iterator iterator = esitoComunicazione.getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
						EsitoComunicazione.Consulenti.CancellaIncarico cancellaIncarico = (EsitoComunicazione.Consulenti.CancellaIncarico) iterator.next();
						Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, cancellaIncarico);
						listIncarichiComunicati.add(incaricoRicevuto);
					}
				} else { //sistema PERLA
					JAXBContext jc = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory.class);

					JAXBElement<?> JAXBcomunicazione;
					try{
						JAXBcomunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_inv()); 
					} catch (ClassCastException e) {
						throw new ValidationException("File inviato alla funzione pubblica non di tipo corretto. "+e.getMessage());
					} catch (JAXBException e) {
						throw new ValidationException("Errore generico in fase di caricamento del file inviato alla funzione pubblica. "+e.getMessage());
					}
						
					JAXBElement<?> JAXBesitoComunicazione;
					try{
						JAXBesitoComunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_ric()); 
					} catch (ClassCastException e) {
						throw new ValidationException("File ricevuto dalla funzione pubblica non di tipo corretto. "+e.getMessage());
					} catch (JAXBException e) {
						throw new ValidationException("Errore generico in fase di caricamento del file ricevuto dalla funzione pubblica. "+e.getMessage());
					}

					if (JAXBcomunicazione==null)
						throw new ValidationException("Errore nel caricamento del file inviato alla Funzione Pubblica.");
					else if (JAXBesitoComunicazione==null)
						throw new ValidationException("Errore nel caricamento del file ricevuto dalla Funzione Pubblica.");

					if (JAXBcomunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType.class)) {
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType comunicazione = ((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_inv())).getValue();
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType esitoComunicazione = ((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_ric())).getValue();
				
						for (Iterator iterator = comunicazione.getInserimentoIncarichi().getNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
							it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType nuovoConsulente = (it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType) iterator.next();
							Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, comunicazione, nuovoConsulente);
							listIncarichiComunicati.add(incaricoComunicato);
						}

						for (Iterator iterator = esitoComunicazione.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
							it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType nuovoConsulenteEsito = (it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType) iterator.next();
							for (Iterator iterator2 = comunicazione.getInserimentoIncarichi().getNuoviIncarichi().getConsulente().iterator(); iterator2.hasNext();) {
								it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType nuovoConsulente = (it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType) iterator2.next();
								if (nuovoConsulente.getIdMittente().equals(nuovoConsulenteEsito.getIdMittente())) {
									Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, comunicazione, nuovoConsulente, nuovoConsulenteEsito);
									listIncarichiComunicati.add(incaricoRicevuto);
								}
							}
						}
					} else if (JAXBcomunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType.class)) {
						it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType comunicazione = ((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_inv())).getValue();
						it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType esitoComunicazione = ((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_ric())).getValue();

						for (Iterator iterator = comunicazione.getVariazioneIncarichi().getModificaIncarichi().getConsulente().iterator(); iterator.hasNext();) {
							it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente = (it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType) iterator.next();
							Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, comunicazione, modificaConsulente);
							listIncarichiComunicati.add(incaricoComunicato);
						}

						for (Iterator iterator = esitoComunicazione.getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().iterator(); iterator.hasNext();) {
							it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType modificaConsulenteEsito = (it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType) iterator.next();
							for (Iterator iterator2 = comunicazione.getVariazioneIncarichi().getModificaIncarichi().getConsulente().iterator(); iterator2.hasNext();) {
								it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType modificaConsulente = (it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType) iterator2.next();
								if (modificaConsulente.getIdMittente().equals(modificaConsulenteEsito.getIdMittente())) {
									Incarichi_comunicati_fpBulk incaricoModificato = Incarichi_comunicati_fpBulk.copyFrom(userContext, comunicazione, modificaConsulente, modificaConsulenteEsito);
									listIncarichiComunicati.add(incaricoModificato);
								}
							}
						}
					} 
				}
				aggiornaIncarichiComunicatiFP(userContext, listIncarichiComunicati);
			} catch (Exception e){
				throw new ComponentException(e);
			}
		}
	}
	
	private void mergePerla(UserContext userContext, Incarichi_archivio_xml_fpBulk incaricoArchivioXmlFp) throws ComponentException{
		Incarichi_archivio_xml_fpHome archiveHome = (Incarichi_archivio_xml_fpHome)getHome(userContext,Incarichi_archivio_xml_fpBulk.class);
		
		if (!(incaricoArchivioXmlFp.getFile_ric() == null || incaricoArchivioXmlFp.getFile_ric().getName().equals(""))) {
			incaricoArchivioXmlFp.setToBeUpdated();

			
			List<Incarichi_comunicati_fpBulk> listIncarichiComunicati = new ArrayList<Incarichi_comunicati_fpBulk>(); 
			List<String[]> righeScartate = new ArrayList<String[]>(); 
			
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yy");
			
			try
            {
				//read csv file
				Reader reader = new FileReader(incaricoArchivioXmlFp.getFile_ric());
				CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
				List<String[]> csvAllLine = csvParser.readAll();

            	int lineNumber = 0;
                  
            	Incarichi_comunicati_fpBulk firstIncaricoComunicato = null;
            	Incarichi_comunicati_fpBulk incaricoComunicato = null;
            	
            	//read comma separated file line by line
				for (String[] csvLine : csvAllLine) {
            		lineNumber++;
            		//salto la prima riga che è l'intestazione
            		if (lineNumber>1 && !csvLine[1].replace(" ","").equals("") && (csvLine.length==20 || csvLine.length==21)){
            			System.out.println("lineNumber: "+lineNumber);
	            		incaricoComunicato = new Incarichi_comunicati_fpBulk();
	            		incaricoComunicato.setTipo_record(Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
	            		incaricoComunicato.setToBeCreated();
	            		
	            		String nominativo=null, tipoImporto=null;
	            		BigDecimal importoPagamento=null;
	            		int annoPagamento=0, semestrePagamento=0; 
	
            			//display csv values
           				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)findByPrimaryKey(userContext, new Unita_organizzativaBulk(csvLine[0].substring(0, 3)));
           				incaricoComunicato.setCodice_ente(uo.getId_funzione_pubblica());
           				
           				incaricoComunicato.setId_incarico(csvLine[1]);
           				
           				incaricoComunicato.setAnno_riferimento(new Integer(csvLine[2]));

           				if (csvLine[3].toUpperCase().equals("PRIMO SEMESTRE"))
           					incaricoComunicato.setSemestre_riferimento(1);
           				else if (csvLine[3].toUpperCase().equals("SECONDO SEMESTRE"))
           					incaricoComunicato.setSemestre_riferimento(2);
	            		
           				incaricoComunicato.setCodice_fiscale_partita_iva(csvLine[4]);
           				
           				nominativo = csvLine[6];
           				
           				incaricoComunicato.setTi_sesso(csvLine[7]);

           				incaricoComunicato.setDescrizione_incarico(csvLine[8]);
	            		
           				if (csvLine[8].indexOf("(")==0) {
	            			int esercizio_repertorio = new Integer(csvLine[8].substring(1,5)); 
		            		Long pg_repertorio = new Long(csvLine[8].substring(6,csvLine[8].indexOf(")")));
		            		Incarichi_repertorioBulk incaricoRepertorio = (Incarichi_repertorioBulk)findByPrimaryKey(userContext, new Incarichi_repertorioBulk(esercizio_repertorio, pg_repertorio));
		            		if (incaricoRepertorio!=null){ 
			            		V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
			            		incaricoRepertorio.setV_terzo(home.loadVTerzo(userContext,Tipo_rapportoBulk.ALTRO, incaricoRepertorio.getCd_terzo(), incaricoRepertorio.getDt_inizio_validita(), incaricoRepertorio.getDt_inizio_validita()));
			            				
			            		incaricoComunicato.setIncarichi_repertorio(incaricoRepertorio);
		            		}
           				}

           				if (csvLine[9].toUpperCase().equals("DI NATURA DISCREZIONALE"))
           					incaricoComunicato.setModalita_acquisizione("10");
           				else if (csvLine[9].toUpperCase().equals("PREVISTO DA NORMA DI LEGGE"))
           					incaricoComunicato.setModalita_acquisizione("1");

           				if (csvLine[10].toUpperCase().equals("COLLABORAZIONE COORDINATA E CONTINUATIVA"))
           					incaricoComunicato.setTipo_rapporto("2");
           				else if (csvLine[10].toUpperCase().equals("PRESTAZIONE OCCASIONALE"))
           					incaricoComunicato.setTipo_rapporto("1");

	               		if (csvLine[11].toUpperCase().equals("ALTRE ATTIVITA' PROFESSIONALI ED IMPRENDITORIALI"))
	               			incaricoComunicato.setAttivita_economica("74");
	            		else if (csvLine[11].toUpperCase().equals("ATTIVITA' DI STUDIO E RICERCA"))
		            		incaricoComunicato.setAttivita_economica("963");
		            	else if (csvLine[11].toUpperCase().equals("CONSULENZA TECNICA"))
		            		incaricoComunicato.setAttivita_economica("956");

	               		//dal secondo semestre 2016 i file prodotti dalla FP hanno questo campo non valorizzato
	               		//o valorizzato solo con uno spazio. Per permettere l'inserimento testo il campo
	               		if (!csvLine[12].isEmpty() && csvLine[12].length()>1)
	               			incaricoComunicato.setDt_inizio(new Timestamp(formatter.parse(csvLine[12]).getTime()));

           				incaricoComunicato.setDt_fine(new Timestamp(formatter.parse(csvLine[13]).getTime()));

           				if (csvLine[14].toUpperCase().equals("PREVISTO"))
           					tipoImporto = "1";
           				else if (csvLine[15].toUpperCase().equals("PRESUNTO"))
           					tipoImporto = "2";

	            		incaricoComunicato.setImporto_previsto(new BigDecimal(csvLine[15].replace(".", "").replace(",", ".")));

	            		if (!csvLine[16].replace(" ","").equals(""))
            				importoPagamento = new BigDecimal(csvLine[16].replace(".", "").replace(",", "."));

            			if (!csvLine[17].replace(" ","").equals(""))
            				annoPagamento = new Integer(csvLine[17]);
            			
            			if (!csvLine[18].replace(" ","").equals("")) {
            				if (csvLine[18].toUpperCase().equals("PRIMO SEMESTRE"))
            					semestrePagamento = 1;
            				else if (csvLine[18].toUpperCase().equals("SECONDO SEMESTRE"))
            					semestrePagamento = 2;
            			}
            			
            			if (csvLine[19].toUpperCase().equals("SI"))
           					incaricoComunicato.setFl_saldo(Boolean.TRUE);	
           				else if (csvLine[19].toUpperCase().equals("NO"))
           					incaricoComunicato.setFl_saldo(Boolean.FALSE);	
	            		
	            		if (incaricoComunicato.getIncarichi_repertorio()!=null) {
	        				if (firstIncaricoComunicato!=null && firstIncaricoComunicato.getId_incarico().equals(incaricoComunicato.getId_incarico())) {
	                			if (importoPagamento!=null && annoPagamento!=0 && semestrePagamento!=0) {
	                				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = new Incarichi_comunicati_fp_detBulk();
	                				incaricoComunicatoDet.setIncarichi_comunicati_fp(firstIncaricoComunicato);
	
	                				incaricoComunicatoDet.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
	                				incaricoComunicatoDet.setAnno_pag(annoPagamento);
	                				incaricoComunicatoDet.setSemestre_pag(semestrePagamento);
	                				incaricoComunicatoDet.setImporto_pag(importoPagamento);
	                				
	                				incaricoComunicatoDet.setToBeCreated();
	                				firstIncaricoComunicato.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);            				
	                			}
	        				} else {
		            			if (nominativo!=null) {
		            				try{
		            					String cognome, nome;
		            					Calendar dtNascita;
		            					
		            					if (incaricoComunicato.getIncarichi_repertorio().getCd_terzo().equals(107796)) {
		            						cognome = "RAMONDELLI";
		            						nome = "GIUSEPPE";
		            						dtNascita = new GregorianCalendar(1943,Calendar.DECEMBER,22);
		            					} else {
		            						cognome = incaricoComunicato.getIncarichi_repertorio().getTerzo().getAnagrafico().getCognome();
		            						nome = incaricoComunicato.getIncarichi_repertorio().getTerzo().getAnagrafico().getNome();
		            						dtNascita = new GregorianCalendar(1943,Calendar.DECEMBER,22);
		            					}
		            					if (cognome.toUpperCase().concat(" "+nome.toUpperCase()).equals(nominativo.toUpperCase())) {
			            					incaricoComunicato.setCognome(cognome);
			            					incaricoComunicato.setNome(nome);
			            					incaricoComunicato.setData_nascita(Timestamp.from(dtNascita.toInstant()));
			            				}
		            				} catch (NullPointerException e) {
		            					throw e;
		            				}
		            			}
		            			if (importoPagamento!=null && annoPagamento!=0 && semestrePagamento!=0) {
		            				Incarichi_comunicati_fp_detBulk incaricoComunicatoDet = new Incarichi_comunicati_fp_detBulk();
		            				incaricoComunicatoDet.setIncarichi_comunicati_fp(incaricoComunicato);
		
		            				incaricoComunicatoDet.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
		            				incaricoComunicatoDet.setAnno_pag(annoPagamento);
		            				incaricoComunicatoDet.setSemestre_pag(semestrePagamento);
		            				incaricoComunicatoDet.setImporto_pag(importoPagamento);
		            				
		            				incaricoComunicatoDet.setToBeCreated();
		            				incaricoComunicato.addToIncarichi_comunicati_fp_detColl(incaricoComunicatoDet);            				
		            			}
		                		listIncarichiComunicati.add(incaricoComunicato);
		                		firstIncaricoComunicato = incaricoComunicato;
	        				}
	            		} else {
	                        righeScartate.add(csvLine);                  
	            		}
            		}
				}
            	aggiornaIncarichiComunicatiFP(userContext, listIncarichiComunicati);
            }
            catch(Exception e)
            {
                System.out.println("Exception while reading csv file: " + e);                  
    			throw new ComponentException(e);
            }
    		for (Iterator<String[]> iterator = righeScartate.iterator(); iterator.hasNext();) {
                System.out.println("Riga Esclusa: " + (String[]) iterator.next());
    		}
		}
	}

	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk) {
			try{
				Incarichi_archivio_xml_fpBulk allegato = (Incarichi_archivio_xml_fpBulk)oggettobulk;
				
				if (!allegato.getFl_perla()) {
					JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
		
		
					if (allegato.getNome_file_inv()!=null) {
						File fileInv = getFile(usercontext, allegato, "BDATA_INV", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_inv());
						allegato.setFile_inv(fileInv);
						allegato.setComunicazione((Comunicazione)jc.createUnmarshaller().unmarshal(fileInv));
					}
					if (allegato.getNome_file_ric()!=null) {
						File fileRic = getFile(usercontext, allegato, "BDATA_RIC", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_ric());
						allegato.setFile_ric(fileRic);
						allegato.setEsitoComunicazione((EsitoComunicazione)jc.createUnmarshaller().unmarshal(fileRic));
					}
				} else {
					JAXBContext jc = JAXBContext.newInstance(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ObjectFactory.class, it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ObjectFactory.class);

					if (allegato.getNome_file_inv()!=null) {
						File fileInv = getFile(usercontext, allegato, "BDATA_INV", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_inv());
						allegato.setFile_inv(fileInv);
						JAXBElement<?> comunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(fileInv); 
						if (comunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType.class))
							allegato.setComunicazioneNuovoIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileInv));
						else if (comunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType.class))
							allegato.setComunicazioneModificaIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileInv));
						else if (comunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType.class))
							allegato.setComunicazioneCancellaIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileInv));
					}
					if (allegato.getNome_file_ric()!=null) {
						File fileRic = getFile(usercontext, allegato, "BDATA_RIC", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_ric());
						allegato.setFile_ric(fileRic);
						if (!allegato.getFl_merge_perla()) {
							JAXBElement<?> esitoComunicazione = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(fileRic); 
							if (esitoComunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType.class))
								allegato.setEsitoComunicazioneNuovoIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileRic));
							else if (esitoComunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType.class))
								allegato.setEsitoComunicazioneModificaIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileRic));
							else if (esitoComunicazione.getDeclaredType().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType.class))
								allegato.setEsitoComunicazioneCancellaIncaricoPerla((JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>)jc.createUnmarshaller().unmarshal(fileRic));
						}
					}
				}
			} catch(JAXBException e){
				throw new ComponentException(e);
			}
		}
		return oggettobulk;
	}
	
	public File getFile(UserContext usercontext, Incarichi_archivio_xml_fpBulk allegato, String tipoBlob, String pathFile, String nomeFile) throws ComponentException {
		try{
			Incarichi_archivio_xml_fpHome home = (Incarichi_archivio_xml_fpHome)getHome(usercontext,Incarichi_archivio_xml_fpBulk.class);
			File outputBinaryFile = new File(pathFile, nomeFile);
			Blob blob = home.getSQLBlob(allegato, tipoBlob);
			IOUtils.copyLarge(blob.getBinaryStream(), new FileOutputStream(outputBinaryFile));
			return outputBinaryFile;
		} catch (PersistencyException|SQLException e) {
			throw new ComponentException(e);
		} catch (FileNotFoundException e) {
			throw new ComponentException(e);
		} catch (IOException e) {
			throw new ComponentException(e);
		}
	}
	/**
	 * Ritorna il pagato di un incarico nell'anno e semestre indicato
	 * @param userContext
	 * @param incarico_anno
	 * @param annoPag
	 * @param semestrePag
	 * @return
	 * @throws ComponentException
	 */
	public List<Incarichi_comunicati_fp_detBulk> getPagatoPerSemestre(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException
	{
		List<Incarichi_comunicati_fp_detBulk> listPagato = new ArrayList<Incarichi_comunicati_fp_detBulk>();
		CompensoHome cHome = (CompensoHome)getHome(userContext, CompensoBulk.class);
		Mandato_rigaHome mrHome = (Mandato_rigaHome)getHome(userContext, Mandato_rigaIBulk.class);
		Iterator listacomp_incarico;
		try {
			listacomp_incarico = cHome.findCompensoIncaricoList(userContext,incarico).iterator();

			Calendar cal = Calendar.getInstance();
			for (Iterator x =listacomp_incarico;x.hasNext();){
				CompensoBulk dett =(CompensoBulk)x.next();
				
				SQLBuilder sqlMr = mrHome.createSQLBuilder();
				sqlMr.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, dett.getPg_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, dett.getPg_obbligazione_scadenzario());
				sqlMr.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_ori_obbligazione());
				sqlMr.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, dett.getCd_cds());
				sqlMr.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, dett.getCd_unita_organizzativa());
				sqlMr.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, dett.getEsercizio());
				sqlMr.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, dett.getPg_compenso());

				List listMr =  mrHome.fetchAll(sqlMr);
				getHomeCache(userContext).fetchAll(userContext);

				Timestamp dataPagamento = null;
				for (Iterator iterator = listMr.iterator(); iterator.hasNext();) {
					Mandato_rigaBulk mrBulk = (Mandato_rigaBulk) iterator.next();
					if (!mrBulk.getMandato().getStato().equals(MandatoBulk.STATO_MANDATO_ANNULLATO) &&
						mrBulk.getMandato().getDt_trasmissione()!=null) {
						dataPagamento=mrBulk.getMandato().getDt_trasmissione();
						break;
					}
				}
				if (dataPagamento!=null) {
					cal.setTime(dataPagamento);
				
					boolean trovato=false;
					int annoCompenso = cal.get(Calendar.YEAR);
					int meseCompenso = cal.get(Calendar.MONTH);
					int semestreCompenso;
					if (meseCompenso==Calendar.JANUARY || meseCompenso==Calendar.FEBRUARY || meseCompenso==Calendar.MARCH||
						meseCompenso==Calendar.APRIL || meseCompenso==Calendar.MAY || meseCompenso==Calendar.JUNE)
						semestreCompenso=1;
					else
						semestreCompenso=2;
					
					for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = listPagato.iterator(); iterator.hasNext();) {
						Incarichi_comunicati_fp_detBulk pagato = iterator.next();
						if (annoCompenso==pagato.getAnno_pag().intValue() && semestreCompenso==pagato.getSemestre_pag().intValue()) {
							pagato.setImporto_pag(pagato.getImporto_pag().add(dett.getIm_lordo_percipiente()));
							trovato=true;
							break;
						}
					}
					if (!trovato){
						Incarichi_comunicati_fp_detBulk pagato = new Incarichi_comunicati_fp_detBulk();
						pagato.setAnno_pag(annoCompenso);
						pagato.setSemestre_pag(semestreCompenso);
						pagato.setImporto_pag(dett.getIm_lordo_percipiente());
						listPagato.add(pagato);
					}
				}
			}
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		}
		return listPagato;
	}
}