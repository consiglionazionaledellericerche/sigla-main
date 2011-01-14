/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Esenzioni_addizionaliBase extends Esenzioni_addizionaliKey implements Keyed {
//    DS_COMUNE VARCHAR(100) NOT NULL
	private java.lang.String ds_comune;
 
//    CD_PROVINCIA VARCHAR(10) NOT NULL
	private java.lang.String cd_provincia;
 
//    IMPORTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo;
 
//    OLD_IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal old_importo;
	private String nota;
 
	public Esenzioni_addizionaliBase() {
		super();
	}
	public Esenzioni_addizionaliBase(java.lang.String cd_catastale) {
		super(cd_catastale);
	}
	public java.lang.String getDs_comune() {
		return ds_comune;
	}
	public void setDs_comune(java.lang.String ds_comune)  {
		this.ds_comune=ds_comune;
	}
	public java.lang.String getCd_provincia() {
		return cd_provincia;
	}
	public void setCd_provincia(java.lang.String cd_provincia)  {
		this.cd_provincia=cd_provincia;
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	public java.math.BigDecimal getOld_importo() {
		return old_importo;
	}
	public void setOld_importo(java.math.BigDecimal old_importo)  {
		this.old_importo=old_importo;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
}