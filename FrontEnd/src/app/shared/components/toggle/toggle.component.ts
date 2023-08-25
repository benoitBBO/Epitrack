import {Component, Input } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { FormsModule } from '@angular/forms';
import { UserMovieService } from '../../services/user-movie.service';
import { UserSerieService } from '../../services/user-serie.service';
import { MessageService } from '../../services/message.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserserieModel } from '../../models/userserie.model';
import { UsermovieModel } from '../../models/usermovie.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ConstantsService } from '../../services/constants.service';

@Component({
  selector: 'app-toggle',
  templateUrl: './toggle.component.html',
  styleUrls: ['./toggle.component.css'],
  standalone: true,
  imports: [MatSlideToggleModule, FormsModule]
})
export class ToggleComponent {
  @Input() status!:string;
  @Input() userVideoId!:number;
  @Input() videoType!:String;
  @Input() userSeasonId!:number; //seulement dans cas de Video type EPISODE
  @Input() userSerieId!:number;  //seulement dans cas de Video type EPISODE et SEASON
  
  checked = false;    //false quand status "A voir" - true quand status 'Déjà vu"
  confirmationMessage: string = "";
  
  constructor(private userMovieService: UserMovieService,
              private userSerieService: UserSerieService,
              private messageService: MessageService,
              private dialog: MatDialog,
              private constants: ConstantsService   
            ){}

  ngOnInit(){
    if (this.status == this.constants.STATUS_WATCHED) {
      this.checked = true;
    }
  }
  
  changed(event: any){
    if (this.checked) {
      this.status = this.constants.STATUS_WATCHED;
    } else {
      this.status = this.constants.STATUS_UNWATCHED;
    }

    switch (this.videoType) {
      case 'MOVIE': 
        this.changedForMovie();
        break;
      case 'SERIE':
        this.changedForSerie();
        break;
      case 'SEASON':
        this.changedForSeason();
        break;
      case 'EPISODE':
        this.changedForEpisode();
        break;
      default:
        this.messageService.show("Erreur type de video inconnu", "error");
    }
  }

  changedForMovie(){
    this.userMovieService.changeStatusUserMovie(this.userVideoId, this.status)
    .subscribe({
      next: (usermovie: UsermovieModel) => {
        this.userMovieService._usermovie$.next(usermovie);
        this.messageService.show("Statut du film mis à jour", "success");
      },
      error: (err:unknown) => {
        if (err instanceof HttpErrorResponse){
          this.checked = !this.checked;
          this.messageService.show("Erreur de mise à jour du statut", "error");
        }
      }
    });
  }

  changedForSerie(){
    // Pour Serie, avant de mettre à jour le statut, on demande confirmation à l'utilisateur
    // car la màj de la serie va entrainer la màj des saisons/épisodes
      
    if (this.checked) {
      this.confirmationMessage = 'Confirmez-vous avoir vu la serie ET toutes les saisons/épisodes ?';
    } else {
      this.confirmationMessage = 'Confirmez-vous vouloir remettre à "A voir" la serie ET toutes les saisons/épisodes ?';
    };
    
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: { message: this.confirmationMessage },
    });

    //    si confirme non -> pas d'update en base + inverser this.checked pour le remettre à son état initial
    //    si confirme oui -> update en base serie/saisons/épisodes avec nouveau status
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userSerieService.changeStatusUserSerie(this.userVideoId, this.status)
          .subscribe({
            next: (userserie: UserserieModel) => {
              this.userSerieService._userserie$.next(userserie);
              this.messageService.show('Statut de la série mis à jour', 'success');
            },
            error: (err: unknown) => {
              if (err instanceof HttpErrorResponse) {
                this.checked = !this.checked;
                this.messageService.show('Erreur de mise à jour du statut', 'error');
              }
            },
          });
      } else {
        this.checked = !this.checked;
      }
    });
  }
  
  changedForSeason(){
    // Pour Season, avant de mettre à jour le statut, on demande confirmation à l'utilisateur
    // car la màj de la season va entrainer la màj des épisodes
      
    if (this.checked) {
      this.confirmationMessage = 'Confirmez-vous avoir vu la saison ET TOUS ses épisodes ?';
    } else {
      this.confirmationMessage = 'Confirmez-vous vouloir remettre à "A voir" la saison ET TOUS ses épisodes ?';
    };

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: { message: this.confirmationMessage },
    });

    //    si confirme non -> pas d'update en base + inverser this.checked pour le remettre à son état initial
    //    si confirme oui -> update en base serie/saisons/épisodes avec nouveau status
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userSerieService.changeStatusUserSeason(this.userSerieId, this.userVideoId, this.status)
          .subscribe({
            next: (userserie: UserserieModel) => {
              this.userSerieService._userserie$.next(userserie);
              this.messageService.show('Statut de la saison mis à jour', 'success');
            },
            error: (err: unknown) => {
              if (err instanceof HttpErrorResponse) {
                this.checked = !this.checked;
                this.messageService.show('Erreur de mise à jour du statut', 'error');
              }
            },
          });
      } else {
        this.checked = !this.checked;
      }
    });
  }

  changedForEpisode(){
    this.userSerieService.changeStatusUserEpisode(this.userSerieId, this.userSeasonId, this.userVideoId, this.status)
    .subscribe({
      next: (userserie:UserserieModel) => {
        this.userSerieService._userserie$.next(userserie);
        this.messageService.show("statut de l'épisode mis à jour", "success");
      },
      error: (err:unknown) => {
        if (err instanceof HttpErrorResponse){
          this.checked = !this.checked;
          this.messageService.show("Erreur de mise à jour du statut", "error");
        }
      }
    });
  }

}
