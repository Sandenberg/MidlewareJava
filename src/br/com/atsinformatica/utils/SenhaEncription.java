/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AlexsanderPimenta
 */
public class SenhaEncription {
    
    private static final char[] TABELA_ENCRIPTACAO = {'0','1','2','3','4','5',
	                                                  '6','7','8','9','A','B',
	                                                  'C','D','E','F','G','H',
	                                                  'I','J','K','L','M','N',
	                                                  'O','P','Q','R','S','T',
	                                                  'U','V','W','X','Y','Z'};
	private static final char[] CHAVE_ENCRIPTACAO = "RESLOGSERVER".toCharArray();
	
	private final Map<Character, Integer> mapaEncriptacao;
	
	public SenhaEncription() {
		this.mapaEncriptacao = new HashMap<Character, Integer>();
		for (int i = 0; i < TABELA_ENCRIPTACAO.length; i++) {
			this.mapaEncriptacao.put(TABELA_ENCRIPTACAO[i], i);
		}
	}

	public String encripta(String original, int tamanho) {
		// TODO: Implementar SenhaEncryptionServiceImpl.encripta(original, tamanho)
		throw new UnsupportedOperationException(
				"SenhaEncryptionServiceImpl.encripta(original, tamanho) não implementado.");
	}

	public String decripta(String encriptado) {
		StringBuilder sb = new StringBuilder();
		
		int chave = 0;
		int i = 0;
		char[] chars = encriptado.toCharArray();
		
		while (i < chars.length) {
			int charNum = this.decriptaByte(chars, i, chave);
			sb.append((char)charNum);
			chave = chave ^ charNum;
			chave = this.proximaChave(chave, i / 2 + 1);
			i += 2;
		}
		
		return sb.toString();
	}
	
	private int decriptaByte(char[] chars, int pos, int chave) {
		if (chars.length - pos < 2) {						
			return 0;
		}
		
		int result = (this.charNum(chars[pos]) - (chave >> 8) % 19) +
				((this.charNum(chars[pos + 1]) - (chave >> 8) % 19) << 4);
		return (result ^ (chave & 0xFF)) & 0xFF;
	}
	
	private int charNum(char ch) {
		Integer n = this.mapaEncriptacao.get(ch);
		if (n == null) {
			throw new InvalidParameterException("Caractere de encriptação inválido: " + ch);
		}
		return n.intValue();
	}
	
	private int proximaChave(int chave, int pos) {
		return (chave + (int)CHAVE_ENCRIPTACAO[pos % CHAVE_ENCRIPTACAO.length]) * 17 % 0x4321; 		
	}
    
}
