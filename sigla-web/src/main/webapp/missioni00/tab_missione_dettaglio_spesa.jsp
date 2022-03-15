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
<div>
	<div class="Group card p-2" style="width:100%">
		<table width="100%">
			<tr>
				<td><% bp.getSpesaController().writeFormLabel( out, "dt_inizio_tappa"); %></td>
			</tr>
			<tr>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "dt_inizio_tappa", !bp.getSpesaController().isEditingSpesa()||spesa.getSpesaIniziale()!=null, null,null);%></td>
				<td>
						<table width="100%">
							<tr>
								<td><% bp.getSpesaController().writeFormLabel( out, "cd_ti_spesa"); %></td>
								<td><% bp.getSpesaController().writeFormInput( out, "default", "find_tipo_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
							</tr>
							<tr>
								<td><% bp.getSpesaController().writeFormLabel( out, "ds_spesa"); %></td>
								<td><% bp.getSpesaController().writeFormInput( out, "default", "ds_spesa", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
							</tr>
						</table>
				</td>	
			</tr>
		</table>
	</div>
	<div class="Group card p-2" style="width:100%">
		<table class="w-100">
			<tr>
				<td colspan="2">
					<% bp.getSpesaController().writeFormLabel( out, "fl_spesa_anticipata"); %>		
					<% bp.getSpesaController().writeFormInput( out, "default", "fl_spesa_anticipata", !bp.getSpesaController().isEditingSpesa(), null, null); %>					
				</td>
			</tr>
			<tr>	
				<td class="w-10"><% bp.getSpesaController().writeFormLabel( out, "localita_spostamento"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "localita_spostamento", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
			</tr>
			<tr>
				<td><% bp.getSpesaController().writeFormLabel( out, "id_giustificativo"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "id_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
			</tr>
			<tr>	
				<td><% 
				if((spesa != null) && (!spesa.isDettaglioMissioneFromGemis())){
					bp.getSpesaController().writeFormLabel( out, "ds_giustificativo");		
				} %></td>
				<td><% 	if((spesa != null) && (!spesa.isDettaglioMissioneFromGemis())){
					bp.getSpesaController().writeFormInput( out, "default", "ds_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null);
				} %></td>
			</tr>
			<tr>
				<td><% bp.getSpesaController().writeFormLabel( out, "ds_no_giustificativo"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "ds_no_giustificativo", !bp.getSpesaController().isEditingSpesa(), null, null); %></td>
			</tr>
		</table>
	</div>
	<div class="Group card p-2" style="width:100%">
		<table width="100%">
			<tr>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "im_spesa_divisa");%></td>
				<td>
				    <% bp.getSpesaController().writeFormInput( out,
				        "default",
				        "im_spesa_divisa",
				        (spesa != null && spesa.isRimborsoKm()) ? true : !bp.getSpesaController().isEditingSpesa(),
				        null, null);%>
				</td>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "cd_divisa_spesa"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out,
					        "default",
					        "find_divisa_spesa",
					        (spesa != null && spesa.isRimborsoKm()) ? true : !bp.getSpesaController().isEditingSpesa(),
					        null, null);%>
				</td>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "cambio_spesa"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out,
				            "default",
				            "cambio_spesa",
				            (spesa != null && spesa.isRimborsoKm()) ? true : bp.getSpesaController().isCambioReadOnly(),
				            null, null);%>
				</td>
			</tr>
		</table>
	</div>
	<%	if((spesa != null) && (spesa.isTrasporto())) {%>
		<div class="Group card p-2">
			<table width="100%">
				<tr>
					<td align="right"><% bp.getSpesaController().writeFormLabel( out, "im_base_maggiorazione");%></td>
					<td><% bp.getSpesaController().writeFormInput( out, "default", "im_base_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
					<td align="right"><% bp.getSpesaController().writeFormLabel( out, "percentuale_maggiorazione");%></td>
					<td><% bp.getSpesaController().writeFormInput( out, "default", "percentuale_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
					<td align="right"><% bp.getSpesaController().writeFormLabel( out, "im_maggiorazione");%></td>
					<td><% bp.getSpesaController().writeFormInput( out, "default", "im_maggiorazione", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
				</tr>
			</table>
		</div>
<%	}
	if((spesa != null) && (spesa.isPasto()))
	{%>
		<div class="Group card p-2">
		<table>
            <tr>
            	<td><% bp.getSpesaController().writeFormLabel( out, "default", "find_tipo_pasto");%></td>
            	<td><% bp.getSpesaController().writeFormInput( out, "default", "find_tipo_pasto", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
            </tr>
		</table>
		</div>
<%	}
	if((spesa != null) && (spesa.isRimborsoKm()))
	{%>
	<div class="Group card p-2">
		<table width="100%">
			<tr>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "ti_auto"); %> </td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "ti_auto", !bp.getSpesaController().isEditingSpesa(), null, null);
					   bp.getSpesaController().writeFormInput( out, "default", "find_tipo_auto", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "chilometri");%></td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "chilometri", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
				<td align="right"><% bp.getSpesaController().writeFormLabel( out, "indennita_chilometrica"); %></td>
				<td><% bp.getSpesaController().writeFormInput( out, "default", "indennita_chilometrica", !bp.getSpesaController().isEditingSpesa(), null, null);%></td>
			</tr>
		</table>
	</div>
<%	}	%>
    <div class="card w-100">
        <table width="100%">
            <tr>
                <td ALIGN="CENTER">
                        <div class="btn-group m-2" role="group">
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
                                "Modifica",
                                "javascript:submitForm('doEditaSpesa')",
                                "btn-outline-secondary btn-title",
                                bp.isEditSpesaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
                                "Conferma",
                                "javascript:submitForm('doConfermaSpesa')",
                                "btn-outline-secondary btn-title",
                                bp.isConfermaSpesaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
                                "Annulla",
                                "javascript:submitForm('doUndoSpesa')",
                                "btn-outline-secondary btn-title",
                                bp.isUndoSpesaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>