/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;
/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_piano_econom_progettoBase extends V_saldi_piano_econom_progettoKey implements Keyed {
	// DISP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal disp;

	// STANZIAMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal stanziamento;

	// VARIAPIU DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variapiu;

	// VARIAMENO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variameno;

	// ASSESTATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal assestato;

	// DISP_RESIDUA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal disp_residua;

	public V_saldi_piano_econom_progettoBase() {
		super();
	}

	public java.math.BigDecimal getDisp() {
		return disp;
	}

	public void setDisp(java.math.BigDecimal disp) {
		this.disp = disp;
	}

	public java.math.BigDecimal getStanziamento() {
		return stanziamento;
	}

	public void setStanziamento(java.math.BigDecimal stanziamento) {
		this.stanziamento = stanziamento;
	}

	public java.math.BigDecimal getVariapiu() {
		return variapiu;
	}

	public void setVariapiu(java.math.BigDecimal variapiu) {
		this.variapiu = variapiu;
	}

	public java.math.BigDecimal getVariameno() {
		return variameno;
	}

	public void setVariameno(java.math.BigDecimal variameno) {
		this.variameno = variameno;
	}

	public java.math.BigDecimal getAssestato() {
		return assestato;
	}

	public void setAssestato(java.math.BigDecimal assestato) {
		this.assestato = assestato;
	}

	public java.math.BigDecimal getDisp_residua() {
		return disp_residua;
	}

	public void setDisp_residua(java.math.BigDecimal disp_residua) {
		this.disp_residua = disp_residua;
	}
}

