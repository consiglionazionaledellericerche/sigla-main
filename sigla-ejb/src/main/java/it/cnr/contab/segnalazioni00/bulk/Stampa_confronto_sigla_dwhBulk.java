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
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
public class Stampa_confronto_sigla_dwhBulk extends OggettoBulk {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFRONTO_SIGLA_DWH
	 **/
	
	private ConfrontoSiglaDwhBulk data;
	private java.util.Collection date;
	
	
	public Stampa_confronto_sigla_dwhBulk() {
		super();
	}
	
	/*public void validate() throws ValidationException{

		if (getData()==null || getData().getDt_elaborazione()==null)
			throw new ValidationException("Il campo Data Elaborazione e' obbligatorio");
	}*/
	
	public java.sql.Timestamp getDt_elaborazioneForPrint() {
		ConfrontoSiglaDwhBulk confSiglaDwh = this.getData();
		if (confSiglaDwh == null)
			return null;
		return confSiglaDwh.getDt_elaborazione();
	}

	public ConfrontoSiglaDwhBulk getData() {
		return data;
	}

	public void setData(ConfrontoSiglaDwhBulk data) {
		this.data = data;
	}

	public java.util.Collection getDate() {
		return date;
	}

	public void setDate(java.util.Collection date) {
		this.date = date;
	}
}