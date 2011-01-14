/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_incaricoBase extends Tipo_incaricoKey implements Keyed {
//    DS_TIPO_INCARICO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_incarico;
 
//   PRC_INCREMENTO DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_incremento;

//   PRC_INCREMENTO_VAR DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_incremento_var;

//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
//    CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

	public Tipo_incaricoBase() {
		super();
	}
	public Tipo_incaricoBase(java.lang.String cd_tipo_incarico) {
		super(cd_tipo_incarico);
	}
	public java.lang.String getDs_tipo_incarico() {
		return ds_tipo_incarico;
	}
	public void setDs_tipo_incarico(java.lang.String ds_tipo_incarico)  {
		this.ds_tipo_incarico=ds_tipo_incarico;
	}
	public java.math.BigDecimal getPrc_incremento() {
		return prc_incremento;
	}
	public void setPrc_incremento(java.math.BigDecimal prc_incremento) {
		this.prc_incremento = prc_incremento;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
	public java.lang.String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}
	public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
		this.cd_tipo_rapporto = cd_tipo_rapporto;
	}
	public java.math.BigDecimal getPrc_incremento_var() {
		return prc_incremento_var;
	}
	public void setPrc_incremento_var(java.math.BigDecimal prc_incremento_var) {
		this.prc_incremento_var = prc_incremento_var;
	}
}