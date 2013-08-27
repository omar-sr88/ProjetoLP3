package Connection;

public  class OperationsFactory {

	//tipo de persistencia
	public static final int LOCAL=1;
	public static final int REMOTA=2;
	
	public Operations getOperation(int persistencia){
		
		if(persistencia==REMOTA){
			return new JSONOperations();
		}
		else
			return null;
	}
}
