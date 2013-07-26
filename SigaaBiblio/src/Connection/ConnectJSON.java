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

public class ConnectJSON extends AsyncTask<String, Void, String>{

	private static String HOST = "http://150.165.250.55:8080/SigaaAndroidSupport";
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
	protected String doInBackground(String... params) {
			
		String jsonString = "";
		try {

			// JSONObject pode ser um String, HashMap ou um MBean
			Map<String,String> map = new HashMap<String,String>();
			map.put("Nome",params[0]);
			JSONObject inputsJson = new JSONObject(map); 

			//Parametros: HOST, Identificador do Hash, Hash
			jsonString = HttpUtils.urlContentPost(HOST, "sigaaTesting", inputsJson.toString());
			
					
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}


	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			jsonResult = new JSONObject(result);
			//Captura parametros do Hash resultado;
//			jsonResult.getString(arg1);
//			jsonResult.getDouble(arg2);
//			jsonResult.get(name); //Retorna object
//			...
			
		} catch (JSONException e) {
			//Tratar Error
			e.printStackTrace();
		}finally{
			pd.dismiss();
		}

	}
	
	public JSONObject getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONObject jsonResult) {
		this.jsonResult = jsonResult;
	}

}
