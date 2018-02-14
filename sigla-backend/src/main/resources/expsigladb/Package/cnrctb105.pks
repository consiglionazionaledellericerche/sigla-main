CREATE OR REPLACE package CNRCTB105 as
--
-- CNRCTB105 - Package gestione applicativa documenti amministrativi
-- Date: 03/11/2004
-- Version: 1.13
--
-- Package gestione applicativa documenti amministrativi
-- in ottica di chiusura contabile
--
-- Dependency:
--
-- History:
--
-- Date: 06/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 16/06/2003
-- Version: 1.1
-- Fix
--
-- Date: 23/06/2003
-- Version: 1.2
-- Corretta impostazione stato
--
-- Date: 30/06/2003
-- Version: 1.3
-- Fix per fatture create da compenso
--
-- Date: 15/07/2003
-- Version: 1.4
-- Aggiunto metodo isRiportato
-- Gestione di tutte le tipologie di documenti
--
-- Date: 16/07/2003
-- Version: 1.5
-- Fix per compenso con cori negativi
--
-- Date: 16/07/2003
-- Version: 1.6
-- Fix errore in isRiportato: semantica invertita
--
-- Date: 17/07/2003
-- Version: 1.7
-- Fix getStatoRiportato per missioni provvisorie o con anticipo> missione
--
-- Date: 15/09/2003
-- Version: 1.8
-- Aggiunta documentazione pre-post conditions
--
-- Date: 30/01/2004
-- Version: 1.9
-- Il metodo isRiportato non funzionava correttamente su ANTICIPO
--
-- Date: 13/10/2004
-- Version: 1.10
-- Fix al metodo getStatoRiportato
--
-- Date: 26/10/2004
-- Version: 1.11
-- Aggiunta metodo getStatoRiportatoInScrivania() e relativi metodi privati
--
-- Date: 02/11/2004
-- Version: 1.12
-- Fix al metodo getStatoRiportato per gestione rimborso/anticipo
--
-- Date: 03/11/2004
-- Version: 1.13
-- Se il documento non ? mai stato riportato, getStatoRiportatoInScrivania
-- ritorna NON_RIPORTATO
--
-- Constants:

-- Stati del documento
COMPLETAMENTE_RIPORTATO CONSTANT CHAR(1) := 'R';
NON_RIPORTATO CONSTANT CHAR(1) := 'N';
PARZIALMENTE_RIPORTATO CONSTANT CHAR(1) := 'Q';

