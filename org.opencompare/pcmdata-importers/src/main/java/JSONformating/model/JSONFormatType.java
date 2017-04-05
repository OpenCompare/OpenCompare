package JSONformating.model;

public enum JSONFormatType {
	BOOLEAN("boolean"),
	REAL("real"),
	INTEGER("integer"),
	STRING("string"),
	DATE("date"),
	VERSION("version"),
	UNDEFINED("undefined"), // when the value is undefined
	MULTIPLE("multiple"),
	URL("url"),
	IMAGE("image");


	private String name = "";


	JSONFormatType(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}
	
	public static JSONFormatType getType(String str){
		str = str.toLowerCase();
		switch(str){
		case "real" : return REAL;
		case "boolean": return BOOLEAN;
		case "integer":return INTEGER;
		case "string":return STRING;
		case "date":return DATE;
		case "version":return VERSION;
		case "multiple":return MULTIPLE;
		case "url":return URL;
		case "image":return IMAGE;
		default: return UNDEFINED;
		}
	}
}
