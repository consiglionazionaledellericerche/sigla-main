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
			    bp.getRighe().writeFormField(out, "unitaMisuraMinima");
			%>
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
				<%bp.getRighe().writeFormField(out, "quantitaRichiesta"); %>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "findElementoVoce");
			%>
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
</div>