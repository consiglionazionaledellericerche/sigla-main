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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.client.docamm.Voce;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.progettiric00.enumeration.StatoProgettoRimodulazione;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.util.DateUtils;
import it.cnr.si.spring.storage.StorageDriver;

public class Progetto_rimodulazioneBulk extends Progetto_rimodulazioneBase implements AllegatoParentBulk {
	private static final long serialVersionUID = 1L;

	public static final String STATO_RIMODULAZIONE_TUTTI = "TUTTI";

	public final static Map<String,String> statoKeys = Arrays.asList(StatoProgettoRimodulazione.values())
            .stream()
            .collect(Collectors.toMap(
            		StatoProgettoRimodulazione::value,
            		StatoProgettoRimodulazione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    
    public final static Map<String,String> statoOfKeys = Arrays.asList(StatoProgetto.values())
            .stream()
            .collect(Collectors.toMap(
            		StatoProgetto::value,
            		StatoProgetto::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

	protected ProgettoBulk progetto;
	
	private BulkList<Progetto_rimodulazione_ppeBulk> dettagliRimodulazione = new BulkList<Progetto_rimodulazione_ppeBulk>();
	private BulkList<Progetto_rimodulazione_voceBulk> dettagliVoceRimodulazione = new BulkList<Progetto_rimodulazione_voceBulk>();

	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoTotale = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAnnoCorrente = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAltriAnni = new BulkList<Progetto_piano_economicoBulk>();

	private BulkList<Progetto_rimodulazione_variazioneBulk> variazioniAssociate = new BulkList<Progetto_rimodulazione_variazioneBulk>();

	private java.math.BigDecimal imFinanziatoRimodulato;
	private java.math.BigDecimal imCofinanziatoRimodulato;
	private java.sql.Timestamp dtInizioRimodulato;
	private java.sql.Timestamp dtFineRimodulato;
	private java.sql.Timestamp dtProrogaRimodulato;
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	private Integer annoFromPianoEconomico;
	private Integer lastEsercizioAperto;
	
	//Flag che indica se nella mappa di rimodulazione occorre visualizzare una versione semplice 
	//senza campi valori attuali
	private Boolean flViewCurrent = Boolean.FALSE;

	//Modelli di variazioni compatibili con la rimodulazione.
	//Se vuota vuol dire che la rimodulazione definitiva non prevede creazione di variazioni di bilancio.
	//Lista che viene valorizzata dal BP sulle rimodulazioni di tipo definitivo.
	List<OggettoBulk> variazioniModels = new ArrayList<OggettoBulk>();

	public Progetto_rimodulazioneBulk() {
		super();
	}

	public Progetto_rimodulazioneBulk(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione) {
		super(pg_progetto,pg_rimodulazione);
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	
	@Override
	public Integer getPg_progetto() {
		return Optional.ofNullable(progetto)
				.map(ProgettoBulk::getPg_progetto)
				.orElse(null);
	}
	
	@Override
	public void setPg_progetto(Integer pg_progetto) {
		Optional.ofNullable(progetto)
			.ifPresent(el->el.setPg_progetto(pg_progetto));
	}
	
	public boolean isStatoProvvisorio() {
		return StatoProgettoRimodulazione.STATO_PROVVISORIO.value().equals(this.getStato());
	}
	
	public boolean isStatoDefinitivo() {
		return StatoProgettoRimodulazione.STATO_DEFINITIVO.value().equals(this.getStato());
	}

	public boolean isStatoValidato() {
		return StatoProgettoRimodulazione.STATO_VALIDATO.value().equals(this.getStato());
	}

	public boolean isStatoApprovato() {
		return StatoProgettoRimodulazione.STATO_APPROVATO.value().equals(this.getStato());
	}

	public boolean isStatoRespinto() {
		return StatoProgettoRimodulazione.STATO_RESPINTO.value().equals(this.getStato());
	}
	
	public int addToDettagliRimodulazione(Progetto_rimodulazione_ppeBulk dett) {
		dett.setProgettoRimodulazione( this );
		dettagliRimodulazione.add(dett);
		return dettagliRimodulazione.size()-1;
	}

	public int addToDettagliVoceRimodulazione(Progetto_rimodulazione_voceBulk dett) {
		dett.setProgettoRimodulazione( this );
		dettagliVoceRimodulazione.add(dett);
		return dettagliVoceRimodulazione.size()-1;
	}
	
	public int addToDettagliPianoEconomicoTotale(Progetto_piano_economicoBulk dett) {
		dett.setProgetto( this.getProgetto() );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoTotale.add(dett);
		return dettagliPianoEconomicoTotale.size()-1;
	}

	public int addToDettagliPianoEconomicoAnnoCorrente(Progetto_piano_economicoBulk dett) {
		dett.setProgettoRimodulazione(this);
		dett.setProgetto( this.getProgetto() );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAnnoCorrente.add(dett);
		return dettagliPianoEconomicoAnnoCorrente.size()-1;
	}

	public int addToDettagliPianoEconomicoAltriAnni(Progetto_piano_economicoBulk dett) {
		dett.setProgettoRimodulazione(this);
		dett.setProgetto( this.getProgetto() );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAltriAnni.add(dett);
		return dettagliPianoEconomicoAltriAnni.size()-1;
	}
	
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {this.getDettagliRimodulazione(),this.getDettagliVoceRimodulazione(),this.getArchivioAllegati()};
	}

	public BulkList<Progetto_rimodulazione_ppeBulk> getDettagliRimodulazione() {
		return dettagliRimodulazione;
	}
	
	public BulkList<Progetto_rimodulazione_voceBulk> getDettagliVoceRimodulazione() {
		return dettagliVoceRimodulazione;
	}
	
	public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoTotale() {
		return dettagliPianoEconomicoTotale;
	}

	public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoAnnoCorrente() {
		return dettagliPianoEconomicoAnnoCorrente;
	}
	
	public BulkList<Progetto_piano_economicoBulk> getDettagliPianoEconomicoAltriAnni() {
		return dettagliPianoEconomicoAltriAnni;
	}

	public BulkList<Progetto_rimodulazione_variazioneBulk> getVariazioniAssociate() {
		return variazioniAssociate;
	}

	public void setDettagliRimodulazione(BulkList<Progetto_rimodulazione_ppeBulk> dettagliRimodulazione) {
		this.dettagliRimodulazione = dettagliRimodulazione;
	}
	
	public void setDettagliVoceRimodulazione(BulkList<Progetto_rimodulazione_voceBulk> dettagliVoceRimodulazione) {
		this.dettagliVoceRimodulazione = dettagliVoceRimodulazione;
	}
	
	public void setDettagliPianoEconomicoTotale(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoTotale) {
		this.dettagliPianoEconomicoTotale = dettagliPianoEconomicoTotale;
	}

	public void setDettagliPianoEconomicoAnnoCorrente(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAnnoCorrente) {
		this.dettagliPianoEconomicoAnnoCorrente = dettagliPianoEconomicoAnnoCorrente;
	}
	
	public void setDettagliPianoEconomicoAltriAnni(BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAltriAnni) {
		this.dettagliPianoEconomicoAltriAnni = dettagliPianoEconomicoAltriAnni;
	}

	public void setVariazioniAssociate(BulkList<Progetto_rimodulazione_variazioneBulk> variazioniAssociate) {
		this.variazioniAssociate = variazioniAssociate;
	}

	public Progetto_rimodulazione_ppeBulk removeFromDettagliRimodulazione(int index) {
		return (Progetto_rimodulazione_ppeBulk)dettagliRimodulazione.remove(index);
	}

	public Progetto_rimodulazione_voceBulk removeFromDettagliVoceRimodulazione(int index) {
		return (Progetto_rimodulazione_voceBulk)dettagliVoceRimodulazione.remove(index);
	}
	
	public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoTotale(int index) {
		return (Progetto_piano_economicoBulk)dettagliPianoEconomicoTotale.remove(index);
	}

	public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoAnnoCorrente(int index) {
		return (Progetto_piano_economicoBulk)dettagliPianoEconomicoAnnoCorrente.remove(index);
	}
	
	public Progetto_piano_economicoBulk removeFromDettagliPianoEconomicoAltriAnni(int index) {
		return (Progetto_piano_economicoBulk)dettagliPianoEconomicoAltriAnni.remove(index);
	}

	public BulkList<Progetto_piano_economicoBulk> getAllDetailsProgettoPianoEconomico() {
		BulkList<Progetto_piano_economicoBulk> items = new BulkList<Progetto_piano_economicoBulk>();
		items.addAll(getDettagliPianoEconomicoTotale());
		items.addAll(getDettagliPianoEconomicoAnnoCorrente());
		items.addAll(getDettagliPianoEconomicoAltriAnni());
		return items;
	}
	
	public BulkList<Progetto_piano_economicoBulk> getPianoEconomicoSummaryVoce() {
		Map<String, List<Progetto_piano_economicoBulk>> resultByVoce = 
				this.getAllDetailsProgettoPianoEconomico().stream()
					 .filter(el->el.getVoce_piano_economico()!=null)
					 .filter(el->el.getVoce_piano_economico().getCd_unita_organizzativa()!=null)
					 .filter(el->el.getVoce_piano_economico().getCd_voce_piano()!=null)					 
					 .collect(Collectors.groupingBy(x->{
						 return x.getCd_unita_organizzativa().concat(x.getCd_voce_piano());
					 }));
		
		return new BulkList<Progetto_piano_economicoBulk>(resultByVoce.keySet().stream()
				.sorted((vocePiano1,vocePiano2)->vocePiano1.compareTo(vocePiano2))
				.map(vocePiano->{
						Progetto_piano_economicoBulk bulk = new Progetto_piano_economicoBulk();
						bulk.setVoce_piano_economico(resultByVoce.get(vocePiano).stream().findFirst().get().getVoce_piano_economico());
						bulk.setIm_spesa_finanziato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setIm_spesa_cofinanziato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setImSpesaFinanziatoRimodulato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setImSpesaCofinanziatoRimodulato(resultByVoce.get(vocePiano).stream().map(el->Optional.ofNullable(el.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));

						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiSpesa));
						BigDecimal totImVarSpesaFinanziatoRimodulato = resultByVoce.get(vocePiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						BigDecimal totImVarSpesaCofinanziatoRimodulato = resultByVoce.get(vocePiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						dettSpesa.setImVarFinanziatoRimodulato(totImVarSpesaFinanziatoRimodulato);
						dettSpesa.setImVarCofinanziatoRimodulato(totImVarSpesaCofinanziatoRimodulato);
						bulk.addToVociBilancioAssociate(dettSpesa);

						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiEntrata));
						BigDecimal totImVarEntrataFinanziatoRimodulato = resultByVoce.get(vocePiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_ENTRATE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						BigDecimal totImVarEntrataCofinanziatoRimodulato = resultByVoce.get(vocePiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_ENTRATE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						dettEntrata.setImVarFinanziatoRimodulato(totImVarEntrataFinanziatoRimodulato);
						dettEntrata.setImVarCofinanziatoRimodulato(totImVarEntrataCofinanziatoRimodulato);
						bulk.addToVociBilancioAssociate(dettEntrata);
						
						return bulk;
					})
				.collect(Collectors.toList()));
	}

	public BulkList<Progetto_piano_economicoBulk> getPianoEconomicoSummaryAnno() {
		Map<Integer, List<Progetto_piano_economicoBulk>> resultByEsercizio = 
				this.getAllDetailsProgettoPianoEconomico().stream()
					 .filter(el->Optional.ofNullable(el.getEsercizio_piano()).isPresent())
					 .collect(Collectors.groupingBy(x->{
						 return x.getEsercizio_piano();
					 }));
		
		return new BulkList<Progetto_piano_economicoBulk>(resultByEsercizio.keySet().stream()
				.sorted((esercizioPiano1,esercizioPiano2)->esercizioPiano1.compareTo(esercizioPiano2))
				.map(esercizioPiano->{
						Progetto_piano_economicoBulk bulk = new Progetto_piano_economicoBulk();
						bulk.setEsercizio_piano(esercizioPiano);
						bulk.setIm_spesa_finanziato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setIm_spesa_cofinanziato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setImSpesaFinanziatoRimodulato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						bulk.setImSpesaCofinanziatoRimodulato(resultByEsercizio.get(esercizioPiano).stream().map(el->Optional.ofNullable(el.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));

						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiSpesa));
						BigDecimal totImVarSpesaFinanziatoRimodulato = resultByEsercizio.get(esercizioPiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						BigDecimal totImVarSpesaCofinanziatoRimodulato = resultByEsercizio.get(esercizioPiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						dettSpesa.setImVarFinanziatoRimodulato(totImVarSpesaFinanziatoRimodulato);
						dettSpesa.setImVarCofinanziatoRimodulato(totImVarSpesaCofinanziatoRimodulato);
						bulk.addToVociBilancioAssociate(dettSpesa);

						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(V_saldi_voce_progettoBulk.getSaldoAccorpato(listSaldiEntrata));
						BigDecimal totImVarEntrataFinanziatoRimodulato = resultByEsercizio.get(esercizioPiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_ENTRATE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						BigDecimal totImVarEntrataCofinanziatoRimodulato = resultByEsercizio.get(esercizioPiano).stream()
								.flatMap(el->Optional.ofNullable(el.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(el->Elemento_voceHome.GESTIONE_ENTRATE.equals(el.getTi_gestione()))
								.map(el->Optional.ofNullable(el.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
								.reduce((x, y)->x.add(y))
								.orElse(BigDecimal.ZERO);
						dettEntrata.setImVarFinanziatoRimodulato(totImVarEntrataFinanziatoRimodulato);
						dettEntrata.setImVarCofinanziatoRimodulato(totImVarEntrataCofinanziatoRimodulato);
						bulk.addToVociBilancioAssociate(dettEntrata);

						return bulk;
					})
				.collect(Collectors.toList()));
	}	

	public List<V_saldi_voce_progettoBulk> getVociMovimentateNonAssociate() {
		return new BulkList<V_saldi_voce_progettoBulk>(
				Optional.ofNullable(this.getProgetto())
				.flatMap(el->Optional.ofNullable(el.getVociBilancioMovimentate()))
				.map(List::stream).orElse(Stream.empty())
					.filter(voceMov->{
						return !this.getAllDetailsProgettoPianoEconomico().stream()
								.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
								.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
								.filter(voceAss->!voceAss.isDetailRimodulatoEliminato())
								.filter(voceAss->Optional.ofNullable(voceAss.getEsercizio_voce()).isPresent())
								.filter(voceAss->Optional.ofNullable(voceAss.getTi_appartenenza()).isPresent())
								.filter(voceAss->Optional.ofNullable(voceAss.getTi_gestione()).isPresent())
								.filter(voceAss->Optional.ofNullable(voceAss.getCd_elemento_voce()).isPresent())
								.filter(voceAss->voceAss.getEsercizio_voce().equals(voceMov.getEsercizio_voce()))
								.filter(voceAss->voceAss.getTi_appartenenza().equals(voceMov.getTi_appartenenza()))
								.filter(voceAss->voceAss.getTi_gestione().equals(voceMov.getTi_gestione()))
								.filter(voceAss->voceAss.getCd_elemento_voce().equals(voceMov.getCd_elemento_voce()))
								.findAny().isPresent();
					})
					.collect(Collectors.toList()));
	}
	
	public java.math.BigDecimal getImFinanziatoRimodulato() {
		return imFinanziatoRimodulato;
	}
	
	public void setImFinanziatoRimodulato(java.math.BigDecimal imFinanziatoRimodulato) {
		this.imFinanziatoRimodulato = imFinanziatoRimodulato;
	}
	
	public java.math.BigDecimal getImCofinanziatoRimodulato() {
		return imCofinanziatoRimodulato;
	}
	
	public void setImCofinanziatoRimodulato(java.math.BigDecimal imCofinanziatoRimodulato) {
		this.imCofinanziatoRimodulato = imCofinanziatoRimodulato;
	}
	
	public java.sql.Timestamp getDtInizioRimodulato() {
		return dtInizioRimodulato;
	}
	
	public void setDtInizioRimodulato(java.sql.Timestamp dtInizioRimodulato) {
		this.dtInizioRimodulato = dtInizioRimodulato;
	}
	
	public java.sql.Timestamp getDtFineRimodulato() {
		return dtFineRimodulato;
	}
	
	public void setDtFineRimodulato(java.sql.Timestamp dtFineRimodulato) {
		this.dtFineRimodulato = dtFineRimodulato;
	}
	
	public java.sql.Timestamp getDtProrogaRimodulato() {
		return dtProrogaRimodulato;
	}
	
	public void setDtProrogaRimodulato(java.sql.Timestamp dtProrogaRimodulato) {
		this.dtProrogaRimodulato = dtProrogaRimodulato;
	}
	
	public java.math.BigDecimal getImTotaleRimodulato() {
		return this.getImFinanziatoRimodulato().add(this.getImCofinanziatoRimodulato());
	}

	public java.math.BigDecimal getImFinanziatoRimodulatoRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getImCofinanziatoRimodulatoRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getImTotaleRimodulatoRipartito() {
		return this.getAllDetailsProgettoPianoEconomico().stream()
				.map(el->Optional.ofNullable(el.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)
						.add(Optional.ofNullable(el.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)))
				.reduce((x, y)->x.add(y))
				.orElse(BigDecimal.ZERO);
	}

	public java.math.BigDecimal getImFinanziatoRimodulatoDaRipartire() {
		return this.getImFinanziatoRimodulato().subtract(this.getImFinanziatoRimodulatoRipartito());
	}
	
	public java.math.BigDecimal getImCofinanziatoRimodulatoDaRipartire() {
		return this.getImCofinanziatoRimodulato().subtract(this.getImCofinanziatoRimodulatoRipartito());
	}
	
	public java.math.BigDecimal getImTotaleRimodulatoDaRipartire() {
		return this.getImTotaleRimodulato().subtract(this.getImTotaleRimodulatoRipartito());
	}
	
	public Integer getAnnoInizioRimodulato() {
		return Optional.ofNullable(this.getDtInizioRimodulato())
				.map(elDate->{
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime(elDate);
					return calendar.get(Calendar.YEAR);
				})
				.orElse(0);
	}

	/*
		Ritorna l'anno di inizio utile per il caricamento del piano economico rimodulato
		E' sempre pari a 14 anni precedenti all'anno inizio rimodulato.
	 */
	public Integer getAnnoInizioForPianoEconomico() {
		return Optional.ofNullable(this.getAnnoInizioRimodulato())
				.map(el->el-Progetto_piano_economicoBulk.ANNIPRE_PIANOECONOMICO)
				.filter(el->el.compareTo(0)>0)
				.orElse(0);
	}

	public Integer getAnnoFineRimodulato() {
		Optional<Timestamp> optDtProroga = Optional.ofNullable(this.getDtProrogaRimodulato());
		Optional<Integer> anno = Optional.empty();
		if (Optional.ofNullable(this.getDtFineRimodulato()).isPresent() || optDtProroga.isPresent()) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.max(this.getDtFineRimodulato(), optDtProroga.orElse(null)));
			anno = Optional.of(calendar.get(Calendar.YEAR));
		}
		return anno.orElse(9999);
	}
    
	public void validaDateRimodulazione() throws ValidationException {
		Optional<Timestamp> optDtProrogaRimodulato = Optional.ofNullable(this.getDtProrogaRimodulato());
		if (!Optional.ofNullable(this.getDtInizioRimodulato()).isPresent() && Optional.ofNullable(this.getDtFineRimodulato()).isPresent())  
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di fine\" senza indicare la \"Data di inizio\".");
		if (!Optional.ofNullable(this.getDtFineRimodulato()).isPresent() && optDtProrogaRimodulato.isPresent())  
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di proroga\" senza indicare la \"Data di fine\".");
		if (Optional.ofNullable(this.getDtInizioRimodulato()).isPresent() && Optional.ofNullable(this.getDtFineRimodulato()).isPresent() &&
			this.getDtInizioRimodulato().after(this.getDtFineRimodulato()))
			throw new ValidationException( "La \"Data di fine\" del progetto deve essere uguale o superiore alla \"Data di inizio\".");
		if (Optional.ofNullable(this.getDtFineRimodulato()).isPresent() && optDtProrogaRimodulato.isPresent() &&
				this.getDtFineRimodulato().after(optDtProrogaRimodulato.get()))
			throw new ValidationException( "La \"Data di proroga\" del progetto deve essere uguale o superiore alla \"Data di fine\".");
		
		this.getAllDetailsProgettoPianoEconomico().stream()
			.filter(progetto_piano_economicoBulk -> Optional.ofNullable(progetto_piano_economicoBulk.getEsercizio_piano()).isPresent())
			.filter(el->el.getEsercizio_piano().compareTo(this.getAnnoInizioForPianoEconomico())<0)
			.filter(el->!el.isDetailRimodulatoEliminato())
			.map(Progetto_piano_economicoBulk::getEsercizio_piano)
			.min(Comparator.comparing(Integer::valueOf))
			.ifPresent(annoMin->{
				throw new ApplicationRuntimeException("Non è possibile indicare una data di inizio con anno maggiore del "+annoMin+
						" per il quale risulta già caricato un piano economico.");
			});

		this.getAllDetailsProgettoPianoEconomico().stream()
			.filter(el->Optional.ofNullable(el.getEsercizio_piano()).isPresent())
			.filter(el->el.getEsercizio_piano().compareTo(this.getAnnoFineRimodulato())>0)
			.filter(el->!el.isDetailRimodulatoEliminato())
			.map(Progetto_piano_economicoBulk::getEsercizio_piano)
			.max(Comparator.comparing(Integer::valueOf))
			.ifPresent(annoMax->{
				throw new ApplicationRuntimeException("Non è possibile indicare una data di fine/proroga con anno inferiore al "+annoMax+
						" per il quale risulta già caricato un piano economico.");
		});
	}   
	
	/**
	 * Indica se è stato modificato il valore della data di inizio del progetto rispetto al valore originario
	 * 
	 * @return boolean
	 */
	public boolean isRimodulatoDtInizio() {
		return Optional.ofNullable(this.getProgetto())
					   .filter(ProgettoBulk::isDatePianoEconomicoRequired)
					   .flatMap(el->Optional.ofNullable(el.getOtherField()))
					   .map(Progetto_other_fieldBulk::getDtInizio)
					   .map(el->el.compareTo(this.getDtInizioRimodulato())!=0)
					   .orElse(Boolean.FALSE);
	}

	/**
	 * Indica se è stato modificato il valore della data di proroga del progetto rispetto al valore originario
	 * 
	 * @return boolean
	 */
	public boolean isRimodulatoDtProroga() {
		return !Optional.ofNullable(this.getProgetto())
					   .filter(ProgettoBulk::isDatePianoEconomicoRequired)
					   .flatMap(el->Optional.ofNullable(el.getOtherField()))
					   .flatMap(el->Optional.ofNullable(el.getDtProroga()))
					   .equals(Optional.ofNullable(this.getDtProrogaRimodulato()));
	}

	/**
	 * Indica se è stato modificato il valore della data di fine del progetto rispetto al valore originario
	 * 
	 * @return boolean
	 */
	public boolean isRimodulatoDtFine() {
		return Optional.ofNullable(this.getProgetto())
					   .filter(ProgettoBulk::isDatePianoEconomicoRequired)
					   .flatMap(el->Optional.ofNullable(el.getOtherField()))
					   .map(Progetto_other_fieldBulk::getDtFine)
					   .map(el->el.compareTo(this.getDtFineRimodulato())!=0)
					   .orElse(Boolean.FALSE);
	}

	/**
	 * Indica se è stato modificato il valore del totale importo finanziato del progetto rispetto al valore originario
	 * 
	 * @return boolean
	 */
	public boolean isRimodulatoImportoFinanziato() {
		return this.getImFinanziatoRimodulato().compareTo(this.getProgetto().getImFinanziato())!=0;
	}

	/**
	 * Indica se è stato modificato il valore del totale importo cofinanziato del progetto rispetto al valore originario
	 * 
	 * @return boolean
	 */
	public boolean isRimodulatoImportoCofinanziato() {
		return this.getImCofinanziatoRimodulato().compareTo(this.getProgetto().getImCofinanziato())!=0;
	}

	/**
	 * Indica se si tratta di una rimodulazione rapida, ossia che nn necessita di validazione da parte di altri organi
	 * Per consentire agli utenti di avere un minimo di flessibilità nella modifica dei piani economici si potrebbe rendere possibile, fermo restando l’importo per ciascuna categoria economica,
	 * la modifica sulle diverse annualità nel caso in cui:
	 *   • non sia già in corso una rimodulazione;
	 *   • gli importi associati a ciascuna categoria non siano già stanziati/utilizzati (l’utilizzo deriva dalle variazioni ad esempio fatte per la copertura dei tempi determinati);
	 *   • il progetto non risulti scaduto;
	 *   • non venga modificata la categoria PER_TI.
	 * @return boolean
	 */
	public boolean isRimodulazioneDiRapidaApprovazione() {
		return !this.getProgetto().isProgettoScaduto() &&
				isRimodulataRipartizioneTraEsercizi() &&
				!this.getAllDetailsProgettoPianoEconomico().stream().anyMatch(el->Optional.ofNullable(el.getDispResiduaFinanziamentoRimodulato())
								.map(disp->disp.compareTo(BigDecimal.ZERO)<0).orElse(Boolean.FALSE) ||
						Optional.ofNullable(el.getDispResiduaCofinanziamentoRimodulato())
								.map(disp->disp.compareTo(BigDecimal.ZERO)<0).orElse(Boolean.FALSE)) &&
				!this.getDettagliRimodulazione().stream().filter(el->el.getVocePianoEconomico().isVocePersonaleTempoIndeterminato()).findAny().isPresent();
	}

	/**
	 * Indica se è stato modificata la ripartizione degli importi tra gli esercizi lasciando invariati i saldi totali per le voci economiche
	 *
	 * @return boolean
	 */
	private boolean isRimodulataRipartizioneTraEsercizi() {
		Map<Voce_piano_economico_prgBulk, List<Progetto_rimodulazione_ppeBulk>> mapVoce =
				this.getDettagliRimodulazione().stream().collect(Collectors.groupingBy(Progetto_rimodulazione_ppeBulk::getVocePianoEconomico));

		//Se per nessuna voce il saldo delle righe associate è diverso da 0 allora si tratta solo di rimodulazione degli importi tra esercizio
		return !mapVoce.keySet().stream().anyMatch(voce->
			mapVoce.get(voce).stream().map(Progetto_rimodulazione_ppeBulk::getImVarSpesaFinanziato).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)!=0 ||
			mapVoce.get(voce).stream().map(Progetto_rimodulazione_ppeBulk::getImVarSpesaCofinanziato).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)!=0
		);
	}

	@Override
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		Optional.ofNullable(allegato)
			    .filter(AllegatoProgettoRimodulazioneBulk.class::isInstance)
			    .map(AllegatoProgettoRimodulazioneBulk.class::cast)
			    .ifPresent(el->el.setRimodulazione(this));
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;
	}

	@Override
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		AllegatoGenericoBulk dett = (AllegatoGenericoBulk)getArchivioAllegati().remove(index);
		return dett;
	}

	@Override
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}

	@Override
	public void setArchivioAllegati(BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	
	public String getStorePath() {
		return getProgetto().getStorePath()
				.concat(StorageDriver.SUFFIX)
				.concat(getCMISFolderName());
	}
	
	public String getCMISFolderName() {
		String suffix = "Rimodulazione n.";
		suffix = suffix.concat(String.valueOf(this.getPg_rimodulazione()));
		return suffix;
	}	
	
	public void setFlViewCurrent(Boolean flViewCurrent) {
		this.flViewCurrent = flViewCurrent;
	}
	
	/*
	 * Flag che indica se nella mappa di rimodulazione occorre visualizzare una versione semplice 
	 * senza campi contenenti i valori attuali
	 */
	public Boolean getFlViewCurrent() {
		return flViewCurrent;
	}
	
	public void setVariazioniModels(List<OggettoBulk> variazioniModels) {
		this.variazioniModels = variazioniModels;
	}
	
	/*
	 * Modelli di variazioni compatibili con la rimodulazione.
	 * Se vuota vuol dire che la rimodulazione definitiva non prevede creazione di variazioni di bilancio.
	 * Lista che viene valorizzata dal BP sulle rimodulazioni di tipo definitivo.
	 */
	public List<OggettoBulk> getVariazioniModels() {
		return variazioniModels;
	}

	public boolean isROFieldRimodulazione() {
		return !this.isStatoProvvisorio();
	}
	
	/*
	 * Variabile che indica da quale anno il piano economico è attivo
	 * Valorizzata dal BP di rimodulazione e usata per la gestione degli errori
	 */
	public Integer getAnnoFromPianoEconomico() {
		return annoFromPianoEconomico;
	}
	
	public void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
		this.annoFromPianoEconomico = annoFromPianoEconomico;
	}
	
	/*
	 * Variabile che indica l'ultimo esercizio contabile aperto
	 * Valorizzata dal BP di rimodulazione e usata per la gestione degli errori
	 */
	public Integer getLastEsercizioAperto() {
		return lastEsercizioAperto;
	}
	
	public void setLastEsercizioAperto(Integer lastEsercizioAperto) {
		this.lastEsercizioAperto = lastEsercizioAperto;
	}
}
