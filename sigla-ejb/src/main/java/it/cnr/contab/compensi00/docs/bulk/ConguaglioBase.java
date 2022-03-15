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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.math.BigDecimal;

public class ConguaglioBase extends ConguaglioKey implements Keyed {
	// CD_CDS_COMPENSO VARCHAR(30)
	private java.lang.String cd_cds_compenso;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_PROVINCIA VARCHAR(10) NOT NULL
	private java.lang.String cd_provincia;

	// CD_REGIONE VARCHAR(10) NOT NULL
	private java.lang.String cd_regione;

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private java.lang.String cd_trattamento;

	// CD_UO_COMPENSO VARCHAR(30)
	private java.lang.String cd_uo_compenso;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// CODICE_FISCALE_ESTERNO VARCHAR(20)
	private java.lang.String codice_fiscale_esterno;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DETRAZIONI_AL_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_al_dovuto;

	// DETRAZIONI_AL_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_al_esterno;

	// DETRAZIONI_AL_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_al_goduto;

	// DETRAZIONI_CO_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_co_dovuto;

	// DETRAZIONI_CO_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_co_esterno;

	// DETRAZIONI_CO_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_co_goduto;

	// DETRAZIONI_FI_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_fi_dovuto;

	// DETRAZIONI_FI_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_fi_esterno;

	// DETRAZIONI_FI_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_fi_goduto;

	// DETRAZIONI_LA_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_la_dovuto;

	// DETRAZIONI_LA_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_la_esterno;

	// DETRAZIONI_LA_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_la_goduto;

	// DETRAZIONI_PE_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_pe_dovuto;

	// DETRAZIONI_PE_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_pe_esterno;

	// DETRAZIONI_PE_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal detrazioni_pe_goduto;

	// DS_CONGUAGLIO VARCHAR(300)
	private java.lang.String ds_conguaglio;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_A_COMPETENZA_ESTERNO TIMESTAMP
	private java.sql.Timestamp dt_a_competenza_esterno;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// DT_DA_COMPETENZA_ESTERNO TIMESTAMP
	private java.sql.Timestamp dt_da_competenza_esterno;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// ESERCIZIO_COMPENSO DECIMAL(4,0)
	private java.lang.Integer esercizio_compenso;

	// FL_ACCANTONA_ADD_TERR CHAR(1) NOT NULL
	private java.lang.Boolean fl_accantona_add_terr;

	// FL_ESCLUDI_QVARIA_DEDUZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_escludi_qvaria_deduzione;

	// FL_INTERA_QFISSA_DEDUZIONE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intera_qfissa_deduzione;

	// IMPONIBILE_FISCALE_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_fiscale_esterno;

	// IMPONIBILE_IRPEF_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_irpef_lordo;

	// IMPONIBILE_IRPEF_NETTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_irpef_netto;

	// IM_ADDCOM_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addcom_dovuto;

	// IM_ADDCOM_ESTERNO DECIMAL(15,2)
	private java.math.BigDecimal im_addcom_esterno;

	// IM_ADDCOM_GODUTO DECIMAL(15,2)
	private java.math.BigDecimal im_addcom_goduto;

	// IM_ADDCOM_RATE_ESEPREC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addcom_rate_eseprec;

	// IM_ADDPROV_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addprov_dovuto;

	// IM_ADDPROV_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addprov_esterno;

	// IM_ADDPROV_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addprov_goduto;

	// IM_ADDPROV_RATE_ESEPREC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addprov_rate_eseprec;

	// IM_ADDREG_DOVUTO DECIMAL(15,2)
	private java.math.BigDecimal im_addreg_dovuto;

	// IM_ADDREG_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addreg_esterno;

	// IM_ADDREG_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addreg_goduto;

	// IM_ADDREG_RATE_ESEPREC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_addreg_rate_eseprec;

	// IM_DEDUZIONE_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_dovuto;

	// IM_DEDUZIONE_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_goduto;

	// IM_DETRAZIONE_PERSONALE_ANAG DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione_personale_anag;

	// IM_IRPEF_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_irpef_dovuto;

	// IM_IRPEF_ESTERNO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_irpef_esterno;

	// IM_IRPEF_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_irpef_goduto;

	// IM_LORDO_CONGUAGLIO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_lordo_conguaglio;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// NUMERO_GIORNI DECIMAL(8,0) NOT NULL
	private java.lang.Integer numero_giorni;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// PG_COMPENSO DECIMAL(10,0)
	private java.lang.Long pg_compenso;

