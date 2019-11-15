/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class TipoMovimentoMagAzBulk extends TipoMovimentoMagAzBase {
	public final static String AZIONE_AZZERA = "0";
	public final static String AZIONE_SOSTITUISCE = "S";
	public final static String AZIONE_SOTTRAE = "-";
	public final static String AZIONE_SOMMA = "+";
	
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	/**
	 * [TIPO_MOVIMENTO_MAG Anagrafica delle Tipologie dei Movimenti.]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMagRif =  new TipoMovimentoMagBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG_AZ
	 **/
	public TipoMovimentoMagAzBulk() {
		super();
	}
	
	
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
		// TODO Auto-generated method stub
		super.initializeForInsert(crudbp, actioncontext);
		
		this.setAggTipmovUltimoConsumo("N");
		this.setAggTipmovUltimoConsumo("N");
		this.setAggDataUltimoCarico("N");
		this.setAggTipmovUltimoCarico("N");
		this.setAggUltimoFornitore("N");
		this.setModAggProgrValCarichi("+");
		this.setAggDataUltimoCaricoVal("N");
		this.setModAggQtaValMagazzino("0");
		this.setModAggProgrQtaCarichi("-");
		this.setAggDataUltimoCaricoVal("N");
		this.setModAggProgrValScarichi("+");
		this.setModAggProgrQtaScarichi("-");
		this.setModAggProgrConsumi("0");
		this.setAggDataUltimoScaricoVal("N");
		this.setAggDataUltimoScaricoQta("N");
		this.setModAggQtaInizioAnno("N");
		this.setModAggValoreInizioAnno("N");
		this.setModAggTempoMedioApprov("+");
		this.setModAggScortaMin("+");
		this.setModAggQtaOrdine("0");
		this.setModAggQtaMagazzino("+");
		this.setRiportaLottoFornitore("N");
		this.setQtaInizialeLotto("-");
		this.setValoreInizialeLotto("+");
		this.setRiportoDataCaricoLotto("N");
		this.setCdCdsRif(null);
		this.setCdTipoMovimentoRif(null);
		this.setFlagSpese("N");
		this.setQtaCaricoLotto("+");
		this.setModAggValoreLotto("+");
		this.setFlMovimentaLottiBloccati(false);
		this.setGeneraBollaScarico("N");
		this.setValido("N");
		this.setModAggQtaPropSca("-");
		this.setValorizzazioneCostoEffettivo("N");		
		return this;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG_AZ
	 **/
	public TipoMovimentoMagAzBulk(java.lang.String cdCds, java.lang.String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
		TipoMovimentoMagBulk tipoMovimento = new TipoMovimentoMagBulk();
		tipoMovimento.setCdCds(cdCds);
		tipoMovimento.setCdTipoMovimento(cdTipoMovimento);
		setTipoMovimentoMag(tipoMovimento);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public TipoMovimentoMagBulk getTipoMovimentoMagRif() {
		return tipoMovimentoMagRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public void setTipoMovimentoMagRif(TipoMovimentoMagBulk tipoMovimentoMagRif)  {
		this.tipoMovimentoMagRif=tipoMovimentoMagRif;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMag() {
		return tipoMovimentoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public void setTipoMovimentoMag(TipoMovimentoMagBulk tipoMovimentoMag)  {
		this.tipoMovimentoMag=tipoMovimentoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getTipoMovimentoMag().setCdCds(cdCds);
	}
	public java.lang.String getCdTipoMovimento() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMag().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdTipoMovimento(java.lang.String cdTipoMovimento)  {
		this.getTipoMovimentoMag().setCdTipoMovimento(cdTipoMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRif]
	 **/
	public java.lang.String getCdCdsRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRif]
	 **/
	public void setCdCdsRif(java.lang.String cdCdsRif)  {
		this.getTipoMovimentoMagRif().setCdCds(cdCdsRif);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRif]
	 **/
	public java.lang.String getCdTipoMovimentoRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRif]
	 **/
	public void setCdTipoMovimentoRif(java.lang.String cdTipoMovimentoRif)  {
		this.getTipoMovimentoMagRif().setCdTipoMovimento(cdTipoMovimentoRif);
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}
}