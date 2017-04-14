package pcm_Filter.simple.pcm_predicate;

import org.opencompare.model.PCM;

import pcm_InfoContainer.*;

public class PCMPredicateMinRowProduct implements PCMPredicateFilter {

	public static final int minimumRows = 3;
	
	@Override
	public boolean isSatisfiable(PCMInfoContainer pcmic) {
		if(pcmic.getStatPcm().getNbProducts()>minimumRows){
			return true;
		}
		return false;
	}

}
