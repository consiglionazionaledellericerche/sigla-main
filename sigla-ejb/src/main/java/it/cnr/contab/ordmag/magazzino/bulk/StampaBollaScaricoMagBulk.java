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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;

import java.sql.Timestamp;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 *
 * @author: Roberto Fantino
 */
public class StampaBollaScaricoMagBulk extends AbilitazioneMagazzinoBulk {

    public static final String TUTTI = "%";
    public static final String TUTTI_ASTERISCO = "*";

    private UnitaOperativaOrdBulk unitaOperativaAbilitata = new UnitaOperativaOrdBulk();
    private UnitaOperativaOrdBulk unitaOperativaDestinazione = new UnitaOperativaOrdBulk();
    //private MagazzinoBulk magazzinoAbilitato =  new MagazzinoBulk();
    private Bene_servizioBulk daBeneServizio = new Bene_servizioBulk();
    private Bene_servizioBulk aBeneServizio = new Bene_servizioBulk();
    private java.sql.Timestamp daData;
    private java.sql.Timestamp aData;
    private String daNumBolla;
    private String aNumBolla;


    /**
     * Stampa_consumiBulk constructor comment.
     */
    public StampaBollaScaricoMagBulk() {
        super();
    }


    public UnitaOperativaOrdBulk getUnitaOperativaDestinazione() {
        return unitaOperativaDestinazione;
    }

