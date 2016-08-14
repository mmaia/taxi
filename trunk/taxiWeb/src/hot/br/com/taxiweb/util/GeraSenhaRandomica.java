package br.com.taxiweb.util;

/**
 * Classe que gera uma senha randomica para ser utilizada pelo device cliente.
 * @author Marcos Maia <a href="mailto:maia.marcos@gmail.com">Envie e-mail: Marcos Maia</a>
 */
public class GeraSenhaRandomica {

	@SuppressWarnings("deprecation")
	public static String randomstring(int tamMinimo, int tamMaximo) {
		int n = rand(tamMinimo, tamMaximo);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) rand('a', 'z');
		return new String(b, 0);
	}

	private static int rand(int tamMinimo, int tamMaximo) {
		java.util.Random rn = new java.util.Random();
		int n = tamMaximo - tamMinimo + 1;
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return tamMinimo + i;
	}

	/**
	 * Método que gera senha entre 6 e 10 caracteres.
	 * @return a senha com os caracteres(entre 6 e 10) gerada randomicamente. 
	 */
	public static String randomstring() {
		return randomstring(6, 10);
	}
}
