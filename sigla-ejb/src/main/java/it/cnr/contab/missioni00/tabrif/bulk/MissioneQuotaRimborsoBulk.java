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
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class MissioneQuotaRimborsoBulk extends MissioneQuotaRimborsoBase {

	private RifAreePaesiEsteriBulk rifAreePaesiEsteri;

	private Gruppo_inquadramentoBulk gruppoInquadramento;
	
	private java.util.Collection gruppiInquadramento;
	
	private java.util.Collection areePaesiEsteri;
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MISSIONE_QUOTA_RIMBORSO
	 **/
	public MissioneQuotaRimborsoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MISSIONE_QUOTA_RIMBORSO
	 **/
	public MissioneQuotaRimborsoBulk(java.lang.String cd_area_estera, java.lang.String cd_gruppo_inquadramento, java.sql.Timestamp dt_inizio_validita) {
		super(cd_area_estera, cd_gruppo_inquadramento, dt_inizio_validita);
		setRifAreePaesiEsteri( new RifAreePaesiEsteriBulk(cd_area_estera) );
		setGruppoInquadramento( new Gruppo_inquadramentoBulk(cd_gruppo_inquadramento) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Classificazione delle Aree dei paesi esteri.]
	 **/
	public RifAreePaesiEsteriBulk getRifAreePaesiEsteri() {
		return rifAreePaesiEsteri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Classificazione delle Aree dei paesi esteri.]
	 **/
	public void setRifAreePaesiEsteri(RifAreePaesiEsteriBulk rifAreePaesiEsteri)  {
		this.rifAreePaesiEsteri=rifAreePaesiEsteri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce i gruppi di inquadramento ministeriali per le diarie estere; gruppi da 1 a 6.
Ogni profilo di inquadramento del CNR, sia quelli dipendente che quelli riferiti a soggetti esterni, deve essere qualificato con il gruppo ministeriale di inquadramento corrispondente per il recupero della diaria.
Era definito anche un valore convenzionale (*)  per gestire il caso in cui tale valorizzazione sia indifferente alla gestione. Tale gestione è ora inutile per le modifiche fatte alla gestione missioni.
L'associazione per il recupero di spese ed abbattimenti è ora fatta in base al profilo di inquadramento e non al gruppo ministeriale.]
	 **/
	public Gruppo_inquadramentoBulk getGruppoInquadramento() {
		return gruppoInquadramento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce i gruppi di inquadramento ministeriali per le diarie estere; gruppi da 1 a 6.
Ogni profilo di inquadramento del CNR, sia quelli dipendente che quelli riferiti a soggetti esterni, deve essere qualificato con il gruppo ministeriale di inquadramento corrispondente per il recupero della diaria.
Era definito anche un valore convenzionale (*)  per gestire il caso in cui tale valorizzazione sia indifferente alla gestione. Tale gestione è ora inutile per le modifiche fatte alla gestione missioni.
L'associazione per il recupero di spese ed abbattimenti è ora fatta in base al profilo di inquadramento e non al gruppo ministeriale.]
	 **/
	public void setGruppoInquadramento(Gruppo_inquadramentoBulk gruppoInquadramento)  {
		this.gruppoInquadramento=gruppoInquadramento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_area_estera]
	 **/
	public java.lang.String getCd_area_estera() {
		RifAreePaesiEsteriBulk rifAreePaesiEsteri = this.getRifAreePaesiEsteri();
		if (rifAreePaesiEsteri == null)
			return null;
		return getRifAreePaesiEsteri().getCd_area_estera();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_area_estera]
	 **/
	public void setCd_area_estera(java.lang.String cd_area_estera)  {
		this.getRifAreePaesiEsteri().setCd_area_estera(cd_area_estera);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_gruppo_inquadramento]
	 **/
	public java.lang.String getCd_gruppo_inquadramento() {
		Gruppo_inquadramentoBulk gruppoInquadramento = this.getGruppoInquadramento();
		if (gruppoInquadramento == null)
			return null;
		return getGruppoInquadramento().getCd_gruppo_inquadramento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_gruppo_inquadramento]
	 **/
	public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento)  {
		this.getGruppoInquadramento().setCd_gruppo_inquadramento(cd_gruppo_inquadramento);
	}
	public java.util.Collection getGruppiInquadramento() {
		return gruppiInquadramento;
	}
	public void setGruppiInquadramento(java.util.Collection newColl) {
		gruppiInquadramento = newColl;
	}
	public java.util.Collection getAreePaesiEsteri() {
		return areePaesiEsteri;
	}
	public void setAreePaesiEsteri(java.util.Collection areePaesiEsteri) {
		this.areePaesiEsteri = areePaesiEsteri;
	}
	public java.sql.Timestamp getDataFineValidita() {
		
		if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
			return null;
		return getDt_fine_validita();
	}
	public void setDataFineValidita(java.sql.Timestamp newDate) {
		
		this.setDt_fine_validita(newDate);
	}	
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		
		super.initializeForInsert(bp,context);
		resetImporti();
		setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);

		return this;	
	}
	private void resetImporti() {

		setIm_rimborso(new java.math.BigDecimal(0));
	}
	public void validate() throws ValidationException {

		// controllo su campo INQUADRAMENTO
		if (getCd_gruppo_inquadramento()==null)
			throw new ValidationException( "Il campo INQUADRAMENTO non può essere vuoto" );

		// controllo su campo DIVISA
		if (getCd_area_estera()==null)
			throw new ValidationException( "Il campo AREA ESTERA non può essere vuoto" );

		// controllo su campo INIZIO VALIDITA
		if (getDt_inizio_validita()==null)
			throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto" );

		// controllo su campo IMPORTO DIARIA
		if (getIm_rimborso()==null)
			throw new ValidationException( "Il campo IMPORTO RIMBORSO non può essere vuoto" );
		if ( getIm_rimborso().compareTo(new java.math.BigDecimal(0))<=0 )
			throw new ValidationException( "Il campo IMPORTO RIMBORSO deve essere maggiore di 0 !" );
	}
}