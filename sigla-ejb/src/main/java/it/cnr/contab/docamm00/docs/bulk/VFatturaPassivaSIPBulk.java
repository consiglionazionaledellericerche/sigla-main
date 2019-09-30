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
 * Date 09/06/2008
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Timestamp;

import it.cnr.jada.persistency.Keyed;

public class VFatturaPassivaSIPBulk extends VFatturaPassivaSIPKey implements Keyed {
	
	public VFatturaPassivaSIPBulk() {
		super();
	}
	public VFatturaPassivaSIPBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_passiva) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva);
	}
//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;
 
//    DENOMINAZIONE VARCHAR(101)
	private java.lang.String denominazione;
 
//    INDIRIZZO VARCHAR(333)
	private java.lang.String indirizzo;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;
 
//    NR_FATTURA_FORNITORE VARCHAR(20)
	private java.lang.String nr_fattura_fornitore;
 
//    DT_FATTURA_FORNITORE TIMESTAMP(7)
	private java.sql.Timestamp dt_fattura_fornitore;
 
//    ID_FATTURA VARCHAR(143)
	private java.lang.String id_fattura;
 
//    DS_FATTURA_PASSIVA VARCHAR(200)
	private java.lang.String ds_fattura_passiva;
 
//    IM_TOTALE_IMPONIBILE_DIVISA DECIMAL(15,2)
	private java.math.BigDecimal im_totale_fattura;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    CD_CENTRO_RESPOSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    GAE VARCHAR(10)
	private java.lang.String gae;
	
	private Long pg_fattura;
	
	private Timestamp dt_pagamento;
 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(java.lang.String denominazione)  {
		this.denominazione=denominazione;
	}
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(java.lang.String indirizzo)  {
		this.indirizzo=indirizzo;
	}
	public java.lang.String getPartita_iva() {
		return partita_iva;
	}
	public void setPartita_iva(java.lang.String partita_iva)  {
		this.partita_iva=partita_iva;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale)  {
		this.codice_fiscale=codice_fiscale;
	}
	public java.lang.String getNr_fattura_fornitore() {
		return nr_fattura_fornitore;
	}
	public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore)  {
		this.nr_fattura_fornitore=nr_fattura_fornitore;
	}
	public java.sql.Timestamp getDt_fattura_fornitore() {
		return dt_fattura_fornitore;
	}
	public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore)  {
		this.dt_fattura_fornitore=dt_fattura_fornitore;
	}
	public java.lang.String getId_fattura() {
		return id_fattura;
	}
	public void setId_fattura(java.lang.String id_fattura)  {
		this.id_fattura=id_fattura;
	}
	public java.lang.String getDs_fattura_passiva() {
		return ds_fattura_passiva;
	}
	public void setDs_fattura_passiva(java.lang.String ds_fattura_passiva)  {
		this.ds_fattura_passiva=ds_fattura_passiva;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getGae() {
		return gae;
	}
	public void setGae(java.lang.String gae)  {
		this.gae=gae;
	}
	public java.math.BigDecimal getIm_totale_fattura() {
		return im_totale_fattura;
	}
	public void setIm_totale_fattura(java.math.BigDecimal im_totale_fattura) {
		this.im_totale_fattura = im_totale_fattura;
	}
	public Long getPg_fattura() {
		return pg_fattura;
	}
	public void setPg_fattura(Long pg_fattura) {
		this.pg_fattura = pg_fattura;
	}
	public Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	public void setDt_pagamento(Timestamp dt_pagamento) {
		this.dt_pagamento = dt_pagamento;
	}
}