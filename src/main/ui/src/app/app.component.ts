import {Component, OnInit} from '@angular/core';
import {AirService} from "../services/air.service";
import {AirResponseStatus, AirResponseSwitch} from "../model/air-response.model";
import {LightService} from "../services/light.service";
import {timeSinceInMicros} from "@angular/compiler-cli/src/ngtsc/perf/src/clock";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Automatech';
  status: any;

  valorAr: number;
  minimoValorAr: number;
  maximoValorAr: number;

  range1: number;
  range2: number;
  range3: number;
  range4: number;
  lampada1: number;
  lampada2: number;
  lampada3: number;
  lampada4: number;
  ligaLampada1: boolean;
  ligaLampada2: boolean;
  ligaLampada3: boolean;
  ligaLampada4: boolean;
  ligaTodasLampadas: boolean;
  alertas: any[];
  arCondicionadoLigado: boolean;
  statusCarregado: boolean;

  constructor(protected airService: AirService,
              protected lightService: LightService) {
    this.valorAr = 0;
    this.minimoValorAr = 17;
    this.maximoValorAr = 25;
    this.range1 = 100;
    this.range2 = 100;
    this.range3 = 100;
    this.range4 = 100;
    this.lampada1 = 100;
    this.lampada2 = 100;
    this.lampada3 = 100;
    this.lampada4 = 100;
    this.ligaLampada1 = true;
    this.ligaLampada2 = true;
    this.ligaLampada3 = true;
    this.ligaLampada4 = true;
    this.ligaTodasLampadas = true;
    this.arCondicionadoLigado = true;

    this.alertas=[];

    this.statusCarregado = false;
  }

  ngOnInit(): void {
    console.log("Inicializando...");

    // Vamos fazer um intervalor pra ir fechadno os alertas.
    setInterval(() => {
      this.alertas.forEach(a => a.contador++);
      this.alertas = this.alertas.filter(a => a.contador <= 10);
    },1000);

    this.airService.getStatus()
      .subscribe((ret: AirResponseStatus) => {
        if (ret.success) {
          ret.result.forEach(r => {
            if (r.code == "power") {
              this.arCondicionadoLigado = r.value == "1";
              console.log("Status do ar condicionado: O ar está " + (this.arCondicionadoLigado ? "ligado" : "desligado"));
            } else if (r.code == "temp") {
              this.valorAr = Number(r.value);
              console.log("Status do ar condicionado: O ar está na temperatura " + this.valorAr);
            } else {
              console.log("Status do ar condicionado: " + r.code + " = " + r.value);
            }
          });
          this.statusCarregado = true;
        } else {
          this.criarAlertaErro("Erro ao buscar o status do ar-condicionado!");
          console.log("Erro ao buscar o status do ar-condicionado: ", ret);
        }
      }, error => {
        this.criarAlertaErro("Erro crítico ao buscar o status do ar-condicionado!");
        console.log("Erro ao buscar o status do ar-condicionado: ", error);
        this.arCondicionadoLigado = !this.arCondicionadoLigado;
      });
  }

  ligarTodasLampadas() {
    if (this.ligaTodasLampadas) {
      this.lightService.ligarTodas()
        .subscribe((ret: string) => {
          console.log(ret);
          this.ajustarControlesLampadas();
        }, error => {
          // TODO: Bug no retorno indicando erro em caso de sucesso.
          if (error.status != 200) {
            this.criarAlertaErro("Erro crítico ao realizar operação nas lâmpadas!");
            console.log("Erro ao realizar operação nas lâmpadas!: ", error);
            this.ligaTodasLampadas = !this.ligaTodasLampadas;
          } else {
            this.ajustarControlesLampadas();
          }
        });
    } else {
      this.lightService.desligarTodas()
        .subscribe((ret: string) => {
          console.log(ret);
          this.ajustarControlesLampadas();
        }, error => {
          // TODO: Bug no retorno indicando erro em caso de sucesso.
          if (error.status != 200) {
            this.criarAlertaErro("Erro crítico ao realizar operação nas lâmpadas!");
            console.log("Erro ao realizar operação nas lâmpadas!: ", error);
            this.ligaTodasLampadas = !this.ligaTodasLampadas;
          } else {
            this.ajustarControlesLampadas();
          }
        });
    }
  }

  ajustarControlesLampadas() {
    this.ligaLampada1 = this.ligaTodasLampadas;
    this.ligaLampada2 = this.ligaTodasLampadas;
    this.ligaLampada3 = this.ligaTodasLampadas;
    this.ligaLampada4 = this.ligaTodasLampadas;
    this.alteradoLampada(1, false);
    this.alteradoLampada(2, false);
    this.alteradoLampada(3, false);
    this.alteradoLampada(4, false);
    this.criarAlertaSucesso("Todas as lâmpadas foram " + (this.ligaTodasLampadas ? "ligadas" : "desligadas") + " com sucesso!");
  }

  ligaDesligaAr() {
    this.airService.switch(this.arCondicionadoLigado)
      .subscribe((ret: AirResponseSwitch) => {
        if (ret.success) {
          this.criarAlertaSucesso("ar-condicionado " + (this.arCondicionadoLigado ? "ligado" : "desligado") + " com sucesso!");
        } else {
          this.criarAlertaErro("Erro ao realizar operação no ar-condicionado: " + ret.msg);
          this.arCondicionadoLigado = !this.arCondicionadoLigado;
        }
      }, error => {
        this.criarAlertaErro("Erro crítico ao realizar operação no ar-condicionado!");
        console.log("Erro ao realizar a operação no ar-condicionado: ", error);
        this.arCondicionadoLigado = !this.arCondicionadoLigado;
      });
  }

  atualizaValorLampada(numero: number, valor: number) {
    this.lightService.alterarBrilho(numero, valor)
      .subscribe((ret: any) => {
        console.log(ret);
      }, error => {
        // TODO: Bug no retorno indicando erro em caso de sucesso.
        if (error.status != 200) {
          this.criarAlertaErro("Erro crítico ao realizar operação nas lâmpadas!");
          console.log("Erro ao realizar operação nas lâmpadas!: ", error);
        }
      });
  }

  atualizaValorAr(valor: number) {
    this.airService.changeTemp(valor)
      .subscribe((ret: any) => {
        if (ret.success) {
          console.log(ret);
          this.valorAr = valor;
          this.criarAlertaSucesso("Temperatura do ar-condicionado alterada para " + this.valorAr);
        } else {
          this.criarAlertaErro("Erro ao realizar operação no ar-condicionado: " + ret.msg);
        }
      }, error => {
        this.criarAlertaErro("Erro crítico ao realizar operação no ar-condicionado!");
        console.log("Erro ao realizar a operação no ar-condicionado: ", error);
      });
  }

  diminuirAr(): void {
    this.atualizaValorAr(this.valorAr - 1);
  }

  aumentarAr(): void {
    this.atualizaValorAr(this.valorAr + 1);
  }

  alteradoCheckLampada(numero: number): void {
    if (numero === 1) {
      this.range1 = this.ligaLampada1 ? 100 : 0;
      this.lampada1 = this.ligaLampada1 ? 100 : 0;
      this.alterarValorLampada(1, this.ligaLampada1 ? 100 : 0, false)
    } else if (numero === 2) {
      this.range2 = this.ligaLampada2 ? 100 : 0;
      this.lampada2 = this.ligaLampada2 ? 100 : 0;
      this.alterarValorLampada(2, this.ligaLampada2 ? 100 : 0, false)
    } else if (numero === 3) {
      this.range3 = this.ligaLampada3 ? 100 : 0;
      this.lampada3 = this.ligaLampada3 ? 100 : 0;
      this.alterarValorLampada(3, this.ligaLampada3 ? 100 : 0, false)
    } else if (numero === 4) {
      this.range4 = this.ligaLampada4 ? 100 : 0;
      this.lampada4 = this.ligaLampada4 ? 100 : 0;
      this.alterarValorLampada(4, this.ligaLampada4 ? 100 : 0, false)
    }
  }

  alteradoLampada(numero: number, logar: boolean): void {
      if (numero === 1) {
          this.range1 = this.ligaLampada1 ? 100 : 0;
          this.lampada1 = this.ligaLampada1 ? 100 : 0;
          this.alterarValorLampada(1, this.ligaLampada1 ? 84 : 0, logar)
      } else if (numero === 2) {
        this.range2 = this.ligaLampada2 ? 100 : 0;
        this.lampada2 = this.ligaLampada2 ? 100 : 0;
        this.alterarValorLampada(2, this.ligaLampada2 ? 84 : 0, logar)
      } else if (numero === 3) {
        this.range3 = this.ligaLampada3 ? 100 : 0;
        this.lampada3 = this.ligaLampada3 ? 100 : 0;
        this.alterarValorLampada(3, this.ligaLampada3 ? 84 : 0, logar)
      } else if (numero === 4) {
        this.range4 = this.ligaLampada4 ? 100 : 0;
        this.lampada4 = this.ligaLampada4 ? 100 : 0;
        this.alterarValorLampada(4, this.ligaLampada4 ? 84 : 0, logar)
      }
  }

  alteradoRange(numero: number) {
    if (numero === 1) {
      if (this.range1 < 15) {
        if (this.lampada1 != 0) {
          this.ligaLampada1 = false;
          this.alterarValorLampada(1, 0, true);
        }
        this.lampada1 = 0;
      } else if (this.range1 > 85) {
        if (this.lampada1 != 100) {
          this.alterarValorLampada(1, 10, true);
        }
        this.lampada1 = 100;
      } else {
        if (this.lampada1 != this.range1) {
          this.alterarValorLampada(1, this.range1, true);
        }
        this.lampada1 = this.range1;
      }
    }
    if (numero === 2) {
      if (this.range2 < 15) {
        if (this.lampada2 != 0) {
          this.ligaLampada2 = false;
          this.alterarValorLampada(2, 0, true);
        }
        this.lampada2 = 0;
      } else if (this.range2 > 85) {
        if (this.lampada2 != 100) {
          this.alterarValorLampada(2, 10, true);
        }
        this.lampada2 = 100;
      } else {
        if (this.lampada2 != this.range2) {
          this.alterarValorLampada(2, this.range2, true);
        }
        this.lampada2 = this.range2;
      }
    }
    if (numero === 3) {
      if (this.range3 < 15) {
        if (this.lampada3 != 0) {
          this.ligaLampada3 = false;
          this.alterarValorLampada(3, 0, true);
        }
        this.lampada3 = 0;
      } else if (this.range3 > 85) {
        if (this.lampada3 != 100) {
          this.alterarValorLampada(3, 10, true);
        }
        this.lampada3 = 100;
      } else {
        if (this.lampada3 != this.range3) {
          this.alterarValorLampada(3, this.range3, true);
        }
        this.lampada3 = this.range3;
      }
    }
    if (numero === 4) {
      if (this.range4 < 15) {
        if (this.lampada4 != 0) {
          this.ligaLampada4 = false;
          this.alterarValorLampada(4, 0, true);
        }
        this.lampada4 = 0;
      } else if (this.range4 > 85) {
        if (this.lampada4 != 100) {
          this.alterarValorLampada(4, 10, true);
        }
        this.lampada4 = 100;
      } else {
        if (this.lampada4 != this.range4) {
          this.alterarValorLampada(4, this.range4, true);
        }
        this.lampada4 = this.range4;
      }
    }
  }

  alterarValorLampada(numero: number, valor: number, logar: boolean) {
    if (logar) {
      this.criarAlertaSucesso("Lâmpada " + numero + " alterada para " + valor + "% com sucesso!");
    }
    this.atualizaValorLampada(numero,  valor);
  }

  criarAlertaSucesso(mensagem: string) {
    this.alertas.push({
      tipo: "alert-success",
      mensagem: mensagem,
      contador: 0
    });
  }

  criarAlertaErro(mensagem: string) {
    this.alertas.push({
      tipo: "alert-danger",
      mensagem: mensagem,
      contador: 0
    });
  }
}
