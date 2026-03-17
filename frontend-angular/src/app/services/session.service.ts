import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private readonly api: ApiService) {}

  getSessions(params: any): Observable<any> {
    return this.api.get('sessions', params);
  }

  getSessionById(id: number): Observable<any> {
    return this.api.get(`sessions/${id}`);
  }

  createSession(sessionData: any): Observable<any> {
    return this.api.post('sessions', sessionData);
  }

  updateSession(id: number, sessionData: any): Observable<any> {
    return this.api.put(`sessions/${id}`, sessionData);
  }

  deleteSession(id: number): Observable<any> {
    return this.api.delete(`sessions/${id}`);
  }
}
