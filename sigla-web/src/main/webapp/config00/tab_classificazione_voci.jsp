<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.pdcfin.cla.bulk.*,
		it.cnr.contab.config00.bp.*"
%>

<%
	CRUDClassificazioneVociBP bp = (CRUDClassificazioneVociBP)BusinessProcess.getBusinessProcess(request);

	SimpleDetailCRUDController controller =  bp.getCrudAssLivelli();

    // Se c'è un record in insert valorizzo con false il flag di gestione dell'inserimento multiplo senza salvare
  	boolean isTableEnabled = true;
	boolean isFirstTab = bp.getTabCorrente().intValue()==1; 
	boolean isFieldReadOnly = !isFirstTab;
%>

<body class="Form">
<BR>
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
		<table class="Panel">
			<TR>
				<TD><%bp.getController().writeFormField(out,"fl_class_sac"); %></TD>
				<TD><%bp.getController().writeFormField(out,"fl_solo_gestione"); %></TD>
				<% if (bp.getModel() instanceof Classificazione_voci_speBulk) { %>
					<TD><%bp.getController().writeFormField(out,"fl_piano_riparto"); %></TD>
					<TD><%bp.getController().writeFormField(out,"fl_prev_obb_anno_suc"); %></TD>
				<%}%>
				<TD><%bp.getController().writeFormField(out,"fl_esterna_da_quadrare_sac"); %></TD>
			</TR>
			<TR>
				<TD><%bp.getController().writeFormField(out,"ti_classificazione"); %></TD>
			</TR>
		</table>
		<% if (bp.getModel() instanceof Classificazione_voci_speBulk) { %>
		<div class="Group" style="width:100%">
		<table class="Panel" cellspacing=1 cellpadding=1>
			<table>
				<TR>
					<TD><%bp.getController().writeFormLabel(out,"fl_decentrato"); %></TD>
					<TD><%bp.getController().writeFormInput(out,"fl_decentrato"); %></TD>
				</TR>
			</table>
			<table class="Panel">
				<tr>
					<TD><%bp.getController().writeFormLabel(out,"fl_accentrato"); %></TD>
					<TD><%bp.getController().writeFormInput(out,"fl_accentrato"); %></TD>
					<% if (!bp.isCdrAccentratoreHidden()) { %>
						<td><%bp.getController().writeFormLabel(out,"default","find_centro_responsabilita"); %></td>
						<td><%bp.getController().writeFormInput(out,"default","find_centro_responsabilita"); %></td>			 
					<%}%>
				</tr>
			</table>
		</table>
		</div> 
		<%}%>
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
		<table class="Panel">
			<TR>
				<TD><%controller.writeFormField(out,"fl_class_sac"); %></TD>
				<TD><%controller.writeFormField(out,"fl_solo_gestione"); %></TD>
				<% if (bp.getModel() instanceof Classificazione_voci_speBulk) { %>
					<TD><%controller.writeFormField(out,"fl_piano_riparto"); %></TD>
					<TD><%controller.writeFormField(out,"fl_prev_obb_anno_suc"); %></TD>
				<%}%>
				<TD><%controller.writeFormField(out,"fl_esterna_da_quadrare_sac"); %></TD>
			</TR>
		</table>
		<% if (bp.getModel() instanceof Classificazione_voci_speBulk) { %>
		<div class="Group" style="width:100%">
		<table class="Panel" cellspacing=1 cellpadding=1>
			<table>
				<TR>
					<TD><%controller.writeFormLabel(out,"fl_decentrato"); %></TD>
					<TD><%controller.writeFormInput(out,"fl_decentrato"); %></TD>
				</TR>
			</table>
			<table>
				<tr>
					<TD><%controller.writeFormLabel(out,"fl_accentrato"); %></TD>
					<TD><%controller.writeFormInput(out,"fl_accentrato"); %></TD>
					<% if (!bp.isCdrAccentratoreHidden()) { %>
						<td><%controller.writeFormLabel(out,"default","find_centro_responsabilita"); %></td>
					    <td><%controller.writeFormInput(out,"default","find_centro_responsabilita"); %></td>			 
					<%}%>
				</tr>
			</table>
		</table>
		</div>
		<%}%>
	<%}%>
</body>
