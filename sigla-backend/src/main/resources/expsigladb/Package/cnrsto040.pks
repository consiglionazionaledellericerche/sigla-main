CREATE OR REPLACE package CNRSTO040 as
--
-- CNRSTO040 - Package per la gestione dello storico ACCERTAMENTO
-- Date: 19/07/2006
-- Version: 1.3
--
--
-- Dependency:
--
-- History:
-- Date: 06/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/05/2002
-- Version: 1.1
-- Lo storico non viene annulato ad ogni scarico
--
-- Date: 10/06/2003
-- Version: 1.2
-- Modificato metodo di insert su accertamento_scadenzario_s
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO
--
-- Constants:

-- Functions and Procedures:

-- Procedura di scarico dell'accertamento sullo storico
--
-- aAcc deve contenere un rowtype completo dell'accertamento (= record :new)
-- aOldAcc deve contenere un rowtype completo di accertamento (= record :old)

 procedure scaricaSuStorico(aDesc varchar2, aOldAcc accertamento%rowtype, aAcc accertamento%rowtype);

 -- Procedure di inserimento nelle tabella di storico dell'accertamento
 -- Viene chiamata da trigger BU_ACCERTAMENTO

 procedure sto_ACCERTAMENTO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO%rowtype);
 procedure sto_ACCERTAMENTO_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCADENZARIO%rowtype);
 procedure sto_ACCERTAMENTO_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCAD_VOCE%rowtype);
 procedure sto_ACCERTAMENTO_PGIRO_MODIF (aPgStorico number, imp_iniziale NUMBER, imp_variazione NUMBER,aDest ACCERTAMENTO%rowtype);

end;
/


