<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
%>

<table class="Form" width="100%">
  <tr>
	<td><b><big>CdR</big></b><% JSPUtils.button(out,bp.encodePath("img/conferma.gif"),bp.encodePath("img/conferma.gif"),null,"javascript:submitForm('doCaricaLineeAttivita')", obbligazione.isConfermaCentriDiResponsabilitaEnabled() && bp.isEditable()); %>
		<% Button.write(out,bp.encodePath("img/zoom16.gif"),bp.encodePath("img/zoom16.gif"),null,"javascript:submitForm('doVisualizzaSpeseCdr')", null, "Prospetto situazione spese", bp.isVisualizzaSpeseCdrButtonEnabled()  ); %>

	</td>
	</tr>
	<tr>
	<td><%	bp.getCentriDiResponsabilita().writeHTMLTable(pageContext,"cdr",false,false,false,"100%","100px"); %></td>
	</tr>
	<tr>
	<td colspan=2>
			<b ALIGN="CENTER"><big>GAE</big></b><% JSPUtils.button(out,bp.encodePath("img/conferma.gif"),bp.encodePath("img/conferma.gif"), null,"javascript:submitForm('doConfermaLineeAttivita')", obbligazione.isConfermaLineeAttivitaEnabled() && bp.isEditable()); %>
	</td>
	</tr>
	<tr>
	<td colspan=2>
	<b>GAE da PdG approvato</b>
	</td>
	<td><b ALIGN="CENTER">Altri GAE</b></td>
	</tr>
	<tr>
		<td colspan=2>
		      <%	bp.getLineeDiAttivita().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
		<td><%	bp.getNuoveLineeDiAttivita().writeHTMLTable(pageContext,"latt",obbligazione.isAddNuoveLattEnabled() && bp.isEditable(),false,true,"100%","200px", false); %></td>
	</tr>
</table>