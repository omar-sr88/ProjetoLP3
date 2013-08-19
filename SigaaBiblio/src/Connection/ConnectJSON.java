package Connection;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import br.nti.SigaaBiblio.activities.LoginActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * @author iron
 *
 */
public class ConnectJSON  extends AsyncTask<String, Void, JSONObject>{

	public static String SISTEMA = "http://testes.nti.ufpb.br/sigaa";
//	public static String SISTEMA = "http://150.165.250.55:8080/sigaa";
	public static String HOST = SISTEMA
			+ "/public/biblioteca/SigaaAndroidServlet";
	private ProgressDialog pd;
	private Activity act;
	private JSONObject jsonResult;

	public ConnectJSON(Activity act) {
		this.act = act;
		pd = new ProgressDialog(act);
		pd.setMessage("Processando...");
		pd.setTitle("Aguarde");
		pd.setIndeterminate(false);		
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd.show();
	}


	@Override
	protected JSONObject doInBackground(String... params) {
		
		String jsonString = "";
		try {
			// JSONObject pode ser um String, HashMap ou um MBean
			Map<String, String> map = new HashMap<String, String>();
			map.put("Operacao", String.valueOf(Operations.LOGIN));
			map.put("Login", params[0]);
			map.put("Senha", params[1]);
			JSONObject inputsJson = new JSONObject(map);

			// Parametros: HOST, Identificador do Hash, Hash
			jsonString = HttpUtils.urlContentPost(HOST, "sigaaAndroid", inputsJson.toString());

			return jsonResult = new JSONObject(jsonString);
		} catch (Exception e) {
			try {
				throw new Exception(e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(pd!= null && pd.isShowing())
			pd.dismiss();
	}


	public JSONObject getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONObject jsonResult) {
		this.jsonResult = jsonResult;
	}

	public static String getMd5Hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);

			while (md5.length() < 32)
				md5 = "0" + md5;

			return md5;
		} catch (NoSuchAlgorithmException e) {
			Log.e("MD5", e.getLocalizedMessage());
			return null;
		}
	}

}
