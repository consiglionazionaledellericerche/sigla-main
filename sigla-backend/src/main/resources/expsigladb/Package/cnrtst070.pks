CREATE OR REPLACE PACKAGE CNRTST070 AS
--
-- CNRTST070 - Package per la generazione di utenze fittizie per gestione TESTS
--
-- Date: 25/03/2002
-- Version: 1.0
--
--
-- Dependency: CNRCTB 001/060 IBMCST001 IBMUTL001
--
-- History:
-- Date: 25/03/2002
-- Version: 1.0
-- Creazione
--
-- Constants:

-- Functions & Procedures:
--
-- Creazione di utenti secondo la seguente specifica:
--
-- Per ogni CDS viene creato un gestore
-- Sotto ogni gestore viene creato un utente per ogni CDR con nome = al CDR e utente template con tutti gli accessi sull'UO di competenza
--

 procedure creaUtentiTest;

end;


CREATE OR REPLACE PACKAGE BODY CNRTST070 AS

 procedure creaUtentiTest is
  aUtente utente%rowtype;
  aUser CONSTANT varchar2(20):=IBMCST001.TESTRECORD_IDENTIFIER;
  aPgVerRec CONSTANT NUMBER:=IBMCST001.TESTRECORD_PG_VER_REC;
  aTSNow date;
  aCdGestore varchar2(20);
  aCdUtente varchar2(20);
 begin
  aTSNow:=sysdate;
  for aCDS in (select * from unita_organizzativa where fl_cds = 'Y') loop
   begin
    aCdGestore:='GESTORE'||IBMUTL001.strip(aCDS.cd_unita_organizzativa,'.');
    insert into UTENTE (
      CD_UTENTE
     ,DS_UTENTE
     ,FL_UTENTE_TEMPL
     ,COGNOME
     ,CD_GESTORE
     ,PASSWORD
     ,FL_PASSWORD_CHANGE
     ,CD_CDR
     ,CD_CDS_CONFIGURATORE
     ,DT_INIZIO_VALIDITA
     ,DT_FINE_VALIDITA
     ,TI_UTENTE
     ,NOME
     ,INDIRIZZO
     ,DUVA
     ,CD_UTENTE_TEMPL
     ,DT_ULTIMA_VAR_PASSWORD
     ,UTUV
     ,DACR
     ,UTCR
     ,PG_VER_REC
    ) values (
      aCdGestore
     ,'GESTORE UTENZE CDS'||aCDS.cd_unita_organizzativa
     ,null
     ,null
     ,'SUPERUTENTE'
     ,null
     ,'N'
     ,null
     ,aCDS.cd_unita_organizzativa
     ,sysdate-1
     ,sysdate+1000
     ,'A'
     ,null
     ,null
     ,aTSNow
     ,NULL
     ,NULL
     ,aUser
     ,aTSNow
     ,aUser
 	 ,aPgVerRec
     );
     for aAccesso in (select * from accesso where ti_accesso = 'A') loop
             insert into UTENTE_UNITA_ACCESSO (
               CD_UTENTE
              ,CD_UNITA_ORGANIZZATIVA
              ,CD_ACCESSO
              ,UTUV
              ,DACR
              ,UTCR
              ,DUVA
              ,PG_VER_REC
             ) values (
               aCdGestore
              ,'*'
              ,aAccesso.cd_accesso
              ,aUser
              ,aTSNow
              ,aUser
              ,aTSNow
              ,aPgVerRec
              );
     end loop;
     for aCDR in (select * from cdr where substr(cd_centro_responsabilita,1,3) = aCDS.cd_unita_organizzativa) loop
      begin
       aCdUtente:=IBMUTL001.strip(aCDR.cd_centro_responsabilita,'.');
       insert into UTENTE (
         CD_UTENTE
        ,DS_UTENTE
        ,FL_UTENTE_TEMPL
        ,COGNOME
        ,CD_GESTORE
        ,PASSWORD
        ,FL_PASSWORD_CHANGE
        ,CD_CDR
        ,CD_CDS_CONFIGURATORE
        ,DT_INIZIO_VALIDITA
        ,DT_FINE_VALIDITA
        ,TI_UTENTE
        ,NOME
        ,INDIRIZZO
        ,DUVA
        ,CD_UTENTE_TEMPL
        ,DT_ULTIMA_VAR_PASSWORD
        ,UTUV
        ,DACR
        ,UTCR
        ,PG_VER_REC
       ) values (
         aCdUtente
        ,'UTENTE CDR '||aCDR.cd_centro_responsabilita
        ,'N'
        ,aCDR.ds_cdr
        ,aCdGestore
        ,null
        ,'N'
        ,aCDR.cd_centro_responsabilita
        ,null
        ,sysdate-1
        ,sysdate+1000
        ,'U'
        ,'UTENTE CDR '||aCDR.cd_centro_responsabilita
        ,null
        ,aTSNow
        ,NULL
        ,NULL
        ,aUser
        ,aTSNow
        ,aUser
    	,aPgVerRec
       );       
       for aAccesso in (select * from accesso where ti_accesso = 'D') loop
         begin
             insert into UTENTE_UNITA_ACCESSO (
               CD_UTENTE
              ,CD_UNITA_ORGANIZZATIVA
              ,CD_ACCESSO
              ,UTUV
              ,DACR
              ,UTCR
              ,DUVA
              ,PG_VER_REC
             ) values (
               aCdUtente
              ,aCDR.CD_UNITA_ORGANIZZATIVA
              ,aAccesso.cd_accesso
              ,aUser
              ,aTSNow
              ,aUser
              ,aTSNow
              ,aPgVerRec
              );
	     exception when dup_val_on_index then
          null;
	     end;
	   end loop;
	  exception when dup_val_on_index then
       null;
	  end;
     end loop;
   exception when dup_val_on_index then
    null;
   end;
  end loop;
 end;

end;