CREATE OR REPLACE package body         CNRSTO040 is
 procedure scaricaSuStorico(aDesc varchar2, aOldAcc accertamento%rowtype, aAcc accertamento%rowtype) is
   imp_variazione		number(15,2):=0;
   imp_iniziale     number(15,2):=0;
 begin
  -- Lo storico non viene scaricato se l'accertamento e temporaneo
  if aOldAcc.cd_tipo_documento_cont like '%$' then
   return;
  end if;
  sto_ACCERTAMENTO (aAcc.pg_ver_rec, aDesc, aOldAcc);
  if ((aOldAcc.CD_TIPO_DOCUMENTO_CONT = 'ACR_RES' or aOldAcc.CD_TIPO_DOCUMENTO_CONT = 'ACR_PGIR_R') and aOldAcc.FL_PGIRO ='Y'
     and ((aOldAcc.cd_elemento_voce !=aAcc.cd_elemento_voce ) or ( aOldAcc.im_accertamento != aAcc.im_accertamento))) then
	 	   imp_variazione :=aAcc.IM_ACCERTAMENTO-aOldAcc.IM_ACCERTAMENTO;
	     begin
	     -- il valore ? sempre quello originale dell'ACCERTAMENTO
	     select im_ORIGINE into imp_iniziale
	     from ACCERTAMENTO_PGIRO_MODIFICA
	 			where
	 		  cd_cds 	  	        = aOldAcc.CD_CDS            	and
	   	  esercizio 	   		  = aOldAcc.ESERCIZIO						and
     	  esercizio_originale = aOldAcc.ESERCIZIO_ORIGINALE and
	   	  pg_ACCERTAMENTO     = aOldAcc.PG_ACCERTAMENTO     and
	   	  rownum = 1;
	   	exception when no_data_found THEN
	   	   imp_iniziale :=aOldAcc.im_ACCERTAMENTO;
	   	end;
	     sto_ACCERTAMENTO_PGIRO_MODIF(aAcc.pg_ver_rec, imp_iniziale,imp_variazione, aOldAcc);
	end if;
  for aScad in (select * from accertamento_scadenzario where
       cd_cds = aOldAcc.cd_cds
   and esercizio = aOldAcc.esercizio
   and esercizio_originale = aOldAcc.esercizio_originale
   and pg_accertamento = aOldAcc.pg_accertamento) loop
   sto_ACCERTAMENTO_SCADENZARIO (aAcc.pg_ver_rec, aDesc, aScad);
  end loop;
  for aDett in (select * from accertamento_scad_voce where
       cd_cds = aOldAcc.cd_cds
   and esercizio = aOldAcc.esercizio
   and esercizio_originale = aOldAcc.esercizio_originale
   and pg_accertamento = aOldAcc.pg_accertamento
  ) loop
   sto_ACCERTAMENTO_SCAD_VOCE (aAcc.pg_ver_rec, aDesc, aDett);
  end loop;
 end;

 procedure sto_ACCERTAMENTO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO%rowtype) is
  begin
   insert into ACCERTAMENTO_S (
     pg_storico_
    ,ds_storico_
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,CD_TIPO_DOCUMENTO_CONT
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_VOCE
    ,DT_REGISTRAZIONE
    ,DS_ACCERTAMENTO
    ,NOTE_ACCERTAMENTO
    ,CD_TERZO
    ,IM_ACCERTAMENTO
    ,DT_CANCELLAZIONE
    ,CD_RIFERIMENTO_CONTRATTO
    ,DT_SCADENZA_CONTRATTO
    ,CD_FONDO_RICERCA
    ,FL_PGIRO
    ,RIPORTATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS_ORI_RIPORTO
    ,ESERCIZIO_ORI_RIPORTO
    ,ESERCIZIO_ORI_ORI_RIPORTO
    ,PG_ACCERTAMENTO_ORI_RIPORTO
    ,ESERCIZIO_COMPETENZA
    ,FL_CALCOLO_AUTOMATICO
    ,ESERCIZIO_CONTRATTO
    ,STATO_CONTRATTO
    ,PG_CONTRATTO
    ,FL_NETTO_SOSPESO
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_ACCERTAMENTO
    ,aDest.NOTE_ACCERTAMENTO
    ,aDest.CD_TERZO
    ,aDest.IM_ACCERTAMENTO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_RIFERIMENTO_CONTRATTO
    ,aDest.DT_SCADENZA_CONTRATTO
    ,aDest.CD_FONDO_RICERCA
    ,aDest.FL_PGIRO
    ,aDest.RIPORTATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_ORI_RIPORTO
    ,aDest.PG_ACCERTAMENTO_ORI_RIPORTO
    ,aDest.ESERCIZIO_COMPETENZA
    ,aDest.FL_CALCOLO_AUTOMATICO
    ,aDest.ESERCIZIO_CONTRATTO
    ,aDest.STATO_CONTRATTO
    ,aDest.PG_CONTRATTO
    ,aDest.FL_NETTO_SOSPESO
    );
 end;
 procedure sto_ACCERTAMENTO_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCADENZARIO%rowtype) is
  begin
   insert into ACCERTAMENTO_SCADENZARIO_S (
     pg_storico_
    ,ds_storico_
    ,PG_ACC_SCAD_ORI_RIPORTO
    ,IM_SCADENZA
    ,IM_ASSOCIATO_DOC_AMM
    ,IM_ASSOCIATO_DOC_CONTABILE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DT_SCADENZA_EMISSIONE_FATTURA
    ,DT_SCADENZA_INCASSO
    ,DS_SCADENZA
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.PG_ACC_SCAD_ORI_RIPORTO
    ,aDest.IM_SCADENZA
    ,aDest.IM_ASSOCIATO_DOC_AMM
    ,aDest.IM_ASSOCIATO_DOC_CONTABILE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DT_SCADENZA_EMISSIONE_FATTURA
    ,aDest.DT_SCADENZA_INCASSO
    ,aDest.DS_SCADENZA
    );
 end;
 procedure sto_ACCERTAMENTO_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCAD_VOCE%rowtype) is
  begin
   insert into ACCERTAMENTO_SCAD_VOCE_S (
     pg_storico_
    ,ds_storico_
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,IM_VOCE
    ,CD_FONDO_RICERCA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.IM_VOCE
    ,aDest.CD_FONDO_RICERCA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
  procedure sto_ACCERTAMENTO_PGIRO_MODIF (aPgStorico number, imp_iniziale number,imp_variazione number, aDest ACCERTAMENTO%rowtype) is
  begin
   insert into ACCERTAMENTO_PGIRO_MODIFICA (
	  CD_CDS,
		ESERCIZIO,
		PG_ACCERTAMENTO,
		ESERCIZIO_ORIGINALE,
		IM_ORIGINE,
		IM_VARIAZIONE,
		CD_ELEMENTO_VOCE,
		DACR,
		UTCR,
		DUVA,
		UTUV,
		PG_VER_REC,
		PG_STORICO_
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORIGINALE
    ,imp_iniziale
    ,imp_variazione
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aPgStorico
    );
 end;
end;
/


