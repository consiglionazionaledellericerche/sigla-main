<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<title>Contabilità CNR</title>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<script language="javascript" src="scripts/css.js"></script>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script>
		if (window.name != 'desktop' && window.top.frames['desktop'])
			window.top.frames['desktop'].location="utenze00/desktop.jsp"
		</script>
	</head>
	<%
		int rows = 130;
		try {
			GestioneUtenteBP utenteBP = (GestioneUtenteBP)BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP"); 
			if (utenteBP.getUserInfo().getUtente().isUtenteComune()) {
				if (utenteBP.getUserInfo().getUtente().getDipartimento() != null && utenteBP.getUserInfo().getUtente().getDipartimento().getCd_dipartimento()!= null)
					rows = 160;
			}else{
				rows = 78;
			}
			if (utenteBP.getUserInfo().getLdap_userid()!=null && !utenteBP.getUserInfo().getLdap_userid().equals(utenteBP.getUserInfo().getUserid()))
				rows = rows+30;
		}catch(Exception e){
			
		}
	%>
	
	<frameset bordercolor="#89aed4" framespacing="2" border="3" id="desktopFrameSet" cols="237,*">
		<frameset rows="<%=rows%>,*">
			<frame frameborder="0" name="menu_header" src="utenze00/menu_title.jsp" marginwidth="4" marginheight="0" scrolling="NO" class="frameColBorder">
			<frame name="menu_tree" src="utenze00/menu_tree.jsp" marginwidth="4" marginheight="4" class="frameColBorder">
		</frameset>
<%	try {
		BusinessProcess bp = BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP/SelezionaCdsBP"); %>
		<frame name="workspace" bordercolor="#89aed4" src="<%= BusinessProcess.encodeUrl(request,bp,"utenze00/form_cds.jsp")%>">
<%	} catch(NoSuchBusinessProcessException ex) { %>
	<%	try {
			BusinessProcess bp = BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP/SelezionatoreUnitaOrganizzativa"); %>
			<frame name="workspace" bordercolor="#89aed4" src="<%= BusinessProcess.encodeUrl(request,bp,"utenze00/lista_unita_organizzative.jsp")%>">
	<%	} catch(NoSuchBusinessProcessException e) { 
			BusinessProcess gestioneUtenteBP = BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP");
		%>
			<frame name="workspace" src="<%= BusinessProcess.encodeUrl(request,gestioneUtenteBP,"util/workspace.jsp")%>" bordercolor="#89aed4">
	<%	} %>
<%	} %>
	</frameset>
	<noframes>
	    <body>
	        Il browser utilizzato non supporta i frames e non è quindi possibile usare l'applicazione
	    </body>
	</noframes>
		
</html>