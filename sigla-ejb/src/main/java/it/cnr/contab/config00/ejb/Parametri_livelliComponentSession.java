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
public interface Parametri_livelliComponentSession
extends it.cnr.jada.ejb.CRUDComponentSession 
{
	boolean isParametriLivelliEtrEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isParametriLivelliSpeEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk getParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	/**
	 * Restituisce la descrizione del livello di Entrata/Spesa definito nei Parametri CNR
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	String getDescrizioneLivello(it.cnr.jada.UserContext param0, Integer param1, String param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;			
	String getDescrizioneLivelloContrSpese(it.cnr.jada.UserContext param0, Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
