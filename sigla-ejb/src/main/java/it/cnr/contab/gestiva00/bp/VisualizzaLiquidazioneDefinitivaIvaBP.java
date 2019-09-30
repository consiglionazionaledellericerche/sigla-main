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

package it.cnr.contab.gestiva00.bp;

/**
 * Insert the type's description here.
 * Creation date: (3/26/2003 11:17:29 AM)
 * @author: CNRADM
 */
public class VisualizzaLiquidazioneDefinitivaIvaBP extends LiquidazioneDefinitivaIvaBP {
/**
 * RistampaLiquidazioneDefinitivaIvaBP constructor comment.
 */
public VisualizzaLiquidazioneDefinitivaIvaBP() {
	super();
}
/**
 * RistampaLiquidazioneDefinitivaIvaBP constructor comment.
 * @param function java.lang.String
 */
public VisualizzaLiquidazioneDefinitivaIvaBP(String function) {
	super(function);
}
public boolean isStartSearchButtonEnabled() {
	return false;
}
public boolean isStartSearchButtonHidden() {
	return true;
}
}