-- Functions e Procedures:
--
-- getStatoRiportato
-- =================
--
-- pre-post name: Documento amministrativo non valido
-- pre: documento amministrativo diverso da documento generico, fattura passiva,
--      fattura attiva, rimborso, anticipo, compenso, missione
-- post: viene sollevato un errore
--
-- Casi richiamati dalle pre-post conditions:
-- A1. si distinguono i seguenti casi:
--       1. esercizio del documento amm pari all'esercizio di scrivania e pari
--          all'esercizio degli accertamenti: documento non riportato, ritorna 'N'
--       2. esercizio del documento amm pari all'esercizio di scrivania, esercizio
--          degli accertamenti diverso dall'esercizio di scrivania: documento
--          riportato, ritorna 'R'
--       3. esercizio di scrivania diverso dall'esercizio del documento amm ed
--          esercizio degli accertamenti pari all'esercizio di scrivania: documento
--          non riportato, ritorna 'N'
--       4. altri casi (esercizio del documento amm diverso da esercizio di
--          scrivania ed esercizio degli accertamenti diverso dall'esercizio
--          del documento amm): documento riportato, ritorna 'R'
--
-- A2. si distinguono i seguenti casi:
--       1. esercizio del documento amm pari all'esercizio di scrivania e pari
--          al massimo esercizio degli accertamenti: documento non riportato, ritorna 'N'
--       2. esercizio del documento amm pari all'esercizio di scrivania, massimo
--          esercizio degli accertamenti diverso dall'esercizio di scrivania: documento
--          parzialmente riportato, ritorna 'Q'
--       3. esercizio di scrivania diverso dall'esercizio del documento amm e
--          massimo esercizio degli accertamenti pari all'esercizio di scrivania:
--          documento non riportato, ritorna 'N'
--       4. altri casi (esercizio del documento amm diverso da esercizio di
--          scrivania e massimo esercizio degli accertamenti diverso dall'esercizio
--          del documento amm): documento parzialmente riportato, ritorna 'Q'
--
-- B1. si distinguono i seguenti casi:
--       1. esercizio del documento amm pari all'esercizio di scrivania e pari
--          all'esercizio delle obbligazioni: documento non riportato, ritorna 'N'
--       2. esercizio del documento amm pari all'esercizio di scrivania, esercizio
--          delle obbligazioni diverso dall'esercizio di scrivania: documento
--          riportato, ritorna 'R'
--       3. esercizio di scrivania diverso dall'esercizio del documento amm ed
--          esercizio delle obbligazioni pari all'esercizio di scrivania: documento
--          non riportato, ritorna 'N'
--       4. altri casi (esercizio del documento amm diverso da esercizio di
--          scrivania ed esercizio delle obbligazioni diverso dall'esercizio
--          del documento amm): documento riportato, ritorna 'R'
--
-- B2. si distinguono i seguenti casi:
--       1. esercizio del documento amm pari all'esercizio di scrivania e pari
--          al massimo esercizio delle obbligazioni: documento non riportato, ritorna 'N'
--       2. esercizio del documento amm pari all'esercizio di scrivania, massimo
--          esercizio delle obbligazioni diverso dall'esercizio di scrivania: documento
--          parzialmente riportato, ritorna 'Q'
--       3. esercizio di scrivania diverso dall'esercizio del documento amm e
--          massimo esercizio delle obbligazion pari all'esercizio di scrivania:
--          documento non riportato, ritorna 'N'
--       4. altri casi (esercizio del documento amm diverso da esercizio di
--          scrivania e massimo esercizio delle obbligazioni diverso dall'esercizio
--          del documento amm): documento parzialmente riportato, ritorna 'Q'
--
-- =============================
-- = OSSERVAZIONI (25/10/2004) =
-- =============================
-- Il metodo ha lo scopo di verificare se il documento amministrativo ? stato riportato dall'esercizio
-- di scrivania ad esercizi successivi, totalmente e parzialmente. Viene utilizzato per l'interfaccia
-- grafica, per l'abilitazione/disabilitazione dei pulsanti di riporto/deriporto/annulla (concorrentemente
-- ad altre condizioni!).
-- I casi 1-2-3-4 elencati sopra, sono i casi significativi. In realt? le condizioni di relazione fra
-- esercizio doc contabile, esercizio doc amministrativo ed esercizio di scrivania sono:
-- 			 - esDocAmm <= esDocCont
-- 			 - esDocAmm <= esScr
-- 			 - esDocCont >=< esScr
-- quindi i casi reali sono 6:
-- 		  1. esDocAmm = esDocCont = esScr (=> pre-post n.1)
-- 		  2. esDocAmm = esDocCont < esScr (non significativo - documento comunque non modificabile)
-- 		  3. esDocAmm < esDocCont = esScr (=> pre-post n.3)
-- 		  4. esDocAmm = esScr < esDocCont (=> pre-post n.2)
-- 		  5. esDocAmm < esDocCont < esScr (non significativo - documento comunque non modificabile)
-- 		  6. esDocAmm < esScr < esDocCont (=> pre-post n.4)
-- Ma quelli significativi sono i 4 gi? esamintati. Nei due casi non significativi 2 e 5 si ? scelto
-- di ritornare NON_RIPORTATO, per non dare l'informazione scorretta sullo stato del documento
-- comunque bloccato in modifica da altre condizioni.
-- =============================
--
-- pre-post name: Documento generico di entrata, righe contabilizzate sullo stesso
--                esercizio
-- pre: la chiave del documento amministrativo identifica un documento generico
--      di entrata, le righe del documento generico sono contabilizzate su accertamenti
--      dello stesso esercizio
-- post: si veda A1.
--
-- pre-post name: Documento generico di entrata, righe contabilizzate sul esercizi
--                diversi
-- pre: la chiave del documento amministrativo identifica un documento generico
--      di entrata, le righe del documento generico sono contabilizzate su accertamenti
--      di esercizi diversi
-- post: si veda A2.
--
-- pre-post name: Documento generico di spesa, righe contabilizzate sullo stesso
--                esercizio
-- pre: la chiave del documento amministrativo identifica un documento generico
--      di spesa, le righe del documento generico sono contabilizzate su obbligazioni
--      dello stesso esercizio
-- post: si veda B1.
--
-- pre-post name: Documento generico di spesa, righe contabilizzate sul esercizi
--                diversi
-- pre: la chiave del documento amministrativo identifica un documento generico
--      di spesa, le righe del documento generico sono contabilizzate su obbligazioni
--      di esercizi diversi
-- post: si veda B2.
--
-- pre-post name: Fattura passiva: nota di credito sull'ente, righe contabilizzate
--                sullo stesso esercizio
-- pre: la chiave del documento amministrativo identifica una fattura passiva,
--      le righe della nota di credito sull'ente sono contabilizzate su accertamenti
--      dello stesso esercizio
-- post: si veda A1.
--
-- pre-post name: Fattura passiva: nota di credito sull'ente, righe contabilizzate
--                su esercizi diversi
-- pre: la chiave del documento amministrativo identifica una fattura passiva,
--      le righe della nota di credito sull'ente sono contabilizzate su accertamenti
--      di esercizi diversi
-- post: si veda A2.
--
-- pre-post name: Fattura passiva e note di debito, righe contabilizzate sullo
--                stesso esercizio
-- pre: la chiave del documento amministrativo identifica una fattura passiva,
--      le righe della fattura passiva sono contabilizzate su obbligazioni
--      dello stesso esercizio
-- post: si veda B1.
--
-- pre-post name: Fattura passiva e note di debito, righe contabilizzate su
--                esercizi diversi
-- pre: la chiave del documento amministrativo identifica una fattura passiva,
--      le righe della fattura passiva sono contabilizzate su obbligazioni
--      di esercizi diversi
-- post: si veda B2.
--
-- pre-post name: Fattura attiva, nota di debito o nota di credito su accertamento,
--                righe contabilizzate sullo stesso esercizio
-- pre: la chiave del documento amministrativo identifica una fattura attiva,
--      le righe della fattura attiva sono contabilizzate su accertamenti
--      dello stesso esercizio
-- post: si veda A1.
--
-- pre-post name: Fattura attiva, nota di debito o nota di credito su accertamento,
--                righe contabilizzate sul esercizi diversi
-- pre: la chiave del documento amministrativo identifica una fattura attiva,
--      le righe della fattura attiva sono contabilizzate su accertamenti
--      di esercizi diversi
-- post: si veda A2.
--
-- pre-post name: Fattura attiva: nota di credito su obbligazione, righe
--                contabilizzate sullo stesso esercizio
-- pre: la chiave del documento amministrativo identifica una fattura attiva,
--      le righe della fattura attiva sono contabilizzate su obbligazioni
--      dello stesso esercizio
-- post: si veda B1.
--
-- pre-post name: Fattura attiva: nota di credito su obbligazione, righe
--                contabilizzate su esercizi diversi di obbligazioni
-- pre: la chiave del documento amministrativo identifica una fattura attiva,
--      le righe della fattura attiva sono contabilizzate su obbligazioni
--      di esercizi diversi
-- post: si veda B2.
--
-- pre-post name: Rimborso
-- pre: la chiave del documento amministrativo identifica un rimborso
-- post: si veda A1.
--
-- pre-post name: Anticipo
-- pre: la chiave del documento amministrativo identifica un anticipo
-- post: si veda B1.
--
-- pre-post name: Compenso associato a linea di attivit?
-- pre: la chiave del documento amministrativo identifica un compenso, il compenso
--      non ? associato a obbligazione ma a linea di attivit?
-- post: si distinguono i casi:
--       1. esercizio del documento pari all'esercizio di scrivania: documento
--          non riportato, ritorna 'N'
--       2. esercizio del documento minore dell'esercizio di scrivania: documento
--          riportato, ritorna 'R'
--
-- pre-post name: Compenso associato ad obbligazione
-- pre: la chiave del documento amministrativo identifica un compenso, il compenso
--      ? associato ad obbligazione
-- post: si veda B1.
--
-- pre-post name: Missione non associata a compenso, o missione provvisoria
-- pre: la chiave del documento amministrativo identifica una missione, la missione
--      ? provvisoria oppure non associata a compenso
-- post: se la missione non ? associata a obbligazione:
--       1. esercizio del documento pari all'esercizio di scrivania: documento
--          non riportato, ritorna 'N'
--       2. esercizio del documento minore dell'esercizio di scrivania: documento
--          riportato, ritorna 'R'
--       se la missione ? associata a obbligazione: si veda B1.
--
-- pre-post name: Missione associata a compenso
-- pre: la chiave del documento amministrativo identifica una missione, la missione
--      ? associata a compenso
-- post: ritorna lo stato del compenso - si veda pre-post relative al compenso

