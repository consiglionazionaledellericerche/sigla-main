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
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.doccont00.intcass.xmlbnl.*;
public class MovimentoContoEvidenzaBulk extends MovimentoContoEvidenzaBase {
	/**
	 * [INFORMAZIONI_CONTO_EVIDENZA null]
	 **/
	private InformazioniContoEvidenzaBulk informazioniContoEvidenza =  new InformazioniContoEvidenzaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza, java.lang.String stato, java.lang.Long progressivo) {
		super(esercizio, identificativoFlusso, contoEvidenza, stato, progressivo);
		setInformazioniContoEvidenza( new InformazioniContoEvidenzaBulk(esercizio,identificativoFlusso,contoEvidenza) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public InformazioniContoEvidenzaBulk getInformazioniContoEvidenza() {
		return informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setInformazioniContoEvidenza(InformazioniContoEvidenzaBulk informazioniContoEvidenza)  {
		this.informazioniContoEvidenza=informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getInformazioniContoEvidenza().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getIdentificativoFlusso();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.getInformazioniContoEvidenza().setIdentificativoFlusso(identificativoFlusso);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [contoEvidenza]
	 **/
	public java.lang.String getContoEvidenza() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getContoEvidenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [contoEvidenza]
	 **/
	public void setContoEvidenza(java.lang.String contoEvidenza)  {
		this.getInformazioniContoEvidenza().setContoEvidenza(contoEvidenza);
	}
}