/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/03/2009
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Cnr_estrazione_coriBase extends Cnr_estrazione_coriKey implements Keyed {
//    DT_INIZIO TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_inizio;
 
//    DT_FINE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_fine;
 
//    IM_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_lordo;
 
//    IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile;
 
//    IM_RITENUTA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ritenuta;
 
//    DT_SCARICO_VERSO_STIPENDI TIMESTAMP(7)
	private java.sql.Timestamp dt_scarico_verso_stipendi;
 
//    DT_INIZIO_COMPENSO TIMESTAMP(7)
	private java.sql.Timestamp dt_inizio_compenso;
 
//    DT_FINE_COMPENSO TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_compenso;
 
	public Cnr_estrazione_coriBase() {
		super();
	}
	public Cnr_estrazione_coriBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.String cd_unita_organizzativa, java.lang.Integer pg_liquidazione, java.lang.Integer matricola, java.lang.String codice_fiscale, java.lang.String ti_pagamento, java.lang.Integer esercizio_compenso, java.lang.String cd_imponibile, java.lang.String ti_ente_percipiente, java.lang.String cd_contributo_ritenuta) {
		super(cd_cds, esercizio, cd_unita_organizzativa, pg_liquidazione, matricola, codice_fiscale, ti_pagamento, esercizio_compenso, cd_imponibile, ti_ente_percipiente, cd_contributo_ritenuta);
	}
	public java.sql.Timestamp getDt_inizio() {
		return dt_inizio;
	}
	public void setDt_inizio(java.sql.Timestamp dt_inizio)  {
		this.dt_inizio=dt_inizio;
	}
	public java.sql.Timestamp getDt_fine() {
		return dt_fine;
	}
	public void setDt_fine(java.sql.Timestamp dt_fine)  {
		this.dt_fine=dt_fine;
	}
	public java.math.BigDecimal getIm_lordo() {
		return im_lordo;
	}
	public void setIm_lordo(java.math.BigDecimal im_lordo)  {
		this.im_lordo=im_lordo;
	}
	public java.math.BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(java.math.BigDecimal imponibile)  {
		this.imponibile=imponibile;
	}
	public java.math.BigDecimal getIm_ritenuta() {
		return im_ritenuta;
	}
	public void setIm_ritenuta(java.math.BigDecimal im_ritenuta)  {
		this.im_ritenuta=im_ritenuta;
	}
	public java.sql.Timestamp getDt_scarico_verso_stipendi() {
		return dt_scarico_verso_stipendi;
	}
	public void setDt_scarico_verso_stipendi(java.sql.Timestamp dt_scarico_verso_stipendi)  {
		this.dt_scarico_verso_stipendi=dt_scarico_verso_stipendi;
	}
	public java.sql.Timestamp getDt_inizio_compenso() {
		return dt_inizio_compenso;
	}
	public void setDt_inizio_compenso(java.sql.Timestamp dt_inizio_compenso)  {
		this.dt_inizio_compenso=dt_inizio_compenso;
	}
	public java.sql.Timestamp getDt_fine_compenso() {
		return dt_fine_compenso;
	}
	public void setDt_fine_compenso(java.sql.Timestamp dt_fine_compenso)  {
		this.dt_fine_compenso=dt_fine_compenso;
	}
}