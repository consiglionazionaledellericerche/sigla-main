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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/09/2011
 */
package it.cnr.contab.prevent00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class VLimiteSpesaPdgpBulk extends OggettoBulk implements Persistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_PDGP
	 **/
	public VLimiteSpesaPdgpBulk() {
		super();
	}
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_CDS VARCHAR(4000)
	private java.lang.String cdCds;
 
//    FONTE VARCHAR(3) NOT NULL
	private java.lang.String fonte;
 
//    IMPORTO_LIMITE DECIMAL(22,0)
	private java.math.BigDecimal importoLimite;
 
//    IMPORTO_PREVISTO DECIMAL(22,0)
	private java.math.BigDecimal importoPrevisto;
 
//    IMPORTO_DISPONIBILE DECIMAL(22,0)
	private java.math.BigDecimal importoDisponibile;
 
//    CD_CLASSIFICAZIONE VARCHAR(34)
	private java.lang.String cdClassificazione;
 
//    ID_CLASSIFICAZIONE DECIMAL(7,0) NOT NULL
	private java.lang.Integer idClassificazione;
 
//    CD_AREA VARCHAR(30) NOT NULL
	private java.lang.String cdArea;
 
	public static final java.util.Dictionary fonteKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String FONTE_INTERNA = "FIN";
	final public static String FONTE_ESTERNA = "FES";
	
	static {
		fonteKeys.put(FONTE_INTERNA,"Interna");
		fonteKeys.put(FONTE_ESTERNA,"Esterna");
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fonte]
	 **/
	public java.lang.String getFonte() {
		return fonte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fonte]
	 **/
	public void setFonte(java.lang.String fonte)  {
		this.fonte=fonte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLimite]
	 **/
	public java.math.BigDecimal getImportoLimite() {
		return importoLimite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLimite]
	 **/
	public void setImportoLimite(java.math.BigDecimal importoLimite)  {
		this.importoLimite=importoLimite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoPrevisto]
	 **/
	public java.math.BigDecimal getImportoPrevisto() {
		return importoPrevisto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoPrevisto]
	 **/
	public void setImportoPrevisto(java.math.BigDecimal importoPrevisto)  {
		this.importoPrevisto=importoPrevisto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoDisponibile]
	 **/
	public java.math.BigDecimal getImportoDisponibile() {
		return importoDisponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoDisponibile]
	 **/
	public void setImportoDisponibile(java.math.BigDecimal importoDisponibile)  {
		this.importoDisponibile=importoDisponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdClassificazione]
	 **/
	public java.lang.String getCdClassificazione() {
		return cdClassificazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdClassificazione]
	 **/
	public void setCdClassificazione(java.lang.String cdClassificazione)  {
		this.cdClassificazione=cdClassificazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idClassificazione]
	 **/
	public java.lang.Integer getIdClassificazione() {
		return idClassificazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idClassificazione]
	 **/
	public void setIdClassificazione(java.lang.Integer idClassificazione)  {
		this.idClassificazione=idClassificazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdArea]
	 **/
	public java.lang.String getCdArea() {
		return cdArea;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdArea]
	 **/
	public void setCdArea(java.lang.String cdArea)  {
		this.cdArea=cdArea;
	}
}