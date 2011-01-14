/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Gruppo_cr_detBase extends Gruppo_cr_detKey implements Keyed {
//    CD_TERZO_VERSAMENTO DECIMAL(8,0)
	private java.lang.Integer cd_terzo_versamento;
 
//    CD_MODALITA_PAGAMENTO VARCHAR(10)
	private java.lang.String cd_modalita_pagamento;
 
//    PG_BANCA DECIMAL(8,0)
	private java.lang.Long pg_banca;
 
	public Gruppo_cr_detBase() {
		super();
	}
	public Gruppo_cr_detBase(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_regione, java.lang.Long pg_comune) {
		super(esercizio, cd_gruppo_cr, cd_regione, pg_comune);
	}
	public java.lang.Integer getCd_terzo_versamento() {
		return cd_terzo_versamento;
	}
	public void setCd_terzo_versamento(java.lang.Integer cd_terzo_versamento)  {
		this.cd_terzo_versamento=cd_terzo_versamento;
	}
	public java.lang.String getCd_modalita_pagamento() {
		return cd_modalita_pagamento;
	}
	public void setCd_modalita_pagamento(java.lang.String cd_modalita_pagamento)  {
		this.cd_modalita_pagamento=cd_modalita_pagamento;
	}
	public java.lang.Long getPg_banca() {
		return pg_banca;
	}
	public void setPg_banca(java.lang.Long pg_banca)  {
		this.pg_banca=pg_banca;
	}
}