<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "login_serv.jsp"
 ?ResourceTimestamp "13/12/00 19.45.49"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Login</title>
<body class="Form">
<% 	FormBP bp = (FormBP)BusinessProcess.getBusinessProcess(request);
	String msg = bp.getAndClearMessage();
	String src = "img/question.gif";
	if (msg != null)
		switch(bp.getMessageStatus()) {
			case FormBP.WARNING_MESSAGE 	:	src = "img/warning.gif";
																	break;
			case FormBP.ERROR_MESSAGE 		: src = "img/error.gif";
																	break;
			case FormBP.QUESTION_MESSAGE	: 
			default										: src = "img/question.gif";
																	break;
		}
	 bp.openFormWindow(pageContext);
	 it.cnr.contab.utenze00.bulk.CNRUserInfo ui = (it.cnr.contab.utenze00.bulk.CNRUserInfo)it.cnr.jada.action.HttpActionContext.getUserInfo(request); %>

	<table class="Panel">
			  <tr>
					<td rowspan="2"><img src="<%= src%>"></td>
			  	<td><%= msg == null ? "" : msg%></td>
			  </tr>
			  <tr>
			  	<td>Inserire l'utente e la password per accedere all'applicazione</td>
			  </tr>
			</table>
			<table class="Panel" width="100%">
			  <tr>
					<td><span class="FormLabel">Utente</span></td>
					<td><input type=text readonly name=userid class=null value="<%= ui.getUserid()%>" size=10></td>
	  		</tr>
			  <tr>
					<TD><span class="FormLabel">Password</span></TD>
					<TD><input type=password readonly name=password class=null value="<%= ui.getPassword()%>" size=10></TD>
	  		</TR>
			  <tr>
					<TD><span class="FormLabel">Descrizione sessione</span></TD>
					<TD><input type=text name="descrizioneSessione" class=null value="<%= request.getSession().getId()%>" size=30></TD>
	  		</TR>
			  <TR><td>&nbsp;</td></TR>
			  <TR>
					<TD colspan="2">
						<table class="Panel" width="100%">
							<tr>
								<td style="width:50%"><input style="width:100%" type=submit value="Entra" name="comando.doEntra"></td>
								<td style="width:50%"><input style="width:100%" type=submit value="Cambia password" name="comando.doCambiaPassword"></td>
							</tr>
						</table>
					</td>
			  </TR>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>