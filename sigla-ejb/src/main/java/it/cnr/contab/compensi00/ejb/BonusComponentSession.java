package it.cnr.contab.compensi00.ejb;


import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.Remote;

import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface BonusComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {

	void checkCodiceFiscale(UserContext userContext, BonusBulk bonus)throws ComponentException, RemoteException, SQLException ;

	void checkCodiceFiscaleComponente(UserContext userContext,
			Bonus_nucleo_famBulk bonus_nucleo_fam)throws ComponentException, RemoteException, SQLException ;
	BonusBulk recuperoDati(UserContext context, BonusBulk bonus)throws ComponentException, RemoteException, SQLException ;

	Boolean verificaLimiteFamiliareCarico(UserContext userContext,
			Bonus_nucleo_famBulk detail)throws ComponentException, RemoteException ;

	BonusBulk completaBonus(UserContext userContext, BonusBulk bonus)throws ComponentException, RemoteException ;

	CompensoBulk cercaCompensoPerBonus(UserContext userContext, BonusBulk bonus)throws ComponentException, RemoteException ;
	
	java.util.List estraiLista(UserContext context)	throws ComponentException, PersistencyException,RemoteException;
	java.util.List estraiDettagli(UserContext context,BonusBulk bulk)	throws ComponentException, PersistencyException,RemoteException;
	
	String recuperaCodiceFiscaleInvio(UserContext context) throws ComponentException, RemoteException ;

	void aggiornaInvio(UserContext userContext) throws ComponentException, RemoteException,PersistencyException ;
}
