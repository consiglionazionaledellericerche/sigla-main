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

package it.cnr.contab.pdg00.comp;
public interface IPdGCostiScaricatiMgr extends it.cnr.jada.comp.IMultipleCRUDMgr {
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
	public void eliminaConBulk (it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;

/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di modificare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
}