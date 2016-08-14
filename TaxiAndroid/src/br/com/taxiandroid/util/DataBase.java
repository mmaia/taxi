package br.com.taxiandroid.util;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.bean.SolicitacaoTaxistaMobile;
import br.com.taxiandroid.bean.UsuarioBean;


public class DataBase {

	   private static final String DATABASE_NAME = "taxiandroid";
	   private static final int DATABASE_VERSION = 1;
	   
	   //TABELAS
	   private static final String TABELA_USUARIO = "usuario";
	   private static final String TABELA_SOLICITACAO = "solicitacaoTaxistaMobile";
	   
	   //Colunas Tabela SOLICITACAO
	   private static final String COLUNA_ID_SOL   			= " id ";
	   private static final String COLUNA_ID_SOLICITACAO   	= " idSolicitacao ";
	   private static final String COLUNA_ORIGEM 			= " origem ";
	   private static final String COLUNA_DESTINO			= " destino ";
	   private static final String COLUNA_DATAHORA 			= " dataHora ";
	   private static final String COLUNA_NUM_PASSAGEIROS 	= " numeroPassageiros ";
	   private static final String COLUNA_INFOR_ADICIONAIS 	= " informacoesAdicionais ";
	   private static final String COLUNA_IS_FINALIZADA 	= " isFinalizada ";
	   private static final String COLUNA_NOME			 	= " nome ";
	   private static final String COLUNA_SOBRENOME		 	= " sobrenome ";
	   private static final String COLUNA_CELULAR 			= " celular ";
	   private static final String COLUNA_VALOR_CORRIDA 	= " valorCorrida ";
	   
	   //Colunas Tabela 
	   
	   private Context context;

	   private SQLiteDatabase db;
	   
	   private static final String DATA_BASES_USUARIO = 
		   " CREATE TABLE IF NOT EXISTS "+DataBase.TABELA_USUARIO+" (id INTEGER PRIMARY KEY, login VARCHAR(60) NULL, senha VARCHAR(30) NULL, idDevice VARCHAR(40), modelo VARCHAR(30)) ";

