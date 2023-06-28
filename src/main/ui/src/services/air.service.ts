import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApplicationConfigService} from "../config/application-config.service";
import {AirResponseStatus, AirResponseSwitch} from "../model/air-response.model";

@Injectable({ providedIn: 'root' })
export class AirService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/air');

  constructor(protected httpClient: HttpClient,
              protected applicationConfigService: ApplicationConfigService) {}

  getStatus(): Observable<AirResponseStatus> {
    return this.httpClient.get<AirResponseStatus>(
      `${this.resourceUrl}/status`
    );
  }

  switch(status: boolean): Observable<AirResponseSwitch> {
    return this.httpClient.post<AirResponseSwitch>(
      `${this.resourceUrl}/switch`, status
    );
  }

  changeTemp(valor: number): Observable<any> {
    return this.httpClient.post<any>(
      `${this.resourceUrl}/temp`, valor
    );
  }

}
