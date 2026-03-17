import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HallService {
  constructor(private readonly api: ApiService) {}

  getAllHalls(): Observable<any[]> {
    return this.api.get('halls');
  }
}