	   private static final String DATA_BASES_RESERVA = 
		   " CREATE TABLE IF NOT EXISTS "+DataBase.TABELA_SOLICITACAO+" ("+
		   DataBase.COLUNA_ID_SOL+" INTEGER, "+DataBase.COLUNA_ID_SOLICITACAO+" INTEGER, "+
		   DataBase.COLUNA_ORIGEM+" VARCHAR(250) NOT NULL, "+DataBase.COLUNA_DESTINO+" VARCHAR(250), "+
		   DataBase.COLUNA_DATAHORA +" VARCHAR(16), "+DataBase.COLUNA_NUM_PASSAGEIROS +" INTEGER, "+
		   DataBase.COLUNA_INFOR_ADICIONAIS +" VARCHAR(100), "+DataBase.COLUNA_IS_FINALIZADA +" INTEGER,"+
		   DataBase.COLUNA_NOME +" VARCHAR(100), "+DataBase.COLUNA_SOBRENOME +" VARCHAR(100),"+
		   DataBase.COLUNA_CELULAR +" VARCHAR(100), "+ DataBase.COLUNA_VALOR_CORRIDA+" VARCHAR(12)"+
		   ")";

	   
	   public DataBase(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      this.db = openHelper.getWritableDatabase();
//	      this.context.deleteDatabase(DATABASE_NAME);
//	      db.execSQL("DROP TABLE IF EXISTS " + "usuario");
//	      db.execSQL(DATA_BASES_MARCADOR);
	   }
	   
	   
	   /**
	    * Insere usu�rio na base local caso ele n�o existe, sen�o faz atualiza��o
	    * @param usuarioBean
	    * @return long 0 se n�o cadastrou
	    * 			   1 se cadastrou
	    */
	   public long insert_usuario(UsuarioBean usuarioBean) {
		    	ContentValues cv = new ContentValues();
		    	cv.put("login", usuarioBean.getLogin());
		    	cv.put("senha", usuarioBean.getSenha());
		    	cv.put("idDevice", usuarioBean.getIdDevice());
		    	cv.put("modelo", usuarioBean.getModelo());
		    	
		    	UsuarioBean us = selectUsuario(usuarioBean.getId(), usuarioBean.getLogin());
		    	
		    	if(us != null){
		    		return atualizaUsuario(us);
		    	}else{
		    		return db.insert("usuario", null, cv);
		    	}
		   }
	   
	   
	   /**
	    * Insere usu�rio na base local caso ele n�o existe, sen�o faz atualiza��o
	    */
	   public long insert_reserva(SolicitacaoTaxistaMobile solicitacaoTaxistaMobile) {
//		   " CREATE TABLE IF NOT EXISTS "+DataBase.TABELA_SOLICITACAO+" ("+ DataBase.COLUNA_ID_SOL+" INTEGER, "+DataBase.COLUNA_ID_SOLICITACAO+" INTEGER, "+
//		   DataBase.COLUNA_ORIGEM+" VARCHAR(250) NOT NULL, "+DataBase.COLUNA_DESTINO+" VARCHAR(250), "+DataBase.COLUNA_DATAHORA +" VARCHAR(16), "+DataBase.COLUNA_NUM_PASSAGEIROS +" INTEGER, "+
//		   DataBase.COLUNA_INFOR_ADICIONAIS +" VARCHAR(100), "+DataBase.COLUNA_IS_FINALIZADA +" INTEGER,"+DataBase.COLUNA_NOME +" VARCHAR(100), "+DataBase.COLUNA_SOBRENOME +" VARCHAR(100),"+
//		   DataBase.COLUNA_CELULAR +" VARCHAR(100) "+ DataBase.COLUNA_VALOR_CORRIDA+" VARCHAR(12)"+")";
		   
		    	ContentValues cv = new ContentValues();
		    	cv.put(DataBase.COLUNA_ID_SOLICITACAO, solicitacaoTaxistaMobile.getIdSolicitacaoTaxistaMobile());
		    	cv.put(DataBase.COLUNA_ORIGEM, solicitacaoTaxistaMobile.getEndereco());
		    	cv.put(DataBase.COLUNA_DESTINO, solicitacaoTaxistaMobile.getDestino());
		    	cv.put(DataBase.COLUNA_DATAHORA,solicitacaoTaxistaMobile.getData());
		    	cv.put(DataBase.COLUNA_NUM_PASSAGEIROS, solicitacaoTaxistaMobile.getNumPassageiros());
		    	cv.put(DataBase.COLUNA_INFOR_ADICIONAIS, solicitacaoTaxistaMobile.getAdicional());
		    	cv.put(DataBase.COLUNA_IS_FINALIZADA, solicitacaoTaxistaMobile.isCorridaJaFinalizada()?"1":"0");
		    	cv.put(DataBase.COLUNA_NOME, solicitacaoTaxistaMobile.getNome());
		    	cv.put(DataBase.COLUNA_SOBRENOME, solicitacaoTaxistaMobile.getSobreNome());
		    	cv.put(DataBase.COLUNA_CELULAR, solicitacaoTaxistaMobile.getCelular());
		    	cv.put(DataBase.COLUNA_VALOR_CORRIDA, solicitacaoTaxistaMobile.getValorCorrida());
		    	
		    	SolicitacaoTaxistaMobile us = selectSolicitacaoTaxistaMobile(solicitacaoTaxistaMobile.getIdSolicitacaoTaxistaMobile());
		    	long result = -1;
		    	if(us != null){
		    		result = atualizaSolicitacaoTaxistaMobile(us);
		    	}else{
		    		result = db.insert(DataBase.TABELA_SOLICITACAO, null, cv);
		    	}
		    	return result;
		   }
	   
	   
	   public int deleteUsuario(String idUsuario) {
		   	  int res = this.db.delete("usuario", "id = ? ", new String[]{idUsuario}); 
		   		  if(res > 0){
			    	 GlobalTaxi.getGlobalExpress().setLogado(false);
		   		  }
		      return res;
	   }

