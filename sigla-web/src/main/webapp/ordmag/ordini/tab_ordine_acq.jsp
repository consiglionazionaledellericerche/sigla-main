<%@ page 
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% CRUDBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
OrdineAcqBulk ordine = (OrdineAcqBulk)bp.getModel();
%>

<fieldset class="fieldset">
<table class="Panel">
<tr><td colspan=4>
 <div class="Group card"><table>      
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
				bp.getController().writeFormField(out, "dataOrdine");
			%>
			<%
				bp.getController().writeFormField(out, "percProrata");
			%>
			<% if (bp.isInserting()) {
				 bp.getController().writeFormField(out, "stato");
			   } else if (bp.isSearching()) {
			     bp.getController().writeFormField(out, "statoForSearch");
			   } else {
			     bp.getController().writeFormField(out, "statoForUpdate");
			   } %>
		</tr>
	</table>
	<table>
	<tr>	
	<td>
		<%
			bp.getController().writeFormField(out, "findDivisa");
		%>
	</td>
	<td>
		<% 	bp.getController().writeFormLabel( out, "cambio");
			bp.getController().writeFormInput( out, "default", "cambio", false, null, null); 				
		%>					
	</td>	
	</tr>
	</table>
    <table>
		<tr>
			<%
			bp.getController().writeFormField(out, "findTipoOrdine");
			%>
		</tr>
	</table>
    <table>
		<tr>
			<%
			bp.getController().writeFormField(out, "imImponibile");
			bp.getController().writeFormField(out, "imIva");
			bp.getController().writeFormField(out, "imIvaD");
			bp.getController().writeFormField(out, "imTotaleOrdine");
			%>
		</tr>
	</table>
	      </table>
      </div>
      </td></tr>
<table>
<tr><td colspan=4>
<div class="Group card">
	  <table>
		<tr>
				    <td><% bp.getController().writeFormLabel( out, "find_contratto"); %></td>
					<td>
					    <% bp.getController().writeFormInput( out, "esercizioContratto"); %>
						<% bp.getController().writeFormInput( out, "pgContratto"); %>
						<% bp.getController().writeFormInput( out, "oggettoContratto"); %>
						<% bp.getController().writeFormInput( out, "find_contratto"); %>
						<% bp.getController().writeFormInput( out, "visualizza_contratto"); %>
					</td>				 

		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "figura_giuridica_interna");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "findFirmatario");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "findResponsabile");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "findDirettore");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "cig");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "procedura_amministrativa");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "cup");
			%>
		</tr>
		<tr>
			<%
			bp.getController().writeFormField(out, "referenteEsterno");
			%>
		</tr>
	      </table>
      </div>
      </td></tr>
	      </table>
<table>
<tr><td colspan=4>
<div class="Group card">
    <table>
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
			bp.getController().writeFormField(out, "findNotaPrecodificata");
			%>
		</tr>
	</table>
      </div>
      </td></tr>
	      </table>
</table>
</fieldset>