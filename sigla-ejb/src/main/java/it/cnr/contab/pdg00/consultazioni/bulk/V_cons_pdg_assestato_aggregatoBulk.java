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
* Date 09/11/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_pdg_assestato_aggregatoBulk extends OggettoBulk implements Persistent {
	public V_cons_pdg_assestato_aggregatoBulk() {
		super();
	}
	//	ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
	 
	//	PESO_DIP DECIMAL(22,0)
	private java.lang.Long peso_dip;
	 
	//	DIP VARCHAR(30)
	private java.lang.String dip;
	 
	//	DS_DIPARTIMENTO VARCHAR(765)
	private java.lang.String ds_dipartimento;
	 
	//	CDS VARCHAR(30)
	private java.lang.String cds;
	 
	//	DES_CDS VARCHAR(300)
	private java.lang.String des_cds;
	 
	//	UO VARCHAR(30)
	private java.lang.String uo;
	 
	//	DES_UO VARCHAR(300)
	private java.lang.String des_uo;
	 
	//	CD_LIVELLO1 VARCHAR(4)
    private java.lang.String cd_livello1;
	 
	//	CD_LIVELLO2 VARCHAR(4)
    private java.lang.String cd_livello2;
	 
	//	CDR VARCHAR(34)
	private java.lang.String cdr;

	//	CD_LINEA_ATTIVITA VARCHAR(34)
	private java.lang.String cd_linea_attivita;

	//	CD_CLASSIFICAZIONE VARCHAR(34)
	private java.lang.String cd_classificazione;

	//	DES_CLASSIFICAZIONE VARCHAR(300)
	private java.lang.String ds_classificazione;

	//	INI DECIMAL(22,0)
    private java.math.BigDecimal ini;
	 
	//	VAR_PIU DECIMAL(22,0)
	private java.math.BigDecimal var_piu;
	 
	//	VAR_MENO DECIMAL(22,0)
	private java.math.BigDecimal var_meno;
	 
	//	ASSESTATO DECIMAL(22,0)
	private java.math.BigDecimal assestato;

	public java.lang.Integer getEsercizio () {
	   return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
	   this.esercizio=esercizio;
	}
	public java.lang.Long getPeso_dip () {
	   return peso_dip;
	}
	public void setPeso_dip(java.lang.Long peso_dip)  {
	   this.peso_dip=peso_dip;
	}
	public java.lang.String getDip () {
	   return dip;
	}
	public void setDip(java.lang.String dip)  {
	   this.dip=dip;
	}
	public java.lang.String getDs_dipartimento () {
	   return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
	   this.ds_dipartimento=ds_dipartimento;
	}
	public java.lang.String getCds () {
	   return cds;
	}
	public void setCds(java.lang.String cds)  {
	   this.cds=cds;
	}
    public java.lang.String getDes_cds () {
  	   return des_cds;
	}
	public void setDes_cds(java.lang.String des_cds)  {
	   this.des_cds=des_cds;
	}
	public java.lang.String getUo () {
	   return uo;
	}
	public void setUo(java.lang.String uo)  {
	   this.uo=uo;
	}
	public java.lang.String getDes_uo () {
	   return des_uo;
	}
	public void setDes_uo(java.lang.String des_uo)  {
	   this.des_uo=des_uo;
	}
    public java.lang.String getCd_livello1 () {
 	   return cd_livello1;
	}
	public void setCd_livello1(java.lang.String cd_livello1)  {
	   this.cd_livello1=cd_livello1;
	}
	public java.lang.String getCd_livello2 () {
	   return cd_livello2;
	}
	public void setCd_livello2(java.lang.String cd_livello2)  {
	   this.cd_livello2=cd_livello2;
	}
    public java.math.BigDecimal getIni () {
	   return ini;
	}
	public void setIni(java.math.BigDecimal ini)  {
	   this.ini=ini;
	}
	public java.math.BigDecimal getVar_piu () {
	   return var_piu;
	}
	public void setVar_piu(java.math.BigDecimal var_piu)  {
	   this.var_piu=var_piu;
	}
	public java.math.BigDecimal getVar_meno () {
	   return var_meno;
	}
	public void setVar_meno(java.math.BigDecimal var_meno)  {
	   this.var_meno=var_meno;
	}
	public java.lang.String getCdr() {
		return cdr;
	}
	public void setCdr(java.lang.String string) {
		cdr = string;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String string) {
		cd_linea_attivita = string;
	}
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}
	public java.lang.String getDs_classificazione() {
		return ds_classificazione;
	}
	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}
	public void setDs_classificazione(java.lang.String string) {
		ds_classificazione = string;
	}
	public java.math.BigDecimal getAssestato() {
		return assestato;
	}
	public void setAssestato(java.math.BigDecimal decimal) {
		assestato = decimal;
	}
}