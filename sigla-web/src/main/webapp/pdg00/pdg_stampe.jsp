<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.utenze00.bp.*,
		it.cnr.contab.pdg00.bulk.*,
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Piano di Gestione</title>
</head>
<body class="Form">
<%
	PdGStampePreventivoBP bp = (PdGStampePreventivoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	CNRUserContext userInfo = (CNRUserContext)HttpActionContext.getUserContext(session);
%>
<table class="Panel">
  <tr> 
    <td colspan=4><b>Stampa del piano di gestione</b>
    </td>
  </tr>
  <tr> 
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione", "if (disableDblClick()) javascript:submitForm('doStampaFunzione')", bp.getParentRoot().isBootstrap());%></td>
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura", "if (disableDblClick()) javascript:submitForm('doStampaNatura')", bp.getParentRoot().isBootstrap());%></td>
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaCapitolo')", bp.getParentRoot().isBootstrap());%></td>
    <td><%JSPUtils.button(out, "img/print16.gif", "Per singolo dettaglio", "if (disableDblClick()) javascript:submitForm('doStampaDettagliata')", bp.getParentRoot().isBootstrap());%></td>
  </tr>
  <tr> 
    <td colspan=4><b>Stampa GAE</b>
    </td>
  </tr>
  <tr> 
    <td><%JSPUtils.button(out, "img/print16.gif", "Stampa", "if (disableDblClick()) javascript:submitForm('doStampaLA')", bp.getParentRoot().isBootstrap());%></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr> 
    <td colspan=4><b>Stampa aggregata per unità organizzativa</b>
    </td>
  </tr>
  <tr> 
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione", "if (disableDblClick()) javascript:submitForm('doStampaUOFunzione')", bp.getParentRoot().isBootstrap());%></td>
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura", "if (disableDblClick()) javascript:submitForm('doStampaUONatura')", bp.getParentRoot().isBootstrap());%></td>
    <td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaUOCapitolo')", bp.getParentRoot().isBootstrap());%></td>
    <td></td>
  </tr>
  <tr> 
    <td colspan=4><b>Stampa discrepanze spese/entrate su insieme GAE</b></td>
  </tr>
  <tr> 
    <td><%JSPUtils.button(out, "img/print16.gif", "Stampa", "if (disableDblClick()) javascript:submitForm('doStampaDiscrepanzeInsieme')", bp.getParentRoot().isBootstrap());%></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr> 
    <td colspan="4"><hr></td>
  </tr>
  <tr> 
    <td colspan="4"><b>Stampe aggregate per commessa</b></td>
  </tr>
  <tr> 
    <td colspan="4">&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="4"><b>Stampa CdR</b></td>
  </tr>
  <tr> 
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "Per natura", "if (disableDblClick()) javascript:submitForm('doStampaCDRNaturaC')", bp.getParentRoot().isBootstrap());%>
    </td>
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "Per natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaCDRCapitoloC')", bp.getParentRoot().isBootstrap());%>
    </td>
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "Per singolo dettaglio", "if (disableDblClick()) javascript:submitForm('doStampaCDRDettagliataC')", bp.getParentRoot().isBootstrap());%>
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="4"><b>Stampa UO</b></td>
  </tr>
  <tr> 
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "Per natura", "if (disableDblClick()) javascript:submitForm('doStampaUONaturaC')", bp.getParentRoot().isBootstrap());%>
    </td>
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "Per natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaUOCapitoloC')", bp.getParentRoot().isBootstrap());%>
    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="4"><b>Stampa CdS</b></td>
  </tr>
  <tr> 
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "img/print16.gif", "Per natura", "if (disableDblClick()) javascript:submitForm('doStampaCDSNaturaC')", bp.isUoPrincipale(userInfo), bp.getParentRoot().isBootstrap());%>
    </td>
    <td> 
      <%JSPUtils.button(out, "img/print16.gif", "img/print16.gif", "Per natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaCDSCapitoloC')", bp.isUoPrincipale(userInfo), bp.getParentRoot().isBootstrap());%>
    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>