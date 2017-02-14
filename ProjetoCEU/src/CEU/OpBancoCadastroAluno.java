/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CEU;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Servidor
 */
public class OpBancoCadastroAluno {
    //objeto responsavel por armazenar a conexao com o BD
    Connection con=null;
    
    //ao construir a classe será carregado o driver de conexão
    public OpBancoCadastroAluno(){
        this.carregarDriver();
    }
    
    public void carregarDriver(){
        try{
            //carrega o drive
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver carregado");
        } catch (ClassNotFoundException ex) {
            //se não carregar
            System.out.println("O driver MySQL não" + " pode ser carregado. Erro: " + ex);
        }
    }//Fim do metodo carregar drive
    
    public Connection obterConexao() {
        try{
            //verifica se conexao fechada ou inexistente
            //abre a conexao
            if((con==null)||(con.isClosed())){
                con = DriverManager.getConnection("jdbc:mysql://localhost/bd_projetoceu","root","");
                System.out.println("Conexao obtida com sucesso");
                }
            } catch (SQLException ex) {
                    System.out.println("SQLException: "+ ex);
            } catch (Exception ex){
                    System.out.println("Exception: "+ex);
            }
    return con;//retorna uma conexao onde serão feitas as conexaos de operações com o BD
    }//fim do metodo obter conexao
    
    public void fecharConexao(){
        try{                    //! significa não: con.isClosed? se true entao vira false
            if((con!=null) && (!con.isClosed())){
                con.close();
                System.out.println("Conexão fechada");
            }
        } catch (SQLException ex){
            System.out.println("SQLException:" + ex);
        } catch (Exception ex){
            System.out.println("Exception: "+ ex);
        }
    }//Fim do método fechar conexão
    
    public void incluirAluno (Aluno a) {
        Connection conexao =  this.obterConexao();
        PreparedStatement pre = null;
        try {
            String sql = "Insert into aluno(nome,endereco,bairro,rg,cpf,fone,curso,instensino,cidade) values (?,?,?,?,?,?,?,?,?)";
            
            pre = conexao.prepareStatement(sql);
            pre.setString(1, a.getNome());
            pre.setString(2, a.getEndereço());
            pre.setString(3, a.getBairro());
            pre.setString(4, a.getRg());//int
            pre.setString(5, a.getCpf());//int
            pre.setString(6, a.getFone());//int
            pre.setString(7, a.getCurso());
            pre.setString(8, a.getInstEnsino());
            pre.setString(9, a.getCidade());
            
            pre.executeUpdate();
            System.out.println("Inclusão Realizada! "+a.getNome());
            JOptionPane.showMessageDialog(null, "Inclusão Realizada!");
        } catch (SQLException b){
            System.out.println("Erro ao incluir "+b.getMessage());
            JOptionPane.showMessageDialog(null, "Falha na inclusão");
        }
    }//Fim do metodo
    
    public void AlteraAluno (Aluno a){
        Connection conexao = this.obterConexao();
        PreparedStatement pre = null;
        try {
            String sql = "update aluno set nome=?,endereco=?,bairro=?,rg=?,cpf=?,fone=?,curso=?,instensino=?,cidade=? where codigo=?";      
            pre = conexao.prepareStatement(sql);
            pre.setString(1, a.getNome());
            pre.setString(2, a.getEndereço());
            pre.setString(3, a.getBairro());
            pre.setString(4, a.getRg());//int
            pre.setString(5, a.getCpf());//int
            pre.setString(6, a.getFone());//int
            pre.setString(7, a.getCurso());
            pre.setString(8, a.getInstEnsino());
            pre.setString(9, a.getCidade());
            pre.setString(10, a.getCodigo());
            pre.executeUpdate();
            System.out.println("Alteração Realizada");
        }catch(SQLException b){
            System.out.println("Erro ao Alterar "+b.getMessage());
        }//fim do catch
    }//Fim do metodo
    
    
    
    public void excluirAlunos(String c){
        Connection conexao = this.obterConexao();
        PreparedStatement pre = null;
        try {
            String sql ="delete from aluno where codigo=?";
            pre = conexao.prepareStatement(sql);
            pre.setString(1, c);
            pre.executeUpdate();
            JOptionPane.showMessageDialog(null, "Excluido com sucesso");
        }catch(SQLException e){
            System.out.println("Erro ao excluir: "+ e.getMessage());
        }//fim do catch
    }//fim do método
    
   public ResultSet listarAlunos(){
       Statement st;
       ResultSet rs=null;//Representação na memoria de uma tabela
       try{
           Connection conexao = this.obterConexao();
       st=conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       //TYPE_SROLL_SENSITIVE: possibilidade de acessar os registros
       //CONCUR_UPDATABLE: alterar os valores contidos no rs
       
       rs=st.executeQuery("Select * from aluno order by nome");

       }catch(Exception e){
           System.out.println("oi"+e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
   
   public ResultSet listarAlunosPorCidade(){
       Statement st;
       ResultSet rs=null;//Representação na memoria de uma tabela
       try{
           Connection conexao = this.obterConexao();
       st=conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       //TYPE_SROLL_SENSITIVE: possibilidade de acessar os registros
       //CONCUR_UPDATABLE: alterar os valores contidos no rs
       
       rs=st.executeQuery("Select * from aluno order by cidade");

       }catch(Exception e){
           System.out.println(e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
   
   public ResultSet executarQuery(String query){
       Statement st;
       ResultSet rs=null;//Representação na memoria de uma tabela
       try{
           Connection conexao = this.obterConexao();
           st=conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           rs = st.executeQuery(query);
       }catch(Exception e){
           System.out.println(e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
    
   public ResultSet buscarAlunoPorCidade(String cidade){
       PreparedStatement st;
       ResultSet rs = null;
       try{
           Connection conexao = this.obterConexao();
           st = conexao.prepareStatement("select * from aluno where cidade like ? order by nome");
                st.setString(1, cidade + '%');
                rs = st.executeQuery();
                System.out.println("\nAluno Buscado");
                
       }catch(Exception e){
           System.out.println("Erro: "+ e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
   
   public ResultSet buscarAlunoPorNome(String nome){
       PreparedStatement st;
       ResultSet rs = null;
       try{
           Connection conexao = this.obterConexao();
           st = conexao.prepareStatement("select * from aluno where nome like ? order by nome");
                st.setString(1, nome + '%');
                rs = st.executeQuery();
                System.out.println("\naluno buscado");
                
       }catch(Exception e){
           System.out.println("Erro: "+ e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
   
   public ResultSet buscarAlunoPorInst(String inst){
       PreparedStatement st;
       ResultSet rs = null;
       try{
           Connection conexao = this.obterConexao();
           st = conexao.prepareStatement("select * from aluno where instensino like ? order by nome");
                st.setString(1, inst + '%');
                rs = st.executeQuery();
                System.out.println("\naluno buscado");
                
       }catch(Exception e){
           System.out.println("Erro: "+ e.getMessage());
       }//fim do catch
       return rs;
   }//fim do método
   
   
   public ResultSet verCpf(String cpf){
       PreparedStatement st;
       ResultSet rs = null;
       try{
           Connection conexao = this.obterConexao();
           st = conexao.prepareStatement("select * from aluno where cpf in(select b.cpf from aluno b group by b.cpf"
                   + " having count(*)>1) order by cpf, nome");
                st.setString(1, cpf + '%');
                rs = st.executeQuery();
                System.out.println("\nCpf Buscado");
                
       }catch(Exception e){
           System.out.println("Erro: "+ e.getMessage());
       }//fim do catch
       return rs;
   }
   
}
