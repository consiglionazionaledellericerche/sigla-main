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

package it.cnr.contab.doccont00.comp;

import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
/**
 * Insert the type's description here.
 * Creation date: (2/8/2002 12:45:17 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoContabileMgr {
public void aggiornaCogeCoanInDifferita(
	UserContext userContext,
	IDocumentoContabileBulk docContabile,
	Map values)
	throws ComponentException;
public void aggiornaSaldiInDifferita(
	UserContext userContext,
	IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param )
	throws ComponentException;
public void lockScadenza(
	UserContext userContext,
	IScadenzaDocumentoContabileBulk scadenza)
	throws ComponentException;
public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(
	UserContext userContext,
	IScadenzaDocumentoContabileBulk scadenza,
	java.math.BigDecimal nuovoImporto,
	boolean aggiornaScadenzaSuccessiva )
	throws ComponentException;
}
