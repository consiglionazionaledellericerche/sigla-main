/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class NumerazioneMagBulk extends NumerazioneMagBase {
	/**
	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_MAG
	 **/
	public NumerazioneMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_MAG
	 **/
	public NumerazioneMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag);
		setMagazzino( new MagazzinoBulk(cdCds,cdMagazzino) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getMagazzino().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}
}