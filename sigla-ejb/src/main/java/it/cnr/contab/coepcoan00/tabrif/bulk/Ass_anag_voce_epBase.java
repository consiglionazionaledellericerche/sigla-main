package it.cnr.contab.coepcoan00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_anag_voce_epBase extends Ass_anag_voce_epKey implements Keyed {
	// CD_VOCE_EP VARCHAR(45)
	private java.lang.String cd_voce_ep;

public Ass_anag_voce_epBase() {
	super();
}
public Ass_anag_voce_epBase(java.lang.String cd_classific_anag,java.lang.String ente_altro,java.lang.Integer esercizio,java.lang.String italiano_estero,java.lang.String ti_entita,java.lang.String ti_terzo) {
	super(cd_classific_anag,ente_altro,esercizio,italiano_estero,ti_entita,ti_terzo);
}
/* 
 * Getter dell'attributo cd_voce_ep
 */
public java.lang.String getCd_voce_ep() {
	return cd_voce_ep;
}
/* 
 * Setter dell'attributo cd_voce_ep
 */
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.cd_voce_ep = cd_voce_ep;
}
}
