<%@ page
	session="false"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">

<html>
	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="javascript" src="scripts/css.js"></script>
		<title>Login</title>
	</head>

	<body class="Workspace">

	<%	
	    it.cnr.contab.utenze00.bp.LoginBP bp = null;
	    try{
	       bp = (it.cnr.contab.utenze00.bp.LoginBP)BusinessProcess.getBusinessProcessRoot(request);
	    }catch (NullPointerException e){
	       bp = (it.cnr.contab.utenze00.bp.LoginBP)new HttpActionContext(this,request,response).getBusinessProcessRoot(true);
	    }   
	    if (bp!=null)
			bp.openForm(pageContext); 
	    else
	    {
	    	%>
			<center>
				<font color="#CC0000" size="5px" face="Arial, Helvetica, sans-serif">Impossibile procedere.<BR>Consultare il <a href="http://contab.cnr.it/manuali/000%20-%2001%20requisiti%20browser.doc">Manuale della Procedura di Contabilità</a> e verificare le Impostazioni del Browser.</font>
			</center>
		<%
			return;
			}
	    %>

	<table align="center" class="Window">
		<tr>
			<td><img src="img/question.gif"></td>
		  	<td>Inserire l'utente e la password per accedere all'applicazione</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr><% bp.getController().writeFormField(out,"userid"); %></tr>
					<tr><% bp.getController().writeFormField(out,"password"); %></tr>
					<tr><td>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr>
						<td style="width:50%"><input style="width:100%" type=submit value="Entra" name="comando.doEntra"></td>
						<td style="width:50%"><input style="width:100%" type=submit value="Cambia password" name="comando.doCambiaPassword"></td>
					</tr>
				</table>
			</td>
		</tr>
  	</table>

	<%	bp.closeForm(pageContext); %>

	</body>

</html>