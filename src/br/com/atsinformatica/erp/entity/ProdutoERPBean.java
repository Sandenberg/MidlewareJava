/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.annotation.GenericController;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author AlexsanderPimenta
 */
@GenericController(classPath = "br.com.atsinformatica.erp.controller.ProdutoERPController")
public class ProdutoERPBean {
    
    private String codProd;
    private int idProdutoEcom;
    private Double estoqueDisponivel;
    private String nomeProd;
    private String ativo;
    private int condicao;
    private String codBarras;
    private String codCategoria;
    private String codFabricante;
    private Double altura;
    private Double largura;
    private Double profundidade;
    private String codAtributo1;
    private Double precoCheio;
    private Double precoFinal;
    private String codAtributo2;
    private String palavrasChave;
    private String metaDescricao;
    private String descricaoCompleta;
    private String descricaoBreve;
    private int grade;
    private String codGrade;
    private Double peso;
    private String mostraNaLoja;
    private String emOferta;
    private Double aliqIPI;
    private Double baseICM00;
    private Double baseICM01;
    private Double baseICM02;
    private Double baseICM03;    
    private String codTribut00;
    private String codTribut01;
    private String codTribut02;
    private String codTribut03;
    private Double aliqICM00;
    private Double aliqICM01;
    private Double aliqICM02;
    private Double aliqICM03;
    private String refFabricante;
    private List<CategoriaEcomErpBean> listaCategorias;
    private List<ImgProdBean> listaImagens;
    private int importaProdEcom;
    private Double preco;
    private Double preco2;
    private Double preco3;
    private Double preco4;
    private String descricao;
//    private String dateAdd;       
    private boolean importadoLoja;
       
    public ProdutoERPBean(){
        super();
        
    }
    public ProdutoERPBean(ResultSet rs) throws SQLException, UnsupportedEncodingException {
        this.codProd = rs.getString("codprod");
        this.idProdutoEcom = rs.getInt("idprodutoecom");        
        this.grade = rs.getInt("grade");
        this.codFabricante = rs.getString("codfabric");
        this.altura = rs.getDouble("altura");
        this.largura = rs.getDouble("largura");
        this.codBarras = rs.getString("codbarras");
        this.codCategoria = rs.getString("codcategoria");
        this.condicao = rs.getInt("condicao");
        this.descricaoCompleta = rs.getString("descricaocompleta");
        this.descricaoBreve = rs.getString("descricaobreve");
        this.nomeProd = rs.getString("nomeprod");
        this.ativo = rs.getString("ativo");
        this.metaDescricao = rs.getString("metadescricao");
        this.palavrasChave = rs.getString("palavraschave");
        this.codAtributo1 = rs.getString("codatributo1");
        this.codAtributo2 = rs.getString("codatributo2");
        this.precoCheio = rs.getDouble("preco"); 
        this.preco = rs.getDouble("preco1");
        this.preco2 = rs.getDouble("preco2");
        this.preco3 = rs.getDouble("preco3");
        this.preco4 = rs.getDouble("preco4");
        this.peso = rs.getDouble("peso");
        this.profundidade = rs.getDouble("profundidade");
        this.mostraNaLoja = rs.getString("mostranaloja");
        this.emOferta = rs.getString("emoferta");        
        this.aliqICM00 = rs.getDouble("ALIQICMSREG00");
        this.aliqICM01 = rs.getDouble("ALIQICMSREG01");
        this.aliqICM02 = rs.getDouble("ALIQICMSREG02");
        this.aliqICM03 = rs.getDouble("ALIQICMSREG03");
        this.baseICM00 = rs.getDouble("BASEICMSREG00");
        this.baseICM01 = rs.getDouble("BASEICMSREG01");
        this.baseICM02 = rs.getDouble("BASEICMSREG02");
        this.baseICM03 = rs.getDouble("BASEICMSREG03");
        this.codTribut00 = rs.getString("CODTRIBUT00");
        this.codTribut01 = rs.getString("CODTRIBUT01");
        this.codTribut02 = rs.getString("CODTRIBUT02");
        this.codTribut03 = rs.getString("CODTRIBUT03");
        this.aliqIPI = rs.getDouble("ALIQIPI");
        this.estoqueDisponivel = rs.getDouble("estoquedisponivel");
        this.refFabricante = rs.getString("reffabricante");
        this.importaProdEcom = rs.getInt("importaprodecom"); 
        this.descricao = rs.getString("descricao");
    }  
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
     * @return the estoqueDisponivel
     */
    public Double getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    /**
     * @param estoqueDisponivel the estoqueDisponivel to set
     */
    public void setEstoqueDisponivel(Double estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }

