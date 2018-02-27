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
<div class="GroupLabel" >Spese</DIV>
<div class="Group">
<table >
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"generale_affitto");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"generale_affitto");%>
		</TD></TR>
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"gestione_nave");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"gestione_nave");%>
		</TD></TR>		
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"cc_brev_pi");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"cc_brev_pi");%>
		</TD></TR>
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"edilizia");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"edilizia");%>
		</TD></TR>	
		<TR><TD align="right" width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"totaleSpese");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"totaleSpese");%>
		</TD></TR>						
	</table>
</DIV>	
<div class="GroupLabel" >Costi Figurativi</DIV>
<div class="Group">
<table >
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"amm_immobili");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"amm_immobili");%>
		</TD></TR>
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"acc_tfr");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"acc_tfr");%>
		</TD></TR>		
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"amm_tecnico");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"amm_tecnico");%>
		</TD></TR>
		<TR><TD width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"amm_altri_beni");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"amm_altri_beni");%>
		</TD></TR>	
		<TR><TD align="right" width="688">
	  		<span class="FormLabel"><% bp.getController().writeFormLabel(out,"totaleCostiFigurativi");%></span>
			</TD><TD align="right" width="163">
	  		<% bp.getController().writeFormInput(out,"totaleCostiFigurativi");%>
		</TD></TR>						
	</table>
</DIV>	