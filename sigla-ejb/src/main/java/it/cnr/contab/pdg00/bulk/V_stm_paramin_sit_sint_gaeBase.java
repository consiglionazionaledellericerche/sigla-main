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
 * Date 25/03/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;
public class V_stm_paramin_sit_sint_gaeBase extends V_stm_paramin_sit_sint_gaeKey implements Keyed {
 
//    CHIAVE VARCHAR(100) NOT NULL
	private java.lang.String chiave;
 
//    TIPO CHAR(1) NOT NULL
	private java.lang.String tipo;
 
//    SEQUENZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal sequenza;
 
//    ESERCIZIO DECIMAL(20,3)
	private java.math.BigDecimal esercizio;
 
//    CD_CDS VARCHAR(200)
	private java.lang.String cd_cds;
 
//    UO VARCHAR(200)
	private java.lang.String uo;
 

	
	public V_stm_paramin_sit_sint_gaeBase() {
		super();
	}
	
	public java.lang.String getChiave() {
		return chiave;
	}
	public void setChiave(java.lang.String chiave)  {
		this.chiave=chiave;
	}
	public java.lang.String getTipo() {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.math.BigDecimal getSequenza() {
		return sequenza;
	}
	public void setSequenza(java.math.BigDecimal sequenza)  {
		this.sequenza=sequenza;
	}
	public java.math.BigDecimal getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.math.BigDecimal esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	
}