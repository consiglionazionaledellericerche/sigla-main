/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;


public class ReversaleCupIBulk extends ReversaleCupBulk {

	protected Reversale_rigaIBulk reversale_rigaI;
	
	public ReversaleCupIBulk() {
		super();
	}

	public ReversaleCupIBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_Reversale, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.String cd_cup) {
		super(cd_cds, esercizio, pg_Reversale, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm,cd_cup);
	}

	public Reversale_rigaBulk getReversale_riga() {
		return getReversale_rigaI();
	}	
	
	public Reversale_rigaIBulk getReversale_rigaI() {
		return reversale_rigaI;
	}	
	
	public void setReversale_riga(Reversale_rigaBulk newReversale_riga) {
		setReversale_rigaI((Reversale_rigaIBulk)newReversale_riga);
	}

	public void setReversale_rigaI(Reversale_rigaIBulk newReversale_rigaI) {
		reversale_rigaI = newReversale_rigaI;
	}
}