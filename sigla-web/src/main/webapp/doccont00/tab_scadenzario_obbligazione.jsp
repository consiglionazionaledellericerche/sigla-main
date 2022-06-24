<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
%>

<div class="Group card">
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_elemento_voceRO"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_elemento_voceRO"); %></td>
		<td class="w-100"><% bp.getController().writeFormInput( out, "ds_elemento_voce"); %></td>				 
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "im_tot_obbligazione"); %></td>
		<td colspan="2"><% bp.getController().writeFormInput( out, "default", "im_tot_obbligazione", true, null, null); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "im_parz_scadenze"); %></td>
		<td colspan="2"><% bp.getController().writeFormInput( out, "im_parz_scadenze"); %>
			            <% bp.getController().writeFormLabel( out, "im_residuo_obbligazione"); %>
           				<% bp.getController().writeFormInput( out, "im_residuo_obbligazione"); %></td>
	</tr>
	
	<tr>
		<td colspan="3">
		      <% bp.getScadenzario().setEnabled( !bp.isEditingScadenza());
			     bp.getScadenzario().writeHTMLTable(pageContext,"obbligazione",true,false,true,"100%","300px"); %>
		</td>
	</tr>
  	<tr>
  		<td colspan="3">
  		<br>
	      <%	JSPUtils.tabbed(
					pageContext,
					"tabScadenzario",
					new String[][] {
						{ "tabScadenza","Scadenza","/doccont00/tab_scadenza_obbligazione.jsp" },
						{ "tabDettaglioScadenza","Dettaglio Scadenza","/doccont00/tab_dettaglio_scadenza_obbligazione.jsp" } },
					bp.getTab("tabScadenzario"),
					"center", 
					"100%", null,
					!bp.isEditingScadenza() );
			
		%>
		</td>
	</tr>
  </table>
</div>