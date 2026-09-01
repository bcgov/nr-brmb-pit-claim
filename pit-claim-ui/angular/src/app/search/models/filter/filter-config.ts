import {FilterOption} from "./filter-option";

export abstract class FilterConfig {
  label?: string;
  param?: string;
  options?: FilterOption[];
  type?: string = 'multi';
}
