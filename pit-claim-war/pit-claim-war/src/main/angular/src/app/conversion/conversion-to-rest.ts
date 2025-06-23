import {
  vmCalculation
} from "./models";
import {
  ClaimCalculationRsrc as CalculationRsrc,
  ClaimCalculationVariety as CalculationVarietyRsrc  
} from "@cirras/cirras-claims-api";

export function convertToCalculationRsrc(calculation: vmCalculation): CalculationRsrc {
  let ret = {
      '@type': "ClaimCalculationRsrc",
      etag: calculation.etag,

      claimCalculationGuid: calculation.claimCalculationGuid ? calculation.claimCalculationGuid : null,
      claimCalculationGrainQuantityGuid: calculation.claimCalculationGrainQuantityGuid,
      
      // calculation
      calculationVersion: calculation.calculationVersion ? calculation.calculationVersion : null,
      totalClaimAmount: calculation.totalClaimAmount ? calculation.totalClaimAmount : 0,
      calculationStatusCode: calculation.calculationStatusCode ? calculation.calculationStatusCode : null,
      calculationComment: calculation.calculationComment ? calculation.calculationComment : null,
      revisionCount: calculation.revisionCount ? calculation.revisionCount : null,
      calculateIivInd: calculation.calculateIivInd ? calculation.calculateIivInd : null,
      createUser: calculation.createUser ? calculation.createUser : null,
      createDate: calculation.createDate ? calculation.createDate : null,
      updateUser: calculation.updateUser ? calculation.updateUser : null,
      updateDate: calculation.updateDate ? calculation.updateDate : null,
      
      // claim
      claimNumber: calculation.claimNumber ? calculation.claimNumber : null,
      commodityCoverageCode: calculation.commodityCoverageCode ? calculation.commodityCoverageCode : null,
      coverageName: calculation.coverageName ? calculation.coverageName : null,
      cropCommodityId: calculation.cropCommodityId ? calculation.cropCommodityId : null,
      commodityName: calculation.commodityName ? calculation.commodityName : null,
      isPedigreeInd: calculation.isPedigreeInd ? calculation.isPedigreeInd : null,
      primaryPerilCode: calculation.primaryPerilCode ? calculation.primaryPerilCode : null,
      secondaryPerilCode: calculation.secondaryPerilCode ? calculation.secondaryPerilCode : null,
      claimStatusCode: calculation.claimStatusCode ? calculation.claimStatusCode : null,
      claimType: calculation.claimType ? calculation.claimType : null,
      submittedByUserid: calculation.submittedByUserid ? calculation.submittedByUserid : null,
      submittedByName: calculation.submittedByName ? calculation.submittedByName : null,
      submittedByDate: calculation.submittedByDate ? calculation.submittedByDate : null,	
      recommendedByUserid: calculation.recommendedByUserid ? calculation.recommendedByUserid : null,
      recommendedByName: calculation.recommendedByName ? calculation.recommendedByName : null,
      recommendedByDate: calculation.recommendedByDate ? calculation.recommendedByDate : null,
      approvedByUserid: calculation.approvedByUserid ? calculation.approvedByUserid : null,
      approvedByName: calculation.approvedByName ? calculation.approvedByName : null,
      approvedByDate: calculation.approvedByDate ? calculation.approvedByDate : null,
      
      // grower
      growerNumber: calculation.growerNumber ? calculation.growerNumber : null,
      growerName: calculation.growerName ? calculation.growerName : null,
      growerAddressLine1: calculation.growerAddressLine1 ? calculation.growerAddressLine1 : null,
      growerAddressLine2: calculation.growerAddressLine2 ? calculation.growerAddressLine2 : null,
      growerPostalCode: calculation.growerPostalCode ? calculation.growerPostalCode : null,
      growerCity: calculation.growerCity ? calculation.growerCity : null,
      growerProvince: calculation.growerProvince ? calculation.growerProvince : null,
      
      // policy

      hasChequeReqInd: calculation.hasChequeReqInd,
      currentHasChequeReqInd: calculation.currentHasChequeReqInd,

      insurancePlanId: calculation.insurancePlanId ? calculation.insurancePlanId : null,
      insurancePlanName: calculation.insurancePlanName ? calculation.insurancePlanName : null,
      cropYear: calculation.cropYear ? calculation.cropYear : null,
      policyNumber: calculation.policyNumber ? calculation.policyNumber : null,
      contractId: calculation.contractId,
      
      insuredByMeasurementType: calculation.insuredByMeasurementType ? calculation.insuredByMeasurementType : null,

      // linked product / claim / calculation
      linkedProductId: calculation.linkedProductId,
      linkedClaimNumber: calculation.linkedClaimNumber,
      linkedClaimCalculationGuid: calculation.linkedClaimCalculationGuid,
      latestLinkedCalculationVersion: calculation.latestLinkedCalculationVersion,
      latestLinkedClaimCalculationGuid: calculation.latestLinkedClaimCalculationGuid,

      currentClaimStatusCode: calculation.currentClaimStatusCode? calculation.currentClaimStatusCode : null,
      varieties: calculation.varieties ? calculation.varieties.map(item => convertToCalculationVarietiesRsrc(item)) : [],

      claimCalculationBerries: calculation.claimCalculationBerries,
      claimCalculationPlantUnits: calculation.claimCalculationPlantUnits,
      claimCalculationPlantAcres: calculation.claimCalculationPlantAcres,
      claimCalculationGrapes: calculation.claimCalculationGrapes,
      claimCalculationGrainUnseeded: calculation.claimCalculationGrainUnseeded,
      claimCalculationGrainSpotLoss: calculation.claimCalculationGrainSpotLoss,
      claimCalculationGrainQuantity: calculation.claimCalculationGrainQuantity,
      claimCalculationGrainQuantityDetail: calculation.claimCalculationGrainQuantityDetail,        

      type: "ClaimCalculationRsrc"
  };
  return ret;
}

export function convertToCalculationVarietiesRsrc(rsrc: CalculationVarietyRsrc): CalculationVarietyRsrc {
  let ret = {
    // etag: etag,
    claimCalculationVarietyGuid: rsrc.claimCalculationVarietyGuid ? rsrc.claimCalculationVarietyGuid : null,  
    claimCalculationGuid: rsrc.claimCalculationGuid ? rsrc.claimCalculationGuid : null,  
    varietyName: rsrc.varietyName ? rsrc.varietyName : null,  
    cropVarietyId: rsrc.cropVarietyId ? rsrc.cropVarietyId : null,  
    averagePrice: rsrc.averagePrice ? rsrc.averagePrice : null,
    averagePriceOverride : rsrc.averagePriceOverride ? rsrc.averagePriceOverride : null, 
    insurableValue: rsrc.insurableValue ? rsrc.insurableValue : null,
    yieldAssessed: rsrc.yieldAssessed ? rsrc.yieldAssessed : null,
    yieldTotal: rsrc.yieldTotal ? rsrc.yieldTotal : null,
    yieldActual: rsrc.yieldActual ? rsrc.yieldActual : null,
    varietyProductionValue: rsrc.varietyProductionValue ? rsrc.varietyProductionValue : null,  
    yieldAssessedReason: rsrc.yieldAssessedReason ? rsrc.yieldAssessedReason : null,
    isOutOfSyncVarietyRemoved : rsrc.isOutOfSyncVarietyRemoved ? rsrc.isOutOfSyncVarietyRemoved : null,
    isOutOfSyncAvgPrice : rsrc.isOutOfSyncAvgPrice ? rsrc.isOutOfSyncAvgPrice : null
  };
  return ret;
}