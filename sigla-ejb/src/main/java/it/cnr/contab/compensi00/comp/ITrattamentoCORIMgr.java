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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10.18.33)
 * @author: Roberto Fantino
 */
public interface ITrattamentoCORIMgr extends ICRUDMgr {
/**
 * Ricerca righe associate al Tipo Trattamento
 *
 * Pre-post-conditions
 *
 * Nome: Ricerca intervalli associati al tipo trattamento selezionato
 * Pre: Viene richiesta la lista delle righe del Trattamento CO/RI
 *		che hanno il Tipo Trattamento selezionato
 * Post: Viene inserita nel Trattamento CO/RI <trattCORI> la lista
 *       di tutti i Trattamenti CO/RI con lo stesso Tipo Trattamento
 *
*/
public abstract it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk fillAllRows(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk param1) throws it.cnr.jada.comp.ComponentException;
}
