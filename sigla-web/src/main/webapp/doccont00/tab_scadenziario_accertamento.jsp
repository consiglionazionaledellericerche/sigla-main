<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>


<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	AccertamentoBulk accertamento = (AccertamentoBulk)bp.getModel();
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)bp.getScadenzario().getModel();
%>

<div class="Group card">
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td class="w-25"><% bp.getController().writeFormLabel( out, "cd_voceRO"); %></td>
			<td><% bp.getController().writeFormInput( out, "cd_voceRO"); %></td>
			<td class="w-100"><% bp.getController().writeFormInput( out, "ds_voce"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_accertamento"); %></td>
			<td colspan="2">
	  		<% if (bp.isROImporto()){
					bp.getController().writeFormInput( out, "default", "im_accertamento_ro", false, null, null);
		  	   } else {
					bp.getController().writeFormInput( out, "default", "im_accertamento", true, null, null);
		  	   }
			%>
			</td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_parz_scadenze"); %></td>
			<td colspan="2"><% bp.getController().writeFormInput( out, "im_parz_scadenze"); %></td>
		</tr>
		<tr>
			<td colspan="3">
			     <%	bp.getScadenzario().setEnabled( !bp.isEditingScadenza());
			      	if((bp.isEditable()) && (accertamento.getDt_cancellazione() == null))
			      	{
			      		bp.getScadenzario().writeHTMLTable(pageContext,"accertamento",true,false,true,"100%","auto;max-height:40vh");
			      	}
			      	else
			      	{
				      	// Visualizzazione accertamento
			      		bp.getScadenzario().writeHTMLTable(pageContext,"accertamento",false,false,false,"100%","auto;max-height:40vh");
				    }  		
		      	%>
			</td>
		</tr>
	
	 	<tr>
		 	<td colspan=3>
	 	 	<br>
		      <%JSPUtils.tabbed(pageContext,
								"tabScadenzario",
								new String[][] {
									{ "tabScadenza","Scadenza","/doccont00/tab_scadenza_accertamento.jsp" },
									{ "tabDettaglioScadenza","Dettaglio Scadenza","/doccont00/tab_dettaglio_scadenza_accertamento.jsp" } },
								bp.getTab("tabScadenzario"),
								"center", 
								"100%", null,
								!bp.isEditingScadenza() );
				
			%>
			</td>
		</tr>
	</table>
</div>

	