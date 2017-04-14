package pcm_InfoContainer;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;

public class PCMInfoContainer {

	protected StatPcm _statPcm;
	protected PCMContainer _pcm;

	public PCMInfoContainer(PCMContainer pcm) {
		_pcm = pcm;
		preCompute(_pcm.getPcm());
	}

	private void preCompute(PCM pcm) {
		_statPcm = new StatPcm(pcm);
	}

	public Boolean isProductChartable() {
		if (_statPcm.scoreProductChartable() >= 0.5)
			return true;
		else
			return false;
		
	}
	
	public StatPcm getStatPcm(){
		return _statPcm;
	}
	
}
