import { HttpErrorResponse } from "@angular/common/http";
import {ERROR_TYPE, ErrorState} from "../store/application/application.state";
import {UUID} from "angular2-uuid";
import {
  vmClaim,
  vmClaimList,
  vmCalculation,
  vmCalculationList  
} from "./models";
import {
  ClaimRsrc as ClaimRsrc,
  ClaimListRsrc as ClaimListRsrc,
  ClaimCalculationRsrc as CalculationRsrc,
  ClaimCalculationListRsrc as CalculationListRsrc
} from "@cirras/cirras-claims-api/model/models";
import {
  getCodeOptions,
  getDescriptionForCode,
} from "../utils/code-table-utils";

const EMPTY_ARRAY = [];

export function convertToErrorState(error: Error, resourceName?: string): ErrorState {
    if (!error) {
        return null;
    }
    if (error instanceof HttpErrorResponse || error.name == "HttpErrorResponse") {
        let err = error as HttpErrorResponse;
        if (err.status == 404) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.NOT_FOUND,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `${resourceName} not found` : err.message,
                name: err.name,
                responseEtag: err.headers.get("ETag"),
            };
        }
        if (err.status == 412) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.FAILED_PRECONDITION,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `${resourceName} has changed since last retrieve` : err.message,
                name: err.name,
                responseEtag: err.headers.get("ETag"),
            };
        }

        if (err.status >= 500 || err.status == 0) {
            return {
                uuid: UUID.UUID(),
                type: ERROR_TYPE.FATAL,
                status: err.status,
                statusText: err.statusText,
                message: resourceName ? `Unexpected error performing operation on ${resourceName}` : err.message,
                name: err.name,
                responseEtag: undefined,
            };
        }
        return {            
            uuid: UUID.UUID(),
            type: err.status == 400 ? ERROR_TYPE.VALIDATION : ERROR_TYPE.FATAL,
            status: err.status,
            statusText: err.statusText,
            message: err.status == 400 ? "Validation Error" : err.message,
            name: err.name,
            validationErrors: err.error.messages,
            responseEtag: err.headers.get("ETag"),
        };
    } else {
        throw error;
    }

}

export function convertToClaimList(claimListRsrc: CalculationListRsrc): vmClaimList {
  if (!claimListRsrc) {
      return {collection: EMPTY_ARRAY};
  }
  return {
      collection: claimListRsrc.collection && claimListRsrc.collection.length > 0 ? claimListRsrc.collection.map(item => convertToClaim(item, null)) : EMPTY_ARRAY,
      pageNumber: claimListRsrc.pageNumber ? claimListRsrc.pageNumber : null,
      pageRowCount: claimListRsrc.pageRowCount ? claimListRsrc.pageRowCount : null,
      totalPageCount: claimListRsrc.totalPageCount ? claimListRsrc.totalPageCount : null,
      totalRowCount: claimListRsrc.totalRowCount ? claimListRsrc.totalRowCount : null
  };
}

export function convertToCalculationList(calculationListRsrc: CalculationListRsrc): vmCalculationList {
  if (!calculationListRsrc) {
      return {collection: EMPTY_ARRAY};
  }
  return {
      collection: calculationListRsrc.collection && calculationListRsrc.collection.length > 0 ? calculationListRsrc.collection.map(item => convertToCalculation(item, null)) : EMPTY_ARRAY,
      pageNumber: calculationListRsrc.pageNumber ? calculationListRsrc.pageNumber : null,
      pageRowCount: calculationListRsrc.pageRowCount ? calculationListRsrc.pageRowCount : null,
      totalPageCount: calculationListRsrc.totalPageCount ? calculationListRsrc.totalPageCount : null,
      totalRowCount: calculationListRsrc.totalRowCount ? calculationListRsrc.totalRowCount : null
  };
}

export function convertToClaim(claimRes: CalculationRsrc, etag?: string): vmClaim {
  let claim = <CalculationRsrc>claimRes;
  let ret: vmClaim = {
      etag: etag,

      claimCalculationGuid: claim.claimCalculationGuid,
      // grower
      growerNumber: claim.growerNumber,
      growerName: claim.growerName,
      growerAddressLine1: claim.growerAddressLine1,
      growerAddressLine2: claim.growerAddressLine2,
      growerPostalCode: claim.growerPostalCode,
      // policy
      insurancePlanName: claim.insurancePlanName, 
      cropYear: claim.cropYear,
      policyNumber: claim.policyNumber,
      // coverage
      coverageName: claim.coverageName,  
      commodityName: claim.commodityName,  

      // claim
      claimNumber: claim.claimNumber,
      calculationVersion: claim.calculationVersion,   
      calculationStatusCode: claim.calculationStatusCode,
      claimType: claim.claimType,
      primaryPerilCode: claim.primaryPerilCode,
      secondaryPerilCode: claim.secondaryPerilCode,
      claimStatusCode: claim.claimStatusCode,
      claimStatusDescription: getDescriptionForCode(claim.claimStatusCode, getCodeOptions("CLAIM_STATUS_CODE")),
 
      totalClaimAmount: claim.totalClaimAmount,
      
      recommendedByUserid: claim.recommendedByUserid,
      recommendedByName: claim.recommendedByName,
      recommendedByDate: claim.recommendedByDate,
      
      varieties: claim.varieties
  };  

  return ret;
}

