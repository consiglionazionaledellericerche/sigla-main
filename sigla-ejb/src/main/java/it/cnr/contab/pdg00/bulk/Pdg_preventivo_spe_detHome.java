package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_preventivo_spe_detHome extends BulkHome {
protected Pdg_preventivo_spe_detHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Pdg_preventivo_spe_detHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivo_spe_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Pdg_preventivo_spe_detHome(java.sql.Connection conn) {
	super(Pdg_preventivo_spe_detBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivo_spe_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Pdg_preventivo_spe_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Pdg_preventivo_spe_detBulk.class,conn,persistentCache);
}
/**
 * Ritorna una Vettore contenente il tipo di rapporto di dipendenza trovate in COSTO DEL DIPENDENTE
 * in corrispondenza della voce utilizzata nel dettaglio del PDG
 *
 * @param userContext contesto utente
 * @param pdg dettaglio del piano di gestione di spesa
 * @exception PersistencyException
 */

public java.util.Vector getVociStipendiali(
    it.cnr.jada.UserContext userContext,
    Pdg_preventivo_spe_detBulk pdg)
    throws it.cnr.jada.persistency.PersistencyException {
    java.util.Vector aV = new java.util.Vector();
    try {
    	LoggableStatement ps =
            new LoggableStatement(getConnection(),
                "select TI_RAPPORTO FROM "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    + "V_VOCI_STIPENDIALI "
                    + "WHERE ESERCIZIO = ? AND "
                    + "TI_PREV_CONS = ? AND "
                    + "CD_ELEMENTO_VOCE = ? ",true,this.getClass());
        ps.setObject(1, pdg.getEsercizio());
        ps.setString(2, "P");
        ps.setString(3, pdg.getElemento_voce().getCd_elemento_voce());

        java.sql.ResultSet aRS = ps.executeQuery();
        while (aRS.next()) {
            aV.add(aRS.getString(1));
        }
        return aV;
    } catch (java.sql.SQLException aSQL) {
        throw new it.cnr.jada.persistency.PersistencyException(aSQL);
    }
}
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano dettagli di spesa PDG
	 *
	 * @param det_etr dettaglio di spesa PDG
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk det_spe) throws PersistencyException {
		try {
			((Pdg_preventivo_spe_detBulk)det_spe).setPg_spesa(
				new Long(
					((Long)findAndLockMax( det_spe, "pg_spesa", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

}
