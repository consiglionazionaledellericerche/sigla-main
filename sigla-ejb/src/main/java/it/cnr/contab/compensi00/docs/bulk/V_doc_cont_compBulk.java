package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_cont_compBulk extends V_doc_cont_compBase {

	private CompensoBulk compenso;
	public final static java.lang.String TIPO_DOC_CONT_REVERSALE = "R";
	public final static java.lang.String TIPO_DOC_CONT_MANDATO = "M";
	public final static it.cnr.jada.util.OrderedHashtable TIPO_DOC_CONT;
	private it.cnr.contab.doccont00.core.bulk.IManRevBulk manRev;

	static {
		TIPO_DOC_CONT = new it.cnr.jada.util.OrderedHashtable();
		TIPO_DOC_CONT.put(TIPO_DOC_CONT_MANDATO,"Mandato");
		TIPO_DOC_CONT.put(TIPO_DOC_CONT_REVERSALE,"Reversale");
	}
public V_doc_cont_compBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 10.48.02)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public CompensoBulk getCompenso() {
	return compenso;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @return it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public it.cnr.contab.doccont00.core.bulk.IManRevBulk getManRev() {
	return manRev;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @return it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public String getTipoDocCont() {
	return (String)TIPO_DOC_CONT.get(getTipo_doc_cont());
}
/**
 * Insert the method's description here.
 * Creation date: (23/10/2002 12.55.12)
 * @return boolean
 */
public boolean isDocumentoPrincipale() {
	return "Y".equals(getPrincipale());
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 10.48.02)
 * @param newCompenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public void setCompenso(CompensoBulk newCompenso) {
	compenso = newCompenso;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @param newManRev it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public void setManRev(it.cnr.contab.doccont00.core.bulk.IManRevBulk newManRev) {
	manRev = newManRev;
}
}