export function convertToCalculation(calculationRes: CalculationRsrc, etag?: string): vmCalculation {
  let calculation = <CalculationRsrc>calculationRes;
  let ret: vmCalculation = {
      etag: etag,

      claimCalculationGuid: calculation.claimCalculationGuid,
      claimCalculationGrainQuantityGuid: calculation.claimCalculationGrainQuantityGuid,
      
      // calculation
      calculationVersion: calculation.calculationVersion,
      calculationVersionDisplay: calculation.calculationVersion ? 'V' + calculation.calculationVersion : null,
      totalClaimAmount: calculation.totalClaimAmount,
      calculationStatusCode: calculation.calculationStatusCode,
      calculationStatusDescription: getDescriptionForCode(calculation.calculationStatusCode, getCodeOptions("CALCULATION_STATUS_CODE")),
      calculationComment: calculation.calculationComment,
      revisionCount: calculation.revisionCount,
      createUser: calculation.createClaimCalcUserName, //calculation.createUser,
      createDate: calculation.createDate,
      updateUser: calculation.updateClaimCalcUserName, //calculation.updateUser,
      updateDate: calculation.updateDate,
      calculateIivInd: calculation.calculateIivInd,
      
      // claim
      claimNumber: calculation.claimNumber,
      commodityCoverageCode: calculation.commodityCoverageCode,
      coverageName: calculation.coverageName,
      cropCommodityId: calculation.cropCommodityId,
      commodityName: calculation.commodityName,
      isPedigreeInd: calculation.isPedigreeInd,
      primaryPerilCode: calculation.primaryPerilCode,
      secondaryPerilCode: calculation.secondaryPerilCode,
      claimStatusCode: calculation.claimStatusCode,
      claimType: calculation.claimType ? calculation.claimType : null,
      submittedByUserid: calculation.submittedByUserid,
      submittedByName: calculation.submittedByName,
      submittedByDate: calculation.submittedByDate,	
      recommendedByUserid: calculation.recommendedByUserid,
      recommendedByName: calculation.recommendedByName,
      recommendedByDate: calculation.recommendedByDate,
      approvedByUserid: calculation.approvedByUserid,
      approvedByName: calculation.approvedByName,
      approvedByDate: calculation.approvedByDate,
              
      // grower
      growerNumber: calculation.growerNumber,
      growerName: calculation.growerName,
      growerAddressLine1: calculation.growerAddressLine1,
      growerAddressLine2: calculation.growerAddressLine2,
      growerPostalCode: calculation.growerPostalCode,
      growerCity: calculation.growerCity,
      growerProvince: calculation.growerProvince ? calculation.growerProvince : null,
      
      // has check req payments
      hasChequeReqInd: calculation.hasChequeReqInd,
      currentHasChequeReqInd: calculation.currentHasChequeReqInd,

      // policy
      insurancePlanId: calculation.insurancePlanId,
      insurancePlanName: calculation.insurancePlanName,
      cropYear: calculation.cropYear,
      policyNumber: calculation.policyNumber,
      contractId: calculation.contractId,
 
      insuredByMeasurementType: calculation.insuredByMeasurementType ? calculation.insuredByMeasurementType : null,

      // linked product / claim / calculation
      linkedProductId: calculation.linkedProductId,
      linkedClaimNumber: calculation.linkedClaimNumber,
      linkedClaimCalculationGuid: calculation.linkedClaimCalculationGuid,

      isOutOfSync: calculation.isOutOfSync,
      isOutOfSyncGrowerNumber: calculation.isOutOfSyncGrowerNumber,
      isOutOfSyncGrowerName: calculation.isOutOfSyncGrowerName,
      isOutOfSyncGrowerAddrLine1: calculation.isOutOfSyncGrowerAddrLine1,
      isOutOfSyncGrowerAddrLine2: calculation.isOutOfSyncGrowerAddrLine2,
      isOutOfSyncGrowerPostalCode: calculation.isOutOfSyncGrowerPostalCode,
      isOutOfSyncGrowerCity: calculation.isOutOfSyncGrowerCity,
      isOutOfSyncGrowerProvince: calculation.isOutOfSyncGrowerProvince,
      isOutOfSyncVarietyAdded: calculation.isOutOfSyncVarietyAdded,

      claimCalculationBerries: calculation.claimCalculationBerries,
      claimCalculationPlantUnits: calculation.claimCalculationPlantUnits,
      claimCalculationPlantAcres: calculation.claimCalculationPlantAcres,
      claimCalculationGrapes: calculation.claimCalculationGrapes,
      claimCalculationGrainUnseeded: calculation.claimCalculationGrainUnseeded,
      claimCalculationGrainSpotLoss: calculation.claimCalculationGrainSpotLoss,
      claimCalculationGrainQuantity: calculation.claimCalculationGrainQuantity,
      claimCalculationGrainQuantityDetail: calculation.claimCalculationGrainQuantityDetail,
      varieties: calculation.varieties,
      currentClaimStatusCode: calculation.currentClaimStatusCode

  };  
  return ret;
}

