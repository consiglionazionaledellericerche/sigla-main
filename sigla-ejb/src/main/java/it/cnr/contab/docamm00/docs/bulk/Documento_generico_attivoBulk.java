package it.cnr.contab.docamm00.docs.bulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
public class Documento_generico_attivoBulk extends Documento_genericoBulk {
/**
 * Documento_generico_attivoBulk constructor comment.
 */
public Documento_generico_attivoBulk() {
	super();
	setTipo_documento(new Tipo_documento_ammBulk());
	setTi_entrate_spese(Documento_genericoBulk.ENTRATE);
}

/**
 * Documento_generico_attivoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_tipo_documento_amm java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_documento_generico java.lang.Long
 */
public Documento_generico_attivoBulk(String cd_cds, String cd_tipo_documento_amm, String cd_unita_organizzativa, Integer esercizio, Long pg_documento_generico) {
	super(cd_cds, cd_tipo_documento_amm, cd_unita_organizzativa, esercizio, pg_documento_generico);
}

public String getManagerName() {

	return "CRUDGenericoAttivoBP";
}

public String getTipologiaDocumentoGenerico() {

	if (getCd_tipo_documento_amm() == null) return "Sconosciuto";
	//Tipo_documento_ammBulk tipo = new Tipo_documento_ammBulk(getCd_tipo_documento_amm());
	//if (getTipi_doc_for_search() != null && 
		//it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(getTipi_doc_for_search(),tipo))
			//return "Entrata";
	//else if (String.valueOf(ENTRATE).equalsIgnoreCase(String.valueOf(getTi_entrate_spese())))
			//return "Entrata";
	if (this.GENERICO_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		Numerazione_doc_ammBulk.TIPO_TRASF_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		Numerazione_doc_ammBulk.TIPO_REGOLA_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		Numerazione_doc_ammBulk.TIPO_GEN_IVA_E.equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		"GEN_CORA_E".equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		"GEN_CORV_E".equalsIgnoreCase(getCd_tipo_documento_amm()) ||
		"GEN_RC_DAT".equalsIgnoreCase(getCd_tipo_documento_amm()))
		return "Entrata";
	return "Spesa";
}
}