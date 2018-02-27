<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
%>

<table width="100%">
	<tr>
	<td>
		<% bp.getController().writeFormLabel( out, "tipo_trattamento"); %>
		<% bp.getController().writeFormInput( out, null,"tipo_trattamento",false,null,"onChange=\"submitForm('doOnTipoTrattamentoChange')\"");%>		
	</td>
	</tr>
</table>

<div class="Group" style="width:93%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
	<td><% bp.getDiariaController().writeHTMLTable(pageContext, "dettaglioDiariaSet",false,false,false,"700px","150px");%></td>
	</tr>

	<tr></tr>
	<tr></tr>
</table>
</div>

<div class="Group" style="width:93%">
<table width="100%">
	<tr>
	<td>
		<% bp.getDiariaController().writeFormLabel( out, "im_diaria_netto"); %>
		<% bp.getDiariaController().writeFormInput( out, "im_diaria_netto"); %>		
	</td>
	<td>
		<% bp.getDiariaController().writeFormLabel( out, "im_quota_esente"); %>
		<% bp.getDiariaController().writeFormInput( out, "im_quota_esente"); %>		
	</td>	
	</tr>
</table>
</div>