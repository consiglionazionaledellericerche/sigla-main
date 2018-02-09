CREATE OR REPLACE package         CNRSTO090 as
--
-- CNRSTO090 - Package per la gestione dello storico dei dati della minicarriera
-- Version: 1.0
--
-- Procedure di inserimento nelle tabelle di storico
-- Viene chiamata da trigger BU_MINICARRIERA
-- Viene chiamata da trigger BU_MINICARRIERA_RATA
-- Viene chiamata da trigger BD_MINICARRIERA
-- Viene chiamata da trigger BD_MINICARRIERA_RATA

 procedure sto_minicarriera (aPgStorico number, aDest MINICARRIERA%rowtype);
 procedure sto_minicarriera_rata (aPgStorico number, aDest MINICARRIERA_RATA%rowtype);
end;
/


CREATE OR REPLACE package body         CNRSTO090 is
 procedure sto_minicarriera (aPgStorico number, aDest MINICARRIERA%rowtype) is
  begin
   insert into MINICARRIERA_S (
        PG_STORICO_                ,
        CD_CDS                     ,
				CD_UNITA_ORGANIZZATIVA     ,
				ESERCIZIO                  ,
				PG_MINICARRIERA            ,
				DT_REGISTRAZIONE           ,
				DS_MINICARRIERA            ,
				TI_ANAGRAFICO              ,
				CD_TERZO                   ,
				RAGIONE_SOCIALE            ,
				NOME                       ,
				COGNOME                    ,
				CODICE_FISCALE             ,
				PARTITA_IVA                ,
				CD_TERMINI_PAG             ,
				CD_MODALITA_PAG            ,
				PG_BANCA                   ,
				CD_TIPO_RAPPORTO           ,
				CD_TRATTAMENTO             ,
				IM_TOTALE_MINICARRIERA     ,
				NUMERO_RATE                ,
				TI_ANTICIPO_POSTICIPO      ,
				MESI_ANTICIPO_POSTICIPO    ,
				DT_INIZIO_MINICARRIERA     ,
				DT_FINE_MINICARRIERA       ,
				STATO_ASS_COMPENSO         ,
				STATO                      ,
				DT_SOSPENSIONE             ,
				DT_RIPRISTINO              ,
				DT_RINNOVO                 ,
				DT_CESSAZIONE              ,
				DACR                       ,
				UTCR                       ,
				DUVA                       ,
				UTUV                       ,
				PG_VER_REC                 ,
				CD_CDS_MINICARRIERA_ORI    ,
				CD_UO_MINICARRIERA_ORI     ,
				ESERCIZIO_MINICARRIERA_ORI ,
				PG_MINICARRIERA_ORI        ,
				TI_ISTITUZ_COMMERC         ,
				FL_TASSAZIONE_SEPARATA     ,
				IMPONIBILE_IRPEF_ESEPREC2  ,
				IMPONIBILE_IRPEF_ESEPREC1  ,
				ALIQUOTA_IRPEF_MEDIA       ,
				FL_ESCLUDI_QVARIA_DEDUZIONE,
				ESERCIZIO_REP              ,
				PG_REPERTORIO              ,
				TI_PRESTAZIONE)
        Values (
            aPgStorico,
           	aDest.CD_CDS                     ,
						aDest.CD_UNITA_ORGANIZZATIVA     ,
						aDest.ESERCIZIO                  ,
						aDest.PG_MINICARRIERA            ,
						aDest.DT_REGISTRAZIONE           ,
						aDest.DS_MINICARRIERA            ,
						aDest.TI_ANAGRAFICO              ,
						aDest.CD_TERZO                   ,
						aDest.RAGIONE_SOCIALE            ,
						aDest.NOME                       ,
						aDest.COGNOME                    ,
						aDest.CODICE_FISCALE             ,
						aDest.PARTITA_IVA                ,
						aDest.CD_TERMINI_PAG             ,
						aDest.CD_MODALITA_PAG            ,
						aDest.PG_BANCA                   ,
						aDest.CD_TIPO_RAPPORTO           ,
						aDest.CD_TRATTAMENTO             ,
						aDest.IM_TOTALE_MINICARRIERA     ,
						aDest.NUMERO_RATE                ,
						aDest.TI_ANTICIPO_POSTICIPO      ,
						aDest.MESI_ANTICIPO_POSTICIPO    ,
						aDest.DT_INIZIO_MINICARRIERA     ,
						aDest.DT_FINE_MINICARRIERA       ,
						aDest.STATO_ASS_COMPENSO         ,
						aDest.STATO                      ,
						aDest.DT_SOSPENSIONE             ,
						aDest.DT_RIPRISTINO              ,
						aDest.DT_RINNOVO                 ,
						aDest.DT_CESSAZIONE              ,
						aDest.DACR                       ,
						aDest.UTCR                       ,
						aDest.DUVA                       ,
						aDest.UTUV                       ,
						aDest.PG_VER_REC                 ,
						aDest.CD_CDS_MINICARRIERA_ORI    ,
						aDest.CD_UO_MINICARRIERA_ORI     ,
						aDest.ESERCIZIO_MINICARRIERA_ORI ,
						aDest.PG_MINICARRIERA_ORI        ,
						aDest.TI_ISTITUZ_COMMERC         ,
						aDest.FL_TASSAZIONE_SEPARATA     ,
						aDest.IMPONIBILE_IRPEF_ESEPREC2  ,
						aDest.IMPONIBILE_IRPEF_ESEPREC1  ,
						aDest.ALIQUOTA_IRPEF_MEDIA       ,
						aDest.FL_ESCLUDI_QVARIA_DEDUZIONE,
						aDest.ESERCIZIO_REP              ,
						aDest.PG_REPERTORIO              ,
						aDest.TI_PRESTAZIONE             );
 end;

 procedure sto_minicarriera_rata (aPgStorico number, aDest MINICARRIERA_RATA%rowtype) is
  begin
   insert into minicarriera_rata_S (
     PG_STORICO_
     ,CD_CDS
     ,CD_UNITA_ORGANIZZATIVA
     ,ESERCIZIO
     ,PG_MINICARRIERA
     ,PG_RATA
     ,DT_INIZIO_RATA
     ,DT_FINE_RATA
     ,DT_SCADENZA
     ,IM_RATA
     ,STATO_ASS_COMPENSO
     ,CD_CDS_COMPENSO
     ,CD_UO_COMPENSO
     ,ESERCIZIO_COMPENSO
     ,PG_COMPENSO
     ,DACR
     ,UTCR
     ,DUVA
     ,UTUV
     ,PG_VER_REC)
     Values (
     aPgStorico
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.PG_MINICARRIERA
    ,aDest.PG_RATA
    ,aDest.DT_INIZIO_RATA
    ,aDest.DT_FINE_RATA
    ,aDest.DT_SCADENZA
    ,aDest.IM_RATA
    ,aDest.STATO_ASS_COMPENSO
    ,aDest.CD_CDS_COMPENSO
    ,aDest.CD_UO_COMPENSO
    ,aDest.ESERCIZIO_COMPENSO
    ,aDest.PG_COMPENSO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC );
 end;
End;
/


