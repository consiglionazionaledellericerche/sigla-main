<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "login_serv.jsp"
 ?ResourceTimestamp "13/12/00 19.45.49"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" archive="scripts.jar" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Informazione</title>
<body class="Workspace">
<FORM name="mainForm" action="Option.do" method=post onSubmit="return disableDblClick()">
<input type=hidden name="comando">
<table class="Window card" cellspacing="0" cellpadding="2" align="center" width="280">
<% 	OptionBP bp = (OptionBP)BusinessProcess.getBusinessProcess(request);
	 	BusinessProcess.encode(bp,pageContext);
		HttpActionContext.encodeActionCounter(pageContext);
		String msg = bp.getAndClearMessage();
		String src = "img/question.gif";
		String iconClass = "fa fa-question-circle fa-fw fa-2x text-info";
		if (msg != null)
			switch(bp.getMessageStatus()) {
				case OptionBP.WARNING_MESSAGE:	
					src = "img/warning.gif";
					iconClass = "fa fa-exclamation-triangle fa-fw fa-2x text-warning";
					break;
				case OptionBP.ERROR_MESSAGE: 
					iconClass = "fa  fa-exclamation-circle fa-fw fa-2x text-danger";
					src = "img/error.gif";
					break;
				case OptionBP.QUESTION_MESSAGE: 
				default: 
					src = "img/question.gif";
				break;
			} %>
		<tr>
			<td class="FormTitle card-header h5">
				<%if (bp.getParentRoot().isBootstrap()) { %>
					<i class="<%=iconClass%>" aria-hidden="true"></i>
				<%}%>
				Informazione
			</td>
		</tr>
		<tr>
			<td>
				<table class="Panel" width="100%">
				  <tr>
						<td valign="center">
							<%if (!bp.getParentRoot().isBootstrap()) { %>
								<img src="<%=src%>">
							<%}%>							
						</td>
				  		<td valign="center" width="100%" <%=bp.isNowrap()? "NOWRAP":"" %>><%= msg%></td>
				  </tr>
				</table>
			</td>
		</tr>
	<tr>
		<td align=center>		
			<hr>
			<table class="Panel" <%=bp.isNowrap()? "":"width=\"100%\"" %>>
				<tr>
					<% if (bp.hasButton(bp.YES_BUTTON)) { %>
						<td><input type="button"  class="btn btn-outline-primary" name="comando.doYes" value="Si" style="<%=bp.isNowrap()? "width:150px":"width:100%" %>" onclick="submitForm('doYes')"></td>
					<% } 
						 if (bp.hasButton(bp.NO_BUTTON)) { %>
						<td><input type="button" class="btn btn-outline-primary" name="comando.doNo" value="No" style="<%=bp.isNowrap()? "width:150px":"width:100%" %>" onclick="submitForm('doNo')"></td>
					<% } 
						 if (bp.hasButton(bp.OK_BUTTON)) { %>
						<td><input type="button" class="btn btn-outline-primary" name="comando.doOk" value="Ok" style="<%=bp.isNowrap()? "width:150px":"width:100%" %>" onclick="submitForm('doOk')"></td>
					<% } 
						 if (bp.hasButton(bp.CANCEL_BUTTON)) { %>
						<td><input type="button" class="btn btn-outline-primary" name="comando.doCancel" value="Annulla" style="<%=bp.isNowrap()? "width:150px":"width:100%" %>" onclick="submitForm('doCancel')"></td>
					<% } 
						 if (bp.hasButton(bp.CLOSE_BUTTON)) { %>
						<td><input type="button" class="btn btn-outline-primary" name="comando.doClose" value="Chiudi" style="<%=bp.isNowrap()? "width:150px":"width:100%" %>" onclick="submitForm('doClose')"></td>
					<% } %>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>  
</body>
</html>