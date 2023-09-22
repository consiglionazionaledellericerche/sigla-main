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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.Timestamp;

public class AutofatturaBase extends AutofatturaKey implements Keyed {
	// CD_CDS_FT_PASSIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_ft_passiva;

	// CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_origine;

	// CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_sezionale;

	// CD_UO_FT_PASSIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_ft_passiva;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;

	// DATA_ESIGIBILITA_IVA TIMESTAMP NOT NULL
	private java.sql.Timestamp data_esigibilita_iva;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// FL_AUTOFATTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_autofattura;

	// FL_INTRA_UE CHAR(1) NOT NULL
	private java.lang.Boolean fl_intra_ue;

	// FL_LIQUIDAZIONE_DIFFERITA CHAR(1) NOT NULL
	private java.lang.Boolean fl_liquidazione_differita;

	// FL_SAN_MARINO_SENZA_IVA CHAR(1) NOT NULL
	private java.lang.Boolean fl_san_marino_senza_iva;

	// PG_FATTURA_PASSIVA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_fattura_passiva;

	// PROTOCOLLO_IVA DECIMAL(10,0) NOT NULL
	private java.lang.Long protocollo_iva;

	// PROTOCOLLO_IVA_GENERALE DECIMAL(10,0) NOT NULL
	private java.lang.Long protocollo_iva_generale;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;

	// TI_FATTURA CHAR(1) NOT NULL
	private java.lang.String ti_fattura;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;

	// FL_SPLIT_PAYMENT CHAR(1) NOT NULL
	private java.lang.Boolean fl_split_payment;

	// CODICE_UNIVOCO_UFFICIO_IPA VARCHAR(6)
	private java.lang.String codiceUnivocoUfficioIpa;

	// CODICE_INVIO_SDI VARCHAR(30)
	private java.lang.String codiceInvioSdi;

	// STATO_INVIO_SDI VARCHAR(3)
	private java.lang.String statoInvioSdi;

	// NOTE_INVIO_SDI VARCHAR(500)
	private java.lang.String noteInvioSdi;

	// NC_ANNULLO_SDI VARCHAR(1)
	private java.lang.String ncAnnulloSdi;

	// PROGR_UNIVOCO_ANNO DECIMAL(10,0)
	private java.lang.Long progrUnivocoAnno;

	// DT_CONSEGNA_SDI TIMESTAMP
	private java.sql.Timestamp dtConsegnaSdi;

	private java.sql.Timestamp dtRicezioneSdi;

	// NOME_FILE_INVIO_SDI CHAR(100) NOT NULL
	private java.lang.String nomeFileInvioSdi;

	// CODICE_DESTINATARIO_FATT VARCHAR(7)
	private java.lang.String codiceDestinatarioFatt;

	private java.lang.String mailFatturaElettronica;

	private java.lang.String pecFatturaElettronica;

	private java.lang.Boolean flFatturaElettronica;

	private java.lang.Boolean flTerzoEnte;

	public AutofatturaBase() {
	super();
}

