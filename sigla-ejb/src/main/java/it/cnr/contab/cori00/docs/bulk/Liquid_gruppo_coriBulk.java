package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_coriBulk extends Liquid_gruppo_coriBase {

	private Liquid_coriBulk liquidazione_cori;

public Liquid_gruppo_coriBulk() {
	super();
}
public Liquid_gruppo_coriBulk(java.lang.String cd_cds,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione) {
	super(cd_cds,cd_gruppo_cr,cd_regione,esercizio,pg_comune,pg_liquidazione);
}
/**
 * Insert the method's description here.
 * Creation date: (14/06/2002 15.43.22)
 * @return it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk
 */
public Liquid_coriBulk getLiquidazione_cori() {
	return liquidazione_cori;
}
/**
 * Insert the method's description here.
 * Creation date: (14/06/2002 15.43.22)
 * @param newLiquidazione_cori it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk
 */
public void setLiquidazione_cori(Liquid_coriBulk newLiquidazione_cori) {
	liquidazione_cori = newLiquidazione_cori;
}
}
