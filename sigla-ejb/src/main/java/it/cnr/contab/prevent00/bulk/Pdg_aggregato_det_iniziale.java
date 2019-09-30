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

package it.cnr.contab.prevent00.bulk;

/**
 * Interfaccia che implementa i metodi comuni delle classi Bulk delle Entrate 
 * 		e delle Spese iniziali. Essa permette di presentare, oltre ai dati
 * 		iniziali, anche quelli modificati, adattando {@link Pdg_aggregato_det }.
 *
 * @author: Vincenzo Bisquadro
 */
public interface Pdg_aggregato_det_iniziale extends Pdg_aggregato_det {
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'det_modificato'
 *
 * @return Il valore della proprietà 'det_modificato'
 */
Pdg_aggregato_det getDet_modificato();
}
