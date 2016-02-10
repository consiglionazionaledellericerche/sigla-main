/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class MissioneQuotaRimborsoBase extends MissioneQuotaRimborsoKey implements Keyed {
//    DT_FINE_VALIDITA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_fine_validita;
 
//    IM_RIMBORSO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rimborso;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MISSIONE_QUOTA_RIMBORSO
	 **/
	public MissioneQuotaRimborsoBase() {
		super();
	}
	public MissioneQuotaRimborsoBase(java.lang.String cd_area_estera, java.lang.String cd_gruppo_inquadramento, java.sql.Timestamp dt_inizio_validita) {
		super(cd_area_estera, cd_gruppo_inquadramento, dt_inizio_validita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt_fine_validita]
	 **/
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt_fine_validita]
	 **/
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita)  {
		this.dt_fine_validita=dt_fine_validita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_rimborso]
	 **/
	public java.math.BigDecimal getIm_rimborso() {
		return im_rimborso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_rimborso]
	 **/
	public void setIm_rimborso(java.math.BigDecimal im_rimborso)  {
		this.im_rimborso=im_rimborso;
	}
}