<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>


<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	AccertamentoBulk accertamento = (AccertamentoBulk)bp.getModel();
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)bp.getScadenzario().getModel();
%>

	<div class="Group card">
	<table class="w-100">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "cd_voceRO"); %></td>
			<td><% bp.getController().writeFormInput( out, "cd_voceRO"); %>
				<% bp.getController().writeFormInput( out, "ds_voce"); %></td>				 
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_accertamento"); %></td>
  		<% if (bp.isROImporto()){ %>
			<td><% bp.getController().writeFormInput( out, "default", "im_accertamento_ro", false, null, null); %></td>
	  	<%} else {%>
			<td><% bp.getController().writeFormInput( out, "default", "im_accertamento", true, null, null); %></td>
	  	<%}%>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_parz_scadenze"); %></td>
			<td><% bp.getController().writeFormInput( out, "im_parz_scadenze"); %></td>
		</tr>
	</table>
	</div>

	<table>
	<tr>
		<td colspan=2>
		     <%	bp.getScadenzario().setEnabled( !bp.isEditingScadenza());
		      	if((bp.isEditable()) && (accertamento.getDt_cancellazione() == null))
		      	{
		      		bp.getScadenzario().writeHTMLTable(pageContext,"accertamento",true,false,true,"100%","100px");
		      	}
		      	else
		      	{
			      	// Visualizzazione accertamento
		      		bp.getScadenzario().writeHTMLTable(pageContext,"accertamento",false,false,false,"100%","100px");
			    }  		
	      	%>
		</td>
	</tr>
	</table>

	
 	<tr>
 	<td colspan=2>
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
	</td></tr>