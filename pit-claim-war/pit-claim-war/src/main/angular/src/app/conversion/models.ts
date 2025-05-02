import {ClaimCalculationGrainSpotLoss, ClaimCalculationGrainUnseeded, ClaimCalculationGrapes, ClaimCalculationPlantAcres, ClaimCalculationPlantUnits, ClaimCalculationVariety} from "@cirras/cirras-claims-api";
import { ClaimCalculationBerries } from '@cirras/cirras-claims-api';

export interface PagedCollection {
    pageNumber?: number;
    pageRowCount?: number;
    totalRowCount?: number;
    totalPageCount?: number;
    collection?: Array<any>;
}

export interface vmClaimList extends PagedCollection {
  collection: Array<vmClaim>;
}

export interface vmCalculationList extends PagedCollection {
  collection: Array<vmCalculation>;
}

export interface vmClaim {
  etag?:string;
  claimCalculationGuid: string;
  // grower
  growerNumber: number;
  growerName: string;
  growerAddressLine1: string;
  growerAddressLine2?: string;
  growerPostalCode: string;
  // policy
  insurancePlanName: string;
  cropYear: number;
  policyNumber: string;
  // coverage
  coverageName: string;
  commodityName: string;
  // insurableValueSelected: number;
  // insurableValueComplete: number;
  // coverageAmount: number;
  // coverageAmountAssessed: number;
  // coverageAssessedReason: string;
  // coverageAmountAdjusted: number;
  // claim
  claimNumber: number;
  calculationVersion: number;
  calculationStatusCode: string;
  claimType: string;
  primaryPerilCode: string;
  secondaryPerilCode: string;
  claimStatusCode: string;
  claimStatusDescription: string;
  // totalIncome: number;
  totalClaimAmount: number;
	
  recommendedByUserid?: string;
  recommendedByName?: string;
  recommendedByDate?: string;
	
  varieties?:Array<ClaimCalculationVariety>;
}

export interface vmCalculation {
  etag?:string;
  claimCalculationGuid: string;

  // calculation
  calculationVersion: number;
  calculationVersionDisplay: string;
  totalClaimAmount: number;
  calculationStatusCode: string;
  calculationStatusDescription: string;
  calculationComment: string;
  revisionCount: number;
  createUser: string;
  createDate: string;
  updateUser: string;
  updateDate: string;
  calculateIivInd?: string;

  // claim
  claimNumber: number;
  commodityCoverageCode: string;
  coverageName: string;
  cropCommodityId: number;
  commodityName: string;
  primaryPerilCode: string;
  secondaryPerilCode: string;
  claimStatusCode: string;
  claimType?: string;
  submittedByUserid: string;
  submittedByName: string;	
  submittedByDate: string;	
  recommendedByUserid: string;
  recommendedByName: string;
  recommendedByDate: string;
  approvedByUserid: string;
  approvedByName: string;	
  approvedByDate: string;	

  // grower
  growerNumber: number;
  growerName: string;
  growerAddressLine1: string;
  growerAddressLine2: string;
  growerPostalCode: string;
  growerCity: string;
  growerProvince?: string;

  // check req payments
  hasChequeReqInd?: boolean;
  currentHasChequeReqInd?: boolean;

  // policy
  insurancePlanId: number;
  insurancePlanName: string;
  cropYear: number;
  policyNumber: string;
  contractId?: number;

  insuredByMeasurementType?: string;

  // out of sync flags
  isOutOfSync?: boolean;
  isOutOfSyncGrowerNumber?: boolean;
  isOutOfSyncGrowerName?: boolean;
  isOutOfSyncGrowerAddrLine1?: boolean;
  isOutOfSyncGrowerAddrLine2?: boolean;
  isOutOfSyncGrowerPostalCode?: boolean;
  isOutOfSyncGrowerCity?: boolean;
  isOutOfSyncGrowerProvince?: boolean;
  isOutOfSyncVarietyAdded?: boolean;
  
  claimCalculationBerries?: ClaimCalculationBerries;
  claimCalculationPlantUnits?: ClaimCalculationPlantUnits;
  claimCalculationPlantAcres?: ClaimCalculationPlantAcres;
  claimCalculationGrapes?: ClaimCalculationGrapes;
  claimCalculationGrainUnseeded?: ClaimCalculationGrainUnseeded;
  claimCalculationGrainSpotLoss?: ClaimCalculationGrainSpotLoss;
  varieties?:Array<ClaimCalculationVariety>;

  currentClaimStatusCode?: string; 

}
