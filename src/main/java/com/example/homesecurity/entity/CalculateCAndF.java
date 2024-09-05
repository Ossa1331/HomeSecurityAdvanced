package com.example.homesecurity.entity;

public sealed interface CalculateCAndF permits HeatSensor {

    Double celsiusToFahrenheit(Double tempInCelsius);

    Double fahrenheitToCelsius(Double tempInFahrenheit);
}
