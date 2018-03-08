<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondiric00.bp.*,
		it.cnr.contab.fondiric00.core.bulk.*"
%>

<%
	TestataFondiRicercaBP bp = (TestataFondiRicercaBP)BusinessProcess.getBusinessProcess(request);
	Fondo_attivita_vincolataBulk bulk = (Fondo_attivita_vincolataBulk)bp.getModel();
	SimpleDetailCRUDController controller = ( (TestataFondiRicercaBP)bp ).getCrudDettagli();
%>

<%	controller.writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); %>

	<table class="Panel">
		<tr>
			<% controller.writeFormField(out,"default","unita_organizzativa"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","responsabile"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","importo"); %>
		</tr>
	</table>