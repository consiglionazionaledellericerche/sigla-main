<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.ScaricoManualeMagazzinoBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<% 
	ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)BusinessProcess.getBusinessProcess(request); 
	ScaricoMagazzinoBulk model = (ScaricoMagazzinoBulk)bp.getModel();
	ScaricoMagazzinoRigaBulk modelRiga = (ScaricoMagazzinoRigaBulk)bp.getBeniServiziColl().getModel();
%>   
<div class="card p-1 m-1">
<table width="100%">
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findBeneServizio");	%></td>
		<td colspan="3"><% bp.getBeniServiziColl().writeFormInput(out, "findBeneServizio");	%></td>
		<td class="pl-5"><% bp.getBeniServiziColl().writeFormLabel(out, "cdUnitaMisuraMinima");	%></td>
		<td><% bp.getBeniServiziColl().writeFormInput(out, "cdUnitaMisuraMinima");	%></td>
	</tr>
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findUnitaMisura");	%></td>
		<td colspan="3"><% bp.getBeniServiziColl().writeFormInput(out, "findUnitaMisura");	%></td>
		<td class="pl-5"><% bp.getBeniServiziColl().writeFormLabel(out, "coefConv");	%></td>
		<td><% bp.getBeniServiziColl().writeFormInput(out, "coefConv");	%></td>
	</tr>
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "findUnitaOperativaOrdRic");%></td>
		<td colspan="5"><% bp.getBeniServiziColl().writeFormInput(out, "findUnitaOperativaOrdRic");%></td>
	</tr>
	<tr>
		<td>
		<% if (modelRiga!=null && modelRiga.isImputazioneScaricoSuBeneEnable())
			  bp.getBeniServiziColl().writeFormLabel(out, "quantita");
  		   else
		      bp.getBeniServiziColl().writeFormLabel(out, "totQtScaricoLotti");
        %>
        </td>
        <td colspan="5" width="100%">
			<table width="100%">
				<tr>
					<td>
						<% if (modelRiga!=null && modelRiga.isImputazioneScaricoSuBeneEnable())
							  bp.getBeniServiziColl().writeFormInput(out, "quantita");
				  		   else
						      bp.getBeniServiziColl().writeFormInput(out, "totQtScaricoLotti");
				        %>
				    </td>
			        <td width="50%">
						<div class="GroupLabel font-weight-bold text-primary ml-2">Totale Lotti</div>  
						<div class="Group card p-3" width="100%">
						<table width="100%">
						    <tr>
								<% bp.getBeniServiziColl().writeFormField(out, "totGiacenzaLotti"); %>
								<% if (modelRiga!=null && modelRiga.isImputazioneScaricoSuBeneEnable())
									  bp.getBeniServiziColl().writeFormField(out, "qtScaricoConvertita");
						  		   else
								      bp.getBeniServiziColl().writeFormField(out, "totQtScaricoLottiConvertita");
								%>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% if (modelRiga!=null && modelRiga.getAnomalia()!=null) {%>
	<tr>
		<td><% bp.getBeniServiziColl().writeFormLabel(out, "anomalia");%></td>
		<td colspan="5"><% bp.getBeniServiziColl().writeFormInput(out, "anomalia");%></td>
	</tr>
	<% } %>
</table>
</div>