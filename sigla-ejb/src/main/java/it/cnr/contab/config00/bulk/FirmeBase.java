package it.cnr.contab.config00.bulk;

import it.cnr.jada.persistency.Keyed;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */
public class FirmeBase extends FirmeKey implements Keyed {
	// FIRMA1 VARCHAR(50) NULL
	private String firma1;
	//	FIRMA2 VARCHAR(50) NULL
	 private String firma2;
	//	FIRMA3 VARCHAR(50) NULL
	 private String firma3;
	//	FIRMA4 VARCHAR(50) NULL
	 private String firma4;
	
	public FirmeBase() {
		super();
	}
	public FirmeBase(java.lang.Integer esercizio, String tipo) {
		super(esercizio,tipo);
	}

	/**
	 * @return
	 */
	public String getFirma1() {
		return firma1;
	}

	/**
	 * @return
	 */
	public String getFirma2() {
		return firma2;
	}

	/**
	 * @return
	 */
	public String getFirma3() {
		return firma3;
	}

	/**
	 * @return
	 */
	public String getFirma4() {
		return firma4;
	}

	/**
	 * @param string
	 */
	public void setFirma1(String string) {
		firma1 = string;
	}

	/**
	 * @param string
	 */
	public void setFirma2(String string) {
		firma2 = string;
	}

	/**
	 * @param string
	 */
	public void setFirma3(String string) {
		firma3 = string;
	}

	/**
	 * @param string
	 */
	public void setFirma4(String string) {
		firma4 = string;
	}

}
