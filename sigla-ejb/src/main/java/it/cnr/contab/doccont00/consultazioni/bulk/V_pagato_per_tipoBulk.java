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
 * Date 17/01/2008
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_pagato_per_tipoBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    VOCE VARCHAR(20)
	private java.lang.String voce;
 
//    DESCRIZIONE VARCHAR(100)
	private java.lang.String descrizione;
 
//    TIPO VARCHAR(100)
	private java.lang.String tipo;
 
//    TI_MANDATO CHAR(1)
	private java.lang.String ti_mandato;
 
//    DS_TIPO VARCHAR(16)
	private java.lang.String ds_tipo;
 
//    TOT_PAGATO DECIMAL(22,0)
	private java.lang.Long tot_pagato;
 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getVoce() {
		return voce;
	}
	public void setVoce(java.lang.String voce)  {
		this.voce=voce;
	}
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.lang.String getTipo() {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.String getTi_mandato() {
		return ti_mandato;
	}
	public void setTi_mandato(java.lang.String ti_mandato)  {
		this.ti_mandato=ti_mandato;
	}
	public java.lang.String getDs_tipo() {
		return ds_tipo;
	}
	public void setDs_tipo(java.lang.String ds_tipo)  {
		this.ds_tipo=ds_tipo;
	}
	public java.lang.Long getTot_pagato() {
		return tot_pagato;
	}
	public void setTot_pagato(java.lang.Long tot_pagato)  {
		this.tot_pagato=tot_pagato;
	}
}