	   public UsuarioBean selectUsuario(String idUsuario, String login) {
		   String consulta = "";
		   String dadosConsulta[] = null;

		   if(login == null){
			   consulta = "id = ?";
			   dadosConsulta = new String[]{""+idUsuario};
		   }else{
			   consulta = "login = ?";
			   dadosConsulta = new String[]{""+login};
			   
		   }
		   
	        Cursor c = db.query("usuario", null, consulta, dadosConsulta, null, null, "id");
	        if (c.getCount() > 0){
		        	c.moveToFirst();
		        	UsuarioBean bean = new UsuarioBean();
	        		bean.setId(c.getString(0));
	        		bean.setLogin(c.getString(1));
	        		bean.setSenha(c.getString(2));
	        		bean.setIdDevice(c.getString(3));
	        		bean.setModelo(c.getString(4));

	        	return bean;
	        }	        
	        
	        return null;
		}	   

	   public UsuarioBean selectUsuario() {
	        Cursor c = db.query("usuario", null, null, null, null, null, "id DESC");
	        if (c.getCount() > 0){
		        	c.moveToFirst();
		        	UsuarioBean bean = new UsuarioBean();
	        		bean.setId(c.getString(0));
	        		bean.setLogin(c.getString(1));
	        		bean.setSenha(c.getString(2));
	        		bean.setIdDevice(c.getString(3));
	        		bean.setModelo(c.getString(4));

	        	return bean;
	        }	        
	        
	        return null;
		}	   

	   /**
	    * Retorna o último objeto SolicitacaoTaxistaMobile, se o parametro for igual -1 envia o último objeto com status da solicita��o em aberto.
	    * @param id
	    * @return
	    */
	   public SolicitacaoTaxistaMobile selectSolicitacaoTaxistaMobile(long idSolicitacao) {
		   String consulta = DataBase.COLUNA_IS_FINALIZADA+"=0";
		   String dadosConsulta[] = null;

		   if(idSolicitacao > -1){
			   consulta = DataBase.COLUNA_ID_SOLICITACAO+ " = ?";
			   dadosConsulta = new String[]{""+idSolicitacao};
		   }
		   
//		   " CREATE TABLE IF NOT EXISTS "+DataBase.TABELA_SOLICITACAO+" ("+DataBase.COLUNA_ID_SOL+" int, "+DataBase.COLUNA_ID_SOLICITACAO+" long, "+DataBase.COLUNA_ORIGEM+" VARCHAR(250) NOT NULL, "+DataBase.COLUNA_DESTINO+" VARCHAR(250), "+DataBase.COLUNA_DATAHORA +" String, "+DataBase.COLUNA_NUM_PASSAGEIROS +" INTEGER, "+DataBase.COLUNA_INFOR_ADICIONAIS +" VARCHAR(100), "+DataBase.COLUNA_IS_FINALIZADA +" INTEGER))";
	        Cursor c = db.query(DataBase.TABELA_SOLICITACAO, null, consulta, dadosConsulta, null, null, DataBase.COLUNA_ID_SOL+" DESC");
	        if (c.getCount() > 0){
		        	c.moveToFirst();
		        	SolicitacaoTaxistaMobile bean = new SolicitacaoTaxistaMobile();
	        		bean.setId(c.getInt(0));
	        		bean.setIdSolicitacaoTaxistaMobile(c.getInt(1));
	        		bean.setEndereco(c.getString(2));
	        		bean.setDestino(c.getString(3));
	        		bean.setData(c.getString(4));
	        		bean.setNumPassageiros(c.getInt(5));
	        		bean.setAdicional(c.getString(6));
	        		bean.setCorridaJaFinalizada(c.getInt(7)==0?false:true);
	        		bean.setNome(c.getString(8));
	        		bean.setSobreNome(c.getString(9));
	        		bean.setCelular(c.getString(10));
	        		bean.setValorCorrida(c.getString(11));

	        	return bean;
	        }	        
	        return null;
		}	   
	   
