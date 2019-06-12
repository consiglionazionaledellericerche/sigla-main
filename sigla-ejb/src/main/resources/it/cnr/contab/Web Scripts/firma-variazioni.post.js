script:{
	var json = jsonUtils.toObject(requestbody.content),
	    nodeRefSource = json.nodeRefSource,
	    nodoDoc = search.findNode(nodeRefSource),
	    username = json.username,
	    password = json.password,
	    otp = json.otp, mimetypeDoc, nameDoc, service, estensione, nameDocFirmato, docFirmato;
	mimetypeDoc = nodoDoc.properties.content.mimetype;
	nameDoc = nodoDoc.name;
	service = cnrutils.getBean('mimetypeService');
	estensione = "." + service.getExtension(nodoDoc.mimetype),
	i = 0,
	fileExists = false;
	if (nodoDoc.name.indexOf(estensione) === -1) {
	  nameDoc = nameDoc + estensione;
	}
	nameDocFirmato = nameDoc + ".p7m";
	//cerco se esiste il documento
	while(i < nodoDoc.parent.children.length) {
		if (nodoDoc.parent.children[i].name == nameDocFirmato) {
			docFirmato = nodoDoc.parent.children[i];
			fileExists = true;
			logger.warn("FIRMA VARIAZIONI SIGLA --- TROVATO FILE FIRMATO: " + nodoDoc.parent.children[i].name);
		}
		i = i + 1;
	}
	//crea il doc firmato
	if (!fileExists) {
		docFirmato = nodoDoc.parent.createFile(nameDocFirmato, "cnr:envelopedDocument");
		docFirmato.mimetype = "application/p7m";
		docFirmato.save();
		try{
			logger.warn("TENTATIVO FIRMA VARIAZIONI SIGLA --- username: " + username + " otp: " + otp);
			arubaSign.pkcs7SignV2(username, password, otp, nodoDoc.nodeRef, docFirmato.nodeRef);
		} catch(ex) {
			logger.warn("ERRORE FIRMA VARIAZIONI SIGLA --- username: " + username + " otp: " + otp + "ERRORE: " + ex.javaException.message);
			docFirmato.remove();
	        status.code = 500;
	        status.message = ex.javaException.message;
			status.redirect = true;
			break script;
		}
	}
	nodoDoc.createAssociation(docFirmato, "varpianogest:allegatiVarBilancio");
	nodoDoc.addAspect("cnr:signedDocument");
	nodoDoc.save();
	docFirmato.createAssociation(nodoDoc, "cnr:signedDocumentAss");  
	model.nodeRef = docFirmato.nodeRef.toString();
}