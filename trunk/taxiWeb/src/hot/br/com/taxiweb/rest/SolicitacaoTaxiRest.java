package br.com.taxiweb.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import br.com.taxiweb.gson.DeviceAutenticadoGson;
import br.com.taxiweb.gson.FinalizaSolicitacaoGson;
import br.com.taxiweb.gson.PosicaoTaxistaGson;
import br.com.taxiweb.gson.ReservaSolicitacaoGson;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.gson.UsuarioGson;
import br.com.taxiweb.model.PosicaoTaxista;
import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.model.Usuario;
import br.com.taxiweb.negocio.SolicitacaoNegocio;
import br.com.taxiweb.seguranca.ValidaUsuarioDevice;

/**
 * Classe que trata as operações REST de Solicitação de Taxi, url padrão: 
 * http://{servidor}/taxiWeb/seam/resource/rest/solicitacaoTaxi
 * @author mmaia
 */
@Path("solicitacaoTaxi")
@Name("solicitacaoTaxiRest")
public class SolicitacaoTaxiRest {
	
	@Logger
	Log log;
	
	@In(create=true)
	SolicitacaoNegocio solicitacaoNegocio;
	
	@In(create=true)
	ValidaUsuarioDevice validaUsuarioDevice;
	
	/**
	 * Recupera ultimas 20 corridas do taxista.
	 * @param email o email do taxista
	 * @param json informando a senha do device;
	 * @return lista das ultimas 20 solicitações atendidas por um taxista.
	 */
	@POST
	@Path("/historicoSolicitacoes/{emailUsuario}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response historicoSolicitacoes(@PathParam("emailUsuario") String email, String json)
	{
		log.info("Executando SolicitacaoTaxiRest.historicoSolicitacoes");
		log.info("Login de usuario enviado na requisicao ==>> " + email);
		
		DeviceAutenticadoGson daGson = DeviceAutenticadoGson.fromJSON(json);
		Usuario taxista = validaUsuarioDevice.valida(email,daGson.getSenhaDevice());
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(taxista == null)
		{
			return Response.status(403).build();
		}
		
		//TODO - Tirar definição de numero de solicitações que está hardcoded, mudar isso para dinâmico.
		List<Solicitacao> listaSolicitacoes = solicitacaoNegocio.recuperaUltimasSolicitacoesTaxista(20, taxista);
		if(listaSolicitacoes.size() < 1)
			return Response.noContent().build();
		
		
		SolicitacaoGson solicitacaoGson = new SolicitacaoGson();
		String result = solicitacaoGson.listaSolicitacoesGson(transformaLista(listaSolicitacoes));
		return Response.ok(result).build();
	}
	
	/**
	 * Método que recupera lista de solicitações no servidor, para chamar este método utilizar o padrão:
	 * http://{servidor}/taxiWeb/seam/resource/rest/solicitacaoTaxi/solicitacoesPendentes/{email}
	 * O email deve ser codificado(encoded) portanto o símbolo @ deverá ser enviado como %40  
	 * http://localhost:8080/taxiWeb/seam/resource/rest/solicitacaoTaxi/solicitacoesPendentes/maia.marcos%40gmail.com
	 * Necessário utilizar método POST pois precisamos validar a senha do device antes de executar a requisição.
	 * @param email do usuario do device
	 * @return lista de solicitações do servidor.
	 */
	@POST
	@Path("/solicitacoesPendentes/{emailUsuario}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json; charset=UTF-8")
	public Response todasSolicitacoesEmAberto(@PathParam("emailUsuario") String email, String json)
	{
		log.info("Executando SolicitacaoTaxiRest.todasSolicitacoesEmAberto");
		log.info("Login de usuario enviado na requisicao ==>> " + email);
		
		DeviceAutenticadoGson daGson = DeviceAutenticadoGson.fromJSON(json);
		Usuario taxista = validaUsuarioDevice.valida(email,daGson.getSenhaDevice());
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(taxista == null)
		{
			return Response.status(403).build();
		}
		
		if(validaUsuarioDevice.ehTaxista(taxista) == false)
		{
			return Response.status(403).build();
		}
		
		List<Solicitacao> listaSolicitacoes = solicitacaoNegocio.listaSolicitacoes();
		
		if(listaSolicitacoes.size() < 1)
			return Response.noContent().build();
		
		SolicitacaoGson solicitacaoGson = new SolicitacaoGson();
		log.info("Construiu solicitacaoGson transformando resultado em string JSON");
		String result = solicitacaoGson.listaSolicitacoesGson(transformaLista(listaSolicitacoes));

		log.info("Quantidade de solicitacoes recuperadas ==>> " + listaSolicitacoes.size());
		return Response.ok(result).build();
	}
	
	private List<SolicitacaoGson> transformaLista(List<Solicitacao> solicitacao)
	{
		List<SolicitacaoGson> sols = new ArrayList<SolicitacaoGson>();
		SolicitacaoGson solGson = null;
		for (Iterator<Solicitacao> iterator = solicitacao.iterator(); iterator.hasNext();) {
			Solicitacao solic = (Solicitacao) iterator.next();
			solGson = new SolicitacaoGson();
			solGson.setIdSolicitacao(solic.getId());
			solGson.setOrigem(solic.getOrigem());
			solGson.setDestino(solic.getDestino());
			solGson.setDataHora(solic.getDataHora());
			solGson.setNumeroPassageiros(solic.getNumeroPassageiros());
			solGson.setInformacoesAdicionais(solic.getInformacoesAdicionais());
			sols.add(solGson);
		}
		return sols;
	}
	
	/**
	 * Faz a reserva da corrida(solicitação) para um taxista. Para chamar este serviço utilizar o padrão:
	 * http://{servidor}/taxiWeb/seam/resource/rest/solicitacaoTaxi/reservaSolicitacao/{emailTaxista}
	 * Esta chamada pode retornar 3 status codes previstos:
	 * 403 - Dados do device não autenticaram com sucesso no servidor, dados do taxista estão inválidos
	 * 409 - Corrida já foi requisitada e autorizada para outro taxista.
	 * 200 - Corrida foi reservada com sucesso para este taxista, retorna então também os dados do passageiro.
	 * @param email - O email do taxista que está tentando reservar a corrida.
	 * @param json - String JSON com a representação do objeto ReservaSolicitacaoGson, deve vir no corpo da requisição.
	 * @return - Dados do passageiro para qual o taxista reservou a corrida
	 */
	@PUT
	@Path("/reservaSolicitacao/{emailTaxista}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response reservaSolicitacao(@PathParam("emailTaxista") String email, String json)
	{
		log.debug("Entrou SolicitacaoTaxiRest.reservaSolicitacao");
		ReservaSolicitacaoGson rsgson = ReservaSolicitacaoGson.fromJSON(json);
		Usuario taxista = validaUsuarioDevice.valida(email,rsgson.getSenhaDevice());
		
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(taxista == null)
		{
			log.debug("Taxista não autenticou retornando 403 - forbidden");
			return Response.status(403).build();
		}

		log.debug("Recuperando solicitacao da base para reservar para taxista: " + taxista.getNome() + ", id da solicitacao: " + rsgson.getIdSolicitacao());
		solicitacaoNegocio.setSolicitacaoId(rsgson.getIdSolicitacao());
		Solicitacao sol = solicitacaoNegocio.getDefinedInstance();
		//checa se solicitação já foi requisitada por outro taxista com sucesso, caso tenha sido retorna
		//status code HTTP 409 - CONFLICT e NÃO reserva a corrida pois já está reservada para outro taxista.
		if(sol.getTaxista() != null)
		{
			log.debug("Corrida ja reservada retornando 409 - conflict");
			return Response.status(409).build();
		}	
		
		log.debug("Reservando solicitacao de corrida para o taxista " + taxista.getNome());
//		log.debug("Dados solicitacao: " + sol.toString());
		//faz a reserva para este taxista.
		sol.setTaxista(taxista);
		String persisted = solicitacaoNegocio.persist();
		
		log.debug("Solicitacao foi gravada? " + persisted);
		
		//recupera passageiro que solicitou a corrida para enviar dados ao taxista que pegou a corrida.
		Usuario passageiro = sol.getPassageiro();
		UsuarioGson ugson = new UsuarioGson();
		ugson.setNome(passageiro.getNome());
		ugson.setDataCadastro(passageiro.getDataCadastro());
		ugson.setSobreNome(passageiro.getSobreNome());
		ugson.setSexo(passageiro.getSexo());
		ugson.setCelular(passageiro.getCelular());
		String result = UsuarioGson.toJSON(ugson);
		return Response.ok(result).build();
	}
	
	
	/**
	 * Faz a atualização do posicionamento do taxista solicitacaoNegociopara uma determinada solicitação.
	 * @param email o email do taxista
	 * @param json objeto do tipo PosicaoTaxistaGson com as informações de id da solicitação, e latitude e longitude 
	 * atuais do taxista para ser atualizado na base.
	 * @return Status da execução da chamada pode ser 403 caso o taxista não estja autorizado ou a solicitação que 
	 * foi passada não esteja reservada para o taxista que passou o posicionamento ou 200 Ok informando que a posição
	 * foi atualizada com sucesso na base de dados.
	 */
	@PUT
	@Path("/atualizaPosicaoTaxista/{emailTaxista}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response atualizaPosicaoTaxista(@PathParam("emailTaxista") String email, String json)
	{
		log.debug("Atualizando posicionamento do taxista em SolicitacaoTaxiRest.reservaSolicitacao");
		PosicaoTaxistaGson ptgson = PosicaoTaxistaGson.fromJSON(json);
		Usuario taxista = validaUsuarioDevice.valida(email,ptgson.getSenhaDevice());
		
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(taxista == null)
		{
			log.debug("Taxista não autenticou retornando 403 - forbidden");
			return Response.status(403).build();
		}
		
		//valida se taxista que enviou posicionamento está mesmo reservado para atendimento da solicitação passada.
		Solicitacao solicitacao = solicitacaoNegocio.recuperaSolicitacaoDeTaxista(ptgson.getIdSolicitacao(), taxista);
		
		if(solicitacao == null)
		{
			log.debug("Taxista tentou se posicionar para uma solicitasolicitacaoNegocio.getSolicitacaoId()ção que não está reservada para ele!");
			return Response.status(403).build();
		}
		
		//passou nas verificações e então recupera instancia de solicitacao para atualizar posicao do taxista.
		log.debug("Atualizando posicao do taxista na base de dados...");
		solicitacaoNegocio.setSolicitacaoId(ptgson.getIdSolicitacao());
		Solicitacao sol = solicitacaoNegocio.getDefinedInstance();
		log.debug("solicitacao recuperada da base, setando latlong...");
		PosicaoTaxista posTax = new PosicaoTaxista();
		posTax.setLatitude(ptgson.getLatitude());
		posTax.setLongitude(ptgson.getLongitude());
		sol.setPosicaoTaxista(posTax);
		log.debug("Chamando persist para atualizar posicao do taxista na base.");
		//atualiza posição do taxista na base
		String persisted = solicitacaoNegocio.persist();
		log.debug("Solicitacao foi gravada? " + persisted);
		
		return Response.ok().build();
	}
	
	/**
	 * Faz a reserva da corrida(solicitação) para um taxista. Para chamar este serviço utilizar o padrão:
	 * http://{servidor}/taxiWeb/seam/resource/rest/solicitacaoTaxi/finalizaSolicitacao/{emailTaxista}
	 * Esta chamada pode retornar 3 status codes previstos:
	 * 403 - Dados do device não autenticaram com sucesso no servidor, dados do taxista estão inválidos
	 * 409 - Corrida nao pode ser finalizada.
	 * 200 - Corrida foi finalizada com sucesso, retorna os dados da solicitação atualizada com codigo do STATUS=2 (Finalizada).
	 * @param email - O email do taxista que está tentando reservar a corrida.
	 * @param json - String JSON com a representação do objeto ReservaSolicitacaoGson, deve vir no corpo da requisição.
	 * @return - Dados da solicitação para o taxista que finalizou a corrida
	 */
	@PUT
	@Path("/finalizaSolicitacao/{emailTaxista}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizaSolicitacao(@PathParam("emailTaxista") String email, String json)
	{
		log.debug("Entrou SolicitacaoTaxiRest.finalizaSolicitacao");
		ReservaSolicitacaoGson rsgson = ReservaSolicitacaoGson.fromJSON(json);
		Usuario taxista = validaUsuarioDevice.valida(email,rsgson.getSenhaDevice());
		
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(taxista == null)
		{
			log.debug("Taxista não autenticou retornando 403 - forbidden");
			return Response.status(403).build();
		}

		log.debug("Verificando solicitacao na base : " + taxista.getNome() + ", id da solicitacao: " + rsgson.getIdSolicitacao());
		solicitacaoNegocio.setSolicitacaoId(rsgson.getIdSolicitacao());
		Solicitacao sol = solicitacaoNegocio.getDefinedInstance();
		//checa se solicitação está com o STATUS 0 ou 1 , caso contrario retorna erro
		//status code HTTP 409 - CONFLICT e NÃO finaliza a corrida novamente pois já foi finalizada.
		if(sol.getStatus() != Solicitacao.STATUS_RESERVADA && sol.getStatus() != Solicitacao.STATUS_PENDENTE)
		{
			log.debug("Corrida nao pode ser finalizada status="+sol.getStatus());
			return Response.status(409).build();
		}	
		
		log.debug("Finalizando a corrida pelo taxista " + taxista.getNome());
//		log.debug("Dados solicitacao: " + sol.toString());
		//altera o status da solicitação.
		sol.setId(solicitacaoNegocio.getSolicitacaoId());
		sol.setStatus(Solicitacao.STATUS_FINALIZADA);
		String updated = solicitacaoNegocio.update();
		
		log.debug("Solicitacao "+solicitacaoNegocio.getSolicitacaoId()+" finalizada gravada? " + updated);
		
		//recupera Status da corrida.
		SolicitacaoGson ugson = new SolicitacaoGson();
		ugson.setStatus(sol.getStatus());
		ugson.setIdSolicitacao(sol.getId());
		String result = SolicitacaoGson.toJSON(ugson);
		return Response.ok(result).build();
	}
	
	/**
	 * O usuário cancela a solicitação pelo device. Para chamar este serviço utilizar o padrão:
	 * http://{servidor}/taxiWeb/seam/resource/rest/solicitacaoTaxi/cancelaSolicitacao/{emailUsuario}
	 * Esta chamada pode retornar 3 status codes previstos:
	 * 403 - Dados do device não autenticaram com sucesso no servidor, dados do usuário estão inválidos
	 * 409 - Corrida nao pode ser cancelada.
	 * 200 - Corrida foi cancelada com sucesso, retorna os dados da solicitação atualDados da solicitação para qual o usuário que 
	 * cancelou a corridaizada com codigo do STATUS=3 (Cancelada).
	 * @param email - O email do taxista que está tentando reservar a corrida.
	 * @param json - String JSON com a representação do objeto ReservaSolicitacaoGson, deve vir no corpo da requisição.
	 * @return - Dados da solicitação para qual o usuário que cancelou a corrida
	 */
	@PUT
	@Path("/cancelaSolicitacao/{emailUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelaSolicitacao(@PathParam("emailUsuario") String email, String json)
	{
		log.debug("Entrou SolicitacaoTaxiRest.cancelaSolicitacao");
		ReservaSolicitacaoGson rsgson = ReservaSolicitacaoGson.fromJSON(json);
		Usuario usuario = validaUsuarioDevice.valida(email,rsgson.getSenhaDevice());
		
		//se taxista retornou null é pq o email e senha passados não são válidos retorna forbidden(403).
		if(usuario == null)
		{
			log.debug("Usuário não autenticou retornando 403 - forbidden");
			return Response.status(403).build();
		}

		log.debug("Verificando solicitacao na base : " + usuario.getNome() + ", id da solicitacao: " + rsgson.getIdSolicitacao());
		solicitacaoNegocio.setSolicitacaoId(rsgson.getIdSolicitacao());
		Solicitacao sol = solicitacaoNegocio.getDefinedInstance();
		//checa se solicitação está com o STATUS 0 ou 1 , caso contrario retorna erro
		//status code HTTP 409 - CONFLICT e NÃO finaliza a corrida novamente pois já foi finalizada.
		if(sol.getStatus() != Solicitacao.STATUS_RESERVADA || sol.getStatus() != Solicitacao.STATUS_PENDENTE)
		{
			log.debug("Corrida nao pode ser cancelada status="+sol.getStatus());
			return Response.status(409).build();
		}	
		
		log.debug("Cancelada a solicitacao pelo usuário " + usuario.getNome());
//		log.debug("Dados solicitacao: " + sol.toString());
		//altera o status da solicitação.
		sol.setId(solicitacaoNegocio.getSolicitacaoId());
		sol.setStatus(Solicitacao.STATUS_CANCELADA);
		String updated = solicitacaoNegocio.update();
		
		log.debug("Solicitacao "+solicitacaoNegocio.getSolicitacaoId()+" cancelada gravada? " + updated);
		
		//recupera Status da corrida.
		SolicitacaoGson ugson = new SolicitacaoGson();
		ugson.setStatus(sol.getStatus());
		ugson.setIdSolicitacao(sol.getId());
		String result = SolicitacaoGson.toJSON(ugson);
		return Response.ok(result).build();
	}
}
