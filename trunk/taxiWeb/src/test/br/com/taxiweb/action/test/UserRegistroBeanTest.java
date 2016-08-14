package br.com.taxiweb.action.test;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class UserRegistroBeanTest extends SeamTest {

	@Test
	public void registrarUsuarioTest() throws Exception {
		new ComponentTest() {
			protected void testComponents() throws Exception {
				
				setValue("#{user.nome}", "Marcos");
				setValue("#{user.sobrenome}", "Maia");
				setValue("#{user.password}", "123456");
						
				assert invokeMethod("#{registroBean.registrarUsuario}").equals("");
			}
		}.run();
	}

}
