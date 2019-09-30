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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
public class LottoMagBulk extends LottoMagBase {
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_SCADUTO = "SCA";
	public final static String STATO_VERIFICA = "VER";
	
	/**
	 * [NUMERAZIONE_MAG Definisce i contatori per la numerazione dei magazzini.]
	 **/
	private NumerazioneMagBulk numerazioneMag =  new NumerazioneMagBulk();
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private OrdineAcqConsegnaBulk ordineAcqConsegna =  new OrdineAcqConsegnaBulk();
	private TerzoBulk terzo =  new TerzoBulk();
	private DivisaBulk divisa =  new DivisaBulk();
	
	public LottoMagBulk() {
		super();
	}
	public LottoMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgLotto) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgLotto);
		setNumerazioneMag( new NumerazioneMagBulk(cdCds,cdMagazzino,esercizio,cdNumeratoreMag) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce i contatori per la numerazione dei magazzini.]
	 **/
	public NumerazioneMagBulk getNumerazioneMag() {
		return numerazioneMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce i contatori per la numerazione dei magazzini.]
	 **/
	public void setNumerazioneMag(NumerazioneMagBulk numerazioneMag)  {
		this.numerazioneMag=numerazioneMag;
	}
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	public OrdineAcqConsegnaBulk getOrdineAcqConsegna() {
		return ordineAcqConsegna;
	}
	public void setOrdineAcqConsegna(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegna=ordineAcqConsegna;
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	public DivisaBulk getDivisa() {
		return divisa;
	}	
	public void setDivisa(DivisaBulk divisa)  {
		this.divisa=divisa;
	}
	public java.lang.String getCdCds() {
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdCds();
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
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdMagazzino();
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
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getEsercizio();
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
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdNumeratoreMag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getNumerazioneMag().setCdNumeratoreMag(cdNumeratoreMag);
	}
	public java.lang.String getCdBeneServizio() {
		if (this.getBeneServizio() == null)
			return null;
		return this.getBeneServizio().getCd_bene_servizio();
	}
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	public java.lang.String getCdCdsMag() {
		if (this.getMagazzino() == null)
			return null;
		return this.getMagazzino().getCdCds();
	}
	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.getMagazzino().setCdCds(cdCdsMag);
	}
	public java.lang.String getCdMagazzinoMag() {
		if (this.getMagazzino() == null)
			return null;
		return this.getMagazzino().getCdMagazzino();
	}
	public void setCdMagazzinoMag(java.lang.String cdMagazzinoMag)  {
		this.getMagazzino().setCdMagazzino(cdMagazzinoMag);
	}
	public java.lang.String getCdCdsOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdCds();
	}
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
	}
	public java.lang.String getCdUnitaOperativa() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdUnitaOperativa();
	}
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativa);
	}
	public java.lang.Integer getEsercizioOrdine() {
		
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getEsercizio();
	}
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
	}
	public java.lang.String getCdNumeratoreOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdNumeratore();
	}
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
	}
	public java.lang.Integer getNumeroOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getNumero();
	}
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
	}
	public java.lang.Integer getRigaOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getRiga();
	}
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
	}
	public java.lang.Integer getConsegna() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getConsegna();
	}
	public void setConsegna(java.lang.Integer consegna)  {
		this.getOrdineAcqConsegna().setConsegna(consegna);
	}
	public java.lang.Integer getCdTerzo() {
		if (this.getTerzo() == null)
			return null;
		return this.getTerzo().getCd_terzo();
	}
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	public java.lang.String getCdDivisa() {
		if (this.getDivisa() == null)
			return null;
		return this.getDivisa().getCd_divisa();
	}
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.getDivisa().setCd_divisa(cdDivisa);
	}
}