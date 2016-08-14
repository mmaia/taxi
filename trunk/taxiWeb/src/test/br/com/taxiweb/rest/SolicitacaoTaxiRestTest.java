package br.com.taxiweb.rest;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PrePersist;

import org.jboss.seam.annotations.In;
import org.jboss.seam.mock.EnhancedMockHttpServletRequest;
import org.jboss.seam.mock.EnhancedMockHttpServletResponse;
import org.jboss.seam.mock.ResourceRequestEnvironment;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.mock.ResourceRequestEnvironment.Method;
import org.jboss.seam.mock.ResourceRequestEnvironment.ResourceRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import br.com.taxiweb.gson.DeviceAutenticadoGson;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.negocio.SolicitacaoNegocio;

public class SolicitacaoTaxiRestTest extends SeamTest{
	
	ResourceRequestEnvironment sharedEnvironment;

	@In(create=true)
	SolicitacaoNegocio solicitacaoNegocio;
	
	@BeforeClass
	public void prepareSharedEnvironment() throws Exception {

		sharedEnvironment = new ResourceRequestEnvironment(this) {

			@SuppressWarnings("serial")
			@Override
			public Map<String, Object> getDefaultHeaders() {

				return new HashMap<String, Object>() {
					{
					}
				};
			}
		};
	}
	
	@Test
	public void todasSolicitacoesEmAbertoTest() throws Exception {
		new ResourceRequest(sharedEnvironment, Method.POST,
				"/rest/login/solicitacaoTaxi/solicitacoesPendentes/maia.marcos%40gmail.com") {
			@Override
			protected void prepareRequest(EnhancedMockHttpServletRequest request) {
				request.setContentType("application/json");
				
				DeviceAutenticadoGson daGson = new DeviceAutenticadoGson();
				daGson.setSenhaDevice("edgrort");
				String json = DeviceAutenticadoGson.toJSON(daGson);
				request.setContent(json.getBytes());
			}
			@Override
			protected void onResponse(EnhancedMockHttpServletResponse response)

			{
				String respServidor = response.getContentAsString();
				
				if(respServidor == null)
					respServidor = "Sem resposta";
				
				
				
				if(response.getStatus() == 200)
				{
					System.out.println("Retornou do servidor ==>> " + respServidor);
				}
				else
				{
					System.out.println("Status da resp do servidor ==>> " + response.getStatus());
				}
			}
		}.run();
	}
	
	/**
	 * Teste verifica se o servidor está respondendo corretamente
	 * a URL 
	 * @throws Exception
	 */
	@Test
	public void finalizaSolicitacao() throws Exception {
		
		
		
		new ResourceRequest(sharedEnvironment, Method.POST,
				"/rest/login/solicitacaoTaxi/finalizaSolicitacao/maia.marcos%40gmail.com") {
			
			
			@PrePersist
			public void atualizaBD(){
				atualizaSolicitacaoStatusPendete();
			}
			
			@Override
			protected void prepareRequest(EnhancedMockHttpServletRequest request) {
				//SIMULA UMA REQUISICAO VINDA DO DEVICE
				request.setContentType("application/json");
				
				DeviceAutenticadoGson daGson = new DeviceAutenticadoGson();
				daGson.setSenhaDevice("kechka");
				String json = DeviceAutenticadoGson.toJSON(daGson);
				request.setContent(json.getBytes());
			}
			
			@Override
			protected void onResponse(EnhancedMockHttpServletResponse response)
			{
				//Pega resposta do servidor
				String respServidor = response.getContentAsString();
				
				//verifica se o servidor retornou alguma coisa
				if(respServidor == null)
				{
					respServidor = "Sem resposta";
				}
				else
				{
					if(response.getStatus() == 200)
					{
						System.out.println("Retornou do servidor ==>> " + respServidor);
						//transforma a resposta gson para Objeto SolicitacaoGson
						Gson gson = new Gson();
						SolicitacaoGson solGson = gson.fromJson(respServidor, SolicitacaoGson.class);
						
						if(solGson.getStatus() == Solicitacao.STATUS_FINALIZADA){
							System.out.println("Teste finalizaSolicitacao passou!");
						}else{
							System.err.println("Teste finalizaSolicitacao ERROR! Status="+solGson.getStatus());
						}
					}
					else
					{
						System.out.println("Status da resp do servidor ==>> " + response.getStatus());
					}
					
					
				}
				
			}
		}.run();
	}
	
	/**
	 * Teste verifica se o servidor está respondendo corretamente
	 * a URL 
	 * @throws Exception
	 */
	@Test
	public void cancelaSolicitacao() throws Exception {
		new ResourceRequest(sharedEnvironment, Method.POST,
				"/rest/login/solicitacaoTaxi/cancelaSolicitacao/maia.marcos%40gmail.com") {
			
			@PrePersist
			public void atualizaBD(){
				atualizaSolicitacaoStatusPendete();
			}
			
			@Override
			protected void prepareRequest(EnhancedMockHttpServletRequest request) {
				request.setContentType("application/json");
				
				DeviceAutenticadoGson daGson = new DeviceAutenticadoGson();
				daGson.setSenhaDevice("kechka");
				String json = DeviceAutenticadoGson.toJSON(daGson);
				request.setContent(json.getBytes());
			}
			@Override
			protected void onResponse(EnhancedMockHttpServletResponse response)

			{
				String respServidor = response.getContentAsString();
				
				
				//verifica se o servidor retornou alguma coisa
				if(respServidor == null)
				{
					respServidor = "Sem resposta";
				}
				else
				{
					if(response.getStatus() == 200)
					{
						System.out.println("Retornou do servidor ==>> " + respServidor);
						//transforma a resposta gson para Objeto SolicitacaoGson
						Gson gson = new Gson();
						SolicitacaoGson solGson = gson.fromJson(respServidor, SolicitacaoGson.class);
						
						if(solGson.getStatus() == Solicitacao.STATUS_CANCELADA){
							System.out.println("Teste finalizaSolicitacao passou!");
						}else{
							System.err.println("Teste finalizaSolicitacao ERROR! Status="+solGson.getStatus());
						}
					}
					else
					{
						System.out.println("Status da resp do servidor ==>> " + response.getStatus());
					}
					
					
				}
			}
		}.run();
	}
	
	/**
	 * Método que atualiza o status=0 (STATUS_PENDENTE) do registro id=1 <br>
	 * da tabela Solicitação
	 */
	private void atualizaSolicitacaoStatusPendete(){
		//cria um objeto para preparar a base com um STATUS válido
		Solicitacao sol = new Solicitacao();
		sol.setId(1L);
		sol.setStatus(Solicitacao.STATUS_PENDENTE);
		solicitacaoNegocio.setInstance(sol);
		//atualiza a base de dados 
		String updated = solicitacaoNegocio.update();
		if(updated.equals(1)){
			System.out.println("Atualizacao para STATUS_PENDENTE OK");
		}
	}
}
