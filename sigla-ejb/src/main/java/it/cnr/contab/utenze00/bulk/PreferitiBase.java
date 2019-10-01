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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.bulk.annotation.Layout;
import it.cnr.jada.persistency.Keyed;
public class PreferitiBase extends PreferitiKey implements Keyed {
//    DESCRIZIONE VARCHAR(200) NOT NULL
	@FieldPropertyAnnotation(name="descrizione",
			inputType=InputType.TEXTAREA,
			cols=100,
			rows=5, 
			maxLength=200,
			nullable=false,
			enabledOnSearch=true,
			label="Descrizione")
	private java.lang.String descrizione;
 
//    URL_ICONA VARCHAR(50) NOT NULL
	@FieldPropertyAnnotation(name="url_icona",
			inputType=InputType.RADIOGROUP,
			keysProperty="iconeKeys",
			layout=Layout.VERTICAL,
			maxLength=50,
			nullable=true,
			enabledOnSearch=true,
			label="Icona")
	private java.lang.String url_icona;
 
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: PREFERITI
	 **/
	public PreferitiBase() {
		super();
	}
	public PreferitiBase(java.lang.String cd_utente, java.lang.String business_process, java.lang.String ti_funzione) {
		super(cd_utente, business_process, ti_funzione);
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [descrizione]
	 **/
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [descrizione]
	 **/
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [url_icona]
	 **/
	public java.lang.String getUrl_icona() {
		return url_icona;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [url_icona]
	 **/
	public void setUrl_icona(java.lang.String url_icona)  {
		this.url_icona=url_icona;
	}
}