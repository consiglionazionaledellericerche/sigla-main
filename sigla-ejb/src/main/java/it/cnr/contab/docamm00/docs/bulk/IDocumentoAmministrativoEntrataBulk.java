package it.cnr.contab.docamm00.docs.bulk;

public interface IDocumentoAmministrativoEntrataBulk extends IDocumentoAmministrativoBulk{
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
