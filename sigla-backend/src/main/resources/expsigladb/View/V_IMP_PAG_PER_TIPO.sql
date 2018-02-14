--------------------------------------------------------
--  DDL for View V_IMP_PAG_PER_TIPO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_IMP_PAG_PER_TIPO" ("ESERCIZIO", "VOCE", "DESCRIZIONE", "TIPO", "IMPEGNATO", "PAGATO") AS 
  Select I.ESERCIZIO, I.VOCE, I.DESCRIZIONE, I.TIPO, Nvl(I.TOT_IMPEGNATO,0), Sum(P.TOT_PAGATO)
From  V_IMPEGNATO_PER_TIPO I, V_PAGATO_PER_TIPO P
Where I.ESERCIZIO = P.ESERCIZIO (+)
And   I.VOCE = P.VOCE (+)
And   I.DESCRIZIONE = P.DESCRIZIONE (+)
And   ((I.TIPO is not null and P.TIPO is not null and I.TIPO = P.TIPO)
	 Or
 	I.TIPO is null
	 Or
	P.TIPO is null)
Group By I.ESERCIZIO, I.VOCE, I.DESCRIZIONE, I.TIPO, Nvl(I.TOT_IMPEGNATO,0);
