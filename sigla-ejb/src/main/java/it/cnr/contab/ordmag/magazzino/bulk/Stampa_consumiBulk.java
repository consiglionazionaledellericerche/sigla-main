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

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Timestamp;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 *
 * @author: Roberto Fantino
 */
public class Stampa_consumiBulk extends AbilitazioneMagazzinoBulk {

    public static final String TUTTI = "%";
    public static final String TUTTI_ASTERISCO = "*";

    private java.sql.Timestamp daDataMovimento;
    private java.sql.Timestamp aDataMovimento;

    private java.sql.Timestamp dataRiferimento;

    private UnitaOperativaOrdBulk daUnitaOperativa = new UnitaOperativaOrdBulk();
    private UnitaOperativaOrdBulk aUnitaOperativa = new UnitaOperativaOrdBulk();

    private Categoria_gruppo_inventBulk daCatgrp;
    private Categoria_gruppo_inventBulk aCatgrp;

    private Bene_servizioBulk daBeneServizio = new Bene_servizioBulk();
    private Bene_servizioBulk aBeneServizio = new Bene_servizioBulk();



    /**
     * Stampa_consumiBulk constructor comment.
     */
    public Stampa_consumiBulk() {
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

    public UnitaOperativaOrdBulk getDaUnitaOperativa() {
        return daUnitaOperativa;
    }

    public void setDaUnitaOperativa(UnitaOperativaOrdBulk daUnitaOperativa) {
        this.daUnitaOperativa = daUnitaOperativa;
    }

    public UnitaOperativaOrdBulk getaUnitaOperativa() {
        return aUnitaOperativa;
    }

    public void setaUnitaOperativa(UnitaOperativaOrdBulk aUnitaOperativa) {
        this.aUnitaOperativa = aUnitaOperativa;
    }

    public Categoria_gruppo_inventBulk getDaCatgrp() {
        return daCatgrp;
    }

    public void setDaCatgrp(Categoria_gruppo_inventBulk daCatgrp) {
        this.daCatgrp = daCatgrp;
    }

    public Categoria_gruppo_inventBulk getaCatgrp() {
        return aCatgrp;
    }

    public void setaCatgrp(Categoria_gruppo_inventBulk aCatgrp) {
        this.aCatgrp = aCatgrp;
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


    public Timestamp getDataRiferimento() {
        return dataRiferimento;
    }

    public void setDataRiferimento(Timestamp dataRiferimento) {
        this.dataRiferimento = dataRiferimento;
    }




    public void validate() throws it.cnr.jada.bulk.ValidationException {
        if(getDaDataMovimento() == null || getaDataMovimento() == null){
            throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Data Movimento");
        }
        else{
            if(getDaDataMovimento().compareTo(getaDataMovimento()) > 0){
                throw new it.cnr.jada.bulk.ValidationException("Intervallo di Data Movimento non corretto, la data Da non può essere maggiore della data A");
            }
        }
        if(getDataRiferimento() == null){
            throw new it.cnr.jada.bulk.ValidationException("Selezionare Data Riferimento");
        }
        if(getMagazzinoAbilitato().getCdMagazzino() == null){
             if(getaUnitaOperativa().getCdUnitaOperativa() == null && getDaUnitaOperativa().getCdUnitaOperativa() == null) {
                 throw new it.cnr.jada.bulk.ValidationException("Seleziona il Magazzino o l'Unità Operativa");
             }
             else{
                 if(getDaUnitaOperativa().getCdUnitaOperativa() == null || getaUnitaOperativa().getCdUnitaOperativa() == null) {
                     throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Unità Operative");
                 }
                 else{
                     if(getDaUnitaOperativa().getCdUnitaOperativa().compareTo(getaUnitaOperativa().getCdUnitaOperativa()) > 0){
                         throw new it.cnr.jada.bulk.ValidationException("Intervallo di Unità Operative non corretto, la UOp Da non può essere maggiore della UOp A");
                     }
                 }
             }
        }
        if(getDaCatgrp() != null || getaCatgrp()!=null) {
            if ((getDaCatgrp().getCd_categoria_gruppo() != null && getaCatgrp().getCd_categoria_gruppo() == null)
                    ||
                    (getDaCatgrp().getCd_categoria_gruppo() == null && getaCatgrp().getCd_categoria_gruppo() != null)) {
                throw new it.cnr.jada.bulk.ValidationException("Selezionare intervallo Categoria Gruppo");
            }
            if ((getDaCatgrp().getCd_categoria_gruppo() != null && getaCatgrp().getCd_categoria_gruppo() != null)
                    &&
                    (getDaCatgrp().getCd_categoria_gruppo().compareTo(getaCatgrp().getCd_categoria_gruppo()) > 0)) {
                throw new it.cnr.jada.bulk.ValidationException("Intervallo di Categoria Gruppo non corretto, la Categoria Gruppo Da non può essere maggiore della Categoria Gruppo A");
            }
        }

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

    }

    public String getCdDaUnitaOperativaForPrint() {
        if(getDaUnitaOperativa() == null)
            return TUTTI;
        if(getDaUnitaOperativa().getCdUnitaOperativa() == null)
            return TUTTI;
        return getDaUnitaOperativa().getCdUnitaOperativa();
    }
    public String getCdAUnitaOperativaForPrint() {
        if(getaUnitaOperativa() == null)
            return TUTTI;
        if(getaUnitaOperativa().getCdUnitaOperativa() == null)
            return TUTTI;
        return getaUnitaOperativa().getCdUnitaOperativa();
    }

    public String getDescDaUnitaOperativaForPrint() {
        if(!getCdDaUnitaOperativaForPrint().equals(TUTTI))
            return getDaUnitaOperativa().getCdUnitaOperativa()+"-"+getDaUnitaOperativa().getDsUnitaOperativa();
        return "";
    }
    public String getDescAUnitaOperativaForPrint() {
        if(!getCdAUnitaOperativaForPrint().equals(TUTTI))
            return getaUnitaOperativa().getCdUnitaOperativa()+"-"+getaUnitaOperativa().getDsUnitaOperativa();
        return "";
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


    public String getCdDaCatGrpForPrint() {
        if (this.getDaCatgrp() == null)
            return TUTTI;
        if (this.getDaCatgrp().getCd_categoria_gruppo() == null)
            return TUTTI;

        return this.getDaCatgrp().getCd_categoria_gruppo();
    }
    public String getCdACatGrpForPrint() {
        if (this.getaCatgrp() == null)
            return TUTTI;
        if (this.getDaCatgrp().getCd_categoria_gruppo() == null)
            return TUTTI;

        return this.getaCatgrp().getCd_categoria_gruppo();
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
    public String getDescCatGruppoDaForPrint(){
        if (this.getDaCatgrp() != null && this.getDaCatgrp().getCd_categoria_gruppo()!= null && !this.getDaCatgrp().getCd_categoria_gruppo().equals(TUTTI_ASTERISCO)){
            String descCategoriaPadre = this.getDaCatgrp().getNodoPadre().getDs_categoria_gruppo();
            String descCategoriaGruppo = this.getDaCatgrp().getDs_categoria_gruppo();
            return descCategoriaPadre+" - "+descCategoriaGruppo;
        }
        return "";
    }
    public String getDescCatGruppoAForPrint(){
        if (this.getaCatgrp() != null && this.getaCatgrp().getCd_categoria_gruppo()!= null && !this.getaCatgrp().getCd_categoria_gruppo().equals(TUTTI_ASTERISCO)){
            String descCategoriaPadre = this.getaCatgrp().getNodoPadre().getDs_categoria_gruppo();
            String descCategoriaGruppo = this.getaCatgrp().getDs_categoria_gruppo();
            return descCategoriaPadre+" - "+descCategoriaGruppo;
        }
        return "";
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
