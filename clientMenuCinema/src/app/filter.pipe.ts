import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'filter',
  pure: true
})
export class FilterPipe implements PipeTransform {

  transform(value: any, filterString: string, propName: string): any {
    const resultArray = [];
    if (value.length === 0 || filterString === '') {
      return value;
    } else if (filterString.length >= 0) {
      let tape = '';
      let saisie = '';
      for (const item of value) {
        tape = item[propName] as string;
        saisie = filterString;
        const newText = saisie[0].toUpperCase();
        saisie = newText + saisie.slice(1, saisie.length);
        if (tape.substr(0, saisie.length) === saisie) {
          resultArray.push(item);
        }
      }
    }
    return resultArray;
  }

}
