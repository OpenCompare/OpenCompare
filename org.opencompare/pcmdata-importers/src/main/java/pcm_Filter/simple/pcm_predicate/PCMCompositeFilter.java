package pcm_Filter.simple.pcm_predicate;

import java.util.Collection;
import java.util.LinkedList;

import pcm_Filter.PCMInfoContainer;



public class PCMCompositeFilter implements PCMPredicateFilter {
	
	private Collection<PCMPredicateFilter> filters;

	public PCMCompositeFilter() {
		filters = new LinkedList<>();
	}
	
	@Override
	public boolean isSatisfiable(PCMInfoContainer pcmic) {
		for (PCMPredicateFilter pcmPredicateFilter : filters) {
			if (!pcmPredicateFilter.isSatisfiable(pcmic))
				return false;
		}
		return true;
	}
	
	public void addFilter(PCMPredicateFilter filter){
		filters.add(filter);
	}
	
	// add (AND)
	// add (OR)

}
