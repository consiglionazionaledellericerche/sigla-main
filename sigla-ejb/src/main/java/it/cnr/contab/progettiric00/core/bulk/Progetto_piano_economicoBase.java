package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_piano_economicoBase extends Progetto_piano_economicoKey implements Keyed {
	// IM_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata;

	// IM_SPESA_FINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_finanziato;

	// IM_SPESA_COFINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spesa_cofinanziato;

	//FL_CTRL-DISP CHAR(1 BYTE) DEFAULT 'Y' NOT NULL
	private java.lang.Boolean fl_ctrl_disp;

	public Progetto_piano_economicoBase() {
		super();
	}
	
	public Progetto_piano_economicoBase(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano);
	}
	
	public java.math.BigDecimal getIm_entrata() {
		return im_entrata;
	}
	
	public void setIm_entrata(java.math.BigDecimal im_entrata) {
		this.im_entrata = im_entrata;
	}
	
	public java.math.BigDecimal getIm_spesa_finanziato() {
		return im_spesa_finanziato;
	}
	
	public void setIm_spesa_finanziato(java.math.BigDecimal im_spesa_finanziato) {
		this.im_spesa_finanziato = im_spesa_finanziato;
	}

	public java.math.BigDecimal getIm_spesa_cofinanziato() {
		return im_spesa_cofinanziato;
	}
	
	public void setIm_spesa_cofinanziato(java.math.BigDecimal im_spesa_cofinanziato) {
		this.im_spesa_cofinanziato = im_spesa_cofinanziato;
	}
	
	public java.lang.Boolean getFl_ctrl_disp() {
		return fl_ctrl_disp;
	}
	
	public void setFl_ctrl_disp(java.lang.Boolean fl_ctrl_disp) {
		this.fl_ctrl_disp = fl_ctrl_disp;
	}

}
