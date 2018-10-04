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
	SimpleDetailCRUDController controller = bp.getPianoEconomicoSummary();
%>
<div class="card">
	<fieldset class="fieldset mb-2">
	<legend class="GroupLabel card-header text-primary">Riepilogo Importi</legend>
	<table class="m-2 p-2">
	  <tr>
	  	<td colspan=3>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Finanziato</span></td>
		<td>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Cofinanziato</span></td>
	  </tr>
	  <tr>         
	    <td><span class="FormLabel">Totale Progetto</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotale");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"default","imFinanziatoOf",true,null,null); %></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"default","imCofinanziatoOf",true,null,null); %></td>
	  </tr>                     	
	  <tr>         
	    <td><span class="FormLabel">Ripartiti</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotaleRipartito");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"imFinanziatoRipartito");%></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"imCofinanziatoRipartito");%></td>
	  </tr>                     	
	  <tr>         
	    <td><span class="FormLabel" style="color:red">da Ripartire</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotaleDaRipartire");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"imFinanziatoDaRipartire");%></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"imCofinanziatoDaRipartire");%></td>
	  </tr>                     	
	</table>
	</fieldset>
</div>
<div class="card">
	<fieldset class="fieldset mb-2">
	<legend class="GroupLabel card-header text-primary">Ripartizione Annuale</legend>
	<table class="m-2 p-2">
	  <tr><td>	
<%
	JSPUtils.tabbed(
				pageContext,
				"tabProgettoPianoEconomico",
				bp.getTabsPianoEconomico(),
				bp.getTab("tabProgettoPianoEconomico"),
				"center",
				"100%",
				null);	
%>
	  </td></tr>                     	
	</table>
	</fieldset>
</div>