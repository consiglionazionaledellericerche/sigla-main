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

import javax.ejb.Remote;

@Remote
public interface IncarichiRepertorioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk completaTerzo(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1, it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk stornaIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk chiudiIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk salvaDefinitivo(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal calcolaUtilizzato(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaDefinitivo(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean hasVariazioneIntegrazioneIncaricoProvvisoria(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaCancellazioneAssociazioneUo(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void salvaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaDatiPerla(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1,Long param2, String param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void comunicaPerla(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
