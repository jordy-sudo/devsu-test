import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClienteFacade } from '../../fecades/cliente.facade';

@Component({
    standalone: true,
    selector: 'app-cliente-list',
    imports: [CommonModule, FormsModule],
    templateUrl: './cliente-list.component.html',
    styleUrls: ['./cliente-list.component.css'],
})
export class ClienteListComponent implements OnInit {

    search = '';
    vm$;

    constructor(public facade: ClienteFacade, private router: Router) { 
        this.vm$ = this.facade.vm$;
    }


    ngOnInit(): void {
        this.facade.load();
    }

    nuevo() {
        this.router.navigate(['/clientes/nuevo']);
    }

    editar(id: number) {
        this.router.navigate(['/clientes/editar', id]);
    }

    eliminar(id: number) {
        if (confirm('¿Seguro que deseas eliminar este cliente?')) {
            this.facade.remove(id);
        }
    }
}
