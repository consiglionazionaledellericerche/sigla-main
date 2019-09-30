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
 * Date 22/06/2007
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_fondo_economaleBulk extends OggettoBulk implements Persistent{
	
	
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CDS VARCHAR(30) NOT NULL
	private java.lang.String cds;
 
//    UO VARCHAR(30) NOT NULL
	private java.lang.String uo;
 
//    CD_CODICE_FONDO VARCHAR(10) NOT NULL
	private java.lang.String cd_codice_fondo;
 
//    DS_FONDO VARCHAR(300)
	private java.lang.String ds_fondo;
 
//    ECONOMO VARCHAR(200) NOT NULL
	private java.lang.String economo;
 
//    FL_APERTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_aperto;
 
//    IM_AMMONTARE_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_iniziale;
 
//    IM_AMMONTARE_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_fondo;
 
//    IM_RESIDUO_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residuo_fondo;
	
//  IM_AMMONTARE_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_spese;

//  IM_AMMONTARE_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_netto_spese;

//  IM_RESIDUO_FONDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_reintegri;

	//    CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;
 
//    DS_MODALITA_PAG VARCHAR(100) NOT NULL
	private java.lang.String ds_modalita_pag;
 
//    ABI CHAR(5)
	private java.lang.String abi;
 
//    CAB CHAR(5)
	private java.lang.String cab;
 
//    NUMERO_CONTO VARCHAR(30)
	private java.lang.String numero_conto;
 
//    CIN VARCHAR(1)
	private java.lang.String cin;
 
//    INTESTAZIONE VARCHAR(200)
	private java.lang.String intestazione;
 
//    CODICE_IBAN VARCHAR(34)
	private java.lang.String codice_iban;
 
//    CODICE_SWIFT VARCHAR(20)
	private java.lang.String codice_swift;
 
	public V_fondo_economaleBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getCd_codice_fondo() {
		return cd_codice_fondo;
	}
	public void setCd_codice_fondo(java.lang.String cd_codice_fondo)  {
		this.cd_codice_fondo=cd_codice_fondo;
	}
	public java.lang.String getDs_fondo() {
		return ds_fondo;
	}
	public void setDs_fondo(java.lang.String ds_fondo)  {
		this.ds_fondo=ds_fondo;
	}
	public java.lang.String getEconomo() {
		return economo;
	}
	public void setEconomo(java.lang.String economo)  {
		this.economo=economo;
	}
	public java.lang.Boolean getFl_aperto() {
		return fl_aperto;
	}
	public void setFl_aperto(java.lang.Boolean fl_aperto)  {
		this.fl_aperto=fl_aperto;
	}
	public java.math.BigDecimal getIm_ammontare_iniziale() {
		return im_ammontare_iniziale;
	}
	public void setIm_ammontare_iniziale(java.math.BigDecimal im_ammontare_iniziale)  {
		this.im_ammontare_iniziale=im_ammontare_iniziale;
	}
	public java.math.BigDecimal getIm_ammontare_fondo() {
		return im_ammontare_fondo;
	}
	public void setIm_ammontare_fondo(java.math.BigDecimal im_ammontare_fondo)  {
		this.im_ammontare_fondo=im_ammontare_fondo;
	}
	public java.math.BigDecimal getIm_residuo_fondo() {
		return im_residuo_fondo;
	}
	public void setIm_residuo_fondo(java.math.BigDecimal im_residuo_fondo)  {
		this.im_residuo_fondo=im_residuo_fondo;
	}
	public java.lang.String getCd_modalita_pag() {
		return cd_modalita_pag;
	}
	public void setCd_modalita_pag(java.lang.String cd_modalita_pag)  {
		this.cd_modalita_pag=cd_modalita_pag;
	}
	public java.lang.String getDs_modalita_pag() {
		return ds_modalita_pag;
	}
	public void setDs_modalita_pag(java.lang.String ds_modalita_pag)  {
		this.ds_modalita_pag=ds_modalita_pag;
	}
	public java.lang.String getAbi() {
		return abi;
	}
	public void setAbi(java.lang.String abi)  {
		this.abi=abi;
	}
	public java.lang.String getCab() {
		return cab;
	}
	public void setCab(java.lang.String cab)  {
		this.cab=cab;
	}
	public java.lang.String getNumero_conto() {
		return numero_conto;
	}
	public void setNumero_conto(java.lang.String numero_conto)  {
		this.numero_conto=numero_conto;
	}
	public java.lang.String getCin() {
		return cin;
	}
	public void setCin(java.lang.String cin)  {
		this.cin=cin;
	}
	public java.lang.String getIntestazione() {
		return intestazione;
	}
	public void setIntestazione(java.lang.String intestazione)  {
		this.intestazione=intestazione;
	}
	public java.lang.String getCodice_iban() {
		return codice_iban;
	}
	public void setCodice_iban(java.lang.String codice_iban)  {
		this.codice_iban=codice_iban;
	}
	public java.lang.String getCodice_swift() {
		return codice_swift;
	}
	public void setCodice_swift(java.lang.String codice_swift)  {
		this.codice_swift=codice_swift;
	}
	public java.math.BigDecimal getIm_totale_spese() {
		return im_totale_spese;
	}

	public void setIm_totale_spese(java.math.BigDecimal im_totale_spese) {
		this.im_totale_spese = im_totale_spese;
	}

	public java.math.BigDecimal getIm_totale_netto_spese() {
		return im_totale_netto_spese;
	}

	public void setIm_totale_netto_spese(java.math.BigDecimal im_totale_netto_spese) {
		this.im_totale_netto_spese = im_totale_netto_spese;
	}

	public java.math.BigDecimal getIm_totale_reintegri() {
		return im_totale_reintegri;
	}

	public void setIm_totale_reintegri(java.math.BigDecimal im_totale_reintegri) {
		this.im_totale_reintegri = im_totale_reintegri;
	}
}