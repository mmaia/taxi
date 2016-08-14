package br.com.taxiweb.test;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.mock.EnhancedMockHttpServletRequest;
import org.jboss.seam.mock.EnhancedMockHttpServletResponse;
import org.jboss.seam.mock.ResourceRequestEnvironment;
import org.jboss.seam.mock.ResourceRequestEnvironment.Method;
import org.jboss.seam.mock.ResourceRequestEnvironment.ResourceRequest;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginRestTest extends SeamTest {
	
	@Logger
	Log log;
	ResourceRequestEnvironment sharedEnvironment;

	@BeforeClass
	public void prepareSharedEnvironment() throws Exception {

		sharedEnvironment = new ResourceRequestEnvironment(this) {

			@SuppressWarnings("serial")
			@Override
			public Map<String, Object> getDefaultHeaders() {

				return new HashMap<String, Object>() {
					{
						put("Accept", "text/plain");
					}
				};
			}
		};
	}

	@Test
	public void acessaTesteDoLoginGET() throws Exception {
		new ResourceRequest(sharedEnvironment, Method.GET, "/rest/login/teste/TesteREST") {
			@Override
			protected void prepareRequest(EnhancedMockHttpServletRequest request) {
				request.addHeader("Accept-Language", "pt_BR");
			}

			@Override
			protected void onResponse(EnhancedMockHttpServletResponse response)

			{
				assert response.getStatus() == 200;
				assert response.getContentAsString().equals(
						"Executou teste REST ==>> TesteREST");
			}
		}.run();
	}

	@Test
	public void autenticarTest() throws Exception {
		new ResourceRequest(sharedEnvironment, Method.POST,
				"/rest/login/autenticar") {
			@Override
			protected void prepareRequest(EnhancedMockHttpServletRequest request) {
				request.addHeader("Accept-Language", "pt_BR");
				request.addParameter("login","maia.marcos@gmail.com" );
				request.addParameter("senha", "12345678");
			}
			@Override
			protected void onResponse(EnhancedMockHttpServletResponse response)

			{
				assert true;//se servidor responder qqer coisa passa no teste...
				assert response.getStatus() == 200;
				String senhaDevice = response.getContentAsString();
				log.debug("Retornou status OK 200, senha retornada ==>> " + senhaDevice);
			}
		}.run();
	}
}
