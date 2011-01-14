package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_uoBulk extends Progetto_uoBase {

	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
public Progetto_uoBulk() {
	super();
}
public Progetto_uoBulk(java.lang.Integer pg_progetto,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo) {
	super(pg_progetto, uo.getCd_unita_organizzativa());
	setUnita_organizzativa(uo);
}

public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}

/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.45)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.45)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}