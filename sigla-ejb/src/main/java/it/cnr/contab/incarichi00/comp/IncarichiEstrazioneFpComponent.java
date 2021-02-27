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

package it.cnr.contab.incarichi00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.incarichi00.bulk.*;
import it.cnr.contab.incarichi00.ejb.IncarichiProceduraComponentSession;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.incarichi00.storage.StorageContrattiAttachment;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.incarichi00.xmlfp.bulk.*;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.perlapa.PerlaIncarico;
import it.cnr.perlapa.incarico.*;
import it.cnr.perlapa.incarico.consulente.Allegati;
import it.cnr.perlapa.incarico.consulente.DatiIncarico;
import it.cnr.perlapa.incarico.consulente.PercettorePf;
import it.cnr.perlapa.incarico.consulente.PercettorePg;
import it.cnr.perlapa.utils.PerlaException;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;

import javax.ejb.EJBException;
import javax.xml.bind.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

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

			Incarichi_repertorioHome incHome = (Incarichi_repertorioHome) getHome(userContext, Incarichi_repertorioBulk.class);
			incaricoRepertorio.setArchivioAllegati(new BulkList(incHome.findArchivioAllegati(incaricoRepertorio)));

			incaricoRepertorio.setIncarichi_repertorio_rappColl(new BulkList(incHome.findIncarichi_repertorio_rappList(userContext, incaricoRepertorio)));
			incaricoRepertorio.setIncarichi_repertorio_rapp_detColl(new BulkList(incHome.findIncarichi_repertorio_rapp_detList(userContext, incaricoRepertorio)));

			return bulk;
		} catch (EJBException|PersistencyException|IntrospectionException ex) {
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
	 * @param incarico
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
		} catch (it.cnr.jada.persistency.PersistencyException | it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		}
		return listPagato;
	}

	public BigDecimal getPagatoIncarico(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException
	{
		BigDecimal pagato = BigDecimal.ZERO;
		CompensoHome cHome = (CompensoHome)getHome(userContext, CompensoBulk.class);
		Mandato_rigaHome mrHome = (Mandato_rigaHome)getHome(userContext, Mandato_rigaIBulk.class);
		Iterator listacomp_incarico;
		try {
			listacomp_incarico = cHome.findCompensoIncaricoList(userContext,incarico).iterator();

			Calendar cal = Calendar.getInstance();
			for (Iterator x =listacomp_incarico;x.hasNext();) {
				CompensoBulk dett = (CompensoBulk) x.next();

				SQLBuilder sqlMr = mrHome.createSQLBuilder();
				sqlMr.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, dett.getPg_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, dett.getPg_obbligazione_scadenzario());
				sqlMr.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_ori_obbligazione());
				sqlMr.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, dett.getCd_cds());
				sqlMr.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, dett.getCd_unita_organizzativa());
				sqlMr.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, dett.getEsercizio());
				sqlMr.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, dett.getPg_compenso());

				List listMr = mrHome.fetchAll(sqlMr);
				getHomeCache(userContext).fetchAll(userContext);

				Timestamp dataPagamento = null;
				for (Iterator iterator = listMr.iterator(); iterator.hasNext(); ) {
					Mandato_rigaBulk mrBulk = (Mandato_rigaBulk) iterator.next();
					if (!mrBulk.getMandato().getStato().equals(MandatoBulk.STATO_MANDATO_ANNULLATO) &&
							mrBulk.getMandato().getDt_trasmissione() != null) {
						dataPagamento = mrBulk.getMandato().getDt_trasmissione();
						break;
					}
				}
				if (dataPagamento != null)
					pagato.add(dett.getIm_lordo_percipiente());
			}
		} catch (it.cnr.jada.persistency.PersistencyException | it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		}
		return pagato;
	}

	private IncaricoConsulente generaNuovoConsulentePerla2018(UserContext userContext, V_incarichi_elenco_fpBulk v_incarico) throws ComponentException {
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
						allegati.setDichiarazioneIncarichi(SpringUtil.getBean("storeService", StoreService.class).getResource(rapporto.getCms_node_ref()));
						break;
					}
				}
			}
		} catch (Throwable e) {
		}
		incaricoPerla.setAllegati(allegati);

		//PAGATO
		if (incaricoPerla.getDatiEconomici().getCompenso().compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal importoPagato = this.getPagatoIncarico(userContext, incarico);
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

	public List<String> getAnomalie(V_incarichi_elenco_fpBulk v_incarico, IncaricoConsulente consulentePerla) {
		List<String> listAnomalie = new ArrayList<String>();
		Incarichi_repertorioBulk incarico = v_incarico.getIncaricoRepertorio();
		if (!Optional.ofNullable(consulentePerla.getDichiarante().getCodiceAooIpa()).isPresent() && !Optional.ofNullable(consulentePerla.getDichiarante().getCodiceUoIpa()).isPresent())
			listAnomalie.add("Manca sia il Codice AOO Ipa sul CDS "+incarico.getCd_cds()+" che il Codice Uo Ipa sulla Uo "+incarico.getCd_unita_organizzativa()+".");
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getPercettorePf().getCodiceFiscale() == null)
				listAnomalie.add("Manca il codice fiscale del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getCognome() == null)
				listAnomalie.add("Manca il cognome del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getNome() == null)
				listAnomalie.add("Manca il nome del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getGenere() == null)
				listAnomalie.add("Manca il sesso del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getDataNascita() == null)
				listAnomalie.add("Manca la data di nascita del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePf().getEstero().equals(YesNoEnum.N) &&
					(consulentePerla.getPercettorePf().getComuneNascita() == null || consulentePerla.getPercettorePf().getComuneNascita().equals("*")))
				listAnomalie.add("Manca il codice catastale del comune di nascita del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
		}
		if (Optional.ofNullable(consulentePerla.getPercettorePg()).isPresent()) {
			if (consulentePerla.getPercettorePg().getEstero().equals(YesNoEnum.N) && consulentePerla.getPercettorePg().getCodiceFiscale() == null)
				listAnomalie.add("Manca il codice fiscale del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
			if (consulentePerla.getPercettorePg().getDenominazione() == null)
				listAnomalie.add("Manca il nominativo del terzo (cod." + incarico.getTerzo().getCd_terzo() + ")");
		}
		if (consulentePerla.getDatiIncarico().getOggettoIncarico()==null)
			listAnomalie.add("Manca l'oggetto dell'incarico.");
		if (consulentePerla.getDatiIncarico().getDataConferimento()==null)
			listAnomalie.add("Manca la data di conferimento dell'incarico.");
		if (consulentePerla.getDatiIncarico().getDataInizio()==null)
			listAnomalie.add("Manca la data di inizio dell'incarico.");
		if (consulentePerla.getDatiIncarico().getEstremiAttoConferimento()==null)
			listAnomalie.add("Mancano gli estremi dell'atto di conferimento dell'incarico.");
		if (consulentePerla.getDatiIncarico().getTipoRapporto()==null)
			listAnomalie.add("Manca l'indicazione del tipo di rapporto dell'incarico.");
		if (consulentePerla.getDatiIncarico().getNaturaConferimento()==null)
			listAnomalie.add("Manca l'indicazione della natura conferimento dell'incarico.");
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getDatiIncarico().getAttestazioneVerificaInsussistenza()==null)
				listAnomalie.add("Manca l'indicazione della presenza dell'attestazione verifica insussistenza.");
		}
		if (consulentePerla.getDatiIncarico().getRiferimentoRegolamento()==null)
			listAnomalie.add("Manca l'indicazione se l'incarico è stato assegnato con riferimento ad un regolamento dell'amministrazione.");

		if (consulentePerla.getDatiEconomici().getTipoCompenso()==null)
			listAnomalie.add("Manca l'indicazione della tipologia di compenso.");
		if (consulentePerla.getDatiEconomici().getCompenso()==null)
			listAnomalie.add("Manca l'indicazione dell'importo del compenso.");
		if (consulentePerla.getDatiEconomici().getAmmontareErogato()==null)
			listAnomalie.add("Manca l'indicazione dell'importo del compenso erogato.");
		if (consulentePerla.getDatiEconomici().getIncaricoSaldato()==null)
			listAnomalie.add("Manca l'indicazione se il compenso è stato saldato.");
		if (consulentePerla.getDatiEconomici().getIncaricoSaldato().equals(YesNoEnum.Y) && consulentePerla.getDatiEconomici().getDataFine()==null)
			listAnomalie.add("Manca l'indicazione della data di fine dell'incarico saldato.");
		if (consulentePerla.getDatiEconomici().getTipoCompenso().equals(TipoCompensoEnum.GRATUITO)) {
			if (consulentePerla.getDatiEconomici().getAmmontareErogato().compareTo(BigDecimal.ZERO) > 0)
				listAnomalie.add("L'incarico gratuito deve risultare di importo zero.");
			if (!consulentePerla.getDatiEconomici().getIncaricoSaldato().equals(YesNoEnum.N))
				listAnomalie.add("L'incarico gratuito deve risultare non saldato.");
			if (!consulentePerla.getDatiEconomici().getComponentiVariabilCompenso().equals(YesNoEnum.N))
				listAnomalie.add("'incarico gratuito non deve avere componenti variabili del compenso.");
		}

		if (consulentePerla.getRiferimentoNormativo()!=null) {
			if (consulentePerla.getRiferimentoNormativo().getRiferimento()==null)
				listAnomalie.add("Manca l'indicazione del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getNumero()==null)
				listAnomalie.add("Manca l'indicazione del numero del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getArticolo()==null)
				listAnomalie.add("Manca l'indicazione dell'articolo del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getComma()==null)
				listAnomalie.add("Manca l'indicazione del comma del riferimento normativo.");
			if (consulentePerla.getRiferimentoNormativo().getData()==null)
				listAnomalie.add("Manca l'indicazione della data del riferimento normativo.");
		}
		if (Optional.ofNullable(consulentePerla.getPercettorePf()).isPresent()) {
			if (consulentePerla.getAllegati().getCurriculumVitae()==null)
				listAnomalie.add("Manca l'allegato di tipo Curriculum Vitae.");
			if (consulentePerla.getAllegati().getDichiarazioneIncarichi()==null)
				listAnomalie.add("Manca l'allegato di tipo Dichiarazione Altri Incarichi.");
		}
		return listAnomalie;
	}

	public void comunicaPerla2018(UserContext userContext, Incarichi_repertorioBulk incaricoRepertorio) throws ComponentException {
		try {
			IncarichiRepertorioComponentSession incRepComponent = Utility.createIncarichiRepertorioComponentSession();

			SQLBuilder sql = getHome(userContext, V_incarichi_elenco_fpBulk.class).createSQLBuilder();
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, incaricoRepertorio.getEsercizio());
			sql.addClause(FindClause.AND, "pg_repertorio", SQLBuilder.EQUALS, incaricoRepertorio.getPg_repertorio());

			List<V_incarichi_elenco_fpBulk> list = getHome(userContext, V_incarichi_elenco_fpBulk.class).fetchAll(sql);
			if (!list.isEmpty()) {
				if (list.size()>1)
					incRepComponent.aggiornaDatiPerla(userContext, incaricoRepertorio, null, "La view V_incarichi_elenco_fpBulk ritorna più record per l'incarico.");
				else {
					try {
						V_incarichi_elenco_fpBulk incaricoElenco = list.get(0);
						incaricoElenco = this.completaIncaricoElencoFP(userContext, incaricoElenco);
						this.comunicaPerla2018(userContext, incaricoElenco);
					} catch (Exception e) {
						incRepComponent.aggiornaDatiPerla(userContext, incaricoRepertorio, null, e.getMessage());
					}
				}

			}
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public void comunicaPerla2018(UserContext userContext, V_incarichi_elenco_fpBulk incaricoElenco) throws ComponentException {
		//La comunicazione riguarda solo incarichi con data stipula > 2018
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(incaricoElenco.getDt_stipula());
		if (calendar.get(Calendar.YEAR)<2018)
			return;

		IncarichiRepertorioComponentSession incRepComponent = Utility.createIncarichiRepertorioComponentSession();
		IncarichiProceduraComponentSession incProcComponent = Utility.createIncarichiProceduraComponentSession();
		try{
			Incarichi_parametriBulk incarichiParametriBulk = incProcComponent.getIncarichiParametri(userContext, incaricoElenco.getIncaricoRepertorio().getIncarichi_procedura());

			//La comunicazione avviene solo se i parametri lo consentono
			if (incarichiParametriBulk.getFl_invio_fp().equals("N"))
				return;

			IncaricoConsulente elementNuovoConsulentePerla = this.generaNuovoConsulentePerla2018(userContext, incaricoElenco);
			if (elementNuovoConsulentePerla!=null) {
				Incarichi_repertorioBulk incarico = incaricoElenco.getIncaricoRepertorio();

				List<String> listaAnomalie = this.getAnomalie(incaricoElenco, elementNuovoConsulentePerla);
				if (listaAnomalie.isEmpty()) {
					if (incarico.getIdPerla()==null) {
						try {
							Long idPerla = PerlaIncarico.getInstance().inserisci(elementNuovoConsulentePerla);
							incRepComponent.aggiornaDatiPerla(userContext, incarico, idPerla, null);

							String xmlInserimento = PerlaIncarico.getInstance().getXmlInserimento(elementNuovoConsulentePerla);
							storeXmlPerla(xmlInserimento, incarico.getCMISFolder().getCMISPath());
						} catch(PerlaException perlaException){
							throw new ApplicationException("Errore Inserimento WEB Perla: " + perlaException.getMessage());
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
								PerlaIncarico.getInstance().elimina(elementNuovoConsulentePerla);
								String xmlCancellazione = PerlaIncarico.getInstance().getXmlCancellazione(elementNuovoConsulentePerla);
								storeXmlPerla(xmlCancellazione, incarico.getCMISFolder().getCMISPath());
							}
							if (tipoAzione == OggettoBulk.TO_BE_DELETED || tipoAzione == OggettoBulk.TO_BE_CREATED) {
								Long idPerla = PerlaIncarico.getInstance().inserisci(elementNuovoConsulentePerla);
								incRepComponent.aggiornaDatiPerla(userContext, incarico, idPerla, null);
								String xmlInserimento = PerlaIncarico.getInstance().getXmlInserimento(elementNuovoConsulentePerla);
								storeXmlPerla(xmlInserimento, incarico.getCMISFolder().getCMISPath());
							} else if (tipoAzione == OggettoBulk.TO_BE_UPDATED) {
								PerlaIncarico.getInstance().modifica(elementNuovoConsulentePerla);
								String xmlVariazione = PerlaIncarico.getInstance().getXmlVariazione(elementNuovoConsulentePerla);
								storeXmlPerla(xmlVariazione, incarico.getCMISFolder().getCMISPath());
							}
						} catch(PerlaException perlaException) {
							throw new ApplicationException("Errore Aggiornamento WEB Perla: " + perlaException.getMessage());
						}
					}
				} else
					throw new ApplicationException(listaAnomalie.toString());
			}else
				throw new ApplicationException("Errore generico - Contattare il servizio assistenza.");
		} catch (Exception e){
			throw handleException(e);
		}
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
}