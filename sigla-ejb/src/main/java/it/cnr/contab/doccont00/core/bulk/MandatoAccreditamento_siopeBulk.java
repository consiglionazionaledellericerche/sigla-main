/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;

public class MandatoAccreditamento_siopeBulk extends Mandato_siopeBulk {

	protected MandatoAccreditamento_rigaBulk mandatoAccreditamento_riga;
	
	public MandatoAccreditamento_siopeBulk() {
		super();
	}

	public MandatoAccreditamento_siopeBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(cd_cds, esercizio, pg_mandato, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope);
	}

	public Mandato_rigaBulk getMandato_riga() {
		return getMandatoAccreditamento_riga();
	}	
	
	public MandatoAccreditamento_rigaBulk getMandatoAccreditamento_riga() {
		return mandatoAccreditamento_riga;
	}	
	
	public void setMandato_riga(Mandato_rigaBulk newMandato_riga) {
		setMandatoAccreditamento_riga((MandatoAccreditamento_rigaBulk)newMandato_riga);
	}

	public void setMandatoAccreditamento_riga(MandatoAccreditamento_rigaBulk newMandatoAccreditamento_riga) {
		mandatoAccreditamento_riga = newMandatoAccreditamento_riga;
	}
}