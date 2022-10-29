package edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical;

import edu.cornell.ncrn.ced2ar.data.FileFormatInfo;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.ElementWithUrn;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.category.RecordLayoutReference;
import edu.cornell.ncrn.ced2ar.ddigen.DataFileIdentification;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PhysicalInstance extends ElementWithUrn {

	public static final String NODE_NAME_PHYSICAL_INSTANCE = "pi:PhysicalInstance";

	private RecordLayoutReference recordLayoutReference;

	private final DataFileIdentification dataFileIdentification;
	private final GrossFileStructure grossFileStructure;
	private final StatisticalSummary statisticalSummary;

	public PhysicalInstance(String agency, String dataFileUri, String ddiLang, long caseQuantity, StatisticalSummary statisticalSummary, FileFormatInfo.Format dataFormat, String productIdentification) {
		super(agency);
		this.dataFileIdentification = new DataFileIdentification(dataFileUri, "pi");
		this.grossFileStructure = new GrossFileStructure(agency, ddiLang, caseQuantity, dataFormat, productIdentification);
		this.statisticalSummary = statisticalSummary;
	}

	@Override
	public void appendToElement(Element parent, Document doc) {
		Element physicalInstance = doc.createElement(NODE_NAME_PHYSICAL_INSTANCE);
		super.appendToElement(physicalInstance, doc);

		getRecordLayoutReference().appendToElement(physicalInstance, doc);

		dataFileIdentification.appendToElement(physicalInstance, doc);

		grossFileStructure.appendToElement(physicalInstance, doc);

		statisticalSummary.appendToElement(physicalInstance, doc);

		parent.appendChild(physicalInstance);
	}

	public RecordLayoutReference getRecordLayoutReference() {
		return recordLayoutReference;
	}

	public void setRecordLayoutReference(RecordLayoutReference recordLayoutReference) {
		this.recordLayoutReference = recordLayoutReference;
	}
}