-- Verifica lo stato dei documenti contabili collegati alle righe dei documenti amministrativi
-- Parametri:
-- (aCdCds, aCdUo, aEs, aPg, aTipoDocAmm) identifica il documento amministrativo
-- aEsScr: esercizio di scrivania rispetto al quale ? necessario verificare lo stato
-- Return: 'N' : documenti contabili non riportati in nessuna riga
-- 		   'Q' : documenti contabili riportati per alcune righe
-- 		   'R' : documenti contabili riportati per tutte le righe
--
function getStatoRiportato(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2, aEsScr number) return varchar2;

-- Verifica se il documento risulta riportato ad esercizio diverso da quello del documento
-- Parametri:
-- (aCdCds, aCdUo, aEs, aPg, aTipoDocAmm) identifica il documento amministrativo
-- Return: 'N' : documenti contabili non riportati per tutte le righe
-- 		   'Y' : documenti contabili riportati per almeno una riga
--
function isRiportato(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2) return varchar2;

-- Stabilisce se il documento amministrativo ? stato riportato totalmente o parzialmente
-- nell'esercizio di scrivania, e non a successivi
-- Parametri:
-- (aCdCds, aCdUo, aEs, aPg, aTipoDocAmm) identifica il documento amministrativo
-- aEsScr: esercizio di scrivania rispetto al quale ? necessario verificare lo stato
-- Return: 'N' : documenti contabili non riportati in nessuna riga
-- 		   'Q' : documenti contabili riportati all'esercizio di scrivania per alcune righe
-- 		   'R' : documenti contabili riportati all'esercizio di scrivania (e non a successivi!) per tutte le righe
function getStatoRiportatoInScrivania(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2, aEsScr number) return varchar2;

