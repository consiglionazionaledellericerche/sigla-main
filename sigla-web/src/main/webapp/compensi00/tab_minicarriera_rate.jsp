<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)BusinessProcess.getBusinessProcess(request);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel(); %>

<div class="Group card p-2">
	<table>	  	
		<tr>
		
			<% bp.getController().writeFormField(out,"im_totale_minicarriera");%>
			<td>
			<% bp.getController().writeFormLabel(out,"numero_rate");%>
			<% if ( carriera.getFl_tassazione_separata()!=null && !carriera.getFl_tassazione_separata().booleanValue()) { %>		
				<% bp.getController().writeFormInput(out,null,"numero_rate",true,null,null);%>
			<% } else { %>
				<% bp.getController().writeFormInput(out,null,"numero_rate",false,null,null);%>
			<%	} %>		
			</td>
		</tr>
		<tr>   
			<td>
				<% bp.getController().writeFormLabel(out,"ti_anticipo_posticipo");%>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"ti_anticipo_posticipo",false,null,"onClick=\"submitForm('doOnAnticipoPosticipoChange')\"");%>
			</td>
			<% bp.getController().writeFormField(out,"mesi_anticipo_posticipo");%>
		</tr>
	</table>
</div>

<%	bp.getRateCRUDController().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %>

<div class="Group card p-2">
	<table class="w-100">
		<tr>
			<% bp.getRateCRUDController().writeFormField(out,"pg_rata");%>
			<% bp.getRateCRUDController().writeFormField(out,"im_rata");%>
		</tr>
		<tr>
			<% bp.getRateCRUDController().writeFormField(out,"dt_inizio_rata");%>
			<% bp.getRateCRUDController().writeFormField(out,"dt_fine_rata");%>
		</tr>
		<tr>
			<% bp.getRateCRUDController().writeFormField(out,"dt_scadenza");%>
		</tr>
	</table>
</div>