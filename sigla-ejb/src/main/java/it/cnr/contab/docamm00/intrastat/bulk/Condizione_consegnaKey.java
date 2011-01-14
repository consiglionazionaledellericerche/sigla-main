package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_consegnaKey extends OggettoBulk implements KeyedPersistent {
	
	private java.lang.String cd_incoterm;
	private java.lang.Integer esercizio;

public Condizione_consegnaKey() {
	super();
}
public Condizione_consegnaKey(java.lang.String cd_incoterm,java.lang.Integer esercizio) {
	super();
	this.cd_incoterm = cd_incoterm;
	this.esercizio = esercizio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Condizione_consegnaKey)) return false;
	Condizione_consegnaKey k = (Condizione_consegnaKey)o;
	if(!compareKey(getCd_incoterm(),k.getCd_incoterm())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
public java.lang.String getCd_incoterm() {
	return cd_incoterm;
}
public void setCd_incoterm(java.lang.String cd_incoterm) {
	this.cd_incoterm = cd_incoterm;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}

public int primaryKeyHashCode() {
	return
	calculateKeyHashCode(getCd_incoterm())+
	calculateKeyHashCode(getEsercizio());
}
}
