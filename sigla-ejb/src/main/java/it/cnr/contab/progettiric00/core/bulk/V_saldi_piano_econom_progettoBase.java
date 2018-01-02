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
	private static final long serialVersionUID = 5918224310476589096L;

	// DISP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal disp;

	// STANZIAMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal stanziamento;

	// VARIAPIU DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variapiu;

	// VARIAMENO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variameno;

	// IMPACC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal impacc;

	// MANRIS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal manris;

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

	public java.math.BigDecimal getImpacc() {
		return impacc;
	}
	
	public void setImpacc(java.math.BigDecimal impacc) {
		this.impacc = impacc;
	}
	
	public java.math.BigDecimal getManris() {
		return manris;
	}
	
	public void setManris(java.math.BigDecimal manris) {
		this.manris = manris;
	}
}

