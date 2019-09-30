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
 * Date 28/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoMovimentoMagAzBase extends TipoMovimentoMagAzKey implements Keyed {
//    AGG_DATA_ULTIMO_CONSUMO VARCHAR(1)
	private java.lang.String aggDataUltimoConsumo;
 
//    AGG_TIPMOV_ULTIMO_CONSUMO VARCHAR(1)
	private java.lang.String aggTipmovUltimoConsumo;
 
//    AGG_DATA_ULTIMO_CARICO VARCHAR(1)
	private java.lang.String aggDataUltimoCarico;
 
//    AGG_TIPMOV_ULTIMO_CARICO VARCHAR(1)
	private java.lang.String aggTipmovUltimoCarico;
 
//    AGG_ULTIMO_FORNITORE VARCHAR(1)
	private java.lang.String aggUltimoFornitore;
 
//    MOD_AGG_PROGR_VAL_CARICHI VARCHAR(1)
	private java.lang.String modAggProgrValCarichi;
 
//    AGG_DATA_ULTIMO_CARICO_VAL VARCHAR(1)
	private java.lang.String aggDataUltimoCaricoVal;
 
//    MOD_AGG_QTA_VAL_MAGAZZINO VARCHAR(1)
	private java.lang.String modAggQtaValMagazzino;
 
//    MOD_AGG_PROGR_QTA_CARICHI VARCHAR(1)
	private java.lang.String modAggProgrQtaCarichi;
 
//    AGG_DATA_ULTIMO_CARICO_QTA VARCHAR(1)
	private java.lang.String aggDataUltimoCaricoQta;
 
//    MOD_AGG_PROGR_VAL_SCARICHI VARCHAR(1)
	private java.lang.String modAggProgrValScarichi;
 
//    MOD_AGG_PROGR_QTA_SCARICHI VARCHAR(1)
	private java.lang.String modAggProgrQtaScarichi;
 
//    MOD_AGG_PROGR_CONSUMI VARCHAR(1)
	private java.lang.String modAggProgrConsumi;
 
//    AGG_DATA_ULTIMO_SCARICO_VAL VARCHAR(1)
	private java.lang.String aggDataUltimoScaricoVal;
 
//    AGG_DATA_ULTIMO_SCARICO_QTA VARCHAR(1)
	private java.lang.String aggDataUltimoScaricoQta;
 
//    MOD_AGG_QTA_INIZIO_ANNO VARCHAR(1)
	private java.lang.String modAggQtaInizioAnno;
 
//    MOD_AGG_VALORE_INIZIO_ANNO VARCHAR(1)
	private java.lang.String modAggValoreInizioAnno;
 
//    MOD_AGG_TEMPO_MEDIO_APPROV VARCHAR(1)
	private java.lang.String modAggTempoMedioApprov;
 
//    MOD_AGG_SCORTA_MIN VARCHAR(1)
	private java.lang.String modAggScortaMin;
 
//    MOD_AGG_QTA_ORDINE VARCHAR(1)
	private java.lang.String modAggQtaOrdine;
 
//    MOD_AGG_QTA_MAGAZZINO VARCHAR(1)
	private java.lang.String modAggQtaMagazzino;
 
//    RIPORTA_LOTTO_FORNITORE VARCHAR(1)
	private java.lang.String riportaLottoFornitore;
 
//    QTA_INIZIALE_LOTTO VARCHAR(1)
	private java.lang.String qtaInizialeLotto;
 
//    VALORE_INIZIALE_LOTTO VARCHAR(1)
	private java.lang.String valoreInizialeLotto;
 
//    RIPORTO_DATA_CARICO_LOTTO VARCHAR(1)
	private java.lang.String riportoDataCaricoLotto;
 
//    CD_CDS_RIF VARCHAR(30) NOT NULL
	private java.lang.String cdCdsRif;
 
//    CD_TIPO_MOVIMENTO_RIF VARCHAR(3)
	private java.lang.String cdTipoMovimentoRif;
 
//    FLAG_SPESE VARCHAR(1)
	private java.lang.String flagSpese;
 
//    QTA_CARICO_LOTTO VARCHAR(1)
	private java.lang.String qtaCaricoLotto;
 
//    MOD_AGG_VALORE_LOTTO VARCHAR(1)
	private java.lang.String modAggValoreLotto;
 
//    FL_MOVIMENTA_LOTTI_BLOCCATI VARCHAR(1)
	private java.lang.Boolean flMovimentaLottiBloccati;
 
//    MOD_AGG_QTA_PROP_SCA VARCHAR(1)
	private java.lang.String modAggQtaPropSca;
 
//    GENERA_BOLLA_SCARICO VARCHAR(1)
	private java.lang.String generaBollaScarico;
 
//    VALIDO VARCHAR(1) NOT NULL
	private java.lang.String valido;
 
//    VALORIZZAZIONE_COSTO_EFFETTIVO VARCHAR(1)
	private java.lang.String valorizzazioneCostoEffettivo;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG_AZ
	 **/
	public TipoMovimentoMagAzBase() {
		super();
	}
	public TipoMovimentoMagAzBase(java.lang.String cdCds, java.lang.String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoConsumo]
	 **/
	public java.lang.String getAggDataUltimoConsumo() {
		return aggDataUltimoConsumo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoConsumo]
	 **/
	public void setAggDataUltimoConsumo(java.lang.String aggDataUltimoConsumo)  {
		this.aggDataUltimoConsumo=aggDataUltimoConsumo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggTipmovUltimoConsumo]
	 **/
	public java.lang.String getAggTipmovUltimoConsumo() {
		return aggTipmovUltimoConsumo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggTipmovUltimoConsumo]
	 **/
	public void setAggTipmovUltimoConsumo(java.lang.String aggTipmovUltimoConsumo)  {
		this.aggTipmovUltimoConsumo=aggTipmovUltimoConsumo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoCarico]
	 **/
	public java.lang.String getAggDataUltimoCarico() {
		return aggDataUltimoCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoCarico]
	 **/
	public void setAggDataUltimoCarico(java.lang.String aggDataUltimoCarico)  {
		this.aggDataUltimoCarico=aggDataUltimoCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggTipmovUltimoCarico]
	 **/
	public java.lang.String getAggTipmovUltimoCarico() {
		return aggTipmovUltimoCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggTipmovUltimoCarico]
	 **/
	public void setAggTipmovUltimoCarico(java.lang.String aggTipmovUltimoCarico)  {
		this.aggTipmovUltimoCarico=aggTipmovUltimoCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggUltimoFornitore]
	 **/
	public java.lang.String getAggUltimoFornitore() {
		return aggUltimoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggUltimoFornitore]
	 **/
	public void setAggUltimoFornitore(java.lang.String aggUltimoFornitore)  {
		this.aggUltimoFornitore=aggUltimoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggProgrValCarichi]
	 **/
	public java.lang.String getModAggProgrValCarichi() {
		return modAggProgrValCarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggProgrValCarichi]
	 **/
	public void setModAggProgrValCarichi(java.lang.String modAggProgrValCarichi)  {
		this.modAggProgrValCarichi=modAggProgrValCarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoCaricoVal]
	 **/
	public java.lang.String getAggDataUltimoCaricoVal() {
		return aggDataUltimoCaricoVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoCaricoVal]
	 **/
	public void setAggDataUltimoCaricoVal(java.lang.String aggDataUltimoCaricoVal)  {
		this.aggDataUltimoCaricoVal=aggDataUltimoCaricoVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggQtaValMagazzino]
	 **/
	public java.lang.String getModAggQtaValMagazzino() {
		return modAggQtaValMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggQtaValMagazzino]
	 **/
	public void setModAggQtaValMagazzino(java.lang.String modAggQtaValMagazzino)  {
		this.modAggQtaValMagazzino=modAggQtaValMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggProgrQtaCarichi]
	 **/
	public java.lang.String getModAggProgrQtaCarichi() {
		return modAggProgrQtaCarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggProgrQtaCarichi]
	 **/
	public void setModAggProgrQtaCarichi(java.lang.String modAggProgrQtaCarichi)  {
		this.modAggProgrQtaCarichi=modAggProgrQtaCarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoCaricoQta]
	 **/
	public java.lang.String getAggDataUltimoCaricoQta() {
		return aggDataUltimoCaricoQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoCaricoQta]
	 **/
	public void setAggDataUltimoCaricoQta(java.lang.String aggDataUltimoCaricoQta)  {
		this.aggDataUltimoCaricoQta=aggDataUltimoCaricoQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggProgrValScarichi]
	 **/
	public java.lang.String getModAggProgrValScarichi() {
		return modAggProgrValScarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggProgrValScarichi]
	 **/
	public void setModAggProgrValScarichi(java.lang.String modAggProgrValScarichi)  {
		this.modAggProgrValScarichi=modAggProgrValScarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggProgrQtaScarichi]
	 **/
	public java.lang.String getModAggProgrQtaScarichi() {
		return modAggProgrQtaScarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggProgrQtaScarichi]
	 **/
	public void setModAggProgrQtaScarichi(java.lang.String modAggProgrQtaScarichi)  {
		this.modAggProgrQtaScarichi=modAggProgrQtaScarichi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggProgrConsumi]
	 **/
	public java.lang.String getModAggProgrConsumi() {
		return modAggProgrConsumi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggProgrConsumi]
	 **/
	public void setModAggProgrConsumi(java.lang.String modAggProgrConsumi)  {
		this.modAggProgrConsumi=modAggProgrConsumi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoScaricoVal]
	 **/
	public java.lang.String getAggDataUltimoScaricoVal() {
		return aggDataUltimoScaricoVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoScaricoVal]
	 **/
	public void setAggDataUltimoScaricoVal(java.lang.String aggDataUltimoScaricoVal)  {
		this.aggDataUltimoScaricoVal=aggDataUltimoScaricoVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggDataUltimoScaricoQta]
	 **/
	public java.lang.String getAggDataUltimoScaricoQta() {
		return aggDataUltimoScaricoQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggDataUltimoScaricoQta]
	 **/
	public void setAggDataUltimoScaricoQta(java.lang.String aggDataUltimoScaricoQta)  {
		this.aggDataUltimoScaricoQta=aggDataUltimoScaricoQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggQtaInizioAnno]
	 **/
	public java.lang.String getModAggQtaInizioAnno() {
		return modAggQtaInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggQtaInizioAnno]
	 **/
	public void setModAggQtaInizioAnno(java.lang.String modAggQtaInizioAnno)  {
		this.modAggQtaInizioAnno=modAggQtaInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggValoreInizioAnno]
	 **/
	public java.lang.String getModAggValoreInizioAnno() {
		return modAggValoreInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggValoreInizioAnno]
	 **/
	public void setModAggValoreInizioAnno(java.lang.String modAggValoreInizioAnno)  {
		this.modAggValoreInizioAnno=modAggValoreInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggTempoMedioApprov]
	 **/
	public java.lang.String getModAggTempoMedioApprov() {
		return modAggTempoMedioApprov;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggTempoMedioApprov]
	 **/
	public void setModAggTempoMedioApprov(java.lang.String modAggTempoMedioApprov)  {
		this.modAggTempoMedioApprov=modAggTempoMedioApprov;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggScortaMin]
	 **/
	public java.lang.String getModAggScortaMin() {
		return modAggScortaMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggScortaMin]
	 **/
	public void setModAggScortaMin(java.lang.String modAggScortaMin)  {
		this.modAggScortaMin=modAggScortaMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggQtaOrdine]
	 **/
	public java.lang.String getModAggQtaOrdine() {
		return modAggQtaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggQtaOrdine]
	 **/
	public void setModAggQtaOrdine(java.lang.String modAggQtaOrdine)  {
		this.modAggQtaOrdine=modAggQtaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggQtaMagazzino]
	 **/
	public java.lang.String getModAggQtaMagazzino() {
		return modAggQtaMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggQtaMagazzino]
	 **/
	public void setModAggQtaMagazzino(java.lang.String modAggQtaMagazzino)  {
		this.modAggQtaMagazzino=modAggQtaMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riportaLottoFornitore]
	 **/
	public java.lang.String getRiportaLottoFornitore() {
		return riportaLottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riportaLottoFornitore]
	 **/
	public void setRiportaLottoFornitore(java.lang.String riportaLottoFornitore)  {
		this.riportaLottoFornitore=riportaLottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtaInizialeLotto]
	 **/
	public java.lang.String getQtaInizialeLotto() {
		return qtaInizialeLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtaInizialeLotto]
	 **/
	public void setQtaInizialeLotto(java.lang.String qtaInizialeLotto)  {
		this.qtaInizialeLotto=qtaInizialeLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valoreInizialeLotto]
	 **/
	public java.lang.String getValoreInizialeLotto() {
		return valoreInizialeLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valoreInizialeLotto]
	 **/
	public void setValoreInizialeLotto(java.lang.String valoreInizialeLotto)  {
		this.valoreInizialeLotto=valoreInizialeLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riportoDataCaricoLotto]
	 **/
	public java.lang.String getRiportoDataCaricoLotto() {
		return riportoDataCaricoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riportoDataCaricoLotto]
	 **/
	public void setRiportoDataCaricoLotto(java.lang.String riportoDataCaricoLotto)  {
		this.riportoDataCaricoLotto=riportoDataCaricoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRif]
	 **/
	public java.lang.String getCdCdsRif() {
		return cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRif]
	 **/
	public void setCdCdsRif(java.lang.String cdCdsRif)  {
		this.cdCdsRif=cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRif]
	 **/
	public java.lang.String getCdTipoMovimentoRif() {
		return cdTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRif]
	 **/
	public void setCdTipoMovimentoRif(java.lang.String cdTipoMovimentoRif)  {
		this.cdTipoMovimentoRif=cdTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagSpese]
	 **/
	public java.lang.String getFlagSpese() {
		return flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagSpese]
	 **/
	public void setFlagSpese(java.lang.String flagSpese)  {
		this.flagSpese=flagSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtaCaricoLotto]
	 **/
	public java.lang.String getQtaCaricoLotto() {
		return qtaCaricoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtaCaricoLotto]
	 **/
	public void setQtaCaricoLotto(java.lang.String qtaCaricoLotto)  {
		this.qtaCaricoLotto=qtaCaricoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggValoreLotto]
	 **/
	public java.lang.String getModAggValoreLotto() {
		return modAggValoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggValoreLotto]
	 **/
	public void setModAggValoreLotto(java.lang.String modAggValoreLotto)  {
		this.modAggValoreLotto=modAggValoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [movimentaLottiBloccati]
	 **/
	public java.lang.Boolean getFlMovimentaLottiBloccati() {
		return flMovimentaLottiBloccati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [movimentaLottiBloccati]
	 **/
	public void setFlMovimentaLottiBloccati(java.lang.Boolean flMovimentaLottiBloccati)  {
		this.flMovimentaLottiBloccati=flMovimentaLottiBloccati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [modAggQtaPropSca]
	 **/
	public java.lang.String getModAggQtaPropSca() {
		return modAggQtaPropSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [modAggQtaPropSca]
	 **/
	public void setModAggQtaPropSca(java.lang.String modAggQtaPropSca)  {
		this.modAggQtaPropSca=modAggQtaPropSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [generaBollaScarico]
	 **/
	public java.lang.String getGeneraBollaScarico() {
		return generaBollaScarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [generaBollaScarico]
	 **/
	public void setGeneraBollaScarico(java.lang.String generaBollaScarico)  {
		this.generaBollaScarico=generaBollaScarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valido]
	 **/
	public java.lang.String getValido() {
		return valido;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valido]
	 **/
	public void setValido(java.lang.String valido)  {
		this.valido=valido;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valorizzazioneCostoEffettivo]
	 **/
	public java.lang.String getValorizzazioneCostoEffettivo() {
		return valorizzazioneCostoEffettivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valorizzazioneCostoEffettivo]
	 **/
	public void setValorizzazioneCostoEffettivo(java.lang.String valorizzazioneCostoEffettivo)  {
		this.valorizzazioneCostoEffettivo=valorizzazioneCostoEffettivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}