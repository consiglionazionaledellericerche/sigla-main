<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = (it.cnr.contab.doccont00.core.bulk.MandatoIBulk)bp.getModel();
%>
	<div class="Group">		
	<table border="0" cellspacing="0" cellpadding="2">

		<tr>
			<td><% bp.getController().writeFormLabel( out, "terzo_cd_terzo"); %></td>
			<td><% bp.getController().writeFormInput( out, "terzo_cd_terzo"); %>
			    <% bp.getController().writeFormInput( out, "terzo_ds_terzo"); %></td>						
			<td><% bp.getController().writeFormLabel( out, "terzo_tipo_bollo"); %></td>
			<td><% bp.getController().writeFormInput( out,"default", "terzo_tipo_bollo", false, "FormInput","onchange=\"submitForm('doCambiaTipoBollo')\"" ); %>
			    <% bp.getController().writeFormInput( out, "terzo_im_tipo_bollo"); %></td>						
		</tr>
		<% if (mandato.getTerzo_cedente() != null && mandato.getTerzo_cedente().getCd_terzo() != null ) { %>
			<td><% bp.getController().writeFormLabel( out, "cd_terzo_cedente"); %></td>
			<td colspan=3><% bp.getController().writeFormInput( out, "cd_terzo_cedente"); %>
			    <% bp.getController().writeFormInput( out, "ds_terzo_cedente"); %></td>						
		<%}%>	
		<tr>
			<td><% bp.writeFormLabel( out, "im_mandato"); %></td>
			<td><% bp.writeFormInput( out, "im_mandato"); %></td>
			<td colspan=2 align = "center">
				<% JSPUtils.button(out,bp.encodePath("img/zoom24.gif"), bp.encodePath("img/zoom24.gif"), bp.encodePath("Disp.sui capitoli"),"javascript:submitForm('doVisualizzaDispCassaCapitolo')",bp.isDispCassaCapitoloButtonEnabled() ); %>
			</td>
		</tr>
		
		
	</table>		
	</div>
	
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan=3>
			      <b><font size=3>Righe mandato</font></b>
			      <% bp.getDocumentiPassiviSelezionati().writeHTMLTable(pageContext,(bp.isSiope_attiva() && mandato.isRequiredSiope())?"columnSetConSiope":null,false,false,bp.isInserting(),"100%","150px", true); %>
			</td>
		</tr>
	</table>
	<% if (bp.isSiope_attiva() && mandato.isRequiredSiope()) {%>
	<br><b><font size=3>Codici SIOPE</font></b>
		<div class="Group">
			<table border="0" cellspacing="0" cellpadding="2">
			<tr>
			<td width="90%">
			<div class="Group">
				<table border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td><span class="GroupLabel">Associati</span></td>
						<td></td>
						<td><span class="GroupLabel">Disponibili</span></td>
					</tr>
					<tr>
						<td  width="55%" rowspan="2">
					        <% bp.getCodiciSiopeCollegati().writeHTMLTable(pageContext,"collegaARigaMandato",false,false,false,"100%","100px", false); %>
						</td>
						<td  width="4%" align="center">
							<% JSPUtils.button(out,bp.encodePath("img/doublerightarrow24.gif"),bp.encodePath("img/doublerightarrow24.gif"),null,"javascript:submitForm('doRimuoviCodiceSiope')",bp.isAggiungiRimuoviCodiciSiopeEnabled()); %>
						</td>
						<td  width="41%" rowspan="2">
					      	<% bp.getCodiciSiopeCollegabili().writeHTMLTable(pageContext,"collegaARigaDocCont",false,false,false,"100%","100px", true); %>
						</td>
					</tr>
					<tr>
						<td align="center">
						<% JSPUtils.button(out,bp.encodePath("img/doubleleftarrow24.gif"),bp.encodePath("img/doubleleftarrow24.gif"),null,"javascript:submitForm('doAggiungiCodiceSiope')",bp.isAggiungiRimuoviCodiciSiopeEnabled()); %>
						</td>
					</tr>
				</table>
			</div>
			</td>
			<td width="10%">
			<fieldset class="fieldset">
			<legend class="GroupLabel">RIEPILOGO</legend>
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
		<br><b><font size=3>CUP</font></b>
		<div class="Group">
			<table border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td colspan="6"> 
					        <% bp.getCupCollegati().writeHTMLTable(pageContext,"collegaARigaMandato",true,false,true,"100%","100px", true); %>
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
