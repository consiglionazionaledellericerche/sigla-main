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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;

public class Mandato_siopeIBulk extends Mandato_siopeBulk {

	protected Mandato_rigaIBulk mandato_rigaI;
	
	public Mandato_siopeIBulk() {
		super();
	}

	public Mandato_siopeIBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(cd_cds, esercizio, pg_mandato, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope);
	}

	public Mandato_rigaBulk getMandato_riga() {
		return getMandato_rigaI();
	}	
	
	public Mandato_rigaIBulk getMandato_rigaI() {
		return mandato_rigaI;
	}	
	
	public void setMandato_riga(Mandato_rigaBulk newMandato_riga) {
		setMandato_rigaI((Mandato_rigaIBulk)newMandato_riga);
	}

	public void setMandato_rigaI(Mandato_rigaIBulk newMandato_rigaI) {
		mandato_rigaI = newMandato_rigaI;
	}
}