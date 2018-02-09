CREATE OR REPLACE TRIGGER BU_CONTRATTO
BEFORE Update
On CONTRATTO
For each row
WHEN (
new.stato != 'P'
      )
Declare
   aOldRowtype contratto%rowtype;
Begin
   --
   -- Creazione
   --
   aOldRowtype.ESERCIZIO              		:= :Old.ESERCIZIO             ;
   aOldRowtype.STATO                  		:= :Old.STATO                 ;
   aOldRowtype.PG_CONTRATTO           		:= :Old.PG_CONTRATTO          ;
   aOldRowtype.ESERCIZIO_PADRE        		:= :Old.ESERCIZIO_PADRE       ;
   aOldRowtype.STATO_PADRE            		:= :Old.STATO_PADRE           ;
   aOldRowtype.PG_CONTRATTO_PADRE     		:= :Old.PG_CONTRATTO_PADRE    ;
   aOldRowtype.CD_UNITA_ORGANIZZATIVA 		:= :Old.CD_UNITA_ORGANIZZATIVA;
   aOldRowtype.DT_REGISTRAZIONE       		:= :Old.DT_REGISTRAZIONE      ;
   aOldRowtype.FIG_GIUR_INT           		:= :Old.FIG_GIUR_INT          ;
   aOldRowtype.CD_TERZO_RESP          		:= :Old.CD_TERZO_RESP         ;
   aOldRowtype.CD_TERZO_FIRMATARIO    		:= :Old.CD_TERZO_FIRMATARIO   ;
   aOldRowtype.FIG_GIUR_EST           		:= :Old.FIG_GIUR_EST          ;
   aOldRowtype.RESP_ESTERNO           		:= :Old.RESP_ESTERNO          ;
   aOldRowtype.NATURA_CONTABILE       		:= :Old.NATURA_CONTABILE      ;
   aOldRowtype.CD_TIPO_CONTRATTO      		:= :Old.CD_TIPO_CONTRATTO     ;
   aOldRowtype.CD_PROC_AMM            		:= :Old.CD_PROC_AMM           ;
   aOldRowtype.OGGETTO                		:= :Old.OGGETTO               ;
   aOldRowtype.CD_PROTOCOLLO          		:= :Old.CD_PROTOCOLLO         ;
   aOldRowtype.DT_STIPULA             		:= :Old.DT_STIPULA            ;
   aOldRowtype.DT_INIZIO_VALIDITA     		:= :Old.DT_INIZIO_VALIDITA    ;
   aOldRowtype.DT_FINE_VALIDITA       		:= :Old.DT_FINE_VALIDITA      ;
   aOldRowtype.DT_PROROGA             		:= :Old.DT_PROROGA            ;
   aOldRowtype.IM_CONTRATTO_ATTIVO    		:= :Old.IM_CONTRATTO_ATTIVO   ;
   aOldRowtype.IM_CONTRATTO_PASSIVO   		:= :Old.IM_CONTRATTO_PASSIVO  ;
   aOldRowtype.CD_TIPO_ATTO           		:= :Old.CD_TIPO_ATTO          ;
   aOldRowtype.DS_ATTO_NON_DEFINITO   		:= :Old.DS_ATTO_NON_DEFINITO  ;
   aOldRowtype.DS_ATTO                		:= :Old.DS_ATTO               ;
   aOldRowtype.CD_ORGANO              		:= :Old.CD_ORGANO             ;
   aOldRowtype.DS_ORGANO_NON_DEFINITO 		:= :Old.DS_ORGANO_NON_DEFINITO;
   aOldRowtype.DT_ANNULLAMENTO        		:= :Old.DT_ANNULLAMENTO       ;
   aOldRowtype.DS_ANNULLAMENTO        		:= :Old.DS_ANNULLAMENTO       ;
   aOldRowtype.CD_TIPO_ATTO_ANN       		:= :Old.CD_TIPO_ATTO_ANN      ;
   aOldRowtype.DS_ATTO_ANN_NON_DEFINITO 	:= :Old.DS_ATTO_ANN_NON_DEFINITO;
   aOldRowtype.DS_ATTO_ANN            		:= :Old.DS_ATTO_ANN           ;
   aOldRowtype.CD_ORGANO_ANN          		:= :Old.CD_ORGANO_ANN         ;
   aOldRowtype.DS_ORGANO_ANN_NON_DEFINITO := :Old.DS_ORGANO_ANN_NON_DEFINITO;
   aOldRowtype.ESERCIZIO_PROTOCOLLO  			:= :Old.ESERCIZIO_PROTOCOLLO  ;
   aOldRowtype.CD_PROTOCOLLO_GENERALE			:= :Old.CD_PROTOCOLLO_GENERALE;
   aOldRowtype.FL_ART82              			:= :Old.FL_ART82              ;
   aOldRowtype.DACR                  			:= :Old.DACR                  ;
   aOldRowtype.UTCR                  			:= :Old.UTCR                  ;
   aOldRowtype.DUVA                  			:= :Old.DUVA                  ;
   aOldRowtype.UTUV                  			:= :Old.UTUV                  ;
   aOldRowtype.PG_VER_REC            			:= :Old.PG_VER_REC            ;
   aOldRowtype.CD_TIPO_NORMA_PERLA   			:= :Old.CD_TIPO_NORMA_PERLA   ;
   aOldRowtype.DIRETTORE             			:= :Old.DIRETTORE             ;
   aOldRowtype.FL_MEPA               			:= :Old.FL_MEPA               ;
   aOldRowtype.FL_PUBBLICA_CONTRATTO 			:= :Old.FL_PUBBLICA_CONTRATTO ;
   aOldRowtype.CD_CIG                			:= :Old.CD_CIG                ;
   aOldRowtype.CD_CUP                			:= :Old.CD_CUP                ;
   aOldRowtype.CD_CIG_FATTURA_ATTIVA 			:= :Old.CD_CIG_FATTURA_ATTIVA ;

   -- Scarico dello storico
   CNRSTO010.scaricaSuStorico(:new.PG_VER_REC, 'STOPRC', aOldRowType);
End;
/


