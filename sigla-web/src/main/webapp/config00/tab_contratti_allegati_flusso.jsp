<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>
<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudArchivioAllegatiFlusso();
	AllegatoContrattoFlussoDocumentBulk bulk = (AllegatoContrattoFlussoDocumentBulk)controller.getModel();
%>
<script language="JavaScript">
function doScaricaAllegato() {	
	  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegatoFlusso&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
	<table width=100%>
	  <tr>
	  	<td>
	  		<%controller.writeHTMLTable(pageContext,"archivioAllegati",!bp.isSearching(),false,!bp.isSearching(),"90%","100px");%>
	  	</td>
	  </tr>
	  <tr>
	  	<td>
			<table class="Panel" cellspacing=2>
					<tr>
				        <td><% controller.writeFormLabel(out,"default","type"); %></td>
				        <td colspan="2"><% controller.writeFormInput(out,"default","type", !bp.isAllegatiEnabled(),null,null); %></td>
					</tr>
				<% if (bp.isAllegatiEnabled()){%>
					<tr>
				        <td><% controller.writeFormLabel(out,"default","file"); %></td>
				        <td colspan="2"><% controller.writeFormInput(out,"default","file", !bp.isAllegatiEnabled(),null,null); %></td>
				    </tr>
			    <%}%>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","nome"); %></td>
			        <td><% controller.writeFormInput(out,"default","nome", true, null,null); %></td>
			        <% if (bulk != null && bulk.isContentStreamPresent()){%>
			        	<td align="left"><% controller.writeFormInput(out,"default","attivaFile");%></td>
			        <%}%>
			    </tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","descrizione"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","descrizione", true,null,null); %></td>
				</tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","titolo"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","titolo", true,null,null); %></td>
				</tr>
			</table>  	
	  	</td>
	  </tr>
	</table>
