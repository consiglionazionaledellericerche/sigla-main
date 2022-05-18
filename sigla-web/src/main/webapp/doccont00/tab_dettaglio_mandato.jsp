<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = (it.cnr.contab.doccont00.core.bulk.MandatoIBulk)bp.getModel();
%>
	<div class="Group card">
        <table border="0" cellspacing="0" cellpadding="2" class="w-100">
            <tr>
                <% bp.getController().writeFormField( out, "terzo"); %>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "terzo_tipo_bollo"); %></td>
                <td>
                    <% bp.getController().writeFormInput( out,"default", "terzo_tipo_bollo", mandato.isAnnullato(), null,"onchange=\"submitForm('doCambiaTipoBollo')\"" ); %>
                    <% bp.getController().writeFormInput( out, "terzo_im_tipo_bollo"); %>
                </td>
            </tr>

            <% if (mandato.getTerzo_cedente() != null && mandato.getTerzo_cedente().getCd_terzo() != null ) { %>
                <tr>
                    <td><% bp.getController().writeFormLabel( out, "cd_terzo_cedente"); %></td>
                    <td><% bp.getController().writeFormInput( out, "cd_terzo_cedente"); %>
                        <% bp.getController().writeFormInput( out, "ds_terzo_cedente"); %></td>
                </tr>
            <%}%>
            <tr>
                <td><% bp.writeFormLabel( out, "im_mandato"); %></td>
                <td>
                    <% bp.writeFormInput( out, "im_mandato"); %>
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                        bp.encodePath("Disp.sui capitoli"),
                        "javascript:submitForm('doVisualizzaDispCassaCapitolo')",
                        "btn-outline-primary btn-title ml-3",
                        bp.isDispCassaCapitoloButtonEnabled(),
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        </table>
	</div>
	
	<div class="Group card">
        <b class="text-primary h3">Righe mandato</b>
        <% bp.getDocumentiPassiviSelezionati().writeHTMLTable(
                    pageContext,
                    (bp.isSiope_attiva() && mandato.isRequiredSiope())?"columnSetConSiope":null,
                    false,
                    false,
                    bp.isInserting(),
                    "100%",
                    "auto",
                    true);
        %>
    </div>
	<% if (bp.isSiope_attiva() && mandato.isRequiredSiope()) {%>
	<br><b class="text-primary h3">Codici SIOPE</b>
		<div class="Group card">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100 h-100">
			<tr>
			<td width="90%">
			<div class="Group">
				<table border="0" cellspacing="0" cellpadding="2" class="w-100 h-100">
					<tr>
						<td><span class="GroupLabel h4 text-primary">Associati</span></td>
						<td></td>
						<td><span class="GroupLabel h4 text-primary">Disponibili</span></td>
					</tr>
					<tr>
						<td  width="55%" rowspan="2">
					        <% bp.getCodiciSiopeCollegati().writeHTMLTable(pageContext,"collegaARigaMandato",false,false,false,"100%","100px",  mandato.isAnnullato()); %>
						</td>
						<td  width="4%" align="center">
						    <% JSPUtils.button(out,
							    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : "img/doublerightarrow24.gif",
							    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : "img/doublerightarrow24.gif",
							    null,
							    "javascript:submitForm('doRimuoviCodiceSiope')",
							    "btn-outline-primary faa-parent animated-hover",
							    bp.isAggiungiRimuoviCodiciSiopeEnabled(),
							    bp.getParentRoot().isBootstrap()); %>
						</td>
						<td  width="41%" rowspan="2">
					      	<% bp.getCodiciSiopeCollegabili().writeHTMLTable(pageContext,"collegaARigaDocCont",false,false,false,"100%","100px", true); %>
						</td>
					</tr>
					<tr>
						<td align="center">
						    <% JSPUtils.button(out,
						        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : bp.encodePath("img/doubleleftarrow24.gif"),
						        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : bp.encodePath("img/doubleleftarrow24.gif"),
                                null,
						        "javascript:submitForm('doAggiungiCodiceSiope')",
						        "btn-outline-primary faa-parent animated-hover",
						        bp.isAggiungiRimuoviCodiciSiopeEnabled(),
						        bp.getParentRoot().isBootstrap()); %>
						</td>
					</tr>
				</table>
			</div>
			</td>
			<td width="10%">
			<fieldset class="fieldset card border-primary text-primary p-2">
			<legend class="GroupLabel card-header bg-primary text-white">RIEPILOGO</legend>
				<table border="0" cellspacing="0" cellpadding="2">
					<tr><td>&nbsp;</td></tr>
					<tr> 
						<td><span class="GroupLabel">Importo</span></td>
						<td><% bp.getDocumentiPassiviSelezionati().writeFormInput( out, "im_mandato_riga"); %></td>
					</tr>
					<tr>
						<td><span class="GroupLabel">Associato</span></td>
						<td><% bp.getDocumentiPassiviSelezionati().writeFormInput( out, "im_associato_siope"); %></td>
					</tr>
					<tr>
						<td><span class="GroupLabel">Residuo</span></td>
						<td><% bp.getDocumentiPassiviSelezionati().writeFormInput( out, "im_da_associare_siope"); %></td>
					</tr>
				</table>
			</fieldset>
			</td>
			</tr>
			</table>
		</div>
	<% } %>
	<%if (bp.isCup_attivo() && mandato.isRequiredSiope()) {%> <!--se non è di regolarizzazione !-->
		<br><b class="text-primary h3">CUP</b>
		<div class="Group card">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100">
					<tr>
						<td colspan="6"> 
					        <% bp.getCupCollegati().writeHTMLTable(pageContext,"collegaARigaMandato", !mandato.isAnnullato(),false, !mandato.isAnnullato(),"100%","100px",true); %>
						</td>
					</tr>
					<tr>	
						<td><% bp.getCupCollegati().writeFormField(out,"cdCup"); %> 
			    			<% bp.getCupCollegati().writeFormField(out,"dsCup"); %>
			   				<% bp.getCupCollegati().writeFormField(out,"findCup"); %></td>
			   			<td><% bp.getCupCollegati().writeFormField(out,"importo"); %></td> 	
					</tr>
			</table>
		</div>
	<% } %>	 
	<%if (bp.isSiope_cup_attivo() && mandato.isRequiredSiope()) {%> <!--se non è di regolarizzazione !-->
		<br><b class="text-primary h3">CUP</b>
		<div class="Group card">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100">
					<tr>
						<td colspan="6"> 
					        <% bp.getSiopeCupCollegati().writeHTMLTable(pageContext,"collegaARigaMandatoSiope",!mandato.isAnnullato(),false,!mandato.isAnnullato(),"100%","100px", true); %>
						</td>
					</tr>
					<tr>	
						<td><% bp.getSiopeCupCollegati().writeFormField(out,"cdCup"); %> 
			    			<% bp.getSiopeCupCollegati().writeFormField(out,"dsCup"); %>
			   				<% bp.getSiopeCupCollegati().writeFormField(out,"findCup"); %></td>
			   			<td><% bp.getSiopeCupCollegati().writeFormField(out,"importo"); %></td> 	
					</tr>
			</table>
		</div>
	<% } %>	 
