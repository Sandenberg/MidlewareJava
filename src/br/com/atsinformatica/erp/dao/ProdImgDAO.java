/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ImgProdBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


/**
 *
 * @author AlexsanderPimenta
 */ 
public class ProdImgDAO {
    private Connection conn;
    
    
    /**
     * Retorna url de imagens do produto
     * @param codProd
     * @return List<ImgProdBean>
     */
    public List<ImgProdBean> listaImgCodProd(String codProd){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ImgProdBean> listaImgProdBean = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODPROD, URLIMAGEM, POSICAO FROM IMGPRODECOMMERCE "
                       + "WHERE CODPROD = ?  ORDER BY POSICAO                      ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codProd);
            rs = pstmt.executeQuery();
            ImgProdBean imgProdBean = null;
            while(rs.next()){
                imgProdBean = new ImgProdBean();
                imgProdBean.setCodProd(rs.getString("codprod"));
                imgProdBean.setUrlImagem(rs.getString("urlimagem"));
                imgProdBean.setPosicao(rs.getInt("posicao"));
                listaImgProdBean.add(imgProdBean);
            }
            Logger.getLogger(ProdImgDAO.class).info("Url de imagens do produto, retornada com sucesso!");
            return listaImgProdBean;
        }catch(Exception e){
            Logger.getLogger(ProdImgDAO.class).error("Erro ao retornar url de imagens do produto: "+e);
            return null;           
        }
    }
    
    /**
     * Lista todas imagens cadastradas
     * @return lista de imagens
     */
    public List<ImgProdBean> listAll(){
        Statement stmt = null;
        ResultSet rs = null;
        List<ImgProdBean> listaImgProdBean = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODPROD, URLIMAGEM, POSICAO FROM IMGPRODECOMMERCE ORDER BY POSICAO";
            stmt = conn.createStatement();            
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ImgProdBean imgProdBean = new ImgProdBean();
                imgProdBean.setCodProd(rs.getString("codprod"));
                imgProdBean.setUrlImagem(rs.getString("urlimagem"));
                imgProdBean.setPosicao(rs.getInt("posicao"));
                listaImgProdBean.add(imgProdBean);
            }
            Logger.getLogger(ProdImgDAO.class).info("Imagens retornada com sucesso!");
            return listaImgProdBean;
        }catch(Exception e){
            Logger.getLogger(ProdImgDAO.class).error("Erro ao retornar imagens: "+e);
            return null;           
        }
    }
       
   
    /**
     * Cadastra imagem do produto
     * @param codProd código do produto
     * @param urlImagem url da imagem
     */
    public void gravar(String codProd, String urlImagem, Integer posicao){
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO IMGPRODECOMMERCE (CODPROD, URLIMAGEM, POSICAO) VALUES (?,?,?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, codProd);
            pstmt.setString(2, urlImagem);
            pstmt.setInt(3, posicao);
            pstmt.executeUpdate();
            Logger.getLogger(ProdImgDAO.class).info("Imagem do produto cadastrada com sucesso.");
        }catch(Exception e){
            Logger.getLogger(ProdImgDAO.class).info("Erro ao cadastra imagem do produto: "+e);           
            throw new RuntimeException(e);
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {
                Logger.getLogger(ProdImgDAO.class).info("Erro ao cadastra imagem do produto: "+ex);
            }
        }
    }
    
    /**
     * Deleta referência de imagens do produto
     * @param codProd código do produto
     */
    public void deletaImagensProd(String codProd){
        PreparedStatement pstmt = null;
        try{            
            conn = ConexaoATS.getConnection();
            String querie = "DELETE FROM IMGPRODECOMMERCE WHERE CODPROD = ?";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, codProd);
            pstmt.executeUpdate();
            Logger.getLogger(ProdImgDAO.class).info("Imagens do produto, deletada com sucesso.");
        }catch(Exception e){
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {}
            
            Logger.getLogger(ProdImgDAO.class).error("Erro ao deletar imagens do produto "+codProd + " " +e);
            throw new RuntimeException(e);
        }       
    }
}