END;


CREATE OR REPLACE PACKAGE BODY CNRCTB105 IS

function getStato(aEs number, aEsScr number, aEsDocCont number,rip_ParzRip varchar2)
return varchar2 is
statoRiportato char(1) := NON_RIPORTATO;
begin
	 -- 1. Es = EsScr = aEsDocCont
	 if (aEs = aEsScr and aEsScr = aEsDocCont) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 2. Es = aEsDocCont < aEsScr
	 -- caso non significativo, il documento non ? comunque modificabile
	 elsif (aEs = aEsDocCont and aEsDocCont < aEsScr) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 3. Es < EsScr = aEsDocCont
	 elsif (aEs < aEsScr and aEsScr = aEsDocCont) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 4. Es = EsScr < aEsDocCont
	 elsif (aEs = aEsScr and aEsScr < aEsDocCont) then
	 	statoRiportato := rip_ParzRip;
	 -- 5. Es < aEsDocCont < EsScr
	 -- caso non significativo, il documento  non ? comunque modificabile
	 elsif (aEs < aEsDocCont and aEsDocCont < aEsScr) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 6. Es < EsScr < aEsDocCont
	 elsif (aEs < aEsScr and aEsScr < aEsDocCont) then
	 	statoRiportato := rip_ParzRip;
	 end if;
	 return statoRiportato;
end;

function getStatoInScriv(aES number,aEsScr number, aEsDocCont number,rip_ParzRip varchar2)
return varchar2 is
begin
	if aEsDocCont = aEsScr then
	   if aEs = aEsDocCont then
	   	  -- documento mai riportato
		  return NON_RIPORTATO;
	   else
	   	   return rip_ParzRip;
	   end if;
	else
	   return NON_RIPORTATO;
	end if;
end;

