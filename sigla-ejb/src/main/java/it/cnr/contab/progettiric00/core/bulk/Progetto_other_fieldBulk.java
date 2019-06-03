package it.cnr.contab.progettiric00.core.bulk;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;

public class Progetto_other_fieldBulk extends Progetto_other_fieldBase {

    public final static Map<String,String> statoKeys = Arrays.asList(StatoProgetto.values())
            .stream()
            .collect(Collectors.toMap(
            		StatoProgetto::value,
            		StatoProgetto::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    
	private Pdg_programmaBulk pdgProgramma;
	
	private Pdg_missioneBulk pdgMissione;
	
	private TipoFinanziamentoBulk tipoFinanziamento;
	
	public Progetto_other_fieldBulk() {
		super();
	}

	public Progetto_other_fieldBulk(java.lang.Integer pg_progetto) {
		super(pg_progetto);
	}
	
	public Pdg_programmaBulk getPdgProgramma() {
		return pdgProgramma;
	}
	
	public void setPdgProgramma(Pdg_programmaBulk pdgProgramma) {
		this.pdgProgramma = pdgProgramma;
	}
	@Override
	public String getCd_programma() {
		Pdg_programmaBulk pdgProgramma = this.getPdgProgramma();
		if (pdgProgramma == null)
			return null;
		return pdgProgramma.getCd_programma();
	}
	
	@Override
	public void setCd_programma(String cd_programma) {
		this.getPdgProgramma().setCd_programma(cd_programma);
	}

	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	@Override
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione = this.getPdgMissione();
		if (pdgMissione == null)
			return null;
		return pdgMissione.getCd_missione();
	}
	
	@Override
	public void setCd_missione(String cd_missione) {
		this.getPdgMissione().setCd_missione(cd_missione);
	}
	
	public TipoFinanziamentoBulk getTipoFinanziamento() {
		return tipoFinanziamento;
	}
	
	public void setTipoFinanziamento(TipoFinanziamentoBulk tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}
	
	@Override
	public Long getIdTipoFinanziamento() {
		TipoFinanziamentoBulk tipoFinanziamento = this.getTipoFinanziamento();
		if (tipoFinanziamento == null)
			return null;
		return tipoFinanziamento.getId();
	}
	
	@Override
	public void setIdTipoFinanziamento(Long idTipoFinanziamento) {
		this.getTipoFinanziamento().setId(idTipoFinanziamento);
	}
	
	public boolean isStatoIniziale() {
		return StatoProgetto.STATO_INIZIALE.value().equals(this.getStato());
	}
	
	public boolean isStatoNegoziazione() {
		return StatoProgetto.STATO_NEGOZIAZIONE.value().equals(this.getStato());
	}

	public boolean isStatoApprovato() {
		return StatoProgetto.STATO_APPROVATO.value().equals(this.getStato());
	}

	public boolean isStatoAnnullato() {
		return StatoProgetto.STATO_ANNULLATO.value().equals(this.getStato());
	}

	public boolean isStatoMigrazione() {
		return StatoProgetto.STATO_MIGRAZIONE.value().equals(this.getStato());
	}

	/*
	 * Il progetto è chiuso se non è obbligatorio indicare le date 
	 * e la data fine risulta essere valorizzata
	 */
	public boolean isStatoChiuso() {
		return Optional.ofNullable(this.getTipoFinanziamento()).map(TipoFinanziamentoBulk::getCodice).isPresent() &&
			   Optional.ofNullable(this.getDtFine()).isPresent() &&
			   !isDatePianoEconomicoRequired();
	}

	/*
	 * Indica che è obbligatorio indicare le date del progetto
	 */
	public boolean isDatePianoEconomicoRequired() {
		return this.isPianoEconomicoRequired();
	}
	
	/*
	 * Indica se è obbligatorio inserire il piano economico sulla base della tipologia 
	 * di finanziamento associata
	 */
	public boolean isPianoEconomicoRequired() {
		return Optional.ofNullable(this.getTipoFinanziamento())
				.flatMap(el->Optional.ofNullable(el.getFlPianoEcoFin()))
				.orElse(Boolean.FALSE);
	}
	
	public void validaDateProgetto() throws ValidationException {
		if (!Optional.ofNullable(this.getDtInizio()).isPresent() && Optional.ofNullable(this.getDtFine()).isPresent())  
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di fine\" senza indicare la \"Data di inizio\".");
		if (!Optional.ofNullable(this.getDtFine()).isPresent() && Optional.ofNullable(this.getDtProroga()).isPresent())  
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di proroga\" senza indicare la \"Data di fine\".");
		if (Optional.ofNullable(this.getDtInizio()).isPresent() && Optional.ofNullable(this.getDtFine()).isPresent() &&
			this.getDtInizio().after(this.getDtFine()))
			throw new ValidationException( "La \"Data di fine\" del progetto deve essere uguale o superiore alla \"Data di inizio\".");
		if (Optional.ofNullable(this.getDtFine()).isPresent() && Optional.ofNullable(this.getDtProroga()).isPresent() &&
				this.getDtFine().after(this.getDtProroga()))
			throw new ValidationException( "La \"Data di proroga\" del progetto deve essere uguale o superiore alla \"Data di fine\".");
	}

	public Integer getAnnoInizio() {
		return Optional.ofNullable(this.getDtInizio())
				.map(elDate->{
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime(elDate);
					return calendar.get(Calendar.YEAR);
				})
				.orElse(0);
	}
	
	public Integer getAnnoFine() {
		Optional<Integer> anno = Optional.empty();
		if (Optional.ofNullable(this.getDtFine()).isPresent() || 
				Optional.ofNullable(this.getDtProroga()).isPresent()) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.max(this.getDtFine(), this.getDtProroga()));
			anno = Optional.of(calendar.get(Calendar.YEAR));
		}
		return anno.orElse(9999);
	}
	
	/**
	 * Ritorna la data di fine effettiva del progetto calcolata come confronto tra data fine e data proroga
	 * @return
	 */
	public java.sql.Timestamp getDtFineEffettiva() {
		return Optional.ofNullable(this.getDtProroga()).orElse(this.getDtFine());
	}
}