	// PG_COMUNE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_comune;

	// RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;

	// TI_ANAGRAFICO CHAR(1) NOT NULL
	private java.lang.String ti_anagrafico;

	// IM_DEDUZIONE_FAMILY_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_family_dovuto;
	
	// IM_FAMILY_DOVUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_family_dovuto;
	
	// IM_DEDUZIONE_FAMILY_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_family_goduto;
	
	// IM_FAMILY_GODUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_family_goduto;

	// FL_DETRAZIONI_FAM_INTERO_ANNO CHAR(1) NOT NULL
	private java.lang.Boolean fl_detrazioni_fam_intero_anno;
	
	// NUMERO_GIORNI_ESTERNO DECIMAL(3,0) NULL
	private java.lang.Long numero_giorni_esterno;
	
	// PG_COMUNE_ADDCOM_ESTERNO DECIMAL(10,0)
	private java.lang.Long pg_comune_addcom_esterno;
	
	// IM_CORI_SOSPESO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cori_sospeso;	
	
	// FL_NO_DETRAZIONI_ALTRE CHAR(1) NOT NULL
	private java.lang.Boolean fl_no_detrazioni_altre;
	
	// FL_NO_DETRAZIONI_FAMILY CHAR(1) NOT NULL
	private java.lang.Boolean fl_no_detrazioni_family;
	
	// FL_DETRAZIONI_ALTRI_TIPI CHAR(1) NOT NULL
	private java.lang.Boolean fl_detrazioni_altri_tipi;	
	
	// FL_NO_CREDITO_IRPEF CHAR(1) NOT NULL
	private java.lang.Boolean fl_no_credito_irpef;

	public Boolean getFl_no_credito_cuneo_irpef() {
		return fl_no_credito_cuneo_irpef;
	}

	public void setFl_no_credito_cuneo_irpef(Boolean fl_no_credito_cuneo_irpef) {
		this.fl_no_credito_cuneo_irpef = fl_no_credito_cuneo_irpef;
	}

	public Boolean getFl_no_detr_cuneo_irpef() {
		return fl_no_detr_cuneo_irpef;
	}

	public void setFl_no_detr_cuneo_irpef(Boolean fl_no_detr_cuneo_irpef) {
		this.fl_no_detr_cuneo_irpef = fl_no_detr_cuneo_irpef;
	}

	private java.lang.Boolean fl_no_credito_cuneo_irpef;
	private java.lang.Boolean fl_no_detr_cuneo_irpef;

	public BigDecimal getIm_cred_irpef_par_det_goduto() {
		return im_cred_irpef_par_det_goduto;
	}

	public void setIm_cred_irpef_par_det_goduto(BigDecimal im_cred_irpef_par_det_goduto) {
		this.im_cred_irpef_par_det_goduto = im_cred_irpef_par_det_goduto;
	}

	public BigDecimal getIm_cred_irpef_par_det_dovuto() {
		return im_cred_irpef_par_det_dovuto;
	}

	public void setIm_cred_irpef_par_det_dovuto(BigDecimal im_cred_irpef_par_det_dovuto) {
		this.im_cred_irpef_par_det_dovuto = im_cred_irpef_par_det_dovuto;
	}

	// IM_CREDITO_IRPEF_GODUTO(15,2)
	private java.math.BigDecimal im_credito_irpef_goduto;
	private java.math.BigDecimal im_cred_irpef_par_det_goduto;
	private java.math.BigDecimal im_cred_irpef_par_det_dovuto;

	public BigDecimal getDetrazioni_rid_cuneo_dovuto() {
		return detrazioni_rid_cuneo_dovuto;
	}

	public void setDetrazioni_rid_cuneo_dovuto(BigDecimal detrazioni_rid_cuneo_dovuto) {
		this.detrazioni_rid_cuneo_dovuto = detrazioni_rid_cuneo_dovuto;
	}

	public BigDecimal getDetrazioni_rid_cuneo_goduto() {
		return detrazioni_rid_cuneo_goduto;
	}

	public void setDetrazioni_rid_cuneo_goduto(BigDecimal detrazioni_rid_cuneo_goduto) {
		this.detrazioni_rid_cuneo_goduto = detrazioni_rid_cuneo_goduto;
	}

	public BigDecimal getDetrazioni_rid_cuneo_esterno() {
		return detrazioni_rid_cuneo_esterno;
	}

	public void setDetrazioni_rid_cuneo_esterno(BigDecimal detrazioni_rid_cuneo_esterno) {
		this.detrazioni_rid_cuneo_esterno = detrazioni_rid_cuneo_esterno;
	}

