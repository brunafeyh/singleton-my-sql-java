package singleton;

import java.lang.reflect.Field;

import model.Model;
import util.Parameter;

public class SingletonMySQL {
	
	private static SingletonMySQL instance = null;
	
	private SingletonMySQL () {	
	}
	
	public static SingletonMySQL getInstance () {
		if (instance==null) instance = new SingletonMySQL();
		return instance;
	}
	
	public String getColumns ( Model model ) {
		Field[] fields = model.getAttributes ();
		String columns = "", sep="";
		for (Field field : fields) {
			columns += (sep + field.getName());
			sep = ", ";
		}
		return columns;
	}
	
	public String getValues ( Model model ) {
		Field[] fields = model.getAttributes ();
		String columns = "", sep="";
		Object value;
		for (Field field : fields) {
			value = model.get(field.getName());
			if (value==null) {
				columns += (sep + value);
			} else {
				columns += (sep + "'" + value + "'");
			}
			sep = ", ";
		}
		return columns;
	}
	
	public String getColumnsValues ( Model model ) {
		Field[] fields = model.getAttributes ();
		String columns = "", sep="";
		Object value;
		for (Field field : fields) {
			value = model.get(field.getName());
			columns += ( sep +  field.getName() + " = " );
			if (value==null) {
				columns += value;
			} else {
				columns += "'" + value + "'";
			}
			sep = ", ";
		}
		return columns;
	}
	
	public String getWhere ( Parameter... params ) {
		String condition = "", sep="";
		for (Parameter parameter : params) {
			switch (parameter.getOperator()) {
			case EQUAL :
				condition += (sep +
						     parameter.getName() + 
				             " = '" + 
						     parameter.getValue() +
						     "' "
				             );
				break;
			case LIKE :
				condition += (sep +
						     parameter.getName() + 
				             " LIKE '%" + 
						     parameter.getValue() +
						     "%' "
				             );
			}
			sep = " AND ";
		}
		return (condition==""? "" : " WHERE " + condition);
	}
	
	private String getOrder_by ( Model model ) {
		String sql = model.getOrder_by();
		if (sql!="") {
			return " ORDER BY " + sql;
		}
		return "";
	}
	
	public String getSelect ( Model model, Parameter... params ) {
		return "SELECT " + getColumns(model) +
				" FROM " + model.getTable_name() +
				getWhere ( params ) +
				getOrder_by ( model );
	}
	
	public String getInsert ( Model model ) {
		return "INSERT INTO " + model.getTable_name() + 
				" ( " + getColumns(model) + " )" +
				" VALUES ( " + getValues (model) + " )";
	}
	
	public String getUpdate ( Model model ) {
		return "UPDATE " + model.getTable_name() + 
				" SET " + getColumnsValues ( model ) +
				" WHERE " + model.getId_name() + " = " + model.getId_value() ;
	}
	
	public String getDelete ( Model model ) {
		return "DELETE FROM " + model.getTable_name() + 
			   " WHERE " + model.getId_name() + " = " + model.getId_value() ;
	}
	

}
