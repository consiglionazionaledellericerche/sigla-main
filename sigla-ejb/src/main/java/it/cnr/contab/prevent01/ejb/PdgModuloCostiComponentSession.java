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

package it.cnr.contab.prevent01.ejb;

import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.jada.UserContext;

import javax.ejb.Remote;

@Remote
public interface PdgModuloCostiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
boolean esisteBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk calcolaPrevisioneAssestataRowByRow(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk param1,it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk param2,Integer param3)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaPdgEsercizio(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk getPdgModuloSpeseBulk(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean soggettaLimite(UserContext usercontext,Pdg_modulo_speseBulk pdg_modulo_spese,String fonte)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}