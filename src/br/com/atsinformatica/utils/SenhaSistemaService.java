/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.utils;

import br.com.atsinformatica.midler.entity.SenhaSistema;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author AlexsanderPimenta
 */
public class SenhaSistemaService extends SenhaEncription {
    
	private static final int LAYOUT_CODIGOCLIENTE_POSICAO = 2;
	private static final int LAYOUT_EMPRESA_POSICAO = 10;
	private static final int LAYOUT_CODIGOCLIENTE_TAMANHO = 8;
	private static final int LAYOUT_EMPRESA_TAMANHO = 35;
	private static final int LAYOUT_DATAVALIDADE_POSICAO = 45;
	private static final int LAYOUT_COMPARTILHAMENTO_POSICAO = 55;
	private static final int LAYOUT_DATAVALIDADE_TAMANHO = 10;
	private static final int LAYOUT_COMPARTILHAMENTO_TAMANHO = 1;
	private static final int LAYOUT_RETAGUARDA_POSICAO = 56;
	private static final int LAYOUT_NUMNOTAS_POSICAO = 59;
	private static final int LAYOUT_RETAGUARDA_TAMANHO = 3;
	private static final int LAYOUT_NUMNOTAS_TAMANHO = 6;
	private static final int LAYOUT_VERSAO_POSICAO = 65;
	private static final int LAYOUT_VERSOESADICIONAIS_POSICAO = 70;
	private static final int LAYOUT_VERSAO_TAMANHO = 5;
	private static final int LAYOUT_VERSOESADICIONAIS_TAMANHO = 1;
	private static final int LAYOUT_TIPOINSTALACAO_POSICAO = 71;
	private static final int LAYOUT_CPFCNPJ_POSICAO = 74;
	private static final int LAYOUT_TIPOINSTALACAO_TAMANHO = 3;
	private static final int LAYOUT_CPFCNPJ_TAMANHO = 14;
	private static final int LAYOUT_REGSISTEMAS_POSICAO = 88;
	private static final int LAYOUT_REGSISTEMAS_SISTEMA_TAMANHO = 3;
	private static final int LAYOUT_REGSISTEMAS_LINKS_TAMANHO = 3;
	private static final int LAYOUT_CHECKSUM_TAMANHO = 4;
	private static final int LAYOUT_TAMANHO_MODULOS = 3;
	
	SenhaEncription senhaEncryptionService;

	public SenhaSistema parse(String senhaEncriptada) {
		if (senhaEncriptada == null || senhaEncriptada.trim().isEmpty()) {
			throw new InvalidParameterException("Senha não pode ficar em branco.");
		}		
		String textoSenha = this.decripta(senhaEncriptada).trim();
		
		SenhaSistema senha = new SenhaSistema();
		senha.setSenha(senhaEncriptada);
		
		senha.setCodigoCliente(
				this.cortaString(textoSenha, LAYOUT_CODIGOCLIENTE_POSICAO, LAYOUT_CODIGOCLIENTE_TAMANHO));
		senha.setEmpresa(this.retiraSimbolos(
				this.cortaString(textoSenha, LAYOUT_EMPRESA_POSICAO, LAYOUT_EMPRESA_TAMANHO)));
		senha.setDataValidade(this.stringParaData(
				this.cortaString(textoSenha, LAYOUT_DATAVALIDADE_POSICAO, LAYOUT_DATAVALIDADE_TAMANHO)));
		senha.setCompartilhado("S".equals(
				this.cortaString(textoSenha, LAYOUT_COMPARTILHAMENTO_POSICAO, LAYOUT_COMPARTILHAMENTO_TAMANHO)));
		senha.setRetaguarda(TipoRetaguarda.porSigla(
				this.cortaString(textoSenha, LAYOUT_RETAGUARDA_POSICAO, LAYOUT_RETAGUARDA_TAMANHO)));
		senha.setQuantidadeNotas(this.hexParaInt(
				this.cortaString(textoSenha, LAYOUT_NUMNOTAS_POSICAO, LAYOUT_NUMNOTAS_TAMANHO)));
		senha.setVersao(this.hexParaInt(
				this.cortaString(textoSenha, LAYOUT_VERSAO_POSICAO, LAYOUT_VERSAO_TAMANHO)));
		senha.setVersoesAdicionais(Integer.parseInt(
				this.cortaString(textoSenha, LAYOUT_VERSOESADICIONAIS_POSICAO, LAYOUT_VERSOESADICIONAIS_TAMANHO)));
		senha.setTipoInstalacao(TipoInstalacao.porSigla(
				this.cortaString(textoSenha, LAYOUT_TIPOINSTALACAO_POSICAO, LAYOUT_TIPOINSTALACAO_TAMANHO)));
		senha.setCpfCnpj(this.retiraSimbolos(
				this.cortaString(textoSenha, LAYOUT_CPFCNPJ_POSICAO, LAYOUT_CPFCNPJ_TAMANHO)));
		
		int numLinks = 0;
		int numModulos = this.hexParaInt(textoSenha.substring(textoSenha.length() - LAYOUT_TAMANHO_MODULOS, textoSenha.length()));
		
		int i = LAYOUT_REGSISTEMAS_POSICAO;
		int tamanhRegistroSistema = LAYOUT_REGSISTEMAS_SISTEMA_TAMANHO + LAYOUT_REGSISTEMAS_LINKS_TAMANHO;
		for (int j = 1; j <= numModulos && i <= textoSenha.length() - tamanhRegistroSistema; j++) {			
			int numLinksSistema = this.hexParaInt(
					this.cortaString(textoSenha, i + LAYOUT_REGSISTEMAS_SISTEMA_TAMANHO, LAYOUT_REGSISTEMAS_LINKS_TAMANHO));
			numLinks += numLinksSistema;
			
			InformacaoSistema sistema = new InformacaoSistema();
			sistema.setSigla(this.cortaString(textoSenha, i, LAYOUT_REGSISTEMAS_SISTEMA_TAMANHO));
			sistema.setTipoSistema(TipoSistema.porSigla(sistema.getSigla()));
			sistema.setQuantidadeLinks(numLinksSistema);
			
			senha.getSistemas().put(sistema.getSigla(), sistema);

			i += tamanhRegistroSistema;
		}
		
		return senha;
	}
	
	/**
	 * @param textoSenha
	 * @param posicaoInicial 
	 * 		Obs: por conveniência, para aproveitar as constantes usadas no Delphi, 
	 * 	este parâmetro começa do 1 ao invés de 0.
	 * @param tamanho
	 * @return
	 */
	private String cortaString(String textoSenha, int posicaoInicial, int tamanho) {
		return textoSenha.substring(posicaoInicial - 1, posicaoInicial - 1 + tamanho); 
	}
	
	private String retiraSimbolos(String s) {
		return s.replaceAll("[-/\\.#*\\]]", "");
	}
        
        private Date stringParaData(String s) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(s);
		} catch (ParseException e) {
			// TODO: Criar uma exceção apropriada.
			throw new RuntimeException(s + " não é uma data válida.", e);
		}
	}
        
	private int hexParaInt(String s) {
		return Integer.parseInt(s, 16);
	}
    
}
