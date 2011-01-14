/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_approvato_dip_areaBase extends Pdg_approvato_dip_areaKey implements Keyed {
//    CD_CDS_AREA VARCHAR(30)
	private java.lang.String cd_cds_area;
 
//    IMPORTO_APPROVATO DECIMAL(15,2)
	private java.math.BigDecimal importo_approvato;
 
	public Pdg_approvato_dip_areaBase() {
		super();
	}
	public Pdg_approvato_dip_areaBase(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_dipartimento, pg_dettaglio);
	}
	public java.lang.String getCd_cds_area() {
		return cd_cds_area;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
	}
	public java.math.BigDecimal getImporto_approvato() {
		return importo_approvato;
	}
	public void setImporto_approvato(java.math.BigDecimal importo_approvato)  {
		this.importo_approvato=importo_approvato;
	}
}