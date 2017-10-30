package it.cnr.contab.config00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrHome extends BulkHome {
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public Parametri_cnrHome(java.sql.Connection conn) {
		super(Parametri_cnrBulk.class, conn);
	}
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public Parametri_cnrHome(
		java.sql.Connection conn,
		PersistentCache persistentCache) {
		super(Parametri_cnrBulk.class, conn, persistentCache);
	}
	public Integer livelloCofogObbligatorio(it.cnr.jada.UserContext userContext) throws PersistencyException
	{
		Parametri_cnrBulk parametri = (Parametri_cnrBulk)findByPrimaryKey( new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
		if (parametri == null||parametri.getLivello_pdg_cofog()==null)
		  return 0;
		else
		  return parametri.getLivello_pdg_cofog();  
	
	}
	public boolean isNuovoPdg(UserContext userContext) throws PersistencyException{
		return isNuovoPdg(CNRUserContext.getEsercizio(userContext));
	}

	public boolean isNuovoPdg(int esercizio) throws PersistencyException{
        Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)findByPrimaryKey(new Parametri_cnrBulk(esercizio));
        return parametriCnr!=null && parametriCnr.getFl_nuovo_pdg();
	}	

	public Parametri_cnrBulk getParametriCnr(UserContext userContext) throws PersistencyException{
		return (Parametri_cnrBulk)findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
	}	
}
