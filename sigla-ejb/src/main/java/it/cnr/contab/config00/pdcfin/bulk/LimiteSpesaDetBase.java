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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class LimiteSpesaDetBase extends LimiteSpesaDetKey implements Keyed {
//    IMPORTO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_limite;
 
//    IMPEGNI_ASSUNTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal impegni_assunti;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA_DET
	 **/
	public LimiteSpesaDetBase() {
		super();
	}
	public LimiteSpesaDetBase(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.Integer esercizio_voce, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, esercizio_voce, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLimite]
	 **/
	public java.math.BigDecimal getImporto_limite() {
		return importo_limite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLimite]
	 **/
	public void setImporto_limite(java.math.BigDecimal importoLimite)  {
		this.importo_limite=importoLimite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [impegniAssunti]
	 **/
	public java.math.BigDecimal getImpegni_assunti() {
		return impegni_assunti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [impegniAssunti]
	 **/
	public void setImpegni_assunti(java.math.BigDecimal impegniAssunti)  {
		this.impegni_assunti=impegniAssunti;
	}
}