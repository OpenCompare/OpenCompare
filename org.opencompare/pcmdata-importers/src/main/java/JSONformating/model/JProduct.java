package JSONformating.model;

import java.util.ArrayList;
import java.util.List;

public class JProduct {
	private String id;
	private List<JCell> cells = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<JCell> getCells() {
		return cells;
	}
	public void setCells(List<JCell> cells) {
		this.cells = cells;
	}
}
