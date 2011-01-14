/*
* Creted by Generator 1.0
* Date 03/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class ElencoinventariobeniBase extends ElencoinventariobeniKey implements Keyed {
//	DATA_REGISTRAZIONE DATE	
	private java.sql.Timestamp data_registrazione;
//    VALORE_INIZIALE DECIMAL(20,6)
	private java.math.BigDecimal valore_iniziale;
 
//    VARIAZIONE_MENO DECIMAL(20,6)
	private java.math.BigDecimal variazione_meno;
 
//    VARIAZIONE_PIU DECIMAL(20,6)
	private java.math.BigDecimal variazione_piu;
 
//    VALORE_FINALE DECIMAL(20,6)
	private java.math.BigDecimal valore_finale;
 	
 	private String etichetta;
 	
 	private String targa;
	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}
	public ElencoinventariobeniBase() {
		super();
	}
	public ElencoinventariobeniBase(java.lang.String cd_unita_organizzativa, java.lang.String cd_categoria_gruppo, java.lang.Long nr_inventario, java.lang.Long progressivo, java.lang.String ds_bene, java.sql.Timestamp data_registrazione) {
		super(cd_unita_organizzativa, cd_categoria_gruppo, nr_inventario, progressivo, ds_bene, data_registrazione);
	}
	public java.math.BigDecimal getValore_iniziale () {
		return valore_iniziale;
	}
	public void setValore_iniziale(java.math.BigDecimal valore_iniziale)  {
		this.valore_iniziale=valore_iniziale;
	}
	public java.math.BigDecimal getVariazione_meno () {
		return variazione_meno;
	}
	public void setVariazione_meno(java.math.BigDecimal variazione_meno)  {
		this.variazione_meno=variazione_meno;
	}
	public java.math.BigDecimal getVariazione_piu () {
		return variazione_piu;
	}
	public void setVariazione_piu(java.math.BigDecimal variazione_piu)  {
		this.variazione_piu=variazione_piu;
	}
	public java.math.BigDecimal getValore_finale () {
		return valore_finale;
	}
	public void setValore_finale(java.math.BigDecimal valore_finale)  {
		this.valore_finale=valore_finale;
	}
	/**
	 * @return
	 */
	public java.sql.Timestamp getData_registrazione() {
		return data_registrazione;
	}

	/**
	 * @param timestamp
	 */
	public void setData_registrazione(java.sql.Timestamp timestamp) {
		data_registrazione = timestamp;
	}

	/**
	 * @return
	 */
	public String getEtichetta() {
		return etichetta;
	}

	/**
	 * @param string
	 */
	public void setEtichetta(String string) {
		etichetta = string;
	}

}