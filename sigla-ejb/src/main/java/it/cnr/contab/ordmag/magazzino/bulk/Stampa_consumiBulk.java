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

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;

import java.sql.Timestamp;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 *
 * @author: Roberto Fantino
 */
public class Stampa_consumiBulk extends AbilitazioneMagazzinoBulk {
    private java.sql.Timestamp dataInventario;

    private Categoria_gruppo_inventBulk catgrp;

    private java.lang.Boolean flDettaglioArticolo = Boolean.TRUE;

    private java.lang.Boolean flRaggCatGruppo = Boolean.TRUE;

    private String ordinamento;

    public static final String ORD_CODICE="C";
    public static final String ORD_DENOMINAZIONE="D";
    public final static java.util.Dictionary <String,String> TIPO_ORDINAMENTO;
    static {
        TIPO_ORDINAMENTO = new it.cnr.jada.util.OrderedHashtable();
        TIPO_ORDINAMENTO.put(ORD_CODICE, "Codice");
        TIPO_ORDINAMENTO.put(ORD_DENOMINAZIONE, "Denominazione");

    }

    public Categoria_gruppo_inventBulk getCatgrp() {
        return catgrp;
    }

    public void setCatgrp(Categoria_gruppo_inventBulk catgrp) {
        this.catgrp = catgrp;
    }

    /**
     * Stampa_consumiBulk constructor comment.
     */
    public Stampa_consumiBulk() {
        super();
    }

    public Timestamp getDataInventario() {
        return dataInventario;
    }

    public void setDataInventario(Timestamp dataInventario) {
        this.dataInventario = dataInventario;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/01/2003 16.22.06)
     *
     * @return java.sql.Timestamp
     */

    public String getCdsMagForPrint() {
        if (this.getMagazzinoAbilitato() == null)
            return "%";
        if (this.getMagazzinoAbilitato().getCdCds() == null)
            return "%";

        return this.getMagazzinoAbilitato().getCdCds();
    }
    public String getCdMagazzinoForPrint() {
        if (this.getMagazzinoAbilitato() == null)
            return "%";
        if (this.getMagazzinoAbilitato().getCdMagazzino() == null)
            return "%";

        return super.getMagazzinoAbilitato().getCdMagazzino();
    }
    public String getCdCatGrpForPrint() {
        if (this.getCatgrp() == null)
            return "%";
        if (this.getCatgrp().getCd_categoria_gruppo() == null)
            return "%";

        return this.getCatgrp().getCd_categoria_gruppo();
    }

    public Boolean getFlDettaglioArticolo() {
        return flDettaglioArticolo;
    }

    public void setFlDettaglioArticolo(Boolean flDettaglioArticolo) {
        this.flDettaglioArticolo = flDettaglioArticolo;
    }

    public Boolean getFlRaggCatGruppo() {
        return flRaggCatGruppo;
    }

    public void setFlRaggCatGruppo(Boolean flRaggCatGruppo) {
        this.flRaggCatGruppo = flRaggCatGruppo;
    }

    public String getOrdinamento() {
        return ordinamento;
    }

    public void setOrdinamento(String ordinamento) {
        this.ordinamento = ordinamento;
    }

    public void validate() throws it.cnr.jada.bulk.ValidationException {
        if ( getMagazzinoAbilitato()==null ||
                getMagazzinoAbilitato().getCdMagazzino()==null
        )
            throw new it.cnr.jada.bulk.ValidationException("Selezionare un magazzino!");
        if ( getDataInventario()==null )
            throw new it.cnr.jada.bulk.ValidationException("Imposta la data inventario!");
        if ( getOrdinamento()==null || getOrdinamento().trim().isEmpty())
            throw new it.cnr.jada.bulk.ValidationException("Selezionare l'ordinamento!");

    }
}
