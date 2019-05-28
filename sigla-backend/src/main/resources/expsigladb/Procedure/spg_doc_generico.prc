CREATE OR REPLACE PROCEDURE         SPG_DOC_GENERICO
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Protocollo VPG per stampa massiva di documenti generici
--
--
-- History:
--
-- Date: 19/03/2003
-- Version: 1.0
-- Creazione
--
-- Date: 21/01/2004
-- Version: 1.1
-- Estrazione CIN dalla BANCA (richiesta n. 697)
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
(
 aCd_cds in varchar2,
 aCd_uo in varchar2,
 aEs in number,
 aCd_tipo_doc_amm in varchar2,
 aPg_da in number,
 aPg_a in number,
 aDt_da in varchar2,
 aDt_a in varchar2,
 aCd_terzo in varchar2,
 acd_tdg in number
) is
 aId number;
 i number;
 ti_e_s char(1);
 DS varchar2(500) := null;
 aVar1 varchar2(300) := null;
 aNum1 number := 0;
 ESISTE_BOLLO VARCHAR2(1) := 'N';
 IMPORTO_BOLLO NUMBER := NULL;
 uoEnte unita_organizzativa%rowtype;
begin
    uoEnte:=cnrctb020.getUoEnte(aEs);
    select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
    i:=0;

    for aDocGen in (select dgen.*, tdg.descrizione, tdg.codice from documento_generico dgen, tipo_documento_generico tdg
                    where dgen.CD_CDS_ORIGINE          = aCd_cds
                      and dgen.CD_UO_ORIGINE          = aCd_uo
                      and dgen.ESERCIZIO              = aEs
                      and dgen.CD_TIPO_DOCUMENTO_AMM  like aCd_tipo_doc_amm
                      and dgen.PG_DOCUMENTO_GENERICO  >= aPg_da
                      and dgen.PG_DOCUMENTO_GENERICO  <= aPg_a
                      and ((acd_tdg is not null and dgen.id_tipo_documento_generico = acd_tdg) or (acd_tdg is null))
                      and dgen.DATA_REGISTRAZIONE      >= to_date(aDt_da,'YYYY/MM/DD')
                      and dgen.DATA_REGISTRAZIONE      <= to_date(aDt_a,'YYYY/MM/DD')
											and dgen.id_tipo_documento_Generico = tdg.id(+)
                      and exists (select 1 from documento_generico_riga dgenr
                                      where dgenr.CD_CDS                  = dgen.CD_CDS
                                    and dgenr.CD_UNITA_ORGANIZZATIVA = dgen.CD_UNITA_ORGANIZZATIVA
                                     and dgenr.ESERCIZIO                   = dgen.ESERCIZIO
                                     and dgenr.CD_TIPO_DOCUMENTO_AMM  = dgen.CD_TIPO_DOCUMENTO_AMM
                                     and dgenr.PG_DOCUMENTO_GENERICO  = dgen.PG_DOCUMENTO_GENERICO
                                    and to_char(dgenr.CD_TERZO) like aCd_terzo) ) loop
    -- inizio loop 1

       i:= i+1;
       
       IF aDocGen.ID_TIPO_DOCUMENTO_GENERICO IS NOT NULL THEN
             begin
                select IM_BOLLO
                INTO    IMPORTO_BOLLO    
                from TIPO_ATTO_BOLLO T, TIPO_DOCUMENTO_GENERICO TD, CONFIGURAZIONE_CNR CNR_CD_BOLLO, CONFIGURAZIONE_CNR CNR_IMPORTO_LIMITE
                where TD.ID = aDocGen.ID_TIPO_DOCUMENTO_GENERICO AND
                            TD.SOGGETTO_BOLLO = 'Y' 
                          and aDocGen.esercizio = Cnr_cd_bollo.ESERCIZIO
                          and 'BOLLO_VIRTUALE' = Cnr_cd_bollo.cd_chiave_primaria
                          AND 'CODICE_DOCUMENTO_FATTURA_ATTIVA' = Cnr_cd_bollo.cd_chiave_secondaria
                          AND '*' = Cnr_cd_bollo.cd_unita_funzionale
                          and aDocGen.esercizio = CNR_IMPORTO_LIMITE.ESERCIZIO
                          and 'BOLLO_VIRTUALE' = CNR_IMPORTO_LIMITE.cd_chiave_primaria
                          AND 'IMPORTO_LIMITE' = CNR_IMPORTO_LIMITE.cd_chiave_secondaria
                          AND '*' = CNR_IMPORTO_LIMITE.cd_unita_funzionale and
                            T.CODICE = cnr_cd_bollo.val01 and
							t.dt_Ini_Validita <= aDocGen.DATA_REGISTRAZIONE and
                            (t.dt_Fin_Validita is null or t.dt_Fin_Validita >= aDocGen.DATA_REGISTRAZIONE);
             exception
                WHEN NO_DATA_FOUND THEN IMPORTO_BOLLO := NULL;
             end;
         ELSE
                 IMPORTO_BOLLO := NULL;
       END IF;
       select ti_entrata_spesa, ds_tipo_documento_amm into ti_e_s, aVar1
       from tipo_documento_amm
       where cd_tipo_documento_amm = aDocGen.CD_TIPO_DOCUMENTO_AMM;

       -- inizio inserimento record di testata: (A,A)

       insert into VPG_DOC_GENERICO (ID,
                                    CHIAVE,
                                    DESCRIZIONE,
                                    SEQUENZA,
                                    CD_CDS,
                                    CD_UNITA_ORGANIZZATIVA,
                                    ESERCIZIO,
                                    CD_TIPO_DOCUMENTO_AMM,
                                    PG_DOCUMENTO_GENERICO,
                                    TI_RECORD_L1,
                                    TI_RECORD_L2,
                                    DS_TIPO_DOCUMENTO_AMM,
                                    TI_ENTRATA_SPESA,
                                    CD_CDS_ORIGINE,
                                    CD_UO_ORIGINE,
                                    DS_CDS,
                                    DS_UO_ORIGINE,
                                    DATA_REGISTRAZIONE,
                                    DS_DOCUMENTO_GENERICO,
                                    TI_ISTITUZ_COMMERC,
                                    IM_TOTALE,
                                    STATO_COFI,
                                    CD_DIVISA,
                                    CAMBIO,
                                    UTCR,
                                    DACR,
                                    CODICE_FISCALE_ENTE,
                                    PARTITA_IVA_ENTE,
                                    VIA_FISCALE_ENTE,
                                    CAP_COMUNE_FISCALE_ENTE,
                                    DS_COMUNE_ENTE,
                                    CD_PROVINCIA_ENTE,
                                    CD_TIPO_DOCUMENTO_GENERICO,
                                    DS_TIPO_DOCUMENTO_GENERICO)
       select aId,
                 'Testata: (A,A)',
              'Stampa RPT',
              i,
              aDocGen.CD_CDS,
              aDocGen.CD_UNITA_ORGANIZZATIVA,
              aDocGen.ESERCIZIO,
              aDocGen.CD_TIPO_DOCUMENTO_AMM,
              aDocGen.PG_DOCUMENTO_GENERICO,
              'A',
              'A',
              aVar1,
              ti_e_s,
              aDocGen.CD_CDS_ORIGINE,
              aDocGen.CD_UO_ORIGINE,
              uo1.DS_UNITA_ORGANIZZATIVA,
              uo2.DS_UNITA_ORGANIZZATIVA,
              aDocGen.DATA_REGISTRAZIONE,
              aDocGen.DS_DOCUMENTO_GENERICO,
              aDocGen.TI_ISTITUZ_COMMERC,
              aDocGen.IM_TOTALE,
              aDocGen.STATO_COFI,
              aDocGen.CD_DIVISA,
              aDocGen.CAMBIO,
              aDocGen.UTCR,
              aDocGen.DACR,
              vat.CODICE_FISCALE,
              vat.PARTITA_IVA,
              vat.VIA_FISCALE || ', ' || vat.NUM_CIVICO_FISCALE,
              vat.CAP_COMUNE_FISCALE,
              vat.DS_COMUNE_FISCALE,
              vat.CD_PROVINCIA_FISCALE,
              aDocGen.codice,
              aDocGen.descrizione
       from unita_organizzativa uo1
           ,unita_organizzativa uo2
           ,v_anagrafico_terzo vat
       where uo1.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_CDS
         and uo2.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_UO_ORIGINE
         and vat.CD_UNITA_ORGANIZZATIVA =uoEnte.cd_unita_organizzativa; -- ente

       -- fine inserimento record di testata: (A,A)

       -- inizio inserimento record di righe: (B,A), (B,B)

       -- ciclo sulle righe di documento generico

       for aDocGenRiga in (select * from documento_generico_riga dgenr
                                    where dgenr.CD_CDS                   = aDocGen.CD_CDS
                             and dgenr.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_UNITA_ORGANIZZATIVA
                             and dgenr.ESERCIZIO              = aDocGen.ESERCIZIO
                             and dgenr.CD_TIPO_DOCUMENTO_AMM  = aDocGen.CD_TIPO_DOCUMENTO_AMM
                             and dgenr.PG_DOCUMENTO_GENERICO  = aDocGen.PG_DOCUMENTO_GENERICO
                             and to_char(dgenr.CD_TERZO) like aCd_terzo) loop
       -- inizio loop 2

              i:= i+1;

           begin
                   select rifmp.DS_MODALITA_PAG into aVar1
                from rif_modalita_pagamento rifmp
                   where rifmp.CD_MODALITA_PAG = aDocGenRiga.CD_MODALITA_PAG;
           exception when NO_DATA_FOUND then
                        aVar1 := null;
           end;
        
             IF IMPORTO_BOLLO IS NOT NULL AND IMPORTO_BOLLO = aDocGenRiga.IM_RIGA THEN
                     ESISTE_BOLLO := 'S';
             ELSE
                     ESISTE_BOLLO := 'N';
             END IF;
                
           insert into VPG_DOC_GENERICO (ID,
                                        CHIAVE,
                                        DESCRIZIONE,
                                        SEQUENZA,
                                        CD_CDS,
                                        CD_UNITA_ORGANIZZATIVA,
                                        ESERCIZIO,
                                        CD_TIPO_DOCUMENTO_AMM,
                                        PG_DOCUMENTO_GENERICO,
                                        TI_RECORD_L1,
                                        TI_RECORD_L2,
                                        TI_ENTRATA_SPESA,
                                        DATA_REGISTRAZIONE,
                                        UTCR,
                                        DACR,
                                        PROGRESSIVO_RIGA,
                                        DS_RIGA,
                                        IM_RIGA_DIVISA,
                                        IM_RIGA,
                                        CD_TERZO,
                                        RAGIONE_SOCIALE,
                                        NOME,
                                        COGNOME,
                                        CODICE_FISCALE,
                                        PARTITA_IVA,
                                        DENOMINAZIONE_SEDE,
                                        VIA_SEDE,
                                        DS_COMUNE_SEDE,
                                        CD_PROVINCIA_SEDE,
                                        CAP_COMUNE_SEDE,
                                        CD_MODALITA_PAG,
                                        DS_MODALITA_PAG)
           select aId,
                     'Righe: (B,A)',
                  'Stampa RPT',
                  i,
                  aDocGen.CD_CDS,
                  aDocGen.CD_UNITA_ORGANIZZATIVA,
                  aDocGen.ESERCIZIO,
                  aDocGen.CD_TIPO_DOCUMENTO_AMM,
                  aDocGen.PG_DOCUMENTO_GENERICO,
                  'B',
                  'A',
                  ti_e_s,
                  aDocGen.DATA_REGISTRAZIONE,
                  aDocGen.UTCR,
                  aDocGen.DACR,
                  aDocGenRiga.PROGRESSIVO_RIGA,
                  aDocGenRiga.DS_RIGA,
                  aDocGenRiga.IM_RIGA_DIVISA,
                  aDocGenRiga.IM_RIGA,
                  aDocGenRiga.CD_TERZO,
                  aDocGenRiga.RAGIONE_SOCIALE,
                  aDocGenRiga.NOME,
                  aDocGenRiga.COGNOME,
                  aDocGenRiga.CODICE_FISCALE,
                  aDocGenRiga.PARTITA_IVA,
                  vat.DENOMINAZIONE_SEDE,
                  vat.VIA_SEDE || ', ' || vat.NUMERO_CIVICO_SEDE,
                  vat.DS_COMUNE_SEDE,
                  vat.CD_PROVINCIA_SEDE,
                  vat.CAP_COMUNE_SEDE,
                  aDocGenRiga.CD_MODALITA_PAG,
                  aVar1
           from v_anagrafico_terzo vat
           where vat.CD_TERZO = aDocGenRiga.CD_TERZO;

           if ti_e_s = 'S' then
           -- documenti generici passivi

           -- modalit? di pagamento al terzo valorizzate solo per generici passivi
               begin
                      update VPG_DOC_GENERICO vpg
                   set (TI_PAGAMENTO,
                        CAB_TERZO,
                        ABI_TERZO,
                        NUMERO_CONTO_TERZO,
                        CIN_TERZO,
                        INTESTAZIONE_TERZO,
                        DS_ABICAB_TERZO,
                        VIA_BANCA_TERZO,
                        CAP_BANCA_TERZO,
                        DS_COMUNE_BANCA_TERZO,
                        CD_PV_BANCA_TERZO,
                        IBAN)
                   =(select ban.TI_PAGAMENTO,
                               ban.CAB,
                            ban.ABI,
                            ban.NUMERO_CONTO,
                            NVL(ban.CIN,' '),
                            ban.INTESTAZIONE,
                            abi.DS_ABICAB,
                            abi.VIA,
                            abi.CAP,
                            com.DS_COMUNE,
                            com.CD_PROVINCIA,
                            ban.codice_iban
                     from banca ban
                          ,abicab abi
                         ,comune com
                     where ban.CD_TERZO     = aDocGenRiga.CD_TERZO
                       and ban.PG_BANCA     = aDocGenRiga.PG_BANCA
                       and abi.ABI         (+)= ban.ABI
                       and abi.CAB         (+)= ban.CAB
                       and com.PG_COMUNE (+)= abi.PG_COMUNE)
                   where vpg.CD_CDS                    = aDocGen.CD_CDS
                     and vpg.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_UNITA_ORGANIZZATIVA
                     and vpg.ESERCIZIO                = aDocGen.ESERCIZIO
                     and vpg.CD_TIPO_DOCUMENTO_AMM  = aDocGen.CD_TIPO_DOCUMENTO_AMM
                     and vpg.PG_DOCUMENTO_GENERICO  = aDocGen.PG_DOCUMENTO_GENERICO
                     and vpg.TI_RECORD_L1            = 'B'
                     and vpg.TI_RECORD_L2            = 'A'
                     and vpg.SEQUENZA                = i;

               exception when NO_DATA_FOUND then
                            null;
               end;

               -- obbligazioni/capitoli
               for aObbv in (select * from obbligazione_scad_voce obbv
                                    where obbv.CD_CDS               = aDocGenRiga.CD_CDS_OBBLIGAZIONE
                               and obbv.ESERCIZIO                   = aDocGenRiga.ESERCIZIO_OBBLIGAZIONE
                               and obbv.ESERCIZIO_ORIGINALE         = aDocGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE
                               and obbv.PG_OBBLIGAZIONE             = aDocGenRiga.PG_OBBLIGAZIONE
                               and obbv.PG_OBBLIGAZIONE_SCADENZARIO = aDocGenRiga.PG_OBBLIGAZIONE_SCADENZARIO) loop
               -- inizio loop 3

                      i:= i+1;

                   begin
                       select distinct mriga.PG_MANDATO into aNum1
                       from mandato_riga mriga
                       where mriga.ESERCIZIO_OBBLIGAZIONE      = aDocGenRiga.ESERCIZIO_OBBLIGAZIONE
                         and mriga.ESERCIZIO_ORI_OBBLIGAZIONE  = aDocGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE
                         and mriga.PG_OBBLIGAZIONE           = aDocGenRiga.PG_OBBLIGAZIONE
                         and mriga.PG_OBBLIGAZIONE_SCADENZARIO = aDocGenRiga.PG_OBBLIGAZIONE_SCADENZARIO
                         and mriga.CD_CDS_DOC_AMM            = aDocGenRiga.CD_CDS
                         and mriga.CD_UO_DOC_AMM             = aDocGenRiga.CD_UNITA_ORGANIZZATIVA
                         and mriga.ESERCIZIO_DOC_AMM        = aDocGenRiga.ESERCIZIO
                         and mriga.CD_TIPO_DOCUMENTO_AMM        = aDocGenRiga.CD_TIPO_DOCUMENTO_AMM
                         and mriga.PG_DOC_AMM           = aDocGenRiga.PG_DOCUMENTO_GENERICO
                         and mriga.STATO               <> 'A';
                   exception when NO_DATA_FOUND then
                                aNum1 := 0;
                   end;

                   insert into VPG_DOC_GENERICO (ID,
                                                CHIAVE,
                                                DESCRIZIONE,
                                                SEQUENZA,
                                                CD_CDS,
                                                CD_UNITA_ORGANIZZATIVA,
                                                ESERCIZIO,
                                                CD_TIPO_DOCUMENTO_AMM,
                                                PG_DOCUMENTO_GENERICO,
                                                TI_RECORD_L1,
                                                TI_RECORD_L2,
                                                TI_ENTRATA_SPESA,
                                                DATA_REGISTRAZIONE,
                                                UTCR,
                                                DACR,
                                                PROGRESSIVO_RIGA,
                                                CD_CDS_OBB_ACC,
                                                ESERCIZIO_OBB_ACC,
                                                ESERCIZIO_ORI_OBB_ACC,
                                                PG_OBB_ACC,
                                                PG_OBB_ACC_SCAD,
                                                CD_VOCE,
                                                DS_VOCE,
                                                PG_MAN_REV)
                   select aId,
                             'Capitoli: (B,B)',
                          'Stampa RPT',
                          i,
                          aDocGen.CD_CDS,
                          aDocGen.CD_UNITA_ORGANIZZATIVA,
                          aDocGen.ESERCIZIO,
                          aDocGen.CD_TIPO_DOCUMENTO_AMM,
                          aDocGen.PG_DOCUMENTO_GENERICO,
                          'B',
                          'B',
                          ti_e_s,
                          aDocGen.DATA_REGISTRAZIONE,
                          aDocGen.UTCR,
                          aDocGen.DACR,
                          aDocGenRiga.PROGRESSIVO_RIGA,
                          aDocGenRiga.CD_CDS_OBBLIGAZIONE,
                          aDocGenRiga.ESERCIZIO_OBBLIGAZIONE,
                          aDocGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE,
                          aDocGenRiga.PG_OBBLIGAZIONE,
                          aDocGenRiga.PG_OBBLIGAZIONE_SCADENZARIO,
                          aObbv.CD_VOCE,
                          voce.DS_VOCE,
                          aNum1
                   from voce_f voce
                   where voce.ESERCIZIO = aObbv.ESERCIZIO
                     and voce.TI_APPARTENENZA = aObbv.TI_APPARTENENZA
                     and voce.TI_GESTIONE      = aObbv.TI_GESTIONE
                     and voce.CD_VOCE          = aObbv.CD_VOCE;

               end loop; -- fine loop 3

           else
           -- documenti generici attivi
     begin
            update VPG_DOC_GENERICO vpg
          set (CAB_IC,
                 ABI_IC,
                 NUMERO_CONTO_IC,
                   CIN_IC,
                 INTESTAZIONE_IC,
                 DS_ABICAB_IC,
                   VIA_IC,
                   CAP_IC,
                   DS_COMUNE_IC,
                   CD_PROVINCIA_IC,
                    IBAN_IC)
          =(select ban.CAB,
                     ban.ABI,
                   ban.NUMERO_CONTO,
                   NVL(ban.CIN,' '),
                   ban.INTESTAZIONE,
                   abi.DS_ABICAB,
                   abi.VIA,
                   abi.CAP,
                   com.DS_COMUNE,
                   com.CD_PROVINCIA,
                   ban.codice_iban
             from banca ban
                          ,abicab abi
                         ,comune com
                         ,terzo ter
                     where  ter.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_Uo_origine
                       and ban.CD_TERZO                 = ter.CD_TERZO
                          and ban.PG_BANCA     = aDocGenRiga.PG_BANCA_uo_cds
                       and abi.ABI         (+)= ban.ABI
                       and abi.CAB         (+)= ban.CAB
                       and com.PG_COMUNE (+)= abi.PG_COMUNE)
                   where vpg.CD_CDS                    = aDocGen.CD_CDS
                     and vpg.CD_UNITA_ORGANIZZATIVA = aDocGen.CD_UNITA_ORGANIZZATIVA
                     and vpg.ESERCIZIO                = aDocGen.ESERCIZIO
                     and vpg.CD_TIPO_DOCUMENTO_AMM  = aDocGen.CD_TIPO_DOCUMENTO_AMM
                     and vpg.PG_DOCUMENTO_GENERICO  = aDocGen.PG_DOCUMENTO_GENERICO
                     and vpg.TI_RECORD_L1            = 'A'
                     and vpg.TI_RECORD_L2            = 'A';
               exception when NO_DATA_FOUND then
                            null;
               end;

               begin
                   select distinct rriga.PG_REVERSALE into aNum1
                   from reversale_riga rriga
                   where rriga.ESERCIZIO_ACCERTAMENTO      = aDocGenRiga.ESERCIZIO_ACCERTAMENTO
                     and rriga.ESERCIZIO_ORI_ACCERTAMENTO  = aDocGenRiga.ESERCIZIO_ORI_ACCERTAMENTO
                     and rriga.PG_ACCERTAMENTO                 = aDocGenRiga.PG_ACCERTAMENTO
                     and rriga.PG_ACCERTAMENTO_SCADENZARIO = aDocGenRiga.PG_ACCERTAMENTO_SCADENZARIO
                     and rriga.CD_CDS_DOC_AMM                 = aDocGenRiga.CD_CDS
                     and rriga.CD_UO_DOC_AMM                  = aDocGenRiga.CD_UNITA_ORGANIZZATIVA
                     and rriga.ESERCIZIO_DOC_AMM            = aDocGenRiga.ESERCIZIO
                     and rriga.CD_TIPO_DOCUMENTO_AMM        = aDocGenRiga.CD_TIPO_DOCUMENTO_AMM
                     and rriga.PG_DOC_AMM                    = aDocGenRiga.PG_DOCUMENTO_GENERICO
                     and rriga.STATO                       <> 'A';
               exception when NO_DATA_FOUND then
                            aNum1 := 0;
               end;
               insert into VPG_DOC_GENERICO (ID,
                                            CHIAVE,
                                            DESCRIZIONE,
                                            SEQUENZA,
                                            CD_CDS,
                                            CD_UNITA_ORGANIZZATIVA,
                                            ESERCIZIO,
                                            CD_TIPO_DOCUMENTO_AMM,
                                            PG_DOCUMENTO_GENERICO,
                                            TI_RECORD_L1,
                                            TI_RECORD_L2,
                                            TI_ENTRATA_SPESA,
                                            DATA_REGISTRAZIONE,
                                            UTCR,
                                            DACR,
                                            PROGRESSIVO_RIGA,
                                            CD_CDS_OBB_ACC,
                                            ESERCIZIO_OBB_ACC,
                                            ESERCIZIO_ORI_OBB_ACC,
                                            PG_OBB_ACC,
                                            PG_OBB_ACC_SCAD,
                                            CD_VOCE,
                                            DS_VOCE,
                                            PG_MAN_REV)
               select aId,
                         'Capitoli: (B,B)',
                      'Stampa RPT',
                      i,
                      aDocGen.CD_CDS,
                      aDocGen.CD_UNITA_ORGANIZZATIVA,
                      aDocGen.ESERCIZIO,
                      aDocGen.CD_TIPO_DOCUMENTO_AMM,
                      aDocGen.PG_DOCUMENTO_GENERICO,
                      'B',
                      'B',
                      ti_e_s,
                      aDocGen.DATA_REGISTRAZIONE,
                      aDocGen.UTCR,
                      aDocGen.DACR,
                      aDocGenRiga.PROGRESSIVO_RIGA,
                      aDocGenRiga.CD_CDS_ACCERTAMENTO,
                      aDocGenRiga.ESERCIZIO_ACCERTAMENTO,
                      aDocGenRiga.ESERCIZIO_ORI_ACCERTAMENTO,
                      aDocGenRiga.PG_ACCERTAMENTO,
                      aDocGenRiga.PG_ACCERTAMENTO_SCADENZARIO,
                      acc.CD_VOCE,
                      voce.DS_VOCE,
                      aNum1
               from accertamento acc
                      ,voce_f voce
               where acc.CD_CDS          = aDocGenRiga.CD_CDS_ACCERTAMENTO
                 and acc.ESERCIZIO       = aDocGenRiga.ESERCIZIO_ACCERTAMENTO
                 and acc.ESERCIZIO_ORIGINALE = aDocGenRiga.ESERCIZIO_ORI_ACCERTAMENTO
                 and acc.PG_ACCERTAMENTO = aDocGenRiga.PG_ACCERTAMENTO
                 and voce.ESERCIZIO         = acc.ESERCIZIO
                 and voce.TI_APPARTENENZA in ('C','D')
                  and voce.TI_GESTIONE      = 'E'
                  and voce.CD_VOCE          = acc.CD_VOCE;

           end if;

       end loop; -- fine loop 2
         IF ESISTE_BOLLO = 'S' THEN
                BEGIN
               i:= i+1;

                   insert into VPG_DOC_GENERICO (ID,
                                                CHIAVE,
                                                DESCRIZIONE,
                                                SEQUENZA,
                                                CD_CDS,
                                                CD_UNITA_ORGANIZZATIVA,
                                                ESERCIZIO,
                                                CD_TIPO_DOCUMENTO_AMM,
                                                PG_DOCUMENTO_GENERICO,
                                                TI_RECORD_L1,
                                                TI_RECORD_L2,
                                                UTCR,
                                                DACR,
                                                DS_VOCE)
                   select aId,
                             'Bollo Virtuale: (C,A)',
                          'Stampa RPT',
                          i,
                          aDocGen.CD_CDS,
                          aDocGen.CD_UNITA_ORGANIZZATIVA,
                          aDocGen.ESERCIZIO,
                          aDocGen.CD_TIPO_DOCUMENTO_AMM,
                          aDocGen.PG_DOCUMENTO_GENERICO,
                          'C',
                          'A',
                          aDocGen.UTCR,
                          aDocGen.DACR,
                          cnr.VAL01
                    from CONFIGURAZIONE_CNR CNR
                    where aDocGen.esercizio = Cnr.ESERCIZIO
                          and 'BOLLO_VIRTUALE' = Cnr.cd_chiave_primaria
                          AND 'ANNOTAZIONE_ASSOLVIMENTO' = Cnr.cd_chiave_secondaria
                          AND '*' = Cnr.cd_unita_funzionale;
             EXCEPTION
                 WHEN NO_DATA_FOUND THEN I := I - 1;
             END;
         END IF;

       -- fine inserimento record di righe: (B,A), (C,A)

    end loop; -- fine loop 1

end;
/
