/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_piano_econom_progettoBulk extends V_saldi_piano_econom_progettoBase {
	private static final long serialVersionUID = 5918224310476589096L;

	public V_saldi_piano_econom_progettoBulk() {
		super();
	}
	
	public java.math.BigDecimal getAssestato() {
		return getStanziamento().add(getVariapiu()).subtract(getVariameno());
	}

	public java.math.BigDecimal getDisp_residua() {
		return getDisp().subtract(getAssestato());
	}
}

