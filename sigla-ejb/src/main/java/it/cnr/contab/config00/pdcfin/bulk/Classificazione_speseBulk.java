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

import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author aimprota
 *
 */
public class Classificazione_speseBulk extends Classificazione_speseBase
{
	private Classificazione_speseBulk classificazione_spese; 
    /**
     * 
     */
    public Classificazione_speseBulk()
    {
        super();		
    }

    /**
     * @param esercizio
     * @param codice_cla_e
     */
    public Classificazione_speseBulk(Integer esercizio, String codice_cla_s)
    {
        super(esercizio, codice_cla_s);
    }




	/**
	 * @param string
	 */
	public void setCodice_cla_s_padre(java.lang.String codice_cla_s_padre)
	{
		
		this.getClassificazione_spese().setCodice_cla_s(codice_cla_s_padre);
	}
	/**
	 * @param integer
	 */
	public void setEsercizio_padre(java.lang.Integer esercizio_padre)
	{		
		this.getClassificazione_spese().setEsercizio(esercizio_padre);
	}
	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_padre()
	{
		if(getClassificazione_spese() == null)
		  return null;
		return this.getClassificazione_spese().getEsercizio_padre();
	}
	
	/**
	 * @return
	 */
	public java.lang.String getCodice_cla_s_padre()
	{
		if(getClassificazione_spese() == null)
		  return null;
		return this.getClassificazione_spese().getCodice_cla_s_padre();
	}	
	
	 public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initialize(bp, context);
		setEsercizio( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );	
	    return this;
	}
	
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForInsert(bp,context);
		setFl_mastrino(Boolean.FALSE);
	  return this;
	}
	
	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForFreeSearch(bp,context);
		setClassificazione_spese(new Classificazione_speseBulk());
	  return this;
	}
	

	public boolean isROClassificazione_spese()
	{
		return getClassificazione_spese()==null||
		       getClassificazione_spese().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}
	
    /**
     * @return
     */
    public Classificazione_speseBulk getClassificazione_spese()
    {
        return classificazione_spese;
    }

    /**
     * @param bulk
     */
    public void setClassificazione_spese(Classificazione_speseBulk bulk)
    {
        classificazione_spese = bulk;
    }

}
