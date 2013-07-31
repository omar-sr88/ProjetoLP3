package Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ConnectJSON extends AsyncTask<String, Void, JSONObject>{
	
	public static String SISTEMA = "http://150.165.250.55:8080/sigaa";
	public static String HOST = SISTEMA+"/public/biblioteca/SigaaAndroidServlet";
	private ProgressDialog pd;
	private Activity act;	
	private JSONObject jsonResult;


	public ConnectJSON(Activity act){
		this.act = act;
	}

	@Override
	protected void onPreExecute() {
		pd = ProgressDialog.show(act,"Aguarde", "Processando...", true, false);
	}

	@Override
	protected JSONObject doInBackground(String... params) {

		String jsonString = "";
		try {

			// JSONObject pode ser um String, HashMap ou um MBean
			Map<String,String> map = new HashMap<String,String>();
			map.put("Login",params[0]);
			map.put("Senha",params[1]);
			JSONObject inputsJson = new JSONObject(map); 

			//Parametros: HOST, Identificador do Hash, Hash
			jsonString = HttpUtils.urlContentPost(HOST, "sigaaLogin", inputsJson.toString());
			
			return jsonResult = new JSONObject(jsonString);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			pd.dismiss();
		}
		return null;
	}


	/*@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		//Captura parametros do Hash resultado;
		pd.dismiss();


	}*/

	public JSONObject getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONObject jsonResult) {
		this.jsonResult = jsonResult;
	}

}
