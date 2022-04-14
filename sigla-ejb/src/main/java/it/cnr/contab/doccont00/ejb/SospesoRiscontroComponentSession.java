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

package it.cnr.contab.doccont00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

@Remote
public interface SospesoRiscontroComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void cambiaStato(it.cnr.jada.UserContext param0,java.util.Collection param1,java.lang.String param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
RemoteIterator cercaSospesiPerStato(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String statoForSearch) throws ComponentException, RemoteException;
Integer caricamentoRigaGiornaleCassa(it.cnr.jada.UserContext param0, boolean tesoreriaUnica, EnteBulk cdsEnte, MovimentoContoEvidenzaBulk riga) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, RemoteException;
}