	// IM_CREDITO_IRPEF_DOVUTO(15,2)
	private java.math.BigDecimal im_credito_irpef_dovuto;

	public BigDecimal getIm_bonus_irpef_dovuto() {
		return im_bonus_irpef_dovuto;
	}

	public void setIm_bonus_irpef_dovuto(BigDecimal im_bonus_irpef_dovuto) {
		this.im_bonus_irpef_dovuto = im_bonus_irpef_dovuto;
	}

	public BigDecimal getIm_bonus_irpef_goduto() {
		return im_bonus_irpef_goduto;
	}

	public void setIm_bonus_irpef_goduto(BigDecimal im_bonus_irpef_goduto) {
		this.im_bonus_irpef_goduto = im_bonus_irpef_goduto;
	}

	private java.math.BigDecimal im_bonus_irpef_dovuto;
	private java.math.BigDecimal im_bonus_irpef_goduto;

	private java.math.BigDecimal detrazioni_rid_cuneo_dovuto;
	private java.math.BigDecimal detrazioni_rid_cuneo_goduto;
	private java.math.BigDecimal detrazioni_rid_cuneo_esterno;
public ConguaglioBase() {
	super();
}
public ConguaglioBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_conguaglio) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_conguaglio);
}
/* 
 * Getter dell'attributo cd_cds_compenso
 */
