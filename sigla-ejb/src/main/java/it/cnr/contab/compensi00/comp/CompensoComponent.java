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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.Anagrafico_terzoBulk;
import it.cnr.contab.anagraf00.core.bulk.Ass_rapp_impiegoBulk;
import it.cnr.contab.anagraf00.core.bulk.Ass_rapp_impiegoHome;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaHome;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.anagraf00.core.bulk.RapportoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Codici_rapporti_inpsBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Codici_rapporti_inpsHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.RegioneHome;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaHome;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.BonusHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk;
import it.cnr.contab.compensi00.docs.bulk.ConguaglioHome;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk;
import it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaHome;
import it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDBulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSBulk;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataHome;
import it.cnr.contab.compensi00.docs.bulk.StampaCertificazioneVBulk;
import it.cnr.contab.compensi00.docs.bulk.StampaCompensiBulk;
import it.cnr.contab.compensi00.docs.bulk.StampaPartitarioCompensiVBulk;
import it.cnr.contab.compensi00.docs.bulk.StampaRiepilogoCompensiVBulk;
import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPBulk;
import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPHome;
import it.cnr.contab.compensi00.docs.bulk.V_doc_cont_compBulk;
import it.cnr.contab.compensi00.docs.bulk.V_doc_cont_compHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriHome;
import it.cnr.contab.compensi00.tabrif.bulk.Filtro_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Quadri_770Bulk;
import it.cnr.contab.compensi00.tabrif.bulk.Quadri_770Home;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoHome;
import it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioHome;
import it.cnr.contab.compensi00.tabrif.bulk.V_tipo_trattamento_tipo_coriBulk;
import it.cnr.contab.compensi00.tabrif.bulk.V_tipo_trattamento_tipo_coriHome;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.client.RicercaTrovato;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.docamm00.ejb.NumerazioneTempDocAmmComponentSession;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileHome;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.util.RemoteIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Insert the type's description here. Creation date: (21/02/2002 16.13.52)
 * 
 * @author: Roberto Fantino
 */
