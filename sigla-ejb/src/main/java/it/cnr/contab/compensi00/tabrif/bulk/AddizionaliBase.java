/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/03/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class AddizionaliBase extends AddizionaliKey implements Keyed {
//    DS_COMUNE VARCHAR(100) NOT NULL
	private java.lang.String ds_comune;
 
//    CD_PROVINCIA VARCHAR(10) NOT NULL
	private java.lang.String cd_provincia;
 
//    ALIQUOTA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota;

	//  OLD_ALIQUOTA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal old_aliquota;
  
	private java.lang.String nota;
	
	public java.lang.String getNota() {
		return nota;
	}
	public void setNota(java.lang.String nota) {
		this.nota = nota;
	}
	public AddizionaliBase() {
		super();
	}
	public AddizionaliBase(java.lang.String cd_catastale) {
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
	public java.math.BigDecimal getAliquota() {
		return aliquota;
	}
	public void setAliquota(java.math.BigDecimal aliquota)  {
		this.aliquota=aliquota;
	}
	public java.math.BigDecimal getOld_aliquota() {
		return old_aliquota;
	}
	public void setOld_aliquota(java.math.BigDecimal old_aliquota) {
		this.old_aliquota = old_aliquota;
	}
}