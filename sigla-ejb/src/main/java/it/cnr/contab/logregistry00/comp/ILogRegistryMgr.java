package it.cnr.contab.logregistry00.comp;

/**
 * Insert the type's description here.
 * Creation date: (30/09/2003 17.14.30)
 * @author: CNRADM
 */
public interface ILogRegistryMgr extends it.cnr.jada.comp.IRicercaMgr {
public it.cnr.jada.util.RemoteIterator cercaTabelleDiLog(
	it.cnr.jada.UserContext userContext,
	it.cnr.jada.persistency.sql.CompoundFindClause clausole,
	it.cnr.jada.bulk.OggettoBulk bulk)
	throws it.cnr.jada.comp.ComponentException;
}
