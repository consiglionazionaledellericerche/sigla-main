<%@ page 
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<%  
CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
	OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)bp.getRighe().getModel();
%>

<div class="Group">
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findBeneServizio");
			    bp.getRighe().writeFormField(out, "cdUnitaMisuraMinima");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<% bp.getRighe().writeFormField(out, "findUnitaMisura");%>
			<td>
				<% bp.getRighe().writeFormLabel(out,"coefConv");%>
			</td>      	
			<td>
				<%
					if (riga != null){
						bp.getRighe().writeFormInput(out,null,"coefConv",riga.isROCoefConv(),null,"");
			    	} else {
						bp.getRighe().writeFormInput(out,null,"coefConv",false,null,"");
			    	}
			    %>
			</td>
			<%
				bp.getRighe().writeFormField(out, "cd_voce_iva");
			%>
			<td>
				<%
					bp.getRighe().writeFormInput(out, "ds_voce_iva");
				%>
			</td>
			<%
				bp.getRighe().writeFormField(out, "percentuale_voce_iva");
			%>
			<td>
				<%
					bp.getRighe().writeFormInput(out, null, "voce_iva", false, null, "");
				%>
			</td>
		</tr>
	</table>
	<table>
		<tr>

			<td>
				<%
					bp.getRighe().writeFormLabel(out, "dspQuantita");
				%>
			</td>
			<td>
				<%
					bp.getRighe().writeFormInput(out, null, "dspQuantita", false, null, "");
				%>
			</td>
			<td>
				<%
					bp.getRighe().writeFormField(out, "prezzoUnitario");
				%>
			</td>      	
			<td>
				<% bp.getRighe().writeFormField(out,"sconto1");%>
			</td>      	
			<td>
				<% bp.getRighe().writeFormField(out,"sconto2");%>
			</td>      	
			<td>
				<% bp.getRighe().writeFormField(out,"sconto3");%>
			</td>      	
		</tr>
	</table>
	<table>
      <tr>      	
			<td>
				<% bp.getRighe().writeFormLabel(out,"notaRiga");%>
			</td>      	
			<td colspan="4">
				<% bp.getRighe().writeFormInput(out,"notaRiga");%>
			</td>
      </tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findObbligazione");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findCentroResponsabilita");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findProgetto");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findLineaAttivita");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "tipoConsegna");
			    bp.getRighe().writeFormField(out, "findMagazzino");
			    bp.getRighe().writeFormField(out, "findLuogoConsegnaMag");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "dtPrevConsegna");
			    bp.getRighe().writeFormField(out, "findUnitaOperativaOrdDest");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "esercizio_ori_obbligazione");
				bp.getRighe().writeFormField(out, "pg_obbligazione");
				bp.getRighe().writeFormField(out, "pg_obbligazione_scadenzario");
				bp.getRighe().writeFormField(out, "dt_scadenza");
				bp.getRighe().writeFormField(out, "ds_scadenza_obbligazione");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "imImponibile");
			    bp.getRighe().writeFormField(out, "imIva");
			    bp.getRighe().writeFormField(out, "imIvaD");
			    bp.getRighe().writeFormField(out, "imTotaleRiga");
			%>
		</tr>
	</table>

</div>