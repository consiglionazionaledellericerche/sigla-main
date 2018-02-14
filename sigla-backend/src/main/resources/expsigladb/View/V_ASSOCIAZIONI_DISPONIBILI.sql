--------------------------------------------------------
--  DDL for View V_ASSOCIAZIONI_DISPONIBILI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASSOCIAZIONI_DISPONIBILI" ("PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "TI_DOCUMENTO", "ESERCIZIO", "PG_BUONO_C_S", "INVENTARIATO", "ASSOCIATO", "DS_BENE", "CD_UNITA_ORGANIZZATIVA") AS 
  Select A.Pg_Inventario, A.Nr_Inventario, A.Progressivo, A.Ti_Documento, A.Esercizio,
       A.Pg_Buono_C_S, Sum(A.Valore_Unitario) Inventariato, Sum(A.Imp_Associato) Associato,A.ds_bene,a.cd_unita_organizzativa
From
(Select Buono_Carico_Scarico_Dett.Pg_Inventario, Buono_Carico_Scarico_Dett.Nr_Inventario, Buono_Carico_Scarico_Dett.Progressivo,
Buono_Carico_Scarico_Dett.Ti_Documento, Buono_Carico_Scarico_Dett.Esercizio, Buono_Carico_Scarico_Dett.Pg_Buono_C_S, Valore_Unitario, 0 Imp_Associato,ds_bene,cd_unita_organizzativa
 From Buono_Carico_Scarico_Dett,INVENTARIO_BENI
 Where
  Buono_Carico_Scarico_Dett.pg_inventario =INVENTARIO_BENI.pg_inventario And
  Buono_Carico_Scarico_Dett.nr_inventario =INVENTARIO_BENI.nr_inventario And
  Buono_Carico_Scarico_Dett.progressivo =INVENTARIO_BENI.progressivo And
  TI_DOCUMENTO ='C' And
 Exists(Select 1 From BUONO_CARICO_SCARICO,TIPO_CARICO_SCARICO
 WHERE
( BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO=TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO ) AND
( BUONO_CARICO_SCARICO.PG_INVENTARIO=BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO ) AND
( BUONO_CARICO_SCARICO.TI_DOCUMENTO=BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO ) AND
( BUONO_CARICO_SCARICO.PG_BUONO_C_S=BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S ) AND
( BUONO_CARICO_SCARICO.ESERCIZIO=BUONO_CARICO_SCARICO_DETT.ESERCIZIO ) AND
( TIPO_CARICO_SCARICO.FL_FATTURABILE = 'Y' ))
 Union All
 /* per l'associazione delle fatture attive e generici attivi non bisogna confrontare il valore unitario ma quello di alienazione*/
 Select  Buono_Carico_Scarico_Dett.Pg_Inventario,  Buono_Carico_Scarico_Dett.Nr_Inventario,  Buono_Carico_Scarico_Dett.Progressivo,
 Buono_Carico_Scarico_Dett.Ti_Documento,  Buono_Carico_Scarico_Dett.Esercizio,  Buono_Carico_Scarico_Dett.Pg_Buono_C_S, Valore_Alienazione valore_unitario, 0 Imp_Associato,ds_bene,cd_unita_organizzativa
 From Buono_Carico_Scarico_Dett,INVENTARIO_BENI
 Where
  Buono_Carico_Scarico_Dett.pg_inventario =INVENTARIO_BENI.pg_inventario And
  Buono_Carico_Scarico_Dett.nr_inventario =INVENTARIO_BENI.nr_inventario And
  Buono_Carico_Scarico_Dett.progressivo =INVENTARIO_BENI.progressivo And
  TI_DOCUMENTO ='S'  And
 Exists(Select 1 From BUONO_CARICO_SCARICO,TIPO_CARICO_SCARICO
 WHERE
( BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO=TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO ) AND
( BUONO_CARICO_SCARICO.PG_INVENTARIO=BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO ) AND
( BUONO_CARICO_SCARICO.TI_DOCUMENTO=BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO ) AND
( BUONO_CARICO_SCARICO.PG_BUONO_C_S=BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S ) AND
( BUONO_CARICO_SCARICO.ESERCIZIO=BUONO_CARICO_SCARICO_DETT.ESERCIZIO ) AND
( TIPO_CARICO_SCARICO.FL_fatturabile = 'Y' and
  TIPO_CARICO_SCARICO.FL_vendita = 'Y' ))
  Union All
 /* per l'associazione delle nc non bisogna confrontare il valore unitario */
 Select  Buono_Carico_Scarico_Dett.Pg_Inventario,  Buono_Carico_Scarico_Dett.Nr_Inventario,  Buono_Carico_Scarico_Dett.Progressivo,
 Buono_Carico_Scarico_Dett.Ti_Documento,  Buono_Carico_Scarico_Dett.Esercizio,  Buono_Carico_Scarico_Dett.Pg_Buono_C_S, valore_unitario, 0 Imp_Associato,ds_bene,cd_unita_organizzativa
 From Buono_Carico_Scarico_Dett,INVENTARIO_BENI
 Where
  Buono_Carico_Scarico_Dett.pg_inventario =INVENTARIO_BENI.pg_inventario And
  Buono_Carico_Scarico_Dett.nr_inventario =INVENTARIO_BENI.nr_inventario And
  Buono_Carico_Scarico_Dett.progressivo =INVENTARIO_BENI.progressivo And
  TI_DOCUMENTO ='S'  And
 Exists(Select 1 From BUONO_CARICO_SCARICO,TIPO_CARICO_SCARICO
 WHERE
( BUONO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO=TIPO_CARICO_SCARICO.CD_TIPO_CARICO_SCARICO ) AND
( BUONO_CARICO_SCARICO.PG_INVENTARIO=BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO ) AND
( BUONO_CARICO_SCARICO.TI_DOCUMENTO=BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO ) AND
( BUONO_CARICO_SCARICO.PG_BUONO_C_S=BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S ) AND
( BUONO_CARICO_SCARICO.ESERCIZIO=BUONO_CARICO_SCARICO_DETT.ESERCIZIO ) AND
( TIPO_CARICO_SCARICO.FL_fatturabile = 'Y' And
  TIPO_CARICO_SCARICO.FL_vendita = 'N' ))
 Union All
 Select  Ass.Pg_Inventario,Ass.Nr_Inventario, Ass.Progressivo, Ass.Ti_Documento, Ass.Esercizio, Ass.Pg_Buono_C_S, 0,
        (Select  Decode(Fatt.TI_ISTITUZ_COMMERC,'I',Fatt.Im_Imponibile+fatt.IM_IVA,Fatt.Im_Imponibile)
            From  Fattura_Passiva_Riga Fatt
	    Where
	    	Ass.Cd_Cds_Fatt_Pass 		= Fatt.Cd_Cds 			And
	    	Ass.Cd_Uo_Fatt_Pass 		= Fatt.Cd_Unita_Organizzativa 	And
	    	Ass.Esercizio_Fatt_Pass 	= Fatt.Esercizio 		And
	    	Ass.Pg_Fattura_Passiva   	= Fatt.Pg_Fattura_Passiva   	And
	    	Ass.Progressivo_Riga_Fatt_Pass 	= Fatt.Progressivo_Riga) Imp_Associato,ds_bene,cd_unita_organizzativa
  	From Ass_Inv_Bene_Fattura Ass,INVENTARIO_BENI
 Where
  ass.pg_inventario =INVENTARIO_BENI.pg_inventario And
  ass.nr_inventario =INVENTARIO_BENI.nr_inventario And
  ass.progressivo =INVENTARIO_BENI.progressivo
   Union All
 Select  Ass.Pg_Inventario,Ass.Nr_Inventario, Ass.Progressivo, Ass.Ti_Documento, Ass.Esercizio, Ass.Pg_Buono_C_S, 0,
        (Select  Fatt.Im_Imponibile
            From  Fattura_Attiva_Riga Fatt
	    Where
	    	Ass.Cd_Cds_Fatt_Att 		= Fatt.Cd_Cds 			And
	    	Ass.Cd_Uo_Fatt_Att 		= Fatt.Cd_Unita_Organizzativa 	And
	    	Ass.Esercizio_Fatt_Att 		= Fatt.Esercizio 		And
	    	Ass.Pg_Fattura_Attiva   	= Fatt.Pg_Fattura_attiva   	And
	    	Ass.Progressivo_Riga_Fatt_Att 	= Fatt.Progressivo_Riga) Imp_Associato,ds_bene,cd_unita_organizzativa
  	From Ass_Inv_Bene_Fattura Ass,INVENTARIO_BENI
 Where
  ass.pg_inventario =INVENTARIO_BENI.pg_inventario And
  ass.nr_inventario =INVENTARIO_BENI.nr_inventario And
  ass.progressivo =INVENTARIO_BENI.progressivo
 Union All
 Select  Ass.Pg_Inventario,Ass.Nr_Inventario, Ass.Progressivo, Ass.Ti_Documento, Ass.Esercizio, Ass.Pg_Buono_C_S, 0,
        (Select  doc.Im_riga
            From  Documento_generico_riga doc
	    Where
	    	Ass.Cd_Cds_Doc_Gen 		= doc.Cd_Cds 			And
	    	Ass.Cd_Uo_Doc_Gen 		= doc.Cd_Unita_Organizzativa 	And
	    	Ass.Esercizio_Doc_Gen 		= doc.Esercizio 		And
	    	Ass.Cd_tipo_documento_amm	= doc.Cd_tipo_documento_amm	And
	    	Ass.Pg_Documento_generico   	= doc.Pg_Documento_generico   	And
	    	Ass.Progressivo_Riga_Doc_Gen 	= doc.Progressivo_Riga) Imp_Associato,ds_bene,cd_unita_organizzativa
  	From Ass_Inv_Bene_Fattura Ass,INVENTARIO_BENI
 Where
  ass.pg_inventario =INVENTARIO_BENI.pg_inventario And
  ass.nr_inventario =INVENTARIO_BENI.nr_inventario And
  ass.progressivo =INVENTARIO_BENI.progressivo
 Union All
 Select  Inv.Pg_Inventario,Inv.Nr_Inventario, Inv.Progressivo,Inv.Ti_Documento, Inv.Esercizio, Inv.Pg_Buono_C_S, 0,
         Inv.Imp_Fattura,Null ds_bene,cd_unita_organizzativa
        From Inventario_Beni_Apg Inv) A
Group By A.Pg_Inventario, A.Nr_Inventario, A.Progressivo, A.Ti_Documento, A.Esercizio, A.Pg_Buono_C_S,a.ds_bene,a.cd_unita_organizzativa
Having Sum(A.Valore_Unitario) - (Sum(A.Imp_Associato)) > 0
;
