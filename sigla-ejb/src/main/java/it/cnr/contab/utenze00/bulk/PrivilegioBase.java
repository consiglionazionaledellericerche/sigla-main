/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class PrivilegioBase extends PrivilegioKey implements Keyed {
//    DS_PRIVILEGIO VARCHAR(50)
	private java.lang.String ds_privilegio;

//	  TI_ACCESSO CHAR(1)
	private java.lang.String ti_privilegio;

	public PrivilegioBase() {
		super();
	}
	public PrivilegioBase(java.lang.String cd_privilegio) {
		super(cd_privilegio);
	}
	public java.lang.String getDs_privilegio() {
		return ds_privilegio;
	}
	public void setDs_privilegio(java.lang.String ds_privilegio)  {
		this.ds_privilegio=ds_privilegio;
	}
	/* 
	 * Getter dell'attributo ti_privilegio
	 */
	public java.lang.String getTi_privilegio() {
		return ti_privilegio;
	}
	/* 
	 * Setter dell'attributo ti_privilegio
	 */
	public void setTi_privilegio(java.lang.String ti_privilegio) {
		this.ti_privilegio = ti_privilegio;
	}
}