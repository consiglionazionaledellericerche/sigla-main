package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_rimodulazione_ppeBase extends Progetto_rimodulazione_ppeKey implements Keyed {
	// TI_OPERAZIONE CHAR(1) NOT NULL
	private java.lang.String ti_operazione;
	
	// IM_VAR_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVarEntrata;

	// IM_VAR_SPESA_FINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVarSpesaFinanziato;

	// IM_VAR_SPESA_COFINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVarSpesaCofinanziato;

	// IM_STOASS_SPESA_FINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imStoassSpesaFinanziato;

	// IM_STOASS_SPESA_COFINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imStoassSpesaCofinanziato;

	public Progetto_rimodulazione_ppeBase() {
		super();
	}
	
	public Progetto_rimodulazione_ppeBase(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super(pg_progetto, pg_rimodulazione, cd_unita_organizzativa, cd_voce_piano, esercizio_piano);
	}
	
	public java.lang.String getTi_operazione() {
		return ti_operazione;
	}
	
	public void setTi_operazione(java.lang.String ti_operazione) {
		this.ti_operazione = ti_operazione;
	}
	
	public java.math.BigDecimal getImVarEntrata() {
		return imVarEntrata;
	}

	public void setImVarEntrata(java.math.BigDecimal imVarEntrata) {
		this.imVarEntrata = imVarEntrata;
	}

	public java.math.BigDecimal getImVarSpesaFinanziato() {
		return imVarSpesaFinanziato;
	}

	public void setImVarSpesaFinanziato(java.math.BigDecimal imVarSpesaFinanziato) {
		this.imVarSpesaFinanziato = imVarSpesaFinanziato;
	}

	public java.math.BigDecimal getImVarSpesaCofinanziato() {
		return imVarSpesaCofinanziato;
	}

	public void setImVarSpesaCofinanziato(java.math.BigDecimal imVarSpesaCofinanziato) {
		this.imVarSpesaCofinanziato = imVarSpesaCofinanziato;
	}
	
	public java.math.BigDecimal getImStoassSpesaFinanziato() {
		return imStoassSpesaFinanziato;
	}
	
	public void setImStoassSpesaFinanziato(java.math.BigDecimal imStoassSpesaFinanziato) {
		this.imStoassSpesaFinanziato = imStoassSpesaFinanziato;
	}
	
	public java.math.BigDecimal getImStoassSpesaCofinanziato() {
		return imStoassSpesaCofinanziato;
	}
	
	public void setImStoassSpesaCofinanziato(java.math.BigDecimal imStoassSpesaCofinanziato) {
		this.imStoassSpesaCofinanziato = imStoassSpesaCofinanziato;
	}
}