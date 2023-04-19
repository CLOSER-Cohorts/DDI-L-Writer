package edu.cornell.ncrn.ced2ar.ddigen.ddi32;

import edu.cornell.ncrn.ced2ar.data.FileFormatInfo;
import edu.cornell.ncrn.ced2ar.ddigen.AbstractSchemaGenerator;
import edu.cornell.ncrn.ced2ar.ddigen.SummaryStatistic;
import edu.cornell.ncrn.ced2ar.ddigen.VariableCategory;
import edu.cornell.ncrn.ced2ar.ddigen.category.Category;
import edu.cornell.ncrn.ced2ar.ddigen.csv.Ced2arVariableStat;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.DDIInstance;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.Label;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.category.CategoryElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.category.CategorySchemeElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.category.RecordLayoutReference;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.logical.DataRelationshipElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.logical.LogicalProductElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.logical.LogicalRecordElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.Purpose;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.ResourcePackageElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.category.CategorySchemeReference;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.code.CodeElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.code.CodeListElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.code.CodeListScheme;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.code.CodeListSchemeReference;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.BasedOnObject;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.GrossRecordStructure;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.PhysicalDataProduct;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.PhysicalInstance;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.PhysicalRecordSegment;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.PhysicalStructure;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.PhysicalStructureScheme;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.physical.StatisticalSummary;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.DataItem;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.ProprietaryInfo;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.ProprietaryProperty;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.RecordLayout;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.RecordLayoutScheme;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.record.VariableSchemeReference;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.CodeVariableRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.DateTimeVariableRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.NumericVariableRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.TextVariableRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.VariableElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.VariableSchemeElement;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.VariableStatistics;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.VariableUsedReference;
import edu.cornell.ncrn.ced2ar.ddigen.ddi32.element.variable.VariablesInRecordElement;
import edu.cornell.ncrn.ced2ar.ddigen.category.CategoryScheme;
import edu.cornell.ncrn.ced2ar.ddigen.code.Code;
import edu.cornell.ncrn.ced2ar.ddigen.code.CodeList;
import edu.cornell.ncrn.ced2ar.ddigen.ddi33.fragment.variable.StatisticType;
import edu.cornell.ncrn.ced2ar.ddigen.representation.CodeRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.representation.DateTimeRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.representation.NumericRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.representation.TextRepresentation;
import edu.cornell.ncrn.ced2ar.ddigen.variable.Variable;
import edu.cornell.ncrn.ced2ar.ddigen.variable.VariableScheme;
import org.apache.commons.math3.stat.Frequency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ElementGenerator extends AbstractSchemaGenerator {

	public ElementGenerator(
		List<CategoryScheme> categorySchemeList,
		List<CodeList> codeListList,
		List<VariableScheme> variableSchemeList,
		List<Ced2arVariableStat> variableStatistics,
		String statistics,
		Map<String, String> excludeVariableToStatMap,
		String agency,
		String ddiLanguage,
		String title,
		FileFormatInfo.Format dataFormat,
		String productIdentification
	) {
		super(
			categorySchemeList,
			codeListList,
			variableSchemeList,
			variableStatistics,
			statistics,
			excludeVariableToStatMap,
			agency,
			ddiLanguage,
			title,
			dataFormat,
			productIdentification
		);
	}

	protected DataRelationshipElement getDataRelationshipElement(Map<String, UUID> variableIdToUuidMap) {
		DataRelationshipElement dataRelationshipElement = new DataRelationshipElement(getAgency());
		dataRelationshipElement.setLogicalRecord(getLogicalRecord(variableIdToUuidMap));
		return dataRelationshipElement;
	}

	public DDIInstance getInstance() {
		Map<String, UUID> categoryIdToUuidMap = getCategoryIdToUuidMap();
		Map<String, UUID> categorySchemeIdToUuidMap = getCategorySchemeIdToUuidMap();
		Map<String, UUID> codeListIdToUuidMap = getCodeListIdToUuidMap();
		Map<String, UUID> variableIdToUuidMap = getVariableIdToUuidMap();
		Map<String, UUID> variableSchemeIdToUuidMap = getVariableSchemeIdToUuidMap();

		DDIInstance ddiInstance = new DDIInstance(getAgency());

		// Resource package
		ResourcePackageElement resourcePackage = new ResourcePackageElement(getAgency());

		// Purpose
		resourcePackage.setPurpose(new Purpose());

		// Logical Product
		resourcePackage.setLogicalProduct(
			getLogicalProduct(
				categorySchemeIdToUuidMap,
				codeListIdToUuidMap,
				variableIdToUuidMap,
				variableSchemeIdToUuidMap
			)
		);

		UUID physicalRecordSegmentId = UUID.randomUUID();

		// Physical Data Product
		PhysicalDataProduct physicalDataProduct = getPhysicalDataProduct(physicalRecordSegmentId);

		Map.Entry<String,UUID> entry = variableSchemeIdToUuidMap.entrySet().iterator().next();
		UUID variableSchemeId = entry.getValue();
		UUID recordLayoutId = UUID.randomUUID();
		physicalDataProduct.setRecordLayoutScheme(
			getRecordLayoutScheme(recordLayoutId, variableSchemeId, variableIdToUuidMap, physicalRecordSegmentId)
		);
		resourcePackage.setPhysicalDataProduct(physicalDataProduct);

		// Physical Instance
		PhysicalInstance physicalInstance = getPhysicalInstance(variableIdToUuidMap);
		physicalInstance.setRecordLayoutReference(new RecordLayoutReference(recordLayoutId.toString(), getAgency()));
		resourcePackage.setPhysicalInstance(physicalInstance);

		// Category Scheme
		for (CategoryScheme categoryScheme : getCategorySchemeList()) {
			UUID categorySchemeId = categorySchemeIdToUuidMap.get(categoryScheme.getId());
			CategorySchemeElement categorySchemeElement = new CategorySchemeElement(categorySchemeId.toString(), getAgency());
			categorySchemeElement.setName(categoryScheme.getId());
			for (Category category : categoryScheme.getCategoryList()) {
				UUID categoryId = categoryIdToUuidMap.get(category.getId());
				CategoryElement categoryElement = new CategoryElement(categoryId.toString(), getAgency());
				categoryElement.setLabel(new Label(category.getLabel(), getDdiLanguage()));
				categorySchemeElement.addCategory(categoryElement);
			}
			resourcePackage.addCategoryScheme(categorySchemeElement);
		}

		// Code List Schemes
		CodeListScheme codeListScheme = new CodeListScheme(getAgency());
		for (CodeList codeList : getCodeListList()) {
			UUID id = codeListIdToUuidMap.get(codeList.getId());
			CodeListElement codeListElement = new CodeListElement(id.toString(), getAgency());
			codeListElement.setName(codeList.getLabel());

			for (Code code : codeList.getCodeList()) {
				CodeElement codeElement = new CodeElement(getAgency(), code.getValue());
				UUID categoryId = categoryIdToUuidMap.get(code.getCategoryId());
				codeElement.setCategoryReference(categoryId.toString());
				codeListElement.addCode(codeElement);
			}
			codeListScheme.addCodeList(codeListElement);
		}
		resourcePackage.setCodeListScheme(codeListScheme);

		// Variable Scheme
		List<VariableSchemeElement> variableSchemeElementList = getVariableSchemeElementList(
			variableIdToUuidMap,
			variableSchemeIdToUuidMap,
			codeListIdToUuidMap
		);

		for (VariableSchemeElement variableSchemeElement : variableSchemeElementList) {
			resourcePackage.addVariableScheme(variableSchemeElement);
		}

		ddiInstance.setResourcePackage(resourcePackage);

		return ddiInstance;
	}

	protected LogicalProductElement getLogicalProduct(
		Map<String, UUID> categorySchemeIdToUuidMap,
		Map<String, UUID> codeListIdToUuidMap,
		Map<String, UUID> variableIdToUuidMap,
		Map<String, UUID> variableSchemeIdToUuidMap
	) {
		LogicalProductElement logicalProduct = new LogicalProductElement(getAgency());

		// Data Relationship
		DataRelationshipElement dataRelationshipElement = getDataRelationshipElement(variableIdToUuidMap);
		logicalProduct.setDataRelationship(dataRelationshipElement);

		// Category Schemes
		for (Map.Entry<String, UUID> entry : categorySchemeIdToUuidMap.entrySet()) {
			UUID uuid = entry.getValue();
			CategorySchemeReference categorySchemeReference = new CategorySchemeReference(uuid.toString(), getAgency());
			logicalProduct.addCategorySchemeReference(categorySchemeReference);
		}

		// Code List Schemes
		for (Map.Entry<String, UUID> entry : codeListIdToUuidMap.entrySet()) {
			UUID uuid = entry.getValue();
			CodeListSchemeReference codeListSchemeReference = new CodeListSchemeReference(uuid.toString(), getAgency());
			logicalProduct.addCodeListSchemeReference(codeListSchemeReference);
		}

		// Variable Schemes
		for (Map.Entry<String, UUID> entry : variableSchemeIdToUuidMap.entrySet()) {
			UUID uuid = entry.getValue();
			VariableSchemeReference variableSchemeReference = new VariableSchemeReference(uuid.toString(), getAgency());
			logicalProduct.addVariableSchemeReference(variableSchemeReference);
		}

		return logicalProduct;
	}

	protected LogicalRecordElement getLogicalRecord(Map<String, UUID> variableIdToUuidMap) {
		LogicalRecordElement logicalRecord = new LogicalRecordElement(getAgency());

		VariablesInRecordElement variablesInRecord = new VariablesInRecordElement();
		for (VariableScheme variableScheme : getVariableSchemeList()) {
			for (Variable variable : variableScheme.getVariableList()) {
				if (variable.getId() != null) {
					UUID id = variableIdToUuidMap.get(variable.getId());
					VariableUsedReference variableUsedReference = new VariableUsedReference(id.toString(), getAgency());
					variablesInRecord.addReference(variableUsedReference);
				}
			}
		}
		logicalRecord.setVariablesInRecord(variablesInRecord);

		logicalRecord.setLogicalProductName(getTitle());

		return logicalRecord;
	}

	protected PhysicalDataProduct getPhysicalDataProduct(UUID physicalRecordSegmentId) {
		PhysicalDataProduct physicalDataProduct = new PhysicalDataProduct(getAgency());

		PhysicalStructureScheme physicalStructureScheme = new PhysicalStructureScheme(getAgency());

		PhysicalStructure physicalStructure = new PhysicalStructure(getAgency());

		BasedOnObject basedOnObject = new BasedOnObject(getAgency());

		basedOnObject.setBasedOnReference(UUID.randomUUID().toString());

		physicalStructure.setBasedOnObject(basedOnObject);

		// Gross Record Structure
		GrossRecordStructure grossRecordStructure = new GrossRecordStructure(getAgency());

		grossRecordStructure.setLogicalRecordReference(UUID.randomUUID().toString());
		grossRecordStructure.setPhysicalRecordSegment(new PhysicalRecordSegment(physicalRecordSegmentId.toString(), getAgency()));

		physicalStructure.setGrossRecordStructure(grossRecordStructure);

		physicalStructureScheme.setPhysicalStructure(physicalStructure);

		physicalDataProduct.setPhysicalStructureScheme(physicalStructureScheme);

		return physicalDataProduct;
	}

	protected PhysicalInstance getPhysicalInstance(Map<String, UUID> variableIdToUuidMap) {
		StatisticalSummary statisticalSummary = new StatisticalSummary();

		for (VariableScheme variableScheme : getVariableSchemeList()) {
			for (Variable variable : variableScheme.getVariableList()) {
				if (variable.getId() == null) {
					continue;
				}

				UUID variableId = variableIdToUuidMap.get(variable.getId());
				VariableStatistics variableStatistics = new VariableStatistics(getAgency(), variableId.toString());

				List<String> statisticList = new ArrayList<>();
				if (getStatistics() != null && !getStatistics().trim().isEmpty()) {
					String[] statisticArray = getStatistics().split(",");
					statisticList.addAll(Arrays.asList(statisticArray));
				}

				for (Ced2arVariableStat variableStat : getVariableStatisticList()) {
					if (variableStat.getName() != null && variable.getName() != null && variableStat.getName().equalsIgnoreCase(variable.getName())) {
						String excludeVariableStat = getExcludeVariableToStatMap().get(variable.getName());

						boolean excludeValid = !statisticList.isEmpty() && !statisticList.contains("valid");
						boolean excludeInvalid = !statisticList.isEmpty() && !statisticList.contains("invalid");
						boolean excludeMin = !statisticList.isEmpty() && !statisticList.contains("min");
						boolean excludeMax = !statisticList.isEmpty() && !statisticList.contains("max");
						boolean excludeMean = !statisticList.isEmpty() && !statisticList.contains("mean");
						boolean excludeStdDev = !statisticList.isEmpty() && !statisticList.contains("stdev");
						boolean excludeFrequency = !statisticList.isEmpty() && !statisticList.contains("freq");

						if (excludeVariableStat != null) {
							String[] excludeVariableStatArray = excludeVariableStat.split(":");

							if (excludeVariableStatArray.length > 0 && !excludeVariableStatArray[0].isEmpty()) {
								List<String> excludeVariableStatList = Arrays.asList(excludeVariableStatArray[0].split(","));
								if (!excludeValid) {
									excludeValid = excludeVariableStatList.contains("valid");
								}
								if (!excludeInvalid) {
									excludeInvalid = excludeVariableStatList.contains("invalid");
								}
								if (!excludeMin) {
									excludeMin = excludeVariableStatList.contains("min");
								}
								if (!excludeMax) {
									excludeMax = excludeVariableStatList.contains("max");
								}
								if (!excludeMean) {
									excludeMean = excludeVariableStatList.contains("mean");
								}
								if (!excludeStdDev) {
									excludeStdDev = excludeVariableStatList.contains("stdev");
								}
								if (!excludeFrequency) {
									excludeFrequency = excludeVariableStatList.contains("freq");
								}
							}
						}

						Long validCount = variableStat.getValidCount();
						if (!excludeValid && validCount != null) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(Long.toString(validCount), StatisticType.VALID_CASES, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						Long invalidCount = variableStat.getInvalidCount();
						if (!excludeInvalid && invalidCount != null) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(Long.toString(invalidCount), StatisticType.INVALID_CASES, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						String min = variableStat.getMinFormatted();
						if (!excludeMin && min != null && !min.isEmpty()) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(min, StatisticType.MINIMUM, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						String max = variableStat.getMaxFormatted();
						if (!excludeMax && max != null && !max.isEmpty()) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(max, StatisticType.MAXIMUM, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						String mean = variableStat.getMeanFormatted();
						if (!excludeMean && mean != null && !mean.isEmpty()) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(mean, StatisticType.MEAN, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						String stdDeviation = variableStat.getStdDeviationFormatted();
						if (!excludeStdDev && stdDeviation != null && !stdDeviation.isEmpty()) {
							SummaryStatistic summaryStatistic = new SummaryStatistic(stdDeviation, StatisticType.STANDARD_DEVIATION, "pi");
							variableStatistics.addSummaryStatistic(summaryStatistic);
						}

						if (!excludeFrequency && getVariableToFrequencyMap() != null) {

							if (variable.getRepresentation() instanceof CodeRepresentation) {
								Frequency variableFrequency = getVariableToFrequencyMap().get(variable.getName());
								CodeRepresentation representation = (CodeRepresentation) variable.getRepresentation();
								for (CodeList codeList : getCodeListList()) {
									if (representation.getCodeSchemeId().equalsIgnoreCase(codeList.getId())) {
										long invalidValueFrequency = variableFrequency.getCount(".");
										if (invalidValueFrequency > 0) {
											VariableCategory variableCategory = new VariableCategory(".", Long.toString(invalidValueFrequency), "pi");
											variableStatistics.addVariableCategory(variableCategory);
										}
										for (Code code : codeList.getCodeList()) {
											long frequency = variableFrequency.getCount(code.getValue());
											if (frequency > 0) {
												VariableCategory variableCategory = new VariableCategory(
														code.getValue(),
														Long.toString(frequency)
												);
												variableStatistics.addVariableCategory(variableCategory);
											}
										}
									}
								}
							}
						}
						break;
					}
				}
				statisticalSummary.addVariableStatistics(variableStatistics);
			}
		}

		return new PhysicalInstance(getAgency(), getTitle(), getDdiLanguage(), 10, statisticalSummary, getDataFormat(), getProductIdentification());
	}

	protected RecordLayoutScheme getRecordLayoutScheme(UUID recordLayoutId, UUID variableSchemeId, Map<String, UUID> variableIdToUuidMap, UUID physicalRecordSegmentId) {
		RecordLayoutScheme recordLayoutScheme = new RecordLayoutScheme(getAgency());

		//FileFormatInfo.Format dataType = getTitle().toLowerCase().endsWith(".dta") ? "STATA" : "SPSS";
		RecordLayout recordLayout = new RecordLayout(recordLayoutId.toString(), getAgency(), variableSchemeId.toString(), getDdiLanguage(), getDataFormat(), getProductIdentification());

		// Physical Structure Link Reference
		recordLayout.setReference(physicalRecordSegmentId.toString());

		// Data List Item
		for (Map.Entry<String, UUID> entry : variableIdToUuidMap.entrySet()) {
			DataItem dataItem = new DataItem();

			dataItem.setReference(entry.getValue().toString(), getAgency());

			ProprietaryInfo proprietaryInfo = new ProprietaryInfo();
			proprietaryInfo.addProprietaryProperty(new ProprietaryProperty("Width", "???"));
			proprietaryInfo.addProprietaryProperty(new ProprietaryProperty("Decimals", "???"));
			proprietaryInfo.addProprietaryProperty(new ProprietaryProperty("WriteFormatType", "???"));
			dataItem.setProprietaryInfo(proprietaryInfo);

			recordLayout.addDataItem(dataItem);
		}

		recordLayoutScheme.setRecordLayout(recordLayout);

		return recordLayoutScheme;
	}

	protected List<VariableSchemeElement> getVariableSchemeElementList(Map<String, UUID> variableIdToUuidMap, Map<String, UUID> variableSchemeIdToUuidMap, Map<String, UUID> codeListIdToUuidMap) {
		List<VariableSchemeElement> variableSchemeElementList = new ArrayList<>();

		for (VariableScheme variableScheme : getVariableSchemeList()) {
			UUID variableSchemeId = variableSchemeIdToUuidMap.get(variableScheme.getId());

			VariableSchemeElement variableSchemeElement = new VariableSchemeElement(variableSchemeId.toString(), getAgency());
			variableSchemeElement.setVariableSchemeName(getTitle());

			for (Variable variable : variableScheme.getVariableList()) {
				UUID variableId = variableIdToUuidMap.get(variable.getId());
				VariableElement variableElement = new VariableElement(variableId.toString(), getAgency());
				variableElement.setName(variable.getName());

				Ced2arVariableStat variableStat = getVariableStatisticList()
						.stream()
						.filter(variableStatistic -> variableStatistic.getName().equalsIgnoreCase(variable.getName()))
						.findFirst()
						.orElse(null);

				String labelContent = variableStat != null ? variableStat.getLabel() : variable.getLabel();

				variableElement.setLabel(labelContent, getDdiLanguage());

				if (variable.getRepresentation() instanceof NumericRepresentation) {
					NumericRepresentation representation = (NumericRepresentation) variable.getRepresentation();

					variableElement.setVariableRepresentation(
						new NumericVariableRepresentation(representation.getType())
					);
				} else if (variable.getRepresentation() instanceof TextRepresentation) {
					variableElement.setVariableRepresentation(
						new TextVariableRepresentation("text")
					);
				} else if (variable.getRepresentation() instanceof DateTimeRepresentation) {
					DateTimeRepresentation representation = (DateTimeRepresentation) variable.getRepresentation();

					variableElement.setVariableRepresentation(
						new DateTimeVariableRepresentation(representation.getType())
					);
				} else if (variable.getRepresentation() instanceof CodeRepresentation) {
					CodeRepresentation representation = (CodeRepresentation) variable.getRepresentation();

					UUID codeSchemeId = codeListIdToUuidMap.get(representation.getCodeSchemeId());

					CodeVariableRepresentation codeVariableRepresentation = new CodeVariableRepresentation("type");
					codeVariableRepresentation.setReferenceElement(codeSchemeId.toString(), getAgency());
					variableElement.setVariableRepresentation(codeVariableRepresentation);
				}

				variableSchemeElement.addVariableElement(variableElement);
			}
			variableSchemeElementList.add(variableSchemeElement);
		}

		return variableSchemeElementList;
	}
}