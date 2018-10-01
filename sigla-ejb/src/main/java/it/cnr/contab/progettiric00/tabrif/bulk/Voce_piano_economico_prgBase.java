package it.cnr.contab.progettiric00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Voce_piano_economico_prgBase extends Voce_piano_economico_prgKey implements Keyed {
	// DS_VOCE_PIANO VARCHAR(100) NOT NULL
	private java.lang.String ds_voce_piano;

	// TIPOLOGIA CHAR(3 BYTE) NULL
	private java.lang.String tipologia;

	//FL_VALIDO CHAR(1 BYTE) DEFAULT 'Y' NOT NULL
	private java.lang.Boolean fl_valido;

	public Voce_piano_economico_prgBase() {
		super();
	}
	
	public Voce_piano_economico_prgBase(java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano) {
		super(cd_unita_organizzativa, cd_voce_piano);
	}
	
	/* 
	 * Getter dell'attributo ds_voce_piano
	 */
	public java.lang.String getDs_voce_piano() {
		return ds_voce_piano;
	}
	
	/* 
	 * Setter dell'attributo ds_voce_piano
	 */
	public void setDs_voce_piano(java.lang.String ds_voce_piano) {
		this.ds_voce_piano = ds_voce_piano;
	}

	public java.lang.String getTipologia() {
		return tipologia;
	}
	
	public void setTipologia(java.lang.String tipologia) {
		this.tipologia = tipologia;
	}
	
	public java.lang.Boolean getFl_valido() {
		return fl_valido;
	}
	
	public void setFl_valido(java.lang.Boolean fl_valido) {
		this.fl_valido = fl_valido;
	}
}
