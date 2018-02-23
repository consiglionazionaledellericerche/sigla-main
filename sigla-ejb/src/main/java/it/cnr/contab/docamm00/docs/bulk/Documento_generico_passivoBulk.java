package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.ValidationException;

import java.util.Calendar;
import java.util.Iterator;

/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
public class Documento_generico_passivoBulk 
	extends Documento_genericoBulk {
/**
 * Documento_generico_passivoBulk constructor comment.
 */
public Documento_generico_passivoBulk() {
	super();
	setTipo_documento(new Tipo_documento_ammBulk());
	setTi_entrate_spese(Documento_genericoBulk.SPESE);
}
/**
 * Documento_generico_passivoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_tipo_documento_amm java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_documento_generico java.lang.Long
 */
public Documento_generico_passivoBulk(String cd_cds, String cd_tipo_documento_amm, String cd_unita_organizzativa, Integer esercizio, Long pg_documento_generico) {
	super(cd_cds, cd_tipo_documento_amm, cd_unita_organizzativa, esercizio, pg_documento_generico);
}
public String getManagerName() {

	return "CRUDGenericoPassivoBP";
}
public String getTipologiaDocumentoGenerico() {

	if (getCd_tipo_documento_amm() == null) return "Sconosciuto";
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
