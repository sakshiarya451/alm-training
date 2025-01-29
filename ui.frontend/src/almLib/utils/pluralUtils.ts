import Polyglot from 'node-polyglot';
import { GetTranslation } from './translationService';

export const initializePolyglot = (locale: string) => {
  const polyglot = new Polyglot({locale: locale});
  const pluralRule = GetTranslation("alm.text.pluralRules",false);
  polyglot.extend(pluralRule);
  return polyglot;
};

export const normalizeLocale = (locale: string): string => {
  const localeWithoutLanguageTag = locale.split("-")[0].toLowerCase();
  return localeWithoutLanguageTag;
};

export const pluralInstance = (locale:string = "en-US")=>{
    return initializePolyglot(normalizeLocale(locale))
}