<%@page import="it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.util00.bp.*,
		it.cnr.contab.bollo00.bulk.*,
		it.cnr.contab.bollo00.tabrif.bulk.*"
%>

<%
	AllegatiCRUDBP bp = (AllegatiCRUDBP)BusinessProcess.getBusinessProcess(request);
	Atto_bolloBulk model = (Atto_bolloBulk)bp.getModel(); 
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function doScaricaFile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<title>Documenti da assoggettare a Bollo Virtuale</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<div class="card p-2">
	<table class="Panel w-100">
		<TR>
			<% bp.getController().writeFormField(out,"esercizio");%>
			<% bp.getController().writeFormField(out,"id");%>
		</TR>
		<TR><% bp.getController().writeFormField(out,"findUnitaOrganizzativa");%></TR>
		<TR><% bp.getController().writeFormField(out,"descrizioneAtto");%></TR>

	    <tr>
	    	<td colspan=4>
		      <div class="card">
			  <fieldset>
			  <legend class="GroupLabel h3 text-primary">Provvedimento di registrazione atto</legend>
		      <table width="100%">
				  <tr>
				    <td><% bp.getController().writeFormField(out,"cd_provv");%></td>
				    <td><% bp.getController().writeFormField(out,"nr_provv");%></td>
				    <td><% bp.getController().writeFormField(out,"dt_provv");%></td>
			      </tr>
		      </table>
		      </fieldset>
		      </div>
	      	</td>
		</tr>
		
	    <tr>
	    	<td colspan=4>
		      <div class="card">
			  <fieldset>
			  <legend class="GroupLabel h3 text-primary">Tipologia atto</legend>
		      <table width="100%">
				<tr><% bp.getController().writeFormField(out,"findTipoAttoBollo"); %></tr>
				<% if (model!=null && model.getTipoAttoBollo()!=null) {
					if (model.getTipoAttoBollo().isCalcoloPerFoglio()) { %>  
						<tr><% bp.getController().writeFormField(out,"numFogli"); %></tr>
						<% if ((model.getNumRighe()!=null && model.getNumRighe()>0) || 
							   (model.getNumDettagli()==null || model.getNumDettagli()==0)) { %>	
						<tr><% bp.getController().writeFormField(out,"numRighe"); %></tr>
						<% } %>
					<% } else if (model.getTipoAttoBollo().isCalcoloPerEsemplare()) { %>  
						<tr><% bp.getController().writeFormField(out,"numEsemplari"); %></tr>
					<% } %>
				<% } %>		
		      </table>
		      </fieldset>
		      </div>
	      	</td>
		</tr>
	</table>
</div>
<div class="Group m-1">
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,bp.getAllegatiFormName(),true,false,true,"100%","150px"); %>  
</div> 
<div class="Group card mt-3">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 
<%	bp.closeFormWindow(pageContext); %>
</body>