public class CompensoComponent extends it.cnr.jada.comp.CRUDComponent implements
		ICompensoMgr, Cloneable, IPrintMgr, Serializable {
	/**
	 * CompensoComponent constructor comment.
	 */
	public CompensoComponent() {
		super();
	}

	/**
	 * Richiama la procedura Oracle CNRCTB550.AGGCOMPENSOSENZACALCOLI che
	 * aggiorna gli importi del compenso a seguito di una modifica manuale dei
	 * contributi e ritenute
	 * 
	 **/
	private void aggCompensoSenzaCalcoli(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB550.AGGCOMPENSOSENZACALCOLI(?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, compenso.getEsercizio());
				cs.setObject(4, compenso.getPg_compenso());
				cs.execute();
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
	}

	private void aggiornaCogeCoan(UserContext userContext,
			CompensoBulk compenso, IDocumentoContabileBulk docCont)
			throws ComponentException {

		try {
			if (docCont != null && compenso != null
					&& compenso.getDefferredSaldi() != null) {
				IDocumentoContabileBulk key = compenso
						.getDefferredSaldoFor(docCont);
				if (key != null) {
					java.util.Map values = (java.util.Map) compenso
							.getDefferredSaldi().get(key);

					// caso di creazione o di nessuna modifica sui doc cont
					if (values == null)
						return;

					// QUI chiamare component del documento contabile
					// interessato
					String jndiName = null;
					Class clazz = null;
					DocumentoContabileComponentSession session = null;
					if (docCont instanceof ObbligazioneBulk) {
						jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
						clazz = ObbligazioneAbstractComponentSession.class;
						session = (ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
								.createEJB(jndiName, clazz);
					} else if (docCont instanceof AccertamentoBulk) {
						jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
						clazz = AccertamentoAbstractComponentSession.class;
						session = (AccertamentoAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
								.createEJB(jndiName, clazz);
					}
					if (session != null) {
						session.aggiornaCogeCoanInDifferita(userContext, key,
								values);
						compenso.getDefferredSaldi().remove(key);
					}
				}
			}
		} catch (javax.ejb.EJBException e) {
			throw handleException(compenso, e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(compenso, e);
		}
	}

	private void aggiornaCogeCoanDocAmm(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso == null || compenso.isTemporaneo())
			return;

		aggiornaCogeCoanObbligazioniDaCancellare(userContext, compenso);
		aggiornaCogeCoanObbligazioni(userContext, compenso);
	}

	private void aggiornaCogeCoanObbligazioni(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso != null) {
			ObbligazioniTable obbligazioniHash = compenso.getObbligazioniHash();
			if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {

				// Aggiorna coge coan per le obbligazioni NON temporanee
				for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(
						userContext,
						((ObbligazioniTable) obbligazioniHash.clone()).keys())
						.keys(); e.hasMoreElements();)
					aggiornaCogeCoan(userContext, compenso,
							(IDocumentoContabileBulk) e.nextElement());

			}
		}
	}

	private void aggiornaCogeCoanObbligazioniDaCancellare(
			UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		if (compenso != null) {
			if (compenso.getDocumentiContabiliCancellati() != null
					&& !compenso.getDocumentiContabiliCancellati().isEmpty()
					&& compenso.getObbligazioniHash() != null) {

				for (java.util.Enumeration e = compenso
						.getDocumentiContabiliCancellati().elements(); e
						.hasMoreElements();) {
					OggettoBulk oggettoBulk = (OggettoBulk) e.nextElement();
					if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
						Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) oggettoBulk;
						if (!scadenza.getObbligazione().isTemporaneo()) {
							PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(
									userContext, compenso.getObbligazioniHash()
											.keys());
							if (!obbligs
									.containsKey(scadenza.getObbligazione()))
								aggiornaCogeCoan(userContext, compenso,
										scadenza.getObbligazione());
						}
					}
				}
			}
		}
	}

	/**
	 * Richiama la procedura Oracle CNRCTB550.AGGIORNAMONTANTI che aggiorna gi
	 * montanti del compenso
	 * 
	 **/
	public void aggiornaMontanti(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB550.AGGIORNAMONTANTI(?,?,?,?,?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, compenso.getEsercizio());
				cs.setObject(4, compenso.getPg_compenso());
				if (compenso.getPgCompensoPerClone() != null) {
					cs.setObject(5, compenso.getCd_cds());
					cs.setObject(6, compenso.getCd_unita_organizzativa());
					cs.setObject(7, compenso.getEsercizio());
					cs.setObject(8, compenso.getPgCompensoPerClone());
				} else {
					cs.setNull(5, java.sql.Types.VARCHAR);
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.NUMERIC);
					cs.setNull(8, java.sql.Types.NUMERIC);
				}
				cs.execute();
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}

	}

	/**
	 * Aggiornamento dell'obbligazione e della scadenza associata al compenso a
	 * seguito di una richiesta di salvataggio (modifica/creazione) di un
	 * compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Aggiornamento obbligazione/scadenza associata a compenso Pre: Viene
	 * richiesto un aggiornamento della obbligazione, della scadenza e dei saldi
	 * a seguito di una richiesta di salvataggio del compenso associato Post:
	 * Viene eseguito l'aggiornamento dei saldi, dell'obbligazione e della
	 * scadenza
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che ha scatenato la richiesta di aggiornamento
	 * 
	 **/
	public CompensoBulk aggiornaObbligazione(UserContext userContext,
			CompensoBulk compenso, OptionRequestParameter status)
			throws ComponentException {

		// gestisco le obbligazioni precedentemente scollegate
		gestisciObbligazioniScollegate(userContext, compenso, status);

		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) compenso
				.getObbligazioneScadenzario();
		// la scadenza puè essere NULL poichè il compenso puè avere importo
		// lordo <= 0
		if (scadenza != null) {
			aggiornaSaldi(userContext, compenso, scadenza.getObbligazione(),
					status);

			Long pgTemp = scadenza.getPg_obbligazione();

			if (scadenza.getObbligazione().isTemporaneo()
					&& !compenso.isApertoDaMinicarriera()) {
				scollegaObbligazioneDaCompenso(userContext, compenso);
				aggiornaObbligazioneTemporanea(userContext, scadenza
						.getObbligazione());
			}

			scadenza.setIm_associato_doc_amm(compenso.getImportoObbligazione());
			updateImportoAssociatoDocAmm(userContext, scadenza);

			compenso.addToRelationsDocContForSaldi(scadenza.getObbligazione(),
					pgTemp);
		}

		return compenso;
	}

	/**
	 * Aggiornamento dell'obbligazione e della scadenza associate al compenso a
	 * seguito di una richiesta di cancellazione del compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Aggiornamento obbligazione/scadenza associata a compenso Pre: Viene
	 * richiesto un aggiornamento della obbligazione e della scadenza a seguito
	 * di una richiesta di cancellazione del compenso associato Post: Viene
	 * impostato a zero l'importo associato a documenti amministrativi della
	 * scadenza e della obbligazione
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che ha scatenato la richiesta di eliminazione
	 * 
	 **/
	private void aggiornaObbligazioneSuCancellazione(UserContext userContext,
			CompensoBulk compenso,
			Obbligazione_scadenzarioBulk scadenzaScollegata,
			ObbligazioneBulk obbligAssociata, OptionRequestParameter status)
			throws ComponentException {

		if (obbligAssociata == null
				|| !scadenzaScollegata.getObbligazione().equalsByPrimaryKey(
						obbligAssociata))
			aggiornaSaldi(userContext, compenso, scadenzaScollegata
					.getObbligazione(), status);

		Long pgTemp = scadenzaScollegata.getPg_obbligazione();

		if (scadenzaScollegata.getObbligazione().isTemporaneo()
				&& !scadenzaScollegata.getObbligazione().equalsByPrimaryKey(
						obbligAssociata)) {
			aggiornaObbligazioneTemporanea(userContext, scadenzaScollegata
					.getObbligazione());
		}
		scadenzaScollegata.setIm_associato_doc_amm(new java.math.BigDecimal(0)
				.setScale(2, java.math.BigDecimal.ROUND_HALF_EVEN));
		updateImportoAssociatoDocAmm(userContext, scadenzaScollegata);

		compenso.addToRelationsDocContForSaldi(scadenzaScollegata
				.getObbligazione(), pgTemp);
	}

	/**
	 * Conferma dell'obbligazione temporanea associata al compenso Viene
	 * recuperato un progressivo definitivo per l'obbligazione e viene salvata
	 * su db
	 * 
	 **/
	private void aggiornaObbligazioneTemporanea(UserContext userContext,
			ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {

		try {
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(
					userContext).getHome(Numerazione_doc_contBulk.class);
			Long pg = null;
			pg = numHome.getNextPg(userContext, obbligazioneTemporanea
					.getEsercizio(), obbligazioneTemporanea.getCd_cds(),
					obbligazioneTemporanea.getCd_tipo_documento_cont(),
					obbligazioneTemporanea.getUser());
			ObbligazioneHome home = (ObbligazioneHome) getHome(userContext,
					obbligazioneTemporanea);
			home.confirmObbligazioneTemporanea(userContext,
					obbligazioneTemporanea, pg);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(obbligazioneTemporanea, e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(obbligazioneTemporanea, e);
		}
	}

	/**
	 * Viene richiesto l'aggiornamento dei saldi dell'obbligazione associata al
	 * compenso
	 * 
	 **/
	private void aggiornaSaldi(it.cnr.jada.UserContext userContext,
			CompensoBulk compenso, IDocumentoContabileBulk docCont,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws ComponentException {

		try {
			if (docCont != null && compenso != null
					&& compenso.getDefferredSaldi() != null) {

				IDocumentoContabileBulk key = compenso
						.getDefferredSaldoFor(docCont);
				if (key != null) {
					java.util.Map values = (java.util.Map) compenso
							.getDefferredSaldi().get(key);
					// caso di creazione o di nessuna modifica sui doc cont
					if (values == null)
						return;
					// QUI chiamare component del documento contabile
					// interessato
					String jndiName = null;
					Class clazz = null;
					it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession session = null;
					if (docCont instanceof ObbligazioneBulk) {
						jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
						clazz = it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class;
						session = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
								.createEJB(jndiName, clazz);
					} else if (docCont instanceof AccertamentoBulk) {
						jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
						clazz = it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession.class;
						session = (it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
								.createEJB(jndiName, clazz);
					}
					if (session != null) {
						session.aggiornaSaldiInDifferita(userContext, key,
								values, status);
						// NON Differibile: si rischia di riprocessare i saldi
						// impropriamente
						compenso.getDefferredSaldi().remove(key);
					}
				}
			}
		} catch (javax.ejb.EJBException e) {
			throw handleException(compenso, e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(compenso, e);
		}
	}

	/**
	 * Viene richiesto un nuovo progressivo per il compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Name: Richiesta nuovo progressivo Pre: Viene richiesto un nuovo
	 * progressivo Post: Viene restituito un nuovo progressivo da assegnare al
	 * compenso
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il CompensoBulk a cui deve essere assegnato il nuovo
	 *            progressivo
	 * @return il nuovo progressivo da utilizzare
	 * 
	 **/
	private Long assegnaProgressivo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			// Assegno un nuovo progressivo al compenso
			ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB(
							"CNRDOCAMM00_EJB_ProgressiviAmmComponentSession",
							ProgressiviAmmComponentSession.class);
			Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(
					compenso);
			return progressiviSession.getNextPG(userContext, numerazione);

		} catch (javax.ejb.EJBException ex) {
			throw handleException(compenso, ex);
		} catch (RemoteException ex) {
			throw handleException(compenso, ex);
		}

	}

	/**
	 * Viene richiesto un nuovo progressivo temporaneo per il compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Name: Richiesta nuovo progressivo temporaneo Pre: Viene richiesto un
	 * nuovo progressivo temporaneo Post: Viene restituito un nuovo progressivo
	 * temporaneo da assegnare al compenso
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il CompensoBulk a cui deve essere assegnato il nuovo
	 *            progressivo
	 * @return il nuovo progressivo temporaneo da utilizzare
	 * 
	 **/
	public Long assegnaProgressivoTemporaneo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			// Assegno un nuovo progressivo alla fattura
			NumerazioneTempDocAmmComponentSession session = (NumerazioneTempDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB(
							"CNRDOCAMM00_EJB_NumerazioneTempDocAmmComponentSession",
							NumerazioneTempDocAmmComponentSession.class);
			Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(
					compenso);
			return session.getNextTempPG(userContext, numerazione);
		} catch (javax.ejb.EJBException ex) {
			throw handleException(compenso, ex);
		} catch (RemoteException ex) {
			throw handleException(compenso, ex);
		}

	}

	/**
	 * Richiama la procedura Oracle CNRCTB505.calcolaImportiCompenso che calcola
	 * l'importo lordo e la quota esente del compenso a partire dagli importi
	 * della missione
	 * 
	 **/
	private CompensoBulk calcolaImportiCompenso(UserContext userContext,
			CompensoBulk compenso,
			it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione)
			throws ComponentException {
		try {
			compenso.setIm_lordo_percipiente(new java.math.BigDecimal(0));
			compenso.setQuota_esente(new java.math.BigDecimal(0));

			LoggableStatement cs = null;
			try {
				cs = new LoggableStatement(
						getConnection(userContext),
						"{call 	"
								+ it.cnr.jada.util.ejb.EJBCommonServices
										.getDefaultSchema()
								+ "CNRCTB505.calcolaImportiCompenso(?,?,?,?,?,?,?)}",
						false, this.getClass());

				cs.setObject(1, missione.getCd_cds());
				cs.setObject(2, missione.getCd_unita_organizzativa());
				cs.setObject(3, missione.getEsercizio());
				cs.setObject(4, missione.getPg_missione());
				cs.setObject(5, compenso.getIm_lordo_percipiente());
				cs.setObject(6, compenso.getQuota_esente());
				cs.setObject(7, compenso.getAliquota_irpef_da_missione());

				cs.registerOutParameter(5, java.sql.Types.DECIMAL);
				cs.registerOutParameter(6, java.sql.Types.DECIMAL);
				cs.registerOutParameter(7, java.sql.Types.DECIMAL);

				cs.executeQuery();
				compenso.setIm_lordo_percipiente(cs.getBigDecimal(5));
				compenso.setQuota_esente(cs.getBigDecimal(6));
				compenso.setAliquota_irpef_da_missione(cs.getBigDecimal(7));
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			throw handleException(missione, e);
		}

		return compenso;
	}

	// ^^@@
	/**
	 * Richiama la procedure sul DB per l'elaborazione del CUD PreCondition:
	 * richiesta di creare un registro nuovo provvisorio o definitivo
	 * PostCondition: crea il registro stampa di un registro PreCondition:
	 * richiesta di stampa di un registro provvisorio o definitivo
	 * PostCondition: crea il prospetto di stampa liquidazione PreCondition:
	 * liquidazione provvisoria o definitiva PostCondition: crea la liquidazione
	 * Si è verificato un errore. PreCondition: Si è verificato un errore.
	 * PostCondition: Viene inviato un messaggio e non permette l'operazione
	 */
	// ^^@@
	private void callEstrazioneCud(it.cnr.jada.UserContext userContext,
			EstrazioneCUDBulk cud) throws java.sql.SQLException,
			it.cnr.jada.persistency.PersistencyException, ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB900.estrazioneCUD(?, ?, ?, ?, ?) }", false, this
					.getClass());

			cs.setInt(1, cud.getEsercizio().intValue()); // Esercizio
			cs.setString(2, cud.getCdAnagParameter()); // Cd_Anag
			cs.setLong(3, cud.getId_report().longValue()); // Id report
			cs.setString(4, null); // Messaggio
			cs.setString(5, cud.getUser()); // Utente

			cs.executeQuery();

		} catch (java.sql.SQLException e) {
			throw SQLExceptionHandler.getInstance().handleSQLException(e);
		} finally {
			if (cs != null)
				cs.close();
		}
	}

	// ^^@@
	/**
	 * Richiama la procedure sul DB per l'elaborazione INPS
	 * 
	 * Si è verificato un errore. PreCondition: Si è verificato un errore.
	 * PostCondition: Viene inviato un messaggio e non permette l'operazione
	 */

	// ^^@@
	private void callEstrazioneINPS(it.cnr.jada.UserContext userContext,
			EstrazioneINPSBulk inps) throws java.sql.SQLException,
			it.cnr.jada.persistency.PersistencyException, ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB920.estrazioneINPS(?, ?, ?, ?, ?, ?) }", false,
					this.getClass());

			cs.setInt(1, inps.getEsercizio().intValue()); // Esercizio
			cs.setString(2, it.cnr.contab.utenze00.bp.CNRUserContext
					.getCd_cds(userContext)); // Cd_cds
			cs.setString(3, it.cnr.contab.utenze00.bp.CNRUserContext
					.getCd_unita_organizzativa(userContext)); // Cd_uo
			cs.setLong(4, inps.getId_report().longValue()); // Id report
			cs.setString(5, null); // Messaggio
			cs.setString(6, inps.getUser()); // Utente

			cs.executeQuery();

		} catch (java.sql.SQLException e) {
			throw SQLExceptionHandler.getInstance().handleSQLException(e);
		} finally {
			if (cs != null)
				cs.close();
		}
	}

	private void callEstrazioneINPSMensile(it.cnr.jada.UserContext userContext,
			EstrazioneINPSMensileBulk inps) throws java.sql.SQLException,
			it.cnr.jada.persistency.PersistencyException, ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB925.estrazioneINPSmensile(?, ?, ?, ?, ?) }",
					false, this.getClass());

			cs.setInt(1, inps.getEsercizio().intValue()); // Esercizio
			cs.setInt(2, inps.getMese().intValue()); // Mese
			// cs.setString(2, inps.getCdAnagParameter()); // Cd_Anag
			cs.setLong(3, inps.getId_report().longValue()); // Id report
			cs.setString(4, null); // Messaggio
			cs.setString(5, inps.getUser()); // Utente

			cs.executeQuery();

		} catch (java.sql.SQLException e) {
			throw SQLExceptionHandler.getInstance().handleSQLException(e);
		} finally {
			if (cs != null)
				cs.close();
		}
	}

	// ^^@@
	/**
	 * Tutti i controlli superati. PreCondition: Ricerca la lista delle scadenze
	 * di obbligazioni congruenti con la fattura passiva che si sta
	 * creando/modificando. PostCondition: Le scadenze vengono aggiunte alla
	 * lista delle scadenze congruenti. Validazione lista delle obbligazioni per
	 * le fatture passive PreCondition: Si è verificato un errore nel
	 * caricamento delle scadenze delle obbligazioni. PostCondition: Viene
	 * inviato il messaggio corrispondente all'errore segnalato. Obbligazione
	 * definitiva PreCondition: La scadenza non appartiene ad un'obbligazione
	 * definitiva PostCondition: La scadenza non viene aggiunta alla lista delle
	 * scadenze congruenti. Obbligazioni non cancellate PreCondition: La
	 * scadenza appartiene ad un'obbligazione cancellata PostCondition: La
	 * scadenza non viene aggiunta alla lista delle scadenze congruenti.
	 * Obbligazioni associate ad altri documenti amministrativi PreCondition: La
	 * scadenza appartiene ad un'obbligazione associata ad altri documenti
	 * amministrativi PostCondition: La scadenza non viene aggiunta alla lista
	 * delle scadenze congruenti. Obbligazioni della stessa UO PreCondition: La
	 * scadenza dell'obbligazione non appartiene alla stessa UO di generazione
	 * fattura passiva PostCondition: La scadenza non viene aggiunta alla lista
	 * delle scadenze congruenti. Abilitatazione filtro di selezione sul
	 * debitore dell'obbligazione PreCondition: La scadenza dell'obbligazione ha
	 * un debitore diverso da quello della fattura passiva PostCondition: La
	 * scadenza non viene aggiunta alla lista delle scadenze congruenti.
	 * Disabilitazione filtro di selezione sul debitore dell'obbligazione
	 * PreCondition: La scadenza dell'obbligazione ha un debitore diverso da
	 * quello della fattura passiva e non è di tipo "diversi" PostCondition: La
	 * scadenza non viene aggiunta alla lista delle scadenze congruenti.
	 * Abilitazione filtro di selezione sulla data di scadenza PreCondition: La
	 * scadenza dell'obbligazione ha una data scadenza precedente alla data di
	 * filtro PostCondition: La scadenza non viene aggiunta alla lista delle
	 * scadenze congruenti. Abilitazione filtro importo scadenza PreCondition:
	 * La scadenza dell'obbligazione ha un importo di scadenza inferiore a
	 * quella di filtro PostCondition: La scadenza non viene aggiunta alla lista
	 * delle scadenze congruenti. Abilitazione filtro sul progressivo
	 * dell'obbligazione PreCondition: La scadenza dell'obbligazione non ha
	 * progressivo specificato PostCondition: La scadenza non viene aggiunta
	 * alla lista delle scadenze congruenti. Associazione di una scadenza a
	 * titolo capitolo dei beni servizio inventariabili da contabilizzare
	 * PreCondition: L'obbligazione non ha titolo capitolo dei beni servizio
	 * inventariabili da contabilizzare PostCondition: La scadenza non viene
	 * aggiunta alla lista delle scadenze congruenti.
	 */
	// ^^@@

	public RemoteIterator cercaObbligazioni(UserContext context,
			Filtro_ricerca_obbligazioniVBulk filtro) throws ComponentException {

		Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(
				context, Obbligazione_scadenzarioBulk.class);
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		sql.setDistinctClause(true);
		sql.addTableToHeader("OBBLIGAZIONE");
		sql
				.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS",
						"OBBLIGAZIONE.CD_CDS");
		sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO",
				"OBBLIGAZIONE.ESERCIZIO");
		sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE",
				"OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE",
				"OBBLIGAZIONE.PG_OBBLIGAZIONE");

		sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
		sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS,
				"D");
		sql.addSQLClause("AND", "OBBLIGAZIONE.RIPORTATO", sql.EQUALS, "N");
		sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL,
				null);
		sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA",
				sql.NOT_EQUALS, new java.math.BigDecimal(0));
		sql
				.addSQLClause(
						"AND",
						"OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
		sql.addParameter(new java.math.BigDecimal(0).setScale(2,
				java.math.BigDecimal.ROUND_HALF_EVEN), java.sql.Types.DECIMAL,
				2);
		sql
				.addSQLClause(
						"AND",
						"OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
		sql.addParameter(new java.math.BigDecimal(0).setScale(2,
				java.math.BigDecimal.ROUND_HALF_EVEN), java.sql.Types.DECIMAL,
				2);
		sql.addSQLClause("AND", "OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA",
				sql.EQUALS, filtro.getCd_unita_organizzativa());

		if (filtro.getElemento_voce() != null) {
			sql.addSQLClause("AND", "OBBLIGAZIONE.CD_ELEMENTO_VOCE",
					sql.STARTSWITH, filtro.getElemento_voce()
							.getCd_elemento_voce());
			sql.addSQLClause("AND", "OBBLIGAZIONE.TI_APPARTENENZA", sql.EQUALS,
					filtro.getElemento_voce().getTi_appartenenza());
			sql.addSQLClause("AND", "OBBLIGAZIONE.TI_GESTIONE", sql.EQUALS,
					filtro.getElemento_voce().getTi_gestione());
			sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS,
					filtro.getElemento_voce().getEsercizio());
		}

		if (!filtro.getFl_fornitore().booleanValue()) {
			sql.addTableToHeader("TERZO");
			sql.addTableToHeader("ANAGRAFICO");
			sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
			sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
			sql.addSQLClause("AND",
					"(OBBLIGAZIONE.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
			sql.addParameter(filtro.getFornitore().getCd_terzo(),
					java.sql.Types.INTEGER, 0);
			sql.addParameter(AnagraficoBulk.DIVERSI, java.sql.Types.VARCHAR, 0);
		} else {
			sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", sql.EQUALS, filtro
					.getFornitore().getCd_terzo());
		}

		if (filtro.getFl_data_scadenziario().booleanValue()
				&& filtro.getData_scadenziario() != null)
			sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA",
					sql.EQUALS, filtro.getData_scadenziario());
		if (filtro.getFl_importo().booleanValue()
				&& filtro.getIm_importo() != null)
			sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA",
					sql.GREATER_EQUALS, filtro.getIm_importo());

		// filtro su Tipo obbligazione
		if (filtro.getFl_nr_obbligazione().booleanValue()
				&& filtro.getTipo_obbligazione() != null) {
			if (ObbligazioneBulk.TIPO_COMPETENZA.equals(filtro
					.getTipo_obbligazione()))
				sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",
						sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
			else if (ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(filtro
					.getTipo_obbligazione()))
				sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",
						sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
			else if (ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(filtro
					.getTipo_obbligazione()))
				sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",
						sql.EQUALS,
						Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
		}

		// filtro su Anno Residuo obbligazione
		if (filtro.getFl_nr_obbligazione().booleanValue()
				&& filtro.getEsercizio_ori_obbligazione() != null)
			sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE",
					sql.EQUALS, filtro.getEsercizio_ori_obbligazione());

		// filtro su Numero obbligazione
		if (filtro.getFl_nr_obbligazione().booleanValue()
				&& filtro.getNr_obbligazione() != null)
			sql.addSQLClause("AND", "OBBLIGAZIONE.PG_OBBLIGAZIONE", sql.EQUALS,
					filtro.getNr_obbligazione());

		return iterator(context, sql, Obbligazione_scadenzarioBulk.class,
				"default");
	}

	/**
	 * Viene richiesto il completamento dell'OggettoBulk <compenso> caricando da
	 * db i seguenti oggetti complessi - terzo - tipo trattamento - regione
	 * Irap, voce iva, tipologia rischio (dati liquidazione) -
	 * contributi/ritenuta - dettagli di ogni contriuto/ritenuta - minicarriera
	 * - conguaglio
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Completamento del compenso Pre: Viene richiesto il completamento
	 * del compenso Post: Viene completato il compenso con i seguenti dati: -
	 * terzo - tipo trattamento - regione Irap, voce iva, tipologia rischio
	 * (dati liquidazione) - contributi/ritenuta - dettagli di ogni
	 * contriuto/ritenuta - minicarriera - conguaglio
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * 
	 *            Metodi privati chiamati: completaTerzo(UserContext
	 *            userContext, CompensoBulk compenso);
	 *            loadTipoTrattamento(UserContext userContext, CompensoBulk
	 *            compenso); loadDatiLiquidazione(UserContext userContext,
	 *            CompensoBulk compenso); loadContributiERitenute(UserContext
	 *            userContext, CompensoBulk compenso);
	 *            loadConguaglio(UserContext userContext, CompensoBulk
	 *            compenso);
	 * 
	 **/
	private void completaCompenso(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {
		try {
			compenso.setV_terzo(loadVTerzo(userContext, compenso));
			completaTerzo(userContext, compenso);

			// I tipi trattamento dipendono dal conguaglio. Prima di caricarli è
			// necessario caricare il conguaglio
			loadConguaglio(userContext, compenso);
			compenso.setTipiTrattamento(findTipiTrattamento(userContext,
					compenso));
			loadTipoTrattamento(userContext, compenso);
			
			loadPignorato(userContext, compenso);
			
			//compenso.setTipiPrestazioneCompenso(getHome(userContext,Tipo_prestazione_compensoBulk.class).findAll());
			compenso.setTipiPrestazioneCompenso(findTipiPrestazioneCompenso(userContext,
					compenso));
			
			loadDatiLiquidazione(userContext, compenso);
			loadContributiERitenute(userContext, compenso);
			loadMinicarriera(userContext, compenso);
			compenso.setBonus(loadBonus(userContext, compenso));
			getHomeCache(userContext).fetchAll(userContext);

			compenso.setRiportata(getStatoRiporto(userContext, compenso));

			/**
			 * Gennaro Borriello - (02/11/2004 15.04.39) Aggiunta gestione dell
			 * Stato Riportato all'esercizio di scrivania.
			 */
			compenso.setRiportataInScrivania(getStatoRiportoInScrivania(
					userContext, compenso));

			if (isGestitiIncarichi(userContext, compenso)) {
				if (compenso.getIncarichi_repertorio_anno() != null)
					compenso.setImporto_utilizzato(prendiUtilizzato(
							userContext, compenso
									.getIncarichi_repertorio_anno()));
			} else
				compenso.setIncarichi_repertorio_anno(null);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Completamento dell'OggettoBulk <compenso> aggiornando i campi relativi al
	 * terzo (nome, cognome, ragSoc, cf, pIva, comune, prov, reg) e caricando da
	 * db i seguenti oggetti complessi - modalita pagamento - termini di
	 * pagamento - tipo di rapporto
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Completamento del compenso Pre: Viene richiesto il completamento
	 * del compenso Post: Viene completato il compenso con i seguenti dati
	 * relativi al terzo: - nome, cognome, ragSoc, cf, pIva, comune, prov, reg -
	 * modalita pagamento - termini di pagamento - tipo di rapporto
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * @return l'OggettoBulk completo
	 * 
	 **/
	private CompensoBulk completaTerzo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso != null) {

			V_terzo_per_compensoBulk vTerzo = compenso.getV_terzo();
			compenso.setCd_terzo(vTerzo.getCd_terzo());
			compenso.setTi_anagrafico(vTerzo.getTi_dipendente_altro());
			compenso.setNome(vTerzo.getNome());
			compenso.setCognome(vTerzo.getCognome());
			compenso.setRagione_sociale(vTerzo.getRagione_sociale());
			compenso.setCodice_fiscale(vTerzo.getCodice_fiscale());
			compenso.setPartita_iva(vTerzo.getPartita_iva());
			compenso.setPg_comune_add(vTerzo.getPg_comune_fiscale());
			compenso.setCd_provincia_add(vTerzo.getCd_provincia_fiscale());
			compenso.setCd_regione_add(vTerzo.getCd_regione_fiscale());

			compenso.setModalita(findModalita(userContext, compenso));
			compenso.setTermini(findTermini(userContext, compenso));
			compenso.setTipiRapporto(findTipiRapporto(userContext, compenso));
			if (compenso.getCd_rapporto_inps() != null)
				compenso.setVisualizzaCodici_rapporti_inps(true);
			if (compenso.getCd_attivita_inps() != null)
				compenso.setVisualizzaCodici_attivita_inps(true);
			if (compenso.getCd_altra_ass_inps() != null)
				compenso.setVisualizzaCodici_altra_forma_ass_inps(true);
		}
		return compenso;
	}

	/**
	 * Completamento dell'OggettoBulk <compenso> aggiornando i campi relativi al
	 * terzo selezionato <vTerzo>
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Completamento del compenso Pre: Viene richiesto il completamento
	 * del compenso Post: Viene restituito il compenso completo di tutti i dati
	 * relativi al terzo selezionato
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * @param vTerzo
	 *            terzo con cui completare il compenso
	 * @return l'OggettoBulk completo
	 * 
	 *         Metodo privato chiamato: completaTerzo(UserContext userContext,
	 *         CompensoBulk compenso);
	 * 
	 **/
	public CompensoBulk completaTerzo(UserContext userContext,
			CompensoBulk compenso, V_terzo_per_compensoBulk vTerzo)
			throws ComponentException {

		compenso.setV_terzo(vTerzo);
		return completaTerzo(userContext, compenso);

	}

	/**
	 * Viene modificata in modo manuale un contributo/ritenuta del compenso
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Conferma modifica manuale di un contributo/ritenuta Pre: Viene
	 * richiesta l'aggiornamento degli importi del compenso a seguito della
	 * modifica di un suo contributo/ritenuta Post: Vengono aggiornati gli
	 * importi del compenso a seguito della modifica manuale di un
	 * contributi/ritenuta Esecuzione del package
	 * CNRCTB550.AGGCOMPENSOSENZACALCOLI Viene restituito il compenso aggiornato
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da aggiornare
	 * @param cori
	 *            il contributo/ritenuta modificato
	 * @return il compenso aggiornato dopo l'esecuzione della procedura oracle
	 * 
	 *         Metodi privati chiamati: aggCompensoSenzaCalcoli(UswerContext
	 *         userContext, CompensoBulk compenso);
	 * 
	 **/
	public CompensoBulk confermaModificaCORI(UserContext userContext,
			CompensoBulk compenso, Contributo_ritenutaBulk cori)
			throws ComponentException {

		try {
			updateBulk(userContext, compenso);
			updateBulk(userContext, cori);

			aggCompensoSenzaCalcoli(userContext, compenso);
			return reloadCompenso(userContext, compenso);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Ritorna il conguaglio a cui è stato associato il compenso selezionato
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Compenso associato a conguaglio Pre: Viene richiesta il conguaglio
	 * a cui il compenso risulta associato Post: Viene restituito il conguaglio
	 * 
	 * Nome: Compenso NON associato a conguaglio Pre: Il compenso non è stato
	 * associato a nessun conguaglio Post: Viene restituito il valore NULL
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da aggiornare
	 * @return il conguaglio a cui è associato il compenso
	 * 
	 **/
	public ConguaglioBulk conguaglioAssociatoACompenso(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			ConguaglioHome home = (ConguaglioHome) getHome(userContext,
					ConguaglioBulk.class);
			return home.findConguaglioAssociatoACompenso(compenso);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Richiama la procedura Oracle CNRCTB560.CONTABILIZZACOMPENSOCOFI che
	 * contabilizza il compenso creando i mandati
	 * 
	 **/
	private void contabilizzaCompensoCofi(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			Integer aEs = it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(userContext);
			if (aEs == null) {
				throw new ApplicationException(
						"Esercizio di scrivania non specificato");
			}

			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext),
					"{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB560.CONTABILIZZACOMPENSOCOFI(?,?,?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, aEs);
				cs.setObject(4, compenso.getEsercizio());
				cs.setObject(5, compenso.getPg_compenso());
				cs.setObject(6, compenso.getUser());
				cs.execute();
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Richiama la procedura Oracle CNRCTB550.COPIACOMPENSO che esegue una copia
	 * del compenso originale
	 * 
	 **/
	private void copiaCompenso(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB550.COPIACOMPENSO(?,?,?,?,?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, compenso.getEsercizio());
				cs.setObject(4, compenso.getPg_compenso());
				cs.setObject(5, compenso.getCd_cds());
				cs.setObject(6, compenso.getCd_unita_organizzativa());
				cs.setObject(7, compenso.getEsercizio());
				cs.setObject(8, compenso.getPgCompensoPerClone());
				cs.execute();
			} finally {
				cs.close();
			}

		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}

	}

	/**
	 * Esegue una operazione di creazione di un OggettoBulk.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata Pre: E' stata richiesta la creazione di una
	 * istanza di CompensoBulk che supera la validazione Post: Consente
	 * l'inserimento del compenso assegnandogli un progressivo definitivo e
	 * cancellando il compenso con progressivo temporaneo precedentemente creato
	 * - assegna un progressivo definitivo al compenso - aggiorna l'obbligazione
	 * associata - inserisce il compenso - inserisce le righe
	 * Contributo/Ritenuta - inserisce le righe Contributo/Ritenuta dettaglio
	 * 
	 * Nome: Validazione NON superata Pre: E' stata richiesta la creazione di
	 * una istanza di CompensoBulk che NON supera la validazione Post: Viene
	 * generata una eccezione con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            OggettoBulk il compenso che deve essere creato
	 * @return l'OggettoBulk risultante dopo l'operazione di creazione.
	 * 
	 *         Metodo di validzione del compenso: validaCompenso(CompensoBulk
	 *         compenso)
	 * 
	 **/
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		return creaConBulk(userContext, bulk, null);
	}

	/**
	 * Esegue una operazione di creazione di un OggettoBulk.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata Pre: E' stata richiesta la creazione di una
	 * istanza di CompensoBulk che supera la validazione Post: Consente
	 * l'inserimento del compenso assegnandogli un progressivo definitivo e
	 * cancellando il compenso con progressivo temporaneo precedentemente creato
	 * - assegna un progressivo definitivo al compenso - aggiorna l'obbligazione
	 * associata - inserisce il compenso - inserisce le righe
	 * Contributo/Ritenuta - inserisce le righe Contributo/Ritenuta dettaglio
	 * 
	 * Nome: Validazione NON superata Pre: E' stata richiesta la creazione di
	 * una istanza di CompensoBulk che NON supera la validazione Post: Viene
	 * generata una eccezione con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            OggettoBulk il compenso che deve essere creato
	 * @return l'OggettoBulk risultante dopo l'operazione di creazione.
	 * 
	 *         Metodo di validzione del compenso: validaCompenso(CompensoBulk
	 *         compenso)
	 * 
	 **/
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {

		try {

			CompensoBulk compenso = (CompensoBulk) bulk;
			validaCompenso(userContext, compenso);

			// Salvo temporaneamente l'hash map dei saldi
			PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
			if (compenso.getDefferredSaldi() != null)
				aTempDiffSaldi = (PrimaryKeyHashMap) compenso
						.getDefferredSaldi().clone();

			// aggiornando l'obbligazione associata
			aggiornaObbligazione(userContext, compenso, status);
			if (compenso.isDaBonus()) {
				if ((compenso.getBonus() != null)
						&& compenso.getBonus().getIm_bonus().compareTo(
								compenso.getIm_netto_percipiente()) != 0)
					throw new ApplicationException(
							"Attenzione l'importo del bonus previsto è di -"
									+ compenso.getBonus().getIm_bonus());
			}
			if (!compenso.isDaMinicarriera()) {

				// inserisco il compenso assegnando un pg definitivo
				Long pgTmp = compenso.getPg_compenso();
				compenso = inserisciCompenso(userContext, compenso);
				Long pg = compenso.getPg_compenso();

				// cancello il compenso temporaneo
				compenso.setPg_compenso(pgTmp);
				deleteBulk(userContext, compenso);
				compenso.setPg_compenso(pg);

				aggiornaMontanti(userContext, compenso);
			} else
				updateBulk(userContext, compenso);

			// Restore dell'hash map dei saldi
			if (compenso.getDefferredSaldi() != null)
				compenso.getDefferredSaldi().putAll(aTempDiffSaldi);
			aggiornaCogeCoanDocAmm(userContext, compenso);

			if (!verificaStatoEsercizio(
					userContext,
					new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
							compenso.getCd_cds(),
							((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
									.getEsercizio())))
				throw new it.cnr.jada.comp.ApplicationException(
						"Impossibile salvare un documento per un esercizio non aperto!");

			validaIncarico(userContext, compenso.getObbligazioneScadenzario(),
					compenso);
			
			validaContratto(userContext, compenso);
			
			controlliCig(compenso);
			return compenso;
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(bulk, ex);
		}
	}

	/**
	 * Viene richiesta l'esecuzione della procedura Oracle
	 * CNRCTB550.CONTABILIZZACOMPENSOCOFI per la contabilizzare il compenso
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata Pre: Viene richiesta la contabilizzazione COFI
	 * del compenso a seguito di una validazione superata Post: Viene eseguita
	 * la procedura oracle per la Contabilizzazione COFI del compenso
	 * 
	 * Nome: Validazione NON superata Pre: Viene richiesta la contabilizzazione
	 * COFI del compenso a seguito di una validazione NON superata Post:
	 * Contabilizzazione COFI non eseguita. Viene sollevata una
	 * ValidationExecption con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param conguaglio
	 *            il conguaglio da abilitare
	 * @return il conguaglio aggiornato dopo l'esecuzione della procedura oracle
	 * 
	 *         Metodo di validazione compenso per contabilizzazione
	 *         validaCompensoPerContabilizzazione(CompensoBulk compenso);
	 *         contabilizzaCompensoCofi(CompensoBulk compenso);
	 * 
	 **/
	public CompensoBulk doContabilizzaCompensoCofi(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
	try{
		Long tmp = compenso.getPgCompensoPerClone();
		Long current = compenso.getPg_compenso();
		if(compenso.getStato_liquidazione()!=null && !compenso.getStato_liquidazione().equals(compenso.LIQ))
			throw new it.cnr.jada.comp.ApplicationException(
					"Lo stato della liquidazione non è compatibile");
		  //controllo cap /swift
        if (compenso.getBanca() != null && 
        		((Rif_modalita_pagamentoBulk.BANCARIO.equals(compenso.getBanca().getTi_pagamento())
     			||(Rif_modalita_pagamentoBulk.IBAN.equals(compenso.getBanca().getTi_pagamento()))))) {
        	
        	  BancaBulk banca = (BancaBulk) getHome(userContext,
        			  BancaBulk.class).findByPrimaryKey(
                      new BancaBulk(compenso.getCd_terzo(),compenso.getPg_banca()));
            
        	if (banca!=null && banca.getCodice_iban()!=null && compenso.getBanca().getAbi()!=null && banca.getCodice_iban().startsWith("IT")){
            if (compenso.getCd_terzo() != null) {
                TerzoBulk terzo = (TerzoBulk) getHome(userContext,
                        TerzoBulk.class).findByPrimaryKey(
                        new TerzoBulk(compenso.getCd_terzo()));
                if (terzo.getPg_comune_sede() != null) {
                    ComuneBulk comune = (ComuneBulk) getHome(userContext,
                            ComuneBulk.class).findByPrimaryKey(
                            new ComuneBulk(terzo.getPg_comune_sede()));
                    if (comune.getTi_italiano_estero().equals(NazioneBulk.ITALIA) && terzo.getCap_comune_sede() == null)
                        throw new ApplicationException(
                                "Attenzione per la modalità di pagamento presente sul documento è necessario indicare il cap sul terzo.");
                }
              }
            }
        	else if (banca!=null && banca.getCodice_iban()!=null && compenso.getBanca().getAbi()==null ){
                NazioneHome nazioneHome = (NazioneHome) getHome(userContext,
                        NazioneBulk.class);
                SQLBuilder sqlExists = nazioneHome.createSQLBuilder();
                sqlExists.addSQLClause("AND","NAZIONE.CD_ISO",SQLBuilder.EQUALS,banca.getCodice_iban().substring(0, 2));
                sqlExists.addSQLClause("AND","NAZIONE.FL_SEPA",SQLBuilder.EQUALS,"Y");
                if (sqlExists.executeCountQuery(getConnection(userContext))!=0 && compenso.getBanca().getCodice_swift()==null) 
        		          throw new ApplicationException(
                                    "Attenzione per la modalità di pagamento presente sul documento è necessario indicare il codice swift/bic.");
                }
        }
		validaCompensoPerContabilizzazione(userContext, compenso);
		contabilizzaCompensoCofi(userContext, compenso);

		if (tmp != null) {
			eliminaCompensoTemporaneo(userContext, compenso, tmp);
			compenso.setPg_compenso(current);
		}
		return reloadCompenso(userContext, compenso);
	} catch (Throwable t) {
		throw handleException(t);
	}
}

	// ^^@@
	/**
	 * Gestisce il richiamo delle procedure sul DB l'estrazione e l'elaborazione
	 * del CUD. PreCondition: richiesta di elaborare un CUD PostCondition:
	 * richiama callEstrazioneCUD, per l'elaborazione del report richiesto
	 * 
	 * Si è verificato un errore. PreCondition: Si è verificato un errore.
	 * PostCondition: Viene inviato un messaggio e non permette l'operazione
	 */
	// ^^@@
	public it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk doElaboraCUD(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		java.math.BigDecimal id_report = getSequence(userContext);

		bulk.setId_report(id_report);

		try {
			if (bulk instanceof EstrazioneCUDBulk) {
				callEstrazioneCud(userContext, (EstrazioneCUDBulk) bulk);
			} else if (bulk instanceof EstrazioneINPSBulk) {
				callEstrazioneINPS(userContext, (EstrazioneINPSBulk) bulk);
			}
		} catch (Throwable t) {
			throw handleException(t);
		}

		return bulk;
	}

	public it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk doElaboraINPSMensile(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		java.math.BigDecimal id_report = getSequence(userContext);

		bulk.setId_report(id_report);

		try {
			if (bulk instanceof EstrazioneINPSMensileBulk) {
				callEstrazioneINPSMensile(userContext,
						(EstrazioneINPSMensileBulk) bulk);
			}
		} catch (Throwable t) {
			throw handleException(t);
		}

		return bulk;
	}

	/**
	 * Richiama la procedura Oracle CNRCTB550.ELABORACOMPENSO che calcola i
	 * contributi/ritenute del compenso
	 * 
	 **/
	private void elaboraCompenso(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext),
					"{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB550.ELABORACOMPENSO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}",
					false, this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, compenso.getEsercizio());
				cs.setObject(4, compenso.getPg_compenso());

				if (compenso.getPgCompensoPerClone() != null) {
					cs.setObject(5, compenso.getCd_cds());
					cs.setObject(6, compenso.getCd_unita_organizzativa());
					cs.setObject(7, compenso.getEsercizio());
					cs.setObject(8, compenso.getPgCompensoPerClone());
				} else {
					cs.setNull(5, java.sql.Types.VARCHAR);
					cs.setNull(6, java.sql.Types.VARCHAR);
					cs.setNull(7, java.sql.Types.NUMERIC);
					cs.setNull(8, java.sql.Types.NUMERIC);
				}
				if (compenso.getMinicarriera() != null) {
					cs.setObject(9, compenso.getMinicarriera().getCd_cds());
					cs.setObject(10, compenso.getMinicarriera()
							.getCd_unita_organizzativa());
					cs.setObject(11, compenso.getMinicarriera().getEsercizio());
					cs.setObject(12, compenso.getMinicarriera()
							.getPg_minicarriera());
				} else {
					cs.setNull(9, java.sql.Types.VARCHAR);
					cs.setNull(10, java.sql.Types.VARCHAR);
					cs.setNull(11, java.sql.Types.NUMERIC);
					cs.setNull(12, java.sql.Types.NUMERIC);
				}
				if (compenso.isElettronica() && !compenso.isSenzaCalcoli()) {
					cs.setObject(13, compenso.getFatturaPassiva().getDocumentoEleTestata().getIdPaese());
					cs.setObject(14, compenso.getFatturaPassiva().getDocumentoEleTestata().getIdCodice());
					cs.setObject(15, compenso.getFatturaPassiva().getDocumentoEleTestata().getIdentificativoSdi());
					cs.setObject(16, compenso.getFatturaPassiva().getDocumentoEleTestata().getProgressivo());
				} else {
					cs.setNull(13, java.sql.Types.VARCHAR);
					cs.setNull(14, java.sql.Types.VARCHAR);
					cs.setNull(15, java.sql.Types.NUMERIC);
					cs.setNull(16, java.sql.Types.NUMERIC);
				}

				cs.execute();
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}

	}

	public CompensoBulk elaboraScadenze(UserContext userContext,
			CompensoBulk compenso, Obbligazione_scadenzarioBulk oldScad,
			Obbligazione_scadenzarioBulk newScad) throws ComponentException {

		try {
			if (oldScad != null)
				oldScad = resyncScadenza(userContext, oldScad);

			validaScadenze(compenso, newScad);

			compenso.addToDocumentiContabiliCancellati(oldScad);
			compenso.sincronizzaScadenzeCancellate(newScad);
			compenso.setObbligazioneScadenzario(newScad);
			if (newScad.getObbligazione() != null && newScad.getObbligazione().getContratto() != null){
				compenso.setCig(newScad.getObbligazione().getContratto().getCig());
			} else {
				compenso.setCig(null);
			}
			compenso.removeFromDocumentiContabiliCancellati(newScad);
			compenso.setStato_cofi(CompensoBulk.STATO_CONTABILIZZATO);

			lockBulk(userContext, newScad);
			return compenso;
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		} catch (it.cnr.jada.bulk.OutdatedResourceException ex) {
			throw handleException(ex);
		} catch (it.cnr.jada.bulk.BusyResourceException ex) {
			throw handleException(ex);
		}
	}

	private void validaScadenze(CompensoBulk compenso,
			Obbligazione_scadenzarioBulk newScad) throws ComponentException {

		Vector scadCanc = compenso.getDocumentiContabiliCancellati();
		if (scadCanc != null) {
			Iterator it = scadCanc.iterator();

			while (it.hasNext()) {
				Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) it
						.next();
				if (scad.getObbligazione() instanceof ObbligazioneResBulk) {
					if (scad.getObbligazione().equalsByPrimaryKey(
							newScad.getObbligazione())
							&& ((ObbligazioneResBulk) scad.getObbligazione())
									.getObbligazione_modifica() != null
							&& ((ObbligazioneResBulk) scad.getObbligazione())
									.getObbligazione_modifica()
									.getPg_modifica() != null) {
						throw new it.cnr.jada.comp.ApplicationException(
								"Impossibile collegare una scadenza dell'impegno residuo "
										+ scad.getPg_obbligazione()
										+ " poiché è stata effettuata una modifica in questo documento amministrativo!");
					}
				}
			}
		}
	}

	/**
	 * Viene richiesta la cancellazione del Compenso Temporaneo generato da
	 * un'operazione di inserimento o modifica (nel caso di clone)
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Cancellazione della copia del compenso originale Pre: Viene
	 * richiesta la cancellazione della copia del compenso originale Post: Viene
	 * eliminata fisicamente la copia del compenso creata precedentemente
	 * 
	 * @param userContex
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            Oggetto buulk da cancellare
	 * @param tmp
	 *            Progressivo della copia del compenso creata
	 * 
	 **/
	public void eliminaCompensoTemporaneo(UserContext userContext,
			CompensoBulk compenso, Long tmp) throws ComponentException {

		try {

			compenso.setPg_compenso(tmp);
			deleteBulk(userContext, compenso);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene richiesta l'eliminazione del Compenso
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata e compenso NON cancellabile fisicamente Pre:
	 * Il compenso passa i criteri di validità di business per l'operazione di
	 * cancellazione ed è in stato Pagato o associato a Mandato/Reversale Post:
	 * Viene consentita la cancellazione logica del compenso con relativi
	 * aggiornamenti degli importi dell'obbligazione associata
	 * 
	 * Nome: Validazione superata e compenso cancellabile fisicamente Pre: Il
	 * compenso passa i criteri di validità di business per l'operazione di
	 * cancellazione ed è in stato Iniziale oppure Contabilizzato ma non
	 * associato a Mandato/Reversale Post: Viene consentita la cancellazione
	 * fisica del compenso con relativi aggiornamenti degli importi
	 * dell'obbligazione associata
	 * 
	 * Nome: Validazione NON superata Pre: Il compenso non passa i criteri di
	 * validità di business per l'operazione di cancellazione Post: Viene
	 * impedita la cancellazione sia fisica che logica del compenso
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da eliminare
	 * @return void
	 * 
	 *         Metodo privato: aggiornaObbligazioneSuCancellazione(UserContext
	 *         userContext, CompensoBulk compenso, OptionRequestParamenter
	 *         status);
	 * 
	 **/
	public void eliminaConBulk(UserContext userContext, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {
		try {
			CompensoBulk compenso = (CompensoBulk) bulk;
			validaEliminaConBulk(userContext, compenso);
			if (compenso.getBonus() != null
					&& compenso.getBonus().getFl_trasmesso()) {
				throw new ApplicationException(
						"Compenso collegato a Bonus già trasmesso, eliminazione non possibile");
			}
			// Controllo dello stato dell'es COEP prec. per compensi riportati
			// Da ese. prec. (isRiportataInScrivania())
			validateEsercizioCOEP(userContext, compenso);

			int risp = eseguiDelCompenso(userContext, compenso);

			if (risp == 0)
				throw new it.cnr.jada.comp.ApplicationException(
						"Errore durante l'esecuzione del package!");

			Long pgTmp = compenso.getPgCompensoPerClone();

			// Salvo temporaneamente l'hash map dei saldi
			PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
			if (compenso.getDefferredSaldi() != null)
				aTempDiffSaldi = (PrimaryKeyHashMap) compenso
						.getDefferredSaldi().clone();

			Obbligazione_scadenzarioBulk scadenzaAssociata = compenso
					.getObbligazioneScadenzario();
			if (scadenzaAssociata != null)
				compenso.addToDocumentiContabiliCancellati(scadenzaAssociata);
			gestisciObbligazioniScollegate(userContext, compenso, null);

			if (risp == compenso.CANCELLAZIONE_FISICA)
				super.eliminaConBulk(userContext, compenso);
			else if (risp == compenso.CANCELLAZIONE_LOGICA) {
				// ************
				// Se l'esercizio di scrivania e' diverso da quello solare
				// inizializzo la data di cancellazione al 31/12/esercizio
				// missione
				java.sql.Timestamp tsOdierno = ((CompensoHome) getHome(
						userContext, compenso)).getServerDate();
				java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar
						.getInstance();
				tsOdiernoGregorian.setTime(tsOdierno);
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"dd/MM/yyyy");

				if (tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) != it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext).intValue())
					compenso.setDt_cancellazione(new java.sql.Timestamp(sdf
							.parse(
									"31/12/"
											+ compenso.getEsercizio()
													.intValue()).getTime()));
				else {
					String currentDate = Integer.toString(tsOdiernoGregorian
							.get(java.util.GregorianCalendar.DAY_OF_MONTH))
							+ "/"
							+ Integer.toString(tsOdiernoGregorian
									.get(java.util.GregorianCalendar.MONTH))
							+ "/"
							+ Integer.toString(tsOdiernoGregorian
									.get(java.util.GregorianCalendar.YEAR));
					compenso.setDt_cancellazione(new java.sql.Timestamp(sdf
							.parse(currentDate).getTime()));
				}
				// ************

				compenso.setStato_cofi(CompensoBulk.STATO_ANNULLATO);
				if (compenso.CONTABILIZZATO_COAN.equals(compenso
						.getStato_coan()))
					compenso.setStato_coan(compenso.DA_RICONTABILIZZARE_COAN);
				if (compenso.CONTABILIZZATO_COGE.equals(compenso
						.getStato_coge()))
					compenso.setStato_coge(compenso.DA_RICONTABILIZZARE_COGE);
				updateBulk(userContext, compenso);
			}
			if (pgTmp != null)
				eliminaCompensoTemporaneo(userContext, compenso, pgTmp);

			// Restore dell'hash map dei saldi
			if (compenso.getDefferredSaldi() != null)
				compenso.getDefferredSaldi().putAll(aTempDiffSaldi);

			aggiornaCogeCoanDocAmm(userContext, compenso);

			if (!verificaStatoEsercizio(
					userContext,
					new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
							compenso.getCd_cds(),
							((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
									.getEsercizio())))
				throw new it.cnr.jada.comp.ApplicationException(
						"Impossibile eliminare un documento per un esercizio non aperto!");

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(bulk, ex);
		} catch (java.text.ParseException e) {
			throw handleException(bulk, e);
		}
	}

	/**
	 * Scollega una obbligazione dal compenso relativo
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Scollego obbligazione da compenso Pre: Viene richiesto di
	 * scollegare una obbligazione da compenso Post: L'obbligazione viene
	 * scollegata e il compenso aggiornato
	 * 
	 **/
	public CompensoBulk eliminaObbligazione(UserContext userContext,
			CompensoBulk compenso) throws it.cnr.jada.comp.ComponentException {

		try {
			compenso.addToDocumentiContabiliCancellati(compenso
					.getObbligazioneScadenzario());
			compenso.setObbligazioneScadenzario(null);
			compenso.setStato_cofi(CompensoBulk.STATO_INIZIALE);
			updateBulk(userContext, compenso);

			return compenso;
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene richiesta l'esecuzione della procedura Oracle
	 * CNRCTB550.ELABORACOMPENSO
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Calcolo CO/RI per Compenso - Validazione superata Pre: Viene
	 * richiesto il calcolo dei Contributi/Ritenute legate al compenso a fronte
	 * di una validazione superata positivamente Post: Viene eseguita la
	 * procedura per il calcolo dei Contributi/Ritenute Viene resitutito il
	 * compenso aggiornato
	 * 
	 * Nome: Calcolo CO/RI per Compenso - Validazione NON superata Pre: Viene
	 * richiesto il calcolo dei Contributi/Ritenute legate al compenso a fronte
	 * di una validazione NON superata Post: Procedura per il calcolo dei
	 * Contributi/Ritenute NON eseguita Invio di una eccezione con messaggio
	 * d'errore associato
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da aggiornare
	 * @return il compenso aggiornato dopo creazione dei contributi/ritenute
	 * 
	 *         Metodo di validazione compenso per contabilizzazione
	 *         validaCompensoSuEseguiCalcolo(CompensoBulk compenso);
	 *         elaboraCompenso(UserContext userContext, CompensoBulk compenso);
	 * 
	 **/
	public CompensoBulk eseguiCalcolo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			validaCompensoSuEseguiCalcolo(userContext, compenso);

			if (compenso.getPg_compenso() == null) {
				Long pgTmp = assegnaProgressivoTemporaneo(userContext, compenso);
				compenso.setPg_compenso(pgTmp);
				insertBulk(userContext, compenso);
			} else {
				updateBulk(userContext, compenso);
			}

			elaboraCompenso(userContext, compenso);
			
			return reloadCompenso(userContext, compenso);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	/**
	 * Richiama la procedura Oracle CNRCTB550.ESEGUIDELCOMPENSO che verifica se
	 * il compenso è cancellabile logicamente, fisicamente oppure no
	 * 
	 **/
	private int eseguiDelCompenso(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {
			int result = 0;
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB550.ESEGUIDELCOMPENSO(?,?,?,?,?)}", false,
					this.getClass());
			try {
				cs.setObject(1, compenso.getCd_cds());
				cs.setObject(2, compenso.getCd_unita_organizzativa());
				cs.setObject(3, compenso.getEsercizio());
				cs.setObject(4, compenso.getPg_compenso());
				cs.setObject(5, new Integer(result));
				cs.registerOutParameter(5, java.sql.Types.INTEGER);
				cs.execute();
				result = cs.getInt(5);

				// Se il compenso e' eleggibile alla cancellazione fisica
				// ma l'obbligazione ad essa associata risulta essere stata
				// riportata --> forzo la cancellazione logica del compenso

				// Gennaro Borriello - (03/11/2004 19.04.48)
				// Fix sul controllo dello "Stato Riportato"
				if (result == compenso.CANCELLAZIONE_FISICA
						&& compenso.isRiportata()
						&& compenso.isRiportataInScrivania())
					result = compenso.CANCELLAZIONE_LOGICA;

				return result;

			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}

	}

	/**
	 * Viene richiesta la lista delle Banche associate ad un Terzo
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Terzo NON selezionato Pre: Non è stato selezionato un Terzo per il
	 * compenso Post: Non vengono caricate le banche.
	 * 
	 * Nome: Terzo selezionato Pre: E' stato selezionato un Terzo valido per il
	 * compenso Post: Viene restituita la lista delle Banche associate al Terzo
	 * e alla Modalità di Pagamento selezionata
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * @return La lista delle banche associate al terzo
	 * 
	 **/
	public java.util.List findListaBanche(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			if (compenso.getTerzo() == null)
				return null;

			return getHome(userContext, BancaBulk.class).fetchAll(
					selectBancaByClause(userContext, compenso, null, null));
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene richiesta la lista delle Modalita di Pagamento associate ad un
	 * Terzo
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Terzo NON selezionato Pre: Non è stato selezionato un Terzo per il
	 * compenso Post: Non vengono caricate le modalita di pagamento
	 * 
	 * Nome: Terzo selezionato Pre: E' stato selezionato un Terzo valido per il
	 * compenso Post: Viene restituita la lista delle Modalita di pagamento
	 * associate al Terzo
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da completare
	 * @return La lista delle modalita di pagamento associate al terzo
	 * 
	 **/
	public java.util.Collection findModalita(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {

		try {

			CompensoBulk compenso = (CompensoBulk) bulk;
			if (compenso.getTerzo() == null)
				return null;

			TerzoHome terzoHome = (TerzoHome) getHome(userContext,
					TerzoBulk.class);
			return terzoHome.findRif_modalita_pagamento(compenso.getTerzo());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(bulk, ex);
		} catch (it.cnr.jada.persistency.IntrospectionException ex) {
			throw handleException(bulk, ex);
		}
	}

	/**
	 * Viene richiesta la lista dei Termini di pagamento associati ad un Terzo
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Terzo NON selezionato Pre: Non è stato selezionato un Terzo per il
	 * compenso Post: Non vengono caricati i termini di pagamento
	 * 
	 * Nome: Terzo selezionato Pre: E' stato selezionato un Terzo valido per il
	 * compenso Post: Viene restituita la lista dei Termini di pagamento
	 * associati al Terzo
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da completare
	 * @return La lista dei Termini di pagamento associati al terzo
	 * 
	 **/
	public java.util.Collection findTermini(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {

		try {
			CompensoBulk compenso = (CompensoBulk) bulk;
			if (compenso.getTerzo() == null)
				return null;

			TerzoHome terzoHome = (TerzoHome) getHome(userContext,
					TerzoBulk.class);
			return terzoHome.findRif_termini_pagamento(compenso.getTerzo());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(bulk, ex);
		} catch (it.cnr.jada.persistency.IntrospectionException ex) {
			throw handleException(bulk, ex);
		}

	}

	/**
	 * Viene richiesta la lista dei Tipi di rapporto associati ad un Terzo
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Terzo NON selezionato Pre: Non è stato selezionato un Terzo per il
	 * compenso Post: Non vengono caricati i Tipi di rapporto
	 * 
	 * Nome: Terzo selezionato Pre: E' stato selezionato un Terzo valido per il
	 * compenso Post: Viene restituita la lista dei Tipi di rapporto associati
	 * al Terzo
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * @return La lista dei Tipi di rapporto associati al terzo
	 * 
	 **/
	public java.util.Collection findTipiRapporto(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			if (compenso.getTerzo() == null)
				return null;

			it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome home = (it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome) getHome(
					userContext,
					it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk.class);
			return home.findTipiRapporto(compenso.getV_terzo(), compenso
					.getDt_da_competenza_coge());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	/**
	 * Viene richiesta la lista dei Tipi di Trattamento legati al Tipo di
	 * Rapporto selezionato
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Tipo di Rapporto NON selezionato Pre: Non è stato selezionato il
	 * tipo di rapporto Post: Non vengono caricati i Tipi Trattamento
	 * 
	 * Nome: Terzo selezionato Pre: E' stato selezionato un tipo di rapporto
	 * valido per il compenso Post: Viene restituita la lista dei Tipi di
	 * Trattamento legati al Tipo di rapporto selezionato
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk da completare
	 * @return La lista dei Tipi di Trattamento associati al Tipo Rapporto
	 *         selezionato
	 * 
	 **/
	public java.util.Collection findTipiTrattamento(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			if (compenso.getTipoRapporto() == null)
				return null;

			Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome) getHome(
					userContext, Tipo_trattamentoBulk.class);

			Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
			filtro.setCdTipoRapporto(compenso.getCd_tipo_rapporto());
			filtro.setTipoAnagrafico(compenso.getTi_anagrafico());
			filtro.setDataValidita(compenso.getDt_registrazione());
			filtro.setFlSenzaCalcoli(compenso.getFl_senza_calcoli());
			filtro.setFlDefaultCongualio(new Boolean(compenso.isDaConguaglio()));
			filtro.setTiIstituzionaleCommerciale(compenso.getTi_istituz_commerc());

			filtro.setFlBonus(compenso.isDaBonus());
			filtro.setFlSplitPayment(compenso.getFl_split_payment()); 
			if (filtro.getCdTipoRapporto() != null
					&& filtro.getCdTipoRapporto().equals("DIP")) {
				try {
					TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
							.getHome(TerzoBulk.class);
					TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
					TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

					RapportoHome rHome = (RapportoHome) getHomeCache(
							userContext).getHome(RapportoBulk.class);
					java.util.Collection collRapp = rHome
							.findByCdAnagCdTipoRapporto(t.getCd_anag(), filtro
									.getCdTipoRapporto());
					boolean exit = false;
					for (java.util.Iterator i = collRapp.iterator(); i
							.hasNext()
							&& !exit;) {
						RapportoBulk r = (RapportoBulk) i.next();
						exit = true;
						if (r.getCd_ente_prev_sti() == null)
							// throw new
							// it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
							// non blocco perchè potrebbero esserci trattamenti
							// che non prevedono contributi previdenziali
							// quindi passo il codice fittizio 'XX'
							filtro.setEntePrev("XX");
						else
							filtro.setEntePrev(r.getCd_ente_prev_sti());
						if (r.getCd_rapp_impiego_sti() == null)
							throw new it.cnr.jada.comp.ApplicationException(
									"Per il dipendente in esame non è definito un Rapporto di Impiego!");
						else {
							Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(
									userContext, Ass_rapp_impiegoBulk.class);
							Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(
									r.getCd_rapp_impiego_sti());
							Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk) assHome
									.findByPrimaryKey(assKey);
							filtro.setTipoRappImpiego(ass
									.getTipo_rapp_impiego());
						}
					}
				} catch (IntrospectionException e) {
					throw handleException(e);
				}
				// solo per il rapporto DIP aggiungo il filtro "Anno prec" a
				// seconda della data di inizio competenza
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				data_da.setTime(compenso.getDt_da_competenza_coge());
				if (data_da.get(GregorianCalendar.YEAR) == (compenso
						.getEsercizio() - 1))
					filtro.setFlAnnoPrec(new Boolean(true));
				else
					filtro.setFlAnnoPrec(new Boolean(false));
			}
			if (compenso.getDt_da_competenza_coge() != null
					&& compenso.getDt_a_competenza_coge() != null) {
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar
						.getInstance();

				data_da.setTime(compenso.getDt_da_competenza_coge());
				data_a.setTime(compenso.getDt_a_competenza_coge());

				TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
						.getHome(TerzoBulk.class);
				TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
				TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(
						userContext).getHome(AnagraficoBulk.class);
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk) aHome
						.findByPrimaryKey(aKey);

				if (a.getFl_cervellone() && (a.getAnno_inizio_res_fis() == null || a.getAnno_fine_agevolazioni() == null)){
					throw new it.cnr.jada.comp.ApplicationException(
							"Per la persona in esame è impostata l'agevolazione fiscale del rientro dei cervelli ma non è indicato l'anno inizio residenza fiscale o l'anno di fine delle agevolazioni.");
				}
				if (data_da.get(GregorianCalendar.YEAR) == data_a
						.get(GregorianCalendar.YEAR)) {
					if (a.getFl_cervellone()
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_inizio_res_fis().intValue()) < 0)
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_fine_agevolazioni().intValue()) > 0)) {
						filtro.setFlAgevolazioniCervelli(new Boolean(a
								.getFl_cervellone()));
					} else
						filtro.setFlAgevolazioniCervelli(new Boolean(false));
				} else
				// solo per i congualgli dei cervelli gestisco anche le date a
				// cavallo
				if (compenso.getFl_compenso_conguaglio()
						&& a.getFl_cervellone()
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_inizio_res_fis()
										.intValue()) < 0)
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_fine_agevolazioni()
										.intValue()) > 0)) {
					filtro.setFlAgevolazioniCervelli(new Boolean(a
							.getFl_cervellone()));
				} else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			if (filtro.getCdTipoRapporto() != null && getTipoRapportoProf(userContext).getCd_tipo_rapporto() != null
					&& filtro.getCdTipoRapporto().equals(getTipoRapportoProf(userContext).getCd_tipo_rapporto())) 
			{
					if (compenso.isGestione_doc_ele())
					{
						if(!compenso.getFl_generata_fattura())
						{
							//se non ho scelto senza calcoli non devo vedere nulla
							if (!compenso.getFl_senza_calcoli())
								filtro.setTipoAnagrafico("X");
						}
						
					} 
			}
			return trattHome.findTipiTrattamento(filtro);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	public java.util.Collection findTipiPrestazioneCompenso(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			if (compenso.getTerzo() == null)
				return null;

			it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoHome home = (it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoHome) getHome(
					userContext,
					it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk.class);
			return home.findTipiPrestazioneCompenso(compenso.getCd_tipo_rapporto());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}
	
	/**
	 * Aggiornamento dell'obbligazione e della scadenza associata al compenso a
	 * seguito di una richiesta di salvataggio (modifica/creazione) di un
	 * compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Aggiornamento obbligazione/scadenza associata a compenso Pre: Viene
	 * richiesto un aggiornamento della obbligazione, della scadenza e dei saldi
	 * a seguito di una richiesta di salvataggio del compenso associato Post:
	 * Viene eseguito l'aggiornamento dei saldi, dell'obbligazione e della
	 * scadenza
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che ha scatenato la richiesta di aggiornamento
	 * 
	 **/
	private void gestisciObbligazioniScollegate(UserContext userContext,
			CompensoBulk compenso, OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {
		for (java.util.Iterator i = compenso.getDocumentiContabiliCancellati()
				.iterator(); i.hasNext();) {
			Obbligazione_scadenzarioBulk scadenzaScollegata = (Obbligazione_scadenzarioBulk) i
					.next();
			Obbligazione_scadenzarioBulk scadenzaAssociata = compenso
					.getObbligazioneScadenzario();
			ObbligazioneBulk obblig = null;
			if (scadenzaAssociata != null)
				obblig = scadenzaAssociata.getObbligazione();

			aggiornaObbligazioneSuCancellazione(userContext, compenso,
					scadenzaScollegata, obblig, status);
		}
	}

	/**
	 * Viene richiesta la data odierna
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @return La data odierna
	 * 
	 **/
	public java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext userContext)
			throws it.cnr.jada.comp.ComponentException {

		try {
			CompensoHome home = (CompensoHome) getHome(userContext,
					CompensoBulk.class);
			return home.getServerDate();
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Pre: L'esercizio di scrivania è antecedente a quello corrente Post: La
	 * data restituita viene inizializzata al 31/12/esercizio scrivania
	 * 
	 * Pre: L'esercizio di scrivania NON è antecedente a quello corrente Post:
	 * La data restituita viene inizializzata alla data odierna
	 * 
	 * @param aUC
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            Il CompensoBulk la cui data deve essere inizializzata.
	 * 
	 * @return La data correttamente inizializzata
	 */

	private Timestamp getDataPerInizializzazioni(UserContext userContext,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			java.text.ParseException {
		CompensoBulk compenso = (CompensoBulk) bulk;

		java.sql.Timestamp tsOdierno = ((CompensoHome) getHome(userContext,
				compenso)).getServerDate();

		java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar
				.getInstance();
		tsOdiernoGregorian.setTime(tsOdierno);

		if (tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR) > compenso
				.getEsercizio().intValue()) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"dd/MM/yyyy");
			return (new java.sql.Timestamp(sdf.parse(
					"31/12/" + compenso.getEsercizio().intValue()).getTime()));
		}
		return tsOdierno;
	}

	private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(
			UserContext userContext, java.util.Enumeration scadenze)
			throws ComponentException {

		it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
		if (scadenze != null)
			while (scadenze.hasMoreElements()) {
				IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk) scadenze
						.nextElement();
				if (!scadenza.getFather().isTemporaneo()) {
					if (!documentiContabiliNonTemporanei.containsKey(scadenza
							.getFather())) {
						java.util.Vector allInstances = new java.util.Vector();
						allInstances.addElement(scadenza.getFather());
						documentiContabiliNonTemporanei.put(scadenza
								.getFather(), allInstances);
					} else {
						((java.util.Vector) documentiContabiliNonTemporanei
								.get(scadenza.getFather())).add(scadenza
								.getFather());
					}
				}
			}
		return documentiContabiliNonTemporanei;
	}

	private java.sql.Timestamp getFirstDayOfYear(int year) {

		java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
		calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
		calendar.set(java.util.Calendar.MONTH, 0);
		calendar.set(java.util.Calendar.YEAR, year);
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		return new java.sql.Timestamp(calendar.getTime().getTime());
	}

	// ^^@@
	/**
	 * Identificativo univoco progressivo per la gestione delle estrazioni
	 * fiscali PreCondition: Viene richiesta un progressivo PostCondition:
	 * ritorna un valore PreCondition: Si è verificato un errore. PostCondition:
	 * Viene inviato un messaggio con il relativo errore ritornato dal DB
	 */
	// ^^@@
	private java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext)
			throws it.cnr.jada.comp.ComponentException {

		// ricavo il progressivo unico pg_estr
		java.math.BigDecimal pg_estr = new java.math.BigDecimal(0);
		try {
			LoggableStatement ps = new LoggableStatement(
					getConnection(userContext),
					"select CNRSEQ00_PG_ESTR_FISC.nextval from dual", true,
					this.getClass());
			try {
				java.sql.ResultSet rs = ps.executeQuery();
				try {
					if (rs.next())
						pg_estr = rs.getBigDecimal(1);
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
		return pg_estr;

	}

	private String getStatoRiporto(UserContext context, CompensoBulk compenso)
			throws ComponentException {
		try {
			RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
							RiportoDocAmmComponentSession.class);
			return session.getStatoRiporto(context, compenso);
		} catch (Throwable t) {
			throw handleException(compenso, t);
		}
	}

	private String getStatoRiportoInScrivania(UserContext context,
			CompensoBulk compenso) throws ComponentException {
		try {
			RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
							RiportoDocAmmComponentSession.class);
			return session.getStatoRiportoInScrivania(context, compenso);
		} catch (Throwable t) {
			throw handleException(compenso, t);
		}
	}

	/**
	 * Gestione della validazione del terzo selezionato Ritorna una
	 * ComponentException con il messaggio realtivo all'errore
	 * 
	 * errorCode Significato ========= =========== 0 Tutto bene 1 Terzo assente
	 * 2 Terzo non valido alla data registrazione 3 Controllo se ho inserito le
	 * modalità di pagamento 4 Controllo se la modalità di pagamento è valida
	 * (ha una banca associata) 5 Tipo rapporto assente 6 Tipo di rapporto non
	 * valido in data inizio competenza coge 7 Tipo trattamento assente 8 Tipo
	 * trattamento non valido alla data registrazione
	 * 
	 **/
	private void handleExceptionsTerzo(int error) throws ComponentException {

		switch (error) {
		case 1: {
			throw new it.cnr.jada.comp.ApplicationException("Inserire il terzo");
		}
		case 2: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Il Terzo selezionato non è valido in Data Registrazione");
		}
		case 3: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare la Modalità di pagamento");
		}
		case 4: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare una Modalità di Pagamento valida");
		}
		case 5: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo Rapporto");
		}
		case 6: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Il Tipo Rapporto selezionato non è valido alla Data Inizio Competenza");
		}
		case 7: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo Trattamento");
		}
		case 8: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Il Tipo Trattamento selezionato non è valido alla Data Registrazione");
		}
		case 9: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo Rapporto INPS");
		}
		case 10: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo Attività INPS");
		}
		case 11: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo altra Assic. INPS");
		}
		case 12: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Comune in cui viene svolta l'Attività lavorativa");
		}
		case 13: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare un Conto valido");
		}
		case 14: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Tipo Prestazione");
		}
		case 15: {
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare il Terzo Pignorato");
		}	
	}
	}

	/**
	 * Prepara un OggettoBulk (compenso) per l'inserimento
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Verifica stato esercizio Pre: Esercizio aperto Post: Il sistema
	 * prosegue con l'inizializzazione del compenso
	 * 
	 * Nome: Verifica stato esercizio Pre: Esercizio chiuso Post: Viene generata
	 * una ApplicationException "Impossibile crare un compenso".
	 * 
	 * Nome: Inizializzazione della data di registrazione e delle date di
	 * competenza del compenso. Pre: Esercizio del compenso (esercizio
	 * scrivania) antecedente a quello corrente Post: Le date vengono
	 * inizializzate al 31/12 dell'esercizio del compenso
	 * 
	 * Nome: Inizializzazione della data di registrazione e delle date di
	 * competenza del compenso. Pre: Esercizio del compenso (esercizio
	 * scrivania) uguale a quello corrente Post: Le date vengono inizializzate
	 * alla data odierna
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da inizializzare per l'inserimento
	 * @return l'OggettoBulk inizializzato
	 */

	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {

		setSavePoint(userContext, "COMP_DA_DOC_AMM_UNDO_SP");
		CompensoBulk compenso = (CompensoBulk) super
				.inizializzaBulkPerInserimento(userContext, bulk);

		try {
			if (!verificaStatoEsercizio(
					userContext,
					new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
							compenso.getCd_cds(),
							((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
									.getEsercizio())))
				throw new it.cnr.jada.comp.ApplicationException(
						"Impossibile inserire un compenso per un esercizio non aperto!");

			// Inizializzazione e controlli della data di registrazione
			compenso.setDt_registrazione(getDataPerInizializzazioni(
					userContext, bulk));
			compenso.setStato_liquidazione(compenso.SOSP);
			compenso.setCausale(compenso.ATTLIQ);
			// r.p. 20/10/2008 commentato perchè non vengono valorizzate in
			// automatico data inizio e fine competenze
			// compenso.setDt_a_competenza_coge(compenso.getDt_registrazione());
			// compenso.setDt_da_competenza_coge(compenso.getDt_registrazione());
			compenso = valorizzaInfoDocEle(userContext,compenso);
			
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(compenso, e);
		} catch (java.text.ParseException e) {
			throw handleException(compenso, e);
		} 
		
		return compenso;
	}

	/**
	 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
	 * operazione di modifica.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Esercizio successivo a quello di srivania Pre: L'esercizio del
	 * compenso da caricare e' successivo a quello di scrivania Post: Viene
	 * sollevata una ApplicationException
	 * 
	 * Nome: Tutti i controlli superati Pre: L'OggettoBulk specificato esiste.
	 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti
	 * collegati e preparato per l'operazione di presentazione e modifica
	 * nell'interfaccia visuale. L'operazione di lettura viene effettuata con
	 * una FetchPolicy il cui nome è ottenuto concatenando il nome della
	 * component con la stringa ".edit"
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            l'OggettoBulk da preparare
	 * @return l'OggettoBulk preparato
	 * 
	 *         Metodo privato chiamato: completaCompenso(UserContaxt
	 *         userContext, CompensoBulk compenso);
	 * 
	 **/
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,
			OggettoBulk bulk) throws ComponentException {

		setSavePoint(userContext, "COMP_DA_DOC_AMM_UNDO_SP");
		CompensoBulk compenso = (CompensoBulk) bulk;

		// Quando si salva un compenso da missione occorre memorizzarsi i
		// documenti
		// contabili elaborati dal compenso ma non associati altrimenti vengono
		// persi
		// durante il reload del compenso salvato.
		// Tali documenti sono necessari perche' successivamente la missione
		// dovra'
		// aggiornare i saldi
		java.util.Vector docContCancellati = null;
		if (compenso.isDaMissione())
			docContCancellati = compenso.getDocumentiContabiliCancellati();

		if (compenso.getEsercizio() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"L'esercizio del documento non è valorizzato! Impossibile proseguire.");

		if (compenso.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext).intValue())
			throw new it.cnr.jada.comp.ApplicationException(
					"Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

		it.cnr.jada.bulk.PrimaryKeyHashMap relazioniObbligazione = compenso
				.getRelationsDocContForSaldi();

		compenso = (CompensoBulk) super.inizializzaBulkPerModifica(userContext,
				compenso);
		completaCompenso(userContext, compenso);

		compenso.setStatoCompensoToContabilizzaCofi();
		compenso.setRelationsDocContForSaldi(relazioniObbligazione);
		compenso = valorizzaInfoDocEle(userContext,compenso);

		// Faccio una copia del compenso appena caricato
		if (userContext.isTransactional()) {
			Long pgTmp = assegnaProgressivoTemporaneo(userContext, compenso);
			compenso.setPgCompensoPerClone(pgTmp);
			copiaCompenso(userContext, compenso);
		}

		// Reimposto i documenti contabili elaborati al compenso ma non
		// associati ad esso
		if (compenso.isDaMissione())
			compenso.setDocumentiContabiliCancellati(docContCancellati);

		try {
			compenso.setTrovato(ricercaDatiTrovato(userContext, compenso.getPg_trovato()));
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
		Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
				.filter(Scrittura_partita_doppiaHome.class::isInstance)
				.map(Scrittura_partita_doppiaHome.class::cast)
				.orElseThrow(() -> new DetailedRuntimeException("Partita doppia Home not found"));
		try {
			final Optional<Scrittura_partita_doppiaBulk> scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(compenso);
			if (scritturaOpt.isPresent()) {
				Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
				scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
						.findMovimentiDareColl(userContext, scrittura)));
				scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
						.findMovimentiAvereColl(userContext, scrittura)));
				compenso.setScrittura_partita_doppia(scrittura);
			}
		} catch (PersistencyException e) {
			throw handleException(compenso, e);
		}
		return compenso;
	}

	public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException {
		if (trovato != null){
			RicercaTrovato ricercaTrovato;
			try {
				ricercaTrovato = new RicercaTrovato();
				return ricercaTrovato.ricercaDatiTrovato(userContext, trovato);
			} catch (FileNotFoundException e) {
				throw new ApplicationException("File in configurazione non trovato "+e.getMessage());
			} catch (IOException e) {
				throw new ApplicationException("Eccezione di IO "+ e.getMessage());
			} catch (Exception e) {
				throw new ApplicationException("Eccezione generica "+e.getMessage());
			}
		}
		return new TrovatoBulk();
	}

	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	private it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext userContext,
			StampaCertificazioneVBulk stampa)
			throws it.cnr.jada.comp.ComponentException {

		stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext));

		stampa.setAnagraficoForPrint(new AnagraficoBulk());

		return stampa;
	}

	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	private it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext userContext,
			StampaPartitarioCompensiVBulk stampa)
			throws it.cnr.jada.comp.ComponentException {

		stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext));
		stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext
				.getCd_cds(userContext));
		stampa
				.setCd_unita_organizzativa(it.cnr.contab.utenze00.bp.CNRUserContext
						.getCd_unita_organizzativa(userContext));

		stampa
				.setDataInizio(getFirstDayOfYear(it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext).intValue()));
		stampa.setDataFine(getDataOdierna(userContext));

		stampa.setAnagraficoForPrint(new AnagraficoBulk());

		return stampa;
	}

	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	private it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext userContext,
			StampaRiepilogoCompensiVBulk stampa)
			throws it.cnr.jada.comp.ComponentException {

		stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext));
		stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext
				.getCd_cds(userContext));
		stampa
				.setCd_unita_organizzativa(it.cnr.contab.utenze00.bp.CNRUserContext
						.getCd_unita_organizzativa(userContext));

		stampa.setTerzoForPrint(new TerzoBulk());

		// stampa.getTerzoForPrint().setCrudStatus(OggettoBulk.TO_BE_CREATED);
		// stampa.getTerzoForPrint().setAnagrafico(new AnagraficoBulk());
		// stampa.getTerzoForPrint().getAnagrafico().setCrudStatus(OggettoBulk.TO_BE_CREATED);

		try {
			String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext
					.getCd_cds(userContext);
			it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome) getHome(
					userContext, CdsBulk.class);
			it.cnr.contab.config00.sto.bulk.CdsBulk cds_scrivania = (CdsBulk) cds_home
					.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

			stampa
					.setUoEnte(cds_scrivania
							.getCd_tipo_unita()
							.equals(
									it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE));

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(stampa, e);
		}

		return stampa;
	}

	private it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			UserContext userContext, StampaCompensiBulk stampa)
			throws it.cnr.jada.comp.ComponentException {
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
		stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
		// stampa.setTerzoForPrint(new TerzoBulk());

		return stampa;
	}

	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			UserContext userContext, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof StampaPartitarioCompensiVBulk)
			inizializzaBulkPerStampa(userContext,
					(StampaPartitarioCompensiVBulk) bulk);
		else if (bulk instanceof StampaRiepilogoCompensiVBulk)
			inizializzaBulkPerStampa(userContext,
					(StampaRiepilogoCompensiVBulk) bulk);
		else if (bulk instanceof StampaCertificazioneVBulk)
			inizializzaBulkPerStampa(userContext,
					(StampaCertificazioneVBulk) bulk);
		else if (bulk instanceof StampaCompensiBulk)
			inizializzaBulkPerStampa(userContext, (StampaCompensiBulk) bulk);
		return bulk;
	}

	/**
	 * Prepara un compenso per la presentazione all'utente per una possibile
	 * operazione di modifica. Il compenso viene completato con i dati ricevuti
	 * dalla minicarriera
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Inizializzazione compenso da minicarriera Pre: Si richiede di
	 * inizializzare il compenso con i dati della minicarriera Post: Viene
	 * restituito il compenso inizializzato con tutti gli oggetti collegati e
	 * preparato per l'operazione di presentazione e modifica nell'interfaccia
	 * visuale.
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da preparare
	 * @param minicarriera
	 *            la minicarriera da cui prendere le informazioni per completare
	 *            il compenso
	 * @return il compenso preparato
	 * 
	 **/
	public CompensoBulk inizializzaCompensoPerMinicarriera(
			UserContext userContext, CompensoBulk compenso,
			MinicarrieraBulk minicarriera, java.util.List rate)
			throws ComponentException {

		if (rate == null || rate.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException(
					"Selezionare le rate che devono originare il compenso");

		// Settaggio Testata Compenso
		try {
			java.sql.Timestamp dataReg = getHome(userContext,
					CompensoBulk.class).getServerDate();
			java.util.Calendar calendar = java.util.GregorianCalendar
					.getInstance();
			calendar.setTime(dataReg);
			int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(userContext).intValue();
			if (calendar.get(java.util.Calendar.YEAR) != esercizioInScrivania)
				dataReg = new java.sql.Timestamp(
						new java.text.SimpleDateFormat("dd/MM/yyyy").parse(
								"31/12/" + esercizioInScrivania).getTime());
			compenso.setDt_registrazione(dataReg);
		} catch (java.text.ParseException ex) {
			throw handleException(ex);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}

		compenso.setDt_da_competenza_coge(((Minicarriera_rataBulk) rate.get(0))
				.getDt_inizio_rata());
		compenso.setDt_a_competenza_coge(((Minicarriera_rataBulk) rate.get(rate
				.size() - 1)).getDt_fine_rata());

		compenso.setDs_compenso("Compenso per Minicarriera");
		compenso.setFl_compenso_minicarriera(Boolean.TRUE);
		compenso.setAperturaDaMinicarriera(Boolean.TRUE);

		// Settaggio Terzo
		completaTerzo(userContext, compenso, minicarriera.getPercipiente());
		compenso.impostaModalitaPagamento(minicarriera.getModalita_pagamento());
		compenso.impostaTerminiPagamento(minicarriera.getTermini_pagamento());
		compenso.setBanca(minicarriera.getBanca());
		compenso.impostaTipoRapporto(minicarriera.getTipo_rapporto());
		if (compenso.getTipoRapporto() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Il tipo rapporto specificato non è valido per il periodo definito per la/e rata/e selezionata/e!");
		compenso.impostaTipoTrattamento(minicarriera.getTipo_trattamento());
		
		compenso.impostaTipoPrestazioneCompenso(minicarriera.getTipoPrestazioneCompenso()); 

		// per settare i dati inps a seconda del trattamento
		try {
			setDatiInps(userContext, compenso);
		} catch (SQLException e) {
			throw new ComponentException(e);
		}
		if (minicarriera.getIncarichi_repertorio() != null) {
			Incarichi_repertorio_annoBulk incarico_anno = new Incarichi_repertorio_annoBulk();
			incarico_anno.setIncarichi_repertorio(minicarriera
					.getIncarichi_repertorio());
			compenso.setIncarichi_repertorio_anno(incarico_anno);
		}
		compenso.setTi_istituz_commerc(minicarriera.getTi_istituz_commerc());
		compenso.setFl_escludi_qvaria_deduzione(minicarriera
				.getFl_escludi_qvaria_deduzione() == null ? Boolean.FALSE
				: minicarriera.getFl_escludi_qvaria_deduzione());

		// Se almeno una delle rate è a tassazione separata (data scad rata <
		// esercizio minicarr)...
		// In minicarriera su creazione compenso viene eseguita validazione di
		// congruenza
		// sulle rate selezionate --> necessario e sufficiente controllare
		// l'ultima rata
		if (minicarriera.isRataATassazioneSeparata((Minicarriera_rataBulk) rate
				.get(rate.size() - 1))) {
			// Si tassazione sep
			compenso.setFl_compenso_mcarriera_tassep(minicarriera
					.getFl_tassazione_separata());
			compenso.setAliquota_irpef_tassep(minicarriera
					.getAliquota_irpef_media());
		} else {
			// No tassazione sep
			compenso.setFl_compenso_mcarriera_tassep(Boolean.FALSE);
			// NB: l'aliquota media è a precisione 6
			compenso.setAliquota_irpef_tassep(new java.math.BigDecimal(0)
					.setScale(6, java.math.BigDecimal.ROUND_HALF_EVEN));
		}

		// L'importo lordo e la quota esente CO/RI vengono impostati dal package
		// prima della chiamata di questo metodo
		setDatiLiquidazione(userContext, compenso);

		compenso.setStatoCompensoToEseguiCalcolo();
		compenso.setMinicarriera(minicarriera);
		compenso.setToBeCreated();
		compenso.setUser(minicarriera.getUser());

		return compenso;
	}

	public CompensoBulk inizializzaCompensoPerBonus(UserContext userContext,
			CompensoBulk compenso, BonusBulk bonus) throws ComponentException {

		// Settaggio Testata Compenso
		compenso.setDt_registrazione(bonus.getDt_registrazione());
		compenso.setDt_da_competenza_coge(bonus.getDt_registrazione());
		compenso.setDt_a_competenza_coge(bonus.getDt_registrazione());

		compenso.setDs_compenso("Compenso per Bonus");
		// compenso.setAperturaDaMinicarriera(Boolean.TRUE);

		// Settaggio Terzo
		completaTerzo(userContext, compenso, bonus.getVTerzo());
		compenso.setTipoRapporto(bonus.getTipo_rapporto());
		compenso.impostaTipoRapporto(bonus.getTipo_rapporto());
		if (compenso.getTipoRapporto() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Il tipo rapporto specificato non è valido per il periodo definito!");
		Tipo_trattamentoHome home = (Tipo_trattamentoHome) getHome(userContext,
				Tipo_trattamentoBulk.class);
		try {
			compenso.impostaTipoTrattamento(home
					.findTipoTrattamentoBonusValido(bonus));
		} catch (PersistencyException e) {
			handleException(e);
		}
		// compenso.impostaTipoTrattamento(minicarriera.getTipo_trattamento());
		compenso.setFl_senza_calcoli(Boolean.TRUE);
		compenso.setBonus(bonus);
		compenso
				.setTi_istituz_commerc(CompensoBulk.TIPO_COMPENSO_ISTITUZIONALE);
		compenso.setStatoCompensoToEseguiCalcolo();
		compenso.setToBeCreated();
		compenso.setUser(bonus.getUser());

		return compenso;
	}

	/**
	 * Prepara un compenso per la presentazione all'utente per una possibile
	 * operazione di modifica. Il compenso viene completato con i dati ricevuti
	 * dalla missione
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Inizializzazione compenso da missione Pre: Si richiede di
	 * inizializzare il compenso con i dati della missione Post: Viene
	 * restituito il compenso inizializzato con tutti gli oggetti collegati e
	 * preparato per l'operazione di presentazione e modifica nell'interfaccia
	 * visuale.
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da preparare
	 * @param missione
	 *            la missione da cui prendere le informazioni per completare il
	 *            compenso
	 * @return il compenso preparato
	 * 
	 **/
	public CompensoBulk inizializzaCompensoPerMissione(UserContext userContext,
			CompensoBulk compenso,
			it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione)
			throws ComponentException {
		calcolaImportiCompenso(userContext, compenso, missione);

		// Settaggio Testata Compenso
		java.sql.Timestamp dataReg = compenso.getDt_registrazione();
		// commentato perchè non deve essere sovrascritta con quella della
		// missione
		// compenso.setDt_registrazione(missione.getDt_registrazione());

		java.sql.Timestamp aDataInizio = it.cnr.jada.util.DateUtils
				.truncate(missione.getDt_inizio_missione());
		compenso.setDt_da_competenza_coge(aDataInizio);

		java.sql.Timestamp aDataFine = it.cnr.jada.util.DateUtils
				.truncate(missione.getDt_fine_missione());
		compenso.setDt_a_competenza_coge(aDataFine);

		compenso.setDs_compenso("Compenso per Missione");
		compenso.setFl_diaria(Boolean.TRUE);
		compenso.setTi_istituz_commerc(missione.getTi_istituz_commerc());
		compenso.setAnnoSolare(missione.getAnnoSolare());
		compenso.setEsercizioScrivania(missione.getEsercizioScrivania());

		// Settaggio Terzo
		completaTerzo(userContext, compenso, missione.getV_terzo());
		compenso.impostaModalitaPagamento(missione.getModalita_pagamento());
		compenso.impostaTerminiPagamento(missione.getTermini_pagamento());
		compenso.setBanca(missione.getBanca());

		compenso.impostaTipoRapporto(missione.getTipo_rapporto());

		String cdTrattCompenso = compenso.getCd_trattamento();
		compenso.impostaTipoTrattamento(missione.getTipo_trattamento());

		// per settare i dati inps a seconda del trattamento
		try {
			setDatiInps(userContext, compenso);
		} catch (SQLException e) {
			throw new ComponentException(e);
		}

		if ((dataReg == null || dataReg.compareTo(compenso
				.getDt_registrazione()) != 0)
				|| (cdTrattCompenso == null || !cdTrattCompenso.equals(compenso
						.getCd_trattamento()))) {
			compenso.resetDatiLiquidazione();
			setDatiLiquidazione(userContext, compenso);
		}

		compenso.setStatoCompensoToEseguiCalcolo();
		compenso.setMissione(missione);
		compenso.setToBeCreated();
		compenso.setUser(missione.getUser());

		return compenso;
	}
	
	public CompensoBulk inizializzaCompensoPerFattura(
			UserContext userContext, CompensoBulk compenso,
			Fattura_passiva_IBulk fp)
			throws ComponentException {

		compenso.setDt_registrazione(fp.getDt_registrazione());
		compenso.setDt_da_competenza_coge(fp.getDt_da_competenza_coge());
		compenso.setDt_a_competenza_coge(fp.getDt_a_competenza_coge());
		
		compenso.setStato_pagamento_fondo_eco(fp.getStato_pagamento_fondo_eco());
		compenso.setStato_liquidazione(fp.getStato_liquidazione());
		compenso.setCausale(fp.getCausale());
		
		compenso.setDs_compenso(fp.getDs_fattura_passiva());
		
		compenso.setEsercizio_fattura_fornitore(fp.getEsercizio_fattura_fornitore());
		compenso.setDt_fattura_fornitore(fp.getDt_fattura_fornitore());
		compenso.setNr_fattura_fornitore(fp.getNr_fattura_fornitore());
	
		compenso.setFl_generata_fattura(fp.getFl_fattura_compenso()); // sarà sempre Y
		compenso.setData_protocollo(fp.getData_protocollo());
		compenso.setNumero_protocollo(fp.getNumero_protocollo());
		compenso.setDt_scadenza(fp.getDt_scadenza());

		compenso.setFl_documento_ele(fp.isElettronica());
		compenso.setFl_split_payment(fp.getFl_split_payment());
		compenso.setTi_istituz_commerc(fp.getTi_istituz_commerc());
		if(compenso.isElettronica())
		try {
			compenso.setUserAbilitatoSenzaCalcolo(((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(userContext, "AMMFATTURDOCSFATPASA"));
		} catch (RemoteException e) {
			throw handleException(compenso, e);
		}
		
		// Settaggio Terzo
		completaTerzo(userContext, compenso);
		
		compenso.impostaModalitaPagamento(fp.getModalita_pagamento());
		compenso.impostaTerminiPagamento(fp.getTermini_pagamento());
		compenso.setBanca(fp.getBanca());
		compenso.impostaTipoRapporto(getTipoRapportoProf(userContext));
		
		if (compenso.getTipoRapporto() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Non esiste un tipo rapporto PROF valido per il periodo di competenza!");

		compenso.setTipoTrattamento(null);
		compenso.setTipiTrattamento(findTipiTrattamento(userContext, compenso));
        
		compenso.setFl_escludi_qvaria_deduzione(Boolean.FALSE);

		compenso.setFl_compenso_mcarriera_tassep(Boolean.FALSE);
		compenso.setAliquota_irpef_tassep(new java.math.BigDecimal(0)
					.setScale(6, java.math.BigDecimal.ROUND_HALF_EVEN));

		// L'importo lordo e la quota esente CO/RI vengono impostati dal package
		// prima della chiamata di questo metodo

		compenso.impostaVoceIva(fp);
		
		compenso.setStatoCompensoToEseguiCalcolo();
		compenso.setFatturaPassiva(fp);
		compenso.setToBeCreated();
		compenso.setUser(fp.getUser());

		return compenso;
	}

	public CompensoBulk inserisciCompenso(UserContext userContext,
			CompensoBulk compenso) throws it.cnr.jada.comp.ComponentException {

		try {

			Long pgTmp = compenso.getPg_compenso();
			Long pg = assegnaProgressivo(userContext, compenso);
			compenso.setPg_compenso(pg);
			compenso.setUser(userContext.getUser());

			insertBulk(userContext, compenso);
			inserisciContributiERitenute(userContext, compenso, pgTmp);
			inserisciContributiERitenuteDett(userContext, compenso, pgTmp);

			return compenso;
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	/**
	 * Inserisce nel db i contributi ritenuti associati al compenso assegnando
	 * loro il progressivo definitivo del compenso
	 * 
	 **/
	private void inserisciContributiERitenute(UserContext userContext,
			CompensoBulk compenso, Long pgTemp) throws ComponentException {

		String schema = it.cnr.jada.util.ejb.EJBCommonServices
				.getDefaultSchema();
		String sql = "INSERT INTO " + schema + "CONTRIBUTO_RITENUTA (";
		sql = sql
				+ "CD_CDS, CD_UNITA_ORGANIZZATIVA, ESERCIZIO, PG_COMPENSO, CD_CONTRIBUTO_RITENUTA, TI_ENTE_PERCIPIENTE, DT_INI_VALIDITA, MONTANTE, IMPONIBILE, ALIQUOTA, BASE_CALCOLO, AMMONTARE, AMMONTARE_LORDO, STATO_COFI_CR, CD_CDS_OBBLIGAZIONE, ESERCIZIO_OBBLIGAZIONE, ESERCIZIO_ORI_OBBLIGAZIONE, PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, CD_CDS_ACCERTAMENTO, ESERCIZIO_ACCERTAMENTO, ESERCIZIO_ORI_ACCERTAMENTO, PG_ACCERTAMENTO, PG_ACCERTAMENTO_SCADENZARIO, DUVA, UTCR, DACR, UTUV, PG_VER_REC, IMPONIBILE_LORDO, IM_DEDUZIONE_IRPEF, IM_DEDUZIONE_FAMILY, IM_CORI_SOSPESO) ";
		sql = sql + "SELECT ";
		sql = sql
				+ "CD_CDS, CD_UNITA_ORGANIZZATIVA, ESERCIZIO, ?, CD_CONTRIBUTO_RITENUTA, TI_ENTE_PERCIPIENTE, DT_INI_VALIDITA, MONTANTE, IMPONIBILE, ALIQUOTA, BASE_CALCOLO, AMMONTARE, AMMONTARE_LORDO, STATO_COFI_CR, CD_CDS_OBBLIGAZIONE, ESERCIZIO_OBBLIGAZIONE, ESERCIZIO_ORI_OBBLIGAZIONE, PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, CD_CDS_ACCERTAMENTO, ESERCIZIO_ACCERTAMENTO, ESERCIZIO_ORI_ACCERTAMENTO, PG_ACCERTAMENTO, PG_ACCERTAMENTO_SCADENZARIO, DUVA, UTCR, DACR, UTUV, PG_VER_REC, IMPONIBILE_LORDO, IM_DEDUZIONE_IRPEF , IM_DEDUZIONE_FAMILY, IM_CORI_SOSPESO ";
		sql = sql
				+ "FROM "
				+ schema
				+ "CONTRIBUTO_RITENUTA WHERE CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_COMPENSO = ?";

		try {
			LoggableStatement ps = new LoggableStatement(
					getConnection(userContext), sql, true, this.getClass());
			try {
				ps.setInt(1, compenso.getPg_compenso().intValue()); // Progressivo
																	// definitivo
				ps.setString(2, compenso.getCd_cds()); // CD CDS
				ps.setString(3, compenso.getCd_unita_organizzativa()); // UNITA
																		// ORGANIZZATIVA
				ps.setInt(4, compenso.getEsercizio().intValue()); // ESERCIZIO
				ps.setInt(5, pgTemp.intValue()); // Progressivo temporaneo
				ps.execute();
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Inserisce nel db i dettagli dei contributi/ritenuti associati al compenso
	 * assegnando loro il progressivo definitivo del compenso
	 * 
	 **/
	private void inserisciContributiERitenuteDett(UserContext userContext,
			CompensoBulk compenso, Long pgTemp) throws ComponentException {

		String schema = it.cnr.jada.util.ejb.EJBCommonServices
				.getDefaultSchema();
		String sql = "INSERT INTO " + schema + "CONTRIBUTO_RITENUTA_DET (";
		sql = sql
				+ "CD_CDS, CD_UNITA_ORGANIZZATIVA, ESERCIZIO, PG_COMPENSO, CD_CONTRIBUTO_RITENUTA, TI_ENTE_PERCIPIENTE, PG_RIGA, IMPONIBILE, ALIQUOTA, BASE_CALCOLO, AMMONTARE, DUVA, UTCR, DACR, UTUV, PG_VER_REC) ";
		sql = sql + "SELECT ";
		sql = sql
				+ "CD_CDS, CD_UNITA_ORGANIZZATIVA, ESERCIZIO, ?, CD_CONTRIBUTO_RITENUTA, TI_ENTE_PERCIPIENTE, PG_RIGA, IMPONIBILE, ALIQUOTA, BASE_CALCOLO, AMMONTARE, DUVA, UTCR, DACR, UTUV, PG_VER_REC ";
		sql = sql
				+ "FROM "
				+ schema
				+ "CONTRIBUTO_RITENUTA_DET WHERE CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_COMPENSO = ?";

		try {
			LoggableStatement ps = new LoggableStatement(
					getConnection(userContext), sql, true, this.getClass());
			try {
				ps.setInt(1, compenso.getPg_compenso().intValue()); // Progressivo
																	// definitivo
				ps.setString(2, compenso.getCd_cds()); // CD CDS
				ps.setString(3, compenso.getCd_unita_organizzativa()); // UNITA
																		// ORGANIZZATIVA
				ps.setInt(4, compenso.getEsercizio().intValue()); // ESERCIZIO
				ps.setInt(5, pgTemp.intValue()); // Progressivo temporaneo
				ps.execute();
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene richiesto lo stato cofi del compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Compenso ANNULLATO - Stato COFI uguale ad 'A' Pre: Il compenso è
	 * annullato Post: Ritorna <true>. Il compenso è annullato
	 * 
	 * Nome: Compenso NON ANNULLATO - Stato COFI diverso da 'A' Pre: Il compenso
	 * non è annullato Post: Ritorna <false>. Il compenso non è annullato
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da controllare
	 * @return vero se il compenso è anullato, falso altrimenti
	 * 
	 **/
	public boolean isCompensoAnnullato(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			CompensoHome home = (CompensoHome) getHome(userContext, compenso);
			CompensoBulk obj = (CompensoBulk) home.findByPrimaryKey(compenso);
			if (obj == null)
				return false;

			return (compenso.STATO_ANNULLATO.equals(obj.getStato_cofi()));

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	private boolean isTipoRapportoValido(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			Tipo_rapportoHome home = (Tipo_rapportoHome) getHome(userContext,
					Tipo_rapportoBulk.class);
			
			return home
					.isTipoRapportoValido(compenso.getV_terzo(), compenso
							.getCd_tipo_rapporto(), compenso
							.getDt_da_competenza_coge());

		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
	}

	private boolean isTipoTrattamentoValido(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			Tipo_trattamentoHome home = (Tipo_trattamentoHome) getHome(
					userContext, Tipo_trattamentoBulk.class);

			Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
			filtro.setCdTipoRapporto(compenso.getCd_tipo_rapporto());
			filtro.setCdTipoTrattamento(compenso.getCd_trattamento());
			filtro.setTipoAnagrafico(compenso.getTi_anagrafico());
			filtro.setDataValidita(compenso.getDt_registrazione());
			filtro.setFlSenzaCalcoli(compenso.getFl_senza_calcoli());
			filtro
					.setFlDefaultCongualio(new Boolean(compenso
							.isDaConguaglio()));
			filtro.setTiIstituzionaleCommerciale(compenso
					.getTi_istituz_commerc());

			filtro.setFlBonus(compenso.isDaBonus());
			filtro.setFlSplitPayment(compenso.getFl_split_payment()); 
			if (filtro.getCdTipoRapporto() != null
					&& filtro.getCdTipoRapporto().equals("DIP")) {
				try {
					TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
							.getHome(TerzoBulk.class);
					TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
					TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

					RapportoHome rHome = (RapportoHome) getHomeCache(
							userContext).getHome(RapportoBulk.class);
					java.util.Collection collRapp = rHome
							.findByCdAnagCdTipoRapporto(t.getCd_anag(), filtro
									.getCdTipoRapporto());
					boolean exit = false;
					for (java.util.Iterator i = collRapp.iterator(); i
							.hasNext()
							&& !exit;) {
						RapportoBulk r = (RapportoBulk) i.next();
						exit = true;
						if (r.getCd_ente_prev_sti() == null)
							// throw new
							// it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
							// non blocco perchè potrebbero esserci trattamenti
							// che non prevedono contributi previdenziali
							// quindi passo il codice fittizio 'XX'
							filtro.setEntePrev("XX");
						else
							filtro.setEntePrev(r.getCd_ente_prev_sti());
						if (r.getCd_rapp_impiego_sti() == null)
							throw new it.cnr.jada.comp.ApplicationException(
									"Per il dipendente in esame non è definito un Rapporto di Impiego!");
						else {
							Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(
									userContext, Ass_rapp_impiegoBulk.class);
							Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(
									r.getCd_rapp_impiego_sti());
							Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk) assHome
									.findByPrimaryKey(assKey);
							filtro.setTipoRappImpiego(ass
									.getTipo_rapp_impiego());
						}
					}
				} catch (IntrospectionException e) {
					throw handleException(e);
				}
				// solo per il rapporto DIP aggiungo il filtro "Anno prec" a
				// seconda della data di inizio competenza
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				data_da.setTime(compenso.getDt_da_competenza_coge());
				if (data_da.get(GregorianCalendar.YEAR) == (compenso
						.getEsercizio() - 1))
					filtro.setFlAnnoPrec(new Boolean(true));
				else
					filtro.setFlAnnoPrec(new Boolean(false));

			}
			if (compenso.getDt_da_competenza_coge() != null
					&& compenso.getDt_a_competenza_coge() != null) {
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar
						.getInstance();

				data_da.setTime(compenso.getDt_da_competenza_coge());
				data_a.setTime(compenso.getDt_a_competenza_coge());

				TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
						.getHome(TerzoBulk.class);
				TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
				TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(
						userContext).getHome(AnagraficoBulk.class);
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk) aHome
						.findByPrimaryKey(aKey);

				if (data_da.get(GregorianCalendar.YEAR) == data_a
						.get(GregorianCalendar.YEAR)) {
					if (a.getFl_cervellone()
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_inizio_res_fis().intValue()) < 0)
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_fine_agevolazioni().intValue()) > 0)) {
						filtro.setFlAgevolazioniCervelli(new Boolean(a
								.getFl_cervellone()));
					} else
						filtro.setFlAgevolazioniCervelli(new Boolean(false));
				} else
				// solo per i congualgli dei cervelli gestisco anche le date a
				// cavallo
				if (compenso.getFl_compenso_conguaglio()
						&& a.getFl_cervellone()
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_inizio_res_fis()
										.intValue()) < 0)
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_fine_agevolazioni()
										.intValue()) > 0)) {
					filtro.setFlAgevolazioniCervelli(new Boolean(a
							.getFl_cervellone()));
				} else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			
			if (filtro.getCdTipoRapporto() != null && getTipoRapportoProf(userContext).getCd_tipo_rapporto() != null
					&& filtro.getCdTipoRapporto().equals(getTipoRapportoProf(userContext).getCd_tipo_rapporto())) 
			{
					if (compenso.isGestione_doc_ele())
					{
						if(!compenso.getFl_generata_fattura())
						{
							//se non ho scelto senza calcoli non devo vedere nulla
							if (!compenso.getFl_senza_calcoli())
								filtro.setTipoAnagrafico("X");
						}
						
					} 
			}
			return home.isTipoTrattamentoValido(filtro);

		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene caricato da db il Conguaglio (se presente) associato al compenso
	 * 
	 **/
	private void loadConguaglio(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {

			ConguaglioHome home = (ConguaglioHome) getHome(userContext,
					ConguaglioBulk.class);
			compenso.setConguaglio(home.findConguaglio(compenso));

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Vengono caricate da db le righe dei contributi/ritenute associate al
	 * compenso
	 * 
	 **/
	private void loadContributiERitenute(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			Contributo_ritenutaHome home = (Contributo_ritenutaHome) getHome(
					userContext, Contributo_ritenutaBulk.class);
			compenso.setContributi(home.loadContributiRitenute(compenso));

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene completato il compenso <compenso> settando i flag per la
	 * visualizzazione della regione irap, voce iva e tipologia rischio e
	 * caricando da db l'oggetto complesso
	 * 
	 * - tipologia rischio
	 * 
	 **/
	private void loadDatiLiquidazione(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso.getCd_regione_irap() != null)
			compenso.setVisualizzaRegioneIrap(true);

		if (compenso.getCd_voce_iva() != null)
			compenso.setVisualizzaVoceIva(true);

		loadTipologiaRischio(userContext, compenso);
		if (compenso.getCd_tipologia_rischio() != null)
			compenso.setVisualizzaTipologiaRischio(true);

	}

	/**
	 * Viene richiesta la lista dei Documenti Contabili (Mandati e Reversali)
	 * associati al compenso
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Compenso non pagato Pre: Viene richiesta la lista dei Documenti
	 * Contabili per un compenso non pagato Post: Viene restituita una
	 * ApplicationException con la descrizione dell'errore
	 * "Il compenso non è pagato"
	 * 
	 * Nome: Compenso pagato Pre: Viene richiesta la lista dei Documenti
	 * Contabili per un compenso pagato Post: Viene restituita la lista dei
	 * Documenti Contabili associati
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            il compenso di cui si vuole caricare i Documenti Contabili
	 * @return La lista dei Documenti Contabili associati al compenso
	 * 
	 **/
	public java.util.List loadDocContAssociati(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			if (!compenso.STATO_PAGATO.equals(compenso.getStato_cofi()))
				return null;

			V_doc_cont_compHome home = (V_doc_cont_compHome) getHome(
					userContext, V_doc_cont_compBulk.class);
			return home.loadAllDocCont(compenso);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(compenso, ex);
		}
	}

	/**
	 * Viene caricata da db la Minicarriera (se presente) associata al compenso
	 * 
	 **/
	private void loadMinicarriera(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {

			Minicarriera_rataHome home = (Minicarriera_rataHome) getHome(
					userContext, Minicarriera_rataBulk.class);
			java.util.List rate = home.findRate(compenso);
			if (rate != null && !rate.isEmpty())
				compenso.setMinicarriera(((Minicarriera_rataBulk) rate.get(0))
						.getMinicarriera());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	private BonusBulk loadBonus(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {

			BonusHome home = (BonusHome) getHome(userContext, BonusBulk.class);
			return (BonusBulk) home.findByPrimaryKey(new BonusBulk(compenso
					.getEsercizio_bonus(), compenso.getPg_bonus()));
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}
/*
	private Tipo_prestazione_compensoBulk loadTipoPrestazione(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		try {

			Tipo_prestazione_compensoHome home = (Tipo_prestazione_compensoHome) getHome(userContext, Tipo_prestazione_compensoBulk.class);
			return (Tipo_prestazione_compensoBulk) home.findByPrimaryKey(new Tipo_prestazione_compensoBulk(compenso
					.getTi_prestazione()));
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}
*/	
	/**
	 * Viene caricata da db la REGIONE di appartenenza del terzo associato
	 * all'Unita Organizzativa di scrivania
	 * 
	 **/
	private RegioneBulk loadRegioneIRAPDefalut(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			RegioneHome home = (RegioneHome) getHome(userContext,
					RegioneBulk.class);
			return home.loadRegioneIRAPDefault(compenso
					.getCd_unita_organizzativa());

		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene caricata da db la TIPOLOGIA RISCHIO valida in Data Registrazione
	 * compenso
	 * 
	 **/
	private void loadTipologiaRischio(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			if (compenso.getCd_tipologia_rischio() == null)
				return;

			Tipologia_rischioHome home = (Tipologia_rischioHome) getHome(
					userContext, Tipologia_rischioBulk.class);
			compenso
					.setTipologiaRischio(home.findTipologiaRischio(compenso
							.getCd_tipologia_rischio(), compenso
							.getDt_registrazione()));

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Viene caricato da db il TIPO TRATTAMENTO valido in Data Registrazione
	 * compenso
	 * 
	 **/
	private void loadTipoTrattamento(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {
			Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome) getHome(
					userContext, Tipo_trattamentoBulk.class);
			Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
			filtro.setCdTipoTrattamento(compenso.getCd_trattamento());
			filtro.setTipoAnagrafico(compenso.getTi_anagrafico());
			filtro.setDataValidita(compenso.getDt_registrazione());
			filtro.setFlSenzaCalcoli(compenso.getFl_senza_calcoli());
			filtro
					.setFlDefaultCongualio(new Boolean(compenso
							.isDaConguaglio()));
			filtro.setTiIstituzionaleCommerciale(compenso
					.getTi_istituz_commerc());
			filtro.setFlBonus(compenso.isDaBonus());
			filtro.setFlSplitPayment(compenso.getFl_split_payment());
			if (filtro.getCdTipoRapporto() != null
					&& filtro.getCdTipoRapporto().equals("DIP")) {
				try {
					TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
							.getHome(TerzoBulk.class);
					TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
					TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

					RapportoHome rHome = (RapportoHome) getHomeCache(
							userContext).getHome(RapportoBulk.class);
					java.util.Collection collRapp = rHome
							.findByCdAnagCdTipoRapporto(t.getCd_anag(), filtro
									.getCdTipoRapporto());
					boolean exit = false;
					for (java.util.Iterator i = collRapp.iterator(); i
							.hasNext()
							&& !exit;) {
						RapportoBulk r = (RapportoBulk) i.next();
						exit = true;
						if (r.getCd_ente_prev_sti() == null)
							// throw new
							// it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
							// non blocco perchè potrebbero esserci trattamenti
							// che non prevedono contributi previdenziali
							// quindi passo il codice fittizio 'XX'
							filtro.setEntePrev("XX");
						else
							filtro.setEntePrev(r.getCd_ente_prev_sti());
						if (r.getCd_rapp_impiego_sti() == null)
							throw new it.cnr.jada.comp.ApplicationException(
									"Per il dipendente in esame non è definito un Rapporto di Impiego!");
						else {
							Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(
									userContext, Ass_rapp_impiegoBulk.class);
							Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(
									r.getCd_rapp_impiego_sti());
							Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk) assHome
									.findByPrimaryKey(assKey);
							filtro.setTipoRappImpiego(ass
									.getTipo_rapp_impiego());
						}
					}
				} catch (IntrospectionException e) {
					throw handleException(e);
				}
				// solo per il rapporto DIP aggiungo il filtro "Anno prec" a
				// seconda della data di inizio competenza
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				data_da.setTime(compenso.getDt_da_competenza_coge());
				if (data_da.get(GregorianCalendar.YEAR) == (compenso
						.getEsercizio() - 1))
					filtro.setFlAnnoPrec(new Boolean(true));
				else
					filtro.setFlAnnoPrec(new Boolean(false));

			}
			if (compenso.getDt_da_competenza_coge() != null
					&& compenso.getDt_a_competenza_coge() != null) {
				GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar
						.getInstance();
				GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar
						.getInstance();

				data_da.setTime(compenso.getDt_da_competenza_coge());
				data_a.setTime(compenso.getDt_a_competenza_coge());

				TerzoHome tHome = (TerzoHome) getHomeCache(userContext)
						.getHome(TerzoBulk.class);
				TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
				TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);

				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(
						userContext).getHome(AnagraficoBulk.class);
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk) aHome
						.findByPrimaryKey(aKey);
				if (data_da.get(GregorianCalendar.YEAR) == data_a
						.get(GregorianCalendar.YEAR)) {
					if (a.getFl_cervellone()
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_inizio_res_fis().intValue()) < 0)
							&& !(new Integer(data_da
									.get(GregorianCalendar.YEAR)).compareTo(a
									.getAnno_fine_agevolazioni().intValue()) > 0)) {
						filtro.setFlAgevolazioniCervelli(new Boolean(a
								.getFl_cervellone()));
					} else
						filtro.setFlAgevolazioniCervelli(new Boolean(false));
				} else
				// solo per i congualgli dei cervelli gestisco anche le date a
				// cavallo
				if (compenso.getFl_compenso_conguaglio()
						&& a.getFl_cervellone()
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_inizio_res_fis()
										.intValue()) < 0)
						&& !(new Integer(data_da.get(GregorianCalendar.YEAR))
								.compareTo(a.getAnno_fine_agevolazioni()
										.intValue()) > 0)) {
					filtro.setFlAgevolazioniCervelli(new Boolean(a
							.getFl_cervellone()));
				} else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			
			if (filtro.getCdTipoRapporto() != null && getTipoRapportoProf(userContext).getCd_tipo_rapporto() != null
					&& filtro.getCdTipoRapporto().equals(getTipoRapportoProf(userContext).getCd_tipo_rapporto())) 
			{
					if (compenso.isGestione_doc_ele())
					{
						if(!compenso.getFl_generata_fattura())
						{
							//se non ho scelto senza calcoli non devo vedere nulla
							if (!compenso.getFl_senza_calcoli())
								filtro.setTipoAnagrafico("X");
						}
						
					} 
			}
			
			Tipo_trattamentoBulk tratt = trattHome
					.findTipoTrattamentoValido(filtro);

			compenso.setTipoTrattamento(tratt);
			if (isCompensoSoloInailEnte(tratt)){
				if (compenso.isDaMinicarriera()){
					throw new ApplicationException(
							"Attenzione! Non è possibile utilizzare un trattamento di tipo 'Torno Subito con solo INAIL a carico ente' per una minicarriera");
				}
				compenso.setCompensoSoloInailEnte(true);
			}

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	private Boolean isCompensoSoggettoConguaglio(Tipo_trattamentoBulk tratt){
		if (tratt != null && tratt.getFl_soggetto_conguaglio() != null && tratt.getFl_soggetto_conguaglio()){
			return true;
		}
		return false;
	}
	private Boolean isCompensoSoloInailEnte(Tipo_trattamentoBulk tratt){
		if (tratt != null && tratt.getFl_solo_inail_ente() != null && tratt.getFl_solo_inail_ente()){
			return true;
		}
		return false;
	}
	/**
	 * Viene caricato da db il TERZO associato al compenso valido in Data
	 * Registrazione e con tipi rapporto validi in Data Competenza Coge
	 * 
	 **/
	private V_terzo_per_compensoBulk loadVTerzo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(
					userContext, V_terzo_per_compensoBulk.class,
					"DISTINCT_TERZO");
			return home.loadVTerzo(userContext, compenso.getTi_anagrafico(),
					compenso.getCd_terzo(), compenso.getDt_registrazione(),
					compenso.getDt_da_competenza_coge());

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Esegue una operazione di modifica di un OggettoBulk.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata Pre: E' stata richiesta la modifica di una
	 * istanza di CompensoBulk che supera la validazione Post: Consente la
	 * modifica del compenso cancellando il compenso clone precedentemente
	 * creato (se necessario) e aggiornando l'importo dell'obbligazione
	 * associata - aggiorna l'obbligazione associata - aggiorna il compenso -
	 * cancellazione compenso clone (se necessario) Ritorna il compenso
	 * aggiornato
	 * 
	 * Nome: Validazione NON superata Pre: E' stata richiesta la modifica di una
	 * istanza di CompensoBulk che NON supera la validazione Post: Viene
	 * generata una eccezione con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            OggettoBulk il compenso che deve essere modificato
	 * @return l'OggettoBulk risultante dopo l'operazione di modifica.
	 * 
	 *         Metodo di validzione del compenso: validaCompenso(CompensoBulk
	 *         compenso)
	 * 
	 **/
	public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		return modificaConBulk(userContext, bulk, null);
	}

	/**
	 * Esegue una operazione di modifica di un OggettoBulk.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Validazione superata Pre: E' stata richiesta la modifica di una
	 * istanza di CompensoBulk che supera la validazione Post: Consente la
	 * modifica del compenso cancellando il compenso clone precedentemente
	 * creato (se necessario) e aggiornando l'importo dell'obbligazione
	 * associata - aggiorna l'obbligazione associata - aggiorna il compenso -
	 * cancellazione compenso clone (se necessario) Ritorna il compenso
	 * aggiornato
	 * 
	 * Nome: Validazione NON superata Pre: E' stata richiesta la modifica di una
	 * istanza di CompensoBulk che NON supera la validazione Post: Viene
	 * generata una eccezione con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            OggettoBulk il compenso che deve essere modificato
	 * @return l'OggettoBulk risultante dopo l'operazione di modifica.
	 * 
	 *         Metodo di validzione del compenso: validaCompenso(CompensoBulk
	 *         compenso)
	 * 
	 **/
	public OggettoBulk modificaConBulk(UserContext userContext,
			OggettoBulk bulk,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {

		CompensoBulk compenso = (CompensoBulk) bulk;

		Long tmp = compenso.getPgCompensoPerClone();
		Long current = compenso.getPg_compenso();
		if (compenso.isDaBonus()) {
			if ((compenso.getBonus() != null)
					&& compenso.getBonus().getIm_bonus().compareTo(
							compenso.getIm_netto_percipiente()) != 0)
				throw new ApplicationException(
						"Attenzione l'importo del bonus previsto è di "
								+ compenso.getBonus().getIm_bonus());
		}
		validaCompenso(userContext, compenso);

		// Salvo temporaneamente l'hash map dei saldi
		PrimaryKeyHashMap aTempDiffSaldi = new PrimaryKeyHashMap();
		if (compenso.getDefferredSaldi() != null)
			aTempDiffSaldi = (PrimaryKeyHashMap) compenso.getDefferredSaldi()
					.clone();

		aggiornaObbligazione(userContext, compenso, status);

		compenso.resetStatoCogeCoan();
		compenso = (CompensoBulk) super.modificaConBulk(userContext, compenso);

		aggiornaMontanti(userContext, compenso);

		if (tmp != null) {
			eliminaCompensoTemporaneo(userContext, compenso, tmp);
			compenso.setPg_compenso(current);
		}

		// Restore dell'hash map dei saldi
		if (compenso.getDefferredSaldi() != null)
			compenso.getDefferredSaldi().putAll(aTempDiffSaldi);

		// Aggiornamenti degli stati COGE e COAN
		boolean aggiornaStatoCoge = false;
		try {
			CompensoBulk compensoDB = (CompensoBulk) getTempHome(userContext,
					CompensoBulk.class).findByPrimaryKey(compenso);
			if (!Utility.equalsNull(compenso.getIm_totale_compenso(),
					compensoDB.getIm_totale_compenso())
					|| !Utility.equalsNull(compenso
							.getStato_pagamento_fondo_eco(), compensoDB
							.getStato_pagamento_fondo_eco())
					|| !Utility.equalsNull(compenso.getCd_terzo(), compensoDB
							.getCd_terzo())
					|| !Utility.equalsNull(compenso.getDt_da_competenza_coge(),
							compensoDB.getDt_da_competenza_coge())
					|| !Utility.equalsNull(compenso.getDt_a_competenza_coge(),
							compensoDB.getDt_a_competenza_coge())
					|| !Utility.equalsNull(compenso.getTi_istituz_commerc(),
							compensoDB.getTi_istituz_commerc())
					|| !Utility.equalsNull(compenso.getIm_lordo_percipiente(),
							compensoDB.getIm_lordo_percipiente().setScale(2)))
				aggiornaStatoCoge = true;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		if (aggiornaStatoCoge) {
			if (CompensoBulk.CONTABILIZZATO_COAN.equalsIgnoreCase(compenso
					.getStato_coan())) {
				compenso.setStato_coan(CompensoBulk.DA_RICONTABILIZZARE_COAN);
				compenso.setToBeUpdated();
			}
			if (CompensoBulk.CONTABILIZZATO_COGE.equalsIgnoreCase(compenso
					.getStato_coge())) {
				compenso.setStato_coge(CompensoBulk.DA_RICONTABILIZZARE_COGE);
				compenso.setToBeUpdated();
			}
		}
		if (!verificaStatoEsercizio(
				userContext,
				new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
						compenso.getCd_cds(),
						((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
								.getEsercizio())))
			throw new it.cnr.jada.comp.ApplicationException(
					"Impossibile salvare un documento per un esercizio non aperto!");

		validaIncarico(userContext, compenso.getObbligazioneScadenzario(),
				compenso);

		validaContratto(userContext, compenso);

		controlliCig(compenso);

		aggiornaModalitaPagamentoMandatoAssociato(userContext, compenso);

		return compenso;

	}

	private void aggiornaModalitaPagamentoMandatoAssociato(UserContext userContext, CompensoBulk compenso) throws ComponentException {
		try {
			BulkHome home = getHome(userContext, Mandato_rigaIBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, compenso.getCd_cds());
			sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, compenso.getCd_unita_organizzativa());
			sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, compenso.getEsercizio());
			sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_COMPENSO);
			sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, compenso.getPg_compenso());
			List<Mandato_rigaIBulk> result = home.fetchAll(sql);
			getHomeCache(userContext).fetchAll(userContext);
			result
					.stream()
					.filter(mandato_rigaIBulk -> {
						return !mandato_rigaIBulk.getModalita_pagamento().getRif_modalita_pagamento().equalsByPrimaryKey(compenso.getModalitaPagamento()) ||
								!mandato_rigaIBulk.getBanca().equalsByPrimaryKey(compenso.getBanca());
					})
					.forEach(mandato_rigaIBulk -> {
						mandato_rigaIBulk.getModalita_pagamento().setRif_modalita_pagamento(compenso.getModalitaPagamento());
						mandato_rigaIBulk.setBanca(compenso.getBanca());
						mandato_rigaIBulk.setToBeUpdated();
						try {
							home.update(mandato_rigaIBulk, userContext);
						} catch (PersistencyException e) {
							throw new DetailedRuntimeException(e);
						}
					});
		} catch (PersistencyException _ex) {
			throw handleException(_ex);
		}
	}

	public void controlliCig(CompensoBulk compenso) throws ApplicationException {
		if (compenso.getTipoTrattamento().isTipoDebitoSiopeCommerciale() &&
		        (!Optional.ofNullable(compenso.getCig()).isPresent() && !Optional.ofNullable(compenso.getMotivo_assenza_cig()).isPresent())) {
		    throw new ApplicationException("Inserire il CIG o il motivo di assenza dello stesso!");
		}
		if (Optional.ofNullable(compenso.getCig()).isPresent() && Optional.ofNullable(compenso.getMotivo_assenza_cig()).isPresent()) {
		    throw new ApplicationException("Inserire solo uno tra il CIG e il motivo di assenza dello stesso!");
		}
	}

	/**
	 * Viene modificato il tipo trattamento del compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Modifica del Tipo Trattamento Pre: Viene modificato il Tipo
	 * Trattamento del compenso Post: Vengono ricalcolati i dati relativi alla
	 * Regione Irap, Voce Iva, Tipologia Rischio
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            il compenso da aggiornare
	 * @return il compenso aggiornato
	 * 
	 * 
	 *         Metodo richiamato: setDatiLiquidazione(userContext, compenso)
	 * 
	 **/
	public CompensoBulk onTipoTrattamentoChange(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		if (isCompensoSoggettoConguaglio(compenso.getTipoTrattamento())){
			controlloRiduzioneCuneo32020(userContext, compenso);
		}
		compenso.resetDatiLiquidazione();
		setDatiLiquidazione(userContext, compenso);

		compenso.setCodici_rapporti_inps(null);
		compenso.setVisualizzaCodici_rapporti_inps(false);
		compenso.setCodici_attivita_inps(null);
		compenso.setVisualizzaCodici_attivita_inps(false);
		compenso.setCodici_altra_forma_ass_inps(null);
		compenso.setVisualizzaCodici_altra_forma_ass_inps(false);
		compenso.setComune_inps(null);
		try {
			setDatiInps(userContext, compenso);
		} catch (SQLException e) {
			throw new ComponentException(e);
		}
		return compenso;
	}

	public CompensoBulk onTipoRapportoInpsChange(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		try {
			Codici_rapporti_inpsHome home = (Codici_rapporti_inpsHome) getHome(
					userContext, Codici_rapporti_inpsBulk.class);
			if (home.findCodiceRapportoConAttivitaObbl(compenso
					.getCd_rapporto_inps()))
				compenso.setVisualizzaCodici_attivita_inps(true);
			else {
				compenso.setCodici_attivita_inps(null);
				compenso.setVisualizzaCodici_attivita_inps(false);
			}
			return compenso;
		} catch (SQLException e) {
			throw new ComponentException(e);
		}

	}

	/**
	 * Ritorna il capitolo presente in CONFIGURAZIONE_CNR entrando con:
	 * ESERCIZIO --> esercizio del compenso CHIAVE_PRIMARIA -->
	 * ELEMENTO_VOCE_SPECIALE CHIAVE_SECONDARIA --> RECUPERA_CREDITI_DA_TERZI
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @return Il capitolo recuperato dalla configurazione CNR
	 * 
	 **/
	private String recuperaCapitolo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
			return sess.getVal01(userContext, compenso.getEsercizio(), null,
					"ELEMENTO_VOCE_SPECIALE", "RECUPERO_CREDITI_DA_TERZI");

		} catch (javax.ejb.EJBException ex) {
			throw handleException(ex);
		} catch (RemoteException ex) {
			throw handleException(ex);
		}

	}

	/**
	 * Viene Richiesto il caricamento di un compenso
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Caricamento compenso Pre: Viene richiesto il caricamento da db del
	 * compenso Post: Viene caricato da database il compenso insieme a tutti gli
	 * oggetti complessi necessari ad una sua corretta gestione - terzo - tipo
	 * trattamento - regione Irap, voce iva, tipologia rischio -
	 * contributi/ritenuta - dettagli di ogni contriuto/ritenuta - minicarriera
	 * - conguaglio
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param bulk
	 *            il compenso che deve essere ri-caricato
	 * @return il compenso aggiornato
	 * 
	 *         Metodo privato chiamato: completaCompenso(UserContext
	 *         userContext, CompensoBulk compenso);
	 * 
	 **/
	public CompensoBulk reloadCompenso(UserContext userContext,
			CompensoBulk bulk) throws ComponentException {

		try {
			Long pgTmp = bulk.getPgCompensoPerClone();
			java.util.Vector docsCanc = bulk.getDocumentiContabiliCancellati();
			it.cnr.jada.bulk.PrimaryKeyHashMap saldi = bulk.getDefferredSaldi();
			Boolean aperturaDaMinicarriera = bulk.getAperturaDaMinicarriera();

			MinicarrieraBulk minicarriera = null;
			if (bulk.isDaMinicarriera())
				minicarriera = bulk.getMinicarriera();

			CompensoHome home = (CompensoHome) getHome(userContext, bulk);
			CompensoBulk compenso = (CompensoBulk) home.findByPrimaryKey(bulk);
			getHomeCache(userContext).fetchAll(userContext);

			if ((compenso !=null && compenso.getIm_totale_compenso()!=null && 
				 bulk.getFatturaPassiva() != null && bulk.getFatturaPassiva().getIm_totale_fattura()!=null &&
				 compenso.getIm_totale_compenso().compareTo(bulk.getFatturaPassiva().getIm_totale_fattura())!=0 && 
				 (!compenso.getFl_split_payment().booleanValue() ||(compenso.getFl_split_payment().booleanValue() &&  
						   compenso.getTi_istituz_commerc().compareTo(Tipo_sezionaleBulk.ISTITUZIONALE)==0 )) 
				 && !compenso.isSenzaCalcoli()))
				throw new it.cnr.jada.comp.ApplicationException("Importo totale del compenso calcolato: " + compenso.getIm_totale_compenso() + " diverso da quello della fattura: "+ bulk.getFatturaPassiva().getIm_totale_fattura());
			
			if (compenso !=null && compenso.getIm_totale_compenso()!=null && bulk.getFatturaPassiva() != null  &&
					   compenso.getFl_split_payment().booleanValue() &&  
					   compenso.getTi_istituz_commerc().compareTo(Tipo_sezionaleBulk.COMMERCIALE)==0  &&
					   bulk.getFatturaPassiva().getIm_totale_imponibile()!=null &&
					   compenso.getIm_totale_compenso().compareTo(bulk.getFatturaPassiva().getIm_totale_imponibile())!=0 && !compenso.isSenzaCalcoli())
						throw new it.cnr.jada.comp.ApplicationException("Importo totale del compenso calcolato: " + compenso.getIm_totale_compenso() + " diverso da quello della fattura: "+ bulk.getFatturaPassiva().getIm_totale_imponibile());
			compenso.setPgCompensoPerClone(pgTmp);
			
			compenso = valorizzaInfoDocEle(userContext,compenso);

			completaCompenso(userContext, compenso);

			compenso.setAperturaDaMinicarriera(aperturaDaMinicarriera);
			compenso.setMinicarriera(minicarriera);
			compenso.setDocumentiContabiliCancellati(docsCanc);
			compenso.setDefferredSaldi(saldi);

			// Per RIPORTO documenti
			compenso.setAnnoSolare(bulk.getAnnoSolare());
			compenso.setEsercizioScrivania(bulk.getEsercizioScrivania());
			
			// 
			compenso.setVoceIvaFattura(bulk.getVoceIvaFattura());
			compenso.setFatturaPassiva(bulk.getFatturaPassiva());
			
			return compenso;

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Ri-sincronizzazione della scadenza dopo un'operazione di modifica
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Ri-sincronizzazione scadenza Pre: Viene richiesta una rilettura da
	 * database della scadenza Post: Viene restituita la scadenza sincronizzata
	 * 
	 * @param userContext
	 *            Lo UserContext che ha generato la richiesta
	 * @param scadenza
	 *            La scadenza da sincronizzare
	 * @return La scadenza sincronizzata
	 */
	public Obbligazione_scadenzarioBulk resyncScadenza(UserContext userContext,
			Obbligazione_scadenzarioBulk scadenza) throws ComponentException {

		try {

			Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(
					userContext, scadenza);
			Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) home
					.findByPrimaryKey(scadenza, true);
			getHomeCache(userContext).fetchAll(userContext);
			return scad;

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * riportaAvanti method comment.
	 */
	public IDocumentoAmministrativoBulk riportaAvanti(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {
		try {
			modificaConBulk(userContext, (CompensoBulk) docAmm, status);

			RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
							RiportoDocAmmComponentSession.class);
			return session.riportaAvanti(userContext, docAmm, status);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(e);
		}
	}

	/**
	 * Annulla le modifiche apportate al compenso e ritorna al savepoint
	 * impostato in precedenza
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Rollback to savePoint Pre: Una richiesta di annullare tutte le
	 * modifiche apportate e di ritornare al savepoint e' stata generata Post:
	 * Tutte le modifiche effettuate sul compenso vengono annullate, mentre
	 * rimangono valide le modifiche apportate al doc. amministrativo che ha
	 * aperto il compenso
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 */
	public void rollbackToSavePoint(UserContext userContext,
			String savePointName) throws ComponentException {

		try {
			rollbackToSavepoint(userContext, savePointName);
		} catch (java.sql.SQLException e) {
			if (e.getErrorCode() != 1086)
				throw handleException(e);
		}
	}

	/**
	 * Questo metodo risolve il problema di "Integrità Refrenziale Violata" fra
	 * il compenso e l'obbligazione.
	 * 
	 * Spiegazione del problema: Il compenso viene salvato temporaneamente sul
	 * db durante la sua elaborazione. Puè capitare che dopo aver collegato
	 * un'obbligazione al compenso, decida di cambiare qualche dato e rieseguire
	 * il calcolo. Quest'operazione comporta il salvataggio (temporaneo) del
	 * compenso (compresi i dati dell'obbligazione collegata). Implicazioni ==>
	 * in fase di salvataggio definitivo del compenso, quando viene resa
	 * definitiva l'obbligazione, il sistema fornisce un'eccezione di Integrità
	 * Refrenziale Violata poichè si tenta di cancellare l'obbligazione
	 * temporanea precedentemente referenziata dal compenso
	 * 
	 **/
	private void scollegaObbligazioneDaCompenso(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso.getPg_obbligazione() != null
				&& compenso.getPg_obbligazione().intValue() <= 0) {
			String schema = it.cnr.jada.util.ejb.EJBCommonServices
					.getDefaultSchema();
			String sql = "UPDATE " + schema + "COMPENSO " + "SET "
					+ "CD_CDS_OBBLIGAZIONE = ?, "
					+ "ESERCIZIO_OBBLIGAZIONE = ?, "
					+ "ESERCIZIO_ORI_OBBLIGAZIONE = ?, "
					+ "PG_OBBLIGAZIONE = ?, "
					+ "PG_OBBLIGAZIONE_SCADENZARIO = ? " + "WHERE "
					+ "CD_CDS = ? AND " + "CD_UNITA_ORGANIZZATIVA = ? AND "
					+ "ESERCIZIO = ? AND " + "PG_COMPENSO = ?";

			try {
				LoggableStatement ps = new LoggableStatement(
						getConnection(userContext), sql, true, this.getClass());
				try {
					ps.setNull(1, java.sql.Types.VARCHAR);
					ps.setNull(2, java.sql.Types.NUMERIC);
					ps.setNull(3, java.sql.Types.NUMERIC);
					ps.setNull(4, java.sql.Types.NUMERIC);
					ps.setNull(5, java.sql.Types.NUMERIC);
					ps.setString(6, compenso.getCd_cds());
					ps.setString(7, compenso.getCd_unita_organizzativa());
					ps.setInt(8, compenso.getEsercizio().intValue());
					ps.setInt(9, compenso.getPg_compenso().intValue());
					ps.execute();
				} finally {
					try {
						ps.close();
					} catch (java.sql.SQLException e) {
					}
					;
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}
		}
	}

	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul
	 * Compenso
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Richiesta di ricerca di un Compenso Pre: E' stata generata la
	 * richiesta di ricerca di un Compenso Post: Viene restituito il SQLBuilder
	 * con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
	 * clausole che il Compenso abbia CDS di origine uguale a quella di
	 * scrivania, Unita Organizzativa uguale a quella di scrivania
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param clauses
	 *            clausole di ricerca gia' specificate dall'utente
	 * @param bulk
	 *            istanza di CompensoBulk che deve essere utilizzata per la
	 *            ricerca
	 * @return il SQLBuilder con le clausole aggiuntive
	 * 
	 **/
	public Query select(UserContext userContext, CompoundFindClause clauses,
			OggettoBulk bulk) throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {

		CompensoBulk compenso = (CompensoBulk) bulk;
		SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
		sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, compenso.getCd_cds());
		sql.addSQLClause("AND", "COMPENSO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
				compenso.getCd_unita_organizzativa());

		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", SQLBuilder.EQUALS,
				compenso.getV_terzo().getCd_terzo_precedente());

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sull'Anagrafico per una operazione di elaborazione CUD.
	 * 
	 * Nome: Richiesta di ricerca di una Anagrafica Pre: E' stata generata la
	 * richiesta di ricerca di una Anagrafica per una operazione di elaborazione
	 * CUD. Post: Viene restituito l'SQLBuilder con le clausole richieste
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectAnagraficoByClause(UserContext userContext,
			EstrazioneCUDVBulk cud, AnagraficoBulk anagrafico,
			CompoundFindClause clauses) throws ComponentException {

		AnagraficoHome home = (AnagraficoHome) getHome(userContext, anagrafico);
		SQLBuilder sql = home.createSQLBuilder();

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sull'Anagrafico per una operazione di Stampa per il Partitario
	 * Compensi.
	 * 
	 * Nome: Richiesta di ricerca di una Anagrafica Pre: E' stata generata la
	 * richiesta di ricerca di una Anagrafica per una operazione di Stampa per
	 * il Partitario Compensi. Post: Viene restituito l'SQLBuilder con le
	 * clausole richieste
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectAnagraficoForPrintByClause(UserContext userContext,
			StampaCertificazioneVBulk stampa, AnagraficoBulk anagrafico,
			CompoundFindClause clauses) throws ComponentException {

		AnagraficoHome home = (AnagraficoHome) getHome(userContext, anagrafico);
		SQLBuilder sql = home.createSQLBuilder();

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sull'Anagrafico per una operazione di Stampa per il Partitario
	 * Compensi.
	 * 
	 * Nome: Richiesta di ricerca di una Anagrafica Pre: E' stata generata la
	 * richiesta di ricerca di una Anagrafica per una operazione di Stampa per
	 * il Partitario Compensi. Post: Viene restituito l'SQLBuilder con le
	 * clausole richieste
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectAnagraficoForPrintByClause(UserContext userContext,
			StampaPartitarioCompensiVBulk stampa, AnagraficoBulk anagrafico,
			CompoundFindClause clauses) throws ComponentException {

		AnagraficoHome home = (AnagraficoHome) getHome(userContext, anagrafico);
		SQLBuilder sql = home.createSQLBuilder();

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sulle Banche
	 * 
	 * Nome: Richiesta di ricerca di una banca Pre: E' stata generata la
	 * richiesta di ricerca delle banche Post: Viene restituito l'SQLBuilder per
	 * filtrare le banche NON CANCELLATE, associate al TERZO selezionato e al
	 * TIPO PAGAMENTO selezionato
	 * 
	 * @param userContext
	 *            Lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param banca
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectBancaByClause(UserContext userContext,
			CompensoBulk compenso, BancaBulk banca, CompoundFindClause clauses)
			throws ComponentException {

		BancaHome bancaHome = (BancaHome) getHome(userContext,
				it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
		return bancaHome.selectBancaFor(compenso.getModalitaPagamento(),
				compenso.getCd_terzo());
	}

	/**
	 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sulle Linee di Attivita
	 * 
	 * Nome: Richiesta di ricerca di una Linea di Attivita Pre: E' stata
	 * generata la richiesta di ricerca della Linea di Attivita Post: Viene
	 * restituito l'SQLBuilder per filtrare le Linee di Attivita
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param lineaAttivita
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectLineaAttivitaByClause(UserContext userContext,
			CompensoBulk compenso,
			it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttivita,
			CompoundFindClause clauses) throws ComponentException {

		try {

			String capitolo = recuperaCapitolo(userContext, compenso);
			if (capitolo == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"Non e' definito in tabella CONFIGURAZIONE_CNR il capitolo");

			CompensoHome home = (CompensoHome) getHome(userContext, compenso);
			return home.selectLineeAttivita(compenso, capitolo, clauses);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Costruisce l'istruzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sul Terzo, (V_ANAGRAFICO_TERZO) per una operazione di Stampa per
	 * il Riepilogo Compensi.
	 * 
	 * Nome: Richiesta di ricerca di un Terzo Pre: E' stata generata la
	 * richiesta di ricerca di un Terzo per una operazione di Stampa dei
	 * Riepiloghi Compensi. Post: Viene restituito l'SQLBuilder con le clausole
	 * richieste
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectTerzoForPrintByClause(UserContext userContext,
			StampaRiepilogoCompensiVBulk stampa, TerzoBulk terzo,
			CompoundFindClause clauses) throws ComponentException {

		TerzoHome home = (TerzoHome) getHome(userContext, terzo);
		SQLBuilder sql = home.createSQLBuilder();

		sql.setAutoJoins(true);

		sql.addClause(clauses);
		return sql;
	}

	public SQLBuilder selectTerzoForPrintByClause(UserContext userContext,
			StampaCompensiBulk stampa, TerzoBulk terzo,
			CompoundFindClause clauses) throws ComponentException {

		TerzoHome home = (TerzoHome) getHome(userContext, terzo);
		SQLBuilder sql = home.createSQLBuilder();

		sql.setDistinctClause(true);
		sql.addTableToHeader("COMPENSO");
		sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "COMPENSO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
				stampa.getCdUoForPrint());
		sql.addSQLClause("AND", "COMPENSO.ESERCIZIO", sql.EQUALS, stampa
				.getEsercizio());

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sulla Tipologia Rischio
	 * 
	 * Nome: Richiesta di ricerca di una Tipologia Rischio Pre: E' stata
	 * generata la richiesta di ricerca delle Tipologie di Rischio Post: Viene
	 * restituito l'SQLBuilder per filtrare le Tipologie di Rischio valide in
	 * data registrazione compenso
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectTipologiaRischioByClause(UserContext userContext,
			CompensoBulk compenso, Tipologia_rischioBulk tipologia,
			CompoundFindClause clauses) throws ComponentException {

		try {

			Tipologia_rischioHome home = (Tipologia_rischioHome) getHome(
					userContext, tipologia);
			return home.selectTipologiaRischio(tipologia
					.getCd_tipologia_rischio(), compenso.getDt_registrazione(),
					clauses);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sul V_terzo
	 * 
	 * Nome: Validazione superata Pre: E' stata generata la richiesta di ricerca
	 * di un V_terzo_per_compensoBulk a fronte di un corretto inserimento delle
	 * date di competenza Post: Viene restituito l'SQLBuilder per filtrare i
	 * terzi con TIPO ANAGRAFICO compatibile con quello selezionato, validi alla
	 * Data Registrazione e con tipi rapporto validi in Data Competenza Coge
	 * 
	 * Nome: Validazione NON superata Pre: E' stata generata la richiesta di
	 * ricerca di un V_terzo_per_compensoBulk a fronte di un errato inserimento
	 * delle date di competenza Post: Viene restituita una ValidationExecption
	 * con la desrizione dell'Errore relativo
	 * 
	 * @param userContext
	 *            Lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param vTerzo
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectV_terzoByClause(UserContext userContext,
			CompensoBulk compenso, V_terzo_per_compensoBulk vTerzo,
			CompoundFindClause clauses) throws ComponentException {
		try {
			compenso.validaDateCompetenzaCoge();

			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(
					userContext, V_terzo_per_compensoBulk.class,
					"DISTINCT_TERZO");
			return home.selectVTerzo(compenso.getTi_anagrafico(), compenso
					.getCd_terzo(), compenso.getDt_registrazione(), compenso
					.getDt_da_competenza_coge(), clauses);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole
	 * specificate. Aggiunge una clausola a tutte le operazioni di ricerca
	 * eseguite sulla Voce Iva
	 * 
	 * Nome: Richiesta di ricerca di una Voce Iva Pre: E' stata generata la
	 * richiesta di ricerca della Voce Iva Post: Viene restituito l'SQLBuilder
	 * per filtrare le Voci Iva di tipo Entrambi e d'Acquisto
	 * 
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param compenso
	 *            l'OggettoBulk che rappresenta il contesto della ricerca.
	 * @param tipologia
	 *            l'OggettoBulk da usare come prototipo della ricerca; sul
	 *            prototipo vengono costruite delle clausole aggiuntive che
	 *            vengono aggiunte in AND alle clausole specificate.
	 * @param clauses
	 *            L'albero logico delle clausole da applicare alla ricerca
	 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire
	 *         e tutti i parametri della query.
	 * 
	 **/
	public SQLBuilder selectVoceIvaByClause(UserContext userContext,
			CompensoBulk compenso, Voce_ivaBulk voceIva,
			CompoundFindClause clauses) throws ComponentException {

		Voce_ivaHome home = (Voce_ivaHome) getHome(userContext, voceIva);
		SQLBuilder sql = home.createSQLBuilder();
		sql
				.addClause("AND", "ti_applicazione", sql.NOT_EQUALS,
						voceIva.VENDITE);

		sql.addClause(clauses);
		return sql;
	}

	/**
	 * Viene richiesto di controllare ed eventualmente gestire i dati Regioen
	 * IRAP, Voce IVA, Tipologia Rischio legati al tipo trattamento selezionato
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Tipo trattamento IRAP Pre: Il Tipo trattamento selezionato contiene
	 * nel codice IRAP Post: Carica la Regione Irap associata al terzo di
	 * scrivania Visualizza la Regione Irap
	 * 
	 * Nome: Tipo trattamento INAIL Pre:Il Tipo trattamento selezionato contiene
	 * nel codice INAIL Post: Visualizza la Tipologia Ricshio
	 * 
	 * Nome: Tipo trattamento IVA Pre: Il Tipo trattamento selezionato contiene
	 * nel codice IVA Post: Visualizza la Voce Iva
	 * 
	 * Nome: Tipo trattamento senza IRAP, INAIL, IVA Pre: Il Tipo trattamento
	 * selezionato non contiene IRAP, INAIL, IVA Post: Non viene visualizzato
	 * nulla. Il controllo torna al metodo chiamante
	 * 
	 * @param userContext
	 *            lo UserContext che ha generato la richiesta
	 * @param compenso
	 *            Il compenso da completare
	 * 
	 **/
	private void setDatiLiquidazione(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		try {

			V_tipo_trattamento_tipo_coriHome home = (V_tipo_trattamento_tipo_coriHome) getHome(
					userContext, V_tipo_trattamento_tipo_coriBulk.class);
			java.util.List l = home.findTipiCoriPerTipoTrattamento(compenso
					.getTipoTrattamento(), compenso.getDt_registrazione());

			for (java.util.Iterator i = l.iterator(); i.hasNext();) {
				V_tipo_trattamento_tipo_coriBulk obj = (V_tipo_trattamento_tipo_coriBulk) i
						.next();
				if (obj.getCd_cori().startsWith(compenso.CODICE_IRAP)) {
					compenso.setVisualizzaRegioneIrap(true);
					compenso.setRegioneIrap(loadRegioneIRAPDefalut(userContext,
							compenso));
				}
				if (isCompensoSoloInailEnte(compenso.getTipoTrattamento())){
					if (compenso.isDaMinicarriera()){
						throw new ApplicationException(
								"Attenzione! Non è possibile utilizzare un trattamento di tipo 'Torno Subito con solo INAIL a carico ente' per una minicarriera");
					}
					compenso.setCompensoSoloInailEnte(true);
				}
				if (obj.getCd_cori().startsWith(compenso.CODICE_INAIL))
					compenso.setVisualizzaTipologiaRischio(true);
				if (obj.getCd_cori().startsWith(compenso.CODICE_IVA))
				{
					compenso.setVisualizzaVoceIva(true);
					if (compenso.isGestione_doc_ele() && compenso.getFl_generata_fattura() && compenso.getVoceIvaFattura() != null)
						compenso.setVoceIva(compenso.getVoceIvaFattura());
				}
			}

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	private void setDatiInps(UserContext userContext, CompensoBulk compenso)
			throws ComponentException, SQLException {
		try {
			V_tipo_trattamento_tipo_coriHome home = (V_tipo_trattamento_tipo_coriHome) getHome(
					userContext, V_tipo_trattamento_tipo_coriBulk.class);
			if (home.findTipiCoriInpsPerTipoTrattamento(compenso
					.getTipoTrattamento(), compenso.getDt_registrazione()))
				compenso.setVisualizzaCodici_rapporti_inps(true);
			else {
				compenso.setCodici_rapporti_inps(null);
				compenso.setVisualizzaCodici_rapporti_inps(false);
				compenso.setComune_inps(null);
			}
			if (home.findTipiCoriAliqRidPerTipoTrattamento(compenso
					.getTipoTrattamento(), compenso.getDt_registrazione()))
				compenso.setVisualizzaCodici_altra_forma_ass_inps(true);
			else {
				compenso.setCodici_altra_forma_ass_inps(null);
				compenso.setVisualizzaCodici_altra_forma_ass_inps(false);
			}
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Imposta un savepoint che consente di salvare le modifiche apportate al
	 * doc. amministrativo fino a quel momento in modo che se gli aggiornamenti
	 * apportati al compenso non venissero confermati (rollback), comunque non
	 * verrebbero persi anche quelli del documento amministrativo.
	 * 
	 * Pre-post-conditions:
	 * 
	 * Nome: Imposta savePoint Pre: Una richiesta di impostare un savepoint e'
	 * stata generata Post: Un savepoint e' stato impostato in modo che le
	 * modifiche apportate al doc. amministrativo vengono consolidate
	 * 
	 * @param uc
	 *            lo UserContext che ha generato la richiesta
	 */
	public void setSavePoint(UserContext userContext, String savePointName)
			throws ComponentException {

		try {
			setSavepoint(userContext, savePointName);
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}

	/**
	 * stampaConBulk method comment.
	 */
	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(
			it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof StampaPartitarioCompensiVBulk)
			validateBulkForPrint(aUC, (StampaPartitarioCompensiVBulk) bulk);
		else if (bulk instanceof StampaRiepilogoCompensiVBulk)
			validateBulkForPrint(aUC, (StampaRiepilogoCompensiVBulk) bulk);
		else if (bulk instanceof StampaCertificazioneVBulk)
			validateBulkForPrint(aUC, (StampaCertificazioneVBulk) bulk);
		else if (bulk instanceof StampaCompensiBulk)
			stampaConBulk(aUC, (StampaCompensiBulk) bulk);

		return bulk;
	}

	public OggettoBulk stampaConBulk(UserContext userContext,
			StampaCompensiBulk stampa) throws ComponentException {
		if (stampa.getPgFine() == null || stampa.getPgInizio() == null)
			throw new ApplicationException(
					"E' necessario selezionare uno o piè compensi");
		return stampa;
	}

	/**
	 * Viene richiesto di aggiornare l'importo associato a documenti
	 * amministrativi dell'obbligazione associata al compenso
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Aggiornamento dell'importo associato a documenti amministrativi di
	 * una scadenza di documento contabile Pre: Richiesto l'aggiornamento
	 * dell'importo associato a documenti amministrativi di una scadenza di
	 * documento contabile Post: Il dettaglio viene aggiornato
	 **/
	public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
			it.cnr.jada.UserContext userContext,
			IScadenzaDocumentoContabileBulk scadenza)
			throws it.cnr.jada.comp.ComponentException {

		try {
			((IScadenzaDocumentoContabileHome) getHome(userContext, scadenza
					.getClass())).aggiornaImportoAssociatoADocAmm(userContext,
					scadenza);
		} catch (it.cnr.jada.persistency.PersistencyException exc) {
			throw handleException((OggettoBulk) scadenza, exc);
		} catch (it.cnr.jada.bulk.BusyResourceException exc) {
			throw handleException((OggettoBulk) scadenza, exc);
		} catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
			throw handleException((OggettoBulk) scadenza, exc);
		}

		return scadenza;
	}

	/**
	 * Viene validato il compenso prima del salvataggio - validazione data
	 * registrazione, date competenza, descrizione compenso - validazioni dati
	 * fattura (se uno è presente, allora devono esserci tutti) - validazione
	 * terzo - validazione dati liquidazione (regione Irap, voce iva, tipologia
	 * rischio) - eseguito il calcolo dei contributi/ritenute - creata
	 * obbligazione e aggiornata con l'importo totale del compenso - terzo
	 * obbligazione e terzo compenso consistenti
	 * 
	 * Viene richiesta la validazione di una Compenso prima del salvataggio
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Validazione Testata compenso NON superata Pre: Data registrazione,
	 * Date competenza o Descrizione compenso non validi Post: Viene restituita
	 * una ComponentException con la desrizione dell'errore
	 * 
	 * Nome: Validazione dati fattura NON superata Pre: Nr. fattura, esercizio
	 * fattura e data fattura contemporaneamente presenti Post: Viene restituita
	 * una ComponentException con la desrizione dell'errore
	 * 
	 * Nome: Validazione Terzo NON superata Pre: Il terzo selezionato non
	 * risulta valido Post: Viene restituita una ComponentException con la
	 * desrizione dell'errore
	 * 
	 * Nome: Validazione Regione IRAP, Voce IVA, Tipologia Rischio NON superata
	 * Pre: Non è stato selezionata una regione IRAP, voce IVA o Tipologia
	 * Rischio valida Post: Viene restituita una ComponentException con la
	 * desrizione dell'errore
	 * 
	 * Nome: Non eseguito il calcolo del compenso Pre: Il calcolo del compenso
	 * (creazione dei contributi/ritenute) non eseguito Post: Viene restituita
	 * una ComponentException con la desrizione dell'errore
	 * 
	 * Nome: Obbligazione non creata o Importo dell'obbligazione non coerente
	 * con l'importo del compenso Pre: Non è stata creata l'obbligazione o
	 * l'importo della stessa non è coerente con l'importo del compenso Post:
	 * Viene restituita una ComponentException con la desrizione dell'errore
	 * 
	 * Nome: Obbligazione non valida Pre: L'obbligazione non supera i controlli
	 * per la sua validazione Post: Viene restituita una ComponentException con
	 * la desrizione dell'errore
	 * 
	 * Nome: Tutte le validazioni precedenti superate Pre: Il compenso supera
	 * tutte le validazioni precedenti Post: Viene validato l'OggettoBulk
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            l'OggettoBulk da validare
	 * 
	 **/

	private void controlloRiduzioneCuneo32020(UserContext userContext, CompensoBulk compenso) throws ComponentException {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		Timestamp dataInizioGestioneRiduzioneCuneo = null;
		Timestamp dataFineGestioneRiduzioneCuneo = null;
		try {
			Configurazione_cnrBulk configurazione = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, Configurazione_cnrBulk.PK_RIDUZIONE_CUNEO_DL_3_2020, Configurazione_cnrBulk.SK_DATA_INIZIO);
			if (configurazione != null){
				dataInizioGestioneRiduzioneCuneo = configurazione.getDt01();
				dataFineGestioneRiduzioneCuneo = configurazione.getDt02();
			}
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		} catch (java.rmi.RemoteException ex) {
			throw handleException(ex);
		}


		if (compenso.getDt_a_competenza_coge() != null && compenso.getDt_da_competenza_coge() != null){
			if (compenso.getDt_da_competenza_coge().compareTo(dataFineGestioneRiduzioneCuneo) < 0 ||
					compenso.getDt_a_competenza_coge().compareTo(dataFineGestioneRiduzioneCuneo) < 0){
				if (compenso.getDt_da_competenza_coge().compareTo(dataInizioGestioneRiduzioneCuneo) < 0 &&
						compenso.getDt_a_competenza_coge().compareTo(dataInizioGestioneRiduzioneCuneo) >= 0){
					throw new ApplicationException("Operazione non consentita. Le date di competenza devono essere entrambe precedenti o uguali/successive alla data di inizio della riduzione del cuneo fiscale DL 3/2020 del "+sdf.format(dataInizioGestioneRiduzioneCuneo));
				}
				if (compenso.getDt_da_competenza_coge().compareTo(dataFineGestioneRiduzioneCuneo) <= 0 &&
						compenso.getDt_a_competenza_coge().compareTo(dataFineGestioneRiduzioneCuneo) > 0){
					throw new ApplicationException("Operazione non consentita. Le date di competenza devono essere entrambe precedenti o uguali/successive alla data di fine della riduzione del cuneo fiscale DL 3/2020 del "+sdf.format(dataFineGestioneRiduzioneCuneo));
				}
			}
		}
	}



	private void validaCompenso(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {
		// Controllo Testata Compenso
		if (isCompensoSoloInailEnte(compenso.getTipoTrattamento())){
			if (compenso.isDaMinicarriera()){
				throw new ApplicationException(
						"Attenzione! Non è possibile utilizzare un trattamento di tipo 'Torno Subito con solo INAIL a carico ente' per una minicarriera");
			}
		}
		if (isCompensoSoggettoConguaglio(compenso.getTipoTrattamento()) && !compenso.isDaConguaglio() ){
			controlloRiduzioneCuneo32020(userContext, compenso);
		}
		try {
	
			compenso.setDataInizioObbligoRegistroUnico(Utility.createConfigurazioneCnrComponentSession().
				getDt01(userContext, new Integer(0), null,"REGISTRO_UNICO_FATPAS", "DATA_INIZIO"));
			compenso.validaTestata();
		} catch (RemoteException e) {
			throw handleException(e);
		}catch (it.cnr.jada.action.BusinessProcessException e) {
			throw handleException(e);
		} catch (java.text.ParseException ex) {
			throw handleException(ex);
		} catch (javax.ejb.EJBException ex) {
			throw handleException(ex);
		}

		// Controllo Terzo per Compenso
		validaTerzo(userContext, compenso);

		// Controllo Dati Fattura Inseriti
		compenso.validaDatiFattura();
		if (compenso.getFl_liquidazione_differita()
				&& compenso.getDt_fattura_fornitore() != null) {
			java.sql.Timestamp data_limite;
			java.sql.Timestamp data_limite_sup;
			try {
				data_limite = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
						.createEJB(
								"CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
								it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class))
						.getDt01(userContext, new Integer(0), "*", "COSTANTI",
								"LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
				data_limite_sup = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
						.createEJB(
								"CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
								it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class))
						.getDt02(userContext, new Integer(0), "*", "COSTANTI",
								"LIMITE_CREAZIONE_FATT_PASS_ES_DIF");				
			} catch (RemoteException e) {
				throw handleException(e);
			}
			if ((compenso.getDt_fattura_fornitore().compareTo(data_limite) < 0)||(data_limite_sup!=null && compenso.getDt_fattura_fornitore().compareTo(data_limite_sup) > 0)) {
				compenso.setFl_liquidazione_differita(false);
				throw new it.cnr.jada.comp.ApplicationException(
						"Non è possibile indicare la liquidazione differita con la data fattura fornitore indicata.");
			}
		}
		// Controlli aggiunti per l'Art.35 DL n.223/2006
		if (compenso.getTipoTrattamento().getFl_utilizzabile_art35()
				&& !isImportoOk(userContext, compenso)
				&& !compenso.LIBERO_FONDO_ECO.equals(compenso
						.getStato_pagamento_fondo_eco()))
			throw new it.cnr.jada.comp.ApplicationException(
					"Il compenso non può essere associato a fondo economale per il tipo di trattamento prescelto");
		if (compenso.getFl_generata_fattura()
				&& compenso.getPartita_iva() == null
				&& !((TerzoBulk) compenso.getTerzo()).getAnagrafico()
						.getFl_non_obblig_p_iva())
			throw new it.cnr.jada.comp.ApplicationException(
					"Per poter generare la fattura è necessario che il terzo abbia la Partita Iva");
		if (compenso.getFl_generata_fattura()
				&& compenso.getCodice_fiscale() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Per poter generare la fattura è necessario che il terzo abbia il Codice Fiscale valorizzato");

		validaDatiLiquidazione(compenso);

		if (compenso.isStatoCompensoEseguiCalcolo())
			throw new it.cnr.jada.comp.ApplicationException(
					"E' necessario eseguire il calcolo prima di continuare");
		// controllo omesso sull'esegui calcolo per  !compenso.isSenzaCalcoli() da fare al riporta 
			if ((compenso !=null && compenso.getIm_totale_compenso()!=null && 
				compenso.getFatturaPassiva() != null && compenso.getFatturaPassiva().getIm_totale_fattura()!=null &&
				 compenso.getIm_totale_compenso().compareTo(compenso.getFatturaPassiva().getIm_totale_fattura())!=0 && 
				 (!compenso.getFl_split_payment().booleanValue() ||(compenso.getFl_split_payment().booleanValue() &&  
						   compenso.getTi_istituz_commerc().compareTo(Tipo_sezionaleBulk.ISTITUZIONALE)==0 ))))
				throw new it.cnr.jada.comp.ApplicationException("Importo totale del compenso calcolato: " + compenso.getIm_totale_compenso() + " diverso da quello della fattura: "+ compenso.getFatturaPassiva().getIm_totale_fattura());
			
			if (compenso !=null && compenso.getIm_totale_compenso()!=null && compenso.getFatturaPassiva() != null  &&
					   compenso.getFl_split_payment().booleanValue() &&  
					   compenso.getTi_istituz_commerc().compareTo(Tipo_sezionaleBulk.COMMERCIALE)==0  &&
							   compenso.getFatturaPassiva().getIm_totale_imponibile()!=null &&
					   compenso.getIm_totale_compenso().compareTo(compenso.getFatturaPassiva().getIm_totale_imponibile())!=0)
						throw new it.cnr.jada.comp.ApplicationException("Importo totale del compenso calcolato: " + compenso.getIm_totale_compenso() + " diverso da quello della fattura: "+ compenso.getFatturaPassiva().getIm_totale_imponibile());
			
		validaObbligazione(userContext, compenso.getObbligazioneScadenzario(),
				compenso);

		validaCompensoPerContabilizzazione(userContext, compenso);
		if (compenso.getObbligazioneScadenzario() == null){
			compenso.setPg_trovato(null);
		} else {
			Elemento_voceBulk voce = compenso.getObbligazioneScadenzario().getObbligazione().getElemento_voce();
		    if (compenso.getPg_trovato()!=null && compenso.getObbligazioneScadenzario()!=null && isInibitaIndicazioneTrovato(voce))
		    	compenso.setPg_trovato(null);
		    if (isObbligatoriaIndicazioneTrovato(voce) && compenso.getPg_trovato()==null )
		        throw new it.cnr.jada.comp.ApplicationException(
		            "Attenzione! Non è stato inserito il Brevetto/Trovato mentre la voce di bilancio utilizzata per la contabilizzazione del dettaglio collegato ne prevede l'indicazione obbligatoria");
		}
	}

	private boolean isObbligatoriaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
		if (voce == null)
			return false;
		return voce.isObbligatoriaIndicazioneTrovato();
	}

	private boolean isInibitaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
		if (voce == null)
			return false;
		return voce.isInibitaIndicazioneTrovato();
	}

	/**
	 * Viene richiesta la validazione degli importi di un compenso CON calcoli
	 * (ossia il flag senza calcoli = FALSE)
	 * 
	 * - Importo lordo > 0 - Quote esenti = Quota esente + Importo no fiscale
	 * Quote esenti <= Importo Lordo
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Importo lordo negativo o uguale a zero Pre: E' stato inserito un
	 * importo lordo negativo o uguale a zero Post: Viene restituita una
	 * ComponentException con la desrizione dell'errore
	 * "L'importo lordo deve essere maggiore di zero"
	 * 
	 * Nome: Validazione Quote esenti NON superata Pre: L'importo delle quote
	 * esenti supera l'importo dell'importo lordo Post: Viene restituita una
	 * ComponentException con la desrizione dell'errore
	 * "L'importo lordo deve essere maggiore delle quote esenti"
	 * 
	 * Nome: Tutte le validazioni precedenti superate Pre: Il compenso supera
	 * tutte le validazioni precedenti Post: Viene validato l'OggettoBulk
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            l'OggettoBulk da validare
	 * 
	 **/
	private void validaCompensoConCalcoli(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (!isCompensoSoloInailEnte(compenso.getTipoTrattamento())){
			if (compenso.getIm_lordo_percipiente().compareTo(
					new java.math.BigDecimal(0)) <= 0)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'importo lordo deve essere maggiore di zero");

			java.math.BigDecimal tmp = compenso.getQuota_esente().add(
					compenso.getIm_no_fiscale());
			if (compenso.getIm_lordo_percipiente().compareTo(tmp) < 0)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'importo lordo deve essere maggiore delle quote esenti IRPEF e CO/RI");
		}
	}

	/**
	 * Validazione Compenso prima della Contabilizzazione
	 * 
	 * Pre-post-conditions
	 * 
	 * Name: Importo totale compenso negativo e Linea Attivitè non selezionata
	 * Pre: Viene richiesta la contabilizzazione di un compenso negativo senza
	 * linea di attivita Post: Viene impedita la contabilizzazione ed emesso il
	 * messaggio "Inserire la Linea di Attivitè!"
	 * 
	 * Name: Importo totale compenso negativo o nullo e compenso assegnato a
	 * fondo economale Pre: Viene richiesta la contabilizzazione di un compenso
	 * senza obbligazione e assegnato a fondo economale Post: Viene impedita la
	 * contabilizzazione ed emesso il messaggio"I compensi con importo negativo o nullo non possono essere associati a Fondo Economale"
	 * 
	 * Nome: Tutte le validazioni precedenti superate Pre: Il compenso supera
	 * tutte le validazioni precedenti Post: Viene consentita la
	 * contabilizzazione del compenso
	 * 
	 **/
	private void validaCompensoPerContabilizzazione(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		if (compenso.getImportoObbligazione().compareTo(
				new java.math.BigDecimal(0)) >= 0)
			compenso.setLineaAttivita(null);

		if (compenso.getImportoObbligazione().compareTo(
				new java.math.BigDecimal(0)) < 0
				&& !compenso.hasLineaAttivita())
			throw new it.cnr.jada.comp.ApplicationException("Inserire il GAE!");

		if (compenso.getImportoObbligazione().compareTo(
				new java.math.BigDecimal(0)) <= 0
				&& !compenso.LIBERO_FONDO_ECO.equals(compenso
						.getStato_pagamento_fondo_eco()))
			throw new it.cnr.jada.comp.ApplicationException(
					"I compensi con importo negativo o nullo non possono essere associati a Fondo Economale");

		if (compenso.getImporto_netto_spesa().compareTo(
				new java.math.BigDecimal(0)) <= 0
				&& !compenso.LIBERO_FONDO_ECO.equals(compenso
						.getStato_pagamento_fondo_eco()))
			if (compenso.isDaMissione())
				throw new it.cnr.jada.comp.ApplicationException(
						"Il compenso non può essere associato a fondo economale poichè l'importo dell'anticipo è superiore al netto percipiente");
			else
				throw new it.cnr.jada.comp.ApplicationException(
						"Il compenso non può essere associato a fondo economale poichè l'importo netto percipiente è negativo");
	}

	/**
	 * 
	 * Viene richiesta la validazione degli importi di un compenso SENZA calcoli
	 * (ossia il flag senza calcoli = FALSE)
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Validazione Quote esenti (quota esente + importo no fiscale) NON
	 * superata Pre: Quote esenti diverse da zero e maggiori dell'importo lordo
	 * Post: Viene restituita una ComponentException con la desrizione
	 * dell'errore "L'importo lordo deve essere maggiore delle quote esenti"
	 * 
	 * Nome: Validazioni precedenti superate Pre: Tutte le validazioni
	 * precedenti sono state superate Post: Viene validato l'OggettoBulk
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            l'OggettoBulk da validare
	 * 
	 **/
	private void validaCompensoSenzaCalcoli(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		java.math.BigDecimal tmp = compenso.getQuota_esente().add(
				compenso.getIm_no_fiscale());
		if (tmp.compareTo(new java.math.BigDecimal(0)) != 0
				&& compenso.getIm_lordo_percipiente().compareTo(tmp) < 0)
			throw new it.cnr.jada.comp.ApplicationException(
					"L'importo lordo deve essere maggiore delle quote esenti IRPEF e CO/RI");
	}

	/**
	 * Viene richiesta la validazione di un compenso prima dell'esecuzione della
	 * procedura oracle ESEGUICALCOLO - validazione date - validazione terzo -
	 * validazione dati liquidazione (regione Irap, voce iva, tipologia rischio)
	 * - validazione importi - validazione compenso CON/SENZA calcoli
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Validazione Date compenso NON superata Pre: Data registrazione,
	 * Date competenza coge NON valide; Post: Viene restituita una
	 * ComponentException con la desrizione dell'errore
	 * 
	 * Nome: Validazione Terzo NON superata Pre: Il terzo selezionato non
	 * risulta valido Post: Viene restituita una ComponentException con la
	 * desrizione dell'errore
	 * 
	 * Nome: Validazione Regione IRAP, Voce IVA, Tipologia Rischio NON superata
	 * Pre: Non è stato selezionata una regione IRAP, voce IVA o Tipologia
	 * Rischio valida Post: Viene restituita una ComponentException con la
	 * desrizione dell'errore
	 * 
	 * Nome: Validazione Importi nel caso di Compenso SENZA calcoli Pre: Gli
	 * importi inseriti (importo lordo e quote esenti) in un compenso senza
	 * calcoli non sono validi Post: Viene restituita una ComponentException con
	 * la desrizione dell'errore
	 * 
	 * Nome: Validazione Importi nel caso di Compenso CON calcoli Pre: Gli
	 * importi inseriti (importo lordo e quote esenti) in un compenso con
	 * calcoli non sono validi Post: Viene restituita una ComponentException con
	 * la desrizione dell'errore
	 * 
	 * Nome: Tutte le validazioni precedenti superate Pre: Il compenso supera
	 * tutte le validazioni precedenti Post: Viene validato l'OggettoBulk
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            l'OggettoBulk da validare
	 * 
	 **/
	private void validaCompensoSuEseguiCalcolo(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		// Controllo testata compenso
		try {
			compenso.validaDate();
		} catch (it.cnr.jada.action.BusinessProcessException e) {
			throw handleException(e);
		} catch (java.text.ParseException ex) {
			throw handleException(ex);
		} catch (javax.ejb.EJBException ex) {
			throw handleException(ex);
		}

		// Controllo Terzo per Compenso
		validaTerzo(userContext, compenso);

		validaDatiLiquidazione(compenso);

		// Controllo su importi
		if (compenso.getIm_lordo_percipiente() == null)
			compenso.setIm_lordo_percipiente(new java.math.BigDecimal(0));
		if (compenso.getIm_no_fiscale() == null)
			compenso.setIm_no_fiscale(new java.math.BigDecimal(0));
		if (compenso.getQuota_esente() == null)
			compenso.setQuota_esente(new java.math.BigDecimal(0));
		if (compenso.getQuota_esente_no_iva() == null)
			compenso.setQuota_esente_no_iva(new java.math.BigDecimal(0));
		if (compenso.getQuota_esente_inps() == null)
			compenso.setQuota_esente_inps(new java.math.BigDecimal(0));

		// Controllo su detrazioni
		if (compenso.getDetrazioni_personali() == null)
			compenso.setDetrazioni_personali(new java.math.BigDecimal(0));
		if (compenso.getDetrazioni_la() == null)
			compenso.setDetrazioni_la(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_coniuge() == null)
			compenso.setDetrazione_coniuge(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_figli() == null)
			compenso.setDetrazione_figli(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_altri() == null)
			compenso.setDetrazione_altri(new java.math.BigDecimal(0));
		if (compenso.getDetrazioni_personali_netto() == null)
			compenso.setDetrazioni_personali_netto(new java.math.BigDecimal(0));
		if (compenso.getDetrazioni_la_netto() == null)
			compenso.setDetrazioni_la_netto(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_coniuge_netto() == null)
			compenso.setDetrazione_coniuge_netto(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_figli_netto() == null)
			compenso.setDetrazione_figli_netto(new java.math.BigDecimal(0));
		if (compenso.getDetrazione_altri_netto() == null)
			compenso.setDetrazione_altri_netto(new java.math.BigDecimal(0));
		if (compenso.getDetrazioneRiduzioneCuneo() == null)
			compenso.setDetrazioneRiduzioneCuneo(new java.math.BigDecimal(0));
		if (compenso.getDetrazioneRidCuneoNetto() == null)
			compenso.setDetrazioneRidCuneoNetto(new java.math.BigDecimal(0));

		if (compenso.isSenzaCalcoli())
			validaCompensoSenzaCalcoli(userContext, compenso);
		else
			validaCompensoConCalcoli(userContext, compenso);
	}

	/**
	 * Viene richiesta la validazione dei dati di liquidazione di un compenso -
	 * regione Irap - voce iva - tipologia rischio
	 * 
	 * Pre-post_conditions
	 * 
	 * Nome: Validazione Regione IRAP NON superata Pre: Non è stato selezionata
	 * una regione IRAP Post: Viene restituita una ComponentException con la
	 * desrizione dell'errore
	 * 
	 * Nome: Validazione Voce IVA NON superata Pre: Non è stato selezionata una
	 * voce IVA Post: Viene restituita una ComponentException con la desrizione
	 * dell'errore
	 * 
	 * Nome: Validazione Tipologia Rischio NON superata Pre: Non è stato
	 * selezionata una Tipologia Rischio Post: Viene restituita una
	 * ComponentException con la desrizione dell'errore
	 * 
	 * @param compenso
	 *            l'OggettoBulk da validare
	 * 
	 **/
	private void validaDatiLiquidazione(CompensoBulk compenso)
			throws ComponentException {

		// Controllo su Regione IRAP, Voce IVA e Tipologia Rischio
		if (compenso.isVisualizzaRegioneIrap()
				&& compenso.getCd_regione_irap() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire la Regione IRAP");

		if (compenso.isVisualizzaVoceIva() && compenso.getCd_voce_iva() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire la Voce IVA");

		if (compenso.isVisualizzaTipologiaRischio()) {
			if (compenso.getCd_tipologia_rischio() == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"Inserire la Tipologia di Rischio");
			if (compenso.getImponibile_inail() == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'imponibile INAIL non può essere vuoto");
		}
	}

	/**
	 * Viene richiesta la validazione dell'obbligazione creata - Inserita
	 * scadenza su compenso con importo negativo o nullo - Nessuna scadenza
	 * inserita - Importo obbligazione e importo scadenza NON NULLI - Importo
	 * sadenza uguale a importo compenso - Terzo dell'obbligazione uguale al
	 * terzo del compenso o di tipo entitè DIVERSI
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Importo Lordo compenso <= 0 e scadenza selezionata Pre: E' stata
	 * associata una scadenza ad un compenso con importo minore o uguale di zero
	 * Post: Non viene consentita la creazione/modifica dell'obbligazione
	 * Generata una ApplicationException con il messaggio: "E' stata collegata
	 * un'obbligazione ad un compenso con importo lordo minore o uguale a zero.
	 * Eliminare l'obbligazione!"
	 * 
	 * Nome: Importo Lordo compenso > 0 e Scadenza non selezionata Pre: Non e'
	 * stata selezionata la scadenza da associare al compenso positivo Post: Non
	 * viene consentita la creazione/modifica dell'obbligazione Generata una
	 * ApplicationException con il messaggio:
	 * "Nessuna obbligazione associata!!!"
	 * 
	 * Nome: Importi obblig./scadenza NULLI Pre: L'importo della obblig. e/o
	 * della scadenza è nullo Post: Non viene consentita la creazione/modifica
	 * dell'obbligazione Generata una ApplicationException con il messaggio:
	 * "L'Importo dell'obbligazione/scadenza è un dato obbligatorio"
	 * 
	 * Nome: Importi scadenza diverso da importo lordo compenso Pre: L'importo
	 * della scadenza è diverso dall'importo lordo del compenso Post: Non viene
	 * consentita la creazione/modifica dell'obbligazione Generata una
	 * ApplicationException con il messaggio:
	 * "L'importo dell scadenza deve corrispondere all'importo del compenso"
	 * 
	 * Nome: Terzo selezionato NON valido Pre: Il terzo selezionato è diverso
	 * dal terzo del compenso oppure il tipo entitè NON è DIVERSI Post: Non
	 * viene consentita la creazione/modifica dell'obbligazione Generata una
	 * ApplicationException con il messaggio:
	 * "L'obbligazione deve avere un creditore valido!"
	 * 
	 * Nome: Tutte le validazioni precedenti superate Pre: L'obbligazione supera
	 * tutte le validazioni precedenti Post: Viene validato l'OggettoBulk
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param scadenza
	 *            la scadenza da validare
	 * @param bulk
	 *            il compenso contenente la scadenza
	 * 
	 **/
	public void validaObbligazione(UserContext userContext,
			Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk)
			throws ComponentException {

		CompensoBulk compenso = (CompensoBulk) bulk;
		if (compenso.getImportoObbligazione().compareTo(
				new java.math.BigDecimal(0)) <= 0
				&& scadenza != null)
			throw new it.cnr.jada.comp.ApplicationException(
					"E' stata collegato un'impegno ad un compenso con importo lordo minore o uguale a zero.\nEliminare l'impegno!");
		else if (compenso.getImportoObbligazione().compareTo(
				new java.math.BigDecimal(0)) > 0) {

			// Controllo esistenza Obbligazione
			if (scadenza == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"Nessun impegno associato!!!");

			ObbligazioneBulk obblig = scadenza.getObbligazione();

			if (obblig.getIm_obbligazione() == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'Importo dell'impegno è un dato obbligatorio");
			if (scadenza.getIm_scadenza() == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'Importo della scadenza è un dato obbligatorio");

			// Importo della scadenza diverso da quello del compenso
			if (scadenza.getIm_scadenza().compareTo(
					compenso.getImportoObbligazione()) != 0)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'importo della scadenza deve corrispondere all'importo del compenso");

			if (compenso.getIncarichi_repertorio_anno() != null
					&& compenso.getIncarichi_repertorio_anno().getCrudStatus() == OggettoBulk.NORMAL
					&& compenso.getIncarichi_repertorio_anno()
							.getEsercizio_limite().compareTo(
									obblig.getEsercizio_originale()) != 0)
				throw new it.cnr.jada.comp.ApplicationException(
						"La tipologia dell'impegno (competenza/residuo) non è coerente con la riga del contratto prescelta.");

			validaTerzoObbligazione(userContext, compenso, obblig);
		}
	}

	/**
	 * Validazione dell'oggetto in fase di stampa
	 * 
	 */

	private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
			StampaCertificazioneVBulk stampa) throws ComponentException {

	}

	/**
	 * Validazione dell'oggetto in fase di stampa
	 * 
	 */

	private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
			StampaPartitarioCompensiVBulk stampa) throws ComponentException {

		try {
			Timestamp dataOdierna = getDataOdierna(userContext);
			java.sql.Timestamp firstDayOfYear = getFirstDayOfYear(stampa
					.getEsercizio().intValue());

			/**** Controlli sulle Date DA A *****/
			if (stampa.getDataInizio() == null)
				throw new ValidationException(
						"Il campo DATA INIZIO PERIODO è obbligatorio");
			if (stampa.getDataFine() == null)
				throw new ValidationException(
						"Il campo DATA FINE PERIODO è obbligatorio");

			// La Data di Inizio Periodo è superiore alla data di Fine Periodo
			if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
				throw new ValidationException(
						"La DATA di INIZIO PERIODO non puè essere superiore alla DATA di FINE PERIODO");

			if (stampa.getAnagraficoForPrint() == null
					|| stampa.getAnagraficoForPrint().getCd_anag() == null)
				throw new ValidationException(
						"Attenzione: il campo Anagrafico è obbligatorio");

		} catch (ValidationException ex) {
			throw new ApplicationException(ex);
		}
	}

	/**
	 * Validazione dell'oggetto in fase di stampa
	 * 
	 */

	private void validateBulkForPrint(it.cnr.jada.UserContext userContext,
			StampaRiepilogoCompensiVBulk stampa) throws ComponentException {

		try {

			if (stampa.getTerzoForPrint() == null
					|| stampa.getTerzoForPrint().getCd_terzo() == null)
				throw new ValidationException(
						"Attenzione: il campo Terzo è obbligatorio");

		} catch (ValidationException ex) {
			throw new ApplicationException(ex);
		}
	}

	/**
	 * Validazione del Compenso per Esercizio COEP precedente
	 * 
	 * // Gennaro Borriello/Luisa Farinella - (05/11/2004 18.11.25) // Aggiunto
	 * controllo per i documenti RIPORTATI, in base all'esercizio COEP
	 * precedente // all'es. di scrivania.
	 */
	private void validateEsercizioCOEP(it.cnr.jada.UserContext userContext,
			CompensoBulk compenso) throws ComponentException {

		LoggableStatement cs = null;
		String status = null;

		try {
			if (compenso.isRiportataInScrivania()) {
				Integer es_prec = new Integer(
						it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(
								userContext).intValue() - 1);

				cs = new LoggableStatement(getConnection(userContext),
						"{ ? = call "
								+ it.cnr.jada.util.ejb.EJBCommonServices
										.getDefaultSchema()
								+ "CNRCTB200.isChiusuraCoepDef(?,?)}", false,
						this.getClass());
				cs.registerOutParameter(1, java.sql.Types.VARCHAR);
				cs.setObject(2, es_prec);
				cs.setObject(3, compenso.getCd_cds());

				cs.executeQuery();

				status = new String(cs.getString(1));

				if (status.compareTo("Y") != 0) {
					throw new it.cnr.jada.comp.ApplicationException(
							"Attenzione: non è possibile eliminare il documento, poichè l'esercizio economico precedente non è chiuso.");
				}
			}

		} catch (java.sql.SQLException ex) {
			throw new it.cnr.jada.comp.ComponentException(ex);
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (Throwable t) {
				throw new it.cnr.jada.comp.ComponentException(t);
			}
		}
	}

	/**
	 * Viene richiesta la validazione del terzo selezionato Ritorna una
	 * ApplicationException con la descrizione dell'errore relativo
	 * 
	 * errorCode Significato ========= =========== 0 Tutto bene 1 Terzo assente
	 * 2 Terzo non valido alla data registrazione 3 Controllo se ho inserito le
	 * modalità di pagamento 4 Banca non inserita 5 Tipo rapporto assente 6 Tipo
	 * di rapporto non valido in data inizio competenza coge 7 Tipo trattamento
	 * assente 8 Tipo trattamento non valido alla data registrazione
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Terzo assente Pre: Non è stato selezionato un terzo Post: Ritorna
	 * una ApplicationException con la descrizione dell'errore
	 * "Inserire il terzo"
	 * 
	 * Nome: Terzo non valido alla data registrazione Pre: Il terzo selezionato
	 * non è valido alla data registrazione Post: Ritorna una
	 * ApplicationException con la descrizione dell'errore
	 * "Il Terzo selezionato non è valido in Data Registrazione"
	 * 
	 * Nome: Modalita di pagamento assente Pre: Non è stato selezionata una
	 * modalita di pagamento Post: Ritorna una ApplicationException con la
	 * descrizione dell'errore "Selezionare la Modalità di pagamento"
	 * 
	 * Nome: Modalita di pagamento non valida Pre: Non è stato selezionata una
	 * modalita di pagamento valida (con banca) Post: Ritorna una
	 * ApplicationException con la descrizione dell'errore
	 * "Selezionare una Modalità di pagamento valida"
	 * 
	 * Nome: Tipo rapporto assente Pre: Non è stato selezionato un tipo rapporto
	 * Post: Ritorna una ApplicationException con la descrizione dell'errore
	 * "Selezionare il Tipo Rapporto"
	 * 
	 * Nome: Tipo rapporto non valido alla data inizio competenza coge Pre: Il
	 * tipo rapporto selezionato non è valido in data competenza coge Post:
	 * Ritorna una ApplicationException con la descrizione dell'errore
	 * "Il Tipo Rapporto selezionato non è valido alla Data Inizio Competenza"
	 * 
	 * Nome: Tipo trattamento assente Pre: Non è stato selezionato un tipo
	 * trattamento Post: Ritorna una ApplicationException con la descrizione
	 * dell'errore "Selezionare il Tipo Trattamento"
	 * 
	 * Nome: Tipo trattamento non valido alla data registrazione Pre: Non è
	 * stato selezionato un tipo trattamento Post: Ritorna una
	 * ApplicationException con la descrizione dell'errore
	 * "Il Tipo Trattamento selezionato non è valido alla Data Registrazione"
	 * 
	 * Nome: Terzo valido Pre: Il terzo selezionato non ha errori Post: Il terzo
	 * è valido e prosegue con l'operazione
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            il compenso di cui validare il terzo
	 * 
	 **/
	public void validaTerzo(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {

		int error = validaTerzo(userContext, compenso, true);
		handleExceptionsTerzo(error);
	}

	/**
	 * Viene richiesta la validazione del terzo selezionato Ritorna il codice di
	 * Errore relativo alla validzione
	 * 
	 * errorCode Significato ========= =========== 0 Tutto bene 1 Terzo assente
	 * 2 Terzo non valido alla data registrazione 3 Controllo se ho inserito le
	 * modalità di pagamento 4 Banca non inserita 5 Tipo rapporto assente 6 Tipo
	 * di rapporto non valido in data inizio competenza coge 7 Tipo trattamento
	 * assente 8 Tipo trattamento non valido alla data registrazione
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Terzo assente Pre: Non è stato selezionato un terzo Post: Ritorna
	 * il valore 1
	 * 
	 * Nome: Terzo non valido alla data registrazione Pre: Il terzo selezionato
	 * non è valido alla data registrazione Post: Ritorna il valore 2
	 * 
	 * Nome: Modalita di pagamento assente Pre: Non è stato selezionata una
	 * modalita di pagamento Post: Ritorna il valore 3
	 * 
	 * Nome: Banca non inserita Pre: Non è stato selezionato un conto corretto
	 * Post: Ritorna il valore 4
	 * 
	 * Nome: Tipo rapporto assente Pre: Non è stato selezionato un tipo rapporto
	 * Post: Ritorna il valore 5
	 * 
	 * Nome: Tipo rapporto non valido alla data inizio competenza coge Pre: Il
	 * tipo rapporto selezionato non è valido in data competenza coge Post:
	 * Ritorna il valore 6
	 * 
	 * Nome: Tipo trattamento assente Pre: Non è stato selezionato un tipo
	 * trattamento Post: Ritorna il valore 7
	 * 
	 * Nome: Tipo trattamento non valido alla data registrazione Pre: Non è
	 * stato selezionato un tipo trattamento Post: Ritorna il valore 8
	 * 
	 * Nome: Terzo valido Pre: Il terzo selezionato non ha errori Post: Ritorna
	 * il valore 0
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            il compenso di cui validare il terzo
	 * @return il codice di errore relativo
	 * 
	 **/
	public int validaTerzo(UserContext userContext, CompensoBulk compenso,
			boolean checkModPag) throws ComponentException {

		TerzoBulk terzo = compenso.getTerzo();

		// terzo assente
		if (terzo == null)
			return 1;

		// terzo non valido alla data registrazione
		if (terzo.getDt_fine_rapporto() != null
				&& terzo.getDt_fine_rapporto().compareTo(
						compenso.getDt_registrazione()) < 0)
			return 2;

		// Controllo se ho inserito le modalità di pagamento
		if (checkModPag && compenso.getModalitaPagamento() == null)
			return 3;

		// banca assente
		if (checkModPag && compenso.getBanca() == null)
			return 4;

		// tipo rapporto assente
		if (compenso.getTipoRapporto() == null)
			return 5;

		// rapporto non valido in data inizio competenza coge
		if (!isTipoRapportoValido(userContext, compenso))
			return 6;

		// tipo trattamento assente
		if (compenso.getTipoTrattamento() == null)
			return 7;

		// tipo trattamento non valido alla data registrazione
		if (!isTipoTrattamentoValido(userContext, compenso))
			return 8;

		// tipo rapporto inps assente
		if (compenso.isVisualizzaCodici_rapporti_inps()
				&& compenso.getCd_rapporto_inps() == null)
			return 9;

		// tipo attivita inps assente
		if (compenso.isVisualizzaCodici_attivita_inps()
				&& compenso.getCd_attivita_inps() == null)
			return 10;

		// tipo altra ass inps assente
		if (compenso.isVisualizzaCodici_altra_forma_ass_inps()
				&& compenso.getCd_altra_ass_inps() == null)
			return 11;

		// comune inps assente
		if (compenso.isVisualizzaCodici_rapporti_inps()
				&& compenso.getPg_comune_inps() == null)
			return 12;
		// banca non valida
		if (checkModPag && compenso.getBanca() != null
				&& compenso.getBanca().getFl_cancellato())
			return 13;
		// tipo prestazione assente
		if (//isGestitePrestazioni(userContext) &&
				compenso.getTipoPrestazioneCompenso() == null 
				&& compenso.isPrestazioneCompensoEnabled())
			return 14;
				
		// pignorato assente
		if (compenso.isVisualizzaPignorato()
				&& compenso.getCd_terzo_pignorato() == null)
			return 15;		
		return (0);
	}

	/**
	 * Viene controllato che il terzo selezionato nel compenso corrisponda con
	 * il terzo selezionato nell'obbligazione
	 * 
	 * Pre-post-conditions
	 * 
	 * Nome: Terzo obbligazione non valido Pre: Non è stato selezionato il terzo
	 * nell'obbligazione Post: Ritorna un ApplicationException con la
	 * descrizione dell'errore
	 * 
	 * Nome: Terzo obbligazione uguale al terzo compenso Pre: Il terzo
	 * selezionato nell'obbligazione corrisponde al terzo del compenso Post:
	 * Viene validato il terzo
	 * 
	 * Nome: L'anagrafica dell'obbligazione ha tipoEntita DIVERSI Pre:
	 * L'anagafica associata al terzo dell'obbligazione ha come tipo entita
	 * DIVERSI Post: Viene validato il terzo
	 * 
	 * Nome: Nessuna delle due condizioni precedenti è verificata Pre: Il terzo
	 * selezionato NON corrisponde al terzo del compenso e l'anagrafica
	 * associata NON ha tipo entita DIVERSI Post: Ritorna un
	 * ApplicationException con la descrizione dell'errore
	 * 
	 * @param userContext
	 *            lo UserContext che genera la richiesta
	 * @param compenso
	 *            il compenso di cui validare il terzo
	 * @param obblig
	 *            l'obbligazione di cui validare il terzo
	 * 
	 **/
	private void validaTerzoObbligazione(UserContext userContext,
			CompensoBulk compenso, ObbligazioneBulk obblig)
			throws ComponentException {

		try {
			// controllo su terzo
			TerzoBulk creditore = obblig.getCreditore();
			TerzoHome terzoHome = (TerzoHome) getHome(userContext,
					TerzoBulk.class);
			creditore = (TerzoBulk) terzoHome.findByPrimaryKey(obblig
					.getCreditore());
			if (creditore == null || creditore.getCd_terzo() == null)
				throw new it.cnr.jada.comp.ApplicationException(
						"L'impegno deve avere un creditore valido!");
			getHomeCache(userContext).fetchAll(userContext);

			AnagraficoHome anaHome = (AnagraficoHome) getHome(userContext,
					AnagraficoBulk.class);
			AnagraficoBulk ana = (AnagraficoBulk) anaHome
					.findByPrimaryKey(creditore.getAnagrafico());
			if (!compenso.getTerzo().equalsByPrimaryKey(creditore)
					&& !AnagraficoBulk.DIVERSI.equalsIgnoreCase(ana
							.getTi_entita()))
				throw new it.cnr.jada.comp.ApplicationException(
						"La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore del compenso!");

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	public boolean verificaStatoEsercizio(UserContext userContext,
			it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio)
			throws ComponentException {
		try {
			it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome) getHome(
					userContext,
					it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);

			return !eHome.isEsercizioChiuso(userContext, anEsercizio
					.getEsercizio(), anEsercizio.getCd_cds());
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}

	// =================================================================================
	// Estrazione dati 770 28/10/2004 by Aurelio D'Amico
	// =================================================================================
	/**
	 * Gestisce il richiamo delle procedure sul DB per l'estrazione del 770.
	 * 
	 * PreCondition: richiesta di elaborare un 770 PostCondition: richiama
	 * callEstrazione770, per l'elaborazione del report richiesto
	 * 
	 * Si è verificato un errore. PreCondition: Si è verificato un errore.
	 * PostCondition: Viene inviato un messaggio e non permette l'operazione
	 */
	public it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk doElabora770(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		java.math.BigDecimal id_report = getSequence(userContext);

		bulk.setId_report(id_report);

		try {
			if (bulk instanceof Estrazione770Bulk) {
				callEstrazione770(userContext, (Estrazione770Bulk) bulk);
			}
		} catch (Throwable t) {
			throw handleException(t);
		}

		return bulk;

	}

	/**
	 * Richiama la procedure sul DB per l'estrazione 770
	 * 
	 * Si è verificato un errore. PreCondition: Si è verificato un errore.
	 * PostCondition: Viene inviato un messaggio e non permette l'operazione
	 */
	private void callEstrazione770(UserContext userContext,
			Estrazione770Bulk bulk) throws java.sql.SQLException,
			it.cnr.jada.persistency.PersistencyException, ComponentException {

		LoggableStatement cs = null;
		try {
			cs = new LoggableStatement(getConnection(userContext), "{call "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB930.estrazione770(?, ?, ?, ?, ?, ?) }", false, this
					.getClass());

			cs.setInt(1, bulk.getEsercizio().intValue()); // Esercizio
			cs.setString(2, bulk.getQuadri_770().getTi_modello()); // Tipo Modello
			cs.setString(3, bulk.getQuadri_770().getCd_quadro()); // Codice Quadro
			cs.setLong(4, bulk.getId_report().longValue()); // Id report
			cs.setString(5, null); // Messaggio
			cs.setString(6, bulk.getUser()); // Utente

			cs.executeQuery();

		} catch (java.sql.SQLException e) {
			throw SQLExceptionHandler.getInstance().handleSQLException(e);
		} finally {
			if (cs != null)
				cs.close();
		}
	}

	// aggiunto per testare l'esercizio della data competenza da/a
	public boolean isEsercizioChiusoPerDataCompetenza(UserContext userContext,
			Integer esercizio, String cd_cds) throws ComponentException,
			PersistencyException {
		try {
			LoggableStatement cs = new LoggableStatement(
					getConnection(userContext), "{ ? = call "
							+ it.cnr.jada.util.ejb.EJBCommonServices
									.getDefaultSchema()
							+ "CNRCTB008.isEsercizioChiusoYesNo(?,?)}", false,
					this.getClass());

			try {
				cs.registerOutParameter(1, java.sql.Types.CHAR);
				cs.setObject(2, esercizio);
				cs.setObject(3, cd_cds);

				cs.execute();

				return "Y".equals(cs.getString(1));
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			throw handleSQLException(e);
		}
	}

	public boolean isTerzoCervellone(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		if (compenso.getCd_terzo() != null) {
			try {
				TerzoHome tHome = (TerzoHome) getHome(userContext,
						TerzoBulk.class);
				TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
				TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);
				if (t == null) {
					return false;
				} else {
					AnagraficoHome aHome = (AnagraficoHome) getHome(
							userContext, AnagraficoBulk.class);
					AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
					AnagraficoBulk a = (AnagraficoBulk) aHome
							.findByPrimaryKey(aKey);

					if (a.getFl_cervellone())
						return true;
					else
						return false;
				}
			} catch (it.cnr.jada.persistency.PersistencyException ex) {
				throw handleException(compenso, ex);
			}
		} else
			return false;
	}

	public SQLBuilder selectCdsForPrintByClause(UserContext userContext,
			StampaCompensiBulk stampa, CdsBulk cds, CompoundFindClause clause)
			throws ComponentException, PersistencyException {
		SQLBuilder sql = ((CdsHome) getHome(userContext, cds.getClass(),
				"V_CDS_VALIDO")).createSQLBuilderIncludeEnte();
		sql.addClause(clause);

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext
				.getCd_cds(userContext);
		it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome) getHome(
				userContext, CdsBulk.class);
		it.cnr.contab.config00.sto.bulk.CdsBulk cds_scrivania = (CdsBulk) cds_home
				.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (cds_scrivania
				.getCd_tipo_unita()
				.equals(
						it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
		} else {
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getEsercizio());
			sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",
					SQLBuilder.EQUALS,
					((it.cnr.contab.utenze00.bp.CNRUserContext) userContext)
							.getCd_cds());
			sql.addClause("AND", "FL_CDS", sql.EQUALS, new Boolean(true));
		}
		return sql;
	}

	public SQLBuilder selectUoForPrintByClause(UserContext userContext,
			StampaCompensiBulk stampa, Unita_organizzativaBulk uo,
			CompoundFindClause clause) throws ComponentException,
			PersistencyException {
		// String cd_cds_scrivania =
		// it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		// it.cnr.contab.config00.sto.bulk.CdsHome cds_home =
		// (CdsHome)getHome(userContext, CdsBulk.class);

		if (clause == null)
			clause = uo.buildFindClauses(null);

		SQLBuilder sql = ((Unita_organizzativaHome) getHome(userContext, uo,
				"V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
				it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext));
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa
				.getCdCdsForPrint());

		if (clause != null)
			sql.addClause(clause);
		return sql;
	}

	public boolean verificaEsistenzaCompenso(UserContext userContext,
			CompensoBulk compenso) throws ComponentException, RemoteException {

		SQLBuilder sql = getHome(userContext, CompensoBulk.class)
				.createSQLBuilder();
		sql.addTableToHeader("V_COMPENSO");
		sql.addSQLJoin("V_COMPENSO.ESERCIZIO", "COMPENSO.ESERCIZIO");
		sql.addSQLJoin("V_COMPENSO.CD_CDS", "COMPENSO.CD_CDS");
		sql.addSQLJoin("V_COMPENSO.CD_UNITA_ORGANIZZATIVA",
				"COMPENSO.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("V_COMPENSO.PG_COMPENSO", "COMPENSO.PG_COMPENSO");
		sql.addSQLClause("AND", "COMPENSO.ESERCIZIO", sql.EQUALS, compenso
				.getEsercizio());
		sql.addSQLClause("AND", "COMPENSO.CD_CDS", sql.EQUALS, compenso
				.getCd_cds());
		sql.addSQLClause("AND", "COMPENSO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,
				compenso.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "COMPENSO.PG_COMPENSO", sql.EQUALS, compenso
				.getPg_compenso());
		try {
			if (sql.executeCountQuery(getConnection(userContext)) > 0)
				return true;
		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isCompensoValido(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		if (isImportoOk(userContext, compenso))
			return true;
		// controllo la modalità di pagamento
		if (!compenso.getModalitaPagamento().getFl_utilizzabile_art35())
			throw new ApplicationException(
					"Modalità di pagamento non coerente con il trattamento prescelto (Art.35 DL n.223/2006)");
		// return false;
		else
			return true;
	}

	public boolean isImportoOk(UserContext userContext, CompensoBulk compenso)
			throws ComponentException {
		BigDecimal importo1 = null;
		Timestamp data_da1 = null;
		Timestamp data_a1 = null;
		BigDecimal importo2 = null;
		Timestamp data_da2 = null;
		Timestamp data_a2 = null;
		BigDecimal importo3 = null;
		Timestamp data_da3 = null;
		Timestamp data_a3 = null;
		BigDecimal importo_limite = null;
		try {
			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");

			if (sess.getIm01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE1") == null
					|| sess.getDt01(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE1") == null
					|| sess.getDt02(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE1") == null
					|| sess.getIm01(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE2") == null
					|| sess.getDt01(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE2") == null
					|| sess.getDt02(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE2") == null
					|| sess.getIm01(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE3") == null
					|| sess.getDt01(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE3") == null
					|| sess.getDt02(userContext, new Integer(0), null,
							"ADEGUAMENTI_ART35_DL223", "LIMITE3") == null)

				throw new ApplicationException(
						"Configurazione CNR: non sono stati impostati i valori per la gestione dell'Art.35 DL n.223/2006");

			importo1 = sess.getIm01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE1");
			data_da1 = sess.getDt01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE1");
			data_a1 = sess.getDt02(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE1");
			importo2 = sess.getIm01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE2");
			data_da2 = sess.getDt01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE2");
			data_a2 = sess.getDt02(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE2");
			importo3 = sess.getIm01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE3");
			data_da3 = sess.getDt01(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE3");
			data_a3 = sess.getDt02(userContext, new Integer(0), null,
					"ADEGUAMENTI_ART35_DL223", "LIMITE3");

			java.sql.Timestamp data_emis_mand = it.cnr.jada.util.ejb.EJBCommonServices
					.getServerDate();

			if (!data_emis_mand.before(data_da1)
					&& !data_emis_mand.after(data_a1)) {
				importo_limite = importo1;
			} else if (!data_emis_mand.before(data_da2)
					&& !data_emis_mand.after(data_a2)) {
				importo_limite = importo2;
			} else if (!data_emis_mand.before(data_da3)
					&& !data_emis_mand.after(data_a3)) {
				importo_limite = importo3;
			} else
				return true;

			if (compenso.getIm_netto_percipiente().compareTo(importo_limite) < 0)
				return true;

			return false;

		} catch (javax.ejb.EJBException e) {
			throw handleException(compenso, e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(compenso, e);
		}
	}

	public boolean isAccontoAddComOkPerContabil(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		Timestamp data_da = null;
		Timestamp data_a = null;
		try {
			if (compenso.getFl_compenso_minicarriera()) {
				Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
						.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");

				if (sess.getDt01(userContext, compenso.getEsercizio(), null,
						"ACCONTO_ADD_COM", "PERIODO_VALIDITA") == null
						|| sess.getDt02(userContext, compenso.getEsercizio(),
								null, "ACCONTO_ADD_COM", "PERIODO_VALIDITA") == null)

					throw new ApplicationException(
							"Configurazione CNR: non sono stati impostati i valori per la gestione dell'Acconto Addizionale Comunale");

				data_da = sess.getDt01(userContext, compenso.getEsercizio(),
						null, "ACCONTO_ADD_COM", "PERIODO_VALIDITA");
				data_a = sess.getDt02(userContext, compenso.getEsercizio(),
						null, "ACCONTO_ADD_COM", "PERIODO_VALIDITA");

				java.sql.Timestamp data_emis_mand = it.cnr.jada.util.ejb.EJBCommonServices
						.getServerDate();

				// Se data_emis_mand è compreso nel periodo di validità, deve
				// essere calcolato l'acconto
				// altrimenti no
				if (compenso.getCd_terzo() != null) {
					try {
						TerzoHome tHome = (TerzoHome) getHome(userContext,
								TerzoBulk.class);
						TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
						TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);
						if (t == null) {
							return false;
						} else {
							Acconto_classific_coriHome aHome = (Acconto_classific_coriHome) getHome(
									userContext,
									Acconto_classific_coriBulk.class);
							Acconto_classific_coriBulk aKey = new Acconto_classific_coriBulk(
									compenso.getEsercizio(), t.getCd_anag(),
									new String("C0"));
							Acconto_classific_coriBulk a = (Acconto_classific_coriBulk) aHome
									.findByPrimaryKey(aKey);

							if (a == null)
								return true;
							else {
								// se esiste l'acconto, devo verificare se il
								// trattamento lo prevede
								if (!esisteAccAddComSuTratt(compenso, a))
									return true;
								else {
									if (data_emis_mand.compareTo(data_da) >= 0
											&& data_emis_mand.compareTo(data_a) <= 0) {
										if (esisteAccAddCom(compenso, a)
												|| a
														.getIm_acconto_trattenuto()
														.equals(
																a
																		.getIm_acconto_calcolato())
												|| a.getPg_conguaglio() != null)
											return true;
										else
											return false;
									} else {
										if (!esisteAccAddCom(compenso, a))
											return true;
										else
											return false;
									}
								}
							}
						}
					} catch (it.cnr.jada.persistency.PersistencyException ex) {
						throw handleException(compenso, ex);
					}
				} else
					return false;
			} else
				return true;

		} catch (javax.ejb.EJBException e) {
			throw handleException(compenso, e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(compenso, e);
		}
	}

	public boolean esisteAccAddCom(CompensoBulk compenso,
			Acconto_classific_coriBulk acconto) {
		java.util.Collection collCori = compenso.getContributi();
		for (java.util.Iterator i = collCori.iterator(); i.hasNext();) {
			Contributo_ritenutaBulk cr = (Contributo_ritenutaBulk) i.next();
			if (cr.getTipoContributoRitenuta().getCd_classificazione_cori()
					.equals(acconto.TIPO_ACCONTO_ADD_COM)
					&& cr.getAmmontare().compareTo(new BigDecimal(0)) != 0)
				return true;
		}
		return false;
	}

	public boolean isGestiteDeduzioniIrpef(UserContext userContext)
			throws ComponentException {
		try {
			Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",
							Parametri_cnrComponentSession.class))
					.getParametriCnr(userContext, CNRUserContext
							.getEsercizio(userContext));
			return par.getFl_deduzione_irpef();
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	
	public SQLBuilder selectIncarichi_repertorio_annoByClause(
			UserContext userContext, CompensoBulk compenso,
			Incarichi_repertorio_annoBulk repertorio, CompoundFindClause clauses)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		if (compenso.getCd_terzo() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire prima il codice del Terzo");
		if (compenso.getTerzo() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Ricercare prima il codice del Terzo");
		if (compenso.getCd_tipo_rapporto() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire prima il Tipo Rapporto");		

		SQLBuilder sql = getHome(userContext,
				Incarichi_repertorio_annoBulk.class).createSQLBuilder();
		// getHomeCache(userContext).fetchAll(userContext);
		if (clauses != null)
			sql.addClause(clauses);

		sql.addTableToHeader("INCARICHI_REPERTORIO");
		sql.addSQLJoin("INCARICHI_REPERTORIO_ANNO.ESERCIZIO",
				"INCARICHI_REPERTORIO.ESERCIZIO");
		sql.addSQLJoin("INCARICHI_REPERTORIO_ANNO.PG_REPERTORIO",
				"INCARICHI_REPERTORIO.PG_REPERTORIO");

		SQLBuilder sqlUoExists = getHome(userContext, Ass_incarico_uoBulk.class)
				.createSQLBuilder();
		sqlUoExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO",
				"INCARICHI_REPERTORIO.ESERCIZIO");
		sqlUoExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO",
				"INCARICHI_REPERTORIO.PG_REPERTORIO");
		sqlUoExists.addSQLClause("AND",
				"ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
				compenso.getCd_unita_organizzativa());

		sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.CD_CDS",
				SQLBuilder.EQUALS, compenso.getCd_cds());
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.CD_UNITA_ORGANIZZATIVA",
				SQLBuilder.EQUALS, compenso.getCd_unita_organizzativa());
		sql.closeParenthesis();
		sql.addSQLExistsClause(FindClause.OR, sqlUoExists);
		sql.closeParenthesis();

		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.STATO",
				SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_DEFINITIVO);
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.TI_ISTITUZ_COMMERC",
				SQLBuilder.EQUALS, compenso.getTi_istituz_commerc());

		if (!compenso.getTerzo().isStudioAssociato())
			sql.addSQLClause("AND", "INCARICHI_REPERTORIO.CD_TERZO",
					SQLBuilder.EQUALS, compenso.getCd_terzo());
		else {
			AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(
					userContext, AnagraficoBulk.class);
			sql.openParenthesis("AND");
			sql.addSQLClause("OR", "INCARICHI_REPERTORIO.CD_TERZO",
					SQLBuilder.EQUALS, compenso.getCd_terzo());
			try {
				for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome
						.findAssociatiStudio(
								compenso.getTerzo().getAnagrafico()).iterator(); i
						.hasNext();) {
					Anagrafico_terzoBulk associato = i.next();
					if (associato.getDt_canc() == null
							|| associato.getDt_canc().after(
									compenso.getDt_a_competenza_coge()))
						sql.addSQLClause("OR", "INCARICHI_REPERTORIO.CD_TERZO",
								SQLBuilder.EQUALS, associato.getCd_terzo());
				}
			} catch (IntrospectionException e) {
			}
			sql.closeParenthesis();
		}

		if (compenso.getCd_tipo_rapporto() != null)
			sql.addSQLClause("AND", "INCARICHI_REPERTORIO.CD_TIPO_RAPPORTO",
					SQLBuilder.EQUALS, compenso.getCd_tipo_rapporto());
		// if(compenso.getCd_trattamento()!=null)
		// sql.addSQLClause("AND","INCARICHI_REPERTORIO.CD_TRATTAMENTO",SQLBuilder.EQUALS,
		// compenso.getCd_trattamento());
		if (compenso.isApertoDaMinicarriera()
				&& compenso.getMinicarriera().getIncarichi_repertorio() != null) {
			sql.addSQLClause("AND", "INCARICHI_REPERTORIO.ESERCIZIO",
					SQLBuilder.EQUALS, compenso.getMinicarriera()
							.getIncarichi_repertorio().getEsercizio());
			sql.addSQLClause("AND", "INCARICHI_REPERTORIO.PG_REPERTORIO",
					SQLBuilder.EQUALS, compenso.getMinicarriera()
							.getIncarichi_repertorio().getPg_repertorio());
		}

		// sql.addSQLClause("AND","INCARICHI_REPERTORIO.IMPORTO_COMPLESSIVO",SQLBuilder.GREATER_EQUALS,
		// prendiUtilizzato(userContext, repertorio));
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_INIZIO_VALIDITA",
				SQLBuilder.LESS_EQUALS, compenso.getDt_da_competenza_coge());

		sql.openParenthesis("AND");
		sql.openParenthesis("");
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_FINE_VALIDITA",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_PROROGA",
				SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_PROROGA",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_PROROGA_PAGAM",
				SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause("AND", "INCARICHI_REPERTORIO.DT_PROROGA_PAGAM",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());
		sql.closeParenthesis();

		SQLBuilder sqlExists = getHome(userContext,
				Incarichi_repertorio_varBulk.class).createSQLBuilder();
		sqlExists.addSQLJoin("INCARICHI_REPERTORIO_VAR.ESERCIZIO",
				"INCARICHI_REPERTORIO.ESERCIZIO");
		sqlExists.addSQLJoin("INCARICHI_REPERTORIO_VAR.PG_REPERTORIO",
				"INCARICHI_REPERTORIO.PG_REPERTORIO");
		sqlExists.openParenthesis(FindClause.AND);
		sqlExists.addSQLClause(FindClause.OR,
				"INCARICHI_REPERTORIO_VAR.TIPO_VARIAZIONE", SQLBuilder.EQUALS,
				Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO_TRANS);
		sqlExists.addSQLClause(FindClause.OR,
				"INCARICHI_REPERTORIO_VAR.TIPO_VARIAZIONE", SQLBuilder.EQUALS,
				Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO);
		sqlExists.closeParenthesis();
		sqlExists.addSQLClause("AND", "INCARICHI_REPERTORIO_VAR.STATO",
				SQLBuilder.EQUALS,
				Incarichi_repertorio_varBulk.STATO_DEFINITIVO);
		sqlExists.addSQLClause("AND",
				"INCARICHI_REPERTORIO_VAR.DT_FINE_VALIDITA",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());

		sql.addSQLExistsClause("OR", sqlExists);
		sql.closeParenthesis();

		return sql;
	}

	public SQLBuilder selectContrattoByClause(
			UserContext userContext, CompensoBulk compenso,
			ContrattoBulk contratto, CompoundFindClause clauses)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException {
		if (compenso.getCd_terzo() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire prima il codice del Terzo");
		if (compenso.getTerzo() == null)
			throw new it.cnr.jada.comp.ApplicationException(
					"Ricercare prima il codice del Terzo");

		SQLBuilder sql = getHome(userContext,
				ContrattoBulk.class).createSQLBuilder();
		// getHomeCache(userContext).fetchAll(userContext);
		if (clauses != null)
			sql.addClause(clauses);

		SQLBuilder sqlUoExists = getHome(userContext, Ass_contratto_uoBulk.class)
				.createSQLBuilder();
		sqlUoExists.addSQLJoin("ASS_CONTRATTO_UO.ESERCIZIO",
				"CONTRATTO.ESERCIZIO");
		sqlUoExists.addSQLJoin("ASS_CONTRATTO_UO.STATO_CONTRATTO",
				"CONTRATTO.STATO");
		sqlUoExists.addSQLJoin("ASS_CONTRATTO_UO.PG_CONTRATTO",
				"CONTRATTO.PG_CONTRATTO");
		sqlUoExists.addSQLClause("AND",
				"ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
				compenso.getCd_unita_organizzativa());

		sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause("AND", "CONTRATTO.CD_UNITA_ORGANIZZATIVA",
				SQLBuilder.EQUALS, compenso.getCd_unita_organizzativa());
		sql.closeParenthesis();
		sql.addSQLExistsClause(FindClause.OR, sqlUoExists);
		sql.closeParenthesis();

		sql.addSQLClause("AND", "CONTRATTO.STATO",
				SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);

		if (!compenso.getTerzo().isStudioAssociato())
			sql.addSQLClause("AND", "CONTRATTO.FIG_GIUR_EST",
					SQLBuilder.EQUALS, compenso.getCd_terzo());
		else {
			AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(
					userContext, AnagraficoBulk.class);
			sql.openParenthesis("AND");
			sql.addSQLClause("OR", "CONTRATTO.FIG_GIUR_EST",
					SQLBuilder.EQUALS, compenso.getCd_terzo());
			try {
				for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome
						.findAssociatiStudio(
								compenso.getTerzo().getAnagrafico()).iterator(); i
						.hasNext();) {
					Anagrafico_terzoBulk associato = i.next();
					if (associato.getDt_canc() == null
							|| associato.getDt_canc().after(
									compenso.getDt_a_competenza_coge()))
						sql.addSQLClause("OR", "CONTRATTO.FIG_GIUR_EST",
								SQLBuilder.EQUALS, associato.getCd_terzo());
				}
			} catch (IntrospectionException e) {
			}
			sql.closeParenthesis();
		}
		
		sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
		sql.addSQLClause("AND", "CONTRATTO.NATURA_CONTABILE",
				SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addSQLClause("AND", "CONTRATTO.NATURA_CONTABILE",
				SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_PASSIVO);
		sql.closeParenthesis();
		sql.closeParenthesis();
		
/*
		if (compenso.isApertoDaMinicarriera()
				&& compenso.getMinicarriera().getContratto() != null) {
			sql.addSQLClause("AND", "CONTRATTO.ESERCIZIO",
					SQLBuilder.EQUALS, compenso.getMinicarriera()
							.getContratto().getEsercizio());
			sql.addSQLClause("AND", "CONTRATTO.STATO_CONTRATTO",
					SQLBuilder.EQUALS, compenso.getMinicarriera()
							.getContratto().getStato_contratto());
			sql.addSQLClause("AND", "CONTRATTO.PG_CONTRATTO",
					SQLBuilder.EQUALS, compenso.getMinicarriera()
							.getContratto().getPg_contratto());
		}
*/
		sql.addSQLClause("AND", "CONTRATTO.DT_INIZIO_VALIDITA",
				SQLBuilder.LESS_EQUALS, compenso.getDt_da_competenza_coge());
		
		sql.openParenthesis("AND");
		
		sql.openParenthesis("");
		sql.addSQLClause("AND", "CONTRATTO.DT_FINE_VALIDITA",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addSQLClause("AND", "CONTRATTO.DT_PROROGA",
				SQLBuilder.ISNOTNULL, null);
		sql.addSQLClause("AND", "CONTRATTO.DT_PROROGA",
				SQLBuilder.GREATER_EQUALS, compenso.getDt_a_competenza_coge());
		sql.closeParenthesis();
		
		sql.closeParenthesis();

		return sql;
	}

	private boolean isGestitiIncarichi(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		try {
			return Utility.createParametriCnrComponentSession()
					.getParametriCnr(userContext,
							CNRUserContext.getEsercizio(userContext))
					.getFl_incarico()
					&& compenso != null && compenso.isIncaricoEnabled();
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public CompensoBulk completaIncarico(UserContext userContext,
			CompensoBulk compenso, Incarichi_repertorio_annoBulk incarico_anno)
			throws ComponentException {

		if (compenso != null) {
			try {
				if (compenso.getTipoTrattamento() == null) {
					Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome) getHome(
							userContext, Tipo_trattamentoBulk.class);
					Tipo_trattamentoBulk tratt;
					tratt = trattHome.findIntervallo(new Tipo_trattamentoBulk(
							incarico_anno.getIncarichi_repertorio()
									.getTipo_trattamento().getCd_trattamento(),
							trattHome.getServerDate()));
					compenso.impostaTipoTratt(tratt);
					onTipoTrattamentoChange(userContext, compenso);
					compenso.setStatoCompensoToEseguiCalcolo();
				}
				/*
				 * int dim =getHome(userContext,compenso).getColumnMap().
				 * getMappingForProperty("ds_compenso").getColumnSize(); String
				 * ds_compenso_new = compenso.getDs_compenso()+" - "+
				 * incarico_anno.getIncarichi_repertorio().getOggetto(); if
				 * (ds_compenso_new.length()>dim-1) ds_compenso_new =
				 * ds_compenso_new.substring(0,dim-1);
				 * compenso.setDs_compenso(ds_compenso_new);
				 */
			} catch (it.cnr.jada.persistency.PersistencyException ex) {
				throw handleException(ex);
			}
		}
		return compenso;
	}

	private void validaIncarico(UserContext userContext,
			Obbligazione_scadenzarioBulk scadenza, OggettoBulk bulk)
			throws ComponentException {

		CompensoBulk compenso = (CompensoBulk) bulk;

		if (!isGestitiIncarichi(userContext, compenso))
			return;

		if (compenso.getIncarichi_repertorio_anno() == null
				|| compenso.getIncarichi_repertorio_anno().getCrudStatus() == OggettoBulk.UNDEFINED)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire il contratto.");
		
		//se sono state cambiate le date di competenza controllo la coerenza con le date dell'incarico
		if (compenso.getIncarichi_repertorio_anno()!= null
			&&
			 (
				(compenso.getDt_da_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_inizio_validita()) < 0)
				||
				(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()== null &&
				 compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()== null &&
				 compenso.getDt_a_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_fine_validita()) > 0)
		     	||
			    (compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()== null &&
	    		 compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()!= null && 
	    		 (compenso.getDt_a_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()) > 0))
	     	    ||
		        (compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()== null && 
		         compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()!= null && 
		        (compenso.getDt_a_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()) > 0))
		        ||
		        (compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()!= null && 
		         compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()!= null &&
		         compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()) > 0 &&
		        (compenso.getDt_a_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()) > 0))
		        ||
		        (compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()!= null && 
		         compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()!= null &&
		         compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga_pagam()) > 0 &&
		        (compenso.getDt_a_competenza_coge().compareTo(compenso.getIncarichi_repertorio_anno().getIncarichi_repertorio().getDt_proroga()) > 0))
		      )
		    )
			throw new it.cnr.jada.comp.ApplicationException(
					"La competenza del compenso non è interna alle date di validità del contratto. ");

		//le borse e gli assegni possono andare su piè fondi
		if (compenso.getTipoPrestazioneCompenso().getFl_controllo_fondi())
		{
			Obbligazione_scadenzarioHome obblHome = (Obbligazione_scadenzarioHome) getHome(
					userContext, Obbligazione_scadenzarioBulk.class);
			Iterator listaScad_voce;
			try {
				listaScad_voce = obblHome.findObbligazione_scad_voceList(
						userContext, scadenza).iterator();
	
				for (Iterator x = listaScad_voce; x.hasNext();) {
					Obbligazione_scad_voceBulk dett = (Obbligazione_scad_voceBulk) x
							.next();
					getHomeCache(userContext).fetchAll(userContext);
					WorkpackageBulk linea_att = dett.getLinea_attivita();
					if (!linea_att.getNatura().getTipo().equals(
							compenso.getIncarichi_repertorio_anno()
									.getIncarichi_repertorio()
									.getIncarichi_procedura().getTipo_natura()))
						throw new it.cnr.jada.comp.ApplicationException(
								"La tipologia della natura dell'impegno è diversa da quella del contratto prescelto.");
				}
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(e);
			} catch (it.cnr.jada.persistency.IntrospectionException e) {
				throw handleException(e);
			}
		}		
		
		try {
			Incarichi_repertorio_annoBulk incarichi_repertorio_annoBulk = (Incarichi_repertorio_annoBulk) getHome(
					userContext, Incarichi_repertorio_annoBulk.class)
					.findByPrimaryKey(compenso.getIncarichi_repertorio_anno(),
							true);
			BigDecimal differenza = incarichi_repertorio_annoBulk
					.getImporto_complessivo().add(
							prendiUtilizzato(userContext,
									compenso.getIncarichi_repertorio_anno())
									.negate());
			BigDecimal impConfronto = new BigDecimal(0);
			if (compenso.isDaMinicarriera()
					&& compenso.getPg_compenso().compareTo(new Long(0)) == -1)
				impConfronto = compenso.getIm_totale_compenso();

			if (differenza.compareTo(impConfronto) == -1)
				throw new it.cnr.jada.comp.ApplicationException(
						"Il totale del compenso supera il limite ancora esistente ("
								+ new it.cnr.contab.util.EuroFormat()
										.format(differenza.abs())
								+ ") per il contratto prescelto.");

		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	private void validaContratto(UserContext userContext, OggettoBulk bulk)
			throws ComponentException {

		CompensoBulk compenso = (CompensoBulk) bulk;

		if (compenso == null || !compenso.isContrattoEnabled())         
			return;

		if (compenso.getContratto() == null
				|| compenso.getContratto().getCrudStatus() == OggettoBulk.UNDEFINED)
			throw new it.cnr.jada.comp.ApplicationException(
					"Inserire il contratto.");
		
		//se sono state cambiate le date di competenza controllo la coerenza con le date del contratto
		if (compenso.getContratto()!= null
			&&
			 (
				(compenso.getDt_da_competenza_coge().compareTo(compenso.getContratto().getDt_inizio_validita()) < 0)
				||
				(compenso.getContratto().getDt_proroga()== null &&
				 compenso.getDt_a_competenza_coge().compareTo(compenso.getContratto().getDt_fine_validita()) > 0)
		     	||
		     	(compenso.getContratto().getDt_proroga()!= null &&
				 compenso.getDt_a_competenza_coge().compareTo(compenso.getContratto().getDt_proroga()) > 0)
		     	)
		    )
			throw new it.cnr.jada.comp.ApplicationException(
					"La competenza del compenso non è interna alle date di validità del contratto. ");
	}

	public BigDecimal prendiUtilizzato(UserContext userContext,
			Incarichi_repertorio_annoBulk incarico_anno)
			throws ComponentException {
		IncarichiRepertorioComponentSession incarico;
		try {
			incarico = (IncarichiRepertorioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB(
							"CNRINCARICHI00_EJB_IncarichiRepertorioComponentSession",
							IncarichiRepertorioComponentSession.class);

			BigDecimal utilizzato = new BigDecimal(0);
			utilizzato = incarico.calcolaUtilizzato(userContext, incarico_anno);

			return utilizzato;

		} catch (javax.ejb.EJBException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean esisteAccAddComSuTratt(CompensoBulk compenso,
			Acconto_classific_coriBulk acconto) {
		java.util.Collection collCori = compenso.getContributi();
		for (java.util.Iterator i = collCori.iterator(); i.hasNext();) {
			Contributo_ritenutaBulk cr = (Contributo_ritenutaBulk) i.next();
			if (cr.getTipoContributoRitenuta().getCd_classificazione_cori()
					.equals(acconto.TIPO_ACCONTO_ADD_COM))
				return true;
		}
		return false;
	}

	public Acconto_classific_coriBulk doCalcolaAccontoAddCom(
			it.cnr.jada.UserContext userContext, Acconto_classific_coriBulk bulk)
			throws it.cnr.jada.comp.ComponentException {

		try {
			if (bulk instanceof Acconto_classific_coriBulk) {
				callCalcolaAccontoAddCom(userContext,
						(Acconto_classific_coriBulk) bulk);
			}
		} catch (Throwable t) {
			throw handleException(t);
		}

		return bulk;

	}

	public Acconto_classific_coriBulk callCalcolaAccontoAddCom(
			UserContext userContext, Acconto_classific_coriBulk acconto)
			throws it.cnr.jada.comp.ComponentException {

		LoggableStatement cs = null;
		try {
			try {
				cs = new LoggableStatement(getConnection(userContext), "{call "
						+ it.cnr.jada.util.ejb.EJBCommonServices
								.getDefaultSchema()
						+ "CNRCTB553.calcolaAddTerritorioAcconto(?,?,?) }",
						false, this.getClass());
				cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext
						.getEsercizio(userContext)); // ESERCIZIO
				cs.setBigDecimal(2, acconto.getPercentuale()); // PERCENTUALE
				cs.setString(3, userContext.getUser()); // USER
				cs.executeQuery();

			} catch (java.sql.SQLException e) {
				throw handleException(e);
			} finally {
				if (cs != null)
					cs.close();
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}
		return acconto;
	}

	public java.util.List findListaCompensiSIP(UserContext userContext,
			String query, String dominio, String uo, String terzo, String voce,
			String cdr, String gae, String tipoRicerca, Timestamp data_inizio,
			Timestamp data_fine) throws ComponentException {
		try {

			VCompensoSIPHome home = (VCompensoSIPHome) getHome(userContext,
					VCompensoSIPBulk.class, "VCOMPENSOSIP_RID");
			SQLBuilder sql = home.createSQLBuilder();
			sql.setDistinctClause(true);
			if (data_inizio != null && data_fine != null) {
				sql.addSQLClause("AND", "DT_PAGAMENTO",
						SQLBuilder.GREATER_EQUALS, data_inizio);
				sql.addSQLClause("AND", "DT_PAGAMENTO", SQLBuilder.LESS_EQUALS,
						data_fine);
			}
			if (uo != null)
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA",
						SQLBuilder.EQUALS, uo);
			if (terzo != null)
				sql.addSQLClause("AND", "CD_TERZO", SQLBuilder.EQUALS, terzo);
			if (voce != null)
				sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS,
						voce);
			if (cdr != null)
				sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA",
						SQLBuilder.EQUALS, cdr);
			if (gae != null)
				sql.addSQLClause("AND", "GAE", SQLBuilder.EQUALS, gae);
			if (dominio.equalsIgnoreCase("pg_compenso"))
				sql
						.addSQLClause("AND", "PG_COMPENSO", SQLBuilder.EQUALS,
								query);
			else if (dominio.equalsIgnoreCase("descrizione")) {
				for (StringTokenizer stringtokenizer = new StringTokenizer(
						query, " "); stringtokenizer.hasMoreElements();) {
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca
							.equalsIgnoreCase("selettiva"))
							|| tipoRicerca == null) {
						if (queryDetail.equalsIgnoreCase(RemoveAccent
								.convert(queryDetail)))
							sql.addSQLClause("AND", "DS_COMPENSO",
									SQLBuilder.CONTAINS, queryDetail);
						else {
							sql.openParenthesis("AND");
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.CONTAINS, queryDetail);
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.CONTAINS, RemoveAccent
											.convert(queryDetail));
							sql.closeParenthesis();
						}
					} else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
						if (queryDetail.equalsIgnoreCase(RemoveAccent
								.convert(queryDetail))) {
							sql.openParenthesis("AND");
							sql.addSQLClause("AND", "UPPER(DS_COMPENSO)",
									SQLBuilder.EQUALS, queryDetail
											.toUpperCase());
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.STARTSWITH, queryDetail + " ");
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.ENDSWITH, " " + queryDetail);
							sql.closeParenthesis();
						} else {
							sql.openParenthesis("AND");
							sql.openParenthesis("AND");
							sql.addSQLClause("OR", "UPPER(DS_COMPENSO)",
									SQLBuilder.EQUALS, queryDetail
											.toUpperCase());
							sql.addSQLClause("OR", "UPPER(DS_COMPENSO)",
									SQLBuilder.EQUALS, RemoveAccent.convert(
											queryDetail).toUpperCase());
							sql.closeParenthesis();
							sql.openParenthesis("OR");
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.STARTSWITH, queryDetail + " ");
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.STARTSWITH, RemoveAccent
											.convert(queryDetail)
											+ " ");
							sql.closeParenthesis();
							sql.openParenthesis("OR");
							sql.addSQLClause("OR", "DS_COMPENSO",
									SQLBuilder.ENDSWITH, " " + queryDetail);
							sql
									.addSQLClause(
											"OR",
											"DS_COMPENSO",
											SQLBuilder.ENDSWITH,
											" "
													+ RemoveAccent
															.convert(queryDetail));
							sql.closeParenthesis();
							sql.closeParenthesis();
						}
					}
				}
				sql.addOrderBy("DS_COMPENSO");
			}
			return home.fetchAll(sql);
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}

	public boolean existARowToBeInventoried(UserContext context,
			CompensoBulk compenso) throws ComponentException {
		if (compenso.getObbligazioneScadenzario() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce().getFl_inv_beni_patr()
						.booleanValue()
				&& !compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce().getFl_inv_beni_comp()
						.booleanValue())
			throw new ApplicationException(
					"Il titolo capitolo selezionato non è utilizzabile dai Compensi! ");
		else if (compenso.getObbligazioneScadenzario() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce().getFl_inv_beni_comp()
						.booleanValue()
				&& compenso.hasCompetenzaCOGEInAnnoPrecedente())
			throw new ApplicationException(
					"Non è possibile utilizzare questo titolo capitolo con queste date competenza! ");
		else if (compenso.getObbligazioneScadenzario() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce() != null
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce().getFl_inv_beni_patr()
						.booleanValue()
				&& compenso.getObbligazioneScadenzario().getObbligazione()
						.getElemento_voce().getFl_inv_beni_comp()
						.booleanValue()) {
			return true;
		}
		return false;
	}

	public boolean isSospensioneIrpefOkPerContabil(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		Timestamp data_da = null;
		Timestamp data_a = null;
		try {
			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");

			if (sess.getDt01(userContext, null, null, "SOSPENSIONE_IRPEF",
					"PERIODO_VALIDITA") == null
					|| sess.getDt02(userContext, null, null,
							"SOSPENSIONE_IRPEF", "PERIODO_VALIDITA") == null)

				throw new ApplicationException(
						"Configurazione CNR: non sono stati impostati i valori per la gestione della Sospensione IRPEF");

			data_da = sess.getDt01(userContext, null, null,
					"SOSPENSIONE_IRPEF", "PERIODO_VALIDITA");
			data_a = sess.getDt02(userContext, null, null, "SOSPENSIONE_IRPEF",
					"PERIODO_VALIDITA");

			java.sql.Timestamp data_emis_mand = it.cnr.jada.util.ejb.EJBCommonServices
					.getServerDate();

			// Se data_emis_mand è compreso nel periodo di validità e se il
			// terzo ed il cori la prevedono
			// deve essere calcolata la sospensione, altrimenti no
			if (compenso.getCd_terzo() != null) {
				try {
					TerzoHome tHome = (TerzoHome) getHome(userContext,
							TerzoBulk.class);
					TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo());
					TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);
					if (t == null) {
						return false;
					} else {
						AnagraficoHome aHome = (AnagraficoHome) getHome(
								userContext, AnagraficoBulk.class);
						AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
						AnagraficoBulk a = (AnagraficoBulk) aHome
								.findByPrimaryKey(aKey);

						if (a == null)
							return false;
						else {
							if (a.getFl_sospensione_irpef()
									&& isPrevistaSospensioneSuTrattCori(compenso)
									&& data_emis_mand.compareTo(data_da) >= 0
									&& data_emis_mand.compareTo(data_a) <= 0) {
								if (isCalcolataSospensione(compenso))
									return true;
								else
									return false;
							} else {
								if (isNonCalcolataSospensione(compenso))
									return true;
								else
									return false;
							}
						}
					}
				} catch (it.cnr.jada.persistency.PersistencyException ex) {
					throw handleException(compenso, ex);
				}
			} else
				return false;

		} catch (javax.ejb.EJBException e) {
			throw handleException(compenso, e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(compenso, e);
		}
	}

	public boolean isPrevistaSospensioneSuTrattCori(CompensoBulk compenso) {
		java.util.Collection collCori = compenso.getContributi();
		for (java.util.Iterator i = collCori.iterator(); i.hasNext();) {
			Contributo_ritenutaBulk cr = (Contributo_ritenutaBulk) i.next();
			if (cr.getTipoContributoRitenuta().getFl_sospensione_irpef())
				return true;
		}
		return false;
	}

	public boolean isCalcolataSospensione(CompensoBulk compenso) {
		java.util.Collection collCori = compenso.getContributi();
		for (java.util.Iterator i = collCori.iterator(); i.hasNext();) {
			Contributo_ritenutaBulk cr = (Contributo_ritenutaBulk) i.next();
			if (cr.getTipoContributoRitenuta().getFl_sospensione_irpef()) {
				if (cr.getIm_cori_sospeso() == null)
					return false;
				if (cr.getIm_cori_sospeso().compareTo(new BigDecimal(0)) != 0)
					return true;
				if (cr.getAmmontare().compareTo(new BigDecimal(0)) == 0)
					return true;
				if (cr.getAmmontare().compareTo(new BigDecimal(0)) < 0)
					return true;
			}
		}
		return false;
	}

	public boolean isNonCalcolataSospensione(CompensoBulk compenso) {
		java.util.Collection collCori = compenso.getContributi();
		for (java.util.Iterator i = collCori.iterator(); i.hasNext();) {
			Contributo_ritenutaBulk cr = (Contributo_ritenutaBulk) i.next();
			if (cr.getTipoContributoRitenuta().getFl_sospensione_irpef()) {
				if (cr.getIm_cori_sospeso() == null)
					return true;
				if (cr.getIm_cori_sospeso().compareTo(new BigDecimal(0)) != 0
						&& cr.getAmmontare().compareTo(new BigDecimal(0)) == 0)
					return true;
				if (cr.getIm_cori_sospeso().compareTo(new BigDecimal(0)) != 0)
					return false;
			}
		}
		return true;
	}
	
	public void archiviaStampa(UserContext userContext, Date fromDate, Date untilDate, CompensoBulk compensoBulk, Integer... years) throws ComponentException{
		CompensoHome compensoHome = (CompensoHome) getHome(userContext, CompensoBulk.class);
		CompoundFindClause clauses = new CompoundFindClause();
		CompoundFindClause clausesYear = new CompoundFindClause();
		if (fromDate != null)
			clauses.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.GREATER_EQUALS, new Timestamp(fromDate.getTime()));
		if (untilDate != null)
			clauses.addClause(FindClause.AND, "dt_registrazione", SQLBuilder.LESS_EQUALS, new Timestamp(untilDate.getTime()));
		for (Integer year : years) {
			clausesYear.addClause(FindClause.OR, "esercizio", SQLBuilder.EQUALS, year);
		}
		clauses = CompoundFindClause.and(clauses, clausesYear);
		try {
			RemoteIterator missioni = cerca(userContext, clauses, compensoBulk);
			while(missioni.hasMoreElements()){
				CompensoBulk compenso = (CompensoBulk) missioni.nextElement();
				try{
					compenso = (CompensoBulk) inizializzaBulkPerModifica(userContext,
							compenso);
					compensoHome.archiviaStampa(userContext, compenso);
				}catch(Exception ex){
					System.err.println("Compenso:"+compenso + " - "+ex);
					continue;
				}
			}
		}catch (RemoteException e) {
			throw handleException(e);
		}
	}
	public SQLBuilder selectQuadri_770ByClause(UserContext userContext,
			Estrazione770Bulk estraz, Quadri_770Bulk quadri,
			CompoundFindClause clauses) throws ComponentException {

		Quadri_770Home home = (Quadri_770Home) getHome(userContext, quadri);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(clauses);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		
		return sql;
	}	
/*	
	private boolean isGestitePrestazioni(UserContext userContext) throws ComponentException {
		try {
			String attivaPrestazione = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(userContext, CNRUserContext.getEsercizio(userContext), "*", "GESTIONE_COMPENSI", "ATTIVA_PRESTAZIONE");
			if (attivaPrestazione.compareTo(new String("Y"))==0)
			    return true;
			else
				return false;
			
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	*/
	private void loadPignorato(UserContext userContext,
			CompensoBulk compenso) throws ComponentException {
		try {  
			if((compenso.getTipoTrattamento()!=null && compenso.getTipoTrattamento().getFl_pignorato_obbl())||compenso.getCd_terzo_pignorato()!=null)
			{	
	             compenso.setVisualizzaPignorato(true);
	             if (compenso.getCd_terzo_pignorato()!=null)
	             {
	            	 TerzoHome tHome = (TerzoHome) getHome(userContext,TerzoBulk.class);
						 TerzoBulk tKey = new TerzoBulk(compenso.getCd_terzo_pignorato());
						 TerzoBulk t = (TerzoBulk) tHome.findByPrimaryKey(tKey);
	            	 compenso.setPignorato(t);
	             }
			}
		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException(ex);
		}
	}
	
	private boolean isGestitePrestazioni(UserContext userContext) throws ComponentException {
		try {
			String attivaPrestazione = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(userContext, CNRUserContext.getEsercizio(userContext), "*", "GESTIONE_COMPENSI", "ATTIVA_PRESTAZIONE");
			if (attivaPrestazione.compareTo(new String("Y"))==0)
			    return true;
			else
				return false;
			
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private CompensoBulk ricercaCompensoTrovato(
			UserContext userContext,
			Long esercizio,
			String cd_cds,
			String cd_unita_organizzativa,
			Long pg_compenso,
			boolean byKey) throws PersistencyException, ComponentException {
		CompensoBulk compenso = new CompensoBulk();
		compenso.setEsercizio(esercizio.intValue());
		if (byKey) {
			compenso.setCd_cds(cd_cds);
			compenso.setCd_unita_organizzativa(cd_unita_organizzativa);
		} else {
			compenso.setCd_cds_origine(cd_cds);
			compenso.setCd_uo_origine(cd_unita_organizzativa);
		}
		compenso.setPg_compenso(pg_compenso);

		List compensi = (getHome(userContext, CompensoBulk.class).find(compenso));
		if (compensi.size()==0)
			compenso=null;
		else if (compensi.size()==1){
			compenso=(CompensoBulk)compensi.get(0);
			recuperoInfoAggiuntiveCompensoPerBrevetto(userContext, compenso);
		}
		else  //non dovrebbe capitare mai!
			throw new FatturaNonTrovataException("Compenso non trovato!");

		if (compenso!=null)
			return compenso;
		else 
			throw new FatturaNonTrovataException("Compenso non trovato!");
	}

	public CompensoBulk ricercaCompensoTrovato(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException{
		return ricercaCompensoTrovato(userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso, false);
	}

	public CompensoBulk ricercaCompensoByKey(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException{
		return ricercaCompensoTrovato(userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso, true);
	}

	public List<CompensoBulk> ricercaCompensiTrovato(
			UserContext userContext,
			Long trovato) throws PersistencyException, ComponentException {
		CompensoBulk compenso = new CompensoBulk();
		compenso.setPg_trovato(trovato);
		List compensiList =(getHome(userContext, CompensoBulk.class).find(compenso));
		for (Iterator<CompensoBulk> i = compensiList.iterator(); i.hasNext(); ) {
			CompensoBulk comp = (CompensoBulk) i.next();
//			List listaDocContAssociati = loadDocContAssociati(userContext, comp);
//			if (listaDocContAssociati!=null && !listaDocContAssociati.isEmpty()) {
//				for (Iterator<V_doc_cont_compBulk> iterator = listaDocContAssociati.iterator(); iterator.hasNext(); ) {
//					V_doc_cont_compBulk docContComp = (V_doc_cont_compBulk) iterator.next();
//
//					if (V_doc_cont_compBulk.TIPO_DOC_CONT_MANDATO.equals(docContComp.getTipo_doc_cont())){
						recuperoInfoAggiuntiveCompensoPerBrevetto(userContext, comp);
//					}
//				}
//			}
		}
		return compensiList;
	}

	private void recuperoInfoAggiuntiveCompensoPerBrevetto(
			UserContext userContext, CompensoBulk comp)
			throws ComponentException, PersistencyException {
		List bl = new ArrayList();
		SQLBuilder sql = getHome( userContext, Mandato_rigaIBulk.class ).createSQLBuilder();
		sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, comp.getCd_cds() );
		sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, comp.getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, comp.getEsercizio() );
		sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_COMPENSO);
		sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, comp.getPg_compenso() );
		sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS, Mandato_rigaBulk.STATO_ANNULLATO);
		List result = getHome( userContext, Mandato_rigaIBulk.class ).fetchAll( sql );
		for (Iterator k = result.iterator(); k.hasNext(); ) {
			Mandato_rigaIBulk manr = (Mandato_rigaIBulk)k.next();
			manr.setMandato((MandatoIBulk)getHome(userContext, MandatoIBulk.class).findByPrimaryKey(manr.getMandato()));
			bl.add(manr);
		}
		comp.setMandatiRigaAssociati(bl);
	}
	public Tipo_rapportoBulk getTipoRapportoProf(UserContext userContext)
			throws ComponentException {
		try {
			Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",
							Parametri_cnrComponentSession.class))
					.getParametriCnr(userContext, CNRUserContext
							.getEsercizio(userContext));
			return par.getTipo_rapporto_prof();
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	/*
	public AnagraficoBulk getAnagraficoEnte(UserContext userContext)
			throws ComponentException {
		try {
			AnagraficoBulk anag = ((AnagraficoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession",
							AnagraficoComponentSession.class))
					.getAnagraficoEnte(userContext);
			return anag;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	*/
	private Timestamp getDataInizioFatturazioneElettronica(UserContext userContext) throws ComponentException {

		try {

			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
			return sess.getDt01(userContext, it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(userContext), null,
					Configurazione_cnrBulk.PK_FATTURAZIONE_ELETTRONICA, Configurazione_cnrBulk.SK_PASSIVA);

		} catch (javax.ejb.EJBException ex) {
			throw handleException(ex);
		} catch (RemoteException ex) {
			throw handleException(ex);
		}

	}
public CompensoBulk valorizzaInfoDocEle(UserContext userContext, CompensoBulk comp)
			throws ComponentException {
		try {
			comp.setDataInizioFatturaElettronica(getDataInizioFatturazioneElettronica(userContext));  
//			if(comp.getDt_registrazione() != null && comp.getDataInizioFatturaElettronica() != null)
//			{
//				if ((comp.getDt_registrazione().compareTo(comp.getDataInizioFatturaElettronica())<0))
//					comp.setGestione_doc_ele(false);
//				else
//					comp.setGestione_doc_ele(true);
//			}
//			else comp.setGestione_doc_ele(true);  //non dovrebbe mai verificarsi
			return comp;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

public SQLBuilder selectCigByClause (UserContext userContext, CompensoBulk compenso, CigBulk cig, CompoundFindClause clause)	throws ComponentException, PersistencyException
{
	if (clause == null) 
	  clause = cig.buildFindClauses(null);
	SQLBuilder sql = getHome(userContext, cig).createSQLBuilder();

	sql.addSQLClause(FindClause.AND, "FL_VALIDO", SQLBuilder.EQUALS, "Y");
	Set<String> uoAbilitate = new HashSet<String>();
	uoAbilitate.add(compenso.getCd_unita_organizzativa());
	if (compenso.getObbligazioneScadenzario() != null){
		ObbligazioneBulk obbligazione = null;
	      try {
	    	  obbligazione = (ObbligazioneBulk)getHome(userContext, ObbligazioneBulk.class).findByPrimaryKey(compenso.getObbligazioneScadenzario().getObbligazione());
		} catch (PersistencyException e1) {
			throw new ComponentException(e1);
		}
		if (obbligazione != null && obbligazione.getContratto() != null){
			ContrattoBulk contratto = obbligazione.getContratto();
			try {
		    	  contratto = (ContrattoBulk)getHome(userContext, ContrattoBulk.class).findByPrimaryKey(contratto);
		    	  uoAbilitate.add(contratto.getCd_unita_organizzativa());
			} catch (PersistencyException e1) {
				throw new ComponentException(e1);
			}
	        ContrattoHome contrattoHome = (ContrattoHome) getHome(userContext, contratto.getClass());
	        try {
	        	Collection assUo = contrattoHome.findAssociazioneUO(obbligazione.getContratto());
	        	if (assUo != null){
	        		for (Iterator i = assUo.iterator(); i.hasNext(); ) {
	        			Ass_contratto_uoBulk ass = (Ass_contratto_uoBulk) i.next();
	        			uoAbilitate.add(ass.getCd_unita_organizzativa());
	        		}
	        	}
	        } catch (Exception e1) {
	        	throw new ComponentException(e1);
	        }
		}
	}
	if (uoAbilitate.size() > 1){
		sql.openParenthesis(FindClause.AND);
		boolean first = true;
		for (Iterator i = uoAbilitate.iterator(); i.hasNext(); ) {
			String uo = (String) i.next();
			sql.addSQLClause(first ? FindClause.AND : FindClause.OR, "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
			first = false;
		}
		sql.closeParenthesis();
	} else {
		sql.addSQLClause(FindClause.AND, "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoAbilitate.iterator().next());
	}
	if (clause != null) 
	  sql.addClause(clause);
    sql.addOrderBy("cd_Unita_Organizzativa, cd_Cig");
	return sql;
}
}
