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

package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.progettiric00.comp.RimodulazioneNonApprovataException;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Progetto_rimodulazioneHome extends BulkHome {
	public Progetto_rimodulazioneHome(java.sql.Connection conn) {
		super(Progetto_rimodulazioneBulk.class,conn);
	}
	
	public Progetto_rimodulazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_rimodulazioneBulk.class,conn,persistentCache);
	}
	
	public java.util.Collection<Progetto_rimodulazione_ppeBulk> findDettagliRimodulazione(Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		Progetto_rimodulazione_ppeHome dettHome = (Progetto_rimodulazione_ppeHome)getHomeCache().getHome(Progetto_rimodulazione_ppeBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Progetto_rimodulazione_voceBulk> findDettagliVoceRimodulazione(Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		Progetto_rimodulazione_voceHome dettHome = (Progetto_rimodulazione_voceHome)getHomeCache().getHome(Progetto_rimodulazione_voceBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Pdg_variazioneBulk> findVariazioniCompetenzaAssociate(Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Pdg_variazioneHome dettHome = (Pdg_variazioneHome)getHomeCache().getHome(Pdg_variazioneBulk.class,"VP_PDG_VARIAZIONE");
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Var_stanz_resBulk> findVariazioniResidueAssociate(Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Var_stanz_resHome dettHome = (Var_stanz_resHome)getHomeCache().getHome(Var_stanz_resBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	/**
	 * Ritorna le variazioni da creare sulla base della rimodulazione indicata
	 * L'oggetto rimodulazione deve risultare costruito nella forma utilizzata dalla mappa di Rimodulazione
	 */
	public List<OggettoBulk> constructVariazioniBilancio(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		List<OggettoBulk> result = new ArrayList<OggettoBulk>();
		Configurazione_cnrHome configurazioneCnrHome = (Configurazione_cnrHome)getHomeCache().getHome(Configurazione_cnrBulk.class);
		Configurazione_cnrBulk configCnr = configurazioneCnrHome.getConfigurazioneCnrBulk(new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
		BigDecimal annoFrom = Optional.ofNullable(configCnr).map(Configurazione_cnrBulk::getIm01).orElse(null);

		Map<Integer, List<Progetto_piano_economicoBulk>> mapEsercizio = 
				rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
						     .filter(el->el.getEsercizio_piano().compareTo(annoFrom.intValue())>=0)
							 .filter(Progetto_piano_economicoBulk::isDetailRimodulato)
							 .filter(el->el.getDispResiduaFinanziamentoRimodulato().compareTo(BigDecimal.ZERO)<0 ||
							  		     el.getDispResiduaCofinanziamentoRimodulato().compareTo(BigDecimal.ZERO)<0)
						     .collect(Collectors.groupingBy(Progetto_piano_economicoBulk::getEsercizio_piano));
		
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHomeCache().getHome(Unita_organizzativaBulk.class);
		CdrBulk cdr = uoHome.findCdrResponsbileUo(rimodulazione.getProgetto().getUnita_organizzativa());
		
		for (Integer esercizio : mapEsercizio.keySet()) {
			Map<String, BigDecimal> newMapFonti = new HashMap<>();
			newMapFonti.put("FIN", mapEsercizio.get(esercizio).stream().map(Progetto_piano_economicoBulk::getDispResiduaCofinanziamentoRimodulato).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
			newMapFonti.put("FES", mapEsercizio.get(esercizio).stream().map(Progetto_piano_economicoBulk::getDispResiduaFinanziamentoRimodulato).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
			newMapFonti.forEach((fonte, imVariazione) -> {
				//creo la variazione solo per fonti esterne
				if (fonte.equals("FES")) {
					if (esercizio.compareTo(CNRUserContext.getEsercizio(userContext))<0){
						Var_stanz_resBulk varStanz = new Var_stanz_resBulk();
						varStanz.setEsercizio(CNRUserContext.getEsercizio(userContext));
						varStanz.setEsercizio_res(new Esercizio_baseBulk(esercizio));
						varStanz.setTipologia_fin(fonte);
						varStanz.setCdr(cdr);
						
						Ass_var_stanz_res_cdrBulk assCdr = new Ass_var_stanz_res_cdrBulk();
						assCdr.setCentro_di_responsabilita(cdr);
						assCdr.setIm_spesa(imVariazione);
						varStanz.addToAssociazioneCDR(assCdr);
					
						result.add(varStanz);
					} else if (esercizio.compareTo(CNRUserContext.getEsercizio(userContext))==0){
						Pdg_variazioneBulk pdgVar = new Pdg_variazioneBulk();
						pdgVar.setEsercizio(CNRUserContext.getEsercizio(userContext));
						pdgVar.setTipologia_fin(fonte);
						pdgVar.setCentro_responsabilita(cdr);
						
						Ass_pdg_variazione_cdrBulk assCdr = new Ass_pdg_variazione_cdrBulk();
						assCdr.setCentro_responsabilita(cdr);
						assCdr.setIm_entrata(BigDecimal.ZERO);
						assCdr.setIm_spesa(imVariazione);
						pdgVar.addToAssociazioneCDR(assCdr);

						result.add(pdgVar);
					}
				}
			});
		}
		
		rimodulazione.getVociMovimentateNonAssociate().stream().forEach(voce->{
			if (voce.getEsercizio().compareTo(CNRUserContext.getEsercizio(userContext))<0){
				Var_stanz_resBulk varStanz = new Var_stanz_resBulk();
				varStanz.setEsercizio(CNRUserContext.getEsercizio(userContext));
				varStanz.setEsercizio_res(new Esercizio_baseBulk(voce.getEsercizio()));
				varStanz.setTipologia_fin("FES");
				varStanz.setCdr(cdr);
				
				Ass_var_stanz_res_cdrBulk assCdr = new Ass_var_stanz_res_cdrBulk();
				assCdr.setCentro_di_responsabilita(cdr);
				assCdr.setIm_spesa(voce.getAssestato().negate());
				varStanz.addToAssociazioneCDR(assCdr);
			
				result.add(varStanz);
			} else if (voce.getEsercizio().compareTo(CNRUserContext.getEsercizio(userContext))==0){
				Pdg_variazioneBulk pdgVar = new Pdg_variazioneBulk();
				pdgVar.setEsercizio(CNRUserContext.getEsercizio(userContext));
				pdgVar.setTipologia_fin("FES");
				pdgVar.setCentro_responsabilita(cdr);
				
				Ass_pdg_variazione_cdrBulk assCdr = new Ass_pdg_variazione_cdrBulk();
				assCdr.setCentro_responsabilita(cdr);
				assCdr.setIm_entrata(BigDecimal.ZERO);
				assCdr.setIm_spesa(voce.getAssestato().negate());
				pdgVar.addToAssociazioneCDR(assCdr);

				result.add(pdgVar);
			}
		});

		return result;
	}
	
	/**
	 * Ritorna la lista dei dettagli Progetto_rimodulazione_ppeBulk da creare sulla rimodulazione 
	 * in base dello stato in cui si trova attualmente la rimodulazione. L'oggetto rimodulazione deve essere
	 * caricato con la modalità utilizzata nella mappa di gestione della Rimodulazione
	 * 
	 * @param userContext
	 * @param progettoRimodulazione
	 * @return List<Progetto_rimodulazione_ppeBulk>
	 */
	public List<Progetto_rimodulazione_ppeBulk> getDettagliRimodulazioneAggiornato(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk progettoRimodulazione) {
		return progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.filter(Progetto_piano_economicoBulk::isDetailRimodulato)
			.map(ppe->{
				Progetto_rimodulazione_ppeBulk newDetail = new Progetto_rimodulazione_ppeBulk();
				newDetail.setVocePianoEconomico(ppe.getVoce_piano_economico());
				newDetail.setEsercizio_piano(ppe.getEsercizio_piano());
				newDetail.setImVarEntrata(BigDecimal.ZERO);
				newDetail.setImVarSpesaFinanziato(Optional.ofNullable(ppe.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)
						.subtract(Optional.ofNullable(ppe.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)));
				newDetail.setImVarSpesaCofinanziato(Optional.ofNullable(ppe.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)
						.subtract(Optional.ofNullable(ppe.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)));
				newDetail.setImStoassSpesaFinanziato(Optional.ofNullable(ppe.getSaldoSpesa())
						.map(V_saldi_piano_econom_progettoBulk::getAssestatoFinanziamento).orElse(BigDecimal.ZERO));
				newDetail.setImStoassSpesaCofinanziato(Optional.ofNullable(ppe.getSaldoSpesa())
						.map(V_saldi_piano_econom_progettoBulk::getAssestatoCofinanziamento).orElse(BigDecimal.ZERO));
				return newDetail;
			}).collect(Collectors.toList());
	}

	/**
	 * Ritorna la lista dei dettagli Progetto_rimodulazione_voceBulk da creare sulla rimodulazione 
	 * in base dello stato in cui si trova attualmente la rimodulazione. L'oggetto rimodulazione deve essere
	 * caricato con la modalità utilizzata nella mappa di gestione della Rimodulazione
	 * 
	 * @param userContext
	 * @param progettoRimodulazione
	 * @return List<Progetto_rimodulazione_voceBulk>
	 */
	public List<Progetto_rimodulazione_voceBulk> getDettagliRimodulazioneVoceAggiornato(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk progettoRimodulazione) {
		return progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
			.filter(el->Optional.ofNullable(el.getElemento_voce()).flatMap(el1->Optional.ofNullable(el.getCd_elemento_voce())).isPresent())
			.filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulato)
			.map(ppeVoce->{
				Progetto_rimodulazione_voceBulk newRimVoce = new Progetto_rimodulazione_voceBulk();
				newRimVoce.setVocePianoEconomico(ppeVoce.getProgetto_piano_economico().getVoce_piano_economico());
				newRimVoce.setEsercizio_piano(ppeVoce.getEsercizio_piano());
				newRimVoce.setElementoVoce(ppeVoce.getElemento_voce());
				newRimVoce.setImVarSpesaFinanziato(BigDecimal.ZERO);
				newRimVoce.setImVarSpesaCofinanziato(BigDecimal.ZERO);

				if (ppeVoce.isDetailRimodulatoEliminato()) {
					newRimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_ELIMINATO);
					newRimVoce.setImVarSpesaFinanziato(BigDecimal.ZERO);
					newRimVoce.setImVarSpesaCofinanziato(BigDecimal.ZERO);
				} else {
					newRimVoce.setImVarSpesaFinanziato(Optional.ofNullable(ppeVoce.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO));
					newRimVoce.setImVarSpesaCofinanziato(Optional.ofNullable(ppeVoce.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO));
					if (ppeVoce.isDetailRimodulatoAggiunto())
						newRimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_AGGIUNTO);
					else 
						newRimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_MODIFICA);
				}							
				return newRimVoce;
			}).collect(Collectors.toList());
	}
	
	public Progetto_rimodulazioneBulk rebuildRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		Progetto_rimodulazioneBulk newRimodulazione = initializeRimodulazione(userContext, rimodulazione);

		if (newRimodulazione.getProgetto()==null) 
			return newRimodulazione;
		else if (newRimodulazione.isStatoApprovato()||newRimodulazione.isStatoRespinto())
			return rebuildRimodulazioneChiusa(userContext, newRimodulazione);
		else
			return rebuildRimodulazioneInCorso(userContext, newRimodulazione);
	}
	
	private Progetto_rimodulazioneBulk initializeRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		if (rimodulazione.getProgetto()==null) 
			return rimodulazione;

		ProgettoHome testataHome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class);
		final ProgettoBulk progetto = testataHome.initializePianoEconomico(userContext, rimodulazione.getProgetto(), true);

		if (Optional.ofNullable(rimodulazione).flatMap(el->Optional.ofNullable(el.getPg_rimodulazione())).isPresent()) {
			rimodulazione.setDettagliRimodulazione(new BulkList<Progetto_rimodulazione_ppeBulk>(this.findDettagliRimodulazione(rimodulazione)));
			rimodulazione.setDettagliVoceRimodulazione(new BulkList<Progetto_rimodulazione_voceBulk>(this.findDettagliVoceRimodulazione(rimodulazione)));
		}

		getHomeCache().fetchAll(userContext);

		rimodulazione.setProgetto(progetto);
		rimodulazione.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setImFinanziatoRimodulato(progetto.getImFinanziato());
		rimodulazione.setImCofinanziatoRimodulato(progetto.getImCofinanziato());
		rimodulazione.setDtInizioRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtInizio).orElse(null));
		rimodulazione.setDtFineRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtFine).orElse(null));
		rimodulazione.setDtProrogaRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtProroga).orElse(null));
		
		progetto.getDettagliPianoEconomicoTotale().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
			});
		
		progetto.getDettagliPianoEconomicoAnnoCorrente().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
			});

		progetto.getDettagliPianoEconomicoAltriAnni().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);
			});
		return rimodulazione;
	}

	private Progetto_rimodulazioneBulk rebuildRimodulazioneInCorso(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		final ProgettoBulk progetto = rimodulazione.getProgetto();
		
		Optional.ofNullable(rimodulazione.getImVarFinanziato()).ifPresent(el->rimodulazione.setImFinanziatoRimodulato(rimodulazione.getImFinanziatoRimodulato().add(el)));
		Optional.ofNullable(rimodulazione.getImVarCofinanziato()).ifPresent(el->rimodulazione.setImCofinanziatoRimodulato(rimodulazione.getImCofinanziatoRimodulato().add(el)));
		Optional.ofNullable(rimodulazione.getDtInizio()).ifPresent(el->rimodulazione.setDtInizioRimodulato(el));
		Optional.ofNullable(rimodulazione.getDtFine()).ifPresent(el->rimodulazione.setDtFineRimodulato(el));
		Optional.ofNullable(rimodulazione.getDtProroga()).ifPresent(el->rimodulazione.setDtProrogaRimodulato(el));

		//Aggiorno i dettagli presenti
		rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.forEach(el->{
				Progetto_rimodulazione_ppeBulk dett = 
						rimodulazione.getDettagliRimodulazione().stream()
							.filter(dettRim->dettRim.getPg_progetto().equals(el.getPg_progetto()))
							.filter(dettRim->dettRim.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
							.filter(dettRim->dettRim.getCd_voce_piano().equals(el.getCd_voce_piano()))
							.filter(dettRim->dettRim.getEsercizio_piano().equals(el.getEsercizio_piano()))
							.findAny().orElse(null);
				el.setImSpesaFinanziatoRimodulato(el.getIm_spesa_finanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaFinanziato).orElse(BigDecimal.ZERO)));
				el.setImSpesaCofinanziatoRimodulato(el.getIm_spesa_cofinanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaCofinanziato).orElse(BigDecimal.ZERO)));
			});

		//Aggiungo i dettagli nuovi
		rimodulazione.getDettagliRimodulazione().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk dett = 
						rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
							.filter(dettPpe->dettPpe.getPg_progetto().equals(el.getPg_progetto()))
							.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
							.filter(dettPpe->dettPpe.getCd_voce_piano().equals(el.getCd_voce_piano()))
							.filter(dettPpe->dettPpe.getEsercizio_piano().equals(el.getEsercizio_piano()))
							.findAny().orElse(null);
				if (!Optional.ofNullable(dett).isPresent()) {
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setProgetto(rimodulazione.getProgetto());
					ppe.setVoce_piano_economico(el.getVocePianoEconomico());
					ppe.setEsercizio_piano(el.getEsercizio_piano());
					ppe.setIm_entrata(el.getImVarEntrata());
					ppe.setIm_spesa_finanziato(BigDecimal.ZERO);
					ppe.setIm_spesa_cofinanziato(BigDecimal.ZERO);
					ppe.setImSpesaFinanziatoRimodulato(el.getImVarSpesaFinanziato());
					ppe.setImSpesaCofinanziatoRimodulato(el.getImVarSpesaCofinanziato());
					
					if (ppe.getEsercizio_piano().equals(0))
						rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
					else if (ppe.getEsercizio_piano().equals(rimodulazione.getProgetto().getEsercizio()))
						rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
					else
						rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);						
				}
			});

		//Aggiorno le voci di bilancio presenti
		rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
			.forEach(ppeVoce->{
				ppeVoce.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
				ppeVoce.setImVarCofinanziatoRimodulato(BigDecimal.ZERO);
				Progetto_rimodulazione_voceBulk rimVoce = 
						rimodulazione.getDettagliVoceRimodulazione().stream()
							.filter(dettVoceRim->dettVoceRim.getPg_progetto().equals(ppeVoce.getPg_progetto()))
							.filter(dettVoceRim->dettVoceRim.getCd_unita_organizzativa().equals(ppeVoce.getCd_unita_organizzativa()))
							.filter(dettVoceRim->dettVoceRim.getCd_voce_piano().equals(ppeVoce.getCd_voce_piano()))
							.filter(dettVoceRim->dettVoceRim.getEsercizio_piano().equals(ppeVoce.getEsercizio_piano()))
							.filter(dettVoceRim->dettVoceRim.getEsercizio_voce().equals(ppeVoce.getEsercizio_voce()))
							.filter(dettVoceRim->dettVoceRim.getTi_appartenenza().equals(ppeVoce.getTi_appartenenza()))
							.filter(dettVoceRim->dettVoceRim.getTi_gestione().equals(ppeVoce.getTi_gestione()))
							.filter(dettVoceRim->dettVoceRim.getCd_elemento_voce().equals(ppeVoce.getCd_elemento_voce()))
							.findAny().orElse(null);
				if (Optional.ofNullable(rimVoce).isPresent()) {
					ppeVoce.setDetailRimodulatoAggiunto(rimVoce.isTiOperazioneAggiunto());
					ppeVoce.setDetailRimodulatoEliminato(rimVoce.isTiOperazioneEliminato());
					ppeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
					ppeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());
				}
			});

		//Aggiungo i dettagli nuovi
		rimodulazione.getDettagliVoceRimodulazione().stream()
			.forEach(rimVoce->{
				Ass_progetto_piaeco_voceBulk ppeVoce = 
						rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
							.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
							.filter(dettVocePpe->dettVocePpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
							.filter(dettVocePpe->dettVocePpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
							.filter(dettVocePpe->dettVocePpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
							.filter(dettVocePpe->dettVocePpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
							.filter(dettVocePpe->dettVocePpe.getEsercizio_voce().equals(rimVoce.getEsercizio_voce()))
							.filter(dettVocePpe->dettVocePpe.getTi_appartenenza().equals(rimVoce.getTi_appartenenza()))
							.filter(dettVocePpe->dettVocePpe.getTi_gestione().equals(rimVoce.getTi_gestione()))
							.filter(dettVocePpe->dettVocePpe.getCd_elemento_voce().equals(rimVoce.getCd_elemento_voce()))
							.findAny().orElse(null);
				if (!Optional.ofNullable(ppeVoce).isPresent()) {
					Progetto_piano_economicoBulk ppe = 
							rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
								.filter(dettPpe->dettPpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
								.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
								.filter(dettPpe->dettPpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
								.filter(dettPpe->dettPpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
								.findAny().orElse(null);
					if (Optional.ofNullable(ppe).isPresent()) {
						Ass_progetto_piaeco_voceBulk newPpeVoce = new Ass_progetto_piaeco_voceBulk();
						newPpeVoce.setElemento_voce(rimVoce.getElementoVoce());
						newPpeVoce.setDetailRimodulatoAggiunto(Boolean.TRUE);
						newPpeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
						newPpeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());
						newPpeVoce.initializeSaldo(progetto.getVociBilancioMovimentate());
						ppe.addToVociBilancioAssociate(newPpeVoce);
					}
				}
			});
		
		if (rimodulazione.isStatoDefinitivo() || rimodulazione.isStatoValidato()) {
			Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHomeCache().getHome(Progetto_rimodulazioneBulk.class);
	       	rimodulazione.setVariazioniModels(new BulkList<>(rimodHome.constructVariazioniBilancio(userContext, rimodulazione)));
		}
		return rimodulazione;
	}

	private Progetto_rimodulazioneBulk rebuildRimodulazioneChiusa(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException {
		ProgettoBulk progetto = rimodulazione.getProgetto();
		ProgettoHome prgHome = (ProgettoHome)getHomeCache().getHome(ProgettoBulk.class);
		List<Progetto_rimodulazioneBulk> listRimodulazioni = prgHome.findRimodulazioni(rimodulazione.getProgetto().getPg_progetto());
		listRimodulazioni.stream()
				.filter(el->el.isStatoApprovato())
				.filter(el->el.getPg_rimodulazione().compareTo(rimodulazione.getPg_rimodulazione())>=0)
				.sorted(Comparator.comparing(Progetto_rimodulazioneBulk::getPg_rimodulazione).reversed())
				.forEachOrdered(currRim->{
					Optional.ofNullable(currRim.getImVarFinanziato()).ifPresent(el->progetto.getOtherField().setImFinanziato(progetto.getOtherField().getImFinanziato().subtract(el)));
					Optional.ofNullable(currRim.getImVarCofinanziato()).ifPresent(el->progetto.getOtherField().setImCofinanziato(progetto.getOtherField().getImCofinanziato().subtract(el)));
					Optional.ofNullable(currRim.getDtInizioOld()).ifPresent(el->progetto.getOtherField().setDtInizio(el));
					Optional.ofNullable(currRim.getDtFineOld()).ifPresent(el->progetto.getOtherField().setDtFine(el));
				});

		rimodulazione.setProgetto(progetto);
		rimodulazione.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
		rimodulazione.setImFinanziatoRimodulato(progetto.getImFinanziato().add(rimodulazione.getImVarFinanziato()));
		rimodulazione.setImCofinanziatoRimodulato(progetto.getImCofinanziato().add(rimodulazione.getImVarCofinanziato()));
		rimodulazione.setDtInizioRimodulato(Optional.ofNullable(rimodulazione.getDtInizio()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtInizio).orElse(null)));
		rimodulazione.setDtFineRimodulato(Optional.ofNullable(rimodulazione.getDtFine()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtFine).orElse(null)));
		rimodulazione.setDtProrogaRimodulato(Optional.ofNullable(rimodulazione.getDtProroga()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtProroga).orElse(null)));
		
		progetto.getDettagliPianoEconomicoTotale().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
			});
		
		progetto.getDettagliPianoEconomicoAnnoCorrente().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
			});

		progetto.getDettagliPianoEconomicoAltriAnni().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
				ppe.setDetailDerivato(Boolean.TRUE);
				ppe.setProgetto(el.getProgetto());
				ppe.setVoce_piano_economico(el.getVoce_piano_economico());
				ppe.setEsercizio_piano(el.getEsercizio_piano());
				ppe.setIm_entrata(el.getIm_entrata());
				ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
				ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
				el.getVociBilancioAssociate().stream()
					.forEach(voce->{
						Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
						newVoce.setElemento_voce(voce.getElemento_voce());
						newVoce.setProgetto_piano_economico(ppe);
						newVoce.setSaldoSpesa(voce.getSaldoSpesa());
						newVoce.setSaldoEntrata(voce.getSaldoEntrata());
						ppe.addToVociBilancioAssociate(newVoce);
					});
				rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);
			});
		
		//Aggiorno i dettagli presenti
		rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.forEach(el->{
				Progetto_rimodulazione_ppeBulk dett = 
						rimodulazione.getDettagliRimodulazione().stream()
							.filter(dettRim->dettRim.getPg_progetto().equals(el.getPg_progetto()))
							.filter(dettRim->dettRim.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
							.filter(dettRim->dettRim.getCd_voce_piano().equals(el.getCd_voce_piano()))
							.filter(dettRim->dettRim.getEsercizio_piano().equals(el.getEsercizio_piano()))
							.findAny().orElse(null);
				el.setImSpesaFinanziatoRimodulato(el.getIm_spesa_finanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaFinanziato).orElse(BigDecimal.ZERO)));
				el.setImSpesaCofinanziatoRimodulato(el.getIm_spesa_cofinanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaCofinanziato).orElse(BigDecimal.ZERO)));
			});

		//Aggiungo i dettagli nuovi
		rimodulazione.getDettagliRimodulazione().stream()
			.forEach(el->{
				Progetto_piano_economicoBulk dett = 
						rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
							.filter(dettPpe->dettPpe.getPg_progetto().equals(el.getPg_progetto()))
							.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
							.filter(dettPpe->dettPpe.getCd_voce_piano().equals(el.getCd_voce_piano()))
							.filter(dettPpe->dettPpe.getEsercizio_piano().equals(el.getEsercizio_piano()))
							.findAny().orElse(null);
				if (!Optional.ofNullable(dett).isPresent()) {
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setProgetto(rimodulazione.getProgetto());
					ppe.setVoce_piano_economico(el.getVocePianoEconomico());
					ppe.setEsercizio_piano(el.getEsercizio_piano());
					ppe.setIm_entrata(el.getImVarEntrata());
					ppe.setIm_spesa_finanziato(BigDecimal.ZERO);
					ppe.setIm_spesa_cofinanziato(BigDecimal.ZERO);
					ppe.setImSpesaFinanziatoRimodulato(el.getImVarSpesaFinanziato());
					ppe.setImSpesaCofinanziatoRimodulato(el.getImVarSpesaCofinanziato());
					
					if (ppe.getEsercizio_piano().equals(0))
						rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
					else if (ppe.getEsercizio_piano().equals(rimodulazione.getProgetto().getEsercizio()))
						rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
					else
						rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);						
				}
			});

		//Aggiorno le voci di bilancio presenti
		rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
			.forEach(ppeVoce->{
				ppeVoce.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
				ppeVoce.setImVarCofinanziatoRimodulato(BigDecimal.ZERO);
				Progetto_rimodulazione_voceBulk rimVoce = 
						rimodulazione.getDettagliVoceRimodulazione().stream()
							.filter(dettVoceRim->dettVoceRim.getPg_progetto().equals(ppeVoce.getPg_progetto()))
							.filter(dettVoceRim->dettVoceRim.getCd_unita_organizzativa().equals(ppeVoce.getCd_unita_organizzativa()))
							.filter(dettVoceRim->dettVoceRim.getCd_voce_piano().equals(ppeVoce.getCd_voce_piano()))
							.filter(dettVoceRim->dettVoceRim.getEsercizio_piano().equals(ppeVoce.getEsercizio_piano()))
							.filter(dettVoceRim->dettVoceRim.getEsercizio_voce().equals(ppeVoce.getEsercizio_voce()))
							.filter(dettVoceRim->dettVoceRim.getTi_appartenenza().equals(ppeVoce.getTi_appartenenza()))
							.filter(dettVoceRim->dettVoceRim.getTi_gestione().equals(ppeVoce.getTi_gestione()))
							.filter(dettVoceRim->dettVoceRim.getCd_elemento_voce().equals(ppeVoce.getCd_elemento_voce()))
							.findAny().orElse(null);
				if (Optional.ofNullable(rimVoce).isPresent()) {
					ppeVoce.setDetailRimodulatoAggiunto(rimVoce.isTiOperazioneAggiunto());
					ppeVoce.setDetailRimodulatoEliminato(rimVoce.isTiOperazioneEliminato());
					ppeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
					ppeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());
				}
			});

		//Aggiungo i dettagli nuovi
		rimodulazione.getDettagliVoceRimodulazione().stream()
			.forEach(rimVoce->{
				Ass_progetto_piaeco_voceBulk ppeVoce = 
						rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
							.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
							.filter(dettVocePpe->dettVocePpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
							.filter(dettVocePpe->dettVocePpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
							.filter(dettVocePpe->dettVocePpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
							.filter(dettVocePpe->dettVocePpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
							.filter(dettVocePpe->dettVocePpe.getEsercizio_voce().equals(rimVoce.getEsercizio_voce()))
							.filter(dettVocePpe->dettVocePpe.getTi_appartenenza().equals(rimVoce.getTi_appartenenza()))
							.filter(dettVocePpe->dettVocePpe.getTi_gestione().equals(rimVoce.getTi_gestione()))
							.filter(dettVocePpe->dettVocePpe.getCd_elemento_voce().equals(rimVoce.getCd_elemento_voce()))
							.findAny().orElse(null);
				if (!Optional.ofNullable(ppeVoce).isPresent()) {
					Progetto_piano_economicoBulk ppe = 
							rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
								.filter(dettPpe->dettPpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
								.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
								.filter(dettPpe->dettPpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
								.filter(dettPpe->dettPpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
								.findAny().orElse(null);
					if (Optional.of(ppe).isPresent()) {
						Ass_progetto_piaeco_voceBulk newPpeVoce = new Ass_progetto_piaeco_voceBulk();
						newPpeVoce.setElemento_voce(rimVoce.getElementoVoce());
						newPpeVoce.setDetailRimodulatoAggiunto(Boolean.TRUE);
						newPpeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
						newPpeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());

						newPpeVoce.initializeSaldo(progetto.getVociBilancioMovimentate());
						ppe.addToVociBilancioAssociate(newPpeVoce);
					}
				}
			});
		return rimodulazione;
	}
	
	/**
	 * Restituisce il progetto associato alla rimodulazione con le modifiche richieste dalla rimodulazione stessa 
	 * Il progetto viene restituito nella versione definitiva che avrebbe se la rimodulazione venisse approvata.
	 * 
	 * N.B. Questo metodo deve essere richiamato solo l'oggetto Rimodulazione è stato caricato con tutti i dettagli
	 * in caso contrario chiamare il metodo precedente.
	 * 
	 * @param rimodulazione Rimodulazione caricato con tutti i dettagli
	 * @return Progetto Rimodulato
	 */
	public ProgettoBulk getProgettoRimodulato(Progetto_rimodulazioneBulk rimodulazione) {
		ProgettoBulk progetto = rimodulazione.getProgetto();
		
		if (rimodulazione.isRimodulatoDtInizio())
			progetto.getOtherField().setDtInizio(rimodulazione.getDtInizioRimodulato());
		if (rimodulazione.isRimodulatoDtFine())
			progetto.getOtherField().setDtFine(rimodulazione.getDtFineRimodulato());
		if (rimodulazione.isRimodulatoDtProroga())
			progetto.getOtherField().setDtProroga(rimodulazione.getDtProrogaRimodulato());
		if (rimodulazione.isRimodulatoImportoFinanziato())
			progetto.getOtherField().setImFinanziato(rimodulazione.getImFinanziatoRimodulato());
		if (rimodulazione.isRimodulatoImportoCofinanziato())
			progetto.getOtherField().setImCofinanziato(rimodulazione.getImCofinanziatoRimodulato());

		rimodulazione.getDettagliPianoEconomicoAnnoCorrente().stream()
			.forEach(ppeRim->{
				Progetto_piano_economicoBulk ppeStorage = 
					progetto.getDettagliPianoEconomicoAnnoCorrente().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(ppeRim.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(ppeRim.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(ppeRim.getEsercizio_piano()))
							.findFirst().orElse(null);
				
				if (ppeRim.isDetailRimodulatoEliminato()) {
					if (Optional.ofNullable(ppeStorage).isPresent()) {

						List<Ass_progetto_piaeco_voceBulk> list = new ArrayList<>();
						ppeStorage.getVociBilancioAssociate().stream()
							.forEach(ppeVocStorage->{
								ppeVocStorage.setToBeDeleted();
								list.add(ppeVocStorage);
						});
						list.forEach(el->ppeStorage.removeFromVociBilancioAssociate(ppeStorage.getVociBilancioAssociate().indexOf(el)));

						ppeStorage.setToBeDeleted();
						progetto.removeFromDettagliPianoEconomicoAnnoCorrente(progetto.getDettagliPianoEconomicoAnnoCorrente().indexOf(ppeStorage));
					}
				} else {
					Progetto_piano_economicoBulk ppe = 
							Optional.ofNullable(ppeStorage)
								.orElseGet(()->{
									Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
									newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
									newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
									newPpe.setFl_ctrl_disp(Boolean.TRUE);
									newPpe.setToBeCreated();
									progetto.addToDettagliPianoEconomicoAnnoCorrente(newPpe);
									return newPpe;
								});

					ppe.setIm_entrata(ppeRim.getIm_entrata());
					ppe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					ppe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());
					ppe.setToBeUpdated();
					
					ppeRim.getVociBilancioAssociate().stream()
						.forEach(ppeRimVoc->{
							Ass_progetto_piaeco_voceBulk ppeVocStorage = 
								ppe.getVociBilancioAssociate().stream()
								   .filter(el->el.getCd_unita_organizzativa().equals(ppeRimVoc.getCd_unita_organizzativa()))
								   .filter(el->el.getCd_voce_piano().equals(ppeRimVoc.getCd_voce_piano()))
								   .filter(el->el.getEsercizio_piano().equals(ppeRimVoc.getEsercizio_piano()))
								   .filter(el->el.getEsercizio_voce().equals(ppeRimVoc.getEsercizio_voce()))
								   .filter(el->el.getTi_appartenenza().equals(ppeRimVoc.getTi_appartenenza()))
								   .filter(el->el.getTi_gestione().equals(ppeRimVoc.getTi_gestione()))
								   .filter(el->el.getCd_elemento_voce().equals(ppeRimVoc.getCd_elemento_voce()))
								   .findFirst().orElse(null);

							if (ppeRimVoc.isDetailRimodulatoEliminato()) {
								if (Optional.ofNullable(ppeVocStorage).isPresent()){
									ppeVocStorage.setToBeDeleted();
									ppe.removeFromVociBilancioAssociate(ppe.getVociBilancioAssociate().indexOf(ppeVocStorage));
								}
							} else {
								if (!Optional.ofNullable(ppeVocStorage).isPresent()) {
									Ass_progetto_piaeco_voceBulk newPpeVoc = new Ass_progetto_piaeco_voceBulk();
									newPpeVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
									newPpeVoc.initializeSaldo(ppe.getProgetto().getVociBilancioMovimentate());
									newPpeVoc.setToBeCreated();
									ppe.addToVociBilancioAssociate(newPpeVoc);
							   }
							}
					});
				}
		});

		rimodulazione.getDettagliPianoEconomicoAltriAnni().stream()
			.forEach(ppeRim->{
				Progetto_piano_economicoBulk ppeStorage = 
					progetto.getDettagliPianoEconomicoAltriAnni().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(ppeRim.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(ppeRim.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(ppeRim.getEsercizio_piano()))
							.findFirst().orElse(null);
				
				if (ppeRim.isDetailRimodulatoEliminato()) {
					if (Optional.ofNullable(ppeStorage).isPresent()) {

						List<Ass_progetto_piaeco_voceBulk> list = new ArrayList<>();
						ppeStorage.getVociBilancioAssociate().stream()
							.forEach(ppeVocStorage->{
								ppeVocStorage.setToBeDeleted();
								list.add(ppeVocStorage);
						});
						list.forEach(el->ppeStorage.removeFromVociBilancioAssociate(ppeStorage.getVociBilancioAssociate().indexOf(el)));

						ppeStorage.setToBeDeleted();
						progetto.removeFromDettagliPianoEconomicoAltriAnni(progetto.getDettagliPianoEconomicoAltriAnni().indexOf(ppeStorage));
					}
				} else {
					Progetto_piano_economicoBulk ppe = 
							Optional.ofNullable(ppeStorage)
								.orElseGet(()->{
									Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
									newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
									newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
									newPpe.setFl_ctrl_disp(Boolean.TRUE);
									newPpe.setToBeCreated();
									progetto.addToDettagliPianoEconomicoAltriAnni(newPpe);
									return newPpe;
								});
	
					ppe.setIm_entrata(ppeRim.getIm_entrata());
					ppe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					ppe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());
					ppe.setToBeUpdated();
					
					ppeRim.getVociBilancioAssociate().stream()
						.forEach(ppeRimVoc->{
							Ass_progetto_piaeco_voceBulk ppeVocStorage = 
								ppe.getVociBilancioAssociate().stream()
								   .filter(el->el.getCd_unita_organizzativa().equals(ppeRimVoc.getCd_unita_organizzativa()))
								   .filter(el->el.getCd_voce_piano().equals(ppeRimVoc.getCd_voce_piano()))
								   .filter(el->el.getEsercizio_piano().equals(ppeRimVoc.getEsercizio_piano()))
								   .filter(el->el.getEsercizio_voce().equals(ppeRimVoc.getEsercizio_voce()))
								   .filter(el->el.getTi_appartenenza().equals(ppeRimVoc.getTi_appartenenza()))
								   .filter(el->el.getTi_gestione().equals(ppeRimVoc.getTi_gestione()))
								   .filter(el->el.getCd_elemento_voce().equals(ppeRimVoc.getCd_elemento_voce()))
								   .findFirst().orElse(null);
	
							if (ppeRimVoc.isDetailRimodulatoEliminato()) {
								if (Optional.ofNullable(ppeVocStorage).isPresent()) {
									ppeVocStorage.setToBeDeleted();
									ppe.removeFromVociBilancioAssociate(ppe.getVociBilancioAssociate().indexOf(ppeVocStorage));
								}
							} else {
								if (!Optional.ofNullable(ppeVocStorage).isPresent()) {
									Ass_progetto_piaeco_voceBulk newPpeVoc = new Ass_progetto_piaeco_voceBulk();
									newPpeVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
									newPpeVoc.initializeSaldo(ppe.getProgetto().getVociBilancioMovimentate());
									newPpeVoc.setToBeCreated();
									ppe.addToVociBilancioAssociate(newPpeVoc);
							   }
							}
					});
				}
		});

		rimodulazione.getDettagliPianoEconomicoTotale().stream()
			.forEach(ppeRim->{
				Progetto_piano_economicoBulk ppeStorage = 
					progetto.getDettagliPianoEconomicoTotale().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(ppeRim.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(ppeRim.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(ppeRim.getEsercizio_piano()))
							.findFirst().orElse(null);
				
				if (ppeRim.isDetailRimodulatoEliminato()) {
					if (Optional.ofNullable(ppeStorage).isPresent())
						progetto.removeFromDettagliPianoEconomicoTotale(progetto.getDettagliPianoEconomicoTotale().indexOf(ppeStorage));
				} else {
					Progetto_piano_economicoBulk ppe = 
							Optional.ofNullable(ppeStorage)
								.orElseGet(()->{
									Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
									newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
									newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
									newPpe.setFl_ctrl_disp(Boolean.TRUE);
									newPpe.setToBeCreated();
									progetto.addToDettagliPianoEconomicoTotale(newPpe);
									return newPpe;
								});
	
					ppe.setIm_entrata(ppeRim.getIm_entrata());
					ppe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					ppe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());
					ppe.setToBeUpdated();
					
					ppeRim.getVociBilancioAssociate().stream()
						.forEach(ppeRimVoc->{
							Ass_progetto_piaeco_voceBulk ppeVocStorage = 
								ppe.getVociBilancioAssociate().stream()
								   .filter(el->el.getCd_unita_organizzativa().equals(ppeRimVoc.getCd_unita_organizzativa()))
								   .filter(el->el.getCd_voce_piano().equals(ppeRimVoc.getCd_voce_piano()))
								   .filter(el->el.getEsercizio_piano().equals(ppeRimVoc.getEsercizio_piano()))
								   .filter(el->el.getEsercizio_voce().equals(ppeRimVoc.getEsercizio_voce()))
								   .filter(el->el.getTi_appartenenza().equals(ppeRimVoc.getTi_appartenenza()))
								   .filter(el->el.getTi_gestione().equals(ppeRimVoc.getTi_gestione()))
								   .filter(el->el.getCd_elemento_voce().equals(ppeRimVoc.getCd_elemento_voce()))
								   .findFirst().orElse(null);
	
							if (ppeRimVoc.isDetailRimodulatoEliminato()) {
								if (Optional.ofNullable(ppeVocStorage).isPresent())
									ppe.removeFromVociBilancioAssociate(ppe.getVociBilancioAssociate().indexOf(ppeVocStorage));
							} else {
								if (!Optional.ofNullable(ppeVocStorage).isPresent()) {
									Ass_progetto_piaeco_voceBulk newPpeVoc = new Ass_progetto_piaeco_voceBulk();
									newPpeVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
									newPpeVoc.initializeSaldo(ppe.getProgetto().getVociBilancioMovimentate());
									newPpeVoc.setToBeCreated();
									ppe.addToVociBilancioAssociate(newPpeVoc);
							   }
							}
					});
				}
		});
		return progetto;
	}
	
	public void validaPassaggioStatoApprovato(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione, OggettoBulk variazioneInApprovazione) throws ComponentException, PersistencyException, IntrospectionException, RimodulazioneNonApprovataException {
		Optional.of(rimodulazione).filter(Progetto_rimodulazioneBulk::isStatoValidato)
		.orElseThrow(()->new ApplicationException("Operazione non possibile! Lo stato approvato può essere assegnato solo a rimodulazioni in stato validato!"));
	
		if (rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
						 .filter(el->el.getDispResiduaFinanziamentoRimodulato().compareTo(BigDecimal.ZERO)<0 ||
								 	 el.getDispResiduaCofinanziamentoRimodulato().compareTo(BigDecimal.ZERO)<0)
						 .findFirst().isPresent())
			throw new RimodulazioneNonApprovataException("Operazione non possibile! Lo stato approvato può essere assegnato solo quando tutte le variazioni a quadratura sono state inserite e approvate!");
	
		if (this.findVariazioniCompetenzaAssociate(rimodulazione).stream()
				.filter(el->Optional.ofNullable(variazioneInApprovazione)
									.filter(Pdg_variazioneBulk.class::isInstance)
									.map(varEscl->!varEscl.equalsByPrimaryKey(el))
									.orElse(Boolean.TRUE))
				.filter(el->el.isPropostaProvvisoria()||el.isPropostaDefinitiva())
	    	.findFirst().isPresent())
	    	throw new RimodulazioneNonApprovataException("Operazione non possibile! Lo stato approvato può essere assegnato solo quando tutte le variazioni a quadratura sono approvate!");
	
	    if (this.findVariazioniResidueAssociate(rimodulazione).stream()
				.filter(el->Optional.ofNullable(variazioneInApprovazione)
						.filter(Var_stanz_resBulk.class::isInstance)
						.map(varEscl->!varEscl.equalsByPrimaryKey(el))
						.orElse(Boolean.TRUE))
	    		.filter(el->el.isPropostaProvvisoria()||el.isPropostaDefinitiva())
			.findFirst().isPresent())
			throw new RimodulazioneNonApprovataException("Operazione non possibile! Lo stato approvato può essere assegnato solo quando tutte le variazioni a quadratura sono approvate!");
	}
}
