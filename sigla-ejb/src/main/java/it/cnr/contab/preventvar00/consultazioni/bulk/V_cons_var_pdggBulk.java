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
* Date 14/09/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_var_pdggBulk extends OggettoBulk implements Persistent{
	
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_VARIAZIONE_PDG DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_variazione_pdg;
 
//    DS_VARIAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_variazione;
 
//    DT_APERTURA TIMESTAMP(7) NOT NULL
	private java.sql.Date dt_apertura;
 
//    DT_CHIUSURA TIMESTAMP(7)
	private java.sql.Date dt_chiusura;

//  DT_APPROVAZIONE TIMESTAMP(7)
	private java.sql.Date dt_approvazione;

//    DT_APPROVAZIONE TIMESTAMP(7)
	private java.sql.Date dt_approvazione_da;

//  DT_APPROVAZIONE TIMESTAMP(7)
	private java.sql.Date dt_approvazione_a;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Date dt_annullamento;
 
//    DS_DELIBERA VARCHAR(200) NOT NULL
	private java.lang.String ds_delibera;
 
//    STATO CHAR(3) NOT NULL
	private java.lang.String stato;
 
//  STATO VARCHAR2(20) NOT NULL
	private java.lang.String ds_stato;
	
//    RIFERIMENTI VARCHAR(200)
	private java.lang.String riferimenti;
 
//    CD_CAUSALE_RESPINTA VARCHAR(100)
	private java.lang.String cd_causale_respinta;
 
//    DS_CAUSALE_RESPINTA VARCHAR(200)
	private java.lang.String ds_causale_respinta;
 
//    DT_APP_FORMALE TIMESTAMP(7)
	private java.sql.Date dt_app_formale;
 
//    TIPOLOGIA VARCHAR(15) NOT NULL
	private java.lang.String tipologia;

//	  TIPOLOGIA VARCHAR(100) NOT NULL
    private java.lang.String ds_tipo_variazione;

//    TIPOLOGIA_FIN VARCHAR(3)
	private java.lang.String tipologia_fin;
	
//  TIPOLOGIA_FIN VARCHAR(13)
	private java.lang.String ds_tipologia_fin;
 
//    PG_RIGA DECIMAL(5,0) NOT NULL
	private java.lang.Integer pg_riga;
 
//    CD_CDR_ASSEGNATARIO VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_assegnatario;
 
//    DS_CDR_ASSEGNATARIO VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr_assegnatario;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    CD_CLASSIFICAZIONE VARCHAR(34)
	private java.lang.String cd_classificazione;
 
//    DS_CLASSIFICAZIONE VARCHAR(250) NOT NULL
	private java.lang.String ds_classificazione;
 
//    NR_LIVELLO DECIMAL(22,0)
	private java.lang.Long nr_livello;
 
//    CD_LIVELLO1 VARCHAR(4) NOT NULL
	private java.lang.String cd_livello1;
 
//    CD_LIVELLO2 VARCHAR(4)
	private java.lang.String cd_livello2;
 
//    CD_LIVELLO3 VARCHAR(4)
	private java.lang.String cd_livello3;
 
//    CD_LIVELLO4 VARCHAR(4)
	private java.lang.String cd_livello4;
 
//    CD_LIVELLO5 VARCHAR(4)
	private java.lang.String cd_livello5;
 
//    CD_LIVELLO6 VARCHAR(4)
	private java.lang.String cd_livello6;
 
//    CD_LIVELLO7 VARCHAR(4)
	private java.lang.String cd_livello7;
 
//    PG_MODULO DECIMAL(10,0)
	private java.lang.Long pg_modulo;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    CD_TIPO_MODULO VARCHAR(10)
	private java.lang.String cd_tipo_modulo;
 
//    DS_TIPO_MODULO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_modulo;
 
//    PG_COMMESSA DECIMAL(10,0)
	private java.lang.Long pg_commessa;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String ds_commessa;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pg_progetto;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    DS_DIPARTIMENTO VARCHAR(765)
	private java.lang.String ds_dipartimento;
 
//    CD_CDS_AREA VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_area;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Date dt_registrazione;
 
//    DESCRIZIONE VARCHAR(300)
	private java.lang.String descrizione;
 
//    IM_SPESE_GEST_DECENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_int;
 
//    IM_SPESE_GEST_DECENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_est;
 
//    IM_SPESE_GEST_ACCENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_int;
 
//    IM_SPESE_GEST_ACCENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_est;
 
//    IM_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata;
 
//  ABS_TOT_VARIAZIONE DECIMAL(22,0)
	private java.lang.Long abs_tot_variazione;
	
//    ABS_TOT_VARIAZIONE DECIMAL(22,0)
	private java.lang.Long abs_tot_variazione_da;

//  ABS_TOT_VARIAZIONE DECIMAL(22,0)
	private java.lang.Long abs_tot_variazione_a;
	
	private V_classificazione_vociBulk v_classificazione_voci;

 
	
//	Raggruppamenti
	private Boolean ragr_STO_S_CDS;
	private Boolean ragr_STO_E_CDS;
	private Boolean ragr_STO_S_TOT;
	private Boolean ragr_STO_E_TOT;
	private Boolean ragr_VAR_PIU_CDS;
	private Boolean ragr_VAR_MENO_CDS;
	private Boolean ragr_VAR_PIU_TOT;
	private Boolean ragr_VAR_MENO_TOT;
	private Boolean ragr_PREL_FON;
	private Boolean ragr_NO_TIPO;
	private Boolean ragr_REST_FOND;
	private Boolean ragr_STO_E_AREA;
	private Boolean ragr_STO_S_AREA;
	private Boolean ragr_VAR_MENO_FON;
	private Boolean ragr_VAR_PIU_FON;
 
	public void inizializzaRagruppamenti() {
		setRagr_STO_S_CDS(new Boolean(false));
		setRagr_STO_E_CDS(new Boolean(false));
		setRagr_STO_S_TOT(new Boolean(false));
		setRagr_STO_E_TOT(new Boolean(false));
		setRagr_VAR_PIU_CDS(new Boolean(false));
		setRagr_VAR_MENO_CDS(new Boolean(false));
		setRagr_VAR_PIU_TOT(new Boolean(false));
		setRagr_VAR_MENO_TOT(new Boolean(false)); 
		setRagr_PREL_FON(new Boolean(false));    
		setRagr_NO_TIPO(new Boolean(false));      
		
		setRagr_REST_FOND(new Boolean(false));
		setRagr_VAR_PIU_FON(new Boolean(false));
		setRagr_VAR_MENO_FON(new Boolean(false)); 
		setRagr_STO_S_AREA(new Boolean(false));    
		setRagr_STO_E_AREA(new Boolean(false));
		
	}
	public void selezionaRagruppamenti(){
		setRagr_STO_S_CDS(new Boolean(!getRagr_STO_S_CDS().booleanValue()));
		setRagr_STO_E_CDS(new Boolean(!getRagr_STO_E_CDS().booleanValue()));
		setRagr_STO_S_TOT(new Boolean(!getRagr_STO_S_TOT().booleanValue()));
		setRagr_STO_E_TOT(new Boolean(!getRagr_STO_E_TOT().booleanValue()));
		setRagr_VAR_PIU_CDS(new Boolean(!getRagr_VAR_PIU_CDS().booleanValue()));
		setRagr_VAR_MENO_CDS(new Boolean(!getRagr_VAR_MENO_CDS().booleanValue()));
		setRagr_VAR_PIU_TOT(new Boolean(!getRagr_VAR_PIU_TOT().booleanValue()));
		setRagr_VAR_MENO_TOT(new Boolean(!getRagr_VAR_MENO_TOT().booleanValue()));	        
		setRagr_PREL_FON(new Boolean(!getRagr_PREL_FON().booleanValue()));	        
		setRagr_NO_TIPO(new Boolean(!getRagr_NO_TIPO().booleanValue()));		
		setRagr_REST_FOND(new Boolean(!getRagr_REST_FOND().booleanValue()));
		setRagr_VAR_PIU_FON(new Boolean(!getRagr_VAR_PIU_FON().booleanValue()));
		setRagr_VAR_MENO_FON(new Boolean(!getRagr_VAR_MENO_FON().booleanValue())); 
		setRagr_STO_S_AREA(new Boolean(!getRagr_STO_S_AREA().booleanValue()));    
		setRagr_STO_E_AREA(new Boolean(!getRagr_STO_E_AREA().booleanValue()));
		
		}


	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	private final static java.util.Dictionary ti_gestioneKeys;

	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
	
	
	
	public V_cons_var_pdggBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_variazione_pdg () {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public java.lang.String getDs_variazione () {
		return ds_variazione;
	}
	public void setDs_variazione(java.lang.String ds_variazione)  {
		this.ds_variazione=ds_variazione;
	}
	public java.sql.Date getDt_apertura () {
		return dt_apertura;
	}
	public void setDt_apertura(java.sql.Date dt_apertura)  {
		this.dt_apertura=dt_apertura;
	}
	public java.sql.Date getDt_chiusura () {
		return dt_chiusura;
	}
	public void setDt_chiusura(java.sql.Date dt_chiusura)  {
		this.dt_chiusura=dt_chiusura;
	}
/*	public java.sql.Timestamp getDt_approvazione () {
		return dt_approvazione;
	}
	public void setDt_approvazione(java.sql.Timestamp dt_approvazione)  {
		this.dt_approvazione=dt_approvazione;
	}*/
	public java.sql.Date getDt_annullamento () {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Date dt_annullamento)  {
		this.dt_annullamento=dt_annullamento;
	}
	public java.lang.String getDs_delibera () {
		return ds_delibera;
	}
	public void setDs_delibera(java.lang.String ds_delibera)  {
		this.ds_delibera=ds_delibera;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getRiferimenti () {
		return riferimenti;
	}
	public void setRiferimenti(java.lang.String riferimenti)  {
		this.riferimenti=riferimenti;
	}
	public java.lang.String getCd_causale_respinta () {
		return cd_causale_respinta;
	}
	public void setCd_causale_respinta(java.lang.String cd_causale_respinta)  {
		this.cd_causale_respinta=cd_causale_respinta;
	}
	public java.lang.String getDs_causale_respinta () {
		return ds_causale_respinta;
	}
	public void setDs_causale_respinta(java.lang.String ds_causale_respinta)  {
		this.ds_causale_respinta=ds_causale_respinta;
	}
	public java.sql.Date getDt_app_formale () {
		return dt_app_formale;
	}
	public void setDt_app_formale(java.sql.Date dt_app_formale)  {
		this.dt_app_formale=dt_app_formale;
	}
	public java.lang.String getTipologia () {
		return tipologia;
	}
	public void setTipologia(java.lang.String tipologia)  {
		this.tipologia=tipologia;
	}
	public java.lang.String getTipologia_fin () {
		return tipologia_fin;
	}
	public void setTipologia_fin(java.lang.String tipologia_fin)  {
		this.tipologia_fin=tipologia_fin;
	}
	public java.lang.Integer getPg_riga () {
		return pg_riga;
	}
	public void setPg_riga(java.lang.Integer pg_riga)  {
		this.pg_riga=pg_riga;
	}
	public java.lang.String getCd_cdr_assegnatario () {
		return cd_cdr_assegnatario;
	}
	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario)  {
		this.cd_cdr_assegnatario=cd_cdr_assegnatario;
	}
	public java.lang.String getDs_cdr_assegnatario () {
		return ds_cdr_assegnatario;
	}
	public void setDs_cdr_assegnatario(java.lang.String ds_cdr_assegnatario)  {
		this.ds_cdr_assegnatario=ds_cdr_assegnatario;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
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
	public java.lang.Long getNr_livello () {
		return nr_livello;
	}
	public void setNr_livello(java.lang.Long nr_livello)  {
		this.nr_livello=nr_livello;
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
	public java.lang.String getCd_livello3 () {
		return cd_livello3;
	}
	public void setCd_livello3(java.lang.String cd_livello3)  {
		this.cd_livello3=cd_livello3;
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
	public java.lang.String getCd_tipo_modulo () {
		return cd_tipo_modulo;
	}
	public void setCd_tipo_modulo(java.lang.String cd_tipo_modulo)  {
		this.cd_tipo_modulo=cd_tipo_modulo;
	}
	public java.lang.String getDs_tipo_modulo () {
		return ds_tipo_modulo;
	}
	public void setDs_tipo_modulo(java.lang.String ds_tipo_modulo)  {
		this.ds_tipo_modulo=ds_tipo_modulo;
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
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getDs_dipartimento () {
		return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
		this.ds_dipartimento=ds_dipartimento;
	}
	public java.lang.String getCd_cds_area () {
		return cd_cds_area;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
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
	public java.sql.Date getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Date dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.String getDescrizione () {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_int () {
		return im_spese_gest_decentrata_int;
	}
	public void setIm_spese_gest_decentrata_int(java.math.BigDecimal im_spese_gest_decentrata_int)  {
		this.im_spese_gest_decentrata_int=im_spese_gest_decentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_est () {
		return im_spese_gest_decentrata_est;
	}
	public void setIm_spese_gest_decentrata_est(java.math.BigDecimal im_spese_gest_decentrata_est)  {
		this.im_spese_gest_decentrata_est=im_spese_gest_decentrata_est;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_int () {
		return im_spese_gest_accentrata_int;
	}
	public void setIm_spese_gest_accentrata_int(java.math.BigDecimal im_spese_gest_accentrata_int)  {
		this.im_spese_gest_accentrata_int=im_spese_gest_accentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_est () {
		return im_spese_gest_accentrata_est;
	}
	public void setIm_spese_gest_accentrata_est(java.math.BigDecimal im_spese_gest_accentrata_est)  {
		this.im_spese_gest_accentrata_est=im_spese_gest_accentrata_est;
	}
	public java.math.BigDecimal getIm_entrata () {
		return im_entrata;
	}
	public void setIm_entrata(java.math.BigDecimal im_entrata)  {
		this.im_entrata=im_entrata;
	}
/*	public java.lang.Long getAbs_tot_variazione () {
		return abs_tot_variazione;
	}
	public void setAbs_tot_variazione(java.lang.Long abs_tot_variazione)  {
		this.abs_tot_variazione=abs_tot_variazione;
	}*/
	
	
//	Raggruppamenti
	public Boolean getRagr_NO_TIPO() {
		return ragr_NO_TIPO;
	}
	public void setRagr_NO_TIPO(Boolean ragr_NO_TIPO) {
		this.ragr_NO_TIPO = ragr_NO_TIPO;
	}
	public Boolean getRagr_PREL_FON() {
		return ragr_PREL_FON;
	}
	public void setRagr_PREL_FON(Boolean ragr_PREL_FON) {
		this.ragr_PREL_FON = ragr_PREL_FON;
	}
	public Boolean getRagr_STO_E_CDS() {
		return ragr_STO_E_CDS;
	}
	public void setRagr_STO_E_CDS(Boolean ragr_STO_E_CDS) {
		this.ragr_STO_E_CDS = ragr_STO_E_CDS;
	}
	public Boolean getRagr_STO_E_TOT() {
		return ragr_STO_E_TOT;
	}
	public void setRagr_STO_E_TOT(Boolean ragr_STO_E_TOT) {
		this.ragr_STO_E_TOT = ragr_STO_E_TOT;
	}
	public Boolean getRagr_STO_S_CDS() {
		return ragr_STO_S_CDS;
	}
	public void setRagr_STO_S_CDS(Boolean ragr_STO_S_CDS) {
		this.ragr_STO_S_CDS = ragr_STO_S_CDS;
	}
	public Boolean getRagr_STO_S_TOT() {
		return ragr_STO_S_TOT;
	}
	public void setRagr_STO_S_TOT(Boolean ragr_STO_S_TOT) {
		this.ragr_STO_S_TOT = ragr_STO_S_TOT;
	}
	public Boolean getRagr_VAR_MENO_CDS() {
		return ragr_VAR_MENO_CDS;
	}
	public void setRagr_VAR_MENO_CDS(Boolean ragr_VAR_MENO_CDS) {
		this.ragr_VAR_MENO_CDS = ragr_VAR_MENO_CDS;
	}
	public Boolean getRagr_VAR_MENO_TOT() {
		return ragr_VAR_MENO_TOT;
	}
	public void setRagr_VAR_MENO_TOT(Boolean ragr_VAR_MENO_TOT) {
		this.ragr_VAR_MENO_TOT = ragr_VAR_MENO_TOT;
	}
	public Boolean getRagr_VAR_PIU_CDS() {
		return ragr_VAR_PIU_CDS;
	}
	public void setRagr_VAR_PIU_CDS(Boolean ragr_VAR_PIU_CDS) {
		this.ragr_VAR_PIU_CDS = ragr_VAR_PIU_CDS;
	}
	public Boolean getRagr_VAR_PIU_TOT() {
		return ragr_VAR_PIU_TOT;
	}
	public void setRagr_VAR_PIU_TOT(Boolean ragr_VAR_PIU_TOT) {
		this.ragr_VAR_PIU_TOT = ragr_VAR_PIU_TOT;
	}
	
	public java.util.Dictionary getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}
	
	public java.sql.Date getDt_approvazione_a() {
		return dt_approvazione_a;
	}
	public void setDt_approvazione_a(java.sql.Date dt_approvazione_a) {
		this.dt_approvazione_a = dt_approvazione_a;
	}
	public java.sql.Date getDt_approvazione_da() {
		return dt_approvazione_da;
	}
	public void setDt_approvazione_da(java.sql.Date dt_approvazione_da) {
		this.dt_approvazione_da = dt_approvazione_da;
	}
	public java.lang.Long getAbs_tot_variazione_a() {
		return abs_tot_variazione_a;
	}
	public void setAbs_tot_variazione_a(java.lang.Long abs_tot_variazione_a) {
		this.abs_tot_variazione_a = abs_tot_variazione_a;
	}
	public java.lang.Long getAbs_tot_variazione_da() {
		return abs_tot_variazione_da;
	}
	public void setAbs_tot_variazione_da(java.lang.Long abs_tot_variazione_da) {
		this.abs_tot_variazione_da = abs_tot_variazione_da;
	}
	
	public V_classificazione_vociBulk getV_classificazione_voci() {
		return v_classificazione_voci;
	}
	public void setV_classificazione_voci(V_classificazione_vociBulk v_classificazione_voci) {
		this.v_classificazione_voci = v_classificazione_voci;
	}
	public java.sql.Date getDt_approvazione() {
		return dt_approvazione;
	}
	public void setDt_approvazione(java.sql.Date dt_approvazione) {
		this.dt_approvazione = dt_approvazione;
	}
	public java.lang.Long getAbs_tot_variazione() {
		return abs_tot_variazione;
	}
	public void setAbs_tot_variazione(java.lang.Long abs_tot_variazione) {
		this.abs_tot_variazione = abs_tot_variazione;
	}
	public java.lang.String getDs_stato() {
		return ds_stato;
	}
	public void setDs_stato(java.lang.String ds_stato) {
		this.ds_stato = ds_stato;
	}
	public java.lang.String getDs_tipo_variazione() {
		return ds_tipo_variazione;
	}
	public void setDs_tipo_variazione(java.lang.String ds_tipo_variazione) {
		this.ds_tipo_variazione = ds_tipo_variazione;
	}
	public java.lang.String getDs_tipologia_fin() {
		return ds_tipologia_fin;
	}
	public void setDs_tipologia_fin(java.lang.String ds_tipologia_fin) {
		this.ds_tipologia_fin = ds_tipologia_fin;
	}
	public Boolean getRagr_REST_FOND() {
		return ragr_REST_FOND;
	}
	public void setRagr_REST_FOND(Boolean ragr_REST_FOND) {
		this.ragr_REST_FOND = ragr_REST_FOND;
	}
	public Boolean getRagr_STO_E_AREA() {
		return ragr_STO_E_AREA;
	}
	public void setRagr_STO_E_AREA(Boolean ragr_STO_E_AREA) {
		this.ragr_STO_E_AREA = ragr_STO_E_AREA;
	}
	public Boolean getRagr_STO_S_AREA() {
		return ragr_STO_S_AREA;
	}
	public void setRagr_STO_S_AREA(Boolean ragr_STO_S_AREA) {
		this.ragr_STO_S_AREA = ragr_STO_S_AREA;
	}
	public Boolean getRagr_VAR_MENO_FON() {
		return ragr_VAR_MENO_FON;
	}
	public void setRagr_VAR_MENO_FON(Boolean ragr_VAR_MENO_FON) {
		this.ragr_VAR_MENO_FON = ragr_VAR_MENO_FON;
	}
	public Boolean getRagr_VAR_PIU_FON() {
		return ragr_VAR_PIU_FON;
	}
	public void setRagr_VAR_PIU_FON(Boolean ragr_VAR_PIU_FON) {
		this.ragr_VAR_PIU_FON = ragr_VAR_PIU_FON;
	}

}