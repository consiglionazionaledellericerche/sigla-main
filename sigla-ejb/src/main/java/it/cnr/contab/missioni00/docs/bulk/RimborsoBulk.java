package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RimborsoBulk extends RimborsoBase 
{
	AnticipoBulk anticipo;
	
	private java.lang.String riportata = IDocumentoAmministrativoBulk.NON_RIPORTATO;
	private java.lang.String riportataInScrivania = IDocumentoAmministrativoBulk.NON_RIPORTATO;
	
	// Stati documento riportato
	public final static java.util.Dictionary STATI_RIPORTO;
	static
	{
		STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_RIPORTO.put(IDocumentoAmministrativoBulk.NON_RIPORTATO,"Non riportata");
		STATI_RIPORTO.put(IDocumentoAmministrativoBulk.PARZIALMENTE_RIPORTATO,"Parzialmente riportata");
		STATI_RIPORTO.put(IDocumentoAmministrativoBulk.COMPLETAMENTE_RIPORTATO,"Completamente riportata");
	}		
public RimborsoBulk() {
	super();
}
public RimborsoBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_rimborso) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_rimborso);
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2002 18.23.22)
 * @return it.cnr.contab.missioni00.docs.bulk.AnticipoBulk
 */
public AnticipoBulk getAnticipo() {
	return anticipo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2003 12.01.34)
 * @return java.lang.String
 */
public java.lang.String getRiportata() {
	return riportata;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2004 14.39.32)
 * @return java.lang.String
 */
public java.lang.String getRiportataInScrivania() {
	return riportataInScrivania;
}
public boolean isRiportata() 
{
	return !IDocumentoAmministrativoBulk.NON_RIPORTATO.equals(riportata);
}
public boolean isRiportataInScrivania() 
{
	return !IDocumentoAmministrativoBulk.NON_RIPORTATO.equals(riportataInScrivania);
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2002 18.23.22)
 * @param newAnticipo it.cnr.contab.missioni00.docs.bulk.AnticipoBulk
 */
public void setAnticipo(AnticipoBulk newAnticipo) {
	anticipo = newAnticipo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2003 12.01.34)
 * @param newRiportata java.lang.String
 */
public void setRiportata(java.lang.String newRiportata) {
	riportata = newRiportata;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2004 14.39.32)
 * @param newRiportataInScrivania java.lang.String
 */
public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
	riportataInScrivania = newRiportataInScrivania;
}
}
