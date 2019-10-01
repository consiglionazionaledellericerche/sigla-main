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

package it.cnr.contab.util00.comp;
public interface IPingMgr {
Integer TIPO_PING_SERVER_ATTIVO = new Integer("0");
Integer TIPO_PING_LOGIN_ATTIVO = new Integer("1");
/**
 * Test attivazione server
 *  PreCondition: 
 *      Richiesto stato del server
 *  PostCondition:
 *      Effettua una query dummy per testare l'attivazione del meccanismo di accesso a DB e l'attivazione dell'EJB server.
 *
 * @return true se il ping ha successo
 */

public boolean ping(String hostname, Integer tipoPing);
}