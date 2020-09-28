import { TestBed } from '@angular/core/testing';

import { RequeteServeurService } from './requete-serveur.service';

describe('RequeteServeurService', () => {
  let service: RequeteServeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequeteServeurService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
