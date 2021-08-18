<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.contratto.bulk.*,
		it.cnr.contab.config00.bp.*"
%>

<% 
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);	
%>
<%!     static String[][] tabs = null;
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);
   ContrattoBulk contratto = (ContrattoBulk)bp.getModel();	                
   if(contratto.isPassivo() &&
		   ((contratto.getFl_pubblica_contratto()!=null && contratto.getFl_pubblica_contratto()) ||  
		   (contratto.isProvvisorio() && contratto.getTipo_contratto()!=null && contratto.getTipo_contratto().getFl_pubblica_contratto()!= null && contratto.getTipo_contratto().getFl_pubblica_contratto()))){
	   
		if (contratto.isFromFlussoAcquisti()){
			   tabs = new String[][] {
	               { "tabTestata","Contratto","/config00/tab_contratti_testata.jsp" },
	               { "tabCessazione","Dati di cessazione dell'efficacia","/config00/tab_contratti_cessazione.jsp" },
	               { "tabAss_contratto_uo","CdR","/config00/tab_ass_contratto_uo.jsp" },
	               { "tabAllegati","Allegati","/config00/tab_contratti_allegati.jsp" },
	               { "tabAllegatiFlusso","Allegati Flusso","/config00/tab_contratti_allegati_flusso.jsp" },
	               { "tabAss_contratto_ditte","Ditte Invitate","/config00/tab_ass_contratto_ditte.jsp" },
	               };   
			
		} else {
			   tabs = new String[][] {
	               { "tabTestata","Contratto","/config00/tab_contratti_testata.jsp" },
	               { "tabCessazione","Dati di cessazione dell'efficacia","/config00/tab_contratti_cessazione.jsp" },
	               { "tabAss_contratto_uo","CdR","/config00/tab_ass_contratto_uo.jsp" },
	               { "tabAllegati","Allegati","/config00/tab_contratti_allegati.jsp" },
	               { "tabAss_contratto_ditte","Ditte Invitate","/config00/tab_ass_contratto_ditte.jsp" },
	               };   
		}
   } else {
		if (contratto.isFromFlussoAcquisti()){
			   tabs = new String[][] {
		           { "tabTestata","Contratto","/config00/tab_contratti_testata.jsp" },
		           { "tabCessazione","Dati di cessazione dell'efficacia","/config00/tab_contratti_cessazione.jsp" },
		           { "tabAss_contratto_uo","CdR","/config00/tab_ass_contratto_uo.jsp" },
		           { "tabAllegati","Allegati","/config00/tab_contratti_allegati.jsp" },
	               { "tabAllegatiFlusso","Allegati Flusso","/config00/tab_contratti_allegati_flusso.jsp" },
		           };
			
		} else {
			   tabs = new String[][] {
		           { "tabTestata","Contratto","/config00/tab_contratti_testata.jsp" },
		           { "tabCessazione","Dati di cessazione dell'efficacia","/config00/tab_contratti_cessazione.jsp" },
		           { "tabAss_contratto_uo","CdR","/config00/tab_ass_contratto_uo.jsp" },
		           { "tabAllegati","Allegati","/config00/tab_contratti_allegati.jsp" },
		           };
		}
	   
   }
%>
<table class="Panel" width="100%">
	<tr>
		<td width="100%">
		   <div class="card p-2">
			<table align="left" cellspacing=4 cellpadding=4>
			  <tr>
		         <% bp.getController().writeFormField(out,"esercizio");%>
		         <% bp.getController().writeFormField(out,"pg_contratto");%>
		      </tr>  
		   	</table>
		   	</div>
		</td>
	</tr>
	<tr>
		<td>
		   <% JSPUtils.tabbed(
		                   pageContext,
		                   "tab",
		                   tabs,
		                   bp.getTab("tab"),
		                   "center",
		                   "100%",
		                   "100%" ); %>
		</td>
	</tr>
</table>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>