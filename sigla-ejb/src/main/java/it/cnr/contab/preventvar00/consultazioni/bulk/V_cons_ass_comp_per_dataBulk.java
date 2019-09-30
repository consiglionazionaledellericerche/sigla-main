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
* Date 09/11/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_cons_ass_comp_per_dataBulk extends OggettoBulk implements Persistent{

//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pg_progetto;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    PG_COMMESSA DECIMAL(10,0)
	private java.lang.Long pg_commessa;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String ds_commessa;
 
//    PG_MODULO DECIMAL(10,0)
	private java.lang.Long pg_modulo;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    CD_LIVELLO1 VARCHAR(4)
	private java.lang.String cd_livello1;
 
//    DS_LIVELLO1 VARCHAR(250)
	private java.lang.String ds_livello1;
 
//    CD_LIVELLO2 VARCHAR(4)
	private java.lang.String cd_livello2;
 
//    DS_LIVELLO2 VARCHAR(250)
	private java.lang.String ds_livello2;
 
//    CD_LIVELLO3 VARCHAR(4)
	private java.lang.String cd_livello3;
 
//    DS_LIVELLO3 VARCHAR(250)
	private java.lang.String ds_livello3;
 
//    CD_LIVELLO4 VARCHAR(4)
	private java.lang.String cd_livello4;
 
//    CD_LIVELLO5 VARCHAR(4)
	private java.lang.String cd_livello5;
 
//    CD_LIVELLO6 VARCHAR(4)
	private java.lang.String cd_livello6;
 
//    CD_LIVELLO7 VARCHAR(4)
	private java.lang.String cd_livello7;
 
//    PG_VARIAZIONE_PDG DECIMAL(22,0)
	private java.lang.Long pg_variazione_pdg;
 
//    DATA_APPROVAZIONE_VAR TIMESTAMP(7)
	private java.sql.Timestamp data_approvazione_var;
 
//    IM_STANZ_INIZIALE_A1 DECIMAL(22,0)
	private java.lang.Long im_stanz_iniziale_a1;
 
//    VARIAZIONI_PIU DECIMAL(22,0)
	private java.lang.Long variazioni_piu;
 
//    VARIAZIONI_MENO DECIMAL(22,0)
	private java.lang.Long variazioni_meno;
//  Colonne calcolate 
//  SUM_IM_STANZ_INIZIALE_A1 DECIMAL(22,0)
	private java.lang.Long tot_im_stanz_iniziale_a1;
 
//    SUM_VARIAZIONI_PIU DECIMAL(22,0)
	private java.lang.Long tot_variazioni_piu;
 
//    SUM_VARIAZIONI_MENO DECIMAL(22,0)
	private java.lang.Long tot_variazioni_meno;
 	
	
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	private final static java.util.Dictionary ti_gestioneKeys;

	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
	
	
	public V_cons_ass_comp_per_dataBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.Long getPg_progetto () {
		return pg_progetto;
	}
	public void setPg_progetto(java.lang.Long pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto () {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.Long getPg_commessa () {
		return pg_commessa;
	}
	public void setPg_commessa(java.lang.Long pg_commessa)  {
		this.pg_commessa=pg_commessa;
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.Long getPg_modulo () {
		return pg_modulo;
	}
	public void setPg_modulo(java.lang.Long pg_modulo)  {
		this.pg_modulo=pg_modulo;
	}
	public java.lang.String getCd_modulo () {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo)  {
		this.cd_modulo=cd_modulo;
	}
	public java.lang.String getDs_modulo () {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getTi_appartenenza () {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		this.ti_appartenenza=ti_appartenenza;
	}
	public java.lang.String getTi_gestione () {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce () {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	public java.lang.String getCd_livello1 () {
		return cd_livello1;
	}
	public void setCd_livello1(java.lang.String cd_livello1)  {
		this.cd_livello1=cd_livello1;
	}
	public java.lang.String getDs_livello1 () {
		return ds_livello1;
	}
	public void setDs_livello1(java.lang.String ds_livello1)  {
		this.ds_livello1=ds_livello1;
	}
	public java.lang.String getCd_livello2 () {
		return cd_livello2;
	}
	public void setCd_livello2(java.lang.String cd_livello2)  {
		this.cd_livello2=cd_livello2;
	}
	public java.lang.String getDs_livello2 () {
		return ds_livello2;
	}
	public void setDs_livello2(java.lang.String ds_livello2)  {
		this.ds_livello2=ds_livello2;
	}
	public java.lang.String getCd_livello3 () {
		return cd_livello3;
	}
	public void setCd_livello3(java.lang.String cd_livello3)  {
		this.cd_livello3=cd_livello3;
	}
	public java.lang.String getDs_livello3 () {
		return ds_livello3;
	}
	public void setDs_livello3(java.lang.String ds_livello3)  {
		this.ds_livello3=ds_livello3;
	}
	public java.lang.String getCd_livello4 () {
		return cd_livello4;
	}
	public void setCd_livello4(java.lang.String cd_livello4)  {
		this.cd_livello4=cd_livello4;
	}
	public java.lang.String getCd_livello5 () {
		return cd_livello5;
	}
	public void setCd_livello5(java.lang.String cd_livello5)  {
		this.cd_livello5=cd_livello5;
	}
	public java.lang.String getCd_livello6 () {
		return cd_livello6;
	}
	public void setCd_livello6(java.lang.String cd_livello6)  {
		this.cd_livello6=cd_livello6;
	}
	public java.lang.String getCd_livello7 () {
		return cd_livello7;
	}
	public void setCd_livello7(java.lang.String cd_livello7)  {
		this.cd_livello7=cd_livello7;
	}
	public java.lang.Long getPg_variazione_pdg () {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public java.sql.Timestamp getData_approvazione_var () {
		return data_approvazione_var;
	}
	public void setData_approvazione_var(java.sql.Timestamp data_approvazione_var)  {
		this.data_approvazione_var=data_approvazione_var;
	}
	public java.lang.Long getIm_stanz_iniziale_a1 () {
		return im_stanz_iniziale_a1;
	}
	public void setIm_stanz_iniziale_a1(java.lang.Long im_stanz_iniziale_a1)  {
		this.im_stanz_iniziale_a1=im_stanz_iniziale_a1;
	}
	public java.lang.Long getVariazioni_piu () {
		return variazioni_piu;
	}
	public void setVariazioni_piu(java.lang.Long variazioni_piu)  {
		this.variazioni_piu=variazioni_piu;
	}
	public java.lang.Long getVariazioni_meno () {
		return variazioni_meno;
	}
	public void setVariazioni_meno(java.lang.Long variazioni_meno)  {
		this.variazioni_meno=variazioni_meno;
	}
	
	public java.util.Dictionary getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}
	public java.lang.Long getTot_im_stanz_iniziale_a1() {
		return tot_im_stanz_iniziale_a1;
	}
	public void setTot_im_stanz_iniziale_a1(java.lang.Long tot_im_stanz_iniziale_a1) {
		this.tot_im_stanz_iniziale_a1 = tot_im_stanz_iniziale_a1;
	}
	public java.lang.Long getTot_variazioni_meno() {
		return tot_variazioni_meno;
	}
	public void setTot_variazioni_meno(java.lang.Long tot_variazioni_meno) {
		this.tot_variazioni_meno = tot_variazioni_meno;
	}
	public java.lang.Long getTot_variazioni_piu() {
		return tot_variazioni_piu;
	}
	public void setTot_variazioni_piu(java.lang.Long tot_variazioni_piu) {
		this.tot_variazioni_piu = tot_variazioni_piu;
	}
}