/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Dictionary;

public class MagazzinoBulk extends MagazzinoBase {
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [LUOGO_CONSEGNA_MAG Luoghi di Consegna]
	 **/
	private LuogoConsegnaMagBulk luogoConsegnaMag =  new LuogoConsegnaMagBulk();
	/**
	 * [TIPO_MOVIMENTO_MAG Anagrafica delle Tipologie dei Movimenti.]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMagCarMag =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagCarTra =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagCarFma =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagScaUo =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagTraSca =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagTraCar =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagRvPos =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagRvNeg =  new TipoMovimentoMagBulk();
	private TipoMovimentoMagBulk tipoMovimentoMagChi =  new TipoMovimentoMagBulk();
	/**
	 * [RAGGR_MAGAZZINO Rappresenta l'anagrafica dei raggruppamenti di magazzino.]
	 **/
	private RaggrMagazzinoBulk raggrMagazzinoSca =  new RaggrMagazzinoBulk();
	private RaggrMagazzinoBulk raggrMagazzinoRim =  new RaggrMagazzinoBulk();
	private BulkList categoriaGruppoColl = new BulkList();
	private BulkList numeratoreColl = new BulkList();
	private boolean isInQuery = false;

	public final static Dictionary METODO_VAL;
	static{
		METODO_VAL = new OrderedHashtable();
	}
	public Dictionary getMetodoValKeys() {
		return METODO_VAL;
	}

