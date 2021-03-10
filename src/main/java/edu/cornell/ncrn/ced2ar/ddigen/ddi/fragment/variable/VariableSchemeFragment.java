package edu.cornell.ncrn.ced2ar.ddigen.ddi.fragment.variable;

import edu.cornell.ncrn.ced2ar.ddigen.ddi.fragment.FragmentWithUrn;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VariableSchemeFragment extends FragmentWithUrn {
	public static final String NODE_NAME_VARIABLE_SCHEME = "VariableScheme";

	private List<VariableReferenceFragment> variableList = new ArrayList<>();

	public VariableSchemeFragment(String id, String agency, int version) {
		super(id, agency, version);
	}

	public void addVariable(VariableReferenceFragment variable) {
		this.variableList.add(variable);
	}

	public List<VariableReferenceFragment> getVariableList() {
		return variableList;
	}

	public void setVariableList(List<VariableReferenceFragment> variableList) {
		this.variableList = variableList;
	}

	@Override
	public void appendToElement(Element element, Document doc) {
		Element fragment = createFragment(doc);

		Element variableScheme = doc.createElementNS(NAMESPACE_LOGICAL_PRODUCT, NODE_NAME_VARIABLE_SCHEME);
		setVersionDateAttribute(variableScheme);
		setUniversallyUniqueAttribute(variableScheme);

		super.appendToElement(variableScheme, doc);

		for (VariableReferenceFragment variable : getVariableList()) {
			variable.appendToElement(variableScheme, doc);
		}

		fragment.appendChild(variableScheme);
		element.appendChild(fragment);
	}
}
