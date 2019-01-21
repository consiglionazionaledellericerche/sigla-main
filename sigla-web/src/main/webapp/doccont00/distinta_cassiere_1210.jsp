<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
 <% it.cnr.contab.doccont00.bp.CRUDDistintaCassiere1210BP bp= (it.cnr.contab.doccont00.bp.CRUDDistintaCassiere1210BP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript">
function doVisualizzaSingoloDocumento(esercizio, cds , uo, numero_documento, tipo) {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Documento contabile '+esercizio+'-'+cds+'-'+numero_documento+'.pdf?esercizio='+esercizio+'&cds='+cds+'&uo='+uo+'&numero_documento='+numero_documento+'&tipo='+tipo+'&methodName=scaricaDocumento&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doVisualizzaDistinta() {	
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Distinta.pdf?&methodName=scaricaDistinta&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
</head>
 <title>Distinta documenti 1210</title>
<body class="Form">
<% 	bp.openFormWindow(pageContext); %>
	<table class="Panel card p-3">
		<tr>
			<% bp.getController().writeFormField(out, "esercizio");%>
			<% bp.getController().writeFormField(out, "pgDistinta");%>
			<td rowspan="2">			
				<% if (bp.isDistintaInviata()) {
					JSPUtils.button(out,
						bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-file-pdf-o text-danger" : bp.encodePath("img/application-pdf.png"),
						bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-file-pdf-o text-danger" : bp.encodePath("img/application-pdf.png"),
						bp.encodePath("Visualizza distinta firmata"), 
						"javascript:doVisualizzaDistinta()", 
						true,
						bp.getParentRoot().isBootstrap()); 
					}
				%>
			</td>
		</tr>
		<tr><% bp.getController().writeFormField(out, "dtEmissione");%><% bp.getController().writeFormField(out, "dtInvio");%></tr>
		
	</table>
	<% if (!bp.isSearching()) {%>
		<% if (!bp.isViewing()) {%>
		<fieldset class="fieldset">
			<legend class="GroupLabel h5 text-primary">Documenti collegabili</legend>
			<div class="card">
                <table class="Panel" style="width:100%">
                <tr>
                    <td>
                         <%bp.getDistintaCassiere1210LettereDaCollegare().writeHTMLTable(pageContext,"firmaEseguita",false,true,false,"100%","200px", true); %>
                    </td>
                </tr>
                </table>
			</div>
		</fieldset>
		<br>
		<center>
			<% JSPUtils.button(out, 
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-arrow-up text-primary" : bp.encodePath("img/export24.gif"),
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-arrow-up text-primary" : bp.encodePath("img/export24.gif"),
					bp.encodePath("Rimuovi i documenti selezionati"), 
					"javascript:submitForm('doRimuoviDocumento')", 
					bp.isRimuoviButtonEnabled(), 
					bp.getParentRoot().isBootstrap() ); %>
			<% JSPUtils.button(out, 
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-arrow-down text-primary" : bp.encodePath("img/import24.gif"),
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-arrow-down text-primary" : bp.encodePath("img/import24.gif"),
					bp.encodePath("Associa i documenti selezionati"), 
					"javascript:submitForm('doAssociaDocumento')", 
					bp.isAssociaButtonEnabled(), 
					bp.getParentRoot().isBootstrap() ); %>
		</center>
		<br>
	<% } %>	
	<fieldset class="fieldset">
		<legend class="GroupLabel h5 text-primary">Documenti collegati</legend>
		<div class="card">
	    <table class="Panel" style="width:100%">	   
		<tr>
			<td>   
	   			 <%bp.getDistintaCassiere1210LettereCollegate().writeHTMLTable(pageContext,"firmaEseguita",false,true,false,"100%","200px", true); %>
			</td>
		</tr>
		</table>
		</div>
	</fieldset>
	<% } %>	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>