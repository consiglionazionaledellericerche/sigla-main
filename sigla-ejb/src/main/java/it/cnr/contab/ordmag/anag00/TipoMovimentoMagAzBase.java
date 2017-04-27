/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoMovimentoMagAzBase extends TipoMovimentoMagAzKey implements Keyed {
//    ULT_PREL VARCHAR(1)
	private java.lang.String ultPrel;
 
//    TIPMOV_PREL VARCHAR(1)
	private java.lang.String tipmovPrel;
 
//    ULT_ENT VARCHAR(1)
	private java.lang.String ultEnt;
 
//    TIPMOV_ENT VARCHAR(1)
	private java.lang.String tipmovEnt;
 
//    ULT_FORN VARCHAR(1)
	private java.lang.String ultForn;
 
//    PROG_VAL VARCHAR(1)
	private java.lang.String progVal;
 
//    ULT_ENT_VAL VARCHAR(1)
	private java.lang.String ultEntVal;
 
//    VALORIZ VARCHAR(1)
	private java.lang.String valoriz;
 
//    PROG_QTA VARCHAR(1)
	private java.lang.String progQta;
 
//    ULT_ENT_QTA VARCHAR(1)
	private java.lang.String ultEntQta;
 
//    PROGSCA_VAL VARCHAR(1)
	private java.lang.String progscaVal;
 
//    PROG_SCA VARCHAR(1)
	private java.lang.String progSca;
 
//    PROGCONS VARCHAR(1)
	private java.lang.String progcons;
 
//    ULT_SCA_VAL VARCHAR(1)
	private java.lang.String ultScaVal;
 
//    ULT_SCA_QTA VARCHAR(1)
	private java.lang.String ultScaQta;
 
//    QTAINZ_ANNO VARCHAR(1)
	private java.lang.String qtainzAnno;
 
//    VALINZ_ANNO VARCHAR(1)
	private java.lang.String valinzAnno;
 
//    MEDIOAPPR VARCHAR(1)
	private java.lang.String medioappr;
 
//    SCORTAMIN VARCHAR(1)
	private java.lang.String scortamin;
 
//    QTAORDI VARCHAR(1)
	private java.lang.String qtaordi;
 
//    GIAC_CAR VARCHAR(1)
	private java.lang.String giacCar;
 
//    LOTTO_FORN VARCHAR(1)
	private java.lang.String lottoForn;
 
//    AGGUBIC VARCHAR(1)
	private java.lang.String aggubic;
 
//    QTAINZ_LOTTO VARCHAR(1)
	private java.lang.String qtainzLotto;
 
//    IMPORTO_LOTTO VARCHAR(1)
	private java.lang.String importoLotto;
 
//    CARICO VARCHAR(1)
	private java.lang.String carico;
 
//    CD_CDS_RIF VARCHAR(30) NOT NULL
	private java.lang.String cdCdsRif;
 
//    CD_TIPO_MOVIMENTO_RIF VARCHAR(3)
	private java.lang.String cdTipoMovimentoRif;
 
//    FLAG_SPESE VARCHAR(1)
	private java.lang.String flagSpese;
 
//    QTACARICO VARCHAR(1)
	private java.lang.String qtacarico;
 
//    COSTO VARCHAR(1)
	private java.lang.String costo;
 
//    FLAG_BLOCCO VARCHAR(1)
	private java.lang.String flagBlocco;
 
//    QTAPROPSCA VARCHAR(1)
	private java.lang.String qtapropsca;
 
//    BOLLASCA VARCHAR(1)
	private java.lang.String bollasca;
 
//    ATTIVO VARCHAR(1) NOT NULL
	private java.lang.String attivo;
 
//    FLAG_CEFF VARCHAR(1)
	private java.lang.String flagCeff;
 
//    QTAINPROD VARCHAR(1)
	private java.lang.String qtainprod;
 
//    CARICO_INIZIALE VARCHAR(1) NOT NULL
	private java.lang.String caricoIniziale;
 
//    TIPO_DA_ESTRARRE VARCHAR(1) NOT NULL
	private java.lang.String tipoDaEstrarre;
 
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
	 * Restituisce il valore di: [ultPrel]
	 **/
	public java.lang.String getUltPrel() {
		return ultPrel;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultPrel]
	 **/
	public void setUltPrel(java.lang.String ultPrel)  {
		this.ultPrel=ultPrel;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipmovPrel]
	 **/
	public java.lang.String getTipmovPrel() {
		return tipmovPrel;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipmovPrel]
	 **/
	public void setTipmovPrel(java.lang.String tipmovPrel)  {
		this.tipmovPrel=tipmovPrel;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultEnt]
	 **/
	public java.lang.String getUltEnt() {
		return ultEnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultEnt]
	 **/
	public void setUltEnt(java.lang.String ultEnt)  {
		this.ultEnt=ultEnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipmovEnt]
	 **/
	public java.lang.String getTipmovEnt() {
		return tipmovEnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipmovEnt]
	 **/
	public void setTipmovEnt(java.lang.String tipmovEnt)  {
		this.tipmovEnt=tipmovEnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultForn]
	 **/
	public java.lang.String getUltForn() {
		return ultForn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultForn]
	 **/
	public void setUltForn(java.lang.String ultForn)  {
		this.ultForn=ultForn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progVal]
	 **/
	public java.lang.String getProgVal() {
		return progVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progVal]
	 **/
	public void setProgVal(java.lang.String progVal)  {
		this.progVal=progVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultEntVal]
	 **/
	public java.lang.String getUltEntVal() {
		return ultEntVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultEntVal]
	 **/
	public void setUltEntVal(java.lang.String ultEntVal)  {
		this.ultEntVal=ultEntVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valoriz]
	 **/
	public java.lang.String getValoriz() {
		return valoriz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valoriz]
	 **/
	public void setValoriz(java.lang.String valoriz)  {
		this.valoriz=valoriz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progQta]
	 **/
	public java.lang.String getProgQta() {
		return progQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progQta]
	 **/
	public void setProgQta(java.lang.String progQta)  {
		this.progQta=progQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultEntQta]
	 **/
	public java.lang.String getUltEntQta() {
		return ultEntQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultEntQta]
	 **/
	public void setUltEntQta(java.lang.String ultEntQta)  {
		this.ultEntQta=ultEntQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progscaVal]
	 **/
	public java.lang.String getProgscaVal() {
		return progscaVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progscaVal]
	 **/
	public void setProgscaVal(java.lang.String progscaVal)  {
		this.progscaVal=progscaVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progSca]
	 **/
	public java.lang.String getProgSca() {
		return progSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progSca]
	 **/
	public void setProgSca(java.lang.String progSca)  {
		this.progSca=progSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progcons]
	 **/
	public java.lang.String getProgcons() {
		return progcons;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progcons]
	 **/
	public void setProgcons(java.lang.String progcons)  {
		this.progcons=progcons;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultScaVal]
	 **/
	public java.lang.String getUltScaVal() {
		return ultScaVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultScaVal]
	 **/
	public void setUltScaVal(java.lang.String ultScaVal)  {
		this.ultScaVal=ultScaVal;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ultScaQta]
	 **/
	public java.lang.String getUltScaQta() {
		return ultScaQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ultScaQta]
	 **/
	public void setUltScaQta(java.lang.String ultScaQta)  {
		this.ultScaQta=ultScaQta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtainzAnno]
	 **/
	public java.lang.String getQtainzAnno() {
		return qtainzAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtainzAnno]
	 **/
	public void setQtainzAnno(java.lang.String qtainzAnno)  {
		this.qtainzAnno=qtainzAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valinzAnno]
	 **/
	public java.lang.String getValinzAnno() {
		return valinzAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valinzAnno]
	 **/
	public void setValinzAnno(java.lang.String valinzAnno)  {
		this.valinzAnno=valinzAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [medioappr]
	 **/
	public java.lang.String getMedioappr() {
		return medioappr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [medioappr]
	 **/
	public void setMedioappr(java.lang.String medioappr)  {
		this.medioappr=medioappr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [scortamin]
	 **/
	public java.lang.String getScortamin() {
		return scortamin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [scortamin]
	 **/
	public void setScortamin(java.lang.String scortamin)  {
		this.scortamin=scortamin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtaordi]
	 **/
	public java.lang.String getQtaordi() {
		return qtaordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtaordi]
	 **/
	public void setQtaordi(java.lang.String qtaordi)  {
		this.qtaordi=qtaordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giacCar]
	 **/
	public java.lang.String getGiacCar() {
		return giacCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giacCar]
	 **/
	public void setGiacCar(java.lang.String giacCar)  {
		this.giacCar=giacCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lottoForn]
	 **/
	public java.lang.String getLottoForn() {
		return lottoForn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lottoForn]
	 **/
	public void setLottoForn(java.lang.String lottoForn)  {
		this.lottoForn=lottoForn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aggubic]
	 **/
	public java.lang.String getAggubic() {
		return aggubic;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aggubic]
	 **/
	public void setAggubic(java.lang.String aggubic)  {
		this.aggubic=aggubic;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtainzLotto]
	 **/
	public java.lang.String getQtainzLotto() {
		return qtainzLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtainzLotto]
	 **/
	public void setQtainzLotto(java.lang.String qtainzLotto)  {
		this.qtainzLotto=qtainzLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLotto]
	 **/
	public java.lang.String getImportoLotto() {
		return importoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLotto]
	 **/
	public void setImportoLotto(java.lang.String importoLotto)  {
		this.importoLotto=importoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [carico]
	 **/
	public java.lang.String getCarico() {
		return carico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [carico]
	 **/
	public void setCarico(java.lang.String carico)  {
		this.carico=carico;
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
	 * Restituisce il valore di: [qtacarico]
	 **/
	public java.lang.String getQtacarico() {
		return qtacarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtacarico]
	 **/
	public void setQtacarico(java.lang.String qtacarico)  {
		this.qtacarico=qtacarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [costo]
	 **/
	public java.lang.String getCosto() {
		return costo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [costo]
	 **/
	public void setCosto(java.lang.String costo)  {
		this.costo=costo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagBlocco]
	 **/
	public java.lang.String getFlagBlocco() {
		return flagBlocco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagBlocco]
	 **/
	public void setFlagBlocco(java.lang.String flagBlocco)  {
		this.flagBlocco=flagBlocco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtapropsca]
	 **/
	public java.lang.String getQtapropsca() {
		return qtapropsca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtapropsca]
	 **/
	public void setQtapropsca(java.lang.String qtapropsca)  {
		this.qtapropsca=qtapropsca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bollasca]
	 **/
	public java.lang.String getBollasca() {
		return bollasca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bollasca]
	 **/
	public void setBollasca(java.lang.String bollasca)  {
		this.bollasca=bollasca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [attivo]
	 **/
	public java.lang.String getAttivo() {
		return attivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [attivo]
	 **/
	public void setAttivo(java.lang.String attivo)  {
		this.attivo=attivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flagCeff]
	 **/
	public java.lang.String getFlagCeff() {
		return flagCeff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flagCeff]
	 **/
	public void setFlagCeff(java.lang.String flagCeff)  {
		this.flagCeff=flagCeff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [qtainprod]
	 **/
	public java.lang.String getQtainprod() {
		return qtainprod;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [qtainprod]
	 **/
	public void setQtainprod(java.lang.String qtainprod)  {
		this.qtainprod=qtainprod;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [caricoIniziale]
	 **/
	public java.lang.String getCaricoIniziale() {
		return caricoIniziale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [caricoIniziale]
	 **/
	public void setCaricoIniziale(java.lang.String caricoIniziale)  {
		this.caricoIniziale=caricoIniziale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoDaEstrarre]
	 **/
	public java.lang.String getTipoDaEstrarre() {
		return tipoDaEstrarre;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoDaEstrarre]
	 **/
	public void setTipoDaEstrarre(java.lang.String tipoDaEstrarre)  {
		this.tipoDaEstrarre=tipoDaEstrarre;
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