procedure determinaEsStatoFP(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number,aEsDocCont in out number,rip_ParzRip in out varchar2) is
aFP fattura_passiva%rowtype;
begin
	select * into aFP
	from fattura_passiva
	where cd_cds 		         = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio				 = aEs
	  and pg_fattura_passiva 	 = aPg;

	if aFp.ti_fattura = 'C' and aFP.cd_cds = cnrctb020.getCDCDSENTE(aEs) then
	    begin  -- nota di credito sull'ente --> accertamento
			 select distinct esercizio_accertamento into aEsDocCont
			 from fattura_passiva_riga
			 where cd_cds			      = aFP.cd_cds
			   and cd_unita_organizzativa = aFP.cd_unita_organizzativa
			   and esercizio 			  = aFP.esercizio
			   and pg_fattura_passiva 	  = aFP.pg_fattura_passiva;

			 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

		exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
			 select max(esercizio_accertamento) into aEsDocCont
			 from fattura_passiva_riga
			 where cd_cds 			      = aFP.cd_cds
			   and cd_unita_organizzativa = aFP.cd_unita_organizzativa
			   and esercizio 			  = aFP.esercizio
			   and pg_fattura_passiva 	  = aFP.pg_fattura_passiva;

			 rip_ParzRip := PARZIALMENTE_RIPORTATO;
		end;

	else -- fattura passiva e note di debito

	    begin -- obbligazione
			 select distinct esercizio_obbligazione into aEsDocCont
			 from fattura_passiva_riga
			 where cd_cds			      = aCdCds
			   and cd_unita_organizzativa = aCdUo
			   and esercizio 			  = aEs
			   and pg_fattura_passiva 	  = aPg;

			 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

		exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
			 select max(esercizio_obbligazione) into aEsDocCont
			 from fattura_passiva_riga
			 where cd_cds = aCdCds
			   and cd_unita_organizzativa = aCdUo
			   and esercizio = aEs
			   and pg_fattura_passiva = aPg;

			 rip_ParzRip := PARZIALMENTE_RIPORTATO;
		end;

	end if;
end;

procedure determinaEsStatoFA(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number,aEsDocCont in out number,rip_ParzRip in out varchar2) is
aFA fattura_attiva%rowtype;
aCdsAccert varchar2(30);
aCdsObblig varchar2(30);
begin
	select * into aFa
	from fattura_attiva
	where cd_cds   		   		 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_fattura_attiva		 = aPg;

	select distinct cd_cds_obbligazione, cd_cds_accertamento
	into aCdsObblig, aCdsAccert
	from fattura_attiva_riga
	where cd_cds 	   	   		 = aFa.cd_cds
	  and cd_unita_organizzativa = aFa.cd_unita_organizzativa
	  and esercizio 			 = aFa.esercizio
	  and pg_fattura_attiva 	 = aFa.pg_fattura_attiva;

	if aFA.ti_fattura <> 'C'
	   or (aFa.ti_fattura = 'C' and aCdsObblig is null) then
	    begin
			 select distinct esercizio_accertamento into aEsDocCont
			 from fattura_attiva_riga
			 where cd_cds			      = aFa.cd_cds
			   and cd_unita_organizzativa = aFa.cd_unita_organizzativa
			   and esercizio 			  = aFa.esercizio
			   and pg_fattura_attiva 	  = aFa.pg_fattura_attiva;

			 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

		exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
			 select max(esercizio_accertamento) into aEsDocCont
			 from fattura_attiva_riga
			 where cd_cds 			      = aFa.cd_cds
			   and cd_unita_organizzativa = aFa.cd_unita_organizzativa
			   and esercizio 			  = aFa.esercizio
			   and pg_fattura_attiva 	  = aFa.pg_fattura_attiva;

			 rip_ParzRip := PARZIALMENTE_RIPORTATO;
		end;

	else
	    begin -- nota di credito su obbligazione
			 select distinct esercizio_obbligazione into aEsDocCont
			 from fattura_attiva_riga
			 where cd_cds			      = aFa.cd_cds
			   and cd_unita_organizzativa = aFa.cd_unita_organizzativa
			   and esercizio 			  = aFa.esercizio
			   and pg_fattura_attiva 	  = aFa.pg_fattura_attiva;

			 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

		exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
			 select max(esercizio_obbligazione) into aEsDocCont
			 from fattura_attiva_riga
			 where cd_cds			      = aFa.cd_cds
			   and cd_unita_organizzativa = aFa.cd_unita_organizzativa
			   and esercizio 			  = aFa.esercizio
			   and pg_fattura_attiva 	  = aFa.pg_fattura_attiva;

			 rip_ParzRip := PARZIALMENTE_RIPORTATO;
		end;

	end if;
end;

