package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_trasportoKey extends OggettoBulk implements KeyedPersistent {

	private java.lang.String cd_modalita_trasporto;
	
	private java.lang.Integer esercizio;

public Modalita_trasportoKey() {
	super();
}
public Modalita_trasportoKey(java.lang.String cd_modalita_trasporto,java.lang.Integer esercizio) {
	super();
	this.cd_modalita_trasporto = cd_modalita_trasporto;
	this.esercizio=esercizio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Modalita_trasportoKey)) return false;
	Modalita_trasportoKey k = (Modalita_trasportoKey)o;
	if(!compareKey(getCd_modalita_trasporto(),k.getCd_modalita_trasporto())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_modalita_trasporto())+
		calculateKeyHashCode(getEsercizio());
}
public java.lang.String getCd_modalita_trasporto() {
	return cd_modalita_trasporto;
}
public void setCd_modalita_trasporto(java.lang.String cd_modalita_trasporto) {
	this.cd_modalita_trasporto = cd_modalita_trasporto;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
}
