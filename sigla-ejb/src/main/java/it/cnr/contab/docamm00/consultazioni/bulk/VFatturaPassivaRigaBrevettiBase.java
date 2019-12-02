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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class VFatturaPassivaRigaBrevettiBase extends VFatturaPassivaRigaBrevettiKey implements Keyed {
//    DS_FATTURA_ATTIVA VARCHAR(1000)
	private java.lang.String dsFatturaPassiva;
 
//    CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cdCdsOrigine;
 
//    CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cdUoOrigine;
 
//    TI_FATTURA CHAR(1) NOT NULL
	private java.lang.String tipoFatturaCompenso;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtRegistrazione;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cdTerzo;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragioneSociale;
 
//    CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;
 
//    CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cdDivisa;
 
//    CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cdVoceIva;
 
//    DS_VOCE_IVA VARCHAR(300) NOT NULL
	private java.lang.String dsVoceIva;
 
//    DS_RIGA_FATTURA VARCHAR(1000)
	private java.lang.String dsRigaFattura;
 
//    PG_TROVATO DECIMAL(10,0)
	private java.lang.Long pgTrovato;
 
//    IM_IMPONIBILE DECIMAL(22,0)
	private java.lang.Long imImponibile;
 
//    FC_IVA DECIMAL(22,0)
	private java.lang.Long fcIva;
 
//    IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIva;
 
//  IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imLordoPercipiente;

//  IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imNettoPercipiente;

//  IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleCompenso;

//    ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizioObbligazione;
 
//    PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//    DT_EMISSIONE_ACCERTAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtEmissioneObbligazione;
 
//    ESERCIZIO_REVERSALE DECIMAL(22,0)
	private java.lang.Long esercizioMandato;
 
//    PG_REVERSALE DECIMAL(22,0)
	private java.lang.Long pgMandato;
 
//    DT_EMISSIONE_REVERSALE TIMESTAMP(7)
	private java.sql.Timestamp dtEmissioneMandato;
 
	// NR_FATTURA_FORNITORE VARCHAR(20) NOT NULL
	private java.lang.String nr_fattura_fornitore;

	// DT_FATTURA_FORNITORE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fattura_fornitore;

	// DT_PAGAMENTO_FONDO_ECO TIMESTAMP
	private java.sql.Timestamp dt_pagamento_fondo_eco;

	// STATO_PAGAMENTO_FONDO_ECO CHAR(1) NOT NULL
	private java.lang.String stato_pagamento_fondo_eco;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATTURA_PASSIVA_RIGA_BREVETTI
	 **/
	public VFatturaPassivaRigaBrevettiBase() {
		super();
	}
	public VFatturaPassivaRigaBrevettiBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva,java.lang.Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsFatturaAttiva]
	 **/
	public java.lang.String getDsFatturaPassiva() {
		return dsFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsFatturaAttiva]
	 **/
	public void setDsFatturaPassiva(java.lang.String dsFatturaPassiva)  {
		this.dsFatturaPassiva=dsFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrigine]
	 **/
	public java.lang.String getCdCdsOrigine() {
		return cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrigine]
	 **/
	public void setCdCdsOrigine(java.lang.String cdCdsOrigine)  {
		this.cdCdsOrigine=cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoOrigine]
	 **/
	public java.lang.String getCdUoOrigine() {
		return cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoOrigine]
	 **/
	public void setCdUoOrigine(java.lang.String cdUoOrigine)  {
		this.cdUoOrigine=cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRegistrazione]
	 **/
	public java.sql.Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRegistrazione]
	 **/
	public void setDtRegistrazione(java.sql.Timestamp dtRegistrazione)  {
		this.dtRegistrazione=dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognome]
	 **/
	public java.lang.String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public java.lang.String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ragioneSociale]
	 **/
	public java.lang.String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ragioneSociale]
	 **/
	public void setRagioneSociale(java.lang.String ragioneSociale)  {
		this.ragioneSociale=ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cambio]
	 **/
	public java.math.BigDecimal getCambio() {
		return cambio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cambio]
	 **/
	public void setCambio(java.math.BigDecimal cambio)  {
		this.cambio=cambio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdDivisa]
	 **/
	public java.lang.String getCdDivisa() {
		return cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdDivisa]
	 **/
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.cdDivisa=cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public java.lang.String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(java.lang.String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsVoceIva]
	 **/
	public java.lang.String getDsVoceIva() {
		return dsVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsVoceIva]
	 **/
	public void setDsVoceIva(java.lang.String dsVoceIva)  {
		this.dsVoceIva=dsVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsRigaFattura]
	 **/
	public java.lang.String getDsRigaFattura() {
		return dsRigaFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsRigaFattura]
	 **/
	public void setDsRigaFattura(java.lang.String dsRigaFattura)  {
		this.dsRigaFattura=dsRigaFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgTrovato]
	 **/
	public java.lang.Long getPgTrovato() {
		return pgTrovato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgTrovato]
	 **/
	public void setPgTrovato(java.lang.Long pgTrovato)  {
		this.pgTrovato=pgTrovato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imImponibile]
	 **/
	public java.lang.Long getImImponibile() {
		return imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imImponibile]
	 **/
	public void setImImponibile(java.lang.Long imImponibile)  {
		this.imImponibile=imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fcIva]
	 **/
	public java.lang.Long getFcIva() {
		return fcIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fcIva]
	 **/
	public void setFcIva(java.lang.Long fcIva)  {
		this.fcIva=fcIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIva]
	 **/
	public java.math.BigDecimal getImIva() {
		return imIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIva]
	 **/
	public void setImIva(java.math.BigDecimal imIva)  {
		this.imIva=imIva;
	}
	public java.lang.Integer getEsercizioObbligazione() {
		return esercizioObbligazione;
	}
	public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione) {
		this.esercizioObbligazione = esercizioObbligazione;
	}
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	public void setPgObbligazione(java.lang.Long pgObbligazione) {
		this.pgObbligazione = pgObbligazione;
	}
	public java.sql.Timestamp getDtEmissioneObbligazione() {
		return dtEmissioneObbligazione;
	}
	public void setDtEmissioneObbligazione(
			java.sql.Timestamp dtEmissioneObbligazione) {
		this.dtEmissioneObbligazione = dtEmissioneObbligazione;
	}
	public java.lang.Long getEsercizioMandato() {
		return esercizioMandato;
	}
	public void setEsercizioMandato(java.lang.Long esercizioMandato) {
		this.esercizioMandato = esercizioMandato;
	}
	public java.lang.Long getPgMandato() {
		return pgMandato;
	}
	public void setPgMandato(java.lang.Long pgMandato) {
		this.pgMandato = pgMandato;
	}
	public java.sql.Timestamp getDtEmissioneMandato() {
		return dtEmissioneMandato;
	}
	public void setDtEmissioneMandato(java.sql.Timestamp dtEmissioneMandato) {
		this.dtEmissioneMandato = dtEmissioneMandato;
	}
	public java.lang.String getNr_fattura_fornitore() {
		return nr_fattura_fornitore;
	}
	public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore) {
		this.nr_fattura_fornitore = nr_fattura_fornitore;
	}
	public java.sql.Timestamp getDt_fattura_fornitore() {
		return dt_fattura_fornitore;
	}
	public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore) {
		this.dt_fattura_fornitore = dt_fattura_fornitore;
	}
	public java.sql.Timestamp getDt_pagamento_fondo_eco() {
		return dt_pagamento_fondo_eco;
	}
	public void setDt_pagamento_fondo_eco(java.sql.Timestamp dt_pagamento_fondo_eco) {
		this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
	}
	public java.lang.String getStato_pagamento_fondo_eco() {
		return stato_pagamento_fondo_eco;
	}
	public void setStato_pagamento_fondo_eco(
			java.lang.String stato_pagamento_fondo_eco) {
		this.stato_pagamento_fondo_eco = stato_pagamento_fondo_eco;
	}
	public java.lang.String getPartita_iva() {
		return partita_iva;
	}
	public void setPartita_iva(java.lang.String partita_iva) {
		this.partita_iva = partita_iva;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}
	public java.lang.String getTipoFatturaCompenso() {
		return tipoFatturaCompenso;
	}
	public void setTipoFatturaCompenso(java.lang.String tipoFatturaCompenso) {
		this.tipoFatturaCompenso = tipoFatturaCompenso;
	}
	public java.math.BigDecimal getImLordoPercipiente() {
		return imLordoPercipiente;
	}
	public void setImLordoPercipiente(java.math.BigDecimal imLordoPercipiente) {
		this.imLordoPercipiente = imLordoPercipiente;
	}
	public java.math.BigDecimal getImNettoPercipiente() {
		return imNettoPercipiente;
	}
	public void setImNettoPercipiente(java.math.BigDecimal imNettoPercipiente) {
		this.imNettoPercipiente = imNettoPercipiente;
	}
	public java.math.BigDecimal getImTotaleCompenso() {
		return imTotaleCompenso;
	}
	public void setImTotaleCompenso(java.math.BigDecimal imTotaleCompenso) {
		this.imTotaleCompenso = imTotaleCompenso;
	}
}