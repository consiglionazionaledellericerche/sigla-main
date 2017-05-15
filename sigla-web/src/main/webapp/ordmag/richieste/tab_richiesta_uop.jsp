<%@ page 
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP,
		it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% CRUDBP bp = (CRUDRichiestaUopBP)BusinessProcess.getBusinessProcess(request);
   RichiestaUopBulk richiesta = (RichiestaUopBulk)bp.getModel();
%>

<div class="Group">
	<table class="Panel">
	  	<tr>
		 	<% bp.getController().writeFormField(out,"findUnitaOperativaOrd"); %>
		    <% if (!bp.isSearching() || (richiesta != null && richiesta.getCdUnitaOperativa() != null)) { %>	 
			  	<tr>
				 	<% bp.getController().writeFormField(out,"esercizio"); %>
				 	<% bp.getController().writeFormField(out,"cdNumeratore"); %>
				 	<% bp.getController().writeFormField(out,"numero"); %>
				</tr>
			<% } %>
		</tr>
	</table>
</div>
