package it.cnr.contab.docamm00.comp;


import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (04/06/2003 14.08.34)
 * @author: Roberto Peli
 */
public interface IRiportoDocAmmMgr {
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.09.12)
 * @return java.lang.String
 */
String getStatoRiporto(
	UserContext userContext,
	IDocumentoAmministrativoBulk documentoAmministrativo)
	throws ComponentException;
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.09.12)
 * @return java.lang.String
 */
String getStatoRiportoInScrivania(
	UserContext userContext,
	IDocumentoAmministrativoBulk documentoAmministrativo)
	throws ComponentException;
public IDocumentoAmministrativoBulk riportaAvanti(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm,
	OptionRequestParameter status)
	throws ComponentException;
public IDocumentoAmministrativoBulk riportaIndietro(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm) 
	throws ComponentException;
}
