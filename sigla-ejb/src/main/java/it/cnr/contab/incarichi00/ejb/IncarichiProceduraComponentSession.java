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

package it.cnr.contab.incarichi00.ejb;

import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Remote interface for Enterprise Bean: CNRINCARICHI00_EJB_IncarichiProceduraComponentSession
 */
@Remote
public interface IncarichiProceduraComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk pubblicaSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaPubblicazioneSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk stornaProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk chiudiProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal calcolaUtilizzato(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_procedura_annoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk concludiProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk concludiIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk caricaSedeUnitaOrganizzativa(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException; 
it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk getIncarichiParametri(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void salvaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
List getIncarichiForMigrateFromDBToCMIS(it.cnr.jada.UserContext param0, Integer param1, Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void migrateAllegatiFromDBToCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
List getIncarichiForMergeWithCMIS(it.cnr.jada.UserContext param0, Integer param1, Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List<String> mergeAllegatiWithCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaAllegati(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
