export interface AirResponseSwitch {
  result: boolean,
  msg: string,
  success: boolean,
  t: number,
  tid: string
}

export interface AirResponseResultStatus {
  code: string,
  value: string
}

export interface AirResponseStatus {
  result: AirResponseResultStatus[],
  success: boolean,
  t: number,
  tid: string
}
