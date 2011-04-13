package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 5:49:25 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoSpesaBulk extends IDocumentoAmministrativoBulk {
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:45:21 AM)
 * @return java.lang.String
 */
String getDescrizione_spesa();
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge();
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge();

public java.sql.Timestamp getDt_documento();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:33 AM)
 * @return java.math.BigDecimal
 */
java.math.BigDecimal getImporto_netto_spesa();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:33 AM)
 * @return java.math.BigDecimal
 */
java.math.BigDecimal getImporto_spesa();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:04 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_cds(String newCd_cds);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_tipo_doc_amm(String newCd_tipo_doc_amm);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_uo(String newCd_uo);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setEsercizio(Integer newEsercizio);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setPg_doc_amm(Long newPg);

}
