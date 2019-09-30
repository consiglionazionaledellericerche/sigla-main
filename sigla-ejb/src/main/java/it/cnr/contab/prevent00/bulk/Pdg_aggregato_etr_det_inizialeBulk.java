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
 * Gestisce i dati iniziali per le Entrate adattando ed implementando {@link Pdg_aggregato_etr_detBulk } ,  
 * 		{@link Pdg_aggregato_det_iniziale }. All'inizio, dati iniziali e modificati coincidono.
 * 		
 * @see Pdg_aggregato_det_iniziale
 */
public class Pdg_aggregato_etr_det_inizialeBulk extends Pdg_aggregato_etr_detBulk implements Pdg_aggregato_det_iniziale {
	private Pdg_aggregato_etr_detBulk etr_modificato;
/**
 * Costruttore standard di Pdg_aggregato_spe_det_inizialeBulk.
 */
public Pdg_aggregato_etr_det_inizialeBulk() {
	super();
}
/**
 * Costruttore di Pdg_aggregato_spe_det_inizialeBulk cui vengono passati in ingresso:
 * 		cd_centro_responsabilita, cd_elemento_voce, cd_natura, esercizio, ti_aggregato, 
 * 		ti_appartenenza, ti_gestione.
 *
 * @param cd_centro_responsabilita java.lang.String
 * @param cd_elemento_voce java.lang.String
 * @param cd_natura java.lang.String
 * @param esercizio java.lang.Integer
 * @param ti_aggregato java.lang.String
 * @param ti_appartenenza java.lang.String
 * @param ti_gestione java.lang.String
 */
public Pdg_aggregato_etr_det_inizialeBulk(String cd_centro_responsabilita, String cd_elemento_voce, String cd_natura, Integer esercizio, String ti_aggregato, String ti_appartenenza, String ti_gestione) {
	super();

	setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce, esercizio, ti_appartenenza, ti_gestione));
	setNatura(new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
	setEsercizio(esercizio);
	setTi_aggregato(ti_aggregato);
}
/**
 * Restituisce il dettaglio Modificato per le Entrate.
 * 
 * @return Pdg_aggregato_det etr_modificato
 */
public Pdg_aggregato_det getDet_modificato() {
	return etr_modificato;
}
/**
 * Restituisce il Bulk delle Entrate modificate.
 * 
 * @return Pdg_aggregato_etr_detBulk etr_modificato
 *
 * @see setEtr_modificato(Pdg_aggregato_etr_detBulk)
 */
public Pdg_aggregato_etr_detBulk getEtr_modificato() {
	return etr_modificato;
}
/**
 * Setta il Bulk delle Entrate modificato.
 * 
 * @param newEtr_modificato Pdg_aggregato_etr_detBulk
 *
 * @see getEtr_modificato()
 */
public void setEtr_modificato(Pdg_aggregato_etr_detBulk newEtr_modificato) {
	etr_modificato = newEtr_modificato;
}
}
