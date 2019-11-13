/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
public class MovimentiMagBulk extends MovimentiMagBase {
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_ANNULLATO = "ANN";
	
	private MovimentiMagBulk movimentoRif;
	private MovimentiMagBulk movimentoAnn;
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	private OrdineAcqConsegnaBulk ordineAcqConsegnaUt =  new OrdineAcqConsegnaBulk();
	private TerzoBulk terzo =  new TerzoBulk();
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	private LottoMagBulk lottoMag =  new LottoMagBulk();
	private MagazzinoBulk magazzinoUt =  new MagazzinoBulk();
	private Bene_servizioBulk beneServizioUt =  new Bene_servizioBulk();
	private BollaScaricoMagBulk bollaScaricoMag =  new BollaScaricoMagBulk();
	private DivisaBulk divisa =  new DivisaBulk();
	public MovimentiMagBulk() {
		super();
	}
	public MovimentiMagBulk(java.lang.Long pgMovimento) {
		super(pgMovimento);
	}
	public TipoMovimentoMagBulk getTipoMovimentoMag() {
		return tipoMovimentoMag;
	}
	public void setTipoMovimentoMag(TipoMovimentoMagBulk tipoMovimentoMag)  {
		this.tipoMovimentoMag=tipoMovimentoMag;
	}
	public OrdineAcqConsegnaBulk getOrdineAcqConsegnaUt() {
		return ordineAcqConsegnaUt;
	}
	public void setOrdineAcqConsegnaUt(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegnaUt=ordineAcqConsegna;
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	public LottoMagBulk getLottoMag() {
		return lottoMag;
	}
	public void setLottoMag(LottoMagBulk lottoMag)  {
		this.lottoMag=lottoMag;
	}
	public MagazzinoBulk getMagazzinoUt() {
		return magazzinoUt;
	}
	public void setMagazzinoUt(MagazzinoBulk magazzino)  {
		this.magazzinoUt=magazzino;
	}
	public Bene_servizioBulk getBeneServizioUt() {
		return beneServizioUt;
	}
	public void setBeneServizioUt(Bene_servizioBulk beneServizio)  {
		this.beneServizioUt=beneServizio;
	}
	public BollaScaricoMagBulk getBollaScaricoMag() {
		return bollaScaricoMag;
	}
	public void setBollaScaricoMag(BollaScaricoMagBulk bollaScaricoMag)  {
		this.bollaScaricoMag=bollaScaricoMag;
	}
	public DivisaBulk getDivisa() {
		return divisa;
	}
	public void setDivisa(DivisaBulk divisa)  {
		this.divisa=divisa;
	}
	public java.lang.String getCdCdsTipoMovimento() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMag().getCdCds();
	}
	public void setCdCdsTipoMovimento(java.lang.String cdCdsTipoMovimento)  {
		this.getTipoMovimentoMag().setCdCds(cdCdsTipoMovimento);
	}
	public java.lang.String getCdTipoMovimento() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMag().getCdTipoMovimento();
	}
	public void setCdTipoMovimento(java.lang.String cdTipoMovimento)  {
		this.getTipoMovimentoMag().setCdTipoMovimento(cdTipoMovimento);
	}
	public Long getPgMovimentoRif() {
		MovimentiMagBulk rif = this.getMovimentoRif();
		if (rif == null)
			return null;
		return getMovimentoRif().getPgMovimento();
	}  
	public void setPgMovimentoRif(java.lang.Long pgMovimento)  {
		if (this.getMovimentoRif() == null)
			setMovimentoRif(new MovimentiMagBulk());
		this.getMovimentoRif().setPgMovimento(pgMovimento);
	}
	public Long getPgMovimentoAnn() {
		MovimentiMagBulk ann = this.getMovimentoAnn();
		if (ann == null)
			return null;
		return getMovimentoAnn().getPgMovimento();
	}
	public void setPgMovimentoAnn(java.lang.Long pgMovimento)  {
		if (this.getMovimentoAnn() == null)
			setMovimentoAnn(new MovimentiMagBulk());
		this.getMovimentoAnn().setPgMovimento(pgMovimento);
	}
	public Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();  
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	public java.lang.String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura == null)
			return null;
		return getUnitaMisura().getCdUnitaMisura();
	}
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.getUnitaMisura().setCdUnitaMisura(cdUnitaMisura);
	}
	public java.lang.String getCdUop() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	public void setCdUop(java.lang.String cdUop)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUop);
	}
	public java.lang.String getCdCdsLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCdCds();
	}
	public void setCdCdsLotto(java.lang.String cdCdsLotto)  {
		this.getLottoMag().setCdCds(cdCdsLotto);
	}
	public java.lang.String getCdMagazzinoLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCdMagazzino();
	}
	public void setCdMagazzinoLotto(java.lang.String cdMagazzinoLotto)  {
		this.getLottoMag().setCdMagazzino(cdMagazzinoLotto);
	}
	public java.lang.Integer getEsercizioLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getEsercizio();
	}
	public void setEsercizioLotto(java.lang.Integer esercizioLotto)  {
		this.getLottoMag().setEsercizio(esercizioLotto);
	}
	public java.lang.String getCdNumeratoreLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCdNumeratoreMag();
	}
	public void setCdNumeratoreLotto(java.lang.String cdNumeratoreLotto)  {
		this.getLottoMag().setCdNumeratoreMag(cdNumeratoreLotto);
	}
	public java.lang.Integer getPgLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getPgLotto();
	}
	public void setPgLotto(java.lang.Integer pgLotto)  {
		this.getLottoMag().setPgLotto(pgLotto);
	}
	public java.lang.String getCdCdsBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCdCds();
	}
	public void setCdCdsBollaSca(java.lang.String cdCdsBollaSca)  {
		this.getBollaScaricoMag().setCdCds(cdCdsBollaSca);
	}
	public java.lang.String getCdMagazzinoBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCdMagazzino();
	}
	public void setCdMagazzinoBollaSca(java.lang.String cdMagazzinoBollaSca)  {
		this.getBollaScaricoMag().setCdMagazzino(cdMagazzinoBollaSca);
	}
	public java.lang.Integer getEsercizioBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return bollaScaricoMag.getEsercizio();
	}
	public void setEsercizioBollaSca(java.lang.Integer esercizioBollaSca)  {
		this.getBollaScaricoMag().setEsercizio(esercizioBollaSca);
	}
	public java.lang.String getCdNumeratoreBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return bollaScaricoMag.getCdNumeratoreMag();
	}
	public void setCdNumeratoreBollaSca(java.lang.String cdNumeratoreBollaSca)  {
		this.getBollaScaricoMag().setCdNumeratoreMag(cdNumeratoreBollaSca);
	}
	public java.lang.Integer getPgBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getPgBollaSca();
	}
	public void setPgBollaSca(java.lang.Integer pgBollaSca)  {
		this.getBollaScaricoMag().setPgBollaSca(pgBollaSca);
	}
	public java.lang.String getCdDivisa() {
		DivisaBulk divisa = this.getDivisa();
		if (divisa == null)
			return null;
		return getDivisa().getCd_divisa();
	}
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.getDivisa().setCd_divisa(cdDivisa);
	}
	public MovimentiMagBulk getMovimentoRif() {
		return movimentoRif;
	}
	public void setMovimentoRif(MovimentiMagBulk movimentoRif) {
		this.movimentoRif = movimentoRif;
	}
	public MovimentiMagBulk getMovimentoAnn() {
		return movimentoAnn;
	}
	public void setMovimentoAnn(MovimentiMagBulk movimentoAnn) {
		this.movimentoAnn = movimentoAnn;
	}
}