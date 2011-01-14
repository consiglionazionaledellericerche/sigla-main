package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_partner_esternoBase extends Progetto_partner_esternoKey implements Keyed {

	// IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;
	// NOTE VARCHAR(400)
	private java.lang.String note;
	// N_PERSONE NUMBER(9)
	private java.lang.Integer n_persone;	

public Progetto_partner_esternoBase() {
	super();
}
public Progetto_partner_esternoBase(java.lang.Integer pg_progetto,java.lang.Integer cd_partner_esterno) {
	super(pg_progetto,cd_partner_esterno);
}
/* 
 * Getter dell'attributo importo
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/* 
 * Setter dell'attributo importo
 */
public void setImporto(java.math.BigDecimal importo) {
	this.importo = importo;
}
	/**
	 * @return
	 */
	public java.lang.Integer getN_persone()
	{
		return n_persone;
	}

	/**
	 * @return
	 */
	public java.lang.String getNote()
	{
		return note;
	}

	/**
	 * @param integer
	 */
	public void setN_persone(java.lang.Integer integer)
	{
		n_persone = integer;
	}

	/**
	 * @param string
	 */
	public void setNote(java.lang.String string)
	{
		note = string;
	}

}