CREATE OR REPLACE Procedure Ins_Tmp_Registro_Inv_Quote
(Ap_Uo  Varchar2, Ap_Cds Varchar2,Ap_Data_Da Varchar2,Ap_Data_A  Varchar2,
 Ap_Categoria Varchar2,Ap_Gruppo  Varchar2,Da_Codice_Bene  Number,
 A_Codice_Bene  Number,Ap_Ds_Tipo Varchar2,Ap_Tipo Varchar2)Is
 Data_Da Date;
 Data_A Date;
 Uo Varchar2(30);
 Cds Varchar2(30);
 Categoria Varchar2(30);
 Gruppo Varchar2(30);
 Tipo Varchar2(30);
 Ds_Tipo Varchar2(100);
Begin
Data_Da:=To_Date(Ap_Data_Da,'dd/mm/yyyy');
Data_A:=To_Date(Ap_Data_A,'dd/mm/yyyy');
 Select Decode(Ap_Uo,'*',Null,Ap_Uo),Decode(Ap_Cds,'*',Null,Ap_Cds),Decode(Ap_Categoria,'*',Null,Ap_Categoria),Decode(Ap_Gruppo,'*',Null,Ap_Gruppo),
 	 Decode(Ap_Ds_Tipo,'*',Null,Ap_Ds_Tipo), Decode(Ap_Tipo,'*',Null,Ap_Tipo)
  Into Uo,Cds,Categoria,Gruppo,Ds_Tipo,Tipo From Dual;
