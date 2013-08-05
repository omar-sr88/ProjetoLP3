package br.nti.SigaaBiblio.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.nti.SigaaBiblio.R;

public class Prefs extends PreferenceActivity {
  // Option names and default values
  private static final String OPT_LEMBRAR = "lembrar";
  private static final boolean OPT_LEMBRAR_DEF = false;
  private static final String OPT_COR = "cor";
  private static final boolean OPT_COR_DEF = false;
  
  private static final String OPT_HISTORICO = "historico";
  private static final boolean OPT_HISTORICO_DEF = false;
  private static final String OPT_PESQUISA = "pesquisa";
  private static final boolean OPT_PESQUISA_DEF = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.layout.settings);
  }

  /** Get the current value of the Lembrar option */
  public static boolean getLembrar(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
        OPT_LEMBRAR, OPT_LEMBRAR_DEF);
  }

  /** Get the current value of the hints option */
  public static boolean getCor(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
        OPT_COR, OPT_COR_DEF);
  }
  
  /** Get the current value of the Historico Livros option */
  public static boolean gethistorico(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
    		OPT_HISTORICO, OPT_HISTORICO_DEF);
  }
  /** Get the current value of the hints option */
  public static boolean getPesquisa(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
    		OPT_PESQUISA, OPT_PESQUISA_DEF);
  }
}