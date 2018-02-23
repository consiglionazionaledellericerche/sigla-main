<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.coepcoan00.bp.*,it.cnr.contab.coepcoan00.core.bulk.*"
%>
<%CRUDScritturaPDoppiaBP bp = (CRUDScritturaPDoppiaBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Panel">
<tr>
	<td><% bp.getMovimentiDare().writeHTMLTable(pageContext,"scrittura", bp.isInserting(),false,bp.isInserting(),"100%","100px", true); %></td>
</tr>
</table>
<table class="Panel">
<tr>
	<td><% bp.getMovimentiDare().writeFormLabel(out, "cd_voce_ep"); %></td>
	<td><% bp.getMovimentiDare().writeFormInput(out, "cd_voce_ep");
	       bp.getMovimentiDare().writeFormInput(out, "ds_voce_ep");
	       bp.getMovimentiDare().writeFormInput(out, "find_voce_ep");%></td>	
</tr>
<tr>
	<td><% bp.getMovimentiDare().writeFormLabel(out, "ti_istituz_commerc");%></td>
	<td><% bp.getMovimentiDare().writeFormInput(out, "ti_istituz_commerc");%></td>	
</tr>
<tr>
	<td><% bp.getMovimentiDare().writeFormLabel(out, "dt_da_competenza_coge"); %></td>
	<td><% bp.getMovimentiDare().writeFormInput(out, "dt_da_competenza_coge");
	       bp.getMovimentiDare().writeFormLabel(out, "dt_a_competenza_coge");
	       bp.getMovimentiDare().writeFormInput(out, "dt_a_competenza_coge");%></td>	
</tr>
<tr>
	<td><% bp.getMovimentiDare().writeFormLabel(out, "im_movimento"); %></td>
	<td><% bp.getMovimentiDare().writeFormInput(out, "im_movimento");%></td>	
</tr>

</table>