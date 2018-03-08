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
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudDettagliPartner_esterni();
%>

<%	controller.writeHTMLTable(pageContext,"partner_esterno",true,false,true,"100%","100px"); %>

	<table class="Panel">
		<tr>
			<% controller.writeFormField(out,"default","partner_esterno"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","importo"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","n_persone"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","note"); %>
		</tr>		
	</table>