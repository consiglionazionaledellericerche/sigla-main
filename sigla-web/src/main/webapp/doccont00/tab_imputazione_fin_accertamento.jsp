<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk)bp.getModel();
%>

<table class="Form" width="100%">
  <tr>
	<td>
		<b><big class="text-primary h5">CdR</big></b>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
				bp.getParentRoot().isBootstrap() ?"Conferma" : null,
				"javascript:submitForm('doCaricaLineeAttivita')",
				"btn-secondary btn-outline-secondary btn-title",
				accertamento.isConfermaCentriDiResponsabilitaEnabled() && bp.isEditable(),
				bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
	<tr>
	<td colspan=2>
	<%	bp.getCentriDiResponsabilita().writeHTMLTable(pageContext,"cdr",false,false,false,"100%","100px"); %><td></td>
	</tr>
	<tr>
		<td colspan=2>
				<b ALIGN="CENTER"><big class="text-primary h5">GAE</big></b>
				<% JSPUtils.button(out,
						bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
						bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
						bp.getParentRoot().isBootstrap() ?"Conferma" : null,
						"javascript:submitForm('doConfermaLineeAttivita')", 
						"btn-secondary btn-outline-secondary btn-title",
						accertamento.isConfermaLineeAttivitaEnabled() && bp.isEditable(),
						bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<b class="h5 text-info">GAE da PdG approvato</b>
		</td>
		<td>
			<b ALIGN="CENTER" class="h5 text-info">Altri GAE</b>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<% bp.getLineeDiAttivita().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
		<td>
			<% bp.getNuoveLineeDiAttivita().writeHTMLTable(pageContext,"latt",accertamento.isAddNuoveLattEnabled() && bp.isEditable(),false,true,"100%","200px", false); %></td>
	</tr>
</table>