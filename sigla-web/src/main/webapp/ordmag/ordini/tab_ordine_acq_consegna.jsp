<%@page import="it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk"%>
<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<%  
    CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
	OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	bp.getConsegne().writeHTMLTable(pageContext,"consegneSetOrdine",true,false,true,"100%","140px"); 
%>

<div class="Group card p-2 mb-2">
	<table class="w-100">
		<tr>
			<td><% bp.getConsegne().writeFormLabel(out, "quantita"); %></td>
			<td><% bp.getConsegne().writeFormInput(out, "quantita"); %></td>
			<td colspan="6">
				<table width="100%">
				    <tr>
						<%
						    bp.getConsegne().writeFormField(out, "tipoConsegna");
							bp.getConsegne().writeFormField(out, "dtPrevConsegna");
						%>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><% bp.getConsegne().writeFormLabel(out, "findMagazzino"); %></td>
		    <td colspan="3"><% bp.getConsegne().writeFormInput(out, "findMagazzino"); %></td>
			<td><% bp.getConsegne().writeFormLabel(out, "findLuogoConsegnaMag"); %></td>
		    <td colspan="3"><% bp.getConsegne().writeFormInput(out, "findLuogoConsegnaMag"); %></td>
		</tr>
		<% if (cons != null && cons.getTipoConsegna() != null && !cons.getTipoConsegna().equals("MAG")) { %>
		<tr>
			<td><% bp.getConsegne().writeFormLabel(out, "findUnitaOperativaOrdDest"); %></td>
		    <td colspan="6"><% bp.getConsegne().writeFormInput(out, "findUnitaOperativaOrdDest"); %></td>
		</tr>
		<% } %>
		<tr>
			<%
				bp.getConsegne().writeFormField(out, "imImponibile");
			    bp.getConsegne().writeFormField(out, "imIva");
			    bp.getConsegne().writeFormField(out, "imIvaD");
			    bp.getConsegne().writeFormField(out, "imTotaleConsegna");
			%>
		</tr>
		<tr>
			<td><%bp.getConsegne().writeFormLabel(out, "findObbligazioneScadenzario"); %></td>
			<td colspan="7"><%bp.getConsegne().writeFormInput(out, "findObbligazioneScadenzario"); %></td>
		</tr>
		<tr>
			<td><%bp.getConsegne().writeFormLabel(out, "cercaConto"); %></td>
			<td colspan="7"><%bp.getConsegne().writeFormInput(out, "cercaConto"); %></td>
		</tr>
	</table>
</div>