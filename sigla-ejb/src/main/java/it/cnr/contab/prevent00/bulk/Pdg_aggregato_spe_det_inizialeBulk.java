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
 * Gestisce i dati iniziali per le Spese adattando ed implementando: {@link Pdg_aggregato_spe_detBulk } e  {@link Pdg_aggregato_det_iniziale } 
 * 		perchè si ottengano e si settino gli oggetti complessi.
 */
public class Pdg_aggregato_spe_det_inizialeBulk extends Pdg_aggregato_spe_detBulk implements Pdg_aggregato_det_iniziale {
	private Pdg_aggregato_spe_detBulk spe_modificato;

/**
 * Costruttore standard di Pdg_aggregato_spe_det_inizialeBulk.
 */
public Pdg_aggregato_spe_det_inizialeBulk() {
	super();
}

/**
 * Costruttore di Pdg_aggregato_spe_det_inizialeBulk cui viengono passati in ingresso
 *		i parametri: cd_cds,cd_centro_responsabilita,cd_elemento_voce,cd_funzione,
 *		cd_natura,esercizio,ti_aggregato,ti_appartenenza,ti_gestione.
 *
 * @param cd_cds java.lang.String
 * @param cd_centro_responsabilita java.lang.String
 * @param cd_elemento_voce java.lang.String
 * @param cd_funzione java.lang.String
 * @param cd_natura java.lang.String
 * @param esercizio java.lang.Integer
 * @param ti_aggregato java.lang.String
 * @param ti_appartenenza java.lang.String
 * @param ti_gestione java.lang.String
 */
public Pdg_aggregato_spe_det_inizialeBulk(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_funzione,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super();
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
	setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce, esercizio, ti_appartenenza, ti_gestione));
	setFunzione(new it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk(cd_funzione));
	setNatura(new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
	setEsercizio(esercizio);
	setTi_aggregato(ti_aggregato);
}

/**
 * Restituisce il dettaglio Modificato per le Spese.
 * 
 * @return Pdg_aggregato_det spe_modificato
 */
public Pdg_aggregato_det getDet_modificato() {
	return spe_modificato;
}

/**
 * Restituisce il Bulk delle Spese modificate.
 * 
 * @return Pdg_aggregato_spe_detBulk spe_modificato
 *
 * @see setEtr_modificato(Pdg_aggregato_etr_detBulk)
 */
public Pdg_aggregato_spe_detBulk getSpe_modificato() {
	return spe_modificato;
}

/**
 * Setta il Bulk delle Spese modificato.
 * 
 * @param newSpe_modificato Pdg_aggregato_spe_detBulk
 *
 * @see getEtr_modificato()
 */
public void setSpe_modificato(Pdg_aggregato_spe_detBulk newSpe_modificato) {
	spe_modificato = newSpe_modificato;
}
}