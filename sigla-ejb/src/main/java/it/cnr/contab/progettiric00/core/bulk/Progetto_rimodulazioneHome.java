package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
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
	
	public java.util.Collection<Progetto_rimodulazione_ppeBulk> findDettagliRimodulazione(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Progetto_rimodulazione_ppeHome dettHome = (Progetto_rimodulazione_ppeHome)getHomeCache().getHome(Progetto_rimodulazione_ppeBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Progetto_rimodulazione_voceBulk> findDettagliVoceRimodulazione(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Progetto_rimodulazione_voceHome dettHome = (Progetto_rimodulazione_voceHome)getHomeCache().getHome(Progetto_rimodulazione_voceBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.List<Progetto_rimodulazioneBulk> findRimodulazioni(it.cnr.jada.UserContext userContext,Integer pgProgetto) throws PersistencyException {
		Progetto_rimodulazioneHome dettHome = (Progetto_rimodulazioneHome)getHomeCache().getHome(Progetto_rimodulazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Pdg_variazioneBulk> findVariazioniCompetenzaAssociate(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
		Pdg_variazioneHome dettHome = (Pdg_variazioneHome)getHomeCache().getHome(Pdg_variazioneBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"pg_progetto_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());
		sql.addClause(FindClause.AND,"pg_rimodulazione",SQLBuilder.EQUALS,rimodulazione.getPg_rimodulazione());
		return dettHome.fetchAll(sql);
	}
	
	public java.util.Collection<Var_stanz_resBulk> findVariazioniResidueAssociate(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws IntrospectionException, PersistencyException {
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
	public List<OggettoBulk> constructVariazioniBilancio(it.cnr.jada.UserContext userContext,Progetto_rimodulazioneBulk rimodulazione) throws PersistencyException,ApplicationException {
		try {
			List<OggettoBulk> result = new ArrayList<OggettoBulk>();
			BigDecimal annoFrom = Utility.createConfigurazioneCnrComponentSession().getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
			
			List<Progetto_rimodulazione_voceBulk> listRimVoce = getDettagliRimodulazioneVoceAggiornato(userContext, rimodulazione);
			Map<Integer, List<Progetto_rimodulazione_voceBulk>> mapEsercizio = 
					listRimVoce.stream()
							   .filter(el->el.getEsercizio_piano().compareTo(annoFrom.intValue())>=0)
							   .filter(el->el.getImVarSpesaFinanziato().compareTo(BigDecimal.ZERO)!=0 ||
							   			   el.getImVarSpesaCofinanziato().compareTo(BigDecimal.ZERO)!=0)
							   .collect(Collectors.groupingBy(Progetto_rimodulazione_voceBulk::getEsercizio_piano));
			
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHomeCache().getHome(Unita_organizzativaBulk.class);
			CdrBulk cdr = uoHome.findCdrResponsbileUo(rimodulazione.getProgetto().getUnita_organizzativa());
			
			for (Integer esercizio : mapEsercizio.keySet()) {
				Map<Elemento_voceBulk, List<Progetto_rimodulazione_voceBulk>> mapVoce = 
						mapEsercizio.get(esercizio).stream()
				   				.collect(Collectors.groupingBy(Progetto_rimodulazione_voceBulk::getElementoVoce));
				
				Map<String, Map<Elemento_voceBulk, BigDecimal>> newMapVoce = new HashMap<String,Map<Elemento_voceBulk,BigDecimal>>();
				newMapVoce.put("FIN", new HashMap<Elemento_voceBulk,BigDecimal>());
				newMapVoce.put("FES", new HashMap<Elemento_voceBulk,BigDecimal>());

				mapVoce.forEach((k, v) -> {
					BigDecimal importoFIN = v.stream().map(Progetto_rimodulazione_voceBulk::getImVarSpesaCofinanziato).reduce((x, y) -> x.add(y))
													  .orElse(BigDecimal.ZERO);
					BigDecimal importoFES = v.stream().map(Progetto_rimodulazione_voceBulk::getImVarSpesaFinanziato).reduce((x, y) -> x.add(y))
							  .orElse(BigDecimal.ZERO);
					if (importoFIN.compareTo(BigDecimal.ZERO)!=0)
		            	newMapVoce.get("FIN").put(k, importoFIN);
					if (importoFES.compareTo(BigDecimal.ZERO)!=0)
		            	newMapVoce.get("FES").put(k, importoFES);
				});
				
				newMapVoce.forEach((fonte, mapVociImporti) -> {
					//creo la variazione solo per fonti esterne
					if (fonte.equals("FES")) {
						BigDecimal imVariazione = mapVociImporti.values().stream().reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
						if (esercizio.compareTo(CNRUserContext.getEsercizio(userContext))<0){
							Var_stanz_resBulk varStanz = new Var_stanz_resBulk();
							varStanz.setEsercizio(esercizio);
							varStanz.setEsercizio_res(new Esercizio_baseBulk(esercizio));
							varStanz.setTipologia_fin(fonte);
							varStanz.setCdr(cdr);
							
							Ass_var_stanz_res_cdrBulk assCdr = new Ass_var_stanz_res_cdrBulk();
							assCdr.setCentro_di_responsabilita(cdr);
							assCdr.setIm_spesa(imVariazione);
							varStanz.addToAssociazioneCDR(assCdr);
							
							mapVociImporti.forEach((voce, importo) -> {
								Var_stanz_res_rigaBulk riga = new Var_stanz_res_rigaBulk();
								riga.setElemento_voce(voce);
								riga.setIm_variazione(importo);
							});
							
							result.add(varStanz);
						} else if (esercizio.compareTo(CNRUserContext.getEsercizio(userContext))==0){
							Pdg_variazioneBulk pdgVar = new Pdg_variazioneBulk();
							pdgVar.setEsercizio(esercizio);
							pdgVar.setTipologia_fin(fonte);
							pdgVar.setCentro_responsabilita(cdr);
							
							Ass_pdg_variazione_cdrBulk assCdr = new Ass_pdg_variazione_cdrBulk();
							assCdr.setCentro_responsabilita(cdr);
							assCdr.setIm_entrata(BigDecimal.ZERO);
							assCdr.setIm_spesa(imVariazione);
							pdgVar.addToAssociazioneCDR(assCdr);
							
							mapVociImporti.forEach((voce, importo) -> {
								Pdg_variazione_riga_gestBulk riga = new Pdg_variazione_riga_gestBulk();
								riga.setElemento_voce(voce);
								riga.setIm_variazione(importo);
							});
							result.add(pdgVar);
						}
					}
				});
			}
			return result;
		}catch(Exception e) {
			throw new PersistencyException(e);
		}		
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
				newDetail.setImVarSpesaFinanziato(ppe.getImSpesaFinanziatoRimodulato().subtract(ppe.getIm_spesa_finanziato()));
				newDetail.setImVarSpesaCofinanziato(ppe.getImSpesaCofinanziatoRimodulato().subtract(ppe.getIm_spesa_cofinanziato()));
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
}
