package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ruolo_bloccoBase extends Ruolo_bloccoKey implements Keyed {

	public Ruolo_bloccoBase() {
		super();
	}
	public Ruolo_bloccoBase(java.lang.String cd_ruolo,java.lang.Integer esercizio) {
		super(cd_ruolo,esercizio);
	}

	// FL_ATTIVO CHAR(1)
	private java.lang.Boolean fl_attivo;
	
	public java.lang.Boolean getFl_attivo() {
		return fl_attivo;
	}
	public void setFl_attivo(java.lang.Boolean fl_attivo) {
		this.fl_attivo = fl_attivo;
	}
}
