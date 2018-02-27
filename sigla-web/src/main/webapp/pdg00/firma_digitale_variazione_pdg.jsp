<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.reports.bp.*,
			it.cnr.contab.pdg00.bp.FirmaDigitalePdgVariazioniBP"
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

<% FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<table class="Panel" width="100%" height="100%">
		<tr>
			<td>
				<% bp.writeFormLabel(out,"ti_signed"); %>
			</td>
			<td align="left">  	
				<% bp.writeFormInput(out,null,"ti_signed",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
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
		<% if (bp.isUploadFile() && bp.isInviaButtonEnabled()) {%>
		<tr>
			<td colspan="1"><span class="FormLabel">Upload file firmato e invio al protocollo informatico</span></td>
			<td><input type=file name="main.file_to_upload" class=null style="background: #F5F5DC" maxLength=400 size=80 onclick="cancelBubble(event)"></td>
			<td><% JSPUtils.button(out,bp.encodePath("img/import24.gif"),bp.encodePath("img/import24.gif"), "Invia File","javascript:submitForm('doInvia')", true, bp.getParentRoot().isBootstrap()); %></td>
		</tr>	
		<% } %>
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
	<% } %>
	<%-- ATTENZIONE - non mettere altri javascript dopo la funzione "onload" altrimenti questa non funziona --%>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>