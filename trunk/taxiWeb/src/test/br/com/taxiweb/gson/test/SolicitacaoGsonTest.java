package br.com.taxiweb.gson.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.model.Solicitacao;

public class SolicitacaoGsonTest {
	
	/**
	 * Teste simples de serialização e recuperaão de JSON String utilizando api Gson do Google.
	 */
	@Test
//	public void testListaSolicitacoesGson()
//	{
//		SolicitacaoGson sgson = new SolicitacaoGson();
//		//faz parse de lista de solicitacoes para STring JSON
//		String result = sgson.listaSolicitacoesGson(geraListaSolicitacoes());
//		System.out.println("JSON gerado ==>> " + result);
//		
//		//retorna string gerada para lista de objetos Gson
//		Collection<SolicitacaoGson> lsgson = sgson.recuperaSolicitacoes(result);
//		System.out.println("Quantidade de objetos Gson "  + lsgson.size());
//		for (Iterator iterator = lsgson.iterator(); iterator.hasNext();) {
//			SolicitacaoGson solicitacaoGson = (SolicitacaoGson) iterator.next();
//			System.out.println("Obj Gson Origem Solicitacao ==>> " + solicitacaoGson.getOrigem());
//		}
//	}
	
	private List<Solicitacao> geraListaSolicitacoes()
	{
		Solicitacao s1 = new Solicitacao();
		Solicitacao s2 = new Solicitacao();
		
		s1.setId(new Long(1));
		s1.setDataHora(new Date());
		s1.setOrigem("Asa Sul");
		s1.setDestino("Aeroporto");
		s1.setNumeroPassageiros(2);
		s1.setInformacoesAdicionais("Extra Info");
		
		s2.setId(new Long(2));
		s2.setDataHora(new Date());
		s2.setOrigem("Asa Norte");
		s2.setDestino("Papuda");
		s2.setNumeroPassageiros(4);
		s2.setInformacoesAdicionais("Testes");
		
		ArrayList<Solicitacao> sols = new ArrayList<Solicitacao>();
		sols.add(s1);
		sols.add(s2);
		
		return sols;
	}
}
