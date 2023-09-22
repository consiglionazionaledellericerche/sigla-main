package it.cnr.contab.docamm00.docs.bulk;

public interface IDocumentoAmministrativoElettronicoBulk {
    Integer getEsercizio();
    Long getPg_docamm();
    Long getProtocollo_iva();
    String getCodiceUnivocoUfficioIpa();
    String getCd_uo_origine();
    Long getProgrUnivocoAnno();
    String getCodiceDestinatarioFatt();
    Boolean isFatturaEstera();
    String getPecFatturaElettronica();
    String getUtcr();
    String recuperoIdFatturaAsString();
    String getTi_fattura();
    String getTipoDocumentoElettronico();
    void setStatoInvioSdi(java.lang.String statoInvioSdi);
    void setDtRicezioneSdi(java.sql.Timestamp dtRicezioneSdi);
    void setCodiceInvioSdi(java.lang.String codiceInvioSdi);
    void setNoteInvioSdi(java.lang.String noteInvioSdi);
    Integer getCdTerzoDocumentoElettronico();
    String getPartita_iva();
    boolean isDocumentoFatturazioneElettronica();
}
