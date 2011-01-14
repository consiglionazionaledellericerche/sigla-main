package it.cnr.contab.config00.latt.bulk;

import java.sql.PreparedStatement;
import java.util.List;

import it.cnr.contab.config00.blob.bulk.PostItBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class WorkpackageHome extends BulkHome {
/**
 * Costrutture linea di attività Home
 *
 * @param conn connessione db
 */
public WorkpackageHome(java.sql.Connection conn) {
	super(WorkpackageBulk.class,conn);
}
/**
 * Costrutture linea di attività Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public WorkpackageHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(WorkpackageBulk.class,conn,persistentCache);
}
/**
 * Cancella un oggetto persistente dalla base dati.
 * Se la linea di attività è comune, non è possibile cancellarla
 */
@Override
public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
	if (((WorkpackageBulk)persistent).getTipo_linea_attivita().getTi_tipo_la().equals(Tipo_linea_attivitaBulk.COMUNE))
	 throw new ApplicationPersistencyException("I GAE comuni non sono cancellabili!",persistent);	
	super.delete(persistent, userContext);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	try {
		WorkpackageBulk linea_attivita = (WorkpackageBulk)bulk;
		linea_attivita.setFl_limite_ass_obblig(Boolean.TRUE);
		if (!Tipo_linea_attivitaBulk.COMUNE.equals(linea_attivita.getTipo_linea_attivita().getTi_tipo_la())) {
			it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession lunghezzaChiavi = (it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Lunghezza_chiaviComponentSession",it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession.class);
			
			String aSuffix = linea_attivita.getTipo_linea_attivita().getTi_tipo_la();
			
			if (linea_attivita.getCd_linea_attivita() == null) {
				SQLBuilder sql = createSQLBuilder();

	// La gestione user delle linee di attività è limitata a quelle proprie.
	// Tali linee hanno una numerazione del tipo PXX..XX dove XX..XX è il progressivo
	// Devono essere inoltre gestite le linee di attività di tipo SISTEMA SXX..XX dove XX..XX è il progressivo
		
				sql.setHeader("SELECT MAX(CD_LINEA_ATTIVITA)");
				sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,linea_attivita.getCentro_responsabilita().getCd_centro_responsabilita());
				sql.addClause("AND","cd_linea_attivita",sql.LIKE,aSuffix+"%");
				PreparedStatement stm = sql.prepareStatement(getConnection());
				try {
					java.sql.ResultSet rs = stm.executeQuery();
					if (rs.next())
						linea_attivita.setCd_linea_attivita(rs.getString(1));
					try{rs.close();}catch( java.sql.SQLException e ){};
				} finally {
					try{stm.close();}catch( java.sql.SQLException e ){};
				}
				if (linea_attivita.getCd_linea_attivita() == null ||
					linea_attivita.getCd_linea_attivita().trim().length() == 0)
					linea_attivita.setCd_linea_attivita("1");
				else {
					findAndLock(linea_attivita);
					linea_attivita.setCd_linea_attivita(String.valueOf(Long.parseLong(linea_attivita.getCd_linea_attivita().replace(aSuffix.charAt(0),'0'))+1).replace('0',aSuffix.charAt(0)));
				}
			}
			linea_attivita.setCd_linea_attivita(lunghezzaChiavi.formatLinea_attivitaKey(userContext,linea_attivita.getCd_linea_attivita().replace(aSuffix.charAt(0),'0')));
			linea_attivita.setCd_linea_attivita(aSuffix+linea_attivita.getCd_linea_attivita().substring(1,linea_attivita.getCd_linea_attivita().length()));
			/*Se la linea di attività del PostIt è vuota la valorizzo*/
			for(int i = 0; linea_attivita.getDettagliPostIt().size() > i; i++) 
			{
			  if ((((PostItBulk)(linea_attivita.getDettagliPostIt().get(i))).getCd_linea_attivita()==null) ||
				 (((PostItBulk)(linea_attivita.getDettagliPostIt().get(i))).getCd_linea_attivita().compareTo(linea_attivita.getCd_linea_attivita())!=0))
			  {
				((PostItBulk)(linea_attivita.getDettagliPostIt().get(i))).setCd_linea_attivita(linea_attivita.getCd_linea_attivita());
			  }

			}
		}
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	} catch(java.rmi.RemoteException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	} catch(OutdatedResourceException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	} catch(BusyResourceException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	} catch(NumberFormatException e) {
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile fornire una numerazione automatica perchè esisitono codici assegnati manualmente non numerici",e);
	}catch(java.sql.SQLException e) {
		throw new PersistencyException(e);
	}
}
public java.util.Collection findDettagliPostIt(WorkpackageBulk testata) throws IntrospectionException, PersistencyException {
	PersistentHome dettHome = getHomeCache().getHome(PostItBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
	sql.addClause("AND","cd_linea_attivita",sql.EQUALS,testata.getCd_linea_attivita());
	return dettHome.fetchAll(sql);
}
public DipartimentoBulk findDipartimento( UserContext userContext, WorkpackageBulk linea )  throws it.cnr.jada.comp.ComponentException, PersistencyException  
{
	linea = (WorkpackageBulk)getHomeCache().getHome(WorkpackageBulk.class).findByPrimaryKey(linea);
	Progetto_sipHome prgHome = (Progetto_sipHome)getHomeCache().getHome(Progetto_sipBulk.class);
	Progetto_sipBulk modulo = new Progetto_sipBulk();
	modulo.setPg_progetto(linea.getProgetto().getPg_progetto());
	modulo = (Progetto_sipBulk)prgHome.findByPrimaryKey(userContext, modulo); 
	return prgHome.findDipartimento(userContext, modulo); 
}
	@Override
	public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception) throws ObjectNotFoundException {
	}
}
