/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.ordmag.magazzino.bulk.*;
import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.ordmag.richieste.bulk.*;
import it.cnr.contab.ordmag.richieste.*;
import it.cnr.contab.ordmag.ordini.*;
public class BollaScaricoRigaMagBulk extends BollaScaricoRigaMagBase {
	/**
	 * [BOLLA_SCARICO_MAG Archivio per la testata delle Bolle di Scarico]
	 **/
	private BollaScaricoMagBulk bollaScaricoMag =  new BollaScaricoMagBulk();
	/**
	 * [BENE_SERVIZIO Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	/**
	 * [UNITA_MISURA Rappresenta l'anagrafica delle unità di misura.]
	 **/
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	/**
	 * [RICHIESTA_UOP_RIGA Riga richieste]
	 **/
	private RichiestaUopRigaBulk richiestaUopRiga =  new RichiestaUopRigaBulk();
	/**
	 * [LOTTO_MAG Giacenze di Magazzino]
	 **/
	private LottoMagBulk lottoMag =  new LottoMagBulk();
	/**
	 * [MOVIMENTI_MAG Archivio dei Movimenti]
	 **/
	private MovimentiMagBulk movimentiMag =  new MovimentiMagBulk();
	/**
	 * [ORDINE_ACQ_CONSEGNA Consegna Ordine d'Acquisto]
	 **/
	private OrdineAcqConsegnaBulk ordineAcqConsegna =  new OrdineAcqConsegnaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_RIGA_MAG
	 **/
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Riga richieste]
	 **/
	public RichiestaUopRigaBulk getRichiestaUopRiga() {
		return richiestaUopRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Riga richieste]
	 **/
	public void setRichiestaUopRiga(RichiestaUopRigaBulk richiestaUopRiga)  {
		this.richiestaUopRiga=richiestaUopRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Giacenze di Magazzino]
	 **/
	public LottoMagBulk getLottoMag() {
		return lottoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Giacenze di Magazzino]
	 **/
	public void setLottoMag(LottoMagBulk lottoMag)  {
		this.lottoMag=lottoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Archivio dei Movimenti]
	 **/
	public MovimentiMagBulk getMovimentiMag() {
		return movimentiMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Archivio dei Movimenti]
	 **/
	public void setMovimentiMag(MovimentiMagBulk movimentiMag)  {
		this.movimentiMag=movimentiMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Consegna Ordine d'Acquisto]
	 **/
	public OrdineAcqConsegnaBulk getOrdineAcqConsegna() {
		return ordineAcqConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Consegna Ordine d'Acquisto]
	 **/
	public void setOrdineAcqConsegna(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegna=ordineAcqConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getBollaScaricoMag().setCd_cds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getCd_magazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getBollaScaricoMag().setCd_magazzino(cdMagazzino);
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
		return getBollaScaricoMag().getCd_numeratore_mag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getBollaScaricoMag().setCd_numeratore_mag(cdNumeratoreMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBollaSca]
	 **/
	public java.lang.Integer getPgBollaSca() {
		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
		if (bollaScaricoMag == null)
			return null;
		return getBollaScaricoMag().getPg_bolla_sca();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBollaSca]
	 **/
	public void setPgBollaSca(java.lang.Integer pgBollaSca)  {
		this.getBollaScaricoMag().setPg_bolla_sca(pgBollaSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura == null)
			return null;
		return getUnitaMisura().getCd_unita_misura();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.getUnitaMisura().setCd_unita_misura(cdUnitaMisura);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRich]
	 **/
	public java.lang.String getCdCdsRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRich]
	 **/
	public void setCdCdsRich(java.lang.String cdCdsRich)  {
		this.getRichiestaUopRiga().setCd_cds(cdCdsRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaRich]
	 **/
	public java.lang.String getCdUnitaOperativaRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCd_unita_operativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaRich]
	 **/
	public void setCdUnitaOperativaRich(java.lang.String cdUnitaOperativaRich)  {
		this.getRichiestaUopRiga().setCd_unita_operativa(cdUnitaOperativaRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioRich]
	 **/
	public java.lang.Integer getEsercizioRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioRich]
	 **/
	public void setEsercizioRich(java.lang.Integer esercizioRich)  {
		this.getRichiestaUopRiga().setEsercizio(esercizioRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreRich]
	 **/
	public java.lang.String getCdNumeratoreRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getCd_numeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreRich]
	 **/
	public void setCdNumeratoreRich(java.lang.String cdNumeratoreRich)  {
		this.getRichiestaUopRiga().setCd_numeratore(cdNumeratoreRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroRich]
	 **/
	public java.lang.Integer getNumeroRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getNumero();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroRich]
	 **/
	public void setNumeroRich(java.lang.Integer numeroRich)  {
		this.getRichiestaUopRiga().setNumero(numeroRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaRich]
	 **/
	public java.lang.Integer getRigaRich() {
		RichiestaUopRigaBulk richiestaUopRiga = this.getRichiestaUopRiga();
		if (richiestaUopRiga == null)
			return null;
		return getRichiestaUopRiga().getRiga();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaRich]
	 **/
	public void setRigaRich(java.lang.Integer rigaRich)  {
		this.getRichiestaUopRiga().setRiga(rigaRich);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsLotto]
	 **/
	public java.lang.String getCdCdsLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsLotto]
	 **/
	public void setCdCdsLotto(java.lang.String cdCdsLotto)  {
		this.getLottoMag().setCd_cds(cdCdsLotto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoLotto]
	 **/
	public java.lang.String getCdMagazzinoLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCd_magazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoLotto]
	 **/
	public void setCdMagazzinoLotto(java.lang.String cdMagazzinoLotto)  {
		this.getLottoMag().setCd_magazzino(cdMagazzinoLotto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioLotto]
	 **/
	public java.lang.Integer getEsercizioLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioLotto]
	 **/
	public void setEsercizioLotto(java.lang.Integer esercizioLotto)  {
		this.getLottoMag().setEsercizio(esercizioLotto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreLotto]
	 **/
	public java.lang.String getCdNumeratoreLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getCd_numeratore_mag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreLotto]
	 **/
	public void setCdNumeratoreLotto(java.lang.String cdNumeratoreLotto)  {
		this.getLottoMag().setCd_numeratore_mag(cdNumeratoreLotto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgLotto]
	 **/
	public java.lang.Integer getPgLotto() {
		LottoMagBulk lottoMag = this.getLottoMag();
		if (lottoMag == null)
			return null;
		return getLottoMag().getPg_lotto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgLotto]
	 **/
	public void setPgLotto(java.lang.Integer pgLotto)  {
		this.getLottoMag().setPg_lotto(pgLotto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMovimento]
	 **/
	public java.lang.Long getPgMovimento() {
		MovimentiMagBulk movimentiMag = this.getMovimentiMag();
		if (movimentiMag == null)
			return null;
		return getMovimentiMag().getPg_movimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMovimento]
	 **/
	public void setPgMovimento(java.lang.Long pgMovimento)  {
		this.getMovimentiMag().setPg_movimento(pgMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.getOrdineAcqConsegna().setCd_cds(cdCdsOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCd_unita_operativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqConsegna().setCd_unita_operativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public java.lang.String getCdNumeratoreOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getCd_numeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.getOrdineAcqConsegna().setCd_numeratore(cdNumeratoreOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public java.lang.Integer getNumeroOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getNumero();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public java.lang.Integer getRigaOrdine() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getRiga();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
		if (ordineAcqConsegna == null)
			return null;
		return getOrdineAcqConsegna().getConsegna();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.getOrdineAcqConsegna().setConsegna(consegna);
	}
}