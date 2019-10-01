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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_prestazioneBase extends Tipo_prestazioneKey implements Keyed {
	//    DS_TIPO_PRESTAZIONE VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_prestazione;
 
	//    TIPO_CLASSIFICAZIONE CHAR(5) NOT NULL
	private java.lang.String tipo_classificazione;

	//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	public Tipo_prestazioneBase() {
		super();
	}
	public Tipo_prestazioneBase(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public java.lang.String getDs_tipo_prestazione() {
		return ds_tipo_prestazione;
	}
	public void setDs_tipo_prestazione(java.lang.String ds_tipo_prestazione)  {
		this.ds_tipo_prestazione=ds_tipo_prestazione;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
    public java.lang.String getTipo_classificazione() {
		return tipo_classificazione;
	}
    public void setTipo_classificazione(java.lang.String tipo_classificazione) {
		this.tipo_classificazione = tipo_classificazione;
	}
}