<%@page import="it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.util00.bp.*,
		it.cnr.contab.bollo00.bp.*,
		it.cnr.contab.bollo00.bulk.*,
		it.cnr.contab.bollo00.tabrif.bulk.*"
%>

<%
	CRUDAttoBolloBP bp = (CRUDAttoBolloBP)BusinessProcess.getBusinessProcess(request);
	Atto_bolloBulk model = (Atto_bolloBulk)bp.getModel(); 
	String collapseIconClass = bp.isAllegatiCollapse() ? "fa-chevron-circle-down" : "fa-chevron-circle-up";
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
<div class="Group card p-3 m-1">
	<table class="Panel w-100">
		<TR>
			<% bp.getController().writeFormField(out,"esercizio");%>
			<% bp.getController().writeFormField(out,"id");%>
		</TR>
		<TR>
			<td><% bp.getController().writeFormLabel(out,"findUnitaOrganizzativa");%></td>
			<td colspan="3"><% bp.getController().writeFormInput(out,"findUnitaOrganizzativa");%></td>
		</TR>
		<TR>
			<td><% bp.getController().writeFormLabel(out,"descrizioneAtto");%></td>
			<td colspan="3"><% bp.getController().writeFormInput(out,"descrizioneAtto");%></td>
		</TR>
	</table>
</div>

<div class="Group card p-3 m-1">
	<div class="GroupLabel font-weight-bold text-primary ml-2">Provvedimento di registrazione atto</div>  
	<div class="Group card p-3 m-1 w-100">
		<table width="100%">
			<tr>
				<% bp.getController().writeFormField(out,"cd_provv");%>
				<% bp.getController().writeFormField(out,"nr_provv");%>
				<% bp.getController().writeFormField(out,"dt_provv");%>
			</tr>
		</table>
	</div>

	<div class="GroupLabel font-weight-bold text-primary ml-2">Repertorio</div>  
	<div class="Group card p-3 m-1 w-100">
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"flContrattoRegistrato");%>
					<% bp.getController().writeFormInput(out,"flContrattoRegistrato");%>
				</td>
			</tr>
		</table>
		<% if (model!=null && model.isFlContrattoRegistrato()) { %>
		<table width="100%">
			<tr>
			    <% bp.getController().writeFormField( out, "find_contratto"); %>
			</tr>
		</table>
		<% } %>
	</div>
</div>		

<div class="Group card p-3 m-1">
	<div class="GroupLabel font-weight-bold text-primary ml-2">Tipologia atto</div>  
	<div class="Group card p-3 m-1 w-100">
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
	</div>
</div>

<div class="Group card m-1">
	<%if (bp.getParentRoot().isBootstrap()) { %>  
	<div class="card-header">
	    <h5 class="mb-0">
	        <a onclick="submitForm('doToggle(allegati)')" class="text-primary"><i aria-hidden="true" class="fa <%=collapseIconClass%>"></i> Allegati</a>
	    </h5>
	</div>
	<% } %>
	<div class="card-block">
	    <% if (!bp.isAllegatiCollapse() || !bp.getParentRoot().isBootstrap()) { %>
			<div class="Group">
			<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,bp.getAllegatiFormName(),true,false,true,"100%","150px"); %>  
			</div> 
			<div class="Group card mt-1">
			  <table>
			  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
			  </table>
			</div>
		<% } %>
	</div> 
</div>	
<%	bp.closeFormWindow(pageContext); %>
</body>