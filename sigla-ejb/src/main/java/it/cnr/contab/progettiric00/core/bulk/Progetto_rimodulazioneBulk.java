package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.jada.bulk.BulkList;

public class Progetto_rimodulazioneBulk extends Progetto_rimodulazioneBase {
	public static final String STATO_PROVVISORIO = "P";
	public static final String STATO_DEFINITIVO = "D";
	public static final String STATO_APPROVATO = "A";

	protected ProgettoBulk progetto;
	
	public final static Dictionary statoKeys;
	static {
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(STATO_PROVVISORIO,"Provvisorio");
		statoKeys.put(STATO_DEFINITIVO,"Definitivo");
		statoKeys.put(STATO_APPROVATO,"Approvato");
	};

	public final static Dictionary statoOfKeys;
	static {
		statoOfKeys = new it.cnr.jada.util.OrderedHashtable();
		statoOfKeys.put(Progetto_other_fieldBulk.STATO_INIZIALE,"Iniziale");
		statoOfKeys.put(Progetto_other_fieldBulk.STATO_NEGOZIAZIONE,"Negoziazione");
		statoOfKeys.put(Progetto_other_fieldBulk.STATO_APPROVATO,"Approvato");
		statoOfKeys.put(Progetto_other_fieldBulk.STATO_ANNULLATO,"Annullato");
		statoOfKeys.put(Progetto_other_fieldBulk.STATO_MIGRAZIONE,"Migrazione");
		statoOfKeys.put(ProgettoBulk.STATO_CHIUSURA,"Chiuso");
	};
	
	private BulkList<Progetto_rimodulazione_ppeBulk> dettagliRimodulazione = new BulkList<Progetto_rimodulazione_ppeBulk>();
	private BulkList<Progetto_rimodulazione_voceBulk> dettagliVoceRimodulazione = new BulkList<Progetto_rimodulazione_voceBulk>();

	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoTotale = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAnnoCorrente = new BulkList<Progetto_piano_economicoBulk>();
	private BulkList<Progetto_piano_economicoBulk> dettagliPianoEconomicoAltriAnni = new BulkList<Progetto_piano_economicoBulk>();

	private java.math.BigDecimal imFinanziatoRimodulato;
	private java.math.BigDecimal imCofinanziatoRimodulato;
	
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
		return STATO_PROVVISORIO.equals(this.getStato());
	}
	
	public boolean isStatoDefinitivo() {
		return STATO_DEFINITIVO.equals(this.getStato());
	}

	public boolean isStatoApprovato() {
		return STATO_APPROVATO.equals(this.getStato());
	}

	public Dictionary getStatoKeys() {
		return statoKeys;
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
		dett.setProgetto( this.getProgetto() );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAnnoCorrente.add(dett);
		return dettagliPianoEconomicoAnnoCorrente.size()-1;
	}

	public int addToDettagliPianoEconomicoAltriAnni(Progetto_piano_economicoBulk dett) {
		dett.setProgetto( this.getProgetto() );
		dett.setPg_progetto( getPg_progetto() );
		dettagliPianoEconomicoAltriAnni.add(dett);
		return dettagliPianoEconomicoAltriAnni.size()-1;
	}
	
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {dettagliRimodulazione,dettagliVoceRimodulazione};
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

						V_saldi_voce_progettoBulk saldoSpesa = new V_saldi_voce_progettoBulk();
						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						saldoSpesa.setStanziamentoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariapiuFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariamenoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfpiuFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfmenoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setStanziamentoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariapiuCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariamenoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfpiuCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfmenoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setImpaccFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setManrisFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setImpaccCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setManrisCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						
						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(saldoSpesa);
						bulk.addToVociBilancioAssociate(dettSpesa);

						V_saldi_voce_progettoBulk saldoEntrata = new V_saldi_voce_progettoBulk();
						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByVoce.get(vocePiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						saldoEntrata.setStanziamentoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariapiuFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariamenoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfpiuFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfmenoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setStanziamentoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariapiuCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariamenoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfpiuCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfmenoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setImpaccFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setManrisFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setImpaccCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setManrisCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));

						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(saldoEntrata);
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

						V_saldi_voce_progettoBulk saldoSpesa = new V_saldi_voce_progettoBulk();
						List<V_saldi_piano_econom_progettoBulk> listSaldiSpesa = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoSpesa).collect(Collectors.toList());
						saldoSpesa.setStanziamentoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariapiuFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariamenoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfpiuFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfmenoFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setStanziamentoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariapiuCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setVariamenoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfpiuCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setTrasfmenoCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setImpaccFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setManrisFin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setImpaccCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoSpesa.setManrisCofin(listSaldiSpesa.stream().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));

						Ass_progetto_piaeco_voceBulk dettSpesa = new Ass_progetto_piaeco_voceBulk();
						dettSpesa.setSaldoSpesa(saldoSpesa);
						bulk.addToVociBilancioAssociate(dettSpesa);

						V_saldi_voce_progettoBulk saldoEntrata = new V_saldi_voce_progettoBulk();
						List<V_saldi_piano_econom_progettoBulk> listSaldiEntrata = resultByEsercizio.get(esercizioPiano).stream()
								.filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
								.map(Progetto_piano_economicoBulk::getSaldoEntrata).collect(Collectors.toList());
						saldoEntrata.setStanziamentoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariapiuFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariamenoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfpiuFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfmenoFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setStanziamentoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariapiuCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setVariamenoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfpiuCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setTrasfmenoCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setImpaccFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setManrisFin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setImpaccCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
						saldoEntrata.setManrisCofin(listSaldiEntrata.stream().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));

						Ass_progetto_piaeco_voceBulk dettEntrata = new Ass_progetto_piaeco_voceBulk();
						dettEntrata.setSaldoEntrata(saldoEntrata);
						bulk.addToVociBilancioAssociate(dettEntrata);

						return bulk;
					})
				.collect(Collectors.toList()));
	}	

	public BulkList<V_saldi_voce_progettoBulk> getVociMovimentateNonAssociate() {
		return new BulkList<V_saldi_voce_progettoBulk>(
				this.getProgetto().getVociBilancioMovimentate().stream()
					.filter(voceMov->{
						return !this.getDettagliPianoEconomicoAnnoCorrente().stream()
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
	
}
