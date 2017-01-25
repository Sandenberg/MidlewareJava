/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller.trigger;

import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class TriggerController{
    private Connection conn;

    //TODO: Refaturar este metodo para a criação de trigger seja mais dinamica
    public void createTriggers() {
        TriggerProduto triggerProduto = new TriggerProduto();
        TriggerCategoria triggerCategoria = new TriggerCategoria();
        TriggerFabricante triggerFabricante = new TriggerFabricante();
        TriggerCompProd triggerCompProd = new TriggerCompProd();
        TriggerAtributoGrade triggerAtributo = new TriggerAtributoGrade();
        TriggerTranspordadora triggerTranspordadora = new TriggerTranspordadora();
        TriggerGrade triggerGrade = new TriggerGrade();
        TriggerSubGrade triggerSubGrade = new TriggerSubGrade();
        TriggerPedidoC triggerPedidoC = new TriggerPedidoC();
        TriggerControleEntrega triggerControleEnt = new TriggerControleEntrega();
        TriggerNfSaidC triggerNfSaidC = new TriggerNfSaidC();
        SugestVendasTrigger sugestVendas = new SugestVendasTrigger();
        TriggerPedidoDev triggerPedidoDev = new TriggerPedidoDev();
        TriggerPedidoTroca triggerPedidoTroca = new TriggerPedidoTroca();
        try{
           //Produto
           triggerProduto.createTriggerAu();
           triggerProduto.createTriggerAi();
           triggerProduto.createTriggerAd();
           //Fabricante
           triggerCategoria.createTriggerAu();
           triggerCategoria.createTriggerAi();
           triggerCategoria.createTriggerAd();
           //Categoria
           triggerFabricante.createTriggerAu();
           triggerFabricante.createTriggerAi();
           triggerFabricante.createTriggerAd();
           //compprod
           triggerCompProd.createTriggerAu(); 
           //atributo grade
           triggerAtributo.createTriggerAu();
           triggerAtributo.createTriggerAi();
           triggerAtributo.createTriggerAd();
           //Verificar Transportadora
           triggerTranspordadora.createTriggerAu();
           triggerTranspordadora.createTriggerAi();
           triggerTranspordadora.createTriggerAd(); 
           //grade
           triggerGrade.createTriggerAu();
           triggerGrade.createTriggerAd();
           //subgrade
           triggerSubGrade.createTriggerAu();
           triggerSubGrade.createTriggerAd();
           //pedidoc
           triggerPedidoC.createTriggerAu();
           triggerPedidoC.createTriggerAd();
           //controleentrega
           triggerControleEnt.createTriggerAu();
           //nfsaidc
           triggerNfSaidC.createTriggerAi();
           //sugestvendas
           sugestVendas.createTriggerAi();
           sugestVendas.createTriggerAd();
           //Pedido troca
           triggerPedidoTroca.createTriggerAi();
           //Pedido devoulação
           triggerPedidoDev.createTriggerAi();
        }catch(Exception e){
            throw new RuntimeException(e);
        }        
    }

    private boolean verificaTrigger(String triggerName) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        boolean exist = false;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "select * "
                           + " from rdb$triggers "
                           + " where upper(rdb$trigger_name) = upper('" + triggerName + "')";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                exist = true;
            }
        } catch (Exception e) {
        }finally{
            rs.close();
            stmt.close();
        }
        return exist;
    }
    
    /**
     * Ativa ou desativa determinada trigger
     * @param trigger nome da trigger a ser desativa
     * @param active ACTIVE para ativar a trigger e INACTIVE para desativa-la
     */
    public void setActive(String trigger, String active) {
        PreparedStatement pstmt = null;
        try{
            String querie = "ALTER TRIGGER " +trigger+ " "+ active;
            conn = ConexaoATS.getConnection();
            pstmt = conn.prepareStatement(querie);
            pstmt.executeUpdate();
            Logger.getLogger(TriggerController.class).info("Trigger "+ trigger + " ativada/desativada com sucesso.");
        }catch(Exception e){
            Logger.getLogger(TriggerController.class).error("Erro ao ativar/desativar trigger "+ trigger + " "+e);          
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException ex) {
                java.util.logging.Logger.getLogger(TriggerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
}
