/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_norma_perlaBase extends Tipo_norma_perlaKey implements Keyed {
//    DS_TIPO_NORMA VARCHAR(50) NOT NULL
	private java.lang.String ds_tipo_norma;
 
//    CD_TIPO_NORMA_PERLA VARCHAR(3) NOT NULL
	private java.lang.String cd_tipo_norma_perla;

//    NUMERO_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String numero_tipo_norma;
	
//    DT_TIPO_NORMA TIMESTAMP(7)
	private java.sql.Timestamp dt_tipo_norma;

//    ARTICOLO_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String articolo_tipo_norma;

//    COMMA_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String comma_tipo_norma;
	
//    TIPO_ASSOCIAZIONE VARCHAR(3) NOT NULL
	private java.lang.String tipo_associazione;

	public Tipo_norma_perlaBase() {
		super();
	}
	public Tipo_norma_perlaBase(java.lang.String cd_tipo_norma) {
		super(cd_tipo_norma);
	}
	public java.lang.String getDs_tipo_norma() {
		return ds_tipo_norma;
	}
	public void setDs_tipo_norma(java.lang.String ds_tipo_norma)  {
		this.ds_tipo_norma=ds_tipo_norma;
	}
	public java.lang.String getCd_tipo_norma_perla() {
		return cd_tipo_norma_perla;
	}
	public void setCd_tipo_norma_perla(java.lang.String cd_tipo_norma_perla) {
		this.cd_tipo_norma_perla = cd_tipo_norma_perla;
	}
	public java.lang.String getNumero_tipo_norma() {
		return numero_tipo_norma;
	}
	public void setNumero_tipo_norma(java.lang.String numero_tipo_norma) {
		this.numero_tipo_norma = numero_tipo_norma;
	}
	public java.sql.Timestamp getDt_tipo_norma() {
		return dt_tipo_norma;
	}
	public void setDt_tipo_norma(java.sql.Timestamp dt_tipo_norma) {
		this.dt_tipo_norma = dt_tipo_norma;
	}
	public java.lang.String getArticolo_tipo_norma() {
		return articolo_tipo_norma;
	}
	public void setArticolo_tipo_norma(java.lang.String articolo_tipo_norma) {
		this.articolo_tipo_norma = articolo_tipo_norma;
	}
	public java.lang.String getComma_tipo_norma() {
		return comma_tipo_norma;
	}
	public void setComma_tipo_norma(java.lang.String comma_tipo_norma) {
		this.comma_tipo_norma = comma_tipo_norma;
	}
	public java.lang.String getTipo_associazione() {
		return tipo_associazione;
	}
	public void setTipo_associazione(java.lang.String tipo_associazione) {
		this.tipo_associazione = tipo_associazione;
	}
}