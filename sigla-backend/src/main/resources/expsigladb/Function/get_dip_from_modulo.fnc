CREATE OR REPLACE function get_dip_from_modulo(
 aEsercizio number,
 aPg_progetto number,
 aTipo_fase varchar2) return varchar2 is
--==================================================================================================
--
-- Date: 12/12/2006
-- Version: 1.0
--
-- Ritorna il codice del dipartimento in base al Modulo
--
-- History:
--
-- Date: 12/12/2006
-- Version: 1.0
-- Creazione function
--
--
-- Body:
--
--==================================================================================================
 cod_dip DIPARTIMENTO.CD_DIPARTIMENTO%TYPE;
 modulo_ese_pr_padre PROGETTO.ESERCIZIO_PROGETTO_PADRE%Type;
 modulo_pg_progetto_padre PROGETTO.PG_PROGETTO_PADRE%Type;
 modulo_fase_pr_padre PROGETTO.TIPO_FASE_PROGETTO_PADRE%Type;
 commessa_ese_pr_padre PROGETTO.ESERCIZIO_PROGETTO_PADRE%Type;
 commessa_pg_progetto_padre PROGETTO.PG_PROGETTO_PADRE%Type;
 commessa_fase_pr_padre PROGETTO.TIPO_FASE_PROGETTO_PADRE%Type;
 livello_prog PROGETTO.LIVELLO%Type;

 APPO_cod_dip DIPARTIMENTO.CD_DIPARTIMENTO%TYPE;

Begin

If aEsercizio Is Not Null And aPg_progetto Is Not Null And aTipo_fase Is Not Null Then

   Select ESERCIZIO_PROGETTO_PADRE,PG_PROGETTO_PADRE,TIPO_FASE_PROGETTO_PADRE, LIVELLO, CD_DIPARTIMENTO
   Into modulo_ese_pr_padre,modulo_pg_progetto_padre,modulo_fase_pr_padre, livello_prog, cod_dip
   From progetto
   Where ESERCIZIO = aEsercizio
     And PG_PROGETTO = aPg_progetto
     And TIPO_FASE = aTipo_fase;

   if (livello_prog = 1) then
      return cod_dip;
   end if;

   Select ESERCIZIO_PROGETTO_PADRE,PG_PROGETTO_PADRE,TIPO_FASE_PROGETTO_PADRE, LIVELLO, CD_DIPARTIMENTO
   Into commessa_ese_pr_padre,commessa_pg_progetto_padre,commessa_fase_pr_padre, livello_prog, cod_dip
   From progetto
   Where ESERCIZIO = modulo_ese_pr_padre
     And PG_PROGETTO = modulo_pg_progetto_padre
     And TIPO_FASE = modulo_fase_pr_padre;

   if (livello_prog = 1) then
      return cod_dip;
   end if;

   Select CD_DIPARTIMENTO Into cod_dip
   From progetto
   Where ESERCIZIO = commessa_ese_pr_padre
     And PG_PROGETTO = commessa_pg_progetto_padre
     And TIPO_FASE = commessa_fase_pr_padre;

  return cod_dip;

Elsif aEsercizio Is Null And aPg_progetto Is Not Null And aTipo_fase Is Null Then

For CUR_COMMESSE In (Select Distinct PG_PROGETTO_PADRE
                     From   progetto
                     Where  PG_PROGETTO = aPg_progetto) Loop

  For CUR_PROGETTI In (Select Distinct PG_PROGETTO_PADRE
                       From   progetto
                       Where  PG_PROGETTO = CUR_COMMESSE.PG_PROGETTO_PADRE) Loop

   Select Distinct CD_DIPARTIMENTO
   Into   APPO_cod_dip
   From   progetto
   Where  PG_PROGETTO = CUR_PROGETTI.PG_PROGETTO_PADRE;

   If APPO_COD_DIP != Nvl(COD_DIP, APPO_COD_DIP) Then
     Return Null;
   Else
     COD_DIP := APPO_COD_DIP;
   End If;

  End Loop;

End Loop;

  return cod_dip;

End If;

Exception
  When No_Data_Found Then return Null;
  When Others Then return Null;
End;
/


