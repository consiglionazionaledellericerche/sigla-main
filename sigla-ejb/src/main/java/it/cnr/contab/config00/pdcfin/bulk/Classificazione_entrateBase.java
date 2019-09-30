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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.persistency.Keyed;

/**
 * @author aimprota
 *
 */
public class Classificazione_entrateBase extends Classificazione_entrateKey implements Keyed
{
	private java.lang.String descrizione;
	private java.lang.Integer esercizio_padre;
	private java.lang.String codice_cla_e_padre;
	private java.lang.Boolean fl_mastrino;


    /**
     * 
     */
    public Classificazione_entrateBase()
    {
        super();
    }

    /**
     * @param esercizio
     * @param codice_cla_e
     */
    public Classificazione_entrateBase(Integer esercizio, String codice_cla_e)
    {
        super(esercizio, codice_cla_e);
    }

    /**
     * @return
     */
    public java.lang.String getCodice_cla_e_padre()
    {
        return codice_cla_e_padre;
    }

    /**
     * @return
     */
    public java.lang.String getDescrizione()
    {
        return descrizione;
    }

    /**
     * @return
     */
    public java.lang.Integer getEsercizio_padre()
    {
        return esercizio_padre;
    }



    /**
     * @param string
     */
    public void setCodice_cla_e_padre(java.lang.String string)
    {
        this.codice_cla_e_padre = string;
    }

    /**
     * @param string
     */
    public void setDescrizione(java.lang.String string)
    {
        descrizione = string;
    }

    /**
     * @param integer
     */
    public void setEsercizio_padre(java.lang.Integer integer)
    {
		this.esercizio_padre = integer;
    }

    /**
     * @return
    */ 
    public java.lang.Boolean getFl_mastrino()
    {
        return fl_mastrino;
    }

    /**
     * @param boolean1
    */ 
    public void setFl_mastrino(java.lang.Boolean boolean1)
    {
        fl_mastrino = boolean1;
    }







}
