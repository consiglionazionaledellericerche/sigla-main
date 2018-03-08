<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.prevent01.bp.*,
		it.cnr.contab.prevent01.bulk.*"
%>

<%
	CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller_det = ((CRUDDettagliContrSpeseBP)bp).getCrudDettagliContrSpese();
	SimpleDetailCRUDController controller_tes = ((CRUDDettagliContrSpeseBP)bp).getCrudDettagliDipArea();
%>

<div class="Group">
	<table class="Panel" cellspacing="2">
		<tr>
	  		<td><% controller_tes.writeFormLabel(out,"cd_dipartimento");%></td>
			<td><% controller_tes.writeFormInput(out,"cd_dipartimento");%></td>
			<td><% controller_tes.writeFormInput(out,"ds_dipartimento");%></td>
		</tr>
		<% if (!bp.isFlNuovoPdg()) { %>
		<tr>
	  		<td><% controller_tes.writeFormLabel(out,"cd_cds_area");%></td>
			<td><% controller_tes.writeFormInput(out,"cd_cds_area");%></td>
			<td><% controller_tes.writeFormInput(out,"ds_cds_area");%></td>
		</tr>
		<% } %>
	</table>
</div>
<%
	if (!bp.isFlNuovoPdg()) 
	  controller_det.writeHTMLTable(pageContext,(bp.getLivelloContrattazione().compareTo(new Integer(0))==0)?"csContrattazioneSenzaVoce":null,true,false,true,"100%","180px");
	else
	  controller_det.writeHTMLTable(pageContext,(bp.getLivelloContrattazione().compareTo(new Integer(0))==0)?"csContrattazioneSenzaVoceNuovoPdg":null,true,false,true,"100%","180px");
%>
<div class="Group">
	<table class="Panel" cellspacing="2">
		<% if (bp.getLivelloContrattazione().compareTo(new Integer(0))!=0) {%>
		<tr>
	  		<td><% controller_det.writeFormLabel(out,"classificazione");%></td>
			<td><% controller_det.writeFormInput(out,"classificazione");%></td>
		</tr>
		<%}%> 
		<% if (!bp.isFlNuovoPdg()) { %>
			<tr>
		  		<td><% controller_det.writeFormLabel(out,"searchtool_progetto");%></td>
				<td><% controller_det.writeFormInput(out,"searchtool_progetto");%></td>
			</tr>
		<% } else { %>
			<tr>
		  		<td><% controller_det.writeFormLabel(out,"searchtool_progetto_liv2");%></td>
				<td><% controller_det.writeFormInput(out,"searchtool_progetto_liv2");%></td>
			</tr>
		<% } %>
		
		<tr>
	  		<td><% controller_det.writeFormLabel(out,"cdr");%></td>
			<td><% controller_det.writeFormInput(out,"cdr");%></td>
		</tr>
	</table>
	<table class="Panel" cellspacing="5">
		<tr>
		  <td NOWRAP colspan=2><span class="FormLabel" style="color:blue">Fonti Interne</span></td>
		  <td NOWRAP colspan=2><span class="FormLabel" style="color:blue">Fonti Esterne</span></td>
		</tr>
		<tr>
		  <td NOWRAP><% controller_det.writeFormLabel(out,"tot_spese_decentr_int");%></td>
		  <td><% controller_det.writeFormInput(out,"tot_spese_decentr_int");%></td>
		  <td NOWRAP><% controller_det.writeFormLabel(out,"tot_spese_decentr_est");%></td>
		  <td><% controller_det.writeFormInput(out,"tot_spese_decentr_est");%></td>
		</tr>
		<tr>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td NOWRAP><% controller_det.writeFormLabel(out,"appr_tot_spese_decentr_int");%></td>
		    	<td><% controller_det.writeFormInput(out,null,"appr_tot_spese_decentr_int",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<td NOWRAP><% controller_det.writeFormLabel(out,"appr_tot_spese_decentr_int_disabled");%></td>
		  		<td><% controller_det.writeFormInput(out,"appr_tot_spese_decentr_int_disabled");%></td>
			<%}%>
			<% if (bp.isContrSpeseEnabled()) {%>
		    	<td NOWRAP><% controller_det.writeFormLabel(out,"appr_tot_spese_decentr_est");%></td>
		    	<td><% controller_det.writeFormInput(out,null,"appr_tot_spese_decentr_est",!bp.isContrSpeseAggiornabile(),null,null);%></td>
			<%} else {%>
		  		<td NOWRAP><% controller_det.writeFormLabel(out,"appr_tot_spese_decentr_est_disabled");%></td>
		  		<td><% controller_det.writeFormInput(out,"appr_tot_spese_decentr_est_disabled");%></td>
			<%}%>
		</tr>
		<tr>
		  <td NOWRAP><% controller_det.writeFormLabel(out,"daApprovareFI");%></td>
		  <td><% controller_det.writeFormInput(out,"daApprovareFI");%></td>
		  <td NOWRAP><% controller_det.writeFormLabel(out,"daApprovareFE");%></td>
		  <td><% controller_det.writeFormInput(out,"daApprovareFE");%></td>
		</tr>
   </table>
</div>