Insert Into tmp_Stampa_Registro_Inv_Quote (
Select Inventario_Beni.Cd_Unita_Organizzativa, Inventario_Beni.Cd_Cds,
       Inventario_Beni.Esercizio_Carico_Bene, Inventario_Beni.Etichetta,
       Categoria_Gruppo_Invent.cd_categoria_padre Categoria,
       Inventario_Beni.Cd_Categoria_Gruppo,
       Inventario_Beni.Cd_Assegnatario,
       Buono_Carico_Scarico.Data_Registrazione,
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico,
       Tipo_Carico_Scarico.Ds_Tipo_Carico_Scarico,
       Decode(Buono_Carico_Scarico.Ti_Documento,'C',Amm.Im_Movimento_Ammort,(-1)*Amm.Im_Movimento_Ammort) Im_Movimento_Ammort,
       Amm.Imponibile_Ammortamento 	Imponibile_Ammortamento,
       Amm.Esercizio Esercizio_Amm,
       (Select Numero_Anni
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Numero_Anni,
       (Select Perc_Ammortamento
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Ammortamento,
       (Select Perc_Primo_Anno
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Primo_Anno,
       (Select Perc_Successivi
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Successivi,
       Inventario_Beni.Nr_Inventario,Inventario_Beni.Fl_Ammortamento,
       Inventario_Beni.Ti_Commerciale_Istituzionale,
       Amm.Esercizio_Competenza
  From Inventario_Beni,
       Ubicazione_Bene,
       Categoria_Gruppo_Invent,
       Buono_Carico_Scarico_Dett,
       Terzo,
       Buono_Carico_Scarico,
       Tipo_Carico_Scarico,
       Ammortamento_Bene_Inv Amm
 Where
      Inventario_Beni.Cd_Unita_Organizzativa 		= Nvl(Uo,Inventario_Beni.Cd_Unita_Organizzativa)		   And
      Inventario_Beni.Cd_Cds		   		= Nvl(Cds,Inventario_Beni.Cd_Cds)  		                   And
      Buono_Carico_Scarico.Data_Registrazione	 	Between Data_Da And Data_A	                                   And
      Inventario_Beni.Esercizio_Carico_Bene     	<= 	To_Char(Data_A,'yyyy') 					   And
      Categoria_Gruppo_Invent.cd_categoria_padre  	= Nvl(Categoria, Categoria_Gruppo_Invent.cd_categoria_padre)  And
      Inventario_Beni.Cd_Categoria_Gruppo	  	= Nvl(Gruppo ,Inventario_Beni.Cd_Categoria_Gruppo)          	   And
      Inventario_Beni.Nr_Inventario   			>= Da_Codice_Bene 						   And
      Inventario_Beni.Nr_Inventario   			<= A_Codice_Bene						   And
      Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico 	= Nvl(Ds_Tipo, Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico)	   And
      Inventario_Beni.Ti_Commerciale_Istituzionale	= Nvl(Tipo,Inventario_Beni.Ti_Commerciale_Istituzionale)	   And
       Inventario_Beni.Cd_Cds = Ubicazione_Bene.Cd_Cds
   And Inventario_Beni.Cd_Unita_Organizzativa 	= Ubicazione_Bene.Cd_Unita_Organizzativa
   And Inventario_Beni.Cd_Ubicazione 		= Ubicazione_Bene.Cd_Ubicazione
   And Inventario_Beni.Cd_Categoria_Gruppo 	= Categoria_Gruppo_Invent.Cd_Categoria_Gruppo
   And Inventario_Beni.Cd_Unita_Organizzativa 	= Terzo.Cd_Unita_Organizzativa
   and Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   where
	 t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 and t.dt_fine_rapporto is null)
   And Buono_Carico_Scarico_Dett.Pg_Inventario 	= Inventario_Beni.Pg_Inventario
   And Buono_Carico_Scarico_Dett.Nr_Inventario 	= Inventario_Beni.Nr_Inventario
   And Buono_Carico_Scarico_Dett.Progressivo 	= Inventario_Beni.Progressivo
   And Buono_Carico_Scarico.Pg_Inventario 	= Buono_Carico_Scarico_Dett.Pg_Inventario
   And Buono_Carico_Scarico.Ti_Documento 	= Buono_Carico_Scarico_Dett.Ti_Documento
   And Buono_Carico_Scarico.Esercizio 		= Buono_Carico_Scarico_Dett.Esercizio
   And Buono_Carico_Scarico.Pg_Buono_C_S 	= Buono_Carico_Scarico_Dett.Pg_Buono_C_S
   And Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico = Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico
   And(( Tipo_Carico_Scarico.Ti_Documento = 'C'
   And Tipo_Carico_Scarico.Fl_Aumento_Valore = 'N'))
   And Inventario_Beni.Pg_Inventario =  Amm.Pg_Inventario
   And Inventario_Beni.Nr_Inventario =	Amm.Nr_Inventario
   And Inventario_Beni.Progressivo  = 	Amm.Progressivo
 Union
 Select Inventario_Beni.Cd_Unita_Organizzativa, Inventario_Beni.Cd_Cds,
       Inventario_Beni.Esercizio_Carico_Bene, Inventario_Beni.Etichetta,
       Categoria_Gruppo_Invent.cd_categoria_padre Categoria,
       Inventario_Beni.Cd_Categoria_Gruppo,
       Inventario_Beni.Cd_Assegnatario,
       Buono_Carico_Scarico.Data_Registrazione,
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico,
       Tipo_Carico_Scarico.Ds_Tipo_Carico_Scarico,
       Decode(Buono_Carico_Scarico.Ti_Documento,'S',-inventario_beni.valore_ammortizzato,0) Im_Movimento_Ammort,
       inventario_beni.Imponibile_Ammortamento 	Imponibile_Ammortamento,
       Buono_Carico_Scarico.Esercizio Esercizio_Amm,
       (Select Numero_Anni
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Numero_Anni,
       (Select Perc_Ammortamento
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Ammortamento,
       (Select Perc_Primo_Anno
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Primo_Anno,
       (Select Perc_Successivi
          From Ammortamento_Bene_Inv
         Where Inventario_Beni.Pg_Inventario = Ammortamento_Bene_Inv.Pg_Inventario
           And Inventario_Beni.Nr_Inventario = Ammortamento_Bene_Inv.Nr_Inventario
           And Inventario_Beni.Progressivo   = Ammortamento_Bene_Inv.Progressivo
           And Ammortamento_Bene_Inv.Numero_Anno = 1
           And Rownum =1) Perc_Successivi,
       Inventario_Beni.Nr_Inventario,Inventario_Beni.Fl_Ammortamento,
       Inventario_Beni.Ti_Commerciale_Istituzionale,
       Buono_Carico_Scarico.esercizio Esercizio_Competenza
  From Inventario_Beni,
       Ubicazione_Bene,
       Categoria_Gruppo_Invent,
       Buono_Carico_Scarico_Dett,
       Terzo,
       Buono_Carico_Scarico,
       Tipo_Carico_Scarico
 Where
      Inventario_Beni.Cd_Unita_Organizzativa 		= Nvl(Uo,Inventario_Beni.Cd_Unita_Organizzativa)		   And
      Inventario_Beni.Cd_Cds		   		= Nvl(Cds,Inventario_Beni.Cd_Cds)  		                   And
      Buono_Carico_Scarico.Data_Registrazione	 	Between Data_Da And Data_A	                                   And
      Inventario_Beni.Esercizio_Carico_Bene     	<= 	To_Char(Data_A,'yyyy') 					   And
      Categoria_Gruppo_Invent.cd_categoria_padre  	= Nvl(Categoria, Categoria_Gruppo_Invent.cd_categoria_padre)  And
      Inventario_Beni.Cd_Categoria_Gruppo	  	= Nvl(Gruppo ,Inventario_Beni.Cd_Categoria_Gruppo)          	   And
      Inventario_Beni.Nr_Inventario   			>= Da_Codice_Bene 						   And
      Inventario_Beni.Nr_Inventario   			<= A_Codice_Bene						   And
      Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico 	= Nvl(Ds_Tipo, Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico)	   And
      Inventario_Beni.Ti_Commerciale_Istituzionale	= Nvl(Tipo,Inventario_Beni.Ti_Commerciale_Istituzionale)	   And
       Inventario_Beni.Cd_Cds = Ubicazione_Bene.Cd_Cds
   And Inventario_Beni.Cd_Unita_Organizzativa 	= Ubicazione_Bene.Cd_Unita_Organizzativa
   And Inventario_Beni.Cd_Ubicazione 		= Ubicazione_Bene.Cd_Ubicazione
   And Inventario_Beni.Cd_Categoria_Gruppo 	= Categoria_Gruppo_Invent.Cd_Categoria_Gruppo
   And Inventario_Beni.Cd_Unita_Organizzativa 	= Terzo.Cd_Unita_Organizzativa
   and Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   where
	 t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 and t.dt_fine_rapporto is null)
   And Buono_Carico_Scarico_Dett.Pg_Inventario 	= Inventario_Beni.Pg_Inventario
   And Buono_Carico_Scarico_Dett.Nr_Inventario 	= Inventario_Beni.Nr_Inventario
   And Buono_Carico_Scarico_Dett.Progressivo 	= Inventario_Beni.Progressivo
   And Buono_Carico_Scarico.Pg_Inventario 	= Buono_Carico_Scarico_Dett.Pg_Inventario
   And Buono_Carico_Scarico.Ti_Documento 	= Buono_Carico_Scarico_Dett.Ti_Documento
   And Buono_Carico_Scarico.Esercizio 		= Buono_Carico_Scarico_Dett.Esercizio
   And Buono_Carico_Scarico.Pg_Buono_C_S 	= Buono_Carico_Scarico_Dett.Pg_Buono_C_S
   And Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico = Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico
   And Tipo_Carico_Scarico.Ti_Documento = 'S'   And
       Inventario_Beni.Fl_Totalmente_Scaricato='Y'
    And (Buono_Carico_Scarico.Pg_Inventario,Buono_Carico_Scarico.Ti_Documento,Buono_Carico_Scarico.Esercizio,Buono_Carico_Scarico.Pg_Buono_C_S) In
    (Select Dett.Pg_Inventario,Dett.Ti_Documento,Dett.Esercizio,Max(Dett.Pg_Buono_C_S) From Buono_Carico_Scarico_Dett Dett
   Where
       Dett.Pg_Inventario  = Inventario_Beni.Pg_Inventario
   And Dett.Nr_Inventario  = Inventario_Beni.Nr_Inventario
   And Dett.Progressivo    = Inventario_Beni.Progressivo
   And  Dett.Ti_Documento = 'S'   And
       Inventario_Beni.Fl_Totalmente_Scaricato='Y' And
  Not Exists(Select 1 From Buono_Carico_Scarico_Dett Dettagli Where
        Dettagli.Esercizio > Buono_Carico_Scarico_Dett.Esercizio And
	Dettagli.Ti_Documento ='S' And
       Buono_Carico_Scarico_Dett.Pg_Inventario  = Dettagli.Pg_Inventario
   And Buono_Carico_Scarico_Dett.Nr_Inventario  = Dettagli.Nr_Inventario
   And Buono_Carico_Scarico_Dett.Progressivo    = Dettagli.Progressivo)
   Group By Dett.Pg_Inventario,Dett.Ti_Documento,Dett.Esercizio)  And
   Exists( Select 1 From AMMORTAMENTO_BENE_INV AMM Where
      Inventario_Beni.Pg_Inventario =  Amm.Pg_Inventario
   And Inventario_Beni.Nr_Inventario =	Amm.Nr_Inventario
   And Inventario_Beni.Progressivo  = 	Amm.Progressivo)
 Union
 Select Prt_Stampa_Registro_Inventario.Cd_Unita_Organizzativa, Prt_Stampa_Registro_Inventario.Cd_Cds,
       Prt_Stampa_Registro_Inventario.Esercizio_Carico_Bene,Null,
       Prt_Stampa_Registro_Inventario.Categoria,
       Prt_Stampa_Registro_Inventario.Cd_Categoria_Gruppo,
       Null,
       Null,
       Prt_Stampa_Registro_Inventario.Cd_Tipo_Carico_Scarico,
       Null, 0, Null,
       9999,
       Null,
       Null,
       Null,
       Null,
       9999999999,Null,
       Prt_Stampa_Registro_Inventario.Tipo,
       9999
  From Prt_Stampa_Registro_Inventario
  	Where
       Prt_Stampa_Registro_Inventario.Cd_Unita_Organizzativa 		= Nvl(Uo,Prt_Stampa_Registro_Inventario.Cd_Unita_Organizzativa)	 And
       Prt_Stampa_Registro_Inventario.Cd_Cds		   		= Nvl(Cds,Prt_Stampa_Registro_Inventario.Cd_Cds)		 And
       Prt_Stampa_Registro_Inventario.Data_Registrazione		Between Data_Da And Data_A					 And
       Prt_Stampa_Registro_Inventario.Esercizio_Carico_Bene     	<= 	To_Char(Data_A,'yyyy') 					 And
       Prt_Stampa_Registro_Inventario.Categoria 			= Nvl(Categoria,Prt_Stampa_Registro_Inventario.Categoria)      	 And
       Prt_Stampa_Registro_Inventario.Cd_Categoria_Gruppo 		= Nvl(Gruppo,Prt_Stampa_Registro_Inventario.Cd_Categoria_Gruppo) And
       Prt_Stampa_Registro_Inventario.Nr_Inventario   			>= Da_Codice_Bene 						 And
       Prt_Stampa_Registro_Inventario.Nr_Inventario   			<= A_Codice_Bene 						 And
       Prt_Stampa_Registro_Inventario.Cd_Tipo_Carico_Scarico 		= Nvl(Ds_Tipo,Prt_Stampa_Registro_Inventario.Cd_Tipo_Carico_Scarico) And
       Prt_Stampa_Registro_Inventario.Tipo				= Nvl(Tipo,Prt_Stampa_Registro_Inventario.Tipo)
  Union
  Select Inventario_Beni.Cd_Unita_Organizzativa, Inventario_Beni.Cd_Cds,
       Inventario_Beni.Esercizio_Carico_Bene, Inventario_Beni.Etichetta,
       Categoria_Gruppo_Invent.cd_categoria_padre Categoria,
       Inventario_Beni.Cd_Categoria_Gruppo,
       Inventario_Beni.Cd_Assegnatario,
       Buono_Carico_Scarico.Data_Registrazione,
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico,
       Tipo_Carico_Scarico.Ds_Tipo_Carico_Scarico,
       Decode(Buono_Carico_Scarico.Ti_Documento,'C',Ass.Valore_Ammortizzato,(-1)*Ass.Valore_Ammortizzato) Im_Movimento_Ammort,
       Ass.Imponibile_Ammortamento 	Imponibile_Ammortamento,
       Inventario_Beni.Esercizio_Carico_Bene Esercizio_Amm,
       Null,
       Null,
       Null,
       Null,
       Inventario_Beni.Nr_Inventario,Inventario_Beni.Fl_Ammortamento,
       Inventario_Beni.Ti_Commerciale_Istituzionale,
       (Inventario_Beni.Esercizio_Carico_Bene-1) Esercizio_Comp
       From
       Inventario_Beni,
       Ubicazione_Bene,
       Categoria_Gruppo_Invent,
       Buono_Carico_Scarico_Dett,
       Terzo,
       Buono_Carico_Scarico,
       Tipo_Carico_Scarico,
       Ass_Trasferimento_Beni_Inv Ass
       Where
      Inventario_Beni.Cd_Unita_Organizzativa 		= Nvl(Uo,Inventario_Beni.Cd_Unita_Organizzativa)	   And
      Inventario_Beni.Cd_Cds		   		= Nvl(Cds,Inventario_Beni.Cd_Cds)  		           And
      Buono_Carico_Scarico.Data_Registrazione	 	Between Data_Da And Data_A	                           And
      Inventario_Beni.Esercizio_Carico_Bene     	<= 	To_Char(Data_A,'yyyy') 				   And
      Categoria_Gruppo_Invent.cd_categoria_padre  	= Nvl(Categoria, Categoria_Gruppo_Invent.cd_categoria_padre)  And
      Inventario_Beni.Cd_Categoria_Gruppo	  	= Nvl(Gruppo ,Inventario_Beni.Cd_Categoria_Gruppo)          	   And
      Inventario_Beni.Nr_Inventario   			>= Da_Codice_Bene 						   And
      Inventario_Beni.Nr_Inventario   			<= A_Codice_Bene						   And
      Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico 	= Nvl(Ds_Tipo, Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico)	   And
      Inventario_Beni.Ti_Commerciale_Istituzionale	= Nvl(Tipo,Inventario_Beni.Ti_Commerciale_Istituzionale)	   And
       Inventario_Beni.Cd_Cds 			= Ubicazione_Bene.Cd_Cds			And
       Inventario_Beni.Cd_Unita_Organizzativa 	= Ubicazione_Bene.Cd_Unita_Organizzativa	And
       Inventario_Beni.Cd_Ubicazione            = Ubicazione_Bene.Cd_Ubicazione			And
       Inventario_Beni.Cd_Categoria_Gruppo      = Categoria_Gruppo_Invent.Cd_Categoria_Gruppo   And
       Inventario_Beni.Cd_Unita_Organizzativa   = Terzo.Cd_Unita_Organizzativa                  And
       Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   		 where
	 		 t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 		 and t.dt_fine_rapporto is null)  and
       Buono_Carico_Scarico_Dett.Pg_Inventario  = Inventario_Beni.Pg_Inventario			And
       Buono_Carico_Scarico_Dett.Nr_Inventario  = Inventario_Beni.Nr_Inventario			And
       Buono_Carico_Scarico_Dett.Progressivo    = Inventario_Beni.Progressivo 			And
       Buono_Carico_Scarico.Pg_Inventario       = Buono_Carico_Scarico_Dett.Pg_Inventario	And
       Buono_Carico_Scarico.Ti_Documento 	= Buono_Carico_Scarico_Dett.Ti_Documento	And
       Buono_Carico_Scarico.Esercizio 		= Buono_Carico_Scarico_Dett.Esercizio		And
       Buono_Carico_Scarico.Pg_Buono_C_S        = Buono_Carico_Scarico_Dett.Pg_Buono_C_S	And
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico = Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico And
       Tipo_Carico_Scarico.Ti_Documento         = 'C'	And
       Tipo_Carico_Scarico.Fl_Aumento_Valore 	= 'N'	And
     /*(Fl_Totalmente_Scaricato='N'  Or
      Exists(Select 1 From Buono_Carico_Scarico_Dett Dett Where
 	Dett.Pg_Inventario = Buono_Carico_Scarico_Dett.Pg_Inventario 	And
        Dett.Nr_Inventario = Buono_Carico_Scarico_Dett.Nr_Inventario	And
 	Dett.Progressivo   = Buono_Carico_Scarico_Dett.Progressivo      And
 	Dett.Ti_Documento  = 'S'					And
 	Dett.Esercizio     > Buono_Carico_Scarico_Dett.Esercizio))      And */
  Inventario_Beni.Pg_Inventario =  Ass.Pg_Inventario_Dest		And
  Inventario_Beni.Nr_Inventario =  Ass.Nr_Inventario_Dest		And
  Inventario_Beni.Progressivo   =  Ass.Progressivo_Dest
  union
  Select Inventario_Beni.Cd_Unita_Organizzativa, Inventario_Beni.Cd_Cds,
       Inventario_Beni.Esercizio_Carico_Bene, Inventario_Beni.Etichetta,
       Categoria_Gruppo_Invent.cd_categoria_padre Categoria,
       Inventario_Beni.Cd_Categoria_Gruppo,
       Inventario_Beni.Cd_Assegnatario,
       Buono_Carico_Scarico.Data_Registrazione,
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico,
       Tipo_Carico_Scarico.Ds_Tipo_Carico_Scarico,
       Decode(Buono_Carico_Scarico.Ti_Documento,'C',Ass.Valore_Ammortizzato,(-1)*Ass.Valore_Ammortizzato) Im_Movimento_Ammort,
       Ass.Imponibile_Ammortamento 	Imponibile_Ammortamento,
       Inventario_Beni.Esercizio_Carico_Bene Esercizio_Amm,
       Null,
       Null,
       Null,
       Null,
       Inventario_Beni.Nr_Inventario,Inventario_Beni.Fl_Ammortamento,
       Inventario_Beni.Ti_Commerciale_Istituzionale,
       (Inventario_Beni.Esercizio_Carico_Bene-1) Esercizio_Comp
       From
       Inventario_Beni,
       Ubicazione_Bene,
       Categoria_Gruppo_Invent,
       Buono_Carico_Scarico_Dett,
       Terzo,
       Buono_Carico_Scarico,
       Tipo_Carico_Scarico,
       Ass_Trasferimento_Beni_Inv Ass
       Where
      Inventario_Beni.Cd_Unita_Organizzativa 		= Nvl(Uo,Inventario_Beni.Cd_Unita_Organizzativa)	   And
      Inventario_Beni.Cd_Cds		   		= Nvl(Cds,Inventario_Beni.Cd_Cds)  		           And
      Buono_Carico_Scarico.Data_Registrazione	 	Between Data_da And Data_A	                           And
      Inventario_Beni.Esercizio_Carico_Bene     	<= 	To_Char(Data_A,'yyyy') 				   And
      Categoria_Gruppo_Invent.cd_categoria_padre  	= Nvl(Categoria, Categoria_Gruppo_Invent.cd_categoria_padre)  And
      Inventario_Beni.Cd_Categoria_Gruppo	  	= Nvl(Gruppo ,Inventario_Beni.Cd_Categoria_Gruppo)          	   And
      Inventario_Beni.Nr_Inventario   			>= Da_Codice_Bene 						   And
      Inventario_Beni.Nr_Inventario   			<= A_Codice_Bene						   And
      Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico 	= Nvl(Ds_tipo, Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico)	   And
      Inventario_Beni.Ti_Commerciale_Istituzionale	= Nvl(Tipo,Inventario_Beni.Ti_Commerciale_Istituzionale)	   And
       Inventario_Beni.Cd_Cds 			= Ubicazione_Bene.Cd_Cds			And
       Inventario_Beni.Cd_Unita_Organizzativa 	= Ubicazione_Bene.Cd_Unita_Organizzativa	And
       Inventario_Beni.Cd_Ubicazione            = Ubicazione_Bene.Cd_Ubicazione			And
       Inventario_Beni.Cd_Categoria_Gruppo      = Categoria_Gruppo_Invent.Cd_Categoria_Gruppo   And
       Inventario_Beni.Cd_Unita_Organizzativa   = Terzo.Cd_Unita_Organizzativa                  And
       Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   			where
	 			t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 			and t.dt_fine_rapporto is null)  and
       Buono_Carico_Scarico_Dett.Pg_Inventario  = Inventario_Beni.Pg_Inventario			And
       Buono_Carico_Scarico_Dett.Nr_Inventario  = Inventario_Beni.Nr_Inventario			And
       Buono_Carico_Scarico_Dett.Progressivo    = Inventario_Beni.Progressivo 			And
       Buono_Carico_Scarico.Pg_Inventario       = Buono_Carico_Scarico_Dett.Pg_Inventario	And
       Buono_Carico_Scarico.Ti_Documento 	= Buono_Carico_Scarico_Dett.Ti_Documento	And
       Buono_Carico_Scarico.Esercizio 		= Buono_Carico_Scarico_Dett.Esercizio		And
       Buono_Carico_Scarico.Pg_Buono_C_S        = Buono_Carico_Scarico_Dett.Pg_Buono_C_S	And
       Tipo_Carico_Scarico.Cd_Tipo_Carico_Scarico = Buono_Carico_Scarico.Cd_Tipo_Carico_Scarico And
       Tipo_Carico_Scarico.Ti_Documento         = 'S'	And
    not  Exists(Select 1 From Buono_Carico_Scarico_Dett Dett Where
 	Dett.Pg_Inventario = Buono_Carico_Scarico_Dett.Pg_Inventario 	And
        Dett.Nr_Inventario = Buono_Carico_Scarico_Dett.Nr_Inventario	And
 	Dett.Progressivo   = Buono_Carico_Scarico_Dett.Progressivo      And
 	Dett.Ti_Documento  = 'S'					And
 	(Dett.Esercizio  >  To_Char(Data_A,'yyyy') or
 	 Dett.pg_buono_c_s > Buono_Carico_Scarico_Dett.pg_buono_c_s))       And
  Inventario_Beni.Pg_Inventario =  Ass.Pg_Inventario_Origine		And
  Inventario_Beni.Nr_Inventario =  Ass.Nr_Inventario_Origine		And
  Inventario_Beni.Progressivo   =  Ass.Progressivo_Origine And
   Not Exists( Select 1 From AMMORTAMENTO_BENE_INV AMM Where
       Inventario_Beni.Pg_Inventario =  Amm.Pg_Inventario
   And Inventario_Beni.Nr_Inventario =	Amm.Nr_Inventario
   And Inventario_Beni.Progressivo  = 	Amm.Progressivo)
  );
  End;
/


