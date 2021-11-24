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

package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.bulk.RicercaContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.Remote;

@Remote
public interface ContrattoComponentSession
	extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.config00.contratto.bulk.ContrattoBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.config00.contratto.bulk.ContrattoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaContrattoCessato(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaCancellazioneAssociazioneUo(it.cnr.jada.UserContext param0,it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.contratto.bulk.ContrattoBulk initializzaUnita_Organizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.contratto.bulk.ContrattoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaContrattiWS(it.cnr.jada.UserContext userContext,String uo,String tipo,String query,String dominio,String tipoRicerca)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaContrattiSIP(it.cnr.jada.UserContext userContext,RicercaContrattoBulk bulk)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
RemoteIterator findListaContrattiElenco(UserContext userContext,String query,String dominio,Integer anno,String cdCds,String order,String strRicerca) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
RemoteIterator findContrattoByCig(UserContext userContext, ContrattoBulk contratto, CigBulk cig)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
ContrattoBulk calcolaTotDocCont (UserContext userContext,ContrattoBulk contratto) throws ComponentException,java.rmi.RemoteException;
ContrattoBulk creaContrattoDaFlussoAcquisti(UserContext userContext, ContrattoBulk contratto,boolean statoDefinitivo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaAllegati(UserContext userContext, ContrattoBulk contratto) throws ComponentException;
}
