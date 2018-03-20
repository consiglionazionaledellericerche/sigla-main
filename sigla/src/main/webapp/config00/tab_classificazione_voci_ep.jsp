<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.pdcep.cla.bulk.*,
		it.cnr.contab.config00.bp.*"
%>

<%
	CRUDClassificazioneVociEPBP bp = (CRUDClassificazioneVociEPBP)BusinessProcess.getBusinessProcess(request);

	SimpleDetailCRUDController controller =  bp.getCrudAssLivelli();

    // Se c'è un record in insert valorizzo con false il flag di gestione dell'inserimento multiplo senza salvare
  	boolean isTableEnabled = true;
	boolean isFirstTab = bp.getTabCorrente().intValue()==1; 
	boolean isFieldReadOnly = !isFirstTab;
%>
<body class="Form">
	<table class="Panel" >
	<% for (int i=1;i<=bp.getLivelliVisualizzati();i++) {%>
		<TR><TD>
			<% bp.getController().writeFormLabel(out,"cd_livello"+i);%>
			</TD><TD colspan="3">
			<% bp.getController().writeFormInput(out,null,"cd_livello"+i,isFieldReadOnly,null,null);%>
			</TD>
			<% if (!isFirstTab) {%>
				<TD colspan="3">
				<% bp.getController().writeFormInput(out,null,bp.getNameDsClassificazioniPre(i),isFieldReadOnly,null,null);%>
				</TD>	
			<%}%>
			</TR>	
	<%}%> 

	<% if (isFirstTab) {%>
		<TR>
			<TD><% bp.getController().writeFormLabel(out,"ds_classificazione");%></TD>
			<TD colspan="3"><% bp.getController().writeFormInput(out,"ds_classificazione");%></TD>
		</TR>
		<BR>
	<%}%>
	</table>
	<BR>
	<%
		if (!isFirstTab) {
		    controller.writeHTMLTable(pageContext,"classVociAssociate",isTableEnabled,true,isTableEnabled,"100%","100px"); 
	%>
		<table class="Panel">
			<TR>
			   <TD><%controller.writeForm(out,"classVociAssociate"); %></TD>
			</TR>
			<TR>
				<TD><% controller.writeFormLabel(out,"ds_classificazione");%></TD>
				<TD colspan="3"><% controller.writeFormInput(out,"ds_classificazione");%></TD>
			</TR>
		</table>
	<%}%>
</body>

