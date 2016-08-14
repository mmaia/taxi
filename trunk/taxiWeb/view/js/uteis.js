/*Script pega a queryString da URL*/

qs = new Array();

variaveis = location.search.replace(/\x3F/, "").replace(/\x2B/g, " ")
		.split("&");
if (variaveis != "") {
	for (i = 0; i < variaveis.length; i++) {
		nvar = variaveis[i].split("=");
		qs[nvar[0]] = unescape(nvar[1]);
	}
}

function QueryString(variavel) {
	return qs[variavel];
}
