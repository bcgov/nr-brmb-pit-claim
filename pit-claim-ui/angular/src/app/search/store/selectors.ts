import {createSelector, MemoizedSelector} from "@ngrx/store";

import {SearchState} from "./state";
import {SortDirection} from "../models/sort/sort-direction";
import * as SearchFunctions from "./functions";


const getSearchState = (state: SearchState) => state;

export const getSearchQuerySelector: MemoizedSelector<SearchState, string> = createSelector(getSearchState, SearchFunctions.getSearchQuery);

export const getSortParamSelector: MemoizedSelector<SearchState, string> = createSelector(getSearchState, SearchFunctions.getSortParam);

export const getSortDirectionSelector: MemoizedSelector<SearchState, SortDirection> =
    createSelector(getSearchState, SearchFunctions.getSortDirection);

export const getEncodedSortSelector: MemoizedSelector<SearchState, string> = createSelector(getSearchState, SearchFunctions.getEncodedSort);

export const isSortModalVisibleSelector: MemoizedSelector<SearchState, boolean> =
    createSelector(getSearchState, SearchFunctions.isSortModalVisible);

export const getActiveFiltersSelector: MemoizedSelector<SearchState, { [param: string]: any[] }> =
    createSelector(getSearchState, SearchFunctions.getActiveFilters);
