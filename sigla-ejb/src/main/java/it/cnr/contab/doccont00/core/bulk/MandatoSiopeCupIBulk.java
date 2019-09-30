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


public class MandatoSiopeCupIBulk extends MandatoSiopeCupBulk {

	
	protected Mandato_siopeIBulk mandato_siopeI;
	public MandatoSiopeCupIBulk() {
		super();
	}

	public MandatoSiopeCupIBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope,java.lang.String cd_cup) {
		super(cd_cds, esercizio, pg_mandato, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope,cd_cup);
	}

	public Mandato_siopeBulk getMandatoSiope() {
		return getMandato_siopeI();
	}	
	public void setMandatoSiope(Mandato_siopeBulk siope) {
		setMandato_siopeI((Mandato_siopeIBulk)siope);
	}

	public Mandato_siopeIBulk getMandato_siopeI() {
		return mandato_siopeI;
	}

	public void setMandato_siopeI(Mandato_siopeIBulk mandato_siopeI) {
		this.mandato_siopeI = mandato_siopeI;
	}

	

	
	
}