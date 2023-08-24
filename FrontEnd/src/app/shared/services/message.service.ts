import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private _snackbar:MatSnackBar
              ,private _dialog:MatDialog
              ) { }

  show(msg:string, type?:'error' | 'success' | 'info'){
     
    if(type=='error'){
      this._snackbar.open(msg, 'Fermer', {
        panelClass: 'message-error',
        horizontalPosition: 'center',
        verticalPosition: 'top'
      })
    } 
    else {
      this._snackbar.open(msg, 'Fermer', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'top'
        });
    }
  }

  openConfirmDialog(msg:string):void{
    this._dialog.open(ConfirmationDialogComponent);
  }

}
