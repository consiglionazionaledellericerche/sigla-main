package it.cnr.contab.utenze00.bulk;


public class Ruolo_bloccoBulk extends Ruolo_bloccoBase {

	RuoloBulk ruolo;

	public Ruolo_bloccoBulk() {
		super();
	}
	public Ruolo_bloccoBulk(java.lang.String cd_ruolo,java.lang.Integer esercizio) {
		super(cd_ruolo,esercizio);
	}
	public RuoloBulk getRuolo() {
		return ruolo;
	}
	public void setRuolo(RuoloBulk ruolo) {
		this.ruolo = ruolo;
	}
	public java.lang.String getCd_ruolo() {
		if (this.getRuolo() == null)
			return null;
		return ruolo.getCd_ruolo();
	}
	public void setCd_ruolo(java.lang.String cd_ruolo) {
		if (this.getRuolo() != null)
			this.getRuolo().setCd_ruolo(cd_ruolo);
	}
}
