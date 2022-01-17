<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<SCRIPT LANGUAGE="Javascript">
function escludiPrimo(index){
	var names=new Array();
	names[1]="main.fl_detraibile";
	names[2]="main.fl_non_imponibile";
	names[3]="main.fl_esente";
	names[4]="main.fl_non_soggetto";
	names[5]="main.fl_escluso";
	
	for (i=1; i<=5;i++)
		if (index!=i)
			document.mainForm.elements(names[i]).checked=false;	
}
</SCRIPT>
<script language="javascript" src="scripts/css.js"></script>
<title>Voce Iva</title>
</head>
<body class="Form">


<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	Voce_ivaBulk voce_iva = (Voce_ivaBulk) bp.getModel();
	bp.openFormWindow(pageContext); %>
	
	<div class="Group card p-2">
	    <table>
			<tr>
				<% bp.getController().writeFormField(out,"cd_voce_iva"); %>
			</tr>
			<tr>
			    <td><% bp.getController().writeFormLabel(out,"ds_voce_iva"); %></td>
			    <td class="w-100"><% bp.getController().writeFormInput(out,"ds_voce_iva"); %></td>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"percentuale"); %>
			</tr>
			<tr>
			    <% bp.getController().writeFormField(out,"dt_inizio_validita"); %>
			</tr>
		</table>
        <div class="Group border rounded border-info p-2 m-1">
            <table class="Panel" cellspacing="0" cellpadding="2">
                <tr>
                    <td>
                        <% bp.getController().writeFormLabel(out,"fl_detraibile");%>
                    </td>
                    <td>
                        <% bp.getController().writeFormInput(out,null,"fl_detraibile",false,null,"onclick=\"escludiPrimo(1);submitForm('doDefault')\"");%>
                    </td>
                    <% 	if (voce_iva!=null && voce_iva.isDetraibile()) { %>
                            <td>&nbsp;</td>
                            <% bp.getController().writeFormField(out,"percentuale_detraibilita"); %>
                    <%	} %>
                </tr>
                <tr>
                    <td>
                        <% bp.getController().writeFormLabel(out,"fl_non_imponibile"); %>
                    </td>
                    <td>
                        <% bp.getController().writeFormInput(out,null,"fl_non_imponibile",false,null,"onclick=\"escludiPrimo(2);submitForm('doDefault')\""); %>
                    </td>
                </tr>
                <tr>
                    <td>
                        <% bp.getController().writeFormLabel(out,"fl_esente"); %>
                    </td>
                    <td>
                        <% bp.getController().writeFormInput(out,null,"fl_esente",false,null,"onclick=\"escludiPrimo(3);submitForm('doDefault')\""); %>
                    </td>
                </tr>
                <tr>
                    <td>
                        <% bp.getController().writeFormLabel(out,"fl_non_soggetto"); %>
                    </td>
                    <td>
                        <% bp.getController().writeFormInput(out,null,"fl_non_soggetto",false,null,"onclick=\"escludiPrimo(4);submitForm('doDefault')\""); %>
                    </td>
                </tr>
                <tr>
                    <td>
                        <% bp.getController().writeFormLabel(out,"fl_escluso"); %>
                    </td>
                    <td>
                        <% bp.getController().writeFormInput(out,null,"fl_escluso",false,null,"onclick=\"escludiPrimo(5);submitForm('doDefault')\""); %>
                    </td>
                </tr>
            </table>
        </div>
		<table cellspacing="0" cellpadding="2">
            <tr>
                <% bp.getController().writeFormField(out,"ti_applicazione"); %>
                <% bp.getController().writeFormField(out,"ti_bene_servizio"); %>
                <% bp.getController().writeFormField(out,"ti_bollo"); %>
            </tr>
        </table>
        <table cellspacing="0" cellpadding="2">
            <tr>
                    <% bp.getController().writeFormField(out,"gruppo_iva"); %>
            </tr>
        </table>
        <table cellspacing="0" cellpadding="2">
            <tr>
                <% bp.getController().writeFormField(out,"fl_default_istituzionale"); %>
                <% bp.getController().writeFormField(out,"fl_solo_italia"); %>
                <% bp.getController().writeFormField(out,"fl_obb_dichiarazione_intento"); %>
                <% bp.getController().writeFormField(out,"fl_iva_non_recuperabile"); %>
            </tr>
		</table>
		<%	if (voce_iva != null) { %>
            <div class="Group card p-1">
                <table>
                    <tr>
                       <td><% bp.getController().writeFormLabel(out,"naturaOperNonImpSdi");%></td>
                       <td class="w-75"><% bp.getController().writeFormInput(out,"naturaOperNonImpSdi");%></td>
                    </tr>
                    <tr>
                        <% bp.getController().writeFormField(out,"rifNormOperNonImpSdi");%>
                    </tr>
                </table>
            </div>
         <%	} %>
	</div>
	<% bp.closeFormWindow(pageContext); %>
</body>
</html>