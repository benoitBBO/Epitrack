import { Component } from '@angular/core';
import { UserModel } from './shared/models/user.model';
import { UserService } from './shared/services/user.service';
import { MessageService } from './shared/services/message.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from './shared/components/confirmation-dialog/confirmation-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Epitrack';
  loggedUser!: UserModel;
  confirmationMessage: string = "";

  constructor(private router:Router,
              private userService:UserService,
              private messageService: MessageService,
              private dialog: MatDialog
              ){}

  ngOnInit() {
    //charger loggedUSer
    this.userService._loggedUser$.subscribe((user:any) => this.loggedUser=user );
  }

  logout() {
    this.confirmationMessage = 'Confirmez-vous vouloir vous déconnecter ?';
    
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: { message: this.confirmationMessage },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userService.clearLoggedUserAndSessionStorage();
        this.messageService.show('Vous êtes bien déconnecté', 'success');
        this.router.navigate(['/']);
      }
    });
  }
  
}
