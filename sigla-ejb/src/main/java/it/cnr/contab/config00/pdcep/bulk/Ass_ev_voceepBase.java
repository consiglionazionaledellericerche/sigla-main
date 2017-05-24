package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_voceepBase extends Ass_ev_voceepKey implements Keyed {

	
	private java.lang.String cd_voce_ep_contr;
public java.lang.String getCd_voce_ep_contr() {
		return cd_voce_ep_contr;
	}
	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep_contr) {
		this.cd_voce_ep_contr = cd_voce_ep_contr;
	}
public Ass_ev_voceepBase() {
	super();
}
public Ass_ev_voceepBase(java.lang.String cd_elemento_voce,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,cd_voce_ep,esercizio,ti_appartenenza,ti_gestione);
}
}
