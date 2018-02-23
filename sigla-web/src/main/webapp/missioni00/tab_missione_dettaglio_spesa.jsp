<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) bp.getSpesaController().getModel();

	if(spesa == null)
		spesa = new Missione_dettaglioBulk();	
%>

<div style="width:93%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
	<td><% bp.getSpesaController().writeFormLabel( out, "dt_inizio_tappa"); %></td>
	<td></td>
	</tr>
	
	<tr>
	<td><% bp.getSpesaController().writeFormInput( out, "default", "dt_inizio_tappa", !bp.getSpesaController().isEditingSpesa()||spesa.getSpesaIniziale()!=null, null,null);%></td>
	<td>
		<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
			<td><% bp.getSpesaController().writeFormLabel( out, "cd_ti_spesa"); %></td>
			<td>
				<% bp.getSpesaController().writeFormInput( out, "default", "cd_ti_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %>
				<% bp.getSpesaController().writeFormInput( out, "default", "ds_ti_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %>
				<% bp.getSpesaController().writeFormInput( out, "default", "find_tipo_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %>			
			</td>
			</tr>

			<tr>
			<td><% bp.getSpesaController().writeFormLabel( out, "ds_spesa"); %></td>
			<td><% bp.getSpesaController().writeFormInput( out, "default", "ds_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
			</tr>
			
			<tr></tr>
			<tr></tr>
		</table>
		</div>
	</td>	
	</tr>
	
</table>
	


<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>	
	<td><% bp.getSpesaController().writeFormLabel( out, "localita_spostamento"); %></td>
	<td><% bp.getSpesaController().writeFormInput( out, "default", "localita_spostamento", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
	<td>
		<% bp.getSpesaController().writeFormLabel( out, "fl_spesa_anticipata"); %>		
		<% bp.getSpesaController().writeFormInput( out, "default", "fl_spesa_anticipata", !bp.getSpesaController().isEditingSpesa(), null, null); %>					
	</td>
	</tr>
	
	<tr>
	<td><% bp.getSpesaController().writeFormLabel( out, "id_giustificativo"); %></td>
	<td><% bp.getSpesaController().writeFormInput( out, "default", "id_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
	<td></td>
	</tr>
	
	<tr>	
	<td><% 
	if((spesa != null) && (!spesa.isDettaglioMissioneFromGemis())){
		bp.getSpesaController().writeFormLabel( out, "ds_giustificativo");		
	} %></td>
	<td><% 	if((spesa != null) && (!spesa.isDettaglioMissioneFromGemis())){
		bp.getSpesaController().writeFormInput( out, "default", "ds_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null);
	} %></td>
	<td></td>	
	</tr>
	
	<tr>
	<td><% bp.getSpesaController().writeFormLabel( out, "ds_no_giustificativo"); %></td>
	<td><% bp.getSpesaController().writeFormInput( out, "default", "ds_no_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
</table>
</div>

<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>
	<tr></tr>

	<tr>
	<td>
		<% 	bp.getSpesaController().writeFormLabel( out, "im_spesa_divisa"); 
			if((spesa != null) && (spesa.isRimborsoKm()))
				bp.getSpesaController().writeFormInput( out, "default", "im_spesa_divisa", true, null, null);
			else
				bp.getSpesaController().writeFormInput( out, "default", "im_spesa_divisa", !bp.getSpesaController().isEditingSpesa(), null, null);
		%>		
	</td>
	<td></td>
	</tr>
	
	<tr>	
	<td>
		<% 	bp.getSpesaController().writeFormLabel( out, "cd_divisa_spesa"); %>
			&nbsp;
		<%			
			if((spesa != null) && (spesa.isRimborsoKm()))
			{
				bp.getSpesaController().writeFormInput( out, "default", "cd_divisa_spesa", true, null, null);
				bp.getSpesaController().writeFormInput( out, "default", "ds_divisa_spesa", true, null, null);
				bp.getSpesaController().writeFormInput( out, "default", "find_divisa_spesa", true, null, null);
			}
			else
			{
				bp.getSpesaController().writeFormInput( out, "default", "cd_divisa_spesa", !bp.getSpesaController().isEditingSpesa(), null, null);
				bp.getSpesaController().writeFormInput( out, "default", "ds_divisa_spesa", !bp.getSpesaController().isEditingSpesa(), null, null);
				bp.getSpesaController().writeFormInput( out, "default", "find_divisa_spesa", !bp.getSpesaController().isEditingSpesa(), null, null);				
			}	
		%>			
	</td>
	<td>
		&nbsp;&nbsp;&nbsp;
		<% 	bp.getSpesaController().writeFormLabel( out, "cambio_spesa");
			if((spesa != null) && (spesa.isRimborsoKm()))
				bp.getSpesaController().writeFormInput( out, "default", "cambio_spesa", true, null, null);
			else
				bp.getSpesaController().writeFormInput( out, "default", "cambio_spesa", bp.getSpesaController().isCambioReadOnly(), null, null); 				
		%>					
	</td>	
	</tr>

	<tr></tr>
	<tr></tr>
</table>
</div>

<%	if((spesa != null) && (spesa.isTrasporto()))
	{%>
		<div class="Group"><table>
		<tr>
		<td>	
			<%	bp.getSpesaController().writeFormLabel( out, "im_base_maggiorazione");%>
		</td>
		<td>	
			<%	bp.getSpesaController().writeFormInput( out, "default", "im_base_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);
				bp.getSpesaController().writeFormLabel( out, "percentuale_maggiorazione");
				bp.getSpesaController().writeFormInput( out, "default", "percentuale_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);
			%>
		</td>
		</tr>
		
		<tr>
		<td>
			<%	bp.getSpesaController().writeFormLabel( out, "im_maggiorazione");%>
		</td>
		<td>	
			<%	bp.getSpesaController().writeFormInput( out, "default", "im_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);%>
		</td>
		</tr></table></div>
<%	}
	if((spesa != null) && (spesa.isPasto()))
	{%>
		<div class="Group"><table><tr><td>
		<% 			
			bp.getSpesaController().writeFormLabel( out, "cd_ti_pasto");
			bp.getSpesaController().writeFormInput( out, "default", "cd_ti_pasto", !bp.getSpesaController().isEditingSpesa(), null, null);			
			bp.getSpesaController().writeFormInput( out, "default", "find_tipo_pasto", !bp.getSpesaController().isEditingSpesa(), null, null);			
		%>			
		</td></tr></table></div>				
<%	}
	if((spesa != null) && (spesa.isRimborsoKm()))
	{%>
		<div class="Group"><table>
		<tr><td>
		<% 					
			bp.getSpesaController().writeFormLabel( out, "ti_auto");
			bp.getSpesaController().writeFormInput( out, "default", "ti_auto", !bp.getSpesaController().isEditingSpesa(), null, null);						
			bp.getSpesaController().writeFormInput( out, "default", "find_tipo_auto", !bp.getSpesaController().isEditingSpesa(), null, null);
		%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			bp.getSpesaController().writeFormLabel( out, "chilometri");
			bp.getSpesaController().writeFormInput( out, "default", "chilometri", !bp.getSpesaController().isEditingSpesa(), null, null);
		%>	
		&nbsp;&nbsp;&nbsp;
		<%
			bp.getSpesaController().writeFormLabel( out, "indennita_chilometrica");
			bp.getSpesaController().writeFormInput( out, "default", "indennita_chilometrica", !bp.getSpesaController().isEditingSpesa(), null, null);				
		%>						
		</td></tr></table></div>				
<%	}	%>

<table width="100%">
	<tr></tr>
	<tr></tr>
	
	<tr>
	<td ALIGN="CENTER">
			<% JSPUtils.button(out,bp.encodePath("img/edit24.gif"),bp.encodePath("img/edit24.gif"), "Modifica","javascript:submitForm('doEditaSpesa')", bp.isEditSpesaButtonEnabled(), bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,bp.encodePath("img/save24.gif"),bp.encodePath("img/save24.gif"), "Conferma","javascript:submitForm('doConfermaSpesa')", bp.isConfermaSpesaButtonEnabled(), bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,bp.encodePath("img/undo24.gif"),bp.encodePath("img/undo24.gif"), "Annulla","javascript:submitForm('doUndoSpesa')", bp.isUndoSpesaButtonEnabled(), bp.getParentRoot().isBootstrap()); %>				
	</td>
	</tr>
	
	<tr></tr>
	<tr></tr>	
</table>

</div>