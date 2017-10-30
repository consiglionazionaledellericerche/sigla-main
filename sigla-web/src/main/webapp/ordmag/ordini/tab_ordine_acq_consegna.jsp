<%@page import="it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk"%>
<%@ page 
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
	bp.getConsegne().writeHTMLTable(pageContext,"consegneSet",true,false,true,"100%","100px"); 
%>

<div class="Group">
	<table>
		<tr>
			<%
				bp.getConsegne().writeFormField(out, "quantita");
			    bp.getConsegne().writeFormField(out, "tipoConsegna");
				bp.getConsegne().writeFormField(out, "dtPrevConsegna");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
		    bp.getConsegne().writeFormField(out, "findMagazzino");
		    bp.getConsegne().writeFormField(out, "findLuogoConsegnaMag");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
			if (cons != null && cons.getTipoConsegna() != null && !cons.getTipoConsegna().equals("MAG")) {
				bp.getConsegne().writeFormField(out, "findUnitaOperativaOrdDest");
			}
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getConsegne().writeFormField(out, "imImponibile");
			    bp.getConsegne().writeFormField(out, "imIva");
			    bp.getConsegne().writeFormField(out, "imIvaD");
			    bp.getConsegne().writeFormField(out, "imTotaleConsegna");
			%>
		</tr>
	</table>
	<table>
		<tr>
			<%
				bp.getConsegne().writeFormField(out, "findObbligazioneScadenzario");
			%>
		</tr>
	</table>
</div>