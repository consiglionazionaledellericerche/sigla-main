--------------------------------------------------------
--  DDL for View CONTROLLO_VARIAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "CONTROLLO_VARIAZIONE" ("ESERCIZIO", "PG_VARIAZIONE_PDG", "TIPOLOGIA", "TIPOLOGIA_FIN", "DIPARTIMENTO", "TITOLO_SPESA", "TITOLO_ENTRATA", "CATEGORIA_SAC", "IMPORTO_STORNO_SPESA", "CONTROLLO_SPESA", "FL_ACCENTRATO", "ACCENTRATO") AS 
  Select Distinct p.ESERCIZIO,                   
	          p.PG_VARIAZIONE_PDG,  
		  P.TIPOLOGIA, 
		  p.TIPOLOGIA_FIN,
	          pro.CD_DIPARTIMENTO, 
	          Decode(e.TI_GESTIONE,'S',c.CD_LIVELLO1, '--') titolo_spesa,  
		  Decode(e.TI_GESTIONE,'E',c.CD_LIVELLO1, '--') titolo_entrata,  
		  Decode(Substr(pd.CD_CDR_ASSEGNATARIO,1,3), '000', c.CD_LIVELLO1||'.'||c.CD_LIVELLO2, '--') categoria_sac,
		  Sum(Decode(Substr(p.tipologia,1,3), 'STO',(IM_SPESE_GEST_DECENTRATA_INT +IM_SPESE_GEST_DECENTRATA_EST), Null)) importo_storno_spesa,
		  Decode(p.tipologia_fin, 'FIN', Decode(e.FL_LIMITE_SPESA, 'Y', 'SOGGETTA A CONTROLLO SPESA', Null), Null)controllo_spesa,  
		  c.FL_ACCENTRATO,
	          Decode(c.FL_ACCENTRATO,'Y',Decode(e.FL_VOCE_PERSONALE,'Y','Y',Decode(pd.IM_SPESE_GEST_ACCENTRATA_EST+pd.IM_SPESE_GEST_ACCENTRATA_INT, 0,'N','Y')),'N') accentrata  
   From  pdg_variazione_riga_gest pd, 
         pdg_variazione p, 
         linea_attivita l, 
         elemento_voce e, 
         classificazione_voci c, 
         progetto_gest modu, 
         progetto_gest comm, 
         progetto_gest pro
   Where p.ESERCIZIO = pd.ESERCIZIO
   And p.PG_VARIAZIONE_PDG = pd.PG_VARIAZIONE_PDG
   And pd.CD_CDR_ASSEGNATARIO = l.CD_CENTRO_RESPONSABILITA
   And pd.CD_LINEA_ATTIVITA = l.CD_LINEA_ATTIVITA
   And l.PG_PROGETTO = modu.PG_PROGETTO
   And modu.ESERCIZIO = p.ESERCIZIO
   And modu.TIPO_FASE = 'G'
   And modu.PG_PROGETTO_PADRE = comm.PG_PROGETTO
   And modu.ESERCIZIO_PROGETTO_PADRE = comm.ESERCIZIO
   And modu.TIPO_FASE_PROGETTO_PADRE = comm.TIPO_FASE
   And comm.PG_PROGETTO_PADRE = pro.PG_PROGETTO
   And comm.ESERCIZIO_PROGETTO_PADRE = pro.ESERCIZIO
   And comm.TIPO_FASE_PROGETTO_PADRE = pro.TIPO_FASE
   And pd.ESERCIZIO = e.ESERCIZIO
   And pd.TI_APPARTENENZA = e.TI_APPARTENENZA
   And pd.TI_GESTIONE = e.TI_GESTIONE
   And pd.CD_ELEMENTO_VOCE = e.CD_ELEMENTO_VOCE
   And e.ID_CLASSIFICAZIONE = c.ID_CLASSIFICAZIONE
   And p.stato = 'PRD'
Group By p.ESERCIZIO,                   
	P.PG_VARIAZIONE_PDG,  
	p.TIPOLOGIA, 
	p.TIPOLOGIA_FIN,
        pro.CD_DIPARTIMENTO, 
        Decode(e.TI_GESTIONE,'S',c.CD_LIVELLO1, '--') ,  
	Decode(e.TI_GESTIONE,'E',c.CD_LIVELLO1, '--') ,  
	Decode(Substr(pd.CD_CDR_ASSEGNATARIO,1,3), '000', c.CD_LIVELLO1||'.'||c.CD_LIVELLO2, '--'),
	Decode(p.tipologia_fin, 'FIN', Decode(e.FL_LIMITE_SPESA, 'Y', 'SOGGETTA A CONTROLLO SPESA', Null), Null),  
	c.FL_ACCENTRATO,
	Decode(c.FL_ACCENTRATO,'Y',Decode(e.FL_VOCE_PERSONALE,'Y','Y',Decode(pd.IM_SPESE_GEST_ACCENTRATA_EST+pd.IM_SPESE_GEST_ACCENTRATA_INT, 0,'N','Y')),'N')  
                 Order by p.PG_VARIAZIONE_PDG;
