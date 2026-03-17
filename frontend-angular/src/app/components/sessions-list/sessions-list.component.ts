import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../services/session.service';
import { MovieService } from '../../services/movie.service';
import { HallService } from '../../services/hall.service';

@Component({
  selector: 'app-sessions-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sessions-list.component.html',
  styleUrls: ['./sessions-list.component.scss']
})
export class SessionsListComponent implements OnInit {
  sessions: any[] = [];
  movies: any[] = [];
  halls: any[] = [];
  page = 0;
  size = 3;
  totalPages = 0;
  totalElements = 0;
  pageSizes = [3, 5, 10, 20];

  showModal = false;
  editingSession: any = null;
  formData: any = {
    movieId: '',
    hallId: '',
    date: '',
    time: '',
    price: ''
  };

  filters = {
    date: '',
    movieId: '',
    hallId: ''
  };

  constructor(
    private readonly sessionService: SessionService,
    private readonly movieService: MovieService,
    private readonly hallService: HallService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMovies();
    this.loadHalls();
    this.loadSessions();
  }

  loadMovies(): void {
    this.movieService.getAllMovies().subscribe({
      next: (data) => {
        this.movies = data;
      },
      error: (err) => console.error('Movies error:', err)
    });
  }

  loadHalls(): void {
    this.hallService.getAllHalls().subscribe({
      next: (data) => {
        this.halls = data;
      },
      error: (err) => console.error('Halls error:', err)
    });
  }

  loadSessions(): void {
    const params: any = {
      page: this.page,
      size: this.size
    };

    if (this.filters.date) params.date = this.filters.date;
    if (this.filters.movieId) params.movieId = this.filters.movieId;
    if (this.filters.hallId) params.hallId = this.filters.hallId;

    this.sessionService.getSessions(params).subscribe({
      next: (res) => {
        const newSessions = res.content || [];
        this.sessions = [];
        setTimeout(() => {
          this.sessions = [...newSessions];
          this.totalPages = res.totalPages || 0;
          this.totalElements = res.totalElements || 0;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        console.error('Sessions error:', err);
      }
    });
  }

  applyFilters(): void {
    this.page = 0;
    this.loadSessions();
  }

  resetFilters(): void {
    this.filters = { date: '', movieId: '', hallId: '' };
    this.page = 0;
    this.loadSessions();
  }

  changePage(delta: number): void {
    const newPage = this.page + delta;
    if (newPage >= 0 && newPage < this.totalPages) {
      this.page = newPage;
      this.loadSessions();
    }
  }

  onPageSizeChange(event: any): void {
    const newSize = Number(event.target.value);
    this.size = newSize;
    this.page = 0;
    this.loadSessions();
  }

  openCreateModal(): void {
    this.editingSession = null;
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

    this.formData = {
      movieId: '',
      hallId: '',
      date: `${year}-${month}-${day}`,
      time: '',
      price: ''
    };
    this.showModal = true;
    this.cdr.detectChanges();
  }

  openEditModal(session: any): void {
    this.editingSession = session;
    const [year, month, day] = session.sessionDate;
    const [hours, minutes] = session.startTime;

    this.formData = {
      movieId: session.movie.movieId,
      hallId: session.hall.hallId,
      date: `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`,
      time: `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`,
      price: session.price
    };
    this.showModal = true;
    this.cdr.detectChanges();
  }

  closeModal(): void {
    this.showModal = false;
    this.editingSession = null;
    this.cdr.detectChanges();
  }

  saveSession(): void {
    if (!this.formData.movieId || !this.formData.hallId || !this.formData.date || !this.formData.time || !this.formData.price) {
      alert('Заполните все поля');
      return;
    }

    const sessionData = {
      movie: { movieId: Number(this.formData.movieId) },
      hall: { hallId: Number(this.formData.hallId) },
      sessionDate: this.formData.date,
      startTime: this.formData.time + ':00',
      price: Number(this.formData.price)
    };

    if (this.editingSession) {
      this.sessionService.updateSession(this.editingSession.sessionId, sessionData).subscribe({
        next: () => {
          this.showModal = false;
          this.editingSession = null;
          this.cdr.detectChanges();
          setTimeout(() => {
            this.page = 0;
            this.loadSessions();
          });
        },
        error: (err) => console.error('Update error:', err)
      });
    } else {
      this.sessionService.createSession(sessionData).subscribe({
        next: () => {
          this.showModal = false;
          this.editingSession = null;
          this.cdr.detectChanges();
          setTimeout(() => {
            this.page = 0;
            this.loadSessions();
          });
        },
        error: (err) => console.error('Create error:', err)
      });
    }
  }

  deleteSession(session: any): void {
    if (confirm(`Удалить сеанс фильма "${session.movie.title}"?`)) {
      this.sessionService.deleteSession(session.sessionId).subscribe({
        next: () => this.loadSessions(),
        error: (err) => console.error('Delete error:', err)
      });
    }
  }

  getMovieTitle(session: any): string {
    return session.movie?.title || '';
  }

  getActors(session: any): string {
    if (!session.movie?.actors) return '';
    return session.movie.actors.map((a: any) => a.fullName).join(', ');
  }

  getHallDisplay(session: any): string {
    if (!session.hall) return '';
    return `Зал ${session.hall.hallNumber} (${session.hall.hallTypeName})`;
  }

  formatDateTime(session: any): string {
    if (!session.sessionDate || !session.startTime) return '';

    const [year, month, day] = session.sessionDate;
    const [hours, minutes] = session.startTime;

    return `${String(day).padStart(2, '0')}.${String(month).padStart(2, '0')}.${year} ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('ru-RU', {
      style: 'currency',
      currency: 'RUB',
      minimumFractionDigits: 0
    }).format(price);
  }
}
