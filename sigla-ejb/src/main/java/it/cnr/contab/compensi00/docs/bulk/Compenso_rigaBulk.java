package it.cnr.contab.compensi00.docs.bulk;

import java.util.Optional;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;

public class Compenso_rigaBulk extends Compenso_rigaBase {
	private static final long serialVersionUID = 1L;

	private CompensoBulk compenso = new CompensoBulk();
	private it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = new Obbligazione_scadenzarioBulk();
	private TrovatoBulk trovato = new TrovatoBulk();

	public Compenso_rigaBulk() {
		super();
	}

	public Compenso_rigaBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio,
			java.lang.Long pg_compenso, java.lang.Long progressivo_riga) {
		super(cd_cds, cd_unita_organizzativa, esercizio, pg_compenso, progressivo_riga);
		this.setCompenso(new CompensoBulk(cd_cds, cd_unita_organizzativa, esercizio, pg_compenso));
	}

	public CompensoBulk getCompenso() {
		return compenso;
	}
	
	public void setCompenso(CompensoBulk compenso) {
		this.compenso = compenso;
	}

	@Override
	public String getCd_cds() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getCd_cds)
				.orElse(null);
	}

	@Override
	public void setCd_cds(java.lang.String cd_cds) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setCd_cds(cd_cds));
	}

	@Override
	public String getCd_unita_organizzativa() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getCd_unita_organizzativa)
				.orElse(null);
	}
	
	@Override
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setCd_unita_organizzativa(cd_unita_organizzativa));
	}
	
	@Override
	public Integer getEsercizio() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getEsercizio)
				.orElse(null);
	}
	
	@Override
	public void setEsercizio(Integer esercizio) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setEsercizio(esercizio));
	}

	@Override
	public Long getPg_compenso() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getPg_compenso)
				.orElse(null);
	}

	@Override
	public void setPg_compenso(Long pg_compenso) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setPg_compenso(pg_compenso));
	}

	public Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
		return obbligazioneScadenzario;
	}
	
	public void setObbligazioneScadenzario(Obbligazione_scadenzarioBulk newObbligazioneScadenzario) {
		obbligazioneScadenzario = newObbligazioneScadenzario;
	}

	
	public String getCd_cds_obbligazione() {
		return Optional.ofNullable(this.getObbligazioneScadenzario())
					.map(Obbligazione_scadenzarioBulk::getObbligazione)
					.map(ObbligazioneBulk::getCds)
					.map(CdsBulk::getCd_unita_organizzativa)
					.orElse(null);
	}

	public java.lang.Integer getEsercizio_obbligazione() {
		return Optional.ofNullable(this.getObbligazioneScadenzario())
				.map(Obbligazione_scadenzarioBulk::getObbligazione)
				.map(ObbligazioneBulk::getEsercizio)
				.orElse(null);
	}

	public Integer getEsercizio_ori_obbligazione() {
		return Optional.ofNullable(this.getObbligazioneScadenzario())
				.map(Obbligazione_scadenzarioBulk::getObbligazione)
				.map(ObbligazioneBulk::getEsercizio_originale)
				.orElse(null);
	}

	public java.lang.Long getPg_obbligazione() {
		return Optional.ofNullable(this.getObbligazioneScadenzario())
				.map(Obbligazione_scadenzarioBulk::getObbligazione)
				.map(ObbligazioneBulk::getPg_obbligazione)
				.orElse(null);
	}

	public java.lang.Long getPg_obbligazione_scadenzario() {
		return Optional.ofNullable(this.getObbligazioneScadenzario())
				.map(Obbligazione_scadenzarioBulk::getPg_obbligazione_scadenzario)
				.orElse(null);
	}

	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
	}

	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setEsercizio(esercizio_obbligazione);
	}

	public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
	}

	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.getObbligazioneScadenzario().getObbligazione().setPg_obbligazione(pg_obbligazione);
	}

	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
		this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
	}

	public TrovatoBulk getTrovato() {
		return trovato;
	}

	public void setTrovato(TrovatoBulk trovato) {
		this.trovato = trovato;
	}

	public java.lang.Long getPg_trovato() {
		return Optional.ofNullable(this.getTrovato())
				.map(TrovatoBulk::getPg_trovato)
				.orElse(null);
	}

	public void setPg_trovato(java.lang.Long pg_trovato) {
		Optional.ofNullable(this.getTrovato()).ifPresent(el->el.setPg_trovato(pg_trovato));
	}
}
