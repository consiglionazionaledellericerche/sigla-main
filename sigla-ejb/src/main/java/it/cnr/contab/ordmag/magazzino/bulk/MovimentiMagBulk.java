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
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
public class MovimentiMagBulk extends MovimentiMagBase {
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_ANNULLATO = "ANN";
	
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	private OrdineAcqConsegnaBulk ordineAcqConsegnaUt =  new OrdineAcqConsegnaBulk();
	private TerzoBulk terzo =  new TerzoBulk();
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	private LottoMagBulk lottoMag =  new LottoMagBulk();
	private MagazzinoBulk magazzinoUt =  new MagazzinoBulk();
	private Bene_servizioBulk beneServizioUt =  new Bene_servizioBulk();
//	private Voce_ivaBulk voceIva =  new Voce_ivaBulk();
//	private BollaScaricoMagBulk bollaScaricoMag =  new BollaScaricoMagBulk();
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
//	public Voce_ivaBulk getVoceIva() {
//		return voceIva;
//	}
//	public void setVoceIva(Voce_ivaBulk voceIva)  {
//		this.voceIva=voceIva;
//	}
//	public BollaScaricoMagBulk getBollaScaricoMag() {
//		return bollaScaricoMag;
//	}
//	public void setBollaScaricoMag(BollaScaricoMagBulk bollaScaricoMag)  {
//		this.bollaScaricoMag=bollaScaricoMag;
//	}
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
//	public java.lang.String getCdCdsOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdCds();
//	}
//	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
//		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
//	}
//	public java.lang.String getCdUnitaOperativaOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdUnitaOperativa();
//	}
//	public void setCdUnitaOperativaOrdine(java.lang.String cdUnitaOperativaOrdine)  {
//		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativaOrdine);
//	}
//	public java.lang.Integer getEsercizioOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getEsercizio();
//	}
//	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
//		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
//	}
//	public java.lang.String getCdNumeratoreOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdNumeratore();
//	}
//	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
//		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
//	}
//	public java.lang.Integer getNumeroOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getNumero();
//	}
//	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
//		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
//	}
//	public java.lang.Integer getRigaOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getRiga();
//	}
//	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
//		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
//	}
//	public java.lang.Integer getConsegna() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getConsegna();
//	}
//	public void setConsegna(java.lang.Integer consegna)  {
//		this.getOrdineAcqConsegna().setConsegna(consegna);
//	}
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
//	public java.lang.String getCdCdsMag() {
//		MagazzinoBulk magazzino = this.getMagazzino();
//		if (magazzino == null)
//			return null;
//		return getMagazzino().getCdCds();
//	}
//	public void setCdCdsMag(java.lang.String cdCdsMag)  {
//		this.getMagazzino().setCdCds(cdCdsMag);
//	}
//	public java.lang.String getCdMagazzino() {
//		MagazzinoBulk magazzino = this.getMagazzino();
//		if (magazzino == null)
//			return null;
//		return getMagazzino().getCdMagazzino();
//	}
//	public void setCdMagazzino(java.lang.String cdMagazzino)  {
//		this.getMagazzino().setCdMagazzino(cdMagazzino);
//	}
//	public java.lang.String getCdBeneServizio() {
//		Bene_servizioBulk beneServizio = this.getBeneServizio();
//		if (beneServizio == null)
//			return null;
//		return getBeneServizio().getCd_bene_servizio();
//	}
//	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
//		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
//	}
//	public java.lang.String getCdVoceIva() {
//		Voce_ivaBulk voceIva = this.getVoceIva();
//		if (voceIva == null)
//			return null;
//		return getVoceIva().getCd_voce_iva();
//	}
//	public void setCdVoceIva(java.lang.String cdVoceIva)  {
//		this.getVoceIva().setCd_voce_iva(cdVoceIva);
//	}
//	public java.lang.String getCdCdsBollaSca() {
//		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
//		if (bollaScaricoMag == null)
//			return null;
//		return getBollaScaricoMag().getCdCds();
//	}
//	public void setCdCdsBollaSca(java.lang.String cdCdsBollaSca)  {
//		this.getBollaScaricoMag().setCdCds(cdCdsBollaSca);
//	}
//	public java.lang.String getCdMagazzinoBollaSca() {
//		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
//		if (bollaScaricoMag == null)
//			return null;
//		return getBollaScaricoMag().getCdMagazzino();
//	}
//	public void setCdMagazzinoBollaSca(java.lang.String cdMagazzinoBollaSca)  {
//		this.getBollaScaricoMag().setCdMagazzino(cdMagazzinoBollaSca);
//	}
//	public java.lang.Integer getEsercizioBollaSca() {
//		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
//		if (bollaScaricoMag == null)
//			return null;
//		return bollaScaricoMag.getEsercizio();
//	}
//	public void setEsercizioBollaSca(java.lang.Integer esercizioBollaSca)  {
//		this.getBollaScaricoMag().setEsercizio(esercizioBollaSca);
//	}
//	public java.lang.String getCdNumeratoreBollaSca() {
//		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
//		if (bollaScaricoMag == null)
//			return null;
//		return bollaScaricoMag.getCdNumeratoreMag();
//	}
//	public void setCdNumeratoreBollaSca(java.lang.String cdNumeratoreBollaSca)  {
//		this.getBollaScaricoMag().setCdNumeratoreMag(cdNumeratoreBollaSca);
//	}
//	public java.lang.Integer getPgBollaSca() {
//		BollaScaricoMagBulk bollaScaricoMag = this.getBollaScaricoMag();
//		if (bollaScaricoMag == null)
//			return null;
//		return getBollaScaricoMag().getPgBollaSca();
//	}
//	public void setPgBollaSca(java.lang.Integer pgBollaSca)  {
//		this.getBollaScaricoMag().setPgBollaSca(pgBollaSca);
//	}
	public java.lang.String getCdDivisa() {
		DivisaBulk divisa = this.getDivisa();
		if (divisa == null)
			return null;
		return getDivisa().getCd_divisa();
	}
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.getDivisa().setCd_divisa(cdDivisa);
	}
//	public java.lang.String getCdCdsOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdCds();
//	}
//	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
//		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
//	}
//	public java.lang.String getCdUnitaOperativaOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdUnitaOperativa();
//	}
//	public void setCdUnitaOperativaOrdine(java.lang.String cdUnitaOperativa)  {
//		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativa);
//	}
//	public java.lang.Integer getEsercizioOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getEsercizio();
//	}
//	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
//		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
//	}
//	public java.lang.String getCdNumeratoreOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getCdNumeratore();
//	}
//	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
//		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
//	}
//	public java.lang.Integer getNumeroOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getNumero();
//	}
//	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
//		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
//	}
//	public java.lang.Integer getRigaOrdine() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getRiga();
//	}
//	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
//		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
//	}
//	public java.lang.Integer getConsegna() {
//		OrdineAcqConsegnaBulk ordineAcqConsegna = this.getOrdineAcqConsegna();
//		if (ordineAcqConsegna == null)
//			return null;
//		return getOrdineAcqConsegna().getConsegna();
//	}
//	public void setConsegna(java.lang.Integer consegna)  {
//		this.getOrdineAcqConsegna().setConsegna(consegna);
//	}
}