	public Dictionary getMetodoValKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getMetodoValKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MAGAZZINO
	 **/
	public MagazzinoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MAGAZZINO
	 **/
	public MagazzinoBulk(String cdCds, String cdMagazzino) {
		super(cdCds, cdMagazzino);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
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
	 * Restituisce il valore di: [Luoghi di Consegna]
	 **/
	public LuogoConsegnaMagBulk getLuogoConsegnaMag() {
		return luogoConsegnaMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Luoghi di Consegna]
	 **/
	public void setLuogoConsegnaMag(LuogoConsegnaMagBulk luogoConsegnaMag)  {
		this.luogoConsegnaMag=luogoConsegnaMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica dei raggruppamenti di magazzino.]
	 **/
	public RaggrMagazzinoBulk getRaggrMagazzinoSca() {
		return raggrMagazzinoSca;
	}
	public RaggrMagazzinoBulk getRaggrMagazzinoRim() {
		return raggrMagazzinoRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica dei raggruppamenti di magazzino.]
	 **/
	public void setRaggrMagazzinoSca(RaggrMagazzinoBulk raggrMagazzino)  {
		this.raggrMagazzinoSca=raggrMagazzino;
	}
	public void setRaggrMagazzinoRim(RaggrMagazzinoBulk raggrMagazzino)  {
		this.raggrMagazzinoRim=raggrMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsLuogo]
	 **/
	public String getCdCdsLuogo() {
		LuogoConsegnaMagBulk luogoConsegnaMag = this.getLuogoConsegnaMag();
		if (luogoConsegnaMag == null)
			return null;
		return getLuogoConsegnaMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsLuogo]
	 **/
	public void setCdCdsLuogo(String cdCdsLuogo)  {
		this.getLuogoConsegnaMag().setCdCds(cdCdsLuogo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLuogoConsegna]
	 **/
	public String getCdLuogoConsegna() {
		LuogoConsegnaMagBulk luogoConsegnaMag = this.getLuogoConsegnaMag();
		if (luogoConsegnaMag == null)
			return null;
		return getLuogoConsegnaMag().getCdLuogoConsegna();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLuogoConsegna]
	 **/
	public void setCdLuogoConsegna(String cdLuogoConsegna)  {
		this.getLuogoConsegnaMag().setCdLuogoConsegna(cdLuogoConsegna);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarMag]
	 **/
	public String getCdCdsCarMag() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarMag]
	 **/
	public void setCdCdsCarMag(String cdCdsCarMag)  {
		this.getTipoMovimentoMagCarMag().setCdCds(cdCdsCarMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarMag]
	 **/
	public String getCdTipoMovimentoCarMag() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarMag();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarMag().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarMag]
	 **/
	public void setCdTipoMovimentoCarMag(String cdTipoMovimentoCarMag)  {
		this.getTipoMovimentoMagCarMag().setCdTipoMovimento(cdTipoMovimentoCarMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarTra]
	 **/
	public String getCdCdsCarTra() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarTra();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarTra().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarTra]
	 **/
	public void setCdCdsCarTra(String cdCdsCarTra)  {
		this.getTipoMovimentoMagCarTra().setCdCds(cdCdsCarTra);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarTra]
	 **/
	public String getCdTipoMovimentoCarTra() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarTra();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarTra().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarTra]
	 **/
	public void setCdTipoMovimentoCarTra(String cdTipoMovimentoCarTra)  {
		this.getTipoMovimentoMagCarTra().setCdTipoMovimento(cdTipoMovimentoCarTra);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarFma]
	 **/
	public String getCdCdsCarFma() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarFma();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarFma().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarFma]
	 **/
	public void setCdCdsCarFma(String cdCdsCarFma)  {
		this.getTipoMovimentoMagCarFma().setCdCds(cdCdsCarFma);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarFma]
	 **/
	public String getCdTipoMovimentoCarFma() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagCarFma();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagCarFma().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarFma]
	 **/
	public void setCdTipoMovimentoCarFma(String cdTipoMovimentoCarFma)  {
		this.getTipoMovimentoMagCarFma().setCdTipoMovimento(cdTipoMovimentoCarFma);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsScaUo]
	 **/
	public String getCdCdsScaUo() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagScaUo();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagScaUo().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsScaUo]
	 **/
	public void setCdCdsScaUo(String cdCdsScaUo)  {
		this.getTipoMovimentoMagScaUo().setCdCds(cdCdsScaUo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoScaUo]
	 **/
	public String getCdTipoMovimentoScaUo() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagScaUo();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagScaUo().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoScaUo]
	 **/
	public void setCdTipoMovimentoScaUo(String cdTipoMovimentoScaUo)  {
		this.getTipoMovimentoMagScaUo().setCdTipoMovimento(cdTipoMovimentoScaUo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTraSca]
	 **/
	public String getCdCdsTraSca() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagTraSca();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagTraSca().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTraSca]
	 **/
	public void setCdCdsTraSca(String cdCdsTraSca)  {
		this.getTipoMovimentoMagTraSca().setCdCds(cdCdsTraSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoTraSca]
	 **/
	public String getCdTipoMovimentoTraSca() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagTraSca();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagTraSca().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoTraSca]
	 **/
	public void setCdTipoMovimentoTraSca(String cdTipoMovimentoTraSca)  {
		this.getTipoMovimentoMagTraSca().setCdTipoMovimento(cdTipoMovimentoTraSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTraCar]
	 **/
	public String getCdCdsTraCar() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagTraCar();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagTraCar().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTraCar]
	 **/
	public void setCdCdsTraCar(String cdCdsTraCar)  {
		this.getTipoMovimentoMagTraCar().setCdCds(cdCdsTraCar);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoTraCar]
	 **/
	public String getCdTipoMovimentoTraCar() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagTraCar();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagTraCar().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoTraCar]
	 **/
	public void setCdTipoMovimentoTraCar(String cdTipoMovimentoTraCar)  {
		this.getTipoMovimentoMagTraCar().setCdTipoMovimento(cdTipoMovimentoTraCar);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRvPos]
	 **/
	public String getCdCdsRvPos() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRvPos();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRvPos().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRvPos]
	 **/
	public void setCdCdsRvPos(String cdCdsRvPos)  {
		this.getTipoMovimentoMagRvPos().setCdCds(cdCdsRvPos);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRvPos]
	 **/
	public String getCdTipoMovimentoRvPos() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRvPos();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRvPos().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRvPos]
	 **/
	public void setCdTipoMovimentoRvPos(String cdTipoMovimentoRvPos)  {
		this.getTipoMovimentoMagRvPos().setCdTipoMovimento(cdTipoMovimentoRvPos);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRvNeg]
	 **/
	public String getCdCdsRvNeg() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRvNeg();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRvNeg().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRvNeg]
	 **/
	public void setCdCdsRvNeg(String cdCdsRvNeg)  {
		this.getTipoMovimentoMagRvNeg().setCdCds(cdCdsRvNeg);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRvNeg]
	 **/
	public String getCdTipoMovimentoRvNeg() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRvNeg();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRvNeg().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRvNeg]
	 **/
	public void setCdTipoMovimentoRvNeg(String cdTipoMovimentoRvNeg)  {
		this.getTipoMovimentoMagRvNeg().setCdTipoMovimento(cdTipoMovimentoRvNeg);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsChi]
	 **/
	public String getCdCdsChi() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagChi();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagChi().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsChi]
	 **/
	public void setCdCdsChi(String cdCdsChi)  {
		this.getTipoMovimentoMagChi().setCdCds(cdCdsChi);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoChi]
	 **/
	public String getCdTipoMovimentoChi() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagChi();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagChi().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoChi]
	 **/
	public void setCdTipoMovimentoChi(String cdTipoMovimentoChi)  {
		this.getTipoMovimentoMagChi().setCdTipoMovimento(cdTipoMovimentoChi);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRaggrSca]
	 **/
	public String getCdCdsRaggrSca() {
		RaggrMagazzinoBulk raggrMagazzino = this.getRaggrMagazzinoSca();
		if (raggrMagazzino == null)
			return null;
		return getRaggrMagazzinoSca().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRaggrSca]
	 **/
	public void setCdCdsRaggrSca(String cdCdsRaggrSca)  {
		this.getRaggrMagazzinoSca().setCdCds(cdCdsRaggrSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdRaggrMagazzinoSca]
	 **/
	public String getCdRaggrMagazzinoSca() {
		RaggrMagazzinoBulk raggrMagazzino = this.getRaggrMagazzinoSca();
		if (raggrMagazzino == null)
			return null;
		return getRaggrMagazzinoSca().getCdRaggrMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdRaggrMagazzinoSca]
	 **/
	public void setCdRaggrMagazzinoSca(String cdRaggrMagazzinoSca)  {
		this.getRaggrMagazzinoSca().setCdRaggrMagazzino(cdRaggrMagazzinoSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRaggrRim]
	 **/
	public String getCdCdsRaggrRim() {
		RaggrMagazzinoBulk raggrMagazzino = this.getRaggrMagazzinoRim();
		if (raggrMagazzino == null)
			return null;
		return getRaggrMagazzinoRim().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRaggrRim]
	 **/
	public void setCdCdsRaggrRim(String cdCdsRaggrRim)  {
		this.getRaggrMagazzinoRim().setCdCds(cdCdsRaggrRim);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdRaggrMagazzinoRim]
	 **/
	public String getCdRaggrMagazzinoRim() {
		RaggrMagazzinoBulk raggrMagazzino = this.getRaggrMagazzinoRim();
		if (raggrMagazzino == null)
			return null;
		return getRaggrMagazzinoRim().getCdRaggrMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdRaggrMagazzinoRim]
	 **/
	public void setCdRaggrMagazzinoRim(String cdRaggrMagazzinoRim)  {
		this.getRaggrMagazzinoRim().setCdRaggrMagazzino(cdRaggrMagazzinoRim);
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagCarMag() {
		return tipoMovimentoMagCarMag;
	}
	public void setTipoMovimentoMagCarMag(TipoMovimentoMagBulk tipoMovimentoMagCarMag) {
		this.tipoMovimentoMagCarMag = tipoMovimentoMagCarMag;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagCarTra() {
		return tipoMovimentoMagCarTra;
	}
	public void setTipoMovimentoMagCarTra(TipoMovimentoMagBulk tipoMovimentoMagCarTra) {
		this.tipoMovimentoMagCarTra = tipoMovimentoMagCarTra;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagCarFma() {
		return tipoMovimentoMagCarFma;
	}
	public void setTipoMovimentoMagCarFma(TipoMovimentoMagBulk tipoMovimentoMagCarFma) {
		this.tipoMovimentoMagCarFma = tipoMovimentoMagCarFma;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagScaUo() {
		return tipoMovimentoMagScaUo;
	}
	public void setTipoMovimentoMagScaUo(TipoMovimentoMagBulk tipoMovimentoMagScaUo) {
		this.tipoMovimentoMagScaUo = tipoMovimentoMagScaUo;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagTraSca() {
		return tipoMovimentoMagTraSca;
	}
	public void setTipoMovimentoMagTraSca(TipoMovimentoMagBulk tipoMovimentoMagTraSca) {
		this.tipoMovimentoMagTraSca = tipoMovimentoMagTraSca;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagTraCar() {
		return tipoMovimentoMagTraCar;
	}
	public void setTipoMovimentoMagTraCar(TipoMovimentoMagBulk tipoMovimentoMagTraCar) {
		this.tipoMovimentoMagTraCar = tipoMovimentoMagTraCar;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagRvPos() {
		return tipoMovimentoMagRvPos;
	}
	public void setTipoMovimentoMagRvPos(TipoMovimentoMagBulk tipoMovimentoMagRvPos) {
		this.tipoMovimentoMagRvPos = tipoMovimentoMagRvPos;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagRvNeg() {
		return tipoMovimentoMagRvNeg;
	}
	public void setTipoMovimentoMagRvNeg(TipoMovimentoMagBulk tipoMovimentoMagRvNeg) {
		this.tipoMovimentoMagRvNeg = tipoMovimentoMagRvNeg;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagChi() {
		return tipoMovimentoMagChi;
	}
	public void setTipoMovimentoMagChi(TipoMovimentoMagBulk tipoMovimentoMagChi) {
		this.tipoMovimentoMagChi = tipoMovimentoMagChi;
	}
	protected OggettoBulk initialize(CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		impostaCds(context);
		return super.initialize(bp,context);
	}
	public OggettoBulk initializeForSearch(CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		super.initializeForSearch(bp,context);
		impostaCds(context);
		return this;
	}
	private void impostaCds(it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
	}
	public BulkList getCategoriaGruppoColl() {
		return categoriaGruppoColl;
	}
	public void setCategoriaGruppoColl(BulkList categoriaGruppoColl) {
		this.categoriaGruppoColl = categoriaGruppoColl;
	}
	public BulkCollection[] getBulkLists() {
		 return new BulkCollection[] {
				 categoriaGruppoColl, numeratoreColl};
	}
	public int addToCategoriaGruppoColl( AbilitBeneServMagBulk abilit) 
	{
		categoriaGruppoColl.add(abilit);
		abilit.setMagazzino(this);
		return categoriaGruppoColl.size()-1;
	}
	public AbilitBeneServMagBulk removeFromCategoriaGruppoColl(int index) 
	{
		// Gestisce la selezione del bottone cancella repertorio
		return (AbilitBeneServMagBulk)categoriaGruppoColl.remove(index);
	}
	public BulkList getNumeratoreColl() {
		return numeratoreColl;
	}
	public void setNumeratoreColl(BulkList numeratoreColl) {
		this.numeratoreColl = numeratoreColl;
	}
	public int addToNumeratoreColl( NumerazioneMagBulk num) 
	{
		numeratoreColl.add(num);
		num.setMagazzino(this);
		return numeratoreColl.size()-1;
	}
	public NumerazioneMagBulk removeFromNumeratoreColl(int index) 
	{
		// Gestisce la selezione del bottone cancella repertorio
		return (NumerazioneMagBulk)numeratoreColl.remove(index);
	}
	public boolean isInQuery() {
		return isInQuery;
	}
	public void setInQuery(boolean isInQuery) {
		this.isInQuery = isInQuery;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMag(String tipoConsegna) {
		switch (tipoConsegna) {
			case "MAG":  return this.getTipoMovimentoMagCarMag();
			case "TRA":  return this.getTipoMovimentoMagCarTra();
			case "FMA":  return this.getTipoMovimentoMagCarFma();
			default: return null;
		}
	}
	public Dictionary getTipoGestioneKeys() {

		return Bene_servizioBulk.TIPO_CONSEGNA;
	}

}