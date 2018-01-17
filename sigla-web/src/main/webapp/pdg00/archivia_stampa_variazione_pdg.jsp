<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<%	
	ArchiviaStampaPdgVariazioneBP bp = (ArchiviaStampaPdgVariazioneBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudArchivioAllegati();
%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
<script language="JavaScript">
function doScaricaFile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeFile()!=null?bp.getNomeFile().replace("'", "_"):""%>?methodName=scaricaFile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doScaricaAllegato() {	
	  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>
<div class="card p-2">
	<table width=100%>
	 <tr>
	  <td>
		<fieldset class="fieldset">
	    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"ti_signedText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>
			<table>
			  <tr>	
				<td><% bp.getController().writeFormLabel(out,"findpdg_variazioneForPrint"); %></td>
				<td><% bp.getController().writeFormInput(out,"findpdg_variazioneForPrint"); %></td>
				<td><% bp.getController().writeFormInput(out,"default","attivaFile");%></td>
			  </tr>  
			</table>
		</fieldset>
	   </td>
	  </tr>
	</table>
</div>	
<fieldset class="fieldset">
<legend class="GroupLabel"><span class="GroupLabel" style="background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;">Allegati</span></legend>
	<table width=100%>
	  <tr>
	  	<td>
	  		<%controller.writeHTMLTable(pageContext,"archivioAllegati",!bp.isSearching(),false,!bp.isSearching(),"90%","100px");%>
	  	</td>
	  </tr>
	  <tr>
	  	<td>
			<table class="Panel" cellspacing=2>
				<% if (!bp.isSigned()){%>
					<tr>
				        <td><% controller.writeFormLabel(out,"default","file"); %></td>
				        <td colspan="2"><% controller.writeFormInput(out,"default","file", bp.isSigned(),null,null); %></td>
				    </tr>
			    <%}%>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","nome"); %></td>
			        <td><% controller.writeFormInput(out,"default","nome"); %></td>
			        <td align="left"><% controller.writeFormInput(out,"default","attivaFile");%></td>
			    </tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","descrizione"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","descrizione", bp.isSigned(),null,null); %></td>
				</tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","titolo"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","titolo", bp.isSigned(),null,null); %></td>
				</tr>
			</table>  	
	  	</td>
	  </tr>
	</table>
</fieldset>	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>