    public void setUnitaOperativaDestinazione(UnitaOperativaOrdBulk unitaOperativaDestinazione) {
        this.unitaOperativaDestinazione = unitaOperativaDestinazione;
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

    public Timestamp getDaData() {
        return daData;
    }

    public void setDaData(Timestamp daData) {
        this.daData = daData;
    }

    public Timestamp getaData() {
        return aData;
    }

    public void setaData(Timestamp aData) {
        this.aData = aData;
    }

    public String getDaNumBolla() {
        return daNumBolla;
    }

    public void setDaNumBolla(String daNumBolla) {
        this.daNumBolla = daNumBolla;
    }

    public String getaNumBolla() {
        return aNumBolla;
    }

    public void setaNumBolla(String aNumBolla) {
        this.aNumBolla = aNumBolla;
    }

    public void validate() throws it.cnr.jada.bulk.ValidationException {
        if(getUnitaOperativaAbilitata() == null || getUnitaOperativaAbilitata().getCdUnitaOperativa() == null)
            throw new it.cnr.jada.bulk.ValidationException("Selezionare l'Unità Operativa");
        if ( getMagazzinoAbilitato()==null || getMagazzinoAbilitato().getCdMagazzino()==null)
            throw new it.cnr.jada.bulk.ValidationException("Selezionare un Magazzino");

        if((getUnitaOperativaDestinazione() == null || getUnitaOperativaDestinazione().getCdUnitaOperativa() == null)
                                                    &&
                                        !rangeArticoloImpostato()
                                                    &&
                                        !rangeDataBollaImpostato()
                                                    &&
                                        !rangeNumBollaImpostato()){
            throw new it.cnr.jada.bulk.ValidationException("Selezionare almeno un filtro tra Unità Operativa di Destinazione, Articolo, Data e Numero Bolla");
        }


    }
    private boolean rangeNumBollaImpostato() throws it.cnr.jada.bulk.ValidationException{
        if(getDaNumBolla() == null &&  getaNumBolla() == null)
            return false;

        if((getDaNumBolla() != null &&  getaNumBolla() == null)
                ||
           (getDaNumBolla() == null &&  getaNumBolla() != null)) {
            throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Numero Bolla");
        }
        if((getDaNumBolla() != null &&  getaNumBolla() != null)
                &&
           (getDaNumBolla().compareTo(getaNumBolla()) > 0)){
            throw new it.cnr.jada.bulk.ValidationException("Intervallo di Numero Bolla non corretto, il Numero Bolla Da non può essere maggiore del Numero Bolla A");
        }
        return true;
    }
    private boolean rangeDataBollaImpostato() throws it.cnr.jada.bulk.ValidationException {
        if(getDaData() == null &&  getaData() == null) {
            return false;
        }
        if((getDaData() != null &&  getaData()  == null)
                ||
                (getDaData()== null &&  getaData() != null)) {
            throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Date");
        }
        if((getDaData()  != null &&  getaData() != null)
                &&
                (getDaData().compareTo(getaData()) > 0)){
            throw new it.cnr.jada.bulk.ValidationException("Intervallo di Date non corretto, la Data Da non può essere maggiore della Data A");
        }

        return true;
    }
    private boolean rangeArticoloImpostato() throws it.cnr.jada.bulk.ValidationException{
        if(getDaBeneServizio().getCd_bene_servizio() == null &&  getaBeneServizio().getCd_bene_servizio() == null)
            return false;

        if((getDaBeneServizio().getCd_bene_servizio() != null &&  getaBeneServizio().getCd_bene_servizio() == null)
                ||
                (getDaBeneServizio().getCd_bene_servizio() == null &&  getaBeneServizio().getCd_bene_servizio() != null)) {
            throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Articolo");
        }
        if((getDaBeneServizio().getCd_bene_servizio() != null &&  getaBeneServizio().getCd_bene_servizio() != null)
                &&
                (getDaBeneServizio().getCd_bene_servizio().compareTo(getaBeneServizio().getCd_bene_servizio()) > 0)){
            throw new it.cnr.jada.bulk.ValidationException("Intervallo di Articolo non corretto, l'Articolo Da non può essere maggiore dell'Articolo A");
        }
        return true;
    }
    public String getCdsMagForPrint() {
        if (this.getMagazzinoAbilitato() == null)
            return TUTTI;
        if (this.getMagazzinoAbilitato().getCdCds() == null)
            return TUTTI;

        return this.getMagazzinoAbilitato().getCdCds();
    }
    public String getCdMagazzinoForPrint() {
        if (this.getMagazzinoAbilitato() == null)
            return TUTTI;
        if (this.getMagazzinoAbilitato().getCdMagazzino() == null)
            return TUTTI;

        return super.getMagazzinoAbilitato().getCdMagazzino();
    }

    public String getDescMagazzinoForPrint() {
        if (this.getMagazzinoAbilitato() != null && this.getMagazzinoAbilitato().getCdMagazzino() != null) {
            return getMagazzinoAbilitato().getDsMagazzino();
        }

        return "";
    }
    public String getCdUnitaOperativaAbilitataForPrint() {
        return getUnitaOperativaAbilitata().getCdUnitaOperativa();
    }
    public String getCdUnitaOperativaDestForPrint() {
        if(getUnitaOperativaDestinazione() == null)
            return TUTTI;
        if(getUnitaOperativaDestinazione().getCdUnitaOperativa() == null)
            return TUTTI;
        return getUnitaOperativaDestinazione().getCdUnitaOperativa();
    }
    public String getDescUnitaOperativaDestForPrint() {
        if(getUnitaOperativaDestinazione() == null)
            return TUTTI;
        if(getUnitaOperativaDestinazione().getCdUnitaOperativa() == null)
            return TUTTI;
        else
            return getUnitaOperativaDestinazione().getCdUnitaOperativa()+"-"+getUnitaOperativaDestinazione().getDsUnitaOperativa();

    }
    public String getCdDaBeneForPrint() {
        if (this.getDaBeneServizio() == null)
            return TUTTI;
        if (this.getDaBeneServizio().getCd_bene_servizio() == null)
            return TUTTI;

        return this.getDaBeneServizio().getCd_bene_servizio();
    }
    public String getCdABeneForPrint() {
        if (this.getaBeneServizio() == null)
            return TUTTI;
        if (this.getaBeneServizio().getCd_bene_servizio() == null)
            return TUTTI;

        return this.getaBeneServizio().getCd_bene_servizio();
    }
    public String getDescDaBeneForPrint(){
        if(this.getDaBeneServizio() != null && this.getDaBeneServizio().getCd_bene_servizio() != null && !this.getDaBeneServizio().getCd_bene_servizio().equals(TUTTI_ASTERISCO)){
            return this.getDaBeneServizio().getDs_bene_servizio();
        }
        return "";
    }
    public String getDescABeneForPrint(){
        if(this.getaBeneServizio() != null && this.getaBeneServizio().getCd_bene_servizio() != null && !this.getaBeneServizio().getCd_bene_servizio().equals(TUTTI_ASTERISCO)){
            return this.getaBeneServizio().getDs_bene_servizio();
        }
        return "";
    }








}
