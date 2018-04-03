--------------------------------------------------------
--  DDL for Package Body CNRCTB105
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB105" IS

function getStato(aEs number, aEsScr number, aEsDocCont number,rip_ParzRip varchar2)
return varchar2 is
statoRiportato char(1) := NON_RIPORTATO;
begin
	 -- 1. Es = EsScr = aEsDocCont
	 if (aEs = aEsScr and aEsScr = aEsDocCont) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 2. Es = aEsDocCont < aEsScr
	 -- caso non significativo, il documento non è comunque modificabile
	 elsif (aEs = aEsDocCont and aEsDocCont < aEsScr) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 3. Es < EsScr = aEsDocCont
	 elsif (aEs < aEsScr and aEsScr = aEsDocCont) then
	 	statoRiportato := NON_RIPORTATO;
	 -- 4. Es = EsScr < aEsDocCont
	 elsif (aEs = aEsScr and aEsScr < aEsDocCont) then
	 	statoRiportato := rip_ParzRip;
	 -- 5. Es < aEsDocCont < EsScr
	 -- caso non significativo, il documento  non è comunque modificabile
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

		exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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

		exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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

		exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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

		exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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

	exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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

	exception when TOO_MANY_ROWS then  -- estrae più di un esercizio
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
	select max(esercizio_obbligazione) into aEsObblig
	from compenso_riga
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_compenso			 = aPg;

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

	select max(esercizio_obbligazione) 
	into aEsObblig
	from missione_riga
	where cd_cds 				 = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and esercizio 			 = aEs
	  and pg_missione			 = aPg;

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
	     select distinct 1 into aNum from compenso c, compenso_riga cr where
					     c.cd_cds = aCdCds
					 and c.cd_unita_organizzativa = aCdUo
					 and c.pg_compenso = aPg
					 and c.esercizio = aEs
					 and c.cd_cds = cr.cd_cds (+)
					 and c.cd_unita_organizzativa = cr.cd_unita_organizzativa (+)
					 and c.pg_compenso = cr.pg_compenso (+)
					 and c.esercizio = cr.esercizio (+)
					 and (
					     c.esercizio_accertamento is not null  and c.esercizio <> c.esercizio_accertamento
					  or cr.esercizio_obbligazione is not null and c.esercizio <> cr.esercizio_obbligazione
					 );
 	   	 return 'Y';
		exception when NO_DATA_FOUND then
		 return 'N';
		end;
	elsif aCdTipoDocAmm = CNRCTB100.TI_MISSIONE then
	    begin
	     select distinct 1 into aNum from missione_riga where
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
