<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
%>

<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
		<td>
		     <%	bp.getTappaController().setEnabled( !bp.isEditingTappa());
		      	if((bp.isEditable()) && (!bp.isSearching()))
		      	{
					bp.getTappaController().writeHTMLTable(pageContext, null,true,false,true,"700px","150px");
		      	}
		      	else
		      	{
			      	// Visualizzazione missione
					bp.getTappaController().writeHTMLTable(pageContext, null,false,false,false,"700px","150px");
			    }  		
	      	%>
		</td>
	</tr>
	
	<tr></tr>
	<tr></tr>	
</table>

<div class="Group" style="width:93%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
	<td>
		<% bp.getTappaController().writeFormLabel( out, "dt_inizio_tappa"); %>
		<% bp.getTappaController().writeFormInput( out,"default","dt_inizio_tappa",!bp.isEditingTappa(),null, "onChange=\"submitForm('doCambiaDataTappa')\""); %>
	</td>	
	</tr>

	<tr></tr>
	<tr></tr>
</table>

<div class="Group" style="width:100%">
<table width="100%">
	<tr>																								
	<td><% bp.getTappaController().writeFormInput(out,"default","comuneRadioGroup",!bp.isEditingTappa(),null,"onClick=\"submitForm('doSetNazioneDivisaCambioItalia')\"");%></td>
	</tr>
</table>	

<table width="100%">
	<tr>
	<td><% bp.getTappaController().writeFormLabel( out, "pg_nazione");%></td>
	<td><% bp.getTappaController().writeFormInput(out,"default","pg_nazione",bp.isNazioneReadOnly(),null, null);%>	
		<% bp.getTappaController().writeFormInput(out,"default","ds_nazione",bp.isNazioneReadOnly(),null, null);%>	
		<% bp.getTappaController().writeFormInput(out,"default","find_nazione",bp.isNazioneReadOnly(),null, null);%>	
	</tr>

	<tr>
	<td><% bp.getTappaController().writeFormLabel( out, "cd_divisa_tappa");%></td>
	<td><% bp.getTappaController().writeFormInput(out,"cd_divisa_tappa");%>
		<% bp.getTappaController().writeFormInput(out,"ds_divisa_tappa");%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<% bp.getTappaController().writeFormLabel( out, "cambio_tappa");%>
		<% bp.getTappaController().writeFormInput(out,"default","cambio_tappa",bp.isCambioTappaReadOnly(),null, null);%>
	</td>		
	</tr>

	<tr>
	<td>
	</tr>
	
</table>	
</div>

<div class="Group" style="width:100%">
<table width="100%">
	<tr>
	<td><% bp.getTappaController().writeFormInput(out,"default","vittoAlloggioNavigazioneRadioGroup",!bp.isEditingTappa(),null, null);%></td>	
	</tr>
</table>
<table width="100%">
	<tr>
	<td><% bp.getTappaController().writeFormLabel( out, "fl_no_diaria"); %>
		<% bp.getTappaController().writeFormInput(out,"default","fl_no_diaria",!bp.isEditingTappa()||!bp.isDiariaEditable(HttpActionContext.getUserContext(session)),null, "onChange=\"submitForm('doOnFlDiariaChange')\"");%></td>
		<% if (bp.isRimborsoVisible(HttpActionContext.getUserContext(session))) { %>
	         <td><% bp.getTappaController().writeFormInput(out,"default","tipoRimborsoRadioGroup",!bp.isEditingTappa()||!bp.isRimborsoEditable(HttpActionContext.getUserContext(session)),null, null);%></td>
        <% } %>	
	</tr>			
</table>
</div>

<div class="Group" style="width:100%">
<table width="100%">
	<tr>
	<td>
		<% bp.getTappaController().writeFormLabel( out, "dt_ingresso_estero"); %>
		<% bp.getTappaController().writeFormInput(out,"default","dt_ingresso_estero",bp.isDataIngressoAbilitata(),null, null);%>
	</td>		
	<td>
		<% bp.getTappaController().writeFormLabel( out, "dt_uscita_estero"); %>
		<% bp.getTappaController().writeFormInput(out,"default","dt_uscita_estero",bp.isDataIngressoAbilitata(),null, null);%>
	</td>
	</tr>
</table>
</div>

<table width="100%">
	<tr></tr>
	<tr></tr>
	
	<tr>
	<td ALIGN="CENTER">
			<% JSPUtils.button(out,bp.encodePath("img/edit24.gif"),bp.encodePath("img/edit24.gif"), "Modifica","javascript:submitForm('doEditaTappa')", bp.isEditTappaButtonEnabled(),bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,bp.encodePath("img/save24.gif"),bp.encodePath("img/save24.gif"), "Conferma","javascript:submitForm('doConfermaTappa')", bp.isConfermaTappaButtonEnabled(),bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,bp.encodePath("img/undo24.gif"),bp.encodePath("img/undo24.gif"), "Annulla","javascript:submitForm('doUndoTappa')", bp.isUndoTappaButtonEnabled(),bp.getParentRoot().isBootstrap()); %>				
	</td>
	</tr>
	
	<tr></tr>
	<tr></tr>	
</table>

</div>