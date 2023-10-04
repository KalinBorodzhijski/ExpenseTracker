import { TestBed } from '@angular/core/testing';

import { NetworthService } from './networth.service';

describe('NetworthService', () => {
  let service: NetworthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetworthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
