package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.bulk.BulkList;

public class Progetto_piano_economicoBulk extends Progetto_piano_economicoBase {
	private Voce_piano_economico_prgBulk voce_piano_economico;
	private ProgettoBulk progetto;
	private BulkList vociBilancioAssociate = new BulkList();
	
	public Progetto_piano_economicoBulk() {
		super();
	}

	public Progetto_piano_economicoBulk(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano);
	}
	
	public Voce_piano_economico_prgBulk getVoce_piano_economico() {
		return voce_piano_economico;
	}
	
	public void setVoce_piano_economico(Voce_piano_economico_prgBulk voce_piano_economico) {
		this.voce_piano_economico = voce_piano_economico;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk voce_piano_economico = this.getVoce_piano_economico();
		if (voce_piano_economico == null)
			return null;
		return voce_piano_economico.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getVoce_piano_economico().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	@Override
	public String getCd_voce_piano() {
		it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk voce_piano_economico = this.getVoce_piano_economico();
		if (voce_piano_economico == null)
			return null;
		return voce_piano_economico.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getVoce_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}

	private V_saldi_piano_econom_progettoBulk saldoEntrata;
	
	private V_saldi_piano_econom_progettoBulk saldoSpesa;

	public V_saldi_piano_econom_progettoBulk getSaldoEntrata() {
		return saldoEntrata;
	}
	
	public void setSaldoEntrata(V_saldi_piano_econom_progettoBulk saldoEntrata) {
		this.saldoEntrata = saldoEntrata;
	}
	
	public V_saldi_piano_econom_progettoBulk getSaldoSpesa() {
		return saldoSpesa;
	}
	
	public void setSaldoSpesa(V_saldi_piano_econom_progettoBulk saldoSpesa) {
		this.saldoSpesa = saldoSpesa;
	}
	
	public java.math.BigDecimal getImTotaleSpesa() {
		return Optional.ofNullable(getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO));
	}
	
	public BulkList getVociBilancioAssociate() {
		return vociBilancioAssociate;
	}
	
	public void setVociBilancioAssociate(BulkList vociBilancioAssociate) {
		this.vociBilancioAssociate = vociBilancioAssociate;
	}
	
	public int addToVociBilancioAssociate(Ass_progetto_piaeco_voceBulk dett) {
		dett.setProgetto_piano_economico( this );
		vociBilancioAssociate.add(dett);
		return vociBilancioAssociate.size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {vociBilancioAssociate};
	}

	public Ass_progetto_piaeco_voceBulk removeFromVociBilancioAssociate(int index) {
		Ass_progetto_piaeco_voceBulk dett = (Ass_progetto_piaeco_voceBulk)vociBilancioAssociate.remove(index);
		return dett;
	}

}
