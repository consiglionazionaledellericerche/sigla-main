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
 * Date 28/02/2020
 */
package it.cnr.contab.util00.bulk;
import it.cnr.jada.persistency.Keyed;
public class HelpBase extends HelpKey implements Keyed {
//    PAGE VARCHAR(2000)
	private java.lang.String page;
 
//    HELP_URL VARCHAR(2000)
	private java.lang.String helpUrl;
 
//    DS_PAGE VARCHAR(200)
	private java.lang.String dsPage;
 
//    FLAG_MAN VARCHAR(1)
	private java.lang.String flagMan;
 
//    BP_NAME VARCHAR(50)
	private java.lang.String bpName;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: HELP_LKT
	 **/
	public HelpBase() {
		super();
	}
	public HelpBase(java.lang.Integer id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Posizione fisica relativa della pagina jsp associata ad una delle form principali con help]
	 **/
	public java.lang.String getPage() {
		return page;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Posizione fisica relativa della pagina jsp associata ad una delle form principali con help]
	 **/
	public void setPage(java.lang.String page)  {
		this.page=page;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [URL della pagina di help da mostrare relativamente alla form specificata in campo page]
	 **/
	public java.lang.String getHelpUrl() {
		return helpUrl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [URL della pagina di help da mostrare relativamente alla form specificata in campo page]
	 **/
	public void setHelpUrl(java.lang.String helpUrl)  {
		this.helpUrl=helpUrl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrizione della form su cui agganciare help (serve per guidare nell'inserimento dei riferimenti chi popola questa tabella)]
	 **/
	public java.lang.String getDsPage() {
		return dsPage;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrizione della form su cui agganciare help (serve per guidare nell'inserimento dei riferimenti chi popola questa tabella)]
	 **/
	public void setDsPage(java.lang.String dsPage)  {
		this.dsPage=dsPage;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Flag per leggere l'Help dal manuale on line]
	 **/
	public java.lang.String getFlagMan() {
		return flagMan;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Flag per leggere l'Help dal manuale on line]
	 **/
	public void setFlagMan(java.lang.String flagMan)  {
		this.flagMan=flagMan;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Nome del Business process]
	 **/
	public java.lang.String getBpName() {
		return bpName;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Nome del Business process]
	 **/
	public void setBpName(java.lang.String bpName)  {
		this.bpName=bpName;
	}
}