package pcm_Filter.simple.pcm_predicate;

import org.opencompare.model.PCM;

import pcm_InfoContainer.*;

public class PCMPredicateMinColumnProduct implements PCMPredicateFilter {

	public static final int minimumColumns = 8; // 8 for test on Vo_IPs test
												// pcms

	@Override
	public boolean isSatisfiable(PCMInfoContainer pcmic) {
		if (pcmic.getStatPcm().getNbFeatures() > minimumColumns) {
			return true;
		}
		return false;
	}

}
