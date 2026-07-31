import {SearchState} from "@wf1/wfcc-core-lib";

export enum ERROR_TYPE {
  VALIDATION,
  WARNING,
  FATAL,
  NOT_FOUND,
  FAILED_PRECONDITION
}

export interface ValidationError {
  path: string;
  message: string;
  messageTemplate: string;
  messageArguments: any[];
}

export interface CodeData {
  code: string;
  description: string;
  displayOrder: number;
  effectiveDate: string;
  expiryDate: string;
}

export interface Option {
  code: string;
  description: string;
  shortDescription?: string;
  effectiveDate?: string;
  expiryDate?: string;
  expired?: boolean;    
}

export interface PagingInfoRequest {
  query?: string;
  pageNumber: number;
  pageRowCount: number;
  sortColumn?: string;
  sortDirection?: string;
}

export interface PagingSearchState extends SearchState {
  pageIndex?: number;
  pageSize?: number;
}

export const EMPTY_SEARCH_STATE: PagingSearchState = {
  query: null,
  sortParam: null,
  sortDirection: null,
  sortModalVisible: false,
  filters: {},
  hiddenFilters: {},
  componentId: null,
  pageIndex: 0,
  pageSize: 10
};

export function getDefaultPagingInfoRequest(pageNumber = 1, pageSize = 5, sortColumn?: string, sortDirection?: string, query?: string): PagingInfoRequest {
  return {
    query: query,
    pageNumber: pageNumber,
    pageRowCount: pageSize,
    sortColumn: sortColumn,
    sortDirection: sortDirection
  };
}

export interface LoadState {
  isLoading: boolean;
}

export interface FormState {
  isUnsaved: boolean;
}

export interface ErrorState {
  uuid: string;
  type: ERROR_TYPE;
  status: number;
  statusText?: string;
  message?: string;
  name: string;
  validationErrors?: ValidationError[];
  responseEtag: string;
}

export interface LoadStates {
  claims: LoadState;
  calculations: LoadState;
  calculationDetailMetadata: LoadState;
}

export interface FormStates {
  calculationDetailMetadata: FormState;
}

export interface ErrorStates {
  claims: ErrorState[];
  calculations: ErrorState[];
  calculationDetailMetadata: ErrorState[];
}

export interface ApplicationState {
  loadStates: LoadStates;
  errorStates: ErrorStates;
  formStates: FormStates;
}

export function getDefaultLoadStates(): LoadStates {
  return {
    claims: {isLoading: false},
    calculations: {isLoading: false},
    calculationDetailMetadata: {isLoading: false}
  };
}

export function getDefaultErrorStates(): ErrorStates {
  return {
    claims: [],
    calculations: [],
    calculationDetailMetadata: []
  };
}

export function getDefaultFormState(): FormState {
  return {
    isUnsaved: false,
  };
}

export function getDefaultFormStates(): FormStates {
  return {
    calculationDetailMetadata: getDefaultFormState()
  };
}

export function getDefaultApplicationState(): ApplicationState {
  return {
    loadStates: getDefaultLoadStates(),
    errorStates: getDefaultErrorStates(),
    formStates: getDefaultFormStates(),
  };
}
