--------------------------------------------------------
--  DDL for View V_CONS_PDG_VAR_GESTIONALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_VAR_GESTIONALE" ("ESERCIZIO", "PG_VARIAZIONE_PDG", "TI_GESTIONE", "PESO_DIP", "CD_DIPARTIMENTO", "CD_CENTRO_SPESA", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "PG_MODULO", "CD_MODULO", "CD_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "DT_APERTURA", "DT_CHIUSURA", "DT_APPROVAZIONE", "DT_ANNULLAMENTO", "DT_APP_FORMALE", "STATO", "TIPOLOGIA", "TIPOLOGIA_FIN", "VARIAZIONI_POSITIVE_DEC_INT", "VARIAZIONI_POSITIVE_DEC_EST", "VARIAZIONI_POSITIVE_ACC_INT", "VARIAZIONI_POSITIVE_ACC_EST", "VARIAZIONI_NEGATIVE_DEC_INT", "VARIAZIONI_NEGATIVE_DEC_EST", "VARIAZIONI_NEGATIVE_ACC_INT", "VARIAZIONI_NEGATIVE_ACC_EST", "VARIAZIONI_POSITIVE_ENTRATA", "VARIAZIONI_NEGATIVE_ENTRATA") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Variazioni Piano di Gestione Gestionale
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
ESERCIZIO, PG_VARIAZIONE_PDG, TI_GESTIONE, PESO_DIP, CD_DIPARTIMENTO, CD_CENTRO_SPESA,
       CD_UNITA_ORGANIZZATIVA, CD_CENTRO_RESPONSABILITA, CD_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       CD_ELEMENTO_VOCE, CD_LINEA_ATTIVITA,
       PG_MODULO, CD_MODULO, CD_TIPO_MODULO, PG_COMMESSA, CD_COMMESSA, PG_PROGETTO, CD_PROGETTO,
       DT_APERTURA, DT_CHIUSURA, DT_APPROVAZIONE, DT_ANNULLAMENTO, DT_APP_FORMALE, STATO, TIPOLOGIA,
       TIPOLOGIA_FIN,
       Nvl(Sum(VARIAZIONI_POSITIVE_DEC_INT), 0) VARIAZIONI_POSITIVE_DEC_INT,
       Nvl(Sum(VARIAZIONI_POSITIVE_DEC_EST), 0) VARIAZIONI_POSITIVE_DEC_EST,
       Nvl(Sum(VARIAZIONI_POSITIVE_ACC_INT), 0) VARIAZIONI_POSITIVE_ACC_INT,
       Nvl(Sum(VARIAZIONI_POSITIVE_ACC_EST), 0) VARIAZIONI_POSITIVE_ACC_EST,
       Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_INT), 0) VARIAZIONI_NEGATIVE_DEC_INT,
       Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_EST), 0) VARIAZIONI_NEGATIVE_DEC_EST,
       Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_INT), 0) VARIAZIONI_NEGATIVE_ACC_INT,
       Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_EST), 0) VARIAZIONI_NEGATIVE_ACC_EST,
       Nvl(Sum(VARIAZIONI_POSITIVE_ENTRATA), 0) VARIAZIONI_POSITIVE_ENTRATA,
       Nvl(Sum(VARIAZIONI_NEGATIVE_ENTRATA), 0) VARIAZIONI_NEGATIVE_ENTRATA
From
(Select  -- DIPARTIMENTI VARIAZIONI PIU
        pdg_variazione_riga_gest.ESERCIZIO, -- DIPARTIMENTI INIZIALE
        pdg_variazione.PG_VARIAZIONE_PDG,
        pdg_variazione_riga_gest.TI_GESTIONE,
         Decode(unita_organizzativa.CD_TIPO_UNITA, 'SAC', '13',to_char(nvl(p.peso,1000))) PESO_DIP,
        Decode(unita_organizzativa.CD_TIPO_UNITA, 'SAC', 'SAC',Nvl(prog.cd_dipartimento, Null)) CD_DIPARTIMENTO,
        unita_organizzativa.CD_UNITA_PADRE CD_CENTRO_SPESA,
        unita_organizzativa.CD_UNITA_ORGANIZZATIVA,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO CD_CENTRO_RESPONSABILITA,
        v_classificazione_voci.CD_CLASSIFICAZIONE,
        v_classificazione_voci.NR_LIVELLO,
        v_classificazione_voci.CD_LIVELLO1,
        v_classificazione_voci.CD_LIVELLO2,
        v_classificazione_voci.CD_LIVELLO3,
        v_classificazione_voci.CD_LIVELLO4,
        v_classificazione_voci.CD_LIVELLO5,
        v_classificazione_voci.CD_LIVELLO6,
        v_classificazione_voci.CD_LIVELLO7,
        Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        modu.PG_PROGETTO PG_MODULO, modu.CD_PROGETTO CD_MODULO, modu.CD_TIPO_PROGETTO CD_TIPO_MODULO,
        comm.PG_PROGETTO PG_COMMESSA, comm.CD_PROGETTO CD_COMMESSA,
        prog.PG_PROGETTO, prog.CD_PROGETTO,
        pdg_variazione.DT_APERTURA,
        pdg_variazione.DT_CHIUSURA,
        pdg_variazione.DT_APPROVAZIONE,
        pdg_variazione.DT_ANNULLAMENTO,
        pdg_variazione.DT_APP_FORMALE,
        pdg_variazione.STATO,
        pdg_variazione.TIPOLOGIA,
        pdg_variazione.TIPOLOGIA_FIN,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0)*-1),
               -1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0),
               0) VARIAZIONI_POSITIVE_DEC_INT,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0)*-1),
               -1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0),
               0) VARIAZIONI_POSITIVE_DEC_EST,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0)*-1),
               -1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0),
               0) VARIAZIONI_POSITIVE_ACC_INT,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)*-1),
               -1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0),
               0) VARIAZIONI_POSITIVE_ACC_EST,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0)*-1),
               1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0),
               0) VARIAZIONI_NEGATIVE_DEC_INT,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0)*-1),
               1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0),
               0) VARIAZIONI_NEGATIVE_DEC_EST,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0)*-1),
               1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0),
               0) VARIAZIONI_NEGATIVE_ACC_INT,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)*-1),
               1,  Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0),
               0) VARIAZIONI_NEGATIVE_ACC_EST,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0)*-1),
               -1,  Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0),
               0) VARIAZIONI_POSITIVE_ENTRATA,
        Decode(Sign(Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0)*-1),
               1,  Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0),
               0) VARIAZIONI_NEGATIVE_ENTRATA
