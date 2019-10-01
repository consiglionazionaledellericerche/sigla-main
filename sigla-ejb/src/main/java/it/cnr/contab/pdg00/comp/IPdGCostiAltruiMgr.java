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

/**
 * Insert the type's description here.
 * Creation date: (17/10/2001 10.08.43)
 * @author: Luca Bessi
 */
public interface IPdGCostiAltruiMgr extends it.cnr.jada.comp.IMultipleCRUDMgr {
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio caricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
		public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
}
