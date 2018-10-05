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

	// IMPORTO_FIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importoFin;

	// STANZIAMENTO_FIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal stanziamentoFin;

	// VARIAPIU_FIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variapiuFin;

	// VARIAMENO_FIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variamenoFin;

	// IMPORTO_COFIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importoCofin;

	// STANZIAMENTO_COFIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal stanziamentoCofin;

	// VARIAPIU_COFIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variapiuCofin;

	// VARIAMENO_COFIN DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variamenoCofin;

	// IMPACC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal impacc;

	// MANRIS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal manris;

	public V_saldi_piano_econom_progettoBase() {
		super();
	}

	public java.math.BigDecimal getImportoFin() {
		return importoFin;
	}
	
	public void setImportoFin(java.math.BigDecimal importoFin) {
		this.importoFin = importoFin;
	}

	public java.math.BigDecimal getStanziamentoFin() {
		return stanziamentoFin;
	}

	public void setStanziamentoFin(java.math.BigDecimal stanziamentoFin) {
		this.stanziamentoFin = stanziamentoFin;
	}

	public java.math.BigDecimal getVariapiuFin() {
		return variapiuFin;
	}

	public void setVariapiuFin(java.math.BigDecimal variapiuFin) {
		this.variapiuFin = variapiuFin;
	}

	public java.math.BigDecimal getVariamenoFin() {
		return variamenoFin;
	}

	public void setVariamenoFin(java.math.BigDecimal variamenoFin) {
		this.variamenoFin = variamenoFin;
	}

	public java.math.BigDecimal getImportoCofin() {
		return importoCofin;
	}

	public void setImportoCofin(java.math.BigDecimal importoCofin) {
		this.importoCofin = importoCofin;
	}

	public java.math.BigDecimal getStanziamentoCofin() {
		return stanziamentoCofin;
	}

	public void setStanziamentoCofin(java.math.BigDecimal stanziamentoCofin) {
		this.stanziamentoCofin = stanziamentoCofin;
	}

	public java.math.BigDecimal getVariapiuCofin() {
		return variapiuCofin;
	}

	public void setVariapiuCofin(java.math.BigDecimal variapiuCofin) {
		this.variapiuCofin = variapiuCofin;
	}

	public java.math.BigDecimal getVariamenoCofin() {
		return variamenoCofin;
	}

	public void setVariamenoCofin(java.math.BigDecimal variamenoCofin) {
		this.variamenoCofin = variamenoCofin;
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

