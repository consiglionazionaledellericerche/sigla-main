CREATE OR REPLACE TRIGGER BU_MINICARRIERA
BEFORE UPDATE
On MINICARRIERA
For each row
WHEN (
(old.PG_MINICARRIERA                 >0 ) and
  	(old.CD_TERZO                  != new.CD_TERZO 						   or
  	 old.CD_TIPO_RAPPORTO          != new.CD_TIPO_RAPPORTO 			 or
  	 old.CD_TRATTAMENTO            != new.CD_TRATTAMENTO    			 or
  	 old.IM_TOTALE_MINICARRIERA    != new.IM_TOTALE_MINICARRIERA  or
  	 old.DT_INIZIO_MINICARRIERA    != new.DT_INIZIO_MINICARRIERA	 or
  	 old.DT_FINE_MINICARRIERA      != new.DT_FINE_MINICARRIERA	   or
  	 old.STATO   								  != new.STATO)
      )
Declare
   aOldRowtype MINICARRIERA%rowtype;
Begin
   --
   -- Trigger attivato su aggiornamento della tabella MINICARRIERA (Before)
   --
   -- Date: 05/05/2014
   -- Version: 1.0
   --
   -- Dependency: CNRSTO090
   --
   -- History:
   --
aOldRowtype.CD_CDS                     :=  :old.CD_CDS                     ;
aOldRowtype.CD_UNITA_ORGANIZZATIVA     :=  :old.CD_UNITA_ORGANIZZATIVA     ;
aOldRowtype.ESERCIZIO                  :=  :old.ESERCIZIO                  ;
aOldRowtype.PG_MINICARRIERA            :=  :old.PG_MINICARRIERA            ;
aOldRowtype.DT_REGISTRAZIONE           :=  :old.DT_REGISTRAZIONE           ;
aOldRowtype.DS_MINICARRIERA            :=  :old.DS_MINICARRIERA            ;
aOldRowtype.TI_ANAGRAFICO              :=  :old.TI_ANAGRAFICO              ;
aOldRowtype.CD_TERZO                   :=  :old.CD_TERZO                   ;
aOldRowtype.RAGIONE_SOCIALE            :=  :old.RAGIONE_SOCIALE            ;
aOldRowtype.NOME                       :=  :old.NOME                       ;
aOldRowtype.COGNOME                    :=  :old.COGNOME                    ;
aOldRowtype.CODICE_FISCALE             :=  :old.CODICE_FISCALE             ;
aOldRowtype.PARTITA_IVA                :=  :old.PARTITA_IVA                ;
aOldRowtype.CD_TERMINI_PAG             :=  :old.CD_TERMINI_PAG             ;
aOldRowtype.CD_MODALITA_PAG            :=  :old.CD_MODALITA_PAG            ;
aOldRowtype.PG_BANCA                   :=  :old.PG_BANCA                   ;
aOldRowtype.CD_TIPO_RAPPORTO           :=  :old.CD_TIPO_RAPPORTO           ;
aOldRowtype.CD_TRATTAMENTO             :=  :old.CD_TRATTAMENTO             ;
aOldRowtype.IM_TOTALE_MINICARRIERA     :=  :old.IM_TOTALE_MINICARRIERA     ;
aOldRowtype.NUMERO_RATE                :=  :old.NUMERO_RATE                ;
aOldRowtype.TI_ANTICIPO_POSTICIPO      :=  :old.TI_ANTICIPO_POSTICIPO      ;
aOldRowtype.MESI_ANTICIPO_POSTICIPO    :=  :old.MESI_ANTICIPO_POSTICIPO    ;
aOldRowtype.DT_INIZIO_MINICARRIERA     :=  :old.DT_INIZIO_MINICARRIERA     ;
aOldRowtype.DT_FINE_MINICARRIERA       :=  :old.DT_FINE_MINICARRIERA       ;
aOldRowtype.STATO_ASS_COMPENSO         :=  :old.STATO_ASS_COMPENSO         ;
aOldRowtype.STATO                      :=  :old.STATO                      ;
aOldRowtype.DT_SOSPENSIONE             :=  :old.DT_SOSPENSIONE             ;
aOldRowtype.DT_RIPRISTINO              :=  :old.DT_RIPRISTINO              ;
aOldRowtype.DT_RINNOVO                 :=  :old.DT_RINNOVO                 ;
aOldRowtype.DT_CESSAZIONE              :=  :old.DT_CESSAZIONE              ;
aOldRowtype.DACR                       :=  :old.DACR                       ;
aOldRowtype.UTCR                       :=  :old.UTCR                       ;
aOldRowtype.DUVA                       :=  :old.DUVA                       ;
aOldRowtype.UTUV                       :=  :old.UTUV                       ;
aOldRowtype.PG_VER_REC                 :=  :old.PG_VER_REC                 ;
aOldRowtype.CD_CDS_MINICARRIERA_ORI    :=  :old.CD_CDS_MINICARRIERA_ORI    ;
aOldRowtype.CD_UO_MINICARRIERA_ORI     :=  :old.CD_UO_MINICARRIERA_ORI     ;
aOldRowtype.ESERCIZIO_MINICARRIERA_ORI :=  :old.ESERCIZIO_MINICARRIERA_ORI ;
aOldRowtype.PG_MINICARRIERA_ORI        :=  :old.PG_MINICARRIERA_ORI        ;
aOldRowtype.TI_ISTITUZ_COMMERC         :=  :old.TI_ISTITUZ_COMMERC         ;
aOldRowtype.FL_TASSAZIONE_SEPARATA     :=  :old.FL_TASSAZIONE_SEPARATA     ;
aOldRowtype.IMPONIBILE_IRPEF_ESEPREC2  :=  :old.IMPONIBILE_IRPEF_ESEPREC2  ;
aOldRowtype.IMPONIBILE_IRPEF_ESEPREC1  :=  :old.IMPONIBILE_IRPEF_ESEPREC1  ;
aOldRowtype.ALIQUOTA_IRPEF_MEDIA       :=  :old.ALIQUOTA_IRPEF_MEDIA       ;
aOldRowtype.FL_ESCLUDI_QVARIA_DEDUZIONE:=  :old.FL_ESCLUDI_QVARIA_DEDUZIONE;
aOldRowtype.ESERCIZIO_REP              :=  :old.ESERCIZIO_REP              ;
aOldRowtype.PG_REPERTORIO              :=  :old.PG_REPERTORIO              ;
aOldRowtype.TI_PRESTAZIONE             :=  :old.TI_PRESTAZIONE             ;
   -- Scarico dello storico
   CNRSTO090.sto_MINICARRIERA(:old.PG_VER_REC+1, aOldRowType);
End;
/


