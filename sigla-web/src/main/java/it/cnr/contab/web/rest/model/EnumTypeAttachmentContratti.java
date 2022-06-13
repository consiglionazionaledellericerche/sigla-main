package it.cnr.contab.web.rest.model;

import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoFlussoDocumentBulk;

public enum EnumTypeAttachmentContratti {

    ALLEGATO_FLUSSO(AllegatoContrattoFlussoDocumentBulk.ALLEGATO_FLUSSO),
    CONTRATTO_FLUSSO( AllegatoContrattoFlussoDocumentBulk.CONTRATTO_FLUSSO),
    AGGIUDICAZIONE_FLUSSO( AllegatoContrattoFlussoDocumentBulk.AGGIUDICAZIONE_FLUSSO),
    REVOCA_FLUSSO(AllegatoContrattoFlussoDocumentBulk.REVOCA_FLUSSO),
    RICHIESTA_ACQUISTO_FLUSSO(AllegatoContrattoFlussoDocumentBulk.RICHIESTA_ACQUISTO_FLUSSO),
    AVVISO_POST_INFORMAZIONE_FLUSSO(AllegatoContrattoFlussoDocumentBulk.AVVISO_POST_INFORMAZIONE_FLUSSO),
    STIPULA_FLUSSO( AllegatoContrattoFlussoDocumentBulk.STIPULA_FLUSSO),
    ELENCO_VERBALI_FLUSSO(AllegatoContrattoFlussoDocumentBulk.ELENCO_VERBALI_FLUSSO),
    NOMINA_COMMISSIONE_FLUSSO(AllegatoContrattoFlussoDocumentBulk.NOMINA_COMMISSIONE_FLUSSO),
    AMMESSI_ESCLUSI_FLUSSO(AllegatoContrattoFlussoDocumentBulk.AMMESSI_ESCLUSI_FLUSSO),
    LETTERA_INVITO_FLUSSO(AllegatoContrattoFlussoDocumentBulk.LETTERA_INVITO_FLUSSO),
    BANDO_AVVISI_FLUSSO(AllegatoContrattoFlussoDocumentBulk.BANDO_AVVISI_FLUSSO),
    MODIFICHE_VARIANTI_FLUSSO(AllegatoContrattoFlussoDocumentBulk.MODIFICHE_VARIANTI_FLUSSO),
    DECISIONE_CONTRATTARE_FLUSSO(AllegatoContrattoFlussoDocumentBulk.DECISIONE_CONTRATTARE_FLUSSO);

    public  String matadata;

    private EnumTypeAttachmentContratti(String matadata) {
        this.matadata = matadata;
    }

    public static void main(String[] arg){
        String s = CONTRATTO_FLUSSO.matadata;
        System.out.println(s);
    }
}
