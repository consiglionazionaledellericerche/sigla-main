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
public class Classificazione_entrateBulk extends Classificazione_entrateBase
{
	private Classificazione_entrateBulk classificazione_entrate; 
    /**
     * 
     */
    public Classificazione_entrateBulk()
    {
        super();		
    }

    /**
     * @param esercizio
     * @param codice_cla_e
     */
    public Classificazione_entrateBulk(Integer esercizio, String codice_cla_e)
    {
        super(esercizio, codice_cla_e);
    }

    /**
     * @return
     */
    public Classificazione_entrateBulk getClassificazione_entrate()
    {
        return classificazione_entrate;
    }

    /**
     * @param bulk
    */ 
    public void setClassificazione_entrate(Classificazione_entrateBulk classificazione)
    {
	
    	classificazione_entrate = classificazione;

    }
	/**
	 * @param string
	 */
	public void setCodice_cla_e_padre(java.lang.String codice_cla_e_padre)
	{
		
		this.getClassificazione_entrate().setCodice_cla_e(codice_cla_e_padre);
	}
	/**
	 * @param integer
	 */
	public void setEsercizio_padre(java.lang.Integer esercizio_padre)
	{		
		this.getClassificazione_entrate().setEsercizio(esercizio_padre);
	}
	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_padre()
	{
		if(getClassificazione_entrate() == null)
		  return null;
		return this.getClassificazione_entrate().getEsercizio_padre();
	}
	
	/**
	 * @return
	 */
	public java.lang.String getCodice_cla_e_padre()
	{
		if(getClassificazione_entrate() == null)
		  return null;
		return this.getClassificazione_entrate().getCodice_cla_e_padre();
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
		setClassificazione_entrate(new Classificazione_entrateBulk());
	  return this;
	}
	
/*	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForSearch(bp,context);
		setClassificazione_entrate(new Classificazione_entrateBulk());
		//setEsercizio_padre( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );
	  return this;
	}*/
	
	
	public boolean isROClassificazione_entrate()
	{
		return getClassificazione_entrate()==null||
		       getClassificazione_entrate().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}
	
}
