<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>
<%
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
%>
<div class="card mb-1">
	<fieldset class="fieldset mb-2">
	<legend class="GroupLabel card-header text-primary">Riepilogo Importi Rimodulati</legend>
	<table class="m-2 p-2">
	  <tr>
	  	<td colspan=3>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Finanziato</span></td>
		<td>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Cofinanziato</span></td>
	  </tr>
	  <tr>         
	    <td><span class="FormLabel">Totale Progetto</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotaleRimodulato");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"default","imFinanziatoRimodulato",true,null,null); %></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"default","imCofinanziatoRimodulato",true,null,null); %></td>
	  </tr>                     	
	  <tr>         
	    <td><span class="FormLabel">Ripartiti</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotaleRimodulatoRipartito");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"imFinanziatoRimodulatoRipartito");%></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"imCofinanziatoRimodulatoRipartito");%></td>
	  </tr>                     	
	  <tr>         
	    <td><span class="FormLabel" style="color:red">da Ripartire</span></td>
	    <td><% bp.getController().writeFormInput(out,"imTotaleRimodulatoDaRipartire");%></td>
	    <td> = </td>
	    <td><% bp.getController().writeFormInput(out,"imFinanziatoRimodulatoDaRipartire");%></td>
		<td> + </td>
	    <td><% bp.getController().writeFormInput(out,"imCofinanziatoRimodulatoDaRipartire");%></td>
	  </tr>                     	
	</table>
	</fieldset>
</div>
<div class="card">
	<fieldset class="fieldset mb-2">
	<legend class="GroupLabel card-header text-primary">Ripartizione Annuale</legend>
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
	</fieldset>
</div>