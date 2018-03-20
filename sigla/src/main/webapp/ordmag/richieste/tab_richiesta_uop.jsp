<%@ page pageEncoding="UTF-8"
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
	<table>
		<tr>
			<%
				bp.getController().writeFormField(out, "findUnitaOperativaOrd");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getController().writeFormField(out, "findNumerazioneOrd");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getController().writeFormField(out, "esercizio");
			%>
			<%
				bp.getController().writeFormField(out, "cdNumeratore");
			%>
			<%
				bp.getController().writeFormField(out, "numero");
			%>
			<%
				bp.getController().writeFormField(out, "dataRichiesta");
			%>
			<% if (!bp.isSearching()) {
				 bp.getController().writeFormField(out, "stato");
			   } else {
			     bp.getController().writeFormField(out, "statoKeysForSearch");
			   } %>
		</tr>
	</table>
    <table>
      <tr>      	
			<td>
				<% bp.getController().writeFormLabel(out,"dsRichiesta");%>
			</td>      	
			<td colspan="4">
				<% bp.getController().writeFormInput(out,"dsRichiesta");%>
			</td>
      </tr>
      <tr>      	
			<td>
				<% bp.getController().writeFormLabel(out,"nota");%>
			</td>      	
			<td colspan="4">
				<% bp.getController().writeFormInput(out,"nota");%>
			</td>
      </tr>
	</table>
    <table>
		<tr>
			<%
			bp.getController().writeFormField(out, "findUnitaOperativaOrdDest");
			%>
		</tr>
	</table>
</div>
