package com.example.myapplication.entities

class Location {

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor() {
        this.longitude = 0.0
        this.latitude = 0.0
    }

    constructor(latitude: Double, longitude:Double) {
        this.longitude = longitude
        this.latitude = latitude
    }


}