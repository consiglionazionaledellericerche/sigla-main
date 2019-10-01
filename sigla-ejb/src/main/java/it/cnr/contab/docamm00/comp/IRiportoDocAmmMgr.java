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

package it.cnr.contab.docamm00.comp;


import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (04/06/2003 14.08.34)
 * @author: Roberto Peli
 */
public interface IRiportoDocAmmMgr {
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.09.12)
 * @return java.lang.String
 */
String getStatoRiporto(
	UserContext userContext,
	IDocumentoAmministrativoBulk documentoAmministrativo)
	throws ComponentException;
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.09.12)
 * @return java.lang.String
 */
String getStatoRiportoInScrivania(
	UserContext userContext,
	IDocumentoAmministrativoBulk documentoAmministrativo)
	throws ComponentException;
public IDocumentoAmministrativoBulk riportaAvanti(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm,
	OptionRequestParameter status)
	throws ComponentException;
public IDocumentoAmministrativoBulk riportaIndietro(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm) 
	throws ComponentException;
}