procedure determinaEsStatoDocGenEtr(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number,aCdTipoDocAmm varchar2,aEsDocCont in out number,rip_ParzRip in out varchar2) is
begin
    begin
		 select distinct esercizio_accertamento into aEsDocCont
		 from documento_generico_riga
		 where cd_cds			      = aCdCds
		   and cd_unita_organizzativa = aCdUo
		   and esercizio 			  = aEs
		   and cd_tipo_documento_amm  = aCdTipoDocAmm
		   and pg_documento_generico  = aPg;

		 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

	exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
		 select max(esercizio_accertamento) into aEsDocCont
		 from documento_generico_riga
		 where cd_cds 			      = aCdCds
		   and cd_unita_organizzativa = aCdUo
		   and esercizio 			  = aEs
		   and cd_tipo_documento_amm  = aCdTipoDocAmm
		   and pg_documento_generico  = aPg;

		 rip_ParzRip := PARZIALMENTE_RIPORTATO;
	end;
end;

procedure determinaEsStatoDocGenSpe(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number,aCdTipoDocAmm varchar2,aEsDocCont in out number,rip_ParzRip in out varchar2) is
begin
    begin
		 select distinct esercizio_obbligazione into aEsDocCont
		 from documento_generico_riga
		 where cd_cds			      = aCdCds
		   and cd_unita_organizzativa = aCdUo
		   and esercizio 			  = aEs
		   and cd_tipo_documento_amm  = aCdTipoDocAmm
		   and pg_documento_generico  = aPg;

		 rip_ParzRip := COMPLETAMENTE_RIPORTATO;

	exception when TOO_MANY_ROWS then  -- estrae pi? di un esercizio
		 select max(esercizio_obbligazione) into aEsDocCont
		 from documento_generico_riga
		 where cd_cds 				  = aCdCds
		   and cd_unita_organizzativa = aCdUo
		   and esercizio 			  = aEs
		   and cd_tipo_documento_amm  = aCdTipoDocAmm
		   and pg_documento_generico  = aPg;

		 rip_ParzRip := PARZIALMENTE_RIPORTATO;
	end;
end;

function getEsDocContRimborso(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number)
return number is
aEsDocCont number;
begin
	select esercizio_accertamento into aEsDocCont
	from rimborso
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_rimborso			 = aPg;
	return aEsDocCont;
end;

function getEsDocContAnticipo(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number)
return number is
aEsDocCont number;
begin
	select esercizio_obbligazione into aEsDocCont
	from anticipo
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_anticipo			 = aPg;
	return aEsDocCont;
end;

function getStatoRiportatoCompenso(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number, aEsScr number,isInScr boolean)
return varchar2 is
statoRiportato char(1);
aEsObblig number;
aComp compenso%rowtype;
begin
	select * into aComp
	from compenso
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_compenso			 = aPg;

	aEsObblig:= aComp.esercizio_obbligazione;

	if aEsObblig is null then
		return NON_RIPORTATO;  -- non ha obbligazioni associate, ma LdA
	else
		if isInScr then
		    return getStatoInScriv(aEs,aEsScr,aEsObblig,COMPLETAMENTE_RIPORTATO);
		else
			return getStato(aEs,aEsScr,aEsObblig,COMPLETAMENTE_RIPORTATO);
		end if;
	end if;

end;

function getStatoRiportatoMissione(aCdCds varchar2,aCdUo varchar2,aEs number,aPg number, aEsScr number,isInScr boolean)
return varchar2 is
aMissione missione%rowtype;
aEsObblig number;
aComp compenso%rowtype;
begin
	select *
	into aMissione
	from missione
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_missione			 = aPg;

	aEsObblig := aMissione.esercizio_obbligazione;

	if aMissione.fl_associato_compenso = 'N'
	   or aMissione.ti_provvisorio_definitivo = 'P'
	then
		if aEsObblig is null then
		    return NON_RIPORTATO;
		else
			if isInScr then
			    return getStatoInScriv(aEs,aEsScr,aEsObblig,COMPLETAMENTE_RIPORTATO);
			else
				return getStato(aEs,aEsScr,aEsObblig,COMPLETAMENTE_RIPORTATO);
			end if;
		end if;

	else  -- associata a compenso e definitiva (non esistono missioni provvisorie, associate a compenso)
		begin
			select * into aComp
			from compenso
			where cd_cds_missione = aCdCds
			  and cd_uo_missione  = aCdUo
			  and esercizio_missione = aEs
			  and pg_missione		 = aPg;
			return getStatoRiportatoCompenso(aComp.cd_cds,aComp.cd_unita_organizzativa,aComp.esercizio,aComp.pg_compenso,aEsScr,isInScr);
		exception when NO_DATA_FOUND then
			ibmerr001.RAISE_ERR_GENERICO('Compenso non trovato per missione associata a compenso n.'||aPg||' cds.'||aCdCds||' esercizio '||aEs);
		end;
	end if;