    /**
     * @return the nomeProd
     */
    public String getNomeProd() {
        return nomeProd;
    }

    /**
     * @param nomeProd the nomeProd to set
     */
    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    /**
     * @return the condicao
     */
    public int getCondicao() {
        return condicao;
    }

    /**
     * @param condicao the condicao to set
     */
    public void setCondicao(int condicao) {
        this.condicao = condicao;
    }

    /**
     * @return the codBarras
     */
    public String getCodBarras() {
        return codBarras;
    }

    /**
     * @param codBarras the codBarras to set
     */
    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    /**
     * @return the codCategoria
     */
    public String getCodCategoria() {
        return codCategoria;
    }

    /**
     * @param codCategoria the codCategoria to set
     */
    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    /**
     * @return the altura
     */
    public Double getAltura() {
        return altura;
    }

    /**
     * @param altura the altura to set
     */
    public void setAltura(Double altura) {
        this.altura = altura;
    }

    /**
     * @return the largura
     */
    public Double getLargura() {
        return largura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(Double largura) {
        this.largura = largura;
    }

    /**
     * @return the profundidade
     */
    public Double getProfundidade() {
        return profundidade;
    }

    /**
     * @param profundidade the profundidade to set
     */
    public void setProfundidade(Double profundidade) {
        this.profundidade = profundidade;
    }

    /**
     * @return the codAtributo1
     */
    public String getCodAtributo1() {
        return codAtributo1;
    }

    /**
     * @param codAtributo1 the codAtributo1 to set
     */
    public void setCodAtributo1(String codAtributo1) {
        this.codAtributo1 = codAtributo1;
    }

    /**
     * @return the precoCheio
     */
    public Double getPrecoCheio() {
        return precoCheio;
    }

    /**
     * @param precoCheio the precoCheio to set
     */
    public void setPrecoCheio(Double precoCheio) {
        this.precoCheio = precoCheio;
    }

    /**
     * @return the precoFinal
     */
    public Double getPrecoFinal() {
        return precoFinal;
    }

    /**
     * @param precoFinal the precoFinal to set
     */
    public void setPrecoFinal(Double precoFinal) {
        this.precoFinal = precoFinal;
    }

    /**
     * @return the codAtributo2
     */
    public String getCodAtributo2() {
        return codAtributo2;
    }

    /**
     * @param codAtributo2 the codAtributo2 to set
     */
    public void setCodAtributo2(String codAtributo2) {
        this.codAtributo2 = codAtributo2;
    }

    /**
     * @return the palavrasChave
     */
    public String getPalavrasChave() {
        return palavrasChave;
    }

    /**
     * @param palavrasChave the palavrasChave to set
     */
    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    /**
     * @return the metaDescricao
     */
    public String getMetaDescricao() {
        return metaDescricao;
    }

    /**
     * @param metaDescricao the metaDescricao to set
     */
    public void setMetaDescricao(String metaDescricao) {
        this.metaDescricao = metaDescricao;
    }

    /**
     * @return the descricaoCompleta
     */
    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    /**
     * @param descricaoCompleta the descricaoCompleta to set
     */
    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }
    
    /**
     * @return the descricaoBreve
     */
    public String getDescricaoBreve() {
        return descricaoBreve;
    }

