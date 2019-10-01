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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.util.List;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
public class BollaScaricoMagBulk extends BollaScaricoMagBase {
	/**
	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private MagazzinoBulk magazzinoDest =  new MagazzinoBulk();
	private BulkList<BollaScaricoRigaMagBulk> righeColl =  new BulkList<BollaScaricoRigaMagBulk>();
	private NumerazioneMagBulk numerazioneMag =  new NumerazioneMagBulk();
	private String stampaBollaScarico;

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
		setNumerazioneMag( new NumerazioneMagBulk(cdCds,cdMagazzino,esercizio,cdNumeratoreMag) );
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
	public NumerazioneMagBulk getNumerazioneMag() {
		return numerazioneMag;
	}
	public void setNumerazioneMag(NumerazioneMagBulk numerazioneMag) {
		this.numerazioneMag = numerazioneMag;
	}
	public java.lang.String getCdCds() {
		NumerazioneMagBulk numerazioneMag = this.getNumerazioneMag();
		if (numerazioneMag == null)
			return null;
		return getNumerazioneMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getNumerazioneMag().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		NumerazioneMagBulk numerazioneMag = this.getNumerazioneMag();
		if (numerazioneMag == null)
			return null;
		return getNumerazioneMag().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getNumerazioneMag().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		NumerazioneMagBulk numerazioneMag = this.getNumerazioneMag();
		if (numerazioneMag == null)
			return null;
		return getNumerazioneMag().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getNumerazioneMag().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public java.lang.String getCdNumeratoreMag() {
		NumerazioneMagBulk numerazioneMag = this.getNumerazioneMag();
		if (numerazioneMag == null)
			return null;
		return getNumerazioneMag().getCdNumeratoreMag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getNumerazioneMag().setCdNumeratoreMag(cdNumeratoreMag);
	}
	public BulkList getRighe() {
		return righeColl;
	}
	public void setRighe(BulkList righe) {
		this.righeColl = righe;
	}
	public BollaScaricoRigaMagBulk removeFromRighe(int index) 
	{
		BollaScaricoRigaMagBulk element = (BollaScaricoRigaMagBulk)righeColl.get(index);
		return (BollaScaricoRigaMagBulk)righeColl.remove(index);
	}
	public int addToRigheColl( BollaScaricoRigaMagBulk nuovoRigo ) 
	{
		nuovoRigo.setBollaScaricoMag(this);
		nuovoRigo.setStato(OrdineAcqRigaBulk.STATO_INSERITA);
		nuovoRigo.setRigaBollaSca(righeColl.stream()
									.max((r1, r2)->Integer.compare(r1.getRigaBollaSca(), r2.getRigaBollaSca()))
									.map(BollaScaricoRigaMagBulk::getRigaBollaSca)
									.orElse(0)+1);
		righeColl.add(nuovoRigo);
		return righeColl.size()-1;
	}
	public List getChildren() {
		return getRighe();
	}
	public BulkCollection[] getBulkLists() {

		return new it.cnr.jada.bulk.BulkCollection[] { 
				righeColl
		};
	}
	public String getStampaBollaScarico() {
		return stampaBollaScarico;
	}
	public void setStampaBollaScarico(String stampaBollaScarico) {
		this.stampaBollaScarico = stampaBollaScarico;
	}
}