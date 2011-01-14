/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Esenzioni_addcomBase extends Esenzioni_addcomKey implements Keyed {
//    IMPORTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo;
 
//    DT_FINE_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_validita;
 
//    PG_COMUNE DECIMAL(10,0)
	private java.lang.Long pg_comune;
 
	public Esenzioni_addcomBase() {
		super();
	}
	public Esenzioni_addcomBase(java.lang.String cd_catastale, java.sql.Timestamp dt_inizio_validita) {
		super(cd_catastale, dt_inizio_validita);
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita)  {
		this.dt_fine_validita=dt_fine_validita;
	}
	public java.lang.Long getPg_comune() {
		return pg_comune;
	}
	public void setPg_comune(java.lang.Long pg_comune)  {
		this.pg_comune=pg_comune;
	}
}