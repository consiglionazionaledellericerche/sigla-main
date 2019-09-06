package it.cnr.contab.progettiric00.core.bulk;

import java.util.Optional;
import java.util.stream.Stream;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.util.OrderedHashtable;

public class Progetto_rimodulazione_voceBulk extends Progetto_rimodulazione_voceBase {
	public static final String TIPO_OPERAZIONE_MODIFICA = "M";
	public static final String TIPO_OPERAZIONE_AGGIUNTO = "A";
	public static final String TIPO_OPERAZIONE_ELIMINATO = "D";
	
	private Progetto_rimodulazioneBulk progettoRimodulazione;
	private Voce_piano_economico_prgBulk vocePianoEconomico;
	private Elemento_voceBulk elementoVoce;
	
	public Progetto_rimodulazione_voceBulk() {
		super();
	}

	public Progetto_rimodulazione_voceBulk(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano, java.lang.Integer pg_variazione) {
		super(pg_progetto, pg_rimodulazione, cd_unita_organizzativa, cd_voce_piano, esercizio_piano, pg_variazione);
	}
	
	public Progetto_rimodulazioneBulk getProgettoRimodulazione() {
		return progettoRimodulazione;
	}
	
	public void setProgettoRimodulazione(Progetto_rimodulazioneBulk progettoRimodulazione) {
		this.progettoRimodulazione = progettoRimodulazione;
	}
	
	@Override
	public Integer getPg_progetto() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_progetto).orElse(null);
	}
	
	@Override
	public void setPg_progetto(Integer pg_progetto) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_progetto(pg_progetto);	
		});
	}

	@Override
	public Integer getPg_rimodulazione() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_rimodulazione).orElse(null);
	}
	
	@Override
	public void setPg_rimodulazione(Integer pg_rimodulazione) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_rimodulazione(pg_rimodulazione);	
		});
	}

	public Voce_piano_economico_prgBulk getVocePianoEconomico() {
		return vocePianoEconomico;
	}
	
	public void setVocePianoEconomico(Voce_piano_economico_prgBulk vocePianoEconomico) {
		this.vocePianoEconomico = vocePianoEconomico;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return Optional.ofNullable(this.getVocePianoEconomico()).map(Voce_piano_economico_prgBulk::getCd_unita_organizzativa).orElse(null);
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		Optional.ofNullable(this.getVocePianoEconomico()).ifPresent(el->{
			el.setCd_unita_organizzativa(cd_unita_organizzativa);
		});
	}
	
	@Override
	public String getCd_voce_piano() {
		return Optional.ofNullable(this.getVocePianoEconomico()).map(Voce_piano_economico_prgBulk::getCd_voce_piano).orElse(null);
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		Optional.ofNullable(this.getVocePianoEconomico()).ifPresent(el->{
			el.setCd_voce_piano(cd_voce_piano);
		});
	}
	
	public Elemento_voceBulk getElementoVoce() {
		return elementoVoce;
	}
	
	public void setElementoVoce(Elemento_voceBulk elementoVoce) {
		this.elementoVoce = elementoVoce;
	}
	
	@Override
	public Integer getEsercizio_voce() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	
	@Override
	public void setEsercizio_voce(Integer esercizio) {
		this.getElementoVoce().setEsercizio(esercizio);
	}

	@Override
	public String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	
	@Override
	public void setTi_appartenenza(String ti_appartenenza) {
		this.getElementoVoce().setTi_appartenenza(ti_appartenenza);
	}
	
	@Override
	public String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	
	@Override
	public void setTi_gestione(String ti_gestione) {
		this.getElementoVoce().setTi_gestione(ti_gestione);
	}
	
	@Override
	public String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElementoVoce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	
	@Override
	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.getElementoVoce().setCd_elemento_voce(cd_elemento_voce);
	}

	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		OrderedHashtable list = new OrderedHashtable();
		for (int i=this.getProgettoRimodulazione().getProgetto().getAnnoFineOf().intValue();i>=this.getProgettoRimodulazione().getProgetto().getAnnoInizioOf();i--)
			list.put(new Integer(i), new Integer(i));
		if (this.getEsercizio_piano()!=null && list.get(this.getEsercizio_piano())==null)
			list.put(this.getEsercizio_piano(), this.getEsercizio_piano());
		list.remove(this.getProgettoRimodulazione().getProgetto().getEsercizio());
		Optional.ofNullable(this.getProgettoRimodulazione().getProgetto())
				.flatMap(el->Optional.ofNullable(el.getPdgModuli()))
				.map(el->el.stream())
				.orElse(Stream.empty())
				.filter(el->Optional.ofNullable(el.getStato()).map(stato->!stato.equals(Pdg_moduloBulk.STATO_AC)).orElse(Boolean.FALSE))
				.forEach(el->list.remove(el));
		return list;
	}
	
	public boolean isTiOperazioneAggiunto() {
		return TIPO_OPERAZIONE_AGGIUNTO.equals(this.getTi_operazione());
	}

	public boolean isTiOperazioneEliminato() {
		return TIPO_OPERAZIONE_ELIMINATO.equals(this.getTi_operazione());
	}
	
	public boolean isTiOperazioneModificato() {
		return TIPO_OPERAZIONE_MODIFICA.equals(this.getTi_operazione());
	}
}