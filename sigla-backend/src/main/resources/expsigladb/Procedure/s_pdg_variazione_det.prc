CREATE OR REPLACE PROCEDURE S_PDG_VARIAZIONE_DET(
  c_esercizio in number,
  c_cds in varchar2,
  c_uo in varchar2,
  c_stato in varchar2,
  c_id_report in varchar2
  )
As
  Cursor cur_c (esercizio_variazione in number, numero_variazione in number, codice_cdr_ass in varchar2) Is
    Select ti_appartenenza,cd_linea_attivita,id_classificazione,sum(variazione) variazione, ti_gestione
    From (Select s.ti_appartenenza, s.cd_linea_attivita, s.ti_gestione, e.id_classificazione,
                 Nvl(Sum(Im_ri_ccs_spese_odc+
                         Im_rj_ccs_spese_odc_altra_uo+
                         Im_rk_ccs_spese_ogc+
                         Im_rl_ccs_spese_ogc_altra_uo+
                         Im_rq_ssc_costi_odc+
                         Im_rr_ssc_costi_odc_altra_uo+
                         Im_rs_ssc_costi_ogc+
                         Im_rt_ssc_costi_ogc_altra_uo),0) variazione
          From pdg_preventivo_spe_det s, elemento_voce e
          Where cd_centro_responsabilita = codice_cdr_ass
          And   esercizio_pdg_variazione = esercizio_variazione
          And   pg_variazione_pdg = numero_variazione
          And   s.esercizio = e.esercizio
          And   s.ti_appartenenza = e.ti_appartenenza
          And   s.ti_gestione = e.ti_gestione
          And   s.cd_elemento_voce = e.cd_elemento_voce
          Group By s.ti_appartenenza,e.id_classificazione,s.cd_linea_attivita,s.ti_gestione
          Union
          Select s.ti_appartenenza,s.cd_linea_attivita,s.ti_gestione,e.id_classificazione,
                 Nvl(Sum(s.Im_ra_rce+s.Im_rc_esr),0) variazione
          From pdg_preventivo_etr_det s, elemento_voce e
          Where cd_centro_responsabilita = codice_cdr_ass
          And   esercizio_pdg_variazione = esercizio_variazione
          And   pg_variazione_pdg = numero_variazione
          And   s.esercizio = e.esercizio
          And   s.ti_appartenenza = e.ti_appartenenza
          And   s.ti_gestione = e.ti_gestione
          And   s.cd_elemento_voce = e.cd_elemento_voce
          Group By s.ti_appartenenza,e.id_classificazione,s.cd_linea_attivita,s.ti_gestione
          Union
          Select s.ti_appartenenza,s.cd_linea_attivita,s.ti_gestione,e.id_classificazione,
                 Nvl(Sum(Nvl(s.Im_spese_gest_decentrata_int, 0) +
                         Nvl(s.Im_spese_gest_decentrata_est, 0) +
                         Nvl(s.Im_spese_gest_accentrata_int, 0) +
                         Nvl(s.Im_spese_gest_accentrata_est, 0) +
                         Nvl(s.Im_entrata, 0)),0) variazione
          From pdg_variazione_riga_gest s, elemento_voce e
          Where s.cd_cdr_assegnatario = codice_cdr_ass
          And   s.esercizio = esercizio_variazione
          And   s.pg_variazione_pdg = numero_variazione
          And   s.esercizio = e.esercizio
          And   s.ti_appartenenza = e.ti_appartenenza
          And   s.ti_gestione = e.ti_gestione
          And   s.cd_elemento_voce = e.cd_elemento_voce
          And   s.categoria_dettaglio != 'SCR'
          Group By s.ti_appartenenza,e.id_classificazione,s.cd_linea_attivita,s.ti_gestione)
    Group By ti_appartenenza,ti_gestione,id_classificazione,cd_linea_attivita;

  seq               NUMBER:=0;
  seq_ID            NUMBER:=0;
  Esegui_Cur1       INTEGER;
  Cur1              INTEGER;
  Stringa           LONG;
  Apice             VARCHAR2(4) := '''';
  d_esercizio       NUMBER(4);
  d_numero_var      NUMBER(9);
  d_ds_var          VARCHAR2(500);
  d_cdr_resp        VARCHAR2(30);
  d_cdr_ass         VARCHAR2(30);
  d_stato           VARCHAR2(3);
  ds_cdr            VARCHAR2(500);
  ds_cdr_resp       VARCHAR2(500);
  des_ev            VARCHAR2(100);
  ds_titolo         VARCHAR2(250);
  ds_categoria      VARCHAR2(250);
  titolo            VARCHAR2(4);
  categoria         VARCHAR2(4);
  d_id_dip          NUMBER(3);
  d_cd_dipartimento VARCHAR2(15);
  d_ds_dipartimento VARCHAR2(800);
  ass_etr           NUMBER:=0;
  ass_spe           NUMBER:=0;
  ass_etr_dip       NUMBER:=0;
  ass_etr_tit       NUMBER:=0;
  ass_spe_dip       NUMBER:=0;
  ass_spe_tit       NUMBER:=0;
  b_d_numero_var    NUMBER:=0;
  ass_dip           NUMBER:=0;
  ass_tit           NUMBER:=0;
  ti_gestione       VARCHAR2(4);
  a                 NUMBER :=0;
  recParametri      CNRCTB056.cParametri;
  reg2006           parametri_cnr.fl_regolamento_2006%Type;
Begin
  Select IBMSEQ00_CR_PACKAGE.nextval
  Into seq_ID
  From dual;

  For recVar In (Select a.esercizio,
                        a.pg_variazione_pdg,
                        Substr(a.ds_variazione,1,490) ds_variazione,
                        a.cd_centro_responsabilita cdr_resp,
                        b.cd_centro_responsabilita cdr_ass,
                        a.stato
                 From PDG_VARIAZIONE a, ASS_PDG_VARIAZIONE_CDR b, CDR c, UNITA_ORGANIZZATIVA d
                 Where a.esercizio = b.esercizio
                 And   a.pg_variazione_pdg = b.pg_variazione_pdg
                 And   a.cd_centro_responsabilita = c.cd_centro_responsabilita
                 And   c.cd_unita_organizzativa = d.cd_unita_organizzativa
                 And   a.esercizio = c_esercizio
                 And   d.cd_unita_padre = Decode(c_cds, '*', d.cd_unita_padre, c_cds)
                 And   d.cd_unita_organizzativa = Decode(c_uo, '*', d.cd_unita_organizzativa, c_uo)
                 And   a.stato = Decode(c_stato, '*', a.stato, c_stato)
                 And   a.pg_variazione_pdg in (Select pg_variazione
                                               From V_STM_PARAMIN_PDG_VARIAZIONE
                                               Where id_report = c_id_report)
                 Order By a.esercizio, a.pg_variazione_pdg) Loop

     d_esercizio  := recVar.esercizio;
     d_numero_var := recVar.pg_variazione_pdg;
     d_ds_var     := recVar.ds_variazione;
     d_cdr_resp   := recVar.cdr_resp;
     d_cdr_ass    := recVar.cdr_ass;
     d_stato      := recVar.stato;

     For rec_c In cur_c(d_esercizio, d_numero_var, d_cdr_ass) Loop
        seq := seq + 1;
        Begin
          Select Dipartimento.cd_dipartimento, Dipartimento.ds_dipartimento,dip_peso.peso
          Into  d_cd_dipartimento, d_ds_dipartimento,d_id_dip
          From Dipartimento, Progetto_gest Progetto, Progetto_gest Commessa, Progetto_gest Modulo, Linea_attivita,dipartimento_peso dip_peso
          Where Linea_attivita.Cd_centro_responsabilita = d_cdr_ass
          And   Linea_attivita.Cd_linea_attivita = rec_c.Cd_linea_attivita
          And   Linea_attivita.pg_progetto = Modulo.pg_progetto
          And   Modulo.esercizio = d_esercizio
          And   Modulo.esercizio_progetto_padre = Commessa.esercizio
          And   Modulo.pg_progetto_padre = Commessa.pg_progetto
          And   Commessa.esercizio_progetto_padre = Progetto.esercizio
          And   Commessa.pg_progetto_padre = Progetto.pg_progetto
          And   Progetto.Cd_dipartimento = Dipartimento.Cd_dipartimento
          and   dip_peso.esercizio = d_esercizio
          and   dip_peso.Cd_dipartimento = dipartimento.Cd_dipartimento;
        Exception
          When no_data_found then
            d_cd_dipartimento:=null;
            d_ds_dipartimento:=null;
            d_id_dip:=null;
        End;

        Select ds_cdr
        Into ds_cdr_resp
        From cdr
        Where cd_centro_responsabilita = d_cdr_resp;

        Select ds_cdr
        Into ds_cdr
        From cdr
        Where cd_centro_responsabilita = d_cdr_ass;

        Select cd_livello1, cd_livello2
        Into titolo, categoria
        From v_classificazione_voci
        Where id_classificazione = rec_c.id_classificazione;

        Select ds_classificazione
        Into ds_titolo
        From v_classificazione_voci
        Where cd_livello1 = titolo
        And   esercizio = d_esercizio
        And   ti_gestione = rec_c.ti_gestione
        And   nr_livello = 1;

        Select ds_classificazione
        Into ds_categoria
        From v_classificazione_voci
        Where cd_livello1 = titolo
        And   cd_livello2 = categoria
        And   esercizio = d_esercizio
        And   ti_gestione = rec_c.ti_gestione
        And   nr_livello = 2;

        If b_d_numero_var != d_numero_var Then
          Insert Into VPG_PDG_VARIAZIONE_DET
                 (ID,CHIAVE,TIPO,SEQUENZA,ESERCIZIO,PG_VARIAZIONE_PDG,DS_VARIAZIONE_PDG,
                  CD_CDR_PROPONENTE,DS_CDR_PROPONENTE,CD_CDR,DS_CDR,TI_GESTIONE,
                  TITOLO,DS_TITOLO,CATEGORIA,DS_CATEGORIA,STATO,
                  VARIAZIONE,ASSESTATO_ENTRATA,ASSESTATO_SPESA,ID_DIPARTIMENTO,
                  CD_DIPARTIMENTO,DS_DIPARTIMENTO)
          Values (seq_ID,'I', 'I',seq,d_esercizio,d_numero_var,d_ds_var,d_cdr_resp,ds_cdr_resp,d_cdr_ass,
                  ds_cdr,null,null,null,null,null,d_stato,null,null,null,
                  d_id_dip,
                  null,null);
          seq:=seq+1;
        End If;

        Insert Into VPG_PDG_VARIAZIONE_DET
               (ID,
                CHIAVE,
                TIPO,
                SEQUENZA,
                ESERCIZIO,
                PG_VARIAZIONE_PDG,
                DS_VARIAZIONE_PDG,
                CD_CDR_PROPONENTE,
                DS_CDR_PROPONENTE,
                CD_CDR,
                DS_CDR,
                TI_GESTIONE,
                TITOLO,
                DS_TITOLO,
                CATEGORIA,
                DS_CATEGORIA,
                STATO,
                VARIAZIONE,
                ASSESTATO_ENTRATA,
                ASSESTATO_SPESA,
                ID_DIPARTIMENTO,
                CD_DIPARTIMENTO,
                DS_DIPARTIMENTO)
         Values
               (seq_ID,
                'Dettagli',
                'D',
                seq,
                d_esercizio,
                d_numero_var,
                d_ds_var,
                d_cdr_resp,
                ds_cdr_resp,
                d_cdr_ass,
                ds_cdr,
                rec_c.ti_gestione,
                titolo,
                ds_titolo,
                categoria,
                ds_categoria,
                d_stato,
                rec_c.variazione,
                ass_etr,
                ass_spe,
                d_id_dip,
                d_cd_dipartimento,
                d_ds_dipartimento);
         b_d_numero_var := d_numero_var;
     End Loop;
   End Loop;

   Begin
     Select fl_regolamento_2006
     Into reg2006
     From parametri_cnr
     Where esercizio = d_esercizio;
   Exception
     When Others Then
       reg2006 := 'N';
   End;

   seq:=1000;
   For rec In (Select distinct a.ESERCIZIO,
                      a.TI_GESTIONE,a.TITOLO,a.DS_TITOLO,a.CATEGORIA,a.DS_CATEGORIA,
                      b.VARIAZIONE,a.ASSESTATO_ENTRATA,a.ASSESTATO_SPESA,
                      a.CD_DIPARTIMENTO,a.DS_DIPARTIMENTO
               From VPG_PDG_VARIAZIONE_DET a,
                    (Select Sum(VARIAZIONE) variazione,TI_GESTIONE,CD_DIPARTIMENTO,TITOLO,CATEGORIA,ID
                     From VPG_PDG_VARIAZIONE_DET
                     Where tipo='D'
                     Group By TI_GESTIONE,CD_DIPARTIMENTO,TITOLO,CATEGORIA,ID) b
               Where a.ID = seq_ID
               And   a.TI_GESTIONE = b.TI_GESTIONE
               And   a.CD_DIPARTIMENTO= b.CD_DIPARTIMENTO
               And   a.TITOLO = b.titolo
               And   a.CATEGORIA = b.CATEGORIA
               And   a.ID = b.ID) Loop
      seq:= seq+1;

      If reg2006 = 'N' Then
         Stringa := Null;
         If rec.ti_gestione = 'E' Then
            If c_id_report Is Not Null Then
              Stringa := 'v_cons_pdg_etr_assestato.pg_variazione_pdg in (Select pg_variazione '||
                                                                        'From V_STM_PARAMIN_PDG_VARIAZIONE '||
                                                                        'Where id_report = '||c_id_report||')';
            End If;
            ass_etr := CAL_ASSESTATO.ASSESTATO_ENTRATA(rec.ESERCIZIO,rec.CD_DIPARTIMENTO,null,null,null,null,null,Null,rec.TITOLO,rec.CATEGORIA,null,null,null,null,null,Stringa);
            ass_spe := 0;
         Else
            If c_id_report Is Not Null Then
              Stringa := 'v_cons_pdg_spe_assestato.pg_variazione_pdg in (Select pg_variazione '||
                                                                        'From V_STM_PARAMIN_PDG_VARIAZIONE '||
                                                                        'Where id_report = '||c_id_report||')';
            End If;
            ass_etr := 0;
            ass_spe := CAL_ASSESTATO.ASSESTATO_SPESA(rec.ESERCIZIO,rec.CD_DIPARTIMENTO,null,null,null,null,null,Null,rec.TITOLO,rec.CATEGORIA,null,null,null,null,null,Stringa);
         End If;
      Else
         recParametri.aEsercizio := rec.ESERCIZIO;
         recParametri.aTiGestione := rec.TI_GESTIONE;
         recParametri.aCdDipartimento := rec.CD_DIPARTIMENTO;
         recParametri.aCdLiv1 := rec.TITOLO;
         recParametri.aCdLiv2 := rec.CATEGORIA;
         recParametri.aStatoVar := 'APF';
         recParametri.aStringa := Null;

         If rec.ti_gestione = 'E' Then
            ass_etr := CNRCTB056.getAssestatoGestionale(recParametri);
            If c_id_report Is Not Null Then
--              recParametri.aStatoVar := Null;
              recParametri.aStringa := 'V_CONS_PDG_VAR_GESTIONALE.PG_VARIAZIONE_PDG IN (Select pg_variazione '||
                                                                                       'From V_STM_PARAMIN_PDG_VARIAZIONE '||
                                                                                       'Where id_report = '||c_id_report||')';
              ass_etr := ass_etr - CNRCTB056.getVariazioniGestionale(recParametri);
            End If;
            ass_spe := 0;
         Else
            ass_etr := 0;
            ass_spe := CNRCTB056.getAssestatoGestionale(recParametri);
            If c_id_report Is Not Null Then
--              recParametri.aStatoVar := Null;
              recParametri.aStringa := 'V_CONS_PDG_VAR_GESTIONALE.PG_VARIAZIONE_PDG IN (Select pg_variazione '||
                                                                                       'From V_STM_PARAMIN_PDG_VARIAZIONE '||
                                                                                       'Where id_report = '||c_id_report||')';
              ass_spe := ass_spe - CNRCTB056.getVariazioniGestionale(recParametri);
            End If;
         End If;
      End If;

      Insert into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,'Dettagli','B',seq,rec.ESERCIZIO,null,null,Null,null,null,null,rec.TI_GESTIONE,
             rec.TITOLO,rec.DS_TITOLO,rec.CATEGORIA,rec.DS_CATEGORIA,null,rec.VARIAZIONE,ass_etr,ass_spe,
             (select peso from dipartimento_peso p where p.esercizio = rec.esercizio and p.cd_dipartimento= rec.cd_dipartimento),
             rec.CD_DIPARTIMENTO,rec.DS_DIPARTIMENTO);
   End Loop;

   Delete From VPG_PDG_VARIAZIONE_DET
   Where ID = seq_ID
   And   tipo = 'D';

   a:=2000;
   For rec In (Select Sum(a.ASSESTATO_ENTRATA) ass_dip, a.esercizio, a.cd_dipartimento,  b.variazione
               From (Select distinct ASSESTATO_ENTRATA, esercizio, cd_dipartimento
                     From VPG_PDG_VARIAZIONE_DET
                     Where ID = seq_ID
                     And   ti_gestione = 'E'
                     And   tipo = 'B'
                     And   titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento
                                                   From VPG_PDG_VARIAZIONE_DET
                                                   Where ID = seq_ID
                                                   And   ti_gestione = 'E'
                                                   And   tipo = 'B'
                                                   And   titolo is not null
                                                   And   categoria is not null
                                                   Group By esercizio, cd_dipartimento) b
               Where a.esercizio = b.esercizio
               And   a.cd_dipartimento= b.cd_dipartimento
               Group By a.esercizio, a.cd_dipartimento,  b.variazione
               Union
               Select sum(ass_dip), esercizio, null, sum(variazione)
               From (Select Sum(a.ASSESTATO_ENTRATA) ass_dip, a.esercizio, a.cd_dipartimento,  b.variazione
                     From (Select Distinct ASSESTATO_ENTRATA, esercizio, cd_dipartimento
                           From VPG_PDG_VARIAZIONE_DET
                           Where ID = seq_ID
                           And ti_gestione = 'E'
                           And tipo = 'B'
                           And titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento
                                                       From VPG_PDG_VARIAZIONE_DET
                                                       Where ID = seq_ID
                                                       And   ti_gestione = 'E'
                                                       And   tipo = 'B'
                                                       And   titolo is not null
                                                       And   categoria is not null
                                                       Group By esercizio, cd_dipartimento) b
                           Where a.esercizio = b.esercizio
                           And a.cd_dipartimento= b.cd_dipartimento
                           Group By  a.esercizio, a.cd_dipartimento,  b.variazione)
                Group by esercizio) Loop

      a:=a+1;
      Insert into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,decode(rec.cd_dipartimento,null,'TOT','DIP'),'B',a, rec.esercizio,Null,Null,Null,Null,Null,Null,'E',
             Null,Null,Null,Null,Null,rec.variazione,rec.ass_dip,0,
             (select peso from dipartimento_peso p where p.esercizio = rec.esercizio and p.cd_dipartimento= rec.cd_dipartimento),
             rec.CD_DIPARTIMENTO,null);
   End loop;

   a:=3000;
   For rec In (Select Sum(a.ASSESTATO_SPESA) ass_dip, a.esercizio, a.cd_dipartimento,  b.variazione
               From (Select Distinct ASSESTATO_SPESA, esercizio, cd_dipartimento
                     From VPG_PDG_VARIAZIONE_DET
                     Where ID = seq_ID
                     And ti_gestione = 'S'
                     And tipo = 'B'
                     And titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento
                                                 From VPG_PDG_VARIAZIONE_DET
                                                 Where ID = seq_ID
                                                 And   ti_gestione = 'S'
                                                 And   tipo = 'B'
                                                 And   titolo is not null
                                                 And   categoria is not null
                                                 Group By esercizio, cd_dipartimento) b
                     Where a.esercizio = b.esercizio
                     And   a.cd_dipartimento= b.cd_dipartimento
                     Group By  a.esercizio, a.cd_dipartimento,  b.variazione
                     Union
                     Select Sum(ass_dip), esercizio, null, sum(variazione)
                     From (Select Sum(a.ASSESTATO_SPESA) ass_dip, a.esercizio, a.cd_dipartimento,  b.variazione
                           From (Select Distinct ASSESTATO_SPESA, esercizio, cd_dipartimento
                                 From VPG_PDG_VARIAZIONE_DET
                                 Where ID = seq_ID
                                 And   ti_gestione = 'S'
                                 And   tipo = 'B'
                                 And   titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento
                                                               From VPG_PDG_VARIAZIONE_DET
                                                               Where ID = seq_ID
                                                               And   ti_gestione = 'S'
                                                               And   tipo = 'B'
                                                               And   titolo is not null
                                                               And   categoria is not null
                                                               Group By esercizio, cd_dipartimento)b
                                 Where a.esercizio = b.esercizio
                                 And   a.cd_dipartimento= b.cd_dipartimento
                                 Group By  a.esercizio, a.cd_dipartimento,  b.variazione)
                     Group By esercizio) Loop
      a:=a+1;

      Insert Into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,decode(rec.cd_dipartimento,null,'TOT','DIP'),'B',a,rec.esercizio,Null,Null,Null,Null,Null,Null,'S',
             Null,Null,Null,Null,Null,rec.variazione,0,rec.ass_dip,
             (select peso from dipartimento_peso p where p.esercizio = rec.esercizio and p.cd_dipartimento= rec.cd_dipartimento),
             rec.CD_DIPARTIMENTO,Null);
   End loop;

   a:=4000;
   For rec In (Select Sum(a.ASSESTATO_ENTRATA) ass_tit, a.esercizio, a.cd_dipartimento, a.titolo,  b.variazione
               From (Select Distinct ASSESTATO_ENTRATA, esercizio, cd_dipartimento, titolo
                     From VPG_PDG_VARIAZIONE_DET
                     Where ID = seq_ID
                     And ti_gestione = 'E'
                     And tipo = 'B'
                     And titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento, titolo
                                                 From VPG_PDG_VARIAZIONE_DET
                                                 Where ID = seq_ID
                                                 And   ti_gestione = 'E'
                                                 And   tipo = 'B'
                                                 And   titolo is not null
                                                 And   categoria is not null
                                                 Group By esercizio, cd_dipartimento, titolo) b
                     Where a.esercizio = b.esercizio
                     And   a.cd_dipartimento= b.cd_dipartimento
                     And   a.titolo=b.titolo
                     Group By a.esercizio, a.cd_dipartimento, a.titolo, b.variazione) Loop
      a:=a+1;

      Insert Into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,'TIT','B',a, rec.esercizio,Null,Null,Null,Null,Null,Null,'E',
             rec.titolo,Null,Null,Null,Null,rec.variazione,rec.ass_tit,0,
             (select peso from dipartimento_peso p where p.esercizio = rec.esercizio and p.cd_dipartimento= rec.cd_dipartimento),
             rec.CD_DIPARTIMENTO,Null);
   End Loop;

   a:=5000;
   For rec In (Select Sum(a.ASSESTATO_SPESA) ass_tit, a.esercizio, a.cd_dipartimento, a.titolo, b.variazione
               From (Select Distinct ASSESTATO_SPESA, esercizio, cd_dipartimento, titolo
                     From VPG_PDG_VARIAZIONE_DET
                     Where ID = seq_ID
                     And   ti_gestione = 'S'
                     And   tipo = 'B'
                     And   titolo is not null) a, (Select Sum(variazione) variazione, esercizio, cd_dipartimento, titolo
                                                   From VPG_PDG_VARIAZIONE_DET
                                                   Where ID = seq_ID
                                                   And   ti_gestione = 'S'
                                                   And   tipo = 'B'
                                                   And   titolo is not null
                                                   And   categoria is not null
                                                   Group By esercizio, cd_dipartimento, titolo) b
                     Where a.esercizio = b.esercizio
                     And   a.cd_dipartimento= b.cd_dipartimento
                     And   a.titolo=b.titolo
                     Group By a.esercizio, a.cd_dipartimento, a.titolo, b.variazione) Loop
      a:=a+1;
      Insert Into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,'TIT','B',a, rec.esercizio,Null,Null,Null,Null,Null,Null,'S',
             rec.titolo,Null,Null,Null,Null,rec.variazione,0,rec.ass_tit,
             (select peso from dipartimento_peso p where p.esercizio = rec.esercizio and p.cd_dipartimento= rec.cd_dipartimento),
             rec.CD_DIPARTIMENTO,Null);
   End Loop;

   seq:=0;
   For rec_fin In (Select ID,CHIAVE,TIPO,SEQUENZA,ESERCIZIO,PG_VARIAZIONE_PDG,DS_VARIAZIONE_PDG,CD_CDR_PROPONENTE,DS_CDR_PROPONENTE,
                           CD_CDR,DS_CDR,TI_GESTIONE,TITOLO,DS_TITOLO,CATEGORIA,DS_CATEGORIA,STATO,
                           VARIAZIONE,ASSESTATO_ENTRATA,ASSESTATO_SPESA,ID_DIPARTIMENTO,CD_DIPARTIMENTO,DS_DIPARTIMENTO
                   From VPG_PDG_VARIAZIONE_DET
                   Where ID = seq_ID
                   Order By tipo desc,ti_gestione, cd_dipartimento, titolo, categoria) Loop

      seq:=seq+1;
      Insert Into VPG_PDG_VARIAZIONE_DET
      Values(seq_ID,rec_fin.chiave,decode(rec_fin.TIPO,'I','T','A'),seq,rec_fin.ESERCIZIO,rec_fin.PG_VARIAZIONE_PDG,rec_fin.DS_VARIAZIONE_PDG,
             rec_fin.CD_CDR_PROPONENTE,rec_fin.DS_CDR_PROPONENTE,rec_fin.CD_CDR,rec_fin.DS_CDR,rec_fin.TI_GESTIONE,
             rec_fin.TITOLO,rec_fin.DS_TITOLO,rec_fin.CATEGORIA,
             rec_fin.DS_CATEGORIA,rec_fin.STATO,rec_fin.VARIAZIONE,rec_fin.ASSESTATO_ENTRATA,rec_fin.ASSESTATO_SPESA,
             (select peso from dipartimento_peso where esercizio = rec_fin.esercizio and cd_dipartimento= rec_fin.cd_dipartimento),
             rec_fin.CD_DIPARTIMENTO,rec_fin.DS_DIPARTIMENTO);
   End Loop;

   Delete From VPG_PDG_VARIAZIONE_DET
   Where ID = seq_ID
   And   TIPO in('I','B');

/*   Open t_c For Select ID, CHIAVE, TIPO, SEQUENZA, ESERCIZIO, PG_VARIAZIONE_PDG, DS_VARIAZIONE_PDG,
                       CD_CDR_PROPONENTE, DS_CDR_PROPONENTE, CD_CDR, DS_CDR, TI_GESTIONE, TITOLO,
                       DS_TITOLO, CATEGORIA, DS_CATEGORIA, STATO, VARIAZIONE, ASSESTATO_ENTRATA,
                       ASSESTATO_SPESA, ID_DIPARTIMENTO, CD_DIPARTIMENTO, DS_DIPARTIMENTO
                From VPG_PDG_VARIAZIONE_DET
                Where id = seq_id
                Order By SEQUENZA;*/
End;
/


