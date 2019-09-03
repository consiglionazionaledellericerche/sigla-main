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
	private RichiestaUopRigaBulk richiestaUopRiga =  new RichiestaUopRigaBulk();
	private MovimentiMagBulk movimentiMag =  new MovimentiMagBulk();
	public BollaScaricoRigaMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_RIGA_MAG
	 **/
	public BollaScaricoRigaMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgBollaSca, java.lang.Integer rigaBollaSca) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgBollaSca, rigaBollaSca);
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
	public RichiestaUopRigaBulk getRichiestaUopRiga() {
		return richiestaUopRiga;
	}
	public void setRichiestaUopRiga(RichiestaUopRigaBulk richiestaUopRiga)  {
		this.richiestaUopRiga=richiestaUopRiga;
	}
	public MovimentiMagBulk getMovimentiMag() {
		return movimentiMag;
	}
	public void setMovimentiMag(MovimentiMagBulk movimentiMag)  {
		this.movimentiMag=movimentiMag;
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
	public java.lang.Long getPgMovimento() {
		MovimentiMagBulk movimentiMag = this.getMovimentiMag();
		if (movimentiMag == null)
			return null;
		return getMovimentiMag().getPgMovimento();
	}
	public void setPgMovimento(java.lang.Long pgMovimento)  {
		this.getMovimentiMag().setPgMovimento(pgMovimento);
	}
}