/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 16/04/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Acconto_classific_coriBase extends Acconto_classific_coriKey implements Keyed {
//    IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile;
 
//    ALIQUOTA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota;
 
//    IM_ACCONTO_CALCOLATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_acconto_calcolato;
 
//    IM_ACCONTO_TRATTENUTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_acconto_trattenuto;
 
//    PG_COMUNE DECIMAL(10,0)
	private java.lang.Long pg_comune;
	
//	 PG_CONGUAGLIO DECIMAL(10,0) NULL
	private java.lang.Long pg_conguaglio;

//  CD_CDS_CONGUAGLIO VARCHAR(30) NULL
	private java.lang.String cd_cds_conguaglio;

//  CD_UO_CONGUAGLIO VARCHAR(30) NULL
	private java.lang.String cd_uo_conguaglio;	
 
	public Acconto_classific_coriBase() {
		super();
	}
	public Acconto_classific_coriBase(java.lang.Integer esercizio, java.lang.Integer cd_anag, java.lang.String cd_classificazione_cori) {
		super(esercizio, cd_anag, cd_classificazione_cori);
	}
	public java.math.BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(java.math.BigDecimal imponibile)  {
		this.imponibile=imponibile;
	}
	public java.math.BigDecimal getAliquota() {
		return aliquota;
	}
	public void setAliquota(java.math.BigDecimal aliquota)  {
		this.aliquota=aliquota;
	}
	public java.math.BigDecimal getIm_acconto_calcolato() {
		return im_acconto_calcolato;
	}
	public void setIm_acconto_calcolato(java.math.BigDecimal im_acconto_calcolato)  {
		this.im_acconto_calcolato=im_acconto_calcolato;
	}
	public java.math.BigDecimal getIm_acconto_trattenuto() {
		return im_acconto_trattenuto;
	}
	public void setIm_acconto_trattenuto(java.math.BigDecimal im_acconto_trattenuto)  {
		this.im_acconto_trattenuto=im_acconto_trattenuto;
	}
	public java.lang.Long getPg_comune() {
		return pg_comune;
	}
	public void setPg_comune(java.lang.Long pg_comune)  {
		this.pg_comune=pg_comune;
	}
	public java.lang.String getCd_cds_conguaglio() {
		return cd_cds_conguaglio;
	}
	public void setCd_cds_conguaglio(java.lang.String cd_cds_conguaglio) {
		this.cd_cds_conguaglio = cd_cds_conguaglio;
	}
	public java.lang.String getCd_uo_conguaglio() {
		return cd_uo_conguaglio;
	}
	public void setCd_uo_conguaglio(java.lang.String cd_uo_conguaglio) {
		this.cd_uo_conguaglio = cd_uo_conguaglio;
	}
	public java.lang.Long getPg_conguaglio() {
		return pg_conguaglio;
	}
	public void setPg_conguaglio(java.lang.Long pg_conguaglio) {
		this.pg_conguaglio = pg_conguaglio;
	}
}