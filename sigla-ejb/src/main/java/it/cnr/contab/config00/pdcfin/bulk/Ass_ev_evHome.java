package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

/**
 * Home che gestisce associazioni tra elementi voce.
 */
public class Ass_ev_evHome extends BulkHome {
protected Ass_ev_evHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Ass_ev_evHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_ev_evHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Ass_ev_evHome(java.sql.Connection conn) {
	super(Ass_ev_evBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_ev_evHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Ass_ev_evHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_ev_evBulk.class,conn,persistentCache);
}
/**
 * Carica in un dictionary l'elenco dei possibili valori che può
 * assumere la natura.
 * @param bulk L'OggettoBulk in uso.
 * @return I valori della natura.
 */
public Dictionary loadNaturaKeys(OggettoBulk bulk) throws ApplicationException 
{
	return new NaturaHome( getConnection()).loadNaturaKeys( bulk );
}
}
