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

import javax.ejb.Remote;

@Remote
public interface Classificazione_voci_epComponentSession extends it.cnr.jada.ejb.CRUDComponentSession 
{
	it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcep.cla.bulk.Classificazione_voci_epBulk caricaClassVociAssociate(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcep.cla.bulk.Classificazione_voci_epBulk param1, it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcep.cla.bulk.Classificazione_voci_epBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2, java.lang.Integer param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
