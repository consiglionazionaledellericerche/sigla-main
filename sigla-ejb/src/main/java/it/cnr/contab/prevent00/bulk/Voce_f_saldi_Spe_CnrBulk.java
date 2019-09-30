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
public class Voce_f_saldi_Spe_CnrBulk extends Voce_f_saldi_cmpBulk {
public Voce_f_saldi_Spe_CnrBulk() {
	super();
}

public Voce_f_saldi_Spe_CnrBulk(String cd_cds, String cd_voce, Integer esercizio, String ti_appartenenza, String ti_competenza_residuo, String ti_gestione) {
	super(cd_cds, cd_voce, esercizio, ti_appartenenza, ti_competenza_residuo, ti_gestione);
}
}