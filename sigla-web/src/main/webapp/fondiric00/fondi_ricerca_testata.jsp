<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondiric00.bp.*,
		it.cnr.contab.fondiric00.core.bulk.*"
%>

<%
	TestataFondiRicercaBP bp = (TestataFondiRicercaBP)BusinessProcess.getBusinessProcess(request);
	Fondo_attivita_vincolataBulk bulk = (Fondo_attivita_vincolataBulk)bp.getModel();
%>

<table class="Panel">
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"cd_fondo");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"cd_fondo");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"ds_fondo");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"ds_fondo");%>
	</TD></TR>

	<TR><TD>
		<% bp.getController().writeFormLabel(out,"tipo");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"tipo");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"unita_organizzativa");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"unita_organizzativa");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"responsabile");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"responsabile");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"ente");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"ente");%>
	</TD></TR>

	<TR><TD>
		<% bp.getController().writeFormLabel(out,"dt_inizio");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"dt_inizio");%>
		</TD><TD>
		<% bp.getController().writeFormLabel(out,"dt_fine");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"dt_fine");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"dt_proroga");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"dt_proroga");%>
	</TD></TR>
	<TR><TD>
		<% bp.getController().writeFormLabel(out,"importo_fondo");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"importo_fondo");%>
		</TD><TD>
		<% bp.getController().writeFormLabel(out,"importo_divisa");%>
		</TD><TD>
		<% bp.getController().writeFormInput(out,"importo_divisa");%>
	</TD></TR>

	<TR><TD>
		<% bp.getController().writeFormLabel(out,"divisa");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"divisa");%>
	</TD></TR>

	<TR><TD>
		<% bp.getController().writeFormLabel(out,"note");%>
		</TD><TD colspan="3">
		<% bp.getController().writeFormInput(out,"note");%>
	</TD></TR>
</table>