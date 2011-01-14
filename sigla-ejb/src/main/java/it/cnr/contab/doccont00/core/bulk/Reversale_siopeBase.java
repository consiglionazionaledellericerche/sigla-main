/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Reversale_siopeBase extends Reversale_siopeKey implements Keyed {
//  IMPORTO DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal importo;
 
	public Reversale_siopeBase() {
		super();
	}
	public Reversale_siopeBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(cd_cds, esercizio, pg_reversale, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope);
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
}