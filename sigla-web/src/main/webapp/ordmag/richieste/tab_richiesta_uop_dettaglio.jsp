<%@ page 
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP,
		it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)BusinessProcess.getBusinessProcess(request);
    RichiestaUopRigaBulk riga = (RichiestaUopRigaBulk)bp.getRighe().getModel();
	bp.getRighe().writeHTMLTable(pageContext,"righeSet",true,false,true,"100%","200px"); %>
  
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
			<%
				bp.getRighe().writeFormField(out, "findUnitaMisura");
				bp.getRighe().writeFormField(out, "coefConv");
				bp.getRighe().writeFormField(out, "quantitaRichiesta");
			%>
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
				bp.getRighe().writeFormField(out, "findProgetto");
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
				bp.getRighe().writeFormField(out, "findLineaAttivita");
			%>
		</tr>
	</table>
</div>
