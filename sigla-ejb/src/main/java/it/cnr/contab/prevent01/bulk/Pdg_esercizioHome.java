/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_esercizioHome extends BulkHome {
	public Pdg_esercizioHome(java.sql.Connection conn) {
		super(Pdg_esercizioBulk.class, conn);
	}
	public Pdg_esercizioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_esercizioBulk.class, conn, persistentCache);
	}

	public Pdg_esercizioBulk findEsercizioPrecedente( Pdg_esercizioBulk esercizioCorrente ) throws IntrospectionException, PersistencyException
	{
		Pdg_esercizioBulk esercizioPrecente = (Pdg_esercizioBulk)findByPrimaryKey( new Pdg_esercizioKey( new Integer(esercizioCorrente.getEsercizio().intValue() - 1), esercizioCorrente.getCd_centro_responsabilita()));
		return esercizioPrecente;
	
	}
	public Pdg_esercizioBulk findEsercizioSuccessivo( Pdg_esercizioBulk esercizioCorrente ) throws IntrospectionException, PersistencyException
	{
		return (Pdg_esercizioBulk)findByPrimaryKey( new Pdg_esercizioKey( new Integer( esercizioCorrente.getEsercizio().intValue() + 1), esercizioCorrente.getCd_centro_responsabilita()));
	
	}
}