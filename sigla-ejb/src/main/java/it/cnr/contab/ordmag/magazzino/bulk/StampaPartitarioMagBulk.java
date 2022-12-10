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

package it.cnr.contab.ordmag.magazzino.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;

import java.sql.Timestamp;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 *
 * @author: Roberto Fantino
 */
public class StampaPartitarioMagBulk extends AbilitazioneMagazzinoBulk {

    Timestamp daDataMovimento;

    Timestamp aDataMovimento;

    Timestamp daDataCompetenza;

    Timestamp aDataCompetenza;

    private MagazzinoBulk magazzinoAbilitato =  new MagazzinoBulk();

    private UnitaOperativaOrdBulk unitaOperativaAbilitata = new UnitaOperativaOrdBulk();

    Bene_servizioBulk daBeneServizio;

    Bene_servizioBulk aBeneServizio;

    TerzoBulk daFornitore;

    TerzoBulk aFornitore;

    OrdineAcqBulk daOrdine;

    OrdineAcqBulk aOrdine;

    String daLottoFornitore;

    String aLottoFornitore;

    String daBollaFornitore;

    String aBollaFornitore;

    public static final String TUTTI = "%";
    public static final String MOVIMENTI_CARICO="C";
    public static final String MOVIMENTI_SCARICO="S";
    public static final String MOVIMENTI_SCARICO_CARICO="E";
    public final static java.util.Dictionary <String,String> TIPO_MOVIMENTO;
    static {
        TIPO_MOVIMENTO = new it.cnr.jada.util.OrderedHashtable();
        TIPO_MOVIMENTO.put(MOVIMENTI_CARICO, "Carcico");
        TIPO_MOVIMENTO.put(MOVIMENTI_SCARICO, "Scarico");
        TIPO_MOVIMENTO.put(MOVIMENTI_SCARICO_CARICO, "Entrambi");

    }


    /**
     * StampaPartitarioMagBulk constructor comment.
     */
    public StampaPartitarioMagBulk() {
        super();
    }

    public Timestamp getDaDataMovimento() {
        return daDataMovimento;
    }

    public void setDaDataMovimento(Timestamp daDataMovimento) {
        this.daDataMovimento = daDataMovimento;
    }

    public Timestamp getaDataMovimento() {
        return aDataMovimento;
    }

    public void setaDataMovimento(Timestamp aDataMovimento) {
        this.aDataMovimento = aDataMovimento;
    }

    public Timestamp getDaDataCompetenza() {
        return daDataCompetenza;
    }

    public void setDaDataCompetenza(Timestamp daDataCompetenza) {
        this.daDataCompetenza = daDataCompetenza;
    }

    public Timestamp getaDataCompetenza() {
        return aDataCompetenza;
    }

    public void setaDataCompetenza(Timestamp aDataCompetenza) {
        this.aDataCompetenza = aDataCompetenza;
    }

    @Override
    public MagazzinoBulk getMagazzinoAbilitato() {
        return magazzinoAbilitato;
    }

    @Override
    public void setMagazzinoAbilitato(MagazzinoBulk magazzinoAbilitato) {
        this.magazzinoAbilitato = magazzinoAbilitato;
    }

    @Override
    public UnitaOperativaOrdBulk getUnitaOperativaAbilitata() {
        return unitaOperativaAbilitata;
    }

    @Override
    public void setUnitaOperativaAbilitata(UnitaOperativaOrdBulk unitaOperativaAbilitata) {
        this.unitaOperativaAbilitata = unitaOperativaAbilitata;
    }

    public Bene_servizioBulk getDaBeneServizio() {
        return daBeneServizio;
    }

    public void setDaBeneServizio(Bene_servizioBulk daBeneServizio) {
        this.daBeneServizio = daBeneServizio;
    }

    public Bene_servizioBulk getaBeneServizio() {
        return aBeneServizio;
    }

    public void setaBeneServizio(Bene_servizioBulk aBeneServizio) {
        this.aBeneServizio = aBeneServizio;
    }

    public String getDaLottoFornitore() {
        return daLottoFornitore;
    }

    public void setDaLottoFornitore(String daLottoFornitore) {
        this.daLottoFornitore = daLottoFornitore;
    }

    public String getaLottoFornitore() {
        return aLottoFornitore;
    }

    public void setaLottoFornitore(String aLottoFornitore) {
        this.aLottoFornitore = aLottoFornitore;
    }

    public String getDaBollaFornitore() {
        return daBollaFornitore;
    }

    public void setDaBollaFornitore(String daBollaFornitore) {
        this.daBollaFornitore = daBollaFornitore;
    }

    public String getaBollaFornitore() {
        return aBollaFornitore;
    }

    public void setaBollaFornitore(String aBollaFornitore) {
        this.aBollaFornitore = aBollaFornitore;
    }

    public TerzoBulk getDaFornitore() {
        return daFornitore;
    }

    public void setDaFornitore(TerzoBulk daFornitore) {
        this.daFornitore = daFornitore;
    }

    public TerzoBulk getaFornitore() {
        return aFornitore;
    }

    public void setaFornitore(TerzoBulk aFornitore) {
        this.aFornitore = aFornitore;
    }

    public OrdineAcqBulk getDaOrdine() {
        return daOrdine;
    }

    public void setDaOrdine(OrdineAcqBulk daOrdine) {
        this.daOrdine = daOrdine;
    }

    public OrdineAcqBulk getaOrdine() {
        return aOrdine;
    }

    public void setaOrdine(OrdineAcqBulk aOrdine) {
        this.aOrdine = aOrdine;
    }

    public String getCdMagazzinoForPrint() {
        return getMagazzinoAbilitato().getCdMagazzino();
    }

    public String getUopForPrint() {
        if (unitaOperativaAbilitata!=null)
            return unitaOperativaAbilitata.getCdUnitaOperativa();
        return null;
    }

    public Integer getDaCdFornitoreForPrint() {
        if (daFornitore!=null)
            return daFornitore.getCd_terzo();
        return null;
    }

    public Integer getaCdFornitoreForPrint() {
        if (aFornitore!=null)
            return aFornitore.getCd_terzo();
        return null;
    }

    public String getDaCdBeneServizioForPrint() {
        if (daBeneServizio!=null)
            return daBeneServizio.getCd_bene_servizio();
        return null;
    }

    public String getaCdBeneServizioForPrint() {
        if (aBeneServizio!=null)
            return aBeneServizio.getCd_bene_servizio();
        return null;
    }
}
