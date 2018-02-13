--------------------------------------------------------
--  DDL for View VP_PDG_STATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_PDG_STATO" ("ESERCIZIO", "CD_CDR_SCRIVANIA", "DS_CDR_SCRIVANIA", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "TI_CDR", "STATO", "DS_STATO") AS 
  (
select
--
-- Date: 28/02/2003
-- Version: 1.2
--
-- Elenco stati pdg dipendenti da cd_cdr_scrivania secondo la seguente logica:
--    cd_cdr_scrivania='999.000.000'
--       cd_centro_responsabilita=cdr i livello o responsabile di AREA
--    cd_cdr_scrivania=cdr di primo livello
--       cd_centro_responsabilita=cd_cdr_scrivania o sottoposto RUO o NRUO di RUO sottoposto
--    cd_cdr_scrivania=cdr RUO
--       cd_centro_responsabilita=cd_cdr_scrivania o sottoposto NRUO
--
-- History:
--
-- Date: 18/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2002
-- Version: 1.1
-- Estrae anche NRUO per CDR di primo livello
--
-- Date: 28/02/2003
-- Version: 1.2
-- Nuovi stati per variazioni a pdg
--
-- Body:
--
 esercizio,
 cd_cdr_scrivania,
 ds_cdr_scrivania,
 cd_centro_responsabilita,
 ds_cdr,
 decode(ti_cdr,'R','RUO','N','NRUO','P','PRIMO','E','ENTE'),
 stato,
 decode(ds_stato,
  'A','Iniziale',
  'B','Pre chiusura',
  'C','Chiuso',
  'D','Pre apertura/chiusura contrattazione',
  'E','Riaperto per contrattazione',
  'C0','Riaperto d''ufficio ENTE',
  'C1','Riaperto d''ufficio CDR di primo livello',
  'C2','Riaperto d''ufficio CDR RUO',
  'F','Chiuso definitivamente',
  'G','Riapertura per variazioni',
  'H','Prechiusura per variazioni',
  'M','Modificato per variazioni',
  'Indefinito'
 ) ds_stato
from (
 -- Se si entra come ENTE '999'
    select
     a.esercizio,
     c.cd_centro_responsabilita cd_cdr_scrivania,
     c.ds_cdr ds_cdr_scrivania,
     a.cd_centro_responsabilita,
     b.ds_cdr,
	 'P' ti_cdr,
     a.stato,
     a.stato ds_stato
    from
      pdg_preventivo a, -- Piano di gestione
      cdr b, -- Cdr del pdg
      cdr c,  -- Cdr dell'utente che si è loggato
      unita_organizzativa d -- UO ENTE
     where
          a.cd_centro_responsabilita = b.cd_centro_responsabilita
	  and (b.livello = 1 or b.livello = 2 and b.cd_cdr_afferenza is null)
	  and d.cd_tipo_unita = 'ENTE'
	  and c.cd_unita_organizzativa = d.cd_unita_organizzativa
 union all
    select -- Se si entra come CDR di primo livello
     a.esercizio,
     c.cd_centro_responsabilita cd_cdr_scrivania,
     c.ds_cdr ds_cdr_scrivania,
     a.cd_centro_responsabilita,
     b.ds_cdr,
	 decode(b.cd_cdr_afferenza,null,'P',decode(to_number(b.cd_proprio_cdr),0,'R','N')) ti_cdr,
     a.stato,
     a.stato ds_stato
    from
      pdg_preventivo a, -- Piano di gestione
      cdr b, -- Cdr del pdg
      cdr c  -- Cdr dell'utente che si è loggato (CDR di primo livello o responsabile di area)
     where
          a.cd_centro_responsabilita = b.cd_centro_responsabilita
	  and (c.livello = 1 or c.livello = 2 and c.cd_cdr_afferenza is null)
	  and (
	      b.cd_centro_responsabilita = c.cd_centro_responsabilita
       or b.cd_cdr_afferenza = c.cd_centro_responsabilita -- RUO ed NRUO collegati al CDR di primo
       or b.cd_unita_organizzativa = c.cd_unita_organizzativa and to_number(b.cd_proprio_cdr) <> 0 -- NRUO collegati al CDR di primo
	  )
 union all
    select -- Se si entra come CDR di secondo livello RUO
     a.esercizio,
     c.cd_centro_responsabilita cd_cdr_scrivania,
     c.ds_cdr ds_cdr_scrivania,
     a.cd_centro_responsabilita,
     b.ds_cdr,
	 decode(to_number(b.cd_proprio_cdr),0,'R','N') ti_cdr,
     a.stato,
     a.stato ds_stato
    from
      pdg_preventivo a, -- Piano di gestione
      cdr b, -- Cdr del pdg
      cdr c  -- Cdr dell'utente che si è loggato (CDR di primo livello o responsabile di area)
     where
          a.cd_centro_responsabilita = b.cd_centro_responsabilita
	  and (c.livello = 2 and c.cd_cdr_afferenza is not null and to_number(c.cd_proprio_cdr) = 0)
	  and (
	      b.cd_centro_responsabilita = c.cd_centro_responsabilita
       or b.cd_unita_organizzativa = c.cd_unita_organizzativa and to_number(b.cd_proprio_cdr) <> 0
	  )
 )
)
;

   COMMENT ON TABLE "VP_PDG_STATO"  IS 'Elenco stati pdg dipendenti da cd_cdr_scrivania secondo la seguente logica:
   cd_cdr_scrivania=''999.000.000''
       cd_centro_responsabilita=cdr i livello o responsabile di AREA
    cd_cdr_scrivania=cdr di primo livello
       cd_centro_responsabilita=cd_cdr_scrivania o sottoposto RUO o NRUO di RUO sottoposto
    cd_cdr_scrivania=cdr RUO
       cd_centro_responsabilita=cd_cdr_scrivania o sottoposto NRUO';
