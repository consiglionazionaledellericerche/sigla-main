<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDReversaleBP bp = (CRUDReversaleBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ReversaleIBulk reversale = (it.cnr.contab.doccont00.core.bulk.ReversaleIBulk)bp.getModel();
%>
	<div class="Group">		
	<table border="0" cellspacing="0" cellpadding="2">

		<tr>
			<td><% bp.getController().writeFormLabel( out, "terzo_cd_terzo"); %></td>
			<td><% bp.getController().writeFormInput( out, "terzo_cd_terzo"); %>
			    <% bp.getController().writeFormInput( out, "terzo_ds_terzo"); %></td>						
			<td><% bp.getController().writeFormLabel( out, "terzo_tipo_bollo"); %></td>
			<td><% bp.getController().writeFormInput( out,"default", "terzo_tipo_bollo", reversale.isAnnullato(), null,"onchange=\"submitForm('doCambiaTipoBollo')\"" ); %>
			    <% bp.getController().writeFormInput( out, "terzo_im_tipo_bollo"); %></td>						
		</tr>
		<tr>
			<td><% bp.writeFormLabel( out, "im_reversale"); %></td>
			<td colspan=3><% bp.writeFormInput( out, "im_reversale"); %></td>
		</tr>
		
		
	</table>		
	</div>
	
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td colspan=3>
			      <b><font size=3 class="h3 text-primary">Righe reversale</font></b>
			      <% bp.getDocumentiAttiviSelezionati().writeHTMLTable(pageContext,(bp.isSiope_attiva() && reversale.isRequiredSiope())?"columnSetConSiope":null,false,false,bp.isInserting(),"100%","150px", true); %>
			</td>
		</tr>
	</table>
		
	<% if (bp.isSiope_attiva() && reversale.isRequiredSiope()) {%>
	<br><b><font size=3 class="h3 text-primary">Codici SIOPE</font></b>
		<div class="Group">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100">
			<tr>
			<td width="90%">
			<div class="Group">
				<table border="0" cellspacing="0" cellpadding="2" class="w-100">
					<tr>
						<td><span class="GroupLabel h5 text-primary">Associati</span></td>
						<td></td>
						<td><span class="GroupLabel h5 text-primary">Disponibili</span></td>
					</tr>
					<tr>
						<td  width="55%" rowspan="2">
					        <% bp.getCodiciSiopeCollegati().writeHTMLTable(pageContext,"collegaARigaReversale",false,false,false,"100%","150px", reversale.isAnnullato()); %>
						</td>
						<td  width="4%" align="center">
							<% JSPUtils.button(out,
									bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : bp.encodePath("img/doublerightarrow24.gif"),
									bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : bp.encodePath("img/doublerightarrow24.gif"),
									null,
									"javascript:submitForm('doRimuoviCodiceSiope')",
									"btn-outline-primary faa-parent animated-hover",
									bp.isAggiungiRimuoviCodiciSiopeEnabled(),
									bp.getParentRoot().isBootstrap()); %>
						</td>
						<td  width="41%" rowspan="2">
					      	<% bp.getCodiciSiopeCollegabili().writeHTMLTable(pageContext,"collegaARigaDocCont",false,false,false,"100%","150px", true); %>
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
			<fieldset class="fieldset card">
			<legend class="GroupLabel card-header h3 text-primary">RIEPILOGO</legend>
				<table border="0" cellspacing="0" cellpadding="2" class="card-block">
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td><span class="GroupLabel">Importo</span></td>
						<td><% bp.getDocumentiAttiviSelezionati().writeFormInput( out, "im_reversale_riga"); %></td>
					</tr>
					<tr>
						<td><span class="GroupLabel">Associato</span></td>
						<td><% bp.getDocumentiAttiviSelezionati().writeFormInput( out, "im_associato_siope"); %></td>
					</tr>
					<tr>
						<td><span class="GroupLabel">Residuo</span></td>
						<td><% bp.getDocumentiAttiviSelezionati().writeFormInput( out, "im_da_associare_siope"); %></td>
					</tr>
				</table>
			</fieldset>
			</td>
			</tr>
			</table>
		</div>
	<% } %>	
	<%if (bp.isCup_attivo() && reversale.isRequiredSiope()) {%> <!--se non è di regolarizzazione !-->
		<br><b><font size=3>CUP</font></b>
		<div class="Group">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100">
					<tr>
						<td colspan="6"> 
					        <% bp.getCupCollegati().writeHTMLTable(pageContext,"collegaARigaReversale",!reversale.isAnnullato(),false,!reversale.isAnnullato(),"100%","150px", true); %>
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
	<%if (bp.isSiope_cup_attivo() && reversale.isRequiredSiope()) {%> <!--se non è di regolarizzazione !-->
		<br><b><font size=3>CUP</font></b>
		<div class="Group">
			<table border="0" cellspacing="0" cellpadding="2" class="w-100">
					<tr>
						<td colspan="6"> 
					        <% bp.getSiopeCupCollegati().writeHTMLTable(pageContext,"collegaARigaReversaleSiope",!reversale.isAnnullato(),false,!reversale.isAnnullato(),"100%","150px", true); %>
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