	public AutofatturaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_autofattura) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_autofattura);
	}
	/*
	 * Getter dell'attributo cd_cds_ft_passiva
	 */
	public java.lang.String getCd_cds_ft_passiva() {
		return cd_cds_ft_passiva;
	}
	/*
	 * Getter dell'attributo cd_cds_origine
	 */
	public java.lang.String getCd_cds_origine() {
		return cd_cds_origine;
	}
	/*
	 * Getter dell'attributo cd_tipo_sezionale
	 */
	public java.lang.String getCd_tipo_sezionale() {
		return cd_tipo_sezionale;
	}
	/*
	 * Getter dell'attributo cd_uo_ft_passiva
	 */
	public java.lang.String getCd_uo_ft_passiva() {
		return cd_uo_ft_passiva;
	}
	/*
	 * Getter dell'attributo cd_uo_origine
	 */
	public java.lang.String getCd_uo_origine() {
		return cd_uo_origine;
	}
	/*
	 * Getter dell'attributo data_esigibilita_iva
	 */
	public java.sql.Timestamp getData_esigibilita_iva() {
		return data_esigibilita_iva;
	}
	/*
	 * Getter dell'attributo dt_registrazione
	 */
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	/*
	 * Getter dell'attributo fl_autofattura
	 */
	public java.lang.Boolean getFl_autofattura() {
		return fl_autofattura;
	}
	/*
	 * Getter dell'attributo fl_intra_ue
	 */
	public java.lang.Boolean getFl_intra_ue() {
		return fl_intra_ue;
	}
	/*
	 * Getter dell'attributo fl_liquidazione_differita
	 */
	public java.lang.Boolean getFl_liquidazione_differita() {
		return fl_liquidazione_differita;
	}
	/*
	 * Getter dell'attributo fl_san_marino_senza_iva
	 */
	public java.lang.Boolean getFl_san_marino_senza_iva() {
		return fl_san_marino_senza_iva;
	}
	/*
	 * Getter dell'attributo pg_fattura_passiva
	 */
	public java.lang.Long getPg_fattura_passiva() {
		return pg_fattura_passiva;
	}
	/*
	 * Getter dell'attributo protocollo_iva
	 */
	public java.lang.Long getProtocollo_iva() {
		return protocollo_iva;
	}
	/*
	 * Getter dell'attributo protocollo_iva_generale
	 */
	public java.lang.Long getProtocollo_iva_generale() {
		return protocollo_iva_generale;
	}
	/*
	 * Getter dell'attributo stato_cofi
	 */
	public java.lang.String getStato_cofi() {
		return stato_cofi;
	}
	/*
	 * Getter dell'attributo stato_coge
	 */
	public java.lang.String getStato_coge() {
		return stato_coge;
	}
	/*
	 * Getter dell'attributo ti_fattura
	 */
	public java.lang.String getTi_fattura() {
		return ti_fattura;
	}
	/*
	 * Getter dell'attributo ti_istituz_commerc
	 */
	public java.lang.String getTi_istituz_commerc() {
		return ti_istituz_commerc;
	}
	/*
	 * Setter dell'attributo cd_cds_ft_passiva
	 */
	public void setCd_cds_ft_passiva(java.lang.String cd_cds_ft_passiva) {
		this.cd_cds_ft_passiva = cd_cds_ft_passiva;
	}
	/*
	 * Setter dell'attributo cd_cds_origine
	 */
	public void setCd_cds_origine(java.lang.String cd_cds_origine) {
		this.cd_cds_origine = cd_cds_origine;
	}
	/*
	 * Setter dell'attributo cd_tipo_sezionale
	 */
	public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
		this.cd_tipo_sezionale = cd_tipo_sezionale;
	}
	/*
	 * Setter dell'attributo cd_uo_ft_passiva
	 */
	public void setCd_uo_ft_passiva(java.lang.String cd_uo_ft_passiva) {
		this.cd_uo_ft_passiva = cd_uo_ft_passiva;
	}
	/*
	 * Setter dell'attributo cd_uo_origine
	 */
	public void setCd_uo_origine(java.lang.String cd_uo_origine) {
		this.cd_uo_origine = cd_uo_origine;
	}
	/*
	 * Setter dell'attributo data_esigibilita_iva
	 */
	public void setData_esigibilita_iva(java.sql.Timestamp data_esigibilita_iva) {
		this.data_esigibilita_iva = data_esigibilita_iva;
	}
	/*
	 * Setter dell'attributo dt_registrazione
	 */
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
		this.dt_registrazione = dt_registrazione;
	}
	/*
	 * Setter dell'attributo fl_autofattura
	 */
	public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
		this.fl_autofattura = fl_autofattura;
	}
	/*
	 * Setter dell'attributo fl_intra_ue
	 */
	public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
		this.fl_intra_ue = fl_intra_ue;
	}
	/*
	 * Setter dell'attributo fl_liquidazione_differita
	 */
	public void setFl_liquidazione_differita(java.lang.Boolean fl_liquidazione_differita) {
		this.fl_liquidazione_differita = fl_liquidazione_differita;
	}
	/*
	 * Setter dell'attributo fl_san_marino_senza_iva
	 */
	public void setFl_san_marino_senza_iva(java.lang.Boolean fl_san_marino_senza_iva) {
		this.fl_san_marino_senza_iva = fl_san_marino_senza_iva;
	}
	/*
	 * Setter dell'attributo pg_fattura_passiva
	 */
	public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
		this.pg_fattura_passiva = pg_fattura_passiva;
	}
	/*
	 * Setter dell'attributo protocollo_iva
	 */
	public void setProtocollo_iva(java.lang.Long protocollo_iva) {
		this.protocollo_iva = protocollo_iva;
	}
	/*
	 * Setter dell'attributo protocollo_iva_generale
	 */
	public void setProtocollo_iva_generale(java.lang.Long protocollo_iva_generale) {
		this.protocollo_iva_generale = protocollo_iva_generale;
	}
	/*
	 * Setter dell'attributo stato_cofi
	 */
	public void setStato_cofi(java.lang.String stato_cofi) {
		this.stato_cofi = stato_cofi;
	}
	/*
	 * Setter dell'attributo stato_coge
	 */
	public void setStato_coge(java.lang.String stato_coge) {
		this.stato_coge = stato_coge;
	}
	/*
	 * Setter dell'attributo ti_fattura
	 */
	public void setTi_fattura(java.lang.String ti_fattura) {
		this.ti_fattura = ti_fattura;
	}
	/*
	 * Setter dell'attributo ti_istituz_commerc
	 */
	public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
		this.ti_istituz_commerc = ti_istituz_commerc;
	}

	public java.lang.Boolean getFl_split_payment() {
		return fl_split_payment;
	}
	public void setFl_split_payment(java.lang.Boolean fl_split_payment) {
		this.fl_split_payment = fl_split_payment;
	}

	public String getCodiceUnivocoUfficioIpa() {
		return codiceUnivocoUfficioIpa;
	}

	public void setCodiceUnivocoUfficioIpa(String codiceUnivocoUfficioIpa) {
		this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
	}

	public String getCodiceInvioSdi() {
		return codiceInvioSdi;
	}

	public void setCodiceInvioSdi(String codiceInvioSdi) {
		this.codiceInvioSdi = codiceInvioSdi;
	}

	public String getStatoInvioSdi() {
		return statoInvioSdi;
	}

	public void setStatoInvioSdi(String statoInvioSdi) {
		this.statoInvioSdi = statoInvioSdi;
	}

	public String getNoteInvioSdi() {
		return noteInvioSdi;
	}

	public void setNoteInvioSdi(String noteInvioSdi) {
		this.noteInvioSdi = noteInvioSdi;
	}

	public String getNcAnnulloSdi() {
		return ncAnnulloSdi;
	}

	public void setNcAnnulloSdi(String ncAnnulloSdi) {
		this.ncAnnulloSdi = ncAnnulloSdi;
	}

	public Long getProgrUnivocoAnno() {
		return progrUnivocoAnno;
	}

	public void setProgrUnivocoAnno(Long progrUnivocoAnno) {
		this.progrUnivocoAnno = progrUnivocoAnno;
	}

	public Timestamp getDtConsegnaSdi() {
		return dtConsegnaSdi;
	}

	public void setDtConsegnaSdi(Timestamp dtConsegnaSdi) {
		this.dtConsegnaSdi = dtConsegnaSdi;
	}

	public Timestamp getDtRicezioneSdi() {
		return dtRicezioneSdi;
	}

	public void setDtRicezioneSdi(Timestamp dtRicezioneSdi) {
		this.dtRicezioneSdi = dtRicezioneSdi;
	}

	public String getNomeFileInvioSdi() {
		return nomeFileInvioSdi;
	}

	public void setNomeFileInvioSdi(String nomeFileInvioSdi) {
		this.nomeFileInvioSdi = nomeFileInvioSdi;
	}

	public String getCodiceDestinatarioFatt() {
		return codiceDestinatarioFatt;
	}

	public void setCodiceDestinatarioFatt(String codiceDestinatarioFatt) {
		this.codiceDestinatarioFatt = codiceDestinatarioFatt;
	}

	public String getMailFatturaElettronica() {
		return mailFatturaElettronica;
	}

	public void setMailFatturaElettronica(String mailFatturaElettronica) {
		this.mailFatturaElettronica = mailFatturaElettronica;
	}

	public String getPecFatturaElettronica() {
		return pecFatturaElettronica;
	}

	public void setPecFatturaElettronica(String pecFatturaElettronica) {
		this.pecFatturaElettronica = pecFatturaElettronica;
	}

	public Boolean getFlFatturaElettronica() {
		return flFatturaElettronica;
	}

	public void setFlFatturaElettronica(Boolean flFatturaElettronica) {
		this.flFatturaElettronica = flFatturaElettronica;
	}

	public Boolean getFlTerzoEnte() {
		return flTerzoEnte;
	}

	public void setFlTerzoEnte(Boolean flTerzoEnte) {
		this.flTerzoEnte = flTerzoEnte;
	}
}
