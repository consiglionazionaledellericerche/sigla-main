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
 * Date 20/06/2007
 */
package it.cnr.contab.docamm00.consultazioni.bulk;

import java.math.BigDecimal;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_estrai_glaBulk extends OggettoBulk implements Persistent {
	public V_estrai_glaBulk() {
		super();
	}
		private AnagraficoBulk anagrafico;
//    	CD_UO_COMPENSO VARCHAR(30)
		private java.lang.String cd_uo_compenso;
		
//	    DT_CMP_DA_COMPENSO VARCHAR(7)
		private java.lang.String dt_cmp_da_compenso;
	 
//	    DT_CMP_A_COMPENSO VARCHAR(7)
		private java.lang.String dt_cmp_a_compenso;
	 
//	    DT_MANDATO VARCHAR(7)
		private java.lang.String dt_mandato;
	 
//	    ESERCIZIO_PAGAMENTO DECIMAL(22,0)
		private java.lang.Long esercizio_pagamento;
	  
//	    CD_TERZO DECIMAL(8,0)
		private java.lang.Integer cd_terzo;
	 
//	    CD_ANAG DECIMAL(8,0)
		private java.lang.Integer cd_anag;
	  
//	    IMPONIBILE DECIMAL(15,2)
		private java.math.BigDecimal imponibile;
	 
//	    ALIQUOTA DECIMAL(22,0)
		private java.math.BigDecimal aliquota;
	 	 
//	    AMMONTARE DECIMAL(22,0)
		private java.math.BigDecimal ammontare;
	 	 
//	    CODICE_FISCALE VARCHAR(20)
		private java.lang.String codice_fiscale;
	 
//	    NOME VARCHAR(50)
		private java.lang.String nome;
	 
//	    COGNOME VARCHAR(50)
		private java.lang.String cognome;
	 
//	    TI_SESSO CHAR(1)
		private java.lang.String ti_sesso;
	 
//	    DT_NASCITA TIMESTAMP(7)
		private java.sql.Timestamp dt_nascita;
	 
//	    COMUNE_NASCITA VARCHAR(100)
		private java.lang.String comune_nascita;
	 
//	    PROVINCIA_NASCITA VARCHAR(10)
		private java.lang.String provincia_nascita;
	 
//	    COMUNE_RESIDENZA VARCHAR(100)
		private java.lang.String comune_residenza;
	 
//	    PROVINCIA_RESIDENZA VARCHAR(10)
		private java.lang.String provincia_residenza;
	 
//	    CAP_RESIDENZA VARCHAR(20)
		private java.lang.String cap_residenza;
	 
//	    VIA VARCHAR(100)
		private java.lang.String via;
	 
//	    NUM_CIVICO VARCHAR(10)
		private java.lang.String num_civico;
	 
//	    DENOMINAZIONE_SEDE VARCHAR(200) NOT NULL
		private java.lang.String denominazione_sede;
	 
//	    VIA_SEDE VARCHAR(100) NOT NULL
		private java.lang.String via_sede;
	 
//	    CIVICO_SEDE VARCHAR(5)
		private java.lang.String civico_sede;
	 
//	    CAP_SEDE VARCHAR(5)
		private java.lang.String cap_sede;
	 
//	    COD_FIS_CNR VARCHAR(20)
		private java.lang.String cod_fis_cnr;
	 
//	    DS_CNR VARCHAR(100)
		private java.lang.String ds_cnr;
	 
//	    COMUNE_CNR VARCHAR(100) NOT NULL
		private java.lang.String comune_cnr;
	 
//	    IND_CNR VARCHAR(100) NOT NULL
		private java.lang.String ind_cnr;
	 
//	    CIV_CNR VARCHAR(10)
		private java.lang.String civ_cnr;
	 
//	    CAP_CNR VARCHAR(5)
		private java.lang.String cap_cnr;
	 
//	    DATA_F24 
		private  java.sql.Timestamp data_f24;
	 
//	    NOTA VARCHAR(0)
		private java.lang.String nota;
//	    NOTA VARCHAR(0)
		private java.lang.String ragione_sociale;
		private String file;
	 
		public java.lang.Integer getCd_terzo() {
			return cd_terzo;
		}
		public void setCd_terzo(java.lang.Integer cd_terzo)  {
			this.cd_terzo=cd_terzo;
		}
		public java.lang.Integer getCd_anag() {
			return cd_anag;
		}
		public void setCd_anag(java.lang.Integer cd_anag)  {
			this.cd_anag=cd_anag;
		}
		public java.math.BigDecimal getImponibile() {
			return imponibile;
		}
		public void setImponibile(java.math.BigDecimal imponibile)  {
			this.imponibile=imponibile;
		}
		public BigDecimal getAliquota() {
			return aliquota;
		}
		public void setAliquota(BigDecimal aliquota)  {
			this.aliquota=aliquota;
		}
		public BigDecimal getAmmontare() {
			return ammontare;
		}
		public void setAmmontare(BigDecimal ammontare)  {
			this.ammontare=ammontare;
		}
		public java.lang.String getCodice_fiscale() {
			return codice_fiscale;
		}
		public void setCodice_fiscale(java.lang.String codice_fiscale)  {
			this.codice_fiscale=codice_fiscale;
		}
		public java.lang.String getNome() {
			return nome;
		}
		public void setNome(java.lang.String nome)  {
			this.nome=nome;
		}
		public java.lang.String getCognome() {
			return cognome;
		}
		public void setCognome(java.lang.String cognome)  {
			this.cognome=cognome;
		}
		public java.lang.String getTi_sesso() {
			return ti_sesso;
		}
		public void setTi_sesso(java.lang.String ti_sesso)  {
			this.ti_sesso=ti_sesso;
		}
		public java.sql.Timestamp getDt_nascita() {
			return dt_nascita;
		}
		public void setDt_nascita(java.sql.Timestamp dt_nascita)  {
			this.dt_nascita=dt_nascita;
		}
		public java.lang.String getComune_nascita() {
			return comune_nascita;
		}
		public void setComune_nascita(java.lang.String comune_nascita)  {
			this.comune_nascita=comune_nascita;
		}
		public java.lang.String getProvincia_nascita() {
			return provincia_nascita;
		}
		public void setProvincia_nascita(java.lang.String provincia_nascita)  {
			this.provincia_nascita=provincia_nascita;
		}
		public java.lang.String getComune_residenza() {
			return comune_residenza;
		}
		public void setComune_residenza(java.lang.String comune_residenza)  {
			this.comune_residenza=comune_residenza;
		}
		public java.lang.String getProvincia_residenza() {
			return provincia_residenza;
		}
		public void setProvincia_residenza(java.lang.String provincia_residenza)  {
			this.provincia_residenza=provincia_residenza;
		}
		public java.lang.String getCap_residenza() {
			return cap_residenza;
		}
		public void setCap_residenza(java.lang.String cap_residenza)  {
			this.cap_residenza=cap_residenza;
		}
		public java.lang.String getVia() {
			return via;
		}
		public void setVia(java.lang.String via)  {
			this.via=via;
		}
		public java.lang.String getNum_civico() {
			return num_civico;
		}
		public void setNum_civico(java.lang.String num_civico)  {
			this.num_civico=num_civico;
		}
		public java.lang.String getDenominazione_sede() {
			return denominazione_sede;
		}
		public void setDenominazione_sede(java.lang.String denominazione_sede)  {
			this.denominazione_sede=denominazione_sede;
		}
		public java.lang.String getVia_sede() {
			return via_sede;
		}
		public void setVia_sede(java.lang.String via_sede)  {
			this.via_sede=via_sede;
		}
		public java.lang.String getCivico_sede() {
			return civico_sede;
		}
		public void setCivico_sede(java.lang.String civico_sede)  {
			this.civico_sede=civico_sede;
		}
		public java.lang.String getCap_sede() {
			return cap_sede;
		}
		public void setCap_sede(java.lang.String cap_sede)  {
			this.cap_sede=cap_sede;
		}
		public java.lang.String getCod_fis_cnr() {
			return cod_fis_cnr;
		}
		public void setCod_fis_cnr(java.lang.String cod_fis_cnr)  {
			this.cod_fis_cnr=cod_fis_cnr;
		}
		public java.lang.String getDs_cnr() {
			return ds_cnr;
		}
		public void setDs_cnr(java.lang.String ds_cnr)  {
			this.ds_cnr=ds_cnr;
		}
		public java.lang.String getComune_cnr() {
			return comune_cnr;
		}
		public void setComune_cnr(java.lang.String comune_cnr)  {
			this.comune_cnr=comune_cnr;
		}
		public java.lang.String getInd_cnr() {
			return ind_cnr;
		}
		public void setInd_cnr(java.lang.String ind_cnr)  {
			this.ind_cnr=ind_cnr;
		}
		public java.lang.String getCiv_cnr() {
			return civ_cnr;
		}
		public void setCiv_cnr(java.lang.String civ_cnr)  {
			this.civ_cnr=civ_cnr;
		}
		public java.lang.String getCap_cnr() {
			return cap_cnr;
		}
		public void setCap_cnr(java.lang.String cap_cnr)  {
			this.cap_cnr=cap_cnr;
		}
		
		public java.lang.String getNota() {
			return nota;
		}
		public void setNota(java.lang.String nota)  {
			this.nota=nota;
		}
		public java.lang.String getDt_cmp_a_compenso() {
			return dt_cmp_a_compenso;
		}
		public void setDt_cmp_a_compenso(java.lang.String dt_cmp_a_compenso) {
			this.dt_cmp_a_compenso = dt_cmp_a_compenso;
		}
		public java.lang.String getDt_cmp_da_compenso() {
			return dt_cmp_da_compenso;
		}
		public void setDt_cmp_da_compenso(java.lang.String dt_cmp_da_compenso) {
			this.dt_cmp_da_compenso = dt_cmp_da_compenso;
		}
		public java.lang.String getDt_mandato() {
			return dt_mandato;
		}
		public void setDt_mandato(java.lang.String dt_mandato) {
			this.dt_mandato = dt_mandato;
		}
		public java.lang.Long getEsercizio_pagamento() {
			return esercizio_pagamento;
		}
		public void setEsercizio_pagamento(java.lang.Long esercizio_pagamento) {
			this.esercizio_pagamento = esercizio_pagamento;
		}
		public java.lang.String getCd_uo_compenso() {
			return cd_uo_compenso;
		}
		public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
			this.cd_uo_compenso = cd_uo_compenso;
		}
		public AnagraficoBulk getAnagrafico() {
			return anagrafico;
		}
		public void setAnagrafico(AnagraficoBulk anagrafico) {
			this.anagrafico = anagrafico;
			if (anagrafico!=null)
				setCd_anag(getAnagrafico().getCd_anag());
			if (getAnagrafico().getRagione_sociale()!=null)
				setRagione_sociale(getAnagrafico().getRagione_sociale());
			else if (getAnagrafico().getCognome()!=null)
				setRagione_sociale(getAnagrafico().getCognome()+" - "+getAnagrafico().getNome());
			else 
				setRagione_sociale(null);
		}
		
		public void setData_f24(java.sql.Timestamp data_f24) {
			this.data_f24 = data_f24;
		}
		public java.sql.Timestamp getData_f24() {
			return data_f24;
		}
		public java.lang.String getRagione_sociale() {
			return ragione_sociale;
		}
		public void setRagione_sociale(java.lang.String ragione_sociale) {
			this.ragione_sociale = ragione_sociale;
		}
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
}