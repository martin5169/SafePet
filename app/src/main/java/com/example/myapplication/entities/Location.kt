package com.example.myapplication.entities

class Location {

    var latitude: Double = 0.1
    var longitude: Double = 0.1

    constructor() {
        this.longitude = 0.1
        this.latitude = 0.1
    }

    constructor(latitude: Double, longitude:Double) {
        this.longitude = longitude
        this.latitude = latitude
    }


}