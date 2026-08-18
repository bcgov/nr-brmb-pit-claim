import {Action} from "@ngrx/store";
// Models
import {SortDirection} from "../models/sort/sort-direction";

export enum SearchActionTypes {
  UPDATE_SEARCH_QUERY = '[ Search ] Update search query text',
  RESET_SEARCH_QUERY = '[ Search ] Reset search query text',
  UPDATE_SORT = '[ Search ] Update active sort',
  TOGGLE_SORT_MODAL = '[ Search ] Toggle sort modal visibility',
  UPDATE_ACTIVE_FILTERS = '[ Search ] Update active filters',
  UPDATE_HIDDEN_FILTERS = '[ Search ] Update hidden filters',
  CLEAR_FILTER = '[ Search ] Clear filter',
  CLEAR_ALL_FILTERS = '[ Search ] Clear all filters',
  REFRESH_SEARCH = '[ Search ] Refresh search'
}

export class UpdateSearchQueryAction implements Action {
  type = SearchActionTypes.UPDATE_SEARCH_QUERY;
  constructor(public query: string, public componentId?: string) {}
}

export class ResetSearchQueryAction implements Action {
  type = SearchActionTypes.RESET_SEARCH_QUERY;
  constructor(public componentId?: string) {}
}

export class UpdateSortAction implements Action {
  type = SearchActionTypes.UPDATE_SORT;
  constructor(public param: string, public direction: SortDirection, public componentId?: string) {}
}

export class ToggleSortModalAction implements Action {
  type = SearchActionTypes.TOGGLE_SORT_MODAL;
  constructor(public componentId?: string) {}
}

export class UpdateActiveFiltersAction implements Action {
  type = SearchActionTypes.UPDATE_ACTIVE_FILTERS;
  constructor(public filters: { [param: string]: any[] }, public componentId?: string) {}
}

export class UpdateHiddenFiltersAction implements Action {
  type = SearchActionTypes.UPDATE_HIDDEN_FILTERS;
  constructor(public filters: { [param: string]: any[] }, public componentId?: string) {}
}

export class ClearFilterAction implements Action {
  type = SearchActionTypes.CLEAR_FILTER;
  constructor(public filterToRemove: string, public componentId?: string) {}
}

export class ClearAllFiltersAction implements Action {
  type = SearchActionTypes.CLEAR_ALL_FILTERS;
  constructor(public componentId?: string) {}
}

export class RefreshSearchAction implements Action {
    type = SearchActionTypes.REFRESH_SEARCH;
    constructor(public componentId?: string) {}
}

export type SearchActions = UpdateSearchQueryAction |
              ResetSearchQueryAction |
              UpdateSortAction |
              ToggleSortModalAction |
              UpdateActiveFiltersAction |
              UpdateHiddenFiltersAction |
              ClearFilterAction |
              ClearAllFiltersAction |
              RefreshSearchAction;