    /**
     * @param descricaoBreve the descricaoBreve to set
     */
    public void setDescricaoBreve(String descricaoBreve) {
        this.descricaoBreve = descricaoBreve;
    }

    /**
     * @return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * @return the codGrade
     */
    public String getCodGrade() {
        return codGrade;
    }

    /**
     * @param codGrade the codGrade to set
     */
    public void setCodGrade(String codGrade) {
        this.codGrade = codGrade;
    }

    /**
     * @return the importadoLoja
     */
    public boolean isImportadoLoja() {
        return importadoLoja;
    }

    /**
     * @param importadoLoja the importadoLoja to set
     */
    public void setImportadoLoja(boolean importadoLoja) {
        this.importadoLoja = importadoLoja;
    }

    /**
     * @return the codFabricante
     */
    public String getCodFabricante() {
        return codFabricante;
    }

    /**
     * @param codFabricante the codFabricante to set
     */
    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
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

    /**
     * @return the peso
     */
    public Double getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(Double peso) {
        this.peso = peso;
    }

    /**
     * @return the ativo
     */
    public String getAtivo() {
        return ativo;
    }

    /**
     * @param ativo the ativo to set
     */
    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }    

    /**
     * @return the mostraNaLoja
     */
    public String getMostraNaLoja() {
        return mostraNaLoja;
    }

    /**
     * @param mostraNaLoja the mostraNaLoja to set
     */
    public void setMostraNaLoja(String mostraNaLoja) {
        this.mostraNaLoja = mostraNaLoja;
    }

    /**
     * @return the emOferta
     */
    public String getEmOferta() {
        return emOferta;
    }

    /**
     * @param emOferta the emOferta to set
     */
    public void setEmOferta(String emOferta) {
        this.emOferta = emOferta;
    }

    /**
     * @return the aliqIPI
     */
    public Double getAliqIPI() {
        return aliqIPI;
    }

    /**
     * @param aliqIPI the aliqIPI to set
     */
    public void setAliqIPI(Double aliqIPI) {
        this.aliqIPI = aliqIPI;
    }

    /**
     * @return the baseICM00
     */
    public Double getBaseICM00() {
        return baseICM00;
    }

    /**
     * @param baseICM00 the baseICM00 to set
     */
    public void setBaseICM00(Double baseICM00) {
        this.baseICM00 = baseICM00;
    }

    /**
     * @return the baseICM01
     */
    public Double getBaseICM01() {
        return baseICM01;
    }

    /**
     * @param baseICM01 the baseICM01 to set
     */
    public void setBaseICM01(Double baseICM01) {
        this.baseICM01 = baseICM01;
    }

    /**
     * @return the baseICM02
     */
    public Double getBaseICM02() {
        return baseICM02;
    }

    /**
     * @param baseICM02 the baseICM02 to set
     */
    public void setBaseICM02(Double baseICM02) {
        this.baseICM02 = baseICM02;
    }

    /**
     * @return the baseICM03
     */
    public Double getBaseICM03() {
        return baseICM03;
    }

    /**
     * @param baseICM03 the baseICM03 to set
     */
    public void setBaseICM03(Double baseICM03) {
        this.baseICM03 = baseICM03;
    }

    /**
     * @return the codTribut00
     */
    public String getCodTribut00() {
        return codTribut00;
    }

    /**
     * @param codTribut00 the codTribut00 to set
     */
    public void setCodTribut00(String codTribut00) {
        this.codTribut00 = codTribut00;
    }

    /**
     * @return the codTribut01
     */
    public String getCodTribut01() {
        return codTribut01;
    }

    /**
     * @param codTribut01 the codTribut01 to set
     */
    public void setCodTribut01(String codTribut01) {
        this.codTribut01 = codTribut01;
    }

    /**
     * @return the codTribut02
     */
    public String getCodTribut02() {
        return codTribut02;
    }

    /**
     * @param codTribut02 the codTribut02 to set
     */
    public void setCodTribut02(String codTribut02) {
        this.codTribut02 = codTribut02;
    }

    /**
     * @return the codTribut03
     */
    public String getCodTribut03() {
        return codTribut03;
    }

    /**
     * @param codTribut03 the codTribut03 to set
     */
    public void setCodTribut03(String codTribut03) {
        this.codTribut03 = codTribut03;
    }

    /**
     * @return the aliqICM00
     */
    public Double getAliqICM00() {
        return aliqICM00;
    }

    /**
     * @param aliqICM00 the aliqICM00 to set
     */
    public void setAliqICM00(Double aliqICM00) {
        this.aliqICM00 = aliqICM00;
    }

    /**
     * @return the aliqICM01
     */
    public Double getAliqICM01() {
        return aliqICM01;
    }

    /**
     * @param aliqICM01 the aliqICM01 to set
     */
    public void setAliqICM01(Double aliqICM01) {
        this.aliqICM01 = aliqICM01;
    }

    /**
     * @return the aliqICM02
     */
    public Double getAliqICM02() {
        return aliqICM02;
    }

    /**
     * @param aliqICM02 the aliqICM02 to set
     */
    public void setAliqICM02(Double aliqICM02) {
        this.aliqICM02 = aliqICM02;
    }

    /**
     * @return the aliqICM03
     */
    public Double getAliqICM03() {
        return aliqICM03;
    }

    /**
     * @param aliqICM03 the aliqICM03 to set
     */
    public void setAliqICM03(Double aliqICM03) {
        this.aliqICM03 = aliqICM03;
    }

    /**
     * @return the refFabricante
     */
    public String getRefFabricante() {
        return refFabricante;
    }

    /**
     * @param refFabricante the refFabricante to set
     */
    public void setRefFabricante(String refFabricante) {
        this.refFabricante = refFabricante;
    }

    /**
     * @return the listaCategorias
     */
    public List<CategoriaEcomErpBean> getListaCategorias() {
        return listaCategorias;
    }

    /**
     * @param listaCategorias the listaCategorias to set
     */
    public void setListaCategorias(List<CategoriaEcomErpBean> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    /**
     * @return the listaImagens
     */
    public List<ImgProdBean> getListaImagens() {
        return listaImagens;
    }

    /**
     * @param listaImagens the listaImagens to set
     */
    public void setListaImagens(List<ImgProdBean> listaImagens) {
        this.listaImagens = listaImagens;
    }

    /**
     * @return the importaProdEcom
     */
    public int getImportaProdEcom() {
        return importaProdEcom;
    }

    /**
     * @param importaProdEcom the importaProdEcom to set
     */
    public void setImportaProdEcom(int importaProdEcom) {
        this.importaProdEcom = importaProdEcom;
    }

    /**
     * @return the preco
     */
    public Double getPreco() {
        return preco;
    }

    /**
     * @param preco the preco to set
     */
    public void setPreco(Double preco) {
        this.preco = preco;
    }

    /**
     * @return the preco2
     */
    public Double getPreco2() {
        return preco2;
    }

    /**
     * @param preco2 the preco2 to set
     */
    public void setPreco2(Double preco2) {
        this.preco2 = preco2;
    }

    /**
     * @return the preco3
     */
    public Double getPreco3() {
        return preco3;
    }

    /**
     * @param preco3 the preco3 to set
     */
    public void setPreco3(Double preco3) {
        this.preco3 = preco3;
    }

    /**
     * @return the preco4
     */
    public Double getPreco4() {
        return preco4;
    }

    /**
     * @param preco4 the preco4 to set
     */
    public void setPreco4(Double preco4) {
        this.preco4 = preco4;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

//    public String getDateAdd() {
//        return dateAdd;
//    }
//
//    public void setDateAdd(String dateAdd) {
//        this.dateAdd = dateAdd;
//    }  

}
