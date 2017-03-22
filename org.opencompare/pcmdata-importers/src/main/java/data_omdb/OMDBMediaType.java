package data_omdb;

public enum OMDBMediaType {
	
	MOVIE ("movie"), 
	SERIES ("series"),
	EPISODE ("episode");
	
	private final String strMediaType;
	
	private OMDBMediaType(String strMediaType) {
			this.strMediaType = strMediaType;
	}

    @Override
    public String toString() {
        return strMediaType;
    }
}
