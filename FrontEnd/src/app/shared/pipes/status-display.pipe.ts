import { Pipe, PipeTransform } from '@angular/core';
import { ConstantsService } from '../services/constants.service';

@Pipe({
  name: 'statusDisplay'
})
export class StatusDisplayPipe implements PipeTransform {

  constructor(private constants: ConstantsService) {
}
  transform(value: string): string {
    let statusToDisplay:string ="";
    switch (value){
      case this.constants.STATUS_WATCHED:
        statusToDisplay = "Déjà vu";
        break;
      case this.constants.STATUS_UNWATCHED:
        statusToDisplay = "A voir";
        break;
      case this.constants.STATUS_ONGOING:
        statusToDisplay = "En cours";
        break;
      default:
        statusToDisplay = "inconnu";
    }
    return statusToDisplay;
  }

}
