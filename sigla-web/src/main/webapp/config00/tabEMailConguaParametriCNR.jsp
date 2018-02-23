<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.config00.bp.*"
%>
<%
	CRUDConfigParametriCnrBP bp = (CRUDConfigParametriCnrBP)BusinessProcess.getBusinessProcess(request);
%>
	<table class="Panel" width="100%">
		<TR><% bp.getController().writeFormField(out,"oggettoEmailTerziCongua");%></TR>
		<TR><% bp.getController().writeFormField(out,"corpoEmailTerziCongua");%></TR>
	</table>
	<script type="text/javascript">		
	  var oFCKeditor = new FCKeditor('main.corpoEmailTerziCongua') ;
	  var sSkinPath = "<%=request.getContextPath()%>/scripts/editor/skins/office2003/" ;
	  oFCKeditor.Config['SkinPath'] = sSkinPath ;
	  oFCKeditor.Config['PreloadImages'] =
		sSkinPath + 'images/toolbar.start.gif' + ';' +
		sSkinPath + 'images/toolbar.end.gif' + ';' +
		sSkinPath + 'images/toolbar.bg.gif' + ';' +
		sSkinPath + 'images/toolbar.buttonarrow.gif' ;
	  oFCKeditor.ReplaceTextarea();
	</script>		
	