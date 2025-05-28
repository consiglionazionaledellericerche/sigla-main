<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();

    SimpleDetailCRUDController controller = bp.getIncarichiColl();
	Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)controller.getModel();
%>

<%if (bp==null||bp.isIncarichiProceduraBP()) {%>
<div class="Group card m-2 p-2">
    <table class="Panel w-100" cellspacing=2>
		<tr><% bp.getController().writeFormField(out,"find_tipo_attivita_fp0");%></tr>
		<tr><% bp.getController().writeFormField(out,"find_tipo_attivita_fp1");%></tr>
		<tr><% bp.getController().writeFormField(out,"find_tipo_attivita_fp");%></tr>
	</table>
</div>	
<% if (!bp.getParentRoot().isBootstrap()) { %>&nbsp; <% } %>
<% } %>
<% if (procedura.isApplicazioneNormaAttiva()) { %>
<div class="Group card m-2 p-2">
    <table class="Panel w-100" cellspacing=2>
		<tr><% bp.getController().writeFormField(out,"tipo_norma_perla");%></tr>
		<% if (procedura.getCd_tipo_norma_perla()!=null && procedura.getCd_tipo_norma_perla().equals("999")) { %>
		<tr><% bp.getController().writeFormField(out,"ds_libera_norma_perla");%></tr>
		<% } %>
	</table>
</div>
<% if (!bp.getParentRoot().isBootstrap()) { %>&nbsp; <% } %>
<% } %>
<div class="Group card m-2 p-2">
    <table class="Panel w-100" cellspacing=2>
		<tr><% bp.getController().writeFormField(out,"procedura_amministrativa_beneficiario");%></tr>
	</table>
</div>
<% if (incarico!=null && (incarico.getIdPerla()!=null || incarico.getIdPerlaNew()!=null || incarico.getAnomalia_perla()!=null)) { %>
<div class="Group card m-2 p-2">
    <table class="Panel w-100" cellspacing=2>
		<tr><% controller.writeFormField(out,"idPerla");%></tr>
		<tr><% controller.writeFormField(out,"idPerlaNew");%></tr>
		<tr><% controller.writeFormField(out,"anomalia_perla");%></tr>
	</table>
</div>
<% } %>
