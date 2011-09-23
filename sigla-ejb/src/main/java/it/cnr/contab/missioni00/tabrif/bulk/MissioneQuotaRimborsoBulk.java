/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class MissioneQuotaRimborsoBulk extends MissioneQuotaRimborsoBase {
	/**
	 * [RIF_AREE_PAESI_ESTERI Classificazione delle Aree dei paesi esteri.]
	 **/
	private RifAreePaesiEsteriBulk rifAreePaesiEsteri =  new RifAreePaesiEsteriBulk();
	/**
	 * [GRUPPO_INQUADRAMENTO Definisce i gruppi di inquadramento ministeriali per le diarie estere; gruppi da 1 a 6.
Ogni profilo di inquadramento del CNR, sia quelli dipendente che quelli riferiti a soggetti esterni, deve essere qualificato con il gruppo ministeriale di inquadramento corrispondente per il recupero della diaria.
Era definito anche un valore convenzionale (*)  per gestire il caso in cui tale valorizzazione sia indifferente alla gestione. Tale gestione è ora inutile per le modifiche fatte alla gestione missioni.
L'associazione per il recupero di spese ed abbattimenti è ora fatta in base al profilo di inquadramento e non al gruppo ministeriale.]
	 **/
	private Gruppo_inquadramentoBulk gruppoInquadramento =  new Gruppo_inquadramentoBulk();
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
}