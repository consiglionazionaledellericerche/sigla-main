CREATE OR REPLACE PROCEDURE S_ANA_PROGETTI(
                                           inUO           In VARCHAR2,
                                           inProgetto     In VARCHAR2,
                                           inLivello      In NUMBER,
                                           inStato        In VARCHAR2,
                                           inEsercizio    In NUMBER,
                                           inFase         In VARCHAR2) Is
  Cursor C Is
    Select PROGETTO.*,
           CNRUTL001.getValore('TERZO','DENOMINAZIONE_SEDE',PROGETTO.CD_RESPONSABILE_TERZO) DS_RESPONSABILE_TERZO,
           CNRUTL001.getValore('TIPO_PROGETTO','DS_TIPO_PROGETTO',PROGETTO.CD_TIPO_PROGETTO) DS_TIPO_PROGETTO,
           CNRUTL001.getValore('UNITA_ORGANIZZATIVA','DS_UNITA_ORGANIZZATIVA',PROGETTO.CD_UNITA_ORGANIZZATIVA) DS_UNITA_ORGANIZZATIVA
    From   PROGETTO
    Where  PROGETTO.LIVELLO <= inLivello
      And  PROGETTO.STATO = Decode(inStato,'*',PROGETTO.STATO,inStato)
      And  PROGETTO.TIPO_FASE = Decode(inFase,'E',PROGETTO.TIPO_FASE,inFase)
      And  PROGETTO.TIPO_FASE !='X'
      And  PROGETTO.ESERCIZIO = inEsercizio
      And  ((inUO != '*' And Exists (Select 1 From PROGETTO_UO
                   Where PROGETTO_UO.PG_PROGETTO = PROGETTO.PG_PROGETTO
                     And PROGETTO_UO.CD_UNITA_ORGANIZZATIVA =
                       Decode(CNRUTL001.getValore('UNITA_ORGANIZZATIVA','CD_TIPO_UNITA',inUO),
                              'ENTE',PROGETTO_UO.CD_UNITA_ORGANIZZATIVA,inUO))) Or (inUO = '*'))
      Start With ((inProgetto = '*' And Progetto.pg_progetto_padre Is Null) Or
                  (inProgetto != '*' And Progetto.pg_progetto = CNRUTL001.getPgProgettoPadre(Null,inProgetto,inEsercizio,progetto.tipo_fase)
                   And Progetto.esercizio = inEsercizio ))
      Connect By Prior (pg_progetto||esercizio||tipo_fase)= (pg_progetto_padre||esercizio_progetto_padre||tipo_fase_progetto_padre);
  Indice NUMBER;
  aId    NUMBER;
Begin

  indice := 0;
  select IBMSEQ00_CR_PACKAGE.nextval
  into aId
  from dual;
  For Rec In C Loop
    Dbms_Output.PUT_LINE('PP '||REC.PG_PROGETTO||' '||REC.PG_PROGETTO_PADRE);
    indice := indice + 10;
    Insert Into VPG_ANA_PROGETTI ( ID, CHIAVE, TIPO, SEQUENZA,
                                   PG_PROGETTO, PG_PROGETTO_PADRE, CD_PROGETTO,
                                   DS_PROGETTO, CD_TIPO_PROGETTO, CD_UNITA_ORGANIZZATIVA,
                                   CD_RESPONSABILE_TERZO, DT_INIZIO, DT_FINE, DT_PROROGA,
                                   IMPORTO_PROGETTO, IMPORTO_DIVISA, CD_DIVISA, NOTE,
                                   STATO, CONDIVISO, DURATA_PROGETTO, LIVELLO,
                                   DS_RESPONSABILE_TERZO, DS_TIPO_PROGETTO, DS_UNITA_ORGANIZZATIVA,TIPO_FASE)
                           Values( aId, 'chiave', 't', indice,
                                   Rec.PG_PROGETTO, Rec.PG_PROGETTO_PADRE, Rec.CD_PROGETTO,
                                   Rec.DS_PROGETTO, Rec.CD_TIPO_PROGETTO, Rec.CD_UNITA_ORGANIZZATIVA,
                                   Rec.CD_RESPONSABILE_TERZO, Rec.DT_INIZIO, Rec.DT_FINE, Rec.DT_PROROGA,
                                   Rec.IMPORTO_PROGETTO, Rec.IMPORTO_DIVISA, Rec.CD_DIVISA, Rec.NOTE,
                                   Rec.STATO, Rec.CONDIVISO, Rec.DURATA_PROGETTO, Rec.LIVELLO,
                                   Rec.DS_RESPONSABILE_TERZO, Rec.DS_TIPO_PROGETTO, Rec.DS_UNITA_ORGANIZZATIVA,rec.TIPO_FASE);
  End Loop;

End;
/


