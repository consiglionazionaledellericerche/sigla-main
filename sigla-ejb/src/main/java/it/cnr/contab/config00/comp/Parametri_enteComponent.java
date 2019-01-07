/*
 * Created on May 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Optional;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.config00.bulk.ServizioPecBulk;
import it.cnr.contab.config00.bulk.ServizioPecHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Parametri_enteComponent extends CRUDComponent {

	private static final long serialVersionUID = 1L;
	
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk oggettobulk)
		throws ComponentException
	{
		if(oggettobulk instanceof Parametri_enteBulk)
			validaRiga(userContext,(Parametri_enteBulk)oggettobulk);
		return super.creaConBulk(userContext, oggettobulk);
	}
		
	public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk oggettobulk)
		throws ComponentException{
			if(oggettobulk instanceof Parametri_enteBulk)
				validaRiga(userContext,(Parametri_enteBulk)oggettobulk);
		return super.modificaConBulk(userContext, oggettobulk);  
	}	
	
	/**
	 * Valida i campi obbligatori
	 * @param uc
	 * @param bulk
	 * @throws ComponentException
	 */
	private void validaCampiObbligatori(UserContext uc, Parametri_enteBulk bulk) throws ComponentException{
		if(bulk.getDescrizione() == null)
		  throw new ApplicationException("Valorizzare la descrizione dell'Ente");
	}

	/**
	 * Valida la riga di Parametri Ente
	 * @param uc
	 * @param bulk
	 * @throws ComponentException
	 */
	private void validaRiga(UserContext userContext, Parametri_enteBulk parametri_ente) throws ComponentException{
	  try
	  {
		validaCampiObbligatori(userContext,parametri_ente);
		
	   	Parametri_enteHome testataHome = (Parametri_enteHome)getHome(userContext, Parametri_enteBulk.class);
		if (parametri_ente.isToBeUpdated()) {
			Parametri_enteBulk parametriEnteDB = (Parametri_enteBulk)testataHome.findByPrimaryKey(parametri_ente);
			if (!parametriEnteDB.getFl_informix().equals(parametri_ente.getFl_informix()))
				throw new ApplicationException("Attenzione! Non è possibile modificare il flag \"Collegamento a Informix\".");
			if (parametriEnteDB.getFl_gae_es() && !parametri_ente.getFl_gae_es())
				throw new ApplicationException("Attenzione! Non è possibile modificare il flag \"Gestione GAE E/S\".");
		}				

		int totAttive = 0;
		
		if(parametri_ente.getAttivo().booleanValue()) {
			SQLBuilder sql = testataHome.createSQLBuilder();
			sql.addSQLClause("AND","ATTIVO",SQLBuilder.EQUALS,"Y");
			sql.addSQLClause("AND","ID", SQLBuilder.NOT_EQUALS, parametri_ente.getId());

			try {
				totAttive = sql.executeCountQuery(testataHome.getConnection()) + 1;
			} catch (java.sql.SQLException e) {
				throw handleSQLException(e);
			}
		}
		if (totAttive > 1)
		  throw new ApplicationException("Attenzione! E' gia' presente una riga con stato " +
		  "attivo. Modificare il valore e ripetere l'operazione.");
	  }
	  catch ( Exception e )
	  {
		throw handleException( e );
	  }	
	}
	public Parametri_enteBulk getParametriEnte(UserContext userContext) throws ComponentException{
		try{
			return ((Parametri_enteHome)getHome(userContext, Parametri_enteBulk.class)).getParametriEnteAttiva();
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}	
	}
