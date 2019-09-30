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

package it.cnr.contab.docamm00.ejb;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.UserContext;

import javax.ejb.Remote;

@Remote
public interface CategoriaGruppoInventComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk findElementoVoce(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoce_f(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findAssVoceFList(UserContext aUC,Categoria_gruppo_inventBulk cgi) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
