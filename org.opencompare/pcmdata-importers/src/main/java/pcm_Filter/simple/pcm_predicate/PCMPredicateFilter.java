package pcm_Filter.simple.pcm_predicate;

import org.opencompare.model.PCM;

import pcm_InfoContainer.*;


public interface PCMPredicateFilter {
	public boolean isSatisfiable(PCMInfoContainer pcmic);
}
