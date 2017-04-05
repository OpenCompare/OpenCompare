package JSONformating.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public void addCell(JCell cell){
		this.cells.add(cell);
	}
	
	/**
	 * Compares cells of both products omitting ids
	 * @param p the product to compare
	 * @param featLinks the links between the features of the 2 JSONFormat objects
	 * @return true if cells of both products are the same, omits ids
	 */
	public boolean sameProduct(JProduct p, Map<String, String> featLinks) {
		List<JCell> tempCells = new ArrayList<>(this.cells);
		if(p.getCells().size() != this.cells.size()){
			return false;
		}
		for(JCell pC : p.getCells()){
			for(JCell thisC: this.cells){
				if(thisC.sameCell(pC, featLinks)){
					if(!tempCells.remove(thisC)){
						return false;
					}
				}
			}
		}
//		if(tempCells.isEmpty()){
//			System.out.println(id);
//		}
		return tempCells.isEmpty();
	}
}
