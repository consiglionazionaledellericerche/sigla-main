<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudDettagliFinanziatori();
%>

<%	controller.writeHTMLTable(pageContext,"finanziatore",true,false,true,"100%","100px"); %>

	<table class="Panel">
		<tr>
			<% controller.writeFormField(out,"default","finanziatore"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","importo_finanziato"); %>
		</tr>
	</table>