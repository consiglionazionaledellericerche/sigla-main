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
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class OrdineAcqConsegnaBulk extends OrdineAcqConsegnaBase {
	public final static String STATO_INSERITA = "INS";
	public final static String STATO_EVASA = "EVA";
	public final static String STATO_EVASA_FORZATAMENTE = "EVF";
	public final static String STATO_ANNULLATA = "ANN";
	public final static String STATO_FATT_NON_ASSOCIATA = "INS";
	public final static String STATO_FATT_ASSOCIATA_PARZIALMENTE = "ASP";
	public final static String STATO_FATT_ASSOCIATA_TOTALMENTE = "ASS";
	private final static String OPERAZIONE_EVADI_FORZATA = "E";
	private final static String OPERAZIONE_CREA_NUOVA_CONSEGNA = "C";

	private OrdineAcqRigaBulk ordineAcqRiga =  new OrdineAcqRigaBulk();
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private LuogoConsegnaMagBulk luogoConsegnaMag =  new LuogoConsegnaMagBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	private ContoBulk contoBulk =  new ContoBulk();

	protected BulkList righeRichiestaCollegate= new BulkList();
	
	private Obbligazione_scadenzarioBulk obbligazioneScadenzario =  new Obbligazione_scadenzarioBulk();
	
	private BigDecimal quantitaEvasa;
	private java.math.BigDecimal coefConvEvasa;
	private UnitaMisuraBulk unitaMisuraEvasa = new UnitaMisuraBulk();	
	private String lottoFornitore;
	private java.sql.Timestamp dtScadenza;
	private java.lang.Boolean obbligazioneInseritaSuConsegna =  Boolean.FALSE;
	private java.lang.Boolean autorizzaQuantitaEvasaMaggioreOrdinata = Boolean.FALSE;
	private String operazioneQuantitaEvasaMinore;
	
	
	public final static Dictionary OPERAZIONE_EVASIONE_CONSEGNA;
	static{
		OPERAZIONE_EVASIONE_CONSEGNA = new it.cnr.jada.util.OrderedHashtable();
		OPERAZIONE_EVASIONE_CONSEGNA.put(OPERAZIONE_CREA_NUOVA_CONSEGNA,"Crea nuova Consegna");
		OPERAZIONE_EVASIONE_CONSEGNA.put(OPERAZIONE_EVADI_FORZATA,"Evadi Forzatamente");
	}
	
	public final static Dictionary TIPO_CONSEGNA;
	static{
		TIPO_CONSEGNA = new it.cnr.jada.util.OrderedHashtable();
		TIPO_CONSEGNA.put(Bene_servizioBulk.TIPO_CONSEGNA_TRANSITO,"Transito");
		TIPO_CONSEGNA.put(Bene_servizioBulk.TIPO_CONSEGNA_MAGAZZINO,"Magazzino");
		TIPO_CONSEGNA.put(Bene_servizioBulk.TIPO_CONSEGNA_FUORI_MAGAZZINO,"Fuori Magazzino");
	}
	
	public final static Dictionary STATO;
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INSERITA,"Inserita");
		STATO.put(STATO_EVASA,"Evasa");
		STATO.put(STATO_ANNULLATA,"Annullata");
		STATO.put(STATO_EVASA_FORZATAMENTE,"Evasa Forzatamente");
	}
	
	public final static Dictionary STATO_FATT;
	static{
		STATO_FATT = new it.cnr.jada.util.OrderedHashtable();
		STATO_FATT.put(STATO_FATT_NON_ASSOCIATA,"Non Associata");
		STATO_FATT.put(STATO_FATT_ASSOCIATA_PARZIALMENTE,"Associata Parzialmente");
		STATO_FATT.put(STATO_FATT_ASSOCIATA_TOTALMENTE,"Associata Totalmente");
	}
	
	public OrdineAcqConsegnaBulk() {
		super();
	}
	
	public OrdineAcqConsegnaBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga, consegna);
		setOrdineAcqRiga( new OrdineAcqRigaBulk(cdCds,cdUnitaOperativa,esercizio,cdNumeratore,numero,riga) );
	}
	
	public OrdineAcqRigaBulk getOrdineAcqRiga() {
		return ordineAcqRiga;
	}
	
	public void setOrdineAcqRiga(OrdineAcqRigaBulk ordineAcqRiga)  {
		this.ordineAcqRiga=ordineAcqRiga;
	}
	
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	
	public Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
		return obbligazioneScadenzario;
	}

	public void setObbligazioneScadenzario(Obbligazione_scadenzarioBulk obbligazioneScadenzario)  {
		this.obbligazioneScadenzario=obbligazioneScadenzario;
	}

	public java.lang.String getCdCds() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdCds();
	}
	
	public void setCdCds(java.lang.String cdCds)  {
		this.getOrdineAcqRiga().setCdCds(cdCds);
	}
	
	public java.lang.String getCdUnitaOperativa() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdUnitaOperativa();
	}
	
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqRiga().setCdUnitaOperativa(cdUnitaOperativa);
	}
	
	public java.lang.Integer getEsercizio() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getEsercizio();
	}
	
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getOrdineAcqRiga().setEsercizio(esercizio);
	}
	
	public java.lang.String getCdNumeratore() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdNumeratore();
	}
	
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getOrdineAcqRiga().setCdNumeratore(cdNumeratore);
	}
	
	public java.lang.Integer getNumero() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getNumero();
	}
	
	public void setNumero(java.lang.Integer numero)  {
		this.getOrdineAcqRiga().setNumero(numero);
	}
	
	public java.lang.Integer getRiga() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getRiga();
	}
	
	public void setRiga(java.lang.Integer riga)  {
		this.getOrdineAcqRiga().setRiga(riga);
	}
	
	public java.lang.String getCdCdsMag() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}

	public ContoBulk getContoBulk() {
		return contoBulk;
	}

	public void setContoBulk(ContoBulk voceEp) {
		this.contoBulk = voceEp;
	}

	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.getMagazzino().setCdCds(cdCdsMag);
	}

	public void setCdVoceEp(java.lang.String cdVoceEp)  {
		this.getContoBulk().setCd_voce_ep(cdVoceEp);
	}

	public java.lang.String getCdVoceEp() {
		ContoBulk contoBulk = this.getContoBulk();
		if (contoBulk == null)
			return null;
		return getContoBulk().getCd_voce_ep();
	}

	public java.lang.String getCdCdsLuogo() {
		LuogoConsegnaMagBulk luogo = this.getLuogoConsegnaMag();
		if (luogo == null)
			return null;
		return getLuogoConsegnaMag().getCdCds();
	}
	
	public void setCdCdsLuogo(java.lang.String cdCdsLuogo)  {
		this.getLuogoConsegnaMag().setCdCds(cdCdsLuogo);
	}

	public java.lang.String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}

	public java.lang.String getCdLuogoConsegna() {
		LuogoConsegnaMagBulk luogo = this.getLuogoConsegnaMag();
		if (luogo == null)
			return null;
		return getLuogoConsegnaMag().getCdLuogoConsegna();
	}

	public void setCdLuogoConsegna(java.lang.String cdLuogoConsegna)  {
		this.getLuogoConsegnaMag().setCdLuogoConsegna(cdLuogoConsegna);
	}

	public java.lang.String getCdUopDest() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}

	public void setCdUopDest(java.lang.String cdUopDest)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUopDest);
	}

	public java.lang.String getCdCdsObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getCd_cds();
	}

	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.getObbligazioneScadenzario().setCd_cds(cdCdsObbl);
	}

	public java.lang.Integer getEsercizioEp() {
		ContoBulk contoBulk = this.getContoBulk();
		if (contoBulk == null)
			return null;
		return getContoBulk().getEsercizio();
	}

	public void setEsercizioEp(java.lang.Integer esercizioEp)  {
		this.getContoBulk().setEsercizio(esercizioEp);
	}

	public java.lang.Integer getEsercizioObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio();
	}

	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.getObbligazioneScadenzario().setEsercizio(esercizioObbl);
	}

	public java.lang.Integer getEsercizioOrigObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio_originale();
	}

	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.getObbligazioneScadenzario().setEsercizio_originale(esercizioOrigObbl);
	}

	public java.lang.Long getPgObbligazione() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione();
	}

	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.getObbligazioneScadenzario().setPg_obbligazione(pgObbligazione);
	}

	public java.lang.Long getPgObbligazioneScad() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione_scadenzario();
	}

	public void setPgObbligazioneScad(java.lang.Long pgObbligazioneScad)  {
		this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pgObbligazioneScad);
	}

	public LuogoConsegnaMagBulk getLuogoConsegnaMag() {
		return luogoConsegnaMag;
	}
	
	public void setLuogoConsegnaMag(LuogoConsegnaMagBulk luogoConsegnaMag) {
		this.luogoConsegnaMag = luogoConsegnaMag;
	}
	
	public Dictionary getTipoConsegnaKeys() {
		return TIPO_CONSEGNA;
	}
	
	public Dictionary getOperazioneEvasioneKeys() {
		return OPERAZIONE_EVASIONE_CONSEGNA;
	}
	
	public Dictionary getStatoKeys() {
		return STATO;
	}
	
	public Dictionary getStatoFattKeys() {
		return STATO_FATT;
	}
	
	public Dictionary getTipoConsegnaKeysForSearch() {
		OrderedHashtable d = (OrderedHashtable)getTipoConsegnaKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}

	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) 
	{
		inizializza();
		return this;
	}
	public OggettoBulk inizializza() 
	{
		setStato(STATO_INSERITA);
		setStatoFatt(STATO_FATT_NON_ASSOCIATA);
		setImImponibile(BigDecimal.ZERO);
		setImImponibileDivisa(BigDecimal.ZERO);
		setImIva(BigDecimal.ZERO);
		setImIvaDivisa(BigDecimal.ZERO);
		setImTotaleConsegna(BigDecimal.ZERO);
		return this;
	}
	
	public OggettoBulk inizializzaConsegnaNuovaRiga(){
		OrdineAcqConsegnaBulk consegna = this;
		consegna.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
		consegna.setConsegna(1);
		consegna.setToBeCreated();
		consegna.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
		consegna.setStatoFatt(OrdineAcqConsegnaBulk.STATO_FATT_NON_ASSOCIATA);
		return consegna;
	}
	
	public Boolean isConsegnaMagazzino(){
		return getTipoConsegna() != null && getTipoConsegna().equals(Bene_servizioBulk.TIPO_CONSEGNA_MAGAZZINO);
	}
	
	public BulkList getRigheRichiestaCollegate() {
		return righeRichiestaCollegate;
	}
	
	public void setRigheRichiestaCollegate(BulkList righeRichiestaCollegate) {
		this.righeRichiestaCollegate = righeRichiestaCollegate;
	}
	
	public Boolean getObbligazioneInseritaSuConsegna() {
		return obbligazioneInseritaSuConsegna;
	}
	
	public void setObbligazioneInseritaSuConsegna(Boolean obbligazioneInseritaSuConsegna) {
		this.obbligazioneInseritaSuConsegna = obbligazioneInseritaSuConsegna;
	}
	
	public BigDecimal getQuantitaEvasa() {
		return quantitaEvasa;
	}
	
	public void setQuantitaEvasa(BigDecimal quantitaEvasa) {
		this.quantitaEvasa = quantitaEvasa;
	}
	
	public java.math.BigDecimal getCoefConvEvasa() {
		return coefConvEvasa;
	}
	
	public void setCoefConvEvasa(java.math.BigDecimal coefConvEvasa) {
		this.coefConvEvasa = coefConvEvasa;
	}
	
	public UnitaMisuraBulk getUnitaMisuraEvasa() {
		return unitaMisuraEvasa;
	}
	
	public void setUnitaMisuraEvasa(UnitaMisuraBulk unitaMisuraEvasa) {
		this.unitaMisuraEvasa = unitaMisuraEvasa;
	}

	public String getOperazioneQuantitaEvasaMinore() {
		return operazioneQuantitaEvasaMinore;
	}
	
	public void setOperazioneQuantitaEvasaMinore(String operazioneQuantitaEvasaMinore) {
		this.operazioneQuantitaEvasaMinore = operazioneQuantitaEvasaMinore;
	}
	
	public java.lang.Boolean getAutorizzaQuantitaEvasaMaggioreOrdinata() {
		return autorizzaQuantitaEvasaMaggioreOrdinata;
	}
	
	public void setAutorizzaQuantitaEvasaMaggioreOrdinata(java.lang.Boolean autorizzaQuantitaEvasaMaggioreOrdinata) {
		this.autorizzaQuantitaEvasaMaggioreOrdinata = autorizzaQuantitaEvasaMaggioreOrdinata;
	}
	
	public Boolean isQuantitaEvasaDiversaOrdine() {
		return getQtConvertita() != null && getQtEvasaConvertita() != null &&
			   getQtConvertita().compareTo(getQtEvasaConvertita()) != 0;
	}
	
	public Boolean isOperazioneCreaNuovaConsegna() {
		return OrdineAcqConsegnaBulk.OPERAZIONE_CREA_NUOVA_CONSEGNA.equals(getOperazioneQuantitaEvasaMinore());
	}
	
	public Boolean isOperazioneEvasaForzata() {
		return OrdineAcqConsegnaBulk.OPERAZIONE_EVADI_FORZATA.equals(getOperazioneQuantitaEvasaMinore());
	}
	
	public Boolean isQuantitaEvasaMinoreOrdine() {
		return isQuantitaEvasaDiversaOrdine() && getQtConvertita().compareTo(getQtEvasaConvertita()) > 0;
	}
	
	public Boolean isQuantitaEvasaMaggioreOrdine() {
		return isQuantitaEvasaDiversaOrdine() && getQtConvertita().compareTo(getQtEvasaConvertita()) < 0;
	}
	
	public String getLottoFornitore() {
		return lottoFornitore;
	}
	
	public void setLottoFornitore(String lottoFornitore) {
		this.lottoFornitore = lottoFornitore;
	}
	
	public java.sql.Timestamp getDtScadenza() {
		return dtScadenza;
	}
	
	public void setDtScadenza(java.sql.Timestamp dtScadenza) {
		this.dtScadenza = dtScadenza;
	}

	public String getConsegnaOrdineString() {
		return getOrdineAcqRiga().getRigaOrdineString()
				.concat("/")
				.concat(String.valueOf(this.getConsegna()));
	}
	
	public String getDescObbligazioneScadenzario() {
		if (getObbligazioneScadenzario() == null || getObbligazioneScadenzario().getEsercizio() == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio_originale()+"-"+getObbligazioneScadenzario().getPg_obbligazione()+"-"+getObbligazioneScadenzario().getPg_obbligazione_scadenzario()+"-"+getObbligazioneScadenzario().getDs_scadenza();
	}
	
	public java.math.BigDecimal getQtConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQuantita()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getOrdineAcqRiga().getCoefConv()).orElse(BigDecimal.ZERO)));
	}

	public java.math.BigDecimal getQtEvasaConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQuantitaEvasa()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getCoefConvEvasa()).orElse(BigDecimal.ZERO)));
	}

	public boolean isROCoefConvEvasa(){
		return !Optional.ofNullable(this.getUnitaMisuraEvasa())
				.map(UnitaMisuraBulk::getCdUnitaMisura)
				.filter(cdUM->!Optional.ofNullable(this.getOrdineAcqRiga().getBeneServizio())
								.map(Bene_servizioBulk::getUnitaMisura)
								.filter(umBene->umBene.getCdUnitaMisura().equals(cdUM))
								.isPresent()
				)
				.isPresent();
	}
}