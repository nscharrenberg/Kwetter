/// <reference types="@types/googlemaps" />
import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    NgZone,
    OnInit,
    Output,
    ViewChild
} from "@angular/core";

@Component({
    selector: 'AutocompleteComponent',
    template: `
      <input class="form-control"
        type="text"
        [(ngModel)]="autocompleteInput"
        #googleAddress
        >
    `,
})
export class AutocompleteComponent  implements OnInit, AfterViewInit {
    @Output() setAddress: EventEmitter<any> = new EventEmitter();
    @ViewChild('googleAddress') addresstext: any;

    autocompleteInput: string;
    queryWait: boolean;
    public latitude: number;
    public longitude: number;

    constructor(private ngZone: NgZone) {
    }

    ngOnInit() {
        //set google maps defaults
        this.latitude = 39.8282;
        this.longitude = -98.5795;
    }

    ngAfterViewInit() {
        this.getPlaceAutocomplete();
    }

    private getPlaceAutocomplete() {
        const autocomplete = new google.maps.places.Autocomplete(this.addresstext.nativeElement,
            {
                types: ["address"]  // 'establishment' / 'address' / 'geocode'
            });
        google.maps.event.addListener(autocomplete, 'place_changed', () => {
            this.ngZone.run(() => {
                //get the place result
                let place: google.maps.places.PlaceResult = autocomplete.getPlace();

                //verify result
                if (place.geometry === undefined || place.geometry === null) {
                    return;
                }

                //set latitude, longitude and zoom
                this.latitude = place.geometry.location.lat();
                this.longitude = place.geometry.location.lng();
            });
        });
    }
}