		private static class OpenHelper extends SQLiteOpenHelper {
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	      
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	    	  db.execSQL(DATA_BASES_USUARIO);//v 1.0
	    	  db.execSQL(DATA_BASES_RESERVA);//v 1.0
	      }
	      
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//	         Log.w("Examplo", "Upgrading bando de dados, ir� droppar as tabelas e recri�-las");
//	         db.execSQL("DROP TABLE IF EXISTS " + "marcacao");
	         onCreate(db);
	      }
	   }

		public void close(){
	   		if(db != null){
	   			db.close();
	   			db = null;
	   			GlobalTaxi.getGlobalExpress().setDatabase(null);
	   		}
	   	}

	   	/**
	   	 * Atualiza um objeto UsuarioBean 
	   	 * @param usuarioBean
	   	 * @return se > que 0 e porque a atualiza��o ocorreu com sucesso
	   	 */
		public long atualizaUsuario(UsuarioBean usuarioBean) {
	    	ContentValues cv = new ContentValues();
	    	cv.put("login", usuarioBean.getLogin());
	    	cv.put("senha", usuarioBean.getSenha());
	    	cv.put("idDevice", usuarioBean.getIdDevice());
	    	cv.put("modelo", usuarioBean.getModelo());

	    	try {
	           return db.update("usuario", cv, "id = ?", new String[]{""+usuarioBean.getId()});
	        } catch (Exception e) {
	            return 0;
	        }			
		}

	   	/**
	   	 * Atualiza um objeto SolicitacaoTaxistaMobile
	   	 * @param solicitacaoTaxistaMobile
	   	 * @return se > que 0 e porque a atualiza��o ocorreu com sucesso
	   	 */
		public long atualizaSolicitacaoTaxistaMobile(SolicitacaoTaxistaMobile solicitacaoTaxistaMobile) {
	    	ContentValues cv = new ContentValues();
	    	cv.put(DataBase.COLUNA_ID_SOL, solicitacaoTaxistaMobile.getId());
	    	cv.put(DataBase.COLUNA_ID_SOLICITACAO, solicitacaoTaxistaMobile.getIdSolicitacaoTaxistaMobile());
	    	cv.put(DataBase.COLUNA_ORIGEM, solicitacaoTaxistaMobile.getEndereco());
	    	cv.put(DataBase.COLUNA_DESTINO, solicitacaoTaxistaMobile.getDestino());
	    	cv.put(DataBase.COLUNA_DATAHORA,solicitacaoTaxistaMobile.getData());
	    	cv.put(DataBase.COLUNA_NUM_PASSAGEIROS, solicitacaoTaxistaMobile.getNumPassageiros());
	    	cv.put(DataBase.COLUNA_INFOR_ADICIONAIS, solicitacaoTaxistaMobile.getAdicional());
	    	cv.put(DataBase.COLUNA_IS_FINALIZADA, solicitacaoTaxistaMobile.isCorridaJaFinalizada()?"1":"0");
	    	cv.put(DataBase.COLUNA_NOME, solicitacaoTaxistaMobile.getNome());
	    	cv.put(DataBase.COLUNA_SOBRENOME, solicitacaoTaxistaMobile.getSobreNome());
	    	cv.put(DataBase.COLUNA_CELULAR, solicitacaoTaxistaMobile.getCelular());
	    	cv.put(DataBase.COLUNA_VALOR_CORRIDA, solicitacaoTaxistaMobile.getValorCorrida());

	    	try {
	           return db.update(DataBase.TABELA_SOLICITACAO, cv, DataBase.COLUNA_ID_SOLICITACAO+" = ?", new String[]{""+solicitacaoTaxistaMobile.getIdSolicitacaoTaxistaMobile()});
	        } catch (Exception e) {
	            return 0;
	        }			
		}
		
		
	   public Context getContext() {
			return context;
		}
}
