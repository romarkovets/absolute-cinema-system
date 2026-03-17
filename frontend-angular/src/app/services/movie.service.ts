import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  constructor(private readonly api: ApiService) {}

  getAllMovies(): Observable<any[]> {
    return this.api.get('movies');
  }

  searchMovies(title: string): Observable<any[]> {
    return this.api.get(`movies/search?title=${title}`);
  }
}
