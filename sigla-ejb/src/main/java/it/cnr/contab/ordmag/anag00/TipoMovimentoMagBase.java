/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoMovimentoMagBase extends TipoMovimentoMagKey implements Keyed {
//    DS_TIPO_MOVIMENTO VARCHAR(100) NOT NULL
	private String dsTipoMovimento;
 
//    TIPO VARCHAR(2) NOT NULL
	private String tipo;
 
//    SEGNO CHAR(1)
	private String segno;
 
//    CD_CDS_STORNO VARCHAR(30)
	private String cdCdsStorno;
 
//    CD_TIPO_MOVIMENTO_STORNO VARCHAR(3)
	private String cdTipoMovimentoStorno;
 
//    CD_CDS_ALT VARCHAR(30)
	private String cdCdsAlt;
 
//    CD_TIPO_MOVIMENTO_ALT VARCHAR(3)
	private String cdTipoMovimentoAlt;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;

//    RIPORTA_LOTTO_FORNITORE VARCHAR(1)
	private Boolean riportaLottoFornitore;

//    AGG_DATA_ULTIMO_CARICO VARCHAR(1)
	private Boolean aggDataUltimoCarico;

//    MOD_AGG_QTA_MAGAZZINO VARCHAR(1)
	private String modAggQtaMagazzino;

//    MOD_AGG_QTA_VAL_MAGAZZINO VARCHAR(1)
	private String modAggQtaValMagazzino;

//    MOD_AGG_VALORE_LOTTO VARCHAR(1)
	private String  modAggValoreLotto;

//    MOD_AGG_QTA_INIZIO_ANNO VARCHAR(1)
	private String modAggQtaInizioAnno;

//    FL_MOVIMENTA_LOTTI_BLOCCATI VARCHAR(1)
	private Boolean flMovimentaLottiBloccati;

//    CD_CDS_RIF VARCHAR(30) NOT NULL
	private String cdCdsRif;

//    CD_TIPO_MOVIMENTO_RIF VARCHAR(3)
	private String cdTipoMovimentoRif;

//    GENERA_BOLLA_SCARICO VARCHAR(1)
	private Boolean generaBollaScarico;

//    GENERA_BOLLA_SCARICO VARCHAR(1)
	private Boolean fl_consumo;

	//    QTA_CARICO_LOTTO VARCHAR(1)
	private java.lang.String qtaCaricoLotto;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG
	 **/
	public TipoMovimentoMagBase() {
		super();
	}
	public TipoMovimentoMagBase(String cdCds, String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoMovimento]
	 **/
	public String getDsTipoMovimento() {
		return dsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoMovimento]
	 **/
	public void setDsTipoMovimento(String dsTipoMovimento)  {
		this.dsTipoMovimento=dsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [segno]
	 **/
	public String getSegno() {
		return segno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [segno]
	 **/
	public void setSegno(String segno)  {
		this.segno=segno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsStorno]
	 **/
	public String getCdCdsStorno() {
		return cdCdsStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsStorno]
	 **/
	public void setCdCdsStorno(String cdCdsStorno)  {
		this.cdCdsStorno=cdCdsStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoStorno]
	 **/
	public String getCdTipoMovimentoStorno() {
		return cdTipoMovimentoStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoStorno]
	 **/
	public void setCdTipoMovimentoStorno(String cdTipoMovimentoStorno)  {
		this.cdTipoMovimentoStorno=cdTipoMovimentoStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsAlt]
	 **/
	public String getCdCdsAlt() {
		return cdCdsAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsAlt]
	 **/
	public void setCdCdsAlt(String cdCdsAlt)  {
		this.cdCdsAlt=cdCdsAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoAlt]
	 **/
	public String getCdTipoMovimentoAlt() {
		return cdTipoMovimentoAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoAlt]
	 **/
	public void setCdTipoMovimentoAlt(String cdTipoMovimentoAlt)  {
		this.cdTipoMovimentoAlt=cdTipoMovimentoAlt;
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

	public Boolean getRiportaLottoFornitore() {
		return riportaLottoFornitore;
	}

	public void setRiportaLottoFornitore(Boolean riportaLottoFornitore) {
		this.riportaLottoFornitore = riportaLottoFornitore;
	}

	public void setAggDataUltimoCarico(Boolean aggDataUltimoCarico) {
		this.aggDataUltimoCarico = aggDataUltimoCarico;
	}

	public Boolean getAggDataUltimoCarico() {
		return aggDataUltimoCarico;
	}

	public String getModAggQtaMagazzino() {
		return modAggQtaMagazzino;
	}

	public void setModAggQtaMagazzino(String modAggQtaMagazzino) {
		this.modAggQtaMagazzino = modAggQtaMagazzino;
	}

	public String getModAggQtaValMagazzino() {
		return modAggQtaValMagazzino;
	}

	public void setModAggQtaValMagazzino(String modAggQtaValMagazzino) {
		this.modAggQtaValMagazzino = modAggQtaValMagazzino;
	}

	public String getModAggValoreLotto() {
		return modAggValoreLotto;
	}

	public void setModAggValoreLotto(String modAggValoreLotto) {
		this.modAggValoreLotto = modAggValoreLotto;
	}

	public String getModAggQtaInizioAnno() {
		return modAggQtaInizioAnno;
	}

	public void setModAggQtaInizioAnno(String modAggQtaInizioAnno) {
		this.modAggQtaInizioAnno = modAggQtaInizioAnno;
	}

	public Boolean getFlMovimentaLottiBloccati() {
		return flMovimentaLottiBloccati;
	}

	public void setFlMovimentaLottiBloccati(Boolean flMovimentaLottiBloccati) {
		this.flMovimentaLottiBloccati = flMovimentaLottiBloccati;
	}

	public String getCdCdsRif() {
		return cdCdsRif;
	}

	public void setCdCdsRif(String cdCdsRif) {
		this.cdCdsRif = cdCdsRif;
	}

	public String getCdTipoMovimentoRif() {
		return cdTipoMovimentoRif;
	}

	public void setCdTipoMovimentoRif(String cdTipoMovimentoRif) {
		this.cdTipoMovimentoRif = cdTipoMovimentoRif;
	}

	public Boolean getGeneraBollaScarico() {
		return generaBollaScarico;
	}

	public void setGeneraBollaScarico(Boolean generaBollaScarico) {
		this.generaBollaScarico = generaBollaScarico;
	}

	public Boolean getFl_consumo() {
		return fl_consumo;
	}

	public void setFl_consumo(Boolean fl_consumo) {
		this.fl_consumo = fl_consumo;
	}

	public String getQtaCaricoLotto() {
		return qtaCaricoLotto;
	}

	public void setQtaCaricoLotto(String qtaCaricoLotto) {
		this.qtaCaricoLotto = qtaCaricoLotto;
	}
}