/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 *
 * @author AlexsanderPimenta
 */
public class ImgProdBean {
    
    private String codProd;
    
    private String urlImagem;
    
    private Integer posicao;
    
    private int idProdutoEcom;

    /**
     * @return the codProd
     */
    public String getCodProd() {
        return codProd;
    }

    /**
     * @param codProd the codProd to set
     */
    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    /**
     * @return the urlImagem
     */
    public String getUrlImagem() {
        return urlImagem;
    }

    /**
     * @param urlImagem the urlImagem to set
     */
    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    /**
     * @return the idProdutoEcom
     */
    public int getIdProdutoEcom() {
        return idProdutoEcom;
    }

    /**
     * @param idProdutoEcom the idProdutoEcom to set
     */
    public void setIdProdutoEcom(int idProdutoEcom) {
        this.idProdutoEcom = idProdutoEcom;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }
    
}
