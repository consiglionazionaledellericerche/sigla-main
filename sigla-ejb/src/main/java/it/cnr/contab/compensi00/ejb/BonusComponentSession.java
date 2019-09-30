/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
