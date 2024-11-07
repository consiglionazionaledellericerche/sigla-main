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

package it.cnr.contab.pdg00.comp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdrKey;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivoHome;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.bulk.Stampa_pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Stampa_pdg_variazione_riepilogoBulk;
import it.cnr.contab.pdg00.bulk.Stampa_situazione_sintetica_x_progettoBulk;
import it.cnr.contab.pdg00.bulk.Stampa_var_stanz_resBulk;
import it.cnr.contab.pdg00.bulk.V_stm_paramin_pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Var_stanz_resBulk;
import it.cnr.contab.pdg00.bulk.Var_stanz_resHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg00.service.PdgVariazioniService;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneHome;
import it.cnr.contab.pdg01.comp.CRUDPdgVariazioneGestionaleComponent;
import it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.V_var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.V_var_stanz_resHome;
import it.cnr.jada.DetailedException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class PdGVariazioniComponent extends it.cnr.jada.comp.CRUDComponent
		implements Cloneable, Serializable, IPrintMgr {
	private static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);
	private static final long serialVersionUID = -3132138853583406225L;
	private PdgVariazioniService pdgVariazioniService;

	public PdGVariazioniComponent() {
		/* Default constructor */
	}

	/**
	 * Prima di eseguire il metodo
	 * {@link it.cnr.jada.comp.CRUDComponent#creaConBulk } vengono effettuati dei
	 * controlli sui dati inseriti.
	 * 
	 * Nome: Creare un nuovo elemento PdGVariazione; Pre: Effetuare il
	 * salvataggio del PdG con i dati corretti; Post: Prima di effettuare il
	 * salvataggio avvia il metodo per i controlli.
	 */
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {
		if (!(bulk instanceof Pdg_variazioneBulk))
			return super.creaConBulk(userContext, bulk);

		inizializzaValoriDefaultCampi(bulk);
		Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) super
				.creaConBulk(userContext, bulk);
		// P.R.: controllo spostato in fase di Salvataggio Definitivo
		// validaDettagliEntrataSpesa(userContext, pdg);
		try {
			MessaggioHome messHome = (MessaggioHome) getHome(userContext,
					MessaggioBulk.class);
			UtenteHome utenteHome = (UtenteHome) getHome(userContext,
					UtenteBulk.class);
			for (java.util.Iterator j = pdg_variazione.getAssociazioneCDR()
					.iterator(); j.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				for (java.util.Iterator i = utenteHome
						.findUtenteByCDRIncludeFirstLevel(
								ass_pdg.getCd_centro_responsabilita())
						.iterator(); i.hasNext();) {
					UtenteBulk utente = (UtenteBulk) i.next();
					MessaggioBulk messaggio = generaMessaggio(userContext,
							utente, pdg_variazione);
					super.creaConBulk(userContext, messaggio);
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
		return pdg_variazione;
	}

	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,
			OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) bulk;
			Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
					userContext, Pdg_variazioneBulk.class);
			pdg_variazione
					.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_PROVVISORIA);
			pdg_variazione.setDt_apertura(DateUtils.dataContabile(
					EJBCommonServices.getServerDate(), CNRUserContext
							.getEsercizio(userContext)));
			pdg_variazione.setTi_causale_respintaKeys(testataHome
					.loadCausaliMancataApprovazione(userContext));
			pdg_variazione.setFl_cda(new Boolean(false));
			pdg_variazione.setFl_visto_dip_variazioni(new Boolean(false));
			inizializzaSommeAZero(pdg_variazione);
			return super.inizializzaBulkPerInserimento(userContext,
					pdg_variazione);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}

	public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,
			OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Pdg_variazioneBulk) {
				Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
						userContext, Pdg_variazioneBulk.class);
				((Pdg_variazioneBulk) oggettobulk)
						.setTi_causale_respintaKeys(testataHome
								.loadCausaliMancataApprovazione(userContext));
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
		return super.inizializzaBulkPerRicerca(userContext, oggettobulk);
	}

	public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext,
			OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Pdg_variazioneBulk) {
				Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
						userContext, Pdg_variazioneBulk.class);
				((Pdg_variazioneBulk) oggettobulk)
						.setTi_causale_respintaKeys(testataHome
								.loadCausaliMancataApprovazione(userContext));
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
		return super.inizializzaBulkPerRicercaLibera(userContext, oggettobulk);
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,
			OggettoBulk pdg_variazione)
			throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) super
					.inizializzaBulkPerModifica(userContext, pdg_variazione);
			Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
					userContext, Pdg_variazioneBulk.class);
			pdg.setAssociazioneCDR(new it.cnr.jada.bulk.BulkList(testataHome
					.findAssociazioneCDR(pdg)));
			pdg.setArchivioConsultazioni(new it.cnr.jada.bulk.BulkList(
					testataHome.findArchivioConsultazioni(pdg)));
			pdg.setRiepilogoEntrate(new it.cnr.jada.bulk.BulkList(testataHome
					.findRiepilogoEntrate(pdg)));
			pdg.setRiepilogoSpese(new it.cnr.jada.bulk.BulkList(testataHome
					.findRiepilogoSpese(pdg)));
			pdg.setTi_causale_respintaKeys(testataHome
					.loadCausaliMancataApprovazione(userContext));
			pdg.setCdsAbilitatoAdApprovare(isCdsAbilitatoAdApprovare(
					userContext, pdg.getCentro_responsabilita().getCd_cds(),
					pdg));
			// RosPuc 28/01/2011
			if(pdg.getStato().equalsIgnoreCase(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA)){
			  pdg.setVar_bilancio(((Var_bilancioHome) getHome(userContext, Var_bilancioBulk.class)).findByPdg_variazione(pdg));
			}
			String desTipoVariazione = getDesTipoVariazione(userContext, pdg);
			if (desTipoVariazione != null)
				pdg.setDesTipoVariazione(desTipoVariazione);
			
			inizializzaSommeAZero(pdg);
			inizializzaSommeDiSpesa(userContext, pdg);
			inizializzaSommeDiEntrata(userContext, pdg);
			inizializzaSommeCdR(userContext, pdg);
			inizializzaVistosuAssociazioneCDR(userContext, pdg);

			getHomeCache(userContext).fetchAll(userContext);
			return pdg;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private boolean isCdsEnte(UserContext userContext) throws ComponentException {
		try {
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
			if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
				return true;
			else
				return false;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
			throws ComponentException {
		if (!(bulk instanceof Pdg_variazioneBulk))
			return super.creaConBulk(userContext, bulk);

		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) bulk;
		try {
			MessaggioHome messHome = (MessaggioHome) getHome(userContext,
					MessaggioBulk.class);
			UtenteHome utenteHome = (UtenteHome) getHome(userContext,
					UtenteBulk.class);
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				if (ass_pdg.getCrudStatus() == OggettoBulk.TO_BE_CREATED
						&& ass_pdg.getCd_centro_responsabilita() != null) {
					for (java.util.Iterator i = utenteHome
							.findUtenteByCDRIncludeFirstLevel(
									ass_pdg.getCd_centro_responsabilita())
							.iterator(); i.hasNext();) {
						UtenteBulk utente = (UtenteBulk) i.next();
						MessaggioBulk messaggio = generaMessaggio(userContext,
								utente, pdg);
						super.creaConBulk(userContext, messaggio);
					}
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
		inizializzaValoriDefaultCampi(pdg);
		super.modificaConBulk(userContext, pdg);
		// P.R.: controllo spostato in fase di Salvataggio Definitivo
		// validaDettagliEntrataSpesa(userContext, pdg);
		return pdg;
	}
	
	public void archiviaVariazioneDocumentale(UserContext userContext, OggettoBulk oggettobulk) throws ComponentException {
		pdgVariazioniService = SpringUtil.getBean(PdgVariazioniService.class);
		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) oggettobulk;
		ArchiviaStampaPdgVariazioneBulk stampapdg = new ArchiviaStampaPdgVariazioneBulk();
		stampapdg.setPdg_variazioneForPrint(pdg);
		if (Optional.ofNullable(stampapdg.getCd_cds()).isPresent()) {
            final Optional<StorageObject> storageObject = Optional.ofNullable(
                    pdgVariazioniService.getPdgVariazioneDocument(stampapdg).getStorageObject());
            if(storageObject.isPresent() && !Optional.of(storageObject)
                    .map(storageObject1 -> storageObject1.get().<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                    .filter(aspects -> aspects.contains(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value())).isPresent()){
                Print_spoolerBulk print = new Print_spoolerBulk();
                print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
                print.setFlEmail(false);
                print.setReport("/cnrpreventivo/pdg/stampa_variazioni_pdg.jasper");
                print.setNomeFile("Variazione al PdG n. "
                        + pdg.getPg_variazione_pdg()
                        + " CdR proponente "
                        + pdg.getCd_centro_responsabilita() + ".pdf");
                print.setUtcr(userContext.getUser());
                print.addParam("Esercizio", pdg.getEsercizio(), Integer.class);
                print.addParam("Variazione", pdg.getPg_variazione_pdg().intValue(), Integer.class);
                try {
                    Report report = SpringUtil.getBean("printService",PrintService.class).executeReport(userContext,print);
                    stampapdg.setPdgVariazioneDocument(pdgVariazioniService.getPdgVariazioneDocument(stampapdg));
                    pdgVariazioniService.updateStream(stampapdg.getPdgVariazioneDocument().getStorageObject().getKey(), report.getInputStream(), report.getContentType());
                    pdgVariazioniService.updateProperties(stampapdg, stampapdg.getPdgVariazioneDocument().getStorageObject());
                } catch (IOException e) {
                    throw new ComponentException(e);
                }
            }
        }
	}
	protected MessaggioBulk generaMessaggio(UserContext userContext,
			UtenteBulk utente, Pdg_variazioneBulk pdg, String tipo)
			throws ComponentException, PersistencyException {
		MessaggioHome messHome = (MessaggioHome) getHome(userContext,
				MessaggioBulk.class);
		MessaggioBulk messaggio = new MessaggioBulk();
		messaggio.setPg_messaggio(new Long(messHome.fetchNextSequenceValue(
				userContext, "CNRSEQ00_PG_MESSAGGIO").longValue()));
		messaggio.setCd_utente(utente.getCd_utente());
		messaggio.setPriorita(new Integer(1));
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		if (tipo == null) {
			messaggio
					.setDs_messaggio(sdf.format(EJBCommonServices
							.getServerTimestamp())
							+ " - È stata aperta una nuova Variazione al Piano di Gestione");
		} else if (tipo.equals(Pdg_variazioneBulk.STATO_APPROVATA)) {
			messaggio
					.setDs_messaggio(sdf.format(EJBCommonServices
							.getServerTimestamp())
							+ " - È stata approvata la Variazione al Piano di Gestione");
		} else if (tipo.equals(Pdg_variazioneBulk.STATO_RESPINTA)) {
			messaggio.setDs_messaggio(sdf.format(EJBCommonServices
					.getServerTimestamp())
					+ " - È stata respinta la Variazione al Piano di Gestione");
		}
		messaggio.setCorpo("Numero variazione:" + pdg.getPg_variazione_pdg());
		messaggio.setCorpo(messaggio.getCorpo() + "\n" + "CdR proponente:"
				+ pdg.getCentro_responsabilita().getCd_ds_cdr());
		messaggio.setSoggetto(messaggio.getDs_messaggio());
		messaggio.setToBeCreated();
		return messaggio;
	}

	private MessaggioBulk generaMessaggio(UserContext userContext,
			UtenteBulk utente, Pdg_variazioneBulk pdg)
			throws ComponentException, PersistencyException {
		return generaMessaggio(userContext, utente, pdg, null);
	}

	private SQLBuilder selectBase(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException,	it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = getHome(userContext, bulk, "VP_PDG_VARIAZIONE").createSQLBuilder();
		sql.addClause(clauses);
		sql.addClause(bulk.buildFindClauses(new Boolean(true)));
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));

		Optional.ofNullable(bulk)
			.filter(Pdg_variazioneBulk.class::isInstance)
			.map(Pdg_variazioneBulk.class::cast)
			.filter(el->Pdg_variazioneBulk.MOTIVAZIONE_GENERICO.equals(el.getMapMotivazioneVariazione()))
			.ifPresent(var->sql.addSQLClause(FindClause.AND,"VP_PDG_VARIAZIONE.TI_MOTIVAZIONE_VARIAZIONE",SQLBuilder.ISNULL,null));
			
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

			CdrHome cdrHome = (CdrHome) getHome(userContext, CdrBulk.class);
			CdrBulk cdrUtente = (CdrBulk) cdrHome.findByPrimaryKey(new CdrKey(
					it.cnr.contab.utenze00.bp.CNRUserContext
							.getCd_cdr(userContext)));

			if (!((CNRUserContext) userContext).getCd_unita_organizzativa()
					.equals(ente.getCd_unita_organizzativa())) {
				sql.openParenthesis("AND");
				sql.addSQLClause("OR",
						"VP_PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA",
						sql.EQUALS, cdrUtente.getCd_centro_responsabilita());
				for (java.util.Iterator j = cdrHome.findCdrAfferenti(cdrUtente)
						.iterator(); j.hasNext();) {
					CdrBulk cdrAfferenti = (CdrBulk) j.next();
					sql.addSQLClause("OR",
							"VP_PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA",
							sql.EQUALS, cdrAfferenti
									.getCd_centro_responsabilita());
				}

				SQLBuilder sqlAssUo = getHome(userContext,
						Ass_pdg_variazione_cdrBulk.class).createSQLBuilder();
				sqlAssUo.addSQLJoin("VP_PDG_VARIAZIONE.ESERCIZIO",
						"ASS_PDG_VARIAZIONE_CDR.ESERCIZIO");
				sqlAssUo.addSQLJoin("VP_PDG_VARIAZIONE.PG_VARIAZIONE_PDG",
						"ASS_PDG_VARIAZIONE_CDR.PG_VARIAZIONE_PDG");

				sqlAssUo.openParenthesis("AND");
				sqlAssUo.addSQLClause("OR",
						"ASS_PDG_VARIAZIONE_CDR.CD_CENTRO_RESPONSABILITA",
						sqlAssUo.EQUALS, cdrUtente
								.getCd_centro_responsabilita());
				for (java.util.Iterator j = cdrHome.findCdrAfferenti(cdrUtente)
						.iterator(); j.hasNext();) {
					CdrBulk cdrAfferenti = (CdrBulk) j.next();
					sqlAssUo.addSQLClause("OR",
							"ASS_PDG_VARIAZIONE_CDR.CD_CENTRO_RESPONSABILITA",
							sqlAssUo.EQUALS, cdrAfferenti
									.getCd_centro_responsabilita());
				}
				sqlAssUo.closeParenthesis();
				sql.addSQLExistsClause("OR", sqlAssUo);
				sql.closeParenthesis();
			}
			return sql;
	}

	protected Query select(UserContext userContext, CompoundFindClause clauses,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = selectBase(userContext, clauses, bulk);
		UtenteBulk utente = (UtenteBulk) getHome(userContext, UtenteBulk.class)
				.findByPrimaryKey(
						new UtenteKey(it.cnr.contab.utenze00.bp.CNRUserContext
								.getUser(userContext)));

		if (utente.getDipartimento() != null
				&& utente.getDipartimento().getCd_dipartimento() != null) {
			Pdg_variazione_riga_gestHome dipHome = (Pdg_variazione_riga_gestHome) getHome(
					userContext, Pdg_variazione_riga_gestBulk.class);
			SQLBuilder sqlRiga = dipHome.createSQLBuilder();
			sqlRiga.resetColumns();
			sqlRiga.addColumn("DISTINCT PG_VARIAZIONE_PDG");
			sqlRiga.addSQLClause("AND", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO = "
					+ CNRUserContext.getEsercizio(userContext));

			sqlRiga.addTableToHeader("LINEA_ATTIVITA");
			sqlRiga.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA",
					"PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
			sqlRiga.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA",
					"PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");

			sqlRiga.addTableToHeader("PROGETTO_GEST MODULO");
			sqlRiga.addSQLJoin("MODULO.ESERCIZIO",
					"PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
			sqlRiga.addSQLJoin("MODULO.PG_PROGETTO",
					"LINEA_ATTIVITA.PG_PROGETTO");

			sqlRiga.addTableToHeader("PROGETTO_GEST COMMESSA");
			sqlRiga.addSQLJoin("COMMESSA.ESERCIZIO",
					"MODULO.ESERCIZIO_PROGETTO_PADRE");
			sqlRiga.addSQLJoin("COMMESSA.PG_PROGETTO",
					"MODULO.PG_PROGETTO_PADRE");

			sqlRiga.addTableToHeader("PROGETTO_GEST PROGETTO");
			sqlRiga.addSQLJoin("PROGETTO.ESERCIZIO",
					"COMMESSA.ESERCIZIO_PROGETTO_PADRE");
			sqlRiga.addSQLJoin("PROGETTO.PG_PROGETTO",
					"COMMESSA.PG_PROGETTO_PADRE");

			sqlRiga.addSQLClause("AND", "PROGETTO.CD_DIPARTIMENTO='"
					+ utente.getDipartimento().getCd_dipartimento() + "'");

			sql.addSQLClause("AND", "VP_PDG_VARIAZIONE.PG_VARIAZIONE_PDG IN ("
					+ sqlRiga.getStatement() + ")");
		}
		return sql;
	}

	public RemoteIterator cercaVariazioniForApposizioneVisto(
			UserContext usercontext, CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk) throws ComponentException {
		try {
			return iterator(usercontext, selectVariazioniForApposizioneVisto(
					usercontext, compoundfindclause, oggettobulk), oggettobulk
					.getClass(), getFetchPolicyName("find"));
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	private Query selectVariazioniForApposizioneVisto(UserContext userContext,
			CompoundFindClause clauses, OggettoBulk bulk)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = selectBase(userContext, clauses, bulk);
		UtenteBulk utente = (UtenteBulk) getHome(userContext, UtenteBulk.class)
				.findByPrimaryKey(
						new UtenteKey(it.cnr.contab.utenze00.bp.CNRUserContext
								.getUser(userContext)));

		if (utente.getDipartimento() != null
				&& utente.getDipartimento().getCd_dipartimento() != null) {
			Pdg_variazione_riga_gestHome dipHome = (Pdg_variazione_riga_gestHome) getHome(
					userContext, Pdg_variazione_riga_gestBulk.class);
			SQLBuilder sqlRiga = dipHome.createSQLBuilder();
			sqlRiga.resetColumns();
			sqlRiga.addColumn("DISTINCT PG_VARIAZIONE_PDG");
			sqlRiga.addSQLClause("AND", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO = "
					+ CNRUserContext.getEsercizio(userContext));
			sqlRiga.addSQLClause("AND",
					"PDG_VARIAZIONE_RIGA_GEST.FL_VISTO_DIP_VARIAZIONI = 'N'");

			sqlRiga.addTableToHeader("LINEA_ATTIVITA");
			sqlRiga.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA",
					"PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
			sqlRiga.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA",
					"PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");

			sqlRiga.addTableToHeader("PROGETTO_GEST MODULO");
			sqlRiga.addSQLJoin("MODULO.ESERCIZIO",
					"PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
			sqlRiga.addSQLJoin("MODULO.PG_PROGETTO",
					"LINEA_ATTIVITA.PG_PROGETTO");

			sqlRiga.addTableToHeader("PROGETTO_GEST COMMESSA");
			sqlRiga.addSQLJoin("COMMESSA.ESERCIZIO",
					"MODULO.ESERCIZIO_PROGETTO_PADRE");
			sqlRiga.addSQLJoin("COMMESSA.PG_PROGETTO",
					"MODULO.PG_PROGETTO_PADRE");

			sqlRiga.addTableToHeader("PROGETTO_GEST PROGETTO");
			sqlRiga.addSQLJoin("PROGETTO.ESERCIZIO",
					"COMMESSA.ESERCIZIO_PROGETTO_PADRE");
			sqlRiga.addSQLJoin("PROGETTO.PG_PROGETTO",
					"COMMESSA.PG_PROGETTO_PADRE");

			sqlRiga.addSQLClause("AND", "PROGETTO.CD_DIPARTIMENTO='"
					+ utente.getDipartimento().getCd_dipartimento() + "'");

			sql.addSQLClause("AND", "VP_PDG_VARIAZIONE.PG_VARIAZIONE_PDG IN ("
					+ sqlRiga.getStatement() + ")");
		}
		return sql;
	}

	public RemoteIterator cercaForPrintRiepilogo(UserContext usercontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk stampa) throws ComponentException {
		try {
			return iterator(usercontext, selectForPrintRiepilogo(usercontext,
					compoundfindclause, oggettobulk, stampa), oggettobulk
					.getClass(), getFetchPolicyName("find"));
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	protected Query selectForPrintRiepilogo(UserContext userContext,
			CompoundFindClause clauses, OggettoBulk bulk, OggettoBulk stampa)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		Stampa_pdg_variazione_riepilogoBulk stampa_pdg = (Stampa_pdg_variazione_riepilogoBulk) stampa;
		SQLBuilder sql = (SQLBuilder) select(userContext, clauses, bulk);
		boolean selCds = stampa_pdg.getCdsForPrint() != null
				&& stampa_pdg.getCdsForPrint().getCd_unita_organizzativa() != null;
		boolean setUO = stampa_pdg.getUoForPrint() != null
				&& stampa_pdg.getUoForPrint().getCd_unita_organizzativa() != null;
		if (selCds && !setUO) {
			sql.addTableToHeader("UNITA_ORGANIZZATIVA");
			sql.addTableToHeader("CDR");
			sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",
					"CDR.CD_UNITA_ORGANIZZATIVA");
			sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA",
					"VP_PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA");
			sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE",
					SQLBuilder.EQUALS, stampa_pdg.getCdsForPrint()
							.getCd_unita_organizzativa());
			sql.addSQLBetweenClause("AND", "VP_PDG_VARIAZIONE.DT_APPROVAZIONE",
					stampa_pdg.getDataApprovazione_da(), stampa_pdg
							.getDataApprovazione_a());

		}
		if (setUO) {
			sql.addTableToHeader("CDR");
			sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA",
					"VP_PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA");
			sql.addSQLClause("AND", "CDR.CD_UNITA_ORGANIZZATIVA",
					SQLBuilder.EQUALS, stampa_pdg.getUoForPrint()
							.getCd_unita_organizzativa());
			sql.addSQLBetweenClause("AND", "VP_PDG_VARIAZIONE.DT_APPROVAZIONE",
					stampa_pdg.getDataApprovazione_da(), stampa_pdg
							.getDataApprovazione_a());
		}
		if (!stampa_pdg.getStato().equals("*"))
			sql.addSQLClause("AND", "VP_PDG_VARIAZIONE.STATO",
					SQLBuilder.EQUALS, stampa_pdg.getStato());
		sql.addSQLBetweenClause("AND", "VP_PDG_VARIAZIONE.DT_APPROVAZIONE",
				stampa_pdg.getDataApprovazione_da(), stampa_pdg
						.getDataApprovazione_a());
		return sql;
	}

	public Pdg_variazioneBulk salvaDefinitivo(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException {
		pdg.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
		pdg.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices
				.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		pdg.setToBeUpdated();
		inizializzaSommeCdR(userContext, pdg);
		if (pdg.getAssociazioneCDR().isEmpty())
			throw new ApplicationException(
					"Associare almeno un Centro di Responsabilità alla Variazione.");
		// P.R.: Controllo proveniente da ModificaConBulk e CreaConBulk
		// Deciso con Angelini di spostare i controlli in fase di salvataggio
		// definitivo
		validaDettagliEntrataSpesa(userContext, pdg);
		/*
		 * Confermo l'operazione E' importante salvare in questo momento in
		 * controllo di disponibilità avviene tramite procedura Pl-Sql che deve
		 * già trovare sul DB la variazione con stato Definitivo altrimenti non
		 * la considera ai fini del controllo
		 */
		pdg = (Pdg_variazioneBulk) super.modificaConBulk(userContext, pdg);

		try {
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				Ass_pdg_variazione_cdrHome ass_pdgHome = (Ass_pdg_variazione_cdrHome) getHome(
						userContext, Ass_pdg_variazione_cdrBulk.class);

				if (ass_pdgHome.findDettagliSpesa(ass_pdg).isEmpty()) {
					if (ass_pdgHome.findDettagliEntrata(ass_pdg).isEmpty())
						throw new ApplicationException(
								"Associare almeno un dettaglio di variazione al Centro di Responsabilità "
										+ ass_pdg.getCd_centro_responsabilita());
				}

				if (ass_pdg.getEntrata_diff().compareTo(ZERO) != 0)
					throw new ApplicationException("La Differenza di entrata ("
							+ new it.cnr.contab.util.EuroFormat()
									.format(ass_pdg.getEntrata_diff()) + ")"
							+ "\n" + "per il Cdr "
							+ ass_pdg.getCd_centro_responsabilita()
							+ " è diversa da zero. ");
				if (ass_pdg.getSpesa_diff().compareTo(ZERO) != 0)
					throw new ApplicationException("La Differenza di spesa ("
							+ new it.cnr.contab.util.EuroFormat()
									.format(ass_pdg.getSpesa_diff()) + ")"
							+ "\n" + "per il Cdr "
							+ ass_pdg.getCd_centro_responsabilita()
							+ " è diversa da zero. ");
			}
			
			/*
			 * Verifico che tutti gli importi assestati dell'aggregato abbiano
			 * valore >= 0
			 */
			Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
					userContext, Pdg_variazioneBulk.class);
			for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
					.iterator(); spesa.hasNext();) {
				controllaAggregatoPdgSpeDetPositivo(userContext,
						(Pdg_preventivo_spe_detBulk) spesa.next());
			}
			for (java.util.Iterator entrate = testataHome.findDettagliEntrata(
					pdg).iterator(); entrate.hasNext();) {
				controllaAggregatoPdgEtrDetPositivo(userContext,
						(Pdg_preventivo_etr_detBulk) entrate.next());
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return pdg;
	}

	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di
	 * aggiornamento Saldi delle voci del piano
	 * 
	 * @return <code>SaldoComponentSession</code> La component dei Saldi delle
	 *         voci del piano
	 */
	private it.cnr.contab.doccont00.ejb.SaldoComponentSession createSaldoComponentSession()
			throws ComponentException {
		try {
			return (SaldoComponentSession) EJBCommonServices
					.createEJB("CNRDOCCONT00_EJB_SaldoComponentSession");
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	protected Voce_f_saldi_cdr_lineaBulk trovaSaldo(UserContext userContext,
			Pdg_preventivo_detBulk pdg_det, Voce_fBulk voce)
			throws PersistencyException, ComponentException {

		Voce_f_saldi_cdr_lineaBulk saldo = (Voce_f_saldi_cdr_lineaBulk) getHome(
				userContext, Voce_f_saldi_cdr_lineaBulk.class)
				.findByPrimaryKey(
						new Voce_f_saldi_cdr_lineaBulk(pdg_det.getEsercizio(),
								pdg_det.getEsercizio(), pdg_det
										.getCd_centro_responsabilita(), pdg_det
										.getCd_linea_attivita(), pdg_det
										.getTi_appartenenza(), pdg_det
										.getTi_gestione(), voce.getCd_voce()));
		return saldo;
	}

	private void aggiornaSaldiCdrLinea(UserContext userContext,
			Pdg_preventivo_detBulk pdg_det) throws ComponentException {
		Pdg_preventivo_spe_detBulk pdg_spe_det = null;
		Pdg_preventivo_etr_detBulk pdg_etr_det = null;
		BigDecimal impDaAggiornare = Utility.ZERO;
		if (pdg_det instanceof Pdg_preventivo_spe_detBulk) {
			pdg_spe_det = (Pdg_preventivo_spe_detBulk) pdg_det;
		} else if (pdg_det instanceof Pdg_preventivo_etr_detBulk) {
			pdg_etr_det = (Pdg_preventivo_etr_detBulk) pdg_det;
		}
		try {
			String cd_voce = getVoce_FdaEV(userContext, pdg_det.getEsercizio(),
					pdg_det.getTi_appartenenza(), pdg_det.getTi_gestione(),
					pdg_det.getCd_elemento_voce(), pdg_det
							.getCd_centro_responsabilita(), pdg_det
							.getCd_linea_attivita());
			Voce_fHome voce_fHome = ((Voce_fHome) getHome(userContext,
					Voce_fBulk.class));
			Voce_fBulk voce = new Voce_fBulk(cd_voce, pdg_det.getEsercizio(),
					pdg_det.getTi_appartenenza(), pdg_det.getTi_gestione());
			voce = (Voce_fBulk) voce_fHome.findByPrimaryKey(voce);
			getHomeCache(userContext).fetchAll(userContext, voce_fHome);
			if (pdg_det.getTi_gestione().equals(
					Elemento_voceHome.GESTIONE_SPESE)) {
				impDaAggiornare = pdg_spe_det
						.getIm_ri_ccs_spese_odc()
						.add(
								pdg_spe_det
										.getIm_rk_ccs_spese_ogc()
										.add(
												pdg_spe_det
														.getIm_rq_ssc_costi_odc()
														.add(
																pdg_spe_det
																		.getIm_rs_ssc_costi_ogc()
																		.add(
																				pdg_spe_det
																						.getIm_ru_spese_costi_altrui()))));
			} else if (pdg_det.getTi_gestione().equals(
					Elemento_voceHome.GESTIONE_ENTRATE)) {
				impDaAggiornare = pdg_etr_det.getIm_ra_rce().add(
						pdg_etr_det.getIm_rc_esr());
			}
			Utility.createSaldoComponentSession()
					.aggiornaVariazioneStanziamento(userContext,
							pdg_det.getCd_centro_responsabilita(),
							pdg_det.getCd_linea_attivita(), voce,
							pdg_det.getEsercizio(),
							Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,
							impDaAggiornare);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
	}

	public Pdg_variazioneBulk approva(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException {
		pdg.setStato(Pdg_variazioneBulk.STATO_APPROVATA);
		pdg.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices
				.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		pdg.setToBeUpdated();
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
				userContext, Pdg_variazioneBulk.class);
		try {
			for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
					.iterator(); spesa.hasNext();) {
				Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spesa
						.next();
				spesa_det.setOrigine(Pdg_preventivo_spe_detBulk.OR_UTENTE);
				spesa_det.setStato(Pdg_preventivo_spe_detBulk.ST_CONFERMA);
				spesa_det.setToBeUpdated();
				super.modificaConBulk(userContext, spesa_det);
			}
			for (java.util.Iterator entrate = testataHome.findDettagliEntrata(
					pdg).iterator(); entrate.hasNext();) {
				Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
						.next();
				etr_det.setOrigine(Pdg_preventivo_etr_detBulk.OR_UTENTE);
				etr_det.setStato(Pdg_preventivo_etr_detBulk.ST_CONFERMA);
				etr_det.setToBeUpdated();
				super.modificaConBulk(userContext, etr_det);
			}
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				if ((ass_pdg.getCentro_responsabilita().getLivello().intValue() == 1)
						|| (ass_pdg.getCentro_responsabilita()
								.getCd_cdr_afferenza() == null)) {
					ribaltaCostiPdGArea(userContext, ass_pdg.getEsercizio(),
							ass_pdg.getCd_centro_responsabilita(), pdg);
				} else {
					ribaltaCostiPdGArea(userContext, ass_pdg.getEsercizio(),
							ass_pdg.getCentro_responsabilita()
									.getCd_cdr_afferenza(), pdg);
				}
			}
			/*
			 * Verifico che tutti gli importi assestati dell'aggregato abbiano
			 * valore >= 0
			 */
			for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
					.iterator(); spesa.hasNext();) {
				controllaAggregatoPdgSpeDetPositivo(userContext,
						(Pdg_preventivo_spe_detBulk) spesa.next());
			}
			for (java.util.Iterator entrate = testataHome.findDettagliEntrata(
					pdg).iterator(); entrate.hasNext();) {
				controllaAggregatoPdgEtrDetPositivo(userContext,
						(Pdg_preventivo_etr_detBulk) entrate.next());
			}

			/*
			 * Confermo l'operazione e provvedo ad aggiornare l'aggregato e a
			 * mandare i messaggi a tutti gli utenti E' importante salvare in
			 * questo momento in quanto l'aggiornamento dell'aggregato avviene
			 * tramite procedura Pl-Sql che deve già trovare sul DB la
			 * variazione con stato Approvato altrimenti non la considera ai
			 * fini del calcolo dell'aggregato
			 */
			super.modificaConBulk(userContext, pdg);

			// lancio la procedura di gestione del visto dipartimenti
			gestioneVistoDipartimenti(userContext, pdg);

			/*
			 * Ricarico il BULK con i dati presenti sul DB che, nel frattempo,
			 * potrebbero essere aumentati a causa di procedure DB lanciate da
			 * comandi precedenti (es. ribaltaCostiPdGArea)
			 */
			pdg = (Pdg_variazioneBulk) testataHome.findByPrimaryKey(pdg);
			pdg.setAssociazioneCDR(new it.cnr.jada.bulk.BulkList(testataHome
					.findAssociazioneCDR(pdg)));

			/*
			 * Spedisco i messaggi di avvertimento a tutti i CDR interessati
			 * alla Variazione
			 */
			MessaggioHome messHome = (MessaggioHome) getHome(userContext,
					MessaggioBulk.class);
			UtenteHome utenteHome = (UtenteHome) getHome(userContext,
					UtenteBulk.class);
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				/*
				 * Solo a questo punto del codice ho tutti i CDR caricati
				 * compresi quelli di Area. Pertanto procedo ad aggiornare
				 * l'Aggregato
				 */
				inizializzaAggregatoPDG(userContext, ass_pdg);
				for (java.util.Iterator i = utenteHome
						.findUtenteByCDRIncludeFirstLevel(
								ass_pdg.getCd_centro_responsabilita())
						.iterator(); i.hasNext();) {
					UtenteBulk utente = (UtenteBulk) i.next();
					MessaggioBulk messaggio = generaMessaggio(userContext,
							utente, pdg, Pdg_variazioneBulk.STATO_APPROVATA);
					super.creaConBulk(userContext, messaggio);
				}
			}
			for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
					.iterator(); spesa.hasNext();) {
				Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spesa
						.next();
				aggiornaSaldiCdrLinea(userContext, spesa_det);
			}
			for (java.util.Iterator entrate = testataHome.findDettagliEntrata(
					pdg).iterator(); entrate.hasNext();) {
				Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
						.next();
				aggiornaSaldiCdrLinea(userContext, etr_det);
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return pdg;
	}

	public Pdg_variazioneBulk respingi(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException {
		if (pdg.getCd_causale_respinta() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Indicare la causale della Mancata Approvazione.");

		pdg.setStato(Pdg_variazioneBulk.STATO_RESPINTA);
		pdg.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices
				.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		pdg.setToBeUpdated();
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
				userContext, Pdg_variazioneBulk.class);
		try {
			for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
					.iterator(); spesa.hasNext();) {
				Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spesa
						.next();
				spesa_det.setStato(Pdg_preventivo_spe_detBulk.ST_ANNULLA);
				spesa_det.setToBeUpdated();
				super.modificaConBulk(userContext, spesa_det);
			}
			for (java.util.Iterator entrate = testataHome.findDettagliEntrata(
					pdg).iterator(); entrate.hasNext();) {
				Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
						.next();
				etr_det.setStato(Pdg_preventivo_etr_detBulk.ST_ANNULLA);
				etr_det.setToBeUpdated();
				super.modificaConBulk(userContext, etr_det);
			}
			MessaggioHome messHome = (MessaggioHome) getHome(userContext,
					MessaggioBulk.class);
			UtenteHome utenteHome = (UtenteHome) getHome(userContext,
					UtenteBulk.class);
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				for (java.util.Iterator i = utenteHome
						.findUtenteByCDRIncludeFirstLevel(
								ass_pdg.getCd_centro_responsabilita())
						.iterator(); i.hasNext();) {
					UtenteBulk utente = (UtenteBulk) i.next();
					MessaggioBulk messaggio = generaMessaggio(userContext,
							utente, pdg, Pdg_variazioneBulk.STATO_RESPINTA);
					super.creaConBulk(userContext, messaggio);
				}
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return (Pdg_variazioneBulk) super.modificaConBulk(userContext, pdg);
	}

	/**
	 * Viene richiesta l'eliminazione dell'oggetto selezionato
	 * 
	 * Pre-post-conditions:
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da eliminare
	 * @return void
	 * 
	 **/
	public void eliminaConBulk(UserContext userContext, OggettoBulk bulk)
			throws ComponentException {
		try {
			if (bulk instanceof ICancellatoLogicamente) {
				((ICancellatoLogicamente) bulk).cancellaLogicamente();
				updateBulk(userContext, bulk);
			} else {
				super.eliminaConBulk(userContext, bulk);
			}
			/*
			 * Annullo i dettagli
			 */
			if (bulk instanceof Pdg_variazioneBulk) {
				Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
						userContext, Pdg_variazioneBulk.class);
				for (java.util.Iterator spesa = testataHome.findDettagliSpesa(
						(Pdg_variazioneBulk) bulk).iterator(); spesa.hasNext();) {
					Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spesa
							.next();
					spesa_det.setStato(Pdg_preventivo_spe_detBulk.ST_ANNULLA);
					spesa_det.setToBeUpdated();
					super.modificaConBulk(userContext, spesa_det);
				}
				for (java.util.Iterator entrate = testataHome
						.findDettagliEntrata((Pdg_variazioneBulk) bulk)
						.iterator(); entrate.hasNext();) {
					Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
							.next();
					etr_det.setStato(Pdg_preventivo_etr_detBulk.ST_ANNULLA);
					etr_det.setToBeUpdated();
					super.modificaConBulk(userContext, etr_det);
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}

	private boolean isImpPositivo(BigDecimal imp) {
		return (imp.compareTo(new BigDecimal(0)) == 1);
	}

	/**
	 * Verifica che la U.O. che ha proposto la variazione di progetto sia di
	 * tipo ENTE
	 * 
	 * Pre-post-conditions:
	 * 
	 * @param userContext
	 * @param pdgBulk
	 *            l'OggettoBulk della variazione di cui controllare il tipo di
	 *            UO proponente
	 * @return boolean
	 * 
	 **/
	public boolean isUoPdgUoEnte(UserContext userContext,
			Pdg_variazioneBulk pdgBulk) throws ComponentException {
		try {
			String cd_uo_variazione = pdgBulk.getCentro_responsabilita()
					.getCd_unita_organizzativa();
			Unita_organizzativaHome unita_organizzativa_home = (Unita_organizzativaHome) getHome(
					userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo_variazione = (Unita_organizzativaBulk) unita_organizzativa_home
					.findByPrimaryKey(new Unita_organizzativaBulk(
							cd_uo_variazione));

			return (uo_variazione.getCd_tipo_unita()
					.equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE));
		} catch (it.cnr.jada.persistency.PersistencyException pe) {
			throw new ComponentException(pe);
		}
	}

	/**
	 * Verifica che il CDR associato alla variazione è eliminabile
	 * 
	 * Pre-post-conditions:
	 * 
	 * @param userContext
	 * @param assBulk
	 *            l'OggettoBulk della associazione CDR della variazione
	 * @return boolean
	 * 
	 **/
	public void validaAssociazioneCDRPerCancellazione(UserContext userContext,
			Ass_pdg_variazione_cdrBulk assBulk) throws ComponentException {
		try {
			Ass_pdg_variazione_cdrHome assHome = (Ass_pdg_variazione_cdrHome) getHome(
					userContext, Ass_pdg_variazione_cdrBulk.class);

			if (!assHome.findDettagliEntrata(assBulk).isEmpty()
					|| !assHome.findDettagliSpesa(assBulk).isEmpty())
				throw new ComponentException(
						"Non è possibile eliminare l'associazione della variazione con il CDR "
								+ assBulk.getCd_centro_responsabilita()
								+ " in quanto esistono dettagli di entrata/spesa collegati.");
		} catch (it.cnr.jada.persistency.PersistencyException pe) {
			throw new ComponentException(pe);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
	}

	private void controllaAggregatoPdgDetPositivo(UserContext userContext,
			Pdg_preventivo_detBulk pdg_dett)
			throws it.cnr.jada.comp.ComponentException {
		if (pdg_dett instanceof Pdg_preventivo_etr_detBulk)
			controllaAggregatoPdgEtrDetPositivo(userContext,
					(Pdg_preventivo_etr_detBulk) pdg_dett);
		else if (pdg_dett instanceof Pdg_preventivo_spe_detBulk)
			controllaAggregatoPdgSpeDetPositivo(userContext,
					(Pdg_preventivo_spe_detBulk) pdg_dett);
	}

	private void controllaAggregatoPdgEtrDetPositivo(UserContext userContext,
			Pdg_preventivo_etr_detBulk pdg_det)
			throws it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext),
					"{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB075.checkAggrPdgEtrDetPositivo(?, ?, ?, ?, ?, ?, ?, ? )}",
					false, this.getClass());
			try {
				cs.setInt(1, pdg_det.getEsercizio().intValue());
				cs.setString(2, pdg_det.getCd_centro_responsabilita());
				cs.setString(3, pdg_det.getCd_linea_attivita());
				cs.setString(4, pdg_det.getTi_appartenenza());
				cs.setString(5, pdg_det.getTi_gestione());
				cs.setString(6, pdg_det.getCd_elemento_voce());
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);
				cs.registerOutParameter(8, java.sql.Types.DECIMAL);
				cs.execute();
				String columnName = cs.getString(7);
				if (columnName != null) {
					String columnDescription = columnName;
					java.math.BigDecimal importo = cs.getBigDecimal(8);
					ColumnMapping mapping = getHome(userContext, pdg_det)
							.getColumnMap().getMappingForColumn(columnName);
					if (mapping != null) {
						FieldProperty property = BulkInfo.getBulkInfo(
								pdg_det.getClass()).getFieldProperty(
								mapping.getPropertyName());
						if (property != null)
							columnDescription = property.getLabel();
					}
					throw new it.cnr.jada.comp.ApplicationException(
							"Si sta tentando di inserire un dettaglio con importi negativi sulla colonna "
									+ columnDescription
									+ ", ma il totale per GAE e voce del piano è negativo ("
									+ new it.cnr.contab.util.EuroFormat()
											.format(importo) + ").");
				}
			} finally {
				cs.close();
			}
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private void controllaAggregatoPdgSpeDetPositivo(UserContext userContext,
			Pdg_preventivo_spe_detBulk pdg_det)
			throws it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext),
					"{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB075.checkAggrPdgSpeDetPositivo(?, ?, ?, ?, ?, ?, ?, ?, ? )}",
					false, this.getClass());
			try {
				cs.setInt(1, pdg_det.getEsercizio().intValue());
				cs.setString(2, pdg_det.getCd_centro_responsabilita());
				cs.setString(3, pdg_det.getCd_linea_attivita());
				cs.setString(4, pdg_det.getTi_appartenenza());
				cs.setString(5, pdg_det.getTi_gestione());
				cs.setString(6, pdg_det.getCd_elemento_voce());
				cs.setLong(7, pdg_det.getPg_spesa().longValue());
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);
				cs.registerOutParameter(9, java.sql.Types.DECIMAL);
				cs.execute();
				String columnName = cs.getString(8);
				if (columnName != null) {
					String columnDescription = columnName;
					java.math.BigDecimal importo = cs.getBigDecimal(9);
					ColumnMapping mapping = getHome(userContext, pdg_det)
							.getColumnMap().getMappingForColumn(columnName);
					if (mapping != null) {
						FieldProperty property = BulkInfo.getBulkInfo(
								pdg_det.getClass()).getFieldProperty(
								mapping.getPropertyName());
						if (property != null)
							columnDescription = property.getLabel();
					}
					throw new it.cnr.jada.comp.ApplicationException(
							"Si sta tentando di inserire un dettaglio con importi negativi sulla colonna "
									+ columnDescription
									+ ", ma il totale per GAE e voce del piano è negativo ("
									+ new it.cnr.contab.util.EuroFormat()
											.format(importo) + ").");
				}
			} finally {
				cs.close();
			}
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	protected BigDecimal nvl(BigDecimal imp) {
		if (imp != null)
			return imp;
		return ZERO;
	}

	protected BigDecimal sommaVariazioniPos(BigDecimal imp) {
		if (isImpPositivo(imp))
			return imp.abs();
		else
			return ZERO;
	}

	protected BigDecimal sommaVariazioniNeg(BigDecimal imp) {
		if (!isImpPositivo(imp))
			return imp.abs();
		else
			return ZERO;
	}

	/**
	 * Inizializza con valori di Default i campi delle tabelle coinvolte
	 * eventualmente non valorizzati dall'utente
	 * 
	 * Pre-post-conditions:
	 * 
	 * @param oggettobulk
	 *            l'OggettoBulk da eliminare
	 * @return void
	 * 
	 **/
	protected void inizializzaValoriDefaultCampi(OggettoBulk oggettobulk) {
		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) oggettobulk;

		if (pdg.getTipologia_fin() == null)
			pdg.setTipologia_fin(NaturaBulk.TIPO_NATURA_FONTI_INTERNE);

		if (pdg.getTipo_variazione() == null)
			pdg.setTipo_variazione(new Tipo_variazioneBulk(pdg.getEsercizio(),
					Tipo_variazioneBulk.NON_DEFINITO));
		else
			pdg.setTipologia(Tipo_variazioneBulk.NON_DEFINITO);

		for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
				.hasNext();) {
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
					.next();
			if (ass_pdg.getIm_entrata() == null)
				ass_pdg.setIm_entrata(ZERO);
			if (ass_pdg.getIm_spesa() == null)
				ass_pdg.setIm_spesa(ZERO);
		}
	}

	private void inizializzaSommeAZero(Pdg_variazioneBulk pdg) {
		pdg.setSomma_costi_diff(ZERO);
		pdg.setSomma_costi_var_meno(ZERO);
		pdg.setSomma_costi_var_piu(ZERO);
		pdg.setSomma_entrata_diff(ZERO);
		pdg.setSomma_entrata_var_meno(ZERO);
		pdg.setSomma_entrata_var_piu(ZERO);
		pdg.setSomma_ricavi_diff(ZERO);
		pdg.setSomma_ricavi_var_meno(ZERO);
		pdg.setSomma_ricavi_var_piu(ZERO);
		pdg.setSomma_spesa_diff(ZERO);
		pdg.setSomma_spesa_var_meno(ZERO);
		pdg.setSomma_spesa_var_piu(ZERO);
		for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
				.hasNext();) {
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
					.next();
			ass_pdg.setEntrata_ripartita(ZERO);
			ass_pdg.setEntrata_diff(ZERO);
			ass_pdg.setSpesa_ripartita(ZERO);
			ass_pdg.setSpesa_diff(ZERO);
		}
	}

	protected void inizializzaSommeDiSpesa(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException,
			IntrospectionException, PersistencyException {
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
				userContext, Pdg_variazioneBulk.class);
		for (java.util.Iterator spesa = testataHome.findDettagliSpesa(pdg)
				.iterator(); spesa.hasNext();) {
			Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spesa
					.next();
			pdg
					.setSomma_spesa_var_piu(pdg
							.getSomma_spesa_var_piu()
							.add(
									(sommaVariazioniPos(spesa_det
											.getIm_ri_ccs_spese_odc()))
											.add((sommaVariazioniPos(spesa_det
													.getIm_rj_ccs_spese_odc_altra_uo()))
													.add((sommaVariazioniPos(spesa_det
															.getIm_rk_ccs_spese_ogc()))
															.add((sommaVariazioniPos(spesa_det
																	.getIm_rl_ccs_spese_ogc_altra_uo()))
																	.add((sommaVariazioniPos(spesa_det
																			.getIm_rq_ssc_costi_odc()))
																			.add((sommaVariazioniPos(spesa_det
																					.getIm_rr_ssc_costi_odc_altra_uo()))
																					.add((sommaVariazioniPos(spesa_det
																							.getIm_rs_ssc_costi_ogc()))
																							.add((sommaVariazioniPos(spesa_det
																									.getIm_rt_ssc_costi_ogc_altra_uo())))))))))));
			pdg
					.setSomma_spesa_var_meno(pdg
							.getSomma_spesa_var_meno()
							.add(
									(sommaVariazioniNeg(spesa_det
											.getIm_ri_ccs_spese_odc()))
											.add((sommaVariazioniNeg(spesa_det
													.getIm_rj_ccs_spese_odc_altra_uo()))
													.add((sommaVariazioniNeg(spesa_det
															.getIm_rk_ccs_spese_ogc()))
															.add((sommaVariazioniNeg(spesa_det
																	.getIm_rl_ccs_spese_ogc_altra_uo()))
																	.add((sommaVariazioniNeg(spesa_det
																			.getIm_rq_ssc_costi_odc()))
																			.add((sommaVariazioniNeg(spesa_det
																					.getIm_rr_ssc_costi_odc_altra_uo()))
																					.add((sommaVariazioniNeg(spesa_det
																							.getIm_rs_ssc_costi_ogc()))
																							.add((sommaVariazioniNeg(spesa_det
																									.getIm_rt_ssc_costi_ogc_altra_uo())))))))))));

			pdg.setSomma_costi_var_piu(pdg.getSomma_costi_var_piu().add(
					(sommaVariazioniPos(spesa_det.getIm_rm_css_ammortamenti()))
							.add((sommaVariazioniPos(spesa_det
									.getIm_rn_css_rimanenze()))
									.add((sommaVariazioniPos(spesa_det
											.getIm_ro_css_altri_costi()))))));
			pdg.setSomma_costi_var_meno(pdg.getSomma_costi_var_meno().add(
					(sommaVariazioniNeg(spesa_det.getIm_rm_css_ammortamenti()))
							.add((sommaVariazioniNeg(spesa_det
									.getIm_rn_css_rimanenze()))
									.add((sommaVariazioniNeg(spesa_det
											.getIm_ro_css_altri_costi()))))));
		}
		pdg.setSomma_spesa_diff(pdg.getSomma_spesa_diff().add(
				pdg.getSomma_spesa_var_piu().subtract(
						pdg.getSomma_spesa_var_meno()).abs()));
		pdg.setSomma_costi_diff(pdg.getSomma_costi_diff().add(
				pdg.getSomma_costi_var_piu().subtract(
						pdg.getSomma_costi_var_meno()).abs()));
	}

	protected void inizializzaSommeDiEntrata(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException,
			IntrospectionException, PersistencyException {
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(
				userContext, Pdg_variazioneBulk.class);
		for (java.util.Iterator entrate = testataHome.findDettagliEntrata(pdg)
				.iterator(); entrate.hasNext();) {
			Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
					.next();
			pdg
					.setSomma_entrata_var_piu(pdg
							.getSomma_entrata_var_piu()
							.add(
									(sommaVariazioniPos(etr_det.getIm_ra_rce()))
											.add((sommaVariazioniPos(etr_det
													.getIm_rc_esr())))));
			pdg
					.setSomma_entrata_var_meno(pdg
							.getSomma_entrata_var_meno()
							.add(
									(sommaVariazioniNeg(etr_det.getIm_ra_rce()))
											.add((sommaVariazioniNeg(etr_det
													.getIm_rc_esr())))));

			pdg.setSomma_ricavi_var_piu(pdg.getSomma_ricavi_var_piu().add(
					(sommaVariazioniPos(etr_det.getIm_rb_rse()))));
			pdg.setSomma_ricavi_var_meno(pdg.getSomma_ricavi_var_meno().add(
					(sommaVariazioniNeg(etr_det.getIm_rb_rse()))));
		}
		pdg.setSomma_entrata_diff(pdg.getSomma_entrata_diff().add(
				pdg.getSomma_entrata_var_piu().subtract(
						pdg.getSomma_entrata_var_meno()).abs()));
		pdg.setSomma_ricavi_diff(pdg.getSomma_ricavi_diff().add(
				pdg.getSomma_ricavi_var_piu().subtract(
						pdg.getSomma_ricavi_var_meno()).abs()));
	}

	protected void validaDettagliEntrataSpesa(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		try {
			Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) oggettobulk;
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome) getHome(
					usercontext, Ass_pdg_variazione_cdrBulk.class);
			BigDecimal totSommaEntrata = ZERO;
			BigDecimal totSommaSpesa = ZERO;

			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();

				// Calcolo il totale delle entrate per il CDR selezionato e lo
				// confronto con il totale
				// assegnato
				BigDecimal sommaEntrata = ZERO;
				for (java.util.Iterator entrate = testataHome
						.findDettagliEntrata(ass_pdg).iterator(); entrate
						.hasNext();) {
					Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
							.next();
					sommaEntrata = (sommaEntrata.add((etr_det.getIm_ra_rce())
							.add((etr_det.getIm_rc_esr()))));
				}
				// Aggiorno il totalizzatore complessivo
				totSommaEntrata = totSommaEntrata.add(sommaEntrata);

				if (ass_pdg.getIm_entrata() != null) {
					if (ass_pdg.getIm_entrata().compareTo(sommaEntrata) < 0)
						throw new ApplicationException(
								"La Somma dei dettagli di entrata ("
										+ new it.cnr.contab.util.EuroFormat()
												.format(sommaEntrata)
										+ ")"
										+ "\n"
										+ "per il Cdr "
										+ ass_pdg.getCd_centro_responsabilita()
										+ " supera la quota di entrata assegnata di "
										+ new it.cnr.contab.util.EuroFormat()
												.format(sommaEntrata
														.subtract(ass_pdg
																.getIm_entrata())));
				}

				// Calcolo il totale delle spese per il CDR selezionato e lo
				// confronto con il totale
				// assegnato
				BigDecimal sommaSpesa = ZERO;
				for (java.util.Iterator spese = testataHome.findDettagliSpesa(
						ass_pdg).iterator(); spese.hasNext();) {
					Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spese
							.next();
					sommaSpesa = (sommaSpesa.add((spesa_det
							.getIm_ri_ccs_spese_odc()).add((spesa_det
							.getIm_rj_ccs_spese_odc_altra_uo()).add((spesa_det
							.getIm_rk_ccs_spese_ogc()).add((spesa_det
							.getIm_rl_ccs_spese_ogc_altra_uo()).add((spesa_det
							.getIm_rq_ssc_costi_odc()).add((spesa_det
							.getIm_rr_ssc_costi_odc_altra_uo()).add((spesa_det
							.getIm_rs_ssc_costi_ogc()).add((spesa_det
							.getIm_rt_ssc_costi_ogc_altra_uo()))))))))));
				}
				// Aggiorno il totalizzatore complessivo
				totSommaSpesa = totSommaSpesa.add(sommaSpesa);

				if (ass_pdg.getIm_spesa() != null) {
					if (ass_pdg.getIm_spesa().compareTo(sommaSpesa) < 0)
						throw new ApplicationException(
								"La Somma dei dettagli di spesa ("
										+ new it.cnr.contab.util.EuroFormat()
												.format(sommaSpesa)
										+ ")"
										+ "\n"
										+ "per il Cdr "
										+ ass_pdg.getCd_centro_responsabilita()
										+ " supera la quota di spesa assegnata di "
										+ new it.cnr.contab.util.EuroFormat()
												.format(sommaSpesa
														.subtract(ass_pdg
																.getIm_spesa())));
				}
			}
			if (totSommaEntrata.compareTo(totSommaSpesa) != 0
					&& !isUoPdgUoEnte(usercontext, pdg)) {
				throw new ApplicationException(
						"Il totale delle variazioni di spesa ("
								+ new it.cnr.contab.util.EuroFormat()
										.format(totSommaSpesa)
								+ ")"
								+ "\n"
								+ "non è uguale al totale delle variazioni di entrata ("
								+ new it.cnr.contab.util.EuroFormat()
										.format(totSommaEntrata) + ")");
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}

	public void inizializzaSommeCdR(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException {
		try {
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome) getHome(
					userContext, Ass_pdg_variazione_cdrBulk.class);
			for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
					.hasNext();) {
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
						.next();
				BigDecimal sommaEntrata = ZERO;
				for (java.util.Iterator entrate = testataHome
						.findDettagliEntrata(ass_pdg).iterator(); entrate
						.hasNext();) {
					Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
							.next();
					sommaEntrata = (sommaEntrata.add((etr_det.getIm_ra_rce())
							.add((etr_det.getIm_rc_esr()))));
				}
				ass_pdg.setEntrata_ripartita(sommaEntrata);
				ass_pdg.setEntrata_diff(nvl(ass_pdg.getIm_entrata()).subtract(
						sommaEntrata));
				BigDecimal sommaSpesa = ZERO;
				for (java.util.Iterator spese = testataHome.findDettagliSpesa(
						ass_pdg).iterator(); spese.hasNext();) {
					Pdg_preventivo_spe_detBulk spesa_det = (Pdg_preventivo_spe_detBulk) spese
							.next();
					sommaSpesa = (sommaSpesa.add((spesa_det
							.getIm_ri_ccs_spese_odc()).add((spesa_det
							.getIm_rj_ccs_spese_odc_altra_uo()).add((spesa_det
							.getIm_rk_ccs_spese_ogc()).add((spesa_det
							.getIm_rl_ccs_spese_ogc_altra_uo()).add((spesa_det
							.getIm_rq_ssc_costi_odc()).add((spesa_det
							.getIm_rr_ssc_costi_odc_altra_uo()).add((spesa_det
							.getIm_rs_ssc_costi_ogc()).add((spesa_det
							.getIm_rt_ssc_costi_ogc_altra_uo()))))))))));
				}
				ass_pdg.setSpesa_ripartita(sommaSpesa);
				ass_pdg.setSpesa_diff(nvl(ass_pdg.getIm_spesa()).subtract(
						sommaSpesa));
			}
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}

	/**
	 * default PreCondition: Viene richiesta la preparazione dei dati relativi
	 * all'Aggregato. PostCondition: Viene invocata la stored procedure
	 * CNRCTB050.inizializzaAggregatoPDG
	 */
	private void inizializzaAggregatoPDG(UserContext userContext,
			Ass_pdg_variazione_cdrBulk ass_pdg)
			throws it.cnr.jada.comp.ApplicationException,
			it.cnr.jada.comp.ComponentException {
		try {
			Pdg_preventivoHome home = (Pdg_preventivoHome) getHome(userContext,
					Pdg_preventivoBulk.class);
			Pdg_preventivoBulk pdg = (Pdg_preventivoBulk) home
					.findByPrimaryKey(new Pdg_preventivoBulk(ass_pdg
							.getEsercizio(), ass_pdg
							.getCd_centro_responsabilita()));
			getHomeCache(userContext).fetchAll(userContext, home);
			lockBulk(userContext, pdg);
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB050.inizializzaAggregatoPDG(?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, ass_pdg.getEsercizio());
				if ((ass_pdg.getCentro_responsabilita().getLivello().intValue() == 1)
						|| (ass_pdg.getCentro_responsabilita()
								.getCd_cdr_afferenza() == null)) {
					cs.setString(2, ass_pdg.getCd_centro_responsabilita());
				} else {
					cs.setString(2, ass_pdg.getCentro_responsabilita()
							.getCd_cdr_afferenza());
				}
				cs.setObject(3, userContext.getUser());
				cs.setObject(4, "Y");
				cs.execute();
			} finally {
				cs.close();
			}
		} catch (Throwable e) {
			throw handleException(ass_pdg, e);
		}
	}

	/**
	 * Tutti controlli superati PreCondition: Viene richiesto il ribaltamento
	 * dei costi del Piano di Gestione del CdR specificato all'area di ricerca a
	 * cui afferisce. Il bilancio del CNR è già stato approvato PostCondition:
	 * La procedura Oracle CNRCTB053.ribaltaSuAreaPDG viene eseguita per l'anno
	 * di esercizio ed il CdR specificati.
	 */
	protected void ribaltaCostiPdGArea(UserContext userContext,
			Integer esercizio, String cd_centro_responsabilita,
			Pdg_variazioneBulk pdg)
			throws it.cnr.jada.comp.ApplicationException,
			it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB053.ribaltaSuAreaPDGVar(?,?,?,?,?)}",
					false, this.getClass());
			cs.setObject(1, esercizio);
			cs.setString(2, cd_centro_responsabilita);
			cs.setObject(3, userContext.getUser());
			cs.setObject(4, pdg.getEsercizio());
			cs.setObject(5, pdg.getPg_variazione_pdg());
			try {
				lockBulk(userContext, pdg);
				cs.executeQuery();
			} catch (Throwable e) {
				throw handleException(pdg, e);
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

	/**
	 * Tutti controlli superati PreCondition: Viene richiesto il ribaltamento
	 * dei costi del Piano di Gestione del CdR specificato all'area di ricerca a
	 * cui afferisce. Il bilancio del CNR è già stato approvato PostCondition:
	 * La procedura Oracle CNRCTB053.ribaltaSuAreaPDG viene eseguita per l'anno
	 * di esercizio ed il CdR specificati.
	 */
	protected void gestioneVistoDipartimenti(UserContext userContext,
			Pdg_variazioneBulk pdg)
			throws it.cnr.jada.comp.ApplicationException,
			it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB053.setdavistaredip(?,?)}", false, this
							.getClass());
			cs.setObject(1, pdg.getEsercizio());
			cs.setObject(2, pdg.getPg_variazione_pdg());
			try {
				lockBulk(userContext, pdg);
				cs.executeQuery();
			} catch (Throwable e) {
				throw handleException(pdg, e);
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

	public void controllaBilancioPreventivoCdsApprovato(
			UserContext userContext, CdrBulk cdr) throws ComponentException {
		try {
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHome(
					userContext, Unita_organizzativaBulk.class)
					.findByPrimaryKey(cdr.getUnita_padre());
			Bilancio_preventivoBulk bilancio = (Bilancio_preventivoBulk) getHome(
					userContext, Bilancio_preventivoBulk.class)
					.findByPrimaryKey(
							new Bilancio_preventivoBulk(uo.getCd_unita_padre(),
									it.cnr.contab.utenze00.bp.CNRUserContext
											.getEsercizio(userContext),
									Elemento_voceHome.APPARTENENZA_CDS));
			if (bilancio == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"Bilancio preventivo inesistente per il cds "
								+ uo.getCd_unita_padre());
			if (!bilancio.STATO_C.equalsIgnoreCase(bilancio.getStato()))
				throw new it.cnr.jada.comp.ApplicationException(
						"Il bilancio preventivo del cds "
								+ uo.getCd_unita_padre()
								+ " deve essere approvato per registrare le variazioni");
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public CdrBulk cercaCdrPrimoLivello(UserContext userContext, CdrBulk cdr)
			throws ComponentException {
		if (cdr.getLivello().intValue() == 1)
			return cdr;
		else {
			try {
				return (CdrBulk) getHome(userContext, CdrBulk.class)
						.findByPrimaryKey(
								new CdrBulk(cdr.getCd_cdr_afferenza()));
			} catch (PersistencyException e) {
				throw handleException(e);
			}
		}
	}

	public SQLBuilder selectCentro_responsabilitaByClause(
			UserContext userContext, Ass_pdg_variazione_cdrBulk ass_pdg,
			CdrBulk cdr, CompoundFindClause clause) throws ComponentException,
			PersistencyException {
		SQLBuilder sql = getHome(userContext, CdrBulk.class, "V_CDR_VALIDO")
				.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext));
		if (clause != null)
			sql.addClause(clause);
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return sql;
	}

	public SQLBuilder selectCentro_responsabilitaByClause(
			UserContext userContext, Pdg_variazioneBulk pdg, CdrBulk cdr,
			CompoundFindClause clause) throws ComponentException,
			PersistencyException {
		SQLBuilder sql = getHome(userContext, CdrBulk.class, "V_CDR_VALIDO")
				.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext));
		if (clause != null)
			sql.addClause(clause);
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return sql;
	}

	public String controllaTotPropostoEntrataSpesa(
			it.cnr.jada.UserContext usercontext,
			it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk pdg)
			throws it.cnr.jada.comp.ComponentException {
		BigDecimal totSommaEntrata = ZERO;
		BigDecimal totSommaSpesa = ZERO;

		for (java.util.Iterator j = pdg.getAssociazioneCDR().iterator(); j
				.hasNext();) {
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) j
					.next();
			if (ass_pdg.getIm_entrata() != null)
				totSommaEntrata = totSommaEntrata.add(ass_pdg.getIm_entrata());
			if (ass_pdg.getIm_spesa() != null)
				totSommaSpesa = totSommaSpesa.add(ass_pdg.getIm_spesa());
		}
		if (totSommaEntrata.compareTo(totSommaSpesa) != 0) {
			return ("La quota di spesa assegnata ("
					+ new it.cnr.contab.util.EuroFormat().format(totSommaSpesa)
					+ ")"
					+ "\n"
					+ "non è uguale alla quota di entrata assegnata ("
					+ new it.cnr.contab.util.EuroFormat()
							.format(totSommaEntrata) + ")");
		}
		return null;
	}

	public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_pdg_variazioneBulk)
			return stampaConBulk(aUC, (Stampa_pdg_variazioneBulk) bulk);
		if (bulk instanceof Stampa_pdg_variazione_riepilogoBulk)
			return stampaConBulk(aUC,
					(Stampa_pdg_variazione_riepilogoBulk) bulk);
		if (bulk instanceof Stampa_var_stanz_resBulk)
			return stampaConBulk(aUC, (Stampa_var_stanz_resBulk) bulk);
		return bulk;
	}

	public OggettoBulk stampaConBulk(UserContext userContext,
			Stampa_pdg_variazioneBulk stampa) throws ComponentException {
		if (stampa.getPdg_variazioneForPrint() == null
				|| stampa.getPdg_variazioneForPrint().getPg_variazione_pdg() == null)
			throw new ApplicationException(
					"E'necessario scegliere una variazione");
		return stampa;
	}

	public OggettoBulk stampaConBulk(UserContext userContext,
			Stampa_pdg_variazione_riepilogoBulk stampa)
			throws ComponentException {
		try {
			if (stampa.getRiepilogovariazioni().isEmpty())
				throw new ApplicationException(
						"Selezionare almeno una variazione!");

			BigDecimal currentSequence = ZERO;
			BigDecimal pgStampa = getPgStampa(userContext);
			stampa.setPg_stampa(pgStampa);

			for (Iterator dettagli = stampa.getRiepilogovariazioni().iterator(); dettagli
					.hasNext();) {
				Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) dettagli
						.next();
				V_stm_paramin_pdg_variazioneBulk paramVariazione = new V_stm_paramin_pdg_variazioneBulk();
				paramVariazione
						.setUser(it.cnr.contab.utenze00.bp.CNRUserContext
								.getUser(userContext));
				paramVariazione.setId_report(pgStampa);
				paramVariazione.setChiave(pgStampa.toString());
				currentSequence = currentSequence.add(new java.math.BigDecimal(
						1));
				paramVariazione.setSequenza(currentSequence);
				paramVariazione.setPg_variazione(pdg_variazione
						.getPg_variazione_pdg());
				insertBulk(userContext, paramVariazione);
			}
			return stampa;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}

	public OggettoBulk stampaConBulk(UserContext userContext,
			Stampa_var_stanz_resBulk stampa) throws ComponentException {
		if (stampa.getPg_variazioneForPrint() == null
				|| stampa.getVariazioneForPrint().getPg_variazione() == null)
			throw new ApplicationException(
					"E' necessario scegliere una variazione");
		return stampa;
	}

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext userContext,
			it.cnr.jada.bulk.OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_pdg_variazioneBulk)
			inizializzaBulkPerStampa(userContext,
					(Stampa_pdg_variazioneBulk) bulk);
		else if (bulk instanceof Stampa_pdg_variazione_riepilogoBulk)
			inizializzaBulkPerStampa(userContext,
					(Stampa_pdg_variazione_riepilogoBulk) bulk);
		else if (bulk instanceof Stampa_var_stanz_resBulk)
			inizializzaBulkPerStampa(userContext,
					(Stampa_var_stanz_resBulk) bulk);
		else if (bulk instanceof Stampa_situazione_sintetica_x_progettoBulk)
			((Stampa_situazione_sintetica_x_progettoBulk)bulk).setEsercizio(CNRUserContext.getEsercizio(userContext));

		return bulk;
	}

	private void inizializzaBulkPerStampa(UserContext userContext,
			Stampa_pdg_variazione_riepilogoBulk stampa)
			throws ComponentException {
		stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
		stampa.setStato(stampa.STATO_TUTTI);
		stampa.setDataApprovazione_aEnabled(true);
		stampa.setDataApprovazione_daEnabled(true);
		// stampa.setCdsForPrint(CNRUserContext.getCd_cds(userContext));
		try {
			Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(
					userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) home
					.findByPrimaryKey(new Unita_organizzativaBulk(
							CNRUserContext
									.getCd_unita_organizzativa(userContext)));

			if (!uoScrivania.isUoCds()) {
				stampa.setUoForPrint(uoScrivania);
				stampa.setUoForPrintEnabled(true);
			} else {
				// stampa.setUoForPrint(new Unita_organizzativaBulk());
				stampa.setUoForPrintEnabled(false);
			}

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(stampa, e);
		}
		try {
			String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext
					.getCd_cds(userContext);

			CdsHome cdsHome = (CdsHome) getHome(userContext, CdsBulk.class);
			CdsBulk cds = (CdsBulk) cdsHome.findByPrimaryKey(new CdsBulk(
					cd_cds_scrivania));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
					userContext, Unita_organizzativa_enteBulk.class).findAll()
					.get(0);

			if (!cds.getCd_unita_organizzativa().equals(
					ente.getCd_unita_padre())) {
				stampa.setCdsForPrint(cds);
				stampa.setCdsForPrintEnabled(true);
			} else {
				// stampa.setCdsForPrint(new CdsBulk());
				stampa.setCdsForPrintEnabled(false);
			}

		} catch (it.cnr.jada.persistency.PersistencyException pe) {
			throw new ComponentException(pe);
		}
	}

	private void inizializzaBulkPerStampa(UserContext userContext,
			Stampa_pdg_variazioneBulk stampa) throws ComponentException {

		stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext));
	}

	private void inizializzaBulkPerStampa(UserContext userContext,
			Stampa_var_stanz_resBulk stampa) throws ComponentException {

		stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext));
	}

	public SQLBuilder selectCdsForPrintByClause(UserContext userContext,
			Stampa_pdg_variazione_riepilogoBulk stampa,
			it.cnr.contab.config00.sto.bulk.CdsBulk cds,
			CompoundFindClause clause) throws ComponentException,
			PersistencyException {
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO")
				.createSQLBuilder();
		sql.addClause(clause);
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,
				((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
						.getEsercizio());
		return sql;
	}

	public SQLBuilder selectUoForPrintByClause(UserContext userContext,
			Stampa_pdg_variazione_riepilogoBulk stampa,
			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo,
			CompoundFindClause clause) throws ComponentException,
			PersistencyException {
		if (clause == null)
			clause = uo.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, uo,
				"V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext));
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa
				.getCdsForPrint() == null ? null : stampa.getCdsForPrint()
				.getCd_unita_organizzativa());

		if (clause != null)
			sql.addClause(clause);
		return sql;
	}

	public SQLBuilder selectPdg_variazioneForPrintByClause(
			UserContext userContext, Stampa_pdg_variazioneBulk stampa,
			Pdg_variazioneBulk pgd_variazione, CompoundFindClause clauses)
			throws ComponentException, PersistencyException {

		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk) getHome(
				userContext, Unita_organizzativaBulk.class).findByPrimaryKey(
				new Unita_organizzativaBulk(CNRUserContext
						.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania
				.getCd_tipo_unita()
				.compareTo(
						it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0;

		Pdg_variazioneHome home = (Pdg_variazioneHome) getHome(userContext,
				pgd_variazione);

		SQLBuilder sql = home.createSQLBuilder();
		if (!isUoEnte) {

			sql.addSQLClause("AND", "PDG_VARIAZIONE.ESERCIZIO", sql.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
			sql.addTableToHeader("PDG_VARIAZIONE_RIGA_GEST");
			sql.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO",
					"PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
			sql.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG",
					"PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");
			sql.addSQLClause("AND",
					"PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO", sql.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getCd_cdr());
			sql.setDistinctClause(true);
		} else
			sql.addSQLClause("AND", "PDG_VARIAZIONE.ESERCIZIO", sql.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());

		sql.addClause(clauses);

		return sql;
	}

	public SQLBuilder selectPdg_variazioneForPrintByClause(
			UserContext userContext,
			Stampa_pdg_variazione_riepilogoBulk stampa,
			Pdg_variazioneBulk pgd_variazione, CompoundFindClause clauses)
			throws ComponentException, PersistencyException {

		Pdg_variazioneHome home = (Pdg_variazioneHome) getHome(userContext,
				pgd_variazione);

		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
				((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
						.getEsercizio());

		sql.addClause(clauses);

		return sql;
	}

	public boolean isCdsAbilitatoAdApprovare(UserContext userContext,
			String cd_cds, Pdg_variazioneBulk pdg) throws ComponentException {
		try {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk) getHome(
					userContext, Parametri_cdsBulk.class)
					.findByPrimaryKey(
							new Parametri_cdsBulk(
									cd_cds,
									((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
											.getEsercizio()));
			if (!param_cds.getFl_approva_var_pdg().booleanValue())
				return false;
			String tipo_var_pdg = tipoVariazione(userContext, pdg);
			for (Iterator lista = ((Configurazione_cnrHome) getHome(
					userContext, Configurazione_cnrBulk.class))
					.findTipoVariazioniPdg().iterator(); lista.hasNext();) {
				Configurazione_cnrBulk config_cnr = (Configurazione_cnrBulk) lista
						.next();
				if (config_cnr.getVal01().equalsIgnoreCase(tipo_var_pdg))
					return true;
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return false;
	}

	protected String tipoVariazione(UserContext userContext,
			Pdg_variazioneBulk pdg)
			throws it.cnr.jada.comp.ApplicationException,
			it.cnr.jada.comp.ComponentException {
		if (this instanceof CRUDPdgVariazioneGestionaleComponent)
			return pdg.getTipologia();

		String tipo = null;
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{? = call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB000.TIPO_VAR_PDG(?,?)}", false, this
							.getClass());
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setObject(2, pdg.getEsercizio());
			cs.setObject(3, pdg.getPg_variazione_pdg());
			try {
				cs.executeQuery();
				tipo = cs.getString(1);
			} catch (Throwable e) {
				throw handleException(pdg, e);
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			throw handleSQLException(e);
		}
		return tipo;
	}

	// 28/12/2005 richiamo la fuzione che restituisce la tipologia della
	// variazione
	public String getTipoVariazione(UserContext userContext,
			Pdg_variazioneBulk tipovar)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		String pg = null;

		try {
			cs = new LoggableStatement(getConnection(userContext),
					"{ ? = call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB000.TIPO_VAR_PDG(?,?) }", false, this
							.getClass());
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setInt(2, tipovar.getEsercizio().intValue());
			cs.setInt(3, tipovar.getPg_variazione_pdg().intValue());
			cs.executeQuery();
			pg = new String(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null)
				try {
					cs.close();
				} catch (SQLException e1) {
					throw handleException(e1);
				}
		}
		return pg;

	}

	public String getDesTipoVariazione(UserContext userContext,
			Pdg_variazioneBulk tipovar)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		String var = null;
		String pg = getTipoVariazione(userContext, tipovar);

		try {
			cs = new LoggableStatement(getConnection(userContext),
					"{ ? = call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB000.DESCR_TIPO_VAR_PDG(?)}", false, this
							.getClass());
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, pg);
			cs.executeQuery();
			if (cs.getString(1) != null)
				var = new String(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null)
				try {
					cs.close();
				} catch (SQLException e1) {
					throw handleException(e1);
				}
		}
		return var;

	}

	protected String getVoce_FdaEV(UserContext userContext, Integer aEsercizio,
			String aTI_APPARTENENZA, String aTI_GESTIONE,
			String aCD_ELEMENTO_VOCE, String acd_centro_responsabilita,
			String acd_linea_attivita)
			throws it.cnr.jada.comp.ComponentException {
		LoggableStatement cs = null;
		String voce_f = null;
		try {
			cs = new LoggableStatement(getConnection(userContext),
					"{ ? = call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB053.getVoce_FdaEV(?,?,?,?,?,?)}", false,
					this.getClass());
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setObject(2, aEsercizio);
			cs.setObject(3, aTI_APPARTENENZA);
			cs.setObject(4, aTI_GESTIONE);
			cs.setObject(5, aCD_ELEMENTO_VOCE);
			cs.setObject(6, acd_centro_responsabilita);
			cs.setObject(7, acd_linea_attivita);
			cs.executeQuery();
			voce_f = new String(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null)
				try {
					cs.close();
				} catch (SQLException e1) {
					throw handleException(e1);
				}
		}
		return voce_f;
	}

	public it.cnr.jada.bulk.OggettoBulk statoPrecedente(
			UserContext userContext, it.cnr.jada.bulk.OggettoBulk oggettoBulk)
			throws ComponentException {
		Pdg_variazioneBulk var = (Pdg_variazioneBulk) oggettoBulk;
		var.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_PROVVISORIA);
		var.setDt_chiusura(null);
		var.setToBeUpdated();
		var = (Pdg_variazioneBulk) super.modificaConBulk(userContext, var);
		return var;
	}

	public List findTipologie_variazione(UserContext userContext,
			Pdg_variazioneBulk bulk) throws it.cnr.jada.comp.ComponentException {
		List lista;
		String cd_uo_variazione = null;
		try {
			Tipo_variazioneHome home = (Tipo_variazioneHome) getHome(
					userContext, Tipo_variazioneBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause("AND", "esercizio", SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());

			if (!bulk.isBulkforSearch()) {
				if (bulk.getCentro_responsabilita() != null
						&& bulk.getCentro_responsabilita()
								.getCd_centro_responsabilita() != null) {
					CdrHome cdrhome = (CdrHome) getHome(userContext,
							CdrBulk.class);
					CdrBulk cdr = (CdrBulk) cdrhome
							.findByPrimaryKey(new CdrBulk(bulk
									.getCentro_responsabilita()
									.getCd_centro_responsabilita()));
					cd_uo_variazione = cdr.getUnita_padre()
							.getCd_unita_organizzativa();
				} else
					cd_uo_variazione = CNRUserContext
							.getCd_unita_organizzativa(userContext);

				Unita_organizzativaHome unita_organizzativa_home = (Unita_organizzativaHome) getHome(
						userContext, Unita_organizzativaBulk.class);
				Unita_organizzativaBulk uo_variazione = (Unita_organizzativaBulk) unita_organizzativa_home
						.findByPrimaryKey(new Unita_organizzativaBulk(
								cd_uo_variazione));

				if (uo_variazione.getCd_tipo_unita().equals(
						Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
					sql.addClause("AND", "fl_utilizzabile_ente",
							SQLBuilder.EQUALS, Boolean.TRUE);
				else if (uo_variazione.getCd_tipo_unita().equals(
						Tipo_unita_organizzativaHome.TIPO_UO_AREA))
					sql.addClause("AND", "fl_utilizzabile_area",
							SQLBuilder.EQUALS, Boolean.TRUE);
				else
					sql.addClause("AND", "fl_utilizzabile_cds",
							SQLBuilder.EQUALS, Boolean.TRUE);
			}

			sql.addOrderBy("ds_tipo_variazione");
			Broker broker = home.createBroker(sql);
			lista = home.fetchAll(broker);
			broker.close();
		} catch (it.cnr.jada.persistency.PersistencyException pe) {
			throw new ComponentException(pe);
		} catch (Exception e) {
			throw handleException(e);
		}
		return lista;
	}

	/**
	 * getPgStampa method comment.
	 */
	private java.math.BigDecimal getPgStampa(it.cnr.jada.UserContext userContext)
			throws it.cnr.jada.comp.ComponentException {

		// ricavo il progressivo unico pg_stampa
		return getSequence(userContext);
	}

	/**
	 * Identificativo univoco progressivo per la stampa del riepilogo variazioni
	 * PreCondition: Viene richiesta un progressivo PostCondition: ritorna un
	 * valore PreCondition: Si è verificato un errore. PostCondition: Viene
	 * inviato un messaggio con il relativo errore ritornato dal DB
	 */
	private java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext)
			throws it.cnr.jada.comp.ComponentException {

		// ricavo il progressivo unico pg_stampa
		java.math.BigDecimal pg_Stampa = new java.math.BigDecimal(0);
		try {
			LoggableStatement ps = new LoggableStatement(
					getConnection(userContext),
					"select IBMSEQ00_STAMPA.nextval from dual", true, this
							.getClass());
			try {
				java.sql.ResultSet rs = ps.executeQuery();
				try {
					if (rs.next())
						pg_Stampa = rs.getBigDecimal(1);
				} finally {
					try {
						rs.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
		return pg_Stampa;
	}

	private void inizializzaVistosuAssociazioneCDR(UserContext userContext,
			Pdg_variazioneBulk pdg) throws ComponentException {
		try {
			if (!pdg.isApprovata())
				return;

			Ass_pdg_variazione_cdrHome assHome = (Ass_pdg_variazione_cdrHome) getHome(
					userContext, Ass_pdg_variazione_cdrBulk.class);
			UtenteBulk utente = (UtenteBulk) getHome(userContext,
					UtenteBulk.class).findByPrimaryKey(
					new UtenteKey(it.cnr.contab.utenze00.bp.CNRUserContext
							.getUser(userContext)));

			Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(
					userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) home
					.findByPrimaryKey(new Unita_organizzativaBulk(
							CNRUserContext
									.getCd_unita_organizzativa(userContext)));

			if (uoScrivania.getCd_tipo_unita().equals(
					Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
				for (java.util.Iterator assIt = pdg.getAssociazioneCDR()
						.iterator(); assIt.hasNext();) {
					Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk) assIt
							.next();
					ass_pdg.setFl_visto_da_apporre(Boolean.FALSE);
					ass_pdg.setFl_riga_vistabile(Boolean.FALSE);

					for (java.util.Iterator dipIt = assHome
							.findDipartimentiAssociati(ass_pdg).iterator(); dipIt
							.hasNext();) {
						DipartimentoBulk dip = (DipartimentoBulk) dipIt.next();
						if (utente.getDipartimento() != null
								&& utente.getDipartimento()
										.getCd_dipartimento() != null
								&& dip.getCd_dipartimento().equals(
										utente.getDipartimento()
												.getCd_dipartimento())) {
							ass_pdg.setFl_riga_vistabile(Boolean.TRUE);

							for (java.util.Iterator detDipIt = assHome
									.findDettagliDipartimento(ass_pdg, dip)
									.iterator(); detDipIt.hasNext();) {
								if (((Pdg_variazione_riga_gestBulk) detDipIt
										.next()).getFl_visto_dip_variazioni()
										.equals(Boolean.FALSE)) {
									ass_pdg
											.setFl_visto_da_apporre(Boolean.TRUE);
									break;
								}
							}
							break;
						} else if (utente.getDipartimento() == null
								|| utente.getDipartimento()
										.getCd_dipartimento() == null) {
							/**
							 * Il flag viene impostato a "TRUE" quando accede un
							 * utente di tipo ENTE senza dipartimento per
							 * consentire a chi deve dare "APPROVAZIONE FORMALE"
							 * di conoscere le righe della variazione ancora in
							 * attesa di apposizione di visto da parte dei
							 * dipartimenti.
							 */
							ass_pdg.setFl_riga_vistabile(Boolean.TRUE);

							for (java.util.Iterator detDipIt = assHome
									.findDettagliDipartimento(ass_pdg, dip)
									.iterator(); detDipIt.hasNext();) {
								if (((Pdg_variazione_riga_gestBulk) detDipIt
										.next()).getFl_visto_dip_variazioni()
										.equals(Boolean.FALSE)) {
									ass_pdg
											.setFl_visto_da_apporre(Boolean.TRUE);
									break;
								}
							}
						}
					}
				}
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Metodo per apporre il visto sulla variazione al o sui suoi dettagli
	 * 
	 * @param userContext
	 * @param bulk
	 *            - deve essere una istanza di Pdg_variazioneBulk,
	 *            Ass_pdg_variazione_cdrBulk o Pdg_variazione_riga_gestBulk
	 * @param dip
	 *            - il DipartimentoBulk che deve apporre il visto
	 * @return il Pdg_variazioneBulk, Ass_pdg_variazione_cdrBulk o
	 *         Pdg_variazione_riga_gestBulk vistato
	 * @throws ComponentException
	 */
	public OggettoBulk apponiVistoDipartimento(UserContext userContext,
			OggettoBulk bulk, DipartimentoBulk dip) throws ComponentException {
		if (bulk instanceof Pdg_variazioneBulk)
			return apponiVistoDipartimento(userContext,
					(Pdg_variazioneBulk) bulk, dip, true);
		else if (bulk instanceof Ass_pdg_variazione_cdrBulk)
			return apponiVistoDipartimento(userContext,
					(Ass_pdg_variazione_cdrBulk) bulk, dip, true);
		else if (bulk instanceof Pdg_variazione_riga_gestBulk)
			return apponiVistoDipartimento(userContext,
					(Pdg_variazione_riga_gestBulk) bulk, dip, true);
		else
			return bulk;
	}

	private Pdg_variazioneBulk apponiVistoDipartimento(UserContext userContext,
			Pdg_variazioneBulk pdg, DipartimentoBulk dip,
			boolean aggiornaTestata) throws ComponentException {
		try {
			Pdg_variazioneHome home = (Pdg_variazioneHome) getHome(userContext,
					Pdg_variazioneBulk.class);
			for (Iterator dett = home.findAssociazioneCDR(pdg).iterator(); dett
					.hasNext();)
				apponiVistoDipartimento(userContext,
						(Ass_pdg_variazione_cdrBulk) dett.next(), dip, false);

			if (aggiornaTestata)
				apponiVistoDipartimentoSuTestata(userContext, pdg);

			return pdg;
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		} catch (IntrospectionException e) {
			throw handleException(e);
		}
	}

	private Ass_pdg_variazione_cdrBulk apponiVistoDipartimento(
			UserContext userContext, Ass_pdg_variazione_cdrBulk ass,
			DipartimentoBulk dip, boolean aggiornaTestata)
			throws ComponentException {
		try {
			Ass_pdg_variazione_cdrHome home = (Ass_pdg_variazione_cdrHome) getHome(
					userContext, Ass_pdg_variazione_cdrBulk.class);
			for (Iterator dett = home
					.findDettagliSpesaVariazioneGestionale(ass).iterator(); dett
					.hasNext();)
				apponiVistoDipartimento(userContext,
						(Pdg_variazione_riga_gestBulk) dett.next(), dip, false);

			for (Iterator dett = home.findDettagliEntrataVariazioneGestionale(
					ass).iterator(); dett.hasNext();)
				apponiVistoDipartimento(userContext,
						(Pdg_variazione_riga_gestBulk) dett.next(), dip, false);

			if (aggiornaTestata)
				apponiVistoDipartimentoSuTestata(userContext, ass
						.getPdg_variazione());

			return ass;
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		}
	}

	private Pdg_variazione_riga_gestBulk apponiVistoDipartimento(
			UserContext userContext, Pdg_variazione_riga_gestBulk riga,
			DipartimentoBulk dip, boolean aggiornaTestata)
			throws ComponentException {
		try {
			WorkpackageHome lineaHome = (WorkpackageHome) getHome(userContext,
					WorkpackageBulk.class);
			if (!riga.getFl_visto_dip_variazioni()
					&& (lineaHome.findDipartimento(userContext, riga
							.getLinea_attivita()).equalsByPrimaryKey(dip))) {
				riga.setFl_visto_dip_variazioni(Boolean.TRUE);
				riga.setToBeUpdated();
				updateBulk(userContext, riga);
				if (aggiornaTestata)
					apponiVistoDipartimentoSuTestata(userContext, riga
							.getPdg_variazione());
			}
			return riga;
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		}
	}

	private Pdg_variazioneBulk apponiVistoDipartimentoSuTestata(
			UserContext userContext, Pdg_variazioneBulk pdg)
			throws ComponentException {
		try {
			Pdg_variazioneHome home = (Pdg_variazioneHome) getHome(userContext,
					Pdg_variazioneBulk.class);
			if (home.findDettagliVariazioneGestionaleDaVistare(pdg).isEmpty()) {
				pdg.setFl_visto_dip_variazioni(Boolean.TRUE);
				pdg.setToBeUpdated();
				updateBulk(userContext, pdg);
			}
			return pdg;
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		}
	}

	public SQLBuilder selectVariazioneForPrintByClause(UserContext userContext,
			Stampa_var_stanz_resBulk stampa, Var_stanz_resBulk variazione,
			CompoundFindClause clauses) throws ComponentException,
			PersistencyException {

		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk) getHome(
				userContext, Unita_organizzativaBulk.class).findByPrimaryKey(
				new Unita_organizzativaBulk(CNRUserContext
						.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania
				.getCd_tipo_unita()
				.compareTo(
						it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0;

		Var_stanz_resHome home = (Var_stanz_resHome) getHome(userContext,
				variazione);

		SQLBuilder sql = home.createSQLBuilder();
		if (isUoEnte)  
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
		else if(!uoScrivania.isUoCds()) {
			sql.addSQLClause("AND", "VAR_STANZ_RES.ESERCIZIO", SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
			sql.addTableToHeader("VAR_STANZ_RES_RIGA");
			sql.addSQLJoin("VAR_STANZ_RES.ESERCIZIO",
					"VAR_STANZ_RES_RIGA.ESERCIZIO");
			sql.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE",
					"VAR_STANZ_RES_RIGA.PG_VARIAZIONE");
			sql.addSQLClause("AND",
					"VAR_STANZ_RES_RIGA.CD_CDR", sql.STARTSWITH,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getCd_unita_organizzativa());
		} else if (uoScrivania.isUoCds()) {
			sql.addSQLClause("AND", "VAR_STANZ_RES.ESERCIZIO", SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
			sql.addTableToHeader("VAR_STANZ_RES_RIGA");
			sql.addSQLJoin("VAR_STANZ_RES.ESERCIZIO",
					"VAR_STANZ_RES_RIGA.ESERCIZIO");
			sql.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE",
					"VAR_STANZ_RES_RIGA.PG_VARIAZIONE");
			sql.addSQLClause("AND",
					"VAR_STANZ_RES_RIGA.CD_CDR", sql.STARTSWITH,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getCd_cds());
		} 	
		sql.setDistinctClause(true);			
		sql.addClause(clauses);
		
		return sql;
	}

	public CompoundFindClause aggiornaClausole(UserContext context,
			Pdg_variazioneBulk pdg, String tipo) throws ComponentException {
		CompoundFindClause clausesModulo = new CompoundFindClause();
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(context,
				Pdg_variazioneBulk.class);
		try {
			if (tipo.compareTo(Elemento_voceHome.GESTIONE_SPESE) == 0) {
				for (java.util.Iterator spese = testataHome.findDettagliSpesa(
						pdg).iterator(); spese.hasNext();) {
					Pdg_preventivo_spe_detBulk spe_det = (Pdg_preventivo_spe_detBulk) spese
							.next();
					WorkpackageBulk wp = (WorkpackageBulk) getHome(context,
							WorkpackageBulk.class).findByPrimaryKey(
							new WorkpackageBulk(spe_det
									.getCd_centro_responsabilita(), spe_det
									.getCd_linea_attivita()));
					if (!(wp.getPg_progetto() == null)) {
						ProgettoBulk pg = (ProgettoBulk) getHome(context,
								ProgettoBulk.class)
								.findByPrimaryKey(
										new ProgettoBulk(
												((it.cnr.contab.utenze00.bp.CNRUserContext) context)
														.getEsercizio(),
												wp.getPg_progetto(),
												ProgettoBulk.TIPO_FASE_PREVISIONE));
						clausesModulo.addClause("OR", "modulo",
								SQLBuilder.EQUALS, pg.getCd_progetto());
					}
				}
			} else {
				for (java.util.Iterator entrate = testataHome
						.findDettagliEntrata(pdg).iterator(); entrate.hasNext();) {
					Pdg_preventivo_etr_detBulk etr_det = (Pdg_preventivo_etr_detBulk) entrate
							.next();
					WorkpackageBulk wp = (WorkpackageBulk) getHome(context,
							WorkpackageBulk.class).findByPrimaryKey(
							new WorkpackageBulk(etr_det
									.getCd_centro_responsabilita(), etr_det
									.getCd_linea_attivita()));
					if (!(wp.getPg_progetto() == null)) {
						ProgettoBulk pg = (ProgettoBulk) getHome(context,
								ProgettoBulk.class)
								.findByPrimaryKey(
										new ProgettoBulk(
												((it.cnr.contab.utenze00.bp.CNRUserContext) context)
														.getEsercizio(),
												wp.getPg_progetto(),
												ProgettoBulk.TIPO_FASE_PREVISIONE));
						clausesModulo.addClause("OR", "modulo",
								SQLBuilder.EQUALS, pg.getCd_progetto());
					}
				}
			}
			return clausesModulo;
		} catch (IntrospectionException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public RemoteIterator cercaVariazioniForDocumentale(
			UserContext usercontext, CompoundFindClause compoundfindclause,
			OggettoBulk oggettobulk, String tiSigned,
			Boolean clausolaIn)
			throws ComponentException {
		try {
			return iterator(usercontext, selectVariazioniForDocumentale(
					usercontext, compoundfindclause, oggettobulk,tiSigned, clausolaIn),
					oggettobulk.getClass(), getFetchPolicyName("find"));
		} catch (Throwable throwable) {
			throw handleException(throwable);
		}
	}

	private Query selectVariazioniForDocumentale(UserContext userContext,
			CompoundFindClause clauses, OggettoBulk bulk, String tiSigned,
				Boolean clausolaIn)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		try{
		SQLBuilder sql = selectBase(userContext, clauses, bulk);
		// RosPuc 28/01/2011
//		sql.openParenthesis(FindClause.AND);
//		sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
//		sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
//		sql.closeParenthesis();
		if(tiSigned ==null && clausolaIn){
			sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			sql.closeParenthesis();
		}
		else if (tiSigned ==null && !clausolaIn){
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
		}
		else if(tiSigned.compareTo(ArchiviaStampaPdgVariazioneBulk.VIEW_APPROVED)==0){
			sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			sql.closeParenthesis();
		}
		else if(tiSigned.compareTo(ArchiviaStampaPdgVariazioneBulk.VIEW_ALL) ==0){
			sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
			sql.closeParenthesis();
		}
		else
		{
			sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
		}

		Unita_organizzativaBulk cdsBulk = null;
		String uo = null;
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(
				userContext, Unita_organizzativa_enteBulk.class).findAll()
				.get(0);
		String cds = CNRUserContext.getCd_cds(userContext);
		cdsBulk = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).
				findByPrimaryKey(new Unita_organizzativaBulk(cds));

		if (!((CNRUserContext) userContext).getCd_unita_organizzativa()
				.equals(ente.getCd_unita_organizzativa())) {
			sql.addClause(FindClause.AND, "cd_centro_responsabilita", SQLBuilder.STARTSWITH, cds);
			 if(Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(cdsBulk.getCd_tipo_unita()) &&
					 bulk !=null && bulk instanceof Pdg_variazioneBulk  && ((Pdg_variazioneBulk)bulk).getPg_variazione_pdg()!=null){
				 Pdg_variazioneHome Pdg_variazioneHome = (Pdg_variazioneHome) getHome(userContext, Pdg_variazioneBulk.class);
				 Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) Pdg_variazioneHome.findByPrimaryKey((Pdg_variazioneBulk)bulk);
				if (pdg!=null && pdg.getCentro_responsabilita()!=null && pdg.getCd_centro_responsabilita()!=null){
					CdrHome cdrHome = (CdrHome) getHome(userContext, CdrBulk.class);
					CdrBulk cdrVar = (CdrBulk) cdrHome.findByPrimaryKey(new CdrKey(pdg.getCd_centro_responsabilita()));
					uo = cdrVar.getCd_unita_organizzativa();
					sql.addClause(FindClause.AND, "cd_centro_responsabilita", SQLBuilder.STARTSWITH, uo);
				}
			}
			else{	
				if (Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(cdsBulk.getCd_tipo_unita())){
					uo = CNRUserContext.getCd_unita_organizzativa(userContext);
					sql.addClause(FindClause.AND, "cd_centro_responsabilita", SQLBuilder.STARTSWITH, uo);
				}
			}
		}
		Long variazionePdg = null;
		if (bulk != null && bulk instanceof Pdg_variazioneBulk ){
			variazionePdg = ((Pdg_variazioneBulk)bulk).getPg_variazione_pdg();
		}
		List<Integer> variazioniPresentiSulDocumentale = variazioniPresentiSulDocumentale(userContext, tiSigned, cdsBulk, uo, variazionePdg);
		if (tiSigned != null) {
			sql.addClause(FindClause.AND, "dt_firma", tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED) ? SQLBuilder.ISNOTNULL : SQLBuilder.ISNULL, null);
		}
		if (clausolaIn && variazioniPresentiSulDocumentale.isEmpty())
			sql.addClause(FindClause.AND, "pg_variazione_pdg", SQLBuilder.EQUALS, -1);
		sql.openParenthesis(FindClause.AND);
		for (Integer numeroVariazione : variazioniPresentiSulDocumentale) {
			if (clausolaIn)
				sql.addClause(FindClause.OR, "pg_variazione_pdg", SQLBuilder.EQUALS, numeroVariazione);
			else
				sql.addClause(FindClause.AND, "pg_variazione_pdg", SQLBuilder.NOT_EQUALS, numeroVariazione);
		}
		sql.closeParenthesis();
		return sql;
		}catch (Exception e) {
			throw new ComponentException(e);
	}
	}

	private List<Integer> variazioniPresentiSulDocumentale(UserContext userContext, String tiSigned, Unita_organizzativaBulk cds, String uo, Long variazionePdg) throws ComponentException, PersistencyException{
		PdgVariazioniService pdgVariazioniService = SpringUtil.getBean("pdgVariazioniService",
				PdgVariazioniService.class);
		return pdgVariazioniService.findVariazioniPresenti(CNRUserContext.getEsercizio(userContext),tiSigned, cds, uo, variazionePdg);
	}
	
//	per la stampa delle variazioni residuo conto terzi
	public byte[] lanciaStampa(UserContext userContext, Integer esercizio,Integer pgVariazione, String tipo_variazione) 
			throws PersistencyException, ComponentException, RemoteException, javax.ejb.EJBException {

		String cdrPersonale = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(esercizio))
				.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale per l'esercizio "+esercizio+"."));

		if (tipo_variazione.equals("R")){
			V_var_stanz_resHome home = (V_var_stanz_resHome) getHome(userContext, V_var_stanz_resBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				
				sql.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
				sql.addSQLClause(FindClause.AND, "PG_VARIAZIONE", SQLBuilder.EQUALS, pgVariazione);
				sql.addSQLClause(FindClause.AND, "CD_CDR_ASSEGNATARIO", SQLBuilder.EQUALS, cdrPersonale);
				sql.addSQLClause(FindClause.AND, "STATO", SQLBuilder.EQUALS, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk.STATO_APPROVATA);
								
				java.util.List list = home.fetchAll(sql);
				if(list.isEmpty())
					throw new FatturaNonTrovataException("Variazione non trovata");
				
			try {
			      File output = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", File.separator + getOutputFileName("stampa_var_stanziamento_res.jasper",esercizio,pgVariazione));
			      Print_spoolerBulk print = new Print_spoolerBulk();
			      print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			      print.setFlEmail(false);
			      print.setReport("/cnrpreventivo/pdg/stampa_var_stanziamento_res.jasper");
			      print.setNomeFile(getOutputFileName("stampa_var_stanziamento_res.jasper", esercizio, pgVariazione));
			      print.setUtcr(userContext.getUser());
			      print.addParam("Esercizio", esercizio, Integer.class);
			      print.addParam("Variazione",pgVariazione, Integer.class);
			      Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);
			      
			      FileOutputStream f = new FileOutputStream(output);
			      	
			      f.write(report.getBytes());
			      f.flush();
				  f.close();
				  
				   return report.getBytes();
				}  catch (IOException e) {
					throw new GenerazioneReportException("Generazione Stampa non riuscita",e);
				}

			}
		
		if (tipo_variazione.equals("C")){
			
			Configurazione_cnrHome homeCDR = (Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class);
			
				Pdg_variazioneHome home = (Pdg_variazioneHome) getHome(userContext, Pdg_variazioneBulk.class);
				SQLBuilder sql =  home.createSQLBuilder();
					sql.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.ESERCIZIO", SQLBuilder.EQUALS, esercizio);
					sql.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.PG_VARIAZIONE_PDG", SQLBuilder.EQUALS, pgVariazione);
				
				sql.addTableToHeader("PDG_VARIAZIONE_RIGA_GEST");
				sql.addSQLClause(FindClause.AND, "PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO", SQLBuilder.EQUALS, cdrPersonale);
				sql.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
				sql.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");
				sql.openParenthesis(FindClause.AND);
					sql.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
					sql.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
				sql.closeParenthesis();
				
				java.util.List list = home.fetchAll(sql);
				if(list.isEmpty())
					throw new FatturaNonTrovataException("Variazione non trovata");
				
			try {
			      File output = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", File.separator + getOutputFileName("stampa_variazioni_pdg.jasper",esercizio,pgVariazione));
			      Print_spoolerBulk print = new Print_spoolerBulk();
			      print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			      print.setFlEmail(false);
			      print.setReport("/cnrpreventivo/pdg/stampa_variazioni_pdg.jasper");
			      print.setNomeFile(getOutputFileName("stampa_variazioni_pdg.jasper", esercizio, pgVariazione));
			      print.setUtcr(userContext.getUser());
			      print.addParam("Esercizio", esercizio, Integer.class);
			      print.addParam("Variazione",pgVariazione, Integer.class);
			      Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);
			      
			      FileOutputStream f = new FileOutputStream(output);
			      	
			      f.write(report.getBytes());
			      f.flush();
				  f.close();
				  
				   return report.getBytes();
				}  catch (IOException e) {
					throw new GenerazioneReportException("Generazione Stampa non riuscita",e);
				}
			}
		
	return null;
	}
	
	private static final DateFormat PDF_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private String getOutputFileName(String reportName,Integer esercizio,Integer pgVariazione)
		{
			String fileName = reportName;
			fileName = fileName.replace('/', '_');
			fileName = fileName.replace('\\', '_');
			if(fileName.startsWith("_"))
			    fileName = fileName.substring(1);
			if(fileName.endsWith(".jasper"))
			    fileName = fileName.substring(0, fileName.length() - 7);
			fileName = fileName + ".pdf";
			fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + fileName + '_' + esercizio + '_' +  pgVariazione;
			return fileName;
		}
	public void aggiornaDataFirma(UserContext userContext, Integer esercizio,
			Integer numeroVariazione) throws  ComponentException {
		try{
			Pdg_variazioneBulk varPdg = (Pdg_variazioneBulk) getHome(userContext, Pdg_variazioneBulk.class).
					findByPrimaryKey(new Pdg_variazioneBulk(esercizio,new Long(numeroVariazione)));
			varPdg.setToBeUpdated();
			varPdg.setDt_firma(EJBCommonServices.getServerDate());
			varPdg = (Pdg_variazioneBulk)super.modificaConBulk(userContext, varPdg);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);

		}

	}

	public boolean isVariazioneFromLiquidazioneIvaDaModificare(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException{
		/**
		 * Recupero la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			if (variazione.isApprovata()){
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
				Pdg_variazioneHome testataHome = (Pdg_variazioneHome) getHome(userContext, Pdg_variazioneBulk.class);
	
				for (java.util.Iterator dett = testataHome.findDettagliVariazioneGestionale(variazione).iterator();dett.hasNext();){
					Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk)dett.next();
					if (rigaVar.getCd_linea_attivita() != null && rigaVar.getCd_linea_attivita().equals(config.getVal01())){
						return true;
					}
				}
			}
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		return false;
	}
	
	public Pdg_variazione_riga_spesa_gestBulk recuperoRigaLiquidazioneIva(UserContext userContext, Ass_pdg_variazione_cdrBulk ass) throws ComponentException{
		/**
		 * Recupero la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
			Ass_pdg_variazione_cdrHome home = (Ass_pdg_variazione_cdrHome) getHome(userContext, Ass_pdg_variazione_cdrBulk.class);
			for (Iterator dett = home.findDettagliSpesaVariazioneGestionale(ass).iterator(); dett.hasNext();){
				Pdg_variazione_riga_spesa_gestBulk spe_det = (Pdg_variazione_riga_spesa_gestBulk) dett.next();
				if (spe_det.getCd_linea_attivita() != null && spe_det.getCd_linea_attivita().equals(config.getVal01())){
					return spe_det;
				}
	
			}
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		return null;
	}

	public boolean isRigaLiquidazioneIva(UserContext userContext, Pdg_variazione_riga_gestBulk riga) throws ComponentException{
		/**
		 * Recupero la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
			if (riga.getCd_linea_attivita() != null && riga.getCd_linea_attivita().equals(config.getVal01())){
				return true;
			}
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		return false;
	}
}
	