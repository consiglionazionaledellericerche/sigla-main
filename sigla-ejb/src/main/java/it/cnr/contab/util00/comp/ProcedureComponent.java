package it.cnr.contab.util00.comp;

import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

public class ProcedureComponent extends it.cnr.jada.comp.GenericComponent {
/**
 * PingComponent constructor comment.
 */
public ProcedureComponent() {
	super();
}
/**
 * Aggiorna lo stato della variazione al PdG ad Approvazione Formale,
 * è stato inserito in questo EJB poichè questo apre una nuova transazione
 * 
 * @param userContext
 * @param list
 * @throws ComponentException
 */
public void aggiornaApprovazioneFormale(UserContext userContext, List list) throws ComponentException {
	Pdg_variazioneBulk var = null;
	if (list!=null && !list.isEmpty()){
		Iterator it = list.iterator();
		while(it.hasNext()) {
			var = (Pdg_variazioneBulk)it.next();
			var.setStato(Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			var.setDt_app_formale(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
			try {
				updateBulk(userContext, var);
			} catch (it.cnr.jada.persistency.PersistencyException e){
				throw new ComponentException(e);
			}
		}
	}
}
/**
 * Aggiorna lo stato della variazione al PdG ad Approvazione Formale,
 * è stato inserito in questo EJB poichè questo apre una nuova transazione
 * 
 * @param userContext
 * @param list
 * @throws ComponentException
 */
public void aggiornaApprovazioneFormale(UserContext userContext, CompoundFindClause clause) throws ComponentException {
	try {
		Pdg_variazioneBulk var = null;
		Pdg_variazioneHome home = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(clause);
		java.util.List list = getHome(userContext,Pdg_variazioneBulk.class).fetchAll(sql);
		Iterator it = list.iterator();
		while(it.hasNext()) { 
			var = (Pdg_variazioneBulk)it.next();
			var.setStato(Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			var.setDt_app_formale(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
			updateBulk(userContext, var);
		}
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	} catch (ComponentException e) {
		throw new ComponentException(e);
	}
}
/**
 * Aggiorna lo stato della variazione al PdG ad Approvazione Formale,
 * è stato inserito in questo EJB poichè questo apre una nuova transazione
 * 
 * @param userContext
 * @param list
 * @throws ComponentException
 */
public void aggiornaApponiVisto(UserContext userContext, List list, DipartimentoBulk dip) throws ComponentException {
	if (list!=null && !list.isEmpty()){
		Iterator it = list.iterator();
		while(it.hasNext()) {
			try {
				Utility.createPdGVariazioniComponentSession().apponiVistoDipartimento(userContext, (Pdg_variazioneBulk)it.next(), dip);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
		}
	}
}
/**
 * Aggiorna lo stato della variazione al PdG ad Approvazione Formale,
 * è stato inserito in questo EJB poichè questo apre una nuova transazione
 * 
 * @param userContext
 * @param list
 * @throws ComponentException
 */
public void aggiornaApponiVisto(UserContext userContext, CompoundFindClause clause, DipartimentoBulk dip) throws ComponentException {
	try {
		Pdg_variazioneHome home = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(clause);
		java.util.List list = getHome(userContext,Pdg_variazioneBulk.class).fetchAll(sql);
		Iterator it = list.iterator();
		while(it.hasNext())
			Utility.createPdGVariazioniComponentSession().apponiVistoDipartimento(userContext, (Pdg_variazioneBulk)it.next(), dip);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
}

}