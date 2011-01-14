/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/05/2009
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseHome;
import it.cnr.contab.config00.pdcfin.bulk.Ass_ev_evBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroHome;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stampa_attivita_siglaHome extends BulkHome {
	public Stampa_attivita_siglaHome(Connection conn) {
		super(Attivita_siglaBulk.class, conn);
	}
	public Stampa_attivita_siglaHome(Connection conn, PersistentCache persistentCache) {
		super(Attivita_siglaBulk.class, conn, persistentCache);
	}
	
		
		public Collection findEsercizi(Stampa_attivita_siglaBulk bulk, Esercizio_baseHome h) throws PersistencyException, IntrospectionException {
				return h.findEsercizi(bulk);
	}
	
}
