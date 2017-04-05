package pcm_Export_Mongo;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;

public class PCMInfoContainer {

	private StatPcm _statPcm;
	private PCMContainer _pcm;

	public PCMInfoContainer(PCMContainer pcm) {
		_pcm = pcm;
		preCompute(_pcm.getPcm());
	}

	private void preCompute(PCM pcm) {
		_statPcm = new StatPcm(_pcm.getPcm());

	}

	public Boolean isProductChartable() {
		if (_statPcm.scoreProductChartable() >= 0.5)
			return true;
		else
			return false;
		
	}
	
}
