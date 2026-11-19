import * as Search from "./actions";
import {SearchState} from "./state";

export function searchReducer(state: SearchState, action: Search.SearchActions): SearchState {
  switch (action.type) {
    case Search.SearchActionTypes.UPDATE_SEARCH_QUERY: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }

      const query = (<Search.UpdateSearchQueryAction>action).query;
      return Object.assign({}, state, { query: query });
    }

    case Search.SearchActionTypes.RESET_SEARCH_QUERY: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      return Object.assign({}, state, { query: null });
    }

    case Search.SearchActionTypes.UPDATE_SORT: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      const { param, direction } = (<Search.UpdateSortAction>action);
      return Object.assign({}, state, { sortParam: param, sortDirection: direction });
    }

    case Search.SearchActionTypes.TOGGLE_SORT_MODAL: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      return Object.assign({}, state, { sortModalVisible: !state.sortModalVisible });
    }

    case Search.SearchActionTypes.UPDATE_ACTIVE_FILTERS: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      const activeFilters = (<Search.UpdateActiveFiltersAction>action).filters;
      return Object.assign({}, state, { filters: activeFilters });
    }

    case Search.SearchActionTypes.UPDATE_HIDDEN_FILTERS: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      const hiddenFilters = (<Search.UpdateHiddenFiltersAction>action).filters;
      return Object.assign({}, state, { hiddenFilters: hiddenFilters });
    }

    case Search.SearchActionTypes.CLEAR_FILTER: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      const filterName = (<Search.ClearFilterAction>action).filterToRemove;
      const { [filterName]: clearedFilter, ...remainingFilters } = state.filters;
      return Object.assign({}, state, { filters: remainingFilters });
    }

    case Search.SearchActionTypes.CLEAR_ALL_FILTERS: {
      if (!isMyComponent(state, action.componentId)) {
        return state;
      }
      return Object.assign({}, state, { filters: {} });
    }

    default: {
      return state;
    }

  }
}

const isMyComponent = (state: any, componentId?: string): boolean => {
  if (state && state.componentId && componentId) {
    if (state.componentId === componentId) {
      return true;
    }
  }
  return false;
};
