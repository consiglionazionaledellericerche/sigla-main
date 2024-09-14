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

import javax.ejb.Remote;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.GenericComponentSession;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
@Remote
public interface ConsDispCompetenzaResiduoIstitutoComponentSession extends GenericComponentSession {
	it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.DetailedRuntimeException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator findConsultazioneCdrGae(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.jada.util.RemoteIterator findConsultazioneVoce(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.jada.util.RemoteIterator findConsultazioneDip(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator findConsultazioneVoceNat(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator findConsultazioneEntrateCdrGae(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.jada.util.RemoteIterator findConsultazioneEntrateVoce(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}

