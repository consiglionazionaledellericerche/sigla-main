/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
public class BollaScaricoRigaMagBulk extends BollaScaricoRigaMagBase {
	/**
	 * [BOLLA_SCARICO_MAG Archivio per la testata delle Bolle di Scarico]
	 **/
	private BollaScaricoMagBulk bollaScaricoMag =  new BollaScaricoMagBulk();
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	private RichiestaUopRigaBulk richiestaUopRiga =  new RichiestaUopRigaBulk();
	private LottoMagBulk lottoMag =  new LottoMagBulk();
	private MovimentiMagBulk movimentiMag =  new MovimentiMagBulk();
	private OrdineAcqConsegnaBulk ordineAcqConsegna =  new OrdineAcqConsegnaBulk();
	public BollaScaricoRigaMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_RIGA_MAG
	 **/
	public BollaScaricoRigaMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgBollaSca, java.lang.Integer rigbollaScaN) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgBollaSca, rigbollaScaN);
		setBollaScaricoMag( new BollaScaricoMagBulk(cdCds,cdMagazzino,esercizio,cdNumeratoreMag,pgBollaSca) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Archivio per la testata delle Bolle di Scarico]
	 **/
	public BollaScaricoMagBulk getBollaScaricoMag() {
		return bollaScaricoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Archivio per la testata delle Bolle di Scarico]
	 **/
	public void setBollaScaricoMag(BollaScaricoMagBulk bollaScaricoMag)  {
		this.bollaScaricoMag=bollaScaricoMag;
	}
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	public RichiestaUopRigaBulk getRichiestaUopRiga() {
		return richiestaUopRiga;
	}
	public void setRichiestaUopRiga(RichiestaUopRigaBulk richiestaUopRiga)  {
		this.richiestaUopRiga=richiestaUopRiga;
	}
	public LottoMagBulk getLottoMag() {
		return lottoMag;
	}
	public void setLottoMag(LottoMagBulk lottoMag)  {
		this.lottoMag=lottoMag;
	}
	public MovimentiMagBulk getMovimentiMag() {
		return movimentiMag;
	}
	public void setMovimentiMag(MovimentiMagBulk movimentiMag)  {
		this.movimentiMag=movimentiMag;
	}
	public OrdineAcqConsegnaBulk getOrdineAcqConsegna() {
		return ordineAcqConsegna;
	}
	public void setOrdineAcqConsegna(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegna=ordineAcqConsegna;
	}
	public java.lang.String getCdCds() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getBollaScaricoMag().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getBollaScaricoMag().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getBollaScaricoMag().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public java.lang.String getCdNumeratoreMag() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCdNumeratoreMag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getBollaScaricoMag().setCdNumeratoreMag(cdNumeratoreMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBollaSca]
	 **/
	public java.lang.Integer getPgBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getPgBollaSca();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBollaSca]
	 **/
	public void setPgBollaSca(java.lang.Integer pgBollaSca)  {
		this.getBollaScaricoMag().setPgBollaSca(pgBollaSca);
	}
	public java.lang.String getCdBeneServizio() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
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
	public java.lang.String getCdCdsRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCdCds();
	}
	public void setCdCdsRich(java.lang.String cdCdsRich)  {
		this.getRichiestaUopRiga().setCdCds(cdCdsRich);
	}
	public java.lang.String getCdUnitaOperativaRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCdUnitaOperativa();
	}
	public void setCdUnitaOperativaRich(java.lang.String cdUnitaOperativaRich)  {
		this.getRichiestaUopRiga().setCdUnitaOperativa(cdUnitaOperativaRich);
	}
	public java.lang.Integer getEsercizioRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getEsercizio();
	}
	public void setEsercizioRich(java.lang.Integer esercizioRich)  {
		this.getRichiestaUopRiga().setEsercizio(esercizioRich);
	}
	public java.lang.String getCdNumeratoreRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCdNumeratore();
	}
	public void setCdNumeratoreRich(java.lang.String cdNumeratoreRich)  {
		this.getRichiestaUopRiga().setCdNumeratore(cdNumeratoreRich);
	}
	public java.lang.Integer getNumeroRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getNumero();
	}
	public void setNumeroRich(java.lang.Integer numeroRich)  {
		this.getRichiestaUopRiga().setNumero(numeroRich);
	}
	public java.lang.Integer getRigaRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getRiga();
	}
	public void setRigaRich(java.lang.Integer rigaRich)  {
		this.getRichiestaUopRiga().setRiga(rigaRich);
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
	public java.lang.Long getPgMovimento() {
		MovimentiMagBulk movimentiMag = this.getMovimentiMag();
		if (movimentiMag == null)
			return null;
		return getMovimentiMag().getPgMovimento();
	}
	public void setPgMovimento(java.lang.Long pgMovimento)  {
		this.getMovimentiMag().setPgMovimento(pgMovimento);
	}
	public java.lang.String getCdCdsOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCdCds();
	}
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
	}
	public java.lang.String getCdUnitaOperativa() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCdUnitaOperativa();
	}
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativa);
	}
	public java.lang.Integer getEsercizioOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getEsercizio();
	}
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
	}
	public java.lang.String getCdNumeratoreOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCdNumeratore();
	}
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
	}
	public java.lang.Integer getNumeroOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getNumero();
	}
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
	}
	public java.lang.Integer getRigaOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getRiga();
	}
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
	}
	public java.lang.Integer getConsegna() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getConsegna();
	}
	public void setConsegna(java.lang.Integer consegna)  {
		this.getOrdineAcqConsegna().setConsegna(consegna);
	}
}