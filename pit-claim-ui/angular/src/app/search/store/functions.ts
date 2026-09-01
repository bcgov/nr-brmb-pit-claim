import {SearchState} from "./state";


export const getSearchQuery = (state: SearchState) => state.query;

export const getSortParam = (state: SearchState) => state.sortParam;
export const getSortDirection = (state: SearchState) => state.sortDirection;
export const getEncodedSort = (state: SearchState) => encodeURIComponent(`${state.sortParam} ${state.sortDirection}`);
export const isSortModalVisible = (state: SearchState) => state.sortModalVisible;

export const getActiveFilters = (state: SearchState) => state.filters;
