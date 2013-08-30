package Connection;

import android.content.Context;

public  class OperationsFactory {

	//tipo de persistencia
	public static final int LOCAL=1;
	public static final int REMOTA=2;
	
	public Operations getOperation(int persistencia, Context contexto){
		
		if(persistencia==REMOTA){
			return new JSONOperations();
		}
		else
			if(persistencia==LOCAL){
				return new LocalStorageOperations(contexto);
			}
		return null;
	}
}
