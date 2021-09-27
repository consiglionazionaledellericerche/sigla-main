<%@page import="it.cnr.contab.ordmag.ordini.bulk.ParametriSelezioneOrdiniAcqBulk"%>
<%@page import="it.cnr.contab.ordmag.ordini.bp.ParametriSelezioneOrdiniAcqBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>


<title>Parametri di Selezione Ordini</title>

</head>
<body class="Form">
<%	ParametriSelezioneOrdiniAcqBP bp = (ParametriSelezioneOrdiniAcqBP)BusinessProcess.getBusinessProcess(request);
	ParametriSelezioneOrdiniAcqBulk parametri = (ParametriSelezioneOrdiniAcqBulk)bp.getModel();
  bp.openFormWindow(pageContext);%>

	<div class="Group card p-2 mt-2" style="width:100%">
		<table>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findUnitaOperativaOrd");
					%>
				</td>
				<td colspan="5">
					<%
						bp.getController().writeFormInput(out, "findUnitaOperativaOrd");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findMagazzino");
					%>
				</td>
				<td colspan="5">
					<%
						bp.getController().writeFormInput(out, "findMagazzino");
					%>
				</td>
			</tr>
		</table>
	</div>

	<div class="Group card p-2" style="width:100%">
		<table width="100%">
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "daDataOrdine");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "daDataOrdine");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "aDataOrdine");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "aDataOrdine");
                    %>
                </td>
            </tr>

			<tr>
				<td>
                            <%
                                bp.getController().writeFormLabel(out, "findUnitaOperativaRicevente");
                            %>
                        </td>
                        <td>
                            <%
                                bp.getController().writeFormInput(out, "findUnitaOperativaRicevente");
                            %>
                        </td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "findNumerazioneOrd");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findNumerazioneOrd");
					%>
				</td>
			</tr>

			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daDataOrdineDef");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daDataOrdineDef");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aDataOrdineDef");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aDataOrdineDef");
					%>
				</td>
			</tr>
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "daDataPrevConsegna");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "daDataPrevConsegna");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "aDataPrevConsegna");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "aDataPrevConsegna");
                    %>
                </td>
            </tr>
            <tr>
                    <td>
                        <%
                            bp.getController().writeFormLabel(out, "findDaBeneServizio");
                        %>
                    </td>
                    <td>
                        <%
                            bp.getController().writeFormInput(out, "findDaBeneServizio");
                        %>
                    </td>
                    <td class="pl-5">
                        <%
                            bp.getController().writeFormLabel(out, "findABeneServizio");
                        %>
                    </td>
                    <td>
                        <%
                            bp.getController().writeFormInput(out, "findABeneServizio");
                        %>
                    </td>
                </tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daNumeroOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daNumeroOrdine");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aNumeroOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aNumeroOrdine");
					%>
				</td>
			</tr>

			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findTerzo");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findTerzo");
					%>
				</td>
				<td>
                    <%
                        bp.getController().writeFormLabel(out, "statoOrdine");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "statoOrdine");
                    %>
                </td>
			</tr>
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "tipoConsegna");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "tipoConsegna");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "statoConsegna");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "statoConsegna");
                    %>
                </td>
            </tr>
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findResponsabile");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findResponsabile");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findCup");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findCup");
                    %>
                </td>
            </tr>
 			<tr>
                 <td>
                     <%
                         bp.getController().writeFormLabel(out, "findCig");
                     %>
                 </td>
                 <td>
                     <%
                         bp.getController().writeFormInput(out, "findCig");
                     %>
                 </td>
                 <td>
                     <%
                         bp.getController().writeFormLabel(out, "findContratto");
                     %>
                 </td>
                 <td>
                     <%
                         bp.getController().writeFormInput(out, "findContratto");
                     %>
                 </td>
             </tr>
  			<tr>
                  <td>
                      <%
                          bp.getController().writeFormLabel(out, "findTipoOrdine");
                      %>
                  </td>
                  <td>
                      <%
                          bp.getController().writeFormInput(out, "findTipoOrdine");
                      %>
                  </td>
                  <td>
                      <%
                          bp.getController().writeFormLabel(out, "findProceduraAmministrativa");
                      %>
                  </td>
                  <td>
                      <%
                          bp.getController().writeFormInput(out, "findProceduraAmministrativa");
                      %>
                  </td>
              </tr>
  			<tr>
                  <td>
                      <%
                          bp.getController().writeFormLabel(out, "findImpegno");
                      %>
                  </td>
                  <td>
                      <%
                          bp.getController().writeFormInput(out, "findImpegno");
                      %>
                  </td>

             </tr>
		</table>
	</div>
	<% bp.closeFormWindow(pageContext); %>
</body>