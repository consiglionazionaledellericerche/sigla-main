/*
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Buono_carico_scaricoBase extends Buono_carico_scaricoKey implements Keyed {
//    DS_BUONO_CARICO_SCARICO VARCHAR(100) NOT NULL
	private java.lang.String ds_buono_carico_scarico;
 
//    DATA_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data_registrazione;
 
//    CD_TIPO_CARICO_SCARICO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_carico_scarico;
 
//    PROVENIENZA VARCHAR(100)
	private java.lang.String provenienza;
 
	public Buono_carico_scaricoBase() {
		super();
	}
	public Buono_carico_scaricoBase(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s) {
		super(pg_inventario, ti_documento, esercizio, pg_buono_c_s);
	}
	public java.lang.String getDs_buono_carico_scarico () {
		return ds_buono_carico_scarico;
	}
	public void setDs_buono_carico_scarico(java.lang.String ds_buono_carico_scarico)  {
		this.ds_buono_carico_scarico=ds_buono_carico_scarico;
	}
	public java.sql.Timestamp getData_registrazione () {
		return data_registrazione;
	}
	public void setData_registrazione(java.sql.Timestamp data_registrazione)  {
		this.data_registrazione=data_registrazione;
	}
	public java.lang.String getCd_tipo_carico_scarico () {
		return cd_tipo_carico_scarico;
	}
	public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico)  {
		this.cd_tipo_carico_scarico=cd_tipo_carico_scarico;
	}
	public java.lang.String getProvenienza () {
		return provenienza;
	}
	public void setProvenienza(java.lang.String provenienza)  {
		this.provenienza=provenienza;
	}
}