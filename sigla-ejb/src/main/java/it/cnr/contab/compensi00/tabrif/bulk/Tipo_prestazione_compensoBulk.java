package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

import java.util.*;

public class Tipo_prestazione_compensoBulk extends Tipo_prestazione_compensoBase {

public Tipo_prestazione_compensoBulk() {
	super();
}
public Tipo_prestazione_compensoBulk(java.lang.String cd_ti_prestazione) {
	super(cd_ti_prestazione);
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);
	resetFlags();

	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public void resetFlags(){
	this.setFl_incarico(new Boolean(false));
	this.setFl_contratto(new Boolean(false));
	this.setFl_controllo_fondi(new Boolean(false));
	}
}