From    PDG_VARIAZIONE_RIGA_GEST,
        V_CLASSIFICAZIONE_VOCI,
        UNITA_ORGANIZZATIVA,
        CDR,
        LINEA_ATTIVITA,
        ELEMENTO_VOCE,
        PROGETTO_GEST PROG,
        PROGETTO_GEST COMM,
        PROGETTO_GEST MODU,
        PDG_VARIAZIONE,
	dipartimento_peso p
Where
 PROG.esercizio= p.esercizio(+) AND
 PROG.cd_dipartimento= p.cd_dipartimento (+) and
   -- join tra PDG_VARIAZIONE_RIGA_GEST e PDG_VARIAZIONE
        Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
-- join tra PDG_MODULO_SPESE_GEST e LINEA_ATTIVITA
And     Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO    = linea_attivita.CD_CENTRO_RESPONSABILITA
And     Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA      = linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
-- join tra PDG_MODULO_SPESE_GEST e ELEMENTO_VOCE
And     Pdg_variazione_riga_gest.ESERCIZIO              = elemento_voce.ESERCIZIO
And     Pdg_variazione_riga_gest.TI_APPARTENENZA        = elemento_voce.TI_APPARTENENZA
And     Pdg_variazione_riga_gest.TI_GESTIONE            = elemento_voce.TI_GESTIONE
And     Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE       = elemento_voce.CD_ELEMENTO_VOCE
-- join tra ELEMENTO_VOCE e V_CLASSIFICAZIONE_VOCI
And     elemento_voce.ID_CLASSIFICAZIONE                = v_classificazione_voci.ID_CLASSIFICAZIONE
-- join tra LINEA_ATTIVITA e MODULO (PROGETTO)
And     linea_attivita.PG_PROGETTO                      = modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = Pdg_variazione_riga_gest.ESERCIZIO
-- join tra MODULO (PROGETTO) e COMMESSA (PROGETTO)
And	modu.ESERCIZIO_PROGETTO_PADRE			= comm.ESERCIZIO
And     modu.PG_PROGETTO_PADRE                          = comm.PG_PROGETTO
-- join tra COMMESSA (PROGETTO) e PROGETTO (PROGETTO)
And	comm.ESERCIZIO_PROGETTO_PADRE                   = prog.ESERCIZIO
And     comm.PG_PROGETTO_PADRE                          = prog.PG_PROGETTO
-- join tra PDG_MODULO_SPESE_GEST e CDR
And     Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO    = cdr.CD_CENTRO_RESPONSABILITA
-- join tra CDR e UNITA_ORGANIZZATIVA
And     cdr.CD_UNITA_ORGANIZZATIVA                      = unita_organizzativa.CD_UNITA_ORGANIZZATIVA)
Group By ESERCIZIO, PG_VARIAZIONE_PDG, TI_GESTIONE, PESO_DIP, CD_DIPARTIMENTO, CD_CENTRO_SPESA,
         CD_UNITA_ORGANIZZATIVA, CD_CENTRO_RESPONSABILITA, CD_CLASSIFICAZIONE, NR_LIVELLO,
         CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
         CD_ELEMENTO_VOCE, CD_LINEA_ATTIVITA,
         PG_MODULO, CD_MODULO, CD_TIPO_MODULO, PG_COMMESSA, CD_COMMESSA, PG_PROGETTO, CD_PROGETTO,
         DT_APERTURA, DT_CHIUSURA, DT_APPROVAZIONE, DT_ANNULLAMENTO, DT_APP_FORMALE, STATO, TIPOLOGIA,
         TIPOLOGIA_FIN
Having ((TI_GESTIONE = 'S' And
         (Nvl(Sum(VARIAZIONI_POSITIVE_DEC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_POSITIVE_DEC_EST), 0) != 0 Or
          Nvl(Sum(VARIAZIONI_POSITIVE_ACC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_POSITIVE_ACC_EST), 0) != 0 Or
          Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_EST), 0) != 0 Or
          Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_EST), 0) != 0)) Or
        (TI_GESTIONE = 'E' And
         (Nvl(Sum(VARIAZIONI_POSITIVE_ENTRATA), 0) != 0 Or Nvl(Sum(VARIAZIONI_NEGATIVE_ENTRATA), 0) != 0)));
