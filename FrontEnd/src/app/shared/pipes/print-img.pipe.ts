import { Pipe, PipeTransform } from '@angular/core';
import { ConstantsService } from '../services/constants.service';

@Pipe({
  name: 'printImg'
})
export class PrintImgPipe implements PipeTransform {

  constructor(private constants: ConstantsService) {}

  transform(value: string): string {
    return this.constants.TMDB_IMG_URL+value;
  }

}
