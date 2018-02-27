<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	ProgettoBulk bulk = (ProgettoBulk)bp.getModel();
%>
<div class="GroupLabel" >Risorse presunte provenienti da es. precedenti</DIV>

<div class="Group" width="700">
<table width="744">
	<TR>
		<TD width="571">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"res_fo");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"res_fo");%>
	</TR>
	<TR>
		<TD width="571">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"res_min");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"res_min");%>
		</TD>
	</TR>
	<TR>
		<TD width="571">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"res_ue_int");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"res_ue_int");%>
		</TD>
	</TR>
	<TR>
		<TD width="571">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"res_privati");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"res_privati");%>
		</TD>
	</TR>
	<TR>
		<TD align="right" width="571">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"totaleRisorsePresunte");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"totaleRisorsePresunte");%>
		</TD>
	</TR>
</table>
</DIV>