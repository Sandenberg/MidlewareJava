/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.atsinformatica.prestashop.clientDAO;

import java.util.List;

/**
 *
 * @author ricardosilva
 * @param <T>
 */
public interface IGenericPrestashopDAO<T> {
  
    public void post(String path,T t);
    
    public int put(String path,T t);
    
    public List<T> get(String path);
    
    public T getId(String path,int key);
    
    public int delete(String path, String id);
}