/**
 * numero dei giorni rimanenti entro i quali si deve effettuare il login su LDAP
 * @param userContext
 * @return
 * @throws ComponentException
 */
	public int getGiorniRimanenti(UserContext userContext) throws ComponentException{
		try{
		   	Parametri_enteHome testataHome = (Parametri_enteHome)getHome(userContext, Parametri_enteBulk.class);
			SQLBuilder sql = testataHome.createSQLBuilder();
			sql.addSQLClause("AND","ATTIVO",SQLBuilder.EQUALS,"Y");

			getHomeCache(userContext).fetchAll(userContext,testataHome);
			Parametri_enteBulk ente = (Parametri_enteBulk) getHome(userContext, Parametri_enteBulk.class).fetchAll(sql).get(0);

			if (ente.getDt_ldap_migrazione()==null)
				return 0;
			
			UtenteHome home = (UtenteHome)getHome(userContext,UtenteBulk.class);
			java.sql.Timestamp currDate = home.getServerTimestamp();

			Calendar endDate = Calendar.getInstance();
			endDate.setTime(ente.getDt_ldap_migrazione());
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(currDate);

			int ended = endDate.compareTo(startDate);

			if (ended<0)
				return 0;

			int days = endDate.get(Calendar.DAY_OF_YEAR)-startDate.get(Calendar.DAY_OF_YEAR);

			if (days<0)
				return 0;
			
			return days;

		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}	
	}
	public ServizioPecBulk getServizioPec(UserContext userContext, String cd_servizio_pec) throws ComponentException{
		try{
		   	ServizioPecHome testataHome = (ServizioPecHome)getHome(userContext, ServizioPecBulk.class);
		   	ServizioPecBulk servizioPec = (ServizioPecBulk)testataHome.findByPrimaryKey(new ServizioPecBulk(cd_servizio_pec));

			return servizioPec;	
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
	public CdsBulk getCds(UserContext userContext, String cd_cds) throws ComponentException{
		try{
		   	CdsHome home = (CdsHome)getHome(userContext, CdsBulk.class);
		   	CdsBulk cds = (CdsBulk)home.findByPrimaryKey(new CdsBulk(cd_cds));

			return cds;	
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
	public Unita_organizzativaBulk getUo(UserContext userContext, String cd_uo) throws ComponentException{
		try{
			Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		   	Unita_organizzativaBulk uo = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

			return uo;	
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
	public CdrBulk getCdr(UserContext userContext, String cd_cdr) throws ComponentException{
		try{
		   	CdrHome cdrHome = (CdrHome)getHome(userContext, CdrBulk.class);
			SQLBuilder sqlCdr = cdrHome.createSQLBuilderEsteso();
			sqlCdr.addClause("AND", "cd_centro_responsibilita", SQLBuilder.EQUALS, cd_cdr);
			Broker brokerCdr = cdrHome.createBroker(sqlCdr);
			if (brokerCdr.next()){
				return (CdrBulk)brokerCdr.fetch(CdrBulk.class);
			}
			return null;	
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
	public UnitaOrganizzativaPecBulk getUnitaOrganizzativaPec(UserContext userContext, String cd_uo) throws ComponentException{
		try{
		   	UnitaOrganizzativaPecHome testataHome = (UnitaOrganizzativaPecHome)getHome(userContext, UnitaOrganizzativaPecBulk.class);
		   	UnitaOrganizzativaPecBulk uoPec = (UnitaOrganizzativaPecBulk)testataHome.findByPrimaryKey(new UnitaOrganizzativaPecBulk(cd_uo));

			return uoPec;	
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}
	
	public boolean isProgettoPianoEconomicoEnabled(UserContext userContext, int esercizio) throws ComponentException{
		try{
			Parametri_enteBulk parametriEnte = getParametriEnte(userContext);
			if (Optional.ofNullable(parametriEnte).map(el->el.getFl_prg_pianoeco()).orElse(Boolean.FALSE)) {
				BigDecimal annoFrom = Utility.createConfigurazioneCnrComponentSession().getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
				return Optional.ofNullable(annoFrom).map(BigDecimal::intValue).map(el->el.compareTo(esercizio)<=0).orElse(Boolean.FALSE);
			}
			return Boolean.FALSE;
		}catch(RemoteException ex){
			throw handleException(ex);
		}
	}
}
