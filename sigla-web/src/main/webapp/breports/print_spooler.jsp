<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.reports.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="javascript" src="scripts/Ellips-sign.js"></script>
<script language="javascript" src="scripts/applet_sign_actalis.js"></script>
</head>

<body class="Form">

<% PrintSpoolerBP bp = (PrintSpoolerBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<table class="Panel" width="100%" height="70%">
		<tr>
			<td>
				<% bp.writeFormLabel(out,"ti_visibilita"); %>
			</td>
			<td>  	
				<% bp.writeFormInput(out,null,"ti_visibilita",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
			</td>
			<td>
				<% if (bp.isSignEnabled()) {%>
					<span class="FormLabel">Firma digitale</span>
				<% } %>
			</td>
			<td>
				<% if (bp.isSignEnabled()) {%>
				    <script type="text/javascript" language="JavaScript">
				  		loadApplet();
				    </script>
				<% } %>
			</td>
		</tr>
		<tr height="100%">
			<td colspan="4">
				<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
			</td> 
		</tr>
		<tr>
			<td colspan="4">
				<% bp.writeHTMLNavigator(out); %>
			</td>
		</tr>
	</table>
	<% if (bp.isSignEnabled() && bp.isSignFile()) {%>
		<% bp.setSignFile(false);
		   request.getSession().setAttribute("sign_pg_stampa", bp.getPgStampa()); %>
	    <script type="text/javascript" language="JavaScript">
		    var oldonload = window.onload;
		    window.onload = function() {
			    if (typeof oldonload != 'function') {
			        oldonload();
			    }
		    	downloadFile="<%=bp.getDownloadFile(pageContext)%>";
				uploadFile="<%=bp.getUploadFile(pageContext)%>";
				bpPath="<%=bp.getPath()%>";
				fileName="<%=bp.getFileName()%>";
		    	signIterative(downloadFile, uploadFile, bpPath, fileName);
		    }
	    </script>
	<%-- ATTENZIONE - non mettere altri javascript dopo la funzione "onload" altrimenti questa non funziona --%>
	<% } %>
	<% if (bp.isEMailEnabled()){ %>
		<div class="GroupLabel">E-Mail</div>          
		<table class="Group" >
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_a");%> </td>
				<td><% bp.getController().writeFormInput(out,null,"email_a",true,null,null);%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_cc");%></td>
				<td><% bp.getController().writeFormInput(out,null,"email_cc",true,null,null);%></td>
			</tr>	
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_ccn");%></td>
				<td><% bp.getController().writeFormInput(out,null,"email_ccn",true,null,null);%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_subject");%></td>
				<td><% bp.getController().writeFormInput(out,null,"email_subject",true,null,null);%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_body");%></td>
				<td><% bp.getController().writeFormInput(out,null,"email_body",true,null,null);%></td>
			</tr>
		</table>
			
	<%}%>
<%	bp.closeFormWindow(pageContext); %>
</body>

</html>