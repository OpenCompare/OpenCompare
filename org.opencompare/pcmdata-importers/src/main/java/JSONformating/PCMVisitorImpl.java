package JSONformating;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.BooleanValue;
import org.opencompare.api.java.value.Conditional;
import org.opencompare.api.java.value.DateValue;
import org.opencompare.api.java.value.Dimension;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.NotApplicable;
import org.opencompare.api.java.value.NotAvailable;
import org.opencompare.api.java.value.Partial;
import org.opencompare.api.java.value.RealValue;
import org.opencompare.api.java.value.StringValue;
import org.opencompare.api.java.value.Unit;
import org.opencompare.api.java.value.Version;

import JSONformating.model.JSONFormat;




public class PCMVisitorImpl implements PCMVisitor {

	private JSONFormat nJSONf;
	
	public PCMVisitorImpl(JSONFormat nJf) {
		nJSONf = nJf;
	}
	
	@Override
	public void visit(PCM pcm) {
		
		for(Feature f : pcm.getConcreteFeatures()){
			visit(f);
		}
		
		for(Product p : pcm.getProducts()){
			visit(p);
		}
	}

	@Override
	public void visit(Feature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FeatureGroup featureGroup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Product product) {
		for(Cell c : product.getCells()){
			visit(c);
		}

	}

	@Override
	public void visit(Cell cell) {
		//visit(cell.getInterpretation());

	}

	@Override
	public void visit(BooleanValue booleanValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Conditional conditional) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Dimension dimension) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IntegerValue integerValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Multiple multiple) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NotApplicable notApplicable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NotAvailable notAvailable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Partial partial) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RealValue realValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(StringValue stringValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Unit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Version version) {
		// TODO Auto-generated method stub

	}

}
