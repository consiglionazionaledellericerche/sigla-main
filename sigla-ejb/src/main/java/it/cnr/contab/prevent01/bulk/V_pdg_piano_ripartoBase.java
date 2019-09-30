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
* Created by Generator 1.0
* Date 01/11/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_pdg_piano_ripartoBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Long esercizio;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    DS_CDR VARCHAR(300)
	private java.lang.String ds_cdr;
 
//    ID_CLASSIFICAZIONE DECIMAL(22,0)
	private java.lang.Long id_classificazione;
 
//    CD_CLASSIFICAZIONE VARCHAR(34)
	private java.lang.String cd_classificazione;
 
//    DS_CLASSIFICAZIONE VARCHAR(250)
	private java.lang.String ds_classificazione;
 
//    IMPORTO_ASSEGNATO DECIMAL(22,0)
	private BigDecimal importo_assegnato;
 
//    IMPORTO_RIPARTITO DECIMAL(22,0)
	private BigDecimal importo_ripartito;
 
//    IMPORTO_DA_RIPARTIRE DECIMAL(22,0)
	private BigDecimal importo_da_ripartire;
 
	public V_pdg_piano_ripartoBase() {
		super();
	}
	public java.lang.Long getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Long esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getDs_cdr () {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.Long getId_classificazione () {
		return id_classificazione;
	}
	public void setId_classificazione(java.lang.Long id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public java.lang.String getCd_classificazione () {
		return cd_classificazione;
	}
	public void setCd_classificazione(java.lang.String cd_classificazione)  {
		this.cd_classificazione=cd_classificazione;
	}
	public java.lang.String getDs_classificazione () {
		return ds_classificazione;
	}
	public void setDs_classificazione(java.lang.String ds_classificazione)  {
		this.ds_classificazione=ds_classificazione;
	}
	public BigDecimal getImporto_assegnato () {
		return importo_assegnato;
	}
	public void setImporto_assegnato(BigDecimal importo_assegnato)  {
		this.importo_assegnato=importo_assegnato;
	}
	public BigDecimal getImporto_ripartito () {
		return importo_ripartito;
	}
	public void setImporto_ripartito(BigDecimal importo_ripartito)  {
		this.importo_ripartito=importo_ripartito;
	}
	public BigDecimal getImporto_da_ripartire () {
		return importo_da_ripartire;
	}
	public void setImporto_da_ripartire(BigDecimal importo_da_ripartire)  {
		this.importo_da_ripartire=importo_da_ripartire;
	}
}