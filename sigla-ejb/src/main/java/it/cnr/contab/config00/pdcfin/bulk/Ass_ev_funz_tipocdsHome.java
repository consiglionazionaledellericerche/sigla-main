package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class Ass_ev_funz_tipocdsHome extends BulkHome {
/**
 * Costruisce un Ass_ev_funz_tipocdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Ass_ev_funz_tipocdsHome(java.sql.Connection conn) {
	super(Ass_ev_funz_tipocdsBulk.class,conn);
}
/**
 * Costruisce un Ass_ev_funz_tipocdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Ass_ev_funz_tipocdsHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_ev_funz_tipocdsBulk.class,conn,persistentCache);
}
/**
 * Carica in una hashtable l'elenco di Funzioni presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */

public it.cnr.jada.util.OrderedHashtable loadFunzioni( ) throws IntrospectionException, PersistencyException 
{
	return new FunzioneHome( getConnection()).loadFunzioni();
	
	/*it.cnr.jada.util.OrderedHashtable oh = new it.cnr.jada.util.OrderedHashtable();
	oh.put("01", "01");
	oh.put("02", "02");
	oh.put("03", "03");
	oh.put("04", "04");
	oh.put("05", "05");
	oh.put("06", "06");	
	return oh;
	*/
}
/**
 * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */

public it.cnr.jada.util.OrderedHashtable loadTipiCds( ) throws IntrospectionException, PersistencyException, it.cnr.jada.comp.ApplicationException 
{
	return new Tipo_unita_organizzativaHome( getConnection()).loadTipologiaCdsKeys();
}
}
