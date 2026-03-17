import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MovieService } from '../../services/movie.service';
import { HallService } from '../../services/hall.service';

@Component({
  selector: 'app-filters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.scss']
})
export class FiltersComponent implements OnInit {
  @Output() filtersChanged = new EventEmitter<any>();

  movies: any[] = [];
  halls: any[] = [];

  filters = {
    movieId: null,
    hallId: null,
    date: ''
  };

  constructor(
    private readonly movieService: MovieService,
    private readonly hallService: HallService
  ) {}

  ngOnInit() {
    this.loadMovies();
    this.loadHalls();
  }

  loadMovies() {
    this.movieService.getAllMovies().subscribe({
      next: (data) => this.movies = data,
      error: (err) => console.error('Error loading movies:', err)
    });
  }

  loadHalls() {
    this.hallService.getAllHalls().subscribe({
      next: (data) => this.halls = data,
      error: (err) => console.error('Error loading halls:', err)
    });
  }

  applyFilters() {
    const cleanedFilters: any = {};
    if (this.filters.movieId) cleanedFilters.movieId = this.filters.movieId;
    if (this.filters.hallId) cleanedFilters.hallId = this.filters.hallId;
    if (this.filters.date) cleanedFilters.date = this.filters.date;
    this.filtersChanged.emit(cleanedFilters);
  }

  resetFilters() {
    this.filters = {
      movieId: null,
      hallId: null,
      date: ''
    };
    this.applyFilters();
  }
}
