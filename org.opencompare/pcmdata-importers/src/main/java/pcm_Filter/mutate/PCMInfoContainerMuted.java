package pcm_Filter.mutate;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;

import pcm_InfoContainer.*;

public class PCMInfoContainerMuted extends PCMInfoContainer {

	private StatPcm _statPcmMutate;
	private PCMContainer _pcmMutate;

	public PCMInfoContainerMuted(PCMContainer pcm) {
		super(pcm);
		_pcmMutate.setPcm(PCMMutate.Mutate(pcm.getPcm()));
		preComputeMutate(_pcmMutate.getPcm());
	}

	private void preComputeMutate(PCM pcm) {
		_statPcmMutate = new StatPcm(pcm);
	}

	public Boolean isProductChartable() {
		return _statPcm.scoreProductChartable() >= 0.5;
	}

	public Boolean isProductChartableMutate() {
		return _statPcmMutate.scoreProductChartable() >= 0.5;
	}

	public PCMContainer getPCM() {
		return _pcm;
	}
	
	public PCMContainer getMutedPcm() {
		return _pcmMutate;
	}
	
	public boolean isSameSizePcm(){
		return _pcm.getPcm().getFeatures().size() == _pcmMutate.getPcm().getFeatures().size() && _pcm.getPcm().getProducts().size() == _pcmMutate.getPcm().getProducts().size() ;
	}
}