end;

function getStatoRiportato(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2, aEsScr number)
return varchar2 is
tipoDocAmm tipo_documento_amm%rowtype;
rip_ParzRip char(1):= null;
aEsDocCont number := null;
begin
	select * into tipoDocAmm
	from tipo_documento_amm
	where cd_tipo_documento_amm = aCdTipoDocAmm;

	if tipoDocAmm.FL_DOC_GENERICO = 'Y' then
 	   if tipoDocAmm.TI_ENTRATA_SPESA = 'E' then
	   	  	determinaEsStatoDocGenEtr(aCdCds,aCdUo,aEs,aPg,aCdTipoDocAmm,aEsDocCont,rip_ParzRip);
			return getStato(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	   else
	   	    determinaEsStatoDocGenSpe(aCdCds,aCdUo,aEs,aPg,aCdTipoDocAmm,aEsDocCont,rip_ParzRip);
			return getStato(aEs,aEsScr,aEsDocCont,rip_ParzRip);
 	   end if;
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_PASSIVA then
	   determinaEsStatoFP(aCdCds,aCdUo,aEs,aPg,aEsDocCont,rip_ParzRip);
	   return getStato(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_ATTIVA then
	   determinaEsStatoFA(aCdCds,aCdUo,aEs,aPg,aEsDocCont,rip_ParzRip);
	   return getStato(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	elsif aCdTipoDocAmm = CNRCTB100.TI_RIMBORSO then
	   aEsDocCont := getEsDocContRimborso(aCdCds,aCdUo,aEs,aPg);
	   return getStato(aEs,aEsScr,aEsDocCont,COMPLETAMENTE_RIPORTATO);
	elsif aCdTipoDocAmm = CNRCTB100.TI_ANTICIPO then
	   aEsDocCont := getEsDocContAnticipo(aCdCds,aCdUo,aEs,aPg);
	   return getStato(aEs,aEsScr,aEsDocCont,COMPLETAMENTE_RIPORTATO);
	elsif aCdTipoDocAmm = CNRCTB100.TI_COMPENSO then
	   return getStatoRiportatoCompenso(aCdCds,aCdUo,aEs,aPg,aEsScr,false);
	elsif aCdTipoDocAmm = CNRCTB100.TI_MISSIONE then
	   return getStatoRiportatoMissione(aCdCds,aCdUo,aEs,aPg,aEsScr,false);
	else
	   ibmerr001.RAISE_ERR_GENERICO('Impossibile recuperare lo stato del documento');
	end if;

end;

 function isRiportato(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2)
 return varchar2 is
  tipoDocAmm tipo_documento_amm%rowtype;
  statoRiportato char(1);
  aNum number;
 begin
	select * into tipoDocAmm
	from tipo_documento_amm
	where cd_tipo_documento_amm = aCdTipoDocAmm;

	if tipoDocAmm.FL_DOC_GENERICO = 'Y' then
	    begin
	     select distinct 1 into aNum from documento_generico_riga where
		                 cd_tipo_documento_amm = aCdTipoDocAmm
					 and cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_documento_generico = aPg
					 and esercizio = aEs
					 and (
					     esercizio_accertamento is not null  and esercizio <> esercizio_accertamento
					  or esercizio_obbligazione is not null and esercizio <> esercizio_obbligazione
					 );
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_PASSIVA then
	    begin
	     select distinct 1 into aNum from fattura_passiva_riga where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_fattura_passiva = aPg
					 and esercizio = aEs
					 and (
					     esercizio_accertamento is not null  and esercizio <> esercizio_accertamento
					  or esercizio_obbligazione is not null and esercizio <> esercizio_obbligazione
					 );
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_ATTIVA then
	    begin
	     select distinct 1 into aNum from fattura_attiva_riga where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_fattura_attiva = aPg
					 and esercizio = aEs
					 and (
					     esercizio_accertamento is not null  and esercizio <> esercizio_accertamento
					  or esercizio_obbligazione is not null and esercizio <> esercizio_obbligazione
					 );
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_COMPENSO then
	    begin
	     select distinct 1 into aNum from compenso where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_compenso = aPg
					 and esercizio = aEs
					 and (
					     esercizio_accertamento is not null  and esercizio <> esercizio_accertamento
					  or esercizio_obbligazione is not null and esercizio <> esercizio_obbligazione
					 );
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_MISSIONE then
	    begin
	     select distinct 1 into aNum from missione where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_missione = aPg
					 and esercizio = aEs
					 and esercizio_obbligazione is not null
					 and esercizio <> esercizio_obbligazione;
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_ANTICIPO then
	    begin
	     select distinct 1 into aNum from anticipo where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_anticipo = aPg
					 and esercizio = aEs
					 and esercizio_obbligazione is not null
					 and esercizio <> esercizio_obbligazione;
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_RIMBORSO then
	    begin
	     select distinct 1 into aNum from rimborso where
					     cd_cds = aCdCds
					 and cd_unita_organizzativa = aCdUo
					 and pg_rimborso = aPg
					 and esercizio = aEs
					 and esercizio_accertamento is not null
					 and esercizio <> esercizio_accertamento;
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	else
	 IBMERR001.RAISE_ERR_GENERICO('Tipo di documento non supportato:'||aCdTipoDocAmm);
	end if;
 end;

function getStatoRiportatoInScrivania(aCdCds varchar2,aCdUo varchar2, aEs number, aPg number, aCdTipoDocAmm varchar2, aEsScr number)
return varchar2 is
tipoDocAmm tipo_documento_amm%rowtype;
rip_ParzRip char(1):= null;
aEsDocCont number := null;
begin
	select * into tipoDocAmm
	from tipo_documento_amm
	where cd_tipo_documento_amm = aCdTipoDocAmm;

	if tipoDocAmm.FL_DOC_GENERICO = 'Y' then
 	   if tipoDocAmm.TI_ENTRATA_SPESA = 'E' then
	   	  	determinaEsStatoDocGenEtr(aCdCds,aCdUo,aEs,aPg,aCdTipoDocAmm,aEsDocCont,rip_ParzRip);
			return getStatoInScriv(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	   else
	   	    determinaEsStatoDocGenSpe(aCdCds,aCdUo,aEs,aPg,aCdTipoDocAmm,aEsDocCont,rip_ParzRip);
			return getStatoInScriv(aEs,aEsScr,aEsDocCont,rip_ParzRip);
 	   end if;
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_PASSIVA then
	   determinaEsStatoFP(aCdCds,aCdUo,aEs,aPg,aEsDocCont,rip_ParzRip);
	   return getStatoInScriv(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	elsif aCdTipoDocAmm = CNRCTB100.TI_FATTURA_ATTIVA then
	   determinaEsStatoFA(aCdCds,aCdUo,aEs,aPg,aEsDocCont,rip_ParzRip);
	   return getStatoInScriv(aEs,aEsScr,aEsDocCont,rip_ParzRip);
	elsif aCdTipoDocAmm = CNRCTB100.TI_RIMBORSO then
	   aEsDocCont := getEsDocContRimborso(aCdCds,aCdUo,aEs,aPg);
	   return getStatoInScriv(aEs,aEsScr,aEsDocCont,COMPLETAMENTE_RIPORTATO);
	elsif aCdTipoDocAmm = CNRCTB100.TI_ANTICIPO then
	   aEsDocCont := getEsDocContAnticipo(aCdCds,aCdUo,aEs,aPg);
	   return getStatoInScriv(aEs,aEsScr,aEsDocCont,COMPLETAMENTE_RIPORTATO);
	elsif aCdTipoDocAmm = CNRCTB100.TI_COMPENSO then
	   return getStatoRiportatoCompenso(aCdCds,aCdUo,aEs,aPg,aEsScr,true);
	elsif aCdTipoDocAmm = CNRCTB100.TI_MISSIONE then
	   return getStatoRiportatoMissione(aCdCds,aCdUo,aEs,aPg,aEsScr,true);
	else
	   ibmerr001.RAISE_ERR_GENERICO('Impossibile recuperare lo stato del documento');
	end if;

end;

END;


