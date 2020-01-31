<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.CaricoManualeMagazzinoBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoRigaBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<% 
	CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)BusinessProcess.getBusinessProcess(request); 
	CaricoMagazzinoBulk model = (CaricoMagazzinoBulk)bp.getModel();
	
%>   
<div class="card p-1 m-1">
<table width="100%">
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findBeneServizio");	%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "findBeneServizio");	%></td>
		<td class="pl-5"><% bp.getBeniServiziColl().writeFormLabel(out, "cdUnitaMisuraMinima");	%></td>
		<td><% bp.getBeniServiziColl().writeFormInput(out, "cdUnitaMisuraMinima");	%></td>
	</tr>
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findUnitaMisura");	%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "findUnitaMisura");	%></td>
		<td class="pl-5"><% bp.getBeniServiziColl().writeFormLabel(out, "coefConv");	%></td>
		<td><% bp.getBeniServiziColl().writeFormInput(out, "coefConv");	%></td>
	</tr>
	<tr>
			<td>
				<%
					bp.getBeniServiziColl().writeFormLabel(out, "voce_iva");
				%>
			</td>
			<td >
				<%
					bp.getBeniServiziColl().writeFormInput(out, "voce_iva");
				%>
			</td>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "lottoFornitore");%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "lottoFornitore");%></td>
		<% CaricoMagazzinoRigaBulk modelRiga = (CaricoMagazzinoRigaBulk)bp.getBeniServiziColl().getModel();
		   if (modelRiga!=null && modelRiga.getBeneServizio() != null && modelRiga.getBeneServizio().getFlScadenza() != null  && modelRiga.getBeneServizio().getFlScadenza()) {%>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "dtScadenza");%></td>
		<td> <% bp.getBeniServiziColl().writeFormInput(out, "dtScadenza");%></td>
		<% }%>
	</tr>
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findTerzo");%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "findTerzo");%></td>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "dataBolla");%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "dataBolla");%></td>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "numeroBolla");%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "numeroBolla");%></td>
	</tr>
	<tr>
		<td class="pl-5"><% bp.getBeniServiziColl().writeFormLabel(out, "prezzoUnitario");	%></td>
		<td><% bp.getBeniServiziColl().writeFormInput(out, "prezzoUnitario");	%></td>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "quantita");%></td>
		<td ><% bp.getBeniServiziColl().writeFormInput(out, "quantita");%></td>
	</tr>
	<tr>
        <td colspan="5" width="100%">
			<table width="100%">
				<tr>
			        <td width="50%">
						<div class="GroupLabel font-weight-bold text-primary ml-2">Totale Lotti</div>  
						<div class="Group card p-3" width="100%">
						<table width="100%">
						    <tr>
								<% bp.getBeniServiziColl().writeFormField(out, "totGiacenzaLotti"); %>
								<% bp.getBeniServiziColl().writeFormField(out, "qtCaricoConvertita");
						  		%>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>