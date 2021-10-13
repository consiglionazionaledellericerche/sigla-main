<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.incarichi00.bp.*,
	it.cnr.contab.incarichi00.bulk.*,
    it.cnr.contab.docamm00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Anagrafica Dottorati</title>
</head>
<body class="Form"> 

<% SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
   bp.openFormWindow(pageContext); %>

<table width="100%" class="card p-2">
  <tr>
	<td><% bp.getController().writeFormLabel( out, "pgAnagraficadottoratiPos"); %></td>
	<td><% bp.getController().writeFormInput( out, "pgAnagraficadottoratiPos"); %></td>
  </tr>
</table>

<table class="Panel" width="100%">
  <tr>
	<td><% JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
						{ "tabAnagraficadottorati","Anagrafica Dottorati","/incarichi00/tab_anagrafica_dottorati.jsp" },
						{ "tabAnagraficadottoratiRate", "Rate", "/incarichi00/tab_anagraficadottorati_rate.jsp"}
					},
					bp.getTab("tab"),
					"center",
					"100%",
					null ); %>

	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>