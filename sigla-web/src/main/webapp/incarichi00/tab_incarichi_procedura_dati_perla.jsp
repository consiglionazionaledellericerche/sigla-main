<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
%>

<%if (bp==null||bp.isIncarichiProceduraBP()) {%>
<div class="Group">
    <table class="Panel" cellspacing=2>
		<tr>
	         <td><% bp.getController().writeFormLabel(out,"find_tipo_attivita_fp0");%></td>
	         <td><% bp.getController().writeFormInput(out,"find_tipo_attivita_fp0");%></td>
		</tr>
		<tr>
	         <td><% bp.getController().writeFormLabel(out,"find_tipo_attivita_fp1");%></td>
	         <td><% bp.getController().writeFormInput(out,"find_tipo_attivita_fp1");%></td>
		</tr>
		<tr>
	         <td><% bp.getController().writeFormLabel(out,"find_tipo_attivita_fp");%></td>
	         <td><% bp.getController().writeFormInput(out,"find_tipo_attivita_fp");%></td>
		</tr>
	</table>
</div>	
&nbsp;
<% } %>
<% if (procedura.isApplicazioneNormaAttiva()) { %>
<div class="Group">
    <table class="Panel" cellspacing=2>
		<tr>
	         <td><% bp.getController().writeFormLabel(out,"tipo_norma_perla");%></td>
	         <td><% bp.getController().writeFormInput(out,"tipo_norma_perla");%></td>
		</tr>
		<% if (procedura.getCd_tipo_norma_perla()!=null && procedura.getCd_tipo_norma_perla().equals("999")) { %>
		<tr>
	         <td><% bp.getController().writeFormLabel(out,"ds_libera_norma_perla");%></td>
	         <td><% bp.getController().writeFormInput(out,"ds_libera_norma_perla");%></td>
		</tr>
		<% } %>
	</table>
</div>
&nbsp;
<% } %>
<div class="Group">
    <table class="Panel" cellspacing=2>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"procedura_amministrativa_beneficiario");%></td>
			<td><% bp.getController().writeFormInput(out,"procedura_amministrativa_beneficiario");%></td>
		</tr>
	</table>
</div>		