public java.lang.String getCd_cds_compenso() {
	return cd_cds_compenso;
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_provincia
 */
public java.lang.String getCd_provincia() {
	return cd_provincia;
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
/* 
 * Getter dell'attributo cd_uo_compenso
 */
public java.lang.String getCd_uo_compenso() {
	return cd_uo_compenso;
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo codice_fiscale_esterno
 */
public java.lang.String getCodice_fiscale_esterno() {
	return codice_fiscale_esterno;
}
/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}
/* 
 * Getter dell'attributo detrazioni_al_dovuto
 */
public java.math.BigDecimal getDetrazioni_al_dovuto() {
	return detrazioni_al_dovuto;
}
/* 
 * Getter dell'attributo detrazioni_al_esterno
 */
public java.math.BigDecimal getDetrazioni_al_esterno() {
	return detrazioni_al_esterno;
}
/* 
 * Getter dell'attributo detrazioni_al_goduto
 */
public java.math.BigDecimal getDetrazioni_al_goduto() {
	return detrazioni_al_goduto;
}
/* 
 * Getter dell'attributo detrazioni_co_dovuto
 */
public java.math.BigDecimal getDetrazioni_co_dovuto() {
	return detrazioni_co_dovuto;
}
/* 
 * Getter dell'attributo detrazioni_co_esterno
 */
public java.math.BigDecimal getDetrazioni_co_esterno() {
	return detrazioni_co_esterno;
}
/* 
 * Getter dell'attributo detrazioni_co_goduto
 */
public java.math.BigDecimal getDetrazioni_co_goduto() {
	return detrazioni_co_goduto;
}
/* 
 * Getter dell'attributo detrazioni_fi_dovuto
 */
public java.math.BigDecimal getDetrazioni_fi_dovuto() {
	return detrazioni_fi_dovuto;
}
/* 
 * Getter dell'attributo detrazioni_fi_esterno
 */
public java.math.BigDecimal getDetrazioni_fi_esterno() {
	return detrazioni_fi_esterno;
}
/* 
 * Getter dell'attributo detrazioni_fi_goduto
 */
public java.math.BigDecimal getDetrazioni_fi_goduto() {
	return detrazioni_fi_goduto;
}
/* 
 * Getter dell'attributo detrazioni_la_dovuto
 */
public java.math.BigDecimal getDetrazioni_la_dovuto() {
	return detrazioni_la_dovuto;
}
/* 
 * Getter dell'attributo detrazioni_la_esterno
 */
public java.math.BigDecimal getDetrazioni_la_esterno() {
	return detrazioni_la_esterno;
}
/* 
 * Getter dell'attributo detrazioni_la_goduto
 */
public java.math.BigDecimal getDetrazioni_la_goduto() {
	return detrazioni_la_goduto;
}
/* 
 * Getter dell'attributo detrazioni_pe_dovuto
 */
public java.math.BigDecimal getDetrazioni_pe_dovuto() {
	return detrazioni_pe_dovuto;
}
/* 
 * Getter dell'attributo detrazioni_pe_esterno
 */
public java.math.BigDecimal getDetrazioni_pe_esterno() {
	return detrazioni_pe_esterno;
}
/* 
 * Getter dell'attributo detrazioni_pe_goduto
 */
public java.math.BigDecimal getDetrazioni_pe_goduto() {
	return detrazioni_pe_goduto;
}
/* 
 * Getter dell'attributo ds_conguaglio
 */
public java.lang.String getDs_conguaglio() {
	return ds_conguaglio;
}
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}
/* 
 * Getter dell'attributo dt_a_competenza_esterno
 */
public java.sql.Timestamp getDt_a_competenza_esterno() {
	return dt_a_competenza_esterno;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}
/* 
 * Getter dell'attributo dt_da_competenza_esterno
 */
public java.sql.Timestamp getDt_da_competenza_esterno() {
	return dt_da_competenza_esterno;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo esercizio_compenso
 */
public java.lang.Integer getEsercizio_compenso() {
	return esercizio_compenso;
}
/* 
 * Getter dell'attributo fl_accantona_add_terr
 */
public java.lang.Boolean getFl_accantona_add_terr() {
	return fl_accantona_add_terr;
}
/* 
 * Getter dell'attributo fl_escludi_qvaria_deduzione
 */
public java.lang.Boolean getFl_escludi_qvaria_deduzione() {
	return fl_escludi_qvaria_deduzione;
}
/* 
 * Getter dell'attributo fl_intera_qfissa_deduzione
 */
public java.lang.Boolean getFl_intera_qfissa_deduzione() {
	return fl_intera_qfissa_deduzione;
}
/* 
 * Getter dell'attributo im_addcom_dovuto
 */
public java.math.BigDecimal getIm_addcom_dovuto() {
	return im_addcom_dovuto;
}
/* 
 * Getter dell'attributo im_addcom_esterno
 */
public java.math.BigDecimal getIm_addcom_esterno() {
	return im_addcom_esterno;
}
/* 
 * Getter dell'attributo im_addcom_goduto
 */
public java.math.BigDecimal getIm_addcom_goduto() {
	return im_addcom_goduto;
}
/* 
 * Getter dell'attributo im_addcom_rate_eseprec
 */
public java.math.BigDecimal getIm_addcom_rate_eseprec() {
	return im_addcom_rate_eseprec;
}
/* 
 * Getter dell'attributo im_addprov_dovuto
 */
public java.math.BigDecimal getIm_addprov_dovuto() {
	return im_addprov_dovuto;
}
/* 
 * Getter dell'attributo im_addprov_esterno
 */
public java.math.BigDecimal getIm_addprov_esterno() {
	return im_addprov_esterno;
}
/* 
 * Getter dell'attributo im_addprov_goduto
 */
public java.math.BigDecimal getIm_addprov_goduto() {
	return im_addprov_goduto;
}
/* 
 * Getter dell'attributo im_addprov_rate_eseprec
 */
public java.math.BigDecimal getIm_addprov_rate_eseprec() {
	return im_addprov_rate_eseprec;
}
/* 
 * Getter dell'attributo im_addreg_dovuto
 */
public java.math.BigDecimal getIm_addreg_dovuto() {
	return im_addreg_dovuto;
}
/* 
 * Getter dell'attributo im_addreg_esterno
 */
public java.math.BigDecimal getIm_addreg_esterno() {
	return im_addreg_esterno;
}
/* 
 * Getter dell'attributo im_addreg_goduto
 */
public java.math.BigDecimal getIm_addreg_goduto() {
	return im_addreg_goduto;
}
/* 
 * Getter dell'attributo im_addreg_rate_eseprec
 */
public java.math.BigDecimal getIm_addreg_rate_eseprec() {
	return im_addreg_rate_eseprec;
}
/* 
 * Getter dell'attributo im_deduzione_dovuto
 */
public java.math.BigDecimal getIm_deduzione_dovuto() {
	return im_deduzione_dovuto;
}
/* 
 * Getter dell'attributo im_deduzione_goduto
 */
public java.math.BigDecimal getIm_deduzione_goduto() {
	return im_deduzione_goduto;
}
/* 
 * Getter dell'attributo im_detrazione_personale_anag
 */
public java.math.BigDecimal getIm_detrazione_personale_anag() {
	return im_detrazione_personale_anag;
}
/* 
 * Getter dell'attributo im_irpef_dovuto
 */
public java.math.BigDecimal getIm_irpef_dovuto() {
	return im_irpef_dovuto;
}
/* 
 * Getter dell'attributo im_irpef_esterno
 */
public java.math.BigDecimal getIm_irpef_esterno() {
	return im_irpef_esterno;
}
/* 
 * Getter dell'attributo im_irpef_goduto
 */
public java.math.BigDecimal getIm_irpef_goduto() {
	return im_irpef_goduto;
}
/* 
 * Getter dell'attributo im_lordo_conguaglio
 */
public java.math.BigDecimal getIm_lordo_conguaglio() {
	return im_lordo_conguaglio;
}
/* 
 * Getter dell'attributo imponibile_fiscale_esterno
 */
public java.math.BigDecimal getImponibile_fiscale_esterno() {
	return imponibile_fiscale_esterno;
}
/* 
 * Getter dell'attributo imponibile_irpef_lordo
 */
public java.math.BigDecimal getImponibile_irpef_lordo() {
	return imponibile_irpef_lordo;
}
/* 
 * Getter dell'attributo imponibile_irpef_netto
 */
public java.math.BigDecimal getImponibile_irpef_netto() {
	return imponibile_irpef_netto;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo numero_giorni
 */
public java.lang.Integer getNumero_giorni() {
	return numero_giorni;
}
/* 
 * Getter dell'attributo partita_iva
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
/* 
 * Getter dell'attributo pg_compenso
 */
public java.lang.Long getPg_compenso() {
	return pg_compenso;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
/* 
 * Getter dell'attributo ragione_sociale
 */
public java.lang.String getRagione_sociale() {
	return ragione_sociale;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Setter dell'attributo cd_cds_compenso
 */
public void setCd_cds_compenso(java.lang.String cd_cds_compenso) {
	this.cd_cds_compenso = cd_cds_compenso;
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_provincia
 */
public void setCd_provincia(java.lang.String cd_provincia) {
	this.cd_provincia = cd_provincia;
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
/* 
 * Setter dell'attributo cd_uo_compenso
 */
public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
	this.cd_uo_compenso = cd_uo_compenso;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo codice_fiscale_esterno
 */
public void setCodice_fiscale_esterno(java.lang.String codice_fiscale_esterno) {
	this.codice_fiscale_esterno = codice_fiscale_esterno;
}
/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}
/* 
 * Setter dell'attributo detrazioni_al_dovuto
 */
public void setDetrazioni_al_dovuto(java.math.BigDecimal detrazioni_al_dovuto) {
	this.detrazioni_al_dovuto = detrazioni_al_dovuto;
}
/* 
 * Setter dell'attributo detrazioni_al_esterno
 */
public void setDetrazioni_al_esterno(java.math.BigDecimal detrazioni_al_esterno) {
	this.detrazioni_al_esterno = detrazioni_al_esterno;
}
/* 
 * Setter dell'attributo detrazioni_al_goduto
 */
public void setDetrazioni_al_goduto(java.math.BigDecimal detrazioni_al_goduto) {
	this.detrazioni_al_goduto = detrazioni_al_goduto;
}
/* 
 * Setter dell'attributo detrazioni_co_dovuto
 */
public void setDetrazioni_co_dovuto(java.math.BigDecimal detrazioni_co_dovuto) {
	this.detrazioni_co_dovuto = detrazioni_co_dovuto;
}
/* 
 * Setter dell'attributo detrazioni_co_esterno
 */
public void setDetrazioni_co_esterno(java.math.BigDecimal detrazioni_co_esterno) {
	this.detrazioni_co_esterno = detrazioni_co_esterno;
}
/* 
 * Setter dell'attributo detrazioni_co_goduto
 */
public void setDetrazioni_co_goduto(java.math.BigDecimal detrazioni_co_goduto) {
	this.detrazioni_co_goduto = detrazioni_co_goduto;
}
/* 
 * Setter dell'attributo detrazioni_fi_dovuto
 */
public void setDetrazioni_fi_dovuto(java.math.BigDecimal detrazioni_fi_dovuto) {
	this.detrazioni_fi_dovuto = detrazioni_fi_dovuto;
}
/* 
 * Setter dell'attributo detrazioni_fi_esterno
 */
public void setDetrazioni_fi_esterno(java.math.BigDecimal detrazioni_fi_esterno) {
	this.detrazioni_fi_esterno = detrazioni_fi_esterno;
}
/* 
 * Setter dell'attributo detrazioni_fi_goduto
 */
public void setDetrazioni_fi_goduto(java.math.BigDecimal detrazioni_fi_goduto) {
	this.detrazioni_fi_goduto = detrazioni_fi_goduto;
}
/* 
 * Setter dell'attributo detrazioni_la_dovuto
 */
public void setDetrazioni_la_dovuto(java.math.BigDecimal detrazioni_la_dovuto) {
	this.detrazioni_la_dovuto = detrazioni_la_dovuto;
}
/* 
 * Setter dell'attributo detrazioni_la_esterno
 */
public void setDetrazioni_la_esterno(java.math.BigDecimal detrazioni_la_esterno) {
	this.detrazioni_la_esterno = detrazioni_la_esterno;
}
/* 
 * Setter dell'attributo detrazioni_la_goduto
 */
public void setDetrazioni_la_goduto(java.math.BigDecimal detrazioni_la_goduto) {
	this.detrazioni_la_goduto = detrazioni_la_goduto;
}
/* 
 * Setter dell'attributo detrazioni_pe_dovuto
 */
public void setDetrazioni_pe_dovuto(java.math.BigDecimal detrazioni_pe_dovuto) {
	this.detrazioni_pe_dovuto = detrazioni_pe_dovuto;
}
/* 
 * Setter dell'attributo detrazioni_pe_esterno
 */
public void setDetrazioni_pe_esterno(java.math.BigDecimal detrazioni_pe_esterno) {
	this.detrazioni_pe_esterno = detrazioni_pe_esterno;
}
/* 
 * Setter dell'attributo detrazioni_pe_goduto
 */
public void setDetrazioni_pe_goduto(java.math.BigDecimal detrazioni_pe_goduto) {
	this.detrazioni_pe_goduto = detrazioni_pe_goduto;
}
/* 
 * Setter dell'attributo ds_conguaglio
 */
public void setDs_conguaglio(java.lang.String ds_conguaglio) {
	this.ds_conguaglio = ds_conguaglio;
}
/* 
 * Setter dell'attributo dt_a_competenza_coge
 */
public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
	this.dt_a_competenza_coge = dt_a_competenza_coge;
}
/* 
 * Setter dell'attributo dt_a_competenza_esterno
 */
public void setDt_a_competenza_esterno(java.sql.Timestamp dt_a_competenza_esterno) {
	this.dt_a_competenza_esterno = dt_a_competenza_esterno;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_da_competenza_coge
 */
public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
	this.dt_da_competenza_coge = dt_da_competenza_coge;
}
/* 
 * Setter dell'attributo dt_da_competenza_esterno
 */
public void setDt_da_competenza_esterno(java.sql.Timestamp dt_da_competenza_esterno) {
	this.dt_da_competenza_esterno = dt_da_competenza_esterno;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo esercizio_compenso
 */
public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
	this.esercizio_compenso = esercizio_compenso;
}
/* 
 * Setter dell'attributo fl_accantona_add_terr
 */
public void setFl_accantona_add_terr(java.lang.Boolean fl_accantona_add_terr) {
	this.fl_accantona_add_terr = fl_accantona_add_terr;
}
/* 
 * Setter dell'attributo fl_escludi_qvaria_deduzione
 */
public void setFl_escludi_qvaria_deduzione(java.lang.Boolean fl_escludi_qvaria_deduzione) {
	this.fl_escludi_qvaria_deduzione = fl_escludi_qvaria_deduzione;
}
/* 
 * Setter dell'attributo fl_intera_qfissa_deduzione
 */
public void setFl_intera_qfissa_deduzione(java.lang.Boolean fl_intera_qfissa_deduzione) {
	this.fl_intera_qfissa_deduzione = fl_intera_qfissa_deduzione;
}
/* 
 * Setter dell'attributo im_addcom_dovuto
 */
public void setIm_addcom_dovuto(java.math.BigDecimal im_addcom_dovuto) {
	this.im_addcom_dovuto = im_addcom_dovuto;
}
/* 
 * Setter dell'attributo im_addcom_esterno
 */
public void setIm_addcom_esterno(java.math.BigDecimal im_addcom_esterno) {
	this.im_addcom_esterno = im_addcom_esterno;
}
/* 
 * Setter dell'attributo im_addcom_goduto
 */
public void setIm_addcom_goduto(java.math.BigDecimal im_addcom_goduto) {
	this.im_addcom_goduto = im_addcom_goduto;
}
/* 
 * Setter dell'attributo im_addcom_rate_eseprec
 */
public void setIm_addcom_rate_eseprec(java.math.BigDecimal im_addcom_rate_eseprec) {
	this.im_addcom_rate_eseprec = im_addcom_rate_eseprec;
}
/* 
 * Setter dell'attributo im_addprov_dovuto
 */
public void setIm_addprov_dovuto(java.math.BigDecimal im_addprov_dovuto) {
	this.im_addprov_dovuto = im_addprov_dovuto;
}
/* 
 * Setter dell'attributo im_addprov_esterno
 */
public void setIm_addprov_esterno(java.math.BigDecimal im_addprov_esterno) {
	this.im_addprov_esterno = im_addprov_esterno;
}
/* 
 * Setter dell'attributo im_addprov_goduto
 */
public void setIm_addprov_goduto(java.math.BigDecimal im_addprov_goduto) {
	this.im_addprov_goduto = im_addprov_goduto;
}
/* 
 * Setter dell'attributo im_addprov_rate_eseprec
 */
public void setIm_addprov_rate_eseprec(java.math.BigDecimal im_addprov_rate_eseprec) {
	this.im_addprov_rate_eseprec = im_addprov_rate_eseprec;
}
/* 
 * Setter dell'attributo im_addreg_dovuto
 */
public void setIm_addreg_dovuto(java.math.BigDecimal im_addreg_dovuto) {
	this.im_addreg_dovuto = im_addreg_dovuto;
}
/* 
 * Setter dell'attributo im_addreg_esterno
 */
public void setIm_addreg_esterno(java.math.BigDecimal im_addreg_esterno) {
	this.im_addreg_esterno = im_addreg_esterno;
}
/* 
 * Setter dell'attributo im_addreg_goduto
 */
public void setIm_addreg_goduto(java.math.BigDecimal im_addreg_goduto) {
	this.im_addreg_goduto = im_addreg_goduto;
}
/* 
 * Setter dell'attributo im_addreg_rate_eseprec
 */
public void setIm_addreg_rate_eseprec(java.math.BigDecimal im_addreg_rate_eseprec) {
	this.im_addreg_rate_eseprec = im_addreg_rate_eseprec;
}
/* 
 * Setter dell'attributo im_deduzione_dovuto
 */
public void setIm_deduzione_dovuto(java.math.BigDecimal im_deduzione_dovuto) {
	this.im_deduzione_dovuto = im_deduzione_dovuto;
}
/* 
 * Setter dell'attributo im_deduzione_goduto
 */
public void setIm_deduzione_goduto(java.math.BigDecimal im_deduzione_goduto) {
	this.im_deduzione_goduto = im_deduzione_goduto;
}
/* 
 * Setter dell'attributo im_detrazione_personale_anag
 */
public void setIm_detrazione_personale_anag(java.math.BigDecimal im_detrazione_personale_anag) {
	this.im_detrazione_personale_anag = im_detrazione_personale_anag;
}
/* 
 * Setter dell'attributo im_irpef_dovuto
 */
public void setIm_irpef_dovuto(java.math.BigDecimal im_irpef_dovuto) {
	this.im_irpef_dovuto = im_irpef_dovuto;
}
/* 
 * Setter dell'attributo im_irpef_esterno
 */
public void setIm_irpef_esterno(java.math.BigDecimal im_irpef_esterno) {
	this.im_irpef_esterno = im_irpef_esterno;
}
/* 
 * Setter dell'attributo im_irpef_goduto
 */
public void setIm_irpef_goduto(java.math.BigDecimal im_irpef_goduto) {
	this.im_irpef_goduto = im_irpef_goduto;
}
/* 
 * Setter dell'attributo im_lordo_conguaglio
 */
public void setIm_lordo_conguaglio(java.math.BigDecimal im_lordo_conguaglio) {
	this.im_lordo_conguaglio = im_lordo_conguaglio;
}
/* 
 * Setter dell'attributo imponibile_fiscale_esterno
 */
public void setImponibile_fiscale_esterno(java.math.BigDecimal imponibile_fiscale_esterno) {
	this.imponibile_fiscale_esterno = imponibile_fiscale_esterno;
}
/* 
 * Setter dell'attributo imponibile_irpef_lordo
 */
public void setImponibile_irpef_lordo(java.math.BigDecimal imponibile_irpef_lordo) {
	this.imponibile_irpef_lordo = imponibile_irpef_lordo;
}
/* 
 * Setter dell'attributo imponibile_irpef_netto
 */
public void setImponibile_irpef_netto(java.math.BigDecimal imponibile_irpef_netto) {
	this.imponibile_irpef_netto = imponibile_irpef_netto;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo numero_giorni
 */
public void setNumero_giorni(java.lang.Integer numero_giorni) {
	this.numero_giorni = numero_giorni;
}
/* 
 * Setter dell'attributo partita_iva
 */
public void setPartita_iva(java.lang.String partita_iva) {
	this.partita_iva = partita_iva;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
/* 
 * Setter dell'attributo pg_compenso
 */
public void setPg_compenso(java.lang.Long pg_compenso) {
	this.pg_compenso = pg_compenso;
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
/* 
 * Setter dell'attributo ragione_sociale
 */
public void setRagione_sociale(java.lang.String ragione_sociale) {
	this.ragione_sociale = ragione_sociale;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_deduzione_family_dovuto() {
		return im_deduzione_family_dovuto;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_deduzione_family_goduto() {
		return im_deduzione_family_goduto;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_family_dovuto() {
		return im_family_dovuto;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_family_goduto() {
		return im_family_goduto;
	}

	/**
	 * @param decimal
	 */
	public void setIm_deduzione_family_dovuto(java.math.BigDecimal decimal) {
		im_deduzione_family_dovuto = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setIm_deduzione_family_goduto(java.math.BigDecimal decimal) {
		im_deduzione_family_goduto = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setIm_family_dovuto(java.math.BigDecimal decimal) {
		im_family_dovuto = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setIm_family_goduto(java.math.BigDecimal decimal) {
		im_family_goduto = decimal;
	}
	public java.lang.Boolean getFl_detrazioni_fam_intero_anno() {
		return fl_detrazioni_fam_intero_anno;
	}
	public void setFl_detrazioni_fam_intero_anno(
			java.lang.Boolean fl_detrazioni_fam_intero_anno) {
		this.fl_detrazioni_fam_intero_anno = fl_detrazioni_fam_intero_anno;
	}
	public java.lang.Long getNumero_giorni_esterno() {
		return numero_giorni_esterno;
	}
	public void setNumero_giorni_esterno(java.lang.Long numero_giorni_esterno) {
		this.numero_giorni_esterno = numero_giorni_esterno;
	}
	public java.lang.Long getPg_comune_addcom_esterno() {
		return pg_comune_addcom_esterno;
	}
	public void setPg_comune_addcom_esterno(java.lang.Long pg_comune_addcom_esterno) {
		this.pg_comune_addcom_esterno = pg_comune_addcom_esterno;
	}
	public java.math.BigDecimal getIm_cori_sospeso() {
		return im_cori_sospeso;
	}
	public void setIm_cori_sospeso(java.math.BigDecimal im_cori_sospeso) {
		this.im_cori_sospeso = im_cori_sospeso;
	}
	public java.lang.Boolean getFl_no_detrazioni_altre() {
		return fl_no_detrazioni_altre;
	}
	public void setFl_no_detrazioni_altre(java.lang.Boolean fl_no_detrazioni_altre) {
		this.fl_no_detrazioni_altre = fl_no_detrazioni_altre;
	}
	public java.lang.Boolean getFl_no_detrazioni_family() {
		return fl_no_detrazioni_family;
	}
	public void setFl_no_detrazioni_family(java.lang.Boolean fl_no_detrazioni_family) {
		this.fl_no_detrazioni_family = fl_no_detrazioni_family;
	}
	public java.lang.Boolean getFl_detrazioni_altri_tipi() {
		return fl_detrazioni_altri_tipi;
	}
	public void setFl_detrazioni_altri_tipi(
			java.lang.Boolean fl_detrazioni_altri_tipi) {
		this.fl_detrazioni_altri_tipi = fl_detrazioni_altri_tipi;
	}
	public java.lang.Boolean getFl_no_credito_irpef() {
		return fl_no_credito_irpef;
	}
	public void setFl_no_credito_irpef(java.lang.Boolean fl_no_credito_irpef) {
		this.fl_no_credito_irpef = fl_no_credito_irpef;
	}
	public java.math.BigDecimal getIm_credito_irpef_dovuto() {
		return im_credito_irpef_dovuto;
	}
	public void setIm_credito_irpef_dovuto(java.math.BigDecimal im_credito_irpef_dovuto) {
		this.im_credito_irpef_dovuto = im_credito_irpef_dovuto;
	}
	public java.math.BigDecimal getIm_credito_irpef_goduto() {
		return im_credito_irpef_goduto;
	}	
	public void setIm_credito_irpef_goduto(java.math.BigDecimal im_credito_irpef_goduto) {
		this.im_credito_irpef_goduto = im_credito_irpef_goduto;
	}	
}
