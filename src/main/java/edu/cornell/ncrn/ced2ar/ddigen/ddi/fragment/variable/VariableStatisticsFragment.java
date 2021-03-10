package edu.cornell.ncrn.ced2ar.ddigen.ddi.fragment.variable;

import edu.cornell.ncrn.ced2ar.ddigen.ddi.fragment.FragmentWithUrn;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VariableStatisticsFragment extends FragmentWithUrn {

	public static final String NODE_NAME_VARIABLE_STATISTICS = "r:VariableStatistics";
	public static final String NODE_NAME_TOTAL_RESPONSES = "TotalResponses";
	public static final String NODE_NAME_UNFILTERED_CATEGORY_STATISTICS = "UnfilteredCategoryStatistics";

	private int totalResponses;
	private VariableReferenceFragment variableReference;
	private List<SummaryStatistic> summaryStatisticList = new ArrayList<>();
	private List<VariableCategoryFragment> variableCategoryList = new ArrayList<>();

	public VariableStatisticsFragment(String id, String agency, int version, int totalResponses) {
		super(id, agency, version);
		setTotalResponses(totalResponses);
	}

	public void addSummaryStatistic(SummaryStatistic statistic) {
		this.summaryStatisticList.add(statistic);
	}

	public void addVariableCategory(VariableCategoryFragment variableCategory) {
		this.variableCategoryList.add(variableCategory);
	}

	@Override
	public void appendToElement(Element element, Document doc) {
		Element fragment = createFragment(doc);

		Element variableStatistics = doc.createElementNS(NAMESPACE_PHYSICAL_INSTANCE, NODE_NAME_VARIABLE_STATISTICS);
		setVersionDateAttribute(variableStatistics);
		setUniversallyUniqueAttribute(variableStatistics);

		super.appendToElement(variableStatistics, doc);

		Element totalResponses = doc.createElement(NODE_NAME_TOTAL_RESPONSES);
		totalResponses.setTextContent(Integer.toString(getTotalResponses()));
		variableStatistics.appendChild(totalResponses);

		if (getVariableReference() != null) {
			getVariableReference().appendToElement(variableStatistics, doc);
		}

		for (SummaryStatistic statistic : getSummaryStatisticList()) {
			statistic.appendToElement(variableStatistics, doc);
		}

		if (getVariableCategoryList().size() > 0) {
			Element unfilteredCategoryStatistics = doc.createElement(NODE_NAME_UNFILTERED_CATEGORY_STATISTICS);
			for (VariableCategoryFragment variableCategory : getVariableCategoryList()) {
				variableCategory.appendToElement(unfilteredCategoryStatistics, doc);
			}
			variableStatistics.appendChild(unfilteredCategoryStatistics);
		}

		fragment.appendChild(variableStatistics);
		element.appendChild(fragment);
	}

	public int getTotalResponses() {
		return totalResponses;
	}

	public List<SummaryStatistic> getSummaryStatisticList() {
		return summaryStatisticList;
	}

	public List<VariableCategoryFragment> getVariableCategoryList() {
		return variableCategoryList;
	}

	public VariableReferenceFragment getVariableReference() {
		return variableReference;
	}

	public void setTotalResponses(int totalResponses) {
		this.totalResponses = totalResponses;
	}

	public void setSummaryStatisticList(List<SummaryStatistic> summaryStatisticList) {
		this.summaryStatisticList = summaryStatisticList;
	}

	public void setVariableCategoryList(List<VariableCategoryFragment> variableCategoryList) {
		this.variableCategoryList = variableCategoryList;
	}

	public void setVariableReference(VariableReferenceFragment variableReference) {
		this.variableReference = variableReference;
	}
}