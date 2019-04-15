package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.util.Utility;

public class Ass_progetto_piaeco_voceBulk extends Ass_progetto_piaeco_voceBase {
	private Progetto_piano_economicoBulk progetto_piano_economico;
	private Elemento_voceBulk elemento_voce;
	
	private V_saldi_voce_progettoBulk saldoEntrata;
	private V_saldi_voce_progettoBulk saldoSpesa;
	
	private boolean detailRimodulatoEliminato;
	private boolean detailRimodulatoAggiunto;

	private java.math.BigDecimal imVarFinanziatoRimodulato;
	private java.math.BigDecimal imVarCofinanziatoRimodulato;	
	
	private java.math.BigDecimal imVarFinanziatoRimodulatoPreDelete;
	private java.math.BigDecimal imVarCofinanziatoRimodulatoPreDelete;

	public Ass_progetto_piaeco_voceBulk() {
		super();
	}

	public Ass_progetto_piaeco_voceBulk(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano,
			java.lang.Integer esercizio_voce, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano, esercizio_voce, ti_appartenenza, ti_gestione, cd_elemento_voce);
	}

	public Progetto_piano_economicoBulk getProgetto_piano_economico() {
		return progetto_piano_economico;
	}
	
	public void setProgetto_piano_economico(Progetto_piano_economicoBulk progetto_piano_economico) {
		this.progetto_piano_economico = progetto_piano_economico;
	}
	
	@Override
	public Integer getPg_progetto() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getPg_progetto();
	}
	
	@Override
	public void setPg_progetto(Integer pg_progetto) {
		this.getProgetto_piano_economico().setPg_progetto(pg_progetto);
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getProgetto_piano_economico().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	@Override
	public String getCd_voce_piano() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getProgetto_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
	
	@Override
	public Integer getEsercizio_piano() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getEsercizio_piano();
	}
	
	@Override
	public void setEsercizio_piano(Integer esercizio_piano) {
		this.getProgetto_piano_economico().setEsercizio_piano(esercizio_piano);
	}
	
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	
	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}
	
	@Override
	public Integer getEsercizio_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	
	@Override
	public void setEsercizio_voce(Integer esercizio) {
		this.getElemento_voce().setEsercizio(esercizio);
	}

	@Override
	public String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	
	@Override
	public void setTi_appartenenza(String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	
	@Override
	public String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	
	@Override
	public void setTi_gestione(String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	
	@Override
	public String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	
	@Override
	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}

	public V_saldi_voce_progettoBulk getSaldoEntrata() {
		return saldoEntrata;
	}

	public void setSaldoEntrata(V_saldi_voce_progettoBulk saldoEntrata) {
		this.saldoEntrata = saldoEntrata;
	}

	public V_saldi_voce_progettoBulk getSaldoSpesa() {
		return saldoSpesa;
	}

	public void setSaldoSpesa(V_saldi_voce_progettoBulk saldoSpesa) {
		this.saldoSpesa = saldoSpesa;
	}
	
	public boolean isDetailRimodulatoEliminato() {
		return this.detailRimodulatoEliminato;
	}

	public void setDetailRimodulatoEliminato(boolean detailRimodulatoEliminato) {
		this.detailRimodulatoEliminato = detailRimodulatoEliminato;
	}
	
	public boolean isDetailRimodulatoAggiunto() {
		return this.detailRimodulatoAggiunto;
	}

	public void setDetailRimodulatoAggiunto(boolean detailRimodulatoAggiunto) {
		this.detailRimodulatoAggiunto = detailRimodulatoAggiunto;
	}

	public java.math.BigDecimal getImVarFinanziatoRimodulato() {
		return imVarFinanziatoRimodulato;
	}

	public void setImVarFinanziatoRimodulato(java.math.BigDecimal imVarFinanziatoRimodulato) {
		this.imVarFinanziatoRimodulato = imVarFinanziatoRimodulato;
	}

	public java.math.BigDecimal getImVarCofinanziatoRimodulato() {
		return imVarCofinanziatoRimodulato;
	}

	public void setImVarCofinanziatoRimodulato(java.math.BigDecimal imVarCofinanziatoRimodulato) {
		this.imVarCofinanziatoRimodulato = imVarCofinanziatoRimodulato;
	}
	
	public java.math.BigDecimal getImTotaleVarRimodulato() {
		return Optional.ofNullable(getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO));
	}
	
	public java.math.BigDecimal getImVarFinanziatoRimodulatoPreDelete() {
		return imVarFinanziatoRimodulatoPreDelete;
	}

	public void setImVarFinanziatoRimodulatoPreDelete(java.math.BigDecimal imVarFinanziatoRimodulatoPreDelete) {
		this.imVarFinanziatoRimodulatoPreDelete = imVarFinanziatoRimodulatoPreDelete;
	}

	public java.math.BigDecimal getImVarCofinanziatoRimodulatoPreDelete() {
		return imVarCofinanziatoRimodulatoPreDelete;
	}

	public void setImVarCofinanziatoRimodulatoPreDelete(java.math.BigDecimal imVarCofinanziatoRimodulatoPreDelete) {
		this.imVarCofinanziatoRimodulatoPreDelete = imVarCofinanziatoRimodulatoPreDelete;
	}

	public boolean isDetailRimodulato() {
		return this.isDetailRimodulatoEliminato() || this.isDetailRimodulatoAggiunto() ||
				Optional.ofNullable(this.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)!=0 ||
				Optional.ofNullable(this.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)!=0;
	}
	
	public boolean isDetailRimodulatoAnomalo() {
		return this.getSaldoSpesa().getDispAssestatoFinanziamento()
				   .add(Optional.ofNullable(this.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO))
				   .compareTo(BigDecimal.ZERO)<0 ||
			   this.getSaldoSpesa().getDispAssestatoCofinanziamento()
				   .add(Optional.ofNullable(this.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))
				   .compareTo(BigDecimal.ZERO)<0;
	}

	public String getMessageAnomaliaDetailRimodulato() {
		StringBuffer anomalia = new StringBuffer();
		anomalia = anomalia.append(
				Optional.ofNullable(this.getImVarFinanziatoRimodulato())
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.filter(el->this.getSaldoSpesa().getDispAssestatoFinanziamento().add(el).compareTo(BigDecimal.ZERO)<0)
				.map(el->
					"QUOTA FINANZIATA:" +
					"\rAssestato (" + new it.cnr.contab.util.EuroFormat().format(this.getSaldoSpesa().getAssestatoFinanziamento())+")"+
					" - Utilizzato (" + new it.cnr.contab.util.EuroFormat().format(this.getSaldoSpesa().getUtilizzatoAssestatoFinanziamento())+")"+
					" + Variazioni (" + new it.cnr.contab.util.EuroFormat().format(el)+")"+
					" = " + new it.cnr.contab.util.EuroFormat().format(el)+" - Valore negativo non consentito.\r\r")
				.orElse(""));
		anomalia = anomalia.append(
				Optional.ofNullable(this.getImVarCofinanziatoRimodulato())
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.filter(el->this.getSaldoSpesa().getDispAssestatoCofinanziamento().add(el).compareTo(BigDecimal.ZERO)<0)
				.map(el->
					"QUOTA COFINANZIATA:" +
					"\rAssestato (" + new it.cnr.contab.util.EuroFormat().format(this.getSaldoSpesa().getAssestatoCofinanziamento())+")"+
					" - Utilizzato (" + new it.cnr.contab.util.EuroFormat().format(this.getSaldoSpesa().getUtilizzatoAssestatoCofinanziamento())+")"+
					" + Variazioni (" + new it.cnr.contab.util.EuroFormat().format(el)+")"+
					" = " + new it.cnr.contab.util.EuroFormat().format(el)+" - Valore negativo non consentito.")
				.orElse(""));

		return Optional.ofNullable(anomalia).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	}
}

