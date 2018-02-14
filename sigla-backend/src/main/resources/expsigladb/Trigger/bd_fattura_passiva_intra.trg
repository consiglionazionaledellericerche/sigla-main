CREATE OR REPLACE TRIGGER BD_FATTURA_PASSIVA_INTRA
BEFORE Delete
On FATTURA_PASSIVA_INTRA
For each row
WHEN (
old.FL_INVIATO = 'Y' And old.nr_protocollo Is Not Null
      )
Declare
   aOldRowtype fattura_passiva_intra%rowtype;
Begin
   --
   -- Trigger attivato su cancellazione della tabella FATTURA_PASSIVA_INTRA (Before)
   --
   -- Date: 01/06/2010
   -- Version: 1.0
   --
   -- Dependency: CNRSTO080
   --
   -- History:
   --
   -- Version: 1.0
   -- Creazione
   --

   If :old.id_cpa Is Not Null  Then
          aOldRowtype.CD_CDS                    := :Old.CD_CDS;
          aOldRowtype.CD_UNITA_ORGANIZZATIVA    := :Old.CD_UNITA_ORGANIZZATIVA;
          aOldRowtype.ESERCIZIO                 := :Old.ESERCIZIO;
          aOldRowtype.PG_FATTURA_PASSIVA        := :Old.PG_FATTURA_PASSIVA;
          aOldRowtype.PG_RIGA_INTRA             := :Old.PG_RIGA_INTRA;
          aOldRowtype.PG_NAZIONE_PROVENIENZA    := :Old.PG_NAZIONE_PROVENIENZA;
          aOldRowtype.PG_NAZIONE_ORIGINE        := :Old.PG_NAZIONE_ORIGINE;
          aOldRowtype.DACR                      := :Old.DACR;
          aOldRowtype.UTCR                      := :Old.UTCR;
          aOldRowtype.DUVA                      := :Old.DUVA;
          aOldRowtype.UTUV                      := :Old.UTUV;
          aOldRowtype.PG_VER_REC                := :Old.PG_VER_REC;
          aOldRowtype.FL_INVIATO                := 'N';
          aOldRowtype.nr_protocollo             := :Old.nr_protocollo;
          aOldRowtype.nr_progressivo            := :Old.nr_progressivo;
   Else
          aOldRowtype.CD_CDS                    := :Old.CD_CDS;
          aOldRowtype.CD_UNITA_ORGANIZZATIVA    := :Old.CD_UNITA_ORGANIZZATIVA;
          aOldRowtype.ESERCIZIO                 := :Old.ESERCIZIO;
          aOldRowtype.PG_FATTURA_PASSIVA        := :Old.PG_FATTURA_PASSIVA;
          aOldRowtype.PG_RIGA_INTRA             := :Old.PG_RIGA_INTRA;
          aOldRowtype.AMMONTARE_EURO            := :Old.AMMONTARE_EURO;
          aOldRowtype.AMMONTARE_DIVISA          := :Old.AMMONTARE_DIVISA;
          aOldRowtype.ID_NATURA_TRANSAZIONE     := :Old.ID_NATURA_TRANSAZIONE;
          aOldRowtype.ID_NOMENCLATURA_COMBINATA := :Old.ID_NOMENCLATURA_COMBINATA;
          aOldRowtype.MASSA_NETTA               := :Old.MASSA_NETTA;
          aOldRowtype.UNITA_SUPPLEMENTARI       := :Old.UNITA_SUPPLEMENTARI;
          aOldRowtype.VALORE_STATISTICO         := :Old.VALORE_STATISTICO;
          aOldRowtype.ESERCIZIO_COND_CONSEGNA   := :Old.ESERCIZIO_COND_CONSEGNA;
          aOldRowtype.CD_INCOTERM               := :Old.CD_INCOTERM;
          aOldRowtype.ESERCIZIO_MOD_TRASPORTO   := :Old.ESERCIZIO_MOD_TRASPORTO;
          aOldRowtype.CD_MODALITA_TRASPORTO     := :Old.CD_MODALITA_TRASPORTO;
          aOldRowtype.PG_NAZIONE_PROVENIENZA    := :Old.PG_NAZIONE_PROVENIENZA;
          aOldRowtype.PG_NAZIONE_ORIGINE        := :Old.PG_NAZIONE_ORIGINE;
          aOldRowtype.CD_PROVINCIA_DESTINAZIONE := :Old.CD_PROVINCIA_DESTINAZIONE;
          aOldRowtype.DS_BENE                   := :Old.DS_BENE;
          aOldRowtype.ID_CPA                    := :Old.ID_CPA;
          aOldRowtype.ESERCIZIO_MOD_INCASSO     := :Old.ESERCIZIO_MOD_INCASSO;
          aOldRowtype.CD_MODALITA_INCASSO       := :Old.CD_MODALITA_INCASSO;
          aOldRowtype.ESERCIZIO_MOD_EROGAZIONE  := :Old.ESERCIZIO_MOD_EROGAZIONE;
          aOldRowtype.CD_MODALITA_EROGAZIONE    := :Old.CD_MODALITA_EROGAZIONE;
          aOldRowtype.DACR                      := :Old.DACR;
          aOldRowtype.UTCR                      := :Old.UTCR;
          aOldRowtype.DUVA                      := :Old.DUVA;
          aOldRowtype.UTUV                      := :Old.UTUV;
          aOldRowtype.PG_VER_REC                := :Old.PG_VER_REC;
          aOldRowtype.FL_INVIATO                := 'N';
          aOldRowtype.nr_protocollo             := :Old.nr_protocollo;
          aOldRowtype.nr_progressivo            := :Old.nr_progressivo;
   End If;

   -- Scarico dello storico
   CNRSTO080.sto_FATTURA_PASSIVA_INTRA(:old.PG_VER_REC+1, aOldRowType);
end;
/


