package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class Condizione_consegnaBulk extends Condizione_consegnaBase {

public Condizione_consegnaBulk() {
	super();
}
public Condizione_consegnaBulk(java.lang.String cd_incoterm,java.lang.Integer esercizio) {
	super(cd_incoterm,esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 11:42:49 AM)
 * @return boolean
 */
public boolean isROcondizione_consegna() {
	return getCrudStatus() == OggettoBulk.NORMAL;
}
@Override
public OggettoBulk initializeForInsert(CRUDBP crudbp,
		ActionContext actioncontext) {
	setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
	return super.initializeForInsert(crudbp, actioncontext);
}
@Override
	public OggettoBulk initializeForSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForSearch(crudbp, actioncontext);
	}
public String getCd_ds_condizione_consegna() {

	String result = "";
	result += ((getCd_incoterm() == null) ? "n.n." : getCd_incoterm()) + " - ";
	result += ((getDs_condizione_consegna() == null) ? "n.n." : getDs_condizione_consegna());
	return result;
}
}
