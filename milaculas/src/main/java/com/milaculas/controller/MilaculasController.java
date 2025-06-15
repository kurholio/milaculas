package com.milaculas.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milaculas.data.ZZCalculator;
import com.milaculas.data.ZZPoint;
import com.milaculas.data.ZZTradePrediction;
import com.milaculas.markets.KrakenManager;
import com.milaculas.predictor.OpenAIPredictor;


@RequestMapping("/api")
@RestController
public class MilaculasController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from backend!";
    }
    
    @GetMapping("/catalog")
    public String catalog() {
        return "Hello from catalogsssssssssssssssssssssssssssssssssssssss!";
    }
    
    ///api/zigzag/XBTUSD?interval=60&leftBars=5&leftBars=5&percentChange=3
    @GetMapping("/zigzag/{pair}")
    public ResponseEntity<List<ZZPoint>> zigzag(
            @PathVariable String pair,
            @RequestParam(defaultValue = "60") Integer interval,
            @RequestParam(defaultValue = "5") Integer leftBars,
            @RequestParam(defaultValue = "5") Integer rightBars,
            @RequestParam(defaultValue = "3") Integer percentChange) {

        try {
            List<ZZPoint> bars = KrakenManager.fetchOHLC(pair, interval);
            List<ZZPoint> zigzag = ZZCalculator.calculateZigZag(bars, leftBars, rightBars, percentChange);
            zigzag = ZZCalculator.generateFeatureEnrichedZZPoints(bars, zigzag,true);
            System.out.println("ZigZag Pivot Points:");
            zigzag.forEach(System.out::println);

            return ResponseEntity.ok(zigzag); // ✅ Automatically serialized as JSON
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.emptyList()); // or return a structured error if needed
        }
    }
    
    @GetMapping("/zigzag/all/{pair}")
    public ResponseEntity<List<ZZPoint>> zigzagAll(
            @PathVariable String pair,
            @RequestParam(defaultValue = "60") Integer interval,
            @RequestParam(defaultValue = "5") Integer leftBars,
            @RequestParam(defaultValue = "5") Integer rightBars,
            @RequestParam(defaultValue = "3") Integer percentChange) {

        try {
            List<ZZPoint> bars = KrakenManager.fetchOHLC(pair, interval);
            List<ZZPoint> zigzag = ZZCalculator.calculateZigZag(bars, leftBars, rightBars, percentChange);
            zigzag = ZZCalculator.generateFeatureEnrichedZZPoints(bars, zigzag, false);
            System.out.println("ZigZag Pivot Points:");
            zigzag.forEach(System.out::println);

            return ResponseEntity.ok(zigzag); // ✅ Automatically serialized as JSON
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.emptyList()); // or return a structured error if needed
        }
    }
    
    @GetMapping("/zigzag/predict/{pair}")
    public ResponseEntity<ZZTradePrediction> zigzagPredict(
            @PathVariable String pair,
            @RequestParam(defaultValue = "60") Integer interval,
            @RequestParam(defaultValue = "5") Integer leftBars,
            @RequestParam(defaultValue = "5") Integer rightBars,
            @RequestParam(defaultValue = "3") Integer percentChange) {

        try {
            List<ZZPoint> bars = KrakenManager.fetchOHLC(pair, interval);
            List<ZZPoint> zigzag = ZZCalculator.calculateZigZag(bars, leftBars, rightBars, percentChange);
            
            zigzag = ZZCalculator.generateFeatureEnrichedZZPoints(bars, zigzag,false);
            
            ZZTradePrediction prediction =  OpenAIPredictor.getPredictionFromGPT(zigzag);
            
            System.out.println("ZigZag Pivot Points:");
            zigzag.forEach(System.out::println);

            return ResponseEntity.ok(prediction); // ✅ Automatically serialized as JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ZZTradePrediction()); // or return a structured error if needed
        }
    }
    
    @GetMapping("/zigzag")
    public String zigzag() {
    	
    	try {
            String pair = "XBTUSD";  // Kraken trading pair
            int interval = 60;       // 60 = hourly candles
            int leftBars = 5;
            int rightBars = 5;
            double minPercentChange = 3;

            List<ZZPoint> bars = KrakenManager.fetchOHLC(pair, interval);
            List<ZZPoint> zigzag = ZZCalculator.calculateZigZag(bars, leftBars, rightBars, minPercentChange);

            System.out.println("ZigZag Pivot Points:");
            zigzag.forEach(System.out::println);
            return zigzag.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"Error\" :\"Not Found\"}";
    }
}