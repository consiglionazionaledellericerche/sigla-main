/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.ordmag.anag00.*;
public class BollaScaricoMagBulk extends BollaScaricoMagBase {
	/**
	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private MagazzinoBulk magazzinoDest =  new MagazzinoBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_MAG
	 **/
	public BollaScaricoMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_MAG
	 **/
	public BollaScaricoMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgBollaSca) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgBollaSca);
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
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsMag]
	 **/
	public java.lang.String getCdCdsMag() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMag]
	 **/
	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.getMagazzino().setCdCds(cdCdsMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoMag]
	 **/
	public java.lang.String getCdMagazzinoMag() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoMag]
	 **/
	public void setCdMagazzinoMag(java.lang.String cdMagazzinoMag)  {
		this.getMagazzino().setCdMagazzino(cdMagazzinoMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsMagDest]
	 **/
	public java.lang.String getCdCdsMagDest() {
		MagazzinoBulk magazzino = this.getMagazzinoDest();
		if (magazzino == null)
			return null;
		return getMagazzinoDest().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMagDest]
	 **/
	public void setCdCdsMagDest(java.lang.String cdCdsMagDest)  {
		this.getMagazzinoDest().setCdCds(cdCdsMagDest);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoDest]
	 **/
	public java.lang.String getCdMagazzinoDest() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzinoDest().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoDest]
	 **/
	public void setCdMagazzinoDest(java.lang.String cdMagazzinoDest)  {
		this.getMagazzinoDest().setCdMagazzino(cdMagazzinoDest);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUopDest]
	 **/
	public java.lang.String getCdUopDest() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUopDest]
	 **/
	public void setCdUopDest(java.lang.String cdUopDest)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUopDest);
	}
	public MagazzinoBulk getMagazzinoDest() {
		return magazzinoDest;
	}
	public void setMagazzinoDest(MagazzinoBulk magazzinoDest) {
		this.magazzinoDest = magazzinoDest